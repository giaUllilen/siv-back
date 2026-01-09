package pe.interseguro.siv.admin.view.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

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

import pe.interseguro.siv.common.dto.request.CotizacionCrmRequestDTO;
import pe.interseguro.siv.common.dto.request.CotizadorCorreoRequestDTO;
import pe.interseguro.siv.common.dto.request.ReprocesoRequestDTO;
import pe.interseguro.siv.common.dto.request.TokenRequestDTO;
import pe.interseguro.siv.common.dto.response.*;
import pe.interseguro.siv.common.dto.response.TipoCambioResponseDTO;
import pe.interseguro.siv.common.dto.response.TokenResponseDTO;
import pe.interseguro.siv.common.persistence.rest.estudionecesidad.response.GenerarPdfResponse;
import pe.interseguro.siv.common.util.Constantes;
import pe.interseguro.siv.common.util.Utilitarios;
/**
 * RestFull de Cotizaciones
 * 
 * @author ti-is
 */
@RestController
@RequestMapping(value = {"/api/v1/cotizaciones"})
public class CotizaController extends BaseController{
	
	/**
	 * Ejemplo mongoDB con multitabla 
	 * 
	 * @param codigoTabla
	 * @return
	 */
	@RequestMapping(value = "/tablas/{codigoTabla}", method = RequestMethod.GET)
	@ResponseBody
	public CotizaTablaResponseDTO obtenerTabla(@PathVariable(value="codigoTabla") String codigoTabla) {
		LOGGER.info("Entro a CotizaController#obtenerTabla(codigoTabla)");
		
		CotizaTablaResponseDTO respuesta = serviceFactory.getCotizaService().obtenerTabla(codigoTabla);
		
		LOGGER.info("Salio a CotizaController#obtenerTabla(codigoTabla)");
		return respuesta;
	}

	@RequestMapping(value = "/get-link-pago/{cotizacion}", method = RequestMethod.GET)
	@ResponseBody
	public LinkPagoCotResponseDTO getLinkPago(@PathVariable(value="cotizacion") String cotizacion) {
		LOGGER.info("Entro a CotizaController#getLinkPago(cotizacion)");

		LinkPagoCotResponseDTO respuesta = serviceFactory.getCotizaService().getLinkPago(cotizacion);

		LOGGER.info("Salio a CotizaController#getLinkPago(cotizacion)");
		return respuesta;
	}
	
	/**
	 * Lista de cotizacion 
	 * 
	 * @param documento
	 * @return
	 */
	@RequestMapping(value = "/lista/{documento}", method = RequestMethod.GET)
	@ResponseBody
	public CotizaListaResponseDTO getlistaDocumentoProducto(@PathVariable(value="documento") String documento) {
		LOGGER.info("Entro a CotizaController#getlistaDocumentoProducto(documento)");
		
		CotizaListaResponseDTO respuesta = serviceFactory.getCotizaService().listaDocumentoProducto(documento, Constantes.COTIZADOR_VIDA_PRODUCTO_PLAN_GARANTIZADO);
		
		LOGGER.info("Salio a CotizaController#getlistaDocumentoProducto(documento)");
		return respuesta;
	}
	
	/**
	 * Detalle de cotizacion 
	 * 
	 * @param documento
	 * @return
	 */
	@RequestMapping(value = "/detalle/{nroCotizacion}", method = RequestMethod.GET)
	@ResponseBody
	public CotizaDetalleResponseDTO getDetalle(@PathVariable(value="nroCotizacion") Long nroCotizacion) {
		LOGGER.info("Entro a CotizaController#getDetalle(nroCotizacion)");
		
		CotizaDetalleResponseDTO respuesta = serviceFactory.getCotizaService().detalle(nroCotizacion);
		
		LOGGER.info("Salio a CotizaController#getDetalle(nroCotizacion)");
		return respuesta;
	}

	/**
	 * 
	 * @param documento
	 * @return
	 */
	@RequestMapping(value = "/intermedio/{documento}/{idUsuario}/{device}/{os}", method = RequestMethod.GET)
	@ResponseBody
	public CotizaListaResponseDTO getlistaCotizacionesVida(@PathVariable(value="documento") String documento,
			@PathVariable(value="idUsuario") String idUsuario, @PathVariable(value="device") String device, 
			@PathVariable(value="os") String os) {
		LOGGER.info("Entro a CotizaController#getlistaCotizacionesVida(documento)");
		
		CotizaListaResponseDTO respuesta = serviceFactory.getCotizaService().listaCotizacionesVida(documento, idUsuario, device, os);
		
		LOGGER.info("Salio a CotizaController#getlistaCotizacionesVida(documento)");
		return respuesta;
	}
	
