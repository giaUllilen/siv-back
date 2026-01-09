package pe.interseguro.siv.admin.transactional.service;

import java.io.ByteArrayInputStream;

import pe.interseguro.siv.common.dto.response.BaseResponseDTO;

public interface SolicitudPDFService {
	/**
	 * Generar Solicitud en PDF	o
	 * 
	 * @param Long idSolicitud - ID de la solicitud
	 * @return BaseResponseDTO
	 */
	public BaseResponseDTO generarSolicitudPDF(Long idSolicitud, String tipoProducto);
	
	/**
	 * 
	 * @param numeroCotizacion
	 * @param tipoProducto
	 * @return
	 */
	public ByteArrayInputStream crearPDFSolicitud(String numeroCotizacion, String tipoProducto);
	
	public BaseResponseDTO obtenerDocumentoSolicitud(String numeroCotizacion);	

	/*public BaseResponseDTO guardarPDF(String numeroCotizacion,MultipartFile file) throws IOException;*/
}
