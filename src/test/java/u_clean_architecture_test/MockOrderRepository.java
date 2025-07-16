package u_clean_architecture_test;

import u_clean_architecture.a_domain.Order;
import u_clean_architecture.b_usecases.OrderRepository;

import java.util.List;

public class MockOrderRepository implements OrderRepository {
  private final List<Order> orders;

  public MockOrderRepository(List<Order> orders) {
    this.orders = orders;
  }

  @Override
  public List<Order> findOrdersByCustomerId(Long customerId) {
    return orders;
  }
}
