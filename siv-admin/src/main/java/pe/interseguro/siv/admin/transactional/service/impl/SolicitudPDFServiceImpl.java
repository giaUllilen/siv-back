package pe.interseguro.siv.admin.transactional.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;

import fr.opensagres.xdocreport.document.images.FileImageProvider;
import fr.opensagres.xdocreport.document.images.IImageProvider;
import pe.interseguro.siv.admin.transactional.service.SolicitudPDFService;
import pe.interseguro.siv.common.bean.SolicitudPDFBean;
import pe.interseguro.siv.common.bean.SolicitudPDFBeneficiarioBean;
import pe.interseguro.siv.common.bean.SolicitudPDFDPSBean;
import pe.interseguro.siv.common.bean.SolicitudPDFDPSPreguntaBean;
import pe.interseguro.siv.common.bean.SolicitudPDFPersonaBean;
import pe.interseguro.siv.common.bean.SolicitudPDFProductoBean;
import pe.interseguro.siv.common.bean.SolicitudPDFProductoDetalleBean;
import pe.interseguro.siv.common.dto.response.BaseResponseDTO;
import pe.interseguro.siv.common.enums.IndicadorConsentimiento;
import pe.interseguro.siv.common.enums.TablaEnum;
import pe.interseguro.siv.common.enums.TipoDocumentoADN;
import pe.interseguro.siv.common.exception.SivSOAException;
import pe.interseguro.siv.common.exception.SivTXException;
import pe.interseguro.siv.common.persistence.db.mysql.domain.CodigoVerificacion;
import pe.interseguro.siv.common.persistence.db.mysql.domain.Multitabla;
import pe.interseguro.siv.common.persistence.db.mysql.domain.Persona;
import pe.interseguro.siv.common.persistence.db.mysql.domain.Solicitud;
import pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudBeneficiario;
import pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudDatosAdicionales;
import pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudDps;
import pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudDpsPregunta;
import pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudProducto;
import pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudProductoDetalle;
import pe.interseguro.siv.common.persistence.db.mysql.repository.CodigoVerificacionRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.MultitablaRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.PersonaRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.SolicitudBeneficiarioRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.SolicitudDatosAdicionalesRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.SolicitudDpsPreguntaRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.SolicitudDpsRepositorioRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.SolicitudProductoDetalleRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.SolicitudProductoRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.SolicitudRepository;
import pe.interseguro.siv.common.persistence.rest.consentimiento.ConsentimientoUniversalRequest;
import pe.interseguro.siv.common.persistence.rest.crm.CrmRestClient;
import pe.interseguro.siv.common.persistence.rest.global.GlobalRestClient;
import pe.interseguro.siv.common.persistence.rest.global.request.UploadArchivoRequest;
import pe.interseguro.siv.common.persistence.rest.global.response.UploadArchivoResponse;
import pe.interseguro.siv.common.persistence.rest.interseguro.InterseguroRestClient;
import pe.interseguro.siv.common.persistence.rest.interseguro.dto.Remitente;
import pe.interseguro.siv.common.persistence.rest.interseguro.request.EnviarCorreoRequest;
import pe.interseguro.siv.common.persistence.rest.interseguro.request.EnviarCorreoRequestNuevo;
import pe.interseguro.siv.common.util.Constantes;
import pe.interseguro.siv.common.util.ConstantesSolicitudPDF;
import pe.interseguro.siv.common.util.DateUtil;
import pe.interseguro.siv.common.util.IXDocReportUtil;
import pe.interseguro.siv.common.util.Utilitarios;

@Service("solicitudPDFService")
public class SolicitudPDFServiceImpl implements SolicitudPDFService {
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MessageSource messageSource;
	
	private Gson gson = new Gson();

	@Autowired
	private CodigoVerificacionRepository codigoVerificacionRepository;
	
	@Autowired
	private MultitablaRepository multitablaRepository;
	
	@Autowired
	private SolicitudRepository solicitudRepository;
	
	@Autowired
	private SolicitudBeneficiarioRepository solicitudBeneficiarioRepository;
	
	@Autowired
	private SolicitudDpsRepositorioRepository solicitudDpsRepositorioRepository;

	@Autowired
	private SolicitudDatosAdicionalesRepository solicitudDatosAdicionalesRepository;
	
	@Autowired
	private SolicitudDpsPreguntaRepository solicitudDpsPreguntaRepository;
	
	@Autowired
	private SolicitudProductoRepository solicitudProductoRepository;
	
	@Autowired
	private SolicitudProductoDetalleRepository solicitudProductoDetalleRepository;
	
	@Autowired
	private PersonaRepository personaRepository;
	
	@Autowired
	private InterseguroRestClient interseguroRestClient;
	
	@Autowired
	private CrmRestClient crmRestClient;
	
	@Autowired
	private GlobalRestClient globalRestClient;
	
	@Value("#{ environment['ruta.plantilla.doc.linux'] }")
	private String rutaPlantillaDocLinux;
	
	@Value("${spring.profiles.active}")
	private String activeProfile;

    @Value("#{ environment['persistence.rest.client.interseguro.correo.enviarCorreo.remitente'] }")
	private String enviarCorreoRemitente;

    @Value("#{ environment['persistence.rest.client.interseguro.correo.enviarCorreo.remitente.display'] }")
	private String enviarCorreoRemitenteDisplay;    

    @Value("#{ environment['persistence.rest.client.interseguro.correo.enviarCorreo.destinatario'] }")
	private String enviarCorreoDestinatario;
    
    @Value("#{ environment['ruta.pdf.solicitud.privada'] }")
	private String rutaPDFSolicitudPrivada;
    
    @Value("#{ environment['ruta.pdf.solicitud.publica'] }")
	private String rutaPDFSolicitudPublica;
    
    @Value("#{ environment['ruta.pdf.privada'] }")
	private String rutaPDFSolicitudFilePublica;
    
