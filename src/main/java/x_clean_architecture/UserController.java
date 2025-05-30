package x_clean_architecture;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  private final GetUserByIdUseCase getUserByIdUseCase;

  public UserController(GetUserByIdUseCase getUserByIdUseCase) {
    this.getUserByIdUseCase = getUserByIdUseCase;
  }

  @GetMapping("/users/{id}")
  public ResponseEntity<User> getUser(@PathVariable Long id) {
    User user = getUserByIdUseCase.execute(id);
    return ResponseEntity.ok(user);
  }
}
