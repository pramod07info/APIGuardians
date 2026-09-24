package com.apiguardian.tool;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MonitoringToolsTest {

    private final MonitoringTools tools = new MonitoringTools();

    @Test
    void metricsAreScopedToRequestedService() {
        assertThat(tools.getMetrics("payment-service"))
                .contains("Service: payment-service", "Error Rate: 18.3%", "Pool Usage: 100%");
    }

    @Test
    void healthAndLogsExposeCorrelatedDatabaseEvidence() {
        assertThat(tools.getHealth("payment-service")).contains("Database: DEGRADED");
        assertThat(tools.getLogs("payment-service")).contains("HikariPool-1", "HTTP 500");
    }
}
