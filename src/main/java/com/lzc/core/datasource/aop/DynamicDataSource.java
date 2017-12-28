package com.lzc.core.datasource.aop;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Created by ziyu.zhang on 2017/6/30 22:08
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

	/* (non-Javadoc)
	* <p>Title: determineCurrentLookupKey</p>
	* <p>Description: 决定要使用的数据源</p>
	* @return
	*/
	@Override
	protected Object determineCurrentLookupKey() {
		return DataSourceSwitcher.getDataSource();
	}

}
