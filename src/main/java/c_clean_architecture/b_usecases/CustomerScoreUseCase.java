package c_clean_architecture.b_usecases;

import c_clean_architecture.a_domain.Customer;
import c_clean_architecture.a_domain.Order;
import c_clean_architecture.a_domain.Return;

import java.util.List;

public class CustomerScoreUseCase {

  private final OrderRepository orderRepo;
  private final ReturnRepository returnRepo;
  private final Logger logger;

  public CustomerScoreUseCase(OrderRepository orderRepo,
                              ReturnRepository returnRepo,
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


