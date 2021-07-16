package org.heyu.ig.core;

import java.lang.annotation.*;

/**
 * @author heyu
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface UipRequestLog {

    String code();

}
