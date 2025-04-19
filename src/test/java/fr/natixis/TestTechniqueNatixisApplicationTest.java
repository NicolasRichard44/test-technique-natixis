package fr.natixis;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
class TestTechniqueNatixisApplicationTest {

    @Test
    void contextLoads() {
        // Test que l'application démarre correctement
        TestTechniqueNatixisApplication.main(new String[]{});
    }
}