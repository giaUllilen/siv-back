package pe.interseguro.siv.common.persistence.rest.niubiz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.google.gson.Gson;

import pe.interseguro.siv.common.persistence.rest.base.BaseRestClientImpl;
import pe.interseguro.siv.common.persistence.rest.niubiz.request.FirstPayRecargoRequest;
import pe.interseguro.siv.common.persistence.rest.niubiz.request.FirstPayRequest;
import pe.interseguro.siv.common.persistence.rest.niubiz.request.SessionRequest;
import pe.interseguro.siv.common.persistence.rest.niubiz.request.TokenizerRequest;
import pe.interseguro.siv.common.persistence.rest.niubiz.response.FisrtPayResponse;
import pe.interseguro.siv.common.persistence.rest.niubiz.response.SessionResponse;
import pe.interseguro.siv.common.persistence.rest.niubiz.response.TokenizerResponse;
import pe.interseguro.siv.common.util.Constantes;

@Component
public class NiubizRestClient extends BaseRestClientImpl {
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
//	@Autowired
//	private MessageSource messageSource;

	/*@Value("#{ environment['persistence.rest.client.niubiz.baseUrl'] }")
    private String baseUrl;
	
	@Value("#{ environment['persistence.rest.client.niubiz.securityService'] }")
    private String secureService;
	
	@Value("#{ environment['persistence.rest.client.niubiz.sessionService'] }")
    private String sessionService;
	
	@Value("#{ environment['persistence.rest.client.niubiz.security.username'] }")
	private String security_username;
	
	@Value("#{ environment['persistence.rest.client.niubiz.security.password'] }")
	private String security_password;
	
	private String getUrlSecurity() {
		return baseUrl+secureService;
	}
    
    private String getUrlSession() {
		return baseUrl+sessionService;
	}
   
    
    HttpHeaders createHeaders(String username, String password){
	   return new HttpHeaders() {{
         String auth = username + ":" + password;
         byte[] encodedAuth = Base64.encodeBase64( 
            auth.getBytes(Charset.forName("US-ASCII")) );
         String authHeader = "Basic " + new String( encodedAuth );
         set( "Authorization", authHeader );
      }};
	}
    
    public NiubizTokenResponse generaToken() {
		LOGGER.info("Entro NiubizRestClient#generaToken(request)");
    	
		NiubizTokenResponse response = new NiubizTokenResponse();
		String token = "";
		String usuario = this.security_username;
		String password = this.security_password;
		try {
			token = obtenerPostPorObjeto(getUrlSecurity(), new HttpEntity<>(createHeaders(usuario, password)), String.class);
			response.setToken(token);
			response.setCodigo(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
		} catch (HttpClientErrorException errorClient) {
			response.setCodigo(Constantes.CODIGO_RESPUESTA_GENERAL_CULQI_ERROR_400);
			response.setMensajeError(errorClient.getResponseBodyAsString());
			response.setErrorHttp(errorClient.getMessage());
		} catch (HttpServerErrorException errorServer) {
			response.setCodigo(Constantes.CODIGO_RESPUESTA_GENERAL_CULQI_ERROR_500);
			response.setErrorHttp(errorServer.getMessage());
			response.setMensajeError(errorServer.getResponseBodyAsString());
		} catch (Exception e) {
			response.setCodigo(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			response.setMensajeError("Error desconocido");
		}
    	
		LOGGER.info("Salio NiubizRestClient#generaToken(request)");
    	return response;
    }
    
    public NiubizSessionResponse generarSesion() {
    	LOGGER.info("Entro NiubizRestClient#generarSesion(request)");
    	
    	NiubizSessionResponse response = new NiubizSessionResponse();
    	NiubizSessionRequest request = new NiubizSessionRequest();
    	request.setAmount(132.40);
    	request.setRecurrenceMaxAmount(1000.0);
    	NiubizSessionAntifraud antifraud = new NiubizSessionAntifraud();
    	antifraud.setClientIp("127.0.0.1");
    	NiubizSessionAntifraudMerchant merchantDefineData = new NiubizSessionAntifraudMerchant();
    	merchantDefineData.setMdd0("1");
    	merchantDefineData.setMdd1("2");
    	merchantDefineData.setMdd2("3");
    	antifraud.setMerchantDefineData(merchantDefineData);
    	request.setAntifraud(antifraud);
    	request.setChannel("web");
    	String token = this.generaToken().getToken();
    	HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", token);
		
		try {
			response = obtenerPostPorObjeto(getUrlSession(), new HttpEntity<NiubizSessionRequest>(request, headers), NiubizSessionResponse.class);
		} catch (HttpClientErrorException errorClient) {
			response.setCodigo(Constantes.CODIGO_RESPUESTA_GENERAL_CULQI_ERROR_400);
			response.setMensajeError(errorClient.getResponseBodyAsString());
			response.setErrorHttp(errorClient.getMessage());
		} catch (HttpServerErrorException errorServer) {
			response.setCodigo(Constantes.CODIGO_RESPUESTA_GENERAL_CULQI_ERROR_500);
			response.setErrorHttp(errorServer.getMessage());
			response.setMensajeError(errorServer.getResponseBodyAsString());
		} catch (Exception e) {
			response.setCodigo(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			response.setMensajeError("Error desconocido");
		}
		
		LOGGER.info("Salio NiubizRestClient#generarSesion(request)");
    	return response;
    }*/
    
