package pe.interseguro.siv.common.persistence.rest.interseguro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.gson.Gson;
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
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import pe.interseguro.siv.common.exception.SivSOAException;
import pe.interseguro.siv.common.persistence.rest.base.BaseRestClientImpl;
import pe.interseguro.siv.common.persistence.rest.interseguro.request.*;
import pe.interseguro.siv.common.persistence.rest.interseguro.response.*;
import pe.interseguro.siv.common.util.Constantes;
import pe.interseguro.siv.common.util.Utilitarios;

/**
 * @author digital-is
 *
 */
@Component
public class InterseguroRestClient extends BaseRestClientImpl {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MessageSource messageSource;

	@Value("#{ environment['persistence.rest.client.interseguro.azman.servidor'] }")
	private String azmanServidor;

	@Value("#{ environment['persistence.rest.client.interseguro.azman.token'] }")
	private String azmanToken;

	@Value("#{ environment['persistence.rest.client.interseguro.azman.obtenerDatosUsuario'] }")
	private String azmanObtenerDatosUsuario;

	@Value("#{ environment['persistence.rest.client.interseguro.azman.token.grantType'] }")
	private String azmanTokenGrantType;

	@Value("#{ environment['persistence.rest.client.interseguro.azman.token.username'] }")
	private String azmanTokenUsername;

	@Value("#{ environment['persistence.rest.client.interseguro.azman.token.password'] }")
	private String azmanTokenPassword;

	@Value("#{ environment['persistence.rest.client.interseguro.azman.token.clientId'] }")
	private String azmanTokenClientId;

	@Value("#{ environment['persistence.rest.client.interseguro.azman.token.clientSecret'] }")
	private String azmanTokenClientSecret;

	@Value("#{ environment['persistence.rest.client.interseguro.azman.obtenerDatosUsuario.aplicacion'] }")
	private String azmanObtenerDatosUsuarioAplicacion;

	@Value("#{ environment['persistence.rest.client.interseguro.azman.obtenerDatosUsuario.dominio'] }")
	private String azmanObtenerDatosUsuarioDominio;

	@Value("#{ environment['persistence.rest.client.interseguro.correo.servidor2'] }")
	private String servidorCorreo2;
	
	@Value("#{ environment['persistence.rest.client.interseguro.correo.servidor.digital'] }")
	private String servidorEnviarCorreoDigital;
	
	@Value("#{ environment['persistence.rest.client.interseguro.correo.enviarCorreo.digital'] }")
	private String correoEnviarCorreoDigital;

	@Value("#{ environment['persistence.rest.client.interseguro.correo.enviarCorreo'] }")
	private String correoEnviarCorreo;
	@Value("#{ environment['persistence.rest.client.pg.api.notify'] }")
	private String servidorApiNotify;
	@Value("#{ environment['persistence.rest.client.interseguro.correo.enviarCorreo2'] }")
	private String correoEnviarCorreo2;

	@Value("#{ environment['persistence.rest.client.interseguro.correo.servidor'] }")
	private String servidorCorreo;

	@Value("#{ environment['persistence.rest.client.pg.api.notify.priority'] }")
	private String apiNotifyPriority;
	@Value("#{ environment['persistence.rest.client.interseguro.correo.servidorSME'] }")
	private String servidorCorreoSME;

	@Value("#{ environment['persistence.rest.client.interseguro.correo.enviarCorreoSME'] }")
	private String correoEnviarCorreoSME;

	@Value("#{ environment['persistence.rest.client.interseguro.sms.servidor'] }")
	private String servidorSms;

	@Value("#{ environment['persistence.rest.client.interseguro.sms.enviarSms'] }")
	private String smsEnviarSms;

	@Value("#{ environment['persistence.rest.client.interseguro.correo.enviarCorreo.solicitud.remitente'] }")
	private String enviarCorreoSolicitudRemitente;

	@Value("#{ environment['persistence.rest.client.interseguro.correo.enviarCorreo.solicitud.remitente.display'] }")
	private String enviarCorreoSolicitudRemitenteDisplay;

	@Value("#{ environment['persistence.rest.client.interseguro.sms.servidor2'] }")
	private String servidorSms2;

	@Value("#{ environment['persistence.rest.client.interseguro.sms.enviarSms2'] }")
	private String smsEnviarSms2;