	@RequestMapping(value = "/intermedio/nuevo/{numDocumento}/{idUsuario}/{device}/{os}", method = RequestMethod.GET)
	@ResponseBody
	public CotizaUrlResponse getURLCotizadorVidaNuevo(@PathVariable(value="numDocumento") String numDocumento, 
			@PathVariable(value="idUsuario") String idUsuario, @PathVariable(value="device") String device,
			@PathVariable(value="os") String os) {
		LOGGER.info("Entro a CotizaController#getURLCotizadorVida(idOportunidad)");
		
		CotizaUrlResponse respuesta = serviceFactory.getCotizaService().ObtenerURLCotizadorVida(numDocumento, idUsuario, device, os); 
		
		LOGGER.info("Salio a CotizaController#getURLCotizadorVida(idOportunidad)");
		return respuesta;
	}
	
	@RequestMapping(value = "/correlativo/{tipoDocumento}/{numeroDocumento}", method = RequestMethod.GET)
	@ResponseBody
	public CotizadorCorrelativoResponseDTO getCorrelativoCotizador(@PathVariable(value="tipoDocumento") String tipoDocumento,
			@PathVariable(value="numeroDocumento") String numeroDocumento) {
		LOGGER.info("Entro a CotizaController#getCorrelativoCotizador(tipoDocumento,numeroDocumento)");
		
		CotizadorCorrelativoResponseDTO respuesta = serviceFactory.getCotizaService().generarCorrelativo(tipoDocumento, numeroDocumento); 
		
		LOGGER.info("Salio a CotizaController#getCorrelativoCotizador(tipoDocumento,numeroDocumento)");
		return respuesta;
	}
	
	@RequestMapping(value = "/cumulo/{tipoDocumento}/{numeroDocumento}", method = RequestMethod.GET)
	@ResponseBody
	public CotizadorCumuloResponseDTO getCumulo(@PathVariable(value="tipoDocumento") String tipoDocumento,
			@PathVariable(value="numeroDocumento") String numeroDocumento) {
		LOGGER.info("Entro a CotizaController#getCumulo(tipoDocumento,numeroDocumento)");
		
		CotizadorCumuloResponseDTO respuesta = serviceFactory.getCotizaService().obtenerCumulo(tipoDocumento, numeroDocumento); 
		
		LOGGER.info("Salio a CotizaController#getCumulo(tipoDocumento,numeroDocumento)");
		return respuesta;
	}
	
	@RequestMapping(value = "/recotiza/{nroDocumento}/{nroCotizacion}/{idUsuario}", method = RequestMethod.GET)
	@ResponseBody
	public CotizaUrlResponse getURLRecotizarVida(@PathVariable(value="nroDocumento") String nroDocumento,
			@PathVariable(value="nroCotizacion") String nroCotizacion, @PathVariable(value="idUsuario") String idUsuario) {
		LOGGER.info("Entro a CotizaController#getURLRecotizarVida(idCotizacion)");
		
		CotizaUrlResponse respuesta = serviceFactory.getCotizaService().ObtenerURLRecotizarVida(nroDocumento,nroCotizacion,idUsuario); 
		
		LOGGER.info("Salio a CotizaController#getURLCotizadorVida(idCotizacion)");
		return respuesta;
	}
	
