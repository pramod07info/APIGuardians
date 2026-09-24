package com.apiguardian.agent;

import com.apiguardian.model.IncidentReport;
import com.apiguardian.tool.MonitoringTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

@Service
public class LeadAgent {

    private static final String SYSTEM_PROMPT = """
            You are the Lead Agent for API Guardians. Investigate the requested service using the
            available monitoring tools. You decide which tools to call and in which order. Base every
            conclusion on tool evidence, distinguish facts from hypotheses, and recommend safe next
            steps. Do not claim to have changed code or production. Return a concise incident report.
            """;

    private final ObjectProvider<ChatClient.Builder> chatClientBuilder;
    private final MonitoringTools monitoringTools;
    private final boolean llmEnabled;
    private final Clock clock;

    @Autowired
    public LeadAgent(ObjectProvider<ChatClient.Builder> chatClientBuilder,
                     MonitoringTools monitoringTools,
                     @Value("${api-guardians.llm.enabled:false}") boolean llmEnabled) {
        this(chatClientBuilder, monitoringTools, llmEnabled, Clock.systemUTC());
    }

    LeadAgent(ObjectProvider<ChatClient.Builder> chatClientBuilder,
              MonitoringTools monitoringTools,
              boolean llmEnabled,
              Clock clock) {
        this.chatClientBuilder = chatClientBuilder;
        this.monitoringTools = monitoringTools;
        this.llmEnabled = llmEnabled;
        this.clock = clock;
    }

    public IncidentReport investigate(String serviceName) {
        if (llmEnabled) {
            return investigateWithLlm(serviceName);
        }
        return investigateDeterministically(serviceName);
    }

    private IncidentReport investigateWithLlm(String serviceName) {
        ChatClient.Builder builder = chatClientBuilder.getIfAvailable();
        if (builder == null) {
            throw new IllegalStateException("LLM mode is enabled, but no ChatClient is configured");
        }

        String report = builder.build()
                .prompt()
                .system(SYSTEM_PROMPT)
                .user("Investigate service: " + serviceName)
                .tools(monitoringTools)
                .call()
                .content();

        return new IncidentReport(
                serviceName,
                "INVESTIGATED",
                report,
                List.of("The LLM selected and invoked the available mocked monitoring tools."),
                "See the agent-generated summary.",
                List.of("Validate the hypothesis in a non-production environment before making changes."),
                "llm",
                Instant.now(clock));
    }

    private IncidentReport investigateDeterministically(String serviceName) {
        String metrics = monitoringTools.getMetrics(serviceName);
        String health = monitoringTools.getHealth(serviceName);
        String logs = monitoringTools.getLogs(serviceName);

        return new IncidentReport(
                serviceName,
                "DEGRADED",
                "High error rate and latency correlate with an exhausted database connection pool.",
                List.of(metrics, health, logs),
                "Probable database connection-pool exhaustion; code and configuration changes still need review.",
                List.of(
                        "Review recent connection-pool and database configuration changes.",
                        "Check for leaked or long-running database connections.",
                        "Reproduce under load and validate a fix before deployment.",
                        "Require human approval before any production change."),
                "deterministic-mock",
                Instant.now(clock));
    }
}
