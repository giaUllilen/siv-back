/**
 * 
 */
package pe.interseguro.siv.admin.view.controller;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;
import java.io.IOException;

import javax.validation.Valid;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import pe.interseguro.siv.common.bean.DocumentoADN;
import pe.interseguro.siv.common.bean.GenericoComboBean;
import pe.interseguro.siv.common.dto.request.ADNAutoguardadoRequestDTO;
import pe.interseguro.siv.common.dto.request.ADNClienteRequestDTO;
import pe.interseguro.siv.common.dto.request.ADNFirmaRequestDTO;
import pe.interseguro.siv.common.dto.request.ADNPolizaRequestDTO;
import pe.interseguro.siv.common.dto.request.ADNPotencialRequestDTO;
import pe.interseguro.siv.common.dto.request.ADNRegistroReferidoRequestDTO;
import pe.interseguro.siv.common.dto.request.ADNRegistroRequestDTO;
import pe.interseguro.siv.common.dto.request.ADNUsuarioRequestDTO;
import pe.interseguro.siv.common.dto.response.ADNAutoguardadoResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNCodigoTablaResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNConsentimientoAceptadoResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNConsultaClienteDTResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNConsultaLdpdpResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNConsultaPolizaBUCResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNConsultaPolizaResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNConsultaPotencialDTResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNConsultaUsuarioDTResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNFirmaResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNInicializacionResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNRegistroReferidoResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNRegistroResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNReglamentoResponseDTO;
import pe.interseguro.siv.common.dto.response.BaseResponseDTO;
import pe.interseguro.siv.common.enums.TipoDocumentoADN;
import pe.interseguro.siv.common.util.Constantes;

/**
 * RESTful del ADN
 * 
 * @author ti-is
 */
@RestController
@RequestMapping(value = { "/api/v1/adns" })
public class AdnController extends BaseController {

	@RequestMapping(value = "/inicializaciones/{idUsuario}/{device}/{os}", method = RequestMethod.GET)
	@ResponseBody
	public ADNInicializacionResponseDTO obtenerInicializacion(String idUsuario, String device, String os) {
		LOGGER.info("Entro a AdnController#obtenerInicializacion()");

		ADNInicializacionResponseDTO response = serviceFactory.getAdnService().obtenerInicializacion(idUsuario, device,
				os);

		LOGGER.info("Salio a AdnController#obtenerInicializacion()");
		return response;
	}
	
	
	@RequestMapping(value = "/reglamentos/{tipoDocCliente}/{numDocCliente}/{idUsuario}/{correoUsuario}/{device}/{os}", method = RequestMethod.GET)
	@ResponseBody
	public ADNReglamentoResponseDTO obtenerReglamento(@PathVariable(value = "tipoDocCliente") String tipoDocCliente,
			@PathVariable(value = "numDocCliente") String numDocCliente,
			@PathVariable(value = "idUsuario") String idUsuario,
			@PathVariable(value = "correoUsuario") String correoUsuario, @PathVariable(value = "device") String device,
			@PathVariable(value = "os") String os) {
		LOGGER.info("Entro a AdnController#obtenerReglamento(tipoDocCliente, numDocCliente, idUsuario)");

		ADNReglamentoResponseDTO response = serviceFactory.getAdnService().obtenerReglamento(tipoDocCliente,
				numDocCliente, idUsuario, correoUsuario, device, os);

		LOGGER.info("Salio a AdnController#obtenerReglamento(tipoDocCliente, numDocCliente, idUsuario)");
		return response;
	}

	@RequestMapping(value = "/cliente/{tipoDocCliente}/{numDocCliente}/{idUsuario}/{device}/{os}", method = RequestMethod.GET)
	@ResponseBody
	public ADNReglamentoResponseDTO obtenerInformacionCliente(
			@PathVariable(value = "tipoDocCliente") String tipoDocCliente,
			@PathVariable(value = "numDocCliente") String numDocCliente,
			@PathVariable(value = "idUsuario") String idUsuario, @PathVariable(value = "device") String device,
			@PathVariable(value = "os") String os) {
		LOGGER.info("Entro a AdnController#obtenerInformacionCliente(tipoDocCliente, numDocCliente)");

		ADNReglamentoResponseDTO response = serviceFactory.getAdnService()
				.obtenerInformacionClienteVarios(tipoDocCliente, numDocCliente, idUsuario, device, os);

		LOGGER.info("Salio a AdnController#obtenerInformacionCliente(tipoDocCliente, numDocCliente)");
		return response;
	}
	
