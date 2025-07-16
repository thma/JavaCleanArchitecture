package b_3_tiers;

import a_it_just_happened.Return;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReturnRepository {

  private final JdbcTemplate jdbcTemplate;

  public ReturnRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public List<Return> findByCustomerId(Long customerId) {
    return jdbcTemplate.query(
        "SELECT * FROM returns WHERE customer_id = ?",
        new Object[]{customerId},
        (rs, rowNum) -> new Return(rs.getLong("id"), rs.getDate("return_date").toLocalDate())
    );
  }
}

