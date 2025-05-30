package u_clean_architecture_test;

import org.junit.jupiter.api.Test;
import u_clean_architecture.b_usecases.CustomerScoreUseCase;
import u_clean_architecture.d_adapters.Slf4jLoggerAdapter;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComputeScoreUseCaseTest {

  CustomerScoreUseCase customerScoreUseCase = new CustomerScoreUseCase(
      new MockOrderRepository(List.of()),
      new MockReturnRepository(List.of()),
      new Slf4jLoggerAdapter()
  );

  @Test
  public void testComputeScore() {
    // Given
    long customerId = 1L;

    // When
    int score = customerScoreUseCase.calculateScore(customerId);

    // Then
    assertEquals(100, score);
  }


}
