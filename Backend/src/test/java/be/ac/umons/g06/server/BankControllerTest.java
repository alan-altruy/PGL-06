package be.ac.umons.g06.server;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class BankControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username="CUSTOMER=R000001FE",roles={"CUSTOMER"})
    public void testGetBanks() throws Exception {
        mockMvc.perform(get("/api/banks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].swift", is("ING-01")))
                .andExpect(jsonPath("$[0].name", is("ING")));
    }

    @Test
    @WithMockUser(username="CUSTOMER=R000001FE",roles={"CUSTOMER"})
    public void testGetBank() throws Exception {
        mockMvc.perform(get("/api/bank/{swift}", "Belfius"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Belfius")))
                .andExpect(jsonPath("$.swift", is("Belfius-02")));
    }
}