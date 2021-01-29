package com.dynamic.config;

import com.dynamic.contextHolder.DynamicRoutingDataSource;
import com.alibaba.druid.pool.DruidDataSource;
import com.dynamic.configDto.DataSourceDTO;
import com.dynamic.configDto.DataSourcePropertiesDTO;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 */
@MapperScan(basePackages = "com.dynamic.dao")
@Configuration
public class DynamicDataSourceConfiguration {
    Logger logger = Logger.getLogger(DynamicDataSourceConfiguration.class);
    @Autowired
    DataSourceDTO dataSourceDTO;
    /**
     * 核心动态数据源
     *
     * @return 数据源实例
     */
    @Bean
    public DataSource dynamicDataSource() {
        DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource();
        logger.info(dataSourceDTO.toString());
        Map<String, DataSourcePropertiesDTO> dsMap = dataSourceDTO.getDataSource();
        DruidDataSource defaultDs = new DruidDataSource();
        Map<Object, Object> dataSourceMap = new HashMap<>();

        if (null!=dsMap.get(dataSourceDTO.getDefaultDataSource())) {
            defaultDs.setDriverClassName(dsMap.get(dataSourceDTO.getDefaultDataSource()).getDriverClassName());
            defaultDs.setUrl(dsMap.get(dataSourceDTO.getDefaultDataSource()).getUrl());
            defaultDs.setUsername(dsMap.get(dataSourceDTO.getDefaultDataSource()).getUsername());
            defaultDs.setPassword(dsMap.get(dataSourceDTO.getDefaultDataSource()).getPassword());
            logger.info("当前设置默认数据源key为： "+dataSourceDTO.getDefaultDataSource());
            dataSource.setDefaultTargetDataSource(defaultDs);
        }
        for (String dataSourceKey:dsMap.keySet()){
            logger.info("数据源key有： "+ dataSourceKey);
            DruidDataSource tempDs = new DruidDataSource();
            tempDs.setDriverClassName(dsMap.get(dataSourceKey).getDriverClassName());
            tempDs.setUrl(dsMap.get(dataSourceKey).getUrl());
            tempDs.setUsername(dsMap.get(dataSourceKey).getUsername());
            tempDs.setPassword(dsMap.get(dataSourceKey).getPassword());
            dataSourceMap.put(dataSourceKey,tempDs);
        }
        dataSource.setTargetDataSources(dataSourceMap);
        return dataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dynamicDataSource());
        //此处设置为了解决找不到mapper文件的问题
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory());
    }

    /**
     * 事务管理
     *
     * @return 事务管理实例
     */
    @Bean
    public PlatformTransactionManager platformTransactionManager() {
        return new DataSourceTransactionManager(dynamicDataSource());
    }
}