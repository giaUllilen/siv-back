package pe.interseguro.siv.admin.transactional.service.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.web.util.UriTemplate;

import com.google.gson.Gson;

import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import pe.interseguro.siv.admin.transactional.service.AdnService;
import pe.interseguro.siv.common.bean.DocumentoADN;
import pe.interseguro.siv.common.bean.DocumentoADNFamiliar;
import pe.interseguro.siv.common.bean.DocumentoADNPoliza;
import pe.interseguro.siv.common.bean.DocumentoADNReferido;
import pe.interseguro.siv.common.bean.GenericoComboBean;
import pe.interseguro.siv.common.dto.request.ADNAutoguardadoRequestDTO;
import pe.interseguro.siv.common.dto.request.ADNClienteRequestDTO;
import pe.interseguro.siv.common.dto.request.ADNFirmaRequestDTO;
import pe.interseguro.siv.common.dto.request.ADNPolizaRequestDTO;
import pe.interseguro.siv.common.dto.request.ADNPotencialRequestDTO;
import pe.interseguro.siv.common.dto.request.ADNReferidoRequestDTO;
import pe.interseguro.siv.common.dto.request.ADNRegistroFamiliarRequestDTO;
import pe.interseguro.siv.common.dto.request.ADNRegistroReferidoRequestDTO;
import pe.interseguro.siv.common.dto.request.ADNRegistroRequestDTO;
import pe.interseguro.siv.common.dto.request.ADNUsuarioRequestDTO;
import pe.interseguro.siv.common.dto.request.TokenRequestDTO;
import pe.interseguro.siv.common.dto.response.ADNAutoguardadoResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNClienteDTResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNConsentimientoAceptadoResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNConsultaClienteDTResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNConsultaLdpdpResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNConsultaPolizaBUCResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNConsultaPolizaResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNConsultaPotencialDTResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNConsultaUsuarioDTResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNFirmaResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNInicializacionParametroResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNInicializacionResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNInicializacionViaCobroResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNPolizaBUCResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNPolizaResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNPotencialDTResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNRegistroReferidoResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNRegistroResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNReglamentoFamiliarResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNReglamentoPlanFuturoBaseResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNReglamentoPlanFuturoCapitalResponsetDTO;
import pe.interseguro.siv.common.dto.response.ADNReglamentoPlanFuturoRespaldoResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNReglamentoPlanFuturoResponsetDTO;
import pe.interseguro.siv.common.dto.response.ADNReglamentoReferidoResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNReglamentoResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNReglamentoTitularDireccionResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNReglamentoTitularResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNUsuarioDTResponseDTO;
import pe.interseguro.siv.common.dto.response.BaseResponseDTO;
import pe.interseguro.siv.common.enums.IndicadorConsentimiento;
import pe.interseguro.siv.common.enums.TablaEnum;
import pe.interseguro.siv.common.enums.TipoDocumentoADN;
import pe.interseguro.siv.common.enums.TipoEnvioConsentimiento;
import pe.interseguro.siv.common.exception.ErrorResourceDTO;
import pe.interseguro.siv.common.exception.SivSOAException;
import pe.interseguro.siv.common.exception.SivTXException;
import pe.interseguro.siv.common.persistence.db.acsele.bean.PolizaAcsele;
import pe.interseguro.siv.common.persistence.db.acsele.bean.PolizaEstado;
import pe.interseguro.siv.common.persistence.db.acsele.repository.AcseleRepository;
import pe.interseguro.siv.common.persistence.db.mysql.domain.Adn;
import pe.interseguro.siv.common.persistence.db.mysql.domain.Autoguardado;
import pe.interseguro.siv.common.persistence.db.mysql.domain.Familiar;
import pe.interseguro.siv.common.persistence.db.mysql.domain.Multitabla;
import pe.interseguro.siv.common.persistence.db.mysql.domain.Persona;
import pe.interseguro.siv.common.persistence.db.mysql.domain.PersonaConsentimiento;

import pe.interseguro.siv.common.persistence.db.mysql.domain.PlanFuturo;
import pe.interseguro.siv.common.persistence.db.mysql.domain.Poliza;
import pe.interseguro.siv.common.persistence.db.mysql.domain.Referido;
import pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudViaCobro;
import pe.interseguro.siv.common.persistence.db.mysql.repository.AdnRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.AutoguardadoRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.FamiliarRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.MultitablaRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.PersonaRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.PersonaConsentimientoRepository;

import pe.interseguro.siv.common.persistence.db.mysql.repository.PlanFuturoRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.PolizaRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.ReferidoRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.SolicitudRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.SolicitudViaCobroRepository;
import pe.interseguro.siv.common.persistence.rest.bupo.BupoRestClient;
import pe.interseguro.siv.common.persistence.rest.bupo.response.ValidarProspectoAsignacionResponse;
import pe.interseguro.siv.common.persistence.rest.consentimiento.ConsentDetail;
import pe.interseguro.siv.common.persistence.rest.consentimiento.ConsentimientoDireccion;
import pe.interseguro.siv.common.persistence.rest.consentimiento.ConsentimientoRequest;
import pe.interseguro.siv.common.persistence.rest.consentimiento.ConsentimientoUniversalRequest;
import pe.interseguro.siv.common.persistence.rest.consentimiento.ConsentimientoV2Request;
import pe.interseguro.siv.common.persistence.rest.consentimiento.CreateConsentimiento;
import pe.interseguro.siv.common.persistence.rest.consentimiento.AdditionalInfo;
import pe.interseguro.siv.common.persistence.rest.crm.CrmRestClient;
import pe.interseguro.siv.common.persistence.rest.crm.request.DatoContactoRequest;
import pe.interseguro.siv.common.persistence.rest.crm.request.UpdateDetRequest;
import pe.interseguro.siv.common.persistence.rest.crm.request.UpdateRequest;
import pe.interseguro.siv.common.persistence.rest.crm.response.GetConsentLogDetailLog;
import pe.interseguro.siv.common.persistence.rest.crm.response.GetConsentLogResponse;
import pe.interseguro.siv.common.persistence.rest.global.GlobalRestClient;
import pe.interseguro.siv.common.persistence.rest.global.request.ActualizarContactoVtigerRequest;
import pe.interseguro.siv.common.persistence.rest.global.request.InsertarPersonaDocumentoRequest;
import pe.interseguro.siv.common.persistence.rest.global.request.ObtenerDetallePolizaRequest;
import pe.interseguro.siv.common.persistence.rest.global.request.ObtenerPolizaRequest;
import pe.interseguro.siv.common.persistence.rest.global.request.ObtenerReglasAsigClienteRequest;
import pe.interseguro.siv.common.persistence.rest.global.request.PolizasCanceladasRequest;
import pe.interseguro.siv.common.persistence.rest.global.request.UpdateTrazabilidadConsentimientoRequest;
import pe.interseguro.siv.common.persistence.rest.global.request.UploadArchivoRequest;
import pe.interseguro.siv.common.persistence.rest.global.response.ActualizarContactoVtigerResponse;
import pe.interseguro.siv.common.persistence.rest.global.response.ObtenerAgenteResponse;
import pe.interseguro.siv.common.persistence.rest.global.response.ObtenerConsultaAsigAcseleResponse;
import pe.interseguro.siv.common.persistence.rest.global.response.ObtenerPersonaDocumentoResponse;
import pe.interseguro.siv.common.persistence.rest.global.response.ObtenerPolizaResponse;
import pe.interseguro.siv.common.persistence.rest.global.response.ObtenerReglasAsigClienteResponse;
import pe.interseguro.siv.common.persistence.rest.global.response.ObtenerStatusResponse;
import pe.interseguro.siv.common.persistence.rest.global.response.PolizaDetalleResponse;
import pe.interseguro.siv.common.persistence.rest.global.response.PolizaResponse;
import pe.interseguro.siv.common.persistence.rest.global.response.PolizasCanceladasResponse;
import pe.interseguro.siv.common.persistence.rest.global.response.UpdateTrazabilidadConsentimientoResponse;
import pe.interseguro.siv.common.persistence.rest.global.response.UploadArchivoResponse;
import pe.interseguro.siv.common.persistence.rest.global.response.obtenerPersonaResponse;
import pe.interseguro.siv.common.persistence.rest.indenova.IndenovaRestClient;
import pe.interseguro.siv.common.persistence.rest.indenova.request.CrearCircuitoDocRequest;
import pe.interseguro.siv.common.persistence.rest.indenova.request.CrearCircuitoOptionRequest;
import pe.interseguro.siv.common.persistence.rest.indenova.request.CrearCircuitoRequest;
import pe.interseguro.siv.common.persistence.rest.indenova.request.CrearCircuitoSignerRequest;
import pe.interseguro.siv.common.persistence.rest.indenova.response.CrearCircuitoResponse;
import pe.interseguro.siv.common.persistence.rest.interseguro.InterseguroRestClient;
import pe.interseguro.siv.common.persistence.rest.interseguro.dto.Adjuntos;
import pe.interseguro.siv.common.persistence.rest.interseguro.dto.Remitente;
import pe.interseguro.siv.common.persistence.rest.interseguro.request.EnviarCorreoRequest;
import pe.interseguro.siv.common.persistence.rest.interseguro.request.EnviarCorreoRequestNuevo;
import pe.interseguro.siv.common.persistence.rest.interseguro.request.EnviarSmsRequest;
import pe.interseguro.siv.common.persistence.rest.interseguro.response.EnviarSmsNotificacionesResponse;
import pe.interseguro.siv.common.persistence.rest.interseguro.response.EnviarSmsResponse;
import pe.interseguro.siv.common.persistence.rest.interseguro.response.ObtenerDatosClienteResponse;
import pe.interseguro.siv.common.persistence.rest.niubiz.response.BaseResponse;
import pe.interseguro.siv.common.persistence.rest.vtigger.VTigerRestClient;
import pe.interseguro.siv.common.persistence.rest.vtigger.request.ActualizarContactoRequest;
import pe.interseguro.siv.common.persistence.rest.vtigger.request.ConsultaClienteRequest;
import pe.interseguro.siv.common.persistence.rest.vtigger.request.ConsultaPotencialRequest;
import pe.interseguro.siv.common.persistence.rest.vtigger.request.ConsultaUsuarioPorCorreoRequest;
import pe.interseguro.siv.common.persistence.rest.vtigger.request.ConsultaUsuarioRequest;
import pe.interseguro.siv.common.persistence.rest.vtigger.request.CrearContactoRequest;
import pe.interseguro.siv.common.persistence.rest.vtigger.response.ActualizarContactoResponse;
import pe.interseguro.siv.common.persistence.rest.vtigger.response.ConsultaClienteResponse;
import pe.interseguro.siv.common.persistence.rest.vtigger.response.ConsultaClienteResultResponse;
import pe.interseguro.siv.common.persistence.rest.vtigger.response.ConsultaPotencialResponse;
import pe.interseguro.siv.common.persistence.rest.vtigger.response.ConsultaPotencialResultResponse;
import pe.interseguro.siv.common.persistence.rest.vtigger.response.ConsultaUsuarioPorCorreoResponse;
import pe.interseguro.siv.common.persistence.rest.vtigger.response.ConsultaUsuarioResponse;
import pe.interseguro.siv.common.persistence.rest.vtigger.response.ConsultaUsuarioResultResponse;
import pe.interseguro.siv.common.persistence.rest.vtigger.response.CrearContactoResponse;
import pe.interseguro.siv.common.util.Constantes;
import pe.interseguro.siv.common.util.DateUtil;
import pe.interseguro.siv.common.util.IXDocReportUtil;
import pe.interseguro.siv.common.util.Utilitarios;

/**
 * Implementación de la transacción del ADN
 * 
 * @author ti-is
 *
 */
@Service("adnService")
@Transactional
public class AdnServiceImpl implements AdnService {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private MultitablaRepository multitablaRepository;

	@Autowired
	private AcseleRepository acseleRepository;

	@Autowired
	private PersonaRepository personaRepository;

	@Autowired
	private PersonaConsentimientoRepository personaConsentimientoRepository;



	@Autowired
	private SolicitudViaCobroRepository viaCobroRepository;

	@Autowired
	private InterseguroRestClient interseguroRestClient;

	@Autowired
	private VTigerRestClient vtigerRestClient;

	@Autowired
	private CrmRestClient crmRestClient;

	@Autowired
	private AdnRepository adnRepository;

	@Autowired
	private FamiliarRepository familiarRepository;

	@Autowired
	private ReferidoRepository referidoRepository;

	@Autowired
	private PlanFuturoRepository planFuturoRepository;

	@Autowired
	private AutoguardadoRepository autoguardadoRepository;

	@Autowired
	private IndenovaRestClient indenovaRestClient;

	@Autowired
	private GlobalRestClient globalRestClient;

	@Autowired
	private BupoRestClient bupoRestClient;

	@Autowired
	private SolicitudRepository solicitudRepository;



	/*@Autowired
	private EventLogRepository logRepository;*/

	@Autowired
	private PolizaRepository polizaRepository;

	@Value("${spring.profiles.active}")
	private String activeProfile;

	@Value("#{ environment['persistence.rest.client.interseguro.correo.enviarCorreo.remitente'] }")
	private String enviarCorreoRemitente;

	@Value("#{ environment['persistence.rest.client.interseguro.correo.enviarCorreo.remitente.display'] }")
	private String enviarCorreoRemitenteDisplay;

	@Value("#{ environment['persistence.rest.client.interseguro.correo.enviarCorreo.destinatario'] }")
	private String enviarCorreoDestinatario;

	@Value("#{ environment['persistence.rest.client.crm.cotizadorFlex.servidor'] }")
	private String urlCotizadorFlex;

	@Value("#{ environment['persistence.rest.client.crm.cotizadorVidaIntermedio.servidor'] }")
	private String urlCotizadorVida;

	@Value("#{ environment['ruta.plantilla.doc.linux'] }")
	private String rutaPlantillaDocLinux;

	@Value("#{ environment['ruta.pdf.privada'] }")
	private String rutaPDFPrivada;

	@Value("#{ environment['ruta.pdf.publica'] }")
	private String rutaPDFPublica;

	@Value("#{ environment['server.port'] }")
	private String serverPort;

	@Value("#{ environment['url.adn.pdf']}")
	private String urlAdnConsentimiento;

	@Value("#{ environment['asunto.envioCorreo.consentimiento']}")
	private String asuntoCorreoConsentimiento;

	@Value("#{ environment['asunto.envioCorreo.consentimiento.pdf']}")
	private String asuntoCorreoConsentimientoPdf;

	@Value("#{ environment['persistence.rest.client.interseguro.correo.enviarCorreo.solicitud.email.pruebas'] }")
	private String enviarCorreoSolicitudEmailPruebas;

	@Value("#{ environment['persistence.rest.client.interseguro.correo.enviarCorreo.solicitud.remitente'] }")
	private String enviarCorreoSolicitudRemitente;

	@Value("#{ environment['persistence.rest.client.interseguro.correo.enviarCorreo.solicitud.remitente.display'] }")
	private String enviarCorreoSolicitudRemitenteDisplay;

	@Value("#{ environment['persistence.rest.client.interseguro.correo.enviarCorreo.solicitud.asunto'] }")
	private String enviarCorreoSolicitudAsunto;

	@Value("#{ environment['persistence.rest.client.interseguro.correo.enviarCorreo.adn.asunto'] }")
	private String enviarCorreoAdnAsunto;

	@Value("#{ environment['persistence.consentimiento.url'] }")
	private String endpointConsentimiento;

	@Value("#{ environment['persistence.sme.process.enviolink'] }")
	private String smeEnvioLink;

	@Value("#{ environment['persistence.sme.process.enviopdf'] }")
	private String smeEnvioPDF;

	@Value("#{ environment['persistence.consentimiento.envviolink.maxretries'] }")
	private String smeEnvioLinkMaxRetires;

	@Value("#{ environment['persistence.rest.client.sme.generacionpdf.rutaword'] }")
	private String smeGenerarPDFRutaWord;

	@Value("#{ environment['persistence.rest.client.sme.generacionpdf.plantilla.intercorp.accepted'] }")
	private String smeGenerarPDFPlantillaIntercorpAccepted;

	@Value("#{ environment['persistence.rest.client.sme.generacionpdf.plantilla.intercorp.noaccepted'] }")
	private String smeGenerarPDFPlantillaIntercorpNoAccepted;

	@Value("#{ environment['persistence.rest.client.sme.generacionpdf.plantilla.accepted'] }")
	private String smeGenerarPDFPlantillaAccepted;

	@Value("#{ environment['persistence.rest.client.sme.generacionpdf.plantilla.noaccepted'] }")
	private String smeGenerarPDFPlantillaNoAccepted;

	@Value("#{ environment['persistence.rest.client.sme.generacionpdf.rutapdf'] }")
	private String smeGenerarPDFRutaPDF;

	@Value("#{ environment['persistence.rest.client.sme.generacionpdf.prefijopdf'] }")
	private String smeGenerarPDFPrefijo;

	@Value("#{ environment['url.adn.front'] }")
	private String urlAdnFront;

	@Value("#{ environment['persistence.rest.client.sme.generacionpdf.tempPDFPath'] }")
	private String tempPDFConentimientoPath;

	@Value("#{ environment['ruta.pdf.consentimiento'] }")
	private String rutaPDFConsentimiento;

	@Value("#{ environment['ruta.pdf.consentimiento.privada'] }")
	private String rutaPDFConsentimientoPrivada;

	@Value("#{ environment['persistence.rest.client.global.source.mail'] }")
	private String sourceConsentimientoMail;

	@Value("#{ environment['persistence.rest.client.global.source.sms'] }")
	private String sourceConsentimientoSms;

	private Gson gson = new Gson();
	// private Gson gson = new Gson();

	@Override
	public ADNInicializacionResponseDTO obtenerInicializacion(String idUsuario, String device, String os) {

		String traza = Utilitarios.trazaLog();
		LOGGER.info("[" + traza + "] Entra a AdnServiceImpl.obtenerInicializacion");
		// -- Logica
		List<Multitabla> listaParametroAdn = multitablaRepository
				.findByCodigoTabla(TablaEnum.TABLA_PARAMETRO_ADN.getCodigoTabla());
		List<Multitabla> listaParametroSolicitud = multitablaRepository
				.findByCodigoTabla(TablaEnum.TABLA_CONFIGURACION_SOLICITUD.getCodigoTabla());

		Double porcentajeIngresoIni = Double
				.valueOf(listaParametroAdn.get(Constantes.PARAMETRO_ADN_PORCENTAJE_ING_INI).getValorAux());
		Double porcentajeIngresoFin = Double
				.valueOf(listaParametroAdn.get(Constantes.PARAMETRO_ADN_PORCENTAJE_ING_FIN).getValorAux());
		Double seguroVidaLeyRegla = Double
				.valueOf(listaParametroAdn.get(Constantes.PARAMETRO_ADN_SEGURO_VIDA_LEY_REGLA).getValorAux());

		Integer porcentajeIngresoDefault = Integer
				.valueOf(listaParametroAdn.get(Constantes.PARAMETRO_ADN_PORCENTAJE_ING_DEFAULT).getValorAux());

		String pdfEvidencias = String
				.valueOf(listaParametroAdn.get(Constantes.PARAMETRO_ADN_URL_PDF_EVIDENCIAS).getValorAux());
		String pdfEducacion = String
				.valueOf(listaParametroAdn.get(Constantes.PARAMETRO_ADN_URL_PDF_EDUCACION).getValorAux());
		String pdfProyectos = String
				.valueOf(listaParametroAdn.get(Constantes.PARAMETRO_ADN_URL_PDF_PROYECTOS).getValorAux());
		String pdfJubilacion = String
				.valueOf(listaParametroAdn.get(Constantes.PARAMETRO_ADN_URL_PDF_JUBILACION).getValorAux());

		String itemParentezco = Strings.EMPTY;
		String itemVinculoAsegurado = Strings.EMPTY;
		String itemVinculoAseguradoRuc = Strings.EMPTY;

		if (listaParametroSolicitud != null && listaParametroSolicitud.size() > 0) {
			itemParentezco = String
					.valueOf(listaParametroSolicitud.get(Constantes.PARAMETRO_SOLICITUD_PARENTEZCO).getValorAux());
			itemVinculoAsegurado = String.valueOf(
					listaParametroSolicitud.get(Constantes.PARAMETRO_SOLICITUD_VINCULO_ASEGURADO).getValorAux());
			itemVinculoAseguradoRuc = String.valueOf(
					listaParametroSolicitud.get(Constantes.PARAMETRO_SOLICITUD_VINCULO_ASEGURADO_RUC).getValorAux());
		}

		// -- Respuesta
		ADNInicializacionResponseDTO response = new ADNInicializacionResponseDTO();
		response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
		response.setMensajeRespuesta(
				Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_EXITO));
		response.setTipoDocumento(
				getDatosTabla(multitablaRepository.findByCodigoTabla(TablaEnum.TABLA_TIPO_DOCUMENTO.getCodigoTabla())));
		response.setGenero(
				getDatosTabla(multitablaRepository.findByCodigoTabla(TablaEnum.TABLA_GENERO.getCodigoTabla())));
		response.setProfesion(
				getDatosTabla(multitablaRepository.findByCodigoTabla(TablaEnum.TABLA_PROFESION.getCodigoTabla())));
		response.setActividadEconomica(getDatosTabla(
				multitablaRepository.findByCodigoTabla(TablaEnum.TABLA_ACTIVIDAD_ECONOMICA.getCodigoTabla())));
		response.setLugarTrabajo(
				getDatosTabla(multitablaRepository.findByCodigoTabla(TablaEnum.TABLA_LUGAR_TRABAJO.getCodigoTabla())));
		response.setFumador(
				getDatosTabla(multitablaRepository.findByCodigoTabla(TablaEnum.TABLA_FUMADOR.getCodigoTabla())));
		response.setTipoRelacion(
				getDatosTabla(multitablaRepository.findByCodigoTabla(TablaEnum.TABLA_TIPO_RELACION.getCodigoTabla())));

		// Solicitud
		response.setEstadoCivil(
				getDatosTabla(multitablaRepository.findByCodigoTabla(TablaEnum.TABLA_ESTADO_CIVIL.getCodigoTabla())));
		response.setNacionalidad(
				getDatosTabla(multitablaRepository.findByCodigoTabla(TablaEnum.TABLA_NACIONALIDAD.getCodigoTabla())));
		response.setTipoDireccion(
				getDatosTabla(multitablaRepository.findByCodigoTabla(TablaEnum.TABLA_TIPO_DIRECCION.getCodigoTabla())));
		response.setDepartamento(getDatosTablaAuxiliar(
				multitablaRepository.findByCodigoTabla(TablaEnum.TABLA_DEPARTAMENTO.getCodigoTabla()),
				Constantes.MULTITABLA_COLUMNA_VALOR_CRM));
		response.setProvincia(getDatosTablaAuxiliar(
				multitablaRepository.findByCodigoTabla(TablaEnum.TABLA_PROVINCIA.getCodigoTabla()),
				Constantes.MULTITABLA_COLUMNA_VALOR_CRM));
		response.setDistrito(
				getDatosTablaAuxiliar(multitablaRepository.findByCodigoTabla(TablaEnum.TABLA_DISTRITO.getCodigoTabla()),
						Constantes.MULTITABLA_COLUMNA_VALOR_CRM));
		response.setTipoRelacionSolicitud(getDatosTabla(
				multitablaRepository.findByCodigoTablaSinEstado(TablaEnum.TABLA_TIPO_RELACION.getCodigoTabla())));

		response.setParametro(new ADNInicializacionParametroResponseDTO(porcentajeIngresoIni, porcentajeIngresoFin,
				seguroVidaLeyRegla, porcentajeIngresoDefault, pdfEvidencias, pdfEducacion, pdfProyectos, pdfJubilacion,
				itemParentezco, itemVinculoAsegurado, itemVinculoAseguradoRuc));

		List<ADNInicializacionViaCobroResponseDTO> viasCobro = new ArrayList<>();
		for (SolicitudViaCobro v : viaCobroRepository.findAll()) {
			viasCobro.add(new ADNInicializacionViaCobroResponseDTO(v.getTipoViaCobro(), v.getCodigoViaCobro(),
					v.getNombreViaCobro(), v.getTipoCuenta(), v.getMascara(), v.getLongitud(), v.getCcv()));
		}
		response.setViasCobro(viasCobro);
		Gson gson = new Gson();
		List<String> observaciones = new ArrayList<String>();
		/*logRepository.save(new EventLog("", response.getCodigoRespuesta(), "/adns/reglamentos",
				response.getMensajeRespuesta(), gson.toJson(response.getObjErrorResource()), gson.toJson(observaciones),
				idUsuario, device, os));*/

