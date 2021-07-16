package org.heyu.ig.core;

import org.springframework.http.HttpMethod;

import java.lang.annotation.*;

/**
 * @author shy19
 */
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface UipRequestUrl {

    HttpMethod value() default HttpMethod.GET;

}
