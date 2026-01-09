package pe.interseguro.siv.common.persistence.rest.vtigger;

import java.security.MessageDigest;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import pe.interseguro.siv.common.persistence.rest.base.BaseRestClientImpl;
import pe.interseguro.siv.common.persistence.rest.indenova.request.CrearTokenRequest;
import pe.interseguro.siv.common.persistence.rest.vtigger.request.ActualizarContactoRequest;
import pe.interseguro.siv.common.persistence.rest.vtigger.request.ConsultaClienteRequest;
import pe.interseguro.siv.common.persistence.rest.vtigger.request.ConsultaPotencialRequest;
import pe.interseguro.siv.common.persistence.rest.vtigger.request.ConsultaUsuarioPorCorreoRequest;
import pe.interseguro.siv.common.persistence.rest.vtigger.request.ConsultaUsuarioRequest;
import pe.interseguro.siv.common.persistence.rest.vtigger.request.CrearContactoRequest;
import pe.interseguro.siv.common.persistence.rest.vtigger.response.ActualizarContactoResponse;
import pe.interseguro.siv.common.persistence.rest.vtigger.response.ConsultaClienteResponse;
import pe.interseguro.siv.common.persistence.rest.vtigger.response.ConsultaPotencialResponse;
import pe.interseguro.siv.common.persistence.rest.vtigger.response.ConsultaUsuarioPorCorreoResponse;
import pe.interseguro.siv.common.persistence.rest.vtigger.response.ConsultaUsuarioResponse;
import pe.interseguro.siv.common.persistence.rest.vtigger.response.CrearContactoResponse;
import pe.interseguro.siv.common.persistence.rest.vtigger.response.CrearSessionResponse;
import pe.interseguro.siv.common.persistence.rest.vtigger.response.CrearTokenResponse;

@Component
public class VTigerRestClient extends BaseRestClientImpl {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MessageSource messageSource;
	
	@Value("#{ environment['persistence.rest.client.vtiger.servidor'] }")
    private String servidorVTiger;
	
	@Value("#{ environment['persistence.rest.client.vtiger.servidor.v2'] }")
    private String servidorVTigerV2;
    
    @Value("#{ environment['persistence.rest.client.vtiger.crearToken'] }")
    private String vtigerCrearToken;

    @Value("#{ environment['persistence.rest.client.vtiger.crearToken.request.username'] }")
    private String vtigerCrearTokenRequestUsername;

    @Value("#{ environment['persistence.rest.client.vtiger.crearSession'] }")
    private String vtigerCrearSession;

    @Value("#{ environment['persistence.rest.client.vtiger.crearSession.request.operation'] }")
    private String vtigerCrearSessionOperation;

    @Value("#{ environment['persistence.rest.client.vtiger.crearSession.request.username'] }")
    private String vtigerCrearSessionUsername;
    
    @Value("#{ environment['persistence.rest.client.vtiger.crearSession.request.userclaveacceso'] }")
    private String vtigerCrearSessionClaveAcceso;
    
    @Value("#{ environment['persistence.rest.client.vtiger.buscar'] }")
    private String vtigerBuscar;
    
    @Value("#{ environment['persistence.rest.client.vtiger.buscar.v2'] }")
    private String vtigerBuscarV2;
    
    @Value("#{ environment['persistence.rest.client.vtiger.Contacto'] }")
    private String vtigerContacto;
    
    @Value("#{ environment['persistence.rest.client.vtiger.crearContacto'] }")
    private String vtigerCrearContacto;
    
    @Value("#{ environment['persistence.rest.client.vtiger.actualizarContacto'] }")
    private String vtigerActualizarContacto;    
    
    private String getUrlVTigerCrearToken() {
		return servidorVTiger+vtigerCrearToken+vtigerCrearTokenRequestUsername;
	}
    private String getUrlVTigerCrearSession() {
		return servidorVTiger+vtigerCrearSession;
	}
    private String getUrlVTigerBuscar() {
		return servidorVTiger+vtigerBuscar;
	}
    
    private String getUrlVTigerBuscarV2() {
		return servidorVTigerV2+vtigerBuscarV2;
	}
    
    private Gson gson = new Gson();
    
