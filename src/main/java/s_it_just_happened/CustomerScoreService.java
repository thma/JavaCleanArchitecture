package s_it_just_happened;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class CustomerScoreService {

  private final JdbcTemplate jdbcTemplate;
  private final Logger logger = LoggerFactory.getLogger(CustomerScoreService.class);

  public CustomerScoreService(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public int calculateScore(Long customerId) {
    logger.info("Calculating score for customer {}", customerId);

    List<Order> orders = jdbcTemplate.query(
        "SELECT * FROM orders WHERE customer_id = ?",
        new Object[]{customerId},
        (rs, rowNum) -> new Order(rs.getLong("id"), rs.getBigDecimal("amount"))
    );

    List<Return> returns = jdbcTemplate.query(
        "SELECT * FROM returns WHERE customer_id = ?",
        new Object[]{customerId},
        (rs, rowNum) -> new Return(rs.getLong("id"), rs.getDate("return_date").toLocalDate())
    );

    int score = (int) (orders.size() * 10 - returns.size() * 20);

    logger.info("Customer {} has score {}", customerId, score);
    return score;
  }
}

