package pe.interseguro.siv.admin.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(
	basePackages = {
		 "pe.interseguro.siv.common.persistence.db",
		"pe.interseguro.siv.common.persistence.rest",
		"pe.interseguro.siv.common.persistence.soap",
		"pe.interseguro.siv.admin.transactional"
	}

)
@PropertySource("application.properties")
@Profile("uat")
public class AdminTestConfig {

	

}