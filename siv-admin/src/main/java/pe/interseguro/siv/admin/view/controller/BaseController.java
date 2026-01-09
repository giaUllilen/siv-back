package pe.interseguro.siv.admin.view.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import com.google.gson.Gson;

import pe.interseguro.siv.admin.transactional.factory.ServiceFactory;
import pe.interseguro.siv.common.exception.SivSOAException;
import pe.interseguro.siv.common.util.Constantes;
import pe.interseguro.siv.common.util.Utilitarios;

/**
 * Base Controller (Reutilización)
 * 
 * @author digital-is
 *
 */
@Component
public class BaseController {

	protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	public Gson gson = new Gson();
	
	@Autowired
	ServiceFactory serviceFactory;

	@Autowired
	@Qualifier("messageSource")
	protected MessageSource messageSource;

	
	/**
	 * Génerico para generar throw Exception de validaciones atributos del DTO request
	 * @param bindingResult
	 * @param messageSource
	 */
	protected void getBindingResultError(BindingResult bindingResult, String requestDTO) {
		LOGGER.info("Entro a BaseController#bindingResultError(usuarioRegistroRequestDTO, bindingResult, requestDTO)");
		
		if(bindingResult.hasErrors()){
			throw new SivSOAException(
				Utilitarios.obtenerMensaje(messageSource, new Object[] { requestDTO }, Constantes.MENSAJE_GENERICO_SOAEXCEPTION),
				bindingResult
			);
		}
		
		LOGGER.info("Salio a BaseController#bindingResultError(usuarioRegistroRequestDTO, bindingResult, requestDTO)");
	}
	
	protected void getBindingResultErrorEmail(BindingResult bindingResult, String requestDTO) {
		LOGGER.info("Entro a BaseController#getBindingResultErrorEmail(bindingResult, requestDTO)");
		
		if(bindingResult.hasErrors()){
			throw new SivSOAException(
				Utilitarios.obtenerMensaje(messageSource, new Object[] { requestDTO }, Constantes.MENSAJE_GENERICO_SOAEXCEPTION_EMAIL),
				bindingResult
			);
		}
		
		LOGGER.info("Salio a BaseController#getBindingResultErrorEmail(bindingResult, requestDTO)");
	}
	
}
