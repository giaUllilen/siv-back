package pe.interseguro.siv.admin.view.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import pe.interseguro.siv.common.dto.response.CotizadorCumuloResponseDTO;

@RestController
@RequestMapping(value = {"/api/v1/recursos"})
public class RecursoController extends BaseController {
	@RequestMapping(value = "/cotizador/cumulo/{tipoDocumento}/{numeroDocumento}", method = RequestMethod.GET)
	@ResponseBody
	public CotizadorCumuloResponseDTO getCumulo(@PathVariable(value="tipoDocumento") String tipoDocumento,
			@PathVariable(value="numeroDocumento") String numeroDocumento) {
		LOGGER.info("Entro a CotizaController#getCumulo(tipoDocumento,numeroDocumento)");
		
		CotizadorCumuloResponseDTO respuesta = serviceFactory.getCotizaService().obtenerCumulo(tipoDocumento, numeroDocumento); 
		
		LOGGER.info("Salio a CotizaController#getCumulo(tipoDocumento,numeroDocumento)");
		return respuesta;
	}
}
