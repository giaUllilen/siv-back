package pe.interseguro.siv.common.persistence.rest.crm;

import java.util.Arrays;
import java.util.Base64;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import pe.interseguro.siv.common.dto.request.SMEGenrarAdjunto4Mail;
import pe.interseguro.siv.common.dto.request.SMERequest;
import pe.interseguro.siv.common.exception.SivSOAException;
import pe.interseguro.siv.common.persistence.rest.base.BaseRestClientImpl;
import pe.interseguro.siv.common.persistence.rest.consentimiento.ConsentimientoRequest;
import pe.interseguro.siv.common.persistence.rest.consentimiento.ConsentimientoUniversalRequest;
import pe.interseguro.siv.common.persistence.rest.consentimiento.ConsentimientoV2Request;
import pe.interseguro.siv.common.persistence.rest.consentimiento.CreateConsentimiento;
import pe.interseguro.siv.common.persistence.rest.crm.request.CrearBeneficiariosRequest;
import pe.interseguro.siv.common.persistence.rest.crm.request.CrmActualizarSitcRequest;
import pe.interseguro.siv.common.persistence.rest.crm.request.CrmCotizacionRequest;
import pe.interseguro.siv.common.persistence.rest.crm.request.UpdateFormatoDPSRequest;
import pe.interseguro.siv.common.persistence.rest.crm.response.CrmCotizacionResponse;
import pe.interseguro.siv.common.persistence.rest.crm.response.GetConsentLogResponse;
import pe.interseguro.siv.common.persistence.rest.crm.response.UpdateResponse;
import pe.interseguro.siv.common.persistence.rest.niubiz.response.BaseResponse;
import pe.interseguro.siv.common.util.Constantes;
import pe.interseguro.siv.common.util.Utilitarios;

/**
 * @author digital-is
 *
 */
@Component
public class CrmRestClient extends BaseRestClientImpl {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());


	@Autowired
	private MessageSource messageSource;

	@Value("#{ environment['persistence.rest.client.crm.servidor'] }")
	private String servidorCrm;

	@Value("#{ environment['persistence.rest.client.crm.servidor.v2'] }")
	private String servidorCrmV2;

	@Value("#{ environment['persistence.rest.client.crm.cotizador.servidor'] }")
	private String servidorCotizadorVida;

	@Value("#{ environment['persistence.rest.client.crm.cotizador.transmitir'] }")
	private String crmTransmitirCotizacion;

	@Value("#{ environment['persistence.rest.client.crm.cotizador.intermedio.servidor'] }")
	private String servidorCotizadorVidaIntemedio;

	@Value("#{ environment['persistence.rest.client.crm.cotizador.nuevo'] }")
	private String crmCotizadorVidaNuevo;

	@Value("#{ environment['persistence.rest.client.crm.cotizador.recotizar'] }")
	private String crmRecotizarVida;

	@Value("#{ environment['persistence.rest.client.crm.cotizador.crear'] }")
	private String crmCrearCotizacion;

	@Value("#{ environment['persistence.rest.client.crm.cotizador.actualizar'] }")
	private String crmActualizarCotizacion;

	@Value("#{ environment['persistence.rest.client.crm.validarCreacionAsignacion'] }")
	private String crmValidarCreacionAsignacion;

	@Value("#{ environment['persistence.rest.client.crm.getDatosContacto'] }")
	private String crmGetDatosContacto;

	@Value("#{ environment['persistence.rest.client.crm.validarAccesoCrm'] }")
	private String crmValidarAccesoCrm;

	@Value("#{ environment['persistence.rest.client.crm.validarAccesoCrm.v2'] }")
	private String crmValidarAccesoCrmV2;

	@Value("#{ environment['persistence.rest.client.crm.updateCrm'] }")
	private String crmUpdateCrm;

	@Value("#{ environment['persistence.rest.client.crm.getIdOportunidad'] }")
	private String crmGetIdOportunidad;

	@Value("#{ environment['persistence.rest.client.crm.getEstadoCotizacion'] }")
	private String crmGetEstadoCotizacion;

	@Value("#{ environment['persistence.rest.client.crm.cotizador.finalizar'] }")
	private String crmGetOportunidadCotizacion;

	@Value("#{ environment['persistence.rest.client.crm.uploadArchivo'] }")
	private String crmUploadArchivo;

	@Value("#{ environment['persistence.rest.client.crm.updateContacto'] }")
	private String crmUpdateContacto;

	@Value("#{ environment['persistence.rest.client.crm.updateCotizacion'] }")
	private String crmUpdateCotizacion;

	@Value("#{ environment['persistence.rest.client.crm.updateDPS'] }")
	private String crmUpdateDPS;

	@Value("#{ environment['persistence.rest.client.crm.crearBeneficiarios'] }")
	private String crmCrearBeneficiarios;

	@Value("#{ environment['persistence.rest.client.crm.cotizador.actualizarSitc'] }")
	private String crmActualizarSitc;

