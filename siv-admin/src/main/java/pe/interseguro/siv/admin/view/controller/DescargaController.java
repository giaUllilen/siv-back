package pe.interseguro.siv.admin.view.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import pe.interseguro.siv.common.dto.response.CotizaTablaResponseDTO;

@RestController
@RequestMapping(value = {"/api/v1/descargas"})
public class DescargaController extends BaseController{
	/**
	 * Descargar archivo 
	 * 
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/{nombreArchivo}", method = RequestMethod.GET)
	@ResponseBody
	public void descargar(@PathVariable(value="nombreArchivo") String nombreArchivo, HttpServletResponse response) {
		LOGGER.info("Entro a DescargaController#descargar(file)");
		
		serviceFactory.getDescargaService().descargar(nombreArchivo, response);
		
		LOGGER.info("Salio a DescargaController#descargar(file)");
	}
}
