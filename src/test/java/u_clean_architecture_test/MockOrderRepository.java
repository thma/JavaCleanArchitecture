package u_clean_architecture_test;

import u_clean_architecture.a_domain.Order;
import u_clean_architecture.c_ports.OrderRepository;

import java.util.List;

public class MockOrderRepository implements OrderRepository {
  private List<Order> orders;

  public MockOrderRepository(List<Order> orders) {
    this.orders = orders;
  }

  @Override
  public List<Order> findOrdersByCustomerId(Long customerId) {
    return orders;
  }
}