	/* Reingeniería ADN / SIV */
	@RequestMapping(value = "/polizas/{tipoDocCliente}/{numDocCliente}/{idUsuario}/{device}/{os}", method = RequestMethod.GET)
	@ResponseBody
	public ADNConsultaPolizaBUCResponseDTO obtenerPolizasCliente(
		@PathVariable(value="tipoDocCliente") String tipoDocCliente,
		@PathVariable(value="numDocCliente") String numDocCliente,
		@PathVariable(value="idUsuario") String idUsuario,
		@PathVariable(value="device") String device,
		@PathVariable(value="os") String os
	) {
		LOGGER.info("Entro a AdnController#obtenerPolizasCliente(tipoDocCliente, numDocCliente)");
		
		ADNConsultaPolizaBUCResponseDTO response = serviceFactory.getAdnService().obtenerPolizasCliente(tipoDocCliente, numDocCliente, idUsuario, device, os);
		
		LOGGER.info("Salio a AdnController#obtenerPolizasCliente(tipoDocCliente, numDocCliente)");
		return response;
	}

	@RequestMapping(value = "/firmas", method = RequestMethod.POST)
	@ResponseBody
	public ADNFirmaResponseDTO registrarFirma(@Valid @RequestBody ADNFirmaRequestDTO aDNFirmaRequestDTO,
			BindingResult bindingResult) {
		LOGGER.info("Entro a AdnController#registrarFirma(aDNFirmaRequestDTO)");

		getBindingResultError(bindingResult, "SesionRequestDTO");

		ADNFirmaResponseDTO response = serviceFactory.getAdnService().registrarFirma(aDNFirmaRequestDTO);

		LOGGER.info("Salio a AdnController#registrarFirma(aDNFirmaRequestDTO)");
		return response;
	}

	@RequestMapping(value = "/registros", method = RequestMethod.POST)
	@ResponseBody
	public ADNRegistroResponseDTO registrarAdn(@Valid @RequestBody ADNRegistroRequestDTO aDNRegistroRequestDTO,
			BindingResult bindingResult) {
		LOGGER.info("Entro a AdnController#registrarAdn(aDNRegistroRequestDTO)");
		
		getBindingResultErrorEmail(bindingResult, "Formato de correo inválido");

		ADNRegistroResponseDTO response = serviceFactory.getAdnService().registrarADNS(aDNRegistroRequestDTO);

		LOGGER.info("Salio a AdnController#registrarADNS(aDNRegistroRequestDTO)");
		return response;
	}

