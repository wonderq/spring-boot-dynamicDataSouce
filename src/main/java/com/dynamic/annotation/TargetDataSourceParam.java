package com.dynamic.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * description: 用于切换动态数据源的参数注解
 *
 * @author zhangqi
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSourceParam {
    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";

    boolean required() default false;

    String defaultValue() default "";
}

