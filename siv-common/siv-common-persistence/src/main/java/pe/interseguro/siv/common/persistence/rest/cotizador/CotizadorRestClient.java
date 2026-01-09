package pe.interseguro.siv.common.persistence.rest.cotizador;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pe.interseguro.siv.common.exception.ErrorResourceDTO;
import pe.interseguro.siv.common.exception.SivSOAException;
import pe.interseguro.siv.common.persistence.rest.acsele.AcseleRestClient;
import pe.interseguro.siv.common.persistence.rest.acsele.response.TerceroResponse;
import pe.interseguro.siv.common.persistence.rest.base.BaseRestClientImpl;
import pe.interseguro.siv.common.persistence.rest.cotizador.request.AsegurableCrmRequest;
import pe.interseguro.siv.common.persistence.rest.cotizador.response.*;
import pe.interseguro.siv.common.persistence.rest.cumulo.Cumulo2Response;
import pe.interseguro.siv.common.persistence.rest.cumulo.CumuloResponse;
import pe.interseguro.siv.common.util.Constantes;
import pe.interseguro.siv.common.util.Utilitarios;

@Component
public class CotizadorRestClient extends BaseRestClientImpl {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private AcseleRestClient acseleRestClient;

    @Value("#{ environment['persistence.rest.client.interseguro.cotizadorcore.servidor'] }")
    private String servidorCotizador;
    @Value("#{ environment['persistence.rest.client.pg.api'] }")
    private String servidorCotizadorPG;

	@Value("#{ environment['persistence.rest.client.pg.api.rpc.cumulo'] }")
	private String servidorCotizadorPGRpcCumulo;
	@Value("#{ environment['persistence.rest.client.api.cumulo'] }")
	private String servidorApiCumulo;

	@Value("#{ environment['flag_api_cumulo'] }")
	private String flagCumulo;

    @Value("#{ environment['persistence.rest.client.pg.api.key'] }")
    private String servidorCotizadorPgKey;
    @Value("#{ environment['persistence.rest.client.pg.crear.cotizacion'] }")
    private String servicioGenerarCorrelativoCotizacionPG;
    @Value("#{ environment['persistence.rest.client.pg.cumulo'] }")
    private String servicioCumuloPG;
    @Value("#{ environment['persistence.rest.client.interseguro.cotizadorcore.obtenerCumulo'] }")
    private String servicioObtenerCumulo;
    @Value("#{ environment['persistence.rest.client.interseguro.cotizadorcore.generarCorrelativoCotizacion'] }")
    private String servicioGenerarCorrelativoCotizacion;
    @Value("#{ environment['persistence.rest.client.interseguro.cotizadorcore.obtenerExigenciasMedicas'] }")
    private String servicioExigenciasMedicas;
    @Value("#{ environment['persistence.rest.client.interseguro.cotizadorcore.asegurable'] }")
    private String servicioAsegurable;
    
    private String getUrlObtenerCumulo() {
        return servidorCotizador + servicioObtenerCumulo;
    }

    private String getUrlObtenerCumuloNuevo() {
        return servidorCotizadorPG + servicioCumuloPG;
    }

    private String getUrlObtenerExigenciasMedicas() {
		return servidorCotizador+servicioExigenciasMedicas;
	}
    private String getUrlGenerarCorrelativoCotizacion() {
		return servidorCotizador+servicioGenerarCorrelativoCotizacion;
	}
    private String getUrlAsegurable() {
		return servidorCotizador+servicioAsegurable;
	}
    
    private final Gson gson = new Gson();
    
