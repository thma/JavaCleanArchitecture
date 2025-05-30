package u_clean_architecture.c_adapters;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import u_clean_architecture.a_domain.Return;
import u_clean_architecture.b_usecases.ReturnHistoryRepository;

import java.util.List;

@Repository
public class JdbcReturnHistoryRepository implements ReturnHistoryRepository {

  private final JdbcTemplate jdbcTemplate;

  public JdbcReturnHistoryRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<Return> findReturnsByCustomerId(Long customerId) {
    return jdbcTemplate.query(
        "SELECT * FROM returns WHERE customer_id = ?",
        new Object[]{customerId},
        (rs, rowNum) -> new Return(rs.getLong("id"), rs.getDate("return_date").toLocalDate())
    );
  }
}

