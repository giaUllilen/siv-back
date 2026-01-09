package pe.interseguro.siv.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.Executor;

@ComponentScan(basePackages = { "pe.interseguro.siv.common", "pe.interseguro.siv.admin" })
@SpringBootApplication
@EnableAsync
@EnableAutoConfiguration
public class SivAdminApplication {

	@Bean(name = "processExecutor")
	public TaskExecutor workExecutor() {
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setCorePoolSize(3);
		threadPoolTaskExecutor.setMaxPoolSize(6);
		threadPoolTaskExecutor.setQueueCapacity(600);
		threadPoolTaskExecutor.afterPropertiesSet();
		threadPoolTaskExecutor.setThreadNamePrefix("AsyncThread-");
		return threadPoolTaskExecutor;
	}

	@Bean(name = "processExecutor2")
	public TaskExecutor workExecutorCopy() {
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setCorePoolSize(3);
		threadPoolTaskExecutor.setMaxPoolSize(6);
		threadPoolTaskExecutor.setQueueCapacity(600);
		threadPoolTaskExecutor.afterPropertiesSet();
		threadPoolTaskExecutor.setThreadNamePrefix("AsyncThread-");
		return threadPoolTaskExecutor;
	}

	@Bean(name = "processExecutorReproceso")
	public TaskExecutor workExecutorReproceso() {
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setCorePoolSize(3);
		threadPoolTaskExecutor.setMaxPoolSize(6);
		threadPoolTaskExecutor.setQueueCapacity(600);
		threadPoolTaskExecutor.afterPropertiesSet();
		threadPoolTaskExecutor.setThreadNamePrefix("AsyncThread-");
		return threadPoolTaskExecutor;
	}

	@Bean(name = "processExecutorEmision")
	public TaskExecutor workExecutorEmision() {
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setCorePoolSize(3);
		threadPoolTaskExecutor.setMaxPoolSize(6);
		threadPoolTaskExecutor.setQueueCapacity(600);
		threadPoolTaskExecutor.afterPropertiesSet();
		threadPoolTaskExecutor.setThreadNamePrefix("AsyncThread-");
		return threadPoolTaskExecutor;
	}

	@Bean(name = "processExecutorComunicaciones")
	public TaskExecutor workExecutorComunicaciones() {
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setCorePoolSize(3);
		threadPoolTaskExecutor.setMaxPoolSize(6);
		threadPoolTaskExecutor.setQueueCapacity(600);
		threadPoolTaskExecutor.afterPropertiesSet();
		threadPoolTaskExecutor.setThreadNamePrefix("AsyncThread-");
		return threadPoolTaskExecutor;
	}
	@Bean(name = "threadPoolTaskExecutorFileDelete")
	public Executor threadPoolTaskExecutor() {
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setCorePoolSize(3);
		threadPoolTaskExecutor.setMaxPoolSize(6);
		threadPoolTaskExecutor.setQueueCapacity(600);
		threadPoolTaskExecutor.afterPropertiesSet();
		threadPoolTaskExecutor.setThreadNamePrefix("TaskExecutorFileDelete-");
		threadPoolTaskExecutor.initialize();
		return threadPoolTaskExecutor;
	}
	public static void main(String[] args) {
		//TimeZone.setDefault(TimeZone.getTimeZone("GMT-5:00"));
		SpringApplication.run(SivAdminApplication.class, args);
	}

}
