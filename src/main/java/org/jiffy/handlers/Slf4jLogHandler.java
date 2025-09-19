package org.jiffy.handlers;

import org.jiffy.core.EffectHandler;
import org.jiffy.definitions.LogEffect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Production implementation of LogEffect handler using SLF4J.
 */
@Component
public class Slf4jLogHandler implements EffectHandler<LogEffect> {

    private static final Logger logger = LoggerFactory.getLogger("Application");

    @Override
    @SuppressWarnings("unchecked")
    public <T> T handle(LogEffect effect) {
        if (effect instanceof LogEffect.Info info) {
            logger.info(info.message());
        } else if (effect instanceof LogEffect.Error error) {
            if (error.error() != null) {
                logger.error(error.message(), error.error());
            } else {
                logger.error(error.message());
            }
        } else if (effect instanceof LogEffect.Warning warning) {
            logger.warn(warning.message());
        } else if (effect instanceof LogEffect.Debug debug) {
            logger.debug(debug.message());
        }
        return null; // LogEffect always returns Void
    }
}