    public ObtenerCumuloResponse obtenerCumuloRpc(String idTercero) {
        LOGGER.info("Entro CotizadorRestClient#obtenerCumuloRpc()");
        ObtenerCumuloResponse response = new ObtenerCumuloResponse();
		String url=servidorCotizadorPGRpcCumulo;
		response.setTitle(url);
		LOGGER.info("obtenerCumuloRpc url: " + url);
        try {
            OkHttpClient client = new OkHttpClient().newBuilder().readTimeout(240, TimeUnit.SECONDS).build();

            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), "{\"idTercero\":"+idTercero+"}");
			LOGGER.info("obtenerCumuloRpc request => "+"{\"idTercero\":"+idTercero+"}");
            Request request = new Request.Builder()
                    .url(url)
                    .method("POST", body)
                    .addHeader("X-ApiKey", servidorCotizadorPgKey)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response res = client.newCall(request).execute();
			LOGGER.info("Respuesta del servidor: " + res.toString());
            int statusCode = res.code();
            if (statusCode >= 200 && statusCode < 300) {
                String responseBody = res.body().string();
                LOGGER.info("Respuesta del servidor: " + responseBody);
                Gson gson = new Gson();
                CumuloResponse apiResponse = gson.fromJson(responseBody, CumuloResponse.class);
                response.setIdTercero(idTercero);
                response.setMontosdet(apiResponse.getData().getCumulo().getMontosdet());
                response.setStatusHttp("200");
            } else {
                response.setStatusHttp("500");
                response.setCode("COT-001");
                response.setMessage("Error al obtener cumulo: " +statusCode);
				LOGGER.info("Error al obtener cumulo: " +statusCode);
            }
        } catch (Exception e) {
            response.setStatusHttp("500");
            response.setCode("COT-001");
            response.setMessage("Error: " + e.getMessage());
			LOGGER.info("Error al obtener obtenerCumuloRpc: " + e.getMessage());
        }
        LOGGER.info("Salio CotizadorRestClient#obtenerCumuloRpc()");
        return response;
    }

	public ObtenerCumuloResponse obtenerCumuloApi(String documento) {
		LOGGER.info("Entro CotizadorRestClient#obtenerCumuloApi()");
		ObtenerCumuloResponse response = new ObtenerCumuloResponse();
		String url=servidorApiCumulo+"/"+documento;
		response.setTitle(url);
		LOGGER.info("obtenerCumuloApi url: " + url);
		try {
			OkHttpClient client = new OkHttpClient().newBuilder().readTimeout(240, TimeUnit.SECONDS).build();
			Request request = new Request.Builder().url(url).get().addHeader("Content-Type", "application/json").build();
			Response res = client.newCall(request).execute();
			LOGGER.info("Respuesta del servidor: " + res.toString());
			int statusCode = res.code();
			if (statusCode >= 200 && statusCode < 300) {
				String responseBody = res.body().string();
				LOGGER.info("Respuesta del servidor: " + responseBody);
				Gson gson = new Gson();
				Cumulo2Response apiResponse = gson.fromJson(responseBody, Cumulo2Response.class);
				List<MontoDetalleResponse> montoDetalleResponses=new ArrayList<>();
				
				if(apiResponse.getCumuloSoles()!=null){
					MontoDetalleResponse montoDetalleResponse= new MontoDetalleResponse();
					montoDetalleResponse.setIdmoneda("2163");
					montoDetalleResponse.setMonto(String.valueOf(apiResponse.getCumuloSoles()));
					montoDetalleResponses.add(montoDetalleResponse);
				}
				if(apiResponse.getCumuloDolar()!=null){
					MontoDetalleResponse montoDetalleResponse= new MontoDetalleResponse();
					montoDetalleResponse.setIdmoneda("2123");
					montoDetalleResponse.setMonto(String.valueOf(apiResponse.getCumuloDolar()));
					montoDetalleResponses.add(montoDetalleResponse);
				}
				response.setMontosdet(montoDetalleResponses);
				response.setStatusHttp("200");
			}
			else {
				response.setStatusHttp("500");
				response.setCode("COT-001");
				response.setMessage("Error al obtener obtenerCumuloApi: " +statusCode);
				LOGGER.info("Error al obtener obtenerCumuloApi: " +statusCode);
			}
		} catch (Exception e) {
			response.setStatusHttp("500");
			response.setCode("COT-001");
			response.setMessage("Error: " + e.getMessage());
			LOGGER.info("Error al obtener cumulo: " + e.getMessage());
		}
		LOGGER.info("Salio CotizadorRestClient#obtenerCumuloApi()");
		return response;
	}

	public ObtenerCumuloResponse obtenerCumuloGeneral(String numeroDocumento, String tipoDocumento) {
		LOGGER.info("Entro CotizadorRestClient#obtenerCumulo()");
		ObtenerCumuloResponse response = new ObtenerCumuloResponse();
		if(flagCumulo.equals("1")){
			String idTercero = "";
			String tipoDocumentoAcsele = tipoDocumento == "2" ? "PersonaJuridica" : "PersonaNatural";
				TerceroResponse tercero = acseleRestClient.obtenerIDTercero(tipoDocumentoAcsele, numeroDocumento);
				if (tercero.getStatusHttp() == "200" && tercero.getTerceros() != null && tercero.getErrores().size() == 0) {
					idTercero = tercero.getTerceros().get(0).getIdTercero();
				}
				if(!idTercero.equals("")){
					response=obtenerCumuloRpc(idTercero);
				}else{
					LOGGER.error("obtenerCumuloGeneral error al obtener idTercero");
					response.setStatusHttp("500");
				}
		}else if(flagCumulo.equals("2")){
			response=obtenerCumuloApi(numeroDocumento);
		}
		LOGGER.info("Salio CotizadorRestClient#obtenerCumulo()");
		return response;
	}
    
    public List<ExigenciaMedicaResponse> obtenerExigenciasMedicas(String IdProducto, String moneda, String edad, 
    		String cumulo, String capitalFallecimiento, String capitalVida) {
    	LOGGER.info("Entro CotizadorRestClient#obtenerCumulo()");
    	ObtenerExigenciaMedicaResponse response = null;
    	String url = getUrlObtenerExigenciasMedicas().concat("/").concat(IdProducto)
    			.concat("/").concat(moneda)
    			.concat("/").concat(edad)
    			.concat("/").concat(cumulo)
    			.concat("/").concat(capitalFallecimiento);
    			//.concat("/").concat(capitalVida); TODO Pendiente de pase a producción
    	LOGGER.info("URL EXIGENCIAS MEDICAS");
    	LOGGER.info(url);
    	try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl( url );
			response = getRestTemplate().getForObject(builder.build().toString(), ObtenerExigenciaMedicaResponse.class);
    	} catch(Exception e) {
    		e.printStackTrace();
			throw new SivSOAException(
				Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_INDENOVA_CREAR_TOKEN).concat( Objects.toString(e.getMessage(), "") ), 
				null
			);
    	}
    	LOGGER.info("Salio CotizadorRestClient#obtenerCumulo()");
    	return response != null ? response.getRequerimientos() : null;
    }
    
    public ObtenerCorrelativoResponse generarCorrelativo() {
    	LOGGER.info("Entro CotizadorRestClient#obtenerCumulo()");
    	ObtenerCorrelativoResponse response = new ObtenerCorrelativoResponse();
    	response.setTitle(getUrlGenerarCorrelativoCotizacion());
    	try { 
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl( getUrlGenerarCorrelativoCotizacion() );
			
			MultiValueMap<String, String> map = new LinkedMultiValueMap();
			map.add("codcli", "31683");
			map.add("login", "adn");			
			response = getRestTemplate().postForObject(builder.build().toString(), map, ObtenerCorrelativoResponse.class);
			response.setStatusHttp("200");
    	} catch(Exception e) {
    		response.setStatusHttp("500");
    		response.setCode("CRM-007");
    		response.setMessage("Error: " + e.getMessage());
    		/* e.printStackTrace();
			throw new SivSOAException(
				Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_INDENOVA_CREAR_TOKEN).concat( Objects.toString(e.getMessage(), "") ), 
				null
			); */
    	}
    	LOGGER.info("Salio CotizadorRestClient#obtenerCumulo()");
    	return response;
    }
    
    public AsegurableCrmResponse obtenerAsegurable(String codigoCliente, String apellidoPaterno, String apellidoMaterno, String nombres,
    		String fechaNacimiento, String sexo, String tipoDocumento, String numeroDocumento, String idCrm, String idCotizacion, 
    		String idCotizacionCrm, String idOportunidadCrm, String flagUpdate, String idTerceroCumulo,
    		String codigoAgente, String nombreAgente, String usuario) {
    	LOGGER.info("Entro CotizadorRestClient#obtenerAsegurable()");
    	AsegurableCrmRequest request = new AsegurableCrmRequest();

    	AsegurableCrmResponse response = null;
    	String url = getUrlAsegurable();
    	try { 
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl( url);
			
			MultiValueMap<String, String> map = new LinkedMultiValueMap();
			map.add("id", codigoCliente);
			map.add("cod", codigoAgente);
			map.add("login", usuario);
			map.add("apepat", apellidoPaterno);
			map.add("nom", nombres);
			map.add("fenac", fechaNacimiento);
			map.add("flagsex", sexo);
			map.add("flagfum", "");
			map.add("nrodoc", numeroDocumento);
			map.add("tipdoc", tipoDocumento);
			map.add("apemat", apellidoMaterno);
			map.add("profes", "");
			map.add("flagigv", "");
			map.add("nomAge", nombreAgente);
			map.add("otros", null);
			map.add("idCrm", idCrm);
			map.add("idCot", idCotizacion);
			map.add("idCotCrm", idCotizacionCrm);
			map.add("idOporCrm", idOportunidadCrm);
			map.add("flagUpdate", flagUpdate);
			map.add("idTerceroCumulo", idTerceroCumulo);
			response = getRestTemplate().postForObject(builder.build().toString(), map, AsegurableCrmResponse.class);
    	} catch(Exception e) {
    		e.printStackTrace();
			throw new SivSOAException(
				Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_COTIZADOR_ASEGURABLE).concat( Objects.toString(e.getMessage(), "") ), 
				null
			);
    	}
    	LOGGER.info("Salio CotizadorRestClient#obtenerAsegurable()");
    	return response;
    }
}
