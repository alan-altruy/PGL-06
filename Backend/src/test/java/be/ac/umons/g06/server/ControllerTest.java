package be.ac.umons.g06.server;

import be.ac.umons.g06.server.controller.*;
import be.ac.umons.g06.server.service.AccountService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private AccountService accountService;

	@Autowired
	private AccountController accountController;

	@Autowired
	private AuthController authController;

	@Autowired
	private BankController bankController;

	@Autowired
	private CustomerController customerController;

	@Test
	public void testAccountController(){
		Assertions.assertThat(accountController).isNotNull();
	}

	@Test
	public void testAuthController(){
		Assertions.assertThat(authController).isNotNull();
	}

	@Test
	public void testBankController(){
		Assertions.assertThat(bankController).isNotNull();
	}

	@Test
	public void testCustomerController(){
		Assertions.assertThat(customerController).isNotNull();
	}

	@Test
	@WithMockUser
	public void testPutAccount() throws Exception {
		mockMvc.perform(put("/api/account/"))
				.andExpect(status().isMethodNotAllowed());

	}

	@Test
	@WithMockUser
	public void testPostSignin() throws Exception {
		mockMvc.perform(post("/signin"))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser
	public void testPostSignup() throws Exception {
		mockMvc.perform(post("/signup"))
				.andExpect(status().isNotFound());
	}
}