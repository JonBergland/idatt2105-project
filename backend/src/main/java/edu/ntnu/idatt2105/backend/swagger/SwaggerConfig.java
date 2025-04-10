package edu.ntnu.idatt2105.backend.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger configuration class for the Yard API.
 */
@Configuration
public class SwaggerConfig {

  /**
   * Configures the OpenAPI documentation for the Yard API.
   *
   * @return an OpenAPI object with the API information
   */
  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Yard API")
            .version("1.0")
            .description("API documentation for Yard"));
  }
}
