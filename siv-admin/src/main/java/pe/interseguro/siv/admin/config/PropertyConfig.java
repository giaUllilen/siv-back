package pe.interseguro.siv.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PropertyConfig {
	
	/**
	 * Configuracion Properties por Perfil
	 * ----------------------------------------------------
	 * - En el pom.xml. Se activo los perfiles develop, uat y master
	 * - Para ejecutar la aplicación solo se tiene que ejecutar el CMD: "clean spring-boot:run -Dspring-boot.run.profiles=dev" (indicando el perfil)
	 * 
	 * 
	 * Configuracion Properties para i18n
	 * ----------------------------------------------------
	 * - Se activa por defecto el ResourceBundleMessageSource para en principio obtener los mensajes a traves de un properties
	 *   en caso se desee internacionalizar se activa los Interceptors 
	 * 
	 */
	
	
	/**
	 * Se activa el MessageResource para utilizar los mensajes a traves de un properties y no dentro del codigo
	 * @return
	 */
	@Bean(name = "messageSource")
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("messages");
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setUseCodeAsDefaultMessage(true);
		messageSource.setCacheSeconds(0);
		return messageSource;
	}
	
	/**
	 * Se activa el MessageResource para utilizar los mensajes a traves de un properties y no dentro del codigo. Para Hibernate Validator
	 * @return
	 */
	@Bean(name = "validationMessageSource")
	public ResourceBundleMessageSource validationMessageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("validationMessages");
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setUseCodeAsDefaultMessage(true);
		messageSource.setCacheSeconds(0);
		return messageSource;
	}
		
	
}
