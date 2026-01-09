/**
 * 
 */
package pe.interseguro.siv.common.persistence.rest.base;

import java.util.Map;

/**
 * @author ti-is
 *
 */
public interface BaseRestClient {

	/**
	 * Obtener method GET
	 * @param url
	 * @param claseRespuesta
	 * @return
	 */
	public <T> T obtenerGetPorObjeto(String url, Class<T> claseRespuesta);
	
	/**
	 * Obtener method GET con parametros
	 * @param url
	 * @param claseRespuesta
	 * @param parametros
	 * @return
	 */
	public <T> T obtenerGetPorObjeto(String url, Class<T> claseRespuesta, Map<String, String> parametros);

	/**
	 * Obtener method GET con Object parametro
	 * @param url
	 * @param claseRespuesta
	 * @param parametros
	 * @return
	 */
	public <T> T obtenerGetPorObjeto(String url, Class<T> claseRespuesta, Object request);

	/**
	 * Obtener method POST 
	 * @param url
	 * @param request
	 * @param claseRespuesta
	 * @return
	 */
	public <T> T obtenerPostPorObjeto(String url, Object request, Class<T> claseRespuesta);
	
	/**
	 * Obtener method PUT
	 * @param url
	 * @param request
	 */
	public void obtenerPut(String url, Object request);
	
	/**
	 * Obtener method DELETE
	 * @param url
	 * @param parametros
	 */
	public void obtenerDelete(String url, Map<String, Object> parametros);
	

}