	@Value("#{ environment['persistence.rest.client.interseguro.passarella.servidor'] }")
    private String baseUrl;
	
	@Value("#{ environment['persistence.rest.client.interseguro.passarella.apiKey'] }")
    private String apiKey;
	
	@Value("#{ environment['persistence.rest.client.interseguro.passarella.niubiz'] }")
    private String niubiz;
	
	@Value("#{ environment['persistence.rest.client.interseguro.passarella.sesion'] }")
	private String service_session;
	
	@Value("#{ environment['persistence.rest.client.interseguro.passarella.tokenizar'] }")
	private String service_tokenizar;
	
	@Value("#{ environment['persistence.rest.client.interseguro.passarella.primeraprima'] }")
	private String service_primeraprima;
	
	@Value("#{ environment['persistence.rest.client.interseguro.passarella.primeraprima.recargo'] }")
	private String service_primeraprimaRecargo;
	
	private String getUrlSesion() {
		return baseUrl + service_session;
	}
	
	private String getUrlTokenizar() {
		return baseUrl + service_tokenizar;
	}
	
	private String getUrlPrimeraPrima() {
		return baseUrl + service_primeraprima;
	}
	
	private String getUrlPrimeraPrimaRecargo() {
		return baseUrl + service_primeraprimaRecargo;
	}
	
	private Gson gson = new Gson(); 
	
