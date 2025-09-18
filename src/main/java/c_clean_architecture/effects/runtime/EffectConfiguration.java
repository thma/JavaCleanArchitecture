package c_clean_architecture.effects.runtime;

import c_clean_architecture.b_usecases.CustomerScoreUseCaseEffects;
import c_clean_architecture.effects.core.EffectRuntime;
import c_clean_architecture.effects.definitions.LogEffect;
import c_clean_architecture.effects.definitions.OrderRepositoryEffect;
import c_clean_architecture.effects.definitions.ReturnRepositoryEffect;
import c_clean_architecture.effects.handlers.JdbcOrderRepositoryHandler;
import c_clean_architecture.effects.handlers.JdbcReturnRepositoryHandler;
import c_clean_architecture.effects.handlers.Slf4jLogHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration for the algebraic effects runtime.
 * This wires up the effect handlers and creates the runtime.
 */
@Configuration
public class EffectConfiguration {

    /**
     * Create the production effect runtime with all handlers.
     */
    @Bean
    public EffectRuntime effectRuntime(
        Slf4jLogHandler logHandler,
        JdbcOrderRepositoryHandler orderHandler,
        JdbcReturnRepositoryHandler returnHandler
    ) {
        return EffectRuntime.builder()
            .withHandlerUnsafe(LogEffect.class, logHandler)
            .withHandlerUnsafe(OrderRepositoryEffect.FindByCustomerId.class, orderHandler)
            .withHandlerUnsafe(ReturnRepositoryEffect.FindByCustomerId.class, returnHandler)
            .build();
    }

    /**
     * Create the effect-based use case bean.
     */
    @Bean
    public CustomerScoreUseCaseEffects customerScoreUseCaseEffects() {
        return new CustomerScoreUseCaseEffects();
    }
}