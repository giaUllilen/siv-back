package pe.interseguro.siv.common.config;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		  entityManagerFactoryRef = "barEntityManagerFactory",
		  transactionManagerRef = "barTransactionManager",
		  basePackages = { "pe.interseguro.siv.common.persistence.db.acsele.repository" }
		)
@MapperScan(basePackages="pe.interseguro.siv.common.persistence.db.acsele.repository", sqlSessionFactoryRef = "barsqlSessionFactory")
public class MybatisConfig {
	@Value("#{ environment['persistence.db.acsele.driverClass'] }")
	private String driverClass;

    @Value("#{ environment['persistence.db.acsele.url'] }")
	private String url;

    @Value("#{ environment['persistence.db.acsele.username'] }")
	private String username;

    @Value("#{ environment['persistence.db.acsele.password'] }")
	private String password;

    @Value("#{ environment['persistence.db.acsele.domain'] }")
	private String domain;
	
	
    @Bean(name = "barDataSource")
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(this.driverClass);
        dataSource.setJdbcUrl(this.url);
        dataSource.setUsername(this.username);
        dataSource.setPassword(this.password);
        //dataSource.setConnectionTimeout(20000);
        return dataSource;
    }
    
    @Bean(name = "barTransactionManager")
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(this.dataSource());
    }

    @Primary
    @Bean(name = "barsqlSessionFactory")
    public SqlSessionFactoryBean sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(this.dataSource());
        sessionFactory.setTypeAliasesPackage(this.domain);
        return sessionFactory;
    }
}