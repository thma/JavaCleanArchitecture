package x_clean_architecture;

import java.util.Optional;

public interface UserRepository {
  Optional<User> findById(Long id);
}