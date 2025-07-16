package u_clean_architecture.c_adapters;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import u_clean_architecture.a_domain.Order;
import u_clean_architecture.b_usecases.OrderRepository;

import java.util.List;

@Repository
public class JdbcOrderRepository implements OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcOrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Order> findOrdersByCustomerId(Long customerId) {
        return jdbcTemplate.query(
                "SELECT * FROM orders WHERE customer_id = ?",
                (rs, rowNum) -> new Order(rs.getLong("id"), rs.getBigDecimal("amount")),
                customerId
        );
    }
}

