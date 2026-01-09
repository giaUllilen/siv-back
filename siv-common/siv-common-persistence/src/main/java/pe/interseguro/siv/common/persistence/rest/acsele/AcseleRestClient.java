package pe.interseguro.siv.common.persistence.rest.acsele;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.web.util.UriComponentsBuilder;

import com.google.gson.Gson;

import fr.opensagres.xdocreport.document.json.JSONObject;
import pe.interseguro.siv.common.exception.SivSOAException;
import pe.interseguro.siv.common.persistence.rest.acsele.request.AcseleTercero;
import pe.interseguro.siv.common.persistence.rest.acsele.request.TipoPersona;
import pe.interseguro.siv.common.persistence.rest.acsele.response.TerceroResponse;
import pe.interseguro.siv.common.persistence.rest.base.BaseRestClientImpl;
import pe.interseguro.siv.common.persistence.rest.indenova.response.CrearTokenResponse;
import pe.interseguro.siv.common.util.Constantes;
import pe.interseguro.siv.common.util.Utilitarios;

@Component
public class AcseleRestClient extends BaseRestClientImpl {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MessageSource messageSource;

    @Value("#{ environment['persistence.rest.client.acsele.servidor'] }")
    private String servidorAcsele;
    
    @Value("#{ environment['persistence.rest.client.acsele.service.token'] }")
    private String servicioGenerarToken;
    
    @Value("#{ environment['persistence.rest.client.acsele.service.obtenerIdTercero'] }")
    private String servicioObtenerIdTercero;
    
    private String getUrlAcseleToken() {
		return servidorAcsele+servicioGenerarToken;
	}
    private String getUrlAcseleGetIdTercero() {
		return servidorAcsele+servicioObtenerIdTercero;
	}
    
    public CrearTokenResponse crearToken() {
    	LOGGER.info("Entro AcseleRestClient#crearToken(request)");
    	
    	CrearTokenResponse respuestaFinal = null;
    	
    	try { 
    		
			//-- Header
			HttpHeaders headers = new HttpHeaders();
			
			MediaType mediaType = new MediaType("application", "x-www-form-urlencoded", StandardCharsets.UTF_8);
			headers.setContentType(mediaType);
			
			List<MediaType> accepts = new ArrayList<>();
			accepts.add(MediaType.TEXT_PLAIN);
			headers.setAccept(accepts);
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl( getUrlAcseleToken() );
			
			JSONObject json = new JSONObject();
			json.put("idSistema", "7");
			json.put("usuario", "interface.crm");
			json.put("clave", "123456");
			json.put("codISOpais", "PE");
			
			String request = "JSONAutentico=" + json.toString();
			
			HttpEntity<String> requestEntity = new HttpEntity<String>(request, headers);
			
			respuestaFinal = getRestTemplate().postForObject(builder.build().toString(), requestEntity, CrearTokenResponse.class);
			
    	} catch(Exception e) {
    		e.printStackTrace();
			throw new SivSOAException(
				Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_INDENOVA_CREAR_TOKEN).concat( Objects.toString(e.getMessage(), "") ), 
				null
			);
    	}
    	LOGGER.info("Salio AcseleRestClient#crearToken(request)");
    	
    	return respuestaFinal;
    }
    
    public TerceroResponse obtenerIDTercero(String tipoDocumento, String numeroDocumento) {
    	LOGGER.info("Entro AcseleRestClient#obtenerIDTercero(request)");
    	
    	TerceroResponse respuestaFinal = new TerceroResponse();
    	respuestaFinal.setTitle(getUrlAcseleGetIdTercero());
    	try { 
    		
			//-- Header
			HttpHeaders headers = new HttpHeaders();
			
			MediaType mediaType = new MediaType("application", "x-www-form-urlencoded", StandardCharsets.UTF_8);
			headers.setContentType(mediaType);
			
			List<MediaType> accepts = new ArrayList<>();
			accepts.add(MediaType.TEXT_PLAIN);
			headers.setAccept(accepts);
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl( getUrlAcseleGetIdTercero() );
			
			JSONObject json = new JSONObject();
			json.put("idSistema", "7");
			json.put("usuario", "interface.crm");
			json.put("clave", "123456");
			json.put("codISOpais", "PE");
			json.put("token", this.crearToken().getToken());
			
			AcseleTercero acseleTercero = new AcseleTercero();
			acseleTercero.setTipoPersona(new TipoPersona(tipoDocumento, numeroDocumento));
			Gson gson = new Gson();
			String JSONTercero = gson.toJson(acseleTercero);
			
			String request = "JSONAutentico=" + json.toString() + "&JSONTercero=" + JSONTercero;
			
			HttpEntity<String> requestEntity = new HttpEntity<String>(request, headers);
					
			respuestaFinal = getRestTemplate().postForObject(builder.build().toString(), requestEntity, TerceroResponse.class);
			respuestaFinal.setStatusHttp("200");
    	} catch(Exception e) {
    		respuestaFinal.setStatusHttp("500");
    		respuestaFinal.setMessage("Error: " + e.getMessage());
    		/*e.printStackTrace();
			throw new SivSOAException(
				Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_INDENOVA_CREAR_TOKEN).concat( Objects.toString(e.getMessage(), "") ), 
				null
			);*/
    	}
    	LOGGER.info("Salio AcseleRestClient#obtenerIDTercero(request)");
    	
    	return respuestaFinal;
    }
}
