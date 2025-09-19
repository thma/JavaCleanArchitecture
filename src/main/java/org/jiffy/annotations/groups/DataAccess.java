package org.jiffy.annotations.groups;

import org.jiffy.annotations.EffectGroup;
import org.jiffy.definitions.OrderRepositoryEffect;
import org.jiffy.definitions.ReturnRepositoryEffect;

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