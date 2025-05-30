package u_clean_architecture_test;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import u_clean_architecture.b_usecases.CustomerScoreUseCase;
import u_clean_architecture.c_adapters.Slf4jLoggerAdapter;

import java.util.List;

public class ComputeScoreTest {

  CustomerScoreUseCase customerScoreUseCase = new CustomerScoreUseCase(
      new MockOrderHistoryRepository(List.of()),
      new MockReturnHistoryRepository(List.of()),
      new Slf4jLoggerAdapter()
  );

  @Test
  public void testComputeScore() {
    // Given
    long customerId = 1L;

    // When
    int score = customerScoreUseCase.calculateScore(customerId);

    // Then
    assert score == 0 : "Expected score to be 0 for a customer with no orders or returns";
  }


}
