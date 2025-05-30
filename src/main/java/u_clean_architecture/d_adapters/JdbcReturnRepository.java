package u_clean_architecture.d_adapters;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import u_clean_architecture.a_domain.Return;
import u_clean_architecture.c_ports.ReturnRepository;

import java.util.List;

@Repository
public class JdbcReturnRepository implements ReturnRepository {

  private final JdbcTemplate jdbcTemplate;

  public JdbcReturnRepository(JdbcTemplate jdbcTemplate) {
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