	@RequestMapping(value = "/intermedio/correo", method = RequestMethod.POST)
	@ResponseBody	
	public BaseResponseDTO enviarCorreo(
	 	@Valid @RequestBody CotizadorCorreoRequestDTO cotizadorCorreoRequestDTO,
	 	BindingResult bindingResult
	) {
		LOGGER.info("Entro a CotizaController#enviarCorreo(cotizadorCorreoRequestDTO)");
			
		getBindingResultError(bindingResult, "CotizadorCorreoRequestDTO");
		
		BaseResponseDTO response = serviceFactory.getCotizaService().enviarCorreo(
				cotizadorCorreoRequestDTO.getDestinatario() 
				, cotizadorCorreoRequestDTO.getAdjuntos()
				, cotizadorCorreoRequestDTO.getAsegurado()
				, cotizadorCorreoRequestDTO.getAgenteNombre()
				, cotizadorCorreoRequestDTO.getAgenteCorreo());
		response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
		response.setMensajeRespuesta(
			Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_EXITO)
		);
		LOGGER.info("Salio a CotizaController#enviarCorreo(cotizadorCorreoRequestDTO)");
		return response;
	}
	
	@RequestMapping(value = "/intermedio/vidafree", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponseDTO obtenerURLVidaFree(@RequestBody TokenRequestDTO tokenRequestDTO) {
		LOGGER.info("Entro a CotizaController#obtenerURLVidaFree(token)");
		
		BaseResponseDTO respuesta = serviceFactory.getCotizaService().obtenerURLVidaFree(tokenRequestDTO); 
		
		LOGGER.info("Salio a CotizaController#obtenerURLVidaFree(token)");
		return respuesta;
	}
	
	@RequestMapping(value = "/intermedio/tipoCambio", method = RequestMethod.GET)
	@ResponseBody
	public ConversionResponseDTO getTipoCambio() {
		LOGGER.info("Entro a CotizaController#getTipoCambio()");
		
		ConversionResponseDTO respuesta = serviceFactory.getCotizaService().obtenerTipoCambio(); 
		
		LOGGER.info("Salio a CotizaController#getTipoCambio()");
		return respuesta;
	}
	
	@RequestMapping(value = "/decrypt", method = RequestMethod.POST)
	@ResponseBody
	public TokenResponseDTO decryptTokenVidaFree(@RequestBody TokenRequestDTO tokenRequestDTO) {
		LOGGER.info("Entro a CotizaController#decryptTokenVidaFree(token)");
		
		TokenResponseDTO respuesta = serviceFactory.getCotizaService().decryptToken(tokenRequestDTO.getToken()); 
		
		LOGGER.info("Salio a CotizaController#decryptTokenVidaFree(token)");
		return respuesta;
	}

	@RequestMapping(value = "/validateToken", method = RequestMethod.POST)
	@ResponseBody
	public TokenResponseDTO validateToken(@RequestBody TokenRequestDTO tokenRequestDTO) {
		LOGGER.info("Entro a CotizaController#validateToken(token)");
		TokenResponseDTO respuesta = serviceFactory.getCotizaService().validateToken(tokenRequestDTO.getToken());
		LOGGER.info("Salio a CotizaController#validateToken(token)");
		return respuesta;
	}
	
	@RequestMapping(value = "/crm/cotiza", method = RequestMethod.POST)
	@ResponseBody
	public CotizacionCrmResponseDTO guardarCotizacionCrm(@RequestBody CotizacionCrmRequestDTO cotizacionCrmRequestDTO) {
		LOGGER.info("Entro a CotizaController#guardarCotizacionCrm(cotizacionCrmRequestDTO)");
		CotizacionCrmResponseDTO respuesta = serviceFactory.getCotizaService().guardaCotizacionAdn(cotizacionCrmRequestDTO);
		LOGGER.info("Salio a CotizaController#guardarCotizacionCrm(cotizacionCrmRequestDTO)");
		return respuesta;
	}
	
	/*@RequestMapping(value = "/intermedio/finalizar", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponseDTO finalizarCotizacionCrm(@RequestBody CotizacionCrmOportunidadRequestDTO cotizacionCrmRequestDTO) {
		LOGGER.info("Entro a CotizaController#finalizarCotizacionCrm(cotizacionCrmRequestDTO)");
		
		BaseResponseDTO respuesta = serviceFactory.getCotizaService().finalizarCotizacion(cotizacionCrmRequestDTO); 
		
		LOGGER.info("Salio a CotizaController#finalizarCotizacionCrm(cotizacionCrmRequestDTO)");
		return respuesta;
	}*/
	
	@RequestMapping(value = "/pdf/{nroCotizacion}/{agente}/{asegurado}", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> exportCotizacionPDF(@PathVariable(value="nroCotizacion") String numeroCotizacion,
    		@PathVariable(value="agente") String agente, @PathVariable(value="asegurado") String asegurado) {

        ByteArrayOutputStream out = serviceFactory.getCotizaService().generarPDFCotizacionVida(numeroCotizacion, agente, asegurado);
		ByteArrayInputStream bis = new ByteArrayInputStream(out.toByteArray());


		HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=cotizacion_" + numeroCotizacion + ".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

	@RequestMapping(value = "/regulariza-exmedicas/{nroCotizacion}", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_PDF_VALUE)
	public String gaaa(@PathVariable(value="nroCotizacion") String numeroCotizacion) {

		serviceFactory.getCotizaService().saveCumuloExigenciasMedicas(numeroCotizacion);

		return "ok";
	}
	
	@RequestMapping(value = "/vidafree/pdf/{nroCotizacion}", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> exportPDFVidaFree(@PathVariable(value="nroCotizacion") String numeroCotizacion){
		ByteArrayOutputStream out = serviceFactory.getCotizaService().generarPDFCotizacionVidaFree(numeroCotizacion);
        ByteArrayInputStream bis = new ByteArrayInputStream(out.toByteArray());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=reporte.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

	@RequestMapping(value = "/pdf/edn/{nroCotizacion}", method = RequestMethod.GET)
	public GenerarPdfResponse exportPDFEDN(@PathVariable(value="nroCotizacion") String numeroCotizacion){
		GenerarPdfResponse out = serviceFactory.getCotizaService().generarPDFEdn(numeroCotizacion);
		return out;
	}
	
	@RequestMapping(value = "/vidafree/clonar/{nroCotizacion}", method = RequestMethod.GET)
	@ResponseBody
    public ClonarCVFResponseDTO clonarVidaFree(@PathVariable(value="nroCotizacion") String numeroCotizacion) {
		LOGGER.info("Entro a CotizaController#clonarVidaFree(numeroCotizacion)");
		
		ClonarCVFResponseDTO response = serviceFactory.getCotizaService().clonarCotizacionVidaFree(numeroCotizacion);

		LOGGER.info("Salio a CotizaController#clonarVidaFree(numeroCotizacion)");
        return response;
    }
	
	@RequestMapping(value = "/vidafree/reprocesar", method = RequestMethod.POST)
	@ResponseBody
    public BaseResponseDTO forzarVidaFree(@RequestBody ReprocesoRequestDTO cotizaciones) {
		LOGGER.info("Entro a CotizaController#forzarVidaFree(cotizaciones)");
		
		BaseResponseDTO response = serviceFactory.getCotizaService().forzarRecotizacion(cotizaciones.getCotizaciones());

		LOGGER.info("Salio a CotizaController#forzarVidaFree(cotizaciones)");
        return response;
    }
	
	@RequestMapping(value = "/vidafree/validar/{nroCotizacion}", method = RequestMethod.GET)
	@ResponseBody
    public BaseResponseDTO validarVidaFree(@PathVariable(value="nroCotizacion") String numeroCotizacion) {
		LOGGER.info("Entro a CotizaController#validarVidaFree(numeroCotizacion)");
		
		BaseResponseDTO response = serviceFactory.getCotizaService().validarNoObservado(numeroCotizacion);

		LOGGER.info("Salio a CotizaController#validarVidaFree(numeroCotizacion)");
        return response;
    }
	
	@RequestMapping(value = "/intermedio/conversionMoneda/{fecha}", method = RequestMethod.GET)
	@ResponseBody
	public TipoCambioResponseDTO getTipoCambioAcsele(@PathVariable(value="fecha") String fecha) {
		LOGGER.info("Entro a CotizaController#getTipoCambioAcsele()");
		
		TipoCambioResponseDTO respuesta = serviceFactory.getCotizaService().obtenerTipoCambioAcsele();
		
		LOGGER.info("Salio a CotizaController#getTipoCambioAcsele()");
		return respuesta;
	}
	
	// Anedd
	/*@RequestMapping(value = "/transmision", method = RequestMethod.POST)
	@ResponseBody
	public TransmisionAcseleResponseDTO transmitirAcsele(@RequestBody TransmisionAcseleRequestDTO request) {
		LOGGER.info("Entro a CotizaController#transmitirAcsele(request)");
		
		TransmisionAcseleResponseDTO response = serviceFactory.getCotizaService().transmitirCotizacion(request);

		LOGGER.info("Salio a CotizaController#transmitirAcsele(request)");
        return response;
    }*/
	
	
}
