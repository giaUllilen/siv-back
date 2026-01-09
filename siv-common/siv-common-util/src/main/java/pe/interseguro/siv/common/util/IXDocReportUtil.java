package pe.interseguro.siv.common.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.images.ClassPathImageProvider;
import fr.opensagres.xdocreport.document.images.FileImageProvider;
import fr.opensagres.xdocreport.document.images.IImageProvider;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

public class IXDocReportUtil {

	/**
	 * Generar documento WORD o PDF a partir de una plantilla WORD
	 * 
	 * @param plantilla
	 * @param nombreArchivo
	 * @param parametros
	 * @param esPDF
	 * @throws Exception
	 */
	
	
	public static String generarDocumento(File plantilla, String nombreArchivo, Map<String, Object> parametros, Boolean esPDF) throws Exception {
		//-- Inicializar xdocReport
		FileInputStream fileInputStream = new FileInputStream(plantilla);
		BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
		IXDocReport xdocReport = XDocReportRegistry.getRegistry().loadReport(bufferedInputStream, TemplateEngineKind.Freemarker);
		
		//-- Parametros
		IContext context = xdocReport.createContext();
		context.putMap(parametros);
		
		//-- Crear apuntador en TMP
		File tmp = File.createTempFile(nombreArchivo, esPDF?Constantes.ARCHIVO_EXT_PDF:Constantes.ARCHIVO_EXT_DOCX );
		nombreArchivo = tmp.getName(); //-- retorna nuevo valor
		
		//-- Generar archivo
		if( esPDF ) {
			xdocReport.convert(
				context, 
				Options.getTo(ConverterTypeTo.PDF), 
				new FileOutputStream( tmp )
			);		
		} else {
			xdocReport.process(
				context, 
				new FileOutputStream( tmp )
			);
		}
		
		//-- Cerrar IO
		fileInputStream.close();
		bufferedInputStream.close();		
		return nombreArchivo;
	}

	
	public static String generarDocumento(File plantilla, String nombreArchivo, Map<String, Object> parametros, Boolean esPDF, List<String> metacampos, Map<String, Object > metadatos ) throws Exception {
		//-- Inicializar xdocReport
		FileInputStream fileInputStream = new FileInputStream(plantilla);
		BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
		IXDocReport xdocReport = XDocReportRegistry.getRegistry().loadReport(bufferedInputStream, TemplateEngineKind.Freemarker);
		
		
		//-- Parametros
		IContext context = xdocReport.createContext();
		// System.out.println(context);
		context.putMap(parametros);
		
		FieldsMetadata fieldsmetadata = new FieldsMetadata();
		
		for(String metacampo : metacampos) {

			fieldsmetadata.addFieldAsImage(metacampo);
		}
		xdocReport.setFieldsMetadata(fieldsmetadata);
		
		for (Map.Entry<String, Object> metadato : metadatos.entrySet()) {
			context.put(metadato.getKey(), metadato.getValue());

		}
		
		
		//-- Crear apuntador en TMP
		File tmp = File.createTempFile(nombreArchivo, esPDF?Constantes.ARCHIVO_EXT_PDF:Constantes.ARCHIVO_EXT_DOCX );
		
		nombreArchivo = tmp.getName(); //-- retorna nuevo valor
		
		//-- Generar archivo
		try {
			if( esPDF ) {
				xdocReport.convert(
					context, 
					Options.getTo(ConverterTypeTo.PDF), 
					new FileOutputStream( tmp )
				);		
			} else {
				xdocReport.process(
					context, 
					new FileOutputStream( tmp )
				);
			}
		} catch (Exception e) {

			System.out.println("error" + e.getMessage());
		}
		
		
		//-- Cerrar IO
		fileInputStream.close();
		bufferedInputStream.close();
		return nombreArchivo;
	}


	
}
