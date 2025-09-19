package c_clean_architecture.effects.handlers;

import c_clean_architecture.a_domain.Order;
import c_clean_architecture.effects.core.EffectHandler;
import c_clean_architecture.effects.definitions.OrderRepositoryEffect;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Production implementation of OrderRepositoryEffect handler using JDBC.
 */
@Component
public class JdbcOrderRepositoryHandler implements EffectHandler<OrderRepositoryEffect<?>> {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Order> orderRowMapper;

    public JdbcOrderRepositoryHandler(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.orderRowMapper = (rs, rowNum) -> new Order(
            rs.getLong("id"),
            rs.getBigDecimal("amount")
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T handle(OrderRepositoryEffect<?> effect) {
        if (effect instanceof OrderRepositoryEffect.FindByCustomerId findByCustomerId) {
            return (T) findOrdersByCustomerId(findByCustomerId.customerId());
        } else if (effect instanceof OrderRepositoryEffect.FindById findById) {
            return (T) findOrderById(findById.orderId());
        } else if (effect instanceof OrderRepositoryEffect.Save save) {
            return (T) saveOrder(save.order());
        } else if (effect instanceof OrderRepositoryEffect.DeleteById deleteById) {
            deleteOrderById(deleteById.orderId());
            return null;
        } else if (effect instanceof OrderRepositoryEffect.CountByCustomerId countByCustomerId) {
            return (T) countOrdersByCustomerId(countByCustomerId.customerId());
        }
        throw new IllegalArgumentException("Unknown effect: " + effect);
    }

    private List<Order> findOrdersByCustomerId(Long customerId) {
        String sql = "SELECT id, amount FROM orders WHERE customer_id = ?";
        return jdbcTemplate.query(sql, orderRowMapper, customerId);
    }

    private Optional<Order> findOrderById(Long orderId) {
        String sql = "SELECT id, amount FROM orders WHERE id = ?";
        List<Order> orders = jdbcTemplate.query(sql, orderRowMapper, orderId);
        return orders.isEmpty() ? Optional.empty() : Optional.of(orders.get(0));
    }

    private Order saveOrder(Order order) {
        if (order.getId() == null) {
            // Insert new order
            String sql = "INSERT INTO orders (amount) VALUES (?)";
            jdbcTemplate.update(sql, order.getAmount());
            // In a real implementation, we'd get the generated ID
            return order;
        } else {
            // Update existing order
            String sql = "UPDATE orders SET amount = ? WHERE id = ?";
            jdbcTemplate.update(sql, order.getAmount(), order.getId());
            return order;
        }
    }

    private void deleteOrderById(Long orderId) {
        String sql = "DELETE FROM orders WHERE id = ?";
        jdbcTemplate.update(sql, orderId);
    }

    private Long countOrdersByCustomerId(Long customerId) {
        String sql = "SELECT COUNT(*) FROM orders WHERE customer_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, customerId);
    }
}