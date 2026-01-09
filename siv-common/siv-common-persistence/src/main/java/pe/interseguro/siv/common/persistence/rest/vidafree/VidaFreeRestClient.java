package pe.interseguro.siv.common.persistence.rest.vidafree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

import com.google.gson.Gson;

import pe.interseguro.siv.common.exception.SivSOAException;
import pe.interseguro.siv.common.persistence.rest.base.BaseRestClientImpl;
import pe.interseguro.siv.common.persistence.rest.interseguro.request.ObtenerDatosUsuarioRequest;
import pe.interseguro.siv.common.persistence.rest.interseguro.response.AzmanTokenResponse;
import pe.interseguro.siv.common.persistence.rest.interseguro.response.ObtenerDatosClienteResponse;
import pe.interseguro.siv.common.persistence.rest.interseguro.response.ObtenerDatosUsuarioResponse;
import pe.interseguro.siv.common.persistence.rest.vidafree.request.ConsultaCotizacionDetalleRequest;
import pe.interseguro.siv.common.persistence.rest.vidafree.request.ConsultaCotizacionRequest;
import pe.interseguro.siv.common.persistence.rest.vidafree.request.CotizacionCoberturaRequest;
import pe.interseguro.siv.common.persistence.rest.vidafree.request.CotizacionRequest;
import pe.interseguro.siv.common.persistence.rest.vidafree.response.ConsultaCotizacionDetalleResponse;
import pe.interseguro.siv.common.persistence.rest.vidafree.response.GenericoResponse;
import pe.interseguro.siv.common.persistence.rest.vidafree.response.ObtenerCotizacionesResponse;
import pe.interseguro.siv.common.util.Constantes;
import pe.interseguro.siv.common.util.Utilitarios;

@Component
public class VidaFreeRestClient extends BaseRestClientImpl {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MessageSource messageSource;

	@Value("#{ environment['persistence.rest.client.interseguro.vidafree.servidor'] }")
	private String vidaFreeServidor;
	
	@Value("#{ environment['persistence.rest.client.interseguro.vidafree.apiKey'] }")
	private String vidaFreeApiKey;
	
	@Value("#{ environment['persistence.rest.client.interseguro.vidafree.obtenerCotizaciones'] }")
	private String vidaFreeObtenerCotizaciones;
	
	@Value("#{ environment['persistence.rest.client.interseguro.vidafree.obtenerCotizacionDetalle'] }")
	private String vidaFreeObtenerCotizacionDetalle;
	
	@Value("#{ environment['persistence.rest.client.interseguro.vidafree.guardar'] }")
	private String vidaFreeGuardarCotizacion;
	
	private String getUrlObtenerCotizaciones() {
		return vidaFreeServidor+vidaFreeObtenerCotizaciones;
	}
	
	private String getUrlObtenerCotizacionDetalle() {
		return vidaFreeServidor+vidaFreeObtenerCotizacionDetalle;
	}
	
	private String getUrlGuardarCotizacion() {
		return vidaFreeServidor+vidaFreeGuardarCotizacion;
	}
	
	public ObtenerCotizacionesResponse obtenerCotizaciones(String numeroDocumento) throws SivSOAException {
		LOGGER.info("Entro a VidaFreeRestClient#obtenerCotizaciones(obtenerCotizacionesRequest)");

		ObtenerCotizacionesResponse obtenerCotizacionesResponse = new ObtenerCotizacionesResponse();
		obtenerCotizacionesResponse.setTitle(getUrlObtenerCotizaciones());
		try {
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("X-Apikey", vidaFreeApiKey);
			ConsultaCotizacionRequest request = new ConsultaCotizacionRequest();
			request.setNumeroDocumento(numeroDocumento);
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl( getUrlObtenerCotizaciones() );
			ResponseEntity<ObtenerCotizacionesResponse> response = getRestTemplate().exchange(
				builder.build().toString(),
				HttpMethod.POST,
				new HttpEntity<ConsultaCotizacionRequest>(request, headers),
				ObtenerCotizacionesResponse.class
			);
			obtenerCotizacionesResponse = response.getBody();
			obtenerCotizacionesResponse.setStatus(200);
		} catch (Exception e) {
			obtenerCotizacionesResponse.setStatus(500);
			obtenerCotizacionesResponse.setCode("DIG-003");
			obtenerCotizacionesResponse.setMessage(e.getMessage());
			/*throw new SivSOAException(
				Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_INTERSEGURO_AZMAN_USUARIO).concat( Objects.toString(e.getMessage(), "") ), 
				null
			);*/
		}
		
		LOGGER.info("Salio a VidaFreeRestClient#obtenerCotizaciones(obtenerDatosUsuarioRequest)");
		return obtenerCotizacionesResponse;
	}
	
