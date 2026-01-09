package pe.interseguro.siv.common.persistence.rest.global;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;
import pe.interseguro.siv.common.exception.SivSOAException;
import pe.interseguro.siv.common.persistence.db.mysql.domain.Persona;
import pe.interseguro.siv.common.persistence.db.mysql.repository.PersonaRepository;
import pe.interseguro.siv.common.persistence.rest.base.BaseRestClientImpl;
import pe.interseguro.siv.common.persistence.rest.bupo.response.ValidarProspectoAsignacionResponse;
import pe.interseguro.siv.common.persistence.rest.global.request.*;
import pe.interseguro.siv.common.persistence.rest.global.response.*;
import pe.interseguro.siv.common.util.Constantes;
import pe.interseguro.siv.common.util.Utilitarios;

import java.util.*;

/**
 * @author digital-is
 */
@Component
public class GlobalRestClient extends BaseRestClientImpl {

	public static final String CARNET_DE_EXTRANJERIA = "CARNET DE EXTRANJERIA";
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MessageSource messageSource;
	@Autowired
	private PersonaRepository personaRepository;

	@Value("#{ environment['persistence.rest.client.global.poliza.servidor'] }")
	private String servidorApiPoliza;

	@Value("#{ environment['persistence.rest.client.global.plan.garantizado.servidor'] }")
	private String servidorApiPlanGarantizado;

	@Value("#{ environment['persistence.rest.client.global.plaft.servidor'] }")
	private String servidorApiPlaft;

	@Value("#{ environment['persistence.rest.client.global.documento.servidor'] }")
	private String servidorApiDocumento;

	@Value("#{ environment['persistence.rest.client.global.estructura.comercial.servidor'] }")
	private String servidorApiEstructuraComercial;

	@Value("#{ environment['persistence.rest.client.global.vtiger.vida.servidor'] }")
	private String servidorApiVtiger;

	@Value("#{ environment['persistence.rest.client.global.vtiger.vida.servidorv2'] }")
	private String servidorApiVtigerV2;

	@Value("#{ environment['persistence.rest.client.global.asl.endoso.vida.servidor'] }")
	private String servidorAslEndosoVida;

	@Value("#{ environment['persistence.rest.client.global.transmitir.servidor'] }")
	private String servidorAslTransmision;

	@Value("#{ environment['persistence.rest.client.global.solicitud.servidor'] }")
	private String servidorApiSolicitud;

	@Value("#{ environment['persistence.rest.client.global.solicitud.envioCorreoEmision'] }")
	private String envioCorreoEmision;

	@Value("#{ environment['persistence.rest.client.global.solicitud.evaluacionAutomatica'] }")
	private String globalEmisionAutomatica;

	@Value("#{ environment['persistence.rest.client.global.solicitud.reevaluacion'] }")
	private String globalReEvaluacion;

	@Value("#{ environment['persistence.rest.client.global.poliza.ObtenerPolizas'] }")
	private String globalObtenerPolizas;

	@Value("#{ environment['persistence.rest.client.global.plaft.ValidarPlaft'] }")
	private String globalValidarPlaft;

	@Value("#{ environment['persistence.rest.client.global.pagos.servidor'] }")
	private String servidorApiPagos;

	@Value("#{ environment['persistence.rest.client.global.pagos.ObtenerMontoRecargo'] }")
	private String servidorObtenerMontoRecarga;

	@Value("#{ environment['persistence.rest.client.global.plaft.AlertaPlaft'] }")
	private String globalAlertaPlaft;

	@Value("#{ environment['persistence.rest.client.global.estructura.comercial.obtenerAgente'] }")
	private String globalObtenerAgente;

	@Value("#{ environment['persistence.rest.client.global.estructura.comercial.obtenerAgenteAll'] }")
	private String globalObtenerAgenteAll;

	@Value("#{ environment['persistence.rest.client.global.estructura.comercial.obtenerCargoAgente'] }")
	private String globalObtenerCargoAgente;

	@Value("#{ environment['persistence.rest.client.global.vtiger.vida.obtenerPotential'] }")
	private String globalObtenerPotential;

	@Value("#{ environment['persistence.rest.client.global.vtiger.vida.obtenerContact'] }")
	private String globalObtenerContact;

	@Value("#{ environment['persistence.rest.client.global.vtiger.vida.contact'] }")
	private String globalContact;

	@Value("#{ environment['persistence.rest.client.global.vtiger.vida.reglasAsigCliente'] }")
	private String globalReglasAsigCliente;

	@Value("#{ environment['persistence.rest.client.global.vtiger.vida.validarAgente'] }")
	private String globalValidarAgente;

	@Value("#{ environment['persistence.rest.client.global.vtiger.vida.actualizarContactoVtiger'] }")
	private String globalActualizarContactoVtiger;

	@Value("#{ environment['persistence.rest.client.global.vtiger.vida.registerContact'] }")
	private String globalCrearContactoVtiger;

	@Value("#{ environment['persistence.rest.client.global.vtiger.vida.registerPotential'] }")
	private String globalCrearPotentialVtiger;

	@Value("#{ environment['persistence.rest.client.global.solicitud.consultarAsignacionAcsele'] }")
	private String globalAsignacionAcsele;

	@Value("#{ environment['persistence.rest.client.global.solicitud.transmitirTraspaso'] }")
	private String gloalTransmitirTraspaso;

	@Value("#{ environment['persistence.rest.client.global.solicitud.obtenerCotizacion'] }")
	private String globalObtenerCotizacion;

	@Value("#{ environment['persistence.rest.client.global.solicitud.validarTraspaso'] }")
	private String globalValidarTraspaso;

	@Value("#{ environment['persistence.rest.client.global.solicitud.consultarAcreditacion'] }")
	private String globalConsultarAcreditacion;

	@Value("#{ environment['persistence.rest.client.global.vtiger.vida.obtenerPersona'] }")
	private String globalObtenerPersona;

	@Value("#{ environment['persistence.rest.client.global.solicitud.obtenerTipoCambio'] }")
	private String globalObtenerTipoCambio;

	@Value("#{ environment['persistence.rest.client.global.estructura.comercial.obtenerAgentexCorreo'] }")
	private String globalObtenerAgentexCorreo;

	@Value("#{ environment['persistence.rest.client.global.plan.garantizado.nuevo'] }")
	private String globalNuevo;

	@Value("#{ environment['persistence.rest.client.global.plan.garantizado.recotizar'] }")
	private String globalRecotizar;

	@Value("#{ environment['persistence.rest.client.global.estructura.comercial.obtenerPersonaDocumento'] }")
	private String globalObtenerPersonaDocumento;

	@Value("#{ environment['persistence.rest.client.global.estructura.comercial.findByDocumento'] }")
	private String globalFindByDocumento;

	@Value("#{ environment['persistence.rest.client.global.estructura.comercial.obtenerPersonaDocumento2'] }")
	private String globalObtenerPersonaDocumento2;

	@Value("#{ environment['persistence.rest.client.global.estructura.comercial.insertarPersonaDocumento'] }")
	private String globalInsertarPersonaDocumento;

	@Value("#{ environment['persistence.rest.client.global.estructura.comercial.obtenerxCodAgente'] }")
	private String globalObtenerAgentexCodigo;

	@Value("#{ environment['persistence.rest.client.cloud-storage.header.app.value'] }")
	private String cloudStorageAppValue;

	@Value("#{ environment['persistence.rest.client.cloud-storage.header.app'] }")
	private String cloudStorageApp;

	@Value("#{ environment['persistence.rest.client.cloud-storage.servidor'] }")
	private String cloudStorageServidor;

	@Value("#{ environment['persistence.rest.client.cloud-storage.upload'] }")
	private String cloudStorageUpload;

	@Value("#{ environment['persistence.rest.client.cloud-storage.file'] }")
	private String cloudStorageFile;

	@Value("#{ environment['persistence.rest.client.cloud-storage.files'] }")
	private String cloudStorageFiles;

	@Value("#{ environment['persistence.rest.client.global.payment.servidor'] }")
	private String globalPayment;

	@Value("#{ environment['persistence.rest.client.global.payment.credentials'] }")
	private String globalPaymentCredentials;

	@Value("#{ environment['persistence.rest.client.global.payment.pubsub.send'] }")
	private String globalPaymentPubSubSend;

	@Value("#{ environment['persistence.rest.client.global.solicitud.consultarPolizaEmisionRapida'] }")
	private String globalSolicitudPolizaEmisionRapida;

	@Value("#{ environment['persistence.rest.client.global.solicitud.obtenerEstadoPropuesta'] }")
	private String globalSolicitudEstadoSolicitudEmisionRapida;

	@Value("#{ environment['persistence.rest.client.global.asl.endoso.vida.emisionAutomatica'] }")
	private String emisionAutomatica;

	@Value("#{ environment['persistence.rest.client.global.solicitud.obtener.muerte.accidental'] }")
	private String obtenerMuerteAccidental;

	@Value("#{ environment['persistence.rest.client.global.solicitud.gestorDocumento'] }")
	private String gestorDocumento;

