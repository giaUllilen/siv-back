package pe.interseguro.siv.common.persistence.rest.indenova;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.net.ssl.HttpsURLConnection;

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
import org.springframework.web.util.UriComponentsBuilder;

import pe.interseguro.siv.common.exception.SivSOAException;
import pe.interseguro.siv.common.persistence.rest.base.BaseRestClientImpl;
import pe.interseguro.siv.common.persistence.rest.indenova.request.CrearCircuitoRequest;
import pe.interseguro.siv.common.persistence.rest.indenova.request.CrearTokenRequest;
import pe.interseguro.siv.common.persistence.rest.indenova.response.CircuitoResponse;
import pe.interseguro.siv.common.persistence.rest.indenova.response.CrearCircuitoResponse;
import pe.interseguro.siv.common.persistence.rest.indenova.response.CrearTokenResponse;
import pe.interseguro.siv.common.persistence.rest.indenova.response.DocumentoCircuitoResponse;
import pe.interseguro.siv.common.util.Constantes;
import pe.interseguro.siv.common.util.Utilitarios;

/**
 * @author digital-is
 *
 */
@Component
public class IndenovaRestClient extends BaseRestClientImpl {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MessageSource messageSource;

    @Value("#{ environment['persistence.rest.client.indenova.servidor'] }")
    private String servidorIndenova;
    
    @Value("#{ environment['persistence.rest.client.indenova.crearToken'] }")
    private String indenovaCrearToken;

    @Value("#{ environment['persistence.rest.client.indenova.crearToken.request.clientId'] }")
    private String indenovaCrearTokenRequestClientId;

    @Value("#{ environment['persistence.rest.client.indenova.crearToken.request.secret'] }")
    private String indenovaCrearTokenRequestSecret;

    @Value("#{ environment['persistence.rest.client.indenova.crearToken.request.userId'] }")
    private String indenovaCrearTokenUserId;

    @Value("#{ environment['persistence.rest.client.indenova.crearToken.llave'] }")
    private String indenovaCrearTokenLlave;

    
    @Value("#{ environment['persistence.rest.client.indenova.crearCircuito'] }")
    private String indenovaCrearCircuito;
    
    @Value("#{ environment['persistence.rest.client.indenova.obtenerCircuito'] }")
    private String indenovaObtenerCircuito;

    @Value("#{ environment['persistence.rest.client.indenova.obtenerDocumentoCircuito'] }")
    private String indenovaObtenerDocumentoCircuito;

    
    private String getUrlIndenovaCrearToken() {
		return servidorIndenova+indenovaCrearToken;
	}
    private String getUrlIndenovaCrearCircuito() {
		return servidorIndenova+indenovaCrearCircuito;
	}
    private String getUrlIndenovaObtenerCircuito() {
		return servidorIndenova+indenovaObtenerCircuito;
	}
    private String getUrlIndenovaObtenerDocumentoCircuito() {
		return servidorIndenova+indenovaObtenerDocumentoCircuito;
	}

    
    /**
     * Crear Token para acceder APIs de Indenova
     * 
     * @param request
     * @return
     */
    public CrearTokenResponse crearToken() {
    	LOGGER.info("Entro IndenovaRestClient#crearToken(request)");
    	
    	CrearTokenResponse respuestaFinal = null;
    	
    	try { 
    		
			//-- Header
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", indenovaCrearTokenLlave.trim());
	
			//-- Request GET 
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl( getUrlIndenovaCrearToken() );
	
			CrearTokenRequest request = new CrearTokenRequest();
			request.setClientId(indenovaCrearTokenRequestClientId.trim());
			request.setSecret(indenovaCrearTokenRequestSecret.trim());
			request.setUserId(indenovaCrearTokenUserId.trim());
						
			HttpEntity<CrearTokenRequest> requestEntity = new HttpEntity<CrearTokenRequest>(request, headers);
	
			
			//-- Response 
			ResponseEntity<String> response = getRestTemplate().exchange(
				builder.build().toString(),
				HttpMethod.GET,
				requestEntity,
				String.class
			);
	
			respuestaFinal = new CrearTokenResponse();
			respuestaFinal.setToken( response.getBody() );
    		
    	
		} catch (Exception e) {
			e.printStackTrace();
			throw new SivSOAException(
				Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_INDENOVA_CREAR_TOKEN).concat( Objects.toString(e.getMessage(), "") ), 
				null
			);
		}

