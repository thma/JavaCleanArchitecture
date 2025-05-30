package x_clean_architecture;

public class GetUserByIdUseCase {

  private final UserRepository userRepository;

  public GetUserByIdUseCase(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User execute(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found"));
  }
}
