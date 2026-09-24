# API Guardians V1

API Guardians is a Spring Boot service that investigates a mocked microservice incident. Its `LeadAgent` can let an LLM choose among three Spring AI tools—metrics, health, and logs—or run in a credential-free deterministic mock mode.

## Stack

- Java 21
- Spring Boot 3.5.16
- Maven
- Spring AI 1.1.8 with the OpenAI starter

## Project structure

```text
src/main/java/com/apiguardian
├── agent/LeadAgent.java
├── controller/IncidentController.java
├── model/IncidentReport.java
├── tool/MonitoringTools.java
└── ApiGuardianApplication.java
```

## Prerequisites

- JDK 21
- Maven 3.6.3+

## Run without an LLM key (default)

The default mode calls all three mocked tools locally and requires no external account or credential.

```bash
mvn spring-boot:run
```

## Run with LLM-directed tool selection

Set credentials in your shell; never put the real key in `application.yml` or commit a `.env` file.

macOS/Linux:

```bash
export OPENAI_API_KEY="your-key"
export API_GUARDIANS_LLM_ENABLED=true
mvn spring-boot:run
```

PowerShell:

```powershell
$env:OPENAI_API_KEY = "your-key"
$env:API_GUARDIANS_LLM_ENABLED = "true"
mvn spring-boot:run
```

Optionally set `OPENAI_MODEL`; it defaults to `gpt-4.1-mini`. `.env` and `.env.*` are ignored by Git. The committed `.env.example` contains placeholders only.

## Investigate a service

```bash
curl -X POST http://localhost:8080/api/incidents/investigate/payment-service
```

The JSON report includes status, summary, evidence, a likely root cause, recommended next actions, execution mode, and timestamp.

## Test and package

```bash
mvn test
mvn clean package
```

## V1 safety boundaries

- Monitoring responses are mock data; the service does not connect to production systems.
- Tools are read-only.
- The agent recommends actions but does not edit code or deploy.
- Production changes remain subject to human review and approval.