	@Override
	public BaseResponseDTO generarSolicitudPDF(Long idSolicitud, String tipoProducto) {
		
		String codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_EXITO;
		String mensajeRespuesta = Strings.EMPTY;
		
		BaseResponseDTO respuesta = new BaseResponseDTO();
		
		try {
			/**
			 * 1. Buscar Solicitud y validar estado
			 * *
			 */
			Solicitud solicitud = new Solicitud();
			solicitud = solicitudRepository.findById(idSolicitud).get();
			LOGGER.info("Generacion del documento solicitud-nroPropuesta -->  " + solicitud.getNumeroCotizacion() + " ==> Inicio"); 
			if(solicitud != null && solicitud.getDocumentoSolicitud().equals(Constantes.CODIGO_SOLICITUD_POR_TRANSMITIR)  || solicitud != null && solicitud.getEstado().equals(Constantes.CODIGO_SOLICITUD_PAGO_SAMP)) {
			/**
			 * 2. Obtener todos los datos de BD para el PDF (solicitud, asegurado, contratante, producto, dps y beneficiarios)
			 * *
			 */
				LOGGER.info("Generacion del documento solicitud-nroPropuesta -->  " + solicitud.getNumeroCotizacion() + " ==> Obtener Plantilla"); 
				SolicitudPDFBean solicitudPDFBean = this.obtenerDatosSolicitudPlantilla(solicitud, tipoProducto);
				LOGGER.info("Generacion del documento solicitud-nroPropuesta -->  " + solicitud.getNumeroCotizacion() + " ==> Fin al obtener Plantilla"); 
				/**
				 * 3. Crear PDF en temporales
				 * *
				 */
				LOGGER.info("Generacion del documento solicitud-nroPropuesta -->  " + solicitud.getNumeroCotizacion() + " ==> Generar PDF "); 
				File archivoPDFTemporal = this.generarPDFTemporal(idSolicitud,solicitudPDFBean);
				LOGGER.info("Generacion del documento solicitud-nroPropuesta -->  " + solicitud.getNumeroCotizacion() + " ==> Fin al Generar PDF "); 
				
				if( !archivoPDFTemporal.exists() ) {
					this.enviarCorreoError(
						solicitudPDFBean, 
						"SivTXException", 
						"[SolicitudPDFServiceImpl.generarSolicitudPDF] Error al generar Solicitud Digital PDF", 
						Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_SOLICITUD_DOCUMENTO_PDF_ERROR),"ERROR",solicitud.getNumeroCotizacion(),""
					);
					throw new SivTXException(
						Utilitarios.obtenerMensaje(
							messageSource, new Object[] {Constantes.MENSAJE_ADMINISTRADOR}, Constantes.MENSAJE_RESPUESTA_GENERAL_ERR0R
						), 
						null
					);			
				}else {
					
					Persona personaAsegurado = personaRepository.findById(solicitud.getPersonaByIdAsegurado().getIdPersona()).get();
					
					//Grabar solicitud PDFS
					StringBuilder sb = new StringBuilder();
					sb.append("SolicitudDigital_");
					sb.append(personaAsegurado.getNumeroDocumento());
					sb.append("_");
					sb.append(solicitud.getNumeroPropuesta());
					sb.append(".pdf");
					
					String nombreArchivoSolicitud = sb.toString();
					//modificar ruta
					//String urlPrivadaSolicitud = "C:\\Users\\NB\\Documents\\interseguro\\" + nombreArchivoSolicitud;
					//String urlPublicaSolicitud = "C:\\Users\\NB\\Documents\\interseguro\\" + nombreArchivoSolicitud;
					
					String urlPrivadaSolicitud = nombreArchivoSolicitud;
					String urlPublicaSolicitud = nombreArchivoSolicitud;
										
//					Files.copy(archivoPDFTemporal.toPath(), (new File(urlPrivadaSolicitud)).toPath(), StandardCopyOption.REPLACE_EXISTING);
					//copiando a temporal 
					
					String solicitudFilePublica = nombreArchivoSolicitud ;
//					Files.copy(archivoPDFTemporal.toPath(), (new File(solicitudFilePublica)).toPath(), StandardCopyOption.REPLACE_EXISTING);
					
					LOGGER.info("nombreSolicitud=>"+nombreArchivoSolicitud);
					LOGGER.info("SolicituDigital PDF copiado a=>"+urlPrivadaSolicitud); //esta ruta es la que esta enviando al crm
					
					solicitudRepository.updateNombreArchivoSolicitud(nombreArchivoSolicitud,idSolicitud);
					
					String tipoDocumentoCRM = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_TIPO_DOCUMENTO, personaAsegurado.getTipoDocumento(), Constantes.MULTITABLA_COLUMNA_VALOR_CRM);
					String numeroDocumento = personaAsegurado.getNumeroDocumento();
					String tipoDocumento = personaAsegurado.getTipoDocumento();
//					this.enviarPDFCRM(urlPrivadaSolicitud, tipoDocumentoCRM, numeroDocumento, nombreArchivoSolicitud, nombreArchivoSolicitud, tipoDocumento);
					
					LOGGER.info("se renombra el archivo temporal en JAVA java.io.tmpdir: " + System.getProperty("java.io.tmpdir"));
		            File solicitudPDFTemporal = new File(System.getProperty("java.io.tmpdir") + "/" + new File(nombreArchivoSolicitud));
		            archivoPDFTemporal.renameTo(solicitudPDFTemporal);
					this.enviarPDFCRM2(solicitudPDFTemporal, tipoDocumentoCRM, numeroDocumento, nombreArchivoSolicitud, nombreArchivoSolicitud, tipoDocumento);
					
 					eliminarFilePrivado(String.valueOf(solicitudPDFTemporal));
				}
				
				
			}else {
				String mensaje = Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_SOLICITUD_NO_FIRMADA);
				throw new SivSOAException(mensaje, null);
			}
			
			
			/*
			 * 1. Buscar Solicitud y validar estado
			 * 2. Obtener todos los de BD para el PDF (solicitud, asegurado, contratante, producto, dps y beneficiarios)
			 * 3. Obtener plantilla word e imagenes
			 * 4. Setear Valores al Mapa
			 * 5. Crear PDF en temporales
			 * 6. Copiar PDF en ruta protegida
			 * 7. Guardar nombre del archivo en la BD
			 * 8. Enviar PDF al CRM
			 * */

			
		}catch(Exception ex) {
			LOGGER.error("Error generarSolicitudPDF=>"+ex.getMessage(),ex);
			codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_ERROR;
			mensajeRespuesta = "Error al crear PDF Solicitud";
		}
		LOGGER.info("codigoRespuesta=>"+codigoRespuesta);
		
		respuesta.setCodigoRespuesta(codigoRespuesta);
		respuesta.setMensajeRespuesta(mensajeRespuesta);
		return respuesta;
	}
	
	private SolicitudPDFBean obtenerDatosSolicitudPlantilla(Solicitud solicitud, String tipoProducto) throws Exception {
		SolicitudPDFBean beanSolicitudPDF = new SolicitudPDFBean();
		String firmaContratante = Strings.EMPTY;
		String firmaAsegurado = Strings.EMPTY;
		Persona asegurado = new Persona();
		
		beanSolicitudPDF.setTipoProducto(tipoProducto);
		beanSolicitudPDF.setNumeroPropuesta(solicitud.getNumeroPropuesta());
		beanSolicitudPDF.setFechSolicitud(DateUtil.dateToString(solicitud.getFechaSolicitud(), DateUtil.FORMATO_DIA_DDMMYYYY));
		beanSolicitudPDF.setCheckSoles(this.checkOpcion(solicitud.getMoneda(), ConstantesSolicitudPDF.MULTITABLA_MONEDA_SOLES));
		beanSolicitudPDF.setCheckDolares(this.checkOpcion(solicitud.getMoneda(), ConstantesSolicitudPDF.MULTITABLA_MONEDA_DOLARES));
		LOGGER.info("Generacion del documento solicitud-nroPropuesta -->  " + solicitud.getNumeroCotizacion() + " ==> Producto - " + tipoProducto); 
		if ("VidaFree".equals(tipoProducto)) {
			beanSolicitudPDF.setCheckSubPlanVidaFree(this.checkOpcion(solicitud.getSubplan(), ConstantesSolicitudPDF.MULTITABLA_SUBPLAN_VIDA_FREE));
			beanSolicitudPDF.setCheckSubPlanVidaFreePlus(this.checkOpcion(solicitud.getSubplan(), ConstantesSolicitudPDF.MULTITABLA_SUBPLAN_VIDA_FREE_PLUS));
			beanSolicitudPDF.setCheckSubPlanVidaFreeTotal(this.checkOpcion(solicitud.getSubplan(), ConstantesSolicitudPDF.MULTITABLA_SUBPLAN_VIDA_FREE_TOTAL));
		} else {
			beanSolicitudPDF.setCheckSubPlanEducacion(this.checkOpcion(solicitud.getSubplan(), ConstantesSolicitudPDF.MULTITABLA_SUBPLAN_EDUCACION));
			beanSolicitudPDF.setCheckSubPlanJubilacion(this.checkOpcion(solicitud.getSubplan(), ConstantesSolicitudPDF.MULTITABLA_SUBPLAN_JUBILACION));
			beanSolicitudPDF.setCheckSubPlanSuenio(this.checkOpcion(solicitud.getSubplan(), ConstantesSolicitudPDF.MULTITABLA_SUBPLAN_SUENIO));
		}
		
		if(solicitud.getPersonaByIdAsegurado() != null) {
			LOGGER.info("Generacion del documento solicitud-nroPropuesta -->  " + solicitud.getNumeroCotizacion() + " ==> getPersonaByIdAsegurado " ); 
			asegurado = personaRepository.findById(solicitud.getPersonaByIdAsegurado().getIdPersona()).get();
			solicitud.setPersonaByIdAsegurado(asegurado);
			firmaAsegurado = this.obtenerCodigoFirma(solicitud.getIdSolicitud(), solicitud.getPersonaByIdAsegurado().getIdPersona());
		}
		
		if(solicitud.getPersonaByIdContratante() != null) {
			LOGGER.info("Generacion del documento solicitud-nroPropuesta -->  " + solicitud.getNumeroCotizacion() + " ==> getPersonaByIdContratante " ); 
			solicitud.setPersonaByIdContratante(personaRepository.findById(solicitud.getPersonaByIdContratante().getIdPersona()).get());
			firmaContratante = this.obtenerCodigoFirma(solicitud.getIdSolicitud(), solicitud.getPersonaByIdContratante().getIdPersona());
		}
		
		beanSolicitudPDF.setVinculoAsegurado(this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_TIPO_RELACION, solicitud.getVinculoAsegurado(),Constantes.MULTITABLA_COLUMNA_VALOR).toUpperCase());
		
		beanSolicitudPDF.setAsegurado(this.completarPersonaBean(solicitud.getIdSolicitud(), solicitud.getPersonaByIdAsegurado(),1));
		beanSolicitudPDF.setContratante(this.completarPersonaBean(solicitud.getIdSolicitud(), solicitud.getPersonaByIdContratante(),0));
		beanSolicitudPDF.setProducto(this.completarProductoBean(solicitud, solicitudProductoRepository.findByIdSolicitud(solicitud.getIdSolicitud()), solicitudProductoDetalleRepository.findByIdSolicitud(solicitud.getIdSolicitud())));
		beanSolicitudPDF.setBeneficiarios(this.completarBeneficiariosBean(solicitudBeneficiarioRepository.findByIdSolicitud(solicitud.getIdSolicitud())));
		beanSolicitudPDF.setDps(this.completarDSPBean(solicitudDpsRepositorioRepository.findByIdSolicitud(solicitud.getIdSolicitud()), solicitudDpsPreguntaRepository.findByIdSolicitud(solicitud.getIdSolicitud()),asegurado));


		beanSolicitudPDF.setAgenteCodigo(this.valorString(solicitud.getAgenteNumVendedor()));
		beanSolicitudPDF.setAgenteNombres(this.valorString(solicitud.getAgenteNombres()));
		beanSolicitudPDF.setAgenteAgencia(Strings.EMPTY);
		
		beanSolicitudPDF.setAseguradoIgualContratante(solicitud.getAseguradoIgualContratante());
		String idUsuario = this.valorString(solicitud.getIdCrmUsuario());
		//FIRMAS
		if(solicitud.getAseguradoIgualContratante().equals(Constantes.SOLICITUD_ASEGURADO_IGUAL_CONTRATANTE_TRUE)) {
			LOGGER.info("Generacion del documento solicitud-nroPropuesta -->  " + solicitud.getNumeroCotizacion() + " ==> FIRMAS - getAseguradoIgualContratante " );
			firmaContratante = firmaAsegurado;
			firmaAsegurado = Strings.EMPTY;
			boolean tieneConsentimientoOpcional = this.hasConsentimientoOpcional(asegurado, idUsuario);
			//El consentimiendo del asegurado que se muestre en el contratante
			beanSolicitudPDF.setCheckTratamientoContratanteSi(this.checkOpcion(solicitud.getTratamientoAseguradoCheck().toString(), tieneConsentimientoOpcional ? ConstantesSolicitudPDF.MULTITABLA_CHECK_TRUE : ConstantesSolicitudPDF.MULTITABLA_CHECK_FALSE));
			beanSolicitudPDF.setCheckTratamientoContratanteNo(this.checkOpcion(solicitud.getTratamientoAseguradoCheck().toString(), tieneConsentimientoOpcional ? ConstantesSolicitudPDF.MULTITABLA_CHECK_FALSE : ConstantesSolicitudPDF.MULTITABLA_CHECK_TRUE));
			
		}else {
			LOGGER.info("Generacion del documento solicitud-nroPropuesta -->  " + solicitud.getNumeroCotizacion() + " ==> FIRMAS - getPersonaByIdAsegurado " );
			boolean aseguradoTieneConsentimientoOpcional = this.hasConsentimientoOpcional(asegurado, idUsuario);
			beanSolicitudPDF.setCheckTratamientoAseguradoSi(this.checkOpcion(solicitud.getTratamientoAseguradoCheck().toString(), aseguradoTieneConsentimientoOpcional ? ConstantesSolicitudPDF.MULTITABLA_CHECK_TRUE : ConstantesSolicitudPDF.MULTITABLA_CHECK_FALSE));
			beanSolicitudPDF.setCheckTratamientoAseguradoNo(this.checkOpcion(solicitud.getTratamientoAseguradoCheck().toString(), aseguradoTieneConsentimientoOpcional ? ConstantesSolicitudPDF.MULTITABLA_CHECK_FALSE : ConstantesSolicitudPDF.MULTITABLA_CHECK_TRUE));		
			
			boolean contratanteTieneConsentimientoOpcional = this.hasConsentimientoOpcional(solicitud.getPersonaByIdContratante(), idUsuario);
			beanSolicitudPDF.setCheckTratamientoContratanteSi(this.checkOpcion(solicitud.getTratamientoContratanteCheck().toString(), contratanteTieneConsentimientoOpcional ? ConstantesSolicitudPDF.MULTITABLA_CHECK_TRUE : ConstantesSolicitudPDF.MULTITABLA_CHECK_FALSE));
			beanSolicitudPDF.setCheckTratamientoContratanteNo(this.checkOpcion(solicitud.getTratamientoContratanteCheck().toString(), contratanteTieneConsentimientoOpcional ? ConstantesSolicitudPDF.MULTITABLA_CHECK_FALSE : ConstantesSolicitudPDF.MULTITABLA_CHECK_TRUE));
		}
		
		beanSolicitudPDF.setFirmaAsegurado(firmaAsegurado);
		beanSolicitudPDF.setFirmaContratante(firmaContratante);
		
		return beanSolicitudPDF;
	}
	
	private String obtenerCodigoFirma(Long idSolicitud, Long idPersona) {
		String codigoFirma = Strings.EMPTY;
		
		Persona persona = personaRepository.findById(idPersona).get();
		if(persona != null) {
			codigoFirma = Utilitarios.nombresCompletos(persona.getNombres(), persona.getApellidoPaterno(), persona.getApellidoMaterno(), persona.getRazonSocial());
		}
		
		/*
		CodigoVerificacion cv = codigoVerificacionRepository.findCodigoFirmado(idSolicitud, idPersona);
		if(cv != null && cv.getCodigo() != null) {
			codigoFirma = Utilitarios.decrypt(cv.getCodigo());
		}
		*/
		return codigoFirma;
	}
	
	private File generarPDFTemporal(Long idSolicitud, SolicitudPDFBean solicitudPDFBean) throws Exception {
		//String nombreArchivo = Strings.EMPTY;
		String nombreArchivo = "solicitud_generado";
		
		LOGGER.info("Plantilla seleccionada ====>>>" + solicitudPDFBean.getTipoProducto());
		String plantilla = "VidaFree".equals(solicitudPDFBean.getTipoProducto()) ? Constantes.PLANTILLA_SOLICITUD_VF : Constantes.PLANTILLA_SOLICITUD_PG;
		LOGGER.info(plantilla);
		File plantillaSolicitud = this.obtenerPlantilla(plantilla);
		File imagenOK = this.obtenerPlantilla(Constantes.PLANTILLA_SOLICITUD_IMAGE_COMPLETE);
		File imagenNOK = this.obtenerPlantilla(Constantes.PLANTILLA_SOLICITUD_IMAGE_INCOMPLETE);
		
		Map<String, Object> parametros = this.valoresParametrosDefault();
		List<String> metacampos = this.valoresMetacamposDefault();
		Map<String, Object> metadatos = this.valoresMetaDatosDefault(metacampos, imagenNOK);
		
		parametros = this.completarParametros(idSolicitud, solicitudPDFBean, parametros);
		metadatos = this.completarMetadatos(idSolicitud, solicitudPDFBean, metadatos, imagenOK);
		
		nombreArchivo = IXDocReportUtil.generarDocumento(
				plantillaSolicitud, 
				nombreArchivo,
				parametros,
				true,
				metacampos,
				metadatos
		);
		
		System.out.println(gson.toJson(nombreArchivo));
		
		File archivo = new File( 
				System.getProperty("java.io.tmpdir").concat(File.separator).concat(nombreArchivo)
		);

		return archivo;
	}
	
	private Map<String, Object> completarMetadatos(Long idSolicitud, SolicitudPDFBean solicitudPDFBean, Map<String, Object> metadatos, File imagenOK){
		
		IImageProvider checkOk = new FileImageProvider(imagenOK, true);

		SolicitudDatosAdicionales solicitudDatosAdicionales = solicitudDatosAdicionalesRepository.findByIdSolicitud(idSolicitud);
		
		if(solicitudPDFBean.isCheckSoles()) {
			metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_MONEDA_SOLES, checkOk);
			metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DESGLOSE_SOLES, checkOk);
		}
		if(solicitudPDFBean.isCheckDolares()) {
			metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_MONEDA_DOLARES, checkOk);
			metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DESGLOSE_DOLARES, checkOk);
		}
		
		if ("VidaFree".equals(solicitudPDFBean.getTipoProducto())) {
			if(solicitudPDFBean.isCheckSubPlanVidaFree()) metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_VIDA_FREE, checkOk);
			if(solicitudPDFBean.isCheckSubPlanVidaFreePlus()) metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_VIDA_FREE_PLUS, checkOk);
			if(solicitudPDFBean.isCheckSubPlanVidaFreeTotal()) metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_VIDA_FREE_TOTAL, checkOk);
		} else {
			if(solicitudPDFBean.isCheckSubPlanEducacion()) metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_PLAN_EDUCACION, checkOk);
			if(solicitudPDFBean.isCheckSubPlanSuenio()) metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_PLAN_SUENIO, checkOk);
			if(solicitudPDFBean.isCheckSubPlanJubilacion()) metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_PLAN_JUBILACION, checkOk);
		}
		
		if(solicitudPDFBean.getAsegurado() != null) {
			if(solicitudPDFBean.getAsegurado().isCheckDocumentoDNI()) metadatos.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_DOCUMENTO_TDOC_DNI, checkOk);
			if(solicitudPDFBean.getAsegurado().isCheckDocumentoCE()) metadatos.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_DOCUMENTO_TDOC_CE, checkOk);
			if(solicitudPDFBean.getAsegurado().isCheckDocumentoRUC()) metadatos.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_DOCUMENTO_TDOC_RUC, checkOk);
			
			if(solicitudPDFBean.getAsegurado().isCheckSexoMasculino()) {
				metadatos.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_SEXO_MASCULINO, checkOk);
			}
			if(solicitudPDFBean.getAsegurado().isCheckSexoFemenino()) {
				metadatos.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_SEXO_FEMENINO, checkOk);
			}
			
			if(solicitudPDFBean.getAsegurado().isCheckEstadoCivilSotero()) metadatos.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_ESTADO_CIVL_SOLTERO, checkOk);
			if(solicitudPDFBean.getAsegurado().isCheckEstadoCivilCasado()) metadatos.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_ESTADO_CIVL_CASADO, checkOk);
			if(solicitudPDFBean.getAsegurado().isCheckEstadoCivilViudo()) metadatos.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_ESTADO_CIVL_VIUDO, checkOk);
			if(solicitudPDFBean.getAsegurado().isCheckEstadoCivilDivorciado()) metadatos.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_ESTADO_CIVL_DIVORCIADO, checkOk);
			if(solicitudPDFBean.getAsegurado().isCheckEstadoCivilConviviente()) metadatos.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_ESTADO_CIVL_CONVIVIENTE, checkOk);
			if(solicitudPDFBean.getAsegurado().isCheckDireccionJR()) metadatos.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_DIRECCION_VIA_JIRON, checkOk);
			if(solicitudPDFBean.getAsegurado().isCheckDireccionAV()) metadatos.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_DIRECCION_VIA_AVENIDA, checkOk);
			if(solicitudPDFBean.getAsegurado().isCheckDireccionCA()) metadatos.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_DIRECCION_VIA_CALLE, checkOk);
			if(solicitudPDFBean.getAsegurado().isCheckDireccionPJ()) metadatos.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_DIRECCION_VIA_PASAJE, checkOk);

			Boolean isPepSi = solicitudDatosAdicionales != null ? (solicitudDatosAdicionales.getEsPepAsegurado() != null ? this.checkOpcion(solicitudDatosAdicionales.getEsPepAsegurado(), ConstantesSolicitudPDF.MULTITABLA_CHECK_CODIGO_TRUE) : solicitudPDFBean.getAsegurado().isCheckPEPSi()) : solicitudPDFBean.getAsegurado().isCheckPEPSi();
			Boolean isPepNo = solicitudDatosAdicionales != null ? (solicitudDatosAdicionales.getEsPepAsegurado() != null ? this.checkOpcion(solicitudDatosAdicionales.getEsPepAsegurado(), ConstantesSolicitudPDF.MULTITABLA_CHECK_CODIGO_FALSE) : solicitudPDFBean.getAsegurado().isCheckPEPNo()) : solicitudPDFBean.getAsegurado().isCheckPEPNo();

			if(isPepSi) {
				metadatos.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_PEP_SI, checkOk);
			}
			if(isPepNo) {
				metadatos.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_PEP_NO, checkOk);
			}

			Boolean isSujetoSi = solicitudDatosAdicionales != null ? (solicitudDatosAdicionales.getEsSujetoObligadoAsegurado() != null ? this.checkOpcion(solicitudDatosAdicionales.getEsSujetoObligadoAsegurado(), ConstantesSolicitudPDF.MULTITABLA_CHECK_CODIGO_TRUE) : solicitudPDFBean.getAsegurado().isCheckSujetoObligadoSi()) : solicitudPDFBean.getAsegurado().isCheckSujetoObligadoSi();
			Boolean isSujetoNo = solicitudDatosAdicionales != null ? (solicitudDatosAdicionales.getEsSujetoObligadoAsegurado() != null ? this.checkOpcion(solicitudDatosAdicionales.getEsSujetoObligadoAsegurado(), ConstantesSolicitudPDF.MULTITABLA_CHECK_CODIGO_FALSE) : solicitudPDFBean.getAsegurado().isCheckSujetoObligadoNo()) : solicitudPDFBean.getAsegurado().isCheckSujetoObligadoNo(); 

			if(isSujetoSi) {
				metadatos.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_SUJETO_OBLIGADO_SI, checkOk);
			}
			if(isSujetoNo) {
				metadatos.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_SUJETO_OBLIGADO_NO, checkOk);
			}
			if(solicitudPDFBean.getAsegurado().isCheckIngresoSoles()) metadatos.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_INGRESO_MONEDA_SOLES, checkOk);
			if(solicitudPDFBean.getAsegurado().isCheckIngresoDolares()) metadatos.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_INGRESO_MONEDA_DOLARES, checkOk);
			if(solicitudPDFBean.getAsegurado().isCheckFumadorSi()) {
				metadatos.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_FUMADOR_SI, checkOk);
			}			
			if(solicitudPDFBean.getAsegurado().isCheckFumadorNo()) {
				metadatos.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_FUMADOR_NO, checkOk);
			}			
		}
		
		if(solicitudPDFBean.getContratante() != null) {
			if(solicitudPDFBean.getContratante().isCheckDocumentoDNI()) metadatos.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_DOCUMENTO_TDOC_DNI, checkOk);
			if(solicitudPDFBean.getContratante().isCheckDocumentoCE()) metadatos.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_DOCUMENTO_TDOC_CE, checkOk);
			if(solicitudPDFBean.getContratante().isCheckDocumentoRUC()) metadatos.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_DOCUMENTO_TDOC_RUC, checkOk);
			
			if(!solicitudPDFBean.getContratante().isCheckDocumentoRUC()) {
				if(solicitudPDFBean.getContratante().isCheckSexoMasculino()) {
					metadatos.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_SEXO_MASCULINO, checkOk);
				}
				if(solicitudPDFBean.getContratante().isCheckSexoFemenino()) {
					metadatos.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_SEXO_FEMENINO, checkOk);
				}
			}
			
			if(solicitudPDFBean.getContratante().isCheckEstadoCivilSotero()) metadatos.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_ESTADO_CIVL_SOLTERO, checkOk);
			if(solicitudPDFBean.getContratante().isCheckEstadoCivilCasado()) metadatos.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_ESTADO_CIVL_CASADO, checkOk);
			if(solicitudPDFBean.getContratante().isCheckEstadoCivilViudo()) metadatos.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_ESTADO_CIVL_VIUDO, checkOk);
			if(solicitudPDFBean.getContratante().isCheckEstadoCivilDivorciado()) metadatos.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_ESTADO_CIVL_DIVORCIADO, checkOk);
			if(solicitudPDFBean.getContratante().isCheckEstadoCivilConviviente()) metadatos.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_ESTADO_CIVL_CONVIVIENTE, checkOk);
			if(solicitudPDFBean.getContratante().isCheckDireccionJR()) metadatos.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_DIRECCION_VIA_JIRON, checkOk);
			if(solicitudPDFBean.getContratante().isCheckDireccionAV()) metadatos.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_DIRECCION_VIA_AVENIDA, checkOk);
			if(solicitudPDFBean.getContratante().isCheckDireccionCA()) metadatos.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_DIRECCION_VIA_CALLE, checkOk);
			if(solicitudPDFBean.getContratante().isCheckDireccionPJ()) metadatos.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_DIRECCION_VIA_PASAJE, checkOk);
			
			Boolean isPepSi = solicitudDatosAdicionales != null ? (solicitudDatosAdicionales.getEsPepContratante() != null ? this.checkOpcion(solicitudDatosAdicionales.getEsPepContratante(), ConstantesSolicitudPDF.MULTITABLA_CHECK_CODIGO_TRUE) : solicitudPDFBean.getContratante().isCheckPEPSi()) : solicitudPDFBean.getContratante().isCheckPEPSi();
			Boolean isPepNo = solicitudDatosAdicionales != null ? (solicitudDatosAdicionales.getEsPepContratante() != null ? this.checkOpcion(solicitudDatosAdicionales.getEsPepContratante(), ConstantesSolicitudPDF.MULTITABLA_CHECK_CODIGO_FALSE) : solicitudPDFBean.getContratante().isCheckPEPNo()) : solicitudPDFBean.getContratante().isCheckPEPNo();

			if(isPepSi) {
				metadatos.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_PEP_SI, checkOk);
			}
			if(isPepNo) {
				metadatos.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_PEP_NO, checkOk);
			}

			Boolean isSujetoSi = solicitudDatosAdicionales != null ? (solicitudDatosAdicionales.getEsSujetoObligadoContratante() != null ? this.checkOpcion(solicitudDatosAdicionales.getEsSujetoObligadoContratante(), ConstantesSolicitudPDF.MULTITABLA_CHECK_CODIGO_TRUE) : solicitudPDFBean.getContratante().isCheckSujetoObligadoSi()) : solicitudPDFBean.getContratante().isCheckSujetoObligadoSi();
			Boolean isSujetoNo = solicitudDatosAdicionales != null ? (solicitudDatosAdicionales.getEsSujetoObligadoContratante() != null ? this.checkOpcion(solicitudDatosAdicionales.getEsSujetoObligadoContratante(), ConstantesSolicitudPDF.MULTITABLA_CHECK_CODIGO_FALSE) : solicitudPDFBean.getContratante().isCheckSujetoObligadoNo()) : solicitudPDFBean.getContratante().isCheckSujetoObligadoNo(); 

			if(isSujetoSi) {
				metadatos.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_SUJETO_OBLIGADO_SI, checkOk);
			}
			if(isSujetoNo) {
				metadatos.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_SUJETO_OBLIGADO_NO, checkOk);
			}
			if(solicitudPDFBean.getContratante().isCheckIngresoSoles()) metadatos.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_INGRESO_MONEDA_SOLES, checkOk);
			if(solicitudPDFBean.getContratante().isCheckIngresoDolares()) metadatos.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_INGRESO_MONEDA_DOLARES, checkOk);
			
		}
		
		if(solicitudPDFBean.getProducto() != null) {
			if(solicitudPDFBean.getProducto().isCheckMonedaFondoSoles()) {
				metadatos.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_FONDO_GARANTIZADO_MONEDA_SOLES, checkOk);
			}
			if(solicitudPDFBean.getProducto().isCheckMonedaFondoDolares()) {
				metadatos.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_FONDO_GARANTIZADO_MONEDA_DOLARES, checkOk);
			}
			
			if(solicitudPDFBean.getProducto().isCheckFrecuenciaMensual()) metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_FRECUENCIA_MENSUAL, checkOk);
			if(solicitudPDFBean.getProducto().isCheckFrecuenciaTrimestral()) metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_FRECUENCIA_TRIMESTRAL, checkOk);
			if(solicitudPDFBean.getProducto().isCheckFrecuenciaSemestral()) metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_FRECUENCIA_SEMESTRAL, checkOk);
			if(solicitudPDFBean.getProducto().isCheckFrecuenciaAnual()) metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_FRECUENCIA_ANUAL, checkOk);
			
			if(solicitudPDFBean.getProducto().isCheckDevolucionPrima0()) metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DEVOLUCION_0, checkOk);
			if(solicitudPDFBean.getProducto().isCheckDevolucionPrima50()) metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DEVOLUCION_50, checkOk);
			if(solicitudPDFBean.getProducto().isCheckDevolucionPrima75()) metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DEVOLUCION_75, checkOk);
			if(solicitudPDFBean.getProducto().isCheckDevolucionPrima100()) metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DEVOLUCION_100, checkOk);
			if(solicitudPDFBean.getProducto().isCheckDevolucionPrima125()) metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DEVOLUCION_125, checkOk);
			if(solicitudPDFBean.getProducto().isCheckDevolucionPrima130()) metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DEVOLUCION_130, checkOk);
			if(solicitudPDFBean.getProducto().isCheckDevolucionPrima135()) metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DEVOLUCION_135, checkOk);
			if(solicitudPDFBean.getProducto().isCheckDevolucionPrima140()) metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DEVOLUCION_140, checkOk);
			if(solicitudPDFBean.getProducto().isCheckDevolucionPrima145()) metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DEVOLUCION_145, checkOk);
			if(solicitudPDFBean.getProducto().isCheckDevolucionPrima150()) metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DEVOLUCION_150, checkOk);
			
			if(solicitudPDFBean.getProducto().isCheckFrecuenciaMensual()) metadatos.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_FRECUENCIA_PAGO_PRIMA_MENSUAL, checkOk);
			if(solicitudPDFBean.getProducto().isCheckFrecuenciaTrimestral()) metadatos.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_FRECUENCIA_PAGO_PRIMA_TRIMESTRAL, checkOk);
			if(solicitudPDFBean.getProducto().isCheckFrecuenciaSemestral()) metadatos.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_FRECUENCIA_PAGO_PRIMA_SEMESTRAL, checkOk);
			if(solicitudPDFBean.getProducto().isCheckFrecuenciaAnual()) metadatos.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_FRECUENCIA_PAGO_PRIMA_ANUAL, checkOk);
			if(solicitudPDFBean.getProducto().isCheckFrecuenciaUnica()) metadatos.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_FRECUENCIA_PAGO_PRIMA_UNICA, checkOk);
			if(solicitudPDFBean.getProducto().isCheckCuotaComodin()) {
				metadatos.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_FRECUENCIA_CUOTA_COMODIN, checkOk);
			}
			if(solicitudPDFBean.getProducto().isCheckCuotaDoble()) {
				metadatos.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_FRECUENCIA_CUOTA_DOBLE, checkOk);
			}
			if(solicitudPDFBean.getProducto().isCheckTipoRiesgoFumadorSi()) metadatos.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_TIPO_RIESGO_FUMADOR_SI, checkOk);
			if(solicitudPDFBean.getProducto().isCheckTipoRiesgoFumadorNo()) metadatos.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_TIPO_RIESGO_FUMADOR_NO, checkOk);
			if(solicitudPDFBean.getProducto().isCheckPrimaMonedaSoles()) {
				metadatos.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_DETALLE_PRIMA_MONEDA_SOLES, checkOk);
			}
			if(solicitudPDFBean.getProducto().isCheckPrimaMonedaDolares()) {
				metadatos.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_DETALLE_PRIMA_MONEDA_DOLARES, checkOk);
			}
			if(solicitudPDFBean.getProducto().isCheckTemporal()) {
				metadatos.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_PERIODO_TEMPORAL, checkOk);
			}
			if(solicitudPDFBean.getProducto().isCheckVitalicio()) {
				metadatos.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_PERIODO_VITALICIO, checkOk);
			}
		}
		
		if(solicitudPDFBean.getDps() != null) {
			
			if(solicitudPDFBean.getDps().isCheckVariacionSi()) metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PESO_VARIACION_SI, checkOk);
			if(solicitudPDFBean.getDps().isCheckVariacionNo()) metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PESO_VARIACION_NO, checkOk);
			if(solicitudPDFBean.getDps().isCheckPesoAumento()) {
				metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PESO_AUMENTO_DESCUENTO_SI, checkOk);
			}
			if(solicitudPDFBean.getDps().isCheckPesoDisminuyo()) {
				metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PESO_AUMENTO_DESCUENTO_NO, checkOk);
			}
			if(solicitudPDFBean.getDps().isCheckFumadorSi()) metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_FUMA_SI, checkOk);
			if(solicitudPDFBean.getDps().isCheckFumadorNo()) metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_FUMA_NO, checkOk);
			if(solicitudPDFBean.getDps().isCheckConsumeDrogasSi()) metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_DROGAS_SI, checkOk);
			if(solicitudPDFBean.getDps().isCheckConsumeDrogasNo()) metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_DROGAS_NO, checkOk);
			if(solicitudPDFBean.getDps().isCheckAlchoolSi()) metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_ALCOHOL_SI, checkOk);
			if(solicitudPDFBean.getDps().isCheckAlchoolNo()) metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_ALCOHOL_NO, checkOk);
			
			if(solicitudPDFBean.getDps().getPreguntas() != null) {
				for(SolicitudPDFDPSPreguntaBean detalle : solicitudPDFBean.getDps().getPreguntas()) {
					if(detalle.isCheckSi()) metadatos.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PREGUNTA_SI, detalle.getPregunta()), checkOk);
					if(detalle.isCheckNo()) metadatos.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PREGUNTA_NO, detalle.getPregunta()), checkOk);
				}
			}
		}
		
		if(solicitudPDFBean.isCheckTratamientoAseguradoSi()) {
			metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_TRATAMIENTO_ASEGURADO_SI, checkOk);
		}
		if(solicitudPDFBean.isCheckTratamientoAseguradoNo()) {
			metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_TRATAMIENTO_ASEGURADO_NO, checkOk);
		}
		
		if(solicitudPDFBean.isCheckTratamientoContratanteSi()) {
			metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_TRATAMIENTO_CONTRATANTE_SI, checkOk);
		}
		if(solicitudPDFBean.isCheckTratamientoContratanteNo()) {
			metadatos.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_TRATAMIENTO_CONTRATANTE_NO, checkOk);
		}
		
		
		return metadatos;
	}
	
	private Map<String, Object> completarParametros(Long idSolicitud, SolicitudPDFBean solicitudPDFBean, Map<String, Object> parametros){

		if (solicitudPDFBean != null) {
			String fechaSolicitud = solicitudPDFBean.getFechSolicitud();
			String fechaSolicitudDia = Constantes.ESPACIO_BLANCO;
			String fechaSolicitudMes = Constantes.ESPACIO_BLANCO;
			String fechaSolicitudAnio = Constantes.ESPACIO_BLANCO;
			
			parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_NUMERO_PROPUESTA, solicitudPDFBean.getNumeroPropuesta());
			parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_FECHA_SOLICITUD, fechaSolicitud);
			
			if(fechaSolicitud.length() == 10) {
				String[] arrayFecha = fechaSolicitud.split("/");
				fechaSolicitudDia = arrayFecha[0];
				fechaSolicitudMes = arrayFecha[1];
				fechaSolicitudAnio = arrayFecha[2];
			}
			
			parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_FECHA_SOLICITUD_DIA, fechaSolicitudDia);
			parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_FECHA_SOLICITUD_MES, Utilitarios.nombreMes(fechaSolicitudMes));
			parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_FECHA_SOLICITUD_ANIO, fechaSolicitudAnio);
			
			//Datos Asegurado

			
			if(solicitudPDFBean.getAsegurado() != null) {

				parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_APELLIDO_PATERNO, solicitudPDFBean.getAsegurado().getApellidoPaterno());
				parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_APELLIDO_MATERNO, solicitudPDFBean.getAsegurado().getApellidoMaterno());
				parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_NOMBRES, solicitudPDFBean.getAsegurado().getNombres());
				parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_NOMBRES_COMPLETOS, solicitudPDFBean.getAsegurado().getNombresCompletos());
				parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_FNAC_DIA_1, solicitudPDFBean.getAsegurado().getNacimientoDia1());
				parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_FNAC_DIA_2, solicitudPDFBean.getAsegurado().getNacimientoDia2());
				parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_FNAC_MES_1, solicitudPDFBean.getAsegurado().getNacimientoMes1());
				parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_FNAC_MES_2, solicitudPDFBean.getAsegurado().getNacimientoMes2());
				parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_FNAC_ANIO_1, solicitudPDFBean.getAsegurado().getNacimientoAnio1());
				parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_FNAC_ANIO_2, solicitudPDFBean.getAsegurado().getNacimientoAnio2());
				parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_FNAC_ANIO_3, solicitudPDFBean.getAsegurado().getNacimientoAnio3());
				parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_FNAC_ANIO_4, solicitudPDFBean.getAsegurado().getNacimientoAnio4());
				parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_DOCUMENTO_NUMERO, solicitudPDFBean.getAsegurado().getNumeroDocumento());
				parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_NACIONALIDAD, solicitudPDFBean.getAsegurado().getNacionalidad());
				parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_DIRECCION, solicitudPDFBean.getAsegurado().getDireccion());
				parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_CELULAR, solicitudPDFBean.getAsegurado().getCelular());
				parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_EMAIL, solicitudPDFBean.getAsegurado().getEmail());
				parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_CENTRO_LABORAL, solicitudPDFBean.getAsegurado().getCentroTrabajo());
				parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_ACTIVIDAD_ECONOMICA, solicitudPDFBean.getAsegurado().getActividadEconomica());
				parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_PROFESION, solicitudPDFBean.getAsegurado().getProfesion());
				parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_INGRESO_MONTO, solicitudPDFBean.getAsegurado().getIngresoMensual());
				parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_DIRECCION_LABORAL, solicitudPDFBean.getAsegurado().getDireccionLaboral());
				
				parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_NOMBRES_COMPLETOS, solicitudPDFBean.getAsegurado().getNombresCompletos());
				if (solicitudPDFBean.getAseguradoIgualContratante().equals(Constantes.SOLICITUD_ASEGURADO_IGUAL_CONTRATANTE_TRUE)) {
					parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_DOCUMENTO_NUMERO_NOTA_COBERTURA, solicitudPDFBean.getAsegurado().getNumeroDocumento());
				} else {
					parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_DOCUMENTO_NUMERO_NOTA_COBERTURA, solicitudPDFBean.getContratante().getNumeroDocumento());
				}
				
			}
			//Datos Contratante
			if(solicitudPDFBean.getContratante() != null) {

				parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_VINCULO_ASEGURADO, solicitudPDFBean.getVinculoAsegurado());
				parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_RAZON_SOCIAL, solicitudPDFBean.getContratante().getRazonSocial());
				parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_APELLIDO_PATERNO, solicitudPDFBean.getContratante().getApellidoPaterno());
				parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_APELLIDO_MATERNO, solicitudPDFBean.getContratante().getApellidoMaterno());
				parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_NOMBRES, solicitudPDFBean.getContratante().getNombres());
				parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_NOMBRES_COMPLETOS, solicitudPDFBean.getContratante().getNombresCompletos());
				parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_FNAC_DIA_1, solicitudPDFBean.getContratante().getNacimientoDia1());
				parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_FNAC_DIA_2, solicitudPDFBean.getContratante().getNacimientoDia2());
				parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_FNAC_MES_1, solicitudPDFBean.getContratante().getNacimientoMes1());
				parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_FNAC_MES_2, solicitudPDFBean.getContratante().getNacimientoMes2());
				parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_FNAC_ANIO_1, solicitudPDFBean.getContratante().getNacimientoAnio1());
				parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_FNAC_ANIO_2, solicitudPDFBean.getContratante().getNacimientoAnio2());
				parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_FNAC_ANIO_3, solicitudPDFBean.getContratante().getNacimientoAnio3());
				parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_FNAC_ANIO_4, solicitudPDFBean.getContratante().getNacimientoAnio4());
				parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_DOCUMENTO_NUMERO, solicitudPDFBean.getContratante().getNumeroDocumento());
				parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_NACIONALIDAD, solicitudPDFBean.getContratante().getNacionalidad());
				parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_DIRECCION, solicitudPDFBean.getContratante().getDireccion());
				parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_CELULAR, solicitudPDFBean.getContratante().getCelular());
				parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_EMAIL, solicitudPDFBean.getContratante().getEmail());
				parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_CENTRO_LABORAL, solicitudPDFBean.getContratante().getCentroTrabajo());
				parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_ACTIVIDAD_ECONOMICA, solicitudPDFBean.getContratante().getActividadEconomica());
				parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_PROFESION, solicitudPDFBean.getContratante().getProfesion());
				parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_INGRESO_MONTO, solicitudPDFBean.getContratante().getIngresoMensual());
				parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_DIRECCION_LABORAL, solicitudPDFBean.getContratante().getDireccionLaboral());
			}
			
			if(solicitudPDFBean.getProducto() != null) {
				parametros.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_FONDO_GARANTIZADO_MONTO, solicitudPDFBean.getProducto().getMontoFondoGarantizado());
				parametros.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_PERIODO_COBERTURA, solicitudPDFBean.getProducto().getPeriodoCoberturaAnual());
				parametros.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_PERIODO_ANUALIDADES, solicitudPDFBean.getProducto().getAnualidadPago());
				parametros.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_GARANTIZADO_PRIMA_COMERCIAL_ANUAL, solicitudPDFBean.getProducto().getPrimaComercialAnual());
				parametros.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_GARANTIZADO_FACTOR_PAGO, solicitudPDFBean.getProducto().getFactorPago());
				parametros.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_GARANTIZADO_PRIMA_COMERCIAL, solicitudPDFBean.getProducto().getPrimaComercial());
				
				parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DEVOLUCION_0, solicitudPDFBean.getProducto().isCheckDevolucionPrima0());
				parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DEVOLUCION_50, solicitudPDFBean.getProducto().isCheckDevolucionPrima50());
				parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DEVOLUCION_75, solicitudPDFBean.getProducto().isCheckDevolucionPrima75());
				parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DEVOLUCION_100, solicitudPDFBean.getProducto().isCheckDevolucionPrima100());
				parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DEVOLUCION_125, solicitudPDFBean.getProducto().isCheckDevolucionPrima125());
				parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DEVOLUCION_130, solicitudPDFBean.getProducto().isCheckDevolucionPrima130());
				parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DEVOLUCION_135, solicitudPDFBean.getProducto().isCheckDevolucionPrima135());
				parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DEVOLUCION_140, solicitudPDFBean.getProducto().isCheckDevolucionPrima140());
				parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DEVOLUCION_145, solicitudPDFBean.getProducto().isCheckDevolucionPrima145());
				parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DEVOLUCION_150, solicitudPDFBean.getProducto().isCheckDevolucionPrima150());
				
				parametros.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_VF_PRIMA_COMERCIAL_ANUAL, solicitudPDFBean.getProducto().getPrimaVidaFreePrimaComercialAnual());
				parametros.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_VF_FACTOR_PAGO, solicitudPDFBean.getProducto().getPrimaVidaFreeFactorPago());
				parametros.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_VF_PRIMA_COMERCIAL, solicitudPDFBean.getProducto().getPrimaVidaFreePrimaComercial());
				parametros.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_VF_PRIMA_COMERCIAL_IGV, solicitudPDFBean.getProducto().getPrimaVidaFreePrimaComercialIgv());
				parametros.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_VF_PRIMA_COMERCIAL_TOTAL, solicitudPDFBean.getProducto().getPrimaVidaFreePrimaComercialTotal());
				
				if(solicitudPDFBean.getProducto().getDetallePrimas() != null) {
					String subplan = ConstantesSolicitudPDF.MULTITABLA_SUBPLAN_EDUCACION;
					if ("VidaFree".equals(solicitudPDFBean.getTipoProducto())) {
						if(solicitudPDFBean.isCheckSubPlanVidaFree()) subplan = ConstantesSolicitudPDF.MULTITABLA_SUBPLAN_VIDA_FREE;
						if(solicitudPDFBean.isCheckSubPlanVidaFreePlus()) subplan = ConstantesSolicitudPDF.MULTITABLA_SUBPLAN_VIDA_FREE_PLUS;
						if(solicitudPDFBean.isCheckSubPlanVidaFreeTotal()) subplan = ConstantesSolicitudPDF.MULTITABLA_SUBPLAN_VIDA_FREE_TOTAL;
						
						for(SolicitudPDFProductoDetalleBean detalle : solicitudPDFBean.getProducto().getDetallePrimas()) {
							parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_PRODUCTO_COBERTURA_PRODUCTO, "1", detalle.getCobertura()), detalle.getCapitalAsegurado());
							parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_PRODUCTO_COBERTURA_PRIMA, detalle.getCobertura()), detalle.getPrimaAnual());
						}
					} else {
						if(solicitudPDFBean.isCheckSubPlanEducacion()) subplan = ConstantesSolicitudPDF.MULTITABLA_SUBPLAN_EDUCACION;
						if(solicitudPDFBean.isCheckSubPlanSuenio()) subplan = ConstantesSolicitudPDF.MULTITABLA_SUBPLAN_SUENIO;
						if(solicitudPDFBean.isCheckSubPlanJubilacion()) subplan = ConstantesSolicitudPDF.MULTITABLA_SUBPLAN_JUBILACION;
						
						for(SolicitudPDFProductoDetalleBean detalle : solicitudPDFBean.getProducto().getDetallePrimas()) {
							parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_PRODUCTO_COBERTURA_PRODUCTO, subplan, detalle.getCobertura()), detalle.getCapitalAsegurado());
							parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_PRODUCTO_COBERTURA_PRIMA, detalle.getCobertura()), detalle.getPrimaAnual());
						}
					}
					
					/* for(SolicitudPDFProductoDetalleBean detalle : solicitudPDFBean.getProducto().getDetallePrimas()) {
						parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_PRODUCTO_COBERTURA_PRODUCTO, "1", detalle.getCobertura()), detalle.getCapitalAsegurado());
						parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_PRODUCTO_COBERTURA_PRIMA, detalle.getCobertura()), detalle.getPrimaAnual());
					} */
				}
			}
			
			if(solicitudPDFBean.getBeneficiarios() != null) {
				int i = 0;
				int j = 0;
				
				for(SolicitudPDFBeneficiarioBean detalle : solicitudPDFBean.getBeneficiarios()) {

					StringBuilder sbNumeroDoc =  new StringBuilder();
					if(!this.valorString(detalle.getTipoDocumento()).equals(Strings.EMPTY)) {
						sbNumeroDoc.append(detalle.getTipoDocumento()).append(ConstantesSolicitudPDF.ESPACIO_EN_BLANCO);
					}
					sbNumeroDoc.append(detalle.getNumeroDocumento());
					
					if(detalle.getTipoBeneficiario().equals(ConstantesSolicitudPDF.MULTITABLA_BENEFICIARIO_PRINCIPAL)) {
						i++;
						parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_APELLIDO_PATERNO, i), detalle.getApellidoPaterno());
						parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_APELLIDO_MATERNO, i), detalle.getApellidoMaterno());
						parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_NOMBRES, i), detalle.getNombres());
						parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_FECHA_NACIMIENTO, i), detalle.getFechaNacimiento());
						parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_DOCUMENTO, i), sbNumeroDoc.toString());
						parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_PARENTESCO, i), detalle.getRelacion());
						parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_DISTRIBUCION, i), detalle.getDistribucion());
					}
					
					if(detalle.getTipoBeneficiario().equals(ConstantesSolicitudPDF.MULTITABLA_BENEFICIARIO_CONTINGENTE)) {
						j++;
						parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_ADICIONAL_APELLIDO_PATERNO, j), detalle.getApellidoPaterno());
						parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_ADICIONAL_APELLIDO_MATERNO, j), detalle.getApellidoMaterno());
						parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_ADICIONAL_NOMBRES, j), detalle.getNombres());
						parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_ADICIONAL_FECHA_NACIMIENTO, j), detalle.getFechaNacimiento());
						parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_ADICIONAL_DOCUMENTO, j), sbNumeroDoc.toString());
						parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_ADICIONAL_PARENTESCO, j), detalle.getRelacion());
						parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_ADICIONAL_DISTRIBUCION, j), detalle.getDistribucion());
					}
					
				}
			}
			
			if(solicitudPDFBean.getDps() != null) {
				parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_ESTATURA, this.valorString(solicitudPDFBean.getDps().getEstatura()));
				parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PESO, this.valorString(solicitudPDFBean.getDps().getPeso()));
				parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PESO_AUMENTO_DESCUENTO_CANTIDAD, this.valorString(solicitudPDFBean.getDps().getPesoVariacionCantidad()));
				parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PESO_AUMENTO_DESCUENTO_MOTIVO, this.valorString(solicitudPDFBean.getDps().getPesoMotivo()));
				parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_FUMA_CANTIDAD, this.valorString(solicitudPDFBean.getDps().getFumadorCantidad()));
				parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_FUMA_FRECUENCIA, this.valorString(solicitudPDFBean.getDps().getFumadorFrecuencia()));
				parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_DROGAS_FECHA, this.valorString(solicitudPDFBean.getDps().getDrogasFecha()));
				parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_ALCOHOL_CANTIDAD, this.valorString(solicitudPDFBean.getDps().getAlcoholCantidad()));
				parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_ALCOHOL_FRECUENCIA, this.valorString(solicitudPDFBean.getDps().getAlcoholFrecuencia()));
				parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_LABOR_DESCRIBE, Constantes.ESPACIO_BLANCO);
				
				if(solicitudPDFBean.getDps().getPreguntas() != null) {
					/*SolicitudPDFDPSPreguntaBean xxx= new SolicitudPDFDPSPreguntaBean();
					xxx.setBloquePregunta("2");
					xxx.setItem("e)");
					xxx.setPregunta("12");
					xxx.setCheckSi(false);
					xxx.setCheckNo(true);*/
					//solicitudPDFBean.getDps().getPreguntas().add(xxx);

					List<SolicitudPDFDPSPreguntaBean> preguntas= solicitudPDFBean.getDps().getPreguntas();
					Predicate<SolicitudPDFDPSPreguntaBean> distinctPorCamposClave =distinctByKey(o -> o.getBloquePregunta() + o.getItem() + o.getPregunta()+ o.isCheckSi()+ o.isCheckNo());
					List<SolicitudPDFDPSPreguntaBean> preguntasSinDuplicados = preguntas.stream()
							.filter(distinctPorCamposClave)
							.filter(pregunta -> Integer.parseInt(pregunta.getPregunta()) <= 12 && !pregunta.getItem().equals(""))
							.collect(Collectors.toList());
					solicitudPDFBean.getDps().setPreguntas(preguntasSinDuplicados);
					if(solicitudPDFBean.getDps().getPreguntas().size() != 12) {
						StringBuilder mensaje = new StringBuilder();
						mensaje.append("[SolicitudPDFServiceImpl.generarSolicitudPDF] Propuesta: ");
						mensaje.append(solicitudPDFBean.getNumeroPropuesta());
						mensaje.append(" - Error DPS Preguntas no tiene 12 preguntas");
						
						this.enviarCorreoError(
								solicitudPDFBean, 
								"SivTXException", 
								mensaje.toString(), 
								"Se generó mal el PDF de la solicitud","ERROR",solicitudPDFBean.getNumeroPropuesta(),""
							);
					}
					
					for(SolicitudPDFDPSPreguntaBean detalle : solicitudPDFBean.getDps().getPreguntas()) {
						parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PREGUNTA_DETALLE, detalle.getPregunta()), this.valorString(detalle.getDetalle()));
						parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PREGUNTA_NUMERO, detalle.getPregunta()), this.valorString(detalle.getItem()));
						parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PREGUNTA_ENFERMEDAD, detalle.getPregunta()), this.valorString(detalle.getEnfermedad()));
						parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PREGUNTA_FECHA, detalle.getPregunta()), this.valorString(detalle.getFechaDiagnostico()));
						parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PREGUNTA_CONDICION, detalle.getPregunta()), this.valorString(detalle.getCondicionActual()));
						parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PREGUNTA_MEDICO, detalle.getPregunta()), this.valorString(detalle.getNombreMedicoHospital()));
					}
				}
			}
			
			parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_AGENTE_NOMBRES, this.valorString(solicitudPDFBean.getAgenteNombres()));
			parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_AGENTE_CODIGO, this.valorString(solicitudPDFBean.getAgenteCodigo()));
			parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_AGENTE_AGENCIA, this.valorString(solicitudPDFBean.getAgenteAgencia()));
			
			parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_FIRMA_CONTRATANTE, this.valorString(solicitudPDFBean.getFirmaContratante()));
			parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_FIRMA_ASEGURADO, this.valorString(solicitudPDFBean.getFirmaAsegurado()));
			
		}
		
		return parametros;
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> seen = new ConcurrentHashMap<>();
		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	private Map<String, Object> valoresParametrosDefault(){
	
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_NUMERO_PROPUESTA, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_FECHA_SOLICITUD,  Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_FECHA_SOLICITUD_DIA, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_FECHA_SOLICITUD_MES, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_FECHA_SOLICITUD_ANIO, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_ESTATURA, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PESO, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PESO_AUMENTO_DESCUENTO_CANTIDAD, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PESO_AUMENTO_DESCUENTO_MOTIVO, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_FUMA_CANTIDAD, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_FUMA_FRECUENCIA, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_DROGAS_FECHA, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_ALCOHOL_CANTIDAD, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_ALCOHOL_FRECUENCIA, Constantes.ESPACIO_BLANCO);
		
		for (int i = 1; i <= 12; i++) {
			parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PREGUNTA_DETALLE, i), Constantes.ESPACIO_BLANCO);
			parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PREGUNTA_NUMERO, i), Constantes.ESPACIO_BLANCO);
			parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PREGUNTA_ENFERMEDAD, i), Constantes.ESPACIO_BLANCO);
			parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PREGUNTA_FECHA, i), Constantes.ESPACIO_BLANCO);
			parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PREGUNTA_CONDICION, i), Constantes.ESPACIO_BLANCO);
			parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_PREGUNTA_MEDICO, i), Constantes.ESPACIO_BLANCO);	
		}
		
		parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_APELLIDO_PATERNO, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_APELLIDO_MATERNO, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_NOMBRES, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_NOMBRES_COMPLETOS, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_FNAC_DIA_1, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_FNAC_DIA_2, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_FNAC_MES_1, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_FNAC_MES_2, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_FNAC_ANIO_1, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_FNAC_ANIO_2, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_FNAC_ANIO_3, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_FNAC_ANIO_4, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_DOCUMENTO_NUMERO, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_NACIONALIDAD, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_DIRECCION, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_TELEFONO_CASA, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_CELULAR, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_EMAIL, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_CENTRO_LABORAL, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_ACTIVIDAD_ECONOMICA, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_PROFESION, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_INGRESO_MONTO, Constantes.ESPACIO_BLANCO);
		//--VIDA FREE--
		parametros.put(ConstantesSolicitudPDF.CODIGO_ASEGURADO_DIRECCION_LABORAL, Constantes.ESPACIO_BLANCO);
		
		//Datos del Contratante
		parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_VINCULO_ASEGURADO, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_RAZON_SOCIAL, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_APELLIDO_PATERNO, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_APELLIDO_MATERNO, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_NOMBRES, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_NOMBRES_COMPLETOS, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_FNAC_DIA_1, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_FNAC_DIA_2, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_FNAC_MES_1, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_FNAC_MES_2, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_FNAC_ANIO_1, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_FNAC_ANIO_2, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_FNAC_ANIO_3, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_FNAC_ANIO_4, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_DOCUMENTO_NUMERO, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_NACIONALIDAD, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_DIRECCION, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_TELEFONO_CASA, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_CELULAR, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_EMAIL, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_CENTRO_LABORAL, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_ACTIVIDAD_ECONOMICA, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_PROFESION, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_INGRESO_MONTO, Constantes.ESPACIO_BLANCO);
		//--VIDA FREE--
		parametros.put(ConstantesSolicitudPDF.CODIGO_CONTRATANTE_DIRECCION_LABORAL, Constantes.ESPACIO_BLANCO);
		
		//Datos del Producto
		parametros.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_FONDO_GARANTIZADO_MONTO, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_PERIODO_COBERTURA, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_PERIODO_ANUALIDADES, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_GARANTIZADO_PRIMA_COMERCIAL_ANUAL, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_GARANTIZADO_FACTOR_PAGO, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_GARANTIZADO_PRIMA_COMERCIAL, Constantes.ESPACIO_BLANCO);
		
		parametros.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_VF_FACTOR_PAGO, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_VF_PRIMA_COMERCIAL, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_VF_PRIMA_COMERCIAL_ANUAL, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_VF_PRIMA_COMERCIAL_IGV, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_PRODUCTO_VF_PRIMA_COMERCIAL_TOTAL, Constantes.ESPACIO_BLANCO);
		
		//Seteando por defecto
		for (int i = 1; i <= 3; i++) {
			//Detalle de prima
			for (int j = 1; j <= 9; j++) {
				parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_PRODUCTO_COBERTURA_PRODUCTO, i , j), Constantes.ESPACIO_BLANCO);
				if(i==1) {
					parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_PRODUCTO_COBERTURA_PRIMA, j), Constantes.ESPACIO_BLANCO);
				}
			}
			
			//Beneficiarios Principales
			parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_APELLIDO_PATERNO, i), Constantes.ESPACIO_BLANCO);
			parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_APELLIDO_MATERNO, i), Constantes.ESPACIO_BLANCO);
			parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_NOMBRES, i), Constantes.ESPACIO_BLANCO);
			parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_FECHA_NACIMIENTO, i), Constantes.ESPACIO_BLANCO);
			parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_DOCUMENTO, i), Constantes.ESPACIO_BLANCO);
			parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_PARENTESCO, i), Constantes.ESPACIO_BLANCO);
			parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_DISTRIBUCION, i), Constantes.ESPACIO_BLANCO);
			
			//Beneficiarios Adicionales
			parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_ADICIONAL_APELLIDO_PATERNO, i), Constantes.ESPACIO_BLANCO);
			parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_ADICIONAL_APELLIDO_MATERNO, i), Constantes.ESPACIO_BLANCO);
			parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_ADICIONAL_NOMBRES, i), Constantes.ESPACIO_BLANCO);
			parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_ADICIONAL_FECHA_NACIMIENTO, i), Constantes.ESPACIO_BLANCO);
			parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_ADICIONAL_DOCUMENTO, i), Constantes.ESPACIO_BLANCO);
			parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_ADICIONAL_PARENTESCO, i), Constantes.ESPACIO_BLANCO);
			parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_ADICIONAL_DISTRIBUCION, i), Constantes.ESPACIO_BLANCO);
		}
		
		//Detalle Prima
		for (int i = 1; i <= 9; i++) {
			parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_PRODUCTO_COBERTURA_PRODUCTO, 1, i), Constantes.ESPACIO_BLANCO);
			parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_PRODUCTO_COBERTURA_PRODUCTO, 2, i), Constantes.ESPACIO_BLANCO);
			parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_PRODUCTO_COBERTURA_PRODUCTO, 3, i), Constantes.ESPACIO_BLANCO);
			parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_PRODUCTO_COBERTURA_PRIMA, i), Constantes.ESPACIO_BLANCO);
		}
		
		//Detalle Beneficiarios Principal
		for (int i = 1; i <= 3; i++) {
			parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_APELLIDO_PATERNO, i), Constantes.ESPACIO_BLANCO);
			parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_APELLIDO_MATERNO, i), Constantes.ESPACIO_BLANCO);
			parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_NOMBRES, i), Constantes.ESPACIO_BLANCO);
			parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_FECHA_NACIMIENTO, i), Constantes.ESPACIO_BLANCO);
			parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_DOCUMENTO, i), Constantes.ESPACIO_BLANCO);
			parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_PARENTESCO, i), Constantes.ESPACIO_BLANCO);
			parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_PRINCIPAL_DISTRIBUCION, i), Constantes.ESPACIO_BLANCO);
		}
		
		//Detalle Beneficiarios Adicional
		for (int i = 1; i <= 2; i++) {
			parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_ADICIONAL_APELLIDO_PATERNO, i), Constantes.ESPACIO_BLANCO);
			parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_ADICIONAL_APELLIDO_MATERNO, i), Constantes.ESPACIO_BLANCO);
			parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_ADICIONAL_NOMBRES, i), Constantes.ESPACIO_BLANCO);
			parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_ADICIONAL_FECHA_NACIMIENTO, i), Constantes.ESPACIO_BLANCO);
			parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_ADICIONAL_DOCUMENTO, i), Constantes.ESPACIO_BLANCO);
			parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_ADICIONAL_PARENTESCO, i), Constantes.ESPACIO_BLANCO);
			parametros.put(MessageFormat.format(ConstantesSolicitudPDF.CODIGO_BENEFICIARIO_ADICIONAL_DISTRIBUCION, i), Constantes.ESPACIO_BLANCO);
		}
		
		
		parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_NOTA_COBERTURA, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_AGENTE_NOMBRES, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_AGENTE_CODIGO, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_AGENTE_AGENCIA, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_AGENTE_ORIGEN_VENTA, Constantes.ESPACIO_BLANCO);
					
		parametros.put(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DPS_LABOR_DESCRIBE, Constantes.ESPACIO_BLANCO);
		
		return parametros;
	}

	
	private List<String> valoresMetacamposDefault() {
		List<String> metacampos = new ArrayList<>();
		
		//Checks_Solicitud
		metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_MONEDA_SOLES);
		metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_MONEDA_DOLARES);
		metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DESGLOSE_SOLES);
		metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DESGLOSE_DOLARES);
		metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_PLAN_EDUCACION);
		metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_PLAN_SUENIO);
		metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_PLAN_JUBILACION);
		metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_VIDA_FREE);
		metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_VIDA_FREE_PLUS);
		metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_VIDA_FREE_TOTAL);
		
		metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_FRECUENCIA_MENSUAL);
		metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_FRECUENCIA_TRIMESTRAL);
		metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_FRECUENCIA_SEMESTRAL);
		metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_FRECUENCIA_ANUAL);
		metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DEVOLUCION_0);
		metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DEVOLUCION_50);
		metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DEVOLUCION_75);
		metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DEVOLUCION_100);
		metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DEVOLUCION_125);
		metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DEVOLUCION_130);
		metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DEVOLUCION_135);
		metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DEVOLUCION_140);
		metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DEVOLUCION_145);
		metacampos.add(ConstantesSolicitudPDF.CODIGO_SOLICITUD_DEVOLUCION_150);
		
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
		metacampos.add(ConstantesSolicitudPDF.CODIGO_PRODUCTO_PERIODO_TEMPORAL);
		metacampos.add(ConstantesSolicitudPDF.CODIGO_PRODUCTO_PERIODO_VITALICIO);
		metacampos.add(ConstantesSolicitudPDF.CODIGO_PRODUCTO_FUMADOR_SI);
		metacampos.add(ConstantesSolicitudPDF.CODIGO_PRODUCTO_FUMADOR_NO);
		
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
		return metacampos;
	}

	private Map<String, Object> valoresMetaDatosDefault(List<String> metacampos, File ImagenNOK){
		Map<String, Object> metadatos = new HashMap<String, Object>();
		IImageProvider checkNok = new FileImageProvider(ImagenNOK, true);
		
		for(String campo :  metacampos ) {
			metadatos.put(campo, checkNok);
		}
		return metadatos;
	}
	
	
	private SolicitudPDFPersonaBean completarPersonaBean(Long idSolicitud, Persona persona, int tipoPersona) throws Exception {
		SolicitudPDFPersonaBean personaBean = null;
		
		SolicitudDatosAdicionales solicitudDatosAdicionales = solicitudDatosAdicionalesRepository.findByIdSolicitud(idSolicitud);
		if(persona != null && persona.getIdPersona() != null ) {
			personaBean = new SolicitudPDFPersonaBean();
			
			String apellidoMaterno = Strings.EMPTY;
			String apellidoPaterno = Strings.EMPTY;
			String nombres = Strings.EMPTY;
			String nombresCompletos = Strings.EMPTY;
			String nacimientoAnio1 = Strings.EMPTY;
			String nacimientoAnio2 = Strings.EMPTY;
			String nacimientoAnio3 = Strings.EMPTY;
			String nacimientoAnio4 = Strings.EMPTY;
			String nacimientoMes1 = Strings.EMPTY;
			String nacimientoMes2 = Strings.EMPTY;
			String nacimientoDia1 = Strings.EMPTY;
			String nacimientoDia2 = Strings.EMPTY;
			String direccion = Strings.EMPTY;
			
			apellidoPaterno = this.valorString(persona.getApellidoPaterno());
			apellidoMaterno = this.valorString(persona.getApellidoMaterno());
			nombres = this.valorString(persona.getNombres());
					
			if(!apellidoPaterno.equals(Strings.EMPTY) && !nombres.equals(Strings.EMPTY)) {
				StringBuilder sb = new StringBuilder();
				sb.append(nombres.toString()).append(ConstantesSolicitudPDF.ESPACIO_EN_BLANCO);
				sb.append(apellidoPaterno.toString());
				
				if(!apellidoMaterno.equals(Strings.EMPTY)) {
					sb.append(ConstantesSolicitudPDF.ESPACIO_EN_BLANCO).append(apellidoMaterno);
				}
				nombresCompletos = sb.toString();				
			}
			
			if(!this.valorString(persona.getRazonSocial()).equals(Strings.EMPTY)) {
				nombresCompletos = this.valorString(persona.getRazonSocial());
			}

			personaBean.setApellidoPaterno(apellidoPaterno);
			personaBean.setApellidoMaterno(apellidoMaterno);
			personaBean.setNombres(nombres);
			personaBean.setNombresCompletos(nombresCompletos);
			personaBean.setRazonSocial(this.valorString(persona.getRazonSocial()));
			
			if(persona.getFechaNacimiento() != null) {
				LOGGER.info("XXXXX =>"+persona.getFechaNacimiento());
				String fechaNacimiento = DateUtil.dateToString(persona.getFechaNacimiento(), DateUtil.FORMATO_DIA_YYYYMMDD);
				if(fechaNacimiento.length() == 8) {
					String[] fechaArray = fechaNacimiento.split("(?!^)");
									
					nacimientoAnio1 = fechaArray[0];
					nacimientoAnio2 = fechaArray[1];
					nacimientoAnio3 = fechaArray[2];
					nacimientoAnio4 = fechaArray[3];
					nacimientoMes1 = fechaArray[4];
					nacimientoMes2 = fechaArray[5];
					nacimientoDia1 = fechaArray[6];
					nacimientoDia2 = fechaArray[7];	
				}
			}

			personaBean.setNacimientoAnio1(nacimientoAnio1);
			personaBean.setNacimientoAnio2(nacimientoAnio2);
			personaBean.setNacimientoAnio3(nacimientoAnio3);
			personaBean.setNacimientoAnio4(nacimientoAnio4);
			personaBean.setNacimientoDia1(nacimientoDia1);
			personaBean.setNacimientoDia2(nacimientoDia2);
			personaBean.setNacimientoMes1(nacimientoMes1);
			personaBean.setNacimientoMes2(nacimientoMes2);
			
			personaBean.setCheckDocumentoDNI(this.checkOpcion(persona.getTipoDocumento(), ConstantesSolicitudPDF.MULTITABLA_DOCUMENTO_DNI));
			personaBean.setCheckDocumentoCE(this.checkOpcion(persona.getTipoDocumento(), ConstantesSolicitudPDF.MULTITABLA_DOCUMENTO_CE));
			personaBean.setCheckDocumentoRUC(this.checkOpcion(persona.getTipoDocumento(), ConstantesSolicitudPDF.MULTITABLA_DOCUMENTO_RUC_JURIDICO));
			if(!personaBean.isCheckDocumentoRUC()) {
				personaBean.setCheckDocumentoRUC(this.checkOpcion(persona.getTipoDocumento(), ConstantesSolicitudPDF.MULTITABLA_DOCUMENTO_RUC_NATURAL));
			}
			
			personaBean.setNumeroDocumento(this.valorString(persona.getNumeroDocumento()));
			
			Boolean sexoM = this.checkOpcion(persona.getGenero(), ConstantesSolicitudPDF.MULTITABLA_GENERO_MASCULINO);
			Boolean sexoF = this.checkOpcion(persona.getGenero(), ConstantesSolicitudPDF.MULTITABLA_GENERO_FEMENINO);
			
			personaBean.setCheckSexoMasculino(sexoM);
			personaBean.setCheckSexoFemenino(sexoF);
			
			personaBean.setCheckEstadoCivilSotero(this.checkOpcion(persona.getEstadoCivil(), ConstantesSolicitudPDF.MULTITABLA_ESTADO_CIVIL_SOLTERO));
			personaBean.setCheckEstadoCivilCasado(this.checkOpcion(persona.getEstadoCivil(), ConstantesSolicitudPDF.MULTITABLA_ESTADO_CIVIL_CASADO));
			personaBean.setCheckEstadoCivilViudo(this.checkOpcion(persona.getEstadoCivil(), ConstantesSolicitudPDF.MULTITABLA_ESTADO_CIVIL_VIUDO));
			personaBean.setCheckEstadoCivilDivorciado(this.checkOpcion(persona.getEstadoCivil(), ConstantesSolicitudPDF.MULTITABLA_ESTADO_CIVIL_DIVORCIADO));
			personaBean.setCheckEstadoCivilConviviente(this.checkOpcion(persona.getEstadoCivil(), ConstantesSolicitudPDF.MULTITABLA_ESTADO_CIVIL_CONVIVIENTE));
			
			personaBean.setNacionalidad(this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_NACIONALIDAD, persona.getNacionalidad(),Constantes.MULTITABLA_COLUMNA_VALOR).toUpperCase());
			
			personaBean.setCheckDireccionJR(this.checkOpcion(persona.getDireccionTipo(), ConstantesSolicitudPDF.MULTITABLA_DIRECCION_JIRON));
			personaBean.setCheckDireccionAV(this.checkOpcion(persona.getDireccionTipo(), ConstantesSolicitudPDF.MULTITABLA_DIRECCION_AVENIDA));
			personaBean.setCheckDireccionCA(this.checkOpcion(persona.getDireccionTipo(), ConstantesSolicitudPDF.MULTITABLA_DIRECCION_CALLE));
			personaBean.setCheckDireccionPJ(this.checkOpcion(persona.getDireccionTipo(), ConstantesSolicitudPDF.MULTITABLA_DIRECCION_PASAJE));
			
			//Formato de dirección
			String direccionTipo = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_DIRECCION_TIPO, persona.getDireccionTipo(),Constantes.MULTITABLA_COLUMNA_VALOR_AUXILIAR).toUpperCase();
			String interior = persona.getDireccionInterior();
			String urbanizacion = persona.getDireccionUrbanizacion();
			String distrito  = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_DISTRITO, persona.getDistrito(),Constantes.MULTITABLA_COLUMNA_VALOR);
			String provincia  = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_PROVINCIA, persona.getProvincia(),Constantes.MULTITABLA_COLUMNA_VALOR);
			String departamento  = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_DEPARTAMENTO, persona.getDepartamento(),Constantes.MULTITABLA_COLUMNA_VALOR);
			
			StringBuilder sbd = new StringBuilder();
			sbd.append(direccionTipo).append(ConstantesSolicitudPDF.ESPACIO_EN_BLANCO);
			sbd.append(persona.getDireccionTipoDes()).append(ConstantesSolicitudPDF.ESPACIO_EN_BLANCO);
			sbd.append(persona.getDireccionNroMz()).append(ConstantesSolicitudPDF.ESPACIO_EN_BLANCO);
			
			if(!this.valorString(interior).equals(Strings.EMPTY)) {
				sbd.append("- ");
				sbd.append(interior).append(ConstantesSolicitudPDF.ESPACIO_EN_BLANCO);
			}
			if(!this.valorString(urbanizacion).equals(Strings.EMPTY)) {
				sbd.append("- ");
				sbd.append(urbanizacion).append(ConstantesSolicitudPDF.ESPACIO_EN_BLANCO);
			}
			if(!this.valorString(distrito).equals(Strings.EMPTY)) {
				sbd.append("- ").append(distrito).append(ConstantesSolicitudPDF.ESPACIO_EN_BLANCO);
			}
			if(!this.valorString(provincia).equals(Strings.EMPTY)) {
				sbd.append("- ").append(provincia).append(ConstantesSolicitudPDF.ESPACIO_EN_BLANCO);
			}
			if(!this.valorString(departamento).equals(Strings.EMPTY)) {
				sbd.append("- ").append(departamento).append(ConstantesSolicitudPDF.ESPACIO_EN_BLANCO);
			}
			
			direccion = sbd.toString().trim();
			
			personaBean.setDireccion(direccion);
			personaBean.setCelular(this.valorString(persona.getCelular()));
			personaBean.setEmail(this.valorString(persona.getCorreo()));
			
			Boolean isPepSi = this.checkOpcion(persona.getEsPep(), ConstantesSolicitudPDF.MULTITABLA_CHECK_CODIGO_TRUE);
			Boolean isPepNo = this.checkOpcion(persona.getEsPep(), ConstantesSolicitudPDF.MULTITABLA_CHECK_CODIGO_FALSE);
			
			personaBean.setCheckPEPSi(isPepSi);
			personaBean.setCheckPEPNo(isPepNo);
			
			Boolean isSujetoSi = this.checkOpcion(persona.getEsSujetoObligado(), ConstantesSolicitudPDF.MULTITABLA_CHECK_CODIGO_TRUE);
			Boolean isSujetoNo = this.checkOpcion(persona.getEsSujetoObligado(), ConstantesSolicitudPDF.MULTITABLA_CHECK_CODIGO_FALSE); 
			String actividadEconomica = null;
			String profesion = null;
			if (tipoPersona == 1 ) {
				actividadEconomica=this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_ACTIVIDAD_ECONOMICA, this.valorString( solicitudDatosAdicionales != null ?(solicitudDatosAdicionales.getActividadEconomicaAsegurado() != null ?solicitudDatosAdicionales.getActividadEconomicaAsegurado():persona.getActividadEconomica()): persona.getActividadEconomica()),Constantes.MULTITABLA_COLUMNA_VALOR).toUpperCase();
				profesion=this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_PROFESION, this.valorString(solicitudDatosAdicionales != null ? (solicitudDatosAdicionales.getProfesionAsegurado() != null ?solicitudDatosAdicionales.getProfesionAsegurado(): persona.getProfesion()):persona.getProfesion()),Constantes.MULTITABLA_COLUMNA_VALOR).toUpperCase();
			} else if (tipoPersona == 0 ) {
				actividadEconomica=this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_ACTIVIDAD_ECONOMICA, this.valorString( solicitudDatosAdicionales != null ?(solicitudDatosAdicionales.getActividadEconomicaContratante() != null ?solicitudDatosAdicionales.getActividadEconomicaContratante():persona.getActividadEconomica()): persona.getActividadEconomica()),Constantes.MULTITABLA_COLUMNA_VALOR).toUpperCase();
				profesion=this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_PROFESION, this.valorString(solicitudDatosAdicionales != null ? (solicitudDatosAdicionales.getProfesionContratante() != null ?solicitudDatosAdicionales.getProfesionContratante(): persona.getProfesion()):persona.getProfesion()),Constantes.MULTITABLA_COLUMNA_VALOR).toUpperCase();
			}
			personaBean.setCheckSujetoObligadoSi(isSujetoSi);
			personaBean.setCheckSujetoObligadoNo(isSujetoNo);
			
			personaBean.setCentroTrabajo(this.valorString(persona.getCentroTrabajo()));
			  
			personaBean.setActividadEconomica(actividadEconomica);
			
			personaBean.setCheckIngresoSoles(this.checkOpcion(persona.getIngresoMoneda(), ConstantesSolicitudPDF.MULTITABLA_MONEDA_SOLES));
			personaBean.setCheckIngresoDolares(this.checkOpcion(persona.getIngresoMoneda(), ConstantesSolicitudPDF.MULTITABLA_MONEDA_DOLARES));
			
			String ingresoValor = this.valorString(persona.getIngresoValor());
			if(ingresoValor.equals(Strings.EMPTY))ingresoValor="0";
			
			personaBean.setIngresoMensual(Utilitarios.formatoMiles(Double.parseDouble(ingresoValor), Utilitarios.FORMATO_MILES_SIN_DECIMAL));
			
			StringBuilder sbp = new StringBuilder();
			
			sbp.append(profesion);
			if(!this.valorString(persona.getProfesionDetalle()).equals(Strings.EMPTY)) {
				sbp.append(" / ");
				sbp.append(persona.getProfesionDetalle());
			}
			
			personaBean.setProfesion(sbp.toString());
			personaBean.setDireccionLaboral("");
			
			Boolean isFumador = this.checkOpcion(persona.getFumador(), ConstantesSolicitudPDF.MULTITABLA_CHECK_CODIGO_TRUE);
			personaBean.setCheckFumadorSi(isFumador);
			personaBean.setCheckFumadorNo(!isFumador);
			
		}//FIN IS NULL PERSONA
		
		return personaBean;
	}
	
	private SolicitudPDFProductoBean completarProductoBean(Solicitud solicitud, SolicitudProducto solicitudProducto, List<SolicitudProductoDetalle> listaProductoDetalle) throws Exception {
		SolicitudPDFProductoBean productoBean = null;
		
		if(solicitudProducto != null) {
			productoBean = new SolicitudPDFProductoBean();
			
			productoBean.setCheckMonedaFondoSoles(this.checkOpcion(solicitud.getMoneda(), ConstantesSolicitudPDF.MULTITABLA_MONEDA_SOLES));
			productoBean.setCheckMonedaFondoDolares(this.checkOpcion(solicitud.getMoneda(), ConstantesSolicitudPDF.MULTITABLA_MONEDA_DOLARES));
			productoBean.setMontoFondoGarantizado(Utilitarios.formatoMiles(Double.parseDouble(solicitudProducto.getMontoFondoGarantizado().toString()), Utilitarios.FORMATO_MILES_CON_DECIMAL));
			productoBean.setPeriodoCoberturaAnual(this.valorString(solicitudProducto.getPeriodoCoberturaAnual().toString()));
			productoBean.setAnualidadPago(this.valorString(solicitudProducto.getAnualidadPago().toString()));			
			productoBean.setCheckFrecuenciaMensual(this.checkOpcion(solicitudProducto.getFrecuencia(), ConstantesSolicitudPDF.MULTITABLA_PRODUCTO_FRECUENCIA_MENSUAL));
			productoBean.setCheckFrecuenciaTrimestral(this.checkOpcion(solicitudProducto.getFrecuencia(), ConstantesSolicitudPDF.MULTITABLA_PRODUCTO_FRECUENCIA_TRIMESTRAL));
			productoBean.setCheckFrecuenciaSemestral(this.checkOpcion(solicitudProducto.getFrecuencia(), ConstantesSolicitudPDF.MULTITABLA_PRODUCTO_FRECUENCIA_SEMESTRAL));
			productoBean.setCheckFrecuenciaAnual(this.checkOpcion(solicitudProducto.getFrecuencia(), ConstantesSolicitudPDF.MULTITABLA_PRODUCTO_FRECUENCIA_ANUAL));
			productoBean.setCheckFrecuenciaUnica(this.checkOpcion(solicitudProducto.getFrecuencia(), ConstantesSolicitudPDF.MULTITABLA_PRODUCTO_FRECUENCIA_UNICA));
			productoBean.setCheckCuotaComodin(this.checkOpcion(solicitudProducto.getTipoCuota(), ConstantesSolicitudPDF.MULTITABLA_PRODUCTO_TIPO_CUOTA_COMODIN));
			productoBean.setCheckCuotaDoble(this.checkOpcion(solicitudProducto.getTipoCuota(), ConstantesSolicitudPDF.MULTITABLA_PRODUCTO_TIPO_CUOTA_DOBLE));
			productoBean.setCheckTipoRiesgoFumadorSi(this.checkOpcion(solicitudProducto.getTipoRiesgo(), ConstantesSolicitudPDF.MULTITABLA_PRODUCTO_TIPO_RIESGO_FUMADOR_SI));
			productoBean.setCheckTipoRiesgoFumadorNo(this.checkOpcion(solicitudProducto.getTipoRiesgo(), ConstantesSolicitudPDF.MULTITABLA_PRODUCTO_TIPO_RIESGO_FUMADOR_NO));
			Boolean monedaSoles = this.checkOpcion(solicitud.getMoneda(), ConstantesSolicitudPDF.MULTITABLA_MONEDA_SOLES);
			Boolean monedaDolares = this.checkOpcion(solicitud.getMoneda(), ConstantesSolicitudPDF.MULTITABLA_MONEDA_DOLARES);
				
			if (solicitudProducto.getPrimaDevolucion() != null) {
				productoBean.setCheckDevolucionPrima0(this.checkOpcion(solicitudProducto.getPrimaDevolucion().toString(), ConstantesSolicitudPDF.MULTITABLA_PRODUCTO_VIDA_FREE_0));
				productoBean.setCheckDevolucionPrima50(this.checkOpcion(solicitudProducto.getPrimaDevolucion().toString(), ConstantesSolicitudPDF.MULTITABLA_PRODUCTO_VIDA_FREE_50));
				productoBean.setCheckDevolucionPrima75(this.checkOpcion(solicitudProducto.getPrimaDevolucion().toString(), ConstantesSolicitudPDF.MULTITABLA_PRODUCTO_VIDA_FREE_75));
				productoBean.setCheckDevolucionPrima100(this.checkOpcion(solicitudProducto.getPrimaDevolucion().toString(), ConstantesSolicitudPDF.MULTITABLA_PRODUCTO_VIDA_FREE_100));
				productoBean.setCheckDevolucionPrima125(this.checkOpcion(solicitudProducto.getPrimaDevolucion().toString(), ConstantesSolicitudPDF.MULTITABLA_PRODUCTO_VIDA_FREE_125));
				productoBean.setCheckDevolucionPrima130(this.checkOpcion(solicitudProducto.getPrimaDevolucion().toString(), ConstantesSolicitudPDF.MULTITABLA_PRODUCTO_VIDA_FREE_130));
				productoBean.setCheckDevolucionPrima135(this.checkOpcion(solicitudProducto.getPrimaDevolucion().toString(), ConstantesSolicitudPDF.MULTITABLA_PRODUCTO_VIDA_FREE_135));
				productoBean.setCheckDevolucionPrima140(this.checkOpcion(solicitudProducto.getPrimaDevolucion().toString(), ConstantesSolicitudPDF.MULTITABLA_PRODUCTO_VIDA_FREE_140));
				productoBean.setCheckDevolucionPrima145(this.checkOpcion(solicitudProducto.getPrimaDevolucion().toString(), ConstantesSolicitudPDF.MULTITABLA_PRODUCTO_VIDA_FREE_145));
				productoBean.setCheckDevolucionPrima150(this.checkOpcion(solicitudProducto.getPrimaDevolucion().toString(), ConstantesSolicitudPDF.MULTITABLA_PRODUCTO_VIDA_FREE_150));
			}
			productoBean.setCheckFrecuenciaMensual(this.checkOpcion(solicitudProducto.getFrecuencia(), ConstantesSolicitudPDF.MULTITABLA_PRODUCTO_FRECUENCIA_MENSUAL));
			
			productoBean.setCheckTemporal(Boolean.TRUE);
			productoBean.setCheckVitalicio(Boolean.FALSE);
			
			productoBean.setCheckPrimaMonedaSoles(monedaSoles);
			productoBean.setCheckPrimaMonedaDolares(monedaDolares);
			productoBean.setPrimaComercialAnual(Utilitarios.formatoMiles(Double.parseDouble(solicitudProducto.getPrimaComercialAnual().toString()), Utilitarios.FORMATO_MILES_CON_DECIMAL));
			productoBean.setFactorPago(Utilitarios.formatoMiles(Double.parseDouble(solicitudProducto.getFactorPago().toString()), Utilitarios.FORMATO_MILES_CON_DECIMAL));
			productoBean.setPrimaComercial(Utilitarios.formatoMiles(Double.parseDouble(solicitudProducto.getPrimaComercial().toString()), Utilitarios.FORMATO_MILES_CON_DECIMAL));
		
			//Producto Vida Free
			if (solicitudProducto.getPrimaComercial() != null) {
				Float primaComercialTemp = solicitudProducto.getPrimaComercial() - solicitudProducto.getPrimaIgv();
				String primaComercialText = Utilitarios.formatoMiles(Double.parseDouble(primaComercialTemp.toString()), Utilitarios.FORMATO_MILES_CON_DECIMAL);
				productoBean.setPrimaVidaFreePrimaComercial(primaComercialText);
			}
			if (solicitudProducto.getFactorPago() != null) {
				productoBean.setPrimaVidaFreeFactorPago(Utilitarios.formatoMiles(Double.parseDouble(solicitudProducto.getFactorPago().toString()), Utilitarios.FORMATO_MILES_CON_DECIMAL));
			}
			if (solicitudProducto.getPrimaComercialAnual() != null) {
				productoBean.setPrimaVidaFreePrimaComercialAnual(Utilitarios.formatoMiles(Double.parseDouble(solicitudProducto.getPrimaComercialAnual().toString()), Utilitarios.FORMATO_MILES_CON_DECIMAL));
			}
			productoBean.setPrimaVidaFreePrimaComercialIgv(solicitudProducto.getPrimaIgv().toString());
			if (solicitudProducto.getPrimaComercial() != null) {
				productoBean.setPrimaVidaFreePrimaComercialTotal(Utilitarios.formatoMiles(Double.parseDouble(solicitudProducto.getPrimaComercial().toString()), Utilitarios.FORMATO_MILES_CON_DECIMAL));
			}
			
			List<SolicitudPDFProductoDetalleBean> detallePrimas = new ArrayList<SolicitudPDFProductoDetalleBean>();
			if(listaProductoDetalle != null) {
				for(SolicitudProductoDetalle detalle : listaProductoDetalle) {
					String capital = Utilitarios.formatoMiles(Double.parseDouble(detalle.getCapitalAsegurado().toString()), Utilitarios.FORMATO_MILES_CON_DECIMAL);
					String prima = Utilitarios.formatoMiles(Double.parseDouble(detalle.getPrimaAnual().toString()), Utilitarios.FORMATO_MILES_CON_DECIMAL);
					detallePrimas.add(new SolicitudPDFProductoDetalleBean(detalle.getTipoCobertura(), detalle.getCobertura(), capital, prima));
				}
			}
			productoBean.setDetallePrimas(detallePrimas);
		}
		
		return productoBean;
	}
	
	
	private List<SolicitudPDFBeneficiarioBean> completarBeneficiariosBean(List<SolicitudBeneficiario> listaBeneficiarios) throws Exception {

		List<SolicitudPDFBeneficiarioBean> beneficiarios = new ArrayList<SolicitudPDFBeneficiarioBean>();
		if(listaBeneficiarios != null) {
			for(SolicitudBeneficiario item : listaBeneficiarios) {
				String fechaNacimiento = this.valorString(DateUtil.dateToString(item.getFechaNacimiento(), DateUtil.FORMATO_DIA_DDMMYYYY));
				String tipoDocumento = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_TIPO_DOCUMENTO, item.getTipoDocumento(),Constantes.MULTITABLA_COLUMNA_VALOR_AUXILIAR).toUpperCase();
				String relacion = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_TIPO_RELACION, item.getTipoRelacion(),Constantes.MULTITABLA_COLUMNA_VALOR).toUpperCase();
				String distribucion = this.valorString(item.getDistribucion()) + "%";
				String nombres = this.valorString(item.getNombres());
				String apellidoPaterno = this.valorString(item.getApellidoPaterno());
				String apellidoMaterno = this.valorString(item.getApellidoMaterno());
				String numeroDocumento = this.valorString(item.getNumeroDocumento());
				
				beneficiarios.add(new SolicitudPDFBeneficiarioBean(item.getTipoBeneficiario(), nombres, apellidoPaterno, apellidoMaterno, fechaNacimiento, tipoDocumento, numeroDocumento, relacion, distribucion));
			}
		}
		
		
		return beneficiarios;
	}
		
	private SolicitudPDFDPSBean completarDSPBean(SolicitudDps solicitudDps, List<SolicitudDpsPregunta> dpsPreguntas, Persona asegurado) throws Exception {
		SolicitudPDFDPSBean dpsBean = null;
		if(solicitudDps != null) {
			String vacio = "               ";
			dpsBean = new SolicitudPDFDPSBean();
			
			String estatura = Strings.EMPTY;
			if(solicitudDps.getEstatura() != null) {
				estatura = String.valueOf(Math.round(solicitudDps.getEstatura()));
			}

			dpsBean.setEstatura(estatura);
			dpsBean.setPeso(this.valorString(solicitudDps.getPeso().toString()));
			dpsBean.setCheckVariacionSi(this.checkOpcion(solicitudDps.getPesoVariacion().toString(), ConstantesSolicitudPDF.MULTITABLA_CHECK_TRUE));
			dpsBean.setCheckVariacionNo(this.checkOpcion(solicitudDps.getPesoVariacion().toString(), ConstantesSolicitudPDF.MULTITABLA_CHECK_FALSE));
			if(dpsBean.isCheckVariacionSi()) {
				Boolean aumento = solicitudDps.getPesoAumentoDisminuyo();
				if(aumento) {
					dpsBean.setCheckPesoAumento(true);
					dpsBean.setCheckPesoDisminuyo(false);
				}else {
					dpsBean.setCheckPesoAumento(false);
					dpsBean.setCheckPesoDisminuyo(true);
				}
			}
			if(solicitudDps.getPesoAdCantidad() != null) {
				dpsBean.setPesoVariacionCantidad(this.valorString(solicitudDps.getPesoAdCantidad().toString()));
			}
			dpsBean.setPesoMotivo(this.valorString(solicitudDps.getPesoMotivo()));
			
			if(solicitudDps.getFumador()) {
				dpsBean.setCheckFumadorSi(true);
				dpsBean.setCheckFumadorNo(false);
			}else {
				dpsBean.setCheckFumadorSi(false);
				dpsBean.setCheckFumadorNo(true);
			}
			
			if(!this.valorString(solicitudDps.getFumadorCantidad()).equals(Strings.EMPTY)) {
				dpsBean.setFumadorCantidad(solicitudDps.getFumadorCantidad());
			}else {
				dpsBean.setFumadorCantidad(vacio);
			}
			
			if(!this.valorString(solicitudDps.getFumadorFrecuencia()).equals(Strings.EMPTY)) {
				dpsBean.setFumadorFrecuencia(solicitudDps.getFumadorFrecuencia());
			}else {
				dpsBean.setFumadorFrecuencia(vacio);
			}

			if(solicitudDps.getDrogas()) {
				dpsBean.setCheckConsumeDrogasSi(true);
				dpsBean.setCheckConsumeDrogasNo(false);
			}else {
				dpsBean.setCheckConsumeDrogasSi(false);
				dpsBean.setCheckConsumeDrogasNo(true);
			}
			
			if(!this.valorString(solicitudDps.getDrogasFecha()).equals(Strings.EMPTY)) {
				dpsBean.setDrogasFecha(this.valorString(solicitudDps.getDrogasFecha()));
			}else {
				dpsBean.setDrogasFecha(vacio);
			}			
			
			if(solicitudDps.getAlcohol()) {
				dpsBean.setCheckAlchoolSi(true);
				dpsBean.setCheckAlchoolNo(false);
			}else {
				dpsBean.setCheckAlchoolSi(false);
				dpsBean.setCheckAlchoolNo(true);
			}
			
			if(!this.valorString(solicitudDps.getAlcoholCantidad()).equals(Strings.EMPTY)) {
				dpsBean.setAlcoholCantidad((valorMultiTabla(TablaEnum.TABLA_CANT_FRECUENCIA.getCodigoTabla(),solicitudDps.getAlcoholCantidad(),Constantes.MULTITABLA_COLUMNA_VALOR).toUpperCase()));
			}else {
				dpsBean.setAlcoholCantidad(vacio);
			}	
			
			if(!this.valorString(solicitudDps.getAlcoholFrecuencia()).equals(Strings.EMPTY)) {
				dpsBean.setAlcoholFrecuencia((valorMultiTabla(TablaEnum.TABLA_FRECUENCIA.getCodigoTabla(),solicitudDps.getAlcoholFrecuencia(),Constantes.MULTITABLA_COLUMNA_VALOR).toUpperCase()));
			}else {
				dpsBean.setAlcoholFrecuencia(vacio);
			}
			
			
			if(dpsPreguntas != null) {
				List<SolicitudPDFDPSPreguntaBean> listaPreguntas = new ArrayList<SolicitudPDFDPSPreguntaBean>();
				for(SolicitudDpsPregunta pregunta : dpsPreguntas) {

					StringBuilder item = new StringBuilder();
					boolean checkSi = false;
					boolean checkNo = false;
					//String fechaDiagnostico = DateUtil.dateToString(pregunta.getFechaDiagnostico(), DateUtil.FORMATO_DIA_MMYYYY);
					String fechaDiagnostico = this.valorString(pregunta.getFechaDiagnostico());
					
					Integer numeroPregunta = Integer.parseInt(pregunta.getPregunta());
					if(numeroPregunta >=1 && numeroPregunta <=7) {
						item.append(numeroPregunta.toString());
					}
					if(numeroPregunta >=8 && numeroPregunta <=12) {
						switch (numeroPregunta) {
							case 8: item = item.append("a"); break;
							case 9: item = item.append("b"); break;
							case 10: item = item.append("c"); break;
							case 11: item = item.append("d"); break;
							case 12: item = item.append("e"); break;
						}
						
						item.append(")");
					}
					
					if(pregunta.getRespuesta()) {
						checkSi = true;
						checkNo = false;
					}else {
						checkSi = false;
						checkNo = true;
					}
					
					if(asegurado.getGenero() != null && asegurado.getGenero().equals(ConstantesSolicitudPDF.MULTITABLA_GENERO_MASCULINO)) {
						if(numeroPregunta >=6 && numeroPregunta <=7) {
							checkSi = false;
							checkNo = false;
						}
					}
					
					listaPreguntas.add(new SolicitudPDFDPSPreguntaBean(pregunta.getbloquePregunta().toString(), item.toString(), pregunta.getPregunta(), checkSi, checkNo, pregunta.getDetalle(), pregunta.getEnfermedad(), fechaDiagnostico, pregunta.getCondicionActual(), pregunta.getNombreMedicoHospital()));
					
				}
				dpsBean.setPreguntas(listaPreguntas);
			}
		}
		
		return dpsBean;
	}
	
	
	private String valorMultiTabla(String codigoTabla, String codigo, String campo) {
		String result = Strings.EMPTY;
		
		if(codigo == null || codigo.equals(Strings.EMPTY)) return result;
		
		List<Multitabla> lista = multitablaRepository.findByCodigoTablaSinEstado(codigoTabla);
		if(lista != null && lista.size() > 0) {
			for (Multitabla m : lista){
				if(m.getCodigo().equals(codigo)) {
					switch (campo) {
					case Constantes.MULTITABLA_COLUMNA_VALOR:
						result = m.getValor();
						break;
					case Constantes.MULTITABLA_COLUMNA_VALOR_AUXILIAR:
						result = m.getValorAux();
						break;
					case Constantes.MULTITABLA_COLUMNA_VALOR_CRM:
						result = m.getValorCrm();
						break;						
					default:
						result = m.getValor();
						break;
					}
					break;
				}
			}
		}
		
		return result;
	}

	private boolean checkOpcion(String codigo, String comparar) {
		boolean check = false;
		if(codigo!=null && codigo.equals(comparar)) {
			check = true;
		}
		return check;
	}
	
	private String valorString(Object valor) {
		String result = Strings.EMPTY;
		
		if(valor != null) {
			result = valor.toString();
		}
		return result;
	}
	
	public File obtenerPlantilla(String plantilla) {
		File plantillaFile = null;
		String rutaPlantilla = "";
		String systemOp = System.getProperty("os.name");
		System.out.println(plantilla);
		if(systemOp.contains("Windows")) {
			rutaPlantilla = Constantes.RUTA_PLANTILLA + "/" + plantilla;
			
			plantillaFile = new File(
				getClass().getClassLoader().getResource(rutaPlantilla).getFile()
			);
			
		}else {
			plantillaFile = new File(this.rutaPlantillaDocLinux + "//" + plantilla);
			LOGGER.info("obtenerPlantilla => " + plantillaFile);
		}
		
		return plantillaFile;
	}
	
	private void enviarCorreoError(Object requestDTO, String tipoError, String asunto, String mensajeError, String motivo, String cotizacion, String documento) {
		
		//-- Body
		StringBuilder body = new StringBuilder();
		body
			.append("<b>Tipo: </b>").append(tipoError) //-- Transaccion, BaseDatos, SOA
			.append("<br>")
			.append("<b>JSON del request "+requestDTO.getClass().getSimpleName()+":</b>")
			.append("<br>")			
			.append(new Gson().toJson(requestDTO))
			.append("<br>")			
			.append("<b>MENSAJE ERROR:</b>")
			.append("<br>")			
			.append(mensajeError);
					
		//-- Enviar
		EnviarCorreoRequestNuevo enviarCorreoRequest = new EnviarCorreoRequestNuevo();
		String[] correoSeparado = enviarCorreoDestinatario.split(",");
		List<Remitente> listaRemitente = new ArrayList<>();
		Remitente remitente;
		for(String correo:correoSeparado) {
			remitente = new Remitente();
			remitente.setEmail(correo);
			listaRemitente.add(remitente);
		}
		
		enviarCorreoRequest.setTitle("Encuesta de clientes");
		enviarCorreoRequest.setTo(listaRemitente);
		enviarCorreoRequest.setSubject(asunto.concat(" - ").concat(activeProfile));
		enviarCorreoRequest.setHtmlContent(body.toString());
//		enviarCorreoRequest.setpRutaArchivoAdjunto("");
		
		try {
			//No enviar correo por el momento.
			interseguroRestClient.enviarCorreo(enviarCorreoRequest,  motivo,  cotizacion,  documento);
			
		} catch (Exception e) {
			LOGGER.error("ERROR - AdnServiceImpl#enviarCorreoError(requestDTO, tipoError, asunto, mensajeError): " + e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	private void enviarPDFCRM2(File fileTmp, String tipoDocumentoCRM, String numeroDocumento, String nombreArchivoPDF, String descripcionArchivo,String tipoDocumentoInput) {	
		try {
			if(!fileTmp.equals(Strings.EMPTY)) {
				UploadArchivoRequest requestArchivo = new UploadArchivoRequest();
				requestArchivo.setTipoDocumento(tipoDocumentoCRM);
				requestArchivo.setNumeroDocumento(numeroDocumento);
				requestArchivo.setFile(fileTmp);
				requestArchivo.setNombreArchivo(nombreArchivoPDF);
				requestArchivo.setDescripcionArchivo(descripcionArchivo);
				requestArchivo.setTipoDocumentoAdn(tipoDocumentoInput);
				UploadArchivoResponse responseUploadCRM = globalRestClient.uploadArchivo(requestArchivo);
				
				Gson gson = new Gson();
				
				LOGGER.info("enviarPDFCRM.requestArchivo=>" + gson.toJson(requestArchivo));
				LOGGER.info("enviarPDFCRM.responseUploadCRM=>" + gson.toJson(responseUploadCRM));
			}
		}catch(Exception e) {
			LOGGER.info("Error enviar PDF Solicitud a CRM=>"+e);
			e.printStackTrace();
		}
	}

	@Override
	public ByteArrayInputStream crearPDFSolicitud(String numeroCotizacion, String tipoProducto) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		File archivoPDFTemporal = null;
		try {
			/**
			 * 1. Buscar Solicitud y validar estado
			 * *
			 */
			Solicitud solicitud = new Solicitud();
			solicitud = solicitudRepository.findByNumeroCotizacion(numeroCotizacion);
			if(solicitud != null && Integer.parseInt(solicitud.getEstado()) > Integer.parseInt(Constantes.CODIGO_SOLICITUD_FIRMA_FINALIZADA)
					|| solicitud.getDocumentoSolicitud().equals(Constantes.CODIGO_SOLICITUD_POR_TRANSMITIR)) {
			/**
			 * 2. Obtener todos los datos de BD para el PDF (solicitud, asegurado, contratante, producto, dps y beneficiarios)
			 * *
			 */
				SolicitudPDFBean solicitudPDFBean = this.obtenerDatosSolicitudPlantilla(solicitud, tipoProducto);
				
				/**
				 * 3. Crear PDF en temporales
				 * *
				 */
				archivoPDFTemporal = this.generarPDFTemporal(solicitud.getIdSolicitud(), solicitudPDFBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (archivoPDFTemporal != null) {
			FileInputStream fis;
			try {
				fis = new FileInputStream(archivoPDFTemporal);
		        ByteArrayOutputStream bos = new ByteArrayOutputStream();
		        byte[] buf = new byte[1024];
		        try {
		            for (int readNum; (readNum = fis.read(buf)) != -1;) {
		                bos.write(buf, 0, readNum);
		                System.out.println("read " + readNum + " bytes,");
		            }
		        } catch (IOException ex) {
		            //Logger.getLogger(genJpeg.class.getName()).log(Level.SEVERE, null, ex);
		        }
		        byte[] bytes = bos.toByteArray();

				try {
					out.write(bytes);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return new ByteArrayInputStream(out.toByteArray());
		} else {
			return null;
		}
		
	}

	@Override
	public BaseResponseDTO obtenerDocumentoSolicitud(String numeroCotizacion) {
		BaseResponseDTO response = new BaseResponseDTO();
		Solicitud solicitud = new Solicitud();
		solicitud = solicitudRepository.findByNumeroCotizacion(numeroCotizacion);
		Persona persona = personaRepository.findById(solicitud.getIdAsegurado()).get();
		response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
		response.setMensajeRespuesta(persona.getNumeroDocumento());
		return response;
	}
	private void eliminarFilePrivado(String urlPDFFirmaPrivada) {
		try {
			File file = new File(urlPDFFirmaPrivada);
			Files.deleteIfExists(file.toPath());

		} catch (Exception ex) {
			LOGGER.info("ERROR eliminarFilePrivado=> {}", ex.getMessage());
		}
	}
	/*@Override
	public BaseResponseDTO guardarPDF(String numeroCotizacion, MultipartFile file) throws IOException {
		
		String codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_EXITO;
		String mensajeRespuesta = Strings.EMPTY;
		
		BaseResponseDTO respuesta = new BaseResponseDTO();
		
		LocalDateTime ldt = LocalDateTime.now();
		ZonedDateTime zdt = ldt.atZone(ZoneId.of("America/Los_Angeles"));
		String traza = String.valueOf(zdt.toInstant().toEpochMilli());
		LOGGER.info("[" + traza + "] Entra a SolicitudServiceImpl.obtenerDetalleSolicitud");
		
		Solicitud solicitud = solicitudRepository.findByNumeroCotizacion(numeroCotizacion);
		Persona personaAsegurado = personaRepository.findById(solicitud.getPersonaByIdAsegurado().getIdPersona()).get();
		
		//Grabar solicitud PDFS
		StringBuilder sb = new StringBuilder();
		sb.append("prueba02");
		//sb.append(personaAsegurado.getNumeroDocumento());
		//sb.append("_");
		//sb.append(solicitud.getNumeroPropuesta());
		sb.append(".pdf");
		
		String nombreArchivoSolicitud = sb.toString();
		//String urlPrivadaSolicitud = this.rutaPDFSolicitudPrivada + nombreArchivoSolicitud;
		String urlPrivadaSolicitud = "C:\\Users\\NB\\Documents\\interseguro\\" + nombreArchivoSolicitud;
		
		System.out.println("***********************************************************");
		System.out.println(urlPrivadaSolicitud);
		//String urlPublicaSolicitud = this.rutaPDFSolicitudPublica + nombreArchivoSolicitud;
		
		File convFile = new File(System.getProperty("java.io.tmpdir")+"/prueba01.pdf");
		file.transferTo(convFile);
		Files.copy(convFile.toPath(), (new File(urlPrivadaSolicitud)).toPath(), StandardCopyOption.REPLACE_EXISTING);
		
		LOGGER.info("nombreSolicitud=>"+nombreArchivoSolicitud);
		LOGGER.info("SolicituDigital PDF copiado a=>"+urlPrivadaSolicitud);
		
		solicitud.setNombreArchivoSolicitud(nombreArchivoSolicitud);
		
		solicitudRepository.save(solicitud);
		
		String tipoDocumentoCRM = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_TIPO_DOCUMENTO, personaAsegurado.getTipoDocumento(), Constantes.MULTITABLA_COLUMNA_VALOR_CRM);
		String numeroDocumento = personaAsegurado.getNumeroDocumento();
		
		this.enviarPDFCRM(urlPrivadaSolicitud, tipoDocumentoCRM, numeroDocumento, nombreArchivoSolicitud, nombreArchivoSolicitud);
		
		
		respuesta.setCodigoRespuesta(codigoRespuesta);
		respuesta.setMensajeRespuesta(mensajeRespuesta);
		return respuesta;
	}*/
	
	public boolean hasConsentimientoOpcional(Persona persona, String idUsuario) {
		boolean consentimiento = false;
		Map<String, Object> response = null;

		if(persona != null) {
			TipoDocumentoADN tipoDocumento = TipoDocumentoADN.parse(Integer.parseInt(persona.getTipoDocumento()));

			ConsentimientoUniversalRequest request = new ConsentimientoUniversalRequest();
			request.setTipoDocumento(tipoDocumento.equals(TipoDocumentoADN.DNI) ? Constantes.CONSENTIMIENTO_TIPODDOCUMENTO_DNI
						: (tipoDocumento.equals(TipoDocumentoADN.CARNE_EXTRANJERIA) ? Constantes.CONSENTIMIENTO_TIPODDOCUMENTO_CE
						: Constantes.CONSENTIMIENTO_TIPODDOCUMENTO_RUC));
			request.setNumeroDocumento(persona.getNumeroDocumento());
			request.setUsuario(idUsuario);
			request.setIdConfiguracion(Constantes.IDTRATAMIENTOUNIVERSALINTERCORP); // 10 - Envíen publicidad, promociones y ofertas de los productos y/o servicios.
			
			LOGGER.info("hasConsentimientoOpcional - Intercorp 10 [" + request + "] Request => " + gson.toJson(request));
			response = crmRestClient.getConsentimientoUniversal(request);
			LOGGER.info("hasConsentimientoOpcional - Intercorp 10 [" + request + "] Response => " + gson.toJson(response));

			if (null != response && !response.isEmpty() && response.containsKey("ind_consentimiento")
				&& !Objects.isNull(response.get("ind_consentimiento")) && StringUtils.equals(
				response.get("ind_consentimiento").toString(), IndicadorConsentimiento.SI.getCodigo())) {
				consentimiento = true;
			} else {
				request.setIdConfiguracion(Constantes.IDTRATAMIENTOUNIVERSALINTERCORP_ESTUDIOS_MERCADO); // 9 - Realicen estudios de mercado y perfiles de compra.
				LOGGER.info("hasConsentimientoOpcional - Intercorp 9 [" + request + "] Request => " + gson.toJson(request));
				response = crmRestClient.getConsentimientoUniversal(request);
				LOGGER.info("hasConsentimientoOpcional - Intercorp 9 [" + request + "] Response => " + gson.toJson(response));

				if (null != response && !response.isEmpty() && response.containsKey("ind_consentimiento")
					&& !Objects.isNull(response.get("ind_consentimiento")) && StringUtils.equals(
					response.get("ind_consentimiento").toString(), IndicadorConsentimiento.SI.getCodigo())) {
					consentimiento = true;
				}
			}
		}
		return consentimiento;
	}
	 
}
