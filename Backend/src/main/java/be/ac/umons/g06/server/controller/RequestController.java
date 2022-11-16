package be.ac.umons.g06.server.controller;

import be.ac.umons.g06.server.model.Util;
import be.ac.umons.g06.server.model.account.Account;
import be.ac.umons.g06.server.model.event.Decision;
import be.ac.umons.g06.server.model.event.Request;
import be.ac.umons.g06.server.model.event.Transfer;
import be.ac.umons.g06.server.payload.request.TransferRequest;
import be.ac.umons.g06.server.payload.response.MessageResponse;
import be.ac.umons.g06.server.service.AccountService;
import be.ac.umons.g06.server.service.OperationService;
import be.ac.umons.g06.server.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/request")
@PreAuthorize("hasRole('BANK') or hasRole('CUSTOMER')")
public class RequestController {

    @Autowired
    private RequestService requestService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private OperationService operationService;

    /**
     * Get one request from its id
     * @param id the id of the request
     * @param principal The object that identifies the user that sent the HTTP request
     * @return a ResponseEntity that contains the request if the request is found and a 404 else
     */
    @GetMapping("{id}")
    public ResponseEntity<Request> getRequest(@PathVariable long id, Principal principal) {
        if (requestService.findRequest(id).isPresent()) {
            Request request = requestService.findRequest(id).get();
            if (!Util.checkPrincipalInvolvedInAccount(request.getAccount(), principal))
                return ResponseEntity.status(401).build();
            return ResponseEntity.ok(request);
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * Get all the requests that concern a user. Only the request that were updated after a given dateTime are selected
     * @param lastUpdate The time before which the requests are not selected
     * @param principal The object that identifies the user
     * @return A ResponseEntity that contains the list of the requests found
     */
    @GetMapping("all")
    public ResponseEntity<Iterable<Request>> getUserRequests(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastUpdate,
            Principal principal) {
        if (Util.isPrincipalCustomer(principal))
            return ResponseEntity.ok().body(requestService.getCustomerRequests(Util.getPrincipalAsCustomer(principal), lastUpdate));
        else
            return ResponseEntity.ok().body(requestService.getBankRequests(Util.getPrincipalAsBank(principal), lastUpdate));
    }

    /**
     * Update the decision of a user concerning a request
     * @param id The id of the request
     * @param decision The decision taken by the user
     * @param principal The object that identifies the user
     * @return A MessageResponse that tells if a request was updated
     */
    @PostMapping("{id}/{decision}")
    public ResponseEntity<MessageResponse> updateDecision(@PathVariable long id, @PathVariable Decision decision, Principal principal) {
        if (requestService.findRequest(id).isPresent()) {
            Request request = requestService.findRequest(id).get();
            if (!Util.checkPrincipalInvolvedInAccount(request.getAccount(), principal))
                return ResponseEntity.status(401).build();
            boolean updated;
            if (Util.isPrincipalCustomer(principal))
                updated = request.updateCustomerDecision(Util.getPrincipalAsCustomer(principal), decision);
            else
                updated = request.updateBankDecision(Util.getPrincipalAsBank(principal), decision);
            requestService.saveRequest(request);
            return updated ? ResponseEntity.ok(new MessageResponse("Decision registered")) : ResponseEntity.badRequest().body(new MessageResponse("No decision registered"));
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Request not found"));
    }

    @PostMapping("transfer")
    public ResponseEntity<MessageResponse> demandTransfer(@Valid @RequestBody TransferRequest transferRequest, Principal principal) {
        Optional<Account> optionalOrigin = accountService.getAccount(transferRequest.getOriginIban());
        Optional<Account> optionalDestination = accountService.getAccount(transferRequest.getDestinationIban());

        if (optionalOrigin.isPresent() && optionalDestination.isPresent()) {
            if (!Util.checkPrincipalInvolvedInAccount(optionalOrigin.get(), principal))
                return ResponseEntity.status(401).build();
            Transfer transfer;
            if (Util.isPrincipalCustomer(principal)) {
                transfer = new Transfer(
                        optionalOrigin.get(),
                        optionalDestination.get(),
                        Util.getPrincipalAsCustomer(principal),
                        transferRequest.getAmount(),
                        transferRequest.getStructuredCommunication(),
                        transferRequest.getCommunication());
            }
            else {
                transfer = new Transfer(
                        optionalOrigin.get(),
                        optionalDestination.get(),
                        Util.getPrincipalAsBank(principal),
                        transferRequest.getAmount(),
                        transferRequest.getStructuredCommunication(),
                        transferRequest.getCommunication());

            }
            requestService.saveRequest(transfer);
            return ResponseEntity.ok(new MessageResponse("Transfer request registered"));
        }
        return ResponseEntity.status(401).body(new MessageResponse("One or more of the given ibans don't belong to an existing account"));
    }
}