    public CrearTokenResponse crearToken() {
    	LOGGER.info("Entro VTigerRestClient#crearToken(request)");
    	
    	CrearTokenResponse respuestaFinal = new CrearTokenResponse();
    	
    	try { 
    		
			//-- Header
			HttpHeaders headers = new HttpHeaders();
	
			//-- Request GET
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl( getUrlVTigerCrearToken() );
			LOGGER.info(gson.toJson(builder));
			CrearTokenRequest request = new CrearTokenRequest();
						
			HttpEntity<CrearTokenRequest> requestEntity = new HttpEntity<CrearTokenRequest>(request, headers);
			
			//-- Response 
			ResponseEntity<CrearTokenResponse> response = getRestTemplate().exchange(
				builder.build().toString(),
				HttpMethod.GET,
				requestEntity,
				CrearTokenResponse.class
			);
	
			respuestaFinal = response.getBody();
			respuestaFinal.setStatusHttp("200");
		} catch(Exception e) {
    		respuestaFinal.setStatusHttp("400");
    		respuestaFinal.setCode("VTG-001");
    		respuestaFinal.setMessage(e.getMessage());
    		respuestaFinal.setTitle(getUrlVTigerCrearToken());
		}

    	LOGGER.info("Salio VTigerRestClient#crearToken(request)");
    	return respuestaFinal;
    }
    
    public CrearSessionResponse crearSession() {
    	LOGGER.info("Entro VTigerRestClient#crearSession(request)");
    	
    	CrearSessionResponse respuestaFinal = new CrearSessionResponse();
    	
    	try { 
    		
			//-- Header
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			
			CrearTokenResponse tokenVTiger = crearToken();
			
			if (tokenVTiger.getStatusHttp() == "200") {
				LOGGER.info("Token generado:" + tokenVTiger.getResult().getToken());
				
				MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
				map.add("operation", this.vtigerCrearSessionOperation);
				map.add("username", this.vtigerCrearSessionUsername);
				String VTIGER_ACCESS_KEY = tokenVTiger.getResult().getToken() + this.vtigerCrearSessionClaveAcceso;
				LOGGER.info("Accesskey: " + VTIGER_ACCESS_KEY);
				MessageDigest md = MessageDigest.getInstance("MD5");
				md.update(VTIGER_ACCESS_KEY.getBytes());
				byte[] digest = md.digest();
				String hash =DatatypeConverter.printHexBinary(digest).toLowerCase();
				map.add("accessKey", hash);
				LOGGER.info("Hash: " + hash);
				HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
				
				//-- Response
				ResponseEntity<CrearSessionResponse> response = getRestTemplate().postForEntity(getUrlVTigerCrearSession(), request, CrearSessionResponse.class);
				
				respuestaFinal = response.getBody();
				respuestaFinal.setStatusHttp("200");
			} else {
				respuestaFinal.setStatusHttp("400");
				respuestaFinal.setMessage(tokenVTiger.getMessage());
				respuestaFinal.setCode(tokenVTiger.getCode());
				respuestaFinal.setTitle(tokenVTiger.getTitle());
			}
		} catch(Exception e) {
    		respuestaFinal.setStatusHttp("400");
    		respuestaFinal.setCode("VTG-002");
    		respuestaFinal.setMessage(e.getMessage());
    		respuestaFinal.setTitle(getUrlVTigerCrearSession());
		}

    	LOGGER.info("Salio VTigerRestClient#crearSession(request)");
    	return respuestaFinal;
    }
    
    
    /*public ConsultaClienteResponse buscarCliente(ConsultaClienteRequest consultaRequest) {
    	LOGGER.info("Entro VTigerRestClient#buscarCliente(request)");
    	
    	ConsultaClienteResponse respuestaFinal = new ConsultaClienteResponse();
    	
    	CrearSessionResponse sessionName = crearSession();
    	if (sessionName.getStatusHttp() != "200") {
    		respuestaFinal.setCode(sessionName.getCode());
    		respuestaFinal.setMessage(sessionName.getMessage());
    		respuestaFinal.setStatusHttp(sessionName.getStatusHttp());
    		respuestaFinal.setTitle(sessionName.getTitle());
    	} else {
    		String query = "SELECT id,cf_852,cf_854,firstname,lastname,cf_920,phone,leadsource,email,assigned_user_id,othercity,cf_860 FROM Contacts WHERE cf_854= '" + consultaRequest.getDocumentoIdentidad() + "';";
			String url = getUrlVTigerBuscar().replace("{1}", sessionName.getResult().getSessionName());
			url = url.replace("{2}", query);
			
			try {
				HttpHeaders headers = new HttpHeaders();
				UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl( url );
				CrearTokenRequest request = new CrearTokenRequest();			
				HttpEntity<CrearTokenRequest> requestEntity = new HttpEntity<CrearTokenRequest>(request, headers);
				ResponseEntity<ConsultaClienteResponse> response = getRestTemplate().exchange(
					builder.build().toString(),
					HttpMethod.GET,
					requestEntity,
					ConsultaClienteResponse.class
				);
				respuestaFinal = response.getBody();
				respuestaFinal.setStatusHttp("200");
	    	} catch(Exception e) {
	    		respuestaFinal.setStatusHttp("400");
	    		respuestaFinal.setCode("VTG-003");
	    		respuestaFinal.setMessage(e.getMessage());
	    		respuestaFinal.setTitle(url);
			}
    	}
    	
    	LOGGER.info("Salio VTigerRestClient#buscarCliente(request)");
    	return respuestaFinal;
    }*/
    
