package edu.ntnu.idatt2105.backend;

import edu.ntnu.idatt2105.backend.config.TestDatabaseConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

// @SpringBootTest(properties = {
//     "spring.datasource.url=jdbc:h2:mem:testdb",
//     "spring.datasource.driverClassName=org.h2.Driver",
//     "spring.datasource.username=sa",
//     "spring.datasource.password=password",
//     "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
// })
@SpringBootTest
@ActiveProfiles("test")
@Import(TestDatabaseConfig.class)
class BackendApplicationTests {

  @Test
  void contextLoads() {
  }
}