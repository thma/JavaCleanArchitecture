package c_clean_architecture.c_adapters;

import c_clean_architecture.b_usecases.Logger;
import org.slf4j.LoggerFactory;

public class Slf4jLoggerAdapter implements Logger {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger("AppLogger");

    @Override
    public void info(String message) {
        logger.info(message);
    }
}