    public ConsultaClienteResponse buscarCliente(ConsultaClienteRequest consultaRequest) {
    	LOGGER.info("Entro VTigerRestClient#buscarCliente(request)");
    	
    	ConsultaClienteResponse respuestaFinal = new ConsultaClienteResponse();

		CrearSessionResponse sessionName = crearSession();
		if (sessionName.getStatusHttp() != "200") {
			respuestaFinal.setCode(sessionName.getCode());
			respuestaFinal.setMessage(sessionName.getMessage());
			respuestaFinal.setStatusHttp(sessionName.getStatusHttp());
			respuestaFinal.setTitle(sessionName.getTitle());
		} else {
			String url = getUrlVTigerBuscar().replace("{1}", sessionName.getResult().getSessionName());
			String query = "SELECT * FROM Contacts WHERE cf_854 = '" + consultaRequest.getDocumentoIdentidad() + "';";
			url = url.replace("{2}", query);
			System.out.println("buscarCliente DOC: " + consultaRequest.getDocumentoIdentidad() + " : " + url);
			try {
				HttpHeaders headers = new HttpHeaders();
				UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
				CrearTokenRequest request = new CrearTokenRequest();
				HttpEntity<CrearTokenRequest> requestEntity = new HttpEntity<CrearTokenRequest>(request, headers);
				ResponseEntity<ConsultaClienteResponse> response = getRestTemplate().exchange(
						builder.build().toString(),
						HttpMethod.GET,
						requestEntity,
						ConsultaClienteResponse.class
				);
				respuestaFinal = response.getBody();
				respuestaFinal.setStatusHttp("200");
			} catch (Exception e) {
				respuestaFinal.setStatusHttp("400");
				respuestaFinal.setCode("VTG-003");
				respuestaFinal.setMessage(e.getMessage());
				respuestaFinal.setTitle(url);
			}
		}
    	
    	LOGGER.info("Salio VTigerRestClient#buscarCliente(request)");
    	return respuestaFinal;
    }
    
    public ConsultaUsuarioResponse buscarUsuario(ConsultaUsuarioRequest consultaRequest) {
    	LOGGER.info("Entro VTigerRestClient#buscarUsuario(request)");
    	ConsultaUsuarioResponse respuestaFinal = new ConsultaUsuarioResponse();
    	
		CrearSessionResponse sessionName = crearSession();
    	if (sessionName.getStatusHttp() != "200") {
    		respuestaFinal.setCode(sessionName.getCode());
    		respuestaFinal.setMessage(sessionName.getMessage());
    		respuestaFinal.setStatusHttp(sessionName.getStatusHttp());
    		respuestaFinal.setTitle(sessionName.getTitle());
    	} else {
    		String query = "SELECT id,user_name,first_name,last_name,roleid,cf_915,email1 FROM Users WHERE id= '" + consultaRequest.getId() + "';";
			String url = getUrlVTigerBuscar().replace("{1}", sessionName.getResult().getSessionName());
			LOGGER.info(url);
			url = url.replace("{2}", query);
			
			try {
				HttpHeaders headers = new HttpHeaders();
				UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl( url );
				CrearTokenRequest request = new CrearTokenRequest();		
				HttpEntity<CrearTokenRequest> requestEntity = new HttpEntity<CrearTokenRequest>(request, headers);
				ResponseEntity<ConsultaUsuarioResponse> response = getRestTemplate().exchange(
					builder.build().toString(),
					HttpMethod.GET,
					requestEntity,
					ConsultaUsuarioResponse.class
				);
				respuestaFinal = response.getBody();
				respuestaFinal.setStatusHttp("200");
	    	} catch (Exception e) {
	    		respuestaFinal.setStatusHttp("400");
	    		respuestaFinal.setCode("VTG-004");
	    		respuestaFinal.setMessage(e.getMessage());
	    		respuestaFinal.setTitle(url);
			}
    	}
    	
    	LOGGER.info("Salio VTigerRestClient#buscarUsuario(request)");
    	return respuestaFinal;
    }
    
