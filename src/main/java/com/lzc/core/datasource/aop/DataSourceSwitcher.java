package com.lzc.core.datasource.aop;

import org.springframework.util.Assert;

/**
 * Created by ziyu.zhang on 2017/6/30 22:08
 */
public class DataSourceSwitcher {
	
	/**
	 * @Fields contextHolder : 数据源切换线程对象声明并初始化
	 */
	@SuppressWarnings("rawtypes")
	private static final ThreadLocal contextHolder = new ThreadLocal();
	
	/**
	 * @Description: 切换数据源操作方法
	 */
	@SuppressWarnings("unchecked")
	public static void setDataSource(String dataSource) {
		Assert.notNull(dataSource, "dataSource cannot be null");
		contextHolder.set(dataSource);
	}

	/**
	 * @Description:  切换至主数据源  
	 */
	public static void setMaster() {
		clearDataSource();
	}

	/**
	 * @Description: 切换至从数据源   
	 */
	public static void setSlave() {
		setDataSource("slave");
	}

	/**
	 * @Description: 获取当前使用的数据源
	 */
	public static String getDataSource() {
		return (String) contextHolder.get();
	}
	
	/**
	 * @Description: 清除设置的数据源，使用默认数据源   
	 */
	public static void clearDataSource() {
		contextHolder.remove();
	}
}
