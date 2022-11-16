package be.ac.umons.g06.server.controller;

import be.ac.umons.g06.server.model.Bank;
import be.ac.umons.g06.server.model.Customer;
import be.ac.umons.g06.server.model.User;
import be.ac.umons.g06.server.model.Util;
import be.ac.umons.g06.server.payload.request.ChangePasswordRequest;
import be.ac.umons.g06.server.payload.request.LoginRequest;
import be.ac.umons.g06.server.payload.request.SignupRequest;
import be.ac.umons.g06.server.payload.response.JwtResponse;
import be.ac.umons.g06.server.payload.response.MessageResponse;
import be.ac.umons.g06.server.security.jwt.JwtUtils;
import be.ac.umons.g06.server.service.BankService;
import be.ac.umons.g06.server.service.CustomerService;
import be.ac.umons.g06.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

/**
 * Class allowing the control of the identification of the server
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/auth", method = {RequestMethod.POST})
public class AuthController {
    /**
     * The Authentication Manager
     */
    @Autowired
    AuthenticationManager authenticationManager;
    /**
     * Service used to interact with the Customers stored in the DB
     */
    @Autowired
    CustomerService customerService;
    /**
     * UserService object
     */
    @Autowired
    UserService userService;
    /**
     * Service used to interact with the Banks stored in the DB
     */
    @Autowired
    BankService bankService;
    /**
     * Object of PasswordEncoder
     */
    @Autowired
    PasswordEncoder encoder;
    /**
     * Object of JwtUtils
     */
    @Autowired
    JwtUtils jwtUtils;

    /**
     * Authenticate a user
     * @param loginRequest The identification key
     * @return Server Response
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        User user = userService.findByUsername(loginRequest.getUsername()).get();
        return ResponseEntity.ok(new JwtResponse(jwt, user, user.getRole()));
    }

    /**
     * Register a new customer
     * @param signUpRequest The identification key
     * @return Server Response
     */
    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerCustomer(@Valid @RequestBody SignupRequest signUpRequest) {
        if (customerService.existsByName(signUpRequest.getName())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: name is already taken!"));
        }

        if (customerService.existsById(signUpRequest.getId())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: id is already taken!"));
        }

        Customer customer = new Customer(
                signUpRequest.getId(),
                signUpRequest.getName(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getDate());

        customerService.save(customer);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/changePassword")
    public ResponseEntity<MessageResponse> changePassword(
            @Valid @RequestBody ChangePasswordRequest newPasswordRequest,
            Principal principal) {
        if (Util.isPrincipalCustomer(principal)) {
            Customer customer = Util.getPrincipalAsCustomer(principal);
            customer.setPassword(encoder.encode(newPasswordRequest.getPassword()));
            customerService.save(customer);
        } else {
            Bank bank = Util.getPrincipalAsBank(principal);
            bank.setPassword(encoder.encode(newPasswordRequest.getPassword()));
            bankService.save(bank);
        }
        return ResponseEntity.ok(new MessageResponse("Password updated"));
    }
}