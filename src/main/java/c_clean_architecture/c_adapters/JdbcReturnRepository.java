package c_clean_architecture.c_adapters;

import c_clean_architecture.a_domain.Return;
import c_clean_architecture.b_usecases.ReturnRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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