	@Value("#{ environment['persistence.rest.client.interseguro.digital.servidor'] }")
	private String servidorDigital;

	@Value("#{ environment['persistence.rest.client.interseguro.digital.token'] }")
	private String digitalObtenerToken;

	@Value("#{ environment['persistence.rest.client.interseguro.digital.token.grantType'] }")
	private String digitalTokenGrantType;

	@Value("#{ environment['persistence.rest.client.interseguro.digital.token.clientId'] }")
	private String digitalTokenClientId;

	@Value("#{ environment['persistence.rest.client.interseguro.digital.token.clientSecret'] }")
	private String digitalTokenClientSecret;

	@Value("#{ environment['persistence.rest.client.interseguro.digital.obtenerCliente'] }")
	private String digitalObtenerCliente;

	@Value("#{ environment['persistence.rest.client.interseguro.sms.enviarSms.v'] }")
	private String versionEnvioSms;

	@Value("#{ environment['persistence.rest.client.interseguro.sms.enviarMail.v'] }")
	private String versionEnvioMail;

	@Value("#{ environment['url.linkPago'] }")
	private String urlLinkPago;

	@Value("#{ environment['url.linkPago.link'] }")
	private String link;

	@Value("#{ environment['persistence.rest.client.pg.api.notificaciones.sms'] }")
	private String apiNotificacionesSms;

	private String getUrlEnviarCorreo2() {
		return servidorCorreo2 + correoEnviarCorreo2 + "?product=TI";
	}
	
	private String getUrlEnviarCorreoDigita() {
		return servidorEnviarCorreoDigital + correoEnviarCorreoDigital + "?product=TI";
	}

	private String getUrlEnviarCorreo() {
		return servidorCorreo + correoEnviarCorreo;
	}
	
	private String getUrlEnviarCorreoDigital() {
		return servidorEnviarCorreoDigital + correoEnviarCorreoDigital + "?product=TI";
	}
	
//    private String getUrlEnviarCorreoSME() {
//		return servidorCorreoSME+correoEnviarCorreoSME;
//	}

	private String getUrlEnviarSms() {
		return servidorSms + smsEnviarSms;
	}

	private String getUrlEnviarSms2() {
		return servidorSms2 + smsEnviarSms2;
	}

	private String getUrlAzmanToken() {
		return azmanServidor + azmanToken;
	}

	private String getUrlObtenerDatosUsuario() {
		return azmanServidor + azmanObtenerDatosUsuario;
	}

	private String getUrlDigitalToken() {
		return servidorDigital + digitalObtenerToken;
	}

	private String getUrlDigitalObtenerCliente() {
		return servidorDigital + digitalObtenerCliente;
	}

	private String getLinkPago() {
		return urlLinkPago + link;
	}

	private String getUrlEnviarSmsNotificaciones() {
		return servidorSms2 + smsEnviarSms2;
	}

	/**
	 * 
	 * @param obtenerDatosUsuarioRequest
	 * @return
	 * @throws SivSOAException
	 */
	public ObtenerDatosUsuarioResponse obtenerDatosUsuario(ObtenerDatosUsuarioRequest obtenerDatosUsuarioRequest)
			throws SivSOAException {
		logger.info("Entro a InterseguroRestClient#obtenerDatosUsuario(obtenerDatosUsuarioRequest)");
		ObtenerDatosUsuarioResponse obtenerDatosUsuarioResponse = new ObtenerDatosUsuarioResponse();
		// -- Obtener Token
		MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
		request.add("grant_type", this.azmanTokenGrantType); // password
		request.add("username", this.azmanTokenUsername); // adnwepro
		request.add("password", this.azmanTokenPassword);
		request.add("client_id", this.azmanTokenClientId);
		request.add("client_secret", this.azmanTokenClientSecret);
		AzmanTokenResponse tokenResponse = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			tokenResponse = obtenerPostPorObjeto(getUrlAzmanToken(),
					new HttpEntity<MultiValueMap<String, String>>(request, headers), AzmanTokenResponse.class);

		} catch (HttpClientErrorException | HttpServerErrorException e) {
			obtenerDatosUsuarioResponse.setStatusHttp(String.valueOf(e.getRawStatusCode()));
			obtenerDatosUsuarioResponse.setCode("AZM-001");
			obtenerDatosUsuarioResponse.setMessage(e.getMessage());
			obtenerDatosUsuarioResponse.setTitle(getUrlAzmanToken());
		} catch (Exception e) {
			obtenerDatosUsuarioResponse.setStatusHttp("400");
			obtenerDatosUsuarioResponse.setCode("AZM-001");
			obtenerDatosUsuarioResponse.setMessage(e.getMessage());
			obtenerDatosUsuarioResponse.setTitle(getUrlAzmanToken());
		}

