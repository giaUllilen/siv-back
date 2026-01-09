package pe.interseguro.siv.admin.view.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.google.gson.Gson;

import pe.interseguro.siv.common.dto.response.BaseResponseDTO;
import pe.interseguro.siv.common.exception.ErrorResourceDTO;
import pe.interseguro.siv.common.exception.FieldErrorResourceDTO;
import pe.interseguro.siv.common.exception.SivDBException;
import pe.interseguro.siv.common.exception.SivSOAException;
import pe.interseguro.siv.common.exception.SivTXException;
import pe.interseguro.siv.common.util.Constantes;
import pe.interseguro.siv.common.util.Utilitarios;


@ControllerAdvice(basePackages = {"pe.interseguro.siv.admin.view.controller"})
public class SivControllerAdvice extends ResponseEntityExceptionHandler {

	@Autowired
	@Qualifier("validationMessageSource")
	private MessageSource messageSource;
	
	
	@ExceptionHandler({ SivSOAException.class })
    protected ResponseEntity<Object> handleInvalidRequestSOA(RuntimeException e, WebRequest request) {

		//-- Variables
		SivSOAException exceptionBase = (SivSOAException) e;
        List<FieldErrorResourceDTO> fieldErrorResources = new ArrayList<FieldErrorResourceDTO>();
        
        //-- Logica - fields
        if(exceptionBase.getErrors()!=null) {
	        List<FieldError> fieldErrors = exceptionBase.getErrors().getFieldErrors();
	        for (FieldError fieldError : fieldErrors) {
	            FieldErrorResourceDTO fieldErrorResource = new FieldErrorResourceDTO();
	            fieldErrorResource.setResource(fieldError.getObjectName());
	            fieldErrorResource.setField(fieldError.getField());
	            fieldErrorResource.setCode(fieldError.getCode());
	            
	            System.out.println( "fieldError.getCodes() = " + new Gson().toJson(fieldError.getCodes()) );
	            fieldErrorResource.setMessage( Utilitarios.obtenerMensaje(messageSource, fieldError.getCodes()[0]) );
	            
	            fieldErrorResources.add(fieldErrorResource);
	        }
        }
        
        ErrorResourceDTO error = new ErrorResourceDTO(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR, exceptionBase.getMsjError());
        error.setFieldErrors(fieldErrorResources);

        //-- Logica - header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
//        headers.setAccessControlAllowOrigin("*");
        
        //-- Respuesta
        BaseResponseDTO response = new BaseResponseDTO();
        response.setObjErrorResource(error);
        response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
        response.setMensajeRespuesta(error.getMessage());
        
        return handleExceptionInternal(e, response, headers, HttpStatus.OK, request); //-- TODO: debe ser HttpStatus.UNPROCESSABLE_ENTITY, pero se pondra 200 para no complicar al front-end
    }
	
	@ExceptionHandler({ SivDBException.class })
    protected ResponseEntity<Object> handleInvalidRequestBD(RuntimeException e, WebRequest request) {
		
		//-- Variables
		SivDBException exceptionBase = (SivDBException) e;

        //-- Logica - base
        ErrorResourceDTO error = new ErrorResourceDTO(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR, exceptionBase.getMsjError());
        
        //-- Logica header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
//        headers.setAccessControlAllowOrigin("*");
        
        //-- Respuesta
        BaseResponseDTO response = new BaseResponseDTO();
        response.setObjErrorResource(error);
        response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
        response.setMensajeRespuesta(error.getMessage());
        
        return handleExceptionInternal(e, response, headers, HttpStatus.OK, request); //-- TODO: debe ser HttpStatus.UNPROCESSABLE_ENTITY, pero se pondra 200 para no complicar al front-end
    }
	
	@ExceptionHandler({ SivTXException.class })
    protected ResponseEntity<Object> handleInvalidRequestGeneral(RuntimeException e, WebRequest request) {
		
		//-- Variables
		SivTXException exceptionBase = (SivTXException) e;
        
        //-- Logica - base
        ErrorResourceDTO error = new ErrorResourceDTO(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR, exceptionBase.getMsjError());
        
        //-- Logica header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
//        headers.setAccessControlAllowOrigin("*");
        
        //-- Respuesta
        BaseResponseDTO response = new BaseResponseDTO();
        response.setObjErrorResource(error);
        response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
        response.setMensajeRespuesta(error.getMessage());
        
        return handleExceptionInternal(e, response, headers, HttpStatus.OK, request); //-- TODO: debe ser HttpStatus.UNPROCESSABLE_ENTITY, pero se pondra 200 para no complicar al front-end
    }
	
	
}
