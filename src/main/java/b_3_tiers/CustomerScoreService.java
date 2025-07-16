package b_3_tiers;

import a_it_just_happened.Order;
import a_it_just_happened.Return;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerScoreService {

  private final OrderRepository orderRepository;
  private final ReturnRepository returnRepository;
  private final Logger logger = LoggerFactory.getLogger(CustomerScoreService.class);

  public CustomerScoreService(OrderRepository orderRepository, ReturnRepository returnRepository) {
    this.orderRepository = orderRepository;
    this.returnRepository = returnRepository;
  }

  public int calculateScore(Long customerId) {
    logger.info("Calculating score for customer {}", customerId);

    List<Order> orders = orderRepository.findByCustomerId(customerId);
    List<Return> returns = returnRepository.findByCustomerId(customerId);

    int score = orders.size() * 10 - returns.size() * 20;

    logger.info("Customer {} has score {}", customerId, score);
    return score;
  }
}

