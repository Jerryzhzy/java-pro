package com.lzc.core.datasource.annotation;

import com.lzc.core.datasource.DataSourceEntry;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * 数据源注解
 * Created by ziyu.zhang on 2017/6/30 19:39
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface DataSource {

	DataSourceEntry.SourceType value() default DataSourceEntry.SourceType.MASTER;

}
