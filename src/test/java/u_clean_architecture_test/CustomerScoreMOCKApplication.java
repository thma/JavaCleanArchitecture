package u_clean_architecture;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import u_clean_architecture.b_usecases.CustomerScoreUseCase;
import u_clean_architecture.b_usecases.Logger;
import u_clean_architecture.b_usecases.OrderHistoryRepository;
import u_clean_architecture.b_usecases.ReturnHistoryRepository;
import u_clean_architecture.c_adapters.Slf4jLoggerAdapter;

import java.util.List;

@ComponentScan({"u_clean_architecture"})
@SpringBootApplication
public class CustomerScoreMOCKApplication {

  public static void main(String[] args) {
    SpringApplication.run(CustomerScoreMOCKApplication.class, args);
  }

  // === Clean Architecture Wiring ===


  @Bean
  public OrderHistoryRepository orderHistoryRepository(JdbcTemplate jdbcTemplate) {
    return new MockOrderHistoryRepository(List.of());
  }

  @Bean
  public ReturnHistoryRepository returnHistoryRepository(JdbcTemplate jdbcTemplate) {
    return new MockReturnHistoryRepository(List.of());
  }

  @Bean
  public Logger logger() {
    return new Slf4jLoggerAdapter();
  }
}
