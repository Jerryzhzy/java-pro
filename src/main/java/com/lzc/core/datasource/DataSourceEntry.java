package com.lzc.core.datasource;

/**
 * Created by ziyu.zhang on 2017/6/30.
 * Description 请用一句话描述
 */
public final class DataSourceEntry {
    private DataSourceEntry(){}


    public enum SourceType {
        MASTER, SLAVE
    }

    public enum QueryType {
        total, data
    }
}