		// -- Buscar usuario
		if (tokenResponse != null) {
			try {
				HttpHeaders headers1 = new HttpHeaders();
				headers1.setContentType(MediaType.APPLICATION_JSON);
				headers1.set("Authorization", "Bearer " + tokenResponse.getAccessToken());

				obtenerDatosUsuarioResponse = obtenerPostPorObjeto(getUrlObtenerDatosUsuario(),
						new HttpEntity<ObtenerDatosUsuarioRequest>(obtenerDatosUsuarioRequest, headers1),
						ObtenerDatosUsuarioResponse.class);
				obtenerDatosUsuarioResponse.setStatusHttp("200");
			} catch (HttpClientErrorException | HttpServerErrorException e) {
				obtenerDatosUsuarioResponse.setStatusHttp(String.valueOf(e.getRawStatusCode()));
				obtenerDatosUsuarioResponse.setCode("AZM-002");
				obtenerDatosUsuarioResponse.setMessage(e.getMessage());
				obtenerDatosUsuarioResponse.setTitle(getUrlObtenerDatosUsuario());
			} catch (Exception e) {
				obtenerDatosUsuarioResponse.setStatusHttp("400");
				obtenerDatosUsuarioResponse.setCode("AZM-002");
				obtenerDatosUsuarioResponse.setMessage(e.getMessage());
				obtenerDatosUsuarioResponse.setTitle(getUrlObtenerDatosUsuario());
			}
		}

