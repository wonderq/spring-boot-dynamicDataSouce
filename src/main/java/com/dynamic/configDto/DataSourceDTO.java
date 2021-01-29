package com.dynamic.configDto;

/**
 * @author: zhangqi
 * @create: 2018-01-29 09:23
 **/
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * description:
 *
 * @author zhangqi
 */
@Component
@ConfigurationProperties(prefix = "multiple")
public class DataSourceDTO {
    /**
     * 默认数据源
     */
    private String defaultDataSource;
    /**
     * 接收所有数据源属性值
     */
    private Map<String, DataSourcePropertiesDTO> dataSource = new HashMap<>();

    public Map<String, DataSourcePropertiesDTO> getDataSource() {
        return dataSource;
    }

    public void setDataSource(Map<String, DataSourcePropertiesDTO> dataSource) {
        this.dataSource = dataSource;
    }

    public String getDefaultDataSource() {
        return defaultDataSource;
    }

    public void setDefaultDataSource(String defaultDataSource) {
        this.defaultDataSource = defaultDataSource;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, DataSourcePropertiesDTO> entry : getDataSource().entrySet()) {
            result.append("数据源名 = " + entry.getKey())
                    .append("驱动类 = " + entry.getValue().getDriverClassName())
                    .append("用户名 = " + entry.getValue().getUsername())
                    .append("密码 = " + entry.getValue().getPassword())
                    .append("连接串 = " + entry.getValue().getUrl());
        }
        return result.toString();
    }
}