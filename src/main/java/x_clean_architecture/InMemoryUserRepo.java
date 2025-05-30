package x_clean_architecture;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryUserRepo implements UserRepository {

  private final Map<Long, User> users = new HashMap<>();

  public void addUser(User user) {
    users.put(user.getId(), user);
  }

  @Override
  public Optional<User> findById(Long id) {
    return Optional.ofNullable(users.get(id));
  }
}

