package com.apiguardian.model;

import java.time.Instant;
import java.util.List;

public record IncidentReport(
        String serviceName,
        String status,
        String summary,
        List<String> evidence,
        String likelyRootCause,
        List<String> recommendedActions,
        String agentMode,
        Instant investigatedAt) {
}
