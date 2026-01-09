/**
 * 
 */
package pe.interseguro.siv.common.persistence.rest.base;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author ti-is
 *
 */
@Component
public class BaseRestClientImpl implements BaseRestClient {

	@Autowired
	@Qualifier("restTemplatePesado")
	private RestTemplate restTemplate;

	
	/**
	 * Retorna el template rest client para usar en casos genericos 
	 * @return
	 */
	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	/* (non-Javadoc)
	 * @see pe.com.interseguro.travel.api.persistence.rest.base.BaseRestClient#obtenerGetPorObjeto(java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> T obtenerGetPorObjeto(String url, Class<T> claseRespuesta) {
		return restTemplate.getForObject(url, claseRespuesta);
	}

	/* (non-Javadoc)
	 * @see pe.com.interseguro.travel.api.persistence.rest.base.BaseRestClient#obtenerGetPorObjeto(java.lang.String, java.lang.Class, java.util.Map)
	 */
	@Override
	public <T> T obtenerGetPorObjeto(String url, Class<T> claseRespuesta, Map<String, String> parametros) {
		return restTemplate.getForObject(url, claseRespuesta, parametros);
	}

	/* (non-Javadoc)
	 * @see pe.com.interseguro.travel.api.persistence.rest.base.BaseRestClient#obtenerPostPorObjeto(java.lang.String, java.lang.Object, java.lang.Class)
	 */
	@Override
	public <T> T obtenerPostPorObjeto(String url, Object request, Class<T> claseRespuesta) {
		return restTemplate.postForObject(url, request, claseRespuesta);
	}

	/* (non-Javadoc)
	 * @see pe.com.interseguro.travel.api.persistence.rest.base.BaseRestClient#obtenerPut(java.lang.String, java.lang.Object)
	 */
	@Override
	public void obtenerPut(String url, Object request) {
		restTemplate.put(url, request);
	}

	/* (non-Javadoc)
	 * @see pe.com.interseguro.travel.api.persistence.rest.base.BaseRestClient#obtenerDelete(java.lang.String, java.util.Map)
	 */
	@Override
	public void obtenerDelete(String url, Map<String, Object> parametros) {
		restTemplate.delete(url, parametros);
	}

	@Override
	public <T> T obtenerGetPorObjeto(String url, Class<T> claseRespuesta, Object request) {
		return restTemplate.getForObject(url, claseRespuesta, request);
	}
	
	
}
