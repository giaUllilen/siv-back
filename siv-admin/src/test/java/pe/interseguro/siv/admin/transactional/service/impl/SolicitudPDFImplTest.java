package pe.interseguro.siv.admin.transactional.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.util.Strings;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;

import fr.opensagres.xdocreport.document.images.FileImageProvider;
import fr.opensagres.xdocreport.document.images.IImageProvider;
import pe.interseguro.siv.admin.config.BaseTest;
import pe.interseguro.siv.admin.transactional.service.SolicitudService;
import pe.interseguro.siv.common.dto.request.SolicitudFiltroRequestDTO;
import pe.interseguro.siv.common.dto.request.SolicitudSMSRequestDTO;
import pe.interseguro.siv.common.dto.request.SolicitudValidarCodigoRequestDTO;
import pe.interseguro.siv.common.dto.request.UsuarioIngresoRequestDTO;
import pe.interseguro.siv.common.dto.response.BaseResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudResponseDTO;
import pe.interseguro.siv.common.util.Constantes;
import pe.interseguro.siv.common.util.ConstantesSolicitudPDF;
import pe.interseguro.siv.common.util.IXDocReportUtil;

public class SolicitudPDFImplTest extends BaseTest{
	
	
	@Autowired
	private SolicitudService solicitudService;

	@Test
	public void validarUsuarioTest() {
		
		UsuarioIngresoRequestDTO request = new UsuarioIngresoRequestDTO();
		request.setUsuario("jaybaram");
		request.setPassword("Diciembre2018");
		
		assertNotNull(request);
	}
	
