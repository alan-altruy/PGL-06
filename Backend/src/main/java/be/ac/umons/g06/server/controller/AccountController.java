package be.ac.umons.g06.server.controller;

import be.ac.umons.g06.server.model.Bank;
import be.ac.umons.g06.server.model.Customer;
import be.ac.umons.g06.server.model.Util;
import be.ac.umons.g06.server.model.account.Account;
import be.ac.umons.g06.server.model.event.EventType;
import be.ac.umons.g06.server.model.event.Operation;
import be.ac.umons.g06.server.model.event.Request;
import be.ac.umons.g06.server.model.ownership.Ownership;
import be.ac.umons.g06.server.payload.response.MessageResponse;
import be.ac.umons.g06.server.payload.response.AccountResponse;
import be.ac.umons.g06.server.service.AccountService;
import be.ac.umons.g06.server.service.OperationService;
import be.ac.umons.g06.server.service.OwnershipService;
import be.ac.umons.g06.server.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api")
@PreAuthorize("hasRole('BANK') or hasRole('CUSTOMER')")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private OperationService operationService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private OwnershipService ownershipService;

    /**
     * Get an account
     * @param iban The iban of the account you want to get
     * @return The account if it exists, a not found http message else
     */
    @GetMapping("account/{iban}")
    public ResponseEntity<Account> getAccount(@PathVariable("iban") final String iban) {
        return accountService.getAccount(iban)
                .map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * GET the list of the accounts that belongs to the user that sent the request
     * @param principal The object that identifies the user that sent the request
     * @return The list of the accounts that belongs to the user that sent the request
     */
    @GetMapping("accounts")
    public ResponseEntity<List<AccountResponse>> getAllAccounts(Principal principal) {
        List<AccountResponse> result = new ArrayList<>();
        if (Util.isPrincipalCustomer(principal)) {
            Customer customer = Util.getPrincipalAsCustomer(principal);
            for (Ownership ownership: ownershipService.getOwnerships()) {
                if (ownership.isCustomerInvolved(customer))
                    result.add(new AccountResponse(ownership.getAccount(), ownership.isCustomerDisabled(customer)));
            }
        }
        else {
            Bank bank = Util.getPrincipalAsBank(principal);
            for (Account account : accountService.findByBank(bank))
                result.add(new AccountResponse(account, false));
        }
        return ResponseEntity.ok(result);
    }

    /**
     * Add an account in the database
     * @param account The account you want to add
     * @param principal Principal object
     * @return Server Response
     */
    @PostMapping("account")
    public ResponseEntity<MessageResponse> createAccount(@Valid @RequestBody Account account, Principal principal) {
        if (!account.checkLinkedAccount(accountService))
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid linkedAccountIban"));

        if (accountService.getAccount(account.getIban()).isPresent())
            return ResponseEntity.badRequest().body(new MessageResponse("IBAN already used"));

        if (!Util.checkPrincipalInvolvedInAccount(account, principal))
            return ResponseEntity.badRequest().body(new MessageResponse("Requester must be involved in the account"));

        account = accountService.saveAccount(account);
        Request request;
        if (Util.isPrincipalCustomer(principal))
            request = new Request(account, EventType.CREATION, Util.getPrincipalAsCustomer(principal), 0);
        else
            request = new Request(account, EventType.CREATION, Util.getPrincipalAsBank(principal), 0);
        requestService.saveRequest(request);
        return ResponseEntity.ok(new MessageResponse("Account creation request registered"));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("account/{iban}/disable")
    public ResponseEntity<MessageResponse> disableAccount(@PathVariable("iban") final String iban, Principal principal) {
        Optional<Account> optionalAccount = accountService.getAccount(iban);
        if (optionalAccount.isEmpty() || !Util.checkPrincipalInvolvedInAccount(optionalAccount.get(), principal))
            return ResponseEntity.badRequest().build();

        Account account = optionalAccount.get();
        account.getOwnership().switchCustomerDisabled(Util.getPrincipalAsCustomer(principal));
        accountService.saveAccount(account);
        return ResponseEntity.ok(new MessageResponse("Account disabled for the wanted customer"));
    }

    @PostMapping("account/{iban}/close")
    public ResponseEntity<MessageResponse> closeAccount(@PathVariable("iban") final String iban, Principal principal) {
        Optional<Account> optionalAccount = accountService.getAccount(iban);
        if (optionalAccount.isEmpty() || !Util.checkPrincipalInvolvedInAccount(optionalAccount.get(), principal))
            return ResponseEntity.badRequest().build();

        Request request;
        if (Util.isPrincipalCustomer(principal))
            request = new Request(optionalAccount.get(), EventType.CLOSURE, Util.getPrincipalAsCustomer(principal), 0);
        else
            request = new Request(optionalAccount.get(), EventType.CLOSURE, Util.getPrincipalAsBank(principal), 0);
        requestService.saveRequest(request);
        return ResponseEntity.ok(new MessageResponse("account closure request registered"));
    }

    @GetMapping("account/{iban}/operations")
    public ResponseEntity<Iterable<Operation>> getAccountOperations(
            @PathVariable("iban") final String iban,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final LocalDateTime dateTime,
            Principal principal) {
        Optional<Account> optionalAccount = accountService.getAccount(iban);
        if (optionalAccount.isEmpty() || !Util.checkPrincipalInvolvedInAccount(optionalAccount.get(), principal))
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(operationService.findByAccount(optionalAccount.get(), dateTime));
    }

    @GetMapping("account/{iban}/requests")
    public ResponseEntity<Iterable<Request>> getAccountRequests(
            @PathVariable("iban") final String iban,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final LocalDateTime dateTime,
            Principal principal) {
        Optional<Account> optionalAccount = accountService.getAccount(iban);
        if (optionalAccount.isEmpty() || !Util.checkPrincipalInvolvedInAccount(optionalAccount.get(), principal))
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(requestService.findByAccount(optionalAccount.get(), dateTime));
    }

    @GetMapping("account/generateIban")
    public ResponseEntity<MessageResponse> generateIban() {
        return ResponseEntity.ok(new MessageResponse(accountService.generateUnusedIban()));
    }
}
