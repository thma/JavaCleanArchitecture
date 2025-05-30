package u_clean_architecture.c_ports;

import u_clean_architecture.a_domain.Order;

import java.util.List;

public interface OrderHistoryRepository {
  List<Order> findOrdersByCustomerId(Long customerId);
}
