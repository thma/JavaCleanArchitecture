package u_clean_architecture.d_adapters;

import org.slf4j.LoggerFactory;
import u_clean_architecture.c_ports.Logger;

public class Slf4jLoggerAdapter implements Logger {

  private final org.slf4j.Logger logger = LoggerFactory.getLogger("AppLogger");

  @Override
  public void info(String message) {
    logger.info(message);
  }
}
