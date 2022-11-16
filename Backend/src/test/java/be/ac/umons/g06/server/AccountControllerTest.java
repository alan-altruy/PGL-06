package be.ac.umons.g06.server;

import be.ac.umons.g06.server.model.Bank;
import be.ac.umons.g06.server.model.Customer;
import be.ac.umons.g06.server.model.account.Account;
import be.ac.umons.g06.server.model.account.AccountBuilder;
import be.ac.umons.g06.server.model.account.AccountType;
import be.ac.umons.g06.server.model.ownership.OwnershipBuilder;
import be.ac.umons.g06.server.model.ownership.OwnershipType;
import be.ac.umons.g06.server.security.services.UserDetailsImpl;
import be.ac.umons.g06.server.service.BankService;
import be.ac.umons.g06.server.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private BankService bankService;
    /**
     * Object of CustomerService
     */
    @Autowired
    private CustomerService customerService;

    @Mock
    private Authentication principal;

    @Test
    @WithMockUser(username = "CUSTOMER=R000001FE", roles = {"CUSTOMER"})
    public void testCreateAccount() throws Exception {
        Bank ing = bankService.findBankByName("ING").get();
        Customer customer = new Customer("R000001FE", "Friedrich Engels", encoder.encode("capital"), LocalDate.of(1820, 11, 28));
        UserDetailsImpl userDetails = UserDetailsImpl.build(customer);
        Account account = new AccountBuilder()
                .type(AccountType.CURRENT_ACCOUNT)
                .iban("BE40521014521454")
                .balance(5045825)
                .bank(ing)
                .creationDate(LocalDate.now())
                .ownership(new OwnershipBuilder()
                        .type(OwnershipType.INDIVIDUAL)
                        .owner(customer)
                        .build())
                .build();
        String json = mapper.writeValueAsString(account);

        Mockito.when(principal.getPrincipal()).thenReturn(userDetails);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/account")
                .content(json)
                .principal(principal)
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Account creation request registered")));
    }

    @Test
    @WithMockUser(username = "CUSTOMER=R000001FE", roles = {"CUSTOMER"})
    public void testCreateAccountAlreadyExist() throws Exception {
        Bank ing = bankService.findBankByName("ING").get();
        Customer customer = new Customer("R000001FE", "Friedrich Engels", encoder.encode("capital"), LocalDate.of(1820, 11, 28));
        UserDetailsImpl userDetails = UserDetailsImpl.build(customer);
        Account account = new AccountBuilder()
                .type(AccountType.CURRENT_ACCOUNT)
                .iban("BE40521014521455")
                .balance(5045825)
                .bank(ing)
                .creationDate(LocalDate.now())
                .ownership(new OwnershipBuilder()
                        .type(OwnershipType.INDIVIDUAL)
                        .owner(customer)
                        .build())
                .build();
        String json = mapper.writeValueAsString(account);

        Mockito.when(principal.getPrincipal()).thenReturn(userDetails);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/account")
                .content(json)
                .principal(principal)
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(UserDetailsImpl.build(customer))))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "CUSTOMER=R000001FE", roles = {"CUSTOMER"})
    public void getAccount() throws Exception {
        mockMvc.perform(get("/api/account/{iban}", "BE07978547171366"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bank.name", is("ING")))
                .andExpect(jsonPath("$.bank.swift", is("ING-01")))
                .andExpect(jsonPath("$.ownership.involvements[0].customer.name", is("Karl Marx")));
    }
}