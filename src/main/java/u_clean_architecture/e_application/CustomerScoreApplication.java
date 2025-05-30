package u_clean_architecture.e_application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import u_clean_architecture.b_usecases.CustomerScoreUseCase;
import u_clean_architecture.c_ports.Logger;
import u_clean_architecture.c_ports.OrderRepository;
import u_clean_architecture.c_ports.ReturnRepository;
import u_clean_architecture.d_adapters.JdbcOrderRepository;
import u_clean_architecture.d_adapters.JdbcReturnRepository;
import u_clean_architecture.d_adapters.Slf4jLoggerAdapter;

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
