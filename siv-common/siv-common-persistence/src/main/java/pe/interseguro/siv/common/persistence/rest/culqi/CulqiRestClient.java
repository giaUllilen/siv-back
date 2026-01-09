package pe.interseguro.siv.common.persistence.rest.culqi;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.google.gson.Gson;

import pe.interseguro.siv.common.exception.SivSOAException;
import pe.interseguro.siv.common.persistence.rest.base.BaseRestClientImpl;
import pe.interseguro.siv.common.persistence.rest.culqui.request.CulqiCargoRequest;
import pe.interseguro.siv.common.persistence.rest.culqui.request.CulqiTokenRequest;
import pe.interseguro.siv.common.persistence.rest.culqui.response.CulqiCargoResponse;
import pe.interseguro.siv.common.persistence.rest.culqui.response.CulqiTokenResponse;
import pe.interseguro.siv.common.persistence.rest.indenova.request.CrearCircuitoRequest;
import pe.interseguro.siv.common.util.Constantes;
import pe.interseguro.siv.common.util.Utilitarios;

@Component
public class CulqiRestClient extends BaseRestClientImpl{
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MessageSource messageSource;

	@Value("#{ environment['persistence.rest.client.culqi.baseSecureUrl'] }")
    private String baseSecureUrl;
	
	@Value("#{ environment['persistence.rest.client.culqi.baseOperationUrl'] }")
    private String baseOperationUrl;
	
	@Value("#{ environment['persistence.rest.client.culqi.tokenizar'] }")
    private String tokenizar;
	
	@Value("#{ environment['persistence.rest.client.culqi.cargo'] }")
    private String cargo;
	
    @Value("#{ environment['persistence.rest.client.culqi.publicKey'] }")
    private String culqiPublicKey;
    
    @Value("#{ environment['persistence.rest.client.culqi.secretKey'] }")
    private String culqiSecretKey;
    
    private String getUrlCulqiTokenizar() {
		return baseSecureUrl+tokenizar;
	}
    
    private String getUrlCulqiCargo() {
		return baseOperationUrl+cargo;
	}
    
    private Gson gson = new Gson();
    
    /**
     * 
     * @param request
     * @return
     */
	public CulqiTokenResponse tokenizarTarjeta(CulqiTokenRequest request) {
		LOGGER.info("Entro CulqiRestClient#tokenizarTarjeta(request)");
    	
		CulqiTokenResponse response = new CulqiTokenResponse();
    	
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer ".concat(culqiPublicKey) );
		 
		try {
			response = obtenerPostPorObjeto(getUrlCulqiTokenizar(), new HttpEntity<CulqiTokenRequest>(request, headers), CulqiTokenResponse.class);
			 
		} catch (HttpClientErrorException errorClient) {
			response.setCodigo(Constantes.CODIGO_RESPUESTA_GENERAL_CULQI_ERROR_400);
			response.setMensajeError(errorClient.getResponseBodyAsString());
			response.setErrorHttp(String.valueOf(errorClient.getRawStatusCode()));
		} catch (HttpServerErrorException errorServer) {
			response.setCodigo(Constantes.CODIGO_RESPUESTA_GENERAL_CULQI_ERROR_500);
			response.setErrorHttp(String.valueOf(errorServer.getRawStatusCode()));
			response.setMensajeError(errorServer.getResponseBodyAsString());
		} catch (Exception e) {
			response.setCodigo(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			response.setMensajeError("Error desconocido");
		}
    	
		LOGGER.info("Salio CulqiRestClient#tokenizarTarjeta(request)");
    	return response;
    }
	
	public CulqiCargoResponse crearCargo(CulqiCargoRequest request) {
		LOGGER.info("Entro CulqiRestClient#crearCargo(request)");
    	
		CulqiCargoResponse response = new CulqiCargoResponse();
    	
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer ".concat(culqiSecretKey) );
		
		try {
			response = obtenerPostPorObjeto(getUrlCulqiCargo(), new HttpEntity<CulqiCargoRequest>(request, headers), CulqiCargoResponse.class);
		
		} catch (HttpClientErrorException errorClient) {
			response.setCodigo(Constantes.CODIGO_RESPUESTA_GENERAL_CULQI_ERROR_400);
			response.setMensajeError(errorClient.getResponseBodyAsString());
			response.setErrorHttp(String.valueOf(errorClient.getRawStatusCode()));
		} catch (HttpServerErrorException errorServer) {
			response.setCodigo(Constantes.CODIGO_RESPUESTA_GENERAL_CULQI_ERROR_500);
			response.setErrorHttp(String.valueOf(errorServer.getRawStatusCode()));
			response.setMensajeError(errorServer.getResponseBodyAsString());
		} catch (Exception e) {
			response.setCodigo(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			response.setMensajeError("Error desconocido");
		}
    	
		LOGGER.info("Salio CulqiRestClient#crearCargo(request)");
    	return response;
	}
}
