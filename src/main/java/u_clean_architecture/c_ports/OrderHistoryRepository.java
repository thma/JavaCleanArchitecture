package u_clean_architecture.b_usecases;

import u_clean_architecture.a_domain.Order;

import java.util.List;

public interface OrderHistoryRepository {
  List<Order> findOrdersByCustomerId(Long customerId);
}