	public ConsultaCotizacionDetalleResponse obtenerCotizacionDetalle(ConsultaCotizacionDetalleRequest request) throws SivSOAException {
		LOGGER.info("Entro a VidaFreeRestClient#obtenerCotizacionDetalle(obtenerCotizacionesRequest)");

		ConsultaCotizacionDetalleResponse obtenerCotizacionesResponse = null;
		try {
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("X-Apikey", vidaFreeApiKey);
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl( getUrlObtenerCotizacionDetalle() );
			
			//-- Response 
			ResponseEntity<ConsultaCotizacionDetalleResponse> response = getRestTemplate().exchange(
				builder.build().toString(),
				HttpMethod.POST,
				new HttpEntity<ConsultaCotizacionDetalleRequest>(request, headers),
				ConsultaCotizacionDetalleResponse.class
			);
			obtenerCotizacionesResponse = response.getBody();
			
		} catch (Exception e) {
			throw new SivSOAException(
				Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_INTERSEGURO_VIDAFREE_CONSULTA).concat( Objects.toString(e.getMessage(), "") ), 
				null
			);
		}
		
		LOGGER.info("Salio a VidaFreeRestClient#obtenerCotizacionDetalle(obtenerDatosUsuarioRequest)");
		return obtenerCotizacionesResponse;
	}
	
	public GenericoResponse guardarCotizacion(String token, String numeroCotizacion, String numeroCotizacionReemplazo, String cumuloMoneda, String cumuloMonto, boolean flagClonacion) throws SivSOAException {
		LOGGER.info("Entro a VidaFreeRestClient#guardarCotizacion(token,numeroCotizacion,numeroCotizacionReemplazo,cumuloMoneda,cumuloMonto,flagClonacion)");

		GenericoResponse guardarResponse = null;
		try {
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("X-Apikey", vidaFreeApiKey);
			headers.set("X-Token", token);
						
			ConsultaCotizacionDetalleRequest request0 = new ConsultaCotizacionDetalleRequest();
			request0.setIdAgente("");
			request0.setNumeroCotizacion(numeroCotizacion);
			ConsultaCotizacionDetalleResponse cotizacionData = obtenerCotizacionDetalle(request0);
			
			CotizacionRequest request1 = new CotizacionRequest();
			request1.setCodMoneda(Integer.valueOf(cotizacionData.getData().getDetalleCotizacion().getCodMoneda()));
			request1.setCodPlan(Integer.valueOf(cotizacionData.getData().getDetalleCotizacion().getCodPlan()));
			request1.setCodProducto(cotizacionData.getData().getDetalleCotizacion().getCodProducto());
			request1.setCondicionFumador(cotizacionData.getData().getCliente().getCondicionFumador());
			request1.setCumuloMoneda("");
			request1.setCumuloMonto(0);
			request1.setCurrentCumuloMonto(0.0);
			request1.setEdadContratacion(Integer.valueOf(cotizacionData.getData().getCliente().getEdadContratacion()));
			request1.setFechaNacimiento(cotizacionData.getData().getCliente().getFechaNacimiento());
			request1.setFrecuenciaPago(cotizacionData.getData().getDetalleCotizacion().getFrecuenciaPago());
			request1.setInversa(Boolean.FALSE);
			String numeroCotizacionTemp = !flagClonacion ? cotizacionData.getData().getDetalleCotizacion().getNumeroCotizacion() : numeroCotizacionReemplazo;
			request1.setNumeroCotizacion(numeroCotizacionTemp);
			request1.setPorcentajeDevolucion(Integer.valueOf(cotizacionData.getData().getDetalleCotizacion().getPorcentajeDevolucion()));
			request1.setPrimaInversaTotal(0.0);
			request1.setSexo(cotizacionData.getData().getCliente().getSexo());
			List<CotizacionCoberturaRequest> coberturas = new ArrayList<>();
			cotizacionData.getData().getDetalleCotizacion().getCoberturas().forEach(c -> {
				coberturas.add(new CotizacionCoberturaRequest(
						c.getCodCobertura(), 
						c.getSumaAsegurada().toString(), 
						c.getPeriodoVigencia(), 
						c.getPeriodoPago(), 
						c.getPrimaRecurrente()));
			});
			request1.setCoberturas(coberturas);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl( getUrlGuardarCotizacion() );
			
			LOGGER.info("Token====>>>" + token);
			Gson gson = new Gson();
			LOGGER.info("Body====>>>" + gson.toJson(request1));
			ResponseEntity<GenericoResponse> response = getRestTemplate().exchange(
				builder.build().toString(),
				HttpMethod.POST,
				new HttpEntity<CotizacionRequest>(request1, headers),
				GenericoResponse.class
			);
			guardarResponse = response.getBody();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new SivSOAException(
				Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_GENERICO_SOAEXCEPTION).concat( Objects.toString(e.getMessage(), "") ), 
				null
			);
		}
		
		LOGGER.info("Salio a VidaFreeRestClient#VidaFreeRestClient#guardarCotizacion(token,numeroCotizacion,numeroCotizacionReemplazo,cumuloMoneda,cumuloMonto,flagClonacion)");
		return guardarResponse;
	}
}
