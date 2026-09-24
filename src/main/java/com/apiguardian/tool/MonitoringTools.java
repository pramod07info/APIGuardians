package com.apiguardian.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class MonitoringTools {

    @Tool(description = "Get production metrics for a microservice, including error rate, latency, CPU, memory, and database connection-pool usage")
    public String getMetrics(@ToolParam(description = "Name of the microservice to inspect") String serviceName) {
        return """
                Service: %s
                Error Rate: 18.3%%
                P95 Latency: 8200ms
                CPU: 34%%
                Memory: 48%%
                Database Connection Pool Usage: 100%%
                """.formatted(serviceName).trim();
    }

    @Tool(description = "Get application and dependency health for a microservice")
    public String getHealth(@ToolParam(description = "Name of the microservice to inspect") String serviceName) {
        return """
                Service: %s
                Application: UP
                Database: DEGRADED
                Kafka: UP
                Redis: UP
                """.formatted(serviceName).trim();
    }

    @Tool(description = "Get recent relevant error logs for a microservice")
    public String getLogs(@ToolParam(description = "Name of the microservice to inspect") String serviceName) {
        return """
                Service: %s
                2026-09-24T10:14:28Z ERROR HikariPool-1 - Connection is not available
                2026-09-24T10:14:28Z ERROR Connection request timed out after 30000ms
                2026-09-24T10:14:29Z ERROR Request failed with HTTP 500
                """.formatted(serviceName).trim();
    }
}
