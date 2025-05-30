package u_clean_architecture.d_adapters;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import u_clean_architecture.a_domain.Order;
import u_clean_architecture.c_ports.OrderHistoryRepository;

import java.util.List;

@Repository
public class JdbcOrderHistoryRepository implements OrderHistoryRepository {

  private final JdbcTemplate jdbcTemplate;

  public JdbcOrderHistoryRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<Order> findOrdersByCustomerId(Long customerId) {
    return jdbcTemplate.query(
        "SELECT * FROM orders WHERE customer_id = ?",
        new Object[]{customerId},
        (rs, rowNum) -> new Order(rs.getLong("id"), rs.getBigDecimal("amount"))
    );
  }
}