		LOGGER.info("Salio AdnServiceImpl#obtenerInicializacion()");
		return response;
	}

	/**
	 * Obtener datos de una tabla de multitabla
	 * 
	 * @param lista
	 * @return
	 */
	private List<GenericoComboBean> getDatosTabla(List<Multitabla> lista) {

		List<GenericoComboBean> listaGenerica = new ArrayList<GenericoComboBean>();
		GenericoComboBean item = null;

		for (Multitabla multitabla : lista) {
			item = new GenericoComboBean();
			item.setCodigo(multitabla.getCodigo());
			item.setDescripcion(multitabla.getValor());

			listaGenerica.add(item);
		}

		return listaGenerica;
	}

	/**
	 * Obtener datos de una tabla de multitabla con valor CRM
	 * 
	 * @param lista
	 * @return
	 */
	private List<GenericoComboBean> getDatosTablaAuxiliar(List<Multitabla> lista, String codigoAuxiliar) {

		List<GenericoComboBean> listaGenerica = new ArrayList<GenericoComboBean>();
		GenericoComboBean item = null;

		for (Multitabla multitabla : lista) {
			item = new GenericoComboBean();
			item.setCodigo(multitabla.getCodigo());
			item.setDescripcion(multitabla.getValor());
			switch (codigoAuxiliar) {
			case Constantes.MULTITABLA_COLUMNA_VALOR_CRM:
				item.setCodigoAuxiliar(multitabla.getValorCrm());
				break;
			case Constantes.MULTITABLA_COLUMNA_VALOR_AUXILIAR:
				item.setCodigoAuxiliar(multitabla.getValorAux());
				break;
			}

			listaGenerica.add(item);
		}

		return listaGenerica;
	}

	private ADNReglamentoResponseDTO obtenerInformacionCliente(String tipoDocCliente, String tipoDocumentoCRM,
			String numDocCliente, String idUsuario, String correoUsuario) {
		String traza = Utilitarios.trazaLog();
		traza = traza + "-" + numDocCliente;
		LOGGER.info("[" + traza + "] Entra a AdnServiceImpl.obtenerInformacionCliente");

		ADNReglamentoResponseDTO response = new ADNReglamentoResponseDTO();
		ADNReglamentoTitularResponseDTO titular = new ADNReglamentoTitularResponseDTO();
		ADNReglamentoPlanFuturoResponsetDTO planFuturo = new ADNReglamentoPlanFuturoResponsetDTO();

		StringBuilder usuarioCRM = new StringBuilder();
		usuarioCRM.append("DINTERSEGURO\\");
		usuarioCRM.append(idUsuario);

		boolean buscarDigital = true;
		boolean buscarPersona = true;

		DatoContactoRequest requestContacto = new DatoContactoRequest();
		requestContacto.setDomainName(usuarioCRM.toString());
		requestContacto.setTipoDocumento(tipoDocumentoCRM);
		requestContacto.setNumeroDocumento(numDocCliente);
		List<String> infAdicional = new ArrayList<>();

		Persona persona = personaRepository.findByTipoNumeroDocumento(tipoDocCliente, numDocCliente);
		// Persona persona = personaRepository.findByNumeroDocumento(numDocCliente);
		// System.out.println(persona.getTipoDocumento());
		if (persona != null) {
			LOGGER.info("[" + traza + "] Cliente ya registrado en base de datos ADN.");
			infAdicional.add("Cliente ya registrado en base de datos ADN.");
			titular.setNumeroDocumento(persona.getNumeroDocumento());
			if (persona.getTipoDocumento() != null)
				titular.setTipoDocumento(persona.getTipoDocumento());
			if (persona.getNombres() != null)
				titular.setNombres(persona.getNombres().trim());
			if (persona.getApellidoPaterno() != null)
				titular.setApellidoPaterno(persona.getApellidoPaterno().trim());
			if (persona.getApellidoMaterno() != null)
				titular.setApellidoMaterno(persona.getApellidoMaterno().trim());
			if (persona.getGenero() != null)
				titular.setGenero(persona.getGenero());
			if (persona.getCelular() != null)
				titular.setCelular(persona.getCelular().toString());
			if (persona.getActividadEconomica() != null)
				titular.setActividadEconomica(persona.getActividadEconomica());
			if (persona.getProfesion() != null)
				titular.setProfesion(persona.getProfesion());
			if (persona.getFumador() != null)
				titular.setFumador(persona.getFumador());
			if (persona.getCorreo() != null)
				titular.setCorreo(persona.getCorreo().trim());
			if (persona.getFechaNacimiento() != null) {
				try {
					titular.setFechaNacimiento(
							DateUtil.dateToString(persona.getFechaNacimiento(), DateUtil.FORMATO_DIA_DDMMYYYY));
					LOGGER.info("persona.getFechaNacimiento()" + persona.getFechaNacimiento());
					LOGGER.info("titular.getFechaNacimiento()" + titular.getFechaNacimiento());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (persona.getTipoCasa() != null)
				titular.setTipoCasa(persona.getTipoCasa());
			if (persona.getTieneVehiculo() != null)
				titular.setTieneVehiculo(persona.getTieneVehiculo());
			if (persona.getTieneAfp() != null)
				titular.setTieneAfp(persona.getTieneAfp());
			if (persona.getTieneSeguroVida() != null)
				titular.setTieneSeguro(persona.getTieneSeguroVida());
		}

		if (titular.getApellidoPaterno() != null && titular.getApellidoPaterno().trim() != Strings.EMPTY
				&& titular.getApellidoMaterno() != null && titular.getApellidoMaterno().trim() != Strings.EMPTY
				&& titular.getCelular() != null && titular.getCelular().trim() != Strings.EMPTY
				&& titular.getCorreo() != null && titular.getCorreo().trim() != Strings.EMPTY
				&& titular.getFechaNacimiento() != null && titular.getFechaNacimiento().trim() != Strings.EMPTY) {
			LOGGER.info("[" + traza + "] No es necesario buscar cliente en Digital.");
			buscarPersona = false;
		}
		LOGGER.info(" buscarPersona =>" + buscarPersona);
		if (buscarPersona) {
			LOGGER.info("[" + traza + "] Cliente buscado en base de Digital.");
			infAdicional.add("Cliente buscado en base de vis_data.");
			// ObtenerDatosClienteResponse datosCliente = null;
			ObtenerPolizaRequest requestPersona = new ObtenerPolizaRequest();
			requestPersona.setTipoDoc("0" + tipoDocCliente);
			requestPersona.setNroDoc(numDocCliente);
			obtenerPersonaResponse datosCliente = globalRestClient.obtenerPersona(requestPersona);
			System.out.println(gson.toJson(datosCliente));
			if (!datosCliente.getID_PERSONA().equals("")) {
				if (titular.getNombres() == null || titular.getNombres() == "") {
					if (datosCliente.getNOMBRE() != null || datosCliente.getNOMBRE() != "") {
						titular.setNombres(datosCliente.getNOMBRE().trim());
					}
				}
				if (titular.getApellidoPaterno() == null || titular.getApellidoPaterno() == "") {
					if (datosCliente.getAPELLIDO_PATERNO() != null || datosCliente.getAPELLIDO_PATERNO() != "") {
						titular.setApellidoPaterno(datosCliente.getAPELLIDO_PATERNO().trim());
					}

				}
				if (titular.getApellidoMaterno() == null || titular.getApellidoMaterno() == "") {
					if (datosCliente.getAPELLIDO_MATERNO() != null && datosCliente.getAPELLIDO_MATERNO() != "") {
						titular.setApellidoMaterno(datosCliente.getAPELLIDO_MATERNO().trim());
					}
				}
				if (titular.getCelular() == null || titular.getCelular() == "") {

					if (datosCliente.getTELEFONO_BUC() != null || datosCliente.getTELEFONO_BUC() != "") {
						titular.setCelular(datosCliente.getTELEFONO_BUC());
					}
				}
				if (titular.getCorreo() == null || titular.getCorreo() == "") {
					if (datosCliente.getCORREO_BUC() != null && datosCliente.getCORREO_BUC() != "") {
						titular.setCorreo(datosCliente.getCORREO_BUC());
					}

				}
				if (titular.getFechaNacimiento() == null || titular.getFechaNacimiento() == "") {
//					SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

					if (datosCliente.getFECHA_NACIMIENTO() != null) {
						try {
							titular.setFechaNacimiento(DateUtil.dateToString(datosCliente.getFECHA_NACIMIENTO(),
									DateUtil.FORMATO_DIA_DDMMYYYY));
							LOGGER.info("titular.getFechaNacimiento()" + titular.getFechaNacimiento());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
//						String fechaNacimiento = format.format(datosCliente.getFECHA_NACIMIENTO());
//						titular.setFechaNacimiento(String.valueOf(fechaNacimiento));
					}

				}
				if (titular.getGenero() == null || titular.getGenero() == "") {

					if (datosCliente.getID_GENERO() != null || datosCliente.getID_GENERO() != "") {
						if (datosCliente.getID_GENERO().equals("02")) {
							titular.setGenero("1");
						} else if (datosCliente.getID_GENERO().equals("01")) {
							titular.setGenero("2");
						}

					}

				}

			}

		}

		if (persona != null) {
			LOGGER.info("[" + traza + "] Seteando datos complementarios de cliente ADN.");
			if (persona.getEdadActuarial() != null) {
				LOGGER.info("[" + traza + "] persona.getEdadActuarial() => " + persona.getEdadActuarial());
				infAdicional.add("Edad actuarial = " + persona.getEdadActuarial());
				titular.setEdadActuarial(persona.getEdadActuarial());
			}

			if (persona.getFamiliars() != null && persona.getFamiliars().size() > 0) {
				LOGGER.info("[" + traza + "] Seteando registro de familiares");
				infAdicional.add("Cliente cuenta con familiares encontrados en registros.");
				List<ADNReglamentoFamiliarResponseDTO> listaFamiliares = new ArrayList<ADNReglamentoFamiliarResponseDTO>();
				for (Familiar f : persona.getFamiliars()) {
					ADNReglamentoFamiliarResponseDTO fam = new ADNReglamentoFamiliarResponseDTO();
					fam.setTipoRelacion(f.getTipoRelacion());
					fam.setNombres(f.getNombres());
					fam.setEdad(f.getEdad());
					fam.setCentroLaboral(f.getCentroLaboral());
					listaFamiliares.add(fam);
				}
				response.setFamiliar(listaFamiliares);
			}

			if (persona.getReferidos() != null && persona.getReferidos().size() > 0) {
				LOGGER.info("[" + traza + "] Seteando registro de referidos");
				infAdicional.add("Cliente cuenta con referidos encontrados.");
				List<ADNReglamentoReferidoResponseDTO> listaReferidos = new ArrayList<ADNReglamentoReferidoResponseDTO>();
				for (Referido r : persona.getReferidos()) {
					ADNReglamentoReferidoResponseDTO refe = new ADNReglamentoReferidoResponseDTO();
					refe.setNombres(r.getNombres());
					refe.setTelefono(r.getTelefono());
					listaReferidos.add(refe);
				}
				response.setReferido(listaReferidos);
			}

			PlanFuturo planFuturoDomain = planFuturoRepository.findByIdPersona(persona.getIdPersona());

			if (planFuturoDomain != null) {
				LOGGER.info("[" + traza + "] Seteando registro de plan futuro");
				infAdicional.add("Cliente cuenta con registros de plan futuro.");
				ADNReglamentoPlanFuturoBaseResponseDTO base = new ADNReglamentoPlanFuturoBaseResponseDTO();
				if (planFuturoDomain.getPlanEducacion() != null) {
					base.setPlanEducacion(planFuturoDomain.getPlanEducacion().toString());
				}

				if (planFuturoDomain.getPlanProyecto() != null) {
					base.setPlanProyecto(planFuturoDomain.getPlanProyecto().toString());
				}

				if (planFuturoDomain.getPlanJubilacion() != null) {
					base.setPlanJubilacion(planFuturoDomain.getPlanJubilacion().toString());
				}

				if (planFuturoDomain.getIngresoTitular() != null) {
					base.setIngresoTitular(planFuturoDomain.getIngresoTitular().toString());
				}

				if (planFuturoDomain.getPorcentajeInversion() != null) {
					base.setPorcentajeIngreso(planFuturoDomain.getPorcentajeInversion().toString());
				}

				if (planFuturoDomain.getTotalInversion() != null) {
					base.setTotalIngreso(planFuturoDomain.getTotalInversion().toString());
				}

				if (planFuturoDomain.getAniosProteger() != null) {
					base.setAnioProteccion(planFuturoDomain.getAniosProteger().toString());
				}

				if (planFuturoDomain.getInformacionAdicional() != null) {
					base.setInformacionAdicional(planFuturoDomain.getInformacionAdicional().toString());
				}

				planFuturo.setBase(base);

				ADNReglamentoPlanFuturoRespaldoResponseDTO respaldo = new ADNReglamentoPlanFuturoRespaldoResponseDTO();

				if (planFuturoDomain.getRespaldoAhorrosCheck() != null) {
					respaldo.setAhorroCheck(planFuturoDomain.getRespaldoAhorrosCheck());
				}

				if (planFuturoDomain.getRespaldoAhorros() != null) {
					respaldo.setAhorro(planFuturoDomain.getRespaldoAhorros().toString());
				}

				if (planFuturoDomain.getRespaldoPropiedadesCheck() != null) {
					respaldo.setPropiedadCheck(planFuturoDomain.getRespaldoPropiedadesCheck());
				}

				if (planFuturoDomain.getRespaldoPropiedades() != null) {
					respaldo.setPropiedad(planFuturoDomain.getRespaldoPropiedades().toString());
				}

				if (planFuturoDomain.getRespaldoVehiculosCheck() != null) {
					respaldo.setVehiculoCheck(planFuturoDomain.getRespaldoVehiculosCheck());
				}

				if (planFuturoDomain.getRespaldoVehiculos() != null) {
					respaldo.setVehiculo(planFuturoDomain.getRespaldoVehiculos().toString());
				}

				if (planFuturoDomain.getRespaldoSeguroVidaCheck() != null) {
					respaldo.setSeguroVidaCheck(planFuturoDomain.getRespaldoSeguroVidaCheck());
				}

				if (planFuturoDomain.getRespaldoSeguroVida() != null) {
					respaldo.setSeguroVida(planFuturoDomain.getRespaldoSeguroVida().toString());
				}
				if (planFuturoDomain.getRespaldoSeguroVidaLeyCheck() != null) {
					respaldo.setSeguroVidaLeyCheck(planFuturoDomain.getRespaldoSeguroVidaLeyCheck());
				}

				if (planFuturoDomain.getRespaldoSeguroVidaLey() != null) {
					respaldo.setSeguroVidaLey(planFuturoDomain.getRespaldoSeguroVidaLey().toString());
				}
				planFuturo.setRespaldo(respaldo);

				ADNReglamentoPlanFuturoCapitalResponsetDTO capitalProteger = new ADNReglamentoPlanFuturoCapitalResponsetDTO();

				if (planFuturoDomain.getCapitalProteccion() != null) {
					capitalProteger.setCapitalProteccion(planFuturoDomain.getCapitalProteccion().toString());
				}

				if (planFuturoDomain.getCapitalRespaldo() != null) {
					capitalProteger.setRespaldoEconomico(planFuturoDomain.getCapitalRespaldo().toString());
				}

				if (planFuturoDomain.getCapitalAfpCheck() != null) {
					capitalProteger.setAfpCheck(planFuturoDomain.getCapitalAfpCheck());
				}

				if (planFuturoDomain.getCapitalAfp() != null) {
					capitalProteger.setAfp(planFuturoDomain.getCapitalAfp().toString());
				}

				if (planFuturoDomain.getCapitalTotalProteger() != null) {
					capitalProteger.setTotalCapitalProteger(planFuturoDomain.getCapitalTotalProteger().toString());
				}

				if (planFuturoDomain.getCapitalAdicional() != null) {
					capitalProteger.setAdicional(planFuturoDomain.getCapitalAdicional().toString());
				}

				if (planFuturoDomain.getCapitalTotalProtegerNuevo() != null) {
					capitalProteger.setNuevoCapitalProteger(planFuturoDomain.getCapitalTotalProtegerNuevo().toString());
				}

				planFuturo.setCapitalProteger(capitalProteger);
			}

			LOGGER.info("[" + traza + "] Seteando valores de dominio");
			Adn adnDomain = adnRepository.findByIdPersona(persona.getIdPersona());
			if (adnDomain != null) {
				LOGGER.info("[" + traza + "] adnDomain.getIdAdn => " + adnDomain.getIdAdn());
				response.setIdAdn((int) (long) adnDomain.getIdAdn());
				response.setIdCircuitoFirma(adnDomain.getIdCircuitoFirma());
				response.setExisteAdn(1);
				response.setLdpdpActivo(1);
			}
		}
		Gson gson = new Gson();

		response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
		response.setMensajeRespuesta("OK");

		LOGGER.info("[" + traza + "] consultarPolizasCanceladas Validaciones de polizas canceladas.");
		// List<PolizaEstado> listaPolizasCanceladas = acseleRepository.obtenerPolizaEstado(numDocCliente);
		PolizasCanceladasRequest request = new PolizasCanceladasRequest();
		request.setNumeroDocumento(numDocCliente);
		PolizasCanceladasResponse listaPolizasCanceladas = globalRestClient.consultarPolizasCanceladas(request);
		LOGGER.info("[" + traza + "] consultarPolizasCanceladas => " + gson.toJson(listaPolizasCanceladas.getRespuesta()));
		if (listaPolizasCanceladas.getRespuesta().size() > 0) {
			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ADVERTENCIA);
			List<String> datos = new ArrayList<>();
			for (PolizaEstado p : listaPolizasCanceladas.getRespuesta()) {
				datos.add("Póliza " + p.getNumeropoliza() + " del producto " + p.getProducto() + " en estado "
						+ p.getEstado() + " menor a 12 meses.");
			}
			String mensaje = gson.toJson(datos);
			infAdicional.add(mensaje);
			response.setMensajeRespuesta(mensaje);
		}
		response.setTitular(titular);
		response.setPlanFuturo(planFuturo);
		response.setObservaciones(gson.toJson(infAdicional));
		return response;

	}

	@Override
	public ADNReglamentoResponseDTO obtenerReglamento(String tipoDocCliente, String numDocCliente, String idUsuario,
			String correoUsuario, String device, String os) {
		String traza = Utilitarios.trazaLog();
		traza = traza + "-" + numDocCliente;
		LOGGER.info("[" + traza + "] Entro AdnServiceImpl#obtenerReglamento(tipoDocCliente, numDocCliente, idUsuario)");
		ADNReglamentoResponseDTO response = new ADNReglamentoResponseDTO();
		List<String> observaciones = new ArrayList<String>();
		StringBuilder usuarioCRM = new StringBuilder();
		usuarioCRM.append("DINTERSEGURO\\");
		usuarioCRM.append(idUsuario);
		Gson gson = new Gson();
		System.out.println(response.getExistePlaft());
		response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
		response.setMensajeRespuesta("OK");

		LOGGER.info("[" + traza + "] Obteniendo equivalencia tipo de documento CRM desde multitabla");
		List<Multitabla> m1 = multitablaRepository.findByCodigoTabla(TablaEnum.TABLA_TIPO_DOCUMENTO.getCodigoTabla());
		LOGGER.info("Fin de consulta a bd por CodigoTabla");
		Multitabla tiposDocumentoCRM = null;

		if (!m1.isEmpty()) {
			tiposDocumentoCRM = m1.stream().filter(t -> t.getCodigo().equals(tipoDocCliente))
					.collect(Collectors.toList()).get(0);
		}

		String tipoDocumentoCRM = tiposDocumentoCRM.getValorCrm();

		ObtenerReglasAsigClienteRequest request = new ObtenerReglasAsigClienteRequest();
		request.setNumDoc(numDocCliente);
		request.setCorreo(correoUsuario);

		LOGGER.info("***** Buscanto datos en => Acsele-[Consultar Asignacion] *****");

		ObtenerConsultaAsigAcseleResponse responseAcsele = globalRestClient.consultarAsignacionAcsele(request);

		LOGGER.info("Fin de consulta a AsignacionAcsele");

		String origen = "";
		LOGGER.info(responseAcsele.getRpta());
		if (responseAcsele.getRpta() != null) {

			LOGGER.info("***** Codigo Agente ===> " + responseAcsele.getNumCot() + " *****");

			ObtenerAgenteResponse rsponseobtenerAgentecCodigo = globalRestClient
					.obtenerAgentecCodigo(responseAcsele.getNumCot());

			LOGGER.info("fin de consulta a ObtenerAgenteCodigo");

			LOGGER.info(
					"***** ===> Validar agente activo - Correo Agente ===> " + responseAcsele.getCorreo() + " *****");

			ObtenerStatusResponse rsponseValidarAgente = globalRestClient.validarAgenteActivo(correoUsuario);

			LOGGER.info("Fin de consulta validarAgenteActivo");
			System.out.println(gson.toJson(rsponseValidarAgente));

			if (rsponseValidarAgente.getStatus().equals("Active")) {

				origen = "ACSELE";

				String correoAcsele = rsponseobtenerAgentecCodigo.getGLS_CORREO_AGENTE().toLowerCase();
				String correoLogueado = correoUsuario.toLowerCase();

				LOGGER.info("****** OBTENER CARGO AGENTE ******");

				ObtenerAgenteResponse cargoAgente = globalRestClient.obtenerCargoAgente(correoAcsele);

				LOGGER.info("fin del metodo ObtenerCargoAgente");
				;

				if (cargoAgente.getResult().equals("JEFE") || cargoAgente.getResult().equals("SUPERVISOR")
						|| cargoAgente.getResult().equals("")) {

					LOGGER.info("[" + traza + "] Agente logueado origen - [ACSELE].");
					observaciones.add("Agente logueado origen - [ACSELE]");
					response = this.obtenerInformacionCliente(tipoDocCliente, tipoDocumentoCRM, numDocCliente,
							idUsuario, correoUsuario);

				}
				else {
					if (correoAcsele.equals(correoLogueado)) {

						LOGGER.info("[" + traza + "] Agente logueado cumple con ID VTiger.");
						observaciones.add("Agente logueado es el mismo que ID VTiger");
						response = this.obtenerInformacionCliente(tipoDocCliente, tipoDocumentoCRM, numDocCliente,
								idUsuario, correoUsuario);

					}
					else {

						response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_CLIENTE_CRM_ERROR);
						observaciones.add("Agente logueado no es el mismo que ID Acsele");
//						response.setMensajeRespuesta("Usuario actual: [" + rsponseobtenerAgentecCodigo.getNOM_AGENTE()
//								+ "]. Agencia: [" + rsponseobtenerAgentecCodigo.getCOD_AGENCIA() + "]. Es cliente Interseguro");
						response.setMensajeRespuesta("El cliente con DNI [" + numDocCliente
								+ "] tiene póliza vigente en interseguro con el agente ["
								+ rsponseobtenerAgentecCodigo.getNOM_AGENTE() + "] y Agencia ["
								+ rsponseobtenerAgentecCodigo.getCOD_AGENCIA() + "]");
					}
				}

			}
			else {

				LOGGER.info("***** Buscanto datos en => Acsele-[Consultar Asignacion] *****");

				ObtenerReglasAsigClienteResponse responseVtiger = globalRestClient.obtenerReglasAsigCliente(request);

				LOGGER.info("fin consulta de obtenerReglasAsigCliente");

				if (!responseVtiger.getMensaje().equals("Documento no encontrado en la base de Vtiger")) {

					// if (responseVtiger.getMensaje().equals("")) {
					// mensaje vacio -> existe

					origen = "VTIGER";
					// 1 disponible
					// 0 no disponible
					if (responseVtiger.getFlg_transfer().equals("1")) {

						LOGGER.info("[" + traza + "] Agente logueado cumple con ID VTiger.");

						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
						String fecha = format.format(new Date());

						ActualizarContactoVtigerRequest ActualizarContactoRequest = new ActualizarContactoVtigerRequest();
						ActualizarContactoRequest
								.setCf_852(Integer.valueOf(tipoDocCliente) == 1 ? "DNI" : "Carnet Extranjeria");
						ActualizarContactoRequest.setCf_854(numDocCliente);
						ActualizarContactoRequest.setCf_1044(fecha);
						ActualizarContactoRequest.setCf_1046("ADN");
						ActualizarContactoRequest.setCf_928("Contacts::::" + numDocCliente);
						ActualizarContactoRequest.setAssigned_user_id(correoUsuario);

						LOGGER.info("******* Actualizar Contacto VTIGER ********");

						ActualizarContactoVtigerResponse responseActualizarContacto = globalRestClient
								.actualizarContactoVtiger(ActualizarContactoRequest);

						LOGGER.info("fin del metodo actualizarContactoVtiger");
						LOGGER.info(gson.toJson(responseActualizarContacto));

						observaciones.add("Agente logueado es el mismo que ID VTiger");
						// actualizarvtiger
						response = this.obtenerInformacionCliente(tipoDocCliente, tipoDocumentoCRM, numDocCliente,
								idUsuario, correoUsuario);

					}
					else {

						if (responseVtiger.getUsuario().equals(request.getCorreo())) {

							// validar mensaje
							LOGGER.info("[" + traza + "] Agente logueado cumple con ID VTiger.");
							observaciones.add("Agente logueado es el mismo que ID VTiger");
							response = this.obtenerInformacionCliente(tipoDocCliente, tipoDocumentoCRM, numDocCliente,
									idUsuario, correoUsuario);

						}
						else {

							response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_CLIENTE_CRM_ERROR);
							response.setMensajeRespuesta("Usuario actual: [" + responseVtiger.getUsuario()
									+ "]. Agencia: [" + rsponseValidarAgente.getAgencia() + "]");
							// no muestra codigo de agencia
						}

					}

				}
				else {

					LOGGER.info("[" + traza + "] Agente logueado cumple con ID VTiger.");
					observaciones.add("Agente logueado es el mismo que ID VTiger");
					response = this.obtenerInformacionCliente(tipoDocCliente, tipoDocumentoCRM, numDocCliente, idUsuario,
							correoUsuario);
					// crear contacto vtiger

				}

			}
		}
		else {
			LOGGER.info("****** OBTENER REGLAS ASIG CLIENTE ******");
			ObtenerReglasAsigClienteResponse responseVtiger = globalRestClient.obtenerReglasAsigCliente(request);

			LOGGER.info("fin del metodo obtenerReglasAsigCliente");

			System.out.println(gson.toJson(responseVtiger));
			if (!responseVtiger.getMensaje().equals("Documento no encontrado en la base de Vtiger")) {

				origen = "VTIGER";

				if (responseVtiger.getFlg_transfer().equals("1")) {

					LOGGER.info("[" + traza + "] Agente logueado cumple con ID VTiger.");

					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					String fecha = format.format(new Date());

					ActualizarContactoVtigerRequest ActualizarContactoRequest = new ActualizarContactoVtigerRequest();
					ActualizarContactoRequest
							.setCf_852(Integer.valueOf(tipoDocCliente) == 1 ? "DNI" : "Carnet Extranjeria");
					ActualizarContactoRequest.setCf_854(numDocCliente);
					ActualizarContactoRequest.setCf_1044(fecha);
					ActualizarContactoRequest.setCf_1046("ADN");
					ActualizarContactoRequest.setCf_928("Contacts::::" + numDocCliente);
					ActualizarContactoRequest.setAssigned_user_id(correoUsuario);
					System.out.println(gson.toJson(ActualizarContactoRequest));
					LOGGER.info("***** Actualizar Contacto VTIGER ******");
					ActualizarContactoVtigerResponse responseActualizarContacto = globalRestClient
							.actualizarContactoVtiger(ActualizarContactoRequest);

					LOGGER.info("fin del metodo actualizarContactoVtiger");

					LOGGER.info(gson.toJson(responseActualizarContacto));

					observaciones.add("Agente logueado es el mismo que ID VTiger");
					response = this.obtenerInformacionCliente(tipoDocCliente, tipoDocumentoCRM, numDocCliente,
							idUsuario, correoUsuario);

				}
				else {

					if (responseVtiger.getUsuario().equals(request.getCorreo())) {

						// validar mensaje
						LOGGER.info("[" + traza + "] Agente logueado cumple con ID VTiger.");
						observaciones.add("Agente logueado es el mismo que ID VTiger");
						response = this.obtenerInformacionCliente(tipoDocCliente, tipoDocumentoCRM, numDocCliente,
								idUsuario, correoUsuario);

					}
					else {
						try {

							LOGGER.info("****** OBTENER AGENTE POR CORREO *******");

							ObtenerAgenteResponse responseGlobal = globalRestClient
									.obtenerAgentexCorreo(responseVtiger.getUsuario());

							LOGGER.info("Fin del metodo obtenerAgentexCorreo");
							response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_CLIENTE_CRM_ERROR);
							response.setMensajeRespuesta("El cliente con DNI [" + numDocCliente
									+ "] está asignado en Vtiger con el agente [" + responseVtiger.getUsuario()
									+ "] y Agencia [" + responseGlobal.getCOD_AGENCIA()
									+ "]. Pónganse en contacto con Inteligencia Comercial");
						}
						catch (Exception e) {
							response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_CLIENTE_CRM_ERROR);
							response.setMensajeRespuesta(
									// "Usuario actual: ["+ responseVtiger.getUsuario() +"]. Agencia: [" +
									// responseGlobal.getCOD_AGENCIA() + "]");
									"Usuario actual: [" + responseVtiger.getUsuario() + "]");
							// no muestra codigo de agencia
						}

					}

				}

			}
			else {

				LOGGER.info("[" + traza + "] Agente logueado cumple con ID VTiger.");
				observaciones.add("Agente logueado es el mismo que ID VTiger");
				response = this.obtenerInformacionCliente(tipoDocCliente, tipoDocumentoCRM, numDocCliente, idUsuario,
						correoUsuario);
				// crear contacto vtiger

			}

		}

		// enviar data de consentimiento
		//response.setDataConsentimiento(
		//		this.getDataConsentimiento(idUsuario, numDocCliente, Integer.parseInt(tipoDocCliente)));
        // modificar este metodo para que apunte a la nueva api de consentimiento
		response.setDataConsentimiento(
				this.getDataConsentimientov2(idUsuario, tipoDocCliente, numDocCliente));

		observaciones.add(gson.toJson(response.getObservaciones()));
		/*logRepository.save(new EventLog("Registro ADN", response.getCodigoRespuesta(), "/adns/reglamentos",
				response.getMensajeRespuesta(), gson.toJson(response.getObjErrorResource()), gson.toJson(observaciones),
				idUsuario, device, os));*/

		// Se valida si el cliente pertenece a la base de Plaft
		try {
			ObtenerPolizaRequest requestPlaft = new ObtenerPolizaRequest();
			requestPlaft.setNroDoc(numDocCliente);
			requestPlaft.setTipoDoc(tipoDocCliente);

			LOGGER.info("*****Validar Plaft*****");
			ValidarProspectoAsignacionResponse existePlaft = globalRestClient.validarPlaft(requestPlaft);
			LOGGER.info("fin metodo ValidarPlaft");

			LOGGER.info("[" + traza + "] Plaft Validación: " + existePlaft.getValidacion());
			LOGGER.info("[" + traza + "] Plaft Mensaje: " + existePlaft.getMensaje());
			response.setExistePlaft(existePlaft.getMensaje());
			response.setCodigoPlaft(existePlaft.getValidacion());
		}
		catch (Exception e) {
			LOGGER.info("error" + e.getMessage());
			response.setExistePlaft("N");
			// TODO: handle exception
		}

		observaciones.add(gson.toJson(response.getObservaciones()));
		/*logRepository.save(new EventLog("Registro ADN", response.getCodigoRespuesta(), "/adns/reglamentos",
				response.getMensajeRespuesta(), gson.toJson(response.getObjErrorResource()), gson.toJson(observaciones),
				idUsuario, device, os));*/

		LOGGER.info("Salio AdnServiceImpl#obtenerReglamento(tipoDocCliente, numDocCliente, idUsuario)");
		return response;

	}

	@Override
	public ADNFirmaResponseDTO registrarFirma(ADNFirmaRequestDTO aDNFirmaRequestDTO) {
		LOGGER.info("Entro AdnServiceImpl#registrarFirma(aDNFirmaRequestDTO)");

		String mensajeError = Utilitarios.obtenerMensaje(messageSource,
				new Object[] { Constantes.MENSAJE_RESPUESTA_GENERAL_ERR0R },
				Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_ADN_FIRMA_TX_GENERAR_CIRCUITO));

		ADNFirmaResponseDTO response = new ADNFirmaResponseDTO();

		try {
			File plantilla = null;
			String rutaPlantilla = "";
			try {
				rutaPlantilla = Constantes.RUTA_PLANTILLA + "/" + Constantes.PLANTILLA_TITULAR;

				plantilla = new File(getClass().getClassLoader().getResource(rutaPlantilla).getFile());

				String systemOp = System.getProperty("os.name");

				LOGGER.info("os.name->" + systemOp);
				LOGGER.info("systemOp.contains->" + systemOp.contains("Windows"));

				if (!systemOp.contains("Windows")) {
					plantilla = new File(rutaPlantillaDocLinux + "//" + Constantes.PLANTILLA_TITULAR);
				}

				LOGGER.info("plantilla=>" + plantilla);

			} catch (Exception e) {
				throw new SivTXException(mensajeError, null);
			}

			// -- Procesamiento del documento
			String nombreCliente = aDNFirmaRequestDTO.getNombres().concat(" " + aDNFirmaRequestDTO.getApellidoPaterno())
					.concat(" " + aDNFirmaRequestDTO.getApellidoMaterno()).toUpperCase();
			String documentoIdentidad = aDNFirmaRequestDTO.getNumeroDocumento();
			String correoCliente = aDNFirmaRequestDTO.getCorreo();
			String nombreArchivo = "adn_titular_generado";
			Date hoy = new Date();

			LOGGER.info("nombreCliente=>" + nombreCliente);
			LOGGER.info("plantilla=>" + plantilla);

			try {

				String tipoDocumento = Strings.EMPTY;
				if (documentoIdentidad != null && !documentoIdentidad.equals(Strings.EMPTY)
						&& aDNFirmaRequestDTO.getTipoDocumento() != null) {

					List<Multitabla> m1 = multitablaRepository
							.findByCodigoTabla(TablaEnum.TABLA_TIPO_DOCUMENTO.getCodigoTabla());
					Multitabla tiposDocumentoCRM = null;
					if (!m1.isEmpty()) {
						tiposDocumentoCRM = m1.stream()
								.filter(t -> t.getCodigo().equals(aDNFirmaRequestDTO.getTipoDocumento().toString()))
								.collect(Collectors.toList()).get(0);

						tipoDocumento = tiposDocumentoCRM.getValor() + " ";
					}

				}

				StringBuilder sbDocumento = new StringBuilder();
				sbDocumento.append(tipoDocumento);
				sbDocumento.append(documentoIdentidad);

				// -- Parametros
				Map<String, Object> parametros = new HashMap<String, Object>();
				parametros.put("nombres", nombreCliente);
				parametros.put("documento", sbDocumento.toString());
				parametros.put("fecha", DateUtil.dateToString(hoy, DateUtil.FORMATO_DIA_DDMMYYYY));

				// -- Generar
				nombreArchivo = IXDocReportUtil.generarDocumento(plantilla, nombreArchivo, parametros, true);

			} catch (Exception e) {
				LOGGER.error("Error Generar documento => " + e.getMessage());

				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));

				this.enviarCorreoError(aDNFirmaRequestDTO, "SivTXException",
						"[AdnServiceImpl.registrarFirma] Error al Generar documento en el procesaiento del documento",
						Utilitarios.obtenerMensaje(messageSource, new Object[] { sw.toString() },
								Constantes.MENSAJE_ADN_FIRMA_TX_GENERAR_DOCUMENTO));
				throw new SivTXException(mensajeError, null);
			}

			// -- Recuperar documento generado
			File archivoGenerado = new File(
					System.getProperty("java.io.tmpdir").concat(File.separator).concat(nombreArchivo));

			if (!archivoGenerado.exists()) {
				this.enviarCorreoError(aDNFirmaRequestDTO, "SivTXException",
						"[AdnServiceImpl.registrarFirma] Error al Enviar Indenova en recuperar documento generado",
						Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_ADN_FIRMA_TX_DOCUMENTO_GENERADO));
				throw new SivTXException(Utilitarios.obtenerMensaje(messageSource,
						new Object[] { Constantes.MENSAJE_ADMINISTRADOR }, Constantes.MENSAJE_RESPUESTA_GENERAL_ERR0R),
						null);
			}

			// -- Enviar INDENOVA
			CrearCircuitoResponse crearCircuitoResponse = null;
			try {

				// -- REQUEST
				CrearCircuitoRequest crearCircuitoRequest = new CrearCircuitoRequest();
				String circuitoAsunto = Constantes.CREAR_CIRCUITO_SUBJECT.concat(nombreCliente);

				crearCircuitoRequest.setInclude(Constantes.CREAR_CIRCUITO_INCLUDE);
				crearCircuitoRequest.setSubject(circuitoAsunto);
				crearCircuitoRequest.setDescription("");

				List<CrearCircuitoSignerRequest> listaSigner = new ArrayList<CrearCircuitoSignerRequest>();
				CrearCircuitoSignerRequest signer = new CrearCircuitoSignerRequest();
				signer.setName(nombreCliente);
				signer.setNif(documentoIdentidad);
				signer.setDescription("");
				signer.setEmail(correoCliente);
				listaSigner.add(signer);

				crearCircuitoRequest.setSigners(listaSigner);

				crearCircuitoRequest.setDocs(new ArrayList<CrearCircuitoDocRequest>());
				crearCircuitoRequest.getDocs()
						.add(new CrearCircuitoDocRequest("",
								"DatosPersonales_".concat(documentoIdentidad).concat(Constantes.ARCHIVO_EXT_PDF),
								Constantes.CREAR_CIRCUITO_SIZE, Constantes.CREAR_CIRCUITO_DOCTYPE,
								Files.readAllBytes(archivoGenerado.toPath())));

				crearCircuitoRequest.setOptions(new ArrayList<CrearCircuitoOptionRequest>());
				crearCircuitoRequest.getOptions().add(new CrearCircuitoOptionRequest(
						Constantes.CREAR_CIRCUITO_OPTION1_NAME, Constantes.CREAR_CIRCUITO_OPTION1_VALUE));
				crearCircuitoRequest.getOptions().add(new CrearCircuitoOptionRequest(
						Constantes.CREAR_CIRCUITO_OPTION2_NAME, Constantes.CREAR_CIRCUITO_OPTION2_VALUE));
				crearCircuitoRequest.getOptions().add(new CrearCircuitoOptionRequest(
						Constantes.CREAR_CIRCUITO_OPTION3_NAME, Constantes.CREAR_CIRCUITO_OPTION3_VALUE));
				crearCircuitoRequest.getOptions().add(new CrearCircuitoOptionRequest(
						Constantes.CREAR_CIRCUITO_OPTION4_NAME, Constantes.CREAR_CIRCUITO_OPTION4_VALUE));

				// -- RESPONSE
				crearCircuitoResponse = indenovaRestClient.crearCircuito(crearCircuitoRequest);

			} catch (Exception e) {
				LOGGER.error("Error crearCircuito => " + e);

				this.enviarCorreoError(aDNFirmaRequestDTO, "SivTXException",
						"[AdnServiceImpl.registrarFirma] Error al Enviar INDENOVA", mensajeError);

				throw new SivTXException(mensajeError, null);
			}

			// ------------------------------------------------------------------------------
			// -- Respuesta
			// ------------------------------------------------------------------------------

			String circuitoID = crearCircuitoResponse.getCircuitId();
			String circuitoURL = crearCircuitoResponse.getLinks().get(0).getLink();

			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
			response.setMensajeRespuesta("OK");
			response.setIdCircuitoFirma(circuitoID);
			response.setCircuitoUrl(circuitoURL); // -- TODO: Para esta caso es un solo documento. Sino hacer un BUCLE

		} catch (SivTXException e) {
			LOGGER.error("Error SivTXException ADNFirmaResponseDTO=> " + e.getMsjError());
			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			response.setMensajeRespuesta(e.getMsjError());
		} catch (Exception e) {
			LOGGER.error("Error Exception ADNFirmaResponseDTO=> " + e);
			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			response.setMensajeRespuesta(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_ERR0R));
		}

		LOGGER.info("Salio AdnServiceImpl#registrarFirma(aDNFirmaRequestDTO)");
		return response;
	}

	@Override
	public ADNRegistroResponseDTO registrarADNS(ADNRegistroRequestDTO aDNRegistroRequestDTO) {
		String traza = Utilitarios.trazaLog();
		traza = traza + "-" + aDNRegistroRequestDTO.getTitular().getNumeroDocumento();
		LOGGER.info("[" + traza + "] Entro AdnServiceImpl#registrarADNS(ADNRegistroRequestDTO)");
		System.out.println(gson.toJson(aDNRegistroRequestDTO));
		ADNRegistroResponseDTO response = new ADNRegistroResponseDTO();
		Adn adn = null;
		Persona persona = null;
		PlanFuturo planFuturo = null;
		Integer indicadorCambioDatos = aDNRegistroRequestDTO.getIndicadorCambio();
		String correoUsuario = aDNRegistroRequestDTO.getCorreoUsuario();
		List<String> observaciones = new ArrayList<>();

		LOGGER.info("[" + traza + "] Indicador de cambio: " + aDNRegistroRequestDTO.getIndicadorCambio());

		Gson gson = new Gson();
		Long adn_id = 0L;

		if (aDNRegistroRequestDTO.getFormulario() == null) {
			aDNRegistroRequestDTO.setFormulario(aDNRegistroRequestDTO.getTipo());
		}

		/**
		 * Validación de consentimiento aceptado
		 */
		Map<String, Object> consentimientoData = this.getDataConsentimiento(aDNRegistroRequestDTO.getIdUsuario(),
				aDNRegistroRequestDTO.getTitular().getNumeroDocumento(),
				aDNRegistroRequestDTO.getTitular().getTipoDocumento());

		// Se usa la nueva api para validar si existe o no el consentimiento
		boolean hasConsentimientov2 = this.validateExistConsentAcceptedInPersonaConsentimiento(aDNRegistroRequestDTO.getTitular().getTipoDocumento(),
		aDNRegistroRequestDTO.getTitular().getNumeroDocumento());
		LOGGER.info("registrarADNS hasConsentimientov2 : " + hasConsentimientov2);
		
		consentimientoData.put("usuario", aDNRegistroRequestDTO.getIdUsuario());
		LOGGER.info("registros consentimientoData: " + gson.toJson(consentimientoData));
		//boolean hasConsentimiento = Boolean.parseBoolean(consentimientoData.get("hasConsentimiento").toString());
		//boolean acceptedConsentimiento = Boolean
		//		.parseBoolean(consentimientoData.get("acceptedConsentimiento").toString());

		int consentTreatmentId = NumberUtils.toInt(consentimientoData.get("id_tratamiento").toString());
		int consentConfigurationId = NumberUtils.toInt(consentimientoData.get("id_configuracion").toString());
		String audUsrIngreso = consentimientoData.get("aud_usr_ingreso").toString();

		if (!hasConsentimientov2) {
			response.setCodigoRespuesta("" + HttpStatus.NOT_FOUND);
			response.setMensajeRespuesta("El cliente no ha registrado su consentimiento para proceder.");
			response.setRequiredUpdateConsentimiento(true);
			return response;
		}

		/*
		if (!acceptedConsentimiento) {
			response.setCodigoRespuesta("" + HttpStatus.NOT_FOUND);
			response.setMensajeRespuesta("El cliente no ha registrado su consentimiento para proceder.");
			response.setRequiredUpdateConsentimiento(true);
			return response;
		}
		*/

		// get the data from ADN and Person table
		try {
			if (aDNRegistroRequestDTO.getTipo() == 2) { // Si flag es de actualizacion
				LOGGER.info("[" + traza + "] Registro de Adn ==> " + aDNRegistroRequestDTO.getIdAdn());
				System.out.println(aDNRegistroRequestDTO.getIdAdn());
				adn = adnRepository.findById(aDNRegistroRequestDTO.getIdAdn()).get();
				persona = personaRepository.findById(adn.getPersona().getIdPersona()).get();
			}
			else if (aDNRegistroRequestDTO.getTipo() == 1) {
				adn = adnRepository.findByTipoNumeroDocumento(
						aDNRegistroRequestDTO.getTitular().getTipoDocumento().toString(),
						aDNRegistroRequestDTO.getTitular().getNumeroDocumento());
				persona = personaRepository.findByTipoNumeroDocumento(
						aDNRegistroRequestDTO.getTitular().getTipoDocumento().toString(),
						aDNRegistroRequestDTO.getTitular().getNumeroDocumento());
			}
		}
		catch (Exception e) {
			LOGGER.info("[" + traza + "] Error => Obtener data de ADN y Persona:: " + e);
		}

		boolean mustSkipUpdateConsent = mustSkipUpdateConsent(consentTreatmentId, consentConfigurationId, audUsrIngreso,
				persona);

		LOGGER.info("consentTreatmentId: " + consentTreatmentId);
		LOGGER.info("consentConfigurationId: " + consentConfigurationId);
		LOGGER.info("audUsrIngreso: " + audUsrIngreso);

		LOGGER.info("persona actualizado lead: " + (!Objects.isNull(persona) ? persona.getActualizadoLead() : "null"));

		LOGGER.info("mustSkipUpdateConsent: " + mustSkipUpdateConsent);

		if (!mustSkipUpdateConsent) {
			// validar si hubo cambios en los datos del consentimiento
			BaseResponseDTO updateConsentimientoResponse = this
					.updateConsentimiento(aDNRegistroRequestDTO.getIdUsuario(), aDNRegistroRequestDTO, persona);
			if (updateConsentimientoResponse.getCodigoRespuesta().equals(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO)) {
				response.setCodigoRespuesta("" + HttpStatus.NOT_FOUND);
				response.setMensajeRespuesta(
						"La informacion del consentimiento ha cambiado. Es necesario reenviar su aprobación.");
				response.setRequiredUpdateConsentimiento(true);
				return response;
			}
		}

		try {
			if (aDNRegistroRequestDTO.getTipo() == 2) { // Si flag es de actualizacion
				LOGGER.info("[" + traza + "] Registro de Adn ==> " + aDNRegistroRequestDTO.getIdAdn());
				adn_id = aDNRegistroRequestDTO.getIdAdn();
				System.out.println(aDNRegistroRequestDTO.getIdAdn());
				// adn = adnRepository.findById(aDNRegistroRequestDTO.getIdAdn()).get();
				adn.setFechaModif(new Date());
				adn.setUsuarioModif(aDNRegistroRequestDTO.getIdUsuario());
				Date hoy = new Date();
				if (aDNRegistroRequestDTO.getFormulario() == 1) {
					String fecha = DateUtil.dateToString(hoy, DateUtil.FORMATO_DIA_DDMMYYYY);
					String hora = LocalTime.now().toString().substring(0, 8);
					aDNRegistroRequestDTO.setFechaLDPDP(fecha + " " + hora);
					adn.setLdpdpFecha(aDNRegistroRequestDTO.getFechaLDPDP());
				} else {
					if (adn.getLdpdpFecha() != null) {
						aDNRegistroRequestDTO.setFechaLDPDP(adn.getLdpdpFecha());
					} else {
						String fecha = DateUtil.dateToString(hoy, DateUtil.FORMATO_DIA_DDMMYYYY);
						String hora = LocalTime.now().toString().substring(0, 8);
						aDNRegistroRequestDTO.setFechaLDPDP(fecha + " " + hora);
						adn.setLdpdpFecha(aDNRegistroRequestDTO.getFechaLDPDP());
					}
				}
				// String idCircuitoFirma = adn.getIdCircuitoFirma();

				// persona = personaRepository.findById(adn.getPersona().getIdPersona()).get();

				LOGGER.info("[" + traza + "] ID de persona a modificar ==> " + adn.getPersona().getIdPersona());
				persona.setFechaModif(new Date());
				persona.setUsuarioModif(aDNRegistroRequestDTO.getIdUsuario());

				if (mustSkipUpdateConsent) {
					persona.setActualizadoLead("1");
				}

				planFuturo = planFuturoRepository.findByIdPersona(persona.getIdPersona());
				if (planFuturo != null) {
					planFuturo.setFechaModif(DateUtil.dateToString(new Date(), DateUtil.FORMATO_DIA_DDMMYYYY));
					planFuturo.setUsuarioModif(aDNRegistroRequestDTO.getIdUsuario());
					LOGGER.info("[" + traza + "] Plan Futuro existe ");
				} else {
					planFuturo = new PlanFuturo();
					planFuturo.setUsuarioCrea(aDNRegistroRequestDTO.getIdUsuario());
					planFuturo.setFechaCrea(new Date());
					LOGGER.info("[" + traza + "] Plan Futuro no existe ");
				}

				List<Familiar> familiares = familiarRepository.findByIdPersona(persona.getIdPersona());
				// LOGGER.info("["+ traza + "] Familiares ==> " + gson.toJson(familiares));
				for (Familiar familiar : familiares) {
					familiarRepository.delete(familiar);
				}

			}
			else if (aDNRegistroRequestDTO.getTipo() == 1) {// Si flag es de insercion

				/*
				 * adn = adnRepository.findByTipoNumeroDocumento(
				 * aDNRegistroRequestDTO.getTitular().getTipoDocumento().toString(),
				 * aDNRegistroRequestDTO.getTitular().getNumeroDocumento());
				 */

				if (adn == null) {
					adn = new Adn();
					adn.setUsuarioCrea(aDNRegistroRequestDTO.getIdUsuario());
					adn.setFechaCrea(new Date());
				}

				/*
				 * persona = personaRepository.findByTipoNumeroDocumento(
				 * aDNRegistroRequestDTO.getTitular().getTipoDocumento().toString(),
				 * aDNRegistroRequestDTO.getTitular().getNumeroDocumento());
				 */

				if (persona == null) {
					persona = new Persona();
					persona.setFechaCrea(new Date());
					persona.setUsuarioCrea(aDNRegistroRequestDTO.getIdUsuario());
					persona.setActualizadoLead("0");
					if (mustSkipUpdateConsent) {
						persona.setActualizadoLead("1");
					}
				}

				if (aDNRegistroRequestDTO.getFormulario() == 1) {
					Date hoy = new Date();
					String fecha = DateUtil.dateToString(hoy, DateUtil.FORMATO_DIA_DDMMYYYY);
					String hora = LocalTime.now().toString().substring(0, 8);
					aDNRegistroRequestDTO.setFechaLDPDP(fecha + " " + hora);
					adn.setLdpdpFecha(aDNRegistroRequestDTO.getFechaLDPDP());
				}

				if (aDNRegistroRequestDTO.getPlanFuturo() != null) {
					planFuturo = new PlanFuturo();
					planFuturo.setUsuarioCrea(aDNRegistroRequestDTO.getIdUsuario());
					planFuturo.setFechaCrea(new Date());
				}

			}
			else {
				response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
				response.setMensajeRespuesta(
						Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_ERR0R));
				return response;
			}

			LOGGER.info("[" + traza + "] Datos básicos de persona...");
			String actividadEconomicaOrigen = aDNRegistroRequestDTO.getTitular().getActividadEconomica() == null ? null
					: aDNRegistroRequestDTO.getTitular().getActividadEconomica().toString();
			String apellidoMaternoOrigen = aDNRegistroRequestDTO.getTitular().getApellidoMaterno() == null ? null
					: aDNRegistroRequestDTO.getTitular().getApellidoMaterno().toUpperCase().trim();
			String apellidoPaternoOrigen = aDNRegistroRequestDTO.getTitular().getApellidoPaterno() == null ? null
					: aDNRegistroRequestDTO.getTitular().getApellidoPaterno().toUpperCase().trim();
			String nombreOrigen = aDNRegistroRequestDTO.getTitular().getNombres() == null ? null
					: aDNRegistroRequestDTO.getTitular().getNombres().toUpperCase().trim();
			String tipoDocumentoOrigen = aDNRegistroRequestDTO.getTitular().getTipoDocumento() == null ? null
					: aDNRegistroRequestDTO.getTitular().getTipoDocumento().toString();
			LOGGER.info("[" + traza + "] Fecha de nacimiento...");
			Date fechaNacimientoOrigen = null;
			if (aDNRegistroRequestDTO.getTitular().getFechaNacimiento() != null) {

				fechaNacimientoOrigen = DateUtil.stringToDate(aDNRegistroRequestDTO.getTitular().getFechaNacimiento(),
						DateUtil.FORMATO_DIA_DDMMYYYY);
				fechaNacimientoOrigen = fechaNacimientoOrigen == null
						? DateUtil.stringToDate(aDNRegistroRequestDTO.getTitular().getFechaNacimiento(),
								DateUtil.FORMATO_DIA_YYYYMMDD2)
						: fechaNacimientoOrigen;

			}
			// Date fechaNacimientoOrigen =
			// aDNRegistroRequestDTO.getTitular().getFechaNacimiento() == null ? null
			// :
			// DateUtil.stringToDate(aDNRegistroRequestDTO.getTitular().getFechaNacimiento(),
			// DateUtil.FORMATO_DIA_YYYYMMDD2);
			if (fechaNacimientoOrigen != null) {
				fechaNacimientoOrigen.setHours(6);// GTI.24117
			}
			LOGGER.info("[" + traza + "] Datos de fumador de persona...");
			String fumadorOrigen = aDNRegistroRequestDTO.getTitular().getFumador() == null ? null
					: aDNRegistroRequestDTO.getTitular().getFumador().toString();
			LOGGER.info("[" + traza + "] Datos de genero de persona...");
			String generoOrigen = aDNRegistroRequestDTO.getTitular().getGenero() == null ? null
					: aDNRegistroRequestDTO.getTitular().getGenero().toString();
			LOGGER.info("[" + traza + "] Datos complementarios de persona...");
			String profesionOrigen = aDNRegistroRequestDTO.getTitular().getProfesion() == null ? null
					: aDNRegistroRequestDTO.getTitular().getProfesion().toString();
			String tipoCasaOrigen = aDNRegistroRequestDTO.getTitular().getTipoCasa() == null ? null
					: aDNRegistroRequestDTO.getTitular().getTipoCasa().toString();
			String tieneVehiculoOrigen = aDNRegistroRequestDTO.getTitular().getTieneVehiculo() == null ? null
					: aDNRegistroRequestDTO.getTitular().getTieneVehiculo().toString();
			String tieneAfpOrigen = aDNRegistroRequestDTO.getTitular().getTieneAfp() == null ? null
					: aDNRegistroRequestDTO.getTitular().getTieneAfp().toString();
			String tieneSeguroVidaOrigen = aDNRegistroRequestDTO.getTitular().getTieneSeguroVida() == null ? null
					: aDNRegistroRequestDTO.getTitular().getTieneSeguroVida().toString();

			LOGGER.info("[" + traza + "] Seteando datos de conversion de persona...");
			persona.setApellidoMaterno(Utilitarios.quitaEspacios(apellidoMaternoOrigen));
			persona.setApellidoPaterno(Utilitarios.quitaEspacios(apellidoPaternoOrigen));
			persona.setCelular(aDNRegistroRequestDTO.getTitular().getCelular());
			persona.setCorreo(aDNRegistroRequestDTO.getTitular().getCorreo());
			persona.setEdadActuarial(aDNRegistroRequestDTO.getTitular().getEdadActuarial());
			persona.setFechaNacimiento(fechaNacimientoOrigen);
			persona.setGenero(generoOrigen);
			persona.setNombres(Utilitarios.quitaEspacios(nombreOrigen));
			persona.setNumeroDocumento(aDNRegistroRequestDTO.getTitular().getNumeroDocumento());
			persona.setTipoDocumento(tipoDocumentoOrigen);
			persona.setTipoCasa(tipoCasaOrigen);
			persona.setTieneVehiculo(tieneVehiculoOrigen);
			persona.setTieneAfp(tieneAfpOrigen);
			persona.setTieneSeguroVida(tieneSeguroVidaOrigen);

			if (persona.getFumador() == null) {
				persona.setFumador(fumadorOrigen);
			}

			if (persona.getProfesion() == null) {
				persona.setProfesion(profesionOrigen);
			}

			if (persona.getActividadEconomica() == null) {
				persona.setActividadEconomica(actividadEconomicaOrigen);
			}
			// LOGGER.info("["+ traza + "] Persona => " + gson.toJson(persona));
			LOGGER.info("[" + traza + "] Intentanto grabar datos de persona...");
			try {
				LOGGER.info("[" + traza + "] Se grabó persona...");
				personaRepository.save(persona);
			} catch (Exception e) {
				LOGGER.info("[" + traza + "] Error => Grabar persona:: " + e);
			}
			Familiar familiar = null;
			if (aDNRegistroRequestDTO.getFamiliar() != null) {
				for (ADNRegistroFamiliarRequestDTO aDNfamiliar : aDNRegistroRequestDTO.getFamiliar()) {
					familiar = new Familiar();
					String nombreFamiliarOrigen = aDNfamiliar.getNombres() == null ? null
							: aDNfamiliar.getNombres().toUpperCase();
					String tipoFamiliarOrigen = aDNfamiliar.getTipoRelacion() == null ? null
							: aDNfamiliar.getTipoRelacion().toString();
					familiar.setEdad(aDNfamiliar.getEdad());
					familiar.setNombres(nombreFamiliarOrigen);
					familiar.setTipoRelacion(tipoFamiliarOrigen);
					familiar.setCentroLaboral(aDNfamiliar.getCentroLaboral());
					familiar.setPersona(persona);
					familiar.setFechaCrea(new Date());
					familiar.setUsuarioCrea(aDNRegistroRequestDTO.getIdUsuario());
					familiarRepository.save(familiar);
					LOGGER.info("[" + traza + "] Se grabó familiar...");
				}
			}
			if (aDNRegistroRequestDTO.getPlanFuturo() != null) {
				LOGGER.info("[" + traza + "] Buscar plan futuro...");

				if (planFuturo == null) {
					planFuturo = new PlanFuturo();
				}

				LOGGER.info("[" + traza + "] Seteando plan futuro...");
				planFuturo.setPlanEducacion(aDNRegistroRequestDTO.getPlanFuturo().getBase().getPlanEducacion());
				planFuturo.setPlanJubilacion(aDNRegistroRequestDTO.getPlanFuturo().getBase().getPlanJubilacion());
				planFuturo.setPlanProyecto(aDNRegistroRequestDTO.getPlanFuturo().getBase().getPlanProyecto());
				planFuturo.setInformacionAdicional(
						aDNRegistroRequestDTO.getPlanFuturo().getBase().getInformacionAdicional());

				if (aDNRegistroRequestDTO.getPlanFuturo() != null) {
					if (aDNRegistroRequestDTO.getPlanFuturo().getBase() != null) {
						Float ingresoTitularOrigen = aDNRegistroRequestDTO.getPlanFuturo().getBase()
								.getIngresoTitular() == null ? null
										: aDNRegistroRequestDTO.getPlanFuturo().getBase().getIngresoTitular()
												.floatValue();
						planFuturo.setIngresoTitular(ingresoTitularOrigen);

						Float porcentajeIngresoOrigen = aDNRegistroRequestDTO.getPlanFuturo().getBase()
								.getPorcentajeIngreso() == null ? null
										: aDNRegistroRequestDTO.getPlanFuturo().getBase().getPorcentajeIngreso()
												.floatValue();
						planFuturo.setPorcentajeInversion(porcentajeIngresoOrigen);

						Float totalIngresoOrigen = aDNRegistroRequestDTO.getPlanFuturo().getBase()
								.getTotalIngreso() == null ? null
										: aDNRegistroRequestDTO.getPlanFuturo().getBase().getTotalIngreso()
												.floatValue();
						planFuturo.setTotalInversion(totalIngresoOrigen);

						planFuturo
								.setAniosProteger(aDNRegistroRequestDTO.getPlanFuturo().getBase().getAnioProteccion());
					}

					if (aDNRegistroRequestDTO.getPlanFuturo().getRespaldo() != null) {
						Float respaldoAhorroOrigen = (aDNRegistroRequestDTO.getPlanFuturo().getRespaldo()
								.getAhorro() == null ? null
										: aDNRegistroRequestDTO.getPlanFuturo().getRespaldo().getAhorro().floatValue());
						planFuturo.setRespaldoAhorros(respaldoAhorroOrigen);

						Float respaldoPropiedadOrigen = aDNRegistroRequestDTO.getPlanFuturo().getRespaldo()
								.getPropiedad() == null ? null
										: aDNRegistroRequestDTO.getPlanFuturo().getRespaldo().getPropiedad()
												.floatValue();
						planFuturo.setRespaldoPropiedades(respaldoPropiedadOrigen);

						Float respaldoVehiculoOrigen = aDNRegistroRequestDTO.getPlanFuturo().getRespaldo()
								.getVehiculo() == null ? null
										: aDNRegistroRequestDTO.getPlanFuturo().getRespaldo().getVehiculo()
												.floatValue();
						planFuturo.setRespaldoVehiculos(respaldoVehiculoOrigen);

						Float respaldoSeguroVidaOrigen = aDNRegistroRequestDTO.getPlanFuturo().getRespaldo()
								.getSeguroVida() == null ? null
										: aDNRegistroRequestDTO.getPlanFuturo().getRespaldo().getSeguroVida()
												.floatValue();
						planFuturo.setRespaldoSeguroVida(respaldoSeguroVidaOrigen);

						Float respaldoSeguroVidaLeyOrigen = aDNRegistroRequestDTO.getPlanFuturo().getRespaldo()
								.getSeguroVidaLey() == null ? null
										: aDNRegistroRequestDTO.getPlanFuturo().getRespaldo().getSeguroVidaLey()
												.floatValue();
						planFuturo.setRespaldoSeguroVidaLey(respaldoSeguroVidaLeyOrigen);

						planFuturo.setRespaldoAhorrosCheck(
								aDNRegistroRequestDTO.getPlanFuturo().getRespaldo().getAhorroCheck());
						planFuturo.setRespaldoPropiedadesCheck(
								aDNRegistroRequestDTO.getPlanFuturo().getRespaldo().getPropiedadCheck());
						planFuturo.setRespaldoSeguroVidaCheck(
								aDNRegistroRequestDTO.getPlanFuturo().getRespaldo().getSeguroVidaCheck());
						planFuturo.setRespaldoSeguroVidaLeyCheck(
								aDNRegistroRequestDTO.getPlanFuturo().getRespaldo().getSeguroVidaLeyCheck());
						planFuturo.setRespaldoVehiculosCheck(
								aDNRegistroRequestDTO.getPlanFuturo().getRespaldo().getVehiculoCheck());

					}

					if (aDNRegistroRequestDTO.getPlanFuturo().getCapitalProteger() != null) {
						planFuturo.setCapitalAfpCheck(
								aDNRegistroRequestDTO.getPlanFuturo().getCapitalProteger().getAfpCheck());

						Float capitalProteccion = aDNRegistroRequestDTO.getPlanFuturo().getCapitalProteger()
								.getCapitalProteccion() == null ? null
										: aDNRegistroRequestDTO.getPlanFuturo().getCapitalProteger()
												.getCapitalProteccion().floatValue();
						planFuturo.setCapitalProteccion(capitalProteccion);

						Float capitalRespaldoEconomico = aDNRegistroRequestDTO.getPlanFuturo().getCapitalProteger()
								.getRespaldoEconomico() == null ? null
										: aDNRegistroRequestDTO.getPlanFuturo().getCapitalProteger()
												.getRespaldoEconomico().floatValue();
						planFuturo.setCapitalRespaldo(capitalRespaldoEconomico);

						Float capitalAfp = aDNRegistroRequestDTO.getPlanFuturo().getCapitalProteger().getAfp() == null
								? null
								: aDNRegistroRequestDTO.getPlanFuturo().getCapitalProteger().getAfp().floatValue();
						planFuturo.setCapitalAfp(capitalAfp);

						Float capitalTotalCapitalProteger = aDNRegistroRequestDTO.getPlanFuturo().getCapitalProteger()
								.getTotalCapitalProteger() == null ? null
										: aDNRegistroRequestDTO.getPlanFuturo().getCapitalProteger()
												.getTotalCapitalProteger().floatValue();
						planFuturo.setCapitalTotalProteger(capitalTotalCapitalProteger);

						Float capitalAdicional = aDNRegistroRequestDTO.getPlanFuturo().getCapitalProteger()
								.getAdicional() == null ? null
										: aDNRegistroRequestDTO.getPlanFuturo().getCapitalProteger().getAdicional()
												.floatValue();
						planFuturo.setCapitalAdicional(capitalAdicional);

						Float capitalNuevoCapitalProteger = aDNRegistroRequestDTO.getPlanFuturo().getCapitalProteger()
								.getNuevoCapitalProteger() == null ? null
										: aDNRegistroRequestDTO.getPlanFuturo().getCapitalProteger()
												.getNuevoCapitalProteger().floatValue();
						planFuturo.setCapitalTotalProtegerNuevo(capitalNuevoCapitalProteger);
					}

				}

				planFuturo.setPersona(persona);
				planFuturoRepository.save(planFuturo);
			}

			adn.setIdCircuitoFirma(aDNRegistroRequestDTO.getIdCircuitoFirma() == null ? ""
					: aDNRegistroRequestDTO.getIdCircuitoFirma());
			if (adn.getIdAdn() == null) {
				adn.setIdAdn(aDNRegistroRequestDTO.getIdAdn());
			}
			adn.setPersona(persona);
			adn.setIdAgente(aDNRegistroRequestDTO.getIdUsuario());
			adn.setLdpdp(aDNRegistroRequestDTO.getFlagLDPDP());

			adnRepository.save(adn);

			LOGGER.info("[" + traza + "] Guardar ADN...");

			// TODO: Actualizar el consentimiento
//			BaseResponseDTO updateConsentimientoResponse = this.updateConsentimiento(
//					aDNRegistroRequestDTO.getIdUsuario(), aDNRegistroRequestDTO);

//			response.setRequiredUpdateConsentimiento(
//					updateConsentimientoResponse.getCodigoRespuesta().equals(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO));

			String urlCotizadorVida = null;
			String urlCotizadorFlex = null;
			String idOportunidadCRM = Strings.EMPTY;
			String idContactoCRM = Strings.EMPTY;

			String tipoDocumentoInputCRM = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_TIPO_DOCUMENTO,
					persona.getTipoDocumento(), Constantes.MULTITABLA_COLUMNA_VALOR_CRM);
			String tipoDocumentoInput = persona.getTipoDocumento();
			String numeroDocumentoInput = persona.getNumeroDocumento();

			LOGGER.info("***** Buscando persona en => VIS_DATA-[MongoDB] *****");
			String tipoDocVal = "";
			if (persona.getTipoDocumento().equals("1")) {
				tipoDocVal = "01";
			} else if (persona.getTipoDocumento().equals("2")) {
				tipoDocVal = "02";
			}

			String idPersonaVal = "01-" + tipoDocVal + "-" + persona.getNumeroDocumento();
			ObtenerPersonaDocumentoResponse responseObtenerPersonaDocumento = globalRestClient
					.obtenerPersonaDocumento2(idPersonaVal);

			if (responseObtenerPersonaDocumento == null) {
				LOGGER.info("***** Insertando datos en => VIS_DATA-[MongoDB] *****");
				// fechaNacimientoOrigen = fechaNacimientoOrigen == null?
				// DateUtil.stringToDate(aDNRegistroRequestDTO.getTitular().getFechaNacimiento(),
				// DateUtil.FORMATO_DIA_YYYYMMDD2) : fechaNacimientoOrigen;
				// String fecgaNac = String.valueOf(persona.getFechaNacimiento());
				/*
				 * String tipoDocVal = ""; if (persona.getTipoDocumento().equals("1")) {
				 * tipoDocVal = "01"; } else if (persona.getTipoDocumento().equals("2")){
				 * tipoDocVal = "02"; }
				 */

				InsertarPersonaDocumentoRequest insertarPersonaDocumentoRequest = new InsertarPersonaDocumentoRequest();
				insertarPersonaDocumentoRequest.setID_PERSONA("01-" + tipoDocVal + "-" + persona.getNumeroDocumento());
				insertarPersonaDocumentoRequest.setID_TIPO_DOCUMENTO(persona.getTipoDocumento());
				insertarPersonaDocumentoRequest.setTIPO_DOCUMENTO(
						Integer.valueOf(persona.getTipoDocumento()) == 1 ? "DNI" : "CARNET DE EXTRANJERIA");
				insertarPersonaDocumentoRequest.setNUMERO_DOCUMENTO(persona.getNumeroDocumento());
				insertarPersonaDocumentoRequest.setNOMBRE(persona.getNombres());
				insertarPersonaDocumentoRequest.setAPELLIDO_PATERNO(persona.getApellidoPaterno());
				insertarPersonaDocumentoRequest.setAPELLIDO_MATERNO(persona.getApellidoMaterno());
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				String fechaNacimiento = format.format(persona.getFechaNacimiento());
				insertarPersonaDocumentoRequest.setFECHA_NACIMIENTO(fechaNacimiento);
				System.out.println(gson.toJson(insertarPersonaDocumentoRequest));
				ObtenerPersonaDocumentoResponse responseInsertPersonaDocumento = globalRestClient
						.insertarPersonaDocumento(insertarPersonaDocumentoRequest);

			}

			LOGGER.info("[" + traza + "] Indicador de cambios prev => " + indicadorCambioDatos);
			if (aDNRegistroRequestDTO.getIndicadorCambio() == 1) {
				indicadorCambioDatos = Constantes.CODIGO_INDICADOR_CAMBIO_SI;
			}
			LOGGER.info("[" + traza + "] Indicador de cambios next => " + indicadorCambioDatos);
			// Si hay cambios graba en CRM
			// if (indicadorCambioDatos == Constantes.CODIGO_INDICADOR_CAMBIO_SI) {
			if (indicadorCambioDatos == Constantes.CODIGO_INDICADOR_CAMBIO_SI) {
				/* CRM - INI - Actualizacion de informacion de informacion */
				LOGGER.info("[" + traza + "] Inicializacion de datos CRM ");
				String generoInput = persona.getGenero();
				String profesionInput = persona.getProfesion();
				String actividadEconomicaInput = persona.getActividadEconomica();
				String fumadorInput = persona.getFumador();
				List<Multitabla> m1 = multitablaRepository
						.findByCodigoTabla(TablaEnum.TABLA_TIPO_DOCUMENTO.getCodigoTabla());
				Multitabla tiposDocumentoCRM = null;
				if (!m1.isEmpty()) {
					LOGGER.info("[" + traza + "] M1 ==> " + tipoDocumentoInput);
					tiposDocumentoCRM = m1.stream().filter(t -> t.getCodigo().equals(tipoDocumentoInput))
							.collect(Collectors.toList()).get(0);
				}

				List<Multitabla> m2 = multitablaRepository.findByCodigoTabla(TablaEnum.TABLA_GENERO.getCodigoTabla());
				Multitabla generoCRM = null;
				if (!m2.isEmpty()) {
					LOGGER.info("[" + traza + "] M2 ==> " + generoInput);
					generoCRM = m2.stream().filter(t -> t.getCodigo().equals(generoInput)).collect(Collectors.toList())
							.get(0);
				}

				List<Multitabla> m3 = multitablaRepository
						.findByCodigoTabla(TablaEnum.TABLA_PROFESION.getCodigoTabla());
				Multitabla profesionCRM = null;
				if (!m3.isEmpty()) {
					LOGGER.info("[" + traza + "] M3 ==> " + profesionInput);
					profesionCRM = m3.stream().filter(t -> t.getCodigo().equals(profesionInput))
							.collect(Collectors.toList()).get(0);
				}

				List<Multitabla> m4 = multitablaRepository
						.findByCodigoTabla(TablaEnum.TABLA_ACTIVIDAD_ECONOMICA.getCodigoTabla());
				Multitabla actividadEconomicaCRM = null;
				if (!m4.isEmpty()) {
					LOGGER.info("[" + traza + "] M4 ==> " + actividadEconomicaInput);
					actividadEconomicaCRM = m4.stream().filter(t -> t.getCodigo().equals(actividadEconomicaInput))
							.collect(Collectors.toList()).get(0);
				}

				List<Multitabla> m5 = multitablaRepository.findByCodigoTabla(TablaEnum.TABLA_FUMADOR.getCodigoTabla());
				Multitabla fumadorCRM = null;
				if (!m5.isEmpty()) {
					LOGGER.info("[" + traza + "] M5 ==> " + fumadorInput);
					fumadorCRM = m5.stream().filter(t -> t.getCodigo().equals(fumadorInput))
							.collect(Collectors.toList()).get(0);
				}

				if (persona.getTipoDocumento().equals("1")) {
					tipoDocVal = "01";
				} else if (persona.getTipoDocumento().equals("2")) {
					tipoDocVal = "02";
				}

				idPersonaVal = "01-" + tipoDocVal + "-" + persona.getNumeroDocumento();
				responseObtenerPersonaDocumento = globalRestClient.obtenerPersonaDocumento2(idPersonaVal);

				idOportunidadCRM = responseObtenerPersonaDocumento.getID_PERSONA();
				idContactoCRM = responseObtenerPersonaDocumento.getID_PERSONA();

				String mensajeOportunidadCRM = "", codigoOportunidadCRM = "", servicioOportunidadCRM = "";

				// -- Procesamiento del documento

				LOGGER.info("[" + traza + "] Inicio Generación de archivo PDF...");
				try {
					LOGGER.info("[" + traza + "] Seteo inicial de datos para plantilla...");

					LOGGER.info("[" + traza + "] Fin de seteo de datos de documento...");
					System.out.println(aDNRegistroRequestDTO);
					// System.out.println(documento);
					// obtener la respuesta de el ok del cliente
//					String urlPDFADN[] = generarPDFADN(aDNRegistroRequestDTO, documento);

//					String urlPDFADNPrivada = "D:\\crm-pdf\\";
					String urlPDFADNPrivada = rutaPDFPrivada;
//					String urlPDFADNPublico = urlPDFADN[1];
					System.out.println(urlPDFADNPrivada);
//					System.out.println(urlPDFADNPublico);

					String numeroDocumento = persona.getNumeroDocumento();
					LOGGER.info("[" + traza + "] URL PUBLICA ADN ==> " + urlPDFADNPrivada);
					String urlPDFADNCloud = System.getProperty("java.io.tmpdir") + File.separator + "adn_"
							+ numeroDocumento + ".pdf";

					// se reprocesa el adn si no existe
					File fileAdn = new File(urlPDFADNCloud);
					boolean exists = fileAdn.exists();
					LOGGER.info("UrlPDFADNCloud: " + urlPDFADNCloud);
					LOGGER.info("FileADN: " + fileAdn.exists());
					if (!exists) {
						this.reprocesarPdfAdn(consentimientoData);
						fileAdn = new File(urlPDFADNCloud);
					}

					try {
						if (!urlPDFADNCloud.equals(Strings.EMPTY)) {
							SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
							String sufixFecha = format.format(new Date());

							String tipoDocumentoCRM = tiposDocumentoCRM.getValorCrm();
							String nombreArchivoPDF = "adn_" + numeroDocumento + ".pdf";
							String descripcionArchivo = "";
							persona.setDocumentoAdn(nombreArchivoPDF);
							personaRepository.save(persona);
							this.enviarPDFCRM2(fileAdn, tipoDocumentoCRM, numeroDocumento, nombreArchivoPDF,
									descripcionArchivo, tipoDocumentoInput);

						}
					} catch (Exception e) {
						LOGGER.info("[" + traza + "] Error enviar PDF ADN a CRM...");
						// e.printStackTrace();
						StringWriter sw = new StringWriter();
						e.printStackTrace(new PrintWriter(sw));
						observaciones.add("No se pudo enviar PDF a CRM. " + sw.toString());
					}
					LOGGER.info("[" + traza + "] Eliminar archivo privado...");
					eliminarFilePrivado(urlPDFADNCloud);

				} catch (Exception e) {
					StringWriter sw = new StringWriter();
					e.printStackTrace(new PrintWriter(sw));

					observaciones.add("No se pudo enviar PDF a CRM. " + sw.toString());
				}
				LOGGER.info("=====Fin Generación de archivo PDF====");
				// Fin Generación de PDF
			} // FIN INDICADOR CAMBIOS

			if (idOportunidadCRM == null || idOportunidadCRM == Strings.EMPTY) {

				if (persona.getTipoDocumento().equals("1")) {
					tipoDocVal = "01";
				} else if (persona.getTipoDocumento().equals("2")) {
					tipoDocVal = "02";
				}

				idPersonaVal = "01-" + tipoDocVal + "-" + persona.getNumeroDocumento();
				responseObtenerPersonaDocumento = globalRestClient.obtenerPersonaDocumento2(idPersonaVal);

				idOportunidadCRM = responseObtenerPersonaDocumento.getID_PERSONA();
				idContactoCRM = responseObtenerPersonaDocumento.getID_PERSONA();

			}

			if (idOportunidadCRM != null && idOportunidadCRM != Strings.EMPTY) {
				adn.setIdOportunidadCrm(idOportunidadCRM);
				observaciones.add("Id Oportunidad: " + idOportunidadCRM);
				adnRepository.save(adn);
			}

			// se obtiene de la tabla SQL
			urlCotizadorVida = obtenerURLCotizadorVida(aDNRegistroRequestDTO, idContactoCRM);
			// GTI.21339
			// urlCotizadorFlex = obtenerURLCotizadorFlex(aDNRegistroRequestDTO,
			// idOportunidadCRM);
			urlCotizadorFlex = Strings.EMPTY;

			// -- INICIO GTI.27780 Carga a Vtiger (contacto)
			LOGGER.info("[" + traza + "] INICIO: Creación o actualización de Contacto en Vtiger");

			try {
				LOGGER.info("[" + traza + "] Valido si existe en VTIGER...");
				ConsultaClienteRequest consultaRequest = new ConsultaClienteRequest();
				consultaRequest.setDocumentoIdentidad(persona.getNumeroDocumento());
				ConsultaClienteResponse clienteVTiger = vtigerRestClient.buscarCliente(consultaRequest);
				String tipoDocVtiger = aDNRegistroRequestDTO.getTitular().getTipoDocumento() != null
						&& aDNRegistroRequestDTO.getTitular().getTipoDocumento() == 1 ? "DNI" : "Carnet Extranjeria";

				if (clienteVTiger.getResult() != null && clienteVTiger.getResult().size() > 0) {
					LOGGER.info("[" + traza + "] Si existe en VTIGER " + clienteVTiger.getResult());
					String assignedUser = clienteVTiger.getResult().get(0).getAssigned_user_id();
					String idContacto = clienteVTiger.getResult().get(0).getId();
					ActualizarContactoRequest actRequestVtiger = new ActualizarContactoRequest();
					actRequestVtiger.setId(idContacto);
					actRequestVtiger.setAssigned_user_id(assignedUser);
					actRequestVtiger.setCf_852(tipoDocVtiger);
					actRequestVtiger.setCf_854(persona.getNumeroDocumento());
					actRequestVtiger.setCf_920(persona.getApellidoMaterno());
					actRequestVtiger.setEmail(persona.getCorreo());
					actRequestVtiger.setFirstname(persona.getNombres());
					actRequestVtiger.setLastname(persona.getApellidoPaterno());
					actRequestVtiger.setPhone(persona.getCelular().toString());
					actRequestVtiger.setCf_886("Si");
					actRequestVtiger.setCf_860(consentimientoData.get("fecha_consentimiento").toString());
					actRequestVtiger.setCf_907(consentimientoData.get("hora_consentimiento").toString());
					actRequestVtiger.setCf_905("Referido Propio");

					LOGGER.info("[" + traza + "] registrarADNS.UpdateRequest.requestVtiger=> " + actRequestVtiger);
					ActualizarContactoResponse responseActualizarContacto = vtigerRestClient
							.actualizarContacto(actRequestVtiger);
					if (responseActualizarContacto.getStatusHttp() != "200") {
						observaciones.add("No se pudo actualizar informacion de contacto en VTiger.");
					}

					LOGGER.info("[" + traza + "] registrarADNS.UpdateRequest.responseActualizarContacto=> "
							+ responseActualizarContacto);

				}
				else {
					LOGGER.info("[" + traza + "] No existe en VTIGER");
					ConsultaUsuarioPorCorreoRequest consultaUsuarioPorCorreoRequest = new ConsultaUsuarioPorCorreoRequest();
					consultaUsuarioPorCorreoRequest.setCorreoUsuario(correoUsuario);
					LOGGER.info("[" + traza + "] VTIGER.Request ==> " + consultaUsuarioPorCorreoRequest);
					ConsultaUsuarioPorCorreoResponse usuarioVtiger = vtigerRestClient
							.buscarUsuarioPorCorreo(consultaUsuarioPorCorreoRequest);
					LOGGER.info("[" + traza + "] VTIGER.Response ==> " + gson.toJson(usuarioVtiger));
					String asignado_a = "";
					if (usuarioVtiger.getResult() != null && usuarioVtiger.getResult().size() > 0) {
						asignado_a = usuarioVtiger.getResult().get(0).getId().toString();
					} else {
						asignado_a = "19x1";
					}

					CrearContactoRequest requestVtiger = new CrearContactoRequest();
					requestVtiger.setFirstname(persona.getNombres());
					requestVtiger.setAssigned_user_id(asignado_a);
					requestVtiger.setCf_852(tipoDocVtiger);
					requestVtiger.setCf_854(persona.getNumeroDocumento());
//					requestVtiger.setCf_860(DateUtil.dateToString(new Date(), DateUtil.FORMATO_DIA_YYYYMMDD2));
					requestVtiger.setCf_886("Si");
//					requestVtiger.setCf_907("00:00:00");
					requestVtiger.setCf_920(persona.getApellidoMaterno());
					requestVtiger.setEmail(persona.getCorreo());
					requestVtiger.setFirstname(persona.getNombres());
					requestVtiger.setLastname(persona.getApellidoPaterno());
					requestVtiger.setLeadsource("Referido ADN");
					requestVtiger.setMobile(persona.getCelular().toString());
					String fec_ultimo_consentimiento = consentimientoData.get("fec_ultimo_consentimiento").toString();
					// Parsea el input a un LocalDateTime
					LocalDateTime dateTime = LocalDateTime.parse(fec_ultimo_consentimiento, DateTimeFormatter.ISO_DATE_TIME);
					// Formatea la fecha y hora como strings
					String fechaConsentimiento = dateTime.toLocalDate().toString();
					String horaConsentimiento = dateTime.toLocalTime().truncatedTo(ChronoUnit.SECONDS).toString();

					if (consentimientoData.containsKey("fecha_consentimiento") && consentimientoData.get("fecha_consentimiento") != null) {
						requestVtiger.setCf_860(consentimientoData.get("fecha_consentimiento").toString());
					}else{
						requestVtiger.setCf_860(fechaConsentimiento);
					}
					if (consentimientoData.containsKey("hora_consentimiento") && consentimientoData.get("hora_consentimiento") != null) {
						requestVtiger.setCf_907(consentimientoData.get("hora_consentimiento").toString());
					}else{
						requestVtiger.setCf_907(horaConsentimiento);
					}
					requestVtiger.setCf_905("Referido Propio");

					LOGGER.info("[" + traza + "] registrarADNS.UpdateRequest.requestVtiger ==> " + requestVtiger);
					CrearContactoResponse responseCrearContacto = vtigerRestClient.crearContacto(requestVtiger);
					if (responseCrearContacto.getStatusHttp() != "200") {
						observaciones.add("No se pudo actualizar informacion de contacto en VTiger.");
					}
					LOGGER.info("[" + traza + "] registrarADNS.UpdateRequest.responseCrearContacto ==> "
							+ responseCrearContacto);
				}

			} catch (Exception e) {
				LOGGER.info("[" + traza + "] ERROR al grabar en VTiger ==> " + e);
				observaciones.add("ERROR al grabar cliente en VTiger ==>" + e.getMessage());
			}

			LOGGER.info("[" + traza + "] FIN: Creación o actualización de Contacto en Vtiger ");
			// -- FIN GTI.27780 Carga a Vtiger (contacto)
			/* Cotizador Web - FIN - Generar token */

			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
			response.setMensajeRespuesta(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_EXITO));

			TokenRequestDTO tokenRequestDTO = new TokenRequestDTO();
			tokenRequestDTO.setNombreCliente(aDNRegistroRequestDTO.getTitular().getNombres() + " "
					+ aDNRegistroRequestDTO.getTitular().getApellidoPaterno() + " "
					+ aDNRegistroRequestDTO.getTitular().getApellidoMaterno());
			tokenRequestDTO.setSexo(aDNRegistroRequestDTO.getTitular().getGenero().toString());
			tokenRequestDTO.setFechaNacimiento(aDNRegistroRequestDTO.getTitular().getFechaNacimiento());
			tokenRequestDTO.setEdadActuarial(aDNRegistroRequestDTO.getTitular().getEdadActuarial().toString());
			tokenRequestDTO.setRol("Agente");
			tokenRequestDTO.setNombreUsuario(aDNRegistroRequestDTO.getNombreVendedorCRM());
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			String fechaCotizacion = format.format(new Date());
			tokenRequestDTO.setFechaCotizacion(fechaCotizacion);
			tokenRequestDTO.setNumeroCotizacion("500008000");

			response.setIdAdn(adn.getIdAdn().intValue());
			response.setIdOportunidadCRM(idOportunidadCRM);
			response.setIdContactoCRM(idContactoCRM);
			response.setUrlCotizadorVida(urlCotizadorVida);
			response.setUrlCotizadorFlex(urlCotizadorFlex);
			LOGGER.info("[" + traza + "] ADN Registro correctamente ==> " + gson.toJson(response));
		} catch (SivTXException e) {
			LOGGER.error("[" + traza + "] ERROR SivTXException ==> " + e.getMsjError());
			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);

			// response.setMensajeRespuesta(e.getMsjError());
		}
		catch (Exception e) {
			LOGGER.error("[" + traza + "] ERROR Exception ==> " + e.getMessage(), e);
			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			// response.setMensajeRespuesta(Utilitarios.obtenerMensaje(messageSource,
			// Constantes.MENSAJE_RESPUESTA_GENERAL_ERR0R));
			response.setMensajeRespuesta(Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_REGISTRAR_ADNS));
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			response.setObjErrorResource(new ErrorResourceDTO("ADN-003", sw.toString()));
		}
		// response.setObservaciones(gson.toJson(observaciones));
		/*logRepository.save(new EventLog("Registro ADN", response.getCodigoRespuesta(), aDNRegistroRequestDTO.getPath(),
				response.getMensajeRespuesta(), gson.toJson(response.getObjErrorResource()), gson.toJson(observaciones),
				aDNRegistroRequestDTO.getIdUsuario(), aDNRegistroRequestDTO.getDevice(),
				aDNRegistroRequestDTO.getOs()));*/
		LOGGER.info("[" + traza + "] Salio AdnServiceImpl#registrarADNS(ADNRegistroRequestDTO)");

		return response;
	}

	public ADNRegistroResponseDTO registrarInternoADNS(ADNRegistroRequestDTO aDNRegistroRequestDTO) {
		String traza = Utilitarios.trazaLog();
		traza = traza + "-" + aDNRegistroRequestDTO.getTitular().getNumeroDocumento();
		LOGGER.info("[" + traza + "] Entro AdnServiceImpl#registrarADNS(ADNRegistroRequestDTO)");

		ADNRegistroResponseDTO response = new ADNRegistroResponseDTO();
		Adn adn = null;
		Persona persona = null;
		PlanFuturo planFuturo = null;
		Integer indicadorCambioDatos = aDNRegistroRequestDTO.getIndicadorCambio();
		String correoUsuario = aDNRegistroRequestDTO.getCorreoUsuario();
		List<String> observaciones = new ArrayList<>();

		LOGGER.info("[" + traza + "] Indicador de cambio: " + aDNRegistroRequestDTO.getIndicadorCambio());

		Gson gson = new Gson();

		if (aDNRegistroRequestDTO.getFormulario() == null) {
			aDNRegistroRequestDTO.setFormulario(aDNRegistroRequestDTO.getTipo());
		}

		try {
			if (aDNRegistroRequestDTO.getTipo() == 2) {

				adn = adnRepository.findByTipoNumeroDocumento(
						aDNRegistroRequestDTO.getTitular().getTipoDocumento().toString(),
						aDNRegistroRequestDTO.getTitular().getNumeroDocumento());

				if (adn == null) {
					adn = new Adn();
					adn.setUsuarioCrea(aDNRegistroRequestDTO.getIdUsuario());
					adn.setFechaCrea(new Date());
				}

				persona = personaRepository.findByTipoNumeroDocumento(
						aDNRegistroRequestDTO.getTitular().getTipoDocumento().toString(),
						aDNRegistroRequestDTO.getTitular().getNumeroDocumento());

				if (persona == null) {
					persona = new Persona();
					persona.setFechaCrea(new Date());
					persona.setUsuarioCrea(aDNRegistroRequestDTO.getIdUsuario());
				}

				if (aDNRegistroRequestDTO.getFormulario() == 1) {
					Date hoy = new Date();
					String fecha = DateUtil.dateToString(hoy, DateUtil.FORMATO_DIA_DDMMYYYY);
					String hora = LocalTime.now().toString().substring(0, 8);
					aDNRegistroRequestDTO.setFechaLDPDP(fecha + " " + hora);
					adn.setLdpdpFecha(aDNRegistroRequestDTO.getFechaLDPDP());
				}

				if (aDNRegistroRequestDTO.getPlanFuturo() != null) {
					planFuturo = new PlanFuturo();
					planFuturo.setUsuarioCrea(aDNRegistroRequestDTO.getIdUsuario());
					planFuturo.setFechaCrea(new Date());
				}

			} else {
				response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
				response.setMensajeRespuesta(
						Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_ERR0R));
				return response;
			}
			LOGGER.info("[" + traza + "] Datos básicos de persona...");
			String actividadEconomicaOrigen = aDNRegistroRequestDTO.getTitular().getActividadEconomica() == null ? null
					: aDNRegistroRequestDTO.getTitular().getActividadEconomica().toString();
			String apellidoMaternoOrigen = aDNRegistroRequestDTO.getTitular().getApellidoMaterno() == null ? null
					: aDNRegistroRequestDTO.getTitular().getApellidoMaterno().toUpperCase().trim();
			String apellidoPaternoOrigen = aDNRegistroRequestDTO.getTitular().getApellidoPaterno() == null ? null
					: aDNRegistroRequestDTO.getTitular().getApellidoPaterno().toUpperCase().trim();
			String nombreOrigen = aDNRegistroRequestDTO.getTitular().getNombres() == null ? null
					: aDNRegistroRequestDTO.getTitular().getNombres().toUpperCase().trim();
			String tipoDocumentoOrigen = aDNRegistroRequestDTO.getTitular().getTipoDocumento() == null ? null
					: aDNRegistroRequestDTO.getTitular().getTipoDocumento().toString();
			LOGGER.info("[" + traza + "] Fecha de nacimiento...");
			Date fechaNacimientoOrigen = aDNRegistroRequestDTO.getTitular().getFechaNacimiento() == null ? null
					: DateUtil.stringToDate(aDNRegistroRequestDTO.getTitular().getFechaNacimiento(),
							DateUtil.FORMATO_DIA_DDMMYYYY);
			if (fechaNacimientoOrigen != null) {
				fechaNacimientoOrigen.setHours(6);// GTI.24117
			}
			LOGGER.info("[" + traza + "] Datos de fumador de persona...");
			String fumadorOrigen = aDNRegistroRequestDTO.getTitular().getFumador() == null ? null
					: aDNRegistroRequestDTO.getTitular().getFumador().toString();
			LOGGER.info("[" + traza + "] Datos de genero de persona...");
			String generoOrigen = aDNRegistroRequestDTO.getTitular().getGenero() == null ? null
					: aDNRegistroRequestDTO.getTitular().getGenero().toString();
			LOGGER.info("[" + traza + "] Datos complementarios de persona...");
			String profesionOrigen = aDNRegistroRequestDTO.getTitular().getProfesion() == null ? null
					: aDNRegistroRequestDTO.getTitular().getProfesion().toString();
			String tipoCasaOrigen = aDNRegistroRequestDTO.getTitular().getTipoCasa() == null ? null
					: aDNRegistroRequestDTO.getTitular().getTipoCasa().toString();
			String tieneVehiculoOrigen = aDNRegistroRequestDTO.getTitular().getTieneVehiculo() == null ? null
					: aDNRegistroRequestDTO.getTitular().getTieneVehiculo().toString();
			String tieneAfpOrigen = aDNRegistroRequestDTO.getTitular().getTieneAfp() == null ? null
					: aDNRegistroRequestDTO.getTitular().getTieneAfp().toString();
			String tieneSeguroVidaOrigen = aDNRegistroRequestDTO.getTitular().getTieneSeguroVida() == null ? null
					: aDNRegistroRequestDTO.getTitular().getTieneSeguroVida().toString();

			LOGGER.info("[" + traza + "] Seteando datos de conversion de persona...");
			persona.setApellidoMaterno(Utilitarios.quitaEspacios(apellidoMaternoOrigen));
			persona.setApellidoPaterno(Utilitarios.quitaEspacios(apellidoPaternoOrigen));
			persona.setCelular(aDNRegistroRequestDTO.getTitular().getCelular());
			persona.setCorreo(aDNRegistroRequestDTO.getTitular().getCorreo());
			persona.setEdadActuarial(aDNRegistroRequestDTO.getTitular().getEdadActuarial());
			persona.setFechaNacimiento(fechaNacimientoOrigen);
			persona.setGenero(generoOrigen);
			persona.setNombres(Utilitarios.quitaEspacios(nombreOrigen));
			persona.setNumeroDocumento(aDNRegistroRequestDTO.getTitular().getNumeroDocumento());
			persona.setTipoDocumento(tipoDocumentoOrigen);
			persona.setTipoCasa(tipoCasaOrigen);
			persona.setTieneVehiculo(tieneVehiculoOrigen);
			persona.setTieneAfp(tieneAfpOrigen);
			persona.setTieneSeguroVida(tieneSeguroVidaOrigen);

			if (persona.getFumador() == null) {
				persona.setFumador(fumadorOrigen);
			}

			if (persona.getProfesion() == null) {
				persona.setProfesion(profesionOrigen);
			}

			if (persona.getActividadEconomica() == null) {
				persona.setActividadEconomica(actividadEconomicaOrigen);
			}
			// LOGGER.info("["+ traza + "] Persona => " + gson.toJson(persona));
			LOGGER.info("[" + traza + "] Intentanto grabar datos de persona...");
			try {
				LOGGER.info("[" + traza + "] Se grabó persona...");

			} catch (Exception e) {
				LOGGER.info("[" + traza + "] Error => Grabar persona:: " + e);
			}
			Familiar familiar = null;
			if (aDNRegistroRequestDTO.getFamiliar() != null) {
				for (ADNRegistroFamiliarRequestDTO aDNfamiliar : aDNRegistroRequestDTO.getFamiliar()) {
					familiar = new Familiar();
					String nombreFamiliarOrigen = aDNfamiliar.getNombres() == null ? null
							: aDNfamiliar.getNombres().toUpperCase();
					String tipoFamiliarOrigen = aDNfamiliar.getTipoRelacion() == null ? null
							: aDNfamiliar.getTipoRelacion().toString();
					familiar.setEdad(aDNfamiliar.getEdad());
					familiar.setNombres(nombreFamiliarOrigen);
					familiar.setTipoRelacion(tipoFamiliarOrigen);
					familiar.setCentroLaboral(aDNfamiliar.getCentroLaboral());
					familiar.setPersona(persona);
					familiar.setFechaCrea(new Date());
					familiar.setUsuarioCrea(aDNRegistroRequestDTO.getIdUsuario());

					LOGGER.info("[" + traza + "] Se grabó familiar...");
				}
			}
			if (aDNRegistroRequestDTO.getPlanFuturo() != null) {
				LOGGER.info("[" + traza + "] Buscar plan futuro...");

				if (planFuturo == null) {
					planFuturo = new PlanFuturo();
				}

				LOGGER.info("[" + traza + "] Seteando plan futuro...");
				planFuturo.setPlanEducacion(aDNRegistroRequestDTO.getPlanFuturo().getBase().getPlanEducacion());
				planFuturo.setPlanJubilacion(aDNRegistroRequestDTO.getPlanFuturo().getBase().getPlanJubilacion());
				planFuturo.setPlanProyecto(aDNRegistroRequestDTO.getPlanFuturo().getBase().getPlanProyecto());
				planFuturo.setInformacionAdicional(
						aDNRegistroRequestDTO.getPlanFuturo().getBase().getInformacionAdicional());

				if (aDNRegistroRequestDTO.getPlanFuturo() != null) {
					if (aDNRegistroRequestDTO.getPlanFuturo().getBase() != null) {
						Float ingresoTitularOrigen = aDNRegistroRequestDTO.getPlanFuturo().getBase()
								.getIngresoTitular() == null ? null
										: aDNRegistroRequestDTO.getPlanFuturo().getBase().getIngresoTitular()
												.floatValue();
						planFuturo.setIngresoTitular(ingresoTitularOrigen);

						Float porcentajeIngresoOrigen = aDNRegistroRequestDTO.getPlanFuturo().getBase()
								.getPorcentajeIngreso() == null ? null
										: aDNRegistroRequestDTO.getPlanFuturo().getBase().getPorcentajeIngreso()
												.floatValue();
						planFuturo.setPorcentajeInversion(porcentajeIngresoOrigen);

						Float totalIngresoOrigen = aDNRegistroRequestDTO.getPlanFuturo().getBase()
								.getTotalIngreso() == null ? null
										: aDNRegistroRequestDTO.getPlanFuturo().getBase().getTotalIngreso()
												.floatValue();
						planFuturo.setTotalInversion(totalIngresoOrigen);

						planFuturo
								.setAniosProteger(aDNRegistroRequestDTO.getPlanFuturo().getBase().getAnioProteccion());
					}

					if (aDNRegistroRequestDTO.getPlanFuturo().getRespaldo() != null) {
						Float respaldoAhorroOrigen = (aDNRegistroRequestDTO.getPlanFuturo().getRespaldo()
								.getAhorro() == null ? null
										: aDNRegistroRequestDTO.getPlanFuturo().getRespaldo().getAhorro().floatValue());
						planFuturo.setRespaldoAhorros(respaldoAhorroOrigen);

						Float respaldoPropiedadOrigen = aDNRegistroRequestDTO.getPlanFuturo().getRespaldo()
								.getPropiedad() == null ? null
										: aDNRegistroRequestDTO.getPlanFuturo().getRespaldo().getPropiedad()
												.floatValue();
						planFuturo.setRespaldoPropiedades(respaldoPropiedadOrigen);

						Float respaldoVehiculoOrigen = aDNRegistroRequestDTO.getPlanFuturo().getRespaldo()
								.getVehiculo() == null ? null
										: aDNRegistroRequestDTO.getPlanFuturo().getRespaldo().getVehiculo()
												.floatValue();
						planFuturo.setRespaldoVehiculos(respaldoVehiculoOrigen);

						Float respaldoSeguroVidaOrigen = aDNRegistroRequestDTO.getPlanFuturo().getRespaldo()
								.getSeguroVida() == null ? null
										: aDNRegistroRequestDTO.getPlanFuturo().getRespaldo().getSeguroVida()
												.floatValue();
						planFuturo.setRespaldoSeguroVida(respaldoSeguroVidaOrigen);

						Float respaldoSeguroVidaLeyOrigen = aDNRegistroRequestDTO.getPlanFuturo().getRespaldo()
								.getSeguroVidaLey() == null ? null
										: aDNRegistroRequestDTO.getPlanFuturo().getRespaldo().getSeguroVidaLey()
												.floatValue();
						planFuturo.setRespaldoSeguroVidaLey(respaldoSeguroVidaLeyOrigen);

						planFuturo.setRespaldoAhorrosCheck(
								aDNRegistroRequestDTO.getPlanFuturo().getRespaldo().getAhorroCheck());
						planFuturo.setRespaldoPropiedadesCheck(
								aDNRegistroRequestDTO.getPlanFuturo().getRespaldo().getPropiedadCheck());
						planFuturo.setRespaldoSeguroVidaCheck(
								aDNRegistroRequestDTO.getPlanFuturo().getRespaldo().getSeguroVidaCheck());
						planFuturo.setRespaldoSeguroVidaLeyCheck(
								aDNRegistroRequestDTO.getPlanFuturo().getRespaldo().getSeguroVidaLeyCheck());
						planFuturo.setRespaldoVehiculosCheck(
								aDNRegistroRequestDTO.getPlanFuturo().getRespaldo().getVehiculoCheck());

					}

					if (aDNRegistroRequestDTO.getPlanFuturo().getCapitalProteger() != null) {
						planFuturo.setCapitalAfpCheck(
								aDNRegistroRequestDTO.getPlanFuturo().getCapitalProteger().getAfpCheck());

						Float capitalProteccion = aDNRegistroRequestDTO.getPlanFuturo().getCapitalProteger()
								.getCapitalProteccion() == null ? null
										: aDNRegistroRequestDTO.getPlanFuturo().getCapitalProteger()
												.getCapitalProteccion().floatValue();
						planFuturo.setCapitalProteccion(capitalProteccion);

						Float capitalRespaldoEconomico = aDNRegistroRequestDTO.getPlanFuturo().getCapitalProteger()
								.getRespaldoEconomico() == null ? null
										: aDNRegistroRequestDTO.getPlanFuturo().getCapitalProteger()
												.getRespaldoEconomico().floatValue();
						planFuturo.setCapitalRespaldo(capitalRespaldoEconomico);

						Float capitalAfp = aDNRegistroRequestDTO.getPlanFuturo().getCapitalProteger().getAfp() == null
								? null
								: aDNRegistroRequestDTO.getPlanFuturo().getCapitalProteger().getAfp().floatValue();
						planFuturo.setCapitalAfp(capitalAfp);

						Float capitalTotalCapitalProteger = aDNRegistroRequestDTO.getPlanFuturo().getCapitalProteger()
								.getTotalCapitalProteger() == null ? null
										: aDNRegistroRequestDTO.getPlanFuturo().getCapitalProteger()
												.getTotalCapitalProteger().floatValue();
						planFuturo.setCapitalTotalProteger(capitalTotalCapitalProteger);

						Float capitalAdicional = aDNRegistroRequestDTO.getPlanFuturo().getCapitalProteger()
								.getAdicional() == null ? null
										: aDNRegistroRequestDTO.getPlanFuturo().getCapitalProteger().getAdicional()
												.floatValue();
						planFuturo.setCapitalAdicional(capitalAdicional);

						Float capitalNuevoCapitalProteger = aDNRegistroRequestDTO.getPlanFuturo().getCapitalProteger()
								.getNuevoCapitalProteger() == null ? null
										: aDNRegistroRequestDTO.getPlanFuturo().getCapitalProteger()
												.getNuevoCapitalProteger().floatValue();
						planFuturo.setCapitalTotalProtegerNuevo(capitalNuevoCapitalProteger);
					}

				}

				planFuturo.setPersona(persona);
				// planFuturoRepository.save(planFuturo);
			}

			adn.setIdCircuitoFirma(aDNRegistroRequestDTO.getIdCircuitoFirma() == null ? ""
					: aDNRegistroRequestDTO.getIdCircuitoFirma());
			if (adn.getIdAdn() == null) {
				adn.setIdAdn(aDNRegistroRequestDTO.getIdAdn());
			}
			adn.setPersona(persona);
			adn.setIdAgente(aDNRegistroRequestDTO.getIdUsuario());
			adn.setLdpdp(aDNRegistroRequestDTO.getFlagLDPDP());

			LOGGER.info("[" + traza + "] Guardar ADN...");

			String urlCotizadorVida = null;
			String urlCotizadorFlex = null;
			String idOportunidadCRM = Strings.EMPTY;
			String idContactoCRM = Strings.EMPTY;

			String tipoDocumentoInput = persona.getTipoDocumento();

			LOGGER.info("[" + traza + "] Actualización CRM...");
			LOGGER.info("[" + traza + "] Indicador de cambios prev => " + indicadorCambioDatos);
			// if (aDNRegistroRequestDTO.getIndicadorCambio() == 1) {
			// indicadorCambioDatos = Constantes.CODIGO_INDICADOR_CAMBIO_SI;
			// }
			LOGGER.info("[" + traza + "] Indicador de cambios next => " + indicadorCambioDatos);
			// Si hay cambios graba en CRM
			if (1 == 1) {
				/* CRM - INI - Actualizacion de informacion de informacion */
				LOGGER.info("[" + traza + "] Inicializacion de datos CRM ");
				String generoInput = persona.getGenero();
				String profesionInput = persona.getProfesion();
				String actividadEconomicaInput = persona.getActividadEconomica();
				String fumadorInput = persona.getFumador();
				List<Multitabla> m1 = multitablaRepository
						.findByCodigoTabla(TablaEnum.TABLA_TIPO_DOCUMENTO.getCodigoTabla());
				Multitabla tiposDocumentoCRM = null;
				if (!m1.isEmpty()) {
					LOGGER.info("[" + traza + "] M1 ==> " + tipoDocumentoInput);
					tiposDocumentoCRM = m1.stream().filter(t -> t.getCodigo().equals(tipoDocumentoInput))
							.collect(Collectors.toList()).get(0);
				}

				List<Multitabla> m2 = multitablaRepository.findByCodigoTabla(TablaEnum.TABLA_GENERO.getCodigoTabla());
				Multitabla generoCRM = null;
				if (!m2.isEmpty()) {
					LOGGER.info("[" + traza + "] M2 ==> " + generoInput);
					generoCRM = m2.stream().filter(t -> t.getCodigo().equals(generoInput)).collect(Collectors.toList())
							.get(0);
				}

				List<Multitabla> m3 = multitablaRepository
						.findByCodigoTabla(TablaEnum.TABLA_PROFESION.getCodigoTabla());
				Multitabla profesionCRM = null;
				if (!m3.isEmpty()) {
					LOGGER.info("[" + traza + "] M3 ==> " + profesionInput);
					profesionCRM = m3.stream().filter(t -> t.getCodigo().equals(profesionInput))
							.collect(Collectors.toList()).get(0);
				}

				List<Multitabla> m4 = multitablaRepository
						.findByCodigoTabla(TablaEnum.TABLA_ACTIVIDAD_ECONOMICA.getCodigoTabla());
				Multitabla actividadEconomicaCRM = null;
				if (!m4.isEmpty()) {
					LOGGER.info("[" + traza + "] M4 ==> " + actividadEconomicaInput);
					actividadEconomicaCRM = m4.stream().filter(t -> t.getCodigo().equals(actividadEconomicaInput))
							.collect(Collectors.toList()).get(0);
				}

				List<Multitabla> m5 = multitablaRepository.findByCodigoTabla(TablaEnum.TABLA_FUMADOR.getCodigoTabla());
				Multitabla fumadorCRM = null;
				if (!m5.isEmpty()) {
					LOGGER.info("[" + traza + "] M5 ==> " + fumadorInput);
					fumadorCRM = m5.stream().filter(t -> t.getCodigo().equals(fumadorInput))
							.collect(Collectors.toList()).get(0);
				}

				UpdateRequest requestCRM = new UpdateRequest();
				requestCRM.setParam(new UpdateDetRequest());
				requestCRM.getParam().setTipoDocumento(tiposDocumentoCRM.getValor());
				requestCRM.getParam().setCodigoTipoDocumento(tiposDocumentoCRM.getValorCrm());
				requestCRM.getParam().setNumeroDocumento(persona.getNumeroDocumento());
				requestCRM.getParam().setFechaNacimiento(
						DateUtil.dateToString(persona.getFechaNacimiento(), DateUtil.FORMATO_DIA_DDMMYYYY));
				requestCRM.getParam().setNombres(persona.getNombres().toUpperCase());
				requestCRM.getParam().setApellidoPaterno(persona.getApellidoPaterno().toUpperCase());
				requestCRM.getParam().setApellidoMaterno(persona.getApellidoMaterno().toUpperCase());
				requestCRM.getParam().setSexo(generoCRM.getValor().toUpperCase());
				requestCRM.getParam().setCodigoSexo(generoCRM.getValorCrm());
				requestCRM.getParam().setTelefonoCelular(persona.getCelular().toString());
				requestCRM.getParam().setEmail(persona.getCorreo());
				requestCRM.getParam().setProfesion(profesionCRM.getValor().toUpperCase());
				requestCRM.getParam().setCodigoProfesion(profesionCRM.getValorCrm());
				requestCRM.getParam().setActividadEconomica(actividadEconomicaCRM.getValor().toUpperCase());
				requestCRM.getParam().setCodigoActividadEconomica(actividadEconomicaCRM.getValorCrm());
				requestCRM.getParam().setCodigoFumador(fumadorCRM.getValorCrm());
				requestCRM.getParam().setFumador(fumadorCRM.getValor().toUpperCase());
				requestCRM.getParam().setDomainName("DINTERSEGURO\\" + aDNRegistroRequestDTO.getIdUsuario());

				LOGGER.info("[" + traza + "] registrarADNS.UpdateRequest.requestCRM ==> " + gson.toJson(requestCRM));

				String mensajeOportunidadCRM = "", codigoOportunidadCRM = "", servicioOportunidadCRM = "";

				// -- Procesamiento del documento

				LOGGER.info("[" + traza + "] Inicio Generación de archivo PDF...");
				try {
					LOGGER.info("[" + traza + "] Seteo inicial de datos para plantilla...");
					String tipoDocumento = tiposDocumentoCRM.getValor();
					String documentoIdentidad = persona.getNumeroDocumento();
					String nombreCliente = persona.getNombres();
					String apellidoPaterno = persona.getApellidoPaterno();
					String apellidoMaterno = persona.getApellidoMaterno();
					String fechaNacimiento = persona.getFechaNacimiento() == null ? ""
							: DateUtil.dateToString(persona.getFechaNacimiento(), DateUtil.FORMATO_DIA_DDMMYYYY);
					String sexo = generoCRM.getValor();
					String telefonoCelular = persona.getCelular().toString();
					String fumador = fumadorCRM.getValor();
					String correoElectronico = persona.getCorreo();
					String profesion = profesionCRM.getValor();
					String actividadEconomica = actividadEconomicaCRM.getValor();
					String tipoCasa = persona.getTipoCasa();
					String tieneVehiculo = persona.getTieneVehiculo() != null && persona.getTieneVehiculo().equals("1")
							? "Si"
							: "No";
					String tieneSeguroVida = persona.getTieneSeguroVida() != null
							&& persona.getTieneSeguroVida().equals("1") ? "Si" : "No";
					String tieneAfp = persona.getTieneVehiculo() != null && persona.getTieneAfp().equals("1") ? "Si"
							: "No";
					String nombreArchivo = "adn_completo_generado";
					boolean tieneFamiliares = aDNRegistroRequestDTO.getFamiliar() != null;

					LOGGER.info("[" + traza + "] Fin Seteo de datos para plantilla...");

					LOGGER.info("[" + traza + "] Inicio de seteo de datos de documento...");
					DocumentoADN documento = new DocumentoADN();
					documento.setTipo_documento(tipoDocumento);
					documento.setDocumento(documentoIdentidad);
					documento.setNombres(nombreCliente);
					documento.setApellido_paterno(apellidoPaterno);
					documento.setApellido_materno(apellidoMaterno);
					documento.setNacimiento(fechaNacimiento);
					documento.setSexo(sexo);
					documento.setTelefono(telefonoCelular);
					documento.setFumador(fumador);
					documento.setEmail(correoElectronico);
					documento.setProfesion(profesion);
					documento.setActividad(actividadEconomica);
					documento.setTipoCasa(tipoCasa);
					documento.setTieneAFP(tieneAfp);
					documento.setTieneSeguroVida(tieneSeguroVida);
					documento.setTieneVehiculo(tieneVehiculo);

					Date hoy = new Date();
					if (aDNRegistroRequestDTO.getFechaLDPDP() != null) {
						String fecha[] = aDNRegistroRequestDTO.getFechaLDPDP().split(" ");
						documento.setFecha(fecha[0]);
						documento.setHora(fecha[1]);
					} else {
						documento.setFecha(DateUtil.dateToString(hoy, DateUtil.FORMATO_DIA_DDMMYYYY));
						documento.setHora(LocalTime.now().toString().substring(0, 8));
					}

					if (planFuturo != null) {
						String checkPlanEducacion = planFuturo.getPlanEducacion() == null ? "  "
								: planFuturo.getPlanEducacion() == 1 ? "X" : "  ";
						String checkPlanProyecto = planFuturo.getPlanProyecto() == null ? "  "
								: planFuturo.getPlanProyecto() == 1 ? "X" : "  ";
						String checkPlanJubilacion = planFuturo.getPlanJubilacion() == null ? "  "
								: planFuturo.getPlanJubilacion() == 1 ? "X" : "  ";

						String ingresoTitular = planFuturo.getIngresoTitular() == null ? ""
								: this.formatoMiles(planFuturo.getIngresoTitular());
						String aniosTitular = planFuturo.getAniosProteger() == null ? ""
								: planFuturo.getAniosProteger().toString();
						String porcentajeTitular = planFuturo.getPorcentajeInversion() == null ? ""
								: String.format("%,.0f", planFuturo.getPorcentajeInversion());// ((Integer)
						// planFuturo.getPorcentajeInversion()).toString();
						String totalTitular = planFuturo.getTotalInversion() == null ? ""
								: this.formatoMiles(planFuturo.getTotalInversion());

						String respaldoAhorros = planFuturo.getRespaldoAhorros() == null ? ""
								: this.formatoMiles(planFuturo.getRespaldoAhorros());
						String respaldoAhorrosCheck = planFuturo.getRespaldoAhorrosCheck() == null ? "  "
								: planFuturo.getRespaldoAhorrosCheck() == 1 ? "X" : "  ";
						String respaldoPropiedades = planFuturo.getRespaldoPropiedades() == null ? ""
								: this.formatoMiles(planFuturo.getRespaldoPropiedades());
						String respaldoPropiedadesCheck = planFuturo.getRespaldoPropiedadesCheck() == null ? "  "
								: planFuturo.getRespaldoPropiedadesCheck() == 1 ? "X" : "  ";
						String respaldoVehiculos = planFuturo.getRespaldoVehiculos() == null ? ""
								: this.formatoMiles(planFuturo.getRespaldoVehiculos());
						String respaldoVehiculosCheck = planFuturo.getRespaldoVehiculosCheck() == null ? "  "
								: planFuturo.getRespaldoVehiculosCheck() == 1 ? "X" : "  ";
						String respaldoSeguroVida = planFuturo.getRespaldoSeguroVida() == null ? ""
								: this.formatoMiles(planFuturo.getRespaldoSeguroVida());
						String respaldoSeguroVidaCheck = planFuturo.getRespaldoSeguroVidaCheck() == null ? "  "
								: planFuturo.getRespaldoSeguroVidaCheck() == 1 ? "X" : "  ";
						String respaldoSeguroVidaLey = planFuturo.getRespaldoSeguroVidaLey() == null ? ""
								: this.formatoMiles(planFuturo.getRespaldoSeguroVidaLey());
						String respaldoSeguroVidaLeyCheck = planFuturo.getRespaldoSeguroVidaLeyCheck() == null ? "  "
								: planFuturo.getRespaldoSeguroVidaLeyCheck() == 1 ? "X" : "  ";

						String capitalProteccion = planFuturo.getCapitalProteccion() == null ? ""
								: this.formatoMiles(planFuturo.getCapitalProteccion());
						String capitalRespaldo = planFuturo.getCapitalRespaldo() == null ? ""
								: this.formatoMiles(planFuturo.getCapitalRespaldo());
						String afp = planFuturo.getCapitalAfp() == null ? ""
								: this.formatoMiles(planFuturo.getCapitalAfp());
						String afpCheck = planFuturo.getCapitalAfpCheck() == null ? ""
								: planFuturo.getCapitalAfpCheck() == 1 ? "X" : "";
						String capitalProteger = planFuturo.getCapitalTotalProteger() == null ? ""
								: this.formatoMiles(planFuturo.getCapitalTotalProteger());
						String capitalAdicional = planFuturo.getCapitalAdicional() == null ? ""
								: this.formatoMiles(planFuturo.getCapitalAdicional());
						String capitalProtegerNuevo = planFuturo.getCapitalTotalProtegerNuevo() == null ? ""
								: this.formatoMiles(planFuturo.getCapitalTotalProtegerNuevo());

						documento.setCheck_educacion(checkPlanEducacion);
						documento.setCheck_proyectos(checkPlanProyecto);
						documento.setCheck_jubilacion(checkPlanJubilacion);
						documento.setTitular_ingreso(ingresoTitular);
						documento.setTitular_anios(aniosTitular);
						documento.setTitular_porcentaje(porcentajeTitular);
						documento.setTitular_total(totalTitular);
						documento.setAhorros(respaldoAhorros);
						documento.setCheck_ahorros(respaldoAhorrosCheck);
						documento.setPropiedades(respaldoPropiedades);
						documento.setCheck_propiedades(respaldoPropiedadesCheck);
						documento.setVehiculos(respaldoVehiculos);
						documento.setCheck_vehiculos(respaldoVehiculosCheck);
						documento.setSeguro_vida(respaldoSeguroVida);
						documento.setCheck_seguro_vida(respaldoSeguroVidaCheck);
						documento.setSeguro_ley(respaldoSeguroVidaLey);
						documento.setCheck_seguro_ley(respaldoSeguroVidaLeyCheck);
						documento.setCapital_proteccion(capitalProteccion);
						documento.setRespaldo_economico(capitalRespaldo);
						documento.setAfp(afp);
						documento.setCheck_afp(afpCheck);
						documento.setCapital_proteger(capitalProteger);
						documento.setAdicional(capitalAdicional);
						documento.setNuevo_capital(capitalProtegerNuevo);
					} else {
						documento.setCheck_educacion("");
						documento.setCheck_proyectos("");
						documento.setCheck_jubilacion("");
						documento.setTitular_ingreso("");
						documento.setTitular_anios("");
						documento.setTitular_porcentaje("");
						documento.setTitular_total("");
						documento.setAhorros("");
						documento.setCheck_ahorros("");
						documento.setPropiedades("");
						documento.setCheck_propiedades("");
						documento.setVehiculos("");
						documento.setCheck_vehiculos("");
						documento.setSeguro_vida("");
						documento.setCheck_seguro_vida("");
						documento.setSeguro_ley("");
						documento.setCheck_seguro_ley("");
						documento.setCapital_proteccion("");
						documento.setRespaldo_economico("");
						documento.setAfp("");
						documento.setCheck_afp("");
						documento.setCapital_proteger("");
						documento.setAdicional("");
						documento.setNuevo_capital("");
					}

					LOGGER.info("[" + traza + "] Fin de seteo de datos de documento...");

					String urlPDFADN[] = generarPDFADN(aDNRegistroRequestDTO, documento);

					String urlPDFADNPrivada = urlPDFADN[0];
					// String urlPDFADNPublico = urlPDFADN[1];
					response.setMensajeRespuesta(urlPDFADNPrivada);
					LOGGER.info("[" + traza + "] URL PUBLICA ADN GENERA PDF INTERNA==> " + urlPDFADNPrivada);

				} catch (Exception e) {
					StringWriter sw = new StringWriter();
					e.printStackTrace(new PrintWriter(sw));

					observaciones.add("No se pudo enviar PDF a CRM. " + sw.toString());
				}
				LOGGER.info("=====Fin Generación de archivo PDF====");
				// Fin Generación de PDF
			} // FIN INDICADOR CAMBIOS

		} catch (SivTXException e) {
			LOGGER.error("[" + traza + "] ERROR SivTXException ==> " + e.getMsjError());
			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);

			// response.setMensajeRespuesta(e.getMsjError());
		} catch (Exception e) {
			LOGGER.error("[" + traza + "] ERROR Exception ==> " + e.getMessage());
			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			// response.setMensajeRespuesta(Utilitarios.obtenerMensaje(messageSource,
			// Constantes.MENSAJE_RESPUESTA_GENERAL_ERR0R));
			response.setMensajeRespuesta("[ADN-003] Error al intentar registrar datos en ADN");
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			response.setObjErrorResource(new ErrorResourceDTO("ADN-003", sw.toString()));
		}
		// response.setObservaciones(gson.toJson(observaciones));

		return response;
	}

	private String generaAdnCompletoPdf() {

		return "ok";
	}

	private void enviarCorreoDatos(String numeroDocumento, String asunto, String destinatario, List<String> adjuntos,
			String... datos) {

		System.out.println(numeroDocumento);
		System.out.println(gson.toJson(asunto));
		System.out.println(gson.toJson(destinatario));
		System.out.println(gson.toJson(adjuntos));
		System.out.println(gson.toJson(datos));
		String traza = Utilitarios.trazaLog();
		traza = traza + "-" + numeroDocumento;
		LOGGER.info("[" + traza
				+ "] Entro AdnServiceImpl#enviarCorreoDatos(numeroDocumento,asunto,destinario,adjuntos,datos)");
		InputStream stream;
		String bodyHtml = "";
		LOGGER.info("[" + traza + "] Obteniendo plantilla ...");
		Gson gson = new Gson();
		try {
			stream = new ClassPathResource(Constantes.RUTA_PLANTILLA + "/" + Constantes.PLANTILLA_ADN_CORREO)
					.getInputStream();
			String systemOp = System.getProperty("os.name");
			if (!systemOp.contains("Windows")) {
				stream = new FileInputStream(new File(rutaPlantillaDocLinux + "//" + Constantes.PLANTILLA_ADN_CORREO));
			}
			bodyHtml = StreamUtils.copyToString(stream, Charset.defaultCharset());
		} catch (IOException e1) {
			LOGGER.error("[" + traza + "] Error al obtener plantilla ==> " + e1.getMessage());
		}
		String body = bodyHtml;
		body = body.replaceAll("\\{nombre\\}", datos[0]);
		body = body.replaceAll("\\{nombreAgente\\}", datos[1]);

		EnviarCorreoRequest enviarCorreoRequest = new EnviarCorreoRequest();
		enviarCorreoRequest.setpRemitente(this.enviarCorreoRemitente);
		enviarCorreoRequest.setpDisplayName("Noreply");
		if (!serverPort.toUpperCase().equals(Constantes.SERVER_PORT_PRODUCCION)) {
			LOGGER.info("[" + traza + "] Seteo de datos para ambiente menor......");
			enviarCorreoRequest.setpDestinatario(enviarCorreoSolicitudEmailPruebas);
			enviarCorreoRequest.setpAsunto("TEST:" + asunto);
		} else {
			LOGGER.info("[" + traza + "] Seteo de datos para ambiente produccion");
			enviarCorreoRequest.setpDestinatario(destinatario);
			enviarCorreoRequest.setpAsunto(asunto);
		}
		enviarCorreoRequest.setpMensaje(body);
		enviarCorreoRequest.setpRutaArchivoAdjunto(adjuntos);
		LOGGER.info("[" + traza + "] Enviando mensaje.request ==> " + gson.toJson(enviarCorreoRequest));
		/*
		 * try { interseguroRestClient.enviarCorreo(enviarCorreoRequest); } catch
		 * (Exception e) { LOGGER.info("[" + traza +
		 * "] Ocurrio un error al enviar correo ==> " + e.getMessage()); }
		 */
		LOGGER.info("[" + traza
				+ "] Salio AdnServiceImpl#enviarCorreoDatos(numeroDocumento,asunto,destinario,adjuntos,datos)");
	}

	public ADNAutoguardadoResponseDTO autoguardado(ADNAutoguardadoRequestDTO aDNAutoguardadoRequestDTO) {
		String traza = Utilitarios.trazaLog();
		traza = traza + "-" + aDNAutoguardadoRequestDTO.getTitular().getNumeroDocumento();
		LOGGER.info("[" + traza + "] Entro AdnServiceImpl#autoguardado(aDNAutoguardadoRequestDTO)");

		ADNAutoguardadoResponseDTO response = new ADNAutoguardadoResponseDTO();
		Autoguardado autoguardado = null;
		Gson gson = new Gson();
		try {
			if (aDNAutoguardadoRequestDTO.getIdAutoguardado().isEmpty()) {
				autoguardado = new Autoguardado();
				autoguardado.setUsuarioCrea(aDNAutoguardadoRequestDTO.getIdUsuario());
				autoguardado.setFechaCrea(new Date());
			} else {
				Optional<Autoguardado> find = autoguardadoRepository
						.findById(Long.valueOf(aDNAutoguardadoRequestDTO.getIdAutoguardado()));
				LOGGER.info("[" + traza + "] Buscando autoguardado ==> " + find.toString());
				if (find.equals(Optional.empty())) {
					LOGGER.error("[" + traza + "] No existe registro...");
					throw new Exception(
							Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_ERR0R));
				}
				autoguardado = find.get();
				LOGGER.error("[" + traza + "] ID: " + autoguardado.getIdAutoguardado().toString());
				autoguardado.setUsuarioModif(aDNAutoguardadoRequestDTO.getIdUsuario());
				autoguardado.setFechaModif(new Date());
			}

			autoguardado.setTipoDocumento(aDNAutoguardadoRequestDTO.getTitular().getTipoDocumento());
			autoguardado.setNumeroDocumento(aDNAutoguardadoRequestDTO.getTitular().getNumeroDocumento());
			autoguardado.setDatosJson(gson.toJson(aDNAutoguardadoRequestDTO).getBytes());

			autoguardadoRepository.save(autoguardado);

			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
			response.setMensajeRespuesta(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_EXITO));
			response.setIdAutoguardado(autoguardado.getIdAutoguardado().toString());

		} catch (Exception e) {
			LOGGER.error("[" + traza + "] Error => " + e.getMessage());
			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			response.setMensajeRespuesta(e.getMessage());
		}

		LOGGER.info("[" + traza + "] Salio AdnServiceImpl#autoguardado(aDNAutoguardadoRequestDTO)");
		return response;
	}

	/*
	 * @Override public UsuarioIngresoResponseDTO
	 * validarUsuario(UsuarioIngresoRequestDTO usuarioIngresoRequestDTO) {
	 * UsuarioIngresoResponseDTO response = new UsuarioIngresoResponseDTO();
	 * ValidarAccesoRequest requestCRM = new ValidarAccesoRequest();
	 *
	 * LOGGER.info("Entro AdnServiceImpl#validarUsuario(usuarioIngresoRequestDTO)");
	 *
	 * try { String usuario = usuarioIngresoRequestDTO.getUsuario(); StringBuilder
	 * usuarioCRM = new StringBuilder(); usuarioCRM.append("DINTERSEGURO\\");
	 * usuarioCRM.append(usuario);
	 *
	 * requestCRM.setDomainName(usuarioCRM.toString());
	 *
	 * ObtenerDatosUsuarioRequest requestAzman = new ObtenerDatosUsuarioRequest();
	 * requestAzman.setAplicacion("SEGURIDAD");
	 * requestAzman.setDominio("DINTERSEGURO");
	 * requestAzman.setUsuario(usuarioIngresoRequestDTO.getUsuario());
	 * requestAzman.setContrasena(usuarioIngresoRequestDTO.getPassword());
	 *
	 * LOGGER.info("inicio.responseAzman=>" + requestAzman);
	 *
	 * ObtenerDatosUsuarioResponse responseAzman =
	 * interseguroRestClient.obtenerDatosUsuario(requestAzman);
	 *
	 * LOGGER.info("fin.responseAzman=>" + responseAzman);
	 *
	 * ValidarAccesoResponse responseCRM =
	 * crmRestClient.validarAccesoCrm(requestCRM);
	 *
	 * LOGGER.info("responseCRM=>" + responseCRM);
	 *
	 * if (responseAzman != null && responseCRM != null) { if
	 * (responseCRM.getRespuesta() == "true") {
	 * response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
	 * response.setMensajeRespuesta(
	 * Utilitarios.obtenerMensaje(messageSource,
	 * Constantes.MENSAJE_RESPUESTA_GENERAL_EXITO)); response.setJwtToken(
	 * "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IiBEb2UiLCJhZG1pbiI6dHJ1ZX0.Wp-p679n41Cnj0yM7egVvDLGR6zWvNIvuulUOFRmtWs"
	 * ); response.setIdUsuario(usuarioIngresoRequestDTO.getUsuario());
	 * response.setNombreUsuario(responseAzman.getNombreCompleto());
	 *
	 * response.setIdUsuarioCRM(responseCRM.getIdUsuario());
	 * LOGGER.info(responseCRM.getIdUsuario());
	 * response.setCodigoAgenciaCRM(responseCRM.getAgencia());
	 * response.setCodigoVendedorCRM(responseCRM.getNumVendedor()); } else { throw
	 * new SivSOAException(responseCRM.getMensaje(), null); } } else { throw new
	 * Exception("No se pudo validar en CRM"); } } catch (SivSOAException e) {
	 * LOGGER.error("Error SivSOAException=> " + e.getMessage());
	 * response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
	 * response.setMensajeRespuesta(e.getMessage()); } catch (Exception e) {
	 * LOGGER.error("Error Exception=> " + e.getMessage());
	 * response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
	 * response.setMensajeRespuesta(e.getMessage()); }
	 *
	 * LOGGER.info("Salio AdnServiceImpl#validarUsuario(usuarioIngresoRequestDTO)");
	 *
	 * return response; }
	 */
	private void enviarCorreoError(Object requestDTO, String tipoError, String asunto, String mensajeError) {

		// -- Body
		StringBuilder body = new StringBuilder();
		body.append("<b>Tipo: </b>").append(tipoError) // -- Transaccion, BaseDatos, SOA
				.append("<br>").append("<b>JSON del request " + requestDTO.getClass().getSimpleName() + ":</b>")
				.append("<br>").append(new Gson().toJson(requestDTO)).append("<br>").append("<b>MENSAJE ERROR:</b>")
				.append("<br>").append(mensajeError);

		// -- Enviar
		EnviarCorreoRequestNuevo enviarCorreoRequest = new EnviarCorreoRequestNuevo();
		String[] correoSeparado = enviarCorreoDestinatario.split(",");
		List<Remitente> listaRemitente = new ArrayList<>();
		Remitente remitente;
		for (String correo : correoSeparado) {
			remitente = new Remitente();
			remitente.setEmail(correo);
			listaRemitente.add(remitente);
		}

		enviarCorreoRequest.setTitle("Encuesta de clientes");
		enviarCorreoRequest.setTo(listaRemitente);
		enviarCorreoRequest.setSubject(asunto.concat(" - ").concat(activeProfile));
		enviarCorreoRequest.setHtmlContent(body.toString());

		try {
			// No enviar correo por el momento.
			// interseguroRestClient.enviarCorreo(enviarCorreoRequest);

		} catch (Exception e) {
			LOGGER.error("ERROR - AdnServiceImpl#enviarCorreoError(requestDTO, tipoError, asunto, mensajeError): "
					+ e.getMessage());
			e.printStackTrace();
		}

	}

	private String obtenerURLCotizadorVida(ADNRegistroRequestDTO aDNRegistroRequestDTO, String contactoID) {

		String urlCotizador = Strings.EMPTY;
		String numDocumento = aDNRegistroRequestDTO.getTitular().getNumeroDocumento();
		String userID = aDNRegistroRequestDTO.getIdUsuarioCRM();
		String contactID = contactoID;

		urlCotizador = String.format(this.urlCotizadorVida, numDocumento, userID, contactID);

		LOGGER.info("obtenerURLCotizadorVidaurlCotizadorVida.NEXT=>" + urlCotizador);

		return urlCotizador;
	}

	@Deprecated
	private String obtenerURLCotizadorFlex(ADNRegistroRequestDTO aDNRegistroRequestDTO, String idOportunidadCRM) {
		String urlCotizador = null;

		try {

			String codTipoDocumento = Strings.EMPTY;
			String codSexo = Strings.EMPTY;
			String codFumador = Strings.EMPTY;
			String numVendedor = aDNRegistroRequestDTO.getCodigoVendedorCRM();
			String idOportunidad = idOportunidadCRM;

			// Tipo Documento
			if (aDNRegistroRequestDTO.getTitular().getTipoDocumento() != null) {
				String codigoADN = aDNRegistroRequestDTO.getTitular().getTipoDocumento().toString();

				List<Multitabla> m1 = multitablaRepository
						.findByCodigoTabla(TablaEnum.TABLA_TIPO_DOCUMENTO.getCodigoTabla());
				if (!m1.isEmpty()) {
					List<Multitabla> filtro = m1.stream().filter(t -> t.getCodigo().equals(codigoADN))
							.collect(Collectors.toList());
					if (filtro != null && !filtro.isEmpty() && filtro.get(0).getValorFlex() != null) {
						codTipoDocumento = filtro.get(0).getValorFlex();
					}
				}
			}

			// Sexo / Genero
			if (aDNRegistroRequestDTO.getTitular().getGenero() != null) {
				String codigoADN = aDNRegistroRequestDTO.getTitular().getGenero().toString();

				List<Multitabla> m1 = multitablaRepository.findByCodigoTabla(TablaEnum.TABLA_GENERO.getCodigoTabla());
				if (!m1.isEmpty()) {
					List<Multitabla> filtro = m1.stream().filter(t -> t.getCodigo().equals(codigoADN))
							.collect(Collectors.toList());
					if (filtro != null && !filtro.isEmpty() && filtro.get(0).getValorFlex() != null) {
						codSexo = filtro.get(0).getValorFlex();
					}
				}
			}

			// Fumador
			if (aDNRegistroRequestDTO.getTitular().getFumador() != null) {
				String codigoADN = aDNRegistroRequestDTO.getTitular().getFumador().toString();

				List<Multitabla> m1 = multitablaRepository.findByCodigoTabla(TablaEnum.TABLA_FUMADOR.getCodigoTabla());
				if (!m1.isEmpty()) {
					List<Multitabla> filtro = m1.stream().filter(t -> t.getCodigo().equals(codigoADN))
							.collect(Collectors.toList());
					if (filtro != null && !filtro.isEmpty() && filtro.get(0).getValorFlex() != null) {
						codFumador = filtro.get(0).getValorFlex();
					}
				}
			}

			StringBuilder sb = new StringBuilder();
			sb.append(this.urlCotizadorFlex);
			sb.append("rut_persona=").append(aDNRegistroRequestDTO.getTitular().getNumeroDocumento());
			sb.append("&ape_paterno=").append(aDNRegistroRequestDTO.getTitular().getApellidoPaterno());
			sb.append("&ape_materno=").append(aDNRegistroRequestDTO.getTitular().getApellidoMaterno());
			sb.append("&nom_persona=").append(aDNRegistroRequestDTO.getTitular().getNombres());
			sb.append("&fec_nacimiento=").append(aDNRegistroRequestDTO.getTitular().getFechaNacimiento());

			sb.append("&cod_tipo_documento=").append(codTipoDocumento);
			sb.append("&cod_sexo=").append(codSexo);
			sb.append("&cod_tipo_fumador=").append(codFumador);
			sb.append("&num_vendedor=").append(numVendedor);
			sb.append("&id_Oportunidad=").append(idOportunidad);
			sb.append("&modo=0&flag=N&indTipoPagina=1");

			urlCotizador = sb.toString();
		} catch (Exception e) {
			LOGGER.error("Error obtenerURLCotizadorFlex => " + e.getMessage());
		}

		return urlCotizador;
	}

	@Override
	public BaseResponseDTO generarPDFADN2(String tipoDocumento, String numeroDocumento) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		BaseResponseDTO response = new BaseResponseDTO();
		String[] urlADN = new String[2];
		Integer Respuesta = 0;
		String urlPublicoADN = Strings.EMPTY;
		LOGGER.info("Preparando PDF");
		try {
			// Obtiene Plantilla
			String systemOsName = System.getProperty("os.name");
			String rutaPlantillaPDFADN = Strings.EMPTY;

			if (systemOsName.contains("Linux")) {
				rutaPlantillaPDFADN = this.rutaPlantillaDocLinux + "//" + Constantes.PLANTILLA_COMPLETA;
			} else {
				String basePlantilla = Constantes.RUTA_PLANTILLA + "/" + Constantes.PLANTILLA_COMPLETA;
				rutaPlantillaPDFADN = getClass().getClassLoader().getResource(basePlantilla).getFile();
			}
			LOGGER.info("rutaPlantillaPDFADN=>" + rutaPlantillaPDFADN);

			File plantillaPDFADN = new File(rutaPlantillaPDFADN);

			LOGGER.info("Ruta de plantilla generada" + plantillaPDFADN.getAbsolutePath());

			// Leyendo plantilla PDF ADN
			FileInputStream fileInputStream = new FileInputStream(plantillaPDFADN);
			BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

			// ############# INICIO CONTEXT ##################
			IXDocReport xdocReport = XDocReportRegistry.getRegistry().loadReport(bufferedInputStream,
					TemplateEngineKind.Freemarker);

			LOGGER.info("IXDocReport Correcto!!!");

			// -- Parametros
			IContext context = xdocReport.createContext();
			FieldsMetadata fieldsmetadata = xdocReport.createFieldsMetadata();
			fieldsmetadata.load("documento", DocumentoADN.class);
			fieldsmetadata.load("Familiares", DocumentoADNFamiliar.class, true);
			fieldsmetadata.load("Referidos", DocumentoADNReferido.class, true);
			fieldsmetadata.load("Polizas", DocumentoADNPoliza.class, true);

			xdocReport.setFieldsMetadata(fieldsmetadata);

			LOGGER.info("IXDocReport setFieldsMetadata Correcto!!!");

			// Para extraer la data desde la base de datos
//				Solicitud solicitudEntidad = solicitudRepository.findByNumeroCotizacion(numeroCotizacion);
			System.out.println(tipoDocumento);
			System.out.println(numeroDocumento);
			Integer tipoDocumen = Integer.parseInt(tipoDocumento);
			Persona personaEntidad = personaRepository.findByTipoNumeroDocumento(tipoDocumen.toString(),
					numeroDocumento);
			LOGGER.info("PersonaEntidad: " + personaEntidad.toString());
			List<Familiar> familiarEntidad = familiarRepository.findByIdPersona(personaEntidad.getIdPersona());
//			System.out.println(gson.toJson(familiarEntidad));
			LOGGER.info("Familiar: " + familiarEntidad.toString());
			PlanFuturo planFuturoEntidad = planFuturoRepository.findByIdPersona(personaEntidad.getIdPersona());

			// #### DOCUMENTO
			DocumentoADN documento = new DocumentoADN();
			documento.setNombres(personaEntidad.getNombres());
			documento.setApellido_paterno(personaEntidad.getApellidoPaterno());
			documento.setApellido_materno(personaEntidad.getApellidoMaterno());
			documento.setDocumento(personaEntidad.getNumeroDocumento());

			Date fecha = new Date();
			documento.setFecha(DateUtil.dateToString(fecha, DateUtil.FORMATO_DIA_DDMMYYYY));

			LocalDateTime locaDate = LocalDateTime.now();
			int hours = locaDate.getHour();
			int minutes = locaDate.getMinute();
			int seconds = locaDate.getSecond();
			documento.setHora(hours + ":" + minutes + ":" + seconds);

			List<Multitabla> mtipoDocumento = multitablaRepository
					.findByCodigoTabla(TablaEnum.TABLA_TIPO_DOCUMENTO.getCodigoTabla());
			Multitabla tiposDocumentoCRM0 = null;
			if (!mtipoDocumento.isEmpty()) {
				System.out.println("============M1=========" + personaEntidad.getTipoDocumento());
				if (personaEntidad.getTipoDocumento() != null) {
					List<Multitabla> listItem = mtipoDocumento.stream()
							.filter(t -> t.getCodigo().equals(personaEntidad.getTipoDocumento()))
							.collect(Collectors.toList());
					if (listItem.size() > 0) {
						tiposDocumentoCRM0 = listItem.get(0);
					}
				}
			}
			documento.setTipo_documento(tiposDocumentoCRM0.getValor());

			String fechaNacimiento = DateUtil.dateToString(personaEntidad.getFechaNacimiento(),
					DateUtil.FORMATO_DIA_DDMMYYYY);
			documento.setNacimiento(fechaNacimiento);

			List<Multitabla> mGenero = multitablaRepository.findByCodigoTabla(TablaEnum.TABLA_GENERO.getCodigoTabla());
			Multitabla tiposGeneroCRM0 = null;
			if (!mGenero.isEmpty()) {
				System.out.println("============M1=========" + personaEntidad.getGenero());
				if (personaEntidad.getGenero() != null) {
					List<Multitabla> listItem = mGenero.stream()
							.filter(t -> t.getCodigo().equals(personaEntidad.getGenero())).collect(Collectors.toList());
					if (listItem.size() > 0) {
						tiposGeneroCRM0 = listItem.get(0);
					}
				}
			}
			documento.setSexo(tiposGeneroCRM0.getValor());

			documento.setTelefono(personaEntidad.getCelular().toString());
			documento.setEmail(personaEntidad.getCorreo());
			documento.setTipoCasa(personaEntidad.getTipoCasa());

			// Tiene Vehículo → Condicional
			if (personaEntidad.getTieneVehiculo().equals("2")) {
				documento.setTieneVehiculo("Si");
			} else {
				documento.setTieneVehiculo("No");
			}

			// Tiene AFP → Condicional
			if (personaEntidad.getTieneAfp().equals("2")) {
				documento.setTieneAFP("Si");
			} else {
				documento.setTieneAFP("No");
			}

			// Tiene SeguroVida → Condicional
			if (personaEntidad.getTieneSeguroVida().equals("2")) {
				documento.setTieneSeguroVida("Si");
			} else {
				documento.setTieneSeguroVida("No");
			}

			String checkPlanEducacion = planFuturoEntidad.getPlanEducacion() == null ? "  "
					: planFuturoEntidad.getPlanEducacion() == 1 ? "X" : "  ";
			String checkPlanProyecto = planFuturoEntidad.getPlanProyecto() == null ? "  "
					: planFuturoEntidad.getPlanProyecto() == 1 ? "X" : "  ";
			String checkPlanJubilacion = planFuturoEntidad.getPlanJubilacion() == null ? "  "
					: planFuturoEntidad.getPlanJubilacion() == 1 ? "X" : "  ";

			documento.setCheck_educacion(checkPlanEducacion);
			documento.setCheck_proyectos(checkPlanProyecto);
			documento.setCheck_jubilacion(checkPlanJubilacion);
			documento.setTitular_ingreso(planFuturoEntidad.getIngresoTitular().toString());
			documento.setTitular_anios("");
			if (planFuturoEntidad.getAniosProteger() != null) {
				documento.setTitular_anios(planFuturoEntidad.getAniosProteger().toString());
			}

			LOGGER.info("IXDocReport Documentos Correcto!!!");
			// #### FIN DOCUMENTO

			// ### POLIZAS
			List<DocumentoADNPoliza> polizas = new ArrayList<DocumentoADNPoliza>();
			ObtenerPolizaRequest request = new ObtenerPolizaRequest();
			String tipoDoc = "";
			if (tipoDocumento.equals("1")) {
				tipoDoc = "01";
			} else if (tipoDocumento.equals("2")) {
				tipoDoc = "02";
			}
			request.setNroDoc(numeroDocumento);
			request.setTipoDoc(tipoDoc);

			// ObtenerPolizaResponse globalResponse =
			// globalRestClient.obtenerPolizas(request);
			List<Poliza> polizasList = polizaRepository.findByPoliza(tipoDoc, numeroDocumento);
			System.out.println(gson.toJson(polizasList));
			/*
			 * LOGGER.info("gson.toJson(globalResponse)");
			 * LOGGER.info(gson.toJson(globalResponse)); for(PolizaResponse item :
			 * globalResponse.getPolizas()) { DocumentoADNPoliza poliza = new
			 * DocumentoADNPoliza(); poliza.setNumeroPoliza(item.getNumero_poliza());
			 * poliza.setFechaEmision(item.getFecha_emision());
			 * poliza.setNombreProducto(item.getNombreProducto()); 
			 * ObtenerDetallePolizaRequest requestDetalle = new
			 * ObtenerDetallePolizaRequest();
			 * requestDetalle.setNumPoliza(item.getNumero_poliza()); PolizaDetalleResponse
			 * globalDetalleResponse =
			 * globalRestClient.obtenerDetallePoliza(requestDetalle);
			 * LOGGER.info("gson.toJson(globalDetalleResponse)");
			 * LOGGER.info(gson.toJson(globalDetalleResponse));
			 * poliza.setPagadoHasta(globalDetalleResponse.getFecha_pagado_hasta());
			 * poliza.setCoberturaVidaAdicional(globalDetalleResponse.
			 * getCobertura_vida_adicional()); poliza.setMoneda(item.getMoneda());
			 * poliza.setEstadoPoliza(item.getEstado());
			 * poliza.setMontoCoberturaPrincipal(item.getMonto_cobertura_principal());
			 * polizas.add(poliza); }
			 */
			LOGGER.info("==> polizas");
			LOGGER.info(gson.toJson(polizasList));
			if (polizasList != null) {
				polizasList.stream().forEach(i -> {

					DocumentoADNPoliza poliza = new DocumentoADNPoliza();

					poliza.setNumeroPoliza(i.getNumeroPoliza());
					poliza.setFechaEmision(i.getFechaEmision());
					poliza.setNombreProducto(i.getNombreProducto());

					poliza.setPagadoHasta(i.getPagadoHasta());
					poliza.setCoberturaVidaAdicional(i.getVidaAdicional());
					poliza.setMoneda(i.getMoneda());
					poliza.setEstadoPoliza(i.getEstadoPoliza());
					poliza.setMontoCoberturaPrincipal(i.getMontoCobertura());

					polizas.add(poliza);

				});
			}

			LOGGER.info("IXDocReport Polizas Correcto!!!");
			// ### FIN POLIZAS

			// #### FAMILIARES
			// Gson gson = new Gson();
			List<Multitabla> mRelacion = multitablaRepository
					.findByCodigoTabla(TablaEnum.TABLA_TIPO_RELACION.getCodigoTabla());
			List<DocumentoADNFamiliar> familiares = new ArrayList<DocumentoADNFamiliar>();
			if (familiarEntidad != null && familiarEntidad.size() > 0) {
				for (Familiar f : familiarEntidad) {
					String nombreFamiliar = f.getNombres().toUpperCase();
					String tipoRelacion = Strings.EMPTY;
					List<Multitabla> mSearch = mRelacion.stream()
							.filter(t -> t.getCodigo().equals(f.getTipoRelacion().toString()))
							.collect(Collectors.toList());
					if (mSearch != null && mSearch.size() > 0) {
						tipoRelacion = mSearch.get(0).getValor();
					}
					familiares.add(new DocumentoADNFamiliar(nombreFamiliar, tipoRelacion, f.getEdad().toString()));
				}
			}
			// #### FIN FAMILIARES
			LOGGER.info("IXDocReport Familiares Correcto!!!");

			// ### REFERIDOS
			List<DocumentoADNReferido> referidos = new ArrayList<DocumentoADNReferido>();

			if (personaEntidad != null) {
				if (personaEntidad.getReferidos() != null && personaEntidad.getReferidos().size() > 0) {
					for (Referido r : personaEntidad.getReferidos()) {
						DocumentoADNReferido refe = new DocumentoADNReferido();
						refe.setNombres(r.getNombres());
						refe.setTelefono(r.getTelefono());
						referidos.add(refe);
					}
				}
			}
			LOGGER.info("IXDocReport Referidos Correcto!!!");
			// for(ADNRegistroReferidoRequestDTO r : aDNRegistroRequestDTO.getR)
			// ### FIN REFERIDOS

			context.put("documento", documento);
			context.put("Familiares", familiares);
			context.put("Referidos", referidos);
			context.put("Polizas", polizas);

			// ############# FIN CONTEXT ##################
			LOGGER.info("FIN context");
			String formatoAdnFilePrivada = "";
			// Crear Archivo Temporal -- Se puede Omitir
			String nombreArchivoTemporal = "adn";
			String nombreArchivoTemporal1 = "adn_completo_generado_temporal.pdf";
			File fileTemportalPDFADN = File.createTempFile(nombreArchivoTemporal, Constantes.ARCHIVO_EXT_PDF);
			System.out.println(hours);
			System.out.println(minutes);
			System.out.println(seconds);
			String hora = "";
			if (hours < 10) {
				hora = "0" + hours;
			} else {
				hora = String.valueOf(hours);
			}
			String minuto = "";
			if (minutes < 10) {
				minuto = "0" + minutes;
			} else {
				minuto = String.valueOf(minutes);
			}
			String segundo = "";
			if (seconds < 10) {
				segundo = "0" + seconds;
			} else {
				segundo = String.valueOf(seconds);
			}

			formatoAdnFilePrivada = rutaPDFPublica + "".concat(nombreArchivoTemporal).concat("_")
					.concat(DateUtil.dateToString(fecha, DateUtil.FORMATO_DIA_YYYYMMDD)).concat("_")
					.concat(hora + minuto + segundo + ".pdf");// .concat(".pdf");//File ffpep =
																// this.obtenerPlantilla(Constantes.PLANTILLA_FORMATO_PEP);
			File adn = new File(formatoAdnFilePrivada);
			// FileUtils fileTemportalPDFADN = FileUtils.copyFile(nombreArchivoTemporal,
			// Constantes.ARCHIVO_EXT_PDF);

			nombreArchivoTemporal = fileTemportalPDFADN.getName();

			System.out.println(nombreArchivoTemporal);
			LOGGER.info("nombreArchivoTemporal: " + nombreArchivoTemporal);

			// Generar archivo
			xdocReport.convert(context, Options.getTo(ConverterTypeTo.PDF), out);

			LOGGER.info("urlPublicoADN=>" + urlPublicoADN);
			File docOut = new File("docOut");
			FileUtils.writeByteArrayToFile(docOut, out.toByteArray());

			FileUtils.copyFile(docOut, adn);
			String tipoDocumentoCRM = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_TIPO_DOCUMENTO,
					tipoDocumento, Constantes.MULTITABLA_COLUMNA_VALOR_CRM);
			

			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
			response.setMensajeRespuesta("Se Gerero el consentimiento con exito.");
			fileInputStream.close();
			bufferedInputStream.close();

		} catch (Exception e) {

			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			response.setMensajeRespuesta("No se puedo generar el documento.");
			LOGGER.info("Error GenerarPDFADN =>" + e);

		}

		return response;
		// return new ByteArrayInputStream(out.toByteArray());
	}

	private void enviarCorreoConAdjunto(String asunto, String email, String body, List<Adjuntos> files, String motivo, String cotizacion, String documento, String usuario) {
		// -- Enviar
		StringBuilder sbAsunto = new StringBuilder();
		EnviarCorreoRequestNuevo enviarCorreoRequest = new EnviarCorreoRequestNuevo();
		enviarCorreoRequest.setTitle("Encuesta de clientes");
		String[] correoSeparado = enviarCorreoSolicitudEmailPruebas.split(",");
		List<Remitente> listaRemitente = new ArrayList<>();
		Remitente remitente;

		if (!serverPort.toUpperCase().equals(Constantes.SERVER_PORT_PRODUCCION)) {
			for (String correo : correoSeparado) {
				remitente = new Remitente();
				remitente.setEmail(correo);
				listaRemitente.add(remitente);
			}
			sbAsunto.append("TEST-:");
		} else {
			remitente = new Remitente();
			remitente.setEmail(email);
			listaRemitente.add(remitente);
		}
		enviarCorreoRequest.setTo(listaRemitente);
		sbAsunto.append(asunto);

		enviarCorreoRequest.setSubject(sbAsunto.toString());
		enviarCorreoRequest.setHtmlContent(body.toString());
		enviarCorreoRequest.setAttachments(files);
		enviarCorreoRequest.setUsuario(usuario);

		LOGGER.info("EnviarCorreoRequest=>" + gson.toJson(enviarCorreoRequest));

		try {
			interseguroRestClient.enviarCorreo(enviarCorreoRequest, motivo, cotizacion, documento);
		} catch (Exception ex) {
			LOGGER.error("interseguroRestClient.enviarCorreo: " +email+" - " + ex.toString());
		}
	}

	private String valorMultiTabla(String codigoTabla, String codigo, String campo) {
		String result = Strings.EMPTY;

		if (codigo == null || codigo.equals(Strings.EMPTY))
			return result;

		List<Multitabla> lista = multitablaRepository.findByCodigoTablaSinEstado(codigoTabla);
		if (lista != null && lista.size() > 0) {
			for (Multitabla m : lista) {
				if (m.getCodigo().equals(codigo)) {
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

	public String[] generarPDFADN(ADNRegistroRequestDTO aDNRegistroRequestDTO, DocumentoADN documento) {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		String[] urlADN = new String[2];
		System.out.println(gson.toJson(urlADN));
		String urlPublicoADN = Strings.EMPTY;
		System.out.println(urlPublicoADN);
		LOGGER.info("Preparando PDF");
		try {
			String systemOsName = System.getProperty("os.name");
			String rutaPlantillaPDFADN = Strings.EMPTY;
			Date fecha = new Date();
			LocalDateTime locaDate = LocalDateTime.now();
			int hours = locaDate.getHour();
			int minutes = locaDate.getMinute();
			int seconds = locaDate.getSecond();

			if (systemOsName.contains("Linux")) {
				rutaPlantillaPDFADN = this.rutaPlantillaDocLinux + "//" + Constantes.PLANTILLA_COMPLETA;
			} else {
				String basePlantilla = Constantes.RUTA_PLANTILLA + "/" + Constantes.PLANTILLA_COMPLETA;
				rutaPlantillaPDFADN = getClass().getClassLoader().getResource(basePlantilla).getFile();
			}

			LOGGER.info("rutaPlantillaPDFADN=> " + rutaPlantillaPDFADN);

			File plantillaPDFADN = new File(rutaPlantillaPDFADN);

			LOGGER.info("Ruta de plantilla generada " + plantillaPDFADN.getAbsolutePath());

			// Leyendo plantilla PDF ADN
			FileInputStream fileInputStream = new FileInputStream(plantillaPDFADN);
			BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

			// ############# INICIO CONTEXT ##################
			IXDocReport xdocReport = XDocReportRegistry.getRegistry().loadReport(bufferedInputStream,
					TemplateEngineKind.Freemarker);

			LOGGER.info("IXDocReport Correcto!!!");

			// -- Parametros
			IContext context = xdocReport.createContext();
			FieldsMetadata fieldsmetadata = xdocReport.createFieldsMetadata();
			fieldsmetadata.load("documento", DocumentoADN.class);
			fieldsmetadata.load("Familiares", DocumentoADNFamiliar.class, true);
			fieldsmetadata.load("Referidos", DocumentoADNReferido.class, true);
			fieldsmetadata.load("Polizas", DocumentoADNPoliza.class, true);

			xdocReport.setFieldsMetadata(fieldsmetadata);

			LOGGER.info("IXDocReport setFieldsMetadata Correcto!!!");
			// #### FAMILIARES
			Gson gson = new Gson();
			LOGGER.info(gson.toJson(aDNRegistroRequestDTO));
			List<Multitabla> mRelacion = multitablaRepository
					.findByCodigoTabla(TablaEnum.TABLA_TIPO_RELACION.getCodigoTabla());
			List<DocumentoADNFamiliar> familiares = new ArrayList<DocumentoADNFamiliar>();
			if (aDNRegistroRequestDTO.getFamiliar() != null && aDNRegistroRequestDTO.getFamiliar().size() > 0) {
				for (ADNRegistroFamiliarRequestDTO f : aDNRegistroRequestDTO.getFamiliar()) {
					String nombreFamiliar = f.getNombres().toUpperCase();
					String tipoRelacion = Strings.EMPTY;
					List<Multitabla> mSearch = mRelacion.stream()
							.filter(t -> t.getCodigo().equals(f.getTipoRelacion().toString()))
							.collect(Collectors.toList());
					if (mSearch != null && mSearch.size() > 0) {
						tipoRelacion = mSearch.get(0).getValor();
					}
					familiares.add(new DocumentoADNFamiliar(nombreFamiliar, tipoRelacion, f.getEdad().toString()));
				}
			}
			// #### FIN FAMILIARES
			LOGGER.info("IXDocReport Familiares Correcto!!!");
			// ### REFERIDOS
			List<DocumentoADNReferido> referidos = new ArrayList<DocumentoADNReferido>();
			Persona persona = personaRepository.findByTipoNumeroDocumento(
					aDNRegistroRequestDTO.getTitular().getTipoDocumento().toString(),
					aDNRegistroRequestDTO.getTitular().getNumeroDocumento());

			if (persona != null) {
				if (persona.getReferidos() != null && persona.getReferidos().size() > 0) {
					for (Referido r : persona.getReferidos()) {
						DocumentoADNReferido refe = new DocumentoADNReferido();
						refe.setNombres(r.getNombres());
						refe.setTelefono(r.getTelefono());
						referidos.add(refe);
					}
				}
			}
			LOGGER.info("IXDocReport Referidos Correcto!!!");
			// for(ADNRegistroReferidoRequestDTO r : aDNRegistroRequestDTO.getR)
			// ### FIN REFERIDOS
			// ### POLIZAS
			List<DocumentoADNPoliza> polizas = new ArrayList<DocumentoADNPoliza>();
			ObtenerPolizaRequest request = new ObtenerPolizaRequest();
			String tipoDoc = "";
			if (persona != null) {
				if (persona.getTipoDocumento().equals("1")) {
					tipoDoc = "01";
				} else if (persona.getTipoDocumento().equals("2")) {
					tipoDoc = "02";
				}
			}

			request.setNroDoc(persona.getNumeroDocumento());
			request.setTipoDoc(tipoDoc);

			// ObtenerPolizaResponse globalResponse =
			// globalRestClient.obtenerPolizas(request);
			List<Poliza> polizasList = polizaRepository.findByPoliza(tipoDoc, persona.getNumeroDocumento());
			System.out.println(gson.toJson(polizasList));
			/*
			 * LOGGER.info("gson.toJson(globalResponse)");
			 * LOGGER.info(gson.toJson(globalResponse)); for(PolizaResponse item :
			 * globalResponse.getPolizas()) { DocumentoADNPoliza poliza = new
			 * DocumentoADNPoliza(); poliza.setNumeroPoliza(item.getNumero_poliza());
			 * poliza.setFechaEmision(item.getFecha_emision());
			 * poliza.setNombreProducto(item.getNombreProducto());
			 * 
			 * ObtenerDetallePolizaRequest requestDetalle = new
			 * ObtenerDetallePolizaRequest();
			 * requestDetalle.setNumPoliza(item.getNumero_poliza()); PolizaDetalleResponse
			 * globalDetalleResponse =
			 * globalRestClient.obtenerDetallePoliza(requestDetalle);
			 * LOGGER.info("gson.toJson(globalDetalleResponse)");
			 * LOGGER.info(gson.toJson(globalDetalleResponse));
			 * poliza.setPagadoHasta(globalDetalleResponse.getFecha_pagado_hasta());
			 * poliza.setCoberturaVidaAdicional(globalDetalleResponse.
			 * getCobertura_vida_adicional()); poliza.setMoneda(item.getMoneda());
			 * poliza.setEstadoPoliza(item.getEstado());
			 * poliza.setMontoCoberturaPrincipal(item.getMonto_cobertura_principal());
			 * polizas.add(poliza); }
			 */
			LOGGER.info("==> polizas");
			LOGGER.info(gson.toJson(polizasList));
			if (polizasList != null) {
				polizasList.stream().forEach(i -> {

					DocumentoADNPoliza poliza = new DocumentoADNPoliza();

					poliza.setNumeroPoliza(i.getNumeroPoliza());
					poliza.setFechaEmision(i.getFechaEmision());
					poliza.setNombreProducto(i.getNombreProducto());

					poliza.setPagadoHasta(i.getPagadoHasta());
					poliza.setCoberturaVidaAdicional(i.getVidaAdicional());
					poliza.setMoneda(i.getMoneda());
					poliza.setEstadoPoliza(i.getEstadoPoliza());
					poliza.setMontoCoberturaPrincipal(i.getMontoCobertura());

					polizas.add(poliza);

				});
			}

			LOGGER.info("IXDocReport Polizas Correcto!!!");
			// ### FIN POLIZAS

			context.put("documento", documento);
			context.put("Familiares", familiares);
			context.put("Referidos", referidos);
			context.put("Polizas", polizas);

			// ############# FIN CONTEXT ##################
			LOGGER.info("FIN context");

			String formatoAdnFilePrivada = "";
			String formatoAdnFilePublica = "";
			// Crear Archivo Temporal -- Se puede Omitir
			// String nombreArchivoTemporal = "adn_completo_generado_temporal";
			String nombreArchivoTemporal = "adn_completo_generado_temporal";
			String nombreArchivoTemporal1 = "adn_completo_generado_temporal.pdf";
			// File fileTemportalPDFADN = File.createTempFile(nombreArchivoTemporal,
			// Constantes.ARCHIVO_EXT_PDF);
			File fileTemportalPDFADN = File.createTempFile(nombreArchivoTemporal, Constantes.ARCHIVO_EXT_PDF);

			String hora = "";
			if (hours < 10) {
				hora = "0" + hours;
			} else {
				hora = String.valueOf(hours);
			}
			String minuto = "";
			if (minutes < 10) {
				minuto = "0" + minutes;
			} else {
				minuto = String.valueOf(minutes);
			}
			String segundo = "";
			if (seconds < 10) {
				segundo = "0" + seconds;
			} else {
				segundo = String.valueOf(seconds);
			}

			formatoAdnFilePrivada = rutaPDFPrivada + "".concat(nombreArchivoTemporal).concat("_")
					.concat(DateUtil.dateToString(fecha, DateUtil.FORMATO_DIA_YYYYMMDD)).concat("_")
					.concat(hora + minuto + segundo + ".pdf");// .concat(".pdf");
			// File ffpep = this.obtenerPlantilla(Constantes.PLANTILLA_FORMATO_PEP);
			formatoAdnFilePublica = rutaPDFPublica + "".concat(nombreArchivoTemporal).concat("_")
					.concat(DateUtil.dateToString(fecha, DateUtil.FORMATO_DIA_YYYYMMDD)).concat("_")
					.concat(hora + minuto + segundo + ".pdf");// .concat(".pdf");//File ffpep =
																// this.obtenerPlantilla(Constantes.PLANTILLA_FORMATO_PEP);

			File adn = new File(formatoAdnFilePrivada);

			nombreArchivoTemporal = fileTemportalPDFADN.getName();

			LOGGER.info("nombreArchivoTemporal: " + nombreArchivoTemporal);
			// -- Generar archivo
			xdocReport.convert(context, Options.getTo(ConverterTypeTo.PDF),
					// new FileOutputStream(fileTemportalPDFADN)
					out);
			LOGGER.info("urlPublicoADN=>" + urlPublicoADN);
			File docOut = new File("docOut");
			FileUtils.writeByteArrayToFile(docOut, out.toByteArray());

			FileUtils.copyFile(docOut, adn);
			// String tipoDocumentoCRM =
			// this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_TIPO_DOCUMENTO,
			// persona.getTipoDocumento(), Constantes.MULTITABLA_COLUMNA_VALOR_CRM);
			// this.enviarPDFCRM(formatoAdnFilePrivada, tipoDocumentoCRM,
			// persona.getNumeroDocumento(), nombreArchivoTemporal1,
			// nombreArchivoTemporal1);

			// solo para local
//			rutaPDFPrivada="D:\\crm-pdf\\";

			// String urlPrivadaADN = rutaPDFPrivada + nombreArchivoTemporal;

//			File filePrivada = new File(urlPrivadaADN);

			// Files.copy(fileTemportalPDFADN.toPath(), (new File(urlPrivadaADN)).toPath(),
			// StandardCopyOption.REPLACE_EXISTING);

			// LOGGER.info("Copiado a pathFileNuevo=>" + urlPrivadaADN);
			// urlPublicoADN = rutaPDFPublica + nombreArchivoTemporal;
			// LOGGER.info("urlPublicoADN=>" + urlPublicoADN);

			urlADN[0] = formatoAdnFilePrivada;
			urlADN[1] = formatoAdnFilePublica;

			// -- Cerrar IO
			fileInputStream.close();
			bufferedInputStream.close();

		} catch (Exception e) {

			LOGGER.info("Error GenerarPDFADN =>" + e);

			this.enviarCorreoError(aDNRegistroRequestDTO, "SivTXException",
					"[AdnServiceImpl.GenerarPDFADN] Error al Generar PDF ADN",
					Utilitarios.obtenerMensaje(messageSource, new Object[] { Constantes.MENSAJE_ADMINISTRADOR },
							Constantes.MENSAJE_RESPUESTA_GENERAL_ERR0R));
			/*
			 * throw new SivTXException( Utilitarios.obtenerMensaje( messageSource, new
			 * Object[] {Constantes.MENSAJE_ADMINISTRADOR},
			 * Constantes.MENSAJE_RESPUESTA_GENERAL_ERR0R ), null );
			 */
		}

		return urlADN;
	}

	private void enviarPDFCRM(String urlPDFPublico, String tipoDocumentoCRM, String numeroDocumento,
			String nombreArchivoPDF, String descripcionArchivo, String tipoDocumentoInput) {
		try {
			if (!urlPDFPublico.equals(Strings.EMPTY)) {
				UploadArchivoRequest requestArchivoADN = new UploadArchivoRequest();
				requestArchivoADN.setTipoDocumento(tipoDocumentoCRM);
				requestArchivoADN.setNumeroDocumento(numeroDocumento);
				requestArchivoADN.setRutaArchivo(urlPDFPublico);
				requestArchivoADN.setNombreArchivo(nombreArchivoPDF);
				requestArchivoADN.setDescripcionArchivo(descripcionArchivo);
				requestArchivoADN.setTipoDocumentoAdn(tipoDocumentoInput);
				LOGGER.info("UploadArchivoRequest PDF ADN=>" + gson.toJson(requestArchivoADN));
				UploadArchivoResponse responseUpload = globalRestClient.uploadArchivo(requestArchivoADN);
				LOGGER.info("UploadArchivoResponse PDF ADN=>" + gson.toJson(responseUpload));
			}
		} catch (Exception e) {
			LOGGER.info("Error enviar PDF a CRM=>" + e);
			e.printStackTrace();
		}
	}

	private void enviarPDFCRM2(File fileTmp, String tipoDocumentoCRM, String numeroDocumento, String nombreArchivoPDF,
			String descripcionArchivo, String tipoDocumentoInput) {
		try {
			if (!fileTmp.equals(Strings.EMPTY)) {
				UploadArchivoRequest requestArchivoADN = new UploadArchivoRequest();
				requestArchivoADN.setTipoDocumento(tipoDocumentoCRM);
				requestArchivoADN.setNumeroDocumento(numeroDocumento);
				requestArchivoADN.setFile(fileTmp);
				requestArchivoADN.setNombreArchivo(nombreArchivoPDF);
				requestArchivoADN.setDescripcionArchivo(descripcionArchivo);
				requestArchivoADN.setTipoDocumentoAdn(tipoDocumentoInput);
				LOGGER.info("UploadArchivoRequest PDF ADN=>" + gson.toJson(requestArchivoADN));
				UploadArchivoResponse responseUpload = globalRestClient.uploadArchivo(requestArchivoADN);
				LOGGER.info("UploadArchivoResponse PDF ADN=>" + gson.toJson(responseUpload));
			}
		} catch (Exception e) {
			LOGGER.info("Error enviar PDF a CRM=>" + e);
			e.printStackTrace();
		}
	}

	private String formatoMiles(Float numero) {
		String formatoMiles = Strings.EMPTY;

		DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
		simbolo.setDecimalSeparator('.');
		simbolo.setGroupingSeparator(',');
		DecimalFormat formateador = new DecimalFormat("###,###.##", simbolo);

		formatoMiles = formateador.format(numero);

		// formatoMiles = String.format("%,.0f", numero);
		return formatoMiles;
	}

	@Override
	public ADNRegistroReferidoResponseDTO registrarReferidoADNS(
			ADNRegistroReferidoRequestDTO aDNRegistroReferidoRequestDTO) {
		Referido referido = null;
		ADNRegistroReferidoResponseDTO response = new ADNRegistroReferidoResponseDTO();
		try {
			if (aDNRegistroReferidoRequestDTO.getIdAdn() == null) {
				throw new SivTXException(Utilitarios.obtenerMensaje(messageSource,
						new Object[] { Constantes.MENSAJE_ADMINISTRADOR }, Constantes.MENSAJE_RESPUESTA_GENERAL_ERR0R),
						null);
			}
			Adn adn = adnRepository.findById(aDNRegistroReferidoRequestDTO.getIdAdn()).get();

			Persona persona = personaRepository.findById(adn.getPersona().getIdPersona()).get();
			LOGGER.info("PERSONA=" + persona.getNombres());

			// LIMPIAR REFERIDOS
			List<Referido> listaReferidosActual = referidoRepository.findByIdPersona(persona.getIdPersona());
			for (Referido referidoItem : listaReferidosActual) {
				referidoRepository.delete(referidoItem);
			}
			// FIN LIMPIAR REFERIDOS

			if (aDNRegistroReferidoRequestDTO.getReferidos() != null) {
				for (ADNReferidoRequestDTO aDNreferido : aDNRegistroReferidoRequestDTO.getReferidos()) {
					referido = new Referido();
					String nombreReferido = aDNreferido.getNombres() == null ? null
							: aDNreferido.getNombres().toUpperCase();
					String telefonoReferido = aDNreferido.getTelefono() == null ? null : aDNreferido.getTelefono();
					referido.setTelefono(telefonoReferido);
					referido.setNombres(nombreReferido);
					referido.setPersona(persona);
					referido.setFechaCrea(new Date());
					referido.setUsuarioCrea(aDNRegistroReferidoRequestDTO.getIdUsuario());
					referidoRepository.save(referido);
				}
				LOGGER.info("GRABO");
				response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
				response.setMensajeRespuesta(
						Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_REFERIDOS_EXITO));
			}

		} catch (Exception e) {
			LOGGER.error("ERROR registrarReferidoADNS=>" + e);
			e.printStackTrace();
			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			response.setMensajeRespuesta(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_ERR0R));
		}

		return response;
	}

	/*
	 * private GetIdOportunidadResponse obtenerOportunidadCRM(String tipoDocumento,
	 * String numeroDocumento, String usuario) { String traza =
	 * Utilitarios.trazaLog(); traza = traza + "-" + numeroDocumento; LOGGER.info(
	 * "[" + traza +
	 * "] Entro AdnServiceImpl#obtenerOportunidadCRM(tipoDocumento, numeroDocumento, usuario)"
	 * ); GetIdOportunidadResponse responseOportunidad; // Equivalencia Tipo Doc CRM
	 * List<Multitabla> m1 =
	 * multitablaRepository.findByCodigoTabla(TablaEnum.TABLA_TIPO_DOCUMENTO.
	 * getCodigoTabla()); Multitabla tiposDocumentoCRM = null; if (!m1.isEmpty()) {
	 * tiposDocumentoCRM = m1.stream().filter(t ->
	 * t.getCodigo().equals(tipoDocumento)) .collect(Collectors.toList()).get(0); }
	 * // Fin Equivalencia Tipo Doc CRM
	 *
	 * GetIdOportunidadRequest requestOportunidad = new GetIdOportunidadRequest();
	 * requestOportunidad.setTipoDocumento(tiposDocumentoCRM.getValorCrm());
	 * requestOportunidad.setNumeroDocumento(numeroDocumento);
	 * requestOportunidad.setDomainName("DINTERSEGURO\\\\" + usuario);
	 *
	 * LOGGER.info("[" + traza + "] Oporunidad.Request ==> " + requestOportunidad);
	 * responseOportunidad = crmRestClient.getIdOportunidad(requestOportunidad);
	 * LOGGER.info("[" + traza + "] Oporunidad.Response ==> " +
	 * responseOportunidad);
	 *
	 * LOGGER.
	 * info("Salio AdnServiceImpl#obtenerOportunidadCRM(tipoDocumento, numeroDocumento, usuario)"
	 * ); return responseOportunidad; }
	 */

	private void eliminarFilePrivado(String urlPDFFirmaPrivada) {
		try {
			File file = new File(urlPDFFirmaPrivada);
			Files.deleteIfExists(file.toPath());

		} catch (Exception ex) {
			LOGGER.info("ERROR eliminarFilePrivado=>" + ex);
		}
	}

	/* Reinigeniería ADN / SIV */
	@Override
	public ADNConsultaPolizaBUCResponseDTO obtenerPolizasCliente(String tipoDocCliente, String numDocCliente,
			String idUsuario, String device, String os) {
		String traza = Utilitarios.trazaLog();
		traza = traza + "-" + tipoDocCliente + "-" + numDocCliente;
		LOGGER.info(
				"[" + traza + "] Entro AdnServiceImpl#obtenerPolizasCliente(tipoDocCliente, numDocCliente, idUsuario)");

		ADNConsultaPolizaBUCResponseDTO response = new ADNConsultaPolizaBUCResponseDTO();
		List<ADNPolizaBUCResponseDTO> listaPolizasDTO = new ArrayList<ADNPolizaBUCResponseDTO>();

		ObtenerPolizaRequest request = new ObtenerPolizaRequest();
		request.setNroDoc(numDocCliente);
		request.setTipoDoc(tipoDocCliente);
		System.out.println("************");

		ObtenerPolizaResponse globalResponse = globalRestClient.obtenerPolizas(request);
		LOGGER.info("globalResponse => " + gson.toJson(globalResponse));
		if (globalResponse != null) {
			for (PolizaResponse item : globalResponse.getPolizas()) {

				ADNPolizaBUCResponseDTO poliza = new ADNPolizaBUCResponseDTO();
				Poliza savePoliza = new Poliza();

				// listar poliza
				poliza.setNumeroPoliza(item.getNumero_poliza());
				poliza.setFechaEmision(item.getFecha_emision());
				poliza.setNombreProducto(item.getNombreProducto());

				ObtenerDetallePolizaRequest requestDetalle = new ObtenerDetallePolizaRequest();
				requestDetalle.setNumPoliza(item.getNumero_poliza());
				PolizaDetalleResponse globalDetalleResponse = globalRestClient.obtenerDetallePoliza(requestDetalle);
				poliza.setPagadoHasta(globalDetalleResponse.getFecha_pagado_hasta());
				poliza.setCoberturaVidaAdicional(globalDetalleResponse.getCobertura_vida_adicional());

				poliza.setMoneda(item.getMoneda());
				poliza.setEstadoPoliza(item.getEstado());
				poliza.setMontoCoberturaPrincipal(item.getMonto_cobertura_principal());

				listaPolizasDTO.add(poliza);

				// registrar poliza

				savePoliza = polizaRepository.findAll().stream()
						.filter(u -> u.getNumeroPoliza().equals(item.getNumero_poliza())).findFirst().orElse(null);

				if (savePoliza != null) {
					LOGGER.info("numero Poliza existe ==> " + item.getNumero_poliza());
					String param = item.getNumero_poliza();
					savePoliza = polizaRepository.findByNroPoliza(param);
					savePoliza.setFechaEmision(item.getFecha_emision());
					savePoliza.setNombreProducto(item.getNombreProducto());

					savePoliza.setPagadoHasta(globalDetalleResponse.getFecha_pagado_hasta());
					savePoliza.setVidaAdicional(globalDetalleResponse.getCobertura_vida_adicional());

					savePoliza.setMoneda(item.getMoneda());
					savePoliza.setEstadoPoliza(item.getEstado());
					savePoliza.setMontoCobertura(item.getMonto_cobertura_principal());

				} else {

					savePoliza = new Poliza();

					savePoliza.setTipoDocumento(tipoDocCliente);
					savePoliza.setNumeroDocumento(numDocCliente);

					savePoliza.setNumeroPoliza(item.getNumero_poliza());
					savePoliza.setFechaEmision(item.getFecha_emision());
					savePoliza.setNombreProducto(item.getNombreProducto());

					savePoliza.setPagadoHasta(globalDetalleResponse.getFecha_pagado_hasta());
					savePoliza.setVidaAdicional(globalDetalleResponse.getCobertura_vida_adicional());

					savePoliza.setMoneda(item.getMoneda());
					savePoliza.setEstadoPoliza(item.getEstado());
					savePoliza.setMontoCobertura(item.getMonto_cobertura_principal());

				}

				savePoliza = polizaRepository.save(savePoliza);
			}
		}

		response.setPolizas(listaPolizasDTO);
		response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
		response.setMensajeRespuesta("OK");

		return response;
	}

	// OLD
	@Override
	public ADNConsultaPolizaResponseDTO busquedaPoliza(ADNPolizaRequestDTO aDNPolizaRequestDTO) {
		String traza = Utilitarios.trazaLog();
		traza = traza + "-" + aDNPolizaRequestDTO.getNumeroDocumento();
		LOGGER.info("[" + traza + "] Entro AdnServiceImpl#busquedaPoliza(aDNPolizaRequestDTO)");
		ADNConsultaPolizaResponseDTO response = new ADNConsultaPolizaResponseDTO();
		Gson gson = new Gson();
		response.setPolizas(new ArrayList<>());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("CURSOR_POLIZAS", null);
		map.put("P_ERR_MENSAJE", null);
		map.put("P_ERR_CODE", null);
		map.put("P_NUMERO_DOCUMENTO", aDNPolizaRequestDTO.getNumeroDocumento());
		LOGGER.info("[" + traza + "] obtenerPolizas.Request ==> " + gson.toJson(map));
		try {
			System.out.println("*************************************");
			System.out.println(map);
			acseleRepository.obtenerPolizas(map);

			LOGGER.info("[" + traza + "] obtenerPolizas.Response ==> " + gson.toJson(map));
			List<PolizaAcsele> listaPolizas = (List<PolizaAcsele>) map.get("CURSOR_POLIZAS");
			for (PolizaAcsele poliza : listaPolizas) {
				response.getPolizas()
						.add(new ADNPolizaResponseDTO(poliza.getNumeroPoliza(), poliza.getEstadoPoliza(),
								poliza.getNombreProducto(), poliza.getFrecuenciaPago(), poliza.getPrimaTotal(),
								poliza.getInicioVigencia(), poliza.getFinVigencia(), poliza.getCobertura(),
								poliza.getCapital(), poliza.getPrimaNeta(), ""));
			}
			response.setCodigoRespuesta("01");
			response.setMensajeRespuesta("OK");
		} catch (Exception e) {
			LOGGER.error("[" + traza + "] obtenerPolizas.Error ==> " + e.getMessage());
			LOGGER.info("ERROR busquedaPoliza=>" + e);
			response.setCodigoRespuesta("99");
			response.setMensajeRespuesta(e.getMessage());
		}
		LOGGER.info("[" + traza + "] Salio AdnServiceImpl#busquedaPoliza(aDNPolizaRequestDTO)");
		return response;
	}

	@Override
	public ADNConsultaClienteDTResponseDTO clienteDT(ADNClienteRequestDTO aDNClienteRequestDTO) {
		// TODO Auto-generated method stub
		ADNConsultaClienteDTResponseDTO response = new ADNConsultaClienteDTResponseDTO();
		response.setResult(new ArrayList<>());
		try {
			ConsultaClienteRequest request = new ConsultaClienteRequest();
			request.setDocumentoIdentidad(aDNClienteRequestDTO.getNumeroDocumento());
			ConsultaClienteResponse listaClientes = vtigerRestClient.buscarCliente(request);

			for (ConsultaClienteResultResponse cliente : listaClientes.getResult()) {
				response.getResult().add(new ADNClienteDTResponseDTO(cliente.getSalutationtype(),
						cliente.getFirstname(), cliente.getContact_no(), cliente.getPhone(), cliente.getLastname(),
						cliente.getMobile(), cliente.getAccount_id(), cliente.getHomephone(), cliente.getLeadsource(),
						cliente.getOtherphone(), cliente.getTitle(), cliente.getFax(), cliente.getDepartment(),
						cliente.getBirthday(), cliente.getEmail(), cliente.getContact_id(), cliente.getAssistant(),
						cliente.getSecondaryemail(), cliente.getAssistantphone(), cliente.getDonotcall(),
						cliente.getEmailoptout(), cliente.getAssigned_user_id(), cliente.getReference(),
						cliente.getNotify_owner(), cliente.getCreatedtime(), cliente.getModifiedtime(),
						cliente.getModifiedby(), cliente.getOtherstreet(), cliente.getOthercity(),
						cliente.getOtherstate(), cliente.getOtherpobox(), cliente.getIsconvertedfromlead(),
						cliente.getSource(), cliente.getStarred(), cliente.getTags(), cliente.getCf_852(),
						cliente.getCf_854(), cliente.getCf_858(), cliente.getCf_860(), cliente.getCf_886(),
						cliente.getCf_902(), cliente.getCf_905(), cliente.getCf_907(), cliente.getCf_902(),
						cliente.getCf_928(), cliente.getCf_930(), cliente.getId()));
			}
			response.setCodigoRespuesta("01");
			response.setMensajeRespuesta("OK");
		} catch (Exception e) {
			response.setCodigoRespuesta("99");
			response.setMensajeRespuesta(e.getMessage());
		}

		return response;
	}

	@Override
	public ADNConsultaUsuarioDTResponseDTO usuarioDT(ADNUsuarioRequestDTO aDNUsuarioRequestDTO) {
		ADNConsultaUsuarioDTResponseDTO response = new ADNConsultaUsuarioDTResponseDTO();
		response.setResult(new ArrayList<>());
		try {
			ConsultaUsuarioRequest request = new ConsultaUsuarioRequest();
			request.setId(aDNUsuarioRequestDTO.getIdUsuario());

			if (aDNUsuarioRequestDTO.getIdUsuario() == Strings.EMPTY) {
				throw new SivSOAException("Usuario vacio", null);
			}
			ConsultaUsuarioResponse listaUsuarios = vtigerRestClient.buscarUsuario(request);

			for (ConsultaUsuarioResultResponse usuario : listaUsuarios.getResult()) {
				response.getResult().add(new ADNUsuarioDTResponseDTO(usuario.getId(), usuario.getUser_name(),
						usuario.getFirst_name(), usuario.getLast_name(), usuario.getRoleid(), usuario.getRolename()));
			}
			response.setCodigoRespuesta("01");
			response.setMensajeRespuesta("OK");
		} catch (Exception e) {
			LOGGER.info("Error usuarioDT:" + e.getMessage());
			response.setCodigoRespuesta("99");
			response.setMensajeRespuesta(e.getMessage());
		}

		return response;
	}

	@Override
	public ADNConsultaPotencialDTResponseDTO clientePotencialDT(ADNPotencialRequestDTO aDNPotencialRequestDTO) {
		ADNConsultaPotencialDTResponseDTO response = new ADNConsultaPotencialDTResponseDTO();
		response.setResult(new ArrayList<>());
		try {
			ConsultaPotencialRequest request = new ConsultaPotencialRequest();
			request.setContactID(aDNPotencialRequestDTO.getIdCliente());
			ConsultaPotencialResponse listaClientes = vtigerRestClient.buscarPotencial(request);

			for (ConsultaPotencialResultResponse cliente : listaClientes.getResult()) {
				response.getResult().add(new ADNPotencialDTResponseDTO(cliente.getId(), cliente.getContact_id(),
						cliente.getLeadsource(), cliente.getSales_stage(), cliente.getCf_924()));
			}
			response.setCodigoRespuesta("01");
			response.setMensajeRespuesta("OK");
		} catch (Exception e) {
			response.setCodigoRespuesta("99");
			response.setMensajeRespuesta(e.getMessage());
		}

		return response;
	}

	@Override
	public List<GenericoComboBean> codigoTabla(String codigo) {
		return getDatosTabla(multitablaRepository.findByCodigoTabla(codigo));
	}

	@Override
	public ADNFirmaResponseDTO registrarLDPDP(ADNFirmaRequestDTO aDNFirmaRequestDTO) {
		LOGGER.info("Entro AdnServiceImpl#registrarLDPDP(aDNFirmaRequestDTO)");

		String mensajeError = Utilitarios.obtenerMensaje(messageSource,
				new Object[] { Constantes.MENSAJE_RESPUESTA_GENERAL_ERR0R },
				Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_ADN_FIRMA_TX_GENERAR_CIRCUITO));

		ADNFirmaResponseDTO response = new ADNFirmaResponseDTO();
		File fileTemportalPDFADN = null;

		try {

			// -- Obtener Plantilla
			File plantilla = null;
			String rutaPlantilla = "";
			try {

				rutaPlantilla = Constantes.RUTA_PLANTILLA + "/" + Constantes.LDPDP;

				plantilla = new File(getClass().getClassLoader().getResource(rutaPlantilla).getFile());

				String systemOp = System.getProperty("os.name");

				LOGGER.info("os.name->" + systemOp);
				LOGGER.info("systemOp.contains->" + systemOp.contains("Windows"));

				if (!systemOp.contains("Windows")) {
					plantilla = new File(rutaPlantillaDocLinux + "//" + Constantes.LDPDP);
				}

				LOGGER.info("plantilla=>" + plantilla);

			} catch (Exception e) {

				this.enviarCorreoError(aDNFirmaRequestDTO, "SivTXException",
						"[AdnServiceImpl.registrarLDPDP] Error al Generar documento en obtener Plantilla",
						Utilitarios.obtenerMensaje(messageSource, new Object[] { rutaPlantilla },
								Constantes.MENSAJE_ADN_FIRMA_TX_PLANTILLA));

				throw new SivTXException(mensajeError, null);
			}

			// -- Procesamiento del documento
			String nombreCliente = aDNFirmaRequestDTO.getNombres().concat(" " + aDNFirmaRequestDTO.getApellidoPaterno())
					.concat(" " + aDNFirmaRequestDTO.getApellidoMaterno()).toUpperCase();
			String documentoIdentidad = aDNFirmaRequestDTO.getNumeroDocumento();
			String correoCliente = aDNFirmaRequestDTO.getCorreo();
			String nombreArchivo = "adn_ldpdp_generado";
			Date hoy = new Date();

			LOGGER.info("nombreCliente=>" + nombreCliente);
			LOGGER.info("plantilla=>" + plantilla);

			try {

				String tipoDocumento = Strings.EMPTY;
				if (documentoIdentidad != null && !documentoIdentidad.equals(Strings.EMPTY)
						&& aDNFirmaRequestDTO.getTipoDocumento() != null) {

					List<Multitabla> m1 = multitablaRepository
							.findByCodigoTabla(TablaEnum.TABLA_TIPO_DOCUMENTO.getCodigoTabla());
					Multitabla tiposDocumentoCRM = null;
					if (!m1.isEmpty()) {
						tiposDocumentoCRM = m1.stream()
								.filter(t -> t.getCodigo().equals(aDNFirmaRequestDTO.getTipoDocumento().toString()))
								.collect(Collectors.toList()).get(0);

						tipoDocumento = tiposDocumentoCRM.getValor() + " ";
					}

				}

				StringBuilder sbDocumento = new StringBuilder();
				sbDocumento.append(tipoDocumento);
				sbDocumento.append(documentoIdentidad);

				// -- Parametros
				Map<String, Object> parametros = new HashMap<String, Object>();
				parametros.put("nombres", nombreCliente);
				parametros.put("documento", sbDocumento.toString());
				parametros.put("fecha", DateUtil.dateToString(hoy, DateUtil.FORMATO_DIA_DDMMYYYY));
				parametros.put("hora", LocalTime.now().toString().substring(0, 8));

				// -- Generar
				nombreArchivo = IXDocReportUtil.generarDocumento(plantilla, nombreArchivo, parametros, true);

			} catch (Exception e) {
				LOGGER.error("Error Generar documento => " + e.getMessage());

				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));

				this.enviarCorreoError(aDNFirmaRequestDTO, "SivTXException",
						"[AdnServiceImpl.registrarFirma] Error al Generar documento en el procesaiento del documento",
						Utilitarios.obtenerMensaje(messageSource, new Object[] { sw.toString() },
								Constantes.MENSAJE_ADN_FIRMA_TX_GENERAR_DOCUMENTO));
				throw new SivTXException(mensajeError, null);
			}

			// -- Recuperar documento generado
			File archivoGenerado = new File(
					System.getProperty("java.io.tmpdir").concat(File.separator).concat(nombreArchivo));
			fileTemportalPDFADN = archivoGenerado;

			if (!archivoGenerado.exists()) {
				this.enviarCorreoError(aDNFirmaRequestDTO, "SivTXException",
						"[AdnServiceImpl.registrarFirma] Error al Enviar Indenova en recuperar documento generado",
						Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_ADN_FIRMA_TX_DOCUMENTO_GENERADO));
				throw new SivTXException(Utilitarios.obtenerMensaje(messageSource,
						new Object[] { Constantes.MENSAJE_ADMINISTRADOR }, Constantes.MENSAJE_RESPUESTA_GENERAL_ERR0R),
						null);
			}

		} catch (SivTXException e) {
			// LOGGER.error("Error SivTXException ADNFirmaResponseDTO=> " +
			// e.getMsjError());
			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			response.setMensajeRespuesta(e.getMsjError());
		} catch (Exception e) {
			// LOGGER.error("Error Exception ADNFirmaResponseDTO=> " + e);
			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			response.setMensajeRespuesta(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_ERR0R));
		}
		LOGGER.info(System.getProperty("java.io.tmpdir").concat(File.separator));

		/**** Envio de PDF a CRM ****/
		String urlPDFADNPublico = rutaPDFPublica;
		try {
			if (!urlPDFADNPublico.equals(Strings.EMPTY)) {
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
				String sufixFecha = format.format(new Date());
				String numeroDocumento = aDNFirmaRequestDTO.getNumeroDocumento();
				String tipoDocumentoInput = aDNFirmaRequestDTO.getTipoDocumento().toString();

				// String tipoDocumento = Strings.EMPTY;
				Multitabla tiposDocumentoCRM = null;
				if (numeroDocumento != null && !numeroDocumento.equals(Strings.EMPTY)
						&& aDNFirmaRequestDTO.getTipoDocumento() != null) {

					List<Multitabla> m1 = multitablaRepository
							.findByCodigoTabla(TablaEnum.TABLA_TIPO_DOCUMENTO.getCodigoTabla());
					if (!m1.isEmpty()) {
						System.out.println("============M1=========" + tipoDocumentoInput);
						tiposDocumentoCRM = m1.stream().filter(t -> t.getCodigo().equals(tipoDocumentoInput))
								.collect(Collectors.toList()).get(0);
					}

				}

				String tipoDocumentoCRM = tiposDocumentoCRM.getValorCrm();
				String nombreArchivoPDF = "Ficha_LDPDP_" + numeroDocumento + "_" + sufixFecha + ".pdf";
				String descripcionArchivo = "";
				urlPDFADNPublico = urlPDFADNPublico + nombreArchivoPDF;

				LOGGER.info("NUMERO DOCUMENTO: " + numeroDocumento);
				LOGGER.info("TIPO DOCUMENTO: " + tipoDocumentoCRM);
				LOGGER.info("NOMBRE ARCHIVO PDF: " + nombreArchivoPDF);
				LOGGER.info("URL PUBLICA: " + urlPDFADNPublico);
				// String urlPrivadaADN = rutaPDFPrivada + nombreArchivoPDF;

				File filePrivada = new File(urlPDFADNPublico);

				Files.copy(fileTemportalPDFADN.toPath(), (new File(urlPDFADNPublico)).toPath(),
						StandardCopyOption.REPLACE_EXISTING);

				LOGGER.info("Copiado a pathFileNuevo=>" + urlPDFADNPublico);
				LOGGER.info("urlPublicoADN=>" + urlPDFADNPublico);

				//

				enviarPDFCRM(rutaPDFPrivada + filePrivada, tipoDocumentoCRM, numeroDocumento, nombreArchivoPDF,
						descripcionArchivo, aDNFirmaRequestDTO.getTipoDocumento().toString());
				response.setRutaArchivogenerado(urlPDFADNPublico);
			}
		} catch (Exception e) {
			LOGGER.info("Error enviar PDF ADN a cloudStorage");
			e.printStackTrace();
		}

		/*** Funcionalidad de correo debe ser enviada en registrarADNS ***/
		/*
		 * String nombreCliente = aDNFirmaRequestDTO.getNombres() .concat(" ")
		 * .concat(aDNFirmaRequestDTO.getApellidoPaterno() .concat(" ")
		 * .concat(aDNFirmaRequestDTO.getApellidoMaterno())); String nombreAgente =
		 * aDNFirmaRequestDTO.getAgente(); String asunto = "Datos de ADN de " +
		 * nombreCliente; this.enviarCorreoDatos(asunto, urlPDFADNPublico,
		 * nombreCliente, nombreAgente);
		 */

		return response;
	}

	@Override
	public ADNReglamentoResponseDTO obtenerInformacionClienteVarios(String tipoDocCliente, String numDocCliente,
			String idUsuario, String device, String os) {
		LOGGER.info("Entro AdnServiceImpl#obtenerInformacionCliente(tipoDocCliente, numDocCliente)");

		ADNReglamentoResponseDTO response = new ADNReglamentoResponseDTO();
		ADNReglamentoTitularResponseDTO titular = new ADNReglamentoTitularResponseDTO();
		ADNReglamentoPlanFuturoResponsetDTO planFuturo = new ADNReglamentoPlanFuturoResponsetDTO();
		List<String> observaciones = new ArrayList<>();
		StringBuilder usuarioCRM = new StringBuilder();
		usuarioCRM.append("DINTERSEGURO\\");

		try {
			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
			response.setMensajeRespuesta(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_EXITO));

			List<Multitabla> m1 = multitablaRepository
					.findAllByCodigoTabla(TablaEnum.TABLA_TIPO_DOCUMENTO.getCodigoTabla());
			LOGGER.info("Registros Tipos de documento: " + m1.size());
			LOGGER.info("Tipo documento: " + tipoDocCliente);
			Multitabla tiposDocumentoCRM = null;
			if (!m1.isEmpty()) {
				tiposDocumentoCRM = m1.stream().filter(t -> t.getCodigo().equals(tipoDocCliente)).findFirst().get();// .collect(Collectors.toList()).get(0);
			}
			LOGGER.info("Tipo documento : " + tiposDocumentoCRM);
			String tipoDocumentoCRM = tiposDocumentoCRM.getValorCrm();

			LOGGER.info("Ingreso Validaciones CRM...");
			boolean buscarDigital = true;

			/*
			 * DatoContactoRequest requestContacto = new DatoContactoRequest();
			 * requestContacto.setDomainName(usuarioCRM.toString());
			 * requestContacto.setTipoDocumento(tipoDocumentoCRM);
			 * requestContacto.setNumeroDocumento(numDocCliente);
			 *
			 * DatoContactoResponse responseContactoCRM =
			 * crmRestClient.getDatosContacto(requestContacto);
			 *
			 * if (responseContactoCRM.getNumeroDocumento() != null) {
			 *
			 * String tipoDocumentoInput = responseContactoCRM.getCodigoTipoDocumento();
			 * String generoInput = responseContactoCRM.getCodigoSexo();
			 * String profesionInput = responseContactoCRM.getCodigoProfesion();
			 * String actividadEconomicaInput = responseContactoCRM.getCodigoActividadEconomica();
			 * String fumadorInput = responseContactoCRM.getCodigoFumador();
			 * System.out.println(
			 * "=======================================================================================0"
			 * ); List<Multitabla> m0 = multitablaRepository
			 * .findByCodigoTabla(TablaEnum.TABLA_TIPO_DOCUMENTO.getCodigoTabla());
			 * Multitabla tiposDocumentoCRM0 = null; if (!m0.isEmpty()) {
			 * System.out.println("============M1=========" + tipoDocumentoInput); if
			 * (tipoDocumentoInput != null) { List<Multitabla> listItem =
			 * m0.stream().filter(t -> t.getValorCrm().equals(tipoDocumentoInput))
			 * .collect(Collectors.toList()); if (listItem.size() > 0) { tiposDocumentoCRM0
			 * = listItem.get(0); } } }
			 *
			 * System.out.println(
			 * "=======================================================================================0"
			 * ); List<Multitabla> m2 =
			 * multitablaRepository.findByCodigoTabla(TablaEnum.TABLA_GENERO.getCodigoTabla(
			 * )); Multitabla generoCRM = null; if (!m2.isEmpty()) {
			 * System.out.println("============M2=========" + generoInput); if (generoInput
			 * != null) { List<Multitabla> listItem = m2.stream().filter(t ->
			 * t.getValorCrm().equals(generoInput)) .collect(Collectors.toList()); if
			 * (listItem.size() > 0) { generoCRM = listItem.get(0); } } }
			 *
			 * System.out.println(
			 * "=======================================================================================0"
			 * ); List<Multitabla> m3 = multitablaRepository
			 * .findByCodigoTabla(TablaEnum.TABLA_PROFESION.getCodigoTabla()); Multitabla
			 * profesionCRM = null; if (!m3.isEmpty()) {
			 * System.out.println("============M3=========" + profesionInput); if
			 * (profesionInput != null) { List<Multitabla> listItem = m3.stream().filter(t
			 * -> t.getValorCrm().equals(profesionInput)) .collect(Collectors.toList()); if
			 * (listItem.size() > 0) { profesionCRM = listItem.get(0); } } }
			 *
			 * System.out.println(
			 * "=======================================================================================0"
			 * ); List<Multitabla> m4 = multitablaRepository
			 * .findByCodigoTabla(TablaEnum.TABLA_ACTIVIDAD_ECONOMICA.getCodigoTabla());
			 * Multitabla actividadEconomicaCRM = null; if (!m4.isEmpty()) {
			 * System.out.println("============M4=========" + actividadEconomicaInput); if
			 * (actividadEconomicaInput != null) { List<Multitabla> listItem = m4.stream()
			 * .filter(t -> t.getValorCrm().equals(actividadEconomicaInput))
			 * .collect(Collectors.toList()); if (listItem.size() > 0) {
			 * actividadEconomicaCRM = listItem.get(0); } } }
			 *
			 * System.out.println(
			 * "=======================================================================================0"
			 * ); List<Multitabla> m5 =
			 * multitablaRepository.findByCodigoTabla(TablaEnum.TABLA_FUMADOR.getCodigoTabla
			 * ()); Multitabla fumadorCRM = null; if (!m5.isEmpty()) {
			 * System.out.println("============M5=========" + fumadorInput); if
			 * (fumadorInput != null) { List<Multitabla> listItem = m5.stream().filter(t ->
			 * t.getValorCrm().equals(fumadorInput)) .collect(Collectors.toList()); if
			 * (listItem.size() > 0) { fumadorCRM = listItem.get(0); } } }
			 * System.out.println(
			 * "=======================================================================================0"
			 * );
			 *
			 * if (tiposDocumentoCRM0 != null)
			 * titular.setTipoDocumento(tiposDocumentoCRM0.getCodigo());
			 * titular.setNumeroDocumento(responseContactoCRM.getNumeroDocumento());
			 * titular.setNombres(responseContactoCRM.getNombres());
			 * titular.setApellidoPaterno(responseContactoCRM.getApellidoPaterno());
			 * titular.setApellidoMaterno(responseContactoCRM.getApellidoMaterno()); if
			 * (generoCRM != null) titular.setGenero(generoCRM.getCodigo());
			 * titular.setCelular(responseContactoCRM.getTelefonoCelular()); if
			 * (actividadEconomicaCRM != null)
			 * titular.setActividadEconomica(actividadEconomicaCRM.getCodigo()); if
			 * (profesionCRM != null) titular.setProfesion(profesionCRM.getCodigo()); if
			 * (fumadorCRM != null) titular.setFumador(fumadorCRM.getCodigo());
			 * titular.setCorreo(responseContactoCRM.getEmail());
			 * titular.setFechaNacimiento(responseContactoCRM.getFechaNacimiento()); Persona
			 * persona = personaRepository.findByTipoNumeroDocumento(tipoDocCliente,
			 * numDocCliente); if (persona != null) { if (persona.getTipoCasa() != null)
			 * titular.setTipoCasa(persona.getTipoCasa()); if (persona.getTieneVehiculo() !=
			 * null) titular.setTieneVehiculo(persona.getTieneVehiculo()); if
			 * (persona.getTieneAfp() != null) titular.setTieneAfp(persona.getTieneAfp());
			 * if (persona.getTieneSeguroVida() != null)
			 * titular.setTieneSeguro(persona.getTieneSeguroVida()); } } // else {//Fin
			 */
			Persona persona = personaRepository.findByTipoNumeroDocumento(tipoDocCliente, numDocCliente);
			if (persona != null) {

				titular.setNumeroDocumento(persona.getNumeroDocumento());
				if (persona.getNombres() != null)
					titular.setNombres(persona.getNombres());
				if (persona.getApellidoPaterno() != null)
					titular.setApellidoPaterno(persona.getApellidoPaterno());
				if (persona.getApellidoMaterno() != null)
					titular.setApellidoMaterno(persona.getApellidoMaterno());
				if (persona.getRazonSocial() != null)
					titular.setRazonSocial(persona.getRazonSocial());
				if (persona.getGenero() != null)
					titular.setGenero(persona.getGenero());
				if (persona.getCelular() != null)
					titular.setCelular(persona.getCelular().toString());
				if (persona.getActividadEconomica() != null)
					titular.setActividadEconomica(persona.getActividadEconomica());
				if (persona.getProfesion() != null)
					titular.setProfesion(persona.getProfesion());
				if (persona.getFumador() != null)
					titular.setFumador(persona.getFumador());
				if (persona.getCorreo() != null)
					titular.setCorreo(persona.getCorreo());
				if (persona.getFechaNacimiento() != null) {
					SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
					String fechaNacimiento = format.format(persona.getFechaNacimiento());
					titular.setFechaNacimiento(fechaNacimiento);
				}
				if (persona.getNacionalidad() != null)
					titular.setNacionalidad(persona.getNacionalidad());
				if (persona.getCentroTrabajo() != null)
					titular.setCentroTrabajo(persona.getCentroTrabajo());
				if (persona.getTipoCasa() != null)
					titular.setTipoCasa(persona.getTipoCasa());
				if (persona.getTieneVehiculo() != null)
					titular.setTieneVehiculo(persona.getTieneVehiculo());
				if (persona.getTieneAfp() != null)
					titular.setTieneAfp(persona.getTieneAfp());
				if (persona.getTieneSeguroVida() != null)
					titular.setTieneSeguro(persona.getTieneSeguroVida());
				if (persona.getEstadoCivil() != null)
					titular.setEstadoCivil(persona.getEstadoCivil());
				if (persona.getProfesionDetalle() != null)
					titular.setProfesionDetalle(persona.getProfesionDetalle());
				if (persona.getIngresoMoneda() != null)
					titular.setIngresoMoneda(persona.getIngresoMoneda());
				if (persona.getIngresoValor() != null)
					titular.setIngresoMonto(String.valueOf(persona.getIngresoValor()));
				ADNReglamentoTitularDireccionResponseDTO direccion = new ADNReglamentoTitularDireccionResponseDTO();
				if (persona.getDireccionUrbanizacion() != null)
					direccion.setUrbanizacion(persona.getDireccionUrbanizacion());
				if (persona.getDireccionNroMz() != null)
					direccion.setNumeroVia(persona.getDireccionNroMz());
				if (persona.getDireccionTipoDes() != null)
					direccion.setNombreVia(persona.getDireccionTipoDes());
				if (persona.getDireccionTipo() != null)
					direccion.setTipoVia(persona.getDireccionTipo());
				if (persona.getDireccionInterior() != null)
					direccion.setDptoNro(persona.getDireccionInterior());
				if (persona.getDepartamento() != null)
					direccion.setDepartamento(persona.getDepartamento());
				if (persona.getProvincia() != null)
					direccion.setProvincia(persona.getProvincia());
				if (persona.getDistrito() != null)
					direccion.setDistrito(persona.getDistrito());
				titular.setDireccion(direccion);
			}
			// }

			if (titular.getApellidoPaterno() != null && titular.getApellidoPaterno().trim() != Strings.EMPTY
					&& titular.getApellidoMaterno() != null && titular.getApellidoMaterno().trim() != Strings.EMPTY
					&& titular.getCelular() != null && titular.getCelular().trim() != Strings.EMPTY
					&& titular.getCorreo() != null && titular.getCorreo().trim() != Strings.EMPTY
					&& titular.getFechaNacimiento() != null && titular.getFechaNacimiento().trim() != Strings.EMPTY) {
				buscarDigital = false;
			}

			if (buscarDigital) {
				ObtenerDatosClienteResponse datosCliente = null;
				try {
					datosCliente = interseguroRestClient.obtenerDatosCliente(numDocCliente);
				} catch (Exception ex) {
					LOGGER.info("Error.ObtenerDatosClienteResponse.Digital=>" + ex);
				}

				if (datosCliente != null && datosCliente.getData() != null) {
					if (titular.getNombres() == null || titular.getNombres() == "") {
						titular.setNombres(datosCliente.getData().getNombres());
					}
					if (titular.getApellidoPaterno() == null || titular.getApellidoPaterno() == "") {
						titular.setApellidoPaterno(datosCliente.getData().getApellidoPaterno());
					}
					if (titular.getApellidoMaterno() == null || titular.getApellidoMaterno() == "") {
						titular.setApellidoMaterno(datosCliente.getData().getApellidoMaterno());
					}
					if (titular.getCelular() == null || titular.getCelular() == "") {
						titular.setCelular(datosCliente.getData().getCelular());
					}
					if (titular.getCorreo() == null || titular.getCorreo() == "") {
						titular.setCorreo(datosCliente.getData().getEmail());
					}
					if (titular.getFechaNacimiento() == null || titular.getFechaNacimiento() == "") {
						titular.setFechaNacimiento(datosCliente.getData().getFechaNacimiento());
					}
				}
			}

			response.setTitular(titular);
			response.setPlanFuturo(planFuturo);

			// existeplaft

			// Si DNI esta libre despues de aplicar las reglas del CRM
			if ((titular.getTipoDocumento() == null && titular.getNumeroDocumento() == null)
					|| (response.getExisteAdn() != null && response.getExisteAdn() != 1)) {

				LOGGER.info("Entro a validaciones VTIGER");
				ConsultaClienteRequest consultaRequest = new ConsultaClienteRequest();
				consultaRequest.setDocumentoIdentidad(numDocCliente);
				ConsultaClienteResponse clienteVTiger = vtigerRestClient.buscarCliente(consultaRequest);
				ConsultaUsuarioRequest consultaUsuarioRequest = new ConsultaUsuarioRequest();

				if (clienteVTiger != null && clienteVTiger.getResult() != null
						&& clienteVTiger.getResult().size() > 0) {
					consultaUsuarioRequest.setId(clienteVTiger.getResult().get(0).getAssigned_user_id());
					ConsultaUsuarioResponse consultaUsuarioResponse = vtigerRestClient
							.buscarUsuario(consultaUsuarioRequest);
					ConsultaPotencialRequest consultaPotencialRequest = new ConsultaPotencialRequest();
					consultaPotencialRequest.setContactID(clienteVTiger.getResult().get(0).getId());
					ConsultaPotencialResponse consultaPotencialResponse = vtigerRestClient
							.buscarPotencial(consultaPotencialRequest);
					if (consultaPotencialResponse.getResult().size() > 0) {
						LOGGER.info("Tiene registro de cliente potencial");
						if (titular.getNombres() == null)
							titular.setNombres(clienteVTiger.getResult().get(0).getFirstname());
						if (titular.getApellidoPaterno() == null)
							titular.setApellidoPaterno(clienteVTiger.getResult().get(0).getLastname());
						if (titular.getApellidoMaterno() == null)
							titular.setApellidoMaterno(clienteVTiger.getResult().get(0).getCf_920());
						LOGGER.info("CEL:" + clienteVTiger.getResult().get(0).getPhone());
						if (titular.getCelular() == null || titular.getCelular() == "")
							titular.setCelular(clienteVTiger.getResult().get(0).getPhone());
						if (titular.getCorreo() == null)
							titular.setCorreo(clienteVTiger.getResult().get(0).getEmail());
						if (titular.getFechaNacimiento() == null)
							titular.setFechaNacimiento(clienteVTiger.getResult().get(0).getBirthday());
					} else {
						LOGGER.info("NO Tiene registro de cliente potencial");
					}
				}
				response.setTitular(titular);
			}

		} catch (SivTXException e) {
			LOGGER.error("Error obtenerReglamento SivTXException=> " + e);
			response.setObjErrorResource(new ErrorResourceDTO("ADN-007", e.getMsjError()));
			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			response.setMensajeRespuesta(e.getMsjError());
		} catch (Exception ex) {
			LOGGER.error("Error obtenerReglamento Exception:", ex);
			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			response.setMensajeRespuesta(
					// Utilitarios.obtenerMensaje(messageSource,
					// Constantes.MENSAJE_RESPUESTA_GENERAL_ERR0R)
					Utilitarios.obtenerMensaje(messageSource, "Error: Comunicación con Acsel-e"));
			response.setObjErrorResource(new ErrorResourceDTO("ADN-008", ex.getMessage()));
			return response;

		}
		Gson gson = new Gson();
		/*logRepository.save(new EventLog("Solicitud", response.getCodigoRespuesta(),
				"/adns/cliente/" + tipoDocCliente + "/" + numDocCliente, response.getMensajeRespuesta(),
				gson.toJson(response.getObjErrorResource()), gson.toJson(observaciones), idUsuario, device, os));*/
		LOGGER.info("Salio AdnServiceImpl#obtenerInformacionCliente(tipoDocCliente, numDocCliente)");
		return response;
	}

	/*
	 * private List<GenericoComboBean> getDatosViaCobro(String campo) {
	 * 
	 * List<GenericoComboBean> listaGenerica = new ArrayList<GenericoComboBean>();
	 * 
	 * 
	 * List<SolicitudViaCobro> viasCobro = viaCobroRepository.findAll();
	 * 
	 * if ("Banco".equals(campo)) { listaGenerica = viasCobro.stream().map(temp -> {
	 * GenericoComboBean obj = new GenericoComboBean();
	 * obj.setCodigo(temp.getIdViaCobro().toString());
	 * obj.setDescripcion(temp.getNombreViaCobro()); return obj;
	 * }).distinct().collect(Collectors.toList()); } if ("Cuenta".equals(campo)) {
	 * listaGenerica = viasCobro.stream().map(temp -> { GenericoComboBean obj = new
	 * GenericoComboBean(); obj.setCodigo(temp.getIdViaCobro().toString());
	 * obj.setDescripcion(temp.getNombreViaCobro()); return obj;
	 * }).distinct().collect(Collectors.toList()); }
	 * 
	 * return listaGenerica; }
	 */

	@Override
	public ByteArrayInputStream printPDFAdn(ADNRegistroRequestDTO aDNRegistroRequestDTO) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		ADNRegistroResponseDTO response = this.registrarInternoADNS(aDNRegistroRequestDTO);

		File file = new File(response.getMensajeRespuesta());

		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			// System.out.println(file.exists() + "!!");
			// InputStream in = resource.openStream();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			try {
				for (int readNum; (readNum = fis.read(buf)) != -1;) {
					bos.write(buf, 0, readNum); // no doubt here is 0
					// Writes len bytes from the specified byte array starting at offset off to this
					// byte array output stream.
					System.out.println("read " + readNum + " bytes,");
				}
			} catch (IOException ex) {
				// Logger.getLogger(genJpeg.class.getName()).log(Level.SEVERE, null, ex);
			}
			byte[] bytes = bos.toByteArray();

			/*
			 * File tmp =
			 * File.createTempFile(model.getNombreArchivoSolicitud().replace(".pdf", ""),
			 * ".pdf" ); String nombreTemporal = tmp.getName();
			 */

			/*
			 * FileOutputStream os = new FileOutputStream(tmp); os.write(bytes); os.flush();
			 * os.close();
			 */
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

	}

	@Override
	public ADNConsultaLdpdpResponseDTO obtenerldpdp(String tipoDocumento, String numeroDocumento) {
		// TODO Auto-generated method stub
		System.out.println(tipoDocumento + " " + numeroDocumento);
		LOGGER.info("Entro AdnServiceImpl#obtenerldpdp(tipoDocCliente, numDocCliente)");
		Adn adnDomain = adnRepository.LdpdpfindByTipoNumeroDocumento(tipoDocumento, numeroDocumento);
		// System.out.println(gson.toJson(adnDomain));
		System.out.println(adnDomain);
		ADNConsultaLdpdpResponseDTO response = new ADNConsultaLdpdpResponseDTO();
		if (adnDomain != null) {
			response.setLdpdp(adnDomain.getLdpdp());
		} else {
			response.setLdpdp(null);
		}

		// System.out.println(adnDomain.getLdpdp());
		response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
		response.setMensajeRespuesta(
				Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_EXITO));

		LOGGER.info("Salio AdnServiceImpl#obtenerldpdp(tipoDocCliente, numDocCliente)");
		return response;
	}

	// this method was added for add remove log
	public boolean hasUniversalConsentAcceptOrNot(String idUsuario, String numeroDocumento, Integer tipoDocumentoADN) {
		TipoDocumentoADN tipoDocumento = TipoDocumentoADN.parse(tipoDocumentoADN);
		ConsentimientoUniversalRequest request = new ConsentimientoUniversalRequest();
		request.setTipoDocumento(
				tipoDocumento.equals(TipoDocumentoADN.DNI) ? Constantes.CONSENTIMIENTO_TIPODDOCUMENTO_DNI
						: Constantes.CONSENTIMIENTO_TIPODDOCUMENTO_CE);
		request.setNumeroDocumento(numeroDocumento);
		request.setUsuario(idUsuario);
		request.setIdConfiguracion(Constantes.IDTRAMITEVIDA);
		Map<String, Object> response = crmRestClient.getConsentimientoUniversal(request);
		LOGGER.info(" request getConsentimientoUniversal [" + request + "] Response => " + gson.toJson(response));
		return null != response && !response.isEmpty() && response.containsKey("ind_consentimiento")
				&& !Objects.isNull(response.get("ind_consentimiento"));
	}

	public Map<String, Object> getDataConsentimientov2(String idUsuario, String documentType, String documentNumber) {
		LOGGER.info("Entro AdnServiceImpl#getDataConsentimientov2(documentType, documentNumber)");

		//obtenerr getDataConsentimiento para obtener campos necesarios
		Map<String, Object> dataConsentimiento = this.getDataConsentimiento(idUsuario, documentNumber, Integer.parseInt(documentType));
		
		Map<String, Object> response = null;
		ConsentimientoV2Request request = new ConsentimientoV2Request();

		// Setear parámetros del request
		request.setDocumentType(documentType);
		request.setDocumentNumber(documentNumber);
		request.setBusiness(Constantes.CONSENTIMIENTO_EMPRESA);
		request.setOnlyLatest(true);
		request.setProduct(Constantes.CONSENTIMIENTO_PRODUCTO);
		request.setPage(1);
		request.setLimit(1);
		
		try {
			response = crmRestClient.getConsentimientov2(request);
			LOGGER.info("request getDataConsentimientov2 [" + gson.toJson(request) + "] Response => " + gson.toJson(response));
		} catch (Exception e) {
			LOGGER.error("Error en getDataConsentimientov2: " + e.getMessage(), e);
			response = new HashMap<>();
		}
		
		if (response == null) {
			response = new HashMap<>();
		}
		
		// Obtener estado_lpdp y token_consentimiento_usado de la tabla persona_consentimiento
		try {
			PersonaConsentimiento personaConsentimiento = personaConsentimientoRepository.findByTipoNumeroDocumento(documentType, documentNumber);
			Integer estadoLpdp = personaConsentimiento != null ? personaConsentimiento.getEstadoLpdp() : Constantes.CONSENTIMIENTO_ESTADO_LPDP_INACCION;
			Integer tokenConsentimientoUsado = personaConsentimiento != null ? personaConsentimiento.getTokenConsentimientoUsado() : null;
			
			// Agregar estado_lpdp, token_consentimiento_usado y campos necesarios al response
			response.put("estado_lpdp", estadoLpdp);
			response.put("token_consentimiento_usado", tokenConsentimientoUsado);
			response.put("aud_usr_ingreso", dataConsentimiento.get("aud_usr_ingreso"));
			response.put("acceptedConsentimiento", dataConsentimiento.get("acceptedConsentimiento"));
			response.put("hasConsentimiento", dataConsentimiento.get("hasConsentimiento"));
			response.put("hasConsentimientoUniversal", dataConsentimiento.get("hasConsentimientoUniversal"));
			response.put("hasConsentimientoIntercorp", dataConsentimiento.get("hasConsentimientoIntercorp"));
			response.put("id_tratamiento", dataConsentimiento.get("id_tratamiento"));
			response.put("id_configuracion", dataConsentimiento.get("id_configuracion"));

			LOGGER.info("Estado LPDP: " + estadoLpdp + " y Token Consentimiento Usado: " + tokenConsentimientoUsado + " agregados al response para documento: " + documentNumber);
		} catch (Exception ex) {
			LOGGER.error("Error al obtener datos de tabla persona_consentimiento: " + ex.getMessage(), ex);
			// En caso de error, asignar valores por defecto
			response.put("estado_lpdp", Constantes.CONSENTIMIENTO_ESTADO_LPDP_INACCION);
			response.put("token_consentimiento_usado", null);
		}
		
		LOGGER.info("Salio AdnServiceImpl#getDataConsentimientov2(documentType, documentNumber)");
		return response;
	}

	@Override
	public Map<String, Object> obtenerConsentimiento(String idUsuario, String tipoDocumento, String numeroDocumento) {
		LOGGER.info("Entro AdnServiceImpl#obtenerConsentimiento(idUsuario, tipoDocumento, numeroDocumento)");

		Map<String, Object> response;
		
		try {
			response = this.getDataConsentimientov2(idUsuario, tipoDocumento, numeroDocumento);
			response.put("codigoRespuesta", "01");
			response.put("mensajeRespuesta", "Datos obtenidos correctamente");
			
		} catch (Exception e) {
			LOGGER.error("Error en obtenerConsentimiento: " + e.getMessage(), e);
			response = new HashMap<>();
			response.put("codigoRespuesta", "99");
			response.put("mensajeRespuesta", e.getMessage());
		}
		
		LOGGER.info("Salio AdnServiceImpl#obtenerConsentimiento(idUsuario, tipoDocumento, numeroDocumento)");
		return response;
	}

	public Map<String, Object> getDataConsentimiento(String idUsuario, String numeroDocumento,
			Integer tipoDocumentoADN) {
		Map<String, Object> response = new HashMap<>();
		Gson gson = new Gson();
		TipoDocumentoADN tipoDocumento = TipoDocumentoADN.parse(tipoDocumentoADN);

		if (null == tipoDocumento) {
			response = new HashMap<>();
			response.put("hasConsentimiento", false);
			response.put("acceptedConsentimiento", false);
			response.put("estado_lpdp", null);

			return addConsentimientov2Data(response, "1", numeroDocumento);
		}

		// Obtener persona_consentimiento para estado_lpdp
		String tipoDocumentoStr = tipoDocumento.equals(TipoDocumentoADN.DNI) ? "1" 
				: (tipoDocumento.equals(TipoDocumentoADN.CARNE_EXTRANJERIA) ? "2" : "11");
		PersonaConsentimiento personaConsentimiento = personaConsentimientoRepository.findByTipoNumeroDocumento(tipoDocumentoStr, numeroDocumento);
		Integer estadoLpdp = personaConsentimiento != null ? personaConsentimiento.getEstadoLpdp() : Constantes.CONSENTIMIENTO_ESTADO_LPDP_INACCION; // 1 = Sin accion

		ConsentimientoUniversalRequest request = new ConsentimientoUniversalRequest();
		request.setTipoDocumento(
				tipoDocumento.equals(TipoDocumentoADN.DNI) ? Constantes.CONSENTIMIENTO_TIPODDOCUMENTO_DNI
						: (tipoDocumento.equals(TipoDocumentoADN.CARNE_EXTRANJERIA) ? Constantes.CONSENTIMIENTO_TIPODDOCUMENTO_CE
						: Constantes.CONSENTIMIENTO_TIPODDOCUMENTO_RUC));
		request.setNumeroDocumento(numeroDocumento);
		request.setUsuario(idUsuario);

		// Consentimiento intercorp
		request.setIdConfiguracion(Constantes.IDTRATAMIENTOUNIVERSALINTERCORP);
		response = crmRestClient.getConsentimientoUniversal(request);
		LOGGER.info(
				" request getConsentimientoUniversal Intercorp [" + request + "] Response => " + gson.toJson(response));
		if (null != response && !response.isEmpty() && response.containsKey("ind_consentimiento")
				&& !Objects.isNull(response.get("ind_consentimiento")) && StringUtils.equals(
						response.get("ind_consentimiento").toString(), IndicadorConsentimiento.SI.getCodigo())) {
			response.put("id_tratamiento", Constantes.IDTRATAMIENTOUNIVERSALINTERCORP);
			response.put("hasConsentimiento", true);
			response.put("hasConsentimientoUniversal", true);
			response.put("hasConsentimientoIntercorp", true);
			response.put("acceptedConsentimiento", response.containsKey("ind_consentimiento")
					&& null != response.get("ind_consentimiento")
					&& response.get("ind_consentimiento").toString().equals(IndicadorConsentimiento.SI.getCodigo()));
			response.put("estado_lpdp", estadoLpdp);
			response.putAll(this.formatFechaConsentimiento(response));
			return addConsentimientov2Data(response, tipoDocumentoStr, numeroDocumento);
		}

		// Consentimiento universal
		request.setIdConfiguracion(Constantes.IDTRAMITEVIDA);
		response = crmRestClient.getConsentimientoUniversal(request);
		LOGGER.info(" request getConsentimientoUniversal [" + request + "] Response => " + gson.toJson(response));
		if (null != response && !response.isEmpty() && response.containsKey("ind_consentimiento")
				&& !Objects.isNull(response.get("ind_consentimiento")) && StringUtils.equals(
						response.get("ind_consentimiento").toString(), IndicadorConsentimiento.SI.getCodigo())) {
			response.put("id_tratamiento", Constantes.IDTRAMITEVIDA);
			response.put("hasConsentimiento", true);
			response.put("hasConsentimientoIntercorp", false);
			response.put("hasConsentimientoUniversal", true);
			response.put("acceptedConsentimiento", response.containsKey("ind_consentimiento")
					&& null != response.get("ind_consentimiento")
					&& response.get("ind_consentimiento").toString().equals(IndicadorConsentimiento.SI.getCodigo()));
			response.put("estado_lpdp", estadoLpdp);
			response.putAll(this.formatFechaConsentimiento(response));
			return addConsentimientov2Data(response, tipoDocumentoStr, numeroDocumento);
		}

		// Consentimiento producto
		request.setIdConfiguracion(Constantes.IDTRATAMIERNTOPRODUCTOVIDA);
		response = crmRestClient.getConsentimientoUniversal(request);
		LOGGER.info(" request getConsentimientoUniversal [" + request + "] Response => " + gson.toJson(response));
		if (null != response && !response.isEmpty() && response.containsKey("ind_consentimiento")
				&& !Objects.isNull(response.get("ind_consentimiento")) && StringUtils.equals(
						response.get("ind_consentimiento").toString(), IndicadorConsentimiento.SI.getCodigo())) {
			response.put("id_tratamiento", Constantes.IDTRATAMIERNTOPRODUCTOVIDA);
			response.put("hasConsentimiento", true);
			response.put("hasConsentimientoIntercorp", false);
			response.put("hasConsentimientoUniversal", false);
			response.put("acceptedConsentimiento", response.containsKey("ind_consentimiento")
					&& null != response.get("ind_consentimiento")
					&& response.get("ind_consentimiento").toString().equals(IndicadorConsentimiento.SI.getCodigo()));
			response.put("estado_lpdp", estadoLpdp);
			response.putAll(this.formatFechaConsentimiento(response));
			return addConsentimientov2Data(response, tipoDocumentoStr, numeroDocumento);
		}

		// consentimiento por tratamiento
		request.setIdConfiguracion(Constantes.IDCONFIGURACIONUNIVERSAL);
		response = crmRestClient.getConsentimientoPorTratamiento(request);
		LOGGER.info(" request getConsentimientoPorTratamiento [" + request + "] Response => " + gson.toJson(response));
		if (null == response) {
			response = new HashMap<>();
			response.put("hasConsentimiento", false);
			response.put("acceptedConsentimiento", false);
		} else {
			response.put("hasConsentimiento", true);
			response.put("acceptedConsentimiento", response.containsKey("ind_consentimiento")
					&& null != response.get("ind_consentimiento")
					&& response.get("ind_consentimiento").toString().equals(IndicadorConsentimiento.SI.getCodigo()));
			response.put("estado_lpdp", estadoLpdp);
			response.putAll(this.formatFechaConsentimiento(response));
		}
		response.put("id_tratamiento", 0);
		response.put("hasConsentimientoIntercorp", false);
		response.put("hasConsentimientoUniversal", false);
		response.put("estado_lpdp", estadoLpdp);

		return addConsentimientov2Data(response, tipoDocumentoStr, numeroDocumento);
	}

	private Map<String, Object> addConsentimientov2Data(Map<String, Object> response, String tipoDocumentoStr, String numeroDocumento) {
		try {
			ConsentimientoV2Request requestV2 = new ConsentimientoV2Request();
			requestV2.setDocumentType(tipoDocumentoStr);
			requestV2.setDocumentNumber(numeroDocumento);
			requestV2.setBusiness(Constantes.CONSENTIMIENTO_EMPRESA);
			requestV2.setProduct(Constantes.CONSENTIMIENTO_PRODUCTO);
			requestV2.setOnlyLatest(true);
			requestV2.setPage(1);
			requestV2.setLimit(1);
			
			Map<String, Object> responseV2 = crmRestClient.getConsentimientov2(requestV2);
			LOGGER.info("request getConsentimientov2 [" + gson.toJson(requestV2) + "] Response => " + gson.toJson(responseV2));
			
			if (responseV2 != null && !responseV2.isEmpty()) {
				response.putAll(responseV2);
				LOGGER.info("Datos de consentimientov2 agregados al response del método getDataConsentimiento");
			}
		} catch (Exception e) {
			LOGGER.error("Error al obtener datos de consentimientov2: " + e.getMessage(), e);
		}
		return response;
	}

	public BaseResponseDTO createConsentimiento(Integer idTipo, ADNRegistroRequestDTO payload) {
		BaseResponseDTO response = new BaseResponseDTO();
		Map<String, Object> result = null;
		Gson gson = new Gson();

		//VALIDACION DE CARNET DE EXTRANJERIA
		if(payload.getTitular().getTipoDocumento().equals(2) && payload.getTitular().getNumeroDocumento().length() < 9) {
			response.setCodigoRespuesta(HttpStatus.BAD_REQUEST.toString());
			response.setMensajeRespuesta("Numero de Carnet de Extranjeria no valido");

			return response;
		}

		// VALIDACION DE CARACTER NO VALIDOS
		if (!payload.getTitular().getNombres().matches("[A-Za-z ñÑ]*")
				|| !payload.getTitular().getApellidoPaterno().matches("[A-Za-z ñÑ]*")
				|| !payload.getTitular().getApellidoMaterno().matches("[A-Za-z ñÑ]*")) {
			response.setCodigoRespuesta(HttpStatus.BAD_REQUEST.toString());
			response.setMensajeRespuesta(
					"No se permite caracteres especiales en Nombres, Apellido Paterno y Apellido Materno");

			return response;
		}

		// VALIDACION DE ESPACIO EN BLANCO EN CORREO
		if(payload.getTitular().getCorreo().contains(" ")) {
			response.setCodigoRespuesta(HttpStatus.BAD_REQUEST.toString());
			response.setMensajeRespuesta("El campo e-mail no debe contener espacios en blanco");

			return response;
		}
		
		if(payload.getTitular().getCorreo().contains("'")) {
			response.setCodigoRespuesta(HttpStatus.BAD_REQUEST.toString());
			response.setMensajeRespuesta("El campo e-mail no debe contener caracteres especiales");

			return response;
		}

		TipoEnvioConsentimiento tipo = TipoEnvioConsentimiento.parse(idTipo);

		if (null == tipo) {
			response.setCodigoRespuesta(HttpStatus.BAD_REQUEST.toString());
			response.setMensajeRespuesta("El tipo enviado no es permitido. Debe enviar o SMS o Correo o WhatsApp");

			return response;
		}

		Map<String, Object> consentimientoData = this.getDataConsentimiento(payload.getIdUsuario(),
				payload.getTitular().getNumeroDocumento(), payload.getTitular().getTipoDocumento());
		Boolean hasConsentimiento = Boolean.parseBoolean(consentimientoData.get("hasConsentimiento").toString());
		
		// Verificar si existe un consentimiento con política de privacidad
		// Se usa la nueva api para validar si existe o no el consentimiento
		// boolean hasConsentimientov2 = this.validateConsentimientoInNewConsentimiento(payload.getTitular().getTipoDocumento(),
		// 	payload.getTitular().getNumeroDocumento());
		// LOGGER.info("registrarADNS hasConsentimientov2 : " + hasConsentimientov2);

		if (hasConsentimiento) {
			// TODO: Actualizar el consentimiento
			BaseResponseDTO updateConsentimientoResponse = this.updateConsentimiento(payload.getIdUsuario(), payload);
			// Procesar el consentimiento en el nuevo formato
			// Map<String, Object> resultProcessConsent = newUpdateConsentimiento(
			// 	payload.getTitular().getTipoDocumento(), payload.getTitular().getNumeroDocumento(),
			// 	payload.getTitular().getNombres(), payload.getTitular().getApellidoPaterno(),
			// 	payload.getTitular().getApellidoMaterno(), payload.getTitular().getCorreo(),
			// 	payload.getTitular().getCelular().toString(), payload.getTitular().getFechaNacimiento(),
			// 	payload.getTitular().getGenero(), Constantes.CONSENTIMIENTO_ACCION_APROBACION, Constantes.CONSENTIMIENTO_ESTADO_LPDP_INACCION);
			// LOGGER.info(" response processFormatConsentimiento: " + gson.toJson(resultProcessConsent));

			boolean saveResult = savePersonaConsentimiento(payload.getTitular().getTipoDocumento(),
			 payload.getTitular().getNumeroDocumento(), Constantes.CONSENTIMIENTO_ESTADO_LPDP_INACCION,
			 payload.getIdUsuario(), 0);
			LOGGER.info("Resultado del guardado en persona_consentimiento: " + (saveResult ? "EXITOSO" : "FALLIDO") + " para documento: " + payload.getTitular().getNumeroDocumento());
			
			response.setCodigoRespuesta("200");
			response.setMensajeRespuesta("El consentimiento fue enviado para ser actualizado.");
			
			return response;
			// return this.sendConsentimiento(idTipo,
			// payload.getTitular().getNumeroDocumento(), payload.getIdUsuario(),
			// payload.getTitular().getTipoDocumento());
		}

		String base64DataconsentimientoCliente = this.generateUrlConsentimientoAceptado(
				payload.getTitular().getNumeroDocumento(), payload.getIdUsuario(),
				payload.getTitular().getTipoDocumento().toString());

		CreateConsentimiento request = new CreateConsentimiento();
		request.setAud_usr_ingreso(payload.getIdUsuario());
		request.setCod_tipo_identificacion(
				payload.getTitular().getTipoDocumento() == 1 ? Constantes.CONSENTIMIENTO_TIPODDOCUMENTO_DNI
						: (payload.getTitular().getTipoDocumento() == 11 ? Constantes.CONSENTIMIENTO_TIPODDOCUMENTO_RUC
						: Constantes.CONSENTIMIENTO_TIPODDOCUMENTO_CE));
		request.setGls_apellido_materno(payload.getTitular().getApellidoMaterno().trim().toUpperCase());
		request.setGls_apellido_paterno(payload.getTitular().getApellidoPaterno().trim().toUpperCase());
		request.setGls_celular(payload.getTitular().getCelular().toString());
		request.setId_configuracion(Constantes.IDCONFIGURACIONUNIVERSAL);
		request.setGls_mail(payload.getTitular().getCorreo().trim());
		request.setGls_mail_agente(payload.getCorreoUsuario().trim());
		request.setGls_nombres(payload.getTitular().getNombres().trim().toUpperCase());
		request.setGls_nombres_agente(payload.getNombreVendedorCRM());
		request.setGls_num_identificacion(payload.getTitular().getNumeroDocumento());
		request.setGls_sexo(payload.getTitular().getGenero() == 1 ? "M" : "F");
		request.setGls_telefono(payload.getTitular().getCelular().toString());
		request.setNum_agente(Integer.parseInt(payload.getCodigoVendedorCRM().toString()));
		// TODO: Obtener el id cleinte
		String exitoUrl = urlAdnFront + "adn-consentimiento-aceptado?token={base64}";
		String finalUrl = new StringBuilder(urlAdnFront).append("adn-consentimiento-aceptado?token=")
				.append(base64DataconsentimientoCliente).toString();
		LOGGER.info(" request finalUrl: " + finalUrl);
		try {
			String url = URLEncoder.encode(new UriTemplate(exitoUrl).expand(base64DataconsentimientoCliente).toString(),
					"UTF-8");
			request.setGls_url_exito(
					// url
					// new
					// UriTemplate(exitoUrl).expand(base64DataconsentimientoCliente).toASCIIString()
					finalUrl);
		} catch (Exception ex) {

		}

		String fechaNacimiento = payload.getTitular().getFechaNacimiento();

		request.setFec_nacimiento(this.getFormattedFechaNacimientoConsentimiento(fechaNacimiento));

		ConsentimientoDireccion direccion = new ConsentimientoDireccion();
		direccion.setCod_departamento(Constantes.CONSENTIMIENTO_DEPARTAMENTO_DEFAULT);
		direccion.setCod_distrito(Constantes.CONSENTIMIENTO_DISTRITO_DEFAULT);
		direccion.setCod_provincia(Constantes.CONSENTIMIENTO_PROVINCIA_DEFAULT);
		direccion.setCod_tipo_via(Constantes.CONSENTIMIENTO_CODVIA_DEFAULT);
		direccion.setGls_direccion(Constantes.CONSENTIMIENTO_DIRECCION_DEFAULT);
		direccion.setGls_espacio_urbano(Constantes.CONSENTIMIENTO_ESPACIO_DEFAULT);

		request.setDireccion(direccion);

		try {
			result = crmRestClient.createConsentimiento(request);
			LOGGER.info(
					" request createConsentimiento [" + gson.toJson(request) + "] Response => " + gson.toJson(result));
		} catch (Exception ex) {
			LOGGER.error(" request createConsentimiento [" + gson.toJson(request) + "] Exceptiom => " + ex.getMessage(),
					ex);
		}

		if (null == result) {
			response.setCodigoRespuesta(HttpStatus.NOT_FOUND.toString());
			response.setMensajeRespuesta("Hubo un problema al crear el consentimiento.");
		} else {
            // Procesar el consentimiento en el nuevo formato
            // Map<String, Object> resultProcessConsent = newCreateConsentimiento(
			// 	payload.getTitular().getTipoDocumento(), payload.getTitular().getNumeroDocumento(),
			// 	payload.getTitular().getNombres(), payload.getTitular().getApellidoPaterno(),
			// 	payload.getTitular().getApellidoMaterno(), payload.getTitular().getCorreo(),
			// 	payload.getTitular().getCelular().toString(), payload.getTitular().getFechaNacimiento(),
			// 	payload.getTitular().getGenero(), Constantes.CONSENTIMIENTO_ACCION_INACCION, Constantes.CONSENTIMIENTO_ESTADO_LPDP_INACCION);
			// LOGGER.info(" response processFormatConsentimiento: " + gson.toJson(resultProcessConsent));
			boolean saveResult = savePersonaConsentimiento(payload.getTitular().getTipoDocumento(),
			 payload.getTitular().getNumeroDocumento(), Constantes.CONSENTIMIENTO_ESTADO_LPDP_INACCION,
			 payload.getIdUsuario(), 0);
			LOGGER.info("Resultado del guardado en persona_consentimiento: " + (saveResult ? "EXITOSO" : "FALLIDO") + " para documento: " + payload.getTitular().getNumeroDocumento());
			
			response.setCodigoRespuesta("200");
			response.setMensajeRespuesta("El consentimiento fue creado exitosamente.");
		}

		return response;
	}

	public BaseResponseDTO sendConsentimiento(Integer idTipo, String dni, String usuario, Integer tipoDocumento,
			Boolean userOnly, Map<String, Object> request) {
		BaseResponseDTO response = new BaseResponseDTO();
		Map<String, Object> result = null;
		Gson gson = new Gson();

		TipoEnvioConsentimiento tipo = TipoEnvioConsentimiento.parse(idTipo);

		if (null == tipo) {
			response.setCodigoRespuesta(HttpStatus.BAD_REQUEST.toString());
			response.setMensajeRespuesta("El tipo enviado no es permitido. Debe enviar o SMS o Correo");

			return response;
		}

		Map<String, Object> consentimientoData = this.getDataConsentimiento(usuario, dni, tipoDocumento);
		//Boolean hasConsentimiento = Boolean.parseBoolean(consentimientoData.get("hasConsentimiento").toString());
		Boolean acceptedConsentimiento = Boolean
				.parseBoolean(consentimientoData.get("acceptedConsentimiento").toString());
		LOGGER.info("consentimientoData : " + gson.toJson(consentimientoData));
		consentimientoData.put("usuario", usuario);
        // Se usa la nueva api para validar si existe o no el consentimiento
		boolean hasConsentimientov2 = this.validateExistConsentInPersonaConsentimiento(tipoDocumento, dni);
		LOGGER.info("registrarADNS hasConsentimientov2 : " + hasConsentimientov2);

		if (!hasConsentimientov2) {
			response.setCodigoRespuesta(HttpStatus.NO_CONTENT.toString());
			response.setMensajeRespuesta("El cliente no cuenta con consentimiento.");

			return response;
		}

		// Verificar si ya acepto el consentimiento basado en token_consentimiento_usado
		boolean consentimientoYaAceptado = this.isConsentimientoYaAceptado(tipoDocumento, dni);
		
		if (consentimientoYaAceptado) {
			// Si ya fue aceptado, enviar PDF de constancia
			LOGGER.info("Consentimiento ya aceptado para documento: " + dni + ", enviando PDF de constancia");
			if (request != null) {
				request.put("id_tratamiento", consentimientoData.get("id_tratamiento").toString());
				request.put("id_configuracion", consentimientoData.get("id_configuracion").toString());
				request.put("aud_usr_ingreso", consentimientoData.get("aud_usr_ingreso").toString());
			}
			return this.sendPDFConsentimiento(idTipo, dni, consentimientoData, userOnly, request);
		}

		StringBuilder strBFullname = new StringBuilder().append(consentimientoData.get("gls_nombres").toString())
				.append(" ").append(consentimientoData.get("gls_apellido_paterno").toString()).append(" ")
				.append(consentimientoData.get("gls_apellido_materno").toString());

		StringBuilder nameAgente = new StringBuilder().append(consentimientoData.get("gls_nombres_agente").toString());

		String token = null != consentimientoData.get("gls_token") ? consentimientoData.get("gls_token").toString()
				: null;

		if (null == token) {
			response.setCodigoRespuesta(HttpStatus.INTERNAL_SERVER_ERROR.toString());
			response.setMensajeRespuesta("No se pudo obtener el token del consentimiento");

			return response;
		}

		String linkConsentimiento = endpointConsentimiento + token;
		switch (tipo) {
		case MAIL:
			try {
				InputStream stream = new ClassPathResource(
						Constantes.RUTA_PLANTILLA + "/" + Constantes.PLANTILLA_ADN_CONSENTIMIENTO).getInputStream();

				String systemOp = System.getProperty("os.name");
				if (!systemOp.contains("Windows")) {
					stream = new FileInputStream(
							new File(rutaPlantillaDocLinux + "//" + Constantes.PLANTILLA_ADN_CONSENTIMIENTO));
				}

				String bodyHtml = Utilitarios.valorString(StreamUtils.copyToString(stream, Charset.defaultCharset()));

				String bodyConsentimiento = bodyHtml;
				linkConsentimiento  += "?src="+sourceConsentimientoMail;
				bodyConsentimiento = bodyConsentimiento.replaceAll("#!Id_nombres!#", strBFullname.toString());
				bodyConsentimiento = bodyConsentimiento.replaceAll("#!Id_agente!#", nameAgente.toString());
				bodyConsentimiento = bodyConsentimiento.replaceAll("#!Id_link!#", linkConsentimiento);

				String asunto = asuntoCorreoConsentimiento;

				enviarCorreoConAdjunto(asunto, consentimientoData.get("gls_mail").toString(), bodyConsentimiento, null,  "ADN",  "",  dni, usuario);

				response.setCodigoRespuesta("200");
				response.setMensajeRespuesta("Se envió correctamente el mail con el link para el consentimiento");

			} catch (Exception e) {
				e.getStackTrace();
			}
			break;
		case SMS:
			linkConsentimiento  += "?src="+sourceConsentimientoSms;
			String nombre = consentimientoData.get("gls_nombres").toString().trim().split(" ").length > 0
					? consentimientoData.get("gls_nombres").toString().trim().split(" ")[0]
					: consentimientoData.get("gls_nombres").toString();
			StringBuilder strb = new StringBuilder("Estimad@ ").append(nombre)
					.append(",\nPor favor ingrese al link para otorganos tu consentimiento de asesoria: ")
					.append(linkConsentimiento);
			EnviarSmsRequest smsRequest = new EnviarSmsRequest();
			
//			SMSRequest smsRequest = new SMSRequest();
			smsRequest.setCelular(consentimientoData.get("gls_celular").toString().toString());
			smsRequest.setMensaje(strb.toString());
			smsRequest.setNroDocumento(consentimientoData.get("gls_num_identificacion").toString());
			smsRequest.setMotivoNotificacion("ADN");
			smsRequest.setUsuario(usuario);

			try {
				EnviarSmsNotificacionesResponse responseSms = new EnviarSmsNotificacionesResponse();
				responseSms = interseguroRestClient.enviarSmsNotificaciones(smsRequest);
				// EnviarSmsResponse responseSms = new EnviarSmsResponse();
				// responseSms = interseguroRestClient.enviarSms(smsRequest);
				LOGGER.info(" request sendSMS [" + gson.toJson(smsRequest) + "] Response => " + gson.toJson(responseSms));

				response.setCodigoRespuesta("200");
				response.setMensajeRespuesta("Se envió correctamente el link para el consentimiento");
			} catch (Exception e) {
				LOGGER.error(" request sendSMS [" + gson.toJson(smsRequest) + "] exception => " + e.getMessage(), e);

				response.setCodigoRespuesta("500");
				response.setMensajeRespuesta("Hubo un problema al enviar el SMS para el consentimiento");
			}

			break;
		default:
			break;
		}

		return response;
	}

	public BaseResponseDTO sendPDFConsentimiento(Integer idTipo, String dni, Map<String, Object> dataConsentimiento,
			Boolean userOnly, Map<String, Object> request) {
		BaseResponseDTO response = new BaseResponseDTO();
		Map<String, Object> result = null;
		Gson gson = new Gson();

		// Validación mínima para evitar errores críticos
		if (null == dataConsentimiento) {
			response.setCodigoRespuesta("400");
			response.setMensajeRespuesta("No se envio la informacion del consentimiento para el envio");
			return response;
		}

		// Las validaciones específicas de hasConsentimiento e ind_consentimiento se omiten 
		// porque ahora se llama solo cuando token_consentimiento_usado = 1
		if (request != null) {
//			if (request.get("correoUsuario") != null && !request.get("correoUsuario").toString().equalsIgnoreCase("")) {
//				mails.add(request.get("correoUsuario").toString());
//			}
			if (request.get("nombreVendedorCRM") != null
					&& !request.get("nombreVendedorCRM").toString().equalsIgnoreCase("")) {
				dataConsentimiento.put("gls_nombres_agente", request.get("nombreVendedorCRM").toString());
			}

			String tratamiento[] = { "4", "5", "9", "10" };
			String idConfiguracion = "3";
			String audUsrIngreso = "leads-backend";
			for (String t : tratamiento) {
				if ((request.get("id_tratamiento").toString().equals(t)
						&& !request.get("id_configuracion").toString().equals(idConfiguracion))
						|| (request.get("id_tratamiento").toString().equals(t)
								&& request.get("id_configuracion").toString().equals(idConfiguracion)
								&& request.get("aud_usr_ingreso").toString().equals(audUsrIngreso))) {
					dataConsentimiento.put("gls_mail", request.get("correo").toString());
				}
			}
		}
		// correos a enviar al cliente y al agente
		List<String> mails = new ArrayList<>();
		mails.add(dataConsentimiento.get("gls_mail").toString());

		if (null == userOnly || !userOnly) {
			mails.add(dataConsentimiento.get("gls_mail_agente").toString());
		}

		LOGGER.info(" request sendPDFConsentimiento: " + gson.toJson(mails));

		String templateFile = null;

		BaseResponse results = new BaseResponse();
		results = crmRestClient.pdfConsentimiento(dataConsentimiento.get("id_consentimiento_asesoria").toString(),
				dataConsentimiento.get("usuario").toString());

		if (results.getStatusHttp().equals(HttpStatus.OK.toString())) {
			templateFile = results.getMessage();
			response.setCodigoRespuesta("200");
		} else {
			response.setCodigoRespuesta("400");
		}
		// si hubo problemas con la generacion del PDF
		if (response.getCodigoRespuesta() != "200") {
			return response;
		}

		LOGGER.info(" sendPDFConsentimiento templateFile: " + templateFile);

		String filename = new StringBuilder(smeGenerarPDFPrefijo)
				.append(dataConsentimiento.get("gls_num_identificacion").toString()).append(".pdf").toString();

		Date fullFechaConsentimiento = null;
		String fechaConsentimiento = null;
		String horaConsentimiento = null;

		String formato = "yyyy-MM-dd'T'HH:mm:ss";
		fullFechaConsentimiento = DateUtil.stringToDate(dataConsentimiento.get("fec_ultimo_consentimiento").toString(),
				formato);

		if (null != fullFechaConsentimiento) {
			DateFormat fechaFormatter = new SimpleDateFormat("dd/MM/yyyy");
			DateFormat horaFormatter = new SimpleDateFormat("hh:mm a");
			fechaConsentimiento = fechaFormatter.format(fullFechaConsentimiento);
			horaConsentimiento = horaFormatter.format(fullFechaConsentimiento);
		}

		StringBuilder strBFullname = new StringBuilder().append(dataConsentimiento.get("gls_nombres").toString())
				.append(" ").append(dataConsentimiento.get("gls_apellido_paterno").toString()).append(" ")
				.append(dataConsentimiento.get("gls_apellido_materno").toString());

		StringBuilder nameAgente = new StringBuilder().append(dataConsentimiento.get("gls_nombres_agente").toString());
// 
		String tempPDFFullPath = null;
		if (null != templateFile) {
			byte[] pdfData = Base64.decodeBase64(templateFile);
//			rutaPDFPrivada = "D:\\crm-pdf\\";
			// tempPDFFullPath = new
			// StringBuilder(rutaPDFPrivada).append(filename).toString();
			String tmpConsentimientoPdf = System.getProperty("java.io.tmpdir") + File.separator;
			tempPDFFullPath = new StringBuilder(tmpConsentimientoPdf).append(filename).toString();
			LOGGER.info(tempPDFFullPath);
			try (OutputStream stream = new FileOutputStream(tempPDFFullPath)) {
				stream.write(pdfData);
				LOGGER.info(" Se escribio el PDF");
			} catch (Exception ex) {
				LOGGER.info(ex.getMessage());
			}

		}

		try {
			InputStream stream = new ClassPathResource(
					Constantes.RUTA_PLANTILLA + "/" + Constantes.PLANTILLA_ADN_CONSENTIMIENTO_2).getInputStream();

			String systemOp = System.getProperty("os.name");
			if (!systemOp.contains("Windows")) {
				stream = new FileInputStream(
						new File(rutaPlantillaDocLinux + "//" + Constantes.PLANTILLA_ADN_CONSENTIMIENTO_2));
			}

			String bodyHtml = Utilitarios.valorString(StreamUtils.copyToString(stream, Charset.defaultCharset()));

			String bodyConsentimiento = bodyHtml;

			bodyConsentimiento = bodyConsentimiento.replaceAll("#!Id_nombres!#", strBFullname.toString());
			bodyConsentimiento = bodyConsentimiento.replaceAll("#!Id_agente!#", nameAgente.toString());

			String urlConsentimiento = urlAdnConsentimiento + filename;

			List<Adjuntos> files = new ArrayList<>();
			Adjuntos adjunto = new Adjuntos();

			adjunto.setName(filename);
			// adjunto.setUrl(urlConsentimiento);
			adjunto.setContent(templateFile);

			files.add(adjunto);

			String asunto = asuntoCorreoConsentimientoPdf;

			for (String mail : mails) {
				enviarCorreoConAdjunto(asunto, mail, bodyConsentimiento, files,  "ADN-CONSTANCIA",  "",  dni, "ADN");
			}
		} catch (Exception e) {
			e.getStackTrace();
		}

		return response;
	}

	public ByteArrayInputStream descargarPDF(String nombreDocumento) {

		File file = new File(System.getProperty("java.io.tmpdir") + File.separator + nombreDocumento);
		LOGGER.info("path temp: " + System.getProperty("java.io.tmpdir") + File.separator + nombreDocumento + "--"
				+ file.exists());
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		if (file.exists()) {
			FileInputStream fis;
			try {
				fis = new FileInputStream(file);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				byte[] buf = new byte[1024];
				try {
					for (int readNum; (readNum = fis.read(buf)) != -1;) {
						bos.write(buf, 0, readNum);
					}
				} catch (IOException ex) {
					// Logger.getLogger(genJpeg.class.getName()).log(Level.SEVERE, null, ex);
					LOGGER.info("descargarPDF(" + nombreDocumento + ") " + ex.getMessage());
				}
				byte[] bytes = bos.toByteArray();

				try {
					out.write(bytes);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			return new ByteArrayInputStream(out.toByteArray());
		}
		return null;
	}

	public ADNConsentimientoAceptadoResponseDTO processConsentimientoAccepted(Map<String, Object> request) {
		ADNConsentimientoAceptadoResponseDTO response = new ADNConsentimientoAceptadoResponseDTO();
		Map<String, Object> result = null;
		Gson gson = new Gson();

		if (null == request || !request.containsKey("token")) {
			response.setCodigoRespuesta("404");
			response.setMensajeRespuesta("No se envio el token con los datos del cliente");

			return response;
		}

		// se agrega un replace ya que cuando el token generado por Utilitarios.encrypt2
		// contiene el caracter "+" cuando llega al controller
		// se interpreta como espacio en blanco
		String token = request.get("token").toString().replace(" ", "+");
		LOGGER.info(" request getDataConsentimientoAccepted token:" + token);
		String[] tokenValues = Utilitarios.decrypt2(token).split("-");

//		try {
//			ByteArrayInputStream byteIn = new ByteArrayInputStream(Base64.decodeBase64(token));
//			ObjectInputStream in = new ObjectInputStream(byteIn);
//			result = (Map<String, Object>) in.readObject();
//		} catch (Exception ex) {
//			LOGGER.error(" error decode token " + ex.getMessage(), ex);
//		}

		LOGGER.info(" request getDataConsentimientoAccepted " + gson.toJson(tokenValues));

		if (null == tokenValues) {
			response.setCodigoRespuesta("404");
			response.setMensajeRespuesta("No se pudo obtener la data del cliente en el token.");
			return response;
		}

		String dni = tokenValues[1];
		String tipoDocumento = tokenValues[0];
		String idUsuario = tokenValues[2];

		Map<String, Object> dataConsentimiento = this.getDataConsentimiento(idUsuario, dni,
				Integer.parseInt(tipoDocumento));
		
		// Verificar si existe un consentimiento con política de privacidad
        // Se usa la nueva api para validar si existe o no el consentimiento
		boolean hasConsentimientov2 = this.validateExistConsentInPersonaConsentimiento(Integer.parseInt(tipoDocumento), dni);
		LOGGER.info("registrarADNS hasConsentimientov2 : " + hasConsentimientov2);

		if (null == dataConsentimiento || !hasConsentimientov2) {
			response.setCodigoRespuesta("404");
			response.setMensajeRespuesta("No se pudo obtener la información de consentimiento.");
			return response;
		}

		LOGGER.info("processConsentimientoAccepted dataConsentimiento " + gson.toJson(dataConsentimiento));

		Date fullFechaConsentimiento = null;

		try {
			fullFechaConsentimiento = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
					.parse(dataConsentimiento.get("fec_ultimo_consentimiento").toString());
		} catch (Exception ex) {

		}
		DateFormat fechaFormatter = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat horaFormatter = new SimpleDateFormat("hh:mm a");
		String fechaConsentimiento = fechaFormatter.format(fullFechaConsentimiento);
		String horaConsentimiento = horaFormatter.format(fullFechaConsentimiento);

		Map<String, Object> data = new HashMap<>();
		data.put("name", dataConsentimiento.get("gls_nombres").toString());
		data.put("date", new StringBuilder().append(fechaConsentimiento).append(" a las ").append(horaConsentimiento));
		data.put("phone",  dataConsentimiento.get("gls_celular").toString());
		data.put("documentType",  dataConsentimiento.get("gls_tipo_identificacion").toString());
		data.put("documentNumber",  dataConsentimiento.get("gls_num_identificacion").toString());

		response.setData(data);
		response.setCodigoRespuesta("200");

		//guardar en persona_consentimiento
		LOGGER.info("processConsentimientoAccepted savePersonaConsentimiento");
		this.savePersonaConsentimiento(Integer.parseInt(tipoDocumento), dni,
		 Constantes.CONSENTIMIENTO_ESTADO_LPDP_ACEPTADO, idUsuario, 1);

		// generar pdf
		this.sendConsentimiento(TipoEnvioConsentimiento.MAIL.getCodigo(), dni, idUsuario,
				Integer.parseInt(tipoDocumento), null, null);

		// Guardar el canal del consentimiento aceptado en tabla notificaciones
		try {
			LOGGER.info("processConsentimientoAccepted dataConsentimiento " + gson.toJson(dataConsentimiento));
			LOGGER.info("processConsentimientoAccepted ultimo_src_cnto_aceptado: " + dataConsentimiento.containsKey("ultimo_src_cnto_aceptado") + " - valor: " + dataConsentimiento.get("ultimo_src_cnto_aceptado"));
			if (dataConsentimiento.containsKey("ultimo_src_cnto_aceptado")) {
				this.saveTrazabilidadConsentimientoAceptado(dni, dataConsentimiento.get("ultimo_src_cnto_aceptado").toString(), dataConsentimiento.get("fec_ultimo_consentimiento").toString());
			}
		} catch (Exception ex) {
			LOGGER.info("processConsentimientoAccepted error guardar trazabilidad consentimiento (continua flujo): " + ex.getMessage());
		}

		return response;
	}

	private String generateUrlConsentimientoAceptado(String dni, String idUsuario, String tipoDocumento) {
		return Utilitarios.encrypt2(new StringBuilder().append(tipoDocumento).append("-").append(dni).append("-")
				.append(idUsuario).toString());
	}

	@Override
	public BaseResponseDTO updateConsentimiento(String usuario, ADNRegistroRequestDTO payload) {
		return updateConsentimiento(usuario, payload, null);
	}

	private BaseResponseDTO updateConsentimiento(String usuario, ADNRegistroRequestDTO payload, Persona persona) {
		BaseResponseDTO response = new BaseResponseDTO();
		Map<String, Object> result;
		Gson gson = new Gson();
		LOGGER.info("request : " + gson.toJson(payload));
		Map<String, Object> consentimientoData = this.getDataConsentimiento(usuario,
				payload.getTitular().getNumeroDocumento(), payload.getTitular().getTipoDocumento());
		Boolean hasConsentimiento = Boolean.parseBoolean(consentimientoData.get("hasConsentimiento").toString());


		LOGGER.info("updateConsentimiento consentimientoData : " + gson.toJson(consentimientoData));
		if (!hasConsentimiento) {
			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_NO_EXISTE);
			response.setMensajeRespuesta("El cliente no cuenta con consentimiento.");

			return response;
		}

//		if (!acceptedConsentimiento) {
//			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_NO_EXISTE);
//			response.setMensajeRespuesta("El cliente no ha dado aun su consentimiento.");
//
//			return response;
//		}

		// campos a validar si cambiaron para el consentimiento
		String nombres = Objects.isNull(consentimientoData.get("gls_nombres")) ? null
				: consentimientoData.get("gls_nombres").toString();
		String apellidoPaterno = Objects.isNull(consentimientoData.get("gls_apellido_paterno")) ? null
				: consentimientoData.get("gls_apellido_paterno").toString();
		String apellidoMaterno = Objects.isNull(consentimientoData.get("gls_apellido_materno")) ? null
				: consentimientoData.get("gls_apellido_materno").toString();
		String correo = Objects.isNull(consentimientoData.get("gls_mail")) ? null
				: consentimientoData.get("gls_mail").toString();
		String celular = Objects.isNull(consentimientoData.get("gls_celular")) ? null
				: consentimientoData.get("gls_celular").toString();
		String fechaNacimientoConsentimientoStr = Objects.isNull(consentimientoData.get("fec_nacimiento")) ? null
				: consentimientoData.get("fec_nacimiento").toString();
		String fechaNacimientoPayloadStr = payload.getTitular().getFechaNacimiento();
		String sexoStr = Objects.isNull(consentimientoData.get("gls_sexo")) ? null
				: consentimientoData.get("gls_sexo").toString();
		int genero = Integer.parseInt(payload.getTitular().getGenero().toString());
		String generoStr = genero == 1 ? "M" : "F";

		Date fechaNacimiento = null;
		Date fechaNacimientoPayload = null;
		Date fechaNacimientoPersona = null;
		boolean checkfecha = false;
		LOGGER.info("updateConsentimiento fechaNacimientoPayloadStr : " + fechaNacimientoPayloadStr);
		LOGGER.info("updateConsentimiento fechaNacimientoConsentimientoStr : " + fechaNacimientoConsentimientoStr);
		try {
			fechaNacimiento = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(fechaNacimientoConsentimientoStr);
			checkfecha = true;
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
		}

		try {
			fechaNacimientoPayload = new SimpleDateFormat("dd/MM/yyyy").parse(fechaNacimientoPayloadStr);
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
		}

		try {
			Calendar calendarFechaPersona = Calendar.getInstance();
			calendarFechaPersona.setTimeInMillis(persona.getFechaNacimiento().getTime());
			calendarFechaPersona.set(Calendar.HOUR, 0);
			calendarFechaPersona.set(Calendar.MINUTE, 0);
			calendarFechaPersona.set(Calendar.SECOND, 0);
			calendarFechaPersona.set(Calendar.MILLISECOND, 0);
			fechaNacimientoPersona = calendarFechaPersona.getTime();
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
		}

		if (!Objects.isNull(persona) && StringUtils.equals(persona.getActualizadoLead(), "1")) {
			if (StringUtils.equals(persona.getNombres(), payload.getTitular().getNombres().trim().toUpperCase())
					&& StringUtils.equals(persona.getApellidoPaterno(),
							payload.getTitular().getApellidoPaterno().trim().toUpperCase())
					&& StringUtils.equals(persona.getApellidoMaterno(),
							payload.getTitular().getApellidoMaterno().trim().toUpperCase())
					&& StringUtils.equals(StringUtils.trimToEmpty(persona.getCorreo()).toUpperCase(),
							payload.getTitular().getCorreo().trim().toUpperCase())
					&& fechaNacimientoPersona.equals(fechaNacimientoPayload)
					&& StringUtils.equals(persona.getCelular().toString(), payload.getTitular().getCelular().toString())
					&& StringUtils.equals(persona.getGenero(), genero + "")) {
				response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_NO_EXISTE);
				response.setMensajeRespuesta("No se realizaron cambios en la información para el consentimiento");
				return response;
			}
		}

		if (StringUtils.equals(nombres, payload.getTitular().getNombres().trim().toUpperCase())
				&& StringUtils.equals(apellidoPaterno, payload.getTitular().getApellidoPaterno().trim().toUpperCase())
				&& StringUtils.equals(apellidoMaterno, payload.getTitular().getApellidoMaterno().trim().toUpperCase())
				&& StringUtils.equals(StringUtils.trimToEmpty(correo).toUpperCase(),
						payload.getTitular().getCorreo().trim().toUpperCase())
				&& (checkfecha && fechaNacimiento.equals(fechaNacimientoPayload))
				&& StringUtils.equals(celular, payload.getTitular().getCelular().toString())
				&& StringUtils.equals(sexoStr, generoStr.trim().toUpperCase())) {
			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_NO_EXISTE);
			response.setMensajeRespuesta("No se realizaron cambios en la información para el consentimiento");
			return response;
		}

		response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);

		Map<String, Object> requestConsentimiento = crmRestClient.getConsentimiento(
				Integer.parseInt(consentimientoData.get("id_consentimiento_asesoria").toString()),
				payload.getIdUsuario());

		if (null == requestConsentimiento) {
			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_NO_EXISTE);
			response.setMensajeRespuesta("No se pudo obtener la info del consentimiento consentimiento.");
			LOGGER.info("updateConsentimiento error: No se pudo obtener la info del consentimiento consentimiento. ");

			return response;
		}

		String base64DataconsentimientoCliente = this.generateUrlConsentimientoAceptado(
				payload.getTitular().getNumeroDocumento(), payload.getIdUsuario(),
				payload.getTitular().getTipoDocumento().toString());

		String finalUrl = new StringBuilder(urlAdnFront).append("adn-consentimiento-aceptado?token=")
				.append(base64DataconsentimientoCliente).toString();

		requestConsentimiento.put("ind_consentimiento", IndicadorConsentimiento.NO.getCodigo());
		requestConsentimiento.put("gls_apellido_materno",
				payload.getTitular().getApellidoMaterno().trim().toUpperCase());
		requestConsentimiento.put("gls_apellido_paterno",
				payload.getTitular().getApellidoPaterno().trim().toUpperCase());
		requestConsentimiento.put("gls_nombres", payload.getTitular().getNombres().trim().toUpperCase());
		requestConsentimiento.put("gls_mail", payload.getTitular().getCorreo());
		requestConsentimiento.put("gls_celular", payload.getTitular().getCelular().toString());
		requestConsentimiento.put("fec_nacimiento",
				this.getFormattedFechaNacimientoConsentimiento(fechaNacimientoPayloadStr));
		requestConsentimiento.put("aud_usr_modificacion", payload.getIdUsuario());
		requestConsentimiento.put("gls_url_exito", finalUrl);
		requestConsentimiento.put("gls_telefono", payload.getTitular().getCelular().toString());

		requestConsentimiento.put("gls_sexo", generoStr.toString());
		requestConsentimiento.put("gls_nombres_agente", payload.getNombreVendedorCRM().toString());
		requestConsentimiento.put("gls_mail_agente", payload.getCorreoUsuario().toString());
		requestConsentimiento.put("num_agente", payload.getCodigoVendedorCRM().toString());

		/*
		 * String numAgente = requestConsentimiento.get("num_agente") != null ?
		 * requestConsentimiento.get("num_agente").toString() : ""; if
		 * ((!numAgente.equals("") && Integer.parseInt(numAgente) == 0) ||
		 * numAgente.equals("")) { requestConsentimiento.put("num_agente",
		 * payload.getCodigoVendedorCRM().toString()); }
		 */
		this.setDataLogToUpdate(requestConsentimiento, payload.getIdUsuario());
		Map<String, Object> logMap = (Map<String, Object>) requestConsentimiento.get("log");

		if (null != logMap) {
			logMap.put("aud_usr_modificacion", payload.getIdUsuario());

			List<Map<String, Object>> logDetalle = (List<Map<String, Object>>) logMap.get("logDetalle");

			if (null != logDetalle) {
				for (Map<String, Object> detalle : logDetalle) {
					detalle.put("ind_tratamiento", IndicadorConsentimiento.NO.getCodigo());
				}
			}
		}

		try {
			// call to get log consentimiento
			LOGGER.info(" request get log consent id consent " + gson.toJson(requestConsentimiento));
			GetConsentLogResponse responseGetConsentLog = crmRestClient
					.getConsentLog(requestConsentimiento.get("id_consentimiento_asesoria").toString());
			LOGGER.info(" request get log consent Response => " + gson.toJson(responseGetConsentLog));
			boolean hasUniversalConsent = hasUniversalConsentAcceptOrNot(usuario,
					payload.getTitular().getNumeroDocumento(), payload.getTitular().getTipoDocumento());
			acceptConsent(responseGetConsentLog, hasUniversalConsent);

			LOGGER.info(" request updateConsentimiento request " + gson.toJson(requestConsentimiento));
			result = crmRestClient.updateConsentimiento(requestConsentimiento);
			LOGGER.info(" request updateConsentimiento Response => " + gson.toJson(result));

			if (result != null) {
				response.setMensajeRespuesta("Se actualizó correctamente el consentimiento");
			} else {
				response.setMensajeRespuesta("No se pudo actualizar el consentimiento");
			}
		} catch (Exception e) {
			LOGGER.error(" request updateConsentimiento exception => " + e.getMessage(), e);
		}
		return response;
	}

	private void acceptConsent(GetConsentLogResponse result, boolean hasUniversalConsent) {
		try {
			LOGGER.info(" request accept consent request Detail Log size " + result.getLog().getLogDetalle().size());
			if (result.getLog().getLogDetalle().size() > 0 || hasUniversalConsent) {
				boolean existsUniversalConsent = false;
				for (GetConsentLogDetailLog getConsentLogDetailLog : result.getLog().getLogDetalle()) {
					getConsentLogDetailLog.setIndTratamiento(IndicadorConsentimiento.NO.getCodigo());
					if (Objects.equals(getConsentLogDetailLog.getIdTratamiento(), Constantes.IDTRAMITEVIDA)) {
						existsUniversalConsent = true;
					}
				}
				if (!existsUniversalConsent) {
					GetConsentLogDetailLog getConsentLogDetailLog = new GetConsentLogDetailLog();
					getConsentLogDetailLog.setIdTratamiento(Constantes.IDTRAMITEVIDA);
					getConsentLogDetailLog.setIndTratamiento(IndicadorConsentimiento.NO.getCodigo());
					result.getLog().getLogDetalle().add(getConsentLogDetailLog);
				}
				LOGGER.info(" request accept consent request " + gson.toJson(result));
				Map<String, Object> response = crmRestClient.acceptConsent(result);
				LOGGER.info(" request accept consent Response => " + gson.toJson(response));
			}
		} catch (Exception e) {
			LOGGER.error("request acceptConsent exception => " + e.getMessage(), e);
		}
	}

	private String getFormattedFechaNacimientoConsentimiento(String fechaNacimiento) {
		DateFormat frontFechaFormatter = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat backFechaFormatter = new SimpleDateFormat("yyyy-MM-dd");

		try {
			return (backFechaFormatter.format(frontFechaFormatter.parse(fechaNacimiento)));
		} catch (Exception ex) {
			return Constantes.CONSENTIMIENTO_DEFAULT_FECHA_NACIMIENTO;
		}
	}

	private Map<String, Object> formatFechaConsentimiento(Map<String, Object> dataConsentimiento) {
		Map<String, Object> response = new HashMap<String, Object>();
		;
		if (null == dataConsentimiento || !dataConsentimiento.containsKey("fec_ultimo_consentimiento")
				|| null == dataConsentimiento.get("fec_ultimo_consentimiento")) {
			return response;
		}

		Date fullFechaConsentimiento = null;

		try {
			fullFechaConsentimiento = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
					.parse(dataConsentimiento.get("fec_ultimo_consentimiento").toString());
		} catch (Exception ex) {
			LOGGER.info(ex.getMessage());
		}

		if (null != fullFechaConsentimiento) {
			DateFormat fechaFormatter = new SimpleDateFormat("yyyy-MM-dd");
			DateFormat horaFormatter = new SimpleDateFormat("hh:mm:ss");
			response.put("fecha_consentimiento", fechaFormatter.format(fullFechaConsentimiento));
			response.put("hora_consentimiento", horaFormatter.format(fullFechaConsentimiento));
		}

		return response;
	}

	private void setDataLogToUpdate(Map<String, Object> dataConsentimiento, String usuario) {
		if (null == dataConsentimiento.get("log")) {
			Map<String, Object> log = new HashMap<>();

			Map<String, Object> logDetalle = new HashMap<>();
			List<Map<String, Object>> logDetalleList = new ArrayList<>();
			logDetalle.put("id_tratamiento", 1);
			logDetalle.put("ind_tratamiento", IndicadorConsentimiento.NO.getCodigo());
			logDetalle.put("aud_usr_ingreso", usuario);
			logDetalle.put("aud_usr_modificacion", usuario);

			logDetalleList.add(logDetalle);

			log.put("logDetalle", logDetalleList);
			log.put("id_consentimiento_asesoria", dataConsentimiento.get("id_consentimiento_asesoria"));
			log.put("gls_mail", dataConsentimiento.get("gls_mail"));
			log.put("gls_telefono", dataConsentimiento.get("gls_telefono"));
			log.put("gls_celular", dataConsentimiento.get("gls_celular"));
			log.put("aud_usr_ingreso", usuario);
			log.put("aud_usr_modificacion", usuario);

			dataConsentimiento.put("log", log);
		}
	}

	private BaseResponseDTO reprocesarPdfAdn(Map<String, Object> dataConsentimiento) {
		BaseResponseDTO response = new BaseResponseDTO();
//		Map<String, Object> result = null;
//		Gson gson = new Gson();

		// correos a enviar al cliente y al agente

		String filename = new StringBuilder(smeGenerarPDFPrefijo)
				.append(dataConsentimiento.get("gls_num_identificacion").toString()).append(".pdf").toString();
//
//		Date fullFechaConsentimiento = null;
//		String fechaConsentimiento = null;
//		String horaConsentimiento = null;
//
//		String formato = "yyyy-MM-dd'T'HH:mm:ss";
//		fullFechaConsentimiento = DateUtil.stringToDate(dataConsentimiento.get("fec_ultimo_consentimiento").toString(),
//				formato);

//		if (null != fullFechaConsentimiento) {
//			DateFormat fechaFormatter = new SimpleDateFormat("dd/MM/yyyy");
//			DateFormat horaFormatter = new SimpleDateFormat("hh:mm a");
//			fechaConsentimiento = fechaFormatter.format(fullFechaConsentimiento);
//			horaConsentimiento = horaFormatter.format(fullFechaConsentimiento);
//		}
////
//		StringBuilder strBFullname = new StringBuilder().append(dataConsentimiento.get("gls_nombres").toString())
//				.append(" ").append(dataConsentimiento.get("gls_apellido_paterno").toString()).append(" ")
//				.append(dataConsentimiento.get("gls_apellido_materno").toString());

//		SMEGenrarAdjunto4Mail request = new SMEGenrarAdjunto4Mail();

//		request.setRutaArchivo(smeGenerarPDFRutaPDF);
//		request.setArchivos(new ArrayList<>());

//		SMEAdjunto4Mail adjunto = new SMEAdjunto4Mail();
//		adjunto.setRutaWord(
//				new StringBuilder().append(smeGenerarPDFRutaWord).append("/").append(templateFile).toString());

//		Map<String, Object> trama = new HashMap<>();
//		trama.put("Nombres", strBFullname.toString());
//		trama.put("Documento de identidad", dataConsentimiento.get("gls_num_identificacion").toString());
//		trama.put("Fecha de aceptacion", fechaConsentimiento);
//		trama.put("Hora de aceptacion", horaConsentimiento);

//		adjunto.setTrama(trama);

//		request.getArchivos().add(adjunto);

//		String base64PDFFile = null;
//		int counter = 0;
//		String filenameToMail = new StringBuilder(smeGenerarPDFPrefijo)
//				.append(dataConsentimiento.get("gls_num_identificacion").toString()).append("_").append(++counter)
//				.append(".pdf").toString();
// 
		try {

			String templateFile = null;
			BaseResponse results = new BaseResponse();
			results = crmRestClient.pdfConsentimiento(dataConsentimiento.get("id_consentimiento_asesoria").toString(),
					dataConsentimiento.get("usuario").toString());

			if (results.getStatusHttp().equals(HttpStatus.OK.toString())) {
				templateFile = results.getMessage();
				response.setCodigoRespuesta("200");
			} else {
				response.setCodigoRespuesta("400");
			}

			LOGGER.info(" sendPDFConsentimiento templateFile: " + templateFile);

			String tempPDFFullPath = null;
			if (null != templateFile) {
				byte[] pdfData = Base64.decodeBase64(templateFile);
//				rutaPDFPrivada = "D:\\crm-pdf\\";
				String tmpConsentimientoPdf = System.getProperty("java.io.tmpdir") + File.separator;
				tempPDFFullPath = new StringBuilder(tmpConsentimientoPdf).append(filename).toString();
				LOGGER.info(tempPDFFullPath);
				try (OutputStream stream = new FileOutputStream(tempPDFFullPath)) {
					stream.write(pdfData);
					LOGGER.info(" Se escribio el PDF");
				} catch (Exception ex) {
					LOGGER.info(ex.getMessage());
				}

			}
		} catch (Exception e) {
			LOGGER.error("request generarPDFConsentimiento" + " exception => " + e.getMessage(), e);

			response.setCodigoRespuesta("500");
			response.setMensajeRespuesta("Hubo un problema al generar el PDF del consentimiento");
		}

		// si hubo problemas con la generacion del PDF
		if (response.getCodigoRespuesta() != "200") {
			return response;
		}
		return response;
	}

	private boolean mustSkipUpdateConsent(int consentTreatmentId, int consentConfigurationId, String audUsrIngreso,
			Persona persona) {
		return (consentTreatmentId == Constantes.IDTRATAMIENTOUNIVERSALINTERCORP
				|| consentTreatmentId == Constantes.IDTRAMITEVIDA
				|| consentTreatmentId == Constantes.IDTRATAMIERNTOPRODUCTOVIDA)
				&& (consentConfigurationId != 3 || StringUtils.equals(audUsrIngreso, "leads-backend")
						| StringUtils.equals(audUsrIngreso, "vida-backend"))
				&& (Objects.isNull(persona) || StringUtils.equals(persona.getActualizadoLead(), "0"));
	}

	public UpdateTrazabilidadConsentimientoResponse saveTrazabilidadConsentimientoAceptado(String numeroDocumento, String canal, String fechaConsentimiento) {
		UpdateTrazabilidadConsentimientoResponse response = null;
		Gson gson = new Gson();
		UpdateTrazabilidadConsentimientoRequest request = new UpdateTrazabilidadConsentimientoRequest();
		request.setNumeroDocumento(numeroDocumento);
		request.setCanal(canal);
		request.setFechaConsentimiento(fechaConsentimiento);
		response = globalRestClient.saveTrazabilidadConsentimientoAceptado(request);
		LOGGER.info(" request saveTrazabilidadConsentimientoAceptado [" + gson.toJson(request) + "] Response => " + gson.toJson(response));
		return response;
	}

	public boolean validateExistConsentInPersonaConsentimiento(Integer tipoDocumento, String numeroDocumento){
		boolean hasConsentimiento = false;
		PersonaConsentimiento personaConsentimiento = personaConsentimientoRepository.findByTipoNumeroDocumento(tipoDocumento.toString(), numeroDocumento);
		if(personaConsentimiento != null && personaConsentimiento.getEstadoLpdp() != null){
			hasConsentimiento = true;
		}
		return hasConsentimiento;
	}

	public boolean validateExistConsentAcceptedInPersonaConsentimiento(Integer tipoDocumento, String numeroDocumento){
		boolean hasConsentimiento = false;
		PersonaConsentimiento personaConsentimiento = personaConsentimientoRepository.findByTipoNumeroDocumento(tipoDocumento.toString(), numeroDocumento);
		if(personaConsentimiento != null && personaConsentimiento.getEstadoLpdp() != null && personaConsentimiento.getEstadoLpdp().equals(Constantes.CONSENTIMIENTO_ESTADO_LPDP_ACEPTADO)){
			hasConsentimiento = true;
		}
		return hasConsentimiento;
	}

	/**
	 * Guarda o actualiza una PersonaConsentimiento con el estado LPDP
	 * @param tipoDocumento Tipo de documento (1=DNI, 2=CE, 11=RUC)
	 * @param numeroDocumento Número de documento
	 * @param estadoLpdp Estado del consentimiento LPDP
	 * (Inaccion = 1, Aceptado = 2, Rechazado = 3, Permiso para consentimiento = 4, Cambio de datos = 5)
	 * @param usuario Usuario que realiza la operación
	 * @return true si se guardó correctamente, false en caso de error
	 */
	private boolean savePersonaConsentimiento(Integer tipoDocumento, String numeroDocumento,
	 Integer estadoLpdp, String usuario, Integer tokenConsentimientoUsado) {
		try {
			String tipoDocumentoStr = tipoDocumento.equals(1) ? "1" 
					: (tipoDocumento.equals(2) ? "2" : "11");
			PersonaConsentimiento personaConsentimiento = personaConsentimientoRepository.findByTipoNumeroDocumento(tipoDocumentoStr, numeroDocumento);
			
			if (personaConsentimiento == null) {
				// Crear nueva entrada si no existe
				personaConsentimiento = new PersonaConsentimiento();
				personaConsentimiento.setTipoDocumento(tipoDocumentoStr);
				personaConsentimiento.setNumeroDocumento(numeroDocumento);
				personaConsentimiento.setUsuarioCrea(usuario);
				personaConsentimiento.setFechaCrea(new Date());
			}
			
			personaConsentimiento.setEstadoLpdp(estadoLpdp);
			personaConsentimiento.setUsuarioModif(usuario);
			personaConsentimiento.setFechaModif(new Date());
			personaConsentimiento.setTokenConsentimientoUsado(tokenConsentimientoUsado);
			personaConsentimientoRepository.save(personaConsentimiento);
			LOGGER.info("Estado LPDP actualizado en persona_consentimiento para documento: " + numeroDocumento);
			
			return true;
		} catch (Exception ex) {
			LOGGER.error("Error al actualizar estado_lpdp en tabla persona_consentimiento: " + ex.getMessage(), ex);
			return false;
		}
	}

	/**
	 * Verifica si el consentimiento ya fue aceptado basado en token_consentimiento_usado = 1
	 * @param tipoDocumento Tipo de documento (1=DNI, 2=CE, 11=RUC)
	 * @param numeroDocumento Número de documento
	 * @return true si el consentimiento ya fue aceptado, false en caso contrario
	 */
	private boolean isConsentimientoYaAceptado(Integer tipoDocumento, String numeroDocumento) {
		try {
			String tipoDocumentoStr = tipoDocumento.equals(1) ? "1" 
					: (tipoDocumento.equals(2) ? "2" : "11");
			PersonaConsentimiento personaConsentimiento = personaConsentimientoRepository.findByTipoNumeroDocumento(tipoDocumentoStr, numeroDocumento);
			
			if (personaConsentimiento != null && personaConsentimiento.getTokenConsentimientoUsado() != null 
					&& personaConsentimiento.getTokenConsentimientoUsado().equals(1)) {
				LOGGER.info("Consentimiento ya aceptado para documento: " + numeroDocumento + ", token_consentimiento_usado = 1");
				return true;
			}
			
			return false;
		} catch (Exception ex) {
			LOGGER.error("Error al verificar aceptación del consentimiento para documento: " + numeroDocumento + " - " + ex.getMessage(), ex);
			return false; // En caso de error, asumir que no está aceptado
		}
	}

}
