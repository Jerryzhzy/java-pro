package com.lzc.core.datasource.annotation;

import java.lang.annotation.*;

/**
 * Created by ziyu.zhang on 2017/6/30 22:08
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface ValidateFormSubmission {
	boolean returnMsg() default true;
	boolean validate() default true; 
}
