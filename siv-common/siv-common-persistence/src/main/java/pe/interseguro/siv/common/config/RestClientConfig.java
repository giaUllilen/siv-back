package pe.interseguro.siv.common.config;

import java.nio.charset.Charset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestClientConfig {

    @Value("#{ environment['persistence.rest.client.encoding'] }")
	private String encoding;

    @Value("#{ environment['persistence.rest.client.timeout.wait.pesado'] }")
	private String timeoutWaitPesado;

    @Value("#{ environment['persistence.rest.client.timeout.connection.pesado'] }")
	private String timeoutConnectionPesado;

    @Value("#{ environment['persistence.rest.client.timeout.wait.ligero'] }")
	private String timeoutWaitLigero;

    @Value("#{ environment['persistence.rest.client.timeout.connection.ligero'] }")
	private String timeoutConnectionLigero;

	
	@Bean(name = "restTemplatePesado")
	public RestTemplate restTemplatePesado() {
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		clientHttpRequestFactory.setReadTimeout( Integer.valueOf(this.timeoutWaitPesado) );
		clientHttpRequestFactory.setConnectTimeout( Integer.valueOf(this.timeoutConnectionPesado) );
		
		RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
		restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter( Charset.forName(this.encoding) ) );
		return restTemplate;
	}

	@Bean(name = "restTemplateLigero")
	public RestTemplate restTemplateLigero() {
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		clientHttpRequestFactory.setReadTimeout( Integer.valueOf(this.timeoutWaitLigero) );
		clientHttpRequestFactory.setConnectTimeout( Integer.valueOf(this.timeoutConnectionLigero) );
		
		RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
		restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter( Charset.forName(this.encoding) ) );
		return restTemplate;
	}
	

}