	@Value("#{ environment['persistence.rest.client.global.transmision.gestor.envio.documento'] }")
	private String gestorEnvioDocumento;

	@Value("#{ environment['persistence.rest.client.global.solicitud.consultarTasaDeCambioActual'] }")
	private String consultarTasaDeCambioActual;

	@Value("#{ environment['persistence.rest.client.global.solicitud.consultarPolizasCanceladas'] }")
	private String consultarPolizasCanceladas;

	@Value("#{ environment['persistence.rest.client.interseguro.passarella.apiKey'] }")
	private String apiKey;

	@Value("#{ environment['persistence.rest.client.global.solicitud.registrarAfiliacionTraspaso'] }")
	private String registrarAfiliacionTraspaso;

	@Value("#{ environment['persistence.rest.client.pg.api.notificaciones.trazabilidad'] }")
	private String urlActualizarTrazabilidadConsentimientoAceptado;

	private String getUrlObtenerPersona() {
		return servidorApiPlaft + globalObtenerPersona;
	}

	private String getUrlObtenerPolizas() {
		return servidorApiPoliza + globalObtenerPolizas;
	}

	private String getUrlValidarPlaft() {
		return servidorApiPlaft + globalValidarPlaft;
	}

	private String getUrlObtenerMontoRecarga() {
		return servidorApiPagos + servidorObtenerMontoRecarga;
	}

	private String getUrlEmisionAutomatica() {
		return servidorAslEndosoVida + emisionAutomatica;
	}

	private String getUrlGestorEnvoDocumento() {
		return servidorAslTransmision + gestorEnvioDocumento;
	}

	private String getUrlAlertaPlaft() {
		return servidorApiPlaft + globalAlertaPlaft;
	}

	private String getUrlEnvioCorreo() {
		return servidorApiSolicitud + envioCorreoEmision;
	}

	private String getUrlObtenerAgente() {
		return servidorApiEstructuraComercial + globalObtenerAgente;
	}

	private String getUrlObtenerAgenteAll() {
		return servidorApiEstructuraComercial + globalObtenerAgenteAll;
	}

	private String getUrlObtenerCargoAgente() {
		return servidorApiEstructuraComercial + globalObtenerCargoAgente;
	}

	private String getUrlObtenerAgentexCorreo() {
		return servidorApiEstructuraComercial + globalObtenerAgentexCorreo;
	}

	private String getUrlReglasAsigCliente() {
		return servidorApiVtiger + globalContact + globalReglasAsigCliente;
	}

	private String getUrlValidarAgenteActivo() {
		return servidorApiVtiger + globalContact + globalValidarAgente;
	}

	private String getUrlObtenerPotential() {
		return servidorApiVtiger + globalContact + globalObtenerPotential;
	}

	private String getUrlObtenerContacto() {
		return servidorApiVtiger + globalContact + globalObtenerContact;
	}

	private String getUrlActualizarContactoVtiger() {
		return servidorApiVtigerV2 + globalActualizarContactoVtiger;
	}

	private String getUrlCrearContactoVtiger() {
		return servidorApiVtigerV2 + globalCrearContactoVtiger;
	}

	private String getUrlCrearPotentialVtiger() {
		return servidorApiVtigerV2 + globalCrearPotentialVtiger;
	}

	private String getUrlConsultaAsignacionAcsele() {
		return servidorApiSolicitud + globalAsignacionAcsele;
	}

	private String getUrlObtenerMuerteAccidental() {
		return servidorApiSolicitud + obtenerMuerteAccidental;
	}

	//
	private String getUrlObtenerTipoCambio() {
		return servidorApiSolicitud + globalObtenerTipoCambio;
	}

	private String getUrlTransmitirTraspaso() {
		return servidorApiSolicitud + gloalTransmitirTraspaso;
	}

	private String getUrlObtenerCotizacion() {
		return servidorApiSolicitud + globalObtenerCotizacion;
	}

	private String getUrlValidarTraspaso() {
		return servidorApiSolicitud + globalValidarTraspaso;
	}

	private String getUrlConsultarAcreditacion() {
		return servidorApiSolicitud + globalConsultarAcreditacion;
	}

	private String getUrlCotizadorNuevo() {
		return servidorApiPlanGarantizado + globalNuevo;
	}

	private String getUrlCrmRecotizarVida() {
		return servidorApiPlanGarantizado + globalRecotizar;
	}

	private String getUrlObtenerPersonaDocuemnto() {
		return servidorApiDocumento + globalFindByDocumento;
	}

	private String getUrlEvaluacionAutomatica() {
		return servidorApiSolicitud + globalEmisionAutomatica;
	}

	private String getUrlReEvaluacion() {
		return servidorApiSolicitud + globalReEvaluacion;
	}

	private String getUrlObtenerPersonaDocuemnto2() {
		return servidorApiDocumento + globalObtenerPersonaDocumento2;
	}

	private String getUrlInsertarPersonaDocuemnto() {
		return servidorApiDocumento + globalInsertarPersonaDocumento;
	}

	private String getUrlObtenerAgentexCodigo() {
		return servidorApiEstructuraComercial + globalObtenerAgentexCodigo;
	}

	private String getUrlCloudStorageUpload() {
		return cloudStorageServidor + cloudStorageUpload;
	}

	public String getCloudStorageFile() {
		return cloudStorageServidor + cloudStorageFile;
	}

	public String getCloudStorageFiles() {
		return cloudStorageServidor + cloudStorageFiles;
	}

	public String getPaymentCredentials() {
		return globalPayment + globalPaymentCredentials;
	}

	public String getPaymentPubSubSend() {
		return globalPayment + globalPaymentPubSubSend;
	}

	public String getUrlSolicitudObtenerPolizaEmisionRapida() {
		return servidorApiSolicitud + globalSolicitudPolizaEmisionRapida;
	}

	public String getUrlSolicitudObtenerEstadoSolicitudEmisionRapida() {
		return servidorApiSolicitud + globalSolicitudEstadoSolicitudEmisionRapida;
	}

	public String getUrlSolicitudGestorDocumento() {
		return servidorApiSolicitud + gestorDocumento;
	}

	// HCR
	private String getUrlConsultaTasaDeCambioActual() {
		return servidorApiSolicitud + consultarTasaDeCambioActual;
	}

	private String getUrlConsultaPolizasCanceladas() {
		return servidorApiSolicitud + consultarPolizasCanceladas;
	}

	private String getUrlRegistrarAfiliacionTraspaso() {
		return servidorApiSolicitud + registrarAfiliacionTraspaso;
	}

	private Gson gson = new Gson();

	/**
	 * Obtener Polizas
	 *
	 * @param request
	 * @return
	 */
	public ObtenerPolizaResponse obtenerPolizas(ObtenerPolizaRequest request) {
		LOGGER.info("Entro BupoRestClient#obtenerPolizas(request)");
		ObtenerPolizaResponse responseFinal = null;

		try {

			String url = getUrlObtenerPolizas().concat("/?tipoDoc=").concat(request.getTipoDoc()).concat("&nroDoc=")
					.concat(request.getNroDoc()).concat("&tipoProducto=VIDA");
			responseFinal = obtenerGetPorObjeto(url, ObtenerPolizaResponse.class);

		} catch (SivSOAException e) {
			throw new SivSOAException(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_GLOBAL_OBTENER_POLIZAS)
							.concat(Objects.toString(e.getMessage(), "")),
					e.getErrors());
		}

