package pe.interseguro.siv.common.config;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		  entityManagerFactoryRef = "crmEntityManagerFactory",
		  transactionManagerRef = "crmTransactionManager",
		  basePackages = { "pe.interseguro.siv.common.persistence.db.crm.repository" }
		)
@MapperScan(basePackages="pe.interseguro.siv.common.persistence.db.crm.repository", sqlSessionFactoryRef = "crmsqlSessionFactory")

public class MyBatisCrmConfig {
	@Value("#{ environment['persistence.db.crm.driverClass'] }")
	private String driverClass;

    @Value("#{ environment['persistence.db.crm.url'] }")
	private String url;

    @Value("#{ environment['persistence.db.crm.username'] }")
	private String username;

    @Value("#{ environment['persistence.db.crm.password'] }")
	private String password;

    @Value("#{ environment['persistence.db.crm.domain'] }")
	private String domain;
	
	
    @Bean(name = "crmDataSource")
    public DataSource crmDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(this.driverClass);
        dataSource.setJdbcUrl(this.url);
        dataSource.setUsername(this.username);
        dataSource.setPassword(this.password);
        //dataSource.setConnectionTimeout(20000);
        return dataSource;
    }
    
    @Bean(name = "crmTransactionManager")
    public PlatformTransactionManager crmTransactionManager() {
        return new DataSourceTransactionManager(this.crmDataSource());
    }

    @Bean(name = "crmsqlSessionFactory")
    public SqlSessionFactoryBean sitcsqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(this.crmDataSource());
        sessionFactory.setTypeAliasesPackage(this.domain);
        return sessionFactory;
    }
}
