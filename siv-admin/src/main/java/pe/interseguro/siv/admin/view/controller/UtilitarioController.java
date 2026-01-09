package pe.interseguro.siv.admin.view.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import pe.interseguro.siv.common.dto.response.CRMReporteDetalleRrvvResponseDTO;
import pe.interseguro.siv.common.dto.response.CRMReporteRrvvResponseDTO;

@RestController
@RequestMapping(value = {"/api/v1/utilidades"})
public class UtilitarioController extends BaseController {

	@RequestMapping(value = "/reporte-rrvv", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public void reporteRrvv(HttpServletResponse response) throws Exception {
		LOGGER.info("Entro a UtilitarioController#reporteRrvv()");
		
		String filename = "reporte.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        //create a csv writer
        StatefulBeanToCsv<CRMReporteDetalleRrvvResponseDTO> writer = new StatefulBeanToCsvBuilder<CRMReporteDetalleRrvvResponseDTO>(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator('|')
                .withOrderedResults(false)
                .build();

        //write all users to csv file
        writer.write(serviceFactory.getUtilitarioService().obtenerReporteRrvv().getRegistros());
	}
	
}
	
