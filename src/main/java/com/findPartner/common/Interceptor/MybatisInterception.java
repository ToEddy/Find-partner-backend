package com.findPartner.common.Interceptor;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 这是一个mybatis-plus的拦截器链，里面可以配置各种各样的拦截器
 * 自动分页: PaginationInnerInterceptor（最常用）
 * 多租户: TenantLineInnerInterceptor
 * 动态表名: DynamicTableNameInnerInterceptor
 * 乐观锁: OptimisticLockerInnerInterceptor
 * sql性能规范: IllegalSQLInnerInterceptor
 * 防止全表更新与删除: BlockAttackInnerInterceptor
 *
 * @return
 */
@Configuration
public class MybatisInterception {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
