package com.apiguardian;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "api-guardians.llm.enabled=false")
class ApiGuardianApplicationTests {

    @Test
    void contextLoads() {
    }
}
