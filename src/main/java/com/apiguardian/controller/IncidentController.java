package com.apiguardian.controller;

import com.apiguardian.agent.LeadAgent;
import com.apiguardian.model.IncidentReport;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    private final LeadAgent leadAgent;

    public IncidentController(LeadAgent leadAgent) {
        this.leadAgent = leadAgent;
    }

    @PostMapping("/investigate/{serviceName}")
    public IncidentReport investigate(
            @PathVariable
            @Size(min = 1, max = 80)
            @Pattern(regexp = "[a-zA-Z0-9][a-zA-Z0-9._-]*", message = "must be a valid service name")
            String serviceName) {
        return leadAgent.investigate(serviceName);
    }
}
