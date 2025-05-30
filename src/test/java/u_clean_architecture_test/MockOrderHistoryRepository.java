package u_clean_architecture;

import u_clean_architecture.a_domain.Order;
import u_clean_architecture.b_usecases.OrderHistoryRepository;

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
