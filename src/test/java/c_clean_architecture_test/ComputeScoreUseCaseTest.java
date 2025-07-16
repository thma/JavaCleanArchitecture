package c_clean_architecture_test;

import c_clean_architecture.b_usecases.CustomerScoreUseCase;
import c_clean_architecture.c_adapters.Slf4jLoggerAdapter;
import org.junit.jupiter.api.Test;

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
