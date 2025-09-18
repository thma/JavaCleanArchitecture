package c_clean_architecture.d_application;

import c_clean_architecture.effects.runtime.EffectConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * Spring Boot application with algebraic effects support.
 * This application demonstrates Clean Architecture with algebraic effects.
 */
@SpringBootApplication
@ComponentScan(basePackages = {
    "c_clean_architecture.d_application",
    "c_clean_architecture.effects.handlers",
    "c_clean_architecture.effects.runtime"
})
@Import(EffectConfiguration.class)
public class CustomerScoreApplicationEffects {

    public static void main(String[] args) {
        SpringApplication.run(CustomerScoreApplicationEffects.class, args);
    }
}