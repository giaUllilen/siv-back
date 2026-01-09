package pe.interseguro.siv.admin.transactional.service.impl;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.interseguro.siv.admin.transactional.service.UtilitarioService;
import pe.interseguro.siv.common.dto.response.CRMReporteDetalleRrvvResponseDTO;
import pe.interseguro.siv.common.dto.response.CRMReporteRrvvResponseDTO;
import pe.interseguro.siv.common.persistence.db.crm.bean.ReporteRRVV;
import pe.interseguro.siv.common.persistence.db.crm.repository.CrmRepository;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service("utilitarioService")
@Transactional
public class UtilitarioServiceImpl implements UtilitarioService {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private CrmRepository crmRepository;
	
	@Override
	public ByteArrayInputStream generarExcelReporteRrvv() throws IOException {
		Workbook workbook = new XSSFWorkbook(); 
		ByteArrayOutputStream out = new ByteArrayOutputStream();
        Sheet sheet = workbook.createSheet("Reporte");

        Font headerFont = workbook.createFont();
        short bold = 500;
        headerFont.setBoldweight(bold);
        headerFont.setColor(IndexedColors.BLUE.getIndex());

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        // Header Row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("CUSPP");
        headerRow.getCell(0).setCellStyle(headerCellStyle);
        headerRow.createCell(1).setCellValue("PROSPECTO");
        headerRow.getCell(1).setCellStyle(headerCellStyle);
        headerRow.createCell(2).setCellValue("OPORTUNIDAD");
        headerRow.getCell(2).setCellStyle(headerCellStyle);
        headerRow.createCell(3).setCellValue("CATEGORIA");
        headerRow.getCell(3).setCellStyle(headerCellStyle);
        headerRow.createCell(4).setCellValue("MODULO");
        headerRow.getCell(4).setCellStyle(headerCellStyle);
        headerRow.createCell(5).setCellValue("ESTADO");
        headerRow.getCell(5).setCellStyle(headerCellStyle);
        headerRow.createCell(6).setCellValue("SALDO CIC TOTAL");
        headerRow.getCell(6).setCellStyle(headerCellStyle);
        headerRow.createCell(7).setCellValue("FECHA NACIMIENTO PROSPECTO");
        headerRow.getCell(7).setCellStyle(headerCellStyle);
        headerRow.createCell(8).setCellValue("FECHA ASIGNACION PROSPECTO");
        headerRow.getCell(8).setCellStyle(headerCellStyle);
        headerRow.createCell(9).setCellValue("RAM PROSPECTO");
        headerRow.getCell(9).setCellStyle(headerCellStyle);
        headerRow.createCell(10).setCellValue("FECHA ULTIMO APORTE");
        headerRow.getCell(10).setCellStyle(headerCellStyle);
        headerRow.createCell(11).setCellValue("PRONOSTICO");
        headerRow.getCell(11).setCellStyle(headerCellStyle);
        headerRow.createCell(12).setCellValue("DIRECCION PARTICULAR PROSPECTO");
        headerRow.getCell(12).setCellStyle(headerCellStyle);
        headerRow.createCell(13).setCellValue("DISTRITO PARTICULAR PROSPECTO");
        headerRow.getCell(13).setCellStyle(headerCellStyle);
        headerRow.createCell(14).setCellValue("PROVINCIA PARTICULAR PROSPECTO");
        headerRow.getCell(14).setCellStyle(headerCellStyle);
        headerRow.createCell(15).setCellValue("DEPARTAMENTO PARTICULAR PROSPECTO");
        headerRow.getCell(15).setCellStyle(headerCellStyle);
        headerRow.createCell(16).setCellValue("SCORING");
        headerRow.getCell(16).setCellStyle(headerCellStyle);
        headerRow.createCell(17).setCellValue("FECHA ULTIMA CITA COMPLETADA");
        headerRow.getCell(17).setCellStyle(headerCellStyle);
        headerRow.createCell(18).setCellValue("PROPIETARIO");
        headerRow.getCell(18).setCellStyle(headerCellStyle);
        headerRow.createCell(19).setCellValue("ADMINISTRADOR USUARIO PROPIETARIO");
        headerRow.getCell(19).setCellStyle(headerCellStyle);
        headerRow.createCell(20).setCellValue("ULTIMA ACTIVIDAD COMPLETADA");
        headerRow.getCell(20).setCellStyle(headerCellStyle);
        headerRow.createCell(21).setCellValue("FECHA ULTIMA ACTIVIDAD COMPLETADA");
        headerRow.getCell(21).setCellStyle(headerCellStyle);
        headerRow.createCell(22).setCellValue("PERIODO PLAN TRABAJO");
        headerRow.getCell(22).setCellStyle(headerCellStyle);
        headerRow.createCell(23).setCellValue("CUMPLIMIENTO PLAN TRABAJO");
        headerRow.getCell(23).setCellStyle(headerCellStyle);
        
        int rowCount = 1;
        for (ReporteRRVV r : crmRepository.obtenerReporteRrvv()) {
            Row userRow =  sheet.createRow(rowCount++);
            userRow.createCell(0).setCellValue(r.getCuspp());
            userRow.createCell(1).setCellValue(r.getProspecto());
            userRow.createCell(2).setCellValue(r.getOportunidad());
            userRow.createCell(3).setCellValue(r.getCategoria());
            userRow.createCell(4).setCellValue(r.getModulo());
            userRow.createCell(5).setCellValue(r.getEstado());
            userRow.createCell(6).setCellValue(r.getSaldoCICTotal());
            userRow.createCell(7).setCellValue(r.getFechaNacimientoProspecto());
            userRow.createCell(8).setCellValue(r.getFechaAsignacionProspecto());
            userRow.createCell(9).setCellValue(r.getRamProspecto());
            userRow.createCell(10).setCellValue(r.getFechaUltimoAporte());
            userRow.createCell(11).setCellValue(r.getPronostico());
            userRow.createCell(12).setCellValue(r.getDireccionParticularProspecto());
            userRow.createCell(13).setCellValue(r.getDistritoParticularProspecto());
            userRow.createCell(14).setCellValue(r.getProvinciaParticularProspecto());
            userRow.createCell(15).setCellValue(r.getDepartamentoParticularProspecto());
            userRow.createCell(16).setCellValue(r.getScoring());
            userRow.createCell(17).setCellValue(r.getFechaUltimaCitaCompletada());
            userRow.createCell(18).setCellValue(r.getPropietario());
            userRow.createCell(19).setCellValue(r.getAdministradorUsuarioPropietario());
            userRow.createCell(20).setCellValue(r.getUltimaActividadCompletada());
            userRow.createCell(21).setCellValue(r.getFechaUltimaActividadCompletada());
            userRow.createCell(22).setCellValue(r.getPeriodoPlanTrabajo());
            userRow.createCell(23).setCellValue(r.getCumplimientoPlanTrabajo());
         }
        
        workbook.write(out);
        
        return new ByteArrayInputStream(out.toByteArray());
		
	}
	
