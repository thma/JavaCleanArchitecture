package c_clean_architecture.b_usecases;

import c_clean_architecture.a_domain.Order;

import java.util.List;

public interface OrderRepository {
    List<Order> findOrdersByCustomerId(Long customerId);
}
