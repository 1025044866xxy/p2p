//package com.xxy.p2p.dao.config;
//
//import com.zaxxer.hikari.HikariDataSource;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class MyBatisConfiguration {
//
//    @Autowired
//    ExecteSqlLogInterceptor execteSqlLogInterceptor;
//
//    @Primary
//    @ConfigurationProperties(prefix = "spring.datasource.p2p")
//    public DataSource skeDataDataSource() {
//        return DataSourceBuilder.create().type(HikariDataSource.class).build();
//    }
//
//    @Bean
//    public SqlSessionFactory sqlSessionFactory() throws Exception {
//        final SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
//        sqlSessionFactoryBean.setDataSource(skeDataDataSource());
//        PathMatchingResourcePatternResolver pathMatchResolver = new PathMatchingResourcePatternResolver();
//        sqlSessionFactoryBean.setMapperLocations(
//                pathMatchResolver.getResources("classpath:com/xxy/p2p/dao/mapping/*-sql-mapping.xml"));
//        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
//        configuration.setMapUnderscoreToCamelCase(true);
//        configuration.addInterceptor(execteSqlLogInterceptor);
//        sqlSessionFactoryBean.setConfiguration(configuration);
//        return sqlSessionFactoryBean.getObject();
//    }
//}