    public ConsultaPotencialResponse buscarPotencial(ConsultaPotencialRequest consultaRequest) {
    	LOGGER.info("Entro VTigerRestClient#buscarPotencial(request)");
    	ConsultaPotencialResponse respuestaFinal = new ConsultaPotencialResponse();
    	CrearSessionResponse sessionName = crearSession();
    	if (sessionName.getStatusHttp() != "200") {
    		respuestaFinal.setCode(sessionName.getCode());
    		respuestaFinal.setMessage(sessionName.getMessage());
    		respuestaFinal.setStatusHttp(sessionName.getStatusHttp());
    		respuestaFinal.setTitle(sessionName.getTitle());
    	} else {
    		String query = "SELECT id,contact_id,leadsource,sales_stage,cf_872,cf_924 FROM Potentials WHERE contact_id = '" + consultaRequest.getContactID() + "';";
			String url = getUrlVTigerBuscar().replace("{1}", sessionName.getResult().getSessionName());
			url = url.replace("{2}", query);
	    	try { 
				HttpHeaders headers = new HttpHeaders();
				UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl( url );
				CrearTokenRequest request = new CrearTokenRequest();
				HttpEntity<CrearTokenRequest> requestEntity = new HttpEntity<CrearTokenRequest>(request, headers);
				ResponseEntity<ConsultaPotencialResponse> response = getRestTemplate().exchange(
					builder.build().toString(),
					HttpMethod.GET,
					requestEntity,
					ConsultaPotencialResponse.class
				);
		
				respuestaFinal = response.getBody();
				respuestaFinal.setStatusHttp("200");
	    	} catch (Exception e) {
	    		respuestaFinal.setStatusHttp("400");
	    		respuestaFinal.setCode("VTG-005");
	    		respuestaFinal.setMessage(e.getMessage());
	    		respuestaFinal.setTitle(url);
			}
    	}
    	
    	LOGGER.info("Salio VTigerRestClient#buscarPotencial(request)");
    	return respuestaFinal;
    }
    
    public CrearContactoResponse crearContacto(CrearContactoRequest contactoRequest) {
    	LOGGER.info("Entro VTigerRestClient#crearContacto(request)");
    	CrearContactoResponse respuestaFinal = new CrearContactoResponse();
    	CrearSessionResponse sessionName = crearSession();
    	if (sessionName.getStatusHttp() != "200") {
    		respuestaFinal.setCode(sessionName.getCode());
    		respuestaFinal.setMessage(sessionName.getMessage());
    		respuestaFinal.setStatusHttp(sessionName.getStatusHttp());
    		respuestaFinal.setTitle(sessionName.getTitle());
    	} else {
    		try {
    			ObjectMapper mapper = new ObjectMapper();
    			String json = mapper.writeValueAsString(contactoRequest);
    			LOGGER.info(json);
    			HttpHeaders headers = new HttpHeaders();
    			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    			
    			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
    			map.add("operation", this.vtigerCrearContacto);
    			map.add("elementType", this.vtigerContacto);
    			map.add("sessionName", sessionName.getResult().getSessionName());
    			map.add("element", json);
    			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
    			ResponseEntity<CrearContactoResponse> response = getRestTemplate().postForEntity(getUrlVTigerCrearSession(), request, CrearContactoResponse.class);
    			respuestaFinal = response.getBody();
    			respuestaFinal.setStatusHttp("200");
        	} catch ( Exception e) {
        		respuestaFinal.setStatusHttp("400");
        		respuestaFinal.setCode("VTG-006");
        		respuestaFinal.setMessage(e.getMessage());
        		respuestaFinal.setTitle(getUrlVTigerCrearSession());
        	}
    	}
    	LOGGER.info("Salio VTigerRestClient#crearContacto(request)");
    	return respuestaFinal;
    }
    
