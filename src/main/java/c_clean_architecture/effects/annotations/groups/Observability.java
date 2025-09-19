package c_clean_architecture.effects.annotations.groups;

import c_clean_architecture.effects.annotations.EffectGroup;
import c_clean_architecture.effects.definitions.LogEffect;

import java.lang.annotation.*;

/**
 * Effect group for observability-related effects.
 * Includes logging and metrics (when available).
 */
@EffectGroup(
    value = {LogEffect.class},
    name = "Observability",
    description = "Effects related to observability: logging, metrics, tracing"
)
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD})
@Documented
public @interface Observability {
}