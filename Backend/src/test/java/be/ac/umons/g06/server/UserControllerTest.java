package be.ac.umons.g06.server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username="CUSTOMER=R000001FE",roles={"CUSTOMER"})
    public void testGetUser() throws Exception {
        mockMvc.perform(get("/api/customer/{id}", "R000002AG"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("R000002AG")))
                .andExpect(jsonPath("$.name", is("Antonio Gramsci")))
                .andExpect(jsonPath("$.birthdate", is("1891-01-21")));
    }

    @Test
    @WithMockUser(username="CUSTOMER=R000001FE",roles={"CUSTOMER"})
    public void testGetUserNotExist() throws Exception {
        mockMvc.perform(get("/api/customer/{id}", "R000002AP"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="CUSTOMER=R000001FE",roles={"CUSTOMER"})
    public void testNotFound() throws Exception {
        mockMvc.perform(get("/api/customer"))
                .andExpect(status().isNotFound());
    }
}