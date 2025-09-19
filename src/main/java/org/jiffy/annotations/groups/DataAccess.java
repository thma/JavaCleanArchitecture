package c_clean_architecture.effects.annotations.groups;

import c_clean_architecture.effects.annotations.EffectGroup;
import c_clean_architecture.effects.definitions.OrderRepositoryEffect;
import c_clean_architecture.effects.definitions.ReturnRepositoryEffect;

import java.lang.annotation.*;

/**
 * Effect group for data access operations.
 * Includes all repository effects.
 */
@EffectGroup(
    value = {OrderRepositoryEffect.class, ReturnRepositoryEffect.class},
    name = "DataAccess",
    description = "Effects related to data persistence and retrieval"
)
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD})
@Documented
public @interface DataAccess {
}