package pe.interseguro.siv.admin.transactional.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;

import pe.interseguro.siv.admin.transactional.factory.ServiceFactory;
import pe.interseguro.siv.admin.transactional.service.SolicitudPDFService;
import pe.interseguro.siv.admin.transactional.service.SolicitudService;
import pe.interseguro.siv.common.dto.request.ObtenerRequestEvaluatorRequestDTO;
import pe.interseguro.siv.common.dto.response.ADNGetDocumentosResponseDTO;
import pe.interseguro.siv.common.dto.response.BaseResponseDTO;
import pe.interseguro.siv.common.dto.response.ObtenerRequestEvaluatorResponseDTO;
import pe.interseguro.siv.common.persistence.db.mysql.domain.Solicitud;
import pe.interseguro.siv.common.persistence.db.mysql.repository.SolicitudRepository;
import pe.interseguro.siv.common.util.Constantes;

public class SolicitudRunnable implements Runnable {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	//@Autowired
	//private ServiceFactory serviceFactory;
	
	@Autowired
	private SolicitudPDFService solicitudPDFService;
	
	@Autowired
	private SolicitudService solicitudService;
	
	 @Autowired
	    private SolicitudRepository solicitudRepository;
	 
	
	private Long idSolicitud;
	private String tipoProducto;

	
	public SolicitudRunnable(Long idSolicitud, String tipoProducto) {
		this.idSolicitud = idSolicitud;
		this.tipoProducto = tipoProducto;
	}
	
	@Override
	public void run() {
		
		try
		{
			/*
			 * 1. Generar solicitud PDF
			 * 2. Enviar correo con solicitud y cotización
			 * 3. Enviar Datos al CRM
			 * */

			LOGGER.info("SolicitudRunnable.generarSolicitudPDF.Inicio");
			BaseResponseDTO responsePDF = solicitudPDFService.generarSolicitudPDF(idSolicitud, tipoProducto);
			LOGGER.info("SolicitudRunnable.generarSolicitudPDF.Fin");

			if(!responsePDF.getCodigoRespuesta().equals(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO)) {
				LOGGER.info("ERROR generarInformacionSolicitud.generarSolicitudPDF:"+responsePDF.getMensajeRespuesta());
			}else {
				LOGGER.info("SolicitudRunnable.enviarCorreo.Inicio");
				Boolean respuestaCorreo = solicitudService.enviarCorreo(idSolicitud, tipoProducto);
				//Boolean respuestaCorreo = solicitudService.enviarCorreoSME(idSolicitud, tipoProducto);
			}

			LOGGER.info("SolicitudRunnable.enviarDatosSolicitudCRM.Inicio");
			BaseResponseDTO responseCRM = solicitudService.enviarDatosSolicitudCRM(idSolicitud);
			LOGGER.info("SolicitudRunnable.enviarDatosSolicitudCRM.Fin");
			if(!responseCRM.getCodigoRespuesta().equals(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO)) {
				LOGGER.info("ERROR generarInformacionSolicitud.enviarDatosSolicitudCRM:"+responseCRM.getMensajeRespuesta());
			}
			
		}catch (Exception ex) {
			LOGGER.info("ERROR SolicitudRunnable.getMessage:"+ex.getMessage());
			LOGGER.info("ERROR SolicitudRunnable:"+ex);
			
		}
		
	}

}
