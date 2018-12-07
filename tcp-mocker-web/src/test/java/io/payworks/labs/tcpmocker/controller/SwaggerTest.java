package io.payworks.labs.tcpmocker.controller;

import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class SwaggerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private MockMvc mvc;

    @Test
    public void anonymousUserCanAccessAPI() throws Exception {
        mvc.perform(get("/v2/api-docs"))
                .andExpect(status().isOk());
    }

    @Test
    public void anonymousUserCanAccessUI() throws Exception {
        mvc.perform(get("/swagger-ui.html"))
                .andExpect(status().isOk());
    }

    @Test
    public void apiHasExpectedDefinitions() throws Exception {
        mvc.perform(get("/v2/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info.title").value(Matchers.equalToIgnoringCase("Payworks TCP Mocker REST API")))
                .andExpect(jsonPath("$.paths./recordings").exists())
                .andExpect(jsonPath("$.paths./recordings/last").exists());
    }
}