	@RequestMapping(value = "/registros-interno-pdf", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<InputStreamResource> registrarInteroAdn(@Valid @RequestBody ADNRegistroRequestDTO aDNRegistroRequestDTO,
			BindingResult bindingResult) throws IOException {
		LOGGER.info("Entro a AdnController#registrarAdn(registrarInteroAdn)");

		ByteArrayInputStream bis = serviceFactory.getAdnService().printPDFAdn(aDNRegistroRequestDTO);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=adn.pdf");

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bis));
   
	}

	@RequestMapping(value = "/autoguardados", method = RequestMethod.POST)
	@ResponseBody
	public ADNAutoguardadoResponseDTO autoguardado(
			@Valid @RequestBody ADNAutoguardadoRequestDTO aDNAutoguardadoRequestDTO, BindingResult bindingResult) {
		LOGGER.info("Entro a AdnController#autoguardado(aDNAutoguardadoRequestDTO)");

		getBindingResultError(bindingResult, "RegistroRequestDTO");

		ADNAutoguardadoResponseDTO response = serviceFactory.getAdnService().autoguardado(aDNAutoguardadoRequestDTO);

		LOGGER.info("Salio a AdnController#autoguardado(aDNAutoguardadoRequestDTO)");
		return response;
	}

	@RequestMapping(value = "/referidos", method = RequestMethod.POST)
	@ResponseBody
	public ADNRegistroReferidoResponseDTO registrarAdnReferido(
			@RequestBody ADNRegistroReferidoRequestDTO aDNRegistroReferidoRequestDTO, BindingResult bindingResult) {
		LOGGER.info("Entro a AdnController#registrarAdn(aDNRegistroRequestDTO)");

		getBindingResultError(bindingResult, "RegistroRequestDTO");

		ADNRegistroReferidoResponseDTO response = serviceFactory.getAdnService()
				.registrarReferidoADNS(aDNRegistroReferidoRequestDTO);

		LOGGER.info("Salio a AdnController#registrarReferidoADNS(aDNRegistroReferidoRequestDTO)");
		return response;
	}
	
	
	// OLD
	@RequestMapping(value = "/polizas", method = RequestMethod.POST)
	@ResponseBody
	public ADNConsultaPolizaResponseDTO consultaPoliza(@RequestBody ADNPolizaRequestDTO aDNPolizaRequestDTO,
			BindingResult bindingResult) {
		LOGGER.info("Entro a AdnController#consultaPoliza(aDNPolizaRequestDTO)");

		getBindingResultError(bindingResult, "ADNPolizaRequestDTO");

		ADNConsultaPolizaResponseDTO response = serviceFactory.getAdnService().busquedaPoliza(aDNPolizaRequestDTO);

		LOGGER.info("Salio a AdnController#consultaPoliza(ADNPolizaRequestDTO)");
		return response;
	}

	@RequestMapping(value = "/clienteDT", method = RequestMethod.POST)
	@ResponseBody
	public ADNConsultaClienteDTResponseDTO consultaClienteDT(@RequestBody ADNClienteRequestDTO aDNClienteRequestDTO,
			BindingResult bindingResult) {
		LOGGER.info("Entro a AdnController#consultaClienteDT(aDNClienteRequestDTO)");

		getBindingResultError(bindingResult, "ADNClienteRequestDTO");

		ADNConsultaClienteDTResponseDTO response = serviceFactory.getAdnService().clienteDT(aDNClienteRequestDTO);

		LOGGER.info("Salio a AdnController#consultaClienteDT(aDNClienteRequestDTO)");
		return response;
	}

	@RequestMapping(value = "/usuarioDT", method = RequestMethod.POST)
	@ResponseBody
	public ADNConsultaUsuarioDTResponseDTO consultaUsuarioDT(@RequestBody ADNUsuarioRequestDTO aDNUsuarioRequestDTO,
			BindingResult bindingResult) {
		LOGGER.info("Entro a AdnController#consultaUsuarioDT(aDNUsuarioRequestDTO)");

		getBindingResultError(bindingResult, "ADNUsuarioRequestDTO");

		ADNConsultaUsuarioDTResponseDTO response = serviceFactory.getAdnService().usuarioDT(aDNUsuarioRequestDTO);

		LOGGER.info("Salio a AdnController#consultaUsuarioDT(aDNUsuarioRequestDTO)");
		return response;
	}

	@RequestMapping(value = "/clientePotencialDT", method = RequestMethod.POST)
	@ResponseBody
	public ADNConsultaPotencialDTResponseDTO consultaPotencialDT(
			@RequestBody ADNPotencialRequestDTO aDNPotencialRequestDTO, BindingResult bindingResult) {
		LOGGER.info("Entro a AdnController#consultaPotencialDT(aDNPotencialRequestDTO)");

		getBindingResultError(bindingResult, "ADNUsuarioRequestDTO");

		ADNConsultaPotencialDTResponseDTO response = serviceFactory.getAdnService()
				.clientePotencialDT(aDNPotencialRequestDTO);

		LOGGER.info("Salio a AdnController#consultaPotencialDT(aDNPotencialRequestDTO)");
		return response;
	}

	@RequestMapping(value = "/codigo-tabla/{codigo}", method = RequestMethod.GET)
	@ResponseBody
	public ADNCodigoTablaResponseDTO codigoTabla(@PathVariable(value = "codigo") String codigo) {
		LOGGER.info("Entro a AdnController#codigoTabla()");

		ADNCodigoTablaResponseDTO response = new ADNCodigoTablaResponseDTO();
		response.setLista(serviceFactory.getAdnService().codigoTabla(codigo));
		response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);

		LOGGER.info("Salio a AdnController#codigoTabla()");
		return response;
	}
	
	/*@RequestMapping(value = "/pdf/{tipoDocumento}/{numeroDocumento}", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> exportPDFADN(@PathVariable(value="tipoDocumento") String tipoDocumento, 
    		@PathVariable(value="numeroDocumento") String numeroDocumento) {

        ByteArrayInputStream bis = serviceFactory.getAdnService().generarPDFADN2(tipoDocumento, numeroDocumento);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=reporte.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }*/
	
	@RequestMapping(value = "/pdf/{tipoDocumento}/{numeroDocumento}", method = RequestMethod.GET 
			)
    public BaseResponseDTO exportPDFADN(@PathVariable(value="tipoDocumento") String tipoDocumento, 
    		@PathVariable(value="numeroDocumento") String numeroDocumento) {

		BaseResponseDTO response = serviceFactory.getAdnService().generarPDFADN2(tipoDocumento, numeroDocumento);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=reporte.pdf");

        return response;
    }
	
	@RequestMapping(value = "/pdf/{nombreDocumento}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> PDFConsentimiento(
			@PathVariable(value = "nombreDocumento") String nombreDocumento) {
		
		ByteArrayInputStream response = serviceFactory.getAdnService().descargarPDF(nombreDocumento);
		if (response.equals(null)) return null;
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename="+nombreDocumento);
        
		return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(response));
	}
	
	@RequestMapping(value = "/mostrar/{tipoDocumento}/{numeroDocumento}", method = RequestMethod.GET)
	@ResponseBody
	public ADNConsultaLdpdpResponseDTO obtenerldpdp(@PathVariable(value="tipoDocumento") String tipoDocumento, 
    		@PathVariable(value="numeroDocumento") String numeroDocumento) {	
		
		ADNConsultaLdpdpResponseDTO response = serviceFactory.getAdnService().obtenerldpdp(tipoDocumento,numeroDocumento);

		return response;
	
	}
	
	@RequestMapping(value = "/createConsentimiento/{tipo}")
	@ResponseBody
	@PostMapping
	public BaseResponseDTO createConsentimiento(/*@Valid*/ @RequestBody ADNRegistroRequestDTO aDNRegistroRequestDTO,
			@PathVariable(value = "tipo") Integer tipo) {
		LOGGER.info("Entro a AdnController#sendConsentimiento()");
		LOGGER.info("aDNRegistroRequestDTO: " + aDNRegistroRequestDTO.toString());
		LOGGER.info("Correo: " + aDNRegistroRequestDTO.getTitular().getCorreo());
		Gson gson = new Gson();

		BaseResponseDTO response = serviceFactory.getAdnService().createConsentimiento(tipo, aDNRegistroRequestDTO);

		LOGGER.info("response AdnController#sendConsentimiento() : "+gson.toJson(response));
		return response;
	}

	@RequestMapping(value = "/obtenerConsentimiento/{idUsuario}/{tipoDocumento}/{numeroDocumento}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> obtenerConsentimiento(@PathVariable(value = "idUsuario") String idUsuario,
			@PathVariable(value = "tipoDocumento") String tipoDocumento,
			@PathVariable(value = "numeroDocumento") String numeroDocumento) {
		LOGGER.info("Entro a AdnController#obtenerConsentimiento(idUsuario, tipoDocumento, numeroDocumento)");

		Map<String, Object> response = serviceFactory.getAdnService().obtenerConsentimiento(idUsuario, tipoDocumento, numeroDocumento);

		LOGGER.info("Salio a AdnController#obtenerConsentimiento(idUsuario, tipoDocumento, numeroDocumento)");
		return response;
	}
	
	@RequestMapping(value = "/sendConsentimiento/{tipo}/{dni}/{usuario}/{tipoDocumento}/{userOnly}")
	@ResponseBody
	@PostMapping
	public BaseResponseDTO sendConsentimiento(@PathVariable(value = "tipo") Integer tipo,
			@PathVariable(value = "dni") String dni,
			@PathVariable(value = "usuario") String usuario,
			@PathVariable(value = "tipoDocumento") Integer tipoDocumento,
			@PathVariable(value = "userOnly") Boolean userOnly,
			@RequestBody Map<String, Object> request
			) {
		LOGGER.info("Entro a AdnController#sendConsentimiento()");
		Gson gson = new Gson();

		BaseResponseDTO response = serviceFactory.getAdnService().sendConsentimiento(tipo, dni, usuario, tipoDocumento, userOnly,request);

		LOGGER.info("response AdnController#sendConsentimiento() : "+gson.toJson(response));
		return response;
	}
	
	@RequestMapping(value = "/getDataConsentimientoAccepted")
	@ResponseBody
	@PostMapping
	public ADNConsentimientoAceptadoResponseDTO getDataConsentimientoAccepted(@RequestBody Map<String, Object> request) {
		LOGGER.info("Entro a AdnController#getDataConsentimientoAccepted()");
		Gson gson = new Gson();
		LOGGER.info("request: "+gson.toJson(request));
		ADNConsentimientoAceptadoResponseDTO response = serviceFactory.getAdnService().processConsentimientoAccepted(request);

		LOGGER.info("response AdnController#sendConsentimiento() : "+gson.toJson(response));
		return response;
	}
	
	@RequestMapping(value = "/getDataConsentimiento/{dni}/{usuario}/{tipoDocumento}")
	@ResponseBody
	@GetMapping
	public ADNConsentimientoAceptadoResponseDTO getDataConsentimiento(@PathVariable(value = "usuario") String usuario,
			@PathVariable(value = "dni") String dni
			,@PathVariable(value = "tipoDocumento") Integer tipoDocumento) {
		LOGGER.info("Entro a AdnController#getDataConsentimiento()");
		Gson gson = new Gson();
		ADNConsentimientoAceptadoResponseDTO response = new ADNConsentimientoAceptadoResponseDTO();
		
		response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
		response.setData(serviceFactory.getAdnService().getDataConsentimiento(usuario, dni,tipoDocumento));

		LOGGER.info("response AdnController#sendConsentimiento() : "+gson.toJson(response));
		return response;
	}
}


