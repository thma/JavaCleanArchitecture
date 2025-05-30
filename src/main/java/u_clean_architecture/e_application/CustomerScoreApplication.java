package u_clean_architecture.e_application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import u_clean_architecture.b_usecases.CustomerScoreUseCase;
import u_clean_architecture.c_ports.Logger;
import u_clean_architecture.c_ports.OrderHistoryRepository;
import u_clean_architecture.c_ports.ReturnHistoryRepository;
import u_clean_architecture.d_adapters.JdbcOrderHistoryRepository;
import u_clean_architecture.d_adapters.JdbcReturnHistoryRepository;
import u_clean_architecture.d_adapters.Slf4jLoggerAdapter;

@SpringBootApplication
public class CustomerScoreApplication {

  public static void main(String[] args) {
    SpringApplication.run(CustomerScoreApplication.class, args);
  }

  // === Clean Architecture Wiring ===

  @Bean
  public CustomerScoreUseCase customerScoreUseCase(OrderHistoryRepository orderRepo,
                                                   ReturnHistoryRepository returnRepo,
                                                   Logger logger) {
    return new CustomerScoreUseCase(orderRepo, returnRepo, logger);
  }

  @Bean
  public OrderHistoryRepository orderHistoryRepository(JdbcTemplate jdbcTemplate) {
    return new JdbcOrderHistoryRepository(jdbcTemplate);
  }

  @Bean
  public ReturnHistoryRepository returnHistoryRepository(JdbcTemplate jdbcTemplate) {
    return new JdbcReturnHistoryRepository(jdbcTemplate);
  }

  @Bean
  public Logger logger() {
    return new Slf4jLoggerAdapter();
  }
}
