package com.apiguardian.controller;

import com.apiguardian.agent.LeadAgent;
import com.apiguardian.model.IncidentReport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IncidentController.class)
class IncidentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LeadAgent leadAgent;

    @Test
    void investigatesAValidService() throws Exception {
        when(leadAgent.investigate("payment-service")).thenReturn(new IncidentReport(
                "payment-service", "DEGRADED", "summary", List.of("evidence"), "cause",
                List.of("action"), "deterministic-mock", Instant.parse("2026-09-24T00:00:00Z")));

        mockMvc.perform(post("/api/incidents/investigate/payment-service"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serviceName").value("payment-service"))
                .andExpect(jsonPath("$.status").value("DEGRADED"));
    }

    @Test
    void rejectsAnInvalidServiceName() throws Exception {
        mockMvc.perform(post("/api/incidents/investigate/invalid%20service"))
                .andExpect(status().isBadRequest());
    }
}
