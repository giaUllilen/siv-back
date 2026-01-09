package pe.interseguro.siv.admin.view.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.validation.Valid;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;

import pe.interseguro.siv.common.dto.request.IndenovaNotificacionRequestDTO;
import pe.interseguro.siv.common.dto.request.LogRequest;
import pe.interseguro.siv.common.dto.request.ObtenerPubSubSendRequestDTO;
import pe.interseguro.siv.common.dto.request.ObtenerRequestEvaluatorRequestDTO;
import pe.interseguro.siv.common.dto.request.PolicyIssuedRequestDTO;
import pe.interseguro.siv.common.dto.request.SolicitudFiltroRequestDTO;
import pe.interseguro.siv.common.dto.request.SolicitudFinalizarProcesoRequestDTO;
import pe.interseguro.siv.common.dto.request.SolicitudFinalizarProcesoResponseDTO;
import pe.interseguro.siv.common.dto.request.SolicitudGuardarRequestDTO;
import pe.interseguro.siv.common.dto.request.SolicitudRegistroPagoRequestDTO;
import pe.interseguro.siv.common.dto.request.SolicitudRequestDTO;
import pe.interseguro.siv.common.dto.request.SolicitudSMSRequestDTO;
import pe.interseguro.siv.common.dto.request.SolicitudValidarCodigoRequestDTO;
import pe.interseguro.siv.common.dto.request.TokenRequestDTO;
import pe.interseguro.siv.common.dto.response.BaseResponseDTO;
import pe.interseguro.siv.common.dto.response.CMRObtenerArchivoResponseDTO;
import pe.interseguro.siv.common.dto.response.CotizaDetalleResponseDTO;
import pe.interseguro.siv.common.dto.response.LinkPagoResponseDTO;
import pe.interseguro.siv.common.dto.response.ObtenerDocumentoResponseDTO;
import pe.interseguro.siv.common.dto.response.ObtenerPubSubSendResponseDTO;
import pe.interseguro.siv.common.dto.response.ObtenerRequestEvaluatorResponseDTO;
import pe.interseguro.siv.common.dto.response.PagoInicializacionResponseDTO;
import pe.interseguro.siv.common.dto.response.RecotizacionValidarResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudAcreditacionResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudCotizacionesTraspasoResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudDetalleResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudFormularioResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudGuardarResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudItemResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudReglamentoResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudResponseDTO;
import pe.interseguro.siv.common.dto.response.TokenLinkPagoResponseDTO;
import pe.interseguro.siv.common.dto.response.AcpByteArrayInput;
import pe.interseguro.siv.common.persistence.rest.niubiz.response.BaseResponse;
import pe.interseguro.siv.common.util.Constantes;
import pe.interseguro.siv.common.util.Utilitarios;

