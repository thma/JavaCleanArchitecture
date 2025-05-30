package u_clean_architecture_test;

import u_clean_architecture.a_domain.Order;
import u_clean_architecture.c_ports.OrderHistoryRepository;

import java.util.List;

public class MockOrderHistoryRepository implements OrderHistoryRepository {
  private List<Order> orders;

  public MockOrderHistoryRepository(List<Order> orders) {
    this.orders = orders;
  }

  @Override
  public List<Order> findOrdersByCustomerId(Long customerId) {
    return orders;
  }
}