	@Override
	public CRMReporteRrvvResponseDTO obtenerReporteRrvv() {
		CRMReporteRrvvResponseDTO response = new CRMReporteRrvvResponseDTO();
		List<CRMReporteDetalleRrvvResponseDTO> detalle = new ArrayList<CRMReporteDetalleRrvvResponseDTO>();
		//response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
		//response.setMensajeRespuesta("OK");
		for (ReporteRRVV r : crmRepository.obtenerReporteRrvv()) {
			CRMReporteDetalleRrvvResponseDTO bean = new CRMReporteDetalleRrvvResponseDTO();
			bean.setCuspp(r.getCuspp());
			bean.setDocumentoProspecto(r.getDocumentoProspecto());
			bean.setProspecto(r.getProspecto());
			bean.setOportunidad(r.getOportunidad());
			bean.setCategoria(r.getCategoria());
			bean.setModulo(r.getModulo());
			bean.setEstado(r.getEstado());
			bean.setSaldoCICTotal(r.getSaldoCICTotal());
			bean.setFechaNacimientoProspecto(r.getFechaNacimientoProspecto());
			bean.setFechaAsignacionProspecto(r.getFechaAsignacionProspecto());
			bean.setRamProspecto(r.getRamProspecto());
			bean.setFechaUltimoAporte(r.getFechaUltimoAporte());
			bean.setPronostico(r.getPronostico());
			bean.setDireccionParticularProspecto(r.getDireccionParticularProspecto());
			bean.setDistritoParticularProspecto(r.getDistritoParticularProspecto());
			bean.setProvinciaParticularProspecto(r.getProvinciaParticularProspecto());
			bean.setDepartamentoParticularProspecto(r.getDepartamentoParticularProspecto());
			bean.setScoring(r.getScoring());
			bean.setFechaUltimaCitaCompletada(r.getFechaUltimaCitaCompletada());
			bean.setPropietario(r.getPropietario());
			bean.setAdministradorUsuarioPropietario(r.getAdministradorUsuarioPropietario());
			bean.setDivisa(r.getDivisa());
			bean.setUltimaActividadCompletada(r.getUltimaActividadCompletada());
			bean.setFechaUltimaActividadCompletada(r.getFechaUltimaActividadCompletada());
			bean.setPeriodoPlanTrabajo(r.getPeriodoPlanTrabajo());
			bean.setCumplimientoPlanTrabajo(r.getCumplimientoPlanTrabajo());
			detalle.add(bean);
		}
		response.setRegistros(detalle);
		return response;
	}

}
