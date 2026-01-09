package pe.interseguro.siv.common.config;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages="pe.interseguro.siv.common.persistence.db.postgres.repository", sqlSessionFactoryRef = "sqlSessionFactoryPostgres")
public class MyBatisPostgresConfig {
	@Value("#{ environment['persistence.db.postgres.driverClass'] }")
	private String driverClass;
    
    @Value("#{ environment['persistence.db.postgres.url'] }")
	private String url;

    @Value("#{ environment['persistence.db.postgres.username'] }")
	private String username;

    @Value("#{ environment['persistence.db.postgres.password'] }")
	private String password;

    @Value("#{ environment['persistence.db.postgres.domain'] }")
	private String domain;

    @Bean("dataSourcePostgres")
	public DataSource dataSourcePostgres() {
		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setDriverClassName(this.driverClass);
		dataSource.setJdbcUrl(this.url);
		dataSource.setUsername(this.username);
		dataSource.setPassword(this.password);
        //dataSource.setConnectionTimeout(20000);
		// Configuración personalizada de HikariCP para PostgreSQL
		dataSource.setMaximumPoolSize(5);
		dataSource.setMinimumIdle(5);
		dataSource.setIdleTimeout(300000); // Tiempo de espera para las conexiones inactivas, en milisegundos
		dataSource.setMaxLifetime(600000); // Tiempo de vida máximo de una conexión, en milisegundos

		return dataSource;		
	}
	
    @Bean("transactionManagerPostgres")
	public PlatformTransactionManager transactionManagerPostgres() {
		return new DataSourceTransactionManager(this.dataSourcePostgres());
	}

    @Bean("sqlSessionFactoryPostgres")
	public SqlSessionFactoryBean sqlSessionFactoryPostgres() throws Exception {
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(this.dataSourcePostgres());
		sessionFactory.setTypeAliasesPackage(this.domain);
		return sessionFactory;
	}
}