package u_clean_architecture_test;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import u_clean_architecture.a_domain.Order;
import u_clean_architecture.b_usecases.CustomerScoreUseCase;
import u_clean_architecture.c_ports.Logger;
import u_clean_architecture.c_ports.OrderHistoryRepository;
import u_clean_architecture.c_ports.ReturnHistoryRepository;
import u_clean_architecture.d_adapters.Slf4jLoggerAdapter;
import u_clean_architecture.e_application.CustomerScoreController;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.List;

@SpringBootApplication
public class CustomerScoreMOCKApplication {

  public static void main(String[] args) {
    SpringApplication.run(CustomerScoreMOCKApplication.class, args);
  }

  @Bean
  CustomerScoreController customerScoreController(CustomerScoreUseCase customerScoreUseCase) {
    return new CustomerScoreController(customerScoreUseCase);
  }

  @Bean
  public CustomerScoreUseCase customerScoreUseCase(@Qualifier("MockOrders") OrderHistoryRepository orderRepo,
                                                   @Qualifier("MockReturns") ReturnHistoryRepository returnRepo,
                                                   Logger logger) {
    return new CustomerScoreUseCase(orderRepo, returnRepo, logger);
  }

  @Bean(name = "MockOrders")
  public OrderHistoryRepository orderHistoryRepository() {
    return new MockOrderHistoryRepository(List.of(
        new Order(0L, BigDecimal.TEN),
        new Order(1L, BigDecimal.valueOf(100))
    ));
  }

  @Bean(name = "MockReturns")
  public ReturnHistoryRepository returnHistoryRepository() {
    return new MockReturnHistoryRepository(List.of());
  }

  @Bean
  public Logger logger() {
    return new Slf4jLoggerAdapter();
  }

  @Bean
  public DataSource dataSource() {
    return new MockDataSource();
  }
}

  class MockDataSource implements DataSource {
    // Implement DataSource methods as needed for testing
    @Override
    public Connection getConnection() throws SQLException {
      return null; // Return a mock or real connection as needed
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
      return null; // Return a mock or real connection as needed
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
      return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
      return 0;
    }

    @Override
    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
      return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
      return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
      return false;
    }

    // Other DataSource methods can be implemented similarly
  }


