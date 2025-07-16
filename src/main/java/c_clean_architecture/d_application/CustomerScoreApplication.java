package c_clean_architecture.d_application;

import c_clean_architecture.b_usecases.CustomerScoreUseCase;
import c_clean_architecture.b_usecases.Logger;
import c_clean_architecture.b_usecases.OrderRepository;
import c_clean_architecture.b_usecases.ReturnRepository;
import c_clean_architecture.c_adapters.JdbcOrderRepository;
import c_clean_architecture.c_adapters.JdbcReturnRepository;
import c_clean_architecture.c_adapters.Slf4jLoggerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class CustomerScoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerScoreApplication.class, args);
    }

    // === Clean Architecture Wiring ===

    @Bean
    public CustomerScoreUseCase customerScoreUseCase(OrderRepository orderRepo,
                                                     ReturnRepository returnRepo,
                                                     Logger logger) {
        return new CustomerScoreUseCase(orderRepo, returnRepo, logger);
    }

    @Bean
    public OrderRepository orderHistoryRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcOrderRepository(jdbcTemplate);
    }

    @Bean
    public ReturnRepository returnHistoryRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcReturnRepository(jdbcTemplate);
    }

    @Bean
    public Logger logger() {
        return new Slf4jLoggerAdapter();
    }
}
