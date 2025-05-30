package x_clean_architecture;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GetUserByIdUseCaseTest {

  @Test
  public void returnsUser_whenUserExists() {
    // Given
    InMemoryUserRepo repo = new InMemoryUserRepo();
    User user = new User(1L, "Alice", "alice@example.com");
    repo.addUser(user);

    GetUserByIdUseCase useCase = new GetUserByIdUseCase(repo);

    // When
    User result = useCase.execute(1L);

    // Then
    Assertions.assertEquals("Alice", result.getName());
  }

  @Test
  public void throwsException_whenUserNotFound() {
    // Given
    InMemoryUserRepo repo = new InMemoryUserRepo();
    GetUserByIdUseCase useCase = new GetUserByIdUseCase(repo);

    // When & Then
    Assertions.assertThrows(RuntimeException.class, () -> {
      useCase.execute(42L);
    });
  }
}
