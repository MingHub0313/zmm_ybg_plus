package com.ybg.config;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.ybg.base.jdbc.DataBaseConstant;

/**
 * * @author Deament
 * 
 * @date 2017/1/1<br>
 *       数据源配置， DataSource 返回的数据源 @Primary 表示 如果不适用@Qualifier 注解时候，则使用默认<br>
 *       JdbcTemplate 是springjdbc 配置，一般是，一个数据源配置一个JdbcTemplate 模板，<br>
 *       通过@Bean(name = DataBaseConstant.DB_QUARTZ)的方式指定数据源
 **/
@Configuration
public class DataBaseConfiguration {
    
    @Primary
    @Bean(name = DataBaseConstant.DB_SYS)
    @ConfigurationProperties("spring.datasource.druid.sys")
    public DataSource dataSourceSys(){
        return DruidDataSourceBuilder.create().build();
    }
    
    @Bean(name = DataBaseConstant.DB_QUARTZ)
    @ConfigurationProperties("spring.datasource.druid.quartz")
    public DataSource dataSourceQuartz(){
        return DruidDataSourceBuilder.create().build();
    }
    
    @Bean(name = DataBaseConstant.DB_REPORT)
    @ConfigurationProperties("spring.datasource.druid.report")
    public DataSource dataSourceReport(){
        return DruidDataSourceBuilder.create().build();
    }
    
    @Primary
    @Bean(name = DataBaseConstant.JD_SYS)
    public JdbcTemplate sysJdbcTemplate(@Qualifier(DataBaseConstant.DB_SYS) DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }
    
    @Bean(name = DataBaseConstant.JD_QUARTZ)
    public JdbcTemplate quertzJdbcTemplate(@Qualifier(DataBaseConstant.DB_QUARTZ) DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }
    
    @Bean(name = DataBaseConstant.JD_REPORT)
    public JdbcTemplate reportJdbcTemplate(@Qualifier(DataBaseConstant.DB_REPORT) DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }
    
    // 事务管理器
    @Bean(name = DataBaseConstant.TM_SYS)
    @Primary
    public PlatformTransactionManager sysTransactionManager(@Qualifier(DataBaseConstant.DB_SYS) DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }
    
    // 事务管理器
    @Bean(name = DataBaseConstant.TM_QUARTZ)
    public PlatformTransactionManager quartzTransactionManager(@Qualifier(DataBaseConstant.DB_QUARTZ) DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }
    
    // 事务管理器
    @Bean(name = DataBaseConstant.TM_REPORT)
    public PlatformTransactionManager reportTransactionManager(@Qualifier(DataBaseConstant.DB_REPORT) DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }
}
