package pe.interseguro.siv.common.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(	basePackages="pe.interseguro.siv.common.persistence.db.mysql.repository",
						entityManagerFactoryRef = "entityManagerFactory",
						transactionManagerRef = "transactionManager")
public class JpaConfig {

    @Value("#{ environment['persistence.db.driverClass'] }")
	private String driverClass;

    @Value("#{ environment['persistence.db.url'] }")
	private String url;

    @Value("#{ environment['persistence.db.username'] }")
	private String username;

    @Value("#{ environment['persistence.db.password'] }")
	private String password;

    @Value("#{ environment['persistence.db.domain'] }")
	private String domain;

    @Value("#{ environment['persistence.db.dialect'] }")
	private String dialect;

    @Value("#{ environment['persistence.db.show_sql'] }")
	private String show_sql;
    
    
    @Primary
	@Bean
	public DataSource dataSource() {
		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setDriverClassName(this.driverClass);
		dataSource.setJdbcUrl(this.url);
		dataSource.setUsername(this.username);
		dataSource.setPassword(this.password);
		return dataSource;		
	}

    @Primary
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(this.dataSource());
		em.setPackagesToScan(new String[] { this.domain });

		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
				
		Properties properties = new Properties();
		properties.setProperty("hibernate.dialect", this.dialect);
		properties.setProperty("hibernate.show_sql", this.show_sql);
//		properties.setProperty("hibernate.generate_statistics", "true");
		
		em.setJpaProperties(properties);

		return em;
	}
	
    @Primary
	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);
		return transactionManager;
	}
	
	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	
}