//  Crear y actualizar cotización con contenedores
	@Value("#{ environment['persistence.rest.client.crmd.cotizador.crear'] }")
	private String crmCrearCotizacionCMRD;

	@Value("#{ environment['persistence.rest.client.crmd.cotizador.actualizar'] }")
	private String crmActualizarCotizacionCRMD;

//  upload file a storage
	@Value("#{ environment['persistence.rest.client.cloud-storage.servidor'] }")
	private String cloudStorageServidor;

	@Value("#{ environment['persistence.rest.client.cloud-storage.upload'] }")
	private String cloudStorageUpload;

	@Value("#{ environment['persistence.rest.client.cloud-storage.file'] }")
	private String cloudStorageFile;

	@Value("#{ environment['persistence.rest.client.cloud-storage.files'] }")
	private String cloudStorageFiles;

	@Value("#{ environment['persistence.rest.client.cloud-storage.header.app'] }")
	private String cloudStorageApp;

	@Value("#{ environment['persistence.rest.client.cloud-storage.header.app.value'] }")
	private String cloudStorageAppValue;

	@Value("#{ environment['persistence.rest.client.consentimiento.api'] }")
	private String apiConsentimiento;

	@Value("#{ environment['persistence.rest.client.consentimiento.v2.api'] }")
	private String apiConsentimientov2;

	@Value("#{ environment['persistence.rest.client.sme.api'] }")
	private String apiSME;

	@Value("#{ environment['persistence.rest.client.sms.api'] }")
	private String apiSMS;
	@Value("#{ environment['persistence.rest.client.interseguro.sms.servidor2'] }")
	private String servidorSms2;

	@Value("#{ environment['persistence.rest.client.interseguro.sms.enviarSms2'] }")
	private String smsEnviarSms2;

	@Value("#{ environment['persistence.rest.client.sme.consentimiento.generarpdf'] }")
	private String apiSMEGeneracionPDFConsentimiento;

	private String getUrlCrmUpdateDPS() {
		return servidorCrm + crmUpdateDPS;
	}

	private String getUrlCrmCrearBeneficiarios() {
		return servidorCrm + crmCrearBeneficiarios;
	}

	private String getUrlCrmCrearCotizacion() {
		return servidorCotizadorVida + crmCrearCotizacion;
	}

	private String getUrlCrmActualizarCotizacion() {
		return servidorCotizadorVida + crmActualizarCotizacion;
	}

	private String getUrlCrmActualizarCotizadorSitc() {
		return servidorCotizadorVida + crmActualizarSitc;
	}

	/**
	 * url para subir archivor al drive
	 */

	public String getCloudStorageFile() {
		return cloudStorageServidor + cloudStorageFile;
	}

	public String getCloudStorageFiles() {
		return cloudStorageServidor + cloudStorageFiles;
	}

	private String getUrlAPIConsentimiento() {
		return apiConsentimiento + "consentimientos/";
	}

	private String getUrlAPISMS() {
		// return apiSMS + "rest/EnviarSMS";
		return servidorSms2 + smsEnviarSms2;
	}

	private String getUrlAPISME() {
		return apiSME + "envios/";
	}

	private String getUrlAPISMEgeneracionPDFConsentimiento() {
		return apiSMEGeneracionPDFConsentimiento + "WordPdf/combinar-word-mergefield";
	}

	private String getUrlUpdateConsentimiento() {
		return apiConsentimiento + "consentimientos";
	}

	private String getPdfConsentimiento() {
		return apiConsentimiento + "reportes/formato-consentimiento/";
	}
	/**
	 * Actualizar datos de la cotizacion en el CRM
	 * 
	 * @param request
	 * @return
	 */
	/*
	 * public UpdateResponse updateCotizacion(UpdateCotizacionRequest request) {
	 * LOGGER.info("Entro CrmRestClient#UpdateCotizacionRequest(request)");
	 * 
	 * UpdateResponse response = null;
	 * 
	 * try { String url = getUrlCrmUpdateCotizacion(); response =
	 * obtenerPostPorObjeto(url, request, UpdateResponse.class); } catch
	 * (SivSOAException e) { throw new SivSOAException(
	 * Utilitarios.obtenerMensaje(messageSource,
	 * Constantes.MENSAJE_RESTCLIENT_CRM_UPDATE_COTIZACION)
	 * .concat(Objects.toString(e.getMessage(), "")), e.getErrors()); }
	 * 
	 * LOGGER.info("Salio CrmRestClient#UpdateCotizacionRequest(request)"); return
	 * response; }
	 */

	/**
	 * Actualizar datos del contacto en el CRM
	 * 
	 * @param request
	 * @return
	 */
	/*
	 * public UpdateResponse updateContacto(UpdateContactoRequest request) {
	 * LOGGER.info("Entro CrmRestClient#updateContacto(request)");
	 * 
	 * UpdateResponse response = null;
	 * 
	 * try { String url = getUrlCrmUpdateContacto(); response =
	 * obtenerPostPorObjeto(url, request, UpdateResponse.class); } catch
	 * (SivSOAException e) { throw new SivSOAException(
	 * Utilitarios.obtenerMensaje(messageSource,
	 * Constantes.MENSAJE_RESTCLIENT_CRM_UPDATE_CONTACTO)
	 * .concat(Objects.toString(e.getMessage(), "")), e.getErrors()); }
	 * 
	 * LOGGER.info("Salio CrmRestClient#updateCrm(request)"); return response; }
	 */

	/**
	 * Actualizar datos de la DPS en el CRM
	 * 
	 * @param request
	 * @return
	 */
	public UpdateResponse updateDPS(UpdateFormatoDPSRequest request) {
		LOGGER.info("Entro CrmRestClient#updateDPS(request)");

		UpdateResponse response = null;

		try {
			String url = getUrlCrmUpdateDPS();
			response = obtenerPostPorObjeto(url, request, UpdateResponse.class);
		} catch (SivSOAException e) {
			throw new SivSOAException(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_CRM_UPDATE_DPS)
							.concat(Objects.toString(e.getMessage(), "")),
					e.getErrors());
		}

		LOGGER.info("Salio CrmRestClient#updateDPS(request)");
		return response;
	}

	/**
	 * Crea beneficiarios en el CRM (antes de crear elimina los existentes)
	 * 
	 * @param request
	 * @return
	 */
	public UpdateResponse crearBenficiarios(CrearBeneficiariosRequest request) {
		LOGGER.info("Entro CrmRestClient#crearBenficiarios(request)");

		UpdateResponse response = null;

		try {
			String url = getUrlCrmCrearBeneficiarios();
			response = obtenerPostPorObjeto(url, request, UpdateResponse.class);
		} catch (SivSOAException e) {
			throw new SivSOAException(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_CRM_CREAR_BENEFICIARIOS)
							.concat(Objects.toString(e.getMessage(), "")),
					e.getErrors());
		}

		LOGGER.info("Salio CrmRestClient#crearBenficiarios(request)");
		return response;
	}

	public CrmCotizacionResponse guardarCotizacionCrm(CrmCotizacionRequest request) {
		LOGGER.info("Entro CrmRestClient#guardarCotizacionCrm(request)");

		CrmCotizacionResponse response = new CrmCotizacionResponse();
		response.setTitle(getUrlCrmCrearCotizacion());
//		response.setTitle(getUrlCrmdCrearCotizacion());
		try {
			String url = getUrlCrmCrearCotizacion();
			if (request.getBeCotizacion().getIdCRM() != null && request.getBeCotizacion().getIdCRM() != ""
					&& !request.getBeCotizacion().getIdCRM().equalsIgnoreCase("")) {
				url = getUrlCrmActualizarCotizacion();
			}
//			String url = getUrlCrmdCrearCotizacion();
//			if(request.getBeCotizacion().getIdCRM() != null && request.getBeCotizacion().getIdCRM() != "" && !request.getBeCotizacion().getIdCRM().equalsIgnoreCase("") ) {
//				url = getUrlCrmdActualizarCotizacion();
//			}
			LOGGER.info("URL:" + url);
			response = obtenerPostPorObjeto(url, request, CrmCotizacionResponse.class);
			LOGGER.info("Respuesta Servicio Rest:" + response.getIdCotizacionCRM());
			response.setStatusHttp("200");
		} catch (Exception e) {
			response.setStatusHttp("500");
			response.setCode("CRM-008");
			response.setMessage("Error: " + e.getMessage());
			/*
			 * e.printStackTrace(); throw new SivSOAException(
			 * Utilitarios.obtenerMensaje(messageSource,
			 * Constantes.MENSAJE_RESTCLIENT_CRM_URL_COTIZACION_RECOTIZAR).concat(
			 * Objects.toString(e.getMessage(), "") ), e.getErrors() );
			 */
		}

		LOGGER.info("Salio CrmRestClient#obtenerUrlRecotizarVida(request)");
		return response;
	}

	public Boolean actualizarFlagCrmSitc(CrmActualizarSitcRequest request) {
		LOGGER.info("Entro CrmRestClient#actualizarFlagCrmSitc(request)");

		Boolean response = false;

		try {
			response = obtenerPostPorObjeto(getUrlCrmActualizarCotizadorSitc(), request, Boolean.class);
		} catch (SivSOAException e) {
			throw new SivSOAException(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_CRM_UPDATE_SITC)
							.concat(Objects.toString(e.getMessage(), "")),
					e.getErrors());
		}

		LOGGER.info("Salio CrmRestClient#actualizarFlagCrmSitc(request)");
		return response;
	}

	// Anedd
	/*
	 * public CrmTransmisionResponse transmitirCotizacionCrm(CrmTransmisionRequest
	 * request) {
	 * LOGGER.info("Entro CrmRestClient#transmitirCotizacionCrm(request)");
	 * 
	 * CrmTransmisionResponse response = new CrmTransmisionResponse();
	 * response.setTitle(getUrlCrmTransmitirCotizacion()); try { response =
	 * obtenerPostPorObjeto(getUrlCrmTransmitirCotizacion(), request,
	 * CrmTransmisionResponse.class); response.setStatusHttp("200"); } catch
	 * (Exception e) { response.setStatusHttp("500");
	 * response.setMessage(e.getMessage()); response.setCode("CRM-006"); }
	 * 
	 * LOGGER.info("Salio CrmRestClient#transmitirCotizacionCrm(request)"); return
	 * response; }
	 */

	public Map<String, Object> getConsentimientoPorTratamiento(ConsentimientoUniversalRequest request) {
		LOGGER.info("Entro CrmRestClient#getConsentimientoUniversal(request)");

		Map<String, Object> response = null;
		Map<String, Object> params = new HashMap<>();
		String endpoint = "por-configuracion-documento/{idConfiguracion}/{idTipoDocumento}/{numeroDocumento}/{usuario}";
		LOGGER.info("api consentimiento: " + getUrlAPIConsentimiento());
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(getUrlAPIConsentimiento() + endpoint);
		params.put("idConfiguracion", request.getIdConfiguracion());
		params.put("idTipoDocumento", request.getTipoDocumento());
		params.put("numeroDocumento", request.getNumeroDocumento());
		params.put("usuario", request.getUsuario());
		Gson gson = new Gson();
		try {
			response = obtenerGetPorObjeto(builder.buildAndExpand(params).toUriString(), Map.class);
			LOGGER.info("gary getConsentimientoUniversal response:" + gson.toJson(response));
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		LOGGER.info("Salio CrmRestClient#getConsentimientoUniversal(request)");
		return response;
	}

	public Map<String, Object> getConsentimientoUniversal(ConsentimientoUniversalRequest request) {
		LOGGER.info("Entro CrmRestClient#getConsentimientoPorTratamiento(request)");

		Map<String, Object> response = null;
		Map<String, Object> params = new HashMap<>();
		String endpoint = "por-tratamiento/{idTratamiento}/{idTipoDocumento}/{numeroDocumento}/{usuario}";

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(getUrlAPIConsentimiento() + endpoint);
		params.put("idTratamiento", request.getIdConfiguracion());
		params.put("idTipoDocumento", request.getTipoDocumento());
		params.put("numeroDocumento", request.getNumeroDocumento());
		params.put("usuario", request.getUsuario());

		try {
			Gson gson = new Gson();
			response = obtenerGetPorObjeto(builder.buildAndExpand(params).toUriString(), Map.class);
			LOGGER.info("gary getConsentimientoPorTratamiento response:" + gson.toJson(response));
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		LOGGER.info("Salio CrmRestClient#getConsentimientoPorTratamiento(request)");
		return response;
	}

	public Map<String, Object> getConsentimientov2(ConsentimientoV2Request request) {
		LOGGER.info("Entro CrmRestClient#getConsentimientov2(request)");

		Map<String, Object> dataResult = new HashMap<>();
		String endpoint = "/v1/consentimientos";

		// Convertir código numérico del tipo de documento a descripción
		String documentTypeDescription = request.getDocumentType();
		if ("1".equals(request.getDocumentType())) {
			documentTypeDescription = "DNI";
		} else if ("2".equals(request.getDocumentType())) {
			documentTypeDescription = "CE";
		}else if ("11".equals(request.getDocumentType())) {
			documentTypeDescription = "RUC";
		}

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiConsentimientov2 + endpoint)
				.queryParam("documentType", documentTypeDescription)
				.queryParam("documentNumber", request.getDocumentNumber())
				.queryParam("business", request.getBusiness())
				.queryParam("product", request.getProduct())
				.queryParam("onlyLatest", request.getOnlyLatest())
				.queryParam("page", request.getPage())
				.queryParam("limit", request.getLimit());

		try {
			Gson gson = new Gson();
			Map<String, Object> response = obtenerGetPorObjeto(builder.toUriString(), Map.class);
			LOGGER.info("getConsentimientov2 full response:" + gson.toJson(response));
			
			if (response == null) {
				LOGGER.warn("No se recibió respuesta del servicio de consentimiento v2");
				return null;
			}
			
			// Extraer solo el objeto "data" de la respuesta
			Object dataObject = response.get("data");
			if (dataObject != null && dataObject instanceof Map) {
				dataResult = (Map<String, Object>) dataObject;
				LOGGER.info("getConsentimientov2 data extracted:" + gson.toJson(dataResult));
			} else {
				LOGGER.warn("La respuesta no contiene el campo 'data' válido");
				return null;
			}
			
		} catch (Exception e) {
			LOGGER.error("Error en getConsentimientov2: " + e.getMessage(), e);
			return null;
		}

		LOGGER.info("Salio CrmRestClient#getConsentimientov2(request) - Data size: " + dataResult.size());
		return dataResult;
	}

	public Map<String, Object> createConsentimiento(CreateConsentimiento request) {
		LOGGER.info("Entro CrmRestClient#createConsentimiento(request)");

		Map<String, Object> response = null;

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(getUrlAPIConsentimiento());
		Gson gson = new Gson();
		try {
			// response = obtenerPostPorObjeto(builder.toUriString(), request, Map.class);
			HttpResponse<String> result2 = Unirest.post(builder.toUriString())
					.header("Content-type", "application/json").header("Accept", "application/json")
					.body(gson.toJson(request)).asString();
			response = new HashMap<>();
			ObjectMapper mapper = new ObjectMapper();
			response.putAll(mapper.readValue(result2.getBody(), Map.class));
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		LOGGER.info("Salio CrmRestClient#createConsentimiento(request)");
		return response;
	}

	public Map<String, Object> sendSME(SMERequest request) {
		LOGGER.info("Entro CrmRestClient#sendSME(request)");

		Map<String, Object> response = null;

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(getUrlAPISME());

		try {
			response = obtenerPostPorObjeto(builder.toUriString(), request, Map.class);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		LOGGER.info("Salio CrmRestClient#sendSME(request)");
		return response;
	}

	// public Map<String, Object> sendSMS(SMSRequest request) {
	// 	LOGGER.info("Entro CrmRestClient#sendSMS(request)");
	// 	Gson gson = new Gson();
	// 	Map<String, Object> response = null;
	// 	EnviarSmsRequest enviarSmsRequest = new EnviarSmsRequest();
	// 	enviarSmsRequest.setFrom("INTERSEGURO");
	// 	enviarSmsRequest.setMessageType("normal");
	// 	enviarSmsRequest.setCelular(request.getCelular());
	// 	enviarSmsRequest.setMensaje(request.getMensaje());


	// 	UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(getUrlAPISMS());

	// 	try {
	// 		String responseStr = obtenerPostPorObjeto(builder.toUriString(), enviarSmsRequest, String.class);
			
	// 		LOGGER.info("CrmRestClient#sendSMS responseStr: " + responseStr);
	// 	} catch (Exception e) {
	// 		LOGGER.error(e.getMessage(), e);
	// 	}

	// 	LOGGER.info("Salio CrmRestClient#sendSMS(request)");
	// 	return response;
	// }
	

	public Map<String, Object> generarPDFConsentimiento(SMEGenrarAdjunto4Mail request) {
		LOGGER.info("Entro CrmRestClient#generarPDFConsentimiento(request)");

		Map<String, Object> response = null;

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(getUrlAPISMEgeneracionPDFConsentimiento());

		try {
			response = obtenerPostPorObjeto(builder.toUriString(), request, Map.class);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		LOGGER.info("Salio CrmRestClient#generarPDFConsentimiento(request)");
		return response;
	}

	public Map<String, Object> updateConsentimiento(Map request) {

		Map<String, Object> response = null;
		Gson gson = new Gson();
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(getUrlUpdateConsentimiento());
		LOGGER.info("Entro CrmRestClient#updateConsentimiento(request) : " + gson.toJson(request));
		try {
			// obtenerPut(builder.toUriString(), request);

			HttpResponse<String> result2 = Unirest.put(builder.toUriString()).header("Content-type", "application/json")
					.header("Accept", "application/json").body(gson.toJson(request)).asString();
			response = new HashMap<>();
			ObjectMapper mapper = new ObjectMapper();
			response.putAll(mapper.readValue(result2.getBody(), Map.class));
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		LOGGER.info("Salio CrmRestClient#updateConsentimiento(request)");
		return response;
	}

	public Map<String, Object> acceptConsent(GetConsentLogResponse request) {
		Map<String, Object> response = null;
		Gson gson = new Gson();
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(getUrlAPIConsentimiento() + "/aceptacion");
		LOGGER.info("Entro CrmRestClient#acceptConsent(request) : " + gson.toJson(request));

		try {
			response = obtenerPostPorObjeto(builder.toUriString(), request, Map.class);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		LOGGER.info("Salio CrmRestClient#acceptConsent(request)");
		return response;
	}

	public GetConsentLogResponse getConsentLog(String idConsent) {
		LOGGER.info("Entro CrmRestClient#getConsentLog(idConsent)");

		GetConsentLogResponse response = null;
		Gson gson = new Gson();
		UriComponentsBuilder builder = UriComponentsBuilder
				.fromUriString(getUrlAPIConsentimiento() + idConsent + "/muntiver");

		try {
			response = obtenerGetPorObjeto(builder.toUriString(), GetConsentLogResponse.class);
			LOGGER.info("#getLogConsentimiento: " + gson.toJson(response));
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		LOGGER.info("Salio CrmRestClient#getConsentLog(idConsent)");
		return response;
	}

	public Map<String, Object> getConsentimiento(Integer idConsentimientoAuditoria, String usuario) {
		LOGGER.info("Entro CrmRestClient#getConsentimiento(request)");

		Map<String, Object> response = null;
		Gson gson = new Gson();
		UriComponentsBuilder builder = UriComponentsBuilder
				.fromUriString(getUrlAPIConsentimiento() + idConsentimientoAuditoria + "/" + usuario);

		try {
			response = obtenerGetPorObjeto(builder.toUriString(), Map.class);
			LOGGER.info("#getConsentimiento: " + gson.toJson(response));
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		LOGGER.info("Salio CrmRestClient#getConsentimiento(request)");
		return response;
	}

	public BaseResponse pdfConsentimiento(String idConsentimiento, String usuario) {
		LOGGER.info("Entro pdfConsentimiento(idConsentimiento:" + idConsentimiento + ", usuario:" + usuario + ")");

		RestTemplate restTemplate = new RestTemplate();
		BaseResponse responses = new BaseResponse();

		try {

			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_PDF, MediaType.APPLICATION_OCTET_STREAM));
			HttpEntity<String> entity = new HttpEntity<>(headers);
			ResponseEntity<byte[]> result = restTemplate.exchange(
					getPdfConsentimiento() + idConsentimiento + "/" + usuario, HttpMethod.GET, entity, byte[].class);

			byte[] content = result.getBody();
			String encodedString = Base64.getEncoder().encodeToString(content);

			if (result.getStatusCodeValue() == HttpStatus.OK.value()) {

				responses.setStatusHttp(result.getStatusCode().toString());
				responses.setMessage(encodedString);
			} else {
				responses.setStatusHttp(result.getStatusCode().toString());
				responses.setMessage(result.getBody().toString());
			}
			LOGGER.info(new Gson().toJson(responses));
		} catch (HttpClientErrorException errorClient) {
			responses.setStatusHttp(errorClient.getStatusCode().toString());
			responses.setMessage(errorClient.getMessage());
		} catch (HttpServerErrorException errorServer) {
			responses.setStatusHttp(errorServer.getStatusCode().toString());
			responses.setMessage(errorServer.getMessage());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		LOGGER.info("Salio CrmRestClient#pdfConsentimiento(request)");

		return responses;
	}

}
