package u_clean_architecture.d_application;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import u_clean_architecture.b_usecases.CustomerScoreUseCase;

@RestController
@RequestMapping("/customers")
public class CustomerScoreController {

    private final CustomerScoreUseCase customerScoreUseCase;

    public CustomerScoreController(CustomerScoreUseCase customerScoreUseCase) {
        this.customerScoreUseCase = customerScoreUseCase;
    }

    @GetMapping("/{id}/score")
    public ResponseEntity<Integer> getScore(@PathVariable Long id) {
        int score = customerScoreUseCase.calculateScore(id);
        return ResponseEntity.ok(score);
    }
}