    	LOGGER.info("Salio IndenovaRestClient#crearToken(request)");
    	return respuestaFinal;
    }
    
    /**
     * Crear circuito Indenova (sirve para crear un documento a ser firmado. Retorna la URL para abrir el popup)
     * 
     * @param request
     * @return
     */
    public CrearCircuitoResponse crearCircuito(CrearCircuitoRequest request) {
    	LOGGER.info("Entro IndenovaRestClient#crearCircuito(request)");

    	
    	CrearCircuitoResponse respuestaFinal = null;
    	
    	try { 
    		
    		/*
			//-- Header
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", "Bearer ".concat(this.crearToken().getToken()) );
	
			//-- Request
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl( getUrlIndenovaCrearCircuito() );
			HttpEntity<CrearCircuitoRequest> requestEntity = new HttpEntity<CrearCircuitoRequest>(request, headers);
	
			
			//-- Response 
			ResponseEntity<CrearCircuitoResponse> response = getRestTemplate().exchange(
				builder.build().toString(),
				HttpMethod.POST,
				requestEntity,
				CrearCircuitoResponse.class
			);
			respuestaFinal = response.getBody();
			*/
    		        	
    		
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", "Bearer ".concat(this.crearToken().getToken()) );
			
			respuestaFinal = obtenerPostPorObjeto(
				getUrlIndenovaCrearCircuito(), 
				new HttpEntity<CrearCircuitoRequest>(request, headers), 
				CrearCircuitoResponse.class
			);
    			
		
		} catch (Exception e) {
			e.printStackTrace();
			throw new SivSOAException(
				Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_INDENOVA_CREAR_CIRCUITO).concat( Objects.toString(e.getMessage(), "") ), 
				null
			);
		}

    	LOGGER.info("Salio IndenovaRestClient#crearCircuito(request)");
    	return respuestaFinal;
    }    
    
    public CircuitoResponse obtenerCircuito(String codigoCircuito) {
    	LOGGER.info("Entro IndenovaRestClient#obtenerCircuito(request)");
    	
    	CircuitoResponse respuestaFinal = null;
    	
    	try { 
    		
			//-- Header
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", "Bearer ".concat(this.crearToken().getToken()) );
	
			//-- Request GET 
			Map<String, Object> uriVariables = new HashMap<String, Object>();
			uriVariables.put("codigoCircuito", codigoCircuito);
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl( getUrlIndenovaObtenerCircuito() );
			builder.uriVariables(uriVariables);
			
			//-- Response 
			ResponseEntity<CircuitoResponse> response = getRestTemplate().exchange(
				builder.build().toString(),
				HttpMethod.GET,
				new HttpEntity<String>(null, headers),
				CircuitoResponse.class
			);
			respuestaFinal = response.getBody();
    		
    	
		} catch (Exception e) {
			e.printStackTrace();
			throw new SivSOAException(
				Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_INDENOVA_CIRCUITO).concat( Objects.toString(e.getMessage(), "") ), 
				null
			);
		}

    	LOGGER.info("Salio IndenovaRestClient#obtenerCircuito(request)");
    	return respuestaFinal;
    }
    
    /*public DocumentoCircuitoResponse obtenerDocumentoCircuito(String codigoCircuito) {
    	LOGGER.info("Entro IndenovaRestClient#obtenerDocumentoCircuito(request)");
    	
    	DocumentoCircuitoResponse respuestaFinal = null;
    	
    	try { 
    		
			//-- Header
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", "Bearer ".concat(this.crearToken().getToken()) );
	
			//-- Request GET 
			Map<String, Object> uriVariables = new HashMap<String, Object>();
			uriVariables.put("codigoCircuito", codigoCircuito);
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl( getUrlIndenovaObtenerCircuito() );
			builder.uriVariables(uriVariables);
			
			//-- Response 
			ResponseEntity<byte[]> response = getRestTemplate().exchange(
				builder.build().toString(),
				HttpMethod.GET,
				new HttpEntity<String>(null, headers),
				byte[].class
			);
			
			respuestaFinal = new DocumentoCircuitoResponse();
			respuestaFinal.setDocumento( response.getBody() );
    		
    	
		} catch (Exception e) {
			e.printStackTrace();
			throw new SivSOAException(
				Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_INDENOVA_DOCUMENTO_CIRCUITO).concat( Objects.toString(e.getMessage(), "") ), 
				null
			);
		}

    	LOGGER.info("Salio IndenovaRestClient#obtenerDocumentoCircuito(request)");
    	return respuestaFinal;
    }*/

    public DocumentoCircuitoResponse obtenerDocumentoCircuito(String codigoCircuito) {
    	DocumentoCircuitoResponse response = new DocumentoCircuitoResponse();
		
		try {
			File tmp = File.createTempFile("adnDigital", ".zip");
			
			CrearTokenResponse responseToken = this.crearToken();
			String auth = responseToken.getToken();
			URL url = new URL("https://www.esignabox.com/api/2.0/circuit/docs/" + codigoCircuito + "?doctype=signed");
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Bearer " + auth);
			
	        BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
	        FileOutputStream fileOutputStream = new FileOutputStream(tmp);
	        
	        byte dataBuffer[] = new byte[1024];
	        int bytesRead;
	        while((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
	        	fileOutputStream.write(dataBuffer, 0, bytesRead);
	        }
	        
	        response.setFileZip(tmp.getPath());
	        ZipInputStream zipStream = new ZipInputStream(new FileInputStream(tmp.getAbsoluteFile()));
			ZipEntry entry = null;
			while ((entry = zipStream.getNextEntry()) != null) {
	
			    String entryName = entry.getName();
			    System.out.println("entryName = " + entryName);
			    File tmpUnZip = File.createTempFile(entryName, ".pdf");
			    FileOutputStream out = new FileOutputStream(tmpUnZip);
	
			    byte[] byteBuff = new byte[4096];
			    int bytesReadZip = 0;
			    while ((bytesReadZip = zipStream.read(byteBuff)) != -1)
			    {
			        out.write(byteBuff, 0, bytesReadZip);
			    }
	
			    out.close();
			    zipStream.closeEntry();
			    response.setFilePdf(tmpUnZip.getPath());
			    response.setFilePdfPath(tmpUnZip.toPath());
			    response.setFilePdfName(tmpUnZip.getName());
			}
			zipStream.close();
			response.setError(false);
		}catch(MalformedURLException e1) {
			e1.printStackTrace();
			response.setError(true);
			response.setErrorMessage(e1.getMessage());
		}catch(IOException e2) {
			e2.printStackTrace();
			response.setError(true);
			response.setErrorMessage(e2.getMessage());
		}
    	return response;
    }
}