		logger.info("Salio a InterseguroRestClient#obtenerDatosUsuario(obtenerDatosUsuarioRequest)");
		return obtenerDatosUsuarioResponse;
	}

	/**
	 * envio de correo desde api digital
	 * 
	 * @param enviarCorreoRequest
	 * @return
	 * @throws SivSOAException
	 */
	public EnviarCorreoResponse enviarCorreo(EnviarCorreoRequestNuevo enviarCorreoRequest, String motivo, String cotizacion, String documento) throws SivSOAException {
		logger.info("Entro a InterseguroRestClient#enviarCorreo(enviarCorreoRequest)");

		EnviarCorreoResponse enviarCorreoResponse=new EnviarCorreoResponse();
		if (Integer.parseInt(versionEnvioMail) == 1) {
			EnviarCorreoRequest enviarCorreoReq = new EnviarCorreoRequest();

			enviarCorreoReq.setpRemitente(this.enviarCorreoSolicitudRemitente);
			enviarCorreoReq.setpDisplayName(this.enviarCorreoSolicitudRemitenteDisplay);
			enviarCorreoReq.setpDestinatario(enviarCorreoRequest.getTo().get(0).getEmail());
			enviarCorreoReq.setpAsunto(enviarCorreoRequest.getSubject());
			enviarCorreoReq.setpMensaje(enviarCorreoRequest.getHtmlContent());
			if (!CollectionUtils.isEmpty(enviarCorreoRequest.getAttachments())) {
				List<String> attach = new ArrayList<>();
				enviarCorreoRequest.getAttachments().forEach(x -> attach.add(x.getUrl()));
				enviarCorreoReq.setpRutaArchivoAdjunto(attach);
			}
			return enviarCorreoOld(enviarCorreoReq);
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		/*try {
			logger.info("INICIO DEL TRY DEL SENDGRID");
			
			ResponseEntity<EnviarCorreoResponse> response = getRestTemplate().exchange(getUrlEnviarCorreo2(),
					HttpMethod.POST, new HttpEntity<EnviarCorreoRequestNuevo>(enviarCorreoRequest, headers),
					EnviarCorreoResponse.class);

			logger.info("Seteo el response: {}", response);

			enviarCorreoResponse = response.getBody();

			logger.info("FIN DEL TRY DEL SENDGRID");
			
		} catch (Exception e) {
			throw new SivSOAException(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_INTERSEGURO_CORREO)
							.concat(Objects.toString(e.getMessage(), "")),
					null);
		}*/
		
		try {
			logger.info("INICIO DEL TRY DEL SENDINBLUE");
			
			ResponseEntity<EnviarCorreoResponse> response2 = getRestTemplate().exchange(getUrlEnviarCorreoDigita(),
					HttpMethod.POST, new HttpEntity<EnviarCorreoRequestNuevo>(enviarCorreoRequest, headers),
					EnviarCorreoResponse.class);
			
			logger.info("Seteo el response: {}", response2);

			enviarCorreoResponse = response2.getBody();
			
			logger.info("FIN DEL TRY DEL SENDINBLUE");
			
		} catch (Exception e) {
			throw new SivSOAException(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_INTERSEGURO_CORREO)
							.concat(Objects.toString(e.getMessage(), "")),
					null);
		}

		try{
			//Gson gson = new Gson();
			logger.info("INICIO ENVIO CORREO");
			enviarCorreoRequest.setNro_documento(documento);
			enviarCorreoRequest.setCotizacion(cotizacion);
			enviarCorreoRequest.setMotivo_notificacion(motivo);
			enviarCorreoRequest.setPriority(apiNotifyPriority);
			ResponseEntity<EnviarCorreoResponseNotify> response3 = getRestTemplate().exchange(servidorApiNotify,
					HttpMethod.POST, new HttpEntity<EnviarCorreoRequestNuevo>(enviarCorreoRequest, headers),
					EnviarCorreoResponseNotify.class);

			logger.info("Seteo el response API NOTIFY: {}", response3);
			logger.info("FIN ENVIO CORREO");
			Gson gson = new Gson();
			logger.info("Seteo el response-body API NOTIFY: {}", gson.toJson(response3.getBody()));
			enviarCorreoResponse.setStatusCode(String.valueOf(response3.getStatusCode()));
			enviarCorreoResponse.setContent(gson.toJson(response3.getBody()));

			/*logger.info("Seteo el response-body API NOTIFY: {}", gson.toJson(response3.getBody()));
			enviarCorreoResponse.setStatusCode(String.valueOf(response3.getStatusCode()));
			enviarCorreoResponse.setContent(gson.toJson(response3.getBody()));



			logger.info("GUARDANDO DATOS DE NOTIFICACION");
			Notificacion notificacion = new Notificacion();
			notificacion.setCotizacion(cotizacion);
			notificacion.setNro_documento(documento);
			notificacion.setTipo_notificación("EMAIL");
			notificacion.setMotivo_notificacion(motivo);
			notificacion.setDestinatarios(enviarCorreoRequest.getTo().stream().map(Remitente::getEmail).collect(Collectors.joining(", ")));
			//notificacion.setRequest(gson.toJson(enviarCorreoRequest));
			notificacion.setResponse_status(String.valueOf(response3.getStatusCode()));
			notificacion.setResponse_header(gson.toJson(response3.getHeaders()));
			notificacion.setResponse_body(gson.toJson(response3.getBody()));
			if(response3.getStatusCode().value()== 201){
				notificacion.setIdCreated(response3.getBody().getIdCreated());
			}
			notificacion.setFecha_creacion(new Date());
			notificacionRepository.save(notificacion);
			logger.info("FIN GUARDANDO DATOS DE NOTIFICACION");*/


		} catch (Exception e) {
			throw new SivSOAException(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_INTERSEGURO_CORREO)
							.concat(Objects.toString(e.getMessage(), "")),
					null);
		}

		logger.info("Salio a InterseguroRestClient#enviarCorreo(enviarCorreoRequest)");
		return enviarCorreoResponse;
	}

	/**
	 * Enviar emails consumiendo un servicio realizado en interseguro Enviar correo
	 * desde sme
	 * 
	 * @param enviarCorreoRequest
	 * @return
	 * @throws SivSOAException
	 */
	public EnviarCorreoResponse enviarCorreoOld(EnviarCorreoRequest enviarCorreoRequest) throws SivSOAException {
		logger.info("Entro a InterseguroRestClient#enviarCorreo(enviarCorreoRequest)");
		EnviarCorreoResponse correoResponse = new EnviarCorreoResponse();

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			ResponseEntity<String> response = getRestTemplate().exchange(getUrlEnviarCorreo(), HttpMethod.POST,
					new HttpEntity<EnviarCorreoRequest>(enviarCorreoRequest, headers), String.class);

			JsonNode root = new ObjectMapper().readTree(response.getBody());

			correoResponse.setStatusCode(response.getStatusCodeValue() + "");
			correoResponse.setContent(root.toString());
			logger.info("Response enviarCorreoOld {}", response);
		} catch (Exception e) {
			throw new SivSOAException(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_INTERSEGURO_CORREO)
							.concat(Objects.toString(e.getMessage(), "")),
					null);
		}

		logger.info("Salio a InterseguroRestClient#enviarCorreo(enviarCorreoRequest)");
		return correoResponse;
	}

	/**
	 * Enviar SMS consumiendo un servicio creado por Interseguro
	 * 
	 * @param enviarSmsRequest
	 * @return
	 * @throws SivSOAException
	 */
	public EnviarSmsResponse enviarSms(EnviarSmsRequest enviarSmsRequest) throws SivSOAException {
		logger.info("Entro a InterseguroRestClient#enviarSms(enviarSmsRequest)");

		EnviarSmsResponse enviarSmsResponse = new EnviarSmsResponse();
		if (Integer.parseInt(versionEnvioSms) != 1) {
			return enviarSmsV2(enviarSmsRequest);
		}
		try {

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			EnviarSmsOldRequest smsOldRequest = new EnviarSmsOldRequest();
			smsOldRequest.setCelular(enviarSmsRequest.getCelular());
			smsOldRequest.setMensaje(enviarSmsRequest.getMensaje());
			ResponseEntity<String> response = getRestTemplate().exchange(getUrlEnviarSms(), HttpMethod.POST,
					new HttpEntity<>(smsOldRequest, headers), String.class);

			JsonNode root = new ObjectMapper().readTree(response.getBody());
			root = new ObjectMapper().readTree(root.asText());
			enviarSmsResponse.setEstado(root.path("estado").asText(""));
			enviarSmsResponse.setCodigo(root.path("codigo").asText(""));

		} catch (Exception e) {
			throw new SivSOAException(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_INTERSEGURO_SMS)
							.concat(Objects.toString(e.getMessage(), "")),
					null);
		}

		logger.info("Salio a InterseguroRestClient#enviarSms(enviarSmsRequest)");
		return enviarSmsResponse;
	}

	public EnviarSmsResponse enviarSmsV2(EnviarSmsRequest enviarSmsRequest) throws SivSOAException {
		logger.info("Entro a InterseguroRestClient#enviarSms(enviarSmsRequest)");

		EnviarSmsResponse enviarSmsResponse;
		try {

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			enviarSmsRequest.setFrom("INTERSEGURO");
			enviarSmsRequest.setMessageType("normal");

			ResponseEntity<EnviarSmsResponse> response = getRestTemplate().exchange(getUrlEnviarSms2(), HttpMethod.POST,
					new HttpEntity<EnviarSmsRequest>(enviarSmsRequest, headers), EnviarSmsResponse.class);
			enviarSmsResponse = response.getBody();
			if (enviarSmsResponse == null) {
				throw new NullPointerException("ENVIAR SMS NULL");
			}

		} catch (Exception e) {
			throw new SivSOAException(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_INTERSEGURO_SMS)
							.concat(Objects.toString(e.getMessage(), "")),
					null);
		}

		logger.info("Salio a InterseguroRestClient#enviarSms(enviarSmsRequest)");
		return enviarSmsResponse;
	}

	public DigitalTokenResponse obtenerTokenDigital() throws SivSOAException {
		logger.info("Entro a InterseguroRestClient#obtenerTokenDigital()");

		// -- Obtener Token
		DigitalTokenRequest requestToken = new DigitalTokenRequest();
		requestToken.setGrantType(this.digitalTokenGrantType);
		requestToken.setCliendId(this.digitalTokenClientId);
		requestToken.setClientSecret(this.digitalTokenClientSecret);

		DigitalTokenResponse tokenResponse = new DigitalTokenResponse();
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			logger.info("URL => {}", getUrlDigitalToken());

			tokenResponse = obtenerPostPorObjeto(getUrlDigitalToken(),
					new HttpEntity<DigitalTokenRequest>(requestToken, headers), DigitalTokenResponse.class);
			tokenResponse.setStatusHttp("200");
		} catch (Exception e) {
			tokenResponse.setStatusHttp("400");
			tokenResponse.setCode("DIG-001");
			tokenResponse.setMessage(e.getMessage());
			tokenResponse.setTitle(getUrlDigitalToken());
		}

		logger.info("Salio a InterseguroRestClient#obtenerTokenDigital(obtenerDatosUsuarioRequest)");
		return tokenResponse;
	}

	public ObtenerDatosClienteResponse obtenerDatosCliente(String numeroDocumento) throws SivSOAException {
		logger.info("Entro a InterseguroRestClient#obtenerDatosCliente(numeroDocumento)");

		// -- Obtener Token

		DigitalTokenResponse tokenResponse = this.obtenerTokenDigital();

		// -- Buscar cliente
		ObtenerDatosClienteResponse obtenerDatosClienteResponse = new ObtenerDatosClienteResponse();
		try {

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", "Bearer " + tokenResponse.getItem().getAccessToken());

			Map<String, Object> uriVariables = new HashMap<>();
			uriVariables.put("numeroDocumento", numeroDocumento);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getUrlDigitalObtenerCliente());
			builder.uriVariables(uriVariables);

			// -- Response
			ResponseEntity<ObtenerDatosClienteResponse> response = getRestTemplate().exchange(
					builder.build().toString(), HttpMethod.GET, new HttpEntity<String>(null, headers),
					ObtenerDatosClienteResponse.class);
			obtenerDatosClienteResponse = response.getBody();
			if (obtenerDatosClienteResponse == null) {
				throw new NullPointerException("Obtener datos de cliente NULL");
			}
			obtenerDatosClienteResponse.setStatusHttp("200");
		} catch (Exception e) {
			tokenResponse.setStatusHttp("400");
			tokenResponse.setCode("DIG-002");
			tokenResponse.setMessage(e.getMessage());
			tokenResponse.setTitle(getUrlDigitalObtenerCliente());
		}

		logger.info("Salio a InterseguroRestClient#obtenerDatosCliente(obtenerDatosUsuarioRequest)");
		return obtenerDatosClienteResponse;
	}

	public LinkPagoResponse generateLinkPago(LinkPagoRequest request) {
		logger.info("Entro a generateLinkPago()");
		LinkPagoResponse response = new LinkPagoResponse();
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<LinkPagoRequest> httpPago = new HttpEntity<>(request, headers);
			response = obtenerPostPorObjeto(getLinkPago(), httpPago, LinkPagoResponse.class);
			if (response == null) {
				throw new NullPointerException("GENERA LINK NULL");
			}
		} catch (Exception e) {
			logger.error("error al llamar a generateLinkPago()");
		}
		logger.info("Sale a generateLinkPago()");
		return response;
	}

	public EnviarSmsNotificacionesResponse enviarSmsNotificaciones(EnviarSmsRequest enviarSmsRequest) throws SivSOAException {
		logger.info("Entro a InterseguroRestClient#enviarSmsNotificaciones(enviarSmsRequest)");

		EnviarSmsNotificacionesResponse enviarSmsResponse;
		try {

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			ResponseEntity<EnviarSmsNotificacionesResponse> response = getRestTemplate().exchange(apiNotificacionesSms, HttpMethod.POST,
					new HttpEntity<EnviarSmsRequest>(enviarSmsRequest, headers), EnviarSmsNotificacionesResponse.class);
			enviarSmsResponse = response.getBody();
			if (enviarSmsResponse == null) {
				throw new NullPointerException("ENVIAR SMS NULL: " + apiNotificacionesSms);
			}

		} catch (Exception e) {
			throw new SivSOAException(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_INTERSEGURO_SMS)
							.concat(Objects.toString(e.getMessage(), "")),
					null);
		}

		logger.info("Salio a InterseguroRestClient#enviarSmsNotificaciones(enviarSmsRequest)");
		return enviarSmsResponse;
	}
}
