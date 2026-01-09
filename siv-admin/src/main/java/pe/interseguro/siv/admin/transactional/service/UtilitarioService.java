package pe.interseguro.siv.admin.transactional.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import pe.interseguro.siv.common.dto.response.CRMReporteRrvvResponseDTO;

public interface UtilitarioService {
	public CRMReporteRrvvResponseDTO obtenerReporteRrvv();
	
	public ByteArrayInputStream generarExcelReporteRrvv() throws IOException;
	
}
