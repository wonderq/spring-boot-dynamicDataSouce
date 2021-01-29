package com.dynamic.aop;

import com.dynamic.contextHolder.DynamicDataSourceContextHolder;
import com.dynamic.annotation.TargetDataSource;
import com.dynamic.configDto.DataSourceDTO;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

/**
 * @author zhangqi
 * @version 1.0
 */
@Aspect
@Order(-1)
@Component
public class DynamicDataSourceAspect {
    @Autowired
    private DataSourceDTO dataSourceDTO;

    private static final Logger LOG = Logger.getLogger(DynamicDataSourceAspect.class);

    @Pointcut(value = "execution(* com.dynamic.service.*.*(..))")
    public void pointCut() {
    }

    /**
     * 执行方法前更换数据源
     */
    @Before(value = "pointCut()")
    public void doBefore(JoinPoint joinPoint) throws ClassNotFoundException {
        LOG.info("我是前置通知，我要获取参数内容了");
        String dataSourceKey;
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        //最关键的一步:通过这获取到方法的所有参数名称的字符串数组
        //获取被@targetdatasourceparam注解的参数
        Annotation[][] paramAnnotations = methodSignature.getMethod().getParameterAnnotations();
        int index = -1;
        outer:
        for (int i = 0; i < paramAnnotations.length; i++) {
            for (Annotation a2 : paramAnnotations[i]) {
                LOG.info("第" + (i + 1) + "个参数的注解类型名为：" + a2.annotationType().getSimpleName());
                if (a2.annotationType().getSimpleName().equals("TargetDataSourceParam")) {
                    index = i;
                    break outer;
                }
            }
        }
        //通过下标获取到对应的值
        Object[] params = joinPoint.getArgs();
        if (index != -1) {
            dataSourceKey = (String) params[index];
            LOG.info("前端传来的 dataSourceKey= " + dataSourceKey);
            if (dataSourceDTO.getDataSource().containsKey(dataSourceKey)) {
                LOG.info(String.format("设置数据源为  %s", dataSourceKey));
                DynamicDataSourceContextHolder.set(dataSourceKey);
            } else {
                useDefaultDataSource();
            }
        }
    }

    void useDefaultDataSource() {
        LOG.info(String.format("使用使用默认数据源  %s", "master"));
        DynamicDataSourceContextHolder.set("master");
    }

    /**
     * 执行方法后清除数据源设置
     *
     * @param joinPoint        切点
     * @param targetDataSource 动态数据源
     */
    @After("@annotation(targetDataSource)")
    public void doAfter(JoinPoint joinPoint, TargetDataSource targetDataSource) {
        LOG.info(String.format("当前数据源  %s  执行清理方法", DynamicDataSourceContextHolder.get()));
        DynamicDataSourceContextHolder.clear();
    }
}