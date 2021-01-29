package com.dynamic.contextHolder;

import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author zhangqi
 * @version 1.0
 */
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {
    private static final Logger LOG = Logger.getLogger(DynamicRoutingDataSource.class);
    @Override
    protected Object determineCurrentLookupKey() {
        LOG.info("当前数据源：{}"+ DynamicDataSourceContextHolder.get());
        return DynamicDataSourceContextHolder.get();
    }
}