@RestController
@RequestMapping(value = { "/api/v1/solicitudes" })
public class SolicitudController extends BaseController {

	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseBody
	public SolicitudResponseDTO lista(@Valid @RequestBody SolicitudFiltroRequestDTO solicitudFiltroRequestDTO,
			BindingResult bindingResult) {
		LOGGER.info("Entro a SolicitudController#lista(solicitudFiltroRequestDTO)");

		getBindingResultError(bindingResult, "SolicitudResponseDTO");

		SolicitudResponseDTO response = serviceFactory.getSolicitudService().lista(solicitudFiltroRequestDTO);
		response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
		response.setMensajeRespuesta(
				Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_EXITO));
		LOGGER.info("Salio a SolicitudController#lista(solicitudFiltroRequestDTO)");
		return response;
	}

	@RequestMapping(value = "/editar", method = RequestMethod.POST)
	@ResponseBody
	public SolicitudItemResponseDTO lista(@Valid @RequestBody SolicitudRequestDTO solicitudRequestDTO,
			BindingResult bindingResult) {
		LOGGER.info("Entro a SolicitudController#lista(solicitudRequestDTO)");

		getBindingResultError(bindingResult, "SolicitudResponseDTO");

		SolicitudItemResponseDTO response = serviceFactory.getSolicitudService().editar(solicitudRequestDTO);
		LOGGER.info("Salio a SolicitudController#lista(solicitudRequestDTO)");
		return response;
	}

	@RequestMapping(value = "/generar-pdf/{id}", method = RequestMethod.GET)
	@ResponseBody
	public BaseResponseDTO generarPdf(@PathVariable(value = "id") Long id) throws IOException {
		LOGGER.info("Entro a SolicitudController#generarPdf(file)");

		BaseResponseDTO result = new BaseResponseDTO();
		String file = serviceFactory.getSolicitudService().generateTemp(id);

		LOGGER.info("Salio a SolicitudController#generarPdf(file)");
		result.setMensajeRespuesta(file);
		return result;
	}

	@RequestMapping(value = "/pdf/valida/{numeroCotizacion}", method = RequestMethod.GET)
	@ResponseBody
	public BaseResponseDTO existeArchivoSolicitud(@PathVariable(value = "numeroCotizacion") String numeroCotizacion) {
		LOGGER.info("Entro a SolicitudController#existeArchivoSolicitud(numeroCotizacion)");

		BaseResponseDTO response = new BaseResponseDTO();

		if (serviceFactory.getSolicitudService().existeArchivoSolicitud(numeroCotizacion)) {
			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
			response.setMensajeRespuesta(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_EXITO));
		} else {
			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_NO_EXISTE);
			response.setMensajeRespuesta(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_NO_EXISTE));
		}

		LOGGER.info("Salio a SolicitudController#existeArchivoSolicitud(numeroCotizacion)");
		return response;
	}

	@RequestMapping(value = "/pdf/{nroCotizacion}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> exportPDF(
			@PathVariable(value = "nroCotizacion") String numeroCotizacion) {

		ByteArrayInputStream bis = serviceFactory.getSolicitudService().printPDFSolicitud(numeroCotizacion);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=reporte.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bis));
	}

	@RequestMapping(value = "/pdf/{nroCotizacion}/{nroDocumento}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> exportPDF(@PathVariable(value = "nroCotizacion") String numeroCotizacion,
			@PathVariable(value = "nroDocumento") String numeroDocumento) {

		ByteArrayInputStream bis = serviceFactory.getSolicitudService().printPDFSolicitud2(numeroCotizacion,
				numeroDocumento);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=reporte.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bis));
	}

	@RequestMapping(value = "/pdf/crear/{nroCotizacion}/{tipoProducto}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> crearPDFSolicitud(
			@PathVariable(value = "nroCotizacion") String numeroCotizacion,
			@PathVariable(value = "tipoProducto") String tipoProducto) {
		String numeroDocumento = serviceFactory.getSolicitudPDFService().obtenerDocumentoSolicitud(numeroCotizacion)
				.getMensajeRespuesta();
		ByteArrayInputStream bis = serviceFactory.getSolicitudPDFService().crearPDFSolicitud(numeroCotizacion,
				tipoProducto);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition",
				"inline; filename=SolicitudDigital_" + numeroDocumento + "_" + numeroCotizacion + ".pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bis));
	}

	@RequestMapping(value = "/indenova/webhooks/confirmacionFirma", method = RequestMethod.POST)
	@ResponseBody
	public String indenovaWebHooksConfirmacionFirma(
			@RequestBody IndenovaNotificacionRequestDTO indenovaNotificacionRequestDTO) {
		LOGGER.info("Entro a SolicitudController#indenovaWebHooksConfirmacionFirma()");

		LOGGER.info("indenovaNotificacionRequestDTO.Gson=>" + gson.toJson(indenovaNotificacionRequestDTO));

		boolean resultado = serviceFactory.getSolicitudService().validarNotificacion(indenovaNotificacionRequestDTO);
		if (!resultado) {
			LOGGER.info("No se logro actualizar la notificación de Firma");
		}

		LOGGER.info("Salio a SolicitudController#indenovaWebHooksConfirmacionFirma()");
		return "OK";
	}

	@RequestMapping(value = "/enviar-correo/{id}/{tipoProducto}", method = RequestMethod.GET)
	@ResponseBody
	public BaseResponseDTO enviarCorreo(@PathVariable(value = "id") Long id,
			@PathVariable(value = "tipoProducto") String tipoProducto) {
		LOGGER.info("Entro a SolicitudController#enviarCorreo(file)");

		BaseResponseDTO response = new BaseResponseDTO();

		if (serviceFactory.getSolicitudService().enviarCorreo(id, tipoProducto)) {
			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
			response.setMensajeRespuesta(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_EXITO));
		}

		LOGGER.info("Salio a SolicitudController#enviarCorreo(file)");
		return response;
	}

	@RequestMapping(value = "/enviar-sms", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponseDTO enviarSMS(@RequestBody SolicitudSMSRequestDTO solicitudSMSRequestDTO) {
		LOGGER.info("Entro a SolicitudController#enviarSMS()");

		BaseResponseDTO response = new BaseResponseDTO();

		if (serviceFactory.getSolicitudService().enviarSMS(solicitudSMSRequestDTO)) {
			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
			response.setMensajeRespuesta(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_EXITO));
		}

		LOGGER.info("Salio a SolicitudController#enviarSMS()");
		return response;
	}

	@RequestMapping(value = "/validar-codigo", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponseDTO validarCodigo(
			@RequestBody SolicitudValidarCodigoRequestDTO solicitudValidarCodigoRequestDTO) {
		LOGGER.info("Entro a SolicitudController#validarCodigo()");

		BaseResponseDTO response = new BaseResponseDTO();

		response = serviceFactory.getSolicitudService().validarCodigo(solicitudValidarCodigoRequestDTO);
		LOGGER.info("Salio a SolicitudController#validarCodigo()");
		return response;
	}

	@RequestMapping(value = "/reglamentos/{tipoDocumento}/{numeroDocumento}/{numeroCotizacion}/{usuarioLogin}/{agenteNombres}/{agenteCorreo}/{agenteNumVendedor}/{agenteIdCRM}/{tipoProducto}", method = RequestMethod.GET)
	@ResponseBody
	public SolicitudReglamentoResponseDTO obtenerReglamento(@PathVariable(value = "tipoDocumento") String tipoDocumento,
			@PathVariable(value = "numeroDocumento") String numeroDocumento,
			@PathVariable(value = "numeroCotizacion") Long numeroCotizacion,
			@PathVariable(value = "usuarioLogin") String usuarioLogin,
			@PathVariable(value = "agenteNombres") String agenteNombres,
			@PathVariable(value = "agenteCorreo") String agenteCorreo,
			@PathVariable(value = "agenteNumVendedor") String agenteNumVendedor,
			@PathVariable(value = "agenteIdCRM") String agenteIdCRM,
			@PathVariable(value = "tipoProducto") String tipoProducto) {

		LOGGER.info(
				"Entro a SolicitudController#obtenerReglamento(tipoDocumento, numeroDocumento, numeroCotizacion, usuario)");

		CotizaDetalleResponseDTO cotizacionDetalle = null;
		if ("VidaFree".equals(tipoProducto)) {
			cotizacionDetalle = serviceFactory.getCotizaService().detalleCotizacionVidaFree(numeroCotizacion);
			LOGGER.info("Trama Cotizacion Detalle Vida Free ====>>>>>>>");
			LOGGER.info(gson.toJson(cotizacionDetalle));
		} else {
			cotizacionDetalle = serviceFactory.getCotizaService().detalle(numeroCotizacion);
		}

		SolicitudReglamentoResponseDTO response = serviceFactory.getSolicitudService().obtenerReglamento(tipoDocumento,
				numeroDocumento, numeroCotizacion, usuarioLogin, agenteNombres, agenteCorreo, agenteNumVendedor,
				agenteIdCRM, cotizacionDetalle, tipoProducto);

		LOGGER.info(
				"Salio a SolicitudController#obtenerReglamento(tipoDocumento, numeroDocumento, numeroCotizacion, usuario)");
		return response;
	}

	@RequestMapping(value = "/obtenerDatosFormulario/{idSolicitud}/{numeroCotizacion}/{tipoProducto}/{idUsuario}/{device}/{os}", method = RequestMethod.GET)
	@ResponseBody
	public SolicitudFormularioResponseDTO obtenerDatosFormulario(@PathVariable(value = "idSolicitud") Long idSolicitud,
			@PathVariable(value = "numeroCotizacion") Long numeroCotizacion,
			@PathVariable(value = "tipoProducto") String tipoProducto,
			@PathVariable(value = "idUsuario") String idUsuario, @PathVariable(value = "device") String device,
			@PathVariable(value = "os") String os) {
		LOGGER.info("Entro a SolicitudController#obtenerDatosFormulario(idSolicitud, numeroCotizacion, tipoProducto)");

		CotizaDetalleResponseDTO cotizacionDetalle = null;
		if (tipoProducto.equalsIgnoreCase("VidaFree")) {
			cotizacionDetalle = serviceFactory.getCotizaService().detalleCotizacionVidaFree(numeroCotizacion);
		} else {
			cotizacionDetalle = serviceFactory.getCotizaService().detalle(numeroCotizacion);
		}
		// CotizaDetalleResponseDTO cotizacionDetalle =
		// serviceFactory.getCotizaService().detalle(numeroCotizacion);

		SolicitudFormularioResponseDTO response = serviceFactory.getSolicitudService()
				.obtenerDatosFormulario(idSolicitud, numeroCotizacion, cotizacionDetalle, idUsuario, device, os);

		LOGGER.info("Salio a SolicitudController#obtenerDatosFormulario(idSolicitud, numeroCotizacion)");
		return response;
	}

	// este si guarda
	@RequestMapping(value = "/autoGuardado", method = RequestMethod.POST)
	@ResponseBody
	public SolicitudGuardarResponseDTO guardar(@RequestBody SolicitudGuardarRequestDTO solicitudGuardarRequestDTO) {
		LOGGER.info("Entro a SolicitudController#guardar()");

		LOGGER.info("SolicitudController#request()=>" + gson.toJson(solicitudGuardarRequestDTO));

		SolicitudGuardarResponseDTO response = serviceFactory.getSolicitudService().guardar(solicitudGuardarRequestDTO);

		LOGGER.info("SolicitudController#response()=>" + gson.toJson(response));

		LOGGER.info("Salio a SolicitudController#guardar()");
		return response;
	}

	@RequestMapping(value = "/guardarValidar", method = RequestMethod.POST)
	@ResponseBody
	public SolicitudGuardarResponseDTO guardarValidar(
		@Valid	@RequestBody SolicitudGuardarRequestDTO solicitudGuardarRequestDTO) {
		LOGGER.info("Entro a SolicitudController#guardarValidar()");

		LOGGER.info("SolicitudController#guardarValidar.request()=>" + gson.toJson(solicitudGuardarRequestDTO));

		SolicitudGuardarResponseDTO response = serviceFactory.getSolicitudService().guardar(solicitudGuardarRequestDTO);

		if (response.getCodigoRespuesta().equals(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO)) {

			if (response.getEstadoSolicitud().equals(Constantes.CODIGO_SOLICITUD_POR_TRANSMITIR)) {
				response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
				response.setMensajeRespuesta("Solicitud ya se encuentra finalizada.");
			} else {
				response = serviceFactory.getSolicitudService().completarSolicitudFirmar(
						solicitudGuardarRequestDTO.getIdSolicitud(), solicitudGuardarRequestDTO.getUsuarioLogin(),
						response);
			}

		}

		LOGGER.info("SolicitudController#guardarValidar.response()=>" + gson.toJson(response));

		LOGGER.info("Salio a SolicitudController#guardarValidar.guardarValidar()");
		return response;
	}

	@RequestMapping(value = "/finalizarProcesoSolicitud", method = RequestMethod.POST)
	@ResponseBody
	public SolicitudFinalizarProcesoResponseDTO finalizarProcesoSolicitud(
			@RequestBody SolicitudFinalizarProcesoRequestDTO solicitudFinalizarProcesoRequestDTO) {
		LOGGER.info("Entro a SolicitudController#finalizarProcesoSolicitud()");

		SolicitudFinalizarProcesoResponseDTO response = serviceFactory.getSolicitudService()
				.finalizarProcesoSolicitud(solicitudFinalizarProcesoRequestDTO);

		LOGGER.info("SolicitudController#finalizarProcesoSolicitud.response()=>" + response);

		LOGGER.info("Salio a SolicitudController#finalizarProcesoSolicitud()");
		return response;
	}

	@RequestMapping(value = "/obtenerURLVidaFree", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponseDTO obtenerURLVidaFree(
			@RequestBody TokenRequestDTO tokenRequestDTO) {
		LOGGER.info("Entro a SolicitudController#obtenerURLVidaFree()");

		BaseResponseDTO response = serviceFactory.getSolicitudService()
				.obtenerURLVidaFree(tokenRequestDTO);

		LOGGER.info("SolicitudController#obtenerURLVidaFree.response()=>" + response);

		LOGGER.info("Salio a SolicitudController#obtenerURLVidaFree()");
		return response;
	}

	@RequestMapping(value = "/finalizarProcesoSolicitudManual/{nroPropuesta}", method = RequestMethod.GET)
	@ResponseBody
	public BaseResponseDTO finalizarProcesoSolicitudManual(
			@PathVariable(value = "nroPropuesta") String numeroPropuesta) {
		LOGGER.info("Entro a SolicitudController#finalizarProcesoSolicitudManual()");

		BaseResponseDTO response = serviceFactory.getSolicitudService().enviarDatosSolicitudManualCRM(numeroPropuesta);

		LOGGER.info("SolicitudController#finalizarProcesoSolicitudManual.response()=>" + response);

		LOGGER.info("Salio a SolicitudController#finalizarProcesoSolicitudManual()");
		return response;
	}

	@RequestMapping(value = "/pdf/acp/crear/{nroPropuesta}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> crearAcpPDF(
			@PathVariable(value = "nroPropuesta") String numeroPropuesta) {

		AcpByteArrayInput response= serviceFactory.getAcpPDFService().crearPDFV2(numeroPropuesta);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition",
				"inline; filename=AcpDigital_" +response.getNumeroDocumento() + "_" + numeroPropuesta + ".pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(response.getAcpFile()));
	}

	@RequestMapping(value = "/registro-pago", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponseDTO registrarPago(@RequestBody SolicitudRegistroPagoRequestDTO solicitudRegistroPagoRequestDTO) {
		LOGGER.info("Entro a SolicitudController#registrarPago()");

		BaseResponseDTO response = new BaseResponseDTO();

		response = serviceFactory.getSolicitudService().registrarAfiliacionPropuesta(solicitudRegistroPagoRequestDTO);

		/*
		 * AcpFormularioTarjetaResponse tarjeta = null; AcpFormularioCuentaResponse
		 * cuenta = null; if (2 == solicitudRegistroPagoRequestDTO.getTipoViaCobro()) {
		 * tarjeta = new AcpFormularioTarjetaResponse(); tarjeta.setCcv("***");
		 * tarjeta.setNumeroTarjeta(solicitudRegistroPagoRequestDTO.
		 * getTarjetaCobroRecurrente());
		 * tarjeta.setFechaVencimiento(solicitudRegistroPagoRequestDTO.
		 * getFechaVencimiento());
		 * tarjeta.setEntidadFinanciera(solicitudRegistroPagoRequestDTO.getViaCobro());
		 * } else { cuenta = new AcpFormularioCuentaResponse();
		 * cuenta.setEntidadBancaria(String.valueOf(solicitudRegistroPagoRequestDTO.
		 * getViaCobro()));
		 * cuenta.setNumeroCuenta(solicitudRegistroPagoRequestDTO.getNumeroTarjeta());
		 * cuenta.setTipoCuenta(String.valueOf(solicitudRegistroPagoRequestDTO.
		 * getTipoCuenta()));
		 * cuenta.setMoneda(solicitudRegistroPagoRequestDTO.getMoneda()); }
		 */

		if (Constantes.CODIGO_RESPUESTA_GENERAL_EXITO.equals(response.getCodigoRespuesta())) {
			response = serviceFactory.getAcpPDFService()
					.generarPDF(String.valueOf(solicitudRegistroPagoRequestDTO.getNumeroPropuesta()), 1);
			response.setCodigoRespuesta("01");
		}
		LOGGER.info("Salio a SolicitudController#registrarPago()");
		return response;
	}

	@RequestMapping(value = "/prueba-asl/generarPDF", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponseDTO registrarPagoPrueba(
			@RequestBody SolicitudRegistroPagoRequestDTO solicitudRegistroPagoRequestDTO) {
		LOGGER.info("Entro a SolicitudController#registrarPago()");

		BaseResponseDTO response = new BaseResponseDTO();

		response = serviceFactory.getAcpPDFService()
				.generarPDFPrueba(String.valueOf(solicitudRegistroPagoRequestDTO.getNumeroPropuesta()), 2);
		response.setCodigoRespuesta("01");

		LOGGER.info("Salio a SolicitudController#registrarPago()");
		return response;
	}

	@RequestMapping(value = "/pdf/acp/{nroCotizacion}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> exportAcpPDF(
			@PathVariable(value = "nroCotizacion") String numeroCotizacion) {

		ByteArrayInputStream bis = serviceFactory.getSolicitudService().printPDFAcp(numeroCotizacion);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=reporte_acp.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bis));
	}

//	@RequestMapping(value = "/link-pago/{numeroPropuesta}/{idUsuario}/{device}/{os}", method = RequestMethod.GET)
//	@ResponseBody
//	public BaseResponseDTO enviarLinkPago(@PathVariable(value = "numeroPropuesta") String numeroPropuesta,
//			@PathVariable(value = "idUsuario") String idUsuario, @PathVariable(value = "device") String device,
//			@PathVariable(value = "os") String os) {
//		LOGGER.info("LINK-PAGO ==> Entro a SolicitudController#enviarLinkPago("+numeroPropuesta+")");
//
//		BaseResponseDTO response = new BaseResponseDTO();
//
//		response = serviceFactory.getSolicitudService().enviarLinkPago(numeroPropuesta, idUsuario, device, os);
//
//		LOGGER.info("LINK-PAGO ==> Salio a SolicitudController#enviarLinkPago("+numeroPropuesta+")");
//		return response;
//	}

	@RequestMapping(value = "/link-pago/{numeroPropuesta}/{idUsuario}/{device}/{os}", method = RequestMethod.GET)
	@ResponseBody
	public LinkPagoResponseDTO enviarLinkPago(@PathVariable(value = "numeroPropuesta") String numeroPropuesta,
			@PathVariable(value = "idUsuario") String idUsuario, @PathVariable(value = "device") String device,
			@PathVariable(value = "os") String os) {
		LOGGER.info("LINK-PAGO ==> Entro a SolicitudController#enviarLinkPago(" + numeroPropuesta + ")");
		Long timeStart = System.nanoTime();
		LinkPagoResponseDTO response = serviceFactory.getSolicitudService().enviarLinkPago(numeroPropuesta, idUsuario,
				device, os);
		Long timeEnd = System.nanoTime();
		LOGGER.info("Diference enviarLinkPago: {}", (timeEnd - timeStart));

		LOGGER.info("LINK-PAGO ==> Salio a SolicitudController#enviarLinkPago(" + numeroPropuesta + ")");
		return response;
	}

	@RequestMapping(value = "/link-pago/help/{numeroPropuesta}/{idUsuario}/{device}/{os}", method = RequestMethod.GET)
	@ResponseBody
	public LinkPagoResponseDTO enviarLinkPagoHelp(@PathVariable(value = "numeroPropuesta") String numeroPropuesta,
			@PathVariable(value = "idUsuario") String idUsuario, @PathVariable(value = "device") String device,
			@PathVariable(value = "os") String os) {
		LOGGER.info("Entro a SolicitudController#enviarLinkPagoHelp(numeroPropuesta)");

		LinkPagoResponseDTO response = serviceFactory.getSolicitudService().enviarLinkPagoHelp(numeroPropuesta, idUsuario,
				device, os);

		LOGGER.info("Salio a SolicitudController#enviarLinkPagoHelp(numeroPropuesta)");
		return response;
	}

	@RequestMapping(value = "/link-pago/operaciones/decrypt", method = RequestMethod.POST)
	@ResponseBody
	public TokenLinkPagoResponseDTO decryptTokenVidaFree(@RequestBody TokenRequestDTO tokenRequestDTO) {
		LOGGER.info("Entro a SolicitudController#decryptTokenVidaFree(token)");

		TokenLinkPagoResponseDTO respuesta = serviceFactory.getSolicitudService()
				.decryptLinkToken(tokenRequestDTO.getToken());

		LOGGER.info("Salio a SolicitudController#decryptTokenVidaFree(token)");
		return respuesta;
	}

	@RequestMapping(value = "/link-pago/operaciones/inicializaciones", method = RequestMethod.GET)
	@ResponseBody
	public PagoInicializacionResponseDTO obtenerInicializacion() {
		LOGGER.info("Entro a SolicitudController#obtenerInicializacion()");

		PagoInicializacionResponseDTO response = serviceFactory.getSolicitudService().obtenerInicializacionPago();

		LOGGER.info("Salio a SolicitudController#obtenerInicializacion()");
		return response;
	}

	@RequestMapping(value = "/detalle/{numeroCotizacion}/{idUsuario}/{device}/{os}", method = RequestMethod.GET)
	@ResponseBody
	public SolicitudDetalleResponseDTO obtenerDetalleSolicitud(
			@PathVariable(value = "numeroCotizacion") String numeroCotizacion,
			@PathVariable(value = "idUsuario") String idUsuario, @PathVariable(value = "device") String device,
			@PathVariable(value = "os") String os) {
		LOGGER.info("Entro a SolicitudController#obtenerDetalleSolicitud()");
		SolicitudDetalleResponseDTO response = serviceFactory.getSolicitudService()
				.obtenerDetalleSolicitud(numeroCotizacion, idUsuario, device, os);

		LOGGER.info("Salio a SolicitudController#obtenerDetalleSolicitud()");
		return response;
	}

	@RequestMapping(value = "/link-pago/operaciones/niubiz/token", method = RequestMethod.POST)
	@ResponseBody
	public String activarTokenNiubiz() {
		LOGGER.info("Entro a SolicitudController#activarTokenNiubiz()");

		// NiubizSessionResponseDTO response =
		// serviceFactory.getSolicitudService().activarSesionNiubiz(request);

		LOGGER.info("Salio a SolicitudController#activarTokenNiubiz()");
		return "";
	}

	// Listar Documentar
	@RequestMapping(value = "/documentos/{numeroCotizacion}", method = RequestMethod.GET)
	@ResponseBody
	public ObtenerDocumentoResponseDTO obtenerDocumento(
			@PathVariable(value = "numeroCotizacion") String numeroCotizacion) {
		LOGGER.info("Entro a SolicitudController#obtenerDocumento()");
		ObtenerDocumentoResponseDTO response = serviceFactory.getSolicitudService().obtenerDocumento(numeroCotizacion);

		LOGGER.info("Salio a SolicitudController#obtenerDocumento()");
		return response;
	}

	// Abrir archivo
	@RequestMapping(value = "/documentos/{numeroCotizacion}/{nombreArchivo}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> exportPDFVidaFree(
			@PathVariable(value = "numeroCotizacion") String numeroCotizacion,
			@PathVariable(value = "nombreArchivo") String nombreArchivo) {

		ByteArrayInputStream bis = serviceFactory.getSolicitudService().leerArchivo(numeroCotizacion, nombreArchivo);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=reporte.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bis));
	}

	@RequestMapping(value = "/{numeroCotizacion}/subirDocumento", headers = {
			"content-type=multipart/form-data" }, method = RequestMethod.POST, consumes = {
					MediaType.MULTIPART_FORM_DATA_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public BaseResponseDTO subirArchivo(MultipartFile file,
			@PathVariable(value = "numeroCotizacion") String numeroCotizacion) {
		LOGGER.info("Entro a SolicitudController#subirArchivo()");

		BaseResponseDTO response = serviceFactory.getSolicitudService().subirArchivo(file, numeroCotizacion);

		LOGGER.info("Salio a SolicitudController#subirArchivo()");
		return response;
	}

	@RequestMapping(value = "/obtenerArchivo/{files}/{nroCot}", method = RequestMethod.GET)
	@ResponseBody
	public CMRObtenerArchivoResponseDTO obtenerArchivo(@PathVariable(value = "files") String files,
			@PathVariable(value = "nroCot") String nroCot) {
		LOGGER.info("Entro a SolicitudController#obtenerArchivo(archivo)");

		CMRObtenerArchivoResponseDTO response = serviceFactory.getSolicitudService().obtenerArchivo(files, nroCot);

		LOGGER.info("Salio a SolicitudController#obtenerArchivo(archivo)");
		return response;
	}

	@RequestMapping(value = "/link-pago/recargo/{numeroPropuesta}/{idUsuario}/{device}/{os}", method = RequestMethod.GET)
	@ResponseBody
	public LinkPagoResponseDTO enviarLinkPagoRecargo(@PathVariable(value = "numeroPropuesta") String numeroPropuesta,
			@PathVariable(value = "idUsuario") String idUsuario, @PathVariable(value = "device") String device,
			@PathVariable(value = "os") String os) {
		LOGGER.info("Entro a SolicitudController#enviarLinkPagoHelp(numeroPropuesta)");

		LinkPagoResponseDTO response = serviceFactory.getSolicitudService().enviarLinkPagoRecargo(numeroPropuesta,
				idUsuario, device, os);

		LOGGER.info("Salio a SolicitudController#enviarLinkPagoHelp(numeroPropuesta)");
		return response;
	}

	@RequestMapping(value = "/afiliacionTraspaso", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponseDTO registrarAfiliacionTraspaso(
			@RequestBody SolicitudRegistroPagoRequestDTO solicitudRegistroPagoRequestDTO) {
		LOGGER.info("Entro a SolicitudController#registrarAfiliacionTraspaso()");

		BaseResponseDTO response = new BaseResponseDTO();
		response = serviceFactory.getSolicitudService().registrarAfiliacionTraspaso(solicitudRegistroPagoRequestDTO);

		LOGGER.info("Salio a SolicitudController#registrarAfiliacionTraspaso()");
		return response;
	}

	@RequestMapping(value = "/obtenerCotizacionesTraspaso/{numDocCliente}", method = RequestMethod.GET)
	@ResponseBody
	public SolicitudCotizacionesTraspasoResponseDTO obtenerCotizacionesTraspaso(
			@PathVariable(value = "numDocCliente") String numDocCliente) {
		LOGGER.info("Entro a SolicitudController#obtenerCotizacionesTraspaso()");

		SolicitudCotizacionesTraspasoResponseDTO response = new SolicitudCotizacionesTraspasoResponseDTO();
		response = serviceFactory.getSolicitudService().obtenerCotizacionesTraspaso(numDocCliente);

		LOGGER.info("Salio a SolicitudController#obtenerCotizacionesTraspaso()");
		return response;
	}

	@RequestMapping(value = "/consultarAcreditacion/{numCotizacion}", method = RequestMethod.GET)
	@ResponseBody
	public SolicitudAcreditacionResponseDTO consultarAcreditacion(
			@PathVariable(value = "numCotizacion") String numCotizacion) {
		LOGGER.info("Entro a SolicitudController#obtenerCotizacionesTraspaso()");

		SolicitudAcreditacionResponseDTO response = new SolicitudAcreditacionResponseDTO();
		response = serviceFactory.getSolicitudService().consultarAcreditacion(numCotizacion);

		LOGGER.info("Salio a SolicitudController#obtenerCotizacionesTraspaso()");
		return response;
	}

	@RequestMapping(value = "/printLog", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponse printLog(@RequestBody LogRequest request) {
		LOGGER.info("Entro a SolicitudController#printLog()");

		BaseResponse response = new BaseResponse();
		response.setMessage(request.getMensaje());
		response.setCode("01");
		LOGGER.info("Response " + new Gson().toJson(response));
		LOGGER.info("Salio a SolicitudController#printLog()");
		return response;
	}

	@RequestMapping(value = "/pubSubSend", method = RequestMethod.POST)
	@ResponseBody
	public ObtenerPubSubSendResponseDTO pubSubSend(@RequestBody ObtenerPubSubSendRequestDTO request) {
		LOGGER.info("LINK-PAGO ==> Entro a SolicitudController#pubSubSend()");

		LOGGER.info("LINK-PAGO ==> Request " + new Gson().toJson(request));
		ObtenerPubSubSendResponseDTO response = new ObtenerPubSubSendResponseDTO();
		response = serviceFactory.getSolicitudService().obtenerPubSubSend(request);
		LOGGER.info("LINK-PAGO ==> Response " + new Gson().toJson(response));
		LOGGER.info("LINK-PAGO ==> Salio a SolicitudController#pubSubSend()");
		return response;
	}

	@RequestMapping(value = "/recotizacion/validar/{numCotizacion}", method = RequestMethod.GET)
	@ResponseBody
	public RecotizacionValidarResponseDTO validarRecotizacion(
			@PathVariable(value = "numCotizacion") String cotizacion) {
		LOGGER.info("Entro a CotizaController#validarRecotizacion(cotizacion)");
		RecotizacionValidarResponseDTO respuesta = serviceFactory.getSolicitudService().validarRecotizacion(cotizacion);

		LOGGER.info("Salio a CotizaController#validarRecotizacion(cotizacion)");
		return respuesta;
	}

	@RequestMapping(value = "/requestEvaluator", method = RequestMethod.POST)
	@ResponseBody
	public ObtenerRequestEvaluatorResponseDTO requestEvaluator(@RequestBody ObtenerRequestEvaluatorRequestDTO request) {
		LOGGER.info("EVALUAR-SOLICITUD ==> Entro a SolicitudController#requestEvaluator()");

		LOGGER.info("EVALUAR-SOLICITUD ==> Request " + new Gson().toJson(request));
		ObtenerRequestEvaluatorResponseDTO response = new ObtenerRequestEvaluatorResponseDTO();
		response = serviceFactory.getSolicitudService().obtenerRequestEvaluator(request);
		LOGGER.info("EVALUAR-SOLICITUD ==> Response " + new Gson().toJson(response));
		LOGGER.info("EVALUAR-SOLICITUD ==> Salio a SolicitudController#requestEvaluator()");
		return response;
	}

	@RequestMapping(value = "/policyIssued", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponseDTO policyIssued(@RequestBody PolicyIssuedRequestDTO policyIssuedRequestDTO) {
		LOGGER.info("EMISION-SOLICITUD ==> Entro a SolicitudController#policyIssued()");

		BaseResponseDTO response = serviceFactory.getSolicitudService().policyIssued(policyIssuedRequestDTO);

		LOGGER.info("EMISION-SOLICITUD ==> Salio a SolicitudController#policyIssued()");
		return response;
	}

	@RequestMapping(value = "/reprocesoEmisionAutomatica", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponseDTO reprocesoEmisionAutomatica(@RequestBody PolicyIssuedRequestDTO policyIssuedRequestDTO) {
		LOGGER.info("Entro a SolicitudController#reprocesarEmisionAutomatica()");

		BaseResponseDTO response = new BaseResponseDTO();

		response = serviceFactory.getSolicitudService().reprocesarEmisionAutomaticaDocumentos(policyIssuedRequestDTO);

		LOGGER.info("Salio a SolicitudController#reprocesarEmisionAutomatica()");
		return response;
	}

	@RequestMapping(value = "/validadorDocumentos", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponseDTO validadorDocumentos(@RequestBody PolicyIssuedRequestDTO policyIssuedRequestDTO) {
		LOGGER.info("Entro a SolicitudController#validadorDocumentos()");

		BaseResponseDTO response = new BaseResponseDTO();

		response = serviceFactory.getSolicitudService().validadorDocumentos(policyIssuedRequestDTO);

		LOGGER.info("Salio a SolicitudController#validadorDocumentos()");
		return response;
	}

	@RequestMapping(value = "/reprocesoDocumentos", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponseDTO reprocesoDocumentos(@RequestBody PolicyIssuedRequestDTO policyIssuedRequestDTO) {
		LOGGER.info("Entro a SolicitudController#reprocesoDocumentos()");

		BaseResponseDTO response = new BaseResponseDTO();

		response = serviceFactory.getSolicitudService().reprocesarDocumentos(policyIssuedRequestDTO);

		LOGGER.info("Salio a SolicitudController#reprocesoDocumentos()");
		return response;
	}
	@RequestMapping(value = "/acp-pdf/{nroPropuesta}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<BaseResponseDTO> acpPdf(
			@PathVariable(value = "nroPropuesta") String numeroPropuesta) {
		return ResponseEntity.ok(serviceFactory.getAcpPDFService()
				.generarPDF(String.valueOf(numeroPropuesta), 1)) ;
	}

	@RequestMapping(value = "/acp-pdf/{nroPropuesta}/{tag}", method = RequestMethod.GET)
	public ResponseEntity<BaseResponseDTO> acpPdf2(
			@PathVariable(value = "nroPropuesta") String numeroPropuesta,@PathVariable(value = "tag") int tag) {
		return ResponseEntity.ok(serviceFactory.getAcpPDFService()
				.generarPDF(String.valueOf(numeroPropuesta), tag)) ;
	}

}
