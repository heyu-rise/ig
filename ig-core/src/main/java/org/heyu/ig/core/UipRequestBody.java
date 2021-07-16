package org.heyu.ig.core;

import java.lang.annotation.*;

/**
 * @author heyu
 */
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface UipRequestBody {

}