    public SessionResponse generarSesionNiubiz(SessionRequest request) {
    	SessionResponse response = new SessionResponse();
    	HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("X-Apikey", apiKey);
		headers.set("X-Provider", niubiz);
		headers.set("Content-Type", "application/json");
		
		try {
			response = obtenerPostPorObjeto(getUrlSesion(), new HttpEntity<SessionRequest>(request, headers), SessionResponse.class);
		} catch (HttpClientErrorException errorClient) {
			response.setCode(Constantes.CODIGO_RESPUESTA_GENERAL_CULQI_ERROR_400);
			response.setMessage(errorClient.getResponseBodyAsString());
			response.setStatusHttp(errorClient.getMessage());
		} catch (HttpServerErrorException errorServer) {
			response.setCode(Constantes.CODIGO_RESPUESTA_GENERAL_CULQI_ERROR_500);
			response.setMessage(errorServer.getMessage());
			response.setStatusHttp(errorServer.getResponseBodyAsString());
		} catch (Exception e) {
			response.setCode(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			response.setMessage("Error desconocido");
		}
    	return response;
    }
    
    public TokenizerResponse tokenizarTarjetaNiubiz(TokenizerRequest request) {
    	TokenizerResponse response = new TokenizerResponse();
    	HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("X-Apikey", apiKey);
		headers.set("X-Provider", niubiz);
		headers.set("Content-Type", "application/json");
		
		try {
			response = obtenerPostPorObjeto(getUrlTokenizar(), new HttpEntity<TokenizerRequest>(request, headers), TokenizerResponse.class);
		} catch (HttpClientErrorException errorClient) {
			response.setCode(Constantes.CODIGO_RESPUESTA_GENERAL_CULQI_ERROR_400);
			errorClient.getStackTrace();
			response.setMessage(errorClient.getResponseBodyAsString());
			response.setStatusHttp(errorClient.getMessage());
		} catch (HttpServerErrorException errorServer) {
			response.setCode(Constantes.CODIGO_RESPUESTA_GENERAL_CULQI_ERROR_500);
			errorServer.getStackTrace();
			response.setMessage(errorServer.getMessage());
			response.setStatusHttp(errorServer.getResponseBodyAsString());
		} catch (Exception e) {
			e.getStackTrace();
			System.out.println(e.getMessage());
			response.setCode(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			response.setMessage("Error desconocido");
		}
    	return response;
    }
    
    public FisrtPayResponse pagarPrimeraPrimaNiubiz(FirstPayRequest request) {
    	LOGGER.info("Entro a pagarPrimeraPrimaNiubiz()");
    	FisrtPayResponse response = new FisrtPayResponse();
    	HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("X-Apikey", apiKey);
		headers.set("X-Provider", request.getProvider() != null ? request.getProvider() : niubiz);
		headers.set("Content-Type", "application/json");
		headers.set("X-TimeZone", "America/Lima");
		
		//LOGGER.info();
		try {
			response = obtenerPostPorObjeto(getUrlPrimeraPrima(), new HttpEntity<FirstPayRequest>(request, headers), FisrtPayResponse.class);
		} catch (HttpClientErrorException errorClient) {
			response.setCode(Constantes.CODIGO_RESPUESTA_GENERAL_CULQI_ERROR_400);
			response.setMessage(errorClient.getResponseBodyAsString());
			response.setStatusHttp(errorClient.getMessage());
		} catch (HttpServerErrorException errorServer) {
			response.setCode(Constantes.CODIGO_RESPUESTA_GENERAL_CULQI_ERROR_500);
			response.setMessage(errorServer.getMessage());
			response.setStatusHttp(errorServer.getResponseBodyAsString());
		} catch (Exception e) {
			e.getStackTrace();
			System.out.println(e.getMessage());
			response.setCode(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			response.setMessage("Error desconocido");
		}
		LOGGER.info("Salio a pagarPrimeraPrimaNiubiz()");
    	return response;
    }
    
    public FisrtPayResponse pagarPrimeraPrimaNiubizRecargo(FirstPayRecargoRequest request) {
    	LOGGER.info("Entro a pagarPrimeraPrimaNiubizRecargo()");
    	LOGGER.info(gson.toJson(request));
    	FisrtPayResponse response = new FisrtPayResponse();
    	HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("X-Apikey", apiKey);
		headers.set("X-Provider", request.getProvider() != null ? request.getProvider() : niubiz);
		headers.set("Content-Type", "application/json");
		headers.set("X-TimeZone", "America/Lima");
		LOGGER.info(gson.toJson(headers));
		LOGGER.info(getUrlPrimeraPrimaRecargo());
		try {
			response = obtenerPostPorObjeto(getUrlPrimeraPrimaRecargo(), new HttpEntity<FirstPayRecargoRequest>(request, headers), FisrtPayResponse.class);
		} catch (HttpClientErrorException errorClient) {
			response.setCode(Constantes.CODIGO_RESPUESTA_GENERAL_CULQI_ERROR_400);
			response.setMessage(errorClient.getResponseBodyAsString());
			response.setStatusHttp(errorClient.getMessage());
		} catch (HttpServerErrorException errorServer) {
			response.setCode(Constantes.CODIGO_RESPUESTA_GENERAL_CULQI_ERROR_500);
			response.setMessage(errorServer.getMessage());
			response.setStatusHttp(errorServer.getResponseBodyAsString());
		} catch (Exception e) {
			e.getStackTrace();
			System.out.println(e.getMessage());
			response.setCode(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			response.setMessage("Error desconocido");
		}
		LOGGER.info(gson.toJson(response));
		LOGGER.info("Salio a pagarPrimeraPrimaNiubizRecargo()");
    	return response;
    }
}