	@Test
	public void generarPlantillaSolicitud() {
		
		String nombrePlantilla = "Plantilla_Solicitud_v1.docx";
		String nombreCheckOK = "mini_circle_complete.png";
		String nombreCheckNOK = "mini_circle_incomplete.png";
		
		File plantilla = new File( 
			getClass().getClassLoader().getResource( 
				Constantes.RUTA_PLANTILLA + "/" + nombrePlantilla 
			).getFile() 
		);
		
		
		File ImagenOK = new File( 
				getClass().getClassLoader().getResource( 
					Constantes.RUTA_PLANTILLA + "/" + nombreCheckOK 
				).getFile() 
			);

		File ImagenNOK = new File( 
				getClass().getClassLoader().getResource( 
					Constantes.RUTA_PLANTILLA + "/" + nombreCheckNOK 
				).getFile() 
			);
		
		
		System.out.println("existe = " + plantilla.exists());
		
		try {

			//-- Generar Documento
			String documentoIdentidad = "76364435";
			String documentoContratante = "76364435";
			String nombreArchivo = "solicitud_43459267_500035745";
			Date hoy = new Date();
			
			String numeroPropuesta = "500035745";
			String codigoSubPlan = "3";
			
			
			//Configuración de Checks
			List<String> metacampos = new ArrayList<>();
			//Checks_Solicitud
			metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_MONEDA_SOLES);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_MONEDA_DOLARES);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DESGLOSE_SOLES);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DESGLOSE_DOLARES);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_PLAN_EDUCACION);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_PLAN_SUENIO);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_PLAN_JUBILACION);
			
			//Checks_Asegurado
			metacampos.add(ConstantesSolicitudPDF.CODIGO_ASEGURADO_DOCUMENTO_TDOC_DNI);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_ASEGURADO_DOCUMENTO_TDOC_RUC);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_ASEGURADO_DOCUMENTO_TDOC_CE);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_ASEGURADO_SEXO_MASCULINO);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_ASEGURADO_SEXO_FEMENINO);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_ASEGURADO_ESTADO_CIVL_SOLTERO);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_ASEGURADO_ESTADO_CIVL_CASADO);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_ASEGURADO_ESTADO_CIVL_VIUDO);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_ASEGURADO_ESTADO_CIVL_DIVORCIADO);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_ASEGURADO_ESTADO_CIVL_CONVIVIENTE);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_ASEGURADO_DIRECCION_VIA_JIRON);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_ASEGURADO_DIRECCION_VIA_AVENIDA);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_ASEGURADO_DIRECCION_VIA_CALLE);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_ASEGURADO_DIRECCION_VIA_PASAJE);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_ASEGURADO_PEP_SI);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_ASEGURADO_PEP_NO);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_ASEGURADO_SUJETO_OBLIGADO_SI);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_ASEGURADO_SUJETO_OBLIGADO_NO);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_ASEGURADO_INGRESO_MONEDA_SOLES);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_ASEGURADO_INGRESO_MONEDA_DOLARES);
			
			//Checks_Contratante
			metacampos.add(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_DOCUMENTO_TDOC_DNI);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_DOCUMENTO_TDOC_RUC);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_DOCUMENTO_TDOC_CE);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_SEXO_MASCULINO);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_SEXO_FEMENINO);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_ESTADO_CIVL_SOLTERO);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_ESTADO_CIVL_CASADO);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_ESTADO_CIVL_VIUDO);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_ESTADO_CIVL_DIVORCIADO);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_ESTADO_CIVL_CONVIVIENTE);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_DIRECCION_VIA_JIRON);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_DIRECCION_VIA_AVENIDA);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_DIRECCION_VIA_CALLE);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_DIRECCION_VIA_PASAJE);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_PEP_SI);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_PEP_NO);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_SUJETO_OBLIGADO_SI);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_SUJETO_OBLIGADO_NO);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_INGRESO_MONEDA_SOLES);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_INGRESO_MONEDA_DOLARES);
			
			//Checks_Producto
			metacampos.add(ConstantesSolicitudPDF.CODIGO_PRODUCTO_FONDO_GARANTIZADO_MONEDA_SOLES);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_PRODUCTO_FONDO_GARANTIZADO_MONEDA_DOLARES);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_PRODUCTO_FRECUENCIA_PAGO_PRIMA_MENSUAL);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_PRODUCTO_FRECUENCIA_PAGO_PRIMA_TRIMESTRAL);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_PRODUCTO_FRECUENCIA_PAGO_PRIMA_SEMESTRAL);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_PRODUCTO_FRECUENCIA_PAGO_PRIMA_ANUAL);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_PRODUCTO_FRECUENCIA_PAGO_PRIMA_UNICA);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_PRODUCTO_FRECUENCIA_CUOTA_COMODIN);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_PRODUCTO_FRECUENCIA_CUOTA_DOBLE);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_PRODUCTO_TIPO_RIESGO_FUMADOR_SI);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_PRODUCTO_TIPO_RIESGO_FUMADOR_NO);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_PRODUCTO_DETALLE_PRIMA_MONEDA_SOLES);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_PRODUCTO_DETALLE_PRIMA_MONEDA_DOLARES);
			
			/*metacampos.add(ConstantesSolicitudPDF.CODIGO_PRODUCTO_DEVOLUCION_PRIMA_0);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_PRODUCTO_DEVOLUCION_PRIMA_50);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_PRODUCTO_DEVOLUCION_PRIMA_75);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_PRODUCTO_DEVOLUCION_PRIMA_100);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_PRODUCTO_DEVOLUCION_PRIMA_125);*/
			
			//Checks_Solicutud
			metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_TRATAMIENTO_CONTRATANTE_SI);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_TRATAMIENTO_CONTRATANTE_NO);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_TRATAMIENTO_ASEGURADO_SI);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_TRATAMIENTO_ASEGURADO_NO);
			
			//Checks_DPS
			metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PESO_VARIACION_SI);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PESO_VARIACION_NO);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PESO_AUMENTO_DESCUENTO_SI);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PESO_AUMENTO_DESCUENTO_NO);
			

			metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_FUMA_SI);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_FUMA_NO);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_DROGAS_SI);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_DROGAS_NO);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_ALCOHOL_SI);
			metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_ALCOHOL_NO);
			
			for (int i = 1; i <= 12; i++) {
				metacampos.add(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PREGUNTA_SI,i));
				metacampos.add(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PREGUNTA_NO,i));
			}
			
			IImageProvider check_ok = new FileImageProvider(ImagenOK, true);
			IImageProvider check_nok = new FileImageProvider(ImagenNOK, true);
			
			//metacampos.add("chk_prueba");
			
			Map<String, Object> metadatos = new HashMap<String, Object>();
			for(String campo :  metacampos ) {
				metadatos.put(campo, check_nok);
			}
			//Fin Configuración de Checks
			//metadatos.put("chk_prueba", check_ok);
			
			Map<String, Object> parametros = new HashMap<String, Object>();
			
			//#######################
			//Campos por defecto
			parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_FIRMA_CONTRATANTE, Strings.EMPTY);
			parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_FIRMA_ASEGURADO, Strings.EMPTY);
			parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_NOMBRES_COMPLETOS, Strings.EMPTY);
			parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_NOMBRES_COMPLETOS, Strings.EMPTY);
			parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_FECHA_SOLICITUD, Strings.EMPTY);
			parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_ESTATURA, Strings.EMPTY);
			parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PESO, Strings.EMPTY);
			parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PESO_AUMENTO_DESCUENTO_CANTIDAD, Strings.EMPTY);
			parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PESO_AUMENTO_DESCUENTO_MOTIVO, Strings.EMPTY);
			parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_FUMA_CANTIDAD, Strings.EMPTY);
			parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_FUMA_FRECUENCIA, Strings.EMPTY);
			parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_DROGAS_FECHA, Strings.EMPTY);
			parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_ALCOHOL_CANTIDAD, Strings.EMPTY);
			parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_ALCOHOL_FRECUENCIA, Strings.EMPTY);
			
			for (int i = 1; i <= 12; i++) {
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PREGUNTA_DETALLE, i), Strings.EMPTY);
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PREGUNTA_NUMERO, i), Strings.EMPTY);
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PREGUNTA_ENFERMEDAD, i), Strings.EMPTY);
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PREGUNTA_FECHA, i), Strings.EMPTY);
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PREGUNTA_CONDICION, i), Strings.EMPTY);
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PREGUNTA_MEDICO, i), Strings.EMPTY);
				
			}
			//#############################3
			
			
			
			//Datos de la solicitud
			parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_NUMERO_PROPUESTA, numeroPropuesta);
			metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_MONEDA_SOLES, check_ok);
			metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DESGLOSE_SOLES, check_ok);
			metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_PLAN_JUBILACION, check_ok);
			//parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_FECHA_SOLICITUD,  DateUtil.dateToString(hoy, DateUtil.FORMATO_DIA_DDMMYYYY));
			parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_FECHA_SOLICITUD,  "24/01/2020");

			parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_FIRMA_CONTRATANTE, "ALLISON MICHELLE LARA DIAZ");
			parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_FIRMA_ASEGURADO, "");
			
			
			//Datos del Asegurado
			parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_APELLIDO_PATERNO, "LARA");
			parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_APELLIDO_MATERNO, "DIAZ");
			parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_NOMBRES, "ALLISON MICHELLE");
			parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_NOMBRES_COMPLETOS, "ALLISON MICHELLE LARA DIAZ");
			parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_FNAC_DIA_1, "0");
			parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_FNAC_DIA_2, "5");
			parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_FNAC_MES_1, "0");
			parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_FNAC_MES_2, "5");
			parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_FNAC_ANIO_1, "1");
			parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_FNAC_ANIO_2, "9");
			parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_FNAC_ANIO_3, "9");
			parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_FNAC_ANIO_4, "4");
			metadatos.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_DOCUMENTO_TDOC_DNI, check_ok);
			// metadatos.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_DOCUMENTO_TDOC_RUC, check_ok);
			parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_DOCUMENTO_NUMERO, documentoIdentidad);
			
						
			//##################33
			//metadatos.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_SEXO_MASCULINO, check_ok);
			metadatos.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_SEXO_FEMENINO, check_ok);
			
			metadatos.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_ESTADO_CIVL_SOLTERO, check_ok);
			parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_NACIONALIDAD, "PERUANO");
			metadatos.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_DIRECCION_VIA_CALLE, check_ok);
			parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_DIRECCION, "CALLE METROPOLITANA 385 - CIACIACION PADRES DE FAMILIA ROSAS DE AMERICA - COMAS - LIMA - LIMA");
			parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_TELEFONO_CASA, Strings.EMPTY);
			parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_CELULAR, "971864005");
			parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_EMAIL, "ALLISONLARA63@GMAIL.COM");
			metadatos.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_PEP_NO, check_ok);
			metadatos.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_SUJETO_OBLIGADO_NO, check_ok);
			parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_CENTRO_LABORAL, "HOSPITAL BARTOLOME");
			parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_ACTIVIDAD_ECONOMICA, "ACTIVIDADES DE HOSPITALES");
			parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_PROFESION, "ABOGADO / LABORES ADMINISTRATIVAS EN OFICINA");
			parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_INGRESO_MONTO, "5,000");
			metadatos.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_INGRESO_MONEDA_SOLES, check_ok);
			parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_DIRECCION_LABORAL, "");
			
			//Datos del Contratante
			parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_VINCULO_ASEGURADO, "");
			parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_RAZON_SOCIAL, "");
			parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_APELLIDO_PATERNO, "");
			parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_APELLIDO_MATERNO, "");
			parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_NOMBRES, "");
			parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_NOMBRES_COMPLETOS, "ALLISON MICHELLE LARA DIAZ");
			parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_FNAC_DIA_1, "");
			parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_FNAC_DIA_2, "");
			parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_FNAC_MES_1, "");
			parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_FNAC_MES_2, "");
			parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_FNAC_ANIO_1, "");
			parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_FNAC_ANIO_2, "");
			parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_FNAC_ANIO_3, "");
			parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_FNAC_ANIO_4, "");
			//metadatos.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_DOCUMENTO_TDOC_RUC, check_ok);
			parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_DOCUMENTO_NUMERO, "");
			parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_DOCUMENTO_NUMERO_NOTA_COBERTURA, documentoContratante);
			
			//metadatos.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_SEXO_MASCULINO, check_ok);
			//metadatos.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_SEXO_FEMENINO, check_ok);
			
			
			//metadatos.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_ESTADO_CIVL_SOLTERO, check_ok);
			parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_NACIONALIDAD, "");
			//metadatos.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_DIRECCION_VIA_AVENIDA, check_ok);
			parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_DIRECCION, "");
			parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_TELEFONO_CASA, Strings.EMPTY);
			parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_CELULAR, "");
			parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_EMAIL, "");
			//metadatos.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_PEP_NO, check_ok);
			//metadatos.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_SUJETO_OBLIGADO_NO, check_ok);
			parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_CENTRO_LABORAL, "");
			parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_ACTIVIDAD_ECONOMICA, "");
			parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_PROFESION, "");
			parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_INGRESO_MONTO, "");
			//metadatos.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_INGRESO_MONEDA_DOLARES, check_ok);
			parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_DIRECCION_LABORAL, "");
			
			//Datos del Producto
			metadatos.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_FONDO_GARANTIZADO_MONEDA_SOLES, check_ok);
			parametros.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_FONDO_GARANTIZADO_MONTO, "50,000");
			parametros.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_PERIODO_COBERTURA, "6");
			parametros.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_PERIODO_ANUALIDADES, "1");
			metadatos.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_FRECUENCIA_PAGO_PRIMA_MENSUAL, check_ok);
			//metadatos.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_FRECUENCIA_CUOTA_COMODIN, check_ok);
			metadatos.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_TIPO_RIESGO_FUMADOR_NO, check_ok);
			parametros.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_GARANTIZADO_PRIMA_COMERCIAL_ANUAL, "7,576.91");
			parametros.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_GARANTIZADO_FACTOR_PAGO, "0.09");
			parametros.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_GARANTIZADO_PRIMA_COMERCIAL, "681.92");
			
			// metadatos.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_DEVOLUCION_PRIMA_0, check_ok);
			
			/*parametros.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_VF_PRIMA_COMERCIAL_ANUAL, "140.94");
			parametros.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_VF_FACTOR_PAGO, "140.94");
			parametros.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_VF_PRIMA_COMERCIAL, "140.94");
			parametros.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_VF_PRIMA_COMERCIAL_IGV, "140.94");
			parametros.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_VF_PRIMA_COMERCIAL_TOTAL, "140.94");*/
			
			//Seteando por defecto
			for (int i = 1; i <= 3; i++) {
				//Detalle de prima
				for (int j = 1; j <= 9; j++) {
					parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_PRODUCTO_COBERTURA_PRODUCTO, i , j), "");
					if(i==2) {
						
						if(j==1) {
							parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_PRODUCTO_COBERTURA_PRIMA, j), "7,477.55");
							parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_PRODUCTO_COBERTURA_PRODUCTO, i , j), "50,000.00");
						}
						if(j==2) {
							parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_PRODUCTO_COBERTURA_PRIMA, j), "19.16");
							parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_PRODUCTO_COBERTURA_PRODUCTO, i , j), "50,000.00");
						}
						if(j==3) {
							parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_PRODUCTO_COBERTURA_PRIMA, j), "48.98");
							parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_PRODUCTO_COBERTURA_PRODUCTO, i , j), "50,000.00");
						}
						if(j==4) {
							parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_PRODUCTO_COBERTURA_PRIMA, j), "31.23");
							parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_PRODUCTO_COBERTURA_PRODUCTO, i , j), "50,000.00");
						}
						if(j > 4) {
							parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_PRODUCTO_COBERTURA_PRIMA, j), Strings.EMPTY);
							parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_PRODUCTO_COBERTURA_PRODUCTO, i , j), Strings.EMPTY);
						}
					}
				}
				
				//Beneficiarios Principales
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_APELLIDO_PATERNO, i), Strings.EMPTY);
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_APELLIDO_MATERNO, i), Strings.EMPTY);
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_NOMBRES, i), Strings.EMPTY);
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_FECHA_NACIMIENTO, i), Strings.EMPTY);
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_DOCUMENTO, i), Strings.EMPTY);
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_PARENTESCO, i), Strings.EMPTY);
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_DISTRIBUCION, i), Strings.EMPTY);
				
				//Beneficiarios Adicionales
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_ADICIONAL_APELLIDO_PATERNO, i), Strings.EMPTY);
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_ADICIONAL_APELLIDO_MATERNO, i), Strings.EMPTY);
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_ADICIONAL_NOMBRES, i), Strings.EMPTY);
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_ADICIONAL_FECHA_NACIMIENTO, i), Strings.EMPTY);
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_ADICIONAL_DOCUMENTO, i), Strings.EMPTY);
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_ADICIONAL_PARENTESCO, i), Strings.EMPTY);
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_ADICIONAL_DISTRIBUCION, i), Strings.EMPTY);
			}
			
			
			//Seteando valores originales
			//Detalle Prima
			/*for (int i = 1; i <= 9; i++) {
				codigoSubPlan = "3";
				Float monto = 25000F*i;
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_PRODUCTO_COBERTURA_PRODUCTO, codigoSubPlan, i), monto.toString());
				
				Float prima = 25000F*i*2;				
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_PRODUCTO_COBERTURA_PRIMA, i), prima);
			}*/
			
			//Detalle Beneficiarios Principal
			//for (int i = 1; i <= 3; i++) {
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_APELLIDO_PATERNO, 1), "LARA");
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_APELLIDO_MATERNO, 1), "DIAZ");
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_NOMBRES, 1), "JESUS DANIEL");
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_FECHA_NACIMIENTO, 1), "05/04/1996");
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_DOCUMENTO, 1), "DNI 75610855");
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_PARENTESCO, 1), "HERMANO/A");
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_DISTRIBUCION, 1), "70%");
				
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_APELLIDO_PATERNO, 2), "LARA");
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_APELLIDO_MATERNO, 2), "DIAZ");
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_NOMBRES, 2), "VICTOR SEBASTIAN");
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_FECHA_NACIMIENTO, 2), "04/05/2002");
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_DOCUMENTO, 2), "DNI 74772490");
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_PARENTESCO, 2), "HERMANO/A");
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_DISTRIBUCION, 2), "30%");
			//}
			
			//Detalle Beneficiarios Adicional
			/*for (int i = 1; i <= 2; i++) {
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_ADICIONAL_APELLIDO_PATERNO, i), "LARA");
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_ADICIONAL_APELLIDO_MATERNO, i), "DIAZ");
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_ADICIONAL_NOMBRES, i), "VICTOR SEBASTIAN");
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_ADICIONAL_FECHA_NACIMIENTO, i), "04/05/2002");
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_ADICIONAL_DOCUMENTO, i), "74772490");
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_ADICIONAL_PARENTESCO, i), "HERMANO/A");
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_ADICIONAL_DISTRIBUCION, i), "30%");
			}*/
			
			
			parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_NOTA_COBERTURA, "");
			parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_AGENTE_NOMBRES, "Milagritos Sanchez Villegas");
			parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_AGENTE_CODIGO, "15937");
			parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_AGENTE_AGENCIA, "");
			parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_AGENTE_ORIGEN_VENTA, "");
			
			metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_TRATAMIENTO_CONTRATANTE_SI, check_ok);
			//metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_TRATAMIENTO_ASEGURADO_NO, check_ok);
			
			
			//DPS
			parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_ESTATURA, "155");
			parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PESO, "57.0");
			metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PESO_VARIACION_NO, check_ok);
			//metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PESO_AUMENTO_DESCUENTO_SI,check_ok);
			metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_FUMA_NO, check_ok);
			
			metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_DROGAS_NO, check_ok);
			
			metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_ALCOHOL_NO, check_ok);
			
			for (int i = 1; i <= 12; i++) {
				metadatos.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PREGUNTA_NO, i), check_ok);
				/*parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PREGUNTA_DETALLE, i), "DETALLE =>" + i);
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PREGUNTA_NUMERO, i), i+")");
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PREGUNTA_ENFERMEDAD, i), "ENFERMEDAD =>" + i);
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PREGUNTA_FECHA, i), "0"+i+"/09/2019");
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PREGUNTA_CONDICION, i), "CONDICION =>" + i);
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PREGUNTA_MEDICO, i), "MEDICO =>" + i);*/
				
			}
			
			parametros.put("fecha","24/01/2020" );
			parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_FECHA_SOLICITUD_DIA, "24");
			parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_FECHA_SOLICITUD_MES, "Enero");
			parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_FECHA_SOLICITUD_ANIO, "2020");
			
			nombreArchivo = IXDocReportUtil.generarDocumento(
				plantilla, 
				nombreArchivo, 
				parametros, 
				true,
				metacampos,
				metadatos
			);
			
			File archivoGenerado = new File( 
				System.getProperty("java.io.tmpdir").concat(File.separator).concat(nombreArchivo)
			);
			
			if(!archivoGenerado.exists()) {
				System.out.println("Error no se genero el archivo");
			}else {
				System.out.println("archivo generado=>"+archivoGenerado.getAbsoluteFile());

			}
		}catch (Exception ex) {
			System.out.println("Error generar solicitud"+ex);
		}
	}

	@Test
	public void validarListaFiltroVacioTest() {
		SolicitudFiltroRequestDTO request = new SolicitudFiltroRequestDTO();
		
		SolicitudResponseDTO solicitudes = solicitudService.lista(request);
		assertNotNull(solicitudes);
		assertFalse(solicitudes.getLista().isEmpty());
	}


	@Test
	public void downloadTest() throws FileNotFoundException, IOException {
		String temp = solicitudService.generateTemp((long) 1);
		System.out.println("Temporal:"+temp);
		assertNotNull(temp);
	}	
	


	@Test
	public void sendSMSTest()  {
		SolicitudSMSRequestDTO dto = new SolicitudSMSRequestDTO();
		dto.setIdSolicitud(5L);
		dto.setIdPersona(173L);
		
		boolean result = solicitudService.enviarSMS(dto);
			
		assertTrue(result);
	}	

	@Test
	public void validarSMSTest()  {
		SolicitudValidarCodigoRequestDTO dto = new SolicitudValidarCodigoRequestDTO();
		
		dto.setIdSolicitud(5L);
		dto.setCodigoAsegurado("YR413E");
		dto.setUsuarioLogin("equisper");
		
		
		BaseResponseDTO result = solicitudService.validarCodigo(dto);
			
		assertEquals(result.getCodigoRespuesta(), "01");
	}	
}
