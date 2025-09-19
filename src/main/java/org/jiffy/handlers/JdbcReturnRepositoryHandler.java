package c_clean_architecture.effects.handlers;

import c_clean_architecture.a_domain.Return;
import c_clean_architecture.effects.core.EffectHandler;
import c_clean_architecture.effects.definitions.ReturnRepositoryEffect;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Production implementation of ReturnRepositoryEffect handler using JDBC.
 */
@Component
public class JdbcReturnRepositoryHandler implements EffectHandler<ReturnRepositoryEffect<?>> {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Return> returnRowMapper;

    public JdbcReturnRepositoryHandler(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.returnRowMapper = (rs, rowNum) -> new Return(
            rs.getLong("id"),
            rs.getLong("order_id"),
            rs.getString("reason"),
            rs.getDate("created_at") != null ? rs.getDate("created_at").toLocalDate() : null,
            rs.getBigDecimal("amount")
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T handle(ReturnRepositoryEffect<?> effect) {
        if (effect instanceof ReturnRepositoryEffect.FindByCustomerId findByCustomerId) {
            return (T) findReturnsByCustomerId(findByCustomerId.customerId());
        } else if (effect instanceof ReturnRepositoryEffect.FindById findById) {
            return (T) findReturnById(findById.returnId());
        } else if (effect instanceof ReturnRepositoryEffect.Save save) {
            return (T) saveReturn(save.returnItem());
        } else if (effect instanceof ReturnRepositoryEffect.DeleteById deleteById) {
            deleteReturnById(deleteById.returnId());
            return null;
        } else if (effect instanceof ReturnRepositoryEffect.CountByCustomerId countByCustomerId) {
            return (T) countReturnsByCustomerId(countByCustomerId.customerId());
        }
        throw new IllegalArgumentException("Unknown effect: " + effect);
    }

    private List<Return> findReturnsByCustomerId(Long customerId) {
        String sql = "SELECT id, amount FROM returns WHERE customer_id = ?";
        return jdbcTemplate.query(sql, returnRowMapper, customerId);
    }

    private Optional<Return> findReturnById(Long returnId) {
        String sql = "SELECT id, amount FROM returns WHERE id = ?";
        List<Return> returns = jdbcTemplate.query(sql, returnRowMapper, returnId);
        return returns.isEmpty() ? Optional.empty() : Optional.of(returns.get(0));
    }

    private Return saveReturn(Return returnItem) {
        if (returnItem.getId() == null) {
            // Insert new return
            String sql = "INSERT INTO returns (amount) VALUES (?)";
            jdbcTemplate.update(sql, returnItem.getAmount());
            return returnItem;
        } else {
            // Update existing return
            String sql = "UPDATE returns SET amount = ? WHERE id = ?";
            jdbcTemplate.update(sql, returnItem.getAmount(), returnItem.getId());
            return returnItem;
        }
    }

    private void deleteReturnById(Long returnId) {
        String sql = "DELETE FROM returns WHERE id = ?";
        jdbcTemplate.update(sql, returnId);
    }

    private Long countReturnsByCustomerId(Long customerId) {
        String sql = "SELECT COUNT(*) FROM returns WHERE customer_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, customerId);
    }
}