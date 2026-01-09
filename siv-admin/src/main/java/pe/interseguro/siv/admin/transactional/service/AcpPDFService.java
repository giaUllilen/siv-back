package pe.interseguro.siv.admin.transactional.service;

import java.io.ByteArrayInputStream;

import pe.interseguro.siv.common.dto.response.AcpByteArrayInput;
import pe.interseguro.siv.common.dto.response.AcpFormularioCuentaResponse;
import pe.interseguro.siv.common.dto.response.AcpFormularioTarjetaResponse;
import pe.interseguro.siv.common.dto.response.BaseResponseDTO;

public interface AcpPDFService {
	
	public BaseResponseDTO generarPDF(String numeroPropuesta, int tag);
	public BaseResponseDTO generarPDFPrueba(String numeroPropuesta, int tag);
	
	
	//public BaseResponseDTO generarPDF(String numeroPropuesta, AcpFormularioTarjetaResponse tarjeta, AcpFormularioCuentaResponse cuenta);

	public ByteArrayInputStream crearPDF(String numeroPropuesta); //, AcpFormularioTarjetaResponse tarjeta, AcpFormularioCuentaResponse cuenta);
	AcpByteArrayInput crearPDFV2(String numeroPropuesta);
}