    public ActualizarContactoResponse actualizarContacto(ActualizarContactoRequest contactoRequest) {
    	LOGGER.info("Entro VTigerRestClient#actualizarContacto(request)");
    	ActualizarContactoResponse respuestaFinal = new ActualizarContactoResponse();
    	CrearSessionResponse sessionName = crearSession();
    	if (sessionName.getStatusHttp() != "200") {
    		respuestaFinal.setCode(sessionName.getCode());
    		respuestaFinal.setMessage(sessionName.getMessage());
    		respuestaFinal.setStatusHttp(sessionName.getStatusHttp());
    		respuestaFinal.setTitle(sessionName.getTitle());
    	} else {
    		try {
        		ObjectMapper mapper = new ObjectMapper();
        		String json = mapper.writeValueAsString(contactoRequest);
        		LOGGER.info(json);
    			HttpHeaders headers = new HttpHeaders();
    			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
    			map.add("operation", this.vtigerActualizarContacto);
    			map.add("elementType", this.vtigerContacto);
    			map.add("sessionName", sessionName.getResult().getSessionName());
    			map.add("element", json);
    			
    			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
    			
    			//-- Response
    			ResponseEntity<ActualizarContactoResponse> response = getRestTemplate().postForEntity(getUrlVTigerCrearSession(), request, ActualizarContactoResponse.class);
    			respuestaFinal = response.getBody();
        		
        	} catch (Exception e) {
        		respuestaFinal.setStatusHttp("400");
        		respuestaFinal.setCode("VTG-007");
        		respuestaFinal.setMessage(e.getMessage());
        		respuestaFinal.setTitle(getUrlVTigerCrearSession());
        	}
    	}
    	LOGGER.info("Salió VTigerRestClient#actualizarContacto(request)");
		return respuestaFinal;
    }
    
    public ConsultaUsuarioPorCorreoResponse buscarUsuarioPorCorreo(ConsultaUsuarioPorCorreoRequest consultaRequest) {
    	LOGGER.info("Entro VTigerRestClient#ConsultaUsuarioPorCorreo(request)");
    	
    	ConsultaUsuarioPorCorreoResponse respuestaFinal = new ConsultaUsuarioPorCorreoResponse();
    	CrearSessionResponse sessionName = crearSession();
    	if (sessionName.getStatusHttp() != "200") {
    		respuestaFinal.setCode(sessionName.getCode());
    		respuestaFinal.setMessage(sessionName.getMessage());
    		respuestaFinal.setStatusHttp(sessionName.getStatusHttp());
    		respuestaFinal.setTitle(sessionName.getTitle());
    	} else {
    		String query = "select id,user_name,first_name,last_name,roleid,cf_915,email1 from Users where email1 = '" + consultaRequest.getCorreoUsuario() + "';";
			String url = getUrlVTigerBuscar().replace("{1}", sessionName.getResult().getSessionName());
			url = url.replace("{2}", query);
    		try {
    			HttpHeaders headers = new HttpHeaders();
    			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl( url );
    			CrearTokenRequest request = new CrearTokenRequest();		
    			HttpEntity<CrearTokenRequest> requestEntity = new HttpEntity<CrearTokenRequest>(request, headers);
    			ResponseEntity<ConsultaUsuarioPorCorreoResponse> response = getRestTemplate().exchange(
    				builder.build().toString(),
    				HttpMethod.GET,
    				requestEntity,
    				ConsultaUsuarioPorCorreoResponse.class
    			);
    	
    			respuestaFinal = response.getBody();
        	} catch (Exception e) {
        		respuestaFinal.setStatusHttp("400");
        		respuestaFinal.setCode("VTG-008");
        		respuestaFinal.setMessage(e.getMessage());
        		respuestaFinal.setTitle(url);
    		}
    	}
    	LOGGER.info("Salio VTigerRestClient#ConsultaUsuarioPorCorreo(request)");
    	return respuestaFinal;
    }
}
