package pe.interseguro.siv.common.persistence.rest.plaft;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import pe.interseguro.siv.common.exception.SivSOAException;
import pe.interseguro.siv.common.persistence.rest.base.BaseRestClientImpl;
import pe.interseguro.siv.common.persistence.rest.plaft.request.CalificacionPropuestaRequest;
import pe.interseguro.siv.common.persistence.rest.plaft.response.CalificacionPropuestaResponse;
import pe.interseguro.siv.common.persistence.rest.plaft.response.PlaftTokenResponse;

@Component
public class PlaftRestClient extends BaseRestClientImpl {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
    @Value("#{ environment['persistence.rest.client.plaft.servidor'] }")
    private String servidorPlaft;
    
    @Value("#{ environment['persistence.rest.client.plaft.token'] }")
    private String plaftObtenerToken;
    
    @Value("#{ environment['persistence.rest.client.plaft.token.grantType'] }")
	private String plaftToken_grantType;

    @Value("#{ environment['persistence.rest.client.plaft.token.clientId'] }")
	private String plaftToken_clientId;
    
    @Value("#{ environment['persistence.rest.client.plaft.clientId'] }")
	private String clientId;

    @Value("#{ environment['persistence.rest.client.plaft.token.clientSecret'] }")
	private String plaftToken_clientSecret;

    @Value("#{ environment['persistence.rest.client.plaft.calificacionPropuesta'] }")
    private String plaftCalificacionPropuesta;
    
	private String getUrlPlaftToken() {
    	return servidorPlaft + plaftObtenerToken;
    }
    
    private String getUrlPlaftCalificacionPropuesta() {
    	return servidorPlaft + plaftCalificacionPropuesta;
    }
    
    
	public PlaftTokenResponse obtenerTokenPlaft() throws SivSOAException {
		LOGGER.info("Entro a PlaftRestClient#obtenerTokenPlaft()");

		//-- Obtener Token	
		MultiValueMap<String, String> requestToken = new LinkedMultiValueMap<String, String>();
		requestToken.add("grant_type", this.plaftToken_grantType);
		requestToken.add("client_id", this.plaftToken_clientId);
		requestToken.add("client_secret", this.plaftToken_clientSecret);
		
//		PlaftTokenRequest requestToken = new PlaftTokenRequest();
//		requestToken.setGrantType(this.plaftToken_grantType);
//		requestToken.setCliendId(this.plaftToken_clientId);
//		requestToken.setClientSecret(this.plaftToken_clientSecret);
				
		PlaftTokenResponse tokenResponse = new PlaftTokenResponse();
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
						
			LOGGER.info("URL => " + getUrlPlaftToken());
			
			tokenResponse = obtenerPostPorObjeto(
				getUrlPlaftToken(),
				new HttpEntity<MultiValueMap<String, String>>(requestToken, headers), PlaftTokenResponse.class);
			tokenResponse.setStatusHttp("200");
		} catch (Exception e) {
			tokenResponse.setStatusHttp("400");
			tokenResponse.setCode("DIG-001");
			tokenResponse.setMessage(e.getMessage());
			tokenResponse.setTitle(getUrlPlaftToken());
		}		
		
