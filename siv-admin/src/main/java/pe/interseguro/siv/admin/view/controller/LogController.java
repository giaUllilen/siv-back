package pe.interseguro.siv.admin.view.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import pe.interseguro.siv.common.dto.response.LogResponseDTO;


@RestController
@RequestMapping(value = {"/api/v1/logger"})
public class LogController extends BaseController {
	/*@RequestMapping(value = "/diario", method = RequestMethod.GET)
	@ResponseBody
	public List<LogResponseDTO> obtenerLogAgenteDiario() {
		LOGGER.info("Entro a LogController#obtenerLogAgenteDiario(agente)");
		
		List<LogResponseDTO> response = serviceFactory.getLogService().listarLogDiario();
		
		LOGGER.info("Salio a LogController#obtenerLogAgenteDiario(agente)");
		return response;
	}
	
	@RequestMapping(value = "/diario/{agente}", method = RequestMethod.GET)
	@ResponseBody
	public List<LogResponseDTO> obtenerLogAgenteDiario(
		@PathVariable(value="agente") String agente
	) {
		LOGGER.info("Entro a LogController#obtenerLogAgenteDiario(agente)");
		
		List<LogResponseDTO> response = serviceFactory.getLogService().listarLogDiario(agente);
		
		LOGGER.info("Salio a LogController#obtenerLogAgenteDiario(agente)");
		return response;
	}*/
}
