package org.pbs.sgladapter;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("SGL Adapter REST API")
                        .description("REST API for Submitting and Retrieving Job status for SGL (File Restore/Archive)")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Tim Kitchens")
                                .email("tkitchens@pbs.org"))
                );
    }
}
