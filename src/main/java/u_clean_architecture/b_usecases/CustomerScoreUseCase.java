package u_clean_architecture.b_usecases;

import u_clean_architecture.a_domain.Customer;
import u_clean_architecture.a_domain.Order;
import u_clean_architecture.a_domain.Return;
import u_clean_architecture.c_ports.Logger;
import u_clean_architecture.c_ports.OrderHistoryRepository;
import u_clean_architecture.c_ports.ReturnHistoryRepository;

import java.util.List;

public class CustomerScoreUseCase {

  private final OrderHistoryRepository orderRepo;
  private final ReturnHistoryRepository returnRepo;
  private final Logger logger;

  public CustomerScoreUseCase(OrderHistoryRepository orderRepo,
                              ReturnHistoryRepository returnRepo,
                              Logger logger) {
    this.orderRepo = orderRepo;
    this.returnRepo = returnRepo;
    this.logger = logger;
  }

  public int calculateScore(long customerId ) {
    logger.info("Calculating score for customer " + customerId);

    List<Order> orders = orderRepo.findOrdersByCustomerId(customerId);
    List<Return> returns = returnRepo.findReturnsByCustomerId(customerId);

    Customer customer = new Customer(customerId);
    int score = customer.calculateScore(orders, returns);

    logger.info("Customer " + customerId + " has score " + score);
    return score;
  }
}


