package b_3_tiers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerScoreController {

  private final CustomerScoreService customerScoreService;

  public CustomerScoreController(CustomerScoreService customerScoreService) {
    this.customerScoreService = customerScoreService;
  }

  @GetMapping("/customers/{id}/score")
  public ResponseEntity<Integer> getCustomerScore(@PathVariable Long id) {
    int score = customerScoreService.calculateScore(id);
    return ResponseEntity.ok(score);
  }
}
