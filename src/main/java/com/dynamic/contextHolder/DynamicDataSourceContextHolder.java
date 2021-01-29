package com.dynamic.contextHolder;

import org.apache.log4j.Logger;

/**
 * @author zhangqi
 * @version 1.0
 */
public class DynamicDataSourceContextHolder {
    private static final Logger LOG = Logger.getLogger(DynamicDataSourceContextHolder.class);

    private static final ThreadLocal<String> currentDataSource = new ThreadLocal<>();

    /**
     * 清除当前数据源
     */
    public static void clear() {
        currentDataSource.remove();
    }

    /**
     * 获取当前使用的数据源
     *
     * @return 当前使用数据源的ID
     */
    public static String get() {
        return currentDataSource.get();
    }

    /**
     * 设置当前使用的数据源
     *
     * @param value 需要设置的数据源ID
     */
    public static void set(String value) {
        currentDataSource.set(value);
    }

}

