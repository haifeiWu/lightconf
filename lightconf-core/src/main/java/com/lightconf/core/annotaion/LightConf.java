package com.lightconf.core.annotaion;

import java.lang.annotation.*;

/**
 *light-conf annotation  (only support filed)
 * @author wuhaifei
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LightConf {

    /**
     * conf key
     *
     * @return
     */
    String value();

    /**
     * conf default value
     *
     * @return
     */
    String defaultValue() default "";

    /**
     *  whether you need a callback refresh, when the value changes.
     *
     * @return
     */
    boolean callback() default true;
}