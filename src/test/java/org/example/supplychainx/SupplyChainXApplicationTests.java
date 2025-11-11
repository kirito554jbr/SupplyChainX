package org.example.supplychainx;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("Integration test - requires MySQL database to be running")
class SupplyChainXApplicationTests {

    @Test
    void contextLoads() {
        // This test verifies that the Spring application context loads successfully
        // Disabled because it requires actual MySQL database connection
    }

}
