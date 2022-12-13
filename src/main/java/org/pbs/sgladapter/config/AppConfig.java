package org.pbs.sgladapter.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class AppConfig {
    @Value("${rest.sgl.flashnet.url}")
    private String sglUrl;
}
