package tn.esprit.ms3locationzone.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenConfiglocationzone {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title("Location & Zone Service API")
                .description("MS3 - RescueAid : zones, points d'intervention et localisation des incidents")
                .version("1.0.0"));
    }
}
