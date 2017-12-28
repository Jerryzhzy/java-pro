package com.lzc.core.datasource.annotation;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface AvoidDuplicateSubmission {
	boolean needSaveToken() default false;
	boolean needRemoveToken() default false;

}