		LOGGER.info("Salio a PlaftRestClient#obtenerTokenPlaft()");
		return tokenResponse;
	}
	
    public CalificacionPropuestaResponse calificarPropuestaPlaft(CalificacionPropuestaRequest propuestaRequest) throws SivSOAException {
    	LOGGER.info("Entro a PlaftRestClient#calificarPropuestaPlaft(propuestaRequest)");
    	CalificacionPropuestaResponse respuestaFinal = new CalificacionPropuestaResponse();
    	PlaftTokenResponse tokenResponse = this.obtenerTokenPlaft();
    	String content = "";
    	propuestaRequest.setClient_id(this.clientId);
    	
    	int code = 0;
    	content = "client_id=" + propuestaRequest.getClient_id() + "&propuesta=" + propuestaRequest.getPropuesta() + "&prima_anualizada=" + propuestaRequest.getPrima_anualizada() + "&producto=" + propuestaRequest.getProducto() + "&moneda=" + propuestaRequest.getMoneda();
        content = content + "&contratante_tipo_documento=" + propuestaRequest.getContratante_tipo_documento() + "&contratante_documento=" + propuestaRequest.getContratante_documento() + "&contratante_actividad_economica=" + propuestaRequest.getContratante_actividad_economica() + "&contratante_profesion=" + propuestaRequest.getContratante_profesion();
        content = content + "&contratante_sujeto_obligado=" + propuestaRequest.getContratante_sujeto_obligado() + "&contratante_nacionalidad_residencia=" + propuestaRequest.getContratante_nacionalidad_residencia() + "&contratante_pep=" + propuestaRequest.getContratante_pep() + "&contratante_nombre1=" + propuestaRequest.getContratante_nombre1();
        content = content + "&contratante_nombre2=" + propuestaRequest.getContratante_nombre2() + "&contratante_nombre3=" + propuestaRequest.getContratante_nombre3() + "&contratante_apellido_paterno=" + propuestaRequest.getContratante_apellido_paterno() + "&contratante_apellido_materno=" + propuestaRequest.getContratante_apellido_materno() + "&contratante_razon_social=" + propuestaRequest.getContratante_razon_social();
        content = content + "&asegurado_tipo_documento=" + propuestaRequest.getAsegurado_tipo_documento() + "&asegurado_documento=" + propuestaRequest.getAsegurado_documento() + "&asegurado_actividad_economica=" + propuestaRequest.getAsegurado_actividad_economica() + "&asegurado_profesion=" + propuestaRequest.getAsegurado_profesion() + "&asegurado_sujeto_obligado=" + propuestaRequest.getAsegurado_sujeto_obligado();
        content = content + "&asegurado_nacionalidad_residencia=" + propuestaRequest.getAsegurado_nacionalidad_residencia() + "&asegurado_pep=" + propuestaRequest.getAsegurado_pep() + "&asegurado_nombre1=" + propuestaRequest.getAsegurado_nombre1() + "&asegurado_nombre2=" + propuestaRequest.getAsegurado_nombre2() + "&asegurado_nombre3=" + propuestaRequest.getAsegurado_nombre3();
        content = content + "&asegurado_apellido_paterno=" + propuestaRequest.getAsegurado_apellido_paterno() + "&asegurado_apellido_materno=" + propuestaRequest.getAsegurado_apellido_materno() + "&asegurado_razon_social=" + propuestaRequest.getAsegurado_razon_social();
        content = content + "&contratante_residencia=" + propuestaRequest.getContratante_residencia() + "&asegurado_residencia=" + propuestaRequest.getAsegurado_residencia();
    	

    	
    	String urlParameters  = content;
    	byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
    	int    postDataLength = postData.length;
    	String request        = getUrlPlaftCalificacionPropuesta();
    	
    	try {
    	
    	URL url = new URL( request );
    	HttpURLConnection conn= (HttpURLConnection) url.openConnection();           
    	conn.setDoOutput( true );
    	conn.setRequestProperty( "Authorization", "Bearer "+ tokenResponse.getRecords().getAccessToken());
    	conn.setRequestMethod( "POST" );
    	conn.setInstanceFollowRedirects( false );
    	conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
    	conn.setRequestProperty( "charset", "utf-8");
    	conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
    	//conn.setUseCaches( false );
    	
    	try(OutputStream os = conn.getOutputStream()){
			os.write(postData, 0, postDataLength);
			os.close();
		}
    	code = conn.getResponseCode();
    	LOGGER.info("\nSending 'POST' request to URL : " + url);
    	LOGGER.info("Post parameters : " + urlParameters);
    	LOGGER.info("Response Code : " + code);
    	
    	
    	BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        LOGGER.info(response.toString());
        ObjectMapper objectMapper = new ObjectMapper();
		respuestaFinal = objectMapper.readValue(response.toString(), CalificacionPropuestaResponse.class);
		LOGGER.info(respuestaFinal.toString());
    	conn.disconnect();
    	}
    	catch (Exception e) {
        		respuestaFinal.setStatusHttp(Integer.toString(code));
        		respuestaFinal.setCode("PLFT-002");
        		respuestaFinal.setMessage(e.getMessage());
        		respuestaFinal.setTitle(getUrlPlaftCalificacionPropuesta());
        	}

    	LOGGER.info("Salió PlaftRestClient#calificarPropuestaPlaft(propuestaRequest)");
		return respuestaFinal;
    }
    
    public CalificacionPropuestaResponse calificarPropuestaPlaft3(CalificacionPropuestaRequest propuestaRequest) throws SivSOAException {
    	LOGGER.info("Entro a PlaftRestClient#calificarPropuestaPlaft(propuestaRequest)");
		CalificacionPropuestaResponse respuestaFinal = new CalificacionPropuestaResponse();
    	PlaftTokenResponse tokenResponse = this.obtenerTokenPlaft();
    	propuestaRequest.setClient_id("miusuariotest");
    	propuestaRequest.setContratante_nacionalidad_residencia("");
    	propuestaRequest.setContratante_documento("");
    	propuestaRequest.setContratante_nacionalidad_residencia("");
    	propuestaRequest.setContratante_razon_social("");
    	propuestaRequest.setAsegurado_nacionalidad_residencia("");
    	propuestaRequest.setAsegurado_razon_social("");
    	
    		try {
        		ObjectMapper mapper = new ObjectMapper();
        		String json = mapper.writeValueAsString(propuestaRequest);
        		LOGGER.info(json);
    			HttpHeaders headers = new HttpHeaders();
    			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    			headers.setContentLength(json.length());
    			headers.set("Authorization", "Bearer "+tokenResponse.getRecords().getAccessToken());    			
    			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
    			map.add("data", json);
    			
    			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
    			
    			//-- Response
    			ResponseEntity<CalificacionPropuestaResponse> response = getRestTemplate().postForEntity(getUrlPlaftCalificacionPropuesta(), request, CalificacionPropuestaResponse.class);
    			respuestaFinal = response.getBody();
        		
        	} catch (Exception e) {
        		respuestaFinal.setStatusHttp("400");
        		respuestaFinal.setCode("PLFT-002");
        		respuestaFinal.setMessage(e.getMessage());
        		respuestaFinal.setTitle(getUrlPlaftCalificacionPropuesta());
        	}

    	LOGGER.info("Salió PlaftRestClient#calificarPropuestaPlaft(propuestaRequest)");
		return respuestaFinal;
    }

}
