package u_clean_architecture_test;

import org.junit.jupiter.api.Test;
import u_clean_architecture.a_domain.Customer;
import u_clean_architecture.a_domain.Order;
import u_clean_architecture.a_domain.Return;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DomainTest {

  @Test
  public void testZeroScoreForCustomerWithNoOrdersOrReturns() {
    // Given
    Customer customer = new Customer(1L);

    // When
    List<Order> orders = List.of(); // Assuming we have an empty list of orders for this test
    List<Return> returns = List.of(); // Assuming we have an empty list of returns for this test

    // Then
    assertEquals(0, customer.calculateSimpleScore(orders,returns));
  }

  @Test
  public void testScoreCalculationWithOrdersAndReturns() {
    // Given
    Customer customer = new Customer(1L);
    List<Order> orders = List.of(
        new Order(1L, BigDecimal.valueOf(100)),
        new Order(2L, BigDecimal.valueOf(200))
    );
    List<Return> returns = List.of(
        new Return(1L, LocalDate.now())
    );

    // When
    int score = customer.calculateSimpleScore(orders, returns);

    // Then
    assertEquals(20, score); // Assuming the score is calculated as total order amount minus total return amount
  }
}