		LOGGER.info("Salio BupoRestClient#obtenerPolizas(request)");
		return responseFinal;
	}

	public ObtenerMontoRecargoResponse obtenerMontoRecargo(ObtenerMontoRecargoRequest request) {
		LOGGER.info("Entro BupoRestClient#obtenerMontoRecargo(request)");
		ObtenerMontoRecargoResponse response = new ObtenerMontoRecargoResponse();

		try {
			// String url =
			// getUrlObtenerMontoRecarga().concat("/").concat(request.getNroCotizacion());
			response = obtenerPostPorObjeto(getUrlObtenerMontoRecarga(), request, ObtenerMontoRecargoResponse.class);
		} catch (SivSOAException e) {
			throw new SivSOAException(Utilitarios
					.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_GLOBAL_OBTENER_MONTO_RECARGO)
					.concat(Objects.toString(e.getMessage(), "")), e.getErrors());
		}

		LOGGER.info("Salio BupoRestClient#obtenerMontoRecargo(request)");
		return response;
	}

	public TransmitirTraspasoResponse transmitirTraspaso(TransmitirTraspasoRequest request) {
		LOGGER.info("Entro BupoRestClient#transmitirTraspaso(request)");
		TransmitirTraspasoResponse response = null;

		try {
			response = obtenerPostPorObjeto(getUrlTransmitirTraspaso(), request, TransmitirTraspasoResponse.class);
		} catch (SivSOAException e) {
			throw new SivSOAException(Utilitarios
					.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_GLOBAL_OBTENER_MONTO_RECARGO)
					.concat(Objects.toString(e.getMessage(), "")), e.getErrors());
		}

		LOGGER.info("Salio BupoRestClient#transmitirTraspaso(request)");
		return response;
	}

	public PolizaDetalleResponse obtenerDetallePoliza(ObtenerDetallePolizaRequest request) {
		LOGGER.info("Entro BupoRestClient#obtenerDetallePoliza(request)");
		PolizaDetalleResponse responseFinal = null;

		try {

			String url = getUrlObtenerPolizas().concat("/").concat(request.getNumPoliza());
			responseFinal = obtenerGetPorObjeto(url, PolizaDetalleResponse.class);

		} catch (SivSOAException e) {
			throw new SivSOAException(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_GLOBAL_OBTENER_POLIZAS)
							.concat(Objects.toString(e.getMessage(), "")),
					e.getErrors());
		}

		LOGGER.info("Salio BupoRestClient#obtenerDetallePoliza(request)");
		return responseFinal;
	}

	/**
	 * Validar Plaft
	 *
	 * @param request
	 * @return
	 */
	public ValidarProspectoAsignacionResponse validarPlaft(ObtenerPolizaRequest request) {
		LOGGER.info("Entro BupoRestClient#validarPlaft(request)");
		ValidarProspectoAsignacionResponse response = null;

		try {
			String url = getUrlValidarPlaft().concat("/0").concat(request.getTipoDoc()).concat("-")
					.concat(request.getNroDoc());
			response = obtenerGetPorObjeto(url, ValidarProspectoAsignacionResponse.class);
		} catch (SivSOAException e) {
			throw new SivSOAException(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_GLOBAL_OBTENER_POLIZAS)
							.concat(Objects.toString(e.getMessage(), "")),
					e.getErrors());
		}

		LOGGER.info("Salio BupoRestClient#validarPlaft(request)");
		return response;
	}

	public ValidarProspectoAsignacionResponse alertaPlaft(ObtenerPolizaRequest request) {
		LOGGER.info("Entro BupoRestClient#alertaPlaft(request)");
		ValidarProspectoAsignacionResponse response = null;

		try {
			String url = getUrlAlertaPlaft().concat("/0").concat(request.getTipoDoc()).concat("-")
					.concat(request.getNroDoc());
			response = obtenerGetPorObjeto(url, ValidarProspectoAsignacionResponse.class);
		} catch (SivSOAException e) {
			throw new SivSOAException(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_GLOBAL_OBTENER_POLIZAS)
							.concat(Objects.toString(e.getMessage(), "")),
					e.getErrors());
		}

		LOGGER.info("Salio BupoRestClient#validarPlaft(request)");
		return response;
	}

	public ValidarProspectoAsignacionResponse alertaPlaftEmision(ObtenerPlaftEmisionRequest request) {

		LOGGER.info("Entro BupoRestClient#alertaPlaftEmision(request)");
		ValidarProspectoAsignacionResponse response = null;
		try {
			// String url = getUrlObtenerPersonaDocuemnto().concat("/").concat(idPersona);
			response = obtenerPostPorObjeto(getUrlAlertaPlaft(), request, ValidarProspectoAsignacionResponse.class);
		} catch (SivSOAException e) {
			throw new SivSOAException(Utilitarios
					.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_CRM_URL_COTIZACION_RECOTIZAR)
					.concat(Objects.toString(e.getMessage(), "")), e.getErrors());
		}
		// response.setData(listados);
		LOGGER.info("Salio BupoRestClient#alertaPlaftEmision(request)");
		return response;
	}

	public EnvioCorreoClienteEmisionResponse envioCorreoCliente(EnvioCorreoClienteEmisionRequest request) {

		LOGGER.info("Entro BupoRestClient#alertaPlaftEmision(request)");
		EnvioCorreoClienteEmisionResponse response = null;
		try {
			// String url = getUrlObtenerPersonaDocuemnto().concat("/").concat(idPersona);
			response = obtenerPostPorObjeto(getUrlEnvioCorreo(), request, EnvioCorreoClienteEmisionResponse.class);
		} catch (SivSOAException e) {
			throw new SivSOAException(Utilitarios
					.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_CRM_URL_COTIZACION_RECOTIZAR)
					.concat(Objects.toString(e.getMessage(), "")), e.getErrors());
		}
		// response.setData(listados);
		LOGGER.info("Salio BupoRestClient#alertaPlaftEmision(request)");
		return response;
	}

	public ObtenerAgenteResponse obtenerAgentexCorreo(String correo) {
		LOGGER.info("Entro BupoRestClient#obtenerAgentexCorreo(request)");
		ObtenerAgenteResponse response = null;

		try {
			String url = getUrlObtenerAgentexCorreo().concat("/").concat(correo);
			response = obtenerGetPorObjeto(url, ObtenerAgenteResponse.class);
			// System.out.println(response);
		} catch (SivSOAException e) {
			throw new SivSOAException(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_GLOBAL_OBTENER_POLIZAS)
							.concat(Objects.toString(e.getMessage(), "")),
					e.getErrors());
		}

		LOGGER.info("Salio BupoRestClient#obtenerAgentexCorreo(request)");
		return response;
	}

	public ObtenerAgenteResponse obtenerAgente(ObtenerAgenteRequest request) {
		LOGGER.info("Entro BupoRestClient#obtenerAgente(request)");
		ObtenerAgenteResponse response = null;

		try {
			String url = getUrlObtenerAgente().concat("/").concat(request.getCodAgencia());
			response = obtenerGetPorObjeto(url, ObtenerAgenteResponse.class);
			// System.out.println(response);
		} catch (SivSOAException e) {
			throw new SivSOAException(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_GLOBAL_OBTENER_POLIZAS)
							.concat(Objects.toString(e.getMessage(), "")),
					e.getErrors());
		}

		LOGGER.info("Salio BupoRestClient#obtenerAgente(request)");
		return response;
	}

	public ObtenerAgenteResponse obtenerAgenteAll(ObtenerAgenteRequest request) {
		LOGGER.info("Entro BupoRestClient#obtenerAgenteAll(request)");
		ObtenerAgenteResponse response = null;

		try {
			String url = getUrlObtenerAgenteAll();
			response = obtenerGetPorObjeto(url, ObtenerAgenteResponse.class);
			// System.out.println(response);
		} catch (SivSOAException e) {
			throw new SivSOAException(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_GLOBAL_OBTENER_POLIZAS)
							.concat(Objects.toString(e.getMessage(), "")),
					e.getErrors());
		}

		LOGGER.info("Salio BupoRestClient#obtenerAgenteAll(request)");
		return response;
	}

	public ObtenerAgenteResponse obtenerCargoAgente(String correo) {
		LOGGER.info("Entro BupoRestClient#obtenerAgente(request)");
		ObtenerAgenteResponse response = null;

		try {
			String url = getUrlObtenerCargoAgente().concat("/").concat(correo);
			response = obtenerGetPorObjeto(url, ObtenerAgenteResponse.class);
			// System.out.println(response);
		} catch (SivSOAException e) {
			throw new SivSOAException(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_GLOBAL_OBTENER_POLIZAS)
							.concat(Objects.toString(e.getMessage(), "")),
					e.getErrors());
		}

		LOGGER.info("Salio BupoRestClient#obtenerAgente(request)");
		return response;
	}

	public ObtenerReglasAsigClienteResponse obtenerReglasAsigCliente(ObtenerReglasAsigClienteRequest request) {
		LOGGER.info("Entro BupoRestClient#obtenerReglasAsigCliente(request)");
		ObtenerReglasAsigClienteResponse response = null;

		try {

			String url = getUrlReglasAsigCliente().concat("/").concat(request.getNumDoc()).concat("/")
					.concat(request.getCorreo());
			System.out.println(url);
			response = obtenerGetPorObjeto(url, ObtenerReglasAsigClienteResponse.class);
			// System.out.println(response);
		} catch (SivSOAException e) {
			throw new SivSOAException(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_GLOBAL_OBTENER_POLIZAS)
							.concat(Objects.toString(e.getMessage(), "")),
					e.getErrors());
		}

		LOGGER.info("Salio BupoRestClient#obtenerReglasAsigCliente(request)");
		return response;
	}

	public ObtenerConsultaAsigAcseleResponse consultarAsignacionAcsele(ObtenerReglasAsigClienteRequest request) {
		LOGGER.info("Entro BupoRestClient#consultarAsignacionAcsele(request)");
		ObtenerConsultaAsigAcseleResponse response = null;

		try {
			String url = getUrlConsultaAsignacionAcsele().concat("/").concat(request.getNumDoc());
			response = obtenerGetPorObjeto(url, ObtenerConsultaAsigAcseleResponse.class);
		} catch (SivSOAException e) {
			throw new SivSOAException(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_GLOBAL_OBTENER_POLIZAS)
							.concat(Objects.toString(e.getMessage(), "")),
					e.getErrors());
		}

		LOGGER.info("Salio BupoRestClient#consultarAsignacionAcsele(request)");
		return response;
	}

	public ListObtenerCotizacionesResponse obtenerCotizacion(ObtenerReglasAsigClienteRequest request) {
		LOGGER.info("Entro BupoRestClient#obtenerCotizacion(request)");
		ListObtenerCotizacionesResponse response = null;

		try {
			System.out.println(gson.toJson(null));
			String url = getUrlObtenerCotizacion().concat("/").concat(request.getNumDoc());
			System.out.println(url);
			response = obtenerGetPorObjeto(url, ListObtenerCotizacionesResponse.class);
		} catch (SivSOAException e) {
			throw new SivSOAException(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_GLOBAL_OBTENER_POLIZAS)
							.concat(Objects.toString(e.getMessage(), "")),
					e.getErrors());
		}

		LOGGER.info("Salio BupoRestClient#obtenerCotizacion(request)");
		return response;
	}

	// HCR
	public ValidarTraspasoResponse validarTraspaso(ValidarTraspasoRequest request) {
		LOGGER.info("Entro GlobalRestClient#validarTraspaso(request)");
		ValidarTraspasoResponse response = null;
		String url = getUrlValidarTraspaso().concat("/").concat(request.getNumCotizacionOrigen()).concat("/")
				.concat(request.getNumCotizacionDestino());
		System.out.println(url);
		try {
			response = obtenerGetPorObjeto(url, ValidarTraspasoResponse.class);
			LOGGER.info("GlobalRestClient#validarTraspaso response: " + new Gson().toJson(response));
		} catch (SivSOAException e) {
			throw new SivSOAException(Utilitarios.obtenerMensaje(messageSource, "Ocurrió un error en HTTP GET: " + url)
					.concat(Objects.toString(e.getMessage(), "")), e.getErrors());
		}

		LOGGER.info("Salio GlobalRestClient#validarTraspaso(request)");
		return response;
	}

	public ConsultarAcreditacionResponse consultarAcreditacion(ObtenerReglasAsigClienteRequest request) {
		LOGGER.info("Entro BupoRestClient#consultarAcreditacion(request)");
		ConsultarAcreditacionResponse response = null;

		try {
			System.out.println(getUrlConsultarAcreditacion());
			System.out.println(request.getNumCotizacion());
			String url = getUrlConsultarAcreditacion().concat("/").concat(request.getNumCotizacion());
			System.out.println(url);
			response = obtenerGetPorObjeto(url, ConsultarAcreditacionResponse.class);
		} catch (SivSOAException e) {
			throw new SivSOAException(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_GLOBAL_OBTENER_POLIZAS)
							.concat(Objects.toString(e.getMessage(), "")),
					e.getErrors());
		}

		LOGGER.info("Salio BupoRestClient#consultarAcreditacion(request)");
		return response;
	}

	public ObtenerAgenteResponse obtenerAgentecCodigo(String codAgente) {
		LOGGER.info("Entro BupoRestClient#obtenerAgentecCodigo(request)");
		ObtenerAgenteResponse response = null;

		try {
			String url = getUrlObtenerAgentexCodigo().concat("/").concat(codAgente);
			response = obtenerGetPorObjeto(url, ObtenerAgenteResponse.class);

		} catch (SivSOAException e) {
			throw new SivSOAException(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_GLOBAL_OBTENER_POLIZAS)
							.concat(Objects.toString(e.getMessage(), "")),
					e.getErrors());
		}

		LOGGER.info("Salio BupoRestClient#obtenerAgentecCodigo(request)");
		return response;
	}

	public ObtenerStatusResponse validarAgenteActivo(String correo) {
		LOGGER.info("Entro BupoRestClient#validarAgenteActivo(request)");
		ObtenerStatusResponse response = null;

		try {
			String url = getUrlValidarAgenteActivo().concat("/").concat(correo);
			response = obtenerGetPorObjeto(url, ObtenerStatusResponse.class);

		} catch (SivSOAException e) {
			throw new SivSOAException(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_GLOBAL_OBTENER_POLIZAS)
							.concat(Objects.toString(e.getMessage(), "")),
					e.getErrors());
		}

		LOGGER.info("Salio BupoRestClient#validarAgenteActivo(request)");
		return response;
	}

	public ActualizarContactoVtigerResponse actualizarContactoVtiger(ActualizarContactoVtigerRequest contactoRequest) {

		LOGGER.info("Entro actualizarContactoVtiger(contactoRequest)");
		ActualizarContactoVtigerResponse actualizarContactoResponse = new ActualizarContactoVtigerResponse();

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getUrlActualizarContactoVtiger());

			HttpEntity<ActualizarContactoVtigerRequest> requestEntity = new HttpEntity<>(contactoRequest, headers);
			// -- Response
			ResponseEntity<ActualizarContactoVtigerResponse> response = getRestTemplate().exchange(
					builder.build().toString(), HttpMethod.PUT, requestEntity, ActualizarContactoVtigerResponse.class);
			actualizarContactoResponse = response.getBody();
			actualizarContactoResponse.setStatusHttp("200");

		} catch (SivSOAException e) {
			throw new SivSOAException(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_GLOBAL_OBTENER_POLIZAS)
							.concat(Objects.toString(e.getMessage(), "")),
					e.getErrors());
		} catch (final HttpClientErrorException e) {
			LOGGER.info(
					"Entro actualizarContactoVtiger(contactoRequest) HttpClientErrorException: " + e.getStatusCode());
			LOGGER.info("Entro actualizarContactoVtiger(contactoRequest) HttpClientErrorException: "
					+ e.getResponseBodyAsString());
			actualizarContactoResponse = gson.fromJson(e.getResponseBodyAsString(),
					ActualizarContactoVtigerResponse.class);
			actualizarContactoResponse.setStatusHttp("404");
			Errors errors = new BeanPropertyBindingResult(ActualizarContactoVtigerResponse.class,
					"actualizarContactoVtiger");
			throw new SivSOAException(actualizarContactoResponse.getMensaje(), errors);
		}

		return actualizarContactoResponse;
	}

	public CrearContactoVtigerResponse crearContactoVtiger(CrearContactoVtigerRequest contactoRequest) {

		LOGGER.info("Entro crearContactoVtiger(request)");
		CrearContactoVtigerResponse response = null;

		try {
			// String url =
			// getUrlObtenerMontoRecarga().concat("/").concat(request.getNroCotizacion());
			response = obtenerPostPorObjeto(getUrlCrearContactoVtiger(), contactoRequest,
					CrearContactoVtigerResponse.class);

		} catch (SivSOAException e) {
			throw new SivSOAException(Utilitarios
					.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_GLOBAL_OBTENER_MONTO_RECARGO)
					.concat(Objects.toString(e.getMessage(), "")), e.getErrors());
		}

		return response;
	}

	public CrearPotentialVtigerResponse crearPotentialVtiger(CrearPotentialVtigerRequest contactoRequest) {

		LOGGER.info("Entro crearPotentialVtiger(request)");
		CrearPotentialVtigerResponse response = null;

		try {
			// String url =
			// getUrlObtenerMontoRecarga().concat("/").concat(request.getNroCotizacion());

			response = obtenerPostPorObjeto(getUrlCrearPotentialVtiger(), contactoRequest,
					CrearPotentialVtigerResponse.class);

		} catch (SivSOAException e) {
			throw new SivSOAException(Utilitarios
					.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_GLOBAL_OBTENER_MONTO_RECARGO)
					.concat(Objects.toString(e.getMessage(), "")), e.getErrors());
		}

		return response;
	}

	public obtenerPotentialVtigerResponse obtenerPotentialVtiger(obtenerPotentialVtigerRequest contactoRequest) {

		LOGGER.info("Entro obtenerPotentialVtiger(request)");
		obtenerPotentialVtigerResponse response = null;

		try {
			// String url =
			// getUrlObtenerMontoRecarga().concat("/").concat(request.getNroCotizacion());
			String url = getUrlObtenerPotential().concat("/").concat(contactoRequest.getTipoDoc()).concat("/")
					.concat(contactoRequest.getNroDoc());
			response = obtenerGetPorObjeto(url, obtenerPotentialVtigerResponse.class);

		} catch (SivSOAException e) {
			throw new SivSOAException(Utilitarios
					.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_GLOBAL_OBTENER_MONTO_RECARGO)
					.concat(Objects.toString(e.getMessage(), "")), e.getErrors());
		}

		return response;
	}

	public obtenerContactVtigerResponse obtenerContactVtiger(obtenerPotentialVtigerRequest contactoRequest) {

		LOGGER.info("Entro obtenerPotential(request)");
		obtenerContactVtigerResponse response = null;

		try {
			// String url =
			// getUrlObtenerMontoRecarga().concat("/").concat(request.getNroCotizacion());
			String url = getUrlObtenerContacto().concat("/").concat(contactoRequest.getTipoDoc()).concat("/")
					.concat(contactoRequest.getNroDoc());
			response = obtenerGetPorObjeto(url, obtenerContactVtigerResponse.class);

		} catch (SivSOAException e) {
			throw new SivSOAException(Utilitarios
					.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_GLOBAL_OBTENER_MONTO_RECARGO)
					.concat(Objects.toString(e.getMessage(), "")), e.getErrors());
		}

		return response;
	}

	public obtenerPersonaResponse obtenerPersona(ObtenerPolizaRequest Request) {

		LOGGER.info("Entro obtenerPersona(request)");
		obtenerPersonaResponse response = null;

		try {
			// String url =
			// getUrlObtenerMontoRecarga().concat("/").concat(request.getNroCotizacion());
			String url = getUrlObtenerPersona().concat("/01-").concat(Request.getTipoDoc()).concat("-")
					.concat(Request.getNroDoc());
			response = obtenerGetPorObjeto(url, obtenerPersonaResponse.class);

		} catch (SivSOAException e) {
			throw new SivSOAException(Utilitarios
					.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_GLOBAL_OBTENER_MONTO_RECARGO)
					.concat(Objects.toString(e.getMessage(), "")), e.getErrors());
		}

		return response;
	}

	public obtenerTipoCambioResponse ObtenerTipoCambio(obtenerTipoCambioRequest Request) {

		LOGGER.info("Entro obtenerPotential(request)");
		obtenerTipoCambioResponse response = null;

		try {
			// String url =
			// getUrlObtenerMontoRecarga().concat("/").concat(request.getNroCotizacion());
			String url = getUrlObtenerTipoCambio().concat("/").concat(Request.getFecha()).concat("/")
					.concat(Request.getTag());
			response = obtenerGetPorObjeto(url, obtenerTipoCambioResponse.class);

		} catch (SivSOAException e) {
			throw new SivSOAException(Utilitarios
					.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_GLOBAL_OBTENER_MONTO_RECARGO)
					.concat(Objects.toString(e.getMessage(), "")), e.getErrors());
		}

		return response;
	}

	public UrlCotizadorNuevoPGResponse obtenerUrlCotizador(UrlCotizadorNuevoPGRequest request) {
		LOGGER.info("Entro CrmRestClient#obtenerUrlCotizador(request)");
		/*
		 * persistence.rest.client.crm.cotizador.intermedio.servidor:
		 * http://10.29.33.154:82/CRM_ServiciosCotizador_ADN/ServicioVD.svc
		 * persistence.rest.client.crm.cotizador.nuevo: /rest/NuevoCotizador
		 */
		Gson gson = new Gson();
		UrlCotizadorNuevoPGResponse response = new UrlCotizadorNuevoPGResponse();
		response.setTitle(getUrlCotizadorNuevo());
		try {
			System.out.println(gson.toJson(request));
			response = obtenerPostPorObjeto(getUrlCotizadorNuevo(), request, UrlCotizadorNuevoPGResponse.class);
			response.setStatusHttp("200");
		} catch (Exception e) {
			response.setStatusHttp("500");
			response.setMessage(e.getMessage());
			response.setCode("CRM-006");
			/*
			 * throw new SivSOAException( Utilitarios.obtenerMensaje(messageSource,
			 * Constantes.MENSAJE_RESTCLIENT_CRM_URL_COTIZACION_NUEVO).concat(
			 * Objects.toString(e.getMessage(), "") ), e.getErrors() );
			 */
		}

		LOGGER.info("Salio CrmRestClient#obtenerUrlCotizador(request)");
		return response;
	}

	public UrlCotizadorNuevoPGResponse obtenerUrlRecotizarVida(UrlRecotizarVidaPGRequest request) {
		LOGGER.info("Entro CrmRestClient#obtenerUrlRecotizarVida(request)");

		UrlCotizadorNuevoPGResponse response = null;

		try {
			String url = getUrlCrmRecotizarVida();
			LOGGER.info("URL:" + url);
			response = obtenerPostPorObjeto(url, request, UrlCotizadorNuevoPGResponse.class);
		} catch (SivSOAException e) {
			throw new SivSOAException(Utilitarios
					.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_CRM_URL_COTIZACION_RECOTIZAR)
					.concat(Objects.toString(e.getMessage(), "")), e.getErrors());
		}

		LOGGER.info("Salio CrmRestClient#obtenerUrlRecotizarVida(request)");
		return response;
	}

	public ObtenerPersonaDocumentoResponse obtenerPersonaDocumento(String tipoDoc, String numDoc) {
		LOGGER.info("Entro CrmRestClient#obtenerPersonaDocumento(request)");

		ObtenerPersonaDocumentoResponse response = null;

		try {
			String url = getUrlObtenerPersonaDocuemnto().concat("/").concat("0" + tipoDoc).concat("-").concat(numDoc);
			response = obtenerGetPorObjeto(url, ObtenerPersonaDocumentoResponse.class);
		} catch (SivSOAException e) {
			throw new SivSOAException(Utilitarios
					.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_CRM_URL_COTIZACION_RECOTIZAR)
					.concat(Objects.toString(e.getMessage(), "")), e.getErrors());
		}

		LOGGER.info("Salio CrmRestClient#obtenerPersonaDocumento(request)");
		return response;
	}

	public ObtenerPersonaDocumentoResponse obtenerPersonaDocumento2(String idPersona) {
		LOGGER.info("Entro CrmRestClient#obtenerPersonaDocumento2(request)");

		ObtenerPersonaDocumentoResponse response = null;

		try {
			String url = getUrlObtenerPersonaDocuemnto2().concat("/").concat(idPersona);
			response = obtenerGetPorObjeto(url, ObtenerPersonaDocumentoResponse.class);
		} catch (SivSOAException e) {
			throw new SivSOAException(Utilitarios
					.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_CRM_URL_COTIZACION_RECOTIZAR)
					.concat(Objects.toString(e.getMessage(), "")), e.getErrors());
		}

		LOGGER.info("Salio CrmRestClient#obtenerPersonaDocumento2(request)");
		return response;
	}

	public ListObtenerIdPersonaResponse obtenerPersonaDocumento(ObtenerIdPersonaRequest obtenerIdPersonaRequest) {
		LOGGER.info("Entro CrmRestClient#obtenerPersonaDocumento(request)");

		ListObtenerIdPersonaResponse response = null;

		try {
			// String url = getUrlObtenerPersonaDocuemnto().concat("/").concat(idPersona)
			response = obtenerPostPorObjeto(getUrlObtenerPersonaDocuemnto(), obtenerIdPersonaRequest,
					ListObtenerIdPersonaResponse.class);
		} catch (SivSOAException e) {
			LOGGER.info(e.getMessage());
			throw new SivSOAException(Utilitarios
					.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_CRM_URL_COTIZACION_RECOTIZAR)
					.concat(Objects.toString(e.getMessage(), "")), e.getErrors());
		}

		LOGGER.info("Salio CrmRestClient#obtenerPersonaDocumento(request)");

		return response;
	}

	public ObtenerEvaluacionSolicitudEmisionResponse obtenerEvaluacionSolicitudEmision(
			ObtenerEvaluacionSolicitudEmisionRequest obtenerEvaluacionSolicitudEmisionRequest) {
		LOGGER.info("Entro CrmRestClient#obtenerEvaluacionSolicitudEmision(request)");
		ObtenerEvaluacionSolicitudEmisionResponse response = null;

		try {
			response = obtenerPostPorObjeto(getUrlEvaluacionAutomatica(), obtenerEvaluacionSolicitudEmisionRequest,
					ObtenerEvaluacionSolicitudEmisionResponse.class);
		} catch (SivSOAException e) {
			throw new SivSOAException(Utilitarios
					.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_CRM_URL_COTIZACION_RECOTIZAR)
					.concat(Objects.toString(e.getMessage(), "")), e.getErrors());
		}
		LOGGER.info("Salio CrmRestClient#obtenerEvaluacionSolicitudEmision(request)");
		return response;
	}

	public ObtenerEvaluacionSolicitudEmisionResponse obtenerReEvaluacionSolicitud(
			ObtenerEvaluacionSolicitudEmisionRequest obtenerEvaluacionSolicitudEmisionRequest) {
		LOGGER.info("Entro CrmRestClient#obtenerEvaluacionSolicitudEmision(request)");
		ObtenerEvaluacionSolicitudEmisionResponse response = null;

		try {
			// String url = getUrlObtenerPersonaDocuemnto().concat("/").concat(idPersona);
			response = obtenerPostPorObjeto(getUrlReEvaluacion(), obtenerEvaluacionSolicitudEmisionRequest,
					ObtenerEvaluacionSolicitudEmisionResponse.class);
		} catch (SivSOAException e) {
			throw new SivSOAException(Utilitarios
					.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_CRM_URL_COTIZACION_RECOTIZAR)
					.concat(Objects.toString(e.getMessage(), "")), e.getErrors());
		}
		// response.setData(listados);
		LOGGER.info("Salio CrmRestClient#obtenerEvaluacionSolicitudEmision(request)");

		return response;
	}

	public EmisionAutomaticaResponse emisionAutomatica(EmisionAutomaticaRequest emisionAutomaticaRequest) {
		long timeStart = System.nanoTime();
		long timeEnd;
		LOGGER.info("Entro CrmRestClient#emisionAutomatica(request)");
		EmisionAutomaticaResponse response = null;

		try {
			// String url = getUrlObtenerPersonaDocuemnto().concat("/").concat(idPersona)
			LOGGER.info("request <----- {}", emisionAutomaticaRequest);
			LOGGER.info(" url -> {}", getUrlEmisionAutomatica());
			response = obtenerPostPorObjeto(getUrlEmisionAutomatica(), emisionAutomaticaRequest,
					EmisionAutomaticaResponse.class);
			LOGGER.info("Response emision automatica {}", response);
		} catch (SivSOAException e) {
			LOGGER.info("Error call emisionAutomatica {}", e.getMessage());
			throw new SivSOAException(Utilitarios
					.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_CRM_URL_COTIZACION_RECOTIZAR)
					.concat(Objects.toString(e.getMessage(), "")), e.getErrors());
		}
		// response.setData(listados)
		LOGGER.info("Salio CrmRestClient#emisionAutomatica(request)");
		timeEnd = System.nanoTime();
		LOGGER.info("Diferencia emisionAutomatica: {} ", (timeEnd - timeStart));
		return response;
	}

	public DocumentoCorreoResponse documentoCorreo(DocumentoCorreoRequest documentoCorreoRequest) {
		LOGGER.info("Entro CrmRestClient#documentoCorreo(request)");
		DocumentoCorreoResponse response = null;

		try {
			// String url = getUrlObtenerPersonaDocuemnto().concat("/").concat(idPersona)
			LOGGER.info(documentoCorreoRequest + "<-----documentoCorreoRequest");
			LOGGER.info(getUrlGestorEnvoDocumento() + "<----- getUrlGestorEnvoDocumento");
			response = obtenerPostPorObjeto(getUrlGestorEnvoDocumento(), documentoCorreoRequest,
					DocumentoCorreoResponse.class);
		} catch (SivSOAException e) {
			throw new SivSOAException(Utilitarios
					.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_CRM_URL_COTIZACION_RECOTIZAR)
					.concat(Objects.toString(e.getMessage(), "")), e.getErrors());
		}
		// response.setData(listados);
		LOGGER.info("Salio CrmRestClient#documentoCorreo(request)");

		return response;
	}

	public GestiorDocumentoResponse gestorDocumentos(GestiorDocumentoRequest gestiorDocumentoRequest) {
		LOGGER.info("Entro CrmRestClient#documentoCorreo(request)");
		GestiorDocumentoResponse response = null;

		try {
			// String url = getUrlObtenerPersonaDocuemnto().concat("/").concat(idPersona);
			LOGGER.info(gestiorDocumentoRequest + "<----gestiorDocumentoRequest");
			LOGGER.info(getUrlSolicitudGestorDocumento() + "<---getUrlSolicitudGestorDocumento");
			response = obtenerPostPorObjeto(getUrlSolicitudGestorDocumento(), gestiorDocumentoRequest,
					GestiorDocumentoResponse.class);
		} catch (SivSOAException e) {
			throw new SivSOAException(Utilitarios
					.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_CRM_URL_COTIZACION_RECOTIZAR)
					.concat(Objects.toString(e.getMessage(), "")), e.getErrors());
		}
		// response.setData(listados);
		LOGGER.info("Salio CrmRestClient#documentoCorreo(request)");

		return response;
	}

	public ObtenerMuerteAccidentalResponse obtenerMuerteAccidental(
			ObtenerMuerteAccidentalRequest obtenerMuerteAccidentalRequest) {
		LOGGER.info("Entro CrmRestClient#obtenerMuerteAccidental(request)");
		ObtenerMuerteAccidentalResponse response = null;

		try {
			// String url = getUrlObtenerPersonaDocuemnto().concat("/").concat(idPersona);

			LOGGER.info(getUrlObtenerMuerteAccidental() + "<-----");
			response = obtenerPostPorObjeto(getUrlObtenerMuerteAccidental(), obtenerMuerteAccidentalRequest,
					ObtenerMuerteAccidentalResponse.class);
		} catch (SivSOAException e) {
			throw new SivSOAException(Utilitarios
					.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_CRM_URL_COTIZACION_RECOTIZAR)
					.concat(Objects.toString(e.getMessage(), "")), e.getErrors());
		}
		// response.setData(listados);
		LOGGER.info("Salio CrmRestClient#obtenerMuerteAccidental(request)");

		return response;
	}

	public ObtenerPersonaDocumentoResponse insertarPersonaDocumento(
			InsertarPersonaDocumentoRequest personaDocumentoRequest) {

		LOGGER.info("Entro insertarPersonaDocumento(request)");
		ObtenerPersonaDocumentoResponse response = null;

		try {
			// String url =
			// getUrlObtenerMontoRecarga().concat("/").concat(request.getNroCotizacion());
			System.out.println(gson.toJson(personaDocumentoRequest));
			response = obtenerPostPorObjeto(getUrlInsertarPersonaDocuemnto(), personaDocumentoRequest,
					ObtenerPersonaDocumentoResponse.class);

		} catch (SivSOAException e) {
			throw new SivSOAException(Utilitarios
					.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_GLOBAL_OBTENER_MONTO_RECARGO)
					.concat(Objects.toString(e.getMessage(), "")), e.getErrors());
		}

		return response;
	}

	public UploadArchivoResponse uploadArchivo(UploadArchivoRequest request) {
		LOGGER.info("Entro CrmRestClient#uploadArchivo(request)");

		UploadArchivoResponse response = null;
		ObtenerUploadArchivoResponse uploadArchivoResponse = new ObtenerUploadArchivoResponse();

		ObtenerPersonaDocumentoResponse item = new ObtenerPersonaDocumentoResponse();

		try {
			String contactVal = "";
			Persona persona = personaRepository.findByNumeroDocumento(request.getNumeroDocumento());
			if (persona != null && persona.getIdPersonaCS() != null) {
				contactVal=persona.getIdPersonaCS();
			} else {
				String tipoDocVal = "";
				if (request.getTipoDocumentoAdn().equals("1")) {
					tipoDocVal = "DNI";
				} else if (request.getTipoDocumentoAdn().equals("2")) {
					tipoDocVal = CARNET_DE_EXTRANJERIA;
				}

				ObtenerIdPersonaRequest obtenerIdPersonaRequest = new ObtenerIdPersonaRequest();
				obtenerIdPersonaRequest.setNumero_documento(request.getNumeroDocumento());
				obtenerIdPersonaRequest.setTipo_documento(tipoDocVal);
				List<ObtenerPersonaDocumentoResponse> lista = new ArrayList<>();
				ListObtenerIdPersonaResponse responseObtenerPersonaDocumento = obtenerPersonaDocumento(obtenerIdPersonaRequest);

				responseObtenerPersonaDocumento.getData().forEach(cot -> {
					item.setID_PERSONA(cot.getID_PERSONA().toLowerCase());
					lista.add(item);
					ArchivoResponse[] listFiles = getFilesStorage(cot.getID_PERSONA());
					if (listFiles.length > 0) {
						uploadArchivoResponse.setContactId(cot.getID_PERSONA().toLowerCase());
						uploadArchivoResponse.setStatusHttp("200");
					}
				});

				if (uploadArchivoResponse.getContactId() != null) {
					contactVal = uploadArchivoResponse.getContactId();
				} else {
					contactVal = (lista.size() > 1)?lista.get(1).getID_PERSONA():lista.get(0).getID_PERSONA();
				}
				if(persona!=null){
					persona.setIdPersonaCS(contactVal);
					personaRepository.save(persona);
				}
			}

			FileSystemResource fileSource = new FileSystemResource(request.getFile());
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			headers.set(cloudStorageApp, cloudStorageAppValue);

			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("file", fileSource);
			body.add("folder", contactVal.toLowerCase());
			LOGGER.info("contactVal {}", contactVal.toLowerCase());
			LOGGER.info("fileSource {}", fileSource);
			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
			response = obtenerPostPorObjeto(getUrlCloudStorageUpload(), requestEntity, UploadArchivoResponse.class);
			response.setRespuesta(true);
			LOGGER.info("RPTA=> {}", response.getMessage());

		} catch (SivSOAException e) {
			throw new SivSOAException(Utilitarios.obtenerMensaje(messageSource, "Error al subir archivo al storage")
					.concat(Objects.toString(e.getMessage(), "")), e.getErrors());
		}
		LOGGER.info("Salio CrmRestClient#uploadArchivo(request)");
		return response;
	}

	/**
	 * Only upload files to cloudstorage
	 * 
	 * @param request UploadArchivoRequest
	 * @return confirmation.
	 */
	public UploadArchivoResponse uploadArchivoV2(UploadArchivoRequest request) {
		String traza = Utilitarios.trazaLog() + "-" + request.getNombreArchivo();
		LOGGER.info("{} Entro CrmRestClient#uploadArchivoV2(request)", traza);
		try {

			FileSystemResource fileSource = new FileSystemResource(request.getFile());
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			headers.set(cloudStorageApp, cloudStorageAppValue);

			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("file", fileSource);
			body.add("folder", request.getContactVal().toLowerCase());

			LOGGER.info("{} body {}", traza, fileSource);
			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
			UploadArchivoResponse response = obtenerPostPorObjeto(getUrlCloudStorageUpload(), requestEntity,
					UploadArchivoResponse.class);
			response.setRespuesta(true);
			LOGGER.info("{} RPTA=> {}", traza, response.getMessage());
			LOGGER.info("{}Salio CrmRestClient#uploadArchivoV2(request)", traza);
			return response;
		} catch (SivSOAException e) {
			throw new SivSOAException(Utilitarios.obtenerMensaje(messageSource, "Error al subir archivo al storage")
					.concat(Objects.toString(e.getMessage(), "")), e.getErrors());
		}
	}

	/**
	 * Obtiene los archivos desde cloud storage por id persona
	 * 
	 * @param idPersona parametro buscado por numero documento
	 * @return listado de archivos
	 */
	public ArchivoResponse[] getFilesStorage(String idPersona) {
		String traza = Utilitarios.trazaLog() + "-" + idPersona;
		try {
			LOGGER.info("{} Entro getFilesStorage", traza);
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getCloudStorageFiles().concat("/").concat(idPersona.toLowerCase()));
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			headers.set(cloudStorageApp, cloudStorageAppValue);

			ResponseEntity<ArchivoResponse[]> fileResponse = getRestTemplate().exchange(builder.build().toString(),
					HttpMethod.GET, new HttpEntity<String>(null, headers), ArchivoResponse[].class);
			ArchivoResponse[] listFiles = fileResponse.getBody();
			if (Objects.isNull(listFiles)) {
				throw new SivSOAException("FILES CLOUD NULL", null);
			}

			LOGGER.info("{} Response getFilesStorage {}", traza, gson.toJson(listFiles));
			LOGGER.info("{} Sale getFilesStorage", traza);
			return listFiles;
		} catch (SivSOAException e) {
			throw new SivSOAException(Utilitarios.obtenerMensaje(messageSource, "Error al obtener archivos de storage")
					.concat(Objects.toString(e.getMessage(), "")), e.getErrors());
		}
	}

	public UrlResponse obtenerUrlArchivo(String contactId, String archivo, String nombreArchivo) {
		String traza = Utilitarios.trazaLog() + "-" + contactId;
		LOGGER.info("{} CORREOOO obtenerUrlArchivo 1416", traza);
		LOGGER.info("{} Entro obtenerArchivo(request)", traza);

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			headers.set(cloudStorageApp, cloudStorageAppValue);
			Map<String, Object> uriVariables = new HashMap<>();

			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(getCloudStorageFile().concat("/").concat(contactId).concat("/").concat(nombreArchivo));
			builder.uriVariables(uriVariables);

			// -- Response
			ResponseEntity<UrlResponse> response = getRestTemplate().exchange(builder.build().toString(),
					HttpMethod.GET, new HttpEntity<String>(null, headers), UrlResponse.class);

			UrlResponse urlArchivoResponse = response.getBody();
			if (Objects.isNull(urlArchivoResponse)) {
				throw new SivSOAException("FILES URL CLOUD NULL", null);
			}
			urlArchivoResponse.setStatusHttp("200");
			LOGGER.info("{} urlArchivoResponse{}", traza, gson.toJson(urlArchivoResponse));
			return urlArchivoResponse;
		} catch (SivSOAException e) {
			throw new SivSOAException(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_GLOBAL_OBTENER_POLIZAS)
							.concat(Objects.toString(e.getMessage(), "")),
					e.getErrors());
		}

	}

	public ObtenerMensajeEmisionesResponse validarArchivos(String tipDoc, String numDoc, String nroCot) {
		LOGGER.info("Entro validarArchivos(request)");
		ObtenerMensajeEmisionesResponse obtenerMensajeResponse = new ObtenerMensajeEmisionesResponse();

		try {

			String tipoDocVal = "DNI";
			if (tipDoc.equals("2")) {
				tipoDocVal = CARNET_DE_EXTRANJERIA;
			}
			/* validandp documentos */
			obtenerMensajeResponse.setAcp("");
			obtenerMensajeResponse.setAdn("");
			obtenerMensajeResponse.setEdn("");
			obtenerMensajeResponse.setCotizacion("");
			obtenerMensajeResponse.setSolicitud("");

			String tagSolicitud = "SolicitudDigital_" + numDoc + "_" + nroCot;
			String tagCotizacion = "cotizacion_" + nroCot;
			String tagCotizacion2 = "cotizacion-" + nroCot;
			String tagAdn = "adn_" + numDoc;
			String tagEdn = "EN_" + nroCot;
			String tagAcp = "AcpDigital_" + numDoc + "_" + nroCot;
			String completed = "completado";



			Persona persona = personaRepository.findByNumeroDocumento(numDoc);
			if(persona.getIdPersonaCS()!=null){
				LOGGER.info("Respuesta de obtener persona documento{}", persona.getIdPersonaCS());
				ArchivoResponse[] listFiles = getFilesStorage(persona.getIdPersonaCS());
				Arrays.stream(listFiles).forEach(fil -> {
					if (fil.getName().contains(tagAcp)) {
						LOGGER.info("Documento Acp Encontrado!!!!!!");
						obtenerMensajeResponse.setAcp(completed);
					} else if (fil.getName().contains(tagAdn)) {
						LOGGER.info("Documento Adn Encontrado!!!!!!");
						obtenerMensajeResponse.setAdn(completed);
					} else if (fil.getName().contains(tagEdn)) {
						LOGGER.info("Documento Edn Encontrado!!!!!!");
						obtenerMensajeResponse.setEdn(completed);
					} else if (fil.getName().contains(tagCotizacion) || fil.getName().contains(tagCotizacion2)) {
						LOGGER.info("Documento Cotizacion Encontrado!!!!!!");
						obtenerMensajeResponse.setCotizacion(completed);
					} else if (fil.getName().contains(tagSolicitud)) {
						LOGGER.info("Documento SolicitudDigital Encontrado!!!!!!");
						obtenerMensajeResponse.setSolicitud(completed);
					}
				});
			}else{
				ObtenerIdPersonaRequest obtenerIdPersonaRequest = new ObtenerIdPersonaRequest();
				obtenerIdPersonaRequest.setNumero_documento(numDoc);
				obtenerIdPersonaRequest.setTipo_documento(tipoDocVal);
				ListObtenerIdPersonaResponse responseObtenerPersonaDocumento = obtenerPersonaDocumento(obtenerIdPersonaRequest);
				LOGGER.info("Respuesta de obtener persona documento{}", responseObtenerPersonaDocumento.getData());
				responseObtenerPersonaDocumento.getData().forEach(cot -> {
					ArchivoResponse[] listFiles = getFilesStorage(cot.getID_PERSONA());
					Arrays.stream(listFiles).forEach(fil -> {
						if (fil.getName().contains(tagAcp)) {
							LOGGER.info("Documento Acp Encontrado!!!!!!");
							obtenerMensajeResponse.setAcp(completed);
						} else if (fil.getName().contains(tagAdn)) {
							LOGGER.info("Documento Adn Encontrado!!!!!!");
							obtenerMensajeResponse.setAdn(completed);
						} else if (fil.getName().contains(tagEdn)) {
							LOGGER.info("Documento Edn Encontrado!!!!!!");
							obtenerMensajeResponse.setEdn(completed);
						} else if (fil.getName().contains(tagCotizacion) || fil.getName().contains(tagCotizacion2)) {
							LOGGER.info("Documento Cotizacion Encontrado!!!!!!");
							obtenerMensajeResponse.setCotizacion(completed);
						} else if (fil.getName().contains(tagSolicitud)) {
							LOGGER.info("Documento SolicitudDigital Encontrado!!!!!!");
							obtenerMensajeResponse.setSolicitud(completed);
						}
					});
				});
			}

		} catch (SivSOAException e) {
			obtenerMensajeResponse.setCotizacion("");
			obtenerMensajeResponse.setSolicitud("");
			LOGGER.error("Ocurrio error al validar archivos{}", e.getMessage());
		}

		return obtenerMensajeResponse;
	}

	public ObtenerUploadArchivoResponse obtenerArchivo(String tipDoc, String numDoc, String nroCot) {
		LOGGER.info("Entro obtenerArchivo(request)");
		ObtenerUploadArchivoResponse uploadArchivoResponse = new ObtenerUploadArchivoResponse();
		try {
			Persona persona=personaRepository.findByNumeroDocumento(numDoc);
				ArchivoResponse[] listFiles = getFilesStorage(persona.getIdPersonaCS());
				if (listFiles.length > 0) {
					uploadArchivoResponse.setFiles(Arrays.asList(listFiles));
					uploadArchivoResponse.setContactId(persona.getIdPersonaCS().toLowerCase());
					uploadArchivoResponse.setStatusHttp("200");
				}
		} catch (SivSOAException e) {
			throw new SivSOAException(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_GLOBAL_OBTENER_POLIZAS)
							.concat(Objects.toString(e.getMessage(), "")),
					e.getErrors());
		}
		return uploadArchivoResponse;
	}

	public ObtenerCredentialsResponse obtenerCredentials() {
		LOGGER.info("Entro BupoRestClient#obtenerCredentials()");
		ObtenerCredentialsResponse responseFinal = null;

		try {

			String url = getPaymentCredentials();
			responseFinal = obtenerGetPorObjeto(url, ObtenerCredentialsResponse.class);

		} catch (SivSOAException e) {
			throw new SivSOAException(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_GLOBAL_OBTENER_POLIZAS)
							.concat(Objects.toString(e.getMessage(), "")),
					e.getErrors());
		}

		LOGGER.info("Salio BupoRestClient#obtenerCredentials()");
		return responseFinal;
	}

	public ObtenerPubSubSendResponse obtenerPubSubSendResponse(ObtenerPubSubSendRequest request) {
		String traza = Utilitarios.trazaLog() + "-" + request.getMetadataPublish().get(0).getPropuesta();
		LOGGER.info("{} Entro BupoRestClient#obtenerPubSubSendResponse(request)", traza);
		ObtenerPubSubSendResponse responseFinal = null;

		try {

			LOGGER.info("{} ObtenerPubSubSendRequest {}", traza, gson.toJson(request));
			HttpHeaders headers = new HttpHeaders();
			headers.set("X-Apikey", apiKey);
			headers.set("X-ClientOrigin", "zonaPrivada");

			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getPaymentPubSubSend());
			LOGGER.info("{} header: {}", traza, gson.toJson(headers));
			LOGGER.info("{} builder: {}", traza, gson.toJson(builder));
			LOGGER.info("{} request: {}", traza, gson.toJson(request));
 
			HttpEntity<ObtenerPubSubSendRequest> requestR = new HttpEntity<>(request, headers);
			int i = 0;
			while (i < 5) {
				LOGGER.info("While loop: i = " + i++,traza);
			    try {
					LOGGER.info("{} Ingesando obtenerPostPorObjeto",traza);
					responseFinal = obtenerPostPorObjeto(getPaymentPubSubSend(), requestR, ObtenerPubSubSendResponse.class);
					//i = 5;
					break;
				} catch (Exception e) {
					LOGGER.info("{} responseFinal: {}");
				}
			}
			LOGGER.info("{} responseFinal: {}", traza, gson.toJson(responseFinal));
		} catch (SivSOAException e) {
			LOGGER.error("{} ERROR {}", traza, e.getMessage());
			throw new SivSOAException(Utilitarios
					.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_GLOBAL_OBTENER_MONTO_RECARGO)
					.concat(Objects.toString(e.getMessage(), "")), e.getErrors());
		}

		LOGGER.info("{} Salio BupoRestClient#obtenerPubSubSendResponse(request)", traza);
		return responseFinal;
	}

	public ObtenerPolizaEmisionRapidaResponse obtenerPolizaEmisionRapida(ObtenerPolizaEmisionRapidaRequest request) {
		LOGGER.info("Entro BupoRestClient#obtenerPolizaEmisionRapida(request)");
		ObtenerPolizaEmisionRapidaResponse responseFinal = null;

		try {

			responseFinal = obtenerPostPorObjeto(getUrlSolicitudObtenerPolizaEmisionRapida(), request,
					ObtenerPolizaEmisionRapidaResponse.class);
			// -- Response

		} catch (SivSOAException e) {
			throw new SivSOAException(Utilitarios
					.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_GLOBAL_OBTENER_MONTO_RECARGO)
					.concat(Objects.toString(e.getMessage(), "")), e.getErrors());
		}

		LOGGER.info("Salio BupoRestClient#obtenerPolizaEmisionRapida(request)");
		return responseFinal;
	}

	public ObtenerEstadoSolicitudEmisionRapidaResponse obtenerEstadoSolicitudEmisionRapida(
			ObtenerEstadoSolicitudEmisionRapidaRequest request) {
		LOGGER.info("Entro BupoRestClient#obtenerEstadoSolicitudEmisionRapida(request)");
		ObtenerEstadoSolicitudEmisionRapidaResponse responseFinal = null;

		try {

			responseFinal = obtenerPostPorObjeto(getUrlSolicitudObtenerEstadoSolicitudEmisionRapida(), request,
					ObtenerEstadoSolicitudEmisionRapidaResponse.class);
			// -- Response

		} catch (SivSOAException e) {
			throw new SivSOAException(Utilitarios
					.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_GLOBAL_OBTENER_MONTO_RECARGO)
					.concat(Objects.toString(e.getMessage(), "")), e.getErrors());
		}

		LOGGER.info("Salio BupoRestClient#obtenerEstadoSolicitudEmisionRapida(request)");
		return responseFinal;
	}

	// HCR
	public TasaCambioResponse consultarTasaDeCambio() {
		LOGGER.info("Entro GlobalRestClient#consultarTasaDeCambio()");
		TasaCambioResponse response = null;

		try {
			String url = getUrlConsultaTasaDeCambioActual();
			response = obtenerGetPorObjeto(url, TasaCambioResponse.class);
		} catch (SivSOAException e) {
			throw new SivSOAException(Utilitarios
					.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_GLOBAL_CONSULTAR_TASA_CAMBIO)
					.concat(Objects.toString(e.getMessage(), "")), e.getErrors());
		}

		LOGGER.info("Salio GlobalRestClient#consultarTasaDeCambio()");
		return response;
	}

	public PolizasCanceladasResponse consultarPolizasCanceladas(PolizasCanceladasRequest request) {
		LOGGER.info("Entro GlobalRestClient#consultarPolizasCanceladas()");
		PolizasCanceladasResponse response = null;

		try {
			String url = getUrlConsultaPolizasCanceladas().concat("/").concat(request.getNumeroDocumento());
			response = obtenerGetPorObjeto(url, PolizasCanceladasResponse.class);
		} catch (SivSOAException e) {
			throw new SivSOAException(Utilitarios
					.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_GLOBAL_CONSULTAR_POLIZAS_CANCELADAS)
					.concat(Objects.toString(e.getMessage(), "")), e.getErrors());
		}

		LOGGER.info("Salio GlobalRestClient#consultarPolizasCanceladas()");
		return response;
	}

	// HCR - Registro de afiliación traspaso (antes SitcRepository.registrarAfiliacionTraspaso)
	public RegistrarAfiliacionTraspasoResponse registrarAfiliacionTraspaso(RegistrarAfiliacionTraspasoRequest request) {
		LOGGER.info("Entro GlobalRestClient#registrarAfiliacionTraspaso(request)");
		RegistrarAfiliacionTraspasoResponse response = null;
		String url = getUrlRegistrarAfiliacionTraspaso();
		LOGGER.info("GlobalRestClient#registrarAfiliacionTraspaso url: " + url);
		try {
			response = obtenerPostPorObjeto(url, request, RegistrarAfiliacionTraspasoResponse.class);
			LOGGER.info("GlobalRestClient#registrarAfiliacionTraspaso response: " + new Gson().toJson(response));
		} catch (SivSOAException e) {
			throw new SivSOAException(Utilitarios.obtenerMensaje(messageSource, "Ocurrió un error en HTTP POST: " + url)
					.concat(Objects.toString(e.getMessage(), "")), e.getErrors());
		}
		LOGGER.info("Salio GlobalRestClient#registrarAfiliacionTraspaso(request)");
		return response;
	}

	public UpdateTrazabilidadConsentimientoResponse saveTrazabilidadConsentimientoAceptado(UpdateTrazabilidadConsentimientoRequest request) {
		LOGGER.info("Entro GlobalRestClient#saveTrazabilidadConsentimientoAceptado(request)");
		UpdateTrazabilidadConsentimientoResponse response = new UpdateTrazabilidadConsentimientoResponse();
		LOGGER.info("GlobalRestClient#saveTrazabilidadConsentimientoAceptado url: " + urlActualizarTrazabilidadConsentimientoAceptado);
		try {
			response = obtenerPostPorObjeto(urlActualizarTrazabilidadConsentimientoAceptado, request, UpdateTrazabilidadConsentimientoResponse.class);
			LOGGER.info("GlobalRestClient#saveTrazabilidadConsentimientoAceptado response: " + new Gson().toJson(response));
		} catch (SivSOAException e) {
			String mensaje = Utilitarios.obtenerMensaje(messageSource, "Ocurrió un error en HTTP POST: " + urlActualizarTrazabilidadConsentimientoAceptado)
			.concat(Objects.toString(e.getMessage(), "")) ;
			LOGGER.info("Error GlobalRestClient#saveTrazabilidadConsentimientoAceptado(request): " + mensaje);
			response.setResult(false);
			response.setMessage(mensaje);
		}
		LOGGER.info("Salio GlobalRestClient#saveTrazabilidadConsentimientoAceptado(request)");
		return response;
	}

}
