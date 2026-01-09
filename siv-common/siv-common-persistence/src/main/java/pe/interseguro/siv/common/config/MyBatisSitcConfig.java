package pe.interseguro.siv.common.config;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
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
		  entityManagerFactoryRef = "sitcEntityManagerFactory",
		  transactionManagerRef = "sitcTransactionManager",
		  basePackages = { "pe.interseguro.siv.common.persistence.db.sitc.repository" }
		)
@MapperScan(basePackages="pe.interseguro.siv.common.persistence.db.sitc.repository", sqlSessionFactoryRef = "sitcsqlSessionFactory")
public class MyBatisSitcConfig {
	@Value("#{ environment['persistence.db.sitc.driverClass'] }")
	private String driverClass;

    @Value("#{ environment['persistence.db.sitc.url'] }")
	private String url;

    @Value("#{ environment['persistence.db.sitc.username'] }")
	private String username;

    @Value("#{ environment['persistence.db.sitc.password'] }")
	private String password;

    @Value("#{ environment['persistence.db.sitc.domain'] }")
	private String domain;
	
	
    @Bean(name = "sitcDataSource")
    public DataSource sitcDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(this.driverClass);
        dataSource.setJdbcUrl(this.url);
        dataSource.setUsername(this.username);
        dataSource.setPassword(this.password);
        //dataSource.setConnectionTimeout(20000);
        return dataSource;
    }
    
    @Bean(name = "sitcTransactionManager")
    public PlatformTransactionManager sitcTransactionManager() {
        return new DataSourceTransactionManager(this.sitcDataSource());
    }

    @Bean(name = "sitcsqlSessionFactory")
    public SqlSessionFactoryBean sitcsqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(this.sitcDataSource());
        sessionFactory.setTypeAliasesPackage(this.domain);
        return sessionFactory;
    }
}
