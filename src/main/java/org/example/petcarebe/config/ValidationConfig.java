package org.example.petcarebe.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.validation")
public class ValidationConfig {
    private Password password = new Password();
    private Username username = new Username();

    @Data
    public static class Password {
        private int minLength = 3;
        private int maxLength = 50;

    }

    @Getter
    @Setter
    public static class Username {
        private int minLength = 3;
        private int maxLength = 50;

    }
}
