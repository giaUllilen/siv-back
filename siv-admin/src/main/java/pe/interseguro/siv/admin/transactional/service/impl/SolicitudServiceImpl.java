package pe.interseguro.siv.admin.transactional.service.impl;

import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pe.interseguro.siv.admin.transactional.factory.ServiceFactory;
import pe.interseguro.siv.admin.transactional.service.AcpPDFService;
import pe.interseguro.siv.admin.transactional.service.CotizaService;
import pe.interseguro.siv.admin.transactional.service.SolicitudPDFService;
import pe.interseguro.siv.admin.transactional.service.SolicitudService;
import pe.interseguro.siv.common.bean.PagoTokenBean;
import pe.interseguro.siv.common.dto.request.CotizacionCrmOportunidadRequestDTO;
import pe.interseguro.siv.common.dto.request.CotizacionCrmRequestDTO;
import pe.interseguro.siv.common.dto.request.IndenovaNotificacionRequestDTO;
import pe.interseguro.siv.common.dto.request.ObtenerPubSubSendRequestDTO;
import pe.interseguro.siv.common.dto.request.ObtenerRequestEvaluatorRequestDTO;
import pe.interseguro.siv.common.dto.request.PaginationRequestDTO;
import pe.interseguro.siv.common.dto.request.PolicyIssuedRequestDTO;
import pe.interseguro.siv.common.dto.request.SolicitudFiltroRequestDTO;
import pe.interseguro.siv.common.dto.request.SolicitudFinalizarProcesoRequestDTO;
import pe.interseguro.siv.common.dto.request.SolicitudFinalizarProcesoResponseDTO;
import pe.interseguro.siv.common.dto.request.SolicitudFormularioContratanteResponseDTO;
import pe.interseguro.siv.common.dto.request.SolicitudGuardarAseguradoRequestDTO;
import pe.interseguro.siv.common.dto.request.SolicitudGuardarBeneficiarioRequestDTO;
import pe.interseguro.siv.common.dto.request.SolicitudGuardarDPSPreguntaRequestDTO;
import pe.interseguro.siv.common.dto.request.SolicitudGuardarDPSRequestDTO;
import pe.interseguro.siv.common.dto.request.SolicitudGuardarRequestDTO;
import pe.interseguro.siv.common.dto.request.SolicitudRegistroPagoRequestDTO;
import pe.interseguro.siv.common.dto.request.SolicitudRequestDTO;
import pe.interseguro.siv.common.dto.request.SolicitudSMSRequestDTO;
import pe.interseguro.siv.common.dto.request.SolicitudValidarCodigoRequestDTO;
import pe.interseguro.siv.common.dto.request.TokenRequestDTO;
import pe.interseguro.siv.common.dto.response.ADNInicializacionViaCobroResponseDTO;
import pe.interseguro.siv.common.dto.response.BaseResponseDTO;
import pe.interseguro.siv.common.dto.response.CMRArchivoResponseDTO;
import pe.interseguro.siv.common.dto.response.CMRObtenerArchivoResponseDTO;
import pe.interseguro.siv.common.dto.response.ConversionResponseDTO;
import pe.interseguro.siv.common.dto.response.CotizaDetalleCoberturaResponseDTO;
import pe.interseguro.siv.common.dto.response.CotizaDetalleResponseDTO;
import pe.interseguro.siv.common.dto.response.LinkPagoResponseDTO;
import pe.interseguro.siv.common.dto.response.ObtenerDataResponseDTO;
import pe.interseguro.siv.common.dto.response.ObtenerDocumentoResponseDTO;
import pe.interseguro.siv.common.dto.response.ObtenerPubSubSendResponseDTO;
import pe.interseguro.siv.common.dto.response.ObtenerRequestEvaluatorResponseDTO;
import pe.interseguro.siv.common.dto.response.PaginationResponseDTO;
import pe.interseguro.siv.common.dto.response.PagoInicializacionResponseDTO;
import pe.interseguro.siv.common.dto.response.RecotizacionValidarResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudAcreditacionResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudCotizacionesTraspasoResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudDetalleResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudFormularioAseguradoResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudFormularioBeneficiarioResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudFormularioCoberturaResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudFormularioCobroResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudFormularioCotizacionResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudFormularioDPSPreguntaResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudFormularioDPSResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudFormularioResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudGuardarPersonaResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudGuardarResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudItemAcreditacionResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudItemCotizacionesTraspasoResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudItemResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudRegistroResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudReglamentoResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudResponseDTO;
import pe.interseguro.siv.common.dto.response.TipoCambioResponseDTO;
import pe.interseguro.siv.common.dto.response.TokenLinkPagoResponseDTO;
import pe.interseguro.siv.common.enums.IndicadorConsentimiento;
import pe.interseguro.siv.common.enums.TablaEnum;
import pe.interseguro.siv.common.enums.TipoDocumentoADN;
import pe.interseguro.siv.common.exception.ErrorResourceDTO;
import pe.interseguro.siv.common.exception.SivSOAException;
import pe.interseguro.siv.common.persistence.db.mysql.domain.Adn;
import pe.interseguro.siv.common.persistence.db.mysql.domain.CodigoVerificacion;
import pe.interseguro.siv.common.persistence.db.mysql.domain.Multitabla;
import pe.interseguro.siv.common.persistence.db.mysql.domain.Persona;
import pe.interseguro.siv.common.persistence.db.mysql.domain.PlanFuturo;
import pe.interseguro.siv.common.persistence.db.mysql.domain.ServicioEdn;
import pe.interseguro.siv.common.persistence.db.mysql.domain.Solicitud;
import pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudBeneficiario;
import pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudDatosAdicionales;
import pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudDps;
import pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudDpsPregunta;
import pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudFiltrado;
import pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudProducto;
import pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudProductoDetalle;
import pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudTraspaso;
import pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudViaCobro;
import pe.interseguro.siv.common.persistence.db.mysql.domain.ViewSolicitud;
import pe.interseguro.siv.common.persistence.db.mysql.domain.ViewSolicitudPlaft;
import pe.interseguro.siv.common.persistence.db.mysql.repository.AdnRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.CodigoVerificacionRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.MultitablaRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.PersonaRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.PlanFuturoRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.ServicioEdnRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.SolicitudBeneficiarioRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.SolicitudDatosAdicionalesRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.SolicitudDpsPreguntaRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.SolicitudDpsRepositorioRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.SolicitudFiltradoRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.SolicitudProductoDetalleRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.SolicitudProductoRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.SolicitudRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.SolicitudTraspasoRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.SolicitudViaCobroRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.ViewSolicitudPlaftRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.ViewSolicitudRepository;
import pe.interseguro.siv.common.persistence.db.postgres.bean.CotizacionDetalle;
import pe.interseguro.siv.common.persistence.db.postgres.repository.CotizacionRepository;
import pe.interseguro.siv.common.persistence.db.sitc.bean.TarjetaPrimaRecurrente;
import pe.interseguro.siv.common.persistence.db.sitc.bean.TarjetaPrimeraPrima;
import pe.interseguro.siv.common.persistence.db.sitc.repository.SitcRepository;
import pe.interseguro.siv.common.persistence.rest.acsele.AcseleRestClient;
import pe.interseguro.siv.common.persistence.rest.bupo.response.ValidarProspectoAsignacionResponse;
import pe.interseguro.siv.common.persistence.rest.consentimiento.ConsentimientoUniversalRequest;
import pe.interseguro.siv.common.persistence.rest.cotizador.CotizadorRestClient;
import pe.interseguro.siv.common.persistence.rest.cotizador.response.AsegurableResponse;
import pe.interseguro.siv.common.persistence.rest.crm.CrmRestClient;
import pe.interseguro.siv.common.persistence.rest.crm.request.CrearBeneficiariosDetalleRequest;
import pe.interseguro.siv.common.persistence.rest.crm.request.CrearBeneficiariosParamRequest;
import pe.interseguro.siv.common.persistence.rest.crm.request.CrearBeneficiariosRequest;
import pe.interseguro.siv.common.persistence.rest.crm.request.GetEstadoCotizacionRequest;
import pe.interseguro.siv.common.persistence.rest.crm.request.UpdateContactoParamRequest;
import pe.interseguro.siv.common.persistence.rest.crm.request.UpdateContactoRequest;
import pe.interseguro.siv.common.persistence.rest.crm.request.UpdateCotizacionParamRequest;
import pe.interseguro.siv.common.persistence.rest.crm.request.UpdateCotizacionRequest;
import pe.interseguro.siv.common.persistence.rest.crm.request.UpdateFormatoDPSParamRequest;
import pe.interseguro.siv.common.persistence.rest.crm.request.UpdateFormatoDPSRequest;
import pe.interseguro.siv.common.persistence.rest.crm.response.UpdateResponse;
import pe.interseguro.siv.common.persistence.rest.estudionecesidad.EstudioNecesidadRestClient;
import pe.interseguro.siv.common.persistence.rest.estudionecesidad.request.GenerarPdfCabeceras;
import pe.interseguro.siv.common.persistence.rest.estudionecesidad.request.GenerarPdfPreguntas;
import pe.interseguro.siv.common.persistence.rest.estudionecesidad.request.GenerarPdfRequest;
import pe.interseguro.siv.common.persistence.rest.estudionecesidad.response.GenerarPdfResponse;
import pe.interseguro.siv.common.persistence.rest.global.GlobalRestClient;
import pe.interseguro.siv.common.persistence.rest.global.request.DocumentoCorreoRequest;
import pe.interseguro.siv.common.persistence.rest.global.request.EmisionAutomaticaRequest;
import pe.interseguro.siv.common.persistence.rest.global.request.EnvioCorreoClienteEmisionRequest;
import pe.interseguro.siv.common.persistence.rest.global.request.GestiorDocumentoRequest;
import pe.interseguro.siv.common.persistence.rest.global.request.ListDocumentoCorreoRequest;
import pe.interseguro.siv.common.persistence.rest.global.request.ObtenerAgenteRequest;
import pe.interseguro.siv.common.persistence.rest.global.request.ObtenerEstadoSolicitudEmisionRapidaRequest;
import pe.interseguro.siv.common.persistence.rest.global.request.ObtenerEvaluacionSolicitudEmisionRequest;
import pe.interseguro.siv.common.persistence.rest.global.request.ObtenerIdPersonaRequest;
import pe.interseguro.siv.common.persistence.rest.global.request.ObtenerMetadataPublishRequest;
import pe.interseguro.siv.common.persistence.rest.global.request.ObtenerMontoRecargoRequest;
import pe.interseguro.siv.common.persistence.rest.global.request.ObtenerMuerteAccidentalRequest;
import pe.interseguro.siv.common.persistence.rest.global.request.ObtenerPolizaEmisionRapidaRequest;
import pe.interseguro.siv.common.persistence.rest.global.request.ObtenerPolizaRequest;
import pe.interseguro.siv.common.persistence.rest.global.request.ObtenerPubSubSendRequest;
import pe.interseguro.siv.common.persistence.rest.global.request.ObtenerReglasAsigClienteRequest;
import pe.interseguro.siv.common.persistence.rest.global.request.RegistrarAfiliacionTraspasoRequest;
import pe.interseguro.siv.common.persistence.rest.global.request.UploadArchivoRequest;
import pe.interseguro.siv.common.persistence.rest.global.request.ValidarTraspasoRequest;
import pe.interseguro.siv.common.persistence.rest.global.request.obtenerTipoCambioRequest;
import pe.interseguro.siv.common.persistence.rest.global.response.ArchivoResponse;
import pe.interseguro.siv.common.persistence.rest.global.response.ConsultarAcreditacionResponse;
import pe.interseguro.siv.common.persistence.rest.global.response.DocumentoCorreoResponse;
import pe.interseguro.siv.common.persistence.rest.global.response.EmisionAutomaticaResponse;
import pe.interseguro.siv.common.persistence.rest.global.response.EnvioCorreoClienteEmisionResponse;
import pe.interseguro.siv.common.persistence.rest.global.response.GestiorDocumentoResponse;
import pe.interseguro.siv.common.persistence.rest.global.response.ListObtenerCotizacionesResponse;
import pe.interseguro.siv.common.persistence.rest.global.response.ListObtenerIdPersonaResponse;
import pe.interseguro.siv.common.persistence.rest.global.response.ObtenerAgenteResponse;
import pe.interseguro.siv.common.persistence.rest.global.response.ObtenerEstadoSolicitudEmisionRapidaResponse;
import pe.interseguro.siv.common.persistence.rest.global.response.ObtenerEvaluacionSolicitudEmisionResponse;
import pe.interseguro.siv.common.persistence.rest.global.response.ObtenerMensajeEmisionesResponse;
import pe.interseguro.siv.common.persistence.rest.global.response.ObtenerMontoRecargoResponse;
import pe.interseguro.siv.common.persistence.rest.global.response.ObtenerMuerteAccidentalResponse;
import pe.interseguro.siv.common.persistence.rest.global.response.ObtenerPersonaDocumentoResponse;
import pe.interseguro.siv.common.persistence.rest.global.response.ObtenerPolizaEmisionRapidaResponse;
import pe.interseguro.siv.common.persistence.rest.global.response.ObtenerPubSubSendResponse;
import pe.interseguro.siv.common.persistence.rest.global.response.ObtenerUploadArchivoResponse;
import pe.interseguro.siv.common.persistence.rest.global.response.RegistrarAfiliacionTraspasoResponse;
import pe.interseguro.siv.common.persistence.rest.global.response.TasaCambioResponse;
import pe.interseguro.siv.common.persistence.rest.global.response.UploadArchivoResponse;
import pe.interseguro.siv.common.persistence.rest.global.response.UrlResponse;
import pe.interseguro.siv.common.persistence.rest.global.response.ValidarTraspasoResponse;
import pe.interseguro.siv.common.persistence.rest.global.response.obtenerTipoCambioResponse;
import pe.interseguro.siv.common.persistence.rest.interseguro.InterseguroRestClient;
import pe.interseguro.siv.common.persistence.rest.interseguro.dto.Adjuntos;
import pe.interseguro.siv.common.persistence.rest.interseguro.dto.Remitente;
import pe.interseguro.siv.common.persistence.rest.interseguro.request.ClienteLinkPago;
import pe.interseguro.siv.common.persistence.rest.interseguro.request.EnviarCorreoRequestNuevo;
import pe.interseguro.siv.common.persistence.rest.interseguro.request.EnviarSmsRequest;
import pe.interseguro.siv.common.persistence.rest.interseguro.request.LinkPagoRequest;
import pe.interseguro.siv.common.persistence.rest.interseguro.request.PagoLinkPago;
import pe.interseguro.siv.common.persistence.rest.interseguro.response.EnviarSmsResponse;
import pe.interseguro.siv.common.persistence.rest.interseguro.response.LinkPagoResponse;
import pe.interseguro.siv.common.persistence.rest.niubiz.response.BaseResponse;
import pe.interseguro.siv.common.persistence.rest.plaft.PlaftRestClient;
import pe.interseguro.siv.common.persistence.rest.plaft.request.CalificacionPropuestaRequest;
import pe.interseguro.siv.common.persistence.rest.plaft.response.CalificacionPropuestaResponse;
import pe.interseguro.siv.common.persistence.rest.sitc.request.TokenSitcRequest;
import pe.interseguro.siv.common.persistence.rest.sitc.response.TokenSitcResponse;
import pe.interseguro.siv.common.persistence.rest.vidafree.VidaFreeRestClient;
import pe.interseguro.siv.common.persistence.rest.vidafree.request.ConsultaCotizacionDetalleRequest;
import pe.interseguro.siv.common.persistence.rest.vidafree.response.ConsultaCotizacionDetalleResponse;
import pe.interseguro.siv.common.persistence.rest.vidafree.response.GenericoResponse;
import pe.interseguro.siv.common.persistence.soap.sitc.SitcSoapClient;
import pe.interseguro.siv.common.util.Constantes;
import pe.interseguro.siv.common.util.ConstantesAcpPDF;
import pe.interseguro.siv.common.util.ConstantesCRM;
import pe.interseguro.siv.common.util.ConstantesSolicitudPDF;
import pe.interseguro.siv.common.util.DateUtil;
import pe.interseguro.siv.common.util.Utilitarios;

@Service("solicitudService")
public class SolicitudServiceImpl implements SolicitudService {

	private final Logger LOGGER = LoggerFactory.getLogger(SolicitudServiceImpl.class);
	private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private static final String KEY_ENCRYPT = Constantes.KEY_ENCRYPT;
	@Autowired
	ServiceFactory serviceFactory;
	@Autowired
	private ApplicationContext applicationContext;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private MultitablaRepository multitablaRepository;

	@Autowired
	private SolicitudViaCobroRepository viaCobroRepository;

	@Autowired
	private ViewSolicitudRepository viewSolicitudRepository;

	@Autowired
	private CotizacionRepository cotizacionRepository;

	@Autowired
	private SolicitudRepository solicitudRepository;

	@Autowired
	private PersonaRepository personaRepository;

	@Autowired
	private SolicitudDatosAdicionalesRepository solicitudDatosAdicionalesRepository;

	@Autowired
	private ServicioEdnRepository servicioEdnRepository;

	@Autowired
	private PlanFuturoRepository planFuturoRepository;

	@Autowired
	private SolicitudBeneficiarioRepository solicitudBeneficiarioRepository;

	@Autowired
	private SolicitudProductoRepository solicitudProductoRepository;

	@Autowired
	private SolicitudTraspasoRepository solicitudTraspasoRepository;

	@Autowired
	private SolicitudProductoDetalleRepository solicitudProductoDetalleRepository;

	@Autowired
	private CodigoVerificacionRepository codigoVerificacionRepository;

	@Autowired
	private SolicitudDpsRepositorioRepository solicitudDpsRepositorioRepository;

	@Autowired
	private SolicitudDpsPreguntaRepository solicitudDpsPreguntaRepository;

	@Autowired
	private SolicitudViaCobroRepository solicitudViaCobroRepository;

	@Autowired
	private SitcRepository sitcRepository;

	@Autowired
	private InterseguroRestClient interseguroRestClient;

	@Autowired
	private CotizadorRestClient cotizadorRestClient;

	@Autowired
	private VidaFreeRestClient vidaFreeRestClient;

	@Autowired
	private AcseleRestClient acseleRestClient;

	@Autowired
	private SitcSoapClient sitcSoapClient;

	@Autowired
	private SolicitudPDFService solicitudPDFService;

	// ini JTRAC 4 - FORMATO PEP
	@Value("#{ environment['archivo.formato.pep'] }")
	private String archivoFormatoPep;
	// fin JTRAC 4 - FORMATO PEP

	@Value("#{ environment['archivo.formato.waiver'] }")
	private String archivoFormatoWaiver;

	@Value("#{ environment['persistence.rest.client.global.payment.topic'] }")
	private String topic;

	@Value("#{ environment['persistence.rest.client.global.payment.applicationUser'] }")
	private String applicationUser;

	@Value("#{ environment['archivo.formato.pj'] }")
	private String archivoFormatoPj;

	@Value("#{ environment['archivo.formato.w9.english'] }")
	private String archivoFormatoW9Eng;

	@Value("#{ environment['archivo.formato.w9.espanol'] }")
	private String archivoFormatoW9Esp;

	@Value("#{ environment['persistence.rest.client.global.documento.servidor'] }")
	private String servidorApiDocumento;

	@Value("#{ environment['persistence.rest.client.global.estructura.comercial.findByDocumento'] }")
	private String globalFindByDocumento;

	@Value("#{ environment['persistence.rest.client.cloud-storage.servidor'] }")
	private String cloudStorageServidor;

	@Value("#{ environment['persistence.rest.client.cloud-storage.files'] }")
	private String cloudStorageFiles;

	@Value("#{ environment['persistence.rest.client.esn.servidor'] }")
	private String baseUrl;

	@Value("#{ environment['persistence.rest.client.esn.credential.user'] }")
	private String esnUser;

	@Value("#{ environment['persistence.rest.client.esn.credential.pass'] }")
	private String esnPasword;

	@Value("#{ environment['persistence.rest.client.esn.generarpdf'] }")
	private String esnGenerarPdf;

	@Autowired
	private AdnRepository adnRepository;

	@Autowired
	private CotizaService cotizaService;

	@Autowired
	private AcpPDFService acpPDFService;

	@Autowired
	private SolicitudFiltradoRepository solicitudFiltradoRepository;

	@Autowired
	private GlobalRestClient globalRestClient;

	@Autowired
	private PlaftRestClient plaftRestClient;

	@Value("#{ environment['ruta.pdf.solicitud.publica'] }")
	private String rutaPDFSolicitud;

	@Value("#{ environment['ruta.pdf.solicitud.privada'] }")
	private String rutaPrivadaPDFSolicitud;

	@Value("#{ environment['ruta.pdf.solicitud.privada.sme'] }")
	private String rutaPrivadaPDFSolicitudSME;

	@Value("#{ environment['persistence.rest.client.interseguro.correo.enviarCorreo.solicitud.remitente'] }")
	private String enviarCorreoSolicitudRemitente;

	@Value("#{ environment['persistence.rest.client.interseguro.correo.enviarCorreo.solicitud.remitente.display'] }")
	private String enviarCorreoSolicitudRemitenteDisplay;

	@Value("#{ environment['persistence.rest.client.interseguro.correo.enviarCorreo.solicitud.asunto'] }")
	private String enviarCorreoSolicitudAsunto;

	@Value("#{ environment['persistence.rest.client.interseguro.correo.enviarCorreo.linkpagoprima.asunto'] }")
	private String enviarCorreoLinkPagoPrimaAsunto;

	@Value("#{ environment['persistence.rest.client.interseguro.correo.enviarCorreo.linkpagorecargo.asunto'] }")
	private String enviarCorreoLinkPagoRecargoAsunto;

	@Value("#{ environment['persistence.rest.client.interseguro.correo.enviarCorreo.emision.asunto'] }")
	private String enviarCorreoEmisionAsunto;

	@Value("#{ environment['persistence.rest.client.interseguro.correo.enviarCorreo.solicitud.email.pruebas'] }")
	private String enviarCorreoSolicitudEmailPruebas;

	@Value("#{ environment['persistence.rest.client.sme.generacionpdf.prefijopdf'] }")
	private String smeGenerarPDFPrefijo;

	@Value("#{ environment['url.cotizacion.pdf'] }")
	private String urlCotizacionPDF;

	@Value("#{ environment['ruta.plantilla.doc.linux'] }")
	private String rutaPlantillaDocLinux;

	@Value("#{ environment['ruta.pdf.privada'] }")
	private String rutaPDFPrivada;

	@Value("#{ environment['ruta.pdf.publica'] }")
	private String rutaPDFPublica;

	@Value("#{ environment['ruta.pdf.publica.sme'] }")
	private String rutaPDFPublicaSME;

	@Value("#{ environment['sms.cantidad.codigo'] }")
	private int cantidadSMSCodigo;

	@Value("#{ environment['sms.cantidad.minuto'] }")
	private int cantidadSMSMinuto;

	@Value("#{ environment['server.port'] }")
	private String serverPort;

	@Value("#{ environment['sms.celular.pruebas'] }")
	private String smsCelularPruebas;

	@Value("${spring.profiles.active}")
	private String activeProfile;

	@Value("#{ environment['persistence.rest.client.interseguro.correo.enviarCorreo.remitente'] }")
	private String enviarCorreoRemitente;

	@Value("#{ environment['persistence.rest.client.interseguro.correo.enviarCorreo.remitente.display'] }")
	private String enviarCorreoRemitenteDisplay;

	@Value("#{ environment['persistence.rest.client.interseguro.correo.enviarCorreo.destinatario'] }")
	private String enviarCorreoDestinatario;

	@Value("#{ environment['url.cotizacion.vidafree.pdf'] }")
	private String urlCotizacionVidaFreePDF;

	@Value("#{ environment['url.cotizacion.vidafree'] }")
	private String urlCotizacionVidaFree;

	@Value("#{ environment['persistence.rest.client.culqi.publicKey'] }")
	private String culqiPublicKey;

	@Value("#{ environment['persistence.rest.client.culqi.secretKey'] }")
	private String culqiSecretKey;

	@Value("#{ environment['url.solicitud.link.pago'] }")
	private String urlSolicitudLinkPago;

	@Value("#{ environment['url.solicitud.link.pagoRecargo'] }")
	private String urlSolicitudLinkPagoRecargo;

	@Value("#{ environment['ruta.sme.publica'] }")
	private String rutaSMEPublica;
	@Value("#{ environment['url.linkPago.aplicacion'] }")
	private String aplicacion;

	@Value("#{ environment['url.linkPago.expiracion'] }")
	private int expiracion;

	@Autowired
	private CrmRestClient crmRestClient;

	/*
	 * @Autowired private EventLogRepository logRepository;
	 */

	@Autowired
	private ViewSolicitudPlaftRepository viewSolicitudPlaftRepository;

	@Value("#{ environment['persistence.rest.client.cloud-storage.header.app'] }")
	private String cloudStorageApp;

	@Value("#{ environment['persistence.rest.client.cloud-storage.header.app.value'] }")
	private String cloudStorageAppValue;

	private final Gson gson = new Gson();

	private String getUrlEsnGeneraPdf() {
		return baseUrl + esnGenerarPdf;
	}

	@Override
	public boolean validarNotificacion(IndenovaNotificacionRequestDTO indenovaNotificacionRequestDTO) {

		if (indenovaNotificacionRequestDTO != null) {
			if (indenovaNotificacionRequestDTO.getEventData() != null) {
				String idCircuitoFirma = indenovaNotificacionRequestDTO.getEventData().getCircuitId();
				String eventoFirma = indenovaNotificacionRequestDTO.getEventData().getEventType();

				Solicitud solicitudDomain = new Solicitud();
				solicitudDomain.setIdCircuitoFirma(idCircuitoFirma);

				// Validar Circuito en Solicitud
				solicitudDomain = solicitudRepository.findByIdCircuitoFirma(idCircuitoFirma);

				if (solicitudDomain != null && eventoFirma.equals(Constantes.CODIGO_INDENOVA_EVENTO_FIRMA_FINALIZADO)) {

					solicitudDomain.setFechaFirmaAsegurado(new Date());
					solicitudDomain.setFechaFirmaContratante(new Date());
					solicitudDomain.setEstado(Constantes.CODIGO_SOLICITUD_FIRMA_FINALIZADA);
					solicitudDomain.setUsuarioModif(Constantes.USUARIO_INDENOVA);
					solicitudDomain.setFechaModif(new Date());

					solicitudRepository.save(solicitudDomain);
				}
			}
		}

		return true;
	}

	@Override
	public SolicitudResponseDTO lista(SolicitudFiltroRequestDTO filter) {
		SolicitudResponseDTO response = new SolicitudResponseDTO();
		List<SolicitudItemResponseDTO> lista = new ArrayList<>();

		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<ViewSolicitud> criteria = cb.createQuery(ViewSolicitud.class);
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<ViewSolicitud> root = criteria.from(ViewSolicitud.class);

		cq.select(cb.count(cq.from(ViewSolicitud.class)));

		// Getting the PropertyDescriptors for the object
		PropertyDescriptor[] objDescriptors = PropertyUtils.getPropertyDescriptors(filter);

		// Iterating through each of the PropertyDescriptors
		for (PropertyDescriptor objDescriptor : objDescriptors) {
			try {
				String propertyName = objDescriptor.getName();
				Object propType = PropertyUtils.getPropertyType(filter, propertyName);
				Object propValue = PropertyUtils.getProperty(filter, propertyName);

				LOGGER.info(propertyName + ": " + propType + " - " + propValue);

				if (propValue instanceof java.lang.Long && ((Long) propValue) > 0) {
					criteria.where(cb.equal(root.get(propertyName), propValue));
					cq.where(cb.equal(root.get(propertyName), propValue));
					LOGGER.info("CRITERIA : " + propertyName + "=" + propValue);
				}

				if (propValue instanceof java.lang.String && !((String) propValue).isEmpty()
						&& !((String) propValue).equals("")) {
					criteria.where(cb.equal(root.get(propertyName), propValue));
					cq.where(cb.equal(root.get(propertyName), propValue));
					LOGGER.info("CRITERIA : " + propertyName + "=" + propValue);
				}

				if (propValue instanceof java.lang.Integer && ((int) propValue) > 0) {
					criteria.where(cb.equal(root.get(propertyName), propValue));
					cq.where(cb.equal(root.get(propertyName), propValue));
					LOGGER.info("CRITERIA : " + propertyName + "=" + propValue);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (filter.getPagination() == null)
			filter.setPagination(new PaginationRequestDTO());

		LOGGER.info("AMOUNT PER PAGE: " + filter.getPagination().getAmountPerPage());
		LOGGER.info("PAGE: " + filter.getPagination().getPage());
		LOGGER.info("OFFSET: " + (filter.getPagination().getAmountPerPage() * (filter.getPagination().getPage() - 1)));

		List<ViewSolicitud> solicitudes = ((EntityManager) em).createQuery(criteria)
				.setFirstResult(filter.getPagination().getAmountPerPage() * (filter.getPagination().getPage() - 1)) // offset
				.setMaxResults(filter.getPagination().getAmountPerPage()) // limit
				.getResultList();

		solicitudes.forEach(solicitud -> {

			SolicitudItemResponseDTO item = new SolicitudItemResponseDTO();
			try {
				BeanUtils.copyProperties(item, solicitud);
				lista.add(item);
			} catch (IllegalAccessException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		response.setLista(lista);

		PaginationResponseDTO pagination = new PaginationResponseDTO();
		pagination.setCount(((EntityManager) em).createQuery(cq).getSingleResult());
		pagination.setPage(filter.getPagination().getPage());
		pagination.setAmountPerPage(filter.getPagination().getAmountPerPage());

		response.setPagination(pagination);

		return response;
	}

	@Override
	public SolicitudItemResponseDTO editar(SolicitudRequestDTO solicitud) {
		SolicitudItemResponseDTO response = new SolicitudItemResponseDTO();

		Solicitud model = solicitudRepository.findById(solicitud.getIdSolicitud()).get();

		try {
			BeanUtils.copyProperties(model, solicitud);
			solicitudRepository.save(model);

			ViewSolicitud view = viewSolicitudRepository.findById(solicitud.getIdSolicitud()).get();
			BeanUtils.copyProperties(response, view);

		} catch (IllegalAccessException | InvocationTargetException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}

		return response;
	}

	@Override
	public boolean existeArchivoSolicitud(String numeroCotizacion) {
		boolean flag = false;
		Solicitud model = solicitudRepository.findByNumeroCotizacion(numeroCotizacion);
		if (model != null && model.getNombreArchivoSolicitud() != null && model.getNombreArchivoSolicitud() != "") {
			flag = true;
		} else {
			flag = false;
		}
		return flag;
	}

	@Override
	public ByteArrayInputStream printPDFSolicitud(String numeroCotizacion) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		Solicitud model = solicitudRepository.findByNumeroCotizacion(numeroCotizacion);

		if (model != null && model.getNombreArchivoSolicitud() != null && model.getNombreArchivoSolicitud() != "") {
			File file = new File(rutaPrivadaPDFSolicitud + model.getNombreArchivoSolicitud());
			LOGGER.info("ruta privada" + rutaPrivadaPDFSolicitud + model.getNombreArchivoSolicitud());
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
					// Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				// Auto-generated catch block
				e.printStackTrace();
			}

			return new ByteArrayInputStream(out.toByteArray());
		} else {
			return null;
		}
	}

	@Override
	public ByteArrayInputStream printPDFSolicitud2(String numeroCotizacion, String numeroDocumento) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		String nombreArchivo = "SolicitudDigital_" + numeroDocumento + "_" + numeroCotizacion + ".pdf";
		File file = new File(nombreArchivo);

		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			try {
				for (int readNum; (readNum = fis.read(buf)) != -1;) {
					bos.write(buf, 0, readNum); // no doubt here is 0
					// System.out.println("read " + readNum + " bytes,");
				}
			} catch (IOException ex) {
				// Logger.getLogger(genJpeg.class.getName()).log(Level.SEVERE, null, ex);
			}
			byte[] bytes = bos.toByteArray();

			try {
				out.write(bytes);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}

		return new ByteArrayInputStream(out.toByteArray());
	}

	@Override
	public ByteArrayInputStream printPDFAcp(String numeroCotizacion) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		Solicitud model = solicitudRepository.findByNumeroCotizacion(numeroCotizacion);
		Persona persona = model.getPersonaByIdAsegurado();

		String nombreArchivo = "AcpDigital_" + persona.getNumeroDocumento() + "_" + numeroCotizacion + ".pdf";

		if (model != null && nombreArchivo != null && nombreArchivo != "") {
			File file = new File(rutaPrivadaPDFSolicitud + nombreArchivo);

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

				try {
					out.write(bytes);
				} catch (IOException e) {
					// Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				// Auto-generated catch block
				e.printStackTrace();
			}

			return new ByteArrayInputStream(out.toByteArray());
		} else {
			return null;
		}
	}

	@Override
	public String generateTemp(Long id) throws FileNotFoundException, IOException {

		Solicitud model = solicitudRepository.findById(id).get();

		File file = new File(rutaPDFSolicitud + model.getNombreArchivoSolicitud());

		FileInputStream fis = new FileInputStream(file);
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

		File tmp = File.createTempFile(model.getNombreArchivoSolicitud().replace(".pdf", ""), ".pdf");
		String nombreTemporal = tmp.getName();

		FileOutputStream os = new FileOutputStream(tmp);
		os.write(bytes);
		os.flush();
		os.close();

		return nombreTemporal;
	}

	// JTRAC4 ADN
	public File obtenerPlantilla(String plantilla) {
		File plantillaFile = null;
		String rutaPlantilla = "";
		String systemOp = System.getProperty("os.name");

		if (systemOp.contains("Windows")) {
			rutaPlantilla = Constantes.RUTA_PLANTILLA + "/" + plantilla;

			plantillaFile = new File(getClass().getClassLoader().getResource(rutaPlantilla).getFile());

			LOGGER.info("rutaPlantilla: " + rutaPlantilla);
		} else {
			plantillaFile = new File(this.rutaPlantillaDocLinux + "//" + plantilla);
			LOGGER.info("obtenerPlantilla: " + this.rutaPlantillaDocLinux + "//" + plantilla);
		}

		return plantillaFile;
	}

	@Override
	public boolean enviarCorreo(Long id, String tipoProducto) {

		Solicitud model = solicitudRepository.findById(id).get();
		String traza = Utilitarios.trazaLog() + "-" + model.getNumeroCotizacion();
		LOGGER.info("[" + traza + "] Entro SolicitudServiceImpl.enviarCorreo");
		// System.out.println(gson.toJson(model));
		Adn adn = adnRepository.findByIdPersona(model.getIdAsegurado()); // jtrac4 adn
		// -- Body
		try {

			InputStream stream = new ClassPathResource(
					Constantes.RUTA_PLANTILLA + "/" + Constantes.PLANTILLA_SOLICITUD_CORREO).getInputStream();

			String systemOp = System.getProperty("os.name");
			if (!systemOp.contains("Windows")) {
				stream = new FileInputStream(
						new File(rutaPlantillaDocLinux + "//" + Constantes.PLANTILLA_SOLICITUD_CORREO));
			}

			String bodyHtml = Utilitarios.valorString(StreamUtils.copyToString(stream, Charset.defaultCharset()));

			SimpleDateFormat formatterTimeStamp = new SimpleDateFormat("yyyyMMddHHmmss");
			Date timestamp = new Date(System.currentTimeMillis());

			Persona personaAsegurado = personaRepository.findById(model.getPersonaByIdAsegurado().getIdPersona()).get();
			SolicitudDatosAdicionales solicitudDatosAdicionales = solicitudDatosAdicionalesRepository
					.findByIdSolicitud(id);

			// ini JTRAC 4 - ADN
			String formatoPepFilePublica = "";
			String formatoPjFilePublica = "";
			String formatoW9EngFilePublica = "";
			String formatoW9EspFilePublica = "";
			String formatoWaiverFilePublica = "";
			String formatoPepFilePrivada = "";
			String formatoPjFilePrivada = "";
			String formatoW9EngFilePrivada = "";
			String formatoW9EspFilePrivada = "";
			String formatoWaiverFilePrivada = "";
			/*
			 * archivoFormatoWaiver archivoFormatoW9Esp archivoFormatoW9Eng
			 */

			// Se valida si el cliente pertenece a la base de Plaft
			ObtenerPolizaRequest polizaRequest = new ObtenerPolizaRequest();
			Persona persona = adn.getPersona();
			polizaRequest.setNroDoc(persona.getNumeroDocumento());
			polizaRequest.setTipoDoc(persona.getTipoDocumento());

			ValidarProspectoAsignacionResponse existePlaft = globalRestClient.validarPlaft(polizaRequest);

			String PEP = "PEP";
			String PJ = "PJ";

			LOGGER.info(gson.toJson(existePlaft));
			// formato pj --> archivoFormatoPj
			if (Boolean.parseBoolean(existePlaft.getValidacion())) {
				LOGGER.info("[" + traza + "] Existe en el API PLAFT");
				if (existePlaft.getMensaje().equals(PEP)) {
					LOGGER.info("PEP");
					formatoPepFilePublica = "".concat(model.getNumeroCotizacion()).concat("-")
							.concat(archivoFormatoPep);
					formatoPepFilePrivada = "".concat(model.getNumeroCotizacion()).concat("-")
							.concat(archivoFormatoPep);
					LOGGER.info("enviarCorreo.formatoPepFilePublica: " + formatoPepFilePublica);

					if (model.getIdAsegurado() != null) {
						LOGGER.info("ASEGURADO = " + model.getIdAsegurado());

						if (personaAsegurado.getNacionalidad().equals("41")) {

							LOGGER.info("U");

							formatoW9EngFilePublica = "".concat(model.getNumeroCotizacion()).concat("-")
									.concat(archivoFormatoW9Eng);
							formatoW9EspFilePublica = "".concat(model.getNumeroCotizacion()).concat("-")
									.concat(archivoFormatoW9Esp);
							formatoWaiverFilePublica = "".concat(model.getNumeroCotizacion()).concat("-")
									.concat(archivoFormatoWaiver);
							formatoW9EngFilePrivada = "".concat(model.getNumeroCotizacion()).concat("-")
									.concat(archivoFormatoW9Eng);
							formatoW9EspFilePrivada = "".concat(model.getNumeroCotizacion()).concat("-")
									.concat(archivoFormatoW9Esp);
							formatoWaiverFilePrivada = "".concat(model.getNumeroCotizacion()).concat("-")
									.concat(archivoFormatoWaiver);

							LOGGER.info("enviarCorreo.formatoW9EngFilePublica: " + formatoW9EngFilePublica);
							LOGGER.info("enviarCorreo.formatoW9EspFilePublica: " + formatoW9EspFilePublica);
							LOGGER.info("enviarCorreo.formatoWaiverFilePublica: " + formatoWaiverFilePublica);

						}

					} else if (model.getIdContratante() != null) {
						LOGGER.info("CONTRATANTE = " + model.getIdContratante());

						Persona personaContratanteSol = personaRepository.findById(model.getIdContratante()).get();

						if (personaContratanteSol.getTipoDocumento().equals("11")) {

							LOGGER.info("PJ");

							formatoPjFilePublica = "".concat(model.getNumeroCotizacion()).concat("-")
									.concat(archivoFormatoPj);
							formatoPjFilePrivada = "".concat(model.getNumeroCotizacion()).concat("-")
									.concat(archivoFormatoPj);
							LOGGER.info("enviarCorreo.formatoPjFilePublica: " + formatoPjFilePublica);
						}

					}

				} else if (existePlaft.getMensaje().equals(PJ)) {
					LOGGER.info("PJ");
					formatoPjFilePublica = "".concat(model.getNumeroCotizacion()).concat("-").concat(archivoFormatoPj);
					formatoPjFilePrivada = "".concat(model.getNumeroCotizacion()).concat("-").concat(archivoFormatoPj);
					LOGGER.info("enviarCorreo.formatoPepFilePublica: " + formatoPjFilePublica);

					if (model.getIdAsegurado() != null) {
						LOGGER.info("ASEGURADO = " + model.getIdAsegurado());
						// Persona personaAsegurada =
						// personaRepository.findById(model.getIdAsegurado()).get();

						if (personaAsegurado.getEsPep().equals("1") || (solicitudDatosAdicionales != null
								&& solicitudDatosAdicionales.getEsPepAsegurado().equals("1"))) {
							LOGGER.info("PEP");
							formatoPepFilePublica = "".concat(model.getNumeroCotizacion()).concat("-")
									.concat(archivoFormatoPep);
							formatoPepFilePrivada = "".concat(model.getNumeroCotizacion()).concat("-")
									.concat(archivoFormatoPep);
							LOGGER.info("enviarCorreo.formatoPepFilePublica: " + formatoPepFilePublica);

						}

					} else if (model.getIdContratante() != null) {
						LOGGER.info("CONTRATANTE = " + model.getIdContratante());
						// Persona personaAsegurada =
						// personaRepository.findById(model.getIdContratante()).get();

						Persona personaContratanteSol = personaRepository.findById(model.getIdContratante()).get();

						if (personaContratanteSol.getEsPep().equals("1") || (solicitudDatosAdicionales != null
								&& solicitudDatosAdicionales.getEsPepContratante().equals("1"))) {

							LOGGER.info("PEP");
							formatoPepFilePublica = "".concat(model.getNumeroCotizacion()).concat("-")
									.concat(archivoFormatoPep);
							formatoPepFilePrivada = "".concat(model.getNumeroCotizacion()).concat("-")
									.concat(archivoFormatoPep);
							LOGGER.info("enviarCorreo.formatoPepFilePublica: " + formatoPepFilePublica);

						}

					}

				} else {

					LOGGER.info("[" + traza + "] Se valida si el cliente es PEP en ADN");
					// Se valida si el cliente es PEP en ADN

					// Solicitud solicitud = solicitudRepository.findById(id).get();
					// System.out.println(gson.toJson(solicitud));
					Persona personaAsegurada = personaRepository.findById(model.getIdAsegurado()).get();

					System.out.println(personaAsegurada.getEsPep());

					if (personaAsegurado.getEsPep().equals("1") || (solicitudDatosAdicionales != null
							&& solicitudDatosAdicionales.getEsPepAsegurado().equals("1"))) {

						LOGGER.info("ASEGURADO = " + personaAsegurado.getEsPep());
						formatoPepFilePublica = "".concat(model.getNumeroCotizacion()).concat("-")
								.concat(archivoFormatoPep);
						formatoPepFilePrivada = "".concat(model.getNumeroCotizacion()).concat("-")
								.concat(archivoFormatoPep);
						LOGGER.info("enviarCorreo.formatoPepFilePublica: " + formatoPepFilePublica);
					}

					if (personaAsegurado.getNacionalidad().equals("41")) {

						LOGGER.info("U");
						LOGGER.info(rutaPDFPublica);
						LOGGER.info(model.getNumeroCotizacion());
						LOGGER.info(archivoFormatoW9Eng);
						formatoW9EngFilePublica = "".concat(model.getNumeroCotizacion()).concat("-")
								.concat(archivoFormatoW9Eng);
						formatoW9EspFilePublica = "".concat(model.getNumeroCotizacion()).concat("-")
								.concat(archivoFormatoW9Esp);
						formatoWaiverFilePublica = "".concat(model.getNumeroCotizacion()).concat("-")
								.concat(archivoFormatoWaiver);
						formatoW9EngFilePrivada = "".concat(model.getNumeroCotizacion()).concat("-")
								.concat(archivoFormatoW9Eng);
						formatoW9EspFilePrivada = "".concat(model.getNumeroCotizacion()).concat("-")
								.concat(archivoFormatoW9Esp);
						formatoWaiverFilePrivada = "".concat(model.getNumeroCotizacion()).concat("-")
								.concat(archivoFormatoWaiver);
						// formatoUFilePublica = rutaPDFPublica +
						// "".concat(model.getNumeroCotizacion()).concat("-").concat(archivoFormatoPj);
						// formatoUFilePrivada = rutaPDFPrivada +
						// "".concat(model.getNumeroCotizacion()).concat("-").concat(archivoFormatoPj);
						LOGGER.info("enviarCorreo.formatoW9EngFilePublica: " + formatoW9EngFilePublica);
						LOGGER.info("enviarCorreo.formatoW9EspFilePublica: " + formatoW9EspFilePublica);
						LOGGER.info("enviarCorreo.formatoWaiverFilePublica: " + formatoWaiverFilePublica);

					}

					if (model.getIdContratante() != null) {
						// Persona personaContratante =
						// personaRepository.findById(model.getIdContratante()).get();
						Persona personaContratanteSol = personaRepository.findById(model.getIdContratante()).get();

						if (personaContratanteSol.getTipoDocumento().equals("11")) {
							LOGGER.info("CONTRATANTE pj = " + personaContratanteSol.getTipoDocumento());
							formatoPjFilePublica = "".concat(model.getNumeroCotizacion()).concat("-")
									.concat(archivoFormatoPj);
							formatoPjFilePrivada = "".concat(model.getNumeroCotizacion()).concat("-")
									.concat(archivoFormatoPj);
							LOGGER.info("enviarCorreo.formatoPepFilePublica: " + formatoPjFilePublica);

						}

						if (personaContratanteSol.getEsPep().equals("1") || (solicitudDatosAdicionales != null
								&& solicitudDatosAdicionales.getEsPepContratante().equals("1"))) {
							LOGGER.info("CONTRATANTE pep = " + personaContratanteSol.getEsPep());
							formatoPepFilePublica = "".concat(model.getNumeroCotizacion()).concat("-")
									.concat(archivoFormatoPep);
							formatoPepFilePrivada = "".concat(model.getNumeroCotizacion()).concat("-")
									.concat(archivoFormatoPep);
							LOGGER.info("enviarCorreo.formatoPepFilePublica: " + formatoPepFilePublica);

						}

					}
				}
				// formatoPepFilePublica = "C:\\Users\\NB\\Documents\\interseguro\\" +
				// "".concat(model.getNumeroCotizacion()).concat("-").concat(archivoFormatoPep);
			} else {

				LOGGER.info("Se valida si el cliente es PEP en ADN");
				// Se valida si el cliente es PEP en ADN
				System.out.println(id);
				// Solicitud solicitud = solicitudRepository.findById(id).get();
				// System.out.println(gson.toJson(solicitud));
				// Persona personaAsegurada =
				// personaRepository.findById(model.getIdAsegurado()).get();

				System.out.println(personaAsegurado.getEsPep());

				if (personaAsegurado.getEsPep().equals("1") || (solicitudDatosAdicionales != null
						&& solicitudDatosAdicionales.getEsPepAsegurado().equals("1"))) {

					LOGGER.info("ASEGURADO = " + personaAsegurado.getEsPep());
					formatoPepFilePublica = "".concat(model.getNumeroCotizacion()).concat("-")
							.concat(archivoFormatoPep);
					formatoPepFilePrivada = "".concat(model.getNumeroCotizacion()).concat("-")
							.concat(archivoFormatoPep);
					LOGGER.info("enviarCorreo.formatoPepFilePublica: " + formatoPepFilePublica);
				}

				if (personaAsegurado.getNacionalidad().equals("41")) {

					LOGGER.info("U");
					LOGGER.info(archivoFormatoW9Eng);
					LOGGER.info(archivoFormatoW9Esp);
					LOGGER.info(archivoFormatoWaiver);
					LOGGER.info(rutaPDFPublica);
					formatoW9EngFilePublica = "".concat(model.getNumeroCotizacion()).concat("-")
							.concat(archivoFormatoW9Eng);
					formatoW9EspFilePublica = "".concat(model.getNumeroCotizacion()).concat("-")
							.concat(archivoFormatoW9Esp);
					formatoWaiverFilePublica = "".concat(model.getNumeroCotizacion()).concat("-")
							.concat(archivoFormatoWaiver);
					formatoW9EngFilePrivada = "".concat(model.getNumeroCotizacion()).concat("-")
							.concat(archivoFormatoW9Eng);
					formatoW9EspFilePrivada = "".concat(model.getNumeroCotizacion()).concat("-")
							.concat(archivoFormatoW9Esp);
					formatoWaiverFilePrivada = "".concat(model.getNumeroCotizacion()).concat("-")
							.concat(archivoFormatoWaiver);
					// formatoUFilePublica = rutaPDFPublica +
					// "".concat(model.getNumeroCotizacion()).concat("-").concat(archivoFormatoPj);
					// formatoUFilePrivada = rutaPDFPrivada +
					// "".concat(model.getNumeroCotizacion()).concat("-").concat(archivoFormatoPj);
					LOGGER.info("enviarCorreo.formatoW9EngFilePublica: " + formatoW9EngFilePublica);
					LOGGER.info("enviarCorreo.formatoW9EspFilePublica: " + formatoW9EspFilePublica);
					LOGGER.info("enviarCorreo.formatoWaiverFilePublica: " + formatoWaiverFilePublica);

				}

				if (model.getIdContratante() != null) {
					// Persona personaContratante =
					// personaRepository.findById(model.getIdContratante()).get();
					Persona personaContratanteSol = personaRepository.findById(model.getIdContratante()).get();

					if (personaContratanteSol.getTipoDocumento().equals("11")) {
						LOGGER.info("CONTRATANTE pj = " + personaContratanteSol.getTipoDocumento());
						formatoPjFilePublica = "".concat(model.getNumeroCotizacion()).concat("-")
								.concat(archivoFormatoPj);
						formatoPjFilePrivada = "".concat(model.getNumeroCotizacion()).concat("-")
								.concat(archivoFormatoPj);
						LOGGER.info("enviarCorreo.formatoPepFilePublica: " + formatoPjFilePublica);

					}

					if (personaContratanteSol.getEsPep().equals("1") || (solicitudDatosAdicionales != null
							&& solicitudDatosAdicionales.getEsPepContratante().equals("1"))) {
						LOGGER.info("CONTRATANTE pep = " + personaContratanteSol.getEsPep());
						formatoPepFilePublica = "".concat(model.getNumeroCotizacion()).concat("-")
								.concat(archivoFormatoPep);
						formatoPepFilePrivada = "".concat(model.getNumeroCotizacion()).concat("-")
								.concat(archivoFormatoPep);
						LOGGER.info("enviarCorreo.formatoPepFilePublica: " + formatoPepFilePublica);

					}

				}

			}

			// fin JTRAC 4 - ADN

			String url = "";

			String cotizacionFileName = "".concat("cotizacion-").concat(model.getNumeroCotizacion()).concat("-")
					.concat(formatterTimeStamp.format(timestamp)).concat(".pdf");

			String cotizacionFilePrivada = "";
			String cotizacionFilePublica = "";

			LOGGER.info("[" + traza + "] enviarCorreo.cotizacionFilePrivada: " + cotizacionFilePrivada);
			// -- EnvÃ­o de la cotizaciÃ³n a
			// //ti-is-admin//TMP_PDF

			File tmpFileCotizacion = new File(cotizacionFileName);
			String templateFile = null;
			List<Adjuntos> files = new ArrayList<>();
			Adjuntos adjunto;
			// Adjuntos adjunto2 = new Adjuntos();

			/*
			 * ObtenerUploadArchivoResponse globalArchivoResponse =
			 * globalRestClient.obtenerArchivo( personaAsegurado.getTipoDocumento(),
			 * personaAsegurado.getNumeroDocumento(), model.getNumeroCotizacion());
			 *
			 * for (ArchivoResponse item : globalArchivoResponse.getFiles()) {
			 * if(item.getName().contains("SolicitudDigital_" +
			 * personaAsegurado.getNumeroDocumento() + "_" + model.getNumeroCotizacion())) {
			 * UrlResponse globalUrlArchivoResponse = globalRestClient.obtenerUrlArchivo(
			 * globalArchivoResponse.getContactId(), personaAsegurado.getNumeroDocumento(),
			 * item.getName());
			 *
			 * String urlArchivo = globalUrlArchivoResponse.getUrl();
			 *
			 * templateFile = downloadAndConvertToBase64(urlArchivo, traza);
			 *
			 * adjunto.setName(item.getName()); adjunto.setContent(templateFile);
			 *
			 * files.add(adjunto); } }
			 */

			if ("VidaFree".equals(tipoProducto)) {
				LOGGER.info(model.getNumeroCotizacion());
				LOGGER.info("[" + traza + "] Tipo producto VidaFree ");
				url = urlCotizacionVidaFreePDF.concat(model.getNumeroCotizacion());

				try {
					LOGGER.info("[" + traza + "] COPIADO COTIZACION");

					tmpFileCotizacion = generarPDFCotizacionVidaFree(model.getNumeroPropuesta());
					cotizacionFilePrivada = "" + cotizacionFileName;
					cotizacionFilePublica = "" + cotizacionFileName;
					LOGGER.info(cotizacionFilePrivada);
					LOGGER.info(cotizacionFilePublica);

					templateFile = convertirBase64(cotizacionFileName, tmpFileCotizacion, traza);

					LOGGER.info("[" + traza + "] Obtenemos el string de arrays: " + templateFile);
					adjunto = new Adjuntos();
					adjunto.setName(cotizacionFileName);
					adjunto.setContent(templateFile);

					files.add(adjunto);

				} catch (Exception ex) {
					LOGGER.info("[" + traza + "] ERROR AL COPIAR PDF COTIZACION EN TEMPORALES" + ex);
					// cotizacionFilePublica = Strings.EMPTY;
					// cotizacionFilePrivada = Strings.EMPTY;
				}

			} else {
				/*
				 * url =
				 * urlCotizacionPDF.concat("&fecot=").concat(formatter.format(date)).concat(
				 * "&aseg=") .concat(personaAsegurado.getNombres().replaceAll("\\s",
				 * "+")).concat("&idCot=") .concat(model.getNumeroCotizacion()).concat("&user=")
				 * .concat(model.getAgenteNombres().replaceAll("\\s", "+"));
				 */

				cotizacionFilePrivada = "" + cotizacionFileName;
				cotizacionFilePublica = "" + cotizacionFileName;
				LOGGER.info(url);

				LOGGER.info("enviarCorreo.url: " + url);
				LOGGER.info("COPIANDO A TEMPORALES");
				LOGGER.info("url: " + url);
				LOGGER.info("propuesta: " + model.getNumeroPropuesta());
				LOGGER.info("cotizacionFilePrivada: " + cotizacionFilePrivada);
				LOGGER.info("**************");

				try {
					LOGGER.info("COPIADO COTIZACION");
					tmpFileCotizacion = generarPDFCotizacionVida(model.getNumeroPropuesta(), model.getAgenteNombres(),
							personaAsegurado.getNombres());
					/*
					 * tmpFileCotizacion = new File(System.getProperty("java.io.tmpdir") +
					 * File.separator + new File(cotizacionFileName)); FileUtils.copyURLToFile(new
					 * URL(url), tmpFileCotizacion);
					 */
					templateFile = convertirBase64(cotizacionFileName, tmpFileCotizacion, traza);
					adjunto = new Adjuntos();

					adjunto.setName(cotizacionFileName);
					adjunto.setContent(templateFile);

					files.add(adjunto);

				} catch (Exception ex) {
					LOGGER.info("ERROR AL COPIAR PDF COTIZACION EN TEMPORALES +", ex);
					cotizacionFilePublica = Strings.EMPTY;
					// cotizacionFilePrivada = Strings.EMPTY;
				}

			}

			// ini JTRAC 4 - ADN
			try {
				LOGGER.info("formatoPepFilePublica: " + formatoPepFilePublica);

				if (!formatoPepFilePrivada.equals(Strings.EMPTY)) {
					LOGGER.info("Entra a copiar el formato pep en la ruta publica");

					File ffpep = this.obtenerPlantilla(Constantes.PLANTILLA_FORMATO_PEP);

					if (ffpep.exists()) {
						LOGGER.info("plantillaFormatoPep: " + ffpep.getPath());
						LOGGER.info("plantillaFormatoPep existe!");
					}

					File tmpFileCotizacionPep = new File(formatoPepFilePrivada);

					tmpFileCotizacionPep = new File(
							System.getProperty("java.io.tmpdir") + "/" + new File(formatoPepFilePrivada));
					FileUtils.copyFile(ffpep, tmpFileCotizacionPep);

					templateFile = convertirBase64(model.getNumeroCotizacion().concat("-").concat(archivoFormatoPep),
							tmpFileCotizacionPep, traza);
					adjunto = new Adjuntos();

					adjunto.setName(model.getNumeroCotizacion().concat("-").concat(archivoFormatoPep));
					adjunto.setContent(templateFile);

					files.add(adjunto);

					if (tmpFileCotizacionPep.exists()) {
						LOGGER.info("formatoPepFilePublica copiado con exito!");
					}

					LOGGER.info("**Ingresa al metodo enviarPDFCRM2-Pep para enviar archivo a la nube**");
					this.enviarPDFCRM2(tmpFileCotizacionPep, personaAsegurado.getTipoDocumento(),
							personaAsegurado.getNumeroDocumento(),
							model.getNumeroCotizacion().concat("-").concat(archivoFormatoPep),
							model.getNumeroCotizacion().concat("-").concat(archivoFormatoPep),
							personaAsegurado.getTipoDocumento());

					LOGGER.info("Finaliza la subida del formato pep en la nube");
				}
				if (!formatoPjFilePrivada.equals(Strings.EMPTY)) {
					LOGGER.info("Entra a copiar el formato pep en la ruta publica");

					File ffpj = this.obtenerPlantilla(Constantes.PLANTILLA_FORMATO_PJ);

					if (ffpj.exists()) {
						LOGGER.info("plantillaFormatoPj: " + ffpj.getPath());
						LOGGER.info("plantillaFormatoPj existe!");
					}

					File tmpFileCotizacionPj = new File(formatoPjFilePrivada);

					tmpFileCotizacionPj = new File(
							System.getProperty("java.io.tmpdir") + "/" + new File(formatoPjFilePrivada));
					FileUtils.copyFile(ffpj, tmpFileCotizacionPj);

					templateFile = convertirBase64(model.getNumeroCotizacion().concat("-").concat(archivoFormatoPj),
							tmpFileCotizacionPj, traza);
					adjunto = new Adjuntos();

					adjunto.setName(model.getNumeroCotizacion().concat("-").concat(archivoFormatoPj));
					adjunto.setContent(templateFile);

					files.add(adjunto);

					if (tmpFileCotizacionPj.exists()) {
						LOGGER.info("formatoPepFilePublica copiado con exito!");
					}

					LOGGER.info("**Ingresa al metodo enviarPDFCRM2-Pj para enviar archivo a la nube**");
					this.enviarPDFCRM2(tmpFileCotizacionPj, personaAsegurado.getTipoDocumento(),
							personaAsegurado.getNumeroDocumento(),
							model.getNumeroCotizacion().concat("-").concat(archivoFormatoPj),
							model.getNumeroCotizacion().concat("-").concat(archivoFormatoPj),
							personaAsegurado.getTipoDocumento());

					LOGGER.info("Finaliza la subida del formato pj en la nube");
				}
				if (!formatoWaiverFilePrivada.equals(Strings.EMPTY)) {
					LOGGER.info("Entra a copiar el formato pep en la ruta publica");

					File ffw = this.obtenerPlantilla(Constantes.PLANTILLA_FORMATO_WAIVER);

					if (ffw.exists()) {
						LOGGER.info("plantillaFormatoPj: " + ffw.getPath());
						LOGGER.info("plantillaFormatoPj existe!");
					}

					File tmpFileCotizacionWaiver = new File(formatoWaiverFilePrivada);
					tmpFileCotizacionWaiver = new File(
							System.getProperty("java.io.tmpdir") + "/" + new File(formatoWaiverFilePrivada));
					FileUtils.copyFile(ffw, tmpFileCotizacionWaiver);

					templateFile = convertirBase64(model.getNumeroCotizacion().concat("-").concat(archivoFormatoWaiver),
							tmpFileCotizacionWaiver, traza);
					adjunto = new Adjuntos();

					adjunto.setName(model.getNumeroCotizacion().concat("-").concat(archivoFormatoWaiver));
					adjunto.setContent(templateFile);

					files.add(adjunto);

					if (tmpFileCotizacionWaiver.exists()) {
						LOGGER.info("formatoW9EngFilePrivada copiado con exito!");
					}

					LOGGER.info("**Ingresa al metodo enviarPDFCRM2-Waiver para enviar archivo a la nube**");
					this.enviarPDFCRM2(tmpFileCotizacionWaiver, personaAsegurado.getTipoDocumento(),
							personaAsegurado.getNumeroDocumento(),
							model.getNumeroCotizacion().concat("-").concat(archivoFormatoWaiver),
							model.getNumeroCotizacion().concat("-").concat(archivoFormatoWaiver),
							personaAsegurado.getTipoDocumento());

					LOGGER.info("Finaliza la subida del formato waiver en la nube");
				}
				if (!formatoW9EspFilePrivada.equals(Strings.EMPTY)) {
					LOGGER.info("Entra a copiar el formato pep en la ruta publica");

					File ffw9es = this.obtenerPlantilla(Constantes.PLANTILLA_FORMATO_W9ES);

					if (ffw9es.exists()) {
						LOGGER.info("plantillaFormatoPj: " + ffw9es.getPath());
						LOGGER.info("plantillaFormatoPj existe!");
					}

					File tmpFileCotizacionW9Esp = new File(formatoW9EspFilePrivada);

					tmpFileCotizacionW9Esp = new File(
							System.getProperty("java.io.tmpdir") + "/" + new File(formatoW9EspFilePrivada));
					FileUtils.copyFile(ffw9es, tmpFileCotizacionW9Esp);

					templateFile = convertirBase64(model.getNumeroCotizacion().concat("-").concat(archivoFormatoW9Esp),
							tmpFileCotizacionW9Esp, traza);
					adjunto = new Adjuntos();

					adjunto.setName(model.getNumeroCotizacion().concat("-").concat(archivoFormatoW9Esp));
					adjunto.setContent(templateFile);

					files.add(adjunto);

					if (tmpFileCotizacionW9Esp.exists()) {
						LOGGER.info("formatoW9EngFilePrivada copiado con exito!");
					}

					LOGGER.info("**Ingresa al metodo enviarPDFCRM2-W9Esp para enviar archivo a la nube**");
					this.enviarPDFCRM2(tmpFileCotizacionW9Esp, personaAsegurado.getTipoDocumento(),
							personaAsegurado.getNumeroDocumento(),
							model.getNumeroCotizacion().concat("-").concat(archivoFormatoW9Esp),
							model.getNumeroCotizacion().concat("-").concat(archivoFormatoW9Esp),
							personaAsegurado.getTipoDocumento());

					LOGGER.info("Finaliza la subida del formato W9Esp en la nube");
				}
				if (!formatoW9EngFilePrivada.equals(Strings.EMPTY)) {
					LOGGER.info("Entra a copiar el formato pep en la ruta publica");

					File ffw9en = this.obtenerPlantilla(Constantes.PLANTILLA_FORMATO_W9EN);

					if (ffw9en.exists()) {
						LOGGER.info("plantillaFormatoPj: " + ffw9en.getPath());
						LOGGER.info("plantillaFormatoPj existe!");
					}

					File tmpFileCotizacionW9Eng = new File(formatoW9EngFilePrivada);

					tmpFileCotizacionW9Eng = new File(
							System.getProperty("java.io.tmpdir") + "/" + new File(formatoW9EngFilePrivada));
					FileUtils.copyFile(ffw9en, tmpFileCotizacionW9Eng);

					templateFile = convertirBase64(model.getNumeroCotizacion().concat("-").concat(archivoFormatoW9Eng),
							tmpFileCotizacionW9Eng, traza);
					adjunto = new Adjuntos();

					adjunto.setName(model.getNumeroCotizacion().concat("-").concat(archivoFormatoW9Eng));
					adjunto.setContent(templateFile);

					files.add(adjunto);

					if (tmpFileCotizacionW9Eng.exists()) {
						LOGGER.info("formatoW9EngFilePrivada copiado con exito!");
					}

					LOGGER.info("**Ingresa al metodo enviarPDFCRM2-W9Eng para enviar archivo a la nube**");
					this.enviarPDFCRM2(tmpFileCotizacionW9Eng, personaAsegurado.getTipoDocumento(),
							personaAsegurado.getNumeroDocumento(),
							model.getNumeroCotizacion().concat("-").concat(archivoFormatoW9Eng),
							model.getNumeroCotizacion().concat("-").concat(archivoFormatoW9Eng),
							personaAsegurado.getTipoDocumento());

					LOGGER.info("Finaliza la subida del formato W9Eng en la nube");
				}

			} catch (Exception ex) {
				LOGGER.info("ERROR AL COPIAR PDF FORMATO PEP EN TEMPORALES + ", ex);
				formatoPepFilePrivada = "";
			}
			// fin JTRAC 4 - ADN
			LOGGER.info("cotizacionFilePublica: " + cotizacionFilePublica);

			// INICIO - ADJUNTAR EL PDF DE LA COTIZACION AL CRM
			String nombreArchivoSolicitud = "Cotizacion_" + model.getNumeroCotizacion() + "_"
					+ formatterTimeStamp.format(timestamp) + ".pdf";

			String tipoDocumentoCRM = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_TIPO_DOCUMENTO,
					personaAsegurado.getTipoDocumento(), Constantes.MULTITABLA_COLUMNA_VALOR_CRM);
			String numeroDocumento = personaAsegurado.getNumeroDocumento();
			String tipoDocumento = personaAsegurado.getTipoDocumento();

			// model.setDocumentoSolicitud(nombreArchivoSolicitud);

			LOGGER.info("[" + traza + "] Antes de enviarPDFCRM2 -----");
			this.enviarPDFCRM2(tmpFileCotizacion, tipoDocumentoCRM, numeroDocumento, nombreArchivoSolicitud,
					nombreArchivoSolicitud, tipoDocumento);
			LOGGER.info("[" + traza + "] Despues de enviarPDFCRM2 -----");

			Thread.sleep(2000);

			ObtenerUploadArchivoResponse globalArchivoResponse = globalRestClient.obtenerArchivo(
					personaAsegurado.getTipoDocumento(), personaAsegurado.getNumeroDocumento(),
					model.getNumeroCotizacion());

			for (ArchivoResponse item : globalArchivoResponse.getFiles()) {
				LOGGER.info(item.getName());

				if (item.getName().contains("SolicitudDigital_" + personaAsegurado.getNumeroDocumento() + "_"
						+ model.getNumeroCotizacion())) {
					UrlResponse globalUrlArchivoResponse = globalRestClient.obtenerUrlArchivo(
							globalArchivoResponse.getContactId(), personaAsegurado.getNumeroDocumento(),
							item.getName());

					String urlArchivo = globalUrlArchivoResponse.getUrl();

					templateFile = downloadAndConvertToBase64(urlArchivo, traza);
					adjunto = new Adjuntos();

					adjunto.setName(item.getName());
					adjunto.setContent(templateFile);

					files.add(adjunto);
				}
			}

			// LOGGER.info("rutaPDFSolicitud: " + rutaPDFSolicitud);
			// fin JTRAC 4 - ADN

			String nombres = Utilitarios.nombresCompletos(personaAsegurado.getNombres(),
					personaAsegurado.getApellidoPaterno(), personaAsegurado.getApellidoMaterno(),
					personaAsegurado.getRazonSocial());
			String bodyAsegurado = bodyHtml;
			bodyAsegurado = bodyAsegurado.replaceAll("\\{nombre\\}", nombres);

			// validacion si el cliente es PEP,PJ

			if (!formatoPepFilePublica.equals(Strings.EMPTY) && !formatoPjFilePublica.equals(Strings.EMPTY)
					&& !formatoWaiverFilePublica.equals(Strings.EMPTY)) {
				bodyAsegurado = bodyAsegurado.replaceAll("\\{plaft\\}", Constantes.MENSAJE_SOLICITUD_EMPTY_PLAFT);
			} else if (!formatoPepFilePublica.equals(Strings.EMPTY) && !formatoPjFilePublica.equals(Strings.EMPTY)) {
				bodyAsegurado = bodyAsegurado.replaceAll("\\{plaft\\}", Constantes.MENSAJE_SOLICITUD_EMPTY_PLAFT);
			} else if (!formatoWaiverFilePublica.equals(Strings.EMPTY) && !formatoPjFilePublica.equals(Strings.EMPTY)) {
				bodyAsegurado = bodyAsegurado.replaceAll("\\{plaft\\}", Constantes.MENSAJE_SOLICITUD_EMPTY_PLAFT);
			} else if (!formatoPepFilePublica.equals(Strings.EMPTY)
					&& !formatoWaiverFilePublica.equals(Strings.EMPTY)) {
				bodyAsegurado = bodyAsegurado.replaceAll("\\{plaft\\}", Constantes.MENSAJE_SOLICITUD_EMPTY_PLAFT);
			} else if (!formatoPepFilePublica.equals(Strings.EMPTY)) {
				bodyAsegurado = bodyAsegurado.replaceAll("\\{plaft\\}", Constantes.MENSAJE_SOLICITUD_EMPTY_PLAFT);
			} else if (!formatoPjFilePublica.equals(Strings.EMPTY)) {
				// files.add(formatoPjFilePublica);
				bodyAsegurado = bodyAsegurado.replaceAll("\\{plaft\\}", Constantes.MENSAJE_SOLICITUD_EMPTY_PLAFT);
			} else if (!formatoWaiverFilePublica.equals(Strings.EMPTY)) {
				// files.add(formatoPjFilePublica)
				bodyAsegurado = bodyAsegurado.replaceAll("\\{plaft\\}", Constantes.MENSAJE_SOLICITUD_EMPTY_PLAFT);
			} else {
				bodyAsegurado = bodyAsegurado.replaceAll("\\{plaft\\}", "");
			}

			LOGGER.info("[" + traza + "] files: " + gson.toJson(files));
			LOGGER.info("[" + traza + "] Entra a enviar correo");
			LOGGER.info("[GAAAAAAAA] => " + gson.toJson(files));
			System.out.println("PRUEBACORREO =>" + gson.toJson(files));
			enviarCorreoConAdjunto(this.enviarCorreoSolicitudAsunto, personaAsegurado.getCorreo(), bodyAsegurado, files, traza, "SOLICITUD" ,model.getNumeroCotizacion() , personaAsegurado.getNumeroDocumento());
			LOGGER.info("[" + traza + "] Salió de enviar correo");

			if (model.getPersonaByIdContratante() != null
					&& !model.getPersonaByIdContratante().equals(model.getPersonaByIdAsegurado())) {
				Persona personaContratante = personaRepository
						.findById(model.getPersonaByIdContratante().getIdPersona()).get();

				if (personaContratante.getCorreo() != null && !personaContratante.getCorreo().isEmpty()) {
					nombres = Utilitarios.nombresCompletos(personaContratante.getNombres(),
							personaContratante.getApellidoPaterno(), personaContratante.getApellidoMaterno(),
							personaContratante.getRazonSocial());
					String bodyContratante = bodyHtml;
					bodyContratante = bodyContratante.replaceAll("\\{nombre\\}", nombres);
					enviarCorreoConAdjunto(this.enviarCorreoSolicitudAsunto, personaContratante.getCorreo(), bodyContratante, files, traza, "SOLICITUD" ,model.getNumeroCotizacion() , personaContratante.getNumeroDocumento());
				}
			}

			// Enviar Correo al agente
			LOGGER.info("[" + traza + "] Entra a enviar correo al Agente");
			if (!Utilitarios.valorString(model.getAgenteCorreo()).equals(Strings.EMPTY)) {
				enviarCorreoConAdjunto(this.enviarCorreoSolicitudAsunto, model.getAgenteCorreo(), bodyAsegurado, files, traza, "SOLICITUD" ,model.getNumeroCotizacion() , personaAsegurado.getNumeroDocumento());
			}
			LOGGER.info("[" + traza + "] Salió de enviar correo al Agente");

		} catch (Exception ex) {
			LOGGER.info("error.enviarCorreo.getMessage=>" + ex.getMessage());
			LOGGER.info("error.enviarCorreo=>" + ex);
			return false;
		}

		return true;

	}

	public String downloadAndConvertToBase64(String urlStr, String traza) {
		String base64Content = null;
		try {
			URL url = new URL(urlStr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			InputStream inputStream = connection.getInputStream();
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int bytesRead;
			byte[] data = new byte[1024];
			while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, bytesRead);
			}

			byte[] documentBytes = buffer.toByteArray();
			base64Content = new String(Base64.encodeBase64(documentBytes));

			LOGGER.info("[" + traza + "] Documento en Base64: " + base64Content);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return base64Content;
	}

	public String convertirBase64(String cotizacionFileName, File tmpFileCotizacion, String traza) throws IOException {
		LOGGER.info("[" + traza + "] Entro a convertir Base64");
		String tempPDFFullPath = null;
		byte[] pdfData = null;
		if (null != tmpFileCotizacion) {
			LOGGER.info("EncodeBase64");
			pdfData = Base64.encodeBase64(convertFileToBytes(tmpFileCotizacion));
			LOGGER.info("Se convirtio en parametros de bytes: " + pdfData);
			String tmpCotizacionPdf = System.getProperty("java.io.tmpdir") + File.separator;
			tempPDFFullPath = new StringBuilder(tmpCotizacionPdf).append(cotizacionFileName).toString();
			LOGGER.info(tempPDFFullPath);
			try (OutputStream stream2 = new FileOutputStream(tempPDFFullPath)) {
				stream2.write(pdfData);
				LOGGER.info("[" + traza + "] Se escribio el PDF");
			} catch (Exception ex) {
				LOGGER.info(ex.getMessage());
			}

		}
		return new String(pdfData);
	}

	public byte[] convertFileToBytes(File filePath) throws IOException {
		LOGGER.info("FilePath: " + filePath.getPath());
		Path path = Paths.get(filePath.getPath());
		return Files.readAllBytes(path);
	}

	private void enviarPDFCRM(String urlPDFPublico, String tipoDocumentoCRM, String numeroDocumento,
			String nombreArchivoPDF, String descripcionArchivo, String tipoDocumentoInput) {
		try {
			if (!urlPDFPublico.equals(Strings.EMPTY)) {
				UploadArchivoRequest requestArchivo = new UploadArchivoRequest();
				requestArchivo.setTipoDocumento(tipoDocumentoCRM);
				requestArchivo.setNumeroDocumento(numeroDocumento);
				requestArchivo.setRutaArchivo(urlPDFPublico); // urlPDFPublico
				requestArchivo.setNombreArchivo(nombreArchivoPDF); // nombreArchivoPDF
				requestArchivo.setDescripcionArchivo(descripcionArchivo);
				requestArchivo.setTipoDocumentoAdn(tipoDocumentoInput);
				// LOGGER.info("error.enviarCorreo=>" + ex);
				UploadArchivoResponse responseUploadCRM = globalRestClient.uploadArchivo(requestArchivo);

				Gson gson = new Gson();

				LOGGER.info("enviarPDFCRM.requestArchivo=>" + gson.toJson(requestArchivo));
				LOGGER.info("enviarPDFCRM.responseUploadCRM=>" + gson.toJson(responseUploadCRM));
			}
		} catch (Exception e) {
			LOGGER.info("Error enviar PDF Solicitud a CRM=>" + e);
			e.printStackTrace();
		}
	}

	@Override
	public String valorMultiTabla(String codigoTabla, String codigo, String campo) {
		AtomicReference<String> result = new AtomicReference<>();
		result.set("");
		if (StringUtils.isBlank(codigo)) {
			return result.get();
		}
		List<Multitabla> multitableList = multitablaRepository.findByCodigoTablaSinEstado(codigoTabla);
		if (!multitableList.isEmpty()) {
			multitableList.stream().filter(fm -> StringUtils.equals(codigo, fm.getCodigo())).forEach(m -> {
				switch (campo) {
				case Constantes.MULTITABLA_COLUMNA_VALOR_AUXILIAR:
					result.set(m.getValorAux());
					break;
				case Constantes.MULTITABLA_COLUMNA_VALOR_CRM:
					result.set(m.getValorCrm());
					break;
				default:
					result.set(m.getValor());
					break;
				}
			});
		}

		return result.get();
	}

	public File generarPDFCotizacionVidaFree(String nroCotizacion) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		File adn = new File("");
		try {
			out = cotizaService.generarPDFCotizacionVidaFree(nroCotizacion);
			String formatoCotizacionFilePrivada = "";
			String nombreArchivoTemporal = "cotizacion";
			Date fecha = new Date();
			formatoCotizacionFilePrivada = "".concat(nombreArchivoTemporal).concat("_").concat(nroCotizacion)
					.concat("_").concat(DateUtil.dateToString(fecha, DateUtil.FORMATO_DIA_DDMMYYYYHHMMSS) + ".pdf");
			adn = new File(formatoCotizacionFilePrivada);
			File docOut = new File("docOut");
			FileUtils.writeByteArrayToFile(docOut, out.toByteArray());
			FileUtils.copyFile(docOut, adn);
		} catch (Exception e) {
			LOGGER.info("Error GenerarPDFADN =>" + e);
		}
		return adn;
	}

	public TipoCambioResponseDTO obtenerTipoCambioAcsele() {
		TipoCambioResponseDTO response = new TipoCambioResponseDTO();
		try {
			// List<pe.interseguro.siv.common.persistence.db.acsele.bean.TipoCambio> data =
			// acseleRepository.obtenerTipoCambio();
			LOGGER.info("***** llama consultarTasaDeCambio: *****");
			TasaCambioResponse data = globalRestClient.consultarTasaDeCambio();
			LOGGER.info("responde consultarTasaDeCambio: " + gson.toJson(data.getRespuesta()));
			List<ConversionResponseDTO> conversiones = new ArrayList<>();
			for (pe.interseguro.siv.common.persistence.db.acsele.bean.TipoCambio val : data.getRespuesta()) {
				conversiones.add(new ConversionResponseDTO(val.getMonedaOrigen(), val.getMonedaDestino(),
						val.getFecha(), val.getConversion()));
			}
			response.setCodigoRespuesta("01");
			response.setMensajeRespuesta("Ok");
			response.setConversiones(conversiones);
		} catch (Exception e) {
			response.setCodigoRespuesta("99");
			response.setMensajeRespuesta(e.getMessage());
			response.setObjErrorResource(new ErrorResourceDTO("ACS-002", e.getMessage()));
		}
		return response;
	}

	public File generarPDFCotizacionVida(String nroCotizacion, String agente, String asegurado) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		File adn = new File("");
		try {
			out = cotizaService.generarPDFCotizacionVida(nroCotizacion, agente, asegurado);
			String formatoCotizacionFilePrivada = "";
			String nombreArchivoTemporal = "cotizacion";
			Date fecha = new Date();
			formatoCotizacionFilePrivada = "".concat(nombreArchivoTemporal).concat("_").concat(nroCotizacion)
					.concat("_").concat(DateUtil.dateToString(fecha, DateUtil.FORMATO_DIA_DDMMYYYYHHMMSS) + ".pdf");
			adn = new File(formatoCotizacionFilePrivada);
			File docOut = new File("docOut");
			FileUtils.writeByteArrayToFile(docOut, out.toByteArray());
			FileUtils.copyFile(docOut, adn);
		} catch (Exception e) {
			LOGGER.info("Error GenerarPDFADN =>" + e);
		}
		return adn;
	}

	public SolicitudFormularioResponseDTO obtenerDatosFormulario(Long idSolicitud, Long numeroCotizacion,
			CotizaDetalleResponseDTO cotizacionDetalle, String idUsuario, String device, String os) {
		String traza = Utilitarios.trazaLog() + "-" + numeroCotizacion;
		LOGGER.info("[" + traza
				+ "] Entro SolicitudServiceImpl#obtenerDatosFormulario(idSolicitud,numeroCotizacion,cotizacionDetalle)");
		SolicitudFormularioResponseDTO response = new SolicitudFormularioResponseDTO();
		String codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_EXITO;
		String mensajeRespuesta = Strings.EMPTY;
		List<String> observaciones = new ArrayList<>();
		try {
			LOGGER.info("[" + traza + "] CotizaDetalleResponseDTO ==> " + gson.toJson(cotizacionDetalle));
			observaciones.add("CotizaDetalleResponseDTO: " + gson.toJson(cotizacionDetalle));
			if (cotizacionDetalle != null && cotizacionDetalle.getNroCotizacion() != null
					&& !cotizacionDetalle.getNroCotizacion().isEmpty()) {
				String tirGarantizada = cotizacionDetalle.getTirGarantizada();
				observaciones.add("TIR: " + tirGarantizada);
				LOGGER.info("[" + traza + "] Buscando datos de solicitud...");
				Solicitud solicitud = solicitudRepository.findById(idSolicitud).get();
				LOGGER.info("[" + traza + "] Solicitud encontrada...");
				observaciones.add("Tiene solicitud iniciada...");
				if (solicitud != null) {
					LOGGER.info("[" + traza + "] Seteando datos de solicitud...");
					response.setIdSolicitud(solicitud.getIdSolicitud());
					response.setNumeroCotizacion(cotizacionDetalle.getNroCotizacion());
					response.setMoneda(this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_MONEDA,
							solicitud.getMoneda(), Constantes.MULTITABLA_COLUMNA_VALOR));
					response.setMonedaSimbolo(this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_MONEDA,
							solicitud.getMoneda(), Constantes.MULTITABLA_COLUMNA_VALOR_CRM));
					response.setAseguradoIgualContratante(
							Utilitarios.valorString(solicitud.getAseguradoIgualContratante()));
					response.setVinculoAsegurado(Utilitarios.valorString(solicitud.getVinculoAsegurado()));
					response.setConsentimientoAsegurado(
							Utilitarios.valorString(solicitud.getTratamientoAseguradoCheck()));
					response.setConsentimientoContratante(
							Utilitarios.valorString(solicitud.getTratamientoContratanteCheck()));
					response.setEstadoSolicitud(solicitud.getEstado());
					response.setSubPlanCotizacionCodigo(solicitud.getSubplan());
					LOGGER.info("-------exclusionCobertura------");
					LOGGER.info(solicitud.getFlagExclusionCobAcc());
					String exclusionCobertura = (solicitud.getFlagExclusionCobAcc() == null) ? "2"
							: solicitud.getFlagExclusionCobAcc();
					LOGGER.info(exclusionCobertura);
					response.setFlagExclusionCobAcc(exclusionCobertura);
					LOGGER.info("[" + traza + "] Seteando fecha de firmas de solicitud...");
					if (solicitud.getFechaFirmaAsegurado() != null) {
						response.setFechaFirmaAsegurado(DateUtil.dateToString(solicitud.getFechaFirmaAsegurado(),
								DateUtil.FORMATO_DIA_DDMMYYYY_HHMMSS));
					}
					if (solicitud.getFechaFirmaContratante() != null) {
						response.setFechaFirmaContratante(DateUtil.dateToString(solicitud.getFechaFirmaContratante(),
								DateUtil.FORMATO_DIA_DDMMYYYY_HHMMSS));
					}
					LOGGER.info("[" + traza + "] Buscando datos de asegurado...");
					Persona personaAsegurado = personaRepository.findById(solicitud.getIdAsegurado()).get();
					SolicitudDatosAdicionales solicitudDatosAdicionales = solicitudDatosAdicionalesRepository
							.findByIdSolicitud(idSolicitud);
					LOGGER.info("[" + traza + "] Nacionalidad = " + personaAsegurado.getNacionalidad());
					LOGGER.info("[" + traza + "] Asegurado encontrado...");
					if (personaAsegurado != null) {
						// ASEGURADO
						response.setAsegurado(new SolicitudFormularioAseguradoResponseDTO());
						ObtenerPolizaRequest request = new ObtenerPolizaRequest();
						request.setNroDoc(personaAsegurado.getNumeroDocumento());
						request.setTipoDoc(personaAsegurado.getTipoDocumento());

						ValidarProspectoAsignacionResponse existePlaft = globalRestClient.validarPlaft(request);
						LOGGER.info("[" + traza + "] Plaft Validación: " + existePlaft.getValidacion());
						LOGGER.info("[" + traza + "] Plaft Mensaje: " + existePlaft.getMensaje());
						response.getAsegurado().setExistePlaft(existePlaft.getMensaje());

						System.out.println("estbannnnnnnnn");
						System.out.println(personaAsegurado.getTipoDocumento());
						System.out.println(personaAsegurado.getNumeroDocumento());
						System.out.println(existePlaft.getMensaje());
						//
						response.getAsegurado().setNombres(personaAsegurado.getNombres());
						response.getAsegurado().setApellidoPaterno(personaAsegurado.getApellidoPaterno());
						response.getAsegurado().setApellidoMaterno(personaAsegurado.getApellidoMaterno());
						response.getAsegurado().setTipoDocumento(this.valorMultiTabla(
								Constantes.MULTITABLA_CODIGO_TABLA_TIPO_DOCUMENTO, personaAsegurado.getTipoDocumento(),
								Constantes.MULTITABLA_COLUMNA_VALOR_AUXILIAR).toUpperCase());
						response.getAsegurado().setNumeroDocumento(personaAsegurado.getNumeroDocumento());
						response.getAsegurado().setFechaNacimiento(DateUtil
								.dateToString(personaAsegurado.getFechaNacimiento(), DateUtil.FORMATO_DIA_DDMMYYYY));
						response.getAsegurado()
								.setGenero(this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_GENERO,
										personaAsegurado.getGenero(), Constantes.MULTITABLA_COLUMNA_VALOR));
						response.getAsegurado()
								.setEstadoCivil(Utilitarios.valorString(personaAsegurado.getEstadoCivil()));
						response.getAsegurado()
								.setNacionalidad(Utilitarios.valorString(personaAsegurado.getNacionalidad()));
						response.getAsegurado()
								.setDireccionTipo(Utilitarios.valorString(personaAsegurado.getDireccionTipo()));
						response.getAsegurado()
								.setDireccionNombreVia(Utilitarios.valorString(personaAsegurado.getDireccionTipoDes()));
						response.getAsegurado()
								.setDireccionNroMz(Utilitarios.valorString(personaAsegurado.getDireccionNroMz()));
						response.getAsegurado()
								.setDireccionInterior(Utilitarios.valorString(personaAsegurado.getDireccionInterior()));
						response.getAsegurado().setDireccionUrbanizacion(
								Utilitarios.valorString(personaAsegurado.getDireccionUrbanizacion()));
						response.getAsegurado()
								.setDireccionDepartamento(Utilitarios.valorString(personaAsegurado.getDepartamento()));
						response.getAsegurado()
								.setDireccionProvincia(Utilitarios.valorString(personaAsegurado.getProvincia()));
						response.getAsegurado()
								.setDireccionDistrito(Utilitarios.valorString(personaAsegurado.getDistrito()));
						response.getAsegurado()
								.setCentroTrabajo(Utilitarios.valorString(personaAsegurado.getCentroTrabajo()));
						response.getAsegurado()
								.setActividadEconomica(solicitudDatosAdicionales != null
										? Utilitarios.valorString(
												solicitudDatosAdicionales.getActividadEconomicaAsegurado() != null
														? solicitudDatosAdicionales.getActividadEconomicaAsegurado()
														: personaAsegurado.getActividadEconomica())
										: Utilitarios.valorString(personaAsegurado.getActividadEconomica()));
						response.getAsegurado().setLugarTrabajo(solicitudDatosAdicionales != null
								? Utilitarios.valorString(solicitudDatosAdicionales.getLugarTrabajoAsegurado() != null
										? solicitudDatosAdicionales.getLugarTrabajoAsegurado()
										: personaAsegurado.getLugarTrabajo())
								: Utilitarios.valorString(personaAsegurado.getLugarTrabajo()));
						response.getAsegurado()
								.setIngresoMoneda(Utilitarios.valorString(personaAsegurado.getIngresoMoneda()));
						response.getAsegurado()
								.setIngresoValor(Utilitarios.valorString(personaAsegurado.getIngresoValor()));
						response.getAsegurado().setProfesion(solicitudDatosAdicionales != null
								? Utilitarios.valorString(solicitudDatosAdicionales.getProfesionAsegurado() != null
										? solicitudDatosAdicionales.getProfesionAsegurado()
										: personaAsegurado.getProfesion())
								: Utilitarios.valorString(personaAsegurado.getProfesion()));
						response.getAsegurado()
								.setProfesionDetalle(Utilitarios.valorString(personaAsegurado.getProfesionDetalle()));
						response.getAsegurado()
								.setEsPEP(solicitudDatosAdicionales != null
										? Utilitarios.valorString(solicitudDatosAdicionales.getEsPepAsegurado() != null
												? solicitudDatosAdicionales.getEsPepAsegurado()
												: personaAsegurado.getEsPep())
										: Utilitarios.valorString(personaAsegurado.getEsPep()));
						response.getAsegurado()
								.setEsSujetoObligado(
										solicitudDatosAdicionales != null
												? Utilitarios.valorString(
														solicitudDatosAdicionales.getEsSujetoObligadoAsegurado() != null
																? solicitudDatosAdicionales
																		.getEsSujetoObligadoAsegurado()
																: personaAsegurado.getEsSujetoObligado())
												: Utilitarios.valorString(personaAsegurado.getEsSujetoObligado()));
						response.getAsegurado().setCelular(Utilitarios.valorString(personaAsegurado.getCelular()));
						LOGGER.info("[" + traza + "] Datos de asegurado seteados...");
						// COTIZACION
						SolicitudProducto solicitudProducto = solicitudProductoRepository
								.findByIdSolicitud(idSolicitud);
						LOGGER.info("[" + traza + "] Obteniendo datos de producto...");
						if (solicitudProducto != null) {
							LOGGER.info("[" + traza + "] Datos de producto encontrado...");
							response.setCotizacion(new SolicitudFormularioCotizacionResponseDTO());
							if ("1".equals(solicitudProducto.getTipoProducto())) {
								response.getCotizacion().setProducto(
										this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_SUBPLAN_COTIZADOR,
												solicitud.getSubplan(), Constantes.MULTITABLA_COLUMNA_VALOR));
							} else {
								response.getCotizacion()
										.setProducto(this.valorMultiTabla(
												Constantes.MULTITABLA_CODIGO_SUBPLAN_COTIZADOR_VIDA_FREE,
												solicitud.getSubplan(), Constantes.MULTITABLA_COLUMNA_VALOR));
							}
							response.getCotizacion().setPlazoCobertura(
									Utilitarios.valorString(solicitudProducto.getPeriodoCoberturaAnual()));
							response.getCotizacion()
									.setMontoPrima(Utilitarios.formatoMiles(
											Double.parseDouble(solicitudProducto.getPrimaComercialAnual().toString()),
											Utilitarios.FORMATO_MILES_CON_DECIMAL));
							response.getCotizacion()
									.setFrecuenciaPago(this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_FRECUENCIA,
											solicitudProducto.getFrecuencia(), Constantes.MULTITABLA_COLUMNA_VALOR));
							response.getCotizacion()
									.setFondoGarantizado(Utilitarios.formatoMiles(
											Double.parseDouble(solicitudProducto.getMontoFondoGarantizado().toString()),
											Utilitarios.FORMATO_MILES_CON_DECIMAL));
							response.getCotizacion().setTasaGarantizada(tirGarantizada);
							response.getCotizacion().setPagoBeneficios(solicitudProducto.getAnualidadPago().toString());
							response.getCotizacion().setNumeroCotizacion(solicitud.getNumeroCotizacion());
							LOGGER.info("[" + traza + "] Obteniendo datos de detalle de producto...");
							List<SolicitudProductoDetalle> listaProductoDetalle = solicitudProductoDetalleRepository
									.findByIdSolicitud(idSolicitud);
							if (listaProductoDetalle != null && !listaProductoDetalle.isEmpty()) {
								List<SolicitudFormularioCoberturaResponseDTO> listaCoberturas = new ArrayList<>();
								String nombre = "";
								for (SolicitudProductoDetalle detalle : listaProductoDetalle) {
									String idCobertura = detalle.getCobertura();
									if ("1".equals(solicitudProducto.getTipoProducto())) {
										nombre = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_COBERTURAS,
												detalle.getCobertura(), Constantes.MULTITABLA_COLUMNA_VALOR);
									} else {
										nombre = this.valorMultiTabla(
												Constantes.MULTITABLA_CODIGO_TABLA_COBERTURAS_VIDA_FREE,
												detalle.getCobertura(), Constantes.MULTITABLA_COLUMNA_VALOR);
									}

									String tipoCobertura = detalle.getTipoCobertura();
									Float capitalAsegurado = detalle.getCapitalAsegurado();
									Float primaAnual = detalle.getPrimaAnual();

									SolicitudFormularioCoberturaResponseDTO cobertura = new SolicitudFormularioCoberturaResponseDTO(
											idCobertura, tipoCobertura, nombre, capitalAsegurado, primaAnual);
									listaCoberturas.add(cobertura);
								}

								response.getCotizacion().setCoberturas(listaCoberturas);
							}
							LOGGER.info("[" + traza + "] Obteniendo datos de beneficiario...");

							List<SolicitudBeneficiario> listaBeneficiario = solicitudBeneficiarioRepository
									.findByIdSolicitud(idSolicitud);
							if (listaBeneficiario != null && !listaBeneficiario.isEmpty()) {
								List<SolicitudFormularioBeneficiarioResponseDTO> listaBeneficiarioResponse = new ArrayList<>();
								for (SolicitudBeneficiario beneficiario : listaBeneficiario) {
									String tipoBeneficiario = Utilitarios
											.valorString(beneficiario.getTipoBeneficiario());
									String nombres = Utilitarios.valorString(beneficiario.getNombres());
									String apellidoPaterno = Utilitarios.valorString(beneficiario.getApellidoPaterno());
									String apellidoMaterno = Utilitarios.valorString(beneficiario.getApellidoMaterno());
									Date fechaNacimientoOrigen = beneficiario.getFechaNacimiento();
									String fechaNacimiento = DateUtil.dateToString(fechaNacimientoOrigen,
											DateUtil.FORMATO_DIA_DDMMYYYY);
									String tipoDocumento = Utilitarios.valorString(beneficiario.getTipoDocumento());
									String numeroDocumento = Utilitarios.valorString(beneficiario.getNumeroDocumento());
									String tipoRelacion = Utilitarios.valorString(beneficiario.getTipoRelacion());
									Integer distribucion = 0;
									if (beneficiario.getDistribucion() != null)
										distribucion = beneficiario.getDistribucion();

									SolicitudFormularioBeneficiarioResponseDTO responseBen = new SolicitudFormularioBeneficiarioResponseDTO(
											tipoBeneficiario, nombres, apellidoPaterno, apellidoMaterno,
											fechaNacimiento, tipoDocumento, numeroDocumento, tipoRelacion,
											distribucion);
									listaBeneficiarioResponse.add(responseBen);
								}

								response.setBeneficiarios(listaBeneficiarioResponse);
							}
							LOGGER.info("[" + traza + "] Obteniendo datos de DPS...");

							SolicitudDps solicitudDps = solicitudDpsRepositorioRepository
									.findByIdSolicitud(idSolicitud);
							if (solicitudDps != null) {
								response.setDps(new SolicitudFormularioDPSResponseDTO());

								response.getDps().setEstatura(Strings.EMPTY);
								response.getDps().setPeso(Strings.EMPTY);

								if (solicitudDps.getEstatura() != null) {
									response.getDps()
											.setEstatura(String.valueOf(Math.round(solicitudDps.getEstatura())));
								}
								if (solicitudDps.getPeso() != null) {
									response.getDps().setPeso(String.valueOf(Math.round(solicitudDps.getPeso())));
								}

								response.getDps().setImc(Utilitarios.valorString(solicitudDps.getImc()));
								response.getDps()
										.setPesoVariacion(Utilitarios.valorString(solicitudDps.getPesoVariacion()));
								response.getDps().setPesoAumentoDisminuyo(
										Utilitarios.valorStringBoolean(solicitudDps.getPesoAumentoDisminuyo()));
								response.getDps()
										.setPesoCantidad(Utilitarios.valorString(solicitudDps.getPesoAdCantidad()));
								response.getDps().setPesoMotivo(Utilitarios.valorString(solicitudDps.getPesoMotivo()));
								response.getDps().setFumador(Utilitarios.valorStringBoolean(solicitudDps.getFumador()));
								response.getDps()
										.setFumadorCantidad(Utilitarios.valorString(solicitudDps.getFumadorCantidad()));
								response.getDps().setFumadorFrecuencia(
										Utilitarios.valorString(solicitudDps.getFumadorFrecuencia()));
								response.getDps().setDrogas(Utilitarios.valorStringBoolean(solicitudDps.getDrogas()));
								response.getDps()
										.setDrogasFecha(Utilitarios.valorString(solicitudDps.getDrogasFecha()));
								response.getDps().setAlcohol(Utilitarios.valorStringBoolean(solicitudDps.getAlcohol()));
								response.getDps()
										.setAlcoholCantidad(Utilitarios.valorString(solicitudDps.getAlcoholCantidad()));
								response.getDps().setAlcoholFrecuencia(
										Utilitarios.valorString(solicitudDps.getAlcoholFrecuencia()));

								LOGGER.info("[" + traza + "] Obteniendo datos de preguntas DPS...");
								List<SolicitudDpsPregunta> listaPreguntas = solicitudDpsPreguntaRepository
										.findByIdSolicitud(idSolicitud);
								if (listaPreguntas != null && !listaPreguntas.isEmpty()) {
									List<SolicitudFormularioDPSPreguntaResponseDTO> listaPreguntasResponse = new ArrayList<>();
									for (SolicitudDpsPregunta preguntaItem : listaPreguntas) {
										String item = Utilitarios.valorString(preguntaItem.getItem());
										String pregunta = Utilitarios.valorString(preguntaItem.getPregunta());
										String respuesta = Utilitarios.valorStringBoolean(preguntaItem.getRespuesta());
										String detalle = Utilitarios.valorString(preguntaItem.getDetalle());
										String enfermedad = Utilitarios.valorString(preguntaItem.getEnfermedad());
										String fechaDiagnostico = Utilitarios
												.valorString(preguntaItem.getFechaDiagnostico());
										String condicionActual = Utilitarios
												.valorString(preguntaItem.getCondicionActual());
										String nombreMedicoHospital = Utilitarios
												.valorString(preguntaItem.getNombreMedicoHospital());

										SolicitudFormularioDPSPreguntaResponseDTO preguntaResponse = new SolicitudFormularioDPSPreguntaResponseDTO(
												item, pregunta, respuesta, detalle, enfermedad, fechaDiagnostico,
												condicionActual, nombreMedicoHospital);

										listaPreguntasResponse.add(preguntaResponse);
									}

									response.getDps().setPreguntas(listaPreguntasResponse);
								}
							}

							// Contratante

							// GTI.23645
							if (solicitud.getIdContratante() != null
									&& solicitud.getEstado().equals(Constantes.CODIGO_SOLICITUD_POR_TRANSMITIR)) {
								LOGGER.info("[" + traza + "] Obteniendo datos de contratante...");
								Persona personaContratante = personaRepository.findById(solicitud.getIdContratante())
										.get();
								if (personaContratante != null) {
									response.setContratante(new SolicitudFormularioContratanteResponseDTO());

									response.getContratante().setNombres(personaContratante.getNombres());
									response.getContratante()
											.setApellidoPaterno(personaContratante.getApellidoPaterno());
									response.getContratante()
											.setApellidoMaterno(personaContratante.getApellidoMaterno());
									response.getContratante().setRazonSocial(personaContratante.getRazonSocial());
									response.getContratante().setTipoDocumento(personaContratante.getTipoDocumento());
									response.getContratante()
											.setNumeroDocumento(personaContratante.getNumeroDocumento());
									Date fechaNacimientoOrigen = personaContratante.getFechaNacimiento();
									// if(fechaNacimientoOrigen != null) {
									// fechaNacimientoOrigen.setHours(6);//GTI.24117
									// }
									String fechaNacimiento = DateUtil.dateToString(fechaNacimientoOrigen,
											DateUtil.FORMATO_DIA_DDMMYYYY);
									response.getContratante().setFechaNacimiento(fechaNacimiento);
									response.getContratante().setGenero(personaContratante.getGenero());
									response.getContratante().setEstadoCivil(
											Utilitarios.valorString(personaContratante.getEstadoCivil()));
									response.getContratante().setNacionalidad(
											Utilitarios.valorString(personaContratante.getNacionalidad()));
									response.getContratante().setDireccionTipo(
											Utilitarios.valorString(personaContratante.getDireccionTipo()));
									response.getContratante().setDireccionNombreVia(
											Utilitarios.valorString(personaContratante.getDireccionTipoDes()));
									response.getContratante().setDireccionNroMz(
											Utilitarios.valorString(personaContratante.getDireccionNroMz()));
									response.getContratante().setDireccionInterior(
											Utilitarios.valorString(personaContratante.getDireccionInterior()));
									response.getContratante().setDireccionUrbanizacion(
											Utilitarios.valorString(personaContratante.getDireccionUrbanizacion()));
									response.getContratante().setDireccionDepartamento(
											Utilitarios.valorString(personaContratante.getDepartamento()));
									response.getContratante().setDireccionProvincia(
											Utilitarios.valorString(personaContratante.getProvincia()));
									response.getContratante().setDireccionDistrito(
											Utilitarios.valorString(personaContratante.getDistrito()));
									response.getContratante().setCentroTrabajo(
											Utilitarios.valorString(personaContratante.getCentroTrabajo()));
									response.getContratante().setActividadEconomica(solicitudDatosAdicionales != null
											? Utilitarios.valorString(
													solicitudDatosAdicionales.getActividadEconomicaContratante() != null
															? solicitudDatosAdicionales
																	.getActividadEconomicaContratante()
															: personaContratante.getActividadEconomica())
											: Utilitarios.valorString(personaContratante.getActividadEconomica()));
									response.getContratante().setLugarTrabajo(solicitudDatosAdicionales != null
											? Utilitarios.valorString(
													solicitudDatosAdicionales.getLugarTrabajoContratante() != null
															? solicitudDatosAdicionales.getLugarTrabajoContratante()
															: personaContratante.getLugarTrabajo())
											: Utilitarios.valorString(personaContratante.getLugarTrabajo()));
									response.getContratante().setIngresoMoneda(
											Utilitarios.valorString(personaContratante.getIngresoMoneda()));
									response.getContratante().setIngresoValor(
											Utilitarios.valorString(personaContratante.getIngresoValor()));
									response.getContratante()
											.setProfesion(solicitudDatosAdicionales != null
													? Utilitarios.valorString(
															solicitudDatosAdicionales.getProfesionContratante() != null
																	? solicitudDatosAdicionales
																			.getProfesionContratante()
																	: personaContratante.getProfesion())
													: Utilitarios.valorString(personaContratante.getProfesion()));
									response.getContratante().setProfesionDetalle(
											Utilitarios.valorString(personaContratante.getProfesionDetalle()));
									response.getContratante()
											.setEsPEP(solicitudDatosAdicionales != null
													? Utilitarios.valorString(
															solicitudDatosAdicionales.getEsPepContratante() != null
																	? solicitudDatosAdicionales.getEsPepContratante()
																	: personaContratante.getEsPep())
													: Utilitarios.valorString(personaContratante.getEsPep()));
									response.getContratante().setEsSujetoObligado(solicitudDatosAdicionales != null
											? Utilitarios.valorString(
													solicitudDatosAdicionales.getEsSujetoObligadoContratante() != null
															? solicitudDatosAdicionales.getEsSujetoObligadoContratante()
															: personaContratante.getEsSujetoObligado())
											: Utilitarios.valorString(personaContratante.getEsSujetoObligado()));
									response.getContratante()
											.setCelular(Utilitarios.valorString(personaContratante.getCelular()));
									response.getContratante()
											.setCorreo(Utilitarios.valorString(personaContratante.getCorreo()));

								}
							} else {
								if (solicitud.getJsonContratante() != null
										&& !solicitud.getJsonContratante().equals(Strings.EMPTY)
										&& solicitud.getAseguradoIgualContratante() != null
										&& !solicitud.getAseguradoIgualContratante()
												.equals(Constantes.SOLICITUD_ASEGURADO_IGUAL_CONTRATANTE_TRUE)) {
									String jsonContratante = solicitud.getJsonContratante();
									SolicitudFormularioContratanteResponseDTO contratanteResponse = gson
											.fromJson(jsonContratante, SolicitudFormularioContratanteResponseDTO.class);

									response.setContratante(contratanteResponse);
								}
							}

						} else {
							LOGGER.error("[" + traza + "] Solicitud Producto no encontrado");
							observaciones.add("Solicitud Producto no encontrado...");
							throw new SivSOAException("Solicitud Producto no encontrado", null);
						}
					} else {
						LOGGER.error("[" + traza + "] Asegurado no encontrado...");
						observaciones.add("Asegurado no encontrado...");
						throw new SivSOAException("Asegurado no encontrado", null);
					}
				} else {
					LOGGER.error("[" + traza + "] Solicitud no encontrada...");
					observaciones.add("Solicitud no encontrado...");
					throw new SivSOAException("Solicitud no encontrada", null);
				}
			} else {
				LOGGER.error("[" + traza + "] Cotización no encontrada...");
				observaciones.add("Cotizacion no encontrado...");
				throw new SivSOAException("Cotización no encontrada", null);
			}

		} catch (Exception ex) {
			LOGGER.error("[" + traza + "] Error obtenerDatosFormulario ==> " + ex.getMessage(), ex);
			mensajeRespuesta = Utilitarios.obtenerMensaje(messageSource,
					Constantes.MENSAJE_SOLICITUD_ERROR_OBTENER_DATOS_FORMULARIO);
			codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_ERROR;
			response.setObjErrorResource(new ErrorResourceDTO("ADN-006", ex.getMessage()));
		}

		response.setCodigoRespuesta(codigoRespuesta);
		response.setMensajeRespuesta(mensajeRespuesta);

		LOGGER.info("[" + traza + "] SolicitudFormularioResponseDTO ==> " + gson.toJson(response));

		LOGGER.info("[" + traza
				+ "] Salio SolicitudServiceImpl#obtenerDatosFormulario(idSolicitud,numeroCotizacion,cotizacionDetalle)");
		return response;
	}

	private void enviarPDFCRM2(File fileTmp, String tipoDocumentoCRM, String numeroDocumento, String nombreArchivoPDF,
			String descripcionArchivo, String tipoDocumentoInput) {
		try {
			if (fileTmp.exists()) {
				UploadArchivoRequest requestArchivo = new UploadArchivoRequest();
				requestArchivo.setTipoDocumento(tipoDocumentoCRM);
				requestArchivo.setNumeroDocumento(numeroDocumento);
				requestArchivo.setFile(fileTmp);
				requestArchivo.setNombreArchivo(nombreArchivoPDF);
				requestArchivo.setDescripcionArchivo(descripcionArchivo);
				requestArchivo.setTipoDocumentoAdn(tipoDocumentoInput);
				UploadArchivoResponse responseUploadCRM = globalRestClient.uploadArchivo(requestArchivo);
				LOGGER.info("enviarPDFCRM.requestArchivo=>{}", gson.toJson(requestArchivo));
				LOGGER.info("enviarPDFCRM.responseUploadCRM=>{}", gson.toJson(responseUploadCRM));
			}
		} catch (Exception e) {
			LOGGER.error("Error enviar PDF Solicitud a CRM=>", e);
		}
	}

	@Async
	private void enviarCorreoConAdjunto(String asunto, String email, String body, List<Adjuntos> files, String traza,String motivo, String cotizacion, String documento) {
		LOGGER.info("LINK-PAGO ==> Entro a SolicitudController#enviarCorreoConAdjunto()");
		StringBuilder sbAsunto = new StringBuilder();
		EnviarCorreoRequestNuevo enviarCorreoRequest = new EnviarCorreoRequestNuevo();
		enviarCorreoRequest.setTitle("Encuesta de clientes");
		String[] correoSeparado = { email };
		if (enviarCorreoSolicitudEmailPruebas != null) {
			correoSeparado = enviarCorreoSolicitudEmailPruebas.split(",");
		}
		List<Remitente> listaRemitente = new ArrayList<>();
		Remitente remitente;

		if (!Constantes.SERVER_PORT_PRODUCCION.equals(serverPort)) {
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
			LOGGER.info("LINK-PAGO ==> Destinatario ----> " + remitente);
		}
		LOGGER.info("LINK-PAGO ==> Puerto ----> " + serverPort + email);
		enviarCorreoRequest.setTo(listaRemitente);
		// sbAsunto.append(this.enviarCorreoSolicitudAsunto);
		sbAsunto.append(asunto);

		enviarCorreoRequest.setSubject(sbAsunto.toString());
		enviarCorreoRequest.setHtmlContent(body);
		enviarCorreoRequest.setAttachments(files);

		LOGGER.info("[" + traza + "] EnviarCorreoRequest=> " + gson.toJson(enviarCorreoRequest));

		try {
			interseguroRestClient.enviarCorreo(enviarCorreoRequest,  motivo,  cotizacion,  documento);
		} catch (Exception ex) {
			LOGGER.error("interseguroRestClient.enviarCorreo: " + email + " - " + ex.getMessage(), ex);
		}

		LOGGER.info("LINK-PAGO ==> Salio SolicitudController#enviarCorreoConAdjunto()");
	}

	private void sendSMS(Long idSolicitud, Persona persona, String usuarioLogin) {
		LOGGER.info("===============");

		Long idPersona = persona.getIdPersona();
		// CodigoVerificacion cv =
		// codigoVerificacionRepository.findCodigoActivo(idSolicitud, idPersona);

		CodigoVerificacion cv = null;
		List<CodigoVerificacion> listaCv = codigoVerificacionRepository.findCodigoActivo(idSolicitud, idPersona);

		if (listaCv != null && listaCv.size() > 0) {
			cv = listaCv.get(0);
		}

		String codigo = "";
		String codigoAnt = "";
		if (cv != null && cv.getIdCodigoVerificacion() != null) {
			codigoAnt = cv.getCodigo();

			LOGGER.info("Codigo codigoAnt DES: " + this.decrypt(codigoAnt));

			cv.setActivo(0);
			cv.setUsuarioModif(usuarioLogin);
			cv.setFechaModif(new Date());

			codigoVerificacionRepository.save(cv);
		}

		do {
			codigo = this.encrypt(this.randomAlphaNumeric(this.cantidadSMSCodigo));
		} while (codigoAnt == codigo);

		LOGGER.info("Enviando SMS:");
		LOGGER.info("ID Solicitud: " + idSolicitud);
		LOGGER.info("ID Persona: " + idPersona);
		LOGGER.info("Número Documento: " + persona.getNumeroDocumento());
		LOGGER.info("Codigo: " + codigo);
		LOGGER.info("Codigo DES: " + this.decrypt(codigo));

		Calendar fechaExpiracion = Calendar.getInstance();
		fechaExpiracion.add(Calendar.MINUTE, this.cantidadSMSMinuto);

		CodigoVerificacion codigoVerificacion = new CodigoVerificacion();
		codigoVerificacion.setIdSolicitud(idSolicitud);
		codigoVerificacion.setIdPersona(idPersona);
		codigoVerificacion.setCodigo(codigo);
		codigoVerificacion.setCelular((long) persona.getCelular());
		codigoVerificacion.setUsuarioCrea(usuarioLogin);
		codigoVerificacion.setFechaCrea(new Date());
		codigoVerificacion.setFechaExpiracion(fechaExpiracion.getTime());
		codigoVerificacionRepository.save(codigoVerificacion);

		// send SMS
		EnviarSmsRequest request = new EnviarSmsRequest();
		StringBuilder sbSMS = new StringBuilder();
		if (!serverPort.equalsIgnoreCase(Constantes.SERVER_PORT_PRODUCCION)) {
			request.setCelular(smsCelularPruebas);
			sbSMS.append("TEST:");
		} else {
			request.setCelular(persona.getCelular().toString());
		}

		sbSMS.append(Utilitarios.obtenerMensaje(messageSource,
				new Object[] { Utilitarios.nombresCompletos(persona.getNombres(), persona.getApellidoPaterno(),
						persona.getApellidoMaterno(), persona.getRazonSocial()), this.decrypt(codigo) },
				Constantes.MENSAJE_SOLICITUD_SMS));
		request.setMensaje(sbSMS.toString());

		EnviarSmsResponse response = null;

		try {
			response = interseguroRestClient.enviarSms(request);

			LOGGER.info("Response SMS: " + gson.toJson(response));

			if (response.getEstado().equals("1")) {
				codigoVerificacion.setEnviado(1);
				codigoVerificacionRepository.save(codigoVerificacion);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		LOGGER.info("===============");

	}

	private String randomAlphaNumeric(int count) {

		StringBuilder builder = new StringBuilder();

		while (count-- != 0) {
			int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}

		return builder.toString();

	}

	private String encrypt(String text) {

		String strData = "";

		try {
			SecretKeySpec skeyspec = new SecretKeySpec(KEY_ENCRYPT.getBytes(), "Blowfish");
			Cipher cipher = Cipher.getInstance("Blowfish");
			cipher.init(Cipher.ENCRYPT_MODE, skeyspec);
			byte[] encrypted = cipher.doFinal(text.getBytes());

			// strData = new String(encrypted);
			// strData = Base64.encodeBase64(encrypted).toString();
			strData = Base64.encodeBase64String(encrypted);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return strData;
	}

	private String decrypt(String encrypted) {
		LOGGER.info("Logger decrypt [INI]");
		String strData = "";
		byte[] encryptedBytes = Base64.decodeBase64(encrypted);

		try {
			LOGGER.info("Logger decrypt [TRY]");
			SecretKeySpec skeyspec = new SecretKeySpec(KEY_ENCRYPT.getBytes(), "Blowfish");
			Cipher cipher = Cipher.getInstance("Blowfish");
			cipher.init(Cipher.DECRYPT_MODE, skeyspec);
			byte[] decrypted = cipher.doFinal(encryptedBytes);
			strData = new String(decrypted);
			LOGGER.info("Logger decrypt [RESULTADO]" + strData);
		} catch (Exception e) {
			LOGGER.info("Logger decrypt [CATCH]" + e.getMessage());
			e.printStackTrace();
		}
		LOGGER.info("Logger decrypt [FIN]");
		return strData;

	}

	@Override
	public TokenLinkPagoResponseDTO decryptLinkToken(String token) {
		LocalDateTime ldt = LocalDateTime.now();
		ZonedDateTime zdt = ldt.atZone(ZoneId.of("America/Los_Angeles"));
		String traza = String.valueOf(zdt.toInstant().toEpochMilli());
		LOGGER.info("[" + traza + "] Entra a SolicitudServiceImpl.decryptLinkToken");
		TokenLinkPagoResponseDTO response = new TokenLinkPagoResponseDTO();
		PagoTokenBean bean = null;
		LOGGER.info("[" + traza + "] Desencriptando token::" + token);
		ObjectMapper mapper = new ObjectMapper();

		String json = this.decrypt(token);
		LOGGER.info("[" + traza + "] Trama obtenida::" + json);

		try {
			bean = mapper.readValue(json, PagoTokenBean.class);
			String tokenExpiry = bean.getTime();
			LocalDateTime localDateTime = LocalDateTime.now();
			Solicitud solicitud = solicitudRepository.findByNumeroCotizacion(bean.getNumeroPropuesta());
			String afiliacion = (solicitud.getIdPagoCulqi() != null || solicitud.getIdPagoNiubiz() != null) ? "1" : "0";
			String estadoSolicitud = Constantes.CODIGO_SOLICITUD_ZERO;
			try {
				GetEstadoCotizacionRequest request0 = new GetEstadoCotizacionRequest();
				request0.setCotizacionId(solicitud.getIdCrmCotizador());

				if (solicitud.getEstadoGeneral().equals(Constantes.ENTREGADO_A_GDC)) {
					estadoSolicitud = solicitud.getEstadoGeneral();
				} else if (solicitud.getEstadoGeneral().equals(Constantes.TRANSMITIDA)) {
					estadoSolicitud = solicitud.getEstadoGeneral();
				}

			} catch (Exception e) {
				LOGGER.error("[" + traza + "] Error al intentar obtener estado de CRM.");
				e.printStackTrace();
			}

			if (Integer.parseInt(estadoSolicitud) > 2) {
				response.setMensajeRespuesta("Propuesta ya ha sido entregada a GDC.");
				response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR_FUNCIONAL);
				LOGGER.error("[" + traza + "] Propuesta ya ha sido entregada a GDC.");
				bean.setNumeroPropuesta("");
				response.setTokenDecrypt(bean);
			} else {
				long expiry = Long.parseLong(tokenExpiry);
				long currentTime = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
				if (expiry > currentTime) {
					response.setMensajeRespuesta("Token ha expirado");
					LOGGER.error("[" + traza + "] Token ha expirado.");
					response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_NO_EXISTE);
					bean.setNumeroPropuesta("");
					response.setTokenDecrypt(bean);
				} else {
					response.setMensajeRespuesta(Constantes.MENSAJE_RESPUESTA_GENERAL_EXITO);
					response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
					bean.setAfiliacion(afiliacion);
					response.setTokenDecrypt(bean);
				}
			}

		} catch (JsonParseException e) {
			LOGGER.error("[" + traza + "] Error en parseo de JSON.");
			response.setMensajeRespuesta("Error en parseo de JSON");
			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			response.setTokenDecrypt(bean);
			e.printStackTrace();
		} catch (JsonMappingException e) {
			LOGGER.error("[" + traza + "] Error en mapeo de JSON.");
			response.setMensajeRespuesta("Error en mapeo de JSON");
			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			response.setTokenDecrypt(bean);
			e.printStackTrace();
		} catch (IOException e) {
			response.setMensajeRespuesta(Constantes.MENSAJE_RESPUESTA_GENERAL_ERR0R);
			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			response.setTokenDecrypt(bean);
			e.printStackTrace();
		}
		LOGGER.info("[" + traza + "] Sale de SolicitudServiceImpl.decryptLinkToken");
		return response;
	}

	@Override
	public BaseResponseDTO enviarDatosSolicitudCRM(Long idSolicitud) {
		BaseResponseDTO respuesta = new BaseResponseDTO();
		String codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_EXITO;
		String mensajeRespuesta = Strings.EMPTY;

		try {
			Solicitud solicitud = new Solicitud();
			StringBuilder sbError = new StringBuilder();
			solicitud = solicitudRepository.findById(idSolicitud).get();
			if (solicitud != null && solicitud.getEstado().equals(Constantes.CODIGO_SOLICITUD_POR_TRANSMITIR)) {

				// 1. ACTUALIZAR COTIZACION

				UpdateCotizacionRequest updateCotizacionRequest = this.completarCotizacionRequestCRM(solicitud);
				LOGGER.info("enviarDatosSolicitudCRM.updateCotizacionRequest=>" + gson.toJson(updateCotizacionRequest));
				// 2. ACTUALIZAR CONTACTO
				this.completarContactoRequestCRM(solicitud);
				mensajeRespuesta = sbError.toString();

			}
		} catch (Exception ex) {
			LOGGER.error("Error enviarDatosSolicitudCRM=>" + ex.getMessage(), ex);
			codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_ERROR;
			mensajeRespuesta = Utilitarios.obtenerMensaje(messageSource,
					Constantes.MENSAJE_SOLICITUD_ERROR_DATOS_NO_ENVIADOS_CRM);
		}
		respuesta.setCodigoRespuesta(codigoRespuesta);
		respuesta.setMensajeRespuesta(mensajeRespuesta);
		return respuesta;
	}

	private UpdateFormatoDPSRequest completarFormatoDPSRequestCRM(Solicitud solicitud)
			throws NumberFormatException, Exception {
		UpdateFormatoDPSRequest requestCRM = new UpdateFormatoDPSRequest();
		requestCRM.setParam(new UpdateFormatoDPSParamRequest());

		SolicitudDps solicitudDps = solicitudDpsRepositorioRepository.findByIdSolicitud(solicitud.getIdSolicitud());
		if (solicitudDps != null) {

			// String cotizacionId = solicitud.getIdCrmCotizador();
			String cotizacionId = "";
			// cotizacionId = solicitud.getIdCrmCotizador();

			if (solicitud.getIdCrmCotizador() == null || solicitud.getIdCrmCotizador().isEmpty()) {
				Long nroCotizacionL = Long.parseLong(solicitud.getNumeroCotizacion());
				LOGGER.info("SOLICITUD SIN ID_CRM_COTIZACION ===>" + nroCotizacionL);
				List<CotizacionDetalle> cotizacionVida = cotizacionRepository.detalleCotizacion2(nroCotizacionL);
				if (cotizacionVida != null && cotizacionVida.size() > 0) {
					solicitud.setIdCrmCotizador(cotizacionVida.get(0).getCrmCotizadorId());
				}
			}

			if (solicitud == null || solicitud.getNumeroCotizacionOrigen() == null
					|| solicitud.getNumeroCotizacionOrigen().isEmpty()) {
				cotizacionId = solicitud.getIdCrmCotizador();
			} else {
				Long nroCotizacionL = Long.parseLong(solicitud.getNumeroCotizacion());
				LOGGER.info("NRO COTIZACION VIDA ===>" + nroCotizacionL);
				List<CotizacionDetalle> cotizacionVida = cotizacionRepository.detalleCotizacion2(nroCotizacionL);
				if (cotizacionVida != null && cotizacionVida.size() > 0) {
					cotizacionId = cotizacionVida.get(0).getCrmCotizadorId();
				}
			}
			String estaturaCm = Utilitarios.formatoMiles(Double.parseDouble(solicitudDps.getEstatura().toString()),
					Utilitarios.FORMATO_NUMERO_SIN_DECIMAL);
			String pesoKg = Utilitarios.formatoMiles(Double.parseDouble(solicitudDps.getPeso().toString()),
					Utilitarios.FORMATO_NUMERO_SIN_DECIMAL);
			String variacionMasDe5kg = ConstantesCRM.DPS_RESPUESTA_NO;
			String ustedFumaCigarros = ConstantesCRM.DPS_RESPUESTA_NO;
			String ustedConsumeDrogas = ConstantesCRM.DPS_RESPUESTA_NO;
			String ustedConsumeAlcohol = ConstantesCRM.DPS_RESPUESTA_NO;

			if (solicitudDps.getPesoVariacion().equals(1))
				variacionMasDe5kg = ConstantesCRM.DPS_RESPUESTA_SI;
			if (solicitudDps.getFumador())
				ustedFumaCigarros = ConstantesCRM.DPS_RESPUESTA_SI;
			if (solicitudDps.getDrogas())
				ustedConsumeDrogas = ConstantesCRM.DPS_RESPUESTA_SI;
			if (solicitudDps.getAlcohol())
				ustedConsumeAlcohol = ConstantesCRM.DPS_RESPUESTA_SI;

			String tomaMedicamentos = ConstantesCRM.DPS_RESPUESTA_NO;
			String tratamientoQuirurgico = ConstantesCRM.DPS_RESPUESTA_NO;
			String evaluacionDiagnostico = ConstantesCRM.DPS_RESPUESTA_NO;
			String problemaFisicoMental = ConstantesCRM.DPS_RESPUESTA_NO;
			String otraInformacionConcerniente = ConstantesCRM.DPS_RESPUESTA_NO;
			String embarazada = ConstantesCRM.DPS_RESPUESTA_NO;
			String altoRiesgo = ConstantesCRM.DPS_RESPUESTA_NO;

			String actividadProfesionalLaboral = ConstantesCRM.DPS_RESPUESTA_NO;
			String practicaDeportesRiesgo = ConstantesCRM.DPS_RESPUESTA_NO;
			String participaEntrenamientosCompetencias = ConstantesCRM.DPS_RESPUESTA_NO;
			String otrosDeportesHobbies = ConstantesCRM.DPS_RESPUESTA_NO;
			String participaEnCompetencias = ConstantesCRM.DPS_RESPUESTA_NO;
			String utilizaMoto = ConstantesCRM.DPS_RESPUESTA_NO;

			List<SolicitudDpsPregunta> listaDpsPregunta = solicitudDpsPreguntaRepository
					.findByIdSolicitud(solicitud.getIdSolicitud());
			for (SolicitudDpsPregunta item : listaDpsPregunta) {
				if (Boolean.TRUE.equals(item.getRespuesta())) {
					switch (item.getPregunta()) {
					case "1":
						tomaMedicamentos = ConstantesCRM.DPS_RESPUESTA_SI;
						break;
					case "2":
						tratamientoQuirurgico = ConstantesCRM.DPS_RESPUESTA_SI;
						break;
					case "3":
						evaluacionDiagnostico = ConstantesCRM.DPS_RESPUESTA_SI;
						break;
					case "4":
						problemaFisicoMental = ConstantesCRM.DPS_RESPUESTA_SI;
						break;
					case "5":
						otraInformacionConcerniente = ConstantesCRM.DPS_RESPUESTA_SI;
						break;
					case "6":
						embarazada = ConstantesCRM.DPS_RESPUESTA_SI;
						break;
					case "7":
						altoRiesgo = ConstantesCRM.DPS_RESPUESTA_SI;
						break;

					case "8":
						actividadProfesionalLaboral = ConstantesCRM.DPS_RESPUESTA_SI;
						break;
					case "9":
						practicaDeportesRiesgo = ConstantesCRM.DPS_RESPUESTA_SI;
						break;
					case "10":
						participaEntrenamientosCompetencias = ConstantesCRM.DPS_RESPUESTA_SI;
						break;
					case "11":
						otrosDeportesHobbies = ConstantesCRM.DPS_RESPUESTA_SI;
						break;
					case "12":
						utilizaMoto = ConstantesCRM.DPS_RESPUESTA_SI;
						break;

					default:
						break;
					}
				}

			}

			String aseguradoTitular = null;
			String conyuge = null;
			String viajaAnioAsegTitular = null;
			String viajaAnioConyuge = null;
			String extraLaboralesAsegTitular = null;
			String extraLaboralesConyuge = null;
			String monitoreadoDedicoAsegTitular = null;
			String monitoreadoMedicoConyuge = null;
			String gradoDiscapacidadAsegTitular = null;
			String gradoDiscapacidadConyuge = null;
			String alcoholismoDrogadiccionAsegTitular = null;
			String alcoholismoDrogadiccionConyuge = null;
			String practicaDeporteAficionadoAsegTitular = null;
			String practicaDeporteAficionadoConyuge = null;
			String aficionadoRemuneradoAsegTitular = null;
			String aficionadoRemuneradoConyuge = null;
			String profesionalAsegTitular = null;
			String profesionalConyuge = null;
			String participanteAsegTitular = null;
			String participanteConyuge = null;
			String medioTransporteAsegTitular = null;
			String medioTransporteConyuge = null;
			String distraccionAsegTitular = null;
			String distraccionConyuge = null;
			String deporteEntrenamientoAsegTitular = null;
			String deporteEntrenamientoConyuge = null;

			requestCRM.getParam().setCotizacionId(cotizacionId);
			requestCRM.getParam().setEstaturaCm(estaturaCm);
			requestCRM.getParam().setPesoKg(pesoKg);
			requestCRM.getParam().setVariacionMasDe5kg(variacionMasDe5kg);
			requestCRM.getParam().setUstedFumaCigarros(ustedFumaCigarros);
			requestCRM.getParam().setUstedConsumeAlcohol(ustedConsumeAlcohol);
			requestCRM.getParam().setTomaMedicamentos(tomaMedicamentos);
			requestCRM.getParam().setEvaluacionDiagnostico(evaluacionDiagnostico);
			requestCRM.getParam().setOtraInformacionConcerniente(otraInformacionConcerniente);
			requestCRM.getParam().setUstedConsumeDrogas(ustedConsumeDrogas);
			requestCRM.getParam().setTratamientoQuirurgico(tratamientoQuirurgico);
			requestCRM.getParam().setProblemaFisicoMental(problemaFisicoMental);
			requestCRM.getParam().setEmbarazada(embarazada);
			requestCRM.getParam().setAltoRiesgo(altoRiesgo);
			requestCRM.getParam().setActividadProfesionalLaboral(actividadProfesionalLaboral);
			requestCRM.getParam().setPracticaDeportesRiesgo(practicaDeportesRiesgo);
			requestCRM.getParam().setParticipaEntrenamientosCompetencias(participaEntrenamientosCompetencias);
			requestCRM.getParam().setOtrosDeportesHobbies(otrosDeportesHobbies);
			requestCRM.getParam().setParticipaEnCompetencias(participaEnCompetencias);
			requestCRM.getParam().setUtilizaMoto(utilizaMoto);
			requestCRM.getParam().setAseguradoTitular(aseguradoTitular);
			requestCRM.getParam().setConyuge(conyuge);
			requestCRM.getParam().setViajaAnioAsegTitular(viajaAnioAsegTitular);
			requestCRM.getParam().setViajaAnioConyuge(viajaAnioConyuge);
			requestCRM.getParam().setExtraLaboralesAsegTitular(extraLaboralesAsegTitular);
			requestCRM.getParam().setExtraLaboralesConyuge(extraLaboralesConyuge);
			requestCRM.getParam().setMonitoreadoDedicoAsegTitular(monitoreadoDedicoAsegTitular);
			requestCRM.getParam().setMonitoreadoMedicoConyuge(monitoreadoMedicoConyuge);
			requestCRM.getParam().setGradoDiscapacidadAsegTitular(gradoDiscapacidadAsegTitular);
			requestCRM.getParam().setGradoDiscapacidadConyuge(gradoDiscapacidadConyuge);
			requestCRM.getParam().setAlcoholismoDrogadiccionAsegTitular(alcoholismoDrogadiccionAsegTitular);
			requestCRM.getParam().setAlcoholismoDrogadiccionConyuge(alcoholismoDrogadiccionConyuge);
			requestCRM.getParam().setPracticaDeporteAficionadoAsegTitular(practicaDeporteAficionadoAsegTitular);
			requestCRM.getParam().setPracticaDeporteAficionadoConyuge(practicaDeporteAficionadoConyuge);
			requestCRM.getParam().setAficionadoRemuneradoAsegTitular(aficionadoRemuneradoAsegTitular);
			requestCRM.getParam().setAficionadoRemuneradoConyuge(aficionadoRemuneradoConyuge);
			requestCRM.getParam().setProfesionalAsegTitular(profesionalAsegTitular);
			requestCRM.getParam().setProfesionalConyuge(profesionalConyuge);
			requestCRM.getParam().setParticipanteAsegTitular(participanteAsegTitular);
			requestCRM.getParam().setParticipanteConyuge(participanteConyuge);
			requestCRM.getParam().setMedioTransporteAsegTitular(medioTransporteAsegTitular);
			requestCRM.getParam().setMedioTransporteConyuge(medioTransporteConyuge);
			requestCRM.getParam().setDistraccionAsegTitular(distraccionAsegTitular);
			requestCRM.getParam().setDistraccionConyuge(distraccionConyuge);
			requestCRM.getParam().setDeporteEntrenamientoAsegTitular(deporteEntrenamientoAsegTitular);
			requestCRM.getParam().setDeporteEntrenamientoConyuge(deporteEntrenamientoConyuge);
		}

		return requestCRM;
	}

	private UpdateContactoRequest completarContactoRequestCRM(Solicitud solicitud) throws Exception {

		UpdateContactoRequest requestCRM = new UpdateContactoRequest();
		requestCRM.setParam(new UpdateContactoParamRequest());

		Persona persona = personaRepository.findById(solicitud.getPersonaByIdAsegurado().getIdPersona()).get();
		SolicitudDatosAdicionales solicitudDatosAdicionales = solicitudDatosAdicionalesRepository
				.findByIdSolicitud(solicitud.getIdSolicitud());

		if (persona != null) {
			String fechaSolicitud = DateUtil.dateToString(solicitud.getFechaSolicitud(), DateUtil.FORMATO_DIA_DDMMYYYY);

			String contactId = null;
			String tipoDocumento = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_TIPO_DOCUMENTO,
					persona.getTipoDocumento(), Constantes.MULTITABLA_COLUMNA_VALOR_CRM);
			String numeroDocumento = persona.getNumeroDocumento();

			// Por defecto todos en NULL para que no cambie el valor en CRM
			String consentimientoAsesoria = null;
			String fuenteConsentimientoAsesoria = null;
			String fechaConsentimientoAsesoria = null;
			String validacionConsentimientoAsesoria = null;
			// Fin por defecto

			String consentimientoTratamiento = ConstantesCRM.CONTACTO_CONSENTIMIENTO_SI;
			String fuenteTratamiento = ConstantesCRM.CONTACTO_CONSENTIMIENTO_FUENTE_ESCRITO;
			String fechaTratamiento = fechaSolicitud;

			String consentimientoTransferencia = ConstantesCRM.CONTACTO_CONSENTIMIENTO_SI;
			String fuenteTransferencia = ConstantesCRM.CONTACTO_CONSENTIMIENTO_FUENTE_ESCRITO;
			String fechaTransferencia = fechaSolicitud;

			String consentimientoComercializacion = ConstantesCRM.CONTACTO_CONSENTIMIENTO_NO;
			if (solicitud.getTratamientoAseguradoCheck().toString().equals("1"))
				consentimientoComercializacion = ConstantesCRM.CONTACTO_CONSENTIMIENTO_SI;
			String fuenteComercializacion = ConstantesCRM.CONTACTO_CONSENTIMIENTO_FUENTE_ESCRITO;
			String fechaConsentimientoComercializacion = fechaSolicitud;
			String validacionConsentimiento = ConstantesCRM.CONTACTO_CONSENTIMIENTO_VALIDACION_SI;

			String telefonoPrincipal = ConstantesCRM.CONTACTO_ADICIONALES_TELEFONO_PRINCIPAL;
			String telefonoFijo = ConstantesCRM.CONTACTO_ADICIONALES_TELEFONO_NUMERO_DEFECTO;
			String telephone1 = ConstantesCRM.CONTACTO_ADICIONALES_TELEFONO_NUMERO_DEFECTO;
			String celular = persona.getCelular().toString();
			String telefonoCelularTrabajo = ConstantesCRM.CONTACTO_ADICIONALES_TELEFONO_NUMERO_DEFECTO;
			String correo = persona.getCorreo();

			String tipoCliente = null; // Por defecto en NULL para que no cambie el valor en CRM

			String estadoCivil = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_ESTADO_CIVIL,
					persona.getEstadoCivil(), Constantes.MULTITABLA_COLUMNA_VALOR_CRM);
			String nacionalidad = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_NACIONALIDAD,
					persona.getNacionalidad(), Constantes.MULTITABLA_COLUMNA_VALOR_CRM);
			String fechaNacimiento = DateUtil.dateToString(persona.getFechaNacimiento(), DateUtil.FORMATO_DIA_DDMMYYYY);
			String profesion = ConstantesCRM.CONTACTO_PERSONALES_PROFESION_ADMINISTRADOR;
			String profesionCotizador = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_PROFESION,
					solicitudDatosAdicionales != null ? (solicitudDatosAdicionales.getProfesionAsegurado() != null
							? solicitudDatosAdicionales.getProfesionAsegurado()
							: persona.getProfesion()) : persona.getProfesion(),
					Constantes.MULTITABLA_COLUMNA_VALOR_CRM);
			String ingresoMoneda = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_MONEDA,
					persona.getIngresoMoneda(), Constantes.MULTITABLA_COLUMNA_VALOR_CRM);
			String ingresoAnual = persona.getIngresoValor().toString();
			String centroLaboral = persona.getCentroTrabajo();
			String jobtitle = persona.getProfesionDetalle();
			String origen = ConstantesCRM.CONTACTO_DATOS_VIDA_INDIVIDUAL_ORIGEN_CLIENTE_POTENCIAL_REFERIDOS;

			String actividadEconomica = ConstantesCRM.CONTACTO_DATOS_VIDA_INDIVIDUAL_ACTIVIDAD_ECONOMICA_ADMIN;
			String actividadEconomicaAcsele = this.valorMultiTabla(
					Constantes.MULTITABLA_CODIGO_TABLA_ACTIVIDAD_ECONOMICA,
					solicitudDatosAdicionales != null
							? (solicitudDatosAdicionales.getActividadEconomicaAsegurado() != null
									? solicitudDatosAdicionales.getActividadEconomicaAsegurado()
									: persona.getActividadEconomica())
							: persona.getActividadEconomica(),
					Constantes.MULTITABLA_COLUMNA_VALOR_CRM);
			String fumador = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_FUMADOR, "2",
					Constantes.MULTITABLA_COLUMNA_VALOR_CRM);
			// String fumador =
			// this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_FUMADOR,
			// persona.getFumador(), Constantes.MULTITABLA_COLUMNA_VALOR_CRM);
			String sujetoObligado = this
					.valorMultiTabla(Constantes.MULTITABLA_CODIGO_SUJETO_OBLIGADO, solicitudDatosAdicionales != null
							? (solicitudDatosAdicionales.getEsSujetoObligadoAsegurado() != null
									? solicitudDatosAdicionales.getEsSujetoObligadoAsegurado()
									: persona.getEsSujetoObligado())
							: persona.getEsSujetoObligado(), Constantes.MULTITABLA_COLUMNA_VALOR_CRM);
			String fechaContacto = fechaSolicitud;
			String indicadorPep = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_PEP,
					solicitudDatosAdicionales != null ? (solicitudDatosAdicionales.getEsPepAsegurado() != null
							? solicitudDatosAdicionales.getEsPepAsegurado()
							: persona.getEsPep()) : persona.getEsPep(),
					Constantes.MULTITABLA_COLUMNA_VALOR_CRM);
			String resultadoAuditoria = ConstantesCRM.CONTACTO_DATOS_VIDA_INDIVIDUAL_AUDITORIA_OK;

			String tipoDireccion = ConstantesCRM.CONTACTO_DIRECCION_TIPO_PARTICULAR;

			// Formato de dirección
			StringBuilder sbd = new StringBuilder();
			String direccionTipo = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_DIRECCION_TIPO,
					persona.getDireccionTipo(), Constantes.MULTITABLA_COLUMNA_VALOR_AUXILIAR).toUpperCase();
			sbd.append(direccionTipo).append(ConstantesSolicitudPDF.ESPACIO_EN_BLANCO);
			sbd.append(persona.getDireccionTipoDes()).append(ConstantesSolicitudPDF.ESPACIO_EN_BLANCO);

			if (persona.getDireccionNroMz() != null && !persona.getDireccionNroMz().equals(Strings.EMPTY)) {
				sbd.append(persona.getDireccionNroMz()).append(ConstantesSolicitudPDF.ESPACIO_EN_BLANCO);
			}
			if (persona.getDireccionUrbanizacion() != null
					&& !persona.getDireccionUrbanizacion().equals(Strings.EMPTY)) {
				sbd.append("URB. ");
				sbd.append(persona.getDireccionUrbanizacion()).append(ConstantesSolicitudPDF.ESPACIO_EN_BLANCO);
			}
			if (persona.getDireccionInterior() != null && !persona.getDireccionInterior().equals(Strings.EMPTY)) {
				sbd.append("INTERIOR").append(ConstantesSolicitudPDF.ESPACIO_EN_BLANCO);
				sbd.append(persona.getDireccionInterior()).append(ConstantesSolicitudPDF.ESPACIO_EN_BLANCO);
			}

			String direccion1 = sbd.toString().trim();
			String departamento = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_DEPARTAMENTO,
					persona.getDepartamento(), Constantes.MULTITABLA_COLUMNA_VALOR_CRM);
			String provincia = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_PROVINCIA, persona.getProvincia(),
					Constantes.MULTITABLA_COLUMNA_VALOR_CRM);
			String distrito = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_DISTRITO, persona.getDistrito(),
					Constantes.MULTITABLA_COLUMNA_VALOR_CRM);
			String direccion2 = null;
			String departamentoComercial = null;
			String provinciaComercial = null;
			String distritoComercial = null;

			requestCRM.getParam().setContactId(contactId);
			requestCRM.getParam().setConsentimientoAsesoria(consentimientoAsesoria);
			requestCRM.getParam().setFuenteConsentimientoAsesoria(fuenteConsentimientoAsesoria);
			requestCRM.getParam().setFechaConsentimientoAsesoria(fechaConsentimientoAsesoria);
			requestCRM.getParam().setValidacionConsentimientoAsesoria(validacionConsentimientoAsesoria);
			requestCRM.getParam().setConsentimientoTratamiento(consentimientoTratamiento);
			requestCRM.getParam().setFuenteTratamiento(fuenteTratamiento);
			requestCRM.getParam().setFechaTratamiento(fechaTratamiento);
			requestCRM.getParam().setConsentimientoTransferencia(consentimientoTransferencia);
			requestCRM.getParam().setFuenteTransferencia(fuenteTransferencia);
			requestCRM.getParam().setFechaTransferencia(fechaTransferencia);
			requestCRM.getParam().setConsentimientoComercializacion(consentimientoComercializacion);
			requestCRM.getParam().setFuenteComercializacion(fuenteComercializacion);
			requestCRM.getParam().setFechaConsentimientoComercializacion(fechaConsentimientoComercializacion);
			requestCRM.getParam().setValidacionConsentimiento(validacionConsentimiento);
			requestCRM.getParam().setTelefonoPrincipal(telefonoPrincipal);
			requestCRM.getParam().setTelefonoFijo(telefonoFijo);
			requestCRM.getParam().setTelephone1(telephone1);
			requestCRM.getParam().setCelular(celular);
			requestCRM.getParam().setTelefonoCelularTrabajo(telefonoCelularTrabajo);
			requestCRM.getParam().setCorreo(correo);
			requestCRM.getParam().setTipoCliente(tipoCliente);
			requestCRM.getParam().setTipoDocumento(tipoDocumento);
			requestCRM.getParam().setNumeroDocumento(numeroDocumento);
			requestCRM.getParam().setEstadoCivil(estadoCivil);
			requestCRM.getParam().setNacionalidad(nacionalidad);
			requestCRM.getParam().setFechaNacimiento(fechaNacimiento);
			requestCRM.getParam().setProfesion(profesion);
			requestCRM.getParam().setProfesionCotizador(profesionCotizador);
			requestCRM.getParam().setIngresoMoneda(ingresoMoneda);
			requestCRM.getParam().setIngresoAnual(ingresoAnual);
			requestCRM.getParam().setCentroLaboral(centroLaboral);
			requestCRM.getParam().setJobtitle(jobtitle);
			requestCRM.getParam().setOrigen(origen);
			requestCRM.getParam().setActividadEconomica(actividadEconomica);
			requestCRM.getParam().setActividadEconomicaAcsele(actividadEconomicaAcsele);
			requestCRM.getParam().setFumador(fumador);
			requestCRM.getParam().setSujetoObligado(sujetoObligado);
			requestCRM.getParam().setFechaContacto(fechaContacto);
			requestCRM.getParam().setIndicadorPep(indicadorPep);
			requestCRM.getParam().setResultadoAuditoria(resultadoAuditoria);
			requestCRM.getParam().setTipoDireccion(tipoDireccion);
			requestCRM.getParam().setDireccion1(direccion1);
			requestCRM.getParam().setDepartamento(departamento);
			requestCRM.getParam().setProvincia(provincia);
			requestCRM.getParam().setDistrito(distrito);
			requestCRM.getParam().setDireccion2(direccion2);
			requestCRM.getParam().setDepartamentoComercial(departamentoComercial);
			requestCRM.getParam().setProvinciaComercial(provinciaComercial);
			requestCRM.getParam().setDistritoComercial(distritoComercial);
		}

		return requestCRM;
	}

	private CrearBeneficiariosRequest completarBeneficiariosRequestCRM(Solicitud solicitud) throws Exception {
		CrearBeneficiariosRequest requestCRM = new CrearBeneficiariosRequest();
		requestCRM.setParam(new CrearBeneficiariosParamRequest());
		String cotizacionId = "";
		// cotizacionId = solicitud.getIdCrmCotizador();
		if (solicitud == null || solicitud.getNumeroCotizacionOrigen() == null
				|| solicitud.getNumeroCotizacionOrigen().isEmpty()) {
			cotizacionId = solicitud.getIdCrmCotizador();
		} else {
			Long nroCotizacionL = Long.parseLong(solicitud.getNumeroCotizacion());
			LOGGER.info("NRO COTIZACION VIDA ===>" + nroCotizacionL);
			List<CotizacionDetalle> cotizacionVida = cotizacionRepository.detalleCotizacion2(nroCotizacionL);
			if (cotizacionVida != null && !cotizacionVida.isEmpty()) {
				cotizacionId = cotizacionVida.get(0).getCrmCotizadorId();
			}
		}
		// requestCRM.getParam().setGuidCotizacion(solicitud.getIdCrmCotizador());
		requestCRM.getParam().setGuidCotizacion(cotizacionId);
		requestCRM.getParam().setOwner(solicitud.getIdCrmUsuario());

		List<CrearBeneficiariosDetalleRequest> listaBeneficiariosCRM = new ArrayList<>();

		List<SolicitudBeneficiario> lista = solicitudBeneficiarioRepository
				.findByIdSolicitud(solicitud.getIdSolicitud());
		if (!lista.isEmpty()) {
			for (SolicitudBeneficiario item : lista) {
				CrearBeneficiariosDetalleRequest beneficiario = new CrearBeneficiariosDetalleRequest();
				String tipoRelacion = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_TIPO_RELACION,
						item.getTipoRelacion(), Constantes.MULTITABLA_COLUMNA_VALOR_CRM);
				String beneficiarioNormal = ConstantesCRM.BENEFICIARIO_FLAG_SI;
				String beneficiarioContigente = ConstantesCRM.BENEFICIARIO_FLAG_NO;
				String tipoDocumento = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_TIPO_DOCUMENTO,
						item.getTipoDocumento(), Constantes.MULTITABLA_COLUMNA_VALOR_CRM);

				if (item.getTipoBeneficiario().equals(ConstantesSolicitudPDF.MULTITABLA_BENEFICIARIO_CONTINGENTE)) {
					beneficiarioNormal = ConstantesCRM.BENEFICIARIO_FLAG_NO;
					beneficiarioContigente = ConstantesCRM.BENEFICIARIO_FLAG_SI;
				}

				beneficiario.setNombres(item.getNombres());
				beneficiario.setApellidoPaterno(item.getApellidoPaterno());
				beneficiario.setApellidoMaterno(item.getApellidoMaterno());
				beneficiario.setNumeroDocumento(item.getNumeroDocumento());
				beneficiario.setTipoDocumento(tipoDocumento);
				beneficiario.setFechaNacimiento(
						DateUtil.dateToString(item.getFechaNacimiento(), DateUtil.FORMATO_DIA_DDMMYYYY));
				beneficiario.setTipoRelacion(tipoRelacion);
				beneficiario.setPorcentajeDistribucion(item.getDistribucion().toString());
				beneficiario.setBeneficiarioContigente(beneficiarioContigente);
				beneficiario.setBeneficiarioNormal(beneficiarioNormal);

				listaBeneficiariosCRM.add(beneficiario);
			}
		}
		requestCRM.getParam().setBeneficiarios(listaBeneficiariosCRM);

		return requestCRM;
	}

	private UpdateCotizacionRequest completarCotizacionRequestCRM(Solicitud solicitud) throws Exception {
		UpdateCotizacionRequest requestCRM = new UpdateCotizacionRequest();
		requestCRM.setParam(new UpdateCotizacionParamRequest());

		Persona personaAsegurado = personaRepository.findById(solicitud.getPersonaByIdAsegurado().getIdPersona()).get();

		if (solicitud.getIdCrmCotizador() == null || solicitud.getIdCrmCotizador().isEmpty()) {
			Long nroCotizacionL = Long.parseLong(solicitud.getNumeroCotizacion());
			LOGGER.info("SOLICITUD SIN ID_CRM_COTIZACION ===>" + nroCotizacionL);
			List<CotizacionDetalle> cotizacionVida = cotizacionRepository.detalleCotizacion2(nroCotizacionL);
			if (cotizacionVida != null && !cotizacionVida.isEmpty()) {
				solicitud.setIdCrmCotizador(cotizacionVida.get(0).getCrmCotizadorId());
			}
		}

		if (personaAsegurado != null) {
			String fechaSolicitud = DateUtil.dateToString(solicitud.getFechaSolicitud(), DateUtil.FORMATO_DIA_DDMMYYYY);
			String cotizacionId = "";
			// cotizacionId = solicitud.getIdCrmCotizador();
			if (solicitud == null || solicitud.getNumeroCotizacionOrigen() == null
					|| solicitud.getNumeroCotizacionOrigen().isEmpty()) {
				cotizacionId = solicitud.getIdCrmCotizador();
			} else {
				Long nroCotizacionL = Long.parseLong(solicitud.getNumeroCotizacion());
				LOGGER.info("NRO COTIZACION VIDA ===>" + nroCotizacionL);
				List<CotizacionDetalle> cotizacionVida = cotizacionRepository.detalleCotizacion2(nroCotizacionL);
				if (cotizacionVida != null && !cotizacionVida.isEmpty()) {
					cotizacionId = cotizacionVida.get(0).getCrmCotizadorId();
				}
			}
			String consentimientoComElectronica = ConstantesCRM.COTIZACION_CONSENTIMIENTO_SI;
			String fuenteComElectronica = ConstantesCRM.COTIZACION_COMUNICACION_FUENTE_CORREO;
			String fechaConsentimientoElectronica = fechaSolicitud;
			String estado = null;// EN NULL SE MANTIENE
			String fechaFirmaDocumentos = fechaSolicitud;
			String contratanteIgualAsegurado = ConstantesCRM.COTIZACION_CONTRATANTE_IGUAL_ASEGURADO_SI;
			String emailAsegurado = personaAsegurado.getCorreo();
			String tipoDocumento = null;
			String tipoDocumentoTemp = null;
			String numeroDocumento = null;
			String nombre = null;
			String apellidoPaterno = null;
			String apellidoMaterno = null;
			String sexo = null;
			String estadoCivil = null;
			String fechaNacimiento = null;
			String fumador = null;
			String condicionSujetObligado = null;
			String indicadorPep = null;
			String telefonoFijo = null;
			String telefonoCelular = null;
			String telefonoTrabajo = null;
			String correoElectronico = null;
			String profesion = null;
			String profesionCotizadorContratante = null;
			String actividadEconomica = null;
			String actividadEconomicaAcsele = null;
			String centroLaboral = null;
			String nacionalidad = null;
			String relacionAsegurable = null;
			String tipoDireccionCorrespondencia = null;
			String direccionParticular = null;
			String departamentoParticular = null;
			String provinciaParticular = null;
			String distritoParticular = null;
			String direccionComercial = null;
			String departamentoComercial = null;
			String provinciaComercial = null;
			String distritoComercial = null;
			String resultadoAuditoria = ConstantesCRM.COTIZACION_CONTRATANTE_DATOS_VIDA_INDIVIDUAL_AUDITORIA_OK;
			String numeroPropuesta = solicitud.getNumeroPropuesta();
			String confirmarNumeropropuesta = solicitud.getNumeroPropuesta();
			String envioEstadodecuenta = ConstantesCRM.COTIZACION_DATOS_ADICIONALES_ENVIO_ESTADO_CUENTA;
			String diaCobro = null;

			if (solicitud.getIdContratante() != null
					&& !solicitud.getIdAsegurado().equals(solicitud.getIdContratante())) {
				contratanteIgualAsegurado = ConstantesCRM.COTIZACION_CONTRATANTE_IGUAL_ASEGURADO_NO;

				Persona personaContrante = personaRepository
						.findById(solicitud.getPersonaByIdContratante().getIdPersona()).get();
				SolicitudDatosAdicionales solicitudDatosAdicionales = solicitudDatosAdicionalesRepository
						.findByIdSolicitud(solicitud.getIdSolicitud());
				String tipoDocumentoEqCrm = personaContrante.getTipoDocumento();
				if ("1".equals(personaContrante.getNumeroDocumento().substring(0, 1))
						&& personaContrante.getNumeroDocumento().length() > 8) {
					tipoDocumentoEqCrm = Constantes.CODIGO_TIPO_DOCUMENTO_RUC_NATURAL;
				}

				tipoDocumento = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_TIPO_DOCUMENTO,
						tipoDocumentoEqCrm, Constantes.MULTITABLA_COLUMNA_VALOR_CRM);
				tipoDocumentoTemp = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_TIPO_DOCUMENTO,
						tipoDocumentoEqCrm, Constantes.MULTITABLA_COLUMNA_VALOR_AUXILIAR);
				numeroDocumento = personaContrante.getNumeroDocumento();
				nombre = personaContrante.getNombres();
				apellidoPaterno = personaContrante.getApellidoPaterno();
				apellidoMaterno = personaContrante.getApellidoMaterno();

				if (!Utilitarios.valorString(personaContrante.getRazonSocial()).equals(Strings.EMPTY)) {
					nombre = Utilitarios.valorString(personaContrante.getRazonSocial());
					apellidoPaterno = Strings.EMPTY;
					apellidoMaterno = Strings.EMPTY;
				}

				// if (!"RUC".equals(tipoDocumentoTemp)) {
				if (!Constantes.CODIGO_TIPO_DOCUMENTO_RUC_JURIDICO.equals(tipoDocumentoEqCrm)) {
					nombre = nombre + " " + apellidoPaterno + " " + apellidoMaterno;
					sexo = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_GENERO, personaContrante.getGenero(),
							Constantes.MULTITABLA_COLUMNA_VALOR_CRM);
					estadoCivil = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_ESTADO_CIVIL,
							personaContrante.getEstadoCivil(), Constantes.MULTITABLA_COLUMNA_VALOR_CRM);
					fechaNacimiento = DateUtil.dateToString(personaContrante.getFechaNacimiento(),
							DateUtil.FORMATO_DIA_DDMMYYYY);
					fumador = ConstantesCRM.COTIZACION_CONTRATANTE_FUMADOR_NO;
					profesion = ConstantesCRM.COTIZACION_CONTRATANTE_PROFESION_ADMINISTRADOR;
					profesionCotizadorContratante = ConstantesCRM.COTIZACION_CONTRATANTE_PROFESION_COTIZADOR_ADMINISTRADOR;
					centroLaboral = personaContrante.getCentroTrabajo();
				}

				condicionSujetObligado = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_SUJETO_OBLIGADO,
						solicitudDatosAdicionales != null
								? (solicitudDatosAdicionales.getEsSujetoObligadoContratante() != null
										? solicitudDatosAdicionales.getEsSujetoObligadoContratante()
										: personaContrante.getEsSujetoObligado())
								: personaContrante.getEsSujetoObligado(),
						Constantes.MULTITABLA_COLUMNA_VALOR_CRM);
				indicadorPep = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_PEP,
						solicitudDatosAdicionales != null ? (solicitudDatosAdicionales.getEsPepContratante() != null
								? solicitudDatosAdicionales.getEsPepContratante()
								: personaContrante.getEsPep()) : personaContrante.getEsPep(),
						Constantes.MULTITABLA_COLUMNA_VALOR_CRM);
				telefonoFijo = ConstantesCRM.COTIZACION_CONTRATANTE_TELEFONO_NUMERO_DEFECTO;
				telefonoCelular = personaContrante.getCelular().toString();
				telefonoTrabajo = ConstantesCRM.COTIZACION_CONTRATANTE_TELEFONO_NUMERO_DEFECTO;
				correoElectronico = personaContrante.getCorreo();

				actividadEconomica = ConstantesCRM.COTIZACION_CONTRATANTE_ACTIVIDAD_ECONOMICA_ADMIN;
				actividadEconomicaAcsele = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_ACTIVIDAD_ECONOMICA,
						solicitudDatosAdicionales != null
								? (solicitudDatosAdicionales.getActividadEconomicaContratante() != null
										? solicitudDatosAdicionales.getActividadEconomicaContratante()
										: personaContrante.getActividadEconomica())
								: personaContrante.getActividadEconomica(),
						Constantes.MULTITABLA_COLUMNA_VALOR_CRM);
				nacionalidad = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_NACIONALIDAD,
						personaContrante.getNacionalidad(), Constantes.MULTITABLA_COLUMNA_VALOR_CRM);
				relacionAsegurable = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_TIPO_RELACION,
						solicitud.getVinculoAsegurado(), Constantes.MULTITABLA_COLUMNA_VALOR_CRM);
				tipoDireccionCorrespondencia = ConstantesCRM.COTIZACION_CONTRATANTE_DIRECCION_TIPO_PARTICULAR;

				// Formato de dirección
				StringBuilder sbd = new StringBuilder();
				String direccionTipo = this
						.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_DIRECCION_TIPO,
								personaContrante.getDireccionTipo(), Constantes.MULTITABLA_COLUMNA_VALOR_AUXILIAR)
						.toUpperCase();
				sbd.append(direccionTipo).append(ConstantesSolicitudPDF.ESPACIO_EN_BLANCO);
				sbd.append(personaContrante.getDireccionTipoDes()).append(ConstantesSolicitudPDF.ESPACIO_EN_BLANCO);

				if (personaContrante.getDireccionNroMz() != null
						&& !personaContrante.getDireccionNroMz().equals(Strings.EMPTY)) {
					sbd.append(personaContrante.getDireccionNroMz()).append(ConstantesSolicitudPDF.ESPACIO_EN_BLANCO);
				}
				if (personaContrante.getDireccionUrbanizacion() != null
						&& !personaContrante.getDireccionUrbanizacion().equals(Strings.EMPTY)) {
					sbd.append("URB. ");
					sbd.append(personaContrante.getDireccionUrbanizacion())
							.append(ConstantesSolicitudPDF.ESPACIO_EN_BLANCO);
				}
				if (personaContrante.getDireccionInterior() != null
						&& !personaContrante.getDireccionInterior().equals(Strings.EMPTY)) {
					sbd.append("INTERIOR").append(ConstantesSolicitudPDF.ESPACIO_EN_BLANCO);
					sbd.append(personaContrante.getDireccionInterior())
							.append(ConstantesSolicitudPDF.ESPACIO_EN_BLANCO);
				}

				direccionParticular = sbd.toString().trim();
				departamentoParticular = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_DEPARTAMENTO,
						personaContrante.getDepartamento(), Constantes.MULTITABLA_COLUMNA_VALOR_CRM);
				provinciaParticular = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_PROVINCIA,
						personaContrante.getProvincia(), Constantes.MULTITABLA_COLUMNA_VALOR_CRM);
				distritoParticular = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_DISTRITO,
						personaContrante.getDistrito(), Constantes.MULTITABLA_COLUMNA_VALOR_CRM);
			}

			requestCRM.getParam().setCotizacionId(cotizacionId);
			requestCRM.getParam().setConsentimientoComElectronica(consentimientoComElectronica);
			requestCRM.getParam().setFuenteComElectronica(fuenteComElectronica);
			requestCRM.getParam().setFechaConsentimientoElectronica(fechaConsentimientoElectronica);
			requestCRM.getParam().setEstado(estado);
			requestCRM.getParam().setFechaFirmaDocumentos(fechaFirmaDocumentos);
			requestCRM.getParam().setContratanteIgualAsegurado(contratanteIgualAsegurado);
			requestCRM.getParam().setEmailAsegurado(emailAsegurado);
			requestCRM.getParam().setTipoDocumento(tipoDocumento);
			requestCRM.getParam().setNumeroDocumento(numeroDocumento);
			requestCRM.getParam().setNombre(nombre);
			requestCRM.getParam().setApellidoPaterno(apellidoPaterno);
			requestCRM.getParam().setApellidoMaterno(apellidoMaterno);
			requestCRM.getParam().setSexo(sexo);
			requestCRM.getParam().setEstadoCivil(estadoCivil);
			requestCRM.getParam().setFechaNacimiento(fechaNacimiento);
			requestCRM.getParam().setFumador(fumador);
			requestCRM.getParam().setCondicionSujetObligado(condicionSujetObligado);
			requestCRM.getParam().setIndicadorPep(indicadorPep);
			requestCRM.getParam().setTelefonoFijo(telefonoFijo);
			requestCRM.getParam().setTelefonoCelular(telefonoCelular);
			requestCRM.getParam().setTelefonoTrabajo(telefonoTrabajo);
			requestCRM.getParam().setCorreoElectronico(correoElectronico);
			requestCRM.getParam().setProfesion(profesion);
			requestCRM.getParam().setProfesionCotizadorContratante(profesionCotizadorContratante);
			requestCRM.getParam().setActividadEconomica(actividadEconomica);
			requestCRM.getParam().setActividadEconomicaAcsele(actividadEconomicaAcsele);
			requestCRM.getParam().setCentroLaboral(centroLaboral);
			requestCRM.getParam().setNacionalidad(nacionalidad);
			requestCRM.getParam().setRelacionAsegurable(relacionAsegurable);
			requestCRM.getParam().setTipoDireccionCorrespondencia(tipoDireccionCorrespondencia);
			requestCRM.getParam().setDireccionParticular(direccionParticular);
			requestCRM.getParam().setDepartamentoParticular(departamentoParticular);
			requestCRM.getParam().setProvinciaParticular(provinciaParticular);
			requestCRM.getParam().setDistritoParticular(distritoParticular);
			requestCRM.getParam().setDireccionComercial(direccionComercial);
			requestCRM.getParam().setDepartamentoComercial(departamentoComercial);
			requestCRM.getParam().setProvinciaComercial(provinciaComercial);
			requestCRM.getParam().setDistritoComercial(distritoComercial);
			requestCRM.getParam().setResultadoAuditoria(resultadoAuditoria);
			requestCRM.getParam().setNumeroPropuesta(numeroPropuesta);
			requestCRM.getParam().setConfirmarNumeropropuesta(confirmarNumeropropuesta);
			requestCRM.getParam().setEnvioEstadodecuenta(envioEstadodecuenta);
			requestCRM.getParam().setDiaCobro(diaCobro);
			if (serverPort.toUpperCase().equals(Constantes.SERVER_PORT_PRODUCCION)) {
				requestCRM.getParam().setOrigenAdnDigital("1");
			}
		}

		return requestCRM;
	}

	@Override
	public BaseResponseDTO validarCodigo(SolicitudValidarCodigoRequestDTO solicitudValidarCodigoRequestDTO) {

		Solicitud solicitud = solicitudRepository.findById(solicitudValidarCodigoRequestDTO.getIdSolicitud()).get();
		String traza = Utilitarios.trazaLog() + "-" + solicitud.getNumeroCotizacion();
		LOGGER.info("[" + traza + "] Entro BaseResponseDTO.validarCodigo.INI(validarCodigo)");

		BaseResponseDTO response = new BaseResponseDTO();
		String mensajeError = Strings.EMPTY;
		String codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_ERROR;
		List<String> observaciones = new ArrayList<>();

		try {

			int valido = 0;

			List<CodigoVerificacion> lista = new ArrayList<>();

			if (!Utilitarios.valorString(solicitudValidarCodigoRequestDTO.getCodigoAsegurado()).equals(Strings.EMPTY)) {
				if (solicitud.getIdAsegurado() != null) {
					LOGGER.info("[" + traza + "] Validamos si el IdAsegurado es diferente a null");
					if (!serverPort.toUpperCase().equals(Constantes.SERVER_PORT_PRODUCCION)) {
						valido = 1;
					} else {
						LOGGER.info("Ingresa al Metodo ValidateCode");
						valido = this.validateCode(solicitud.getIdSolicitud(), solicitud.getIdAsegurado(),
								solicitudValidarCodigoRequestDTO.getCodigoAsegurado(), lista, "asegurado",
								solicitudValidarCodigoRequestDTO.getUsuarioLogin());
						LOGGER.info("[" + traza + "] Respuesta del metodo ValidateCode: " + valido);
					}
					observaciones.add("Codigo valido Asegurado: " + (valido == 1));
					if (valido == 1) {
						solicitud.setFechaFirmaAsegurado(new Date());
						solicitud.setFechaModif(new Date());
						solicitud.setUsuarioModif(solicitudValidarCodigoRequestDTO.getUsuarioLogin());
						solicitud.setFechaSolicitud(new Date());
						solicitud.setFechaFirmaAsegurado(new Date());

						solicitudRepository.save(solicitud);
						LOGGER.info("[" + traza + "] Guarda los datos en la tabla Solicitud");
						observaciones.add("Datos de firma asegurado actualizado...");
						codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_EXITO;
					}
				}
			}

			if (!Utilitarios.valorString(solicitudValidarCodigoRequestDTO.getCodigoContratante())
					.equals(Strings.EMPTY)) {
				if (solicitud.getIdContratante() != null) {
					LOGGER.info("[" + traza + "] Validamos si el IdContratante es diferente a null");
					if (!serverPort.toUpperCase().equals(Constantes.SERVER_PORT_PRODUCCION)) {
						valido = 1;
					} else {
						LOGGER.info("Ingresa al Metodo ValidateCode del Contratante");
						valido = this.validateCode(solicitud.getIdSolicitud(), solicitud.getIdContratante(),
								solicitudValidarCodigoRequestDTO.getCodigoContratante(), lista, "contratante",
								solicitudValidarCodigoRequestDTO.getUsuarioLogin());
						LOGGER.info("[" + traza + "] Respuesta del metodo ValidateCode del Contratante: " + valido);
					}
					observaciones.add("Codigo valido Contratante: " + (valido == 1));
					if (valido == 1) {
						solicitud.setFechaFirmaContratante(new Date());
						solicitud.setFechaModif(new Date());
						solicitud.setUsuarioModif(solicitudValidarCodigoRequestDTO.getUsuarioLogin());
						solicitud.setFechaSolicitud(new Date());
						solicitud.setFechaFirmaAsegurado(new Date());

						solicitudRepository.save(solicitud);
						LOGGER.info("[" + traza + "] Guarda los datos en la tabla Solicitud del contratante");
						observaciones.add("Datos de firma contratante actualizado...");
						codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_EXITO;
					}
				}
			}

			if (valido == 1) {
				lista.forEach(codigoVerificacion -> {
					codigoVerificacionRepository.save(codigoVerificacion);
				});

			}

			if (solicitud.getAseguradoIgualContratante().equals("1")) {
				if (solicitud.getFechaFirmaAsegurado() != null) {
					solicitud.setEstado(Constantes.CODIGO_SOLICITUD_FIRMA_FINALIZADA);
					solicitud.setFechaModif(new Date());
					solicitud.setUsuarioModif(solicitudValidarCodigoRequestDTO.getUsuarioLogin());

					solicitudRepository.save(solicitud);
					LOGGER.info("[" + traza
							+ "] Guarda los datos en la tabla Solicitud si el Asegurado es igual al Contratante");
					observaciones.add("Datos de estado actualizado...");
					LOGGER.info("[" + traza + "] Estado Actualizado");
					codigoRespuesta = Constantes.CODIGO_RESPUESTA_SOLICITUD_FIRMA_COMPLETA;
				}
			} else {
				if (solicitud.getFechaFirmaAsegurado() != null && solicitud.getFechaFirmaContratante() != null) {

					solicitud.setEstado(Constantes.CODIGO_SOLICITUD_FIRMA_FINALIZADA);
					solicitud.setFechaModif(new Date());
					solicitud.setUsuarioModif(solicitudValidarCodigoRequestDTO.getUsuarioLogin());

					solicitudRepository.save(solicitud);
					LOGGER.info(
							"Guarda en la tabla Solicitud si fechafirmaAsegurado y fechafirmaContratante diferente a null");
					observaciones.add("Datos de estado actualizado...");
					LOGGER.info("[" + traza + "] Estado Actualizado");
					codigoRespuesta = Constantes.CODIGO_RESPUESTA_SOLICITUD_FIRMA_COMPLETA;
				}
			}

		} catch (Exception ex) {
			LOGGER.info("[" + traza + "] ERROR validarCodigo=>" + ex);
			mensajeError = "Error al validar el codigo, intente nuevamente.";
			response.setObjErrorResource(
					new ErrorResourceDTO("ADN-008", "Error al validar el codigo: " + ex.getMessage()));
		}

		response.setCodigoRespuesta(codigoRespuesta);
		response.setMensajeRespuesta(mensajeError);
		/*
		 * logRepository.save( new EventLog("Solicitud", response.getCodigoRespuesta(),
		 * solicitudValidarCodigoRequestDTO.getPath(), response.getMensajeRespuesta(),
		 * gson.toJson(response.getObjErrorResource()), gson.toJson(observaciones),
		 * solicitudValidarCodigoRequestDTO.getUsuarioLogin(),
		 * solicitudValidarCodigoRequestDTO.getDevice(),
		 * solicitudValidarCodigoRequestDTO.getOs()));
		 */
		return response;
	}

	protected int validateCode(Long idSolicitud, Long idPersona, String codigo, List<CodigoVerificacion> lista,
			String tipo, String usuarioLogin) throws Exception {
		// CodigoVerificacion cv =
		// codigoVerificacionRepository.findCodigoActivo(idSolicitud, idPersona);

		List<CodigoVerificacion> listaBD = codigoVerificacionRepository.findCodigoActivo(idSolicitud, idPersona);

		for (CodigoVerificacion cv : listaBD) {
			if (cv.getCodigo() != null) {
				String codigoAnt = cv.getCodigo();
				String codigoDecript = this.decrypt(codigoAnt);
				LOGGER.info("codigoAnt :" + codigoDecript);
				LOGGER.info("codigo :" + codigo);

				if (codigoDecript.equalsIgnoreCase(codigo)) {
					cv.setActivo(0);
					cv.setUsado(1);
					cv.setUsuarioModif(usuarioLogin);
					cv.setFechaUsado(new Date());
					cv.setFechaModif(new Date());

					lista.add(cv);

					return 1;
				}
			}

		}

		throw new Exception(Utilitarios.obtenerMensaje(messageSource, new Object[] { tipo },
				Constantes.MENSAJE_SOLICITUD_ERROR_VALIDAR_CODIGO));
	}

	public SolicitudReglamentoResponseDTO obtenerReglamento(String tipoDocumento, String numeroDocumento,
			Long numeroCotizacion, String usuarioLogin, String agenteNombres, String agenteCorreo,
			String agenteNumVendedor, String agenteIdCRM, CotizaDetalleResponseDTO cotizacionDetalle,
			String tipoProducto) {
		SolicitudReglamentoResponseDTO response = new SolicitudReglamentoResponseDTO();
		String codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_EXITO;
		String mensajeRespuesta = Strings.EMPTY;

		LOGGER.info("obtenerReglamento.CotizaDetalleResponseDTO=>" + gson.toJson(cotizacionDetalle));

		Solicitud solicitudN = solicitudRepository.findByNumeroCotizacion(String.valueOf(numeroCotizacion));
		int estadoSolicitud = 0;
		if (solicitudN != null) {
			estadoSolicitud = Integer.parseInt(solicitudN.getEstado());
		}
		if (cotizacionDetalle.getCrmCotizadorId() == null && estadoSolicitud > 3) {
			mensajeRespuesta = Utilitarios.obtenerMensaje(messageSource,
					Constantes.MENSAJE_SOLICITUD_COTIZACION_NO_EXISTE_CRM);
			// codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_ERROR;
		}

		if (cotizacionDetalle.getCrmCotizadorId() != null && cotizacionDetalle.getCrmCotizadorId().equals(Strings.EMPTY)
				&& estadoSolicitud > 3) {
			mensajeRespuesta = Utilitarios.obtenerMensaje(messageSource,
					Constantes.MENSAJE_SOLICITUD_COTIZACION_NO_EXISTE_CRM);
			// codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_ERROR;
		}

		Persona personaAsegurado = personaRepository.findByTipoNumeroDocumento(tipoDocumento, numeroDocumento);
		if (personaAsegurado != null) {
			Adn adn = adnRepository.findByIdPersona(personaAsegurado.getIdPersona());
			if (adn != null) {
				if (cotizacionDetalle != null) {
					SolicitudRegistroResponseDTO temp = this.guardarSolicitud(cotizacionDetalle, personaAsegurado,
							usuarioLogin, agenteNombres, agenteCorreo, agenteNumVendedor, agenteIdCRM, tipoProducto,
							false);
					Long idSolicitud = temp.getIdSolicitud();
					if (idSolicitud != null) {
						response.setIdSolicitud(idSolicitud.toString());
						response.setNumeroCotizacion(numeroCotizacion.toString());
						response.setEsNuevo(temp.getEsNuevo());

						Solicitud solicitud = solicitudRepository
								.findByNumeroCotizacion(String.valueOf(numeroCotizacion));
						try {
							SolicitudFormularioCobroResponseDTO cobro = new SolicitudFormularioCobroResponseDTO();
							// LOGGER.info("Tarjeta buscada==>>>" + solicitud.getIdAfiliacionPropuesta());
							// String tarjetaPR = "";
							LOGGER.info("Intentando recuperar tarjeta");
							TarjetaPrimaRecurrente tarjeta = sitcRepository
									.obtenerTarjetaPrimaRecurrente(solicitud.getIdAfiliacionPropuesta());
							LOGGER.info("Tarjeta recuperada===>>>");
							LOGGER.info(gson.toJson(tarjeta));
							if (tarjeta != null) {
								cobro.setNumeroCuentaTarjeta(tarjeta.getTarjeta());
								cobro.setFechaVencimiento(tarjeta.getTarjetaVencimiento());
								cobro.setMoneda(tarjeta.getMoneda());
								cobro.setTipoCuenta(tarjeta.getTipoCuenta());
								cobro.setTipoViaCobro(tarjeta.getViaCobro());
								cobro.setViaCobro(tarjeta.getMedioPago());
							}
							LOGGER.info(gson.toJson(cobro));

							response.setCobro(cobro);
						} catch (Exception e) {
							// e.printStackTrace();
						}
						try {
							CotizacionCrmRequestDTO cotizacionCrmRequestDTO = new CotizacionCrmRequestDTO();
							cotizacionCrmRequestDTO.setNombreProducto(tipoProducto);
							cotizacionCrmRequestDTO.setNumeroCotizacion(numeroCotizacion.toString());
							cotizacionCrmRequestDTO.setNumeroDocumento(numeroDocumento);
							cotizacionCrmRequestDTO
									.setTipoDocumento((tipoDocumento.equals("1")) ? "DocumentoIdentidad" : "CE");
							cotizacionCrmRequestDTO.setUsuarioLogin(usuarioLogin);
							generarPdfEdn(cotizacionCrmRequestDTO, solicitud);
						} catch (Exception e) {
							// TODO: handle exception
						}
						try {
							LOGGER.info("Validando muerte Accidental");
							ObtenerMuerteAccidentalRequest obtenerMuerteAccidentalRequest = new ObtenerMuerteAccidentalRequest();
							obtenerMuerteAccidentalRequest.setPropuesta(String.valueOf(numeroCotizacion));
							ObtenerMuerteAccidentalResponse valaidarMuerteAccidental = globalRestClient
									.obtenerMuerteAccidental(obtenerMuerteAccidentalRequest);
							LOGGER.info(gson.toJson(valaidarMuerteAccidental));
							response.setMuerteAccidental(valaidarMuerteAccidental.getEstado());
						} catch (Exception e) {
							// TODO: handle exception
						}
					} else {
						mensajeRespuesta = Utilitarios.obtenerMensaje(messageSource,
								Constantes.MENSAJE_SOLICITUD_NO_REGISTRADA);
						codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_ERROR;
					}
				} else {
					mensajeRespuesta = Utilitarios.obtenerMensaje(messageSource,
							Constantes.MENSAJE_SOLICITUD_COTIZACION_NO_ENCONTRADA);
					codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_ERROR;
				}
			} else {
				mensajeRespuesta = Utilitarios.obtenerMensaje(messageSource,
						Constantes.MENSAJE_SOLICITUD_CLIENTE_SIN_ADN);
				codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_ERROR;
			}
		} else {
			mensajeRespuesta = Utilitarios.obtenerMensaje(messageSource,
					Constantes.MENSAJE_SOLICITUD_CLIENTE_NO_EXISTE);
			codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_ERROR;
		}

		response.setCodigoRespuesta(codigoRespuesta);
		response.setMensajeRespuesta(mensajeRespuesta);

		return response;
	}

	public SolicitudRegistroResponseDTO guardarSolicitud(CotizaDetalleResponseDTO cotizacionDetalle,
			Persona personaAsegurado, String usuarioLogin, String agenteNombres, String agenteCorreo,
			String agenteNumVendedor, String agenteIdCRM, String tipoProducto, Boolean recotizado) {
		// SolicitudRegistroResponseDTO response = new
		// SolicitudRegistroResponseDTO(null, null);
		Long idSolicitud = null;
		String numeroCotizacion = cotizacionDetalle.getNroCotizacion();

		String idCrmCotizador = cotizacionDetalle.getCrmCotizadorId();
		String idCrmOportunidad = cotizacionDetalle.getCrmOportunidadId();
		String fechaCotizacion = "";
		try {
			fechaCotizacion = DateUtil.dateToString(cotizacionDetalle.getFechaCotizacion(),
					DateUtil.FORMATO_DIA_DDMMYYYY);
		} catch (Exception e) {
			LOGGER.error("", e);
		}
		String numeroPropuesta = numeroCotizacion; // POR DEFINIR
		String codigoMoneda = "";
		String codigoSubPlan = "";
		if (!"VidaFree".equals(tipoProducto)) {
			codigoMoneda = this.obtenerCodigoMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_MONEDA,
					Constantes.MULTITABLA_COLUMNA_VALOR_AUXILIAR, cotizacionDetalle.getMoneda());
			codigoSubPlan = this.obtenerCodigoMultiTabla(Constantes.MULTITABLA_CODIGO_SUBPLAN_COTIZADOR,
					Constantes.MULTITABLA_COLUMNA_VALOR_AUXILIAR, cotizacionDetalle.getSubplanId());
		} else {
			codigoMoneda = cotizacionDetalle.getMoneda();
			codigoSubPlan = cotizacionDetalle.getPlanId();
		}

		// Registrar Cotizacion
		Solicitud solicitudDomain = new Solicitud();
		solicitudDomain.setNumeroCotizacion(numeroCotizacion);
		// solicitudDomain.setFecha
		solicitudDomain = solicitudRepository.findByNumeroCotizacion(numeroCotizacion);

		if (solicitudDomain != null && !solicitudDomain.getEstado().equals("0")) {
// 			CrmCotizacion crmCotizacion = crmRepository.obtenerDataCotizacion(numeroCotizacion);
// 			idCrmCotizador = crmCotizacion != null ? crmCotizacion.getCotizacionId() : null;
			idCrmCotizador = null;
		}

		if (solicitudDomain == null) {
			solicitudDomain = new Solicitud();
			solicitudDomain.setNumeroCotizacion(numeroCotizacion);
			solicitudDomain.setNumeroCotizacionOrigen("");
			solicitudDomain.setEstado(Constantes.CODIGO_SOLICITUD_PENDIENTE);
			solicitudDomain.setAgenteNombres(agenteNombres);
			solicitudDomain.setAgenteNumVendedor(agenteNumVendedor);
			solicitudDomain.setIdCrmUsuario(agenteIdCRM);
			solicitudDomain.setPersonaByIdAsegurado(personaAsegurado);
			solicitudDomain.setIdCrmCotizador(idCrmCotizador);
			solicitudDomain.setIdCrmOportunidad(idCrmOportunidad);
			solicitudDomain.setUsuarioCrea(usuarioLogin);
			solicitudDomain.setFechaCrea(new Date());
		} else {
			LOGGER.info("Solicitud Producto ya existe!!");
			/*
			 * if(!recotizado &&
			 * !solicitudDomain.getEstado().equals(Constantes.CODIGO_SOLICITUD_PENDIENTE)) {
			 * return new SolicitudRegistroResponseDTO(solicitudDomain.getIdSolicitud(),
			 * false); }
			 */
			if (solicitudDomain.getEstado().equals("0")) {
				solicitudDomain.setEstado(Constantes.CODIGO_SOLICITUD_PENDIENTE);
				solicitudDomain.setAgenteNombres(agenteNombres);
				solicitudDomain.setAgenteNumVendedor(agenteNumVendedor);
				solicitudDomain.setIdCrmUsuario(agenteIdCRM);
				solicitudDomain.setIdCrmCotizador(idCrmCotizador);
				solicitudDomain.setIdCrmOportunidad(idCrmOportunidad);
				solicitudDomain.setUsuarioCrea(usuarioLogin);
			} else {
				solicitudDomain.setIdCrmCotizador(idCrmCotizador);
				solicitudDomain.setUsuarioModif(usuarioLogin);
				solicitudDomain.setFechaModif(new Date());
			}
		}

		if (!recotizado) {
			solicitudDomain.setAgenteCorreo(agenteCorreo);
		}

		solicitudDomain.setNumeroPropuesta(numeroPropuesta);
		solicitudDomain.setSubplan(codigoSubPlan);
		solicitudDomain.setMoneda(codigoMoneda);

		solicitudRepository.save(solicitudDomain);

		idSolicitud = solicitudDomain.getIdSolicitud();

		// #####################
		// Guardar Cotización en SolicitudProducto
		// #####################
		String tipoProductoVar = "VidaFree".equals(tipoProducto)
				? ConstantesSolicitudPDF.MULTITABLA_PRODUCTO_TIPO_PRODUCTO_VIDA_FREE
				: ConstantesSolicitudPDF.MULTITABLA_PRODUCTO_TIPO_PRODUCTO_PLAN_GARANTIZADO;
		String frecuencia = this.obtenerCodigoMultiTabla(Constantes.MULTITABLA_CODIGO_FRECUENCIA,
				Constantes.MULTITABLA_COLUMNA_VALOR_AUXILIAR, cotizacionDetalle.getFrecuenciaPagoPrima());

		String tipoCuota = ConstantesSolicitudPDF.MULTITABLA_PRODUCTO_TIPO_CUOTA_SIN_CUOTA;
		String tipoRiesgo = personaAsegurado.getFumador();
		Float montoFondoGarantizado = cotizacionDetalle.getMontoTotalFondoGarantizado();
		Integer periodoCoberturaAnual = cotizacionDetalle.getPeriodoCobertura();
		Integer anualidadPago = cotizacionDetalle.getNroAnualidadesPagoBeneficio();
		Float primaComercialAnual = cotizacionDetalle.getPrimaComercialAnual();
		Float factorPago = cotizacionDetalle.getFactorPago();
		Float primaComercial = cotizacionDetalle.getPrimaComercial();
		Float primaIgv = cotizacionDetalle.getPrimaIgv();
		Integer primaDevolucion = cotizacionDetalle.getPrimaDevolucion();

		if (cotizacionDetalle.getPlanCuotaComodin().equals(Constantes.COTIZADOR_VIDA_VALOR_SI)) {
			tipoCuota = ConstantesSolicitudPDF.MULTITABLA_PRODUCTO_TIPO_CUOTA_COMODIN;
		} else {
			if (cotizacionDetalle.getPlanCuotaDoble().equals(Constantes.COTIZADOR_VIDA_VALOR_SI)) {
				tipoCuota = ConstantesSolicitudPDF.MULTITABLA_PRODUCTO_TIPO_CUOTA_DOBLE;
			}
		}

		SolicitudProducto solicitudProducto = new SolicitudProducto();
		solicitudProducto.setSolicitud(solicitudDomain);

		solicitudProducto = solicitudProductoRepository.findByIdSolicitud(idSolicitud);

		if (solicitudProducto == null) {
			solicitudProducto = new SolicitudProducto();
			solicitudProducto.setSolicitud(solicitudDomain);
			solicitudProducto.setUsuarioCrea(usuarioLogin);
			solicitudProducto.setFechaCrea(new Date());
		} else {
			solicitudProducto.setUsuarioModif(usuarioLogin);
			solicitudProducto.setFechaModif(new Date());
		}

		solicitudProducto.setTipoProducto(tipoProductoVar);
		solicitudProducto.setFechaCotizacion(fechaCotizacion);
		solicitudProducto.setMontoFondoGarantizado(montoFondoGarantizado);
		solicitudProducto.setPeriodoCoberturaAnual(periodoCoberturaAnual);
		solicitudProducto.setAnualidadPago(anualidadPago);
		solicitudProducto.setFrecuencia(frecuencia);
		solicitudProducto.setTipoCuota(tipoCuota);
		solicitudProducto.setTipoRiesgo(tipoRiesgo);
		solicitudProducto.setPrimaComercialAnual(primaComercialAnual);
		solicitudProducto.setFactorPago(factorPago);
		solicitudProducto.setPrimaComercial(primaComercial);
		solicitudProducto.setPrimaIgv(primaIgv);
		solicitudProducto.setprimaDevolucion(primaDevolucion);

		solicitudProductoRepository.save(solicitudProducto);

		// #####################
		// Guardar Cotización Detalle en SolicitudProductoDetalle
		// #####################

		// Inicio - Eliminar las creadas previamente
		List<SolicitudProductoDetalle> listaProductoDetalle = solicitudProductoDetalleRepository
				.findByIdSolicitud(idSolicitud);
		for (SolicitudProductoDetalle item : listaProductoDetalle) {
			solicitudProductoDetalleRepository.delete(item);
		}
		// Fin Eliminar las creadas previamente

		if (cotizacionDetalle.getCobertura() != null) {
			for (CotizaDetalleCoberturaResponseDTO coberturaItem : cotizacionDetalle.getCobertura()) {
				String cobertura = "";
				if ("VidaFree".equals(tipoProducto)) {
					cobertura = coberturaItem.getId();
				} else {
					cobertura = this.obtenerCodigoMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_COBERTURAS,
							Constantes.MULTITABLA_COLUMNA_VALOR_AUXILIAR, coberturaItem.getId());
				}

				String tipoCobertura = coberturaItem.getTipo(); // Ya se homologó el codigo en la query
				Float capitalAsegurado = coberturaItem.getCapital();
				Float primaAnual = coberturaItem.getPrima();

				SolicitudProductoDetalle solicitudProductoDetalle = new SolicitudProductoDetalle();
				solicitudProductoDetalle.setSolicitudProducto(solicitudProducto);
				solicitudProductoDetalle.setCobertura(cobertura);
				solicitudProductoDetalle.setTipoCobertura(tipoCobertura);
				solicitudProductoDetalle.setCapitalAsegurado(capitalAsegurado);
				solicitudProductoDetalle.setPrimaAnual(primaAnual);

				solicitudProductoDetalle.setUsuarioCrea(usuarioLogin);
				solicitudProductoDetalle.setFechaCrea(new Date());

				solicitudProductoDetalleRepository.save(solicitudProductoDetalle);
			}
		}

		return new SolicitudRegistroResponseDTO(idSolicitud, true);
	}

	private String obtenerCodigoMultiTabla(String codigoTabla, String campo, String valorBuscar) {
		AtomicReference<String> result = new AtomicReference<>();
		result.set("");
		if (StringUtils.isBlank(valorBuscar)) {
			return result.get();
		}
		List<Multitabla> lista = multitablaRepository.findByCodigoTablaSinEstado(codigoTabla);
		if (!lista.isEmpty()) {
			lista.forEach(multitabla -> {
				switch (campo) {
				case Constantes.MULTITABLA_COLUMNA_VALOR:
					if (StringUtils.equals(valorBuscar, multitabla.getValor())) {
						result.set(multitabla.getCodigo());
					}
					break;
				case Constantes.MULTITABLA_COLUMNA_VALOR_AUXILIAR:
					if (StringUtils.equals(valorBuscar, multitabla.getValorAux())) {
						result.set(multitabla.getCodigo());
					}
					break;
				case Constantes.MULTITABLA_COLUMNA_VALOR_CRM:
					if (StringUtils.equals(valorBuscar, multitabla.getValorCrm())) {
						result.set(multitabla.getCodigo());
					}
					break;
				default:
					break;
				}
			});
		}

		return result.get();
	}

	private void generarPdfEdn(CotizacionCrmRequestDTO cotizacionCrmRequestDTO, Solicitud solicitud) {
		try {
			DateFormat sourceFormat = new SimpleDateFormat(DateUtil.FORMATO_DIA_DDMMYYYY);
			CotizacionDetalle model = new CotizacionDetalle();
			ConsultaCotizacionDetalleRequest request0 = new ConsultaCotizacionDetalleRequest();
			ConsultaCotizacionDetalleResponse response0 = new ConsultaCotizacionDetalleResponse();

			String fechaCotizacion;
			if (cotizacionCrmRequestDTO.getNombreProducto().equals("VidaFree")) {

				request0.setIdAgente("");
				request0.setNumeroCotizacion(cotizacionCrmRequestDTO.getNumeroCotizacion());
				response0 = vidaFreeRestClient.obtenerCotizacionDetalle(request0);
				fechaCotizacion = response0.getData().getDetalleCotizacion().getFechaCotizacion();
			} else {
				model = cotizacionRepository.detalleCotizacion(Long.parseLong(solicitud.getNumeroPropuesta())).get(0);
				fechaCotizacion = sourceFormat.format(model.getFechaCotizacion());
			}

			String fechaEDN_string = valorMultiTabla("060", "1", "valor");
			Date fechaEdnDate = sourceFormat.parse(fechaEDN_string);
			Date fechaCotizacionDate = sourceFormat.parse(fechaCotizacion);

			if (fechaCotizacionDate.compareTo(fechaEdnDate) >= 0) {
				// obtener CarpetaCS
				Persona persona = personaRepository.findById(solicitud.getIdAsegurado()).get();
				String contactVal = persona.getIdPersonaCS();
				LOGGER.info("Fin PersonaxDocumento: " + contactVal);
				// obtener datos
				String planesFuturo = new String();
				String ahorroDeseasRecibir = new String();
				String frecuenciaPago = new String();
				String porcentajeDevolucion = new String();
				String montoMes = new String();
				String aniosAsegurado = new String();
				String aniosPago = new String();

				if (cotizacionCrmRequestDTO.getNombreProducto().equals("VidaFree")) {
					PlanFuturo planFuturoDomain = planFuturoRepository
							.findByIdPersona(solicitud.getPersonaByIdAsegurado().getIdPersona());

					List<String> planes = new ArrayList<>();
					if (planFuturoDomain.getPlanEducacion() == 1)
						planes.add("Educación");
					if (planFuturoDomain.getPlanProyecto() == 1)
						planes.add("Sueños");
					if (planFuturoDomain.getPlanJubilacion() == 1)
						planes.add("Jubilación");
					planesFuturo = StringUtils.join(planes, ',');

					aniosAsegurado = response0.getData().getDetalleCotizacion().getPeriodoVigencia().toString();
					aniosPago = response0.getData().getDetalleCotizacion().getPeriodoPago().toString();
					String moneda = response0.getData().getDetalleCotizacion().getMonedaDescripcion()
							.equalsIgnoreCase("Nuevo Sol") ? "S/. " : "$ ";
					montoMes = moneda + response0.getData().getDetalleCotizacion().getPrimaRecurrenteTotal().toString();
					porcentajeDevolucion = response0.getData().getDetalleCotizacion().getPorcentajeDevolucion()
							.toString() + "%";
					frecuenciaPago = response0.getData().getDetalleCotizacion().getFrecuenciaPagoDescripcion();

					if (solicitud.getSubplan() != null) {
						switch (solicitud.getSubplan()) {
						case "1":
							ahorroDeseasRecibir = "Fin de cobertura";
							break;
						case "2":
							ahorroDeseasRecibir = "Fin de periodo de cobertura";
							break;
						case "3":
							ahorroDeseasRecibir = "Fin de periodo de pago";
							break;
						default:
							break;
						}
					}
				}
				else {

					if (solicitud.getSubplan() != null) {
						switch (solicitud.getSubplan()) {
						case "1":
							planesFuturo = "Educación";
							break;
						case "2":
							planesFuturo = "Jubilación";
							break;
						case "3":
							planesFuturo = "Sueño";
							break;
						default:
							break;
						}
					}
					frecuenciaPago = model.getFrecuenciaPagoPrima();
					String moneda = model.getMoneda().equalsIgnoreCase("SOLES") ? "S/. " : "$ ";
					montoMes = moneda + String.valueOf(model.getPrimaComercial());
					aniosAsegurado = String.valueOf(model.getPeriodoCobertura());
					aniosPago = String.valueOf(Integer.parseInt(model.getPer_cob()) / 12);
					porcentajeDevolucion = "No aplica";
					ahorroDeseasRecibir = "Fin de cobertura";
				}

				// consumir esn generar pdf
				GenerarPdfRequest esnGenerarPdfRequest = new GenerarPdfRequest();

				esnGenerarPdfRequest.setProducto("VIDA");
				esnGenerarPdfRequest.setRepositorioCS("CRM");
				esnGenerarPdfRequest.setCarpetaCS(contactVal.toLowerCase());
				esnGenerarPdfRequest.setSolicitud(cotizacionCrmRequestDTO.getNumeroCotizacion());
				esnGenerarPdfRequest.setNumerodocumento(cotizacionCrmRequestDTO.getNumeroDocumento());
				esnGenerarPdfRequest.setCodigoPlantillaEN("1");
				List<GenerarPdfCabeceras> generarPdfCabeceras = new ArrayList<>();
				GenerarPdfCabeceras generarPdfCabecera1 = new GenerarPdfCabeceras();
				generarPdfCabecera1.setCampo("CLIENTE");
				String nombre = persona.getNombres()  + " " + persona.getApellidoPaterno();
				String apeMaterno = (persona.getApellidoPaterno() != null)? persona.getApellidoPaterno(): "";
				generarPdfCabecera1.setValor(nombre + " " + apeMaterno);
				GenerarPdfCabeceras generarPdfCabecera2 = new GenerarPdfCabeceras();
				generarPdfCabecera2.setCampo(cotizacionCrmRequestDTO.getNumeroDocumento().length() == 8 ? "DNI" : "CE");
				generarPdfCabecera2.setValor(cotizacionCrmRequestDTO.getNumeroDocumento());
				GenerarPdfCabeceras generarPdfCabecera3 = new GenerarPdfCabeceras();
				generarPdfCabecera3.setCampo("FECHA");
				generarPdfCabecera3.setValor(fechaCotizacion);
				generarPdfCabeceras.add(generarPdfCabecera1);
				generarPdfCabeceras.add(generarPdfCabecera2);
				generarPdfCabeceras.add(generarPdfCabecera3);
				esnGenerarPdfRequest.setCabeceras(generarPdfCabeceras);
				List<GenerarPdfPreguntas> generarPdfPreguntas = new ArrayList<>();
				GenerarPdfPreguntas generarPdfPregunta = new GenerarPdfPreguntas();
				generarPdfPregunta.setPregunta("¿Cuáles son los planes a futuro?");
				generarPdfPregunta.setRespuesta(planesFuturo);
				generarPdfPreguntas.add(generarPdfPregunta);
				esnGenerarPdfRequest.setPreguntas(generarPdfPreguntas);
				List<String[]> cuadroDetalles = new ArrayList<>();
				String[] detalles1 = { "Cotización|10%", "¿Por cuántos años quisieras estar asegurado?|15%",
						"¿Por cuántos años quisieras pagar tu seguro?|15%",
						"¿Cuánto deseas invertir al mes, y qué moneda elegirías?|15%",
						"¿Qué porcentaje de devolución deseas recibir por tu inversión?|15%",
						"¿Cuál es la frecuencia de pago para su seguro?|15%",
						"¿El ahorro lo deseas recibir al término del periodo de pago o cobertura?|15%" };
				String[] detalles2 = { cotizacionCrmRequestDTO.getNumeroCotizacion(), aniosAsegurado, aniosPago,
						montoMes, porcentajeDevolucion, frecuenciaPago, ahorroDeseasRecibir };
				cuadroDetalles.add(detalles1);
				cuadroDetalles.add(detalles2);
				esnGenerarPdfRequest.setCuadroDetalle(cuadroDetalles);
				guardarServicioEdn(esnGenerarPdfRequest, cotizacionCrmRequestDTO.getUsuarioLogin());
			}
		} catch (Exception e) {
			LOGGER.error("generarPdfEdn {}", e.getMessage());
		}
	}

	@Async("processExecutor")
	public void guardarServicioEdn(GenerarPdfRequest generarPdfRequest, String usuarioLogin) {
		try {
			LOGGER.info("Ingresa a GuardarServicioEdn");
			LOGGER.info("request  --->" + gson.toJson(generarPdfRequest));
			Solicitud solicitud = solicitudRepository.findByNumeroCotizacion(generarPdfRequest.getSolicitud());
			EstudioNecesidadRestClient esnRestClient = new EstudioNecesidadRestClient();
			GenerarPdfResponse esnGenerarPdfResponse = new GenerarPdfResponse();

			Gson gson = new Gson();
			String jsonRequest = gson.toJson(generarPdfRequest);
			OkHttpClient client = new OkHttpClient().newBuilder().build();
			okhttp3.MediaType mediaType = okhttp3.MediaType.parse(Constantes.APPLICATION_JSON);
			RequestBody body = RequestBody.create(mediaType, jsonRequest);
			String credential = Credentials.basic(esnUser, esnPasword);

			ServicioEdn servicioEdn = new ServicioEdn();
			servicioEdn.setUsuario_crea(usuarioLogin);
			servicioEdn.setFecha_crea(new Date());
			servicioEdn.setSolicitud(generarPdfRequest.getSolicitud());
			servicioEdn.setRequest(gson.toJson(generarPdfRequest));

			Request request = new Request.Builder().url(getUrlEsnGeneraPdf()).method("POST", body)
					.addHeader("Authorization", credential)
					.addHeader(Constantes.CONTENT_TYPE, Constantes.APPLICATION_JSON).build();
			Response response = client.newCall(request).execute();
			esnGenerarPdfResponse.setCode(String.valueOf(response.code()));
			esnGenerarPdfResponse.setStatusHttp(String.valueOf(response.code()));
			esnGenerarPdfResponse.setMessage(response.message());
			LOGGER.info("response  --->" + response.message());
			if (response.code() == 200) {
				String rpta = response.body().string();
				esnGenerarPdfResponse.setData(rpta);
				servicioEdn.setFlag_edn("S");
				servicioEdn.setResponse(gson.toJson(esnGenerarPdfResponse));
				LOGGER.info("Respuesta servicio: " + gson.toJson(esnGenerarPdfResponse));
			} else {
				servicioEdn.setFlag_edn("N");
			}
			LOGGER.info("Guardando respuesta en tabla servicio_edn");
			servicioEdnRepository.save(servicioEdn); // servicioEdnRepository.flush();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
			System.out.println(e.getMessage());
		}
		LOGGER.info("Fin GuardarServicioEdn");
	}

	public ListObtenerIdPersonaResponse obtenerPersonaDocumento(ObtenerIdPersonaRequest obtenerIdPersonaRequest) {
		LOGGER.info("Entro a ObtenerPersonaDocumento(obtenerIdPersonaRequest)");
		ListObtenerIdPersonaResponse listObtenerIdPersonaResponse = new ListObtenerIdPersonaResponse();
		try {
			Gson gson = new Gson();
			String jsonRequest = gson.toJson(obtenerIdPersonaRequest);
			OkHttpClient client = new OkHttpClient().newBuilder().build();
			okhttp3.MediaType mediaType = okhttp3.MediaType.parse(Constantes.APPLICATION_JSON);
			RequestBody body = RequestBody.create(mediaType, jsonRequest);

			Request request = new Request.Builder().url(servidorApiDocumento + globalFindByDocumento)
					.method("POST", body).addHeader(Constantes.CONTENT_TYPE, Constantes.APPLICATION_JSON).build();
			Response response = client.newCall(request).execute();

			if (response.code() == 200) {
				String data = response.body().string();
				listObtenerIdPersonaResponse = gson.fromJson(data, ListObtenerIdPersonaResponse.class);
			}
		} catch (Exception e) {
			e.getStackTrace();
			LOGGER.error(e.getMessage());

		}
		LOGGER.info("Salió obtenerPersonaDocumento");
		return listObtenerIdPersonaResponse;
	}

	public SolicitudGuardarResponseDTO guardar(SolicitudGuardarRequestDTO solicitudGuardarRequestDTO) {
		String traza = Utilitarios.trazaLog() + "-" + solicitudGuardarRequestDTO.getNumeroCotizacion();
		LOGGER.info("[" + traza + "] Entro SolicitudServiceImpl#guardar(solicitudGuardarRequestDTO)");
		SolicitudGuardarResponseDTO response = new SolicitudGuardarResponseDTO();
		String codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_EXITO;
		String mensajeRespuesta = Strings.EMPTY;
		List<String> observaciones = new ArrayList<>();
		try {
			Solicitud solicitudBD = solicitudRepository.findById(solicitudGuardarRequestDTO.getIdSolicitud()).get();

			int estadoSolicitud = Integer.parseInt(solicitudBD.getEstado());
			int estadoPorTrasmitir = Integer.parseInt(Constantes.CODIGO_SOLICITUD_POR_TRANSMITIR);
			observaciones.add("Estado actual: " + estadoSolicitud);
			LOGGER.info("[" + traza + "] Estado Actual: " + estadoSolicitud);
			if (estadoSolicitud < estadoPorTrasmitir) {
				if (solicitudBD.getPersonaByIdAsegurado() != null) {
					Persona personaAseguradoBD = personaRepository
							.findById(solicitudBD.getPersonaByIdAsegurado().getIdPersona()).get();
					SolicitudDatosAdicionales solicitudDatosAdicionalesBD = solicitudDatosAdicionalesRepository
							.findByIdSolicitud(solicitudGuardarRequestDTO.getIdSolicitud());
					if (solicitudDatosAdicionalesBD == null) {
						solicitudDatosAdicionalesBD = new SolicitudDatosAdicionales();
					}
					solicitudDatosAdicionalesBD.setSolicitud(solicitudBD);
					if (personaAseguradoBD != null) {

						SolicitudGuardarAseguradoRequestDTO aseguradoRequest = solicitudGuardarRequestDTO
								.getAsegurado();
						observaciones.add("Asegurado: " + gson.toJson(aseguradoRequest));
						if (aseguradoRequest != null) {
							if (aseguradoRequest.getEstadoCivil() != null) {
								personaAseguradoBD.setEstadoCivil(aseguradoRequest.getEstadoCivil());
							}
							if (aseguradoRequest.getNacionalidad() != null) {
								personaAseguradoBD.setNacionalidad(aseguradoRequest.getNacionalidad());
							}
							if (aseguradoRequest.getDireccionTipo() != null) {
								personaAseguradoBD.setDireccionTipo(aseguradoRequest.getDireccionTipo());
							}
							if (aseguradoRequest.getDireccionNombreVia() != null) {
								personaAseguradoBD
										.setDireccionTipoDes(aseguradoRequest.getDireccionNombreVia().toUpperCase());
							}
							if (aseguradoRequest.getDireccionNroMz() != null) {
								personaAseguradoBD
										.setDireccionNroMz(aseguradoRequest.getDireccionNroMz().toUpperCase());
							}
							if (aseguradoRequest.getDireccionInterior() != null) {
								personaAseguradoBD
										.setDireccionInterior(aseguradoRequest.getDireccionInterior().toUpperCase());
							}
							if (aseguradoRequest.getDireccionUrbanizacion() != null) {
								personaAseguradoBD.setDireccionUrbanizacion(
										aseguradoRequest.getDireccionUrbanizacion().toUpperCase());
							}
							if (aseguradoRequest.getDireccionDepartamento() != null) {
								personaAseguradoBD.setDepartamento(aseguradoRequest.getDireccionDepartamento());
							}
							if (aseguradoRequest.getDireccionProvincia() != null) {
								personaAseguradoBD.setProvincia(aseguradoRequest.getDireccionProvincia());
							}
							if (aseguradoRequest.getDireccionDistrito() != null) {
								personaAseguradoBD.setDistrito(aseguradoRequest.getDireccionDistrito());
							}
							if (aseguradoRequest.getCentroTrabajo() != null) {
								personaAseguradoBD.setCentroTrabajo(aseguradoRequest.getCentroTrabajo().toUpperCase());
							}
							if (aseguradoRequest.getActividadEconomica() != null) {
								personaAseguradoBD.setActividadEconomica(aseguradoRequest.getActividadEconomica());
								solicitudDatosAdicionalesBD
										.setActividadEconomicaAsegurado(aseguradoRequest.getActividadEconomica());
							}
							if (aseguradoRequest.getLugarTrabajo() != null) {
								personaAseguradoBD.setLugarTrabajo(aseguradoRequest.getLugarTrabajo());
								solicitudDatosAdicionalesBD
										.setLugarTrabajoAsegurado(aseguradoRequest.getLugarTrabajo());
							}
							if (aseguradoRequest.getIngresoMoneda() != null) {
								personaAseguradoBD.setIngresoMoneda(aseguradoRequest.getIngresoMoneda());
							}
							if (aseguradoRequest.getIngresoValor() != null) {
								personaAseguradoBD.setIngresoValor(aseguradoRequest.getIngresoValor());
							}
							if (aseguradoRequest.getProfesion() != null) {
								personaAseguradoBD.setProfesion(aseguradoRequest.getProfesion());
								solicitudDatosAdicionalesBD.setProfesionAsegurado(aseguradoRequest.getProfesion());
							}
							if (aseguradoRequest.getProfesionDetalle() != null) {
								personaAseguradoBD
										.setProfesionDetalle(aseguradoRequest.getProfesionDetalle().toUpperCase());
							}
							if (aseguradoRequest.getEsPEP() != null) {
								personaAseguradoBD.setEsPep(aseguradoRequest.getEsPEP());
								solicitudDatosAdicionalesBD.setEsPepAsegurado(aseguradoRequest.getEsPEP());
							}
							if (aseguradoRequest.getEsSujetoObligado() != null) {
								personaAseguradoBD.setEsSujetoObligado(aseguradoRequest.getEsSujetoObligado());
								solicitudDatosAdicionalesBD
										.setEsSujetoObligadoAsegurado(aseguradoRequest.getEsSujetoObligado());
							}

							personaAseguradoBD.setUsuarioModif(solicitudGuardarRequestDTO.getUsuarioLogin());
							personaAseguradoBD.setFechaModif(new Date());

							personaRepository.save(personaAseguradoBD);
							LOGGER.info("[" + traza + "] Guardando datos en la tabla Persona");

						}
						List<SolicitudGuardarBeneficiarioRequestDTO> beneficiariosRequest1 = solicitudGuardarRequestDTO
								.getBeneficiarios();

						List<SolicitudGuardarBeneficiarioRequestDTO> beneficiariosRequest = filtrarBeneficiarios(
								beneficiariosRequest1);
						if (beneficiariosRequest != null && !beneficiariosRequest.isEmpty()) {

							// Inicio - Eliminar las creadas previamente
							List<SolicitudBeneficiario> listaBeneficarios = solicitudBeneficiarioRepository
									.findByIdSolicitud(solicitudBD.getIdSolicitud());
							solicitudBeneficiarioRepository.deleteAll(listaBeneficarios);
							// Fin Eliminar las creadas previamente

							for (SolicitudGuardarBeneficiarioRequestDTO beneficiarioRequest : beneficiariosRequest) {
								SolicitudBeneficiario solicitudBeneficiarioBD = new SolicitudBeneficiario();
								solicitudBeneficiarioBD.setSolicitud(solicitudBD);
								observaciones.add("Beneficiario: " + gson.toJson(beneficiariosRequest));
								if (beneficiarioRequest.getTipoBeneficiario() != null
										&& !beneficiarioRequest.getTipoBeneficiario().equals(Strings.EMPTY)) {
									solicitudBeneficiarioBD
											.setTipoBeneficiario(beneficiarioRequest.getTipoBeneficiario());

									if (beneficiarioRequest.getApellidoPaterno() != null) {
										solicitudBeneficiarioBD.setApellidoPaterno(
												beneficiarioRequest.getApellidoPaterno().toUpperCase());
									}
									if (beneficiarioRequest.getApellidoMaterno() != null) {
										solicitudBeneficiarioBD.setApellidoMaterno(
												beneficiarioRequest.getApellidoMaterno().toUpperCase());
									}
									if (beneficiarioRequest.getNombres() != null) {
										solicitudBeneficiarioBD
												.setNombres(beneficiarioRequest.getNombres().toUpperCase());
									}
									if (beneficiarioRequest.getFechaNacimiento() != null) {
										Date fechaNacimientoOrigen = DateUtil.stringToDate(
												beneficiarioRequest.getFechaNacimiento(),
												DateUtil.FORMATO_DIA_DDMMYYYY);
										fechaNacimientoOrigen.setHours(6);// GTI.24117
										solicitudBeneficiarioBD.setFechaNacimiento(fechaNacimientoOrigen);
									}
									if (beneficiarioRequest.getTipoDocumento() != null) {
										solicitudBeneficiarioBD
												.setTipoDocumento(beneficiarioRequest.getTipoDocumento());
									}
									if (beneficiarioRequest.getNumeroDocumento() != null) {
										solicitudBeneficiarioBD
												.setNumeroDocumento(beneficiarioRequest.getNumeroDocumento());
									}
									if (beneficiarioRequest.getTipoRelacion() != null) {
										solicitudBeneficiarioBD.setTipoRelacion(beneficiarioRequest.getTipoRelacion());
									}
									if (beneficiarioRequest.getDistribucion() != null) {
										solicitudBeneficiarioBD.setDistribucion(beneficiarioRequest.getDistribucion());
									}

									solicitudBeneficiarioBD
											.setUsuarioCrea(solicitudGuardarRequestDTO.getUsuarioLogin());
									solicitudBeneficiarioBD.setFechaCrea(new Date());

									solicitudBeneficiarioRepository.save(solicitudBeneficiarioBD);
									LOGGER.info("[" + traza + "] Guardando datos en la tabla solicitudBeneficiario");
								}
							}

						}

						SolicitudGuardarDPSRequestDTO dpsRequest = solicitudGuardarRequestDTO.getDps();
						observaciones.add("Dps: " + gson.toJson(dpsRequest));
						if (dpsRequest != null) {
							SolicitudDps solicitudDpsBD = solicitudDpsRepositorioRepository
									.findByIdSolicitud(solicitudBD.getIdSolicitud());
							if (solicitudDpsBD == null) {
								solicitudDpsBD = new SolicitudDps();
								solicitudDpsBD.setSolicitud(solicitudBD);
								solicitudDpsBD.setUsuarioCrea(solicitudGuardarRequestDTO.getUsuarioLogin());
								solicitudDpsBD.setFechaCrea(new Date());
							} else {
								solicitudDpsBD.setUsuarioModif(solicitudGuardarRequestDTO.getUsuarioLogin());
								solicitudDpsBD.setFechaModif(new Date());
							}

							solicitudDpsBD.setPesoVariacionCantidad(5);

							if (dpsRequest.getEstatura() != null) {
								solicitudDpsBD.setEstatura(dpsRequest.getEstatura());
							}
							if (dpsRequest.getPeso() != null) {
								solicitudDpsBD.setPeso(dpsRequest.getPeso());
							}
							if (dpsRequest.getImc() != null) {
								solicitudDpsBD.setImc(dpsRequest.getImc());
							}
							if (dpsRequest.getPesoVariacion() != null) {
								solicitudDpsBD.setPesoVariacion(dpsRequest.getPesoVariacion());
							}
							if (dpsRequest.getPesoAumentoDisminuyo() != null) {
								solicitudDpsBD.setPesoAumentoDisminuyo(
										Utilitarios.checkOpcion(dpsRequest.getPesoAumentoDisminuyo().toString(),
												ConstantesSolicitudPDF.MULTITABLA_CHECK_TRUE));
							}
							if (dpsRequest.getPesoCantidad() != null) {
								solicitudDpsBD.setPesoAdCantidad(dpsRequest.getPesoCantidad());
							}
							if (dpsRequest.getPesoMotivo() != null) {
								solicitudDpsBD.setPesoMotivo(dpsRequest.getPesoMotivo().toUpperCase());
							}
							if (dpsRequest.getFumador() != null) {
								solicitudDpsBD.setFumador(Utilitarios.checkOpcion(dpsRequest.getFumador().toString(),
										ConstantesSolicitudPDF.MULTITABLA_CHECK_TRUE));

								String tipoRiesgo = ConstantesSolicitudPDF.MULTITABLA_PRODUCTO_TIPO_RIESGO_FUMADOR_NO;
								if (solicitudDpsBD.getFumador()) {
									tipoRiesgo = ConstantesSolicitudPDF.MULTITABLA_PRODUCTO_TIPO_RIESGO_FUMADOR_SI;
								}

								SolicitudProducto solicitudProducto = solicitudProductoRepository
										.findByIdSolicitud(solicitudBD.getIdSolicitud());
								if (solicitudProducto != null
										&& !solicitudProducto.getTipoRiesgo().equals(tipoRiesgo)) {

									solicitudProducto.setTipoRiesgo(tipoRiesgo);
									solicitudProducto.setUsuarioModif(solicitudGuardarRequestDTO.getUsuarioLogin());
									solicitudProducto.setFechaModif(new Date());

									solicitudProductoRepository.save(solicitudProducto);
									LOGGER.info("[" + traza + "] Guardando datos en la tabla SolicitudProducto");
								}
							}
							if (dpsRequest.getFumadorCantidad() != null) {
								solicitudDpsBD.setFumadorCantidad(dpsRequest.getFumadorCantidad().toUpperCase());
							}
							if (dpsRequest.getFumadorFrecuencia() != null) {
								solicitudDpsBD.setFumadorFrecuencia(dpsRequest.getFumadorFrecuencia().toUpperCase());
							}
							if (dpsRequest.getDrogas() != null) {
								solicitudDpsBD.setDrogas(Utilitarios.checkOpcion(dpsRequest.getDrogas().toString(),
										ConstantesSolicitudPDF.MULTITABLA_CHECK_TRUE));
							}
							if (dpsRequest.getDrogasFecha() != null) {
								solicitudDpsBD.setDrogasFecha(dpsRequest.getDrogasFecha().toUpperCase());
							}
							if (dpsRequest.getAlcohol() != null) {
								solicitudDpsBD.setAlcohol(Utilitarios.checkOpcion(dpsRequest.getAlcohol().toString(),
										ConstantesSolicitudPDF.MULTITABLA_CHECK_TRUE));
							}
							if (dpsRequest.getAlcoholCantidad() != null) {
								solicitudDpsBD.setAlcoholCantidad(dpsRequest.getAlcoholCantidad().toUpperCase());
							}
							if (dpsRequest.getAlcoholFrecuencia() != null) {
								solicitudDpsBD.setAlcoholFrecuencia(dpsRequest.getAlcoholFrecuencia().toUpperCase());
							}

							solicitudDpsRepositorioRepository.save(solicitudDpsBD);
							LOGGER.info("[" + traza + "] Guardando datos en la tabla solicitudDps");

							List<SolicitudGuardarDPSPreguntaRequestDTO> dpsPreguntasRequest = solicitudGuardarRequestDTO
									.getDps().getPreguntas();
							if (dpsPreguntasRequest != null && !dpsPreguntasRequest.isEmpty()) {
								observaciones.add("DPS preguntas: " + dpsPreguntasRequest.size());
								// Inicio - Eliminar las creadas previamente
								List<SolicitudDpsPregunta> listaPreguntas = solicitudDpsPreguntaRepository
										.findByIdSolicitud(solicitudBD.getIdSolicitud());

								solicitudDpsPreguntaRepository.deleteAll(listaPreguntas);
								// Fin Eliminar las creadas previamente
								observaciones.add("Preguntas: " + gson.toJson(dpsPreguntasRequest));
								for (SolicitudGuardarDPSPreguntaRequestDTO dpsPreguntaRequest : dpsPreguntasRequest) {
									String pregunta = dpsPreguntaRequest.getPregunta();
									int bloque = 0;

									if (StringUtils.isNotBlank(pregunta)) {
										int preguntaInt = Integer.parseInt(pregunta);
										if (preguntaInt >= 1 && preguntaInt <= 7)
											bloque = 1;
										if (preguntaInt >= 8 && preguntaInt <= 12)
											bloque = 2;
									}

									SolicitudDpsPregunta solicitudDpsPreguntaBD = new SolicitudDpsPregunta();
									solicitudDpsPreguntaBD.setSolicitudDps(solicitudDpsBD);
									solicitudDpsPreguntaBD.setbloquePregunta(bloque);
									solicitudDpsPreguntaBD.setItem(pregunta);
									solicitudDpsPreguntaBD.setPregunta(pregunta);
									solicitudDpsPreguntaBD
											.setRespuesta(Utilitarios.checkOpcion(dpsPreguntaRequest.getRespuesta(),
													ConstantesSolicitudPDF.MULTITABLA_CHECK_TRUE));
									solicitudDpsPreguntaBD.setDetalle(
											Utilitarios.valorString(dpsPreguntaRequest.getDetalle()).toUpperCase());
									solicitudDpsPreguntaBD.setEnfermedad(
											Utilitarios.valorString(dpsPreguntaRequest.getEnfermedad()).toUpperCase());
									solicitudDpsPreguntaBD.setFechaDiagnostico(Utilitarios
											.valorString(dpsPreguntaRequest.getFechaDiagnostico()).toUpperCase());
									solicitudDpsPreguntaBD.setCondicionActual(Utilitarios
											.valorString(dpsPreguntaRequest.getCondicionActual()).toUpperCase());
									solicitudDpsPreguntaBD.setNombreMedicoHospital(Utilitarios
											.valorString(dpsPreguntaRequest.getNombreMedicoHospital()).toUpperCase());

									solicitudDpsPreguntaBD.setUsuarioCrea(solicitudGuardarRequestDTO.getUsuarioLogin());
									solicitudDpsPreguntaBD.setFechaCrea(new Date());

									solicitudDpsPreguntaRepository.save(solicitudDpsPreguntaBD);
									LOGGER.info("[" + traza + "] Guardando datos en la tabla solicitudDpsPreguntas");
								}
							}

							List<SolicitudDpsPregunta> listaPreguntas = solicitudDpsPreguntaRepository
									.findByIdSolicitud(solicitudBD.getIdSolicitud());
							if (listaPreguntas != null && listaPreguntas.size() != 12) {

								StringBuilder mensaje = new StringBuilder();
								mensaje.append("[SolicitudPDFServiceImpl.generarSolicitudPDF] Solicitud: ");
								mensaje.append(solicitudBD.getIdSolicitud());
								mensaje.append(" - Error DPS Preguntas no tiene 12 preguntas");

								observaciones.add("Problema en pregunta DPS: " + mensaje.toString());
								/*
								 * this.enviarCorreoError( dpsPreguntasRequest, "SivTXException",
								 * mensaje.toString(), "Error al grabar datos de preguntas dps" );
								 */
							}

						}

						if (!solicitudGuardarRequestDTO.getConsentimientoContratante().equals(Constantes.VALOR_VACIO)) {
							solicitudBD.setTratamientoContratanteCheck(
									Integer.parseInt(solicitudGuardarRequestDTO.getConsentimientoContratante()));
						}

						if (!solicitudGuardarRequestDTO.getConsentimientoAsegurado().equals(Constantes.VALOR_VACIO)) {
							solicitudBD.setTratamientoAseguradoCheck(
									Integer.parseInt(solicitudGuardarRequestDTO.getConsentimientoAsegurado()));
						}

						if (solicitudGuardarRequestDTO.getAseguradoIgualContratante().equals("2")) {
							if (solicitudGuardarRequestDTO.getContratante() != null) {
								String jsonContratante = gson.toJson(solicitudGuardarRequestDTO.getContratante());
								solicitudBD.setJsonContratante(jsonContratante);

								if (solicitudGuardarRequestDTO.getContratante().getLugarTrabajo() != null) {
									solicitudDatosAdicionalesBD.setLugarTrabajoContratante(
											solicitudGuardarRequestDTO.getContratante().getLugarTrabajo());
								}

								if (solicitudGuardarRequestDTO.getContratante().getActividadEconomica() != null) {
									solicitudDatosAdicionalesBD.setActividadEconomicaContratante(
											solicitudGuardarRequestDTO.getContratante().getActividadEconomica());
								}

								if (solicitudGuardarRequestDTO.getContratante().getProfesion() != null) {
									solicitudDatosAdicionalesBD.setProfesionContratante(
											solicitudGuardarRequestDTO.getContratante().getProfesion());
								}

								if (solicitudGuardarRequestDTO.getContratante().getEsPEP() != null) {
									solicitudDatosAdicionalesBD.setEsPepContratante(
											solicitudGuardarRequestDTO.getContratante().getEsPEP());
								}

								if (solicitudGuardarRequestDTO.getContratante().getEsSujetoObligado() != null) {
									solicitudDatosAdicionalesBD.setEsSujetoObligadoContratante(
											solicitudGuardarRequestDTO.getContratante().getEsSujetoObligado());
								}
							}
							if (solicitudGuardarRequestDTO.getVinculoAsegurado() != null) {
								solicitudBD.setVinculoAsegurado(solicitudGuardarRequestDTO.getVinculoAsegurado());
							}
						} else {
							solicitudBD.setJsonContratante(null);
							solicitudBD.setVinculoAsegurado("");
						}

						solicitudBD.setAseguradoIgualContratante(
								solicitudGuardarRequestDTO.getAseguradoIgualContratante());
						solicitudBD.setUsuarioModif(solicitudGuardarRequestDTO.getUsuarioLogin());
						solicitudBD.setFlagExclusionCobAcc(solicitudGuardarRequestDTO.getFlagExclusionCobAcc());
						solicitudBD.setFechaModif(new Date());
						solicitudDatosAdicionalesRepository.save(solicitudDatosAdicionalesBD);
						solicitudRepository.save(solicitudBD);
						LOGGER.info("[" + traza + "] Guardando datos en la tabla Solicitud");

					}
				}

			} // fin estado solicitud

			response.setAseguradoIgualContratante(Utilitarios.valorString(solicitudBD.getAseguradoIgualContratante()));
			response.setIdSolicitud(solicitudBD.getIdSolicitud());
			response.setNumeroCotizacion(solicitudBD.getNumeroCotizacion());
			response.setEstadoSolicitud(solicitudBD.getEstado());

			if (solicitudBD.getFechaFirmaAsegurado() != null) {
				response.setFechaFirmaAsegurado(DateUtil.dateToString(solicitudBD.getFechaFirmaAsegurado(),
						DateUtil.FORMATO_DIA_DDMMYYYY_HHMMSS));
			}

			if (solicitudBD.getFechaFirmaContratante() != null) {
				response.setFechaFirmaContratante(DateUtil.dateToString(solicitudBD.getFechaFirmaContratante(),
						DateUtil.FORMATO_DIA_DDMMYYYY_HHMMSS));
			}

		} catch (Exception ex) {
			LOGGER.error("[" + traza + "] Error guardar =>" + ex.getMessage());
			mensajeRespuesta = Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_SOLICITUD_ERROR_GUARDAR);
			codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_ERROR;
			response.setObjErrorResource(
					new ErrorResourceDTO("ADN-007", "Error al guardar solicitud: " + ex.getMessage()));
		}
		response.setCodigoRespuesta(codigoRespuesta);
		response.setMensajeRespuesta(mensajeRespuesta);
		LOGGER.info("guardar.SolicitudGuardarResponseDTO =>: " + gson.toJson(response));
		LOGGER.info("[" + traza + "] Salio SolicitudServiceImpl#guardar(solicitudGuardarRequestDTO)");
		return response;
	}

	public List<SolicitudGuardarBeneficiarioRequestDTO> filtrarBeneficiarios(
			List<SolicitudGuardarBeneficiarioRequestDTO> lista) {
		List<SolicitudGuardarBeneficiarioRequestDTO> resultado = new ArrayList<>();
		if (lista != null && !lista.isEmpty()) {
			double sumaPorcentajes = 0.0;
			for (SolicitudGuardarBeneficiarioRequestDTO x : lista) {
				// Si tipoBeneficiario es 2, se añade a la lista sin importar el valor de
				// distribucion
				if (x.getTipoBeneficiario().equalsIgnoreCase("2")) {
					resultado.add(x);
					continue;
				}
				if (x.getDistribucion() == 0.0) {
					continue;
				}
				sumaPorcentajes += x.getDistribucion();
				if (sumaPorcentajes > 100.0) {
					break;
				}
				resultado.add(x);
			}
		}
		return resultado;
	}

	@Override
	public boolean enviarSMS(SolicitudSMSRequestDTO solicitudSMSRequestDTO) {
		String codigoRespuesta = "01";
		String mensajeRespuesta = "";

		try {
			Solicitud solicitud = solicitudRepository.findById(solicitudSMSRequestDTO.getIdSolicitud()).get();

			if (solicitud != null) {
				if (solicitudSMSRequestDTO.getIdPersona() != null) {
					Persona persona = personaRepository.findById(solicitudSMSRequestDTO.getIdPersona()).get();
					this.sendSMS(solicitudSMSRequestDTO.getIdSolicitud(), persona,
							solicitudSMSRequestDTO.getUsuarioLogin());
					mensajeRespuesta = "OK";
				} else {
					codigoRespuesta = "99";
					mensajeRespuesta = "Persona no encontrada.";
					throw new SivSOAException("Persona no encontrada", null);
				}
			}

		} catch (Exception e) {
			codigoRespuesta = "99";
			mensajeRespuesta = "Error enviarSMS: " + e.getMessage();
			// TODO Auto-generated catch block
			LOGGER.info("Error enviarSMS: " + e);

			return false;
		}
		ErrorResourceDTO error = new ErrorResourceDTO("", "");
		List<String> observaciones = new ArrayList<>();
		/*
		 * logRepository .save(new EventLog("Solicitud", codigoRespuesta,
		 * solicitudSMSRequestDTO.getPath(), mensajeRespuesta, gson.toJson(error),
		 * gson.toJson(observaciones), solicitudSMSRequestDTO.getUsuarioLogin(),
		 * solicitudSMSRequestDTO.getDevice(), solicitudSMSRequestDTO.getOs()));
		 */
		return true;
	}

	@Override
	public BaseResponseDTO validarCodigoAseguradoyContratante(
			SolicitudValidarCodigoRequestDTO solicitudValidarCodigoRequestDTO) {

		// codigoVerificacionRepository.updateExpired();

		BaseResponseDTO response = new BaseResponseDTO();

		Solicitud solicitud = solicitudRepository.findById(solicitudValidarCodigoRequestDTO.getIdSolicitud()).get();

		int valid = 1;
		List<CodigoVerificacion> lista = new ArrayList<>();

		String messageError = "";
		try {
			valid = valid * this.validateCode(solicitud.getIdSolicitud(), solicitud.getIdAsegurado(),
					solicitudValidarCodigoRequestDTO.getCodigoAsegurado(), lista, "asegurado",
					solicitudValidarCodigoRequestDTO.getUsuarioLogin());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			messageError = messageError.concat(e.getMessage());
			valid = 0;
		}

		if (solicitud.getIdContratante() != null) {
			try {
				valid = valid * this.validateCode(solicitud.getIdSolicitud(), solicitud.getIdContratante(),
						solicitudValidarCodigoRequestDTO.getCodigoContratante(), lista, "contratante",
						solicitudValidarCodigoRequestDTO.getUsuarioLogin());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				messageError = messageError.concat(e.getMessage());
				valid = 0;
			}
		}

		if (!serverPort.equals(Constantes.SERVER_PORT_PRODUCCION)) {
			valid = 1;
		}

		if (valid == 1) {
			lista.forEach(codigoVerificacion -> codigoVerificacionRepository.save(codigoVerificacion));

			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
			response.setMensajeRespuesta(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_EXITO));
		} else {
			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			response.setMensajeRespuesta(messageError);
		}

		return response;
	}

	@Override
	public BaseResponseDTO obtenerURLVidaFree(TokenRequestDTO tokenRequestDTO) {
		BaseResponseDTO response = new BaseResponseDTO();

		ObjectMapper om = new ObjectMapper();

		String tokenJson;
		try {
			Map<String, Object> dict = new HashMap();
			dict.put("tipoDocumentoCliente", tokenRequestDTO.getTipoDocumentoCliente());
			dict.put("numeroDocumentoCliente", tokenRequestDTO.getNumeroDocumentoCliente());
			dict.put("nombreCliente", tokenRequestDTO.getNombreCliente());
			dict.put("edadActuarial", tokenRequestDTO.getEdadActuarial());
			dict.put("fechaNacimiento", tokenRequestDTO.getFechaNacimiento());
			dict.put("sexo", tokenRequestDTO.getSexo());
			dict.put("idUsuarioCrm", tokenRequestDTO.getIdUsuarioCrm());
			dict.put("nombreUsuario", tokenRequestDTO.getNombreUsuario());
			dict.put("rol", tokenRequestDTO.getRol());
			// dict.put("numeroCotizacion", tokenRequestDTO.getNumeroCotizacion());
			dict.put("fechaCotizacion", tokenRequestDTO.getFechaCotizacion());
			dict.put("idOportunidadCrm", tokenRequestDTO.getIdOportunidadCrm());
			dict.put("idCotizacionCrm", tokenRequestDTO.getIdCotizacionCrm());
			dict.put("flagIGV", tokenRequestDTO.getFlagIGV());

			if (tokenRequestDTO.getNumeroCotizacion() == "" || tokenRequestDTO.getNumeroCotizacion().equals("")) {
				try {
					ObjectMapper mapper = new ObjectMapper();
					AsegurableResponse bean = mapper.readValue(cotizadorRestClient.generarCorrelativo().getMsg(),
							AsegurableResponse.class);
					if (bean != null) {
						dict.put("numeroCotizacion", bean.getCotnum());
					}

				} catch (Exception e) {
					LOGGER.error("Problemas al invocar servicio de Cotizador[ObtenerCumulo]");
					LOGGER.error(e.getMessage());
					dict.put("numeroCotizacion", "");
					dict.put("cumuloMoneda", "");
				}
			} else {
				dict.put("numeroCotizacion", tokenRequestDTO.getNumeroCotizacion());
			}

			// Obteniendo ID Tercero
			String idTercero = "";
			try {
				String tipoDocumentoAcsele = tokenRequestDTO.getTipoDocumentoCliente() == "2" ? "PersonaJuridica"
						: "PersonaNatural";
				if (tokenRequestDTO.getNumeroDocumentoCliente() != null) {
					if (acseleRestClient.obtenerIDTercero(tipoDocumentoAcsele,
							tokenRequestDTO.getNumeroDocumentoCliente()) != null
							&& acseleRestClient
									.obtenerIDTercero(tipoDocumentoAcsele, tokenRequestDTO.getNumeroDocumentoCliente())
									.getErrores().isEmpty()
							&& acseleRestClient
									.obtenerIDTercero(tipoDocumentoAcsele, tokenRequestDTO.getNumeroDocumentoCliente())
									.getTerceros() != null) {
						idTercero = acseleRestClient
								.obtenerIDTercero(tipoDocumentoAcsele, tokenRequestDTO.getNumeroDocumentoCliente())
								.getTerceros().get(0).getIdTercero();
					}
					;
				}
			} catch (Exception e) {
				LOGGER.error("Problemas al invocar servicio de Acsele[ObtenerIdTercero]");
				LOGGER.error(e.getMessage());
			}

			if (idTercero != "") {
				try {
				} catch (Exception e) {
					LOGGER.error("Problemas al invocar servicio de Cotizador[ObtenerCumulo]");
					LOGGER.error(e.getMessage());
					dict.put("cumuloMonto", "");
					dict.put("cumuloMoneda", "");
				}
			} else {
				dict.put("cumuloMonto", "");
				dict.put("cumuloMoneda", "");
			}

			tokenJson = om.writeValueAsString(dict);
			response.setCodigoRespuesta("01");
			response.setMensajeRespuesta(urlCotizacionVidaFree + this.encrypt(tokenJson));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.setCodigoRespuesta("99");
			response.setMensajeRespuesta("Ocurrio un erro al generar token.");
		}

		return response;
	}

	public SolicitudGuardarResponseDTO completarSolicitudFirmar(Long idSolicitud, String usuarioLogin,
			SolicitudGuardarResponseDTO respuesta) {
		// BaseResponseDTO respuesta = new BaseResponseDTO();
		String codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_EXITO;
		String mensajeRespuesta = Strings.EMPTY;
		Long idContratante = null;

		try {
			Solicitud solicitudBD = solicitudRepository.findById(idSolicitud).get();
			SolicitudDatosAdicionales solicitudDatosAdicionalesBD = solicitudDatosAdicionalesRepository
					.findByIdSolicitud(idSolicitud);

			Integer estadoSolicitud = Integer.valueOf(solicitudBD.getEstado());
			Integer estadoPorTrasmitir = Integer.valueOf(Constantes.CODIGO_SOLICITUD_POR_TRANSMITIR);

			if (estadoSolicitud < estadoPorTrasmitir) {
				String jsonContratante = solicitudBD.getJsonContratante();
				if (jsonContratante != null && !jsonContratante.equals(Strings.EMPTY)) {
					SolicitudFormularioContratanteResponseDTO contratanteResponse = gson.fromJson(jsonContratante,
							SolicitudFormularioContratanteResponseDTO.class);
					if (contratanteResponse != null
							&& !Utilitarios.valorString(contratanteResponse.getTipoDocumento()).equals(Strings.EMPTY)
							&& !Utilitarios.valorString(contratanteResponse.getNumeroDocumento())
									.equals(Strings.EMPTY)) {

						Persona personaContratanteBD = personaRepository.findByTipoNumeroDocumento(
								contratanteResponse.getTipoDocumento(), contratanteResponse.getNumeroDocumento());

						if (personaContratanteBD != null) {
							personaContratanteBD.setUsuarioModif(usuarioLogin);
							personaContratanteBD.setFechaModif(new Date());
						} else {
							personaContratanteBD = new Persona();
							personaContratanteBD.setUsuarioCrea(usuarioLogin);
							personaContratanteBD.setFechaCrea(new Date());
							personaContratanteBD.setTipoDocumento(contratanteResponse.getTipoDocumento());
							personaContratanteBD.setNumeroDocumento(contratanteResponse.getNumeroDocumento());
						}

						personaContratanteBD
								.setNombres(Utilitarios.valorString(contratanteResponse.getNombres()).toUpperCase());
						personaContratanteBD
								.setFumador(ConstantesSolicitudPDF.MULTITABLA_PRODUCTO_TIPO_RIESGO_FUMADOR_NO);
						personaContratanteBD.setApellidoPaterno(
								Utilitarios.valorString(contratanteResponse.getApellidoPaterno()).toUpperCase());
						personaContratanteBD.setApellidoMaterno(
								Utilitarios.valorString(contratanteResponse.getApellidoMaterno()).toUpperCase());
						personaContratanteBD.setRazonSocial(
								Utilitarios.valorString(contratanteResponse.getRazonSocial()).toUpperCase());
						personaContratanteBD.setFechaNacimiento(DateUtil
								.stringToDate(contratanteResponse.getFechaNacimiento(), DateUtil.FORMATO_DIA_DDMMYYYY));

						String generoContratante = ConstantesSolicitudPDF.MULTITABLA_GENERO_MASCULINO;
						if (!Utilitarios.valorString(contratanteResponse.getGenero()).equals(Strings.EMPTY)) {
							generoContratante = contratanteResponse.getGenero();
						}
						personaContratanteBD.setGenero(generoContratante);

						if (contratanteResponse.getCelular() != null
								&& !contratanteResponse.getCelular().equals(Strings.EMPTY)) {
							personaContratanteBD.setCelular(Integer.parseInt(contratanteResponse.getCelular()));
						}
						if (contratanteResponse.getCorreo() != null) {
							personaContratanteBD.setCorreo(contratanteResponse.getCorreo().toLowerCase());
						}
						if (contratanteResponse.getEstadoCivil() != null) {
							personaContratanteBD.setEstadoCivil(contratanteResponse.getEstadoCivil());
						}
						if (contratanteResponse.getNacionalidad() != null) {
							personaContratanteBD.setNacionalidad(contratanteResponse.getNacionalidad());
						}
						if (contratanteResponse.getDireccionTipo() != null) {
							personaContratanteBD.setDireccionTipo(contratanteResponse.getDireccionTipo());
						}
						if (contratanteResponse.getDireccionNombreVia() != null) {
							personaContratanteBD.setDireccionTipoDes(
									Utilitarios.valorString(contratanteResponse.getDireccionNombreVia()).toUpperCase());
						}
						if (contratanteResponse.getDireccionNroMz() != null) {
							personaContratanteBD.setDireccionNroMz(
									Utilitarios.valorString(contratanteResponse.getDireccionNroMz()).toUpperCase());
						}
						if (contratanteResponse.getDireccionInterior() != null) {
							personaContratanteBD.setDireccionInterior(
									Utilitarios.valorString(contratanteResponse.getDireccionInterior()).toUpperCase());
						}
						if (contratanteResponse.getDireccionUrbanizacion() != null) {
							personaContratanteBD.setDireccionUrbanizacion(Utilitarios
									.valorString(contratanteResponse.getDireccionUrbanizacion()).toUpperCase());
						}
						if (contratanteResponse.getDireccionDepartamento() != null) {
							personaContratanteBD.setDepartamento(contratanteResponse.getDireccionDepartamento());
						}
						if (contratanteResponse.getDireccionProvincia() != null) {
							personaContratanteBD.setProvincia(contratanteResponse.getDireccionProvincia());
						}
						if (contratanteResponse.getDireccionDistrito() != null) {
							personaContratanteBD.setDistrito(contratanteResponse.getDireccionDistrito());
						}
						if (contratanteResponse.getCentroTrabajo() != null) {
							personaContratanteBD.setCentroTrabajo(
									Utilitarios.valorString(contratanteResponse.getCentroTrabajo()).toUpperCase());
						}
						if (contratanteResponse.getActividadEconomica() != null) {
							personaContratanteBD.setActividadEconomica(contratanteResponse.getActividadEconomica());
							solicitudDatosAdicionalesBD
									.setActividadEconomicaContratante(contratanteResponse.getActividadEconomica());
						}
						if (contratanteResponse.getIngresoMoneda() != null) {
							personaContratanteBD.setIngresoMoneda(contratanteResponse.getIngresoMoneda());
						}
						if (contratanteResponse.getIngresoValor() != null
								&& !contratanteResponse.getIngresoValor().equals(Strings.EMPTY)) {
							personaContratanteBD
									.setIngresoValor(Integer.parseInt(contratanteResponse.getIngresoValor()));
						}
						if (contratanteResponse.getProfesion() != null) {
							personaContratanteBD.setProfesion(contratanteResponse.getProfesion());
							solicitudDatosAdicionalesBD.setProfesionContratante(contratanteResponse.getProfesion());
						}
						if (contratanteResponse.getProfesionDetalle() != null) {
							personaContratanteBD
									.setProfesionDetalle(contratanteResponse.getProfesionDetalle().toUpperCase());
						}
						if (contratanteResponse.getEsPEP() != null) {
							personaContratanteBD.setEsPep(contratanteResponse.getEsPEP());
							solicitudDatosAdicionalesBD.setEsPepContratante(contratanteResponse.getEsPEP());
						}
						if (contratanteResponse.getEsSujetoObligado() != null) {
							personaContratanteBD.setEsSujetoObligado(contratanteResponse.getEsSujetoObligado());
							solicitudDatosAdicionalesBD
									.setEsSujetoObligadoContratante(contratanteResponse.getEsSujetoObligado());
						}

						try{
							int edadActurial = calcularEdadActurial(contratanteResponse.getFechaNacimiento());
							personaContratanteBD.setEdadActuarial(edadActurial);
						}catch (Exception ex){
							LOGGER.info("Error calculando edad actuarial contratante:" + ex.getMessage());
						}

						personaRepository.save(personaContratanteBD);
						idContratante = personaContratanteBD.getIdPersona();

					}
				}

				if (idContratante != null) {
					solicitudBD.setPersonaByIdContratante(new Persona());
					solicitudBD.getPersonaByIdContratante().setIdPersona(idContratante);
				}

				if (solicitudBD.getEstado().equals(Constantes.CODIGO_SOLICITUD_PENDIENTE)) {
					solicitudBD.setEstado(Constantes.CODIGO_SOLICITUD_PENDIENTE_FIRMA);
				}
				solicitudRepository.save(solicitudBD);
				solicitudDatosAdicionalesRepository.save(solicitudDatosAdicionalesBD);

			}

			respuesta.setAseguradoIgualContratante(Utilitarios.valorString(solicitudBD.getAseguradoIgualContratante()));

			// Devolver datos para SMS
			if (solicitudBD.getPersonaByIdContratante() != null) {
				Persona personaContratanteBD = personaRepository
						.findById(solicitudBD.getPersonaByIdContratante().getIdPersona()).get();

				respuesta.setContratante(new SolicitudGuardarPersonaResponseDTO());
				respuesta.getContratante().setIdPersona(personaContratanteBD.getIdPersona());
				respuesta.getContratante().setCelular(Utilitarios.valorString(personaContratanteBD.getCelular()));
				respuesta.getContratante()
						.setTipoDocumento(this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_TIPO_DOCUMENTO,
								personaContratanteBD.getTipoDocumento(), Constantes.MULTITABLA_COLUMNA_VALOR_AUXILIAR)
								.toUpperCase());
				respuesta.getContratante()
						.setNumeroDocumento(Utilitarios.valorString(personaContratanteBD.getNumeroDocumento()));
				respuesta.getContratante().setNombres(Utilitarios.valorString(personaContratanteBD.getNombres()));
				respuesta.getContratante()
						.setApellidoPaterno(Utilitarios.valorString(personaContratanteBD.getApellidoPaterno()));
				respuesta.getContratante()
						.setApellidoMaterno(Utilitarios.valorString(personaContratanteBD.getApellidoMaterno()));
				respuesta.getContratante()
						.setRazonSocial(Utilitarios.valorString(personaContratanteBD.getRazonSocial()));
			}

			if (solicitudBD.getPersonaByIdAsegurado() != null) {
				Persona personaAseguradoBD = personaRepository
						.findById(solicitudBD.getPersonaByIdAsegurado().getIdPersona()).get();
				respuesta.setAsegurado(new SolicitudGuardarPersonaResponseDTO());
				respuesta.getAsegurado().setIdPersona(personaAseguradoBD.getIdPersona());
				respuesta.getAsegurado().setCelular(Utilitarios.valorString(personaAseguradoBD.getCelular()));
				respuesta.getAsegurado()
						.setTipoDocumento(this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_TIPO_DOCUMENTO,
								personaAseguradoBD.getTipoDocumento(), Constantes.MULTITABLA_COLUMNA_VALOR_AUXILIAR)
								.toUpperCase());
				respuesta.getAsegurado()
						.setNumeroDocumento(Utilitarios.valorString(personaAseguradoBD.getNumeroDocumento()));
				respuesta.getAsegurado().setNombres(Utilitarios.valorString(personaAseguradoBD.getNombres()));
				respuesta.getAsegurado()
						.setApellidoPaterno(Utilitarios.valorString(personaAseguradoBD.getApellidoPaterno()));
				respuesta.getAsegurado()
						.setApellidoMaterno(Utilitarios.valorString(personaAseguradoBD.getApellidoMaterno()));
			}

		} catch (Exception ex) {
			LOGGER.info("Error completarSolicitudFirmar:" + ex);
			codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_ERROR;
			mensajeRespuesta = "Error, intente nuevamente.";
		}

		respuesta.setCodigoRespuesta(codigoRespuesta);
		respuesta.setMensajeRespuesta(mensajeRespuesta);

		return respuesta;
	}

	public CotizaDetalleResponseDTO detalleCotizacionVidaFree(Long nroCotizacion) {
		LOGGER.info("Entro CotizaDetalleResponseDTO#detalleCotizacionVidaFree(String nroCotizacion)");

		CotizaDetalleResponseDTO response = new CotizaDetalleResponseDTO();

		try {
			List<CotizacionDetalle> model = cotizacionRepository.detalleCotizacion2(nroCotizacion);
			LOGGER.info("Inicio solicitud ========>>>>>>");
			LOGGER.info(gson.toJson(model));
			if (model != null && model.size() > 0) {
				response.setCrmCotizadorId(model.get(0).getCrmCotizadorId());
				response.setCrmOportunidadId(model.get(0).getCrmOportunidadId());
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			response.setMensajeRespuesta(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_ERR0R));
		}

		try {
			ConsultaCotizacionDetalleRequest request = new ConsultaCotizacionDetalleRequest();

			request.setNumeroCotizacion(nroCotizacion.toString());
			request.setIdAgente("xxx");
			ConsultaCotizacionDetalleResponse cotizacionVF = vidaFreeRestClient.obtenerCotizacionDetalle(request);

			response.setNroCotizacion(cotizacionVF.getData().getDetalleCotizacion().getNumeroCotizacion());
			DateFormat sourceFormat = new SimpleDateFormat(DateUtil.FORMATO_DIA_DDMMYYYY);
			String dateAsString = cotizacionVF.getData().getDetalleCotizacion().getFechaCotizacion();
			Date date = sourceFormat.parse(dateAsString);
			response.setFechaCotizacion(date);
			// response.setFechaCotizacion(cotizacionVF.getData().getDetalleCotizacion().getFechaCotizacion());
			response.setMoneda(cotizacionVF.getData().getDetalleCotizacion().getCodMoneda());
			// response.setMontoTotalFondoGarantizado(cotizacion.getMontoTotalFondoGarantizado());
			response.setFrecuenciaPagoPrima(
					cotizacionVF.getData().getDetalleCotizacion().getFrecuenciaPagoDescripcion());
			response.setPeriodoCobertura(cotizacionVF.getData().getDetalleCotizacion().getPeriodoVigencia());
			response.setNroAnualidadesPagoBeneficio(cotizacionVF.getData().getDetalleCotizacion().getPeriodoPago());
			response.setPlanId(cotizacionVF.getData().getDetalleCotizacion().getCodPlan());
			response.setPlan(cotizacionVF.getData().getDetalleCotizacion().getNombrePlan());
			response.setPlanCuotaComodin("");
			response.setPlanCuotaDoble("");
			response.setFumador(cotizacionVF.getData().getCliente().getCondicionFumador());
			response.setPrimaComercialAnual(
					cotizacionVF.getData().getDetalleCotizacion().getPrimaClienteAnualTotal().floatValue());
			// response.setPrimaComercialAnual(cotizacionVF.getData().getDetalleCotizacion().getPrimaRecurrenteTotal().floatValue());
			response.setPrimaComercial(
					cotizacionVF.getData().getDetalleCotizacion().getPrimaRecurrenteTotal().floatValue());
			response.setFactorPago(Float.valueOf(cotizacionVF.getData().getDetalleCotizacion().getFactorPago()));
			// response.setPrimaComercial(Float.parseFloat(cotizacionVF.getData().getDetalleCotizacion().getPrimaRecurrenteTotal().toString()));
			response.setPrimaDevolucion(cotizacionVF.getData().getDetalleCotizacion().getPorcentajeDevolucion());
			response.setSubplanId(cotizacionVF.getData().getDetalleCotizacion().getCodPlan());
			response.setSubplan(cotizacionVF.getData().getDetalleCotizacion().getNombrePlan());
			response.setTirGarantizada(
					cotizacionVF.getData().getDetalleCotizacion().getPorcentajeDevolucion().toString());
			response.setPrimaIgv(cotizacionVF.getData().getDetalleCotizacion().getIgv().floatValue());
			// response.setMontoTotalFondoGarantizado(cotizacionVF.getData().getDetalleCotizacion().getPrimaRecurrenteTotal().floatValue());
			response.setMontoTotalFondoGarantizado(
					cotizacionVF.getData().getDetalleCotizacion().getMontoDevolucionTotal().floatValue());
			List<CotizaDetalleCoberturaResponseDTO> coberturas = new ArrayList<>();
			cotizacionVF.getData().getDetalleCotizacion().getCoberturas().forEach(c -> {
				coberturas.add(new CotizaDetalleCoberturaResponseDTO(c.getCodCobertura(),
						("1".equals(c.getCodCobertura()) ? "1" : "2"), c.getNombreCobertura(),
						c.getSumaAsegurada().floatValue(), c.getPrimaClienteAnual().floatValue()));
			});
			response.setCobertura(coberturas);

			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
			response.setMensajeRespuesta(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_EXITO));
			LOGGER.info("Response ====>>>>>");
			LOGGER.info(gson.toJson(response));
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			response.setMensajeRespuesta(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_ERR0R));
		}

		LOGGER.info("Salio CotizaDetalleResponseDTO#detalleCotizacionVidaFree(String nroCotizacion)");
		return response;
	}

	public SolicitudFinalizarProcesoResponseDTO finalizarProcesoSolicitud(
			SolicitudFinalizarProcesoRequestDTO solicitudFinalizarProcesoRequestDTO) {

		Solicitud solicitud = solicitudRepository.findById(solicitudFinalizarProcesoRequestDTO.getIdSolicitud()).get();
		String traza = Utilitarios.trazaLog() + "-" + solicitud.getNumeroCotizacion();
		LOGGER.info("[" + traza + "]" + " SolicitudFinalizarProcesoResponseDTO.finalizarProcesoSolicitud.INI");
		SolicitudFinalizarProcesoResponseDTO response = new SolicitudFinalizarProcesoResponseDTO();
		String mensajeRespuesta = Strings.EMPTY;
		String codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_ERROR;
		List<String> observaciones = new ArrayList<>();
		LOGGER.info("finalizarProcesoSolicitud.idSolicitud: " + solicitudFinalizarProcesoRequestDTO.getIdSolicitud());

		try {
			// aca ingresar codigo para guardar cotizacion
			int estadoSolicidu = Integer.parseInt(solicitud.getEstado());
			boolean estadoCrmCotiza = true;
			LOGGER.info("Estado Solicitud al finalizarProcesoSolicitud" + solicitud.getEstado());

			boolean finalizar = false;
			if (solicitud != null && estadoCrmCotiza) {
				/*if (solicitud.getFechaFirmaAsegurado() != null) {
					solicitud.setTratamientoAseguradoCheck(1);
				}
				if (solicitud.getFechaFirmaContratante() != null) {
					solicitud.setTratamientoContratanteCheck(1);
				}*/
				if ((solicitud.getAseguradoIgualContratante().equals("1")
						&& solicitud.getFechaFirmaAsegurado() != null)
						|| (solicitud.getAseguradoIgualContratante().equals("0")
								&& solicitud.getFechaFirmaAsegurado() != null
								&& solicitud.getFechaFirmaContratante() != null)) {
					solicitud.setEstado(Constantes.CODIGO_SOLICITUD_FIRMA_FINALIZADA);
				}
				if (solicitud.getEstado().equals(Constantes.CODIGO_SOLICITUD_POR_TRANSMITIR)
						|| solicitud.getEstado().equals(Constantes.CODIGO_SOLICITUD_PAGO_CULQI)
						|| solicitud.getEstado().equals(Constantes.CODIGO_SOLICITUD_PAGO_SAMP)) {
					LOGGER.info("[" + traza + "]" + "Estado al estado 4,5 o 6");
					codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_EXITO;
					mensajeRespuesta = Constantes.MENSAJE_RESPUESTA_GENERAL_EXITO;
					response.setNumeroPropuesta(solicitud.getNumeroCotizacion());
				} else if (solicitud.getEstado().equals(Constantes.CODIGO_SOLICITUD_FIRMA_FINALIZADA)) {
					LOGGER.info("[" + traza + "]" + "Estado en firma finalizada");
					if (solicitud.getAseguradoIgualContratante()
							.equals(Constantes.SOLICITUD_ASEGURADO_IGUAL_CONTRATANTE_TRUE)) {
						if (solicitud.getFechaFirmaAsegurado() != null) {
							finalizar = true;
						} else {
							throw new Exception("Favor de ingresar nuevamente a la solicitud y Finalizar.");
						}
					} else {
						if (solicitud.getFechaFirmaAsegurado() != null) {
							if (solicitud.getFechaFirmaContratante() != null) {
								finalizar = true;
							} else {
								throw new Exception("Favor de ingresar nuevamente a la solicitud y Finalizar.");
							}
						} else {
							throw new Exception("Solicitud no se encuentra firmada por el asegurado.");
						}
					}
				} else {
					LOGGER.info("[" + traza + "]" + "Estado de la firma" + solicitud.getEstado());
					throw new Exception("Solicitud no esta en estado Firma Finalizada.");
				}
			} else {
				throw new Exception("Solicitud no encontrada");
			}

			SolicitudProducto solicitudProducto = solicitudProductoRepository
					.findByIdSolicitud(solicitud.getIdSolicitud());

			// se asigna valores a request de consulta cotizacion
			ConsultaCotizacionDetalleRequest request0 = new ConsultaCotizacionDetalleRequest();
			request0.setIdAgente("");
			request0.setNumeroCotizacion(solicitud.getNumeroCotizacion());

			if ("2".equals(solicitudProducto.getTipoProducto()) && finalizar) {
				LOGGER.info("[" + traza + "]" + "=== PRODUCTO ES VIDA FREE ===");
				if (!Constantes.SOLICITUD_ASEGURADO_IGUAL_CONTRATANTE_TRUE
						.equals(solicitud.getAseguradoIgualContratante())) {
					LOGGER.info("[" + traza + "]" + "=== ASEGURADO DIFERENTE A CONTRATANTE ===");
					Solicitud solicitudBD = solicitudRepository.findById(solicitud.getIdSolicitud()).get();
					String tipoDocumentoCliente = "1";
					String flagIGV = "0";
					String jsonContratante = solicitudBD.getJsonContratante();
					if (jsonContratante != null && !jsonContratante.equals(Strings.EMPTY)) {
						SolicitudFormularioContratanteResponseDTO contratanteResponse = gson.fromJson(jsonContratante,
								SolicitudFormularioContratanteResponseDTO.class);
						if (contratanteResponse != null
								&& !Utilitarios.valorString(contratanteResponse.getTipoDocumento())
										.equals(Strings.EMPTY)
								&& !Utilitarios.valorString(contratanteResponse.getNumeroDocumento())
										.equals(Strings.EMPTY)) {
							tipoDocumentoCliente = contratanteResponse.getTipoDocumento();
							// corregir: se esta cobrando igv a diferente de dni
							if ("10".equals(tipoDocumentoCliente) || "11".equals(tipoDocumentoCliente)) {
								tipoDocumentoCliente = "2";
								flagIGV = "1";
							}
						}
					}
					if (tipoDocumentoCliente.equals("2")) {
						solicitud.setIdCrmCotizador("");

						LOGGER.info("[" + traza + "]" + "=== CONTRATANTE ES RUC ===");
						// Generando token

						LOGGER.info("=== OBTENIENDO COTIZACION VIDA FREE ===");
						ConsultaCotizacionDetalleResponse response0 = vidaFreeRestClient
								.obtenerCotizacionDetalle(request0);

						if (response0 == null || response0.getCode() == "99") {
							throw new Exception("No se pudo obtener información de cotización de vida free");
						}

						Persona personaAsegurado = personaRepository.findById(solicitud.getIdAsegurado()).get();
						TokenRequestDTO requestToken = new TokenRequestDTO();
						requestToken.setEdadActuarial(personaAsegurado.getEdadActuarial().toString());
						requestToken
								.setFechaCotizacion(response0.getData().getDetalleCotizacion().getFechaCotizacion());
						requestToken.setFechaNacimiento(DateUtil.dateToString(personaAsegurado.getFechaNacimiento(),
								DateUtil.FORMATO_DIA_DDMMYYYY));
						requestToken.setIdCotizacionCrm(solicitud.getIdSolicitud().toString());
						requestToken.setIdOportunidadCrm("99999");
						requestToken.setIdUsuarioCrm(response0.getData().getAgente().getId());
						requestToken.setNombreCliente(response0.getData().getCliente().getNombre());
						requestToken.setNombreUsuario(response0.getData().getAgente().getNombre());
						requestToken.setNumeroCotizacion(solicitud.getNumeroCotizacion());
						requestToken.setNumeroDocumentoCliente(response0.getData().getCliente().getNumeroDocumento());
						requestToken.setRol("AGENTE");
						requestToken.setSexo("M".equals(response0.getData().getCliente().getSexo()) ? "1" : "2");
						requestToken.setTipoDocumentoCliente(tipoDocumentoCliente);
						requestToken.setFlagIGV(flagIGV);
						requestToken.setToken("");
						LOGGER.info("=== OBTENIENDO TOKEN VIDA FREE ===");
						BaseResponseDTO responseToken = obtenerURLVidaFree(requestToken);
						String token = "";
						if (responseToken == null || responseToken.getCodigoRespuesta() == "99") {
							throw new Exception("No se pudo obtener token de vida free");
						} else {
							token = responseToken.getMensajeRespuesta()
									.substring(responseToken.getMensajeRespuesta().indexOf("=") + 1);
						}

						LOGGER.info("=== RECOTIZANDO ===>>>");
						String monedaCumulo = "";
						String montoCumulo = "0.0";
						GenericoResponse responseCotizacion = vidaFreeRestClient.guardarCotizacion(token,
								solicitud.getNumeroCotizacion(), "", monedaCumulo, montoCumulo, false);

						if (responseCotizacion == null || responseCotizacion.getCode() == null
								|| responseCotizacion.getCode() == "99") {
							finalizar = false;
							throw new Exception("Problemas al recotizar.");
						} else {
							LOGGER.info("=== Actualizando solicitud con datos de recotizacion ===");
							CotizaDetalleResponseDTO detalleNuevo = this
									.detalleCotizacionVidaFree(Long.valueOf(solicitud.getNumeroCotizacion()));
							LOGGER.info(gson.toJson(detalleNuevo));
							this.guardarSolicitud(detalleNuevo, solicitud.getPersonaByIdAsegurado(),
									solicitud.getUsuarioCrea(), requestToken.getNombreUsuario(), "", "",
									requestToken.getIdUsuarioCrm(), "VidaFree", true);
						}
					}
				}
			}

			if (finalizar) {
				LOGGER.info("Entrando en finalizar");

				Persona personaAsegurado = personaRepository.findById(solicitud.getIdAsegurado()).get();

				CotizacionCrmOportunidadRequestDTO requestOportunidad = new CotizacionCrmOportunidadRequestDTO();
				requestOportunidad.setUserId("");
				requestOportunidad.setOportunidadId("99999");
				List<String> arrayJson = new ArrayList<>();
				arrayJson.add(solicitud.getNumeroCotizacion());
				Gson json = new Gson();
				requestOportunidad.setJsonCotizaciones(json.toJson(arrayJson));

				solicitud.setIdCrmCotizador("");
				solicitud.setIdCrmOportunidad("");
				solicitud.setFechaSolicitud(new Date());
				LOGGER.info("Actualizando estado " + solicitud.getEstadoPropuesta() + "-->"
						+ (solicitud.getEstadoPropuesta() == null ? "estado propuesta vacio"
								: "successful evaluation"));
				solicitud.setDocumentoSolicitud(Constantes.CODIGO_SOLICITUD_POR_TRANSMITIR);
				solicitud.setUsuarioModif(solicitudFinalizarProcesoRequestDTO.getUsuarioLogin());
				solicitud.setFechaModif(new Date());
				solicitudRepository.save(solicitud);
				LOGGER.info("Se guarda los datos en la bd Solicitud");
				response.setNumeroPropuesta(solicitud.getNumeroPropuesta());

				// GTI.24247-INI - Se actualiza al campo fumador en base a la respuesta de la
				// DPS si fuma
				SolicitudDps solicitudDps = solicitudDpsRepositorioRepository
						.findByIdSolicitud(solicitud.getIdSolicitud());
				if (solicitudDps != null && solicitudDps.getFumador() != null) {
					String fumadorDPS = ConstantesSolicitudPDF.MULTITABLA_PRODUCTO_TIPO_RIESGO_FUMADOR_NO;

					if (solicitudDps.getFumador()) {
						fumadorDPS = ConstantesSolicitudPDF.MULTITABLA_PRODUCTO_TIPO_RIESGO_FUMADOR_SI;
					}

					if (personaAsegurado != null && personaAsegurado.getFumador() != null
							&& !personaAsegurado.getFumador().equals(fumadorDPS)) {
						personaAsegurado.setFumador(fumadorDPS);
						personaAsegurado.setUsuarioModif(solicitudFinalizarProcesoRequestDTO.getUsuarioLogin());
						personaAsegurado.setFechaModif(new Date());

						personaRepository.save(personaAsegurado);
						LOGGER.info("Se guarda los datos en la bd Persona");
					}
				}
				// GTI.24247-FIN
				LOGGER.info(solicitud.getDocumentoCotizacion());
				LOGGER.info(solicitud.getDocumentoSolicitud());
				solicitudRepository.save(solicitud);

				this.generarInformacionSolicitud(solicitud, solicitudFinalizarProcesoRequestDTO.getTipoProducto());

				codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_EXITO;

				try {
					DateFormat sourceFormat = new SimpleDateFormat(DateUtil.FORMATO_DIA_DDMMYYYY);
					CotizacionDetalle model = new CotizacionDetalle();

					String fechaCotizacion = new String();
					if (solicitudProducto.getTipoProducto().equals("2")) {
						ConsultaCotizacionDetalleResponse response0 = vidaFreeRestClient
								.obtenerCotizacionDetalle(request0);
						fechaCotizacion = response0.getData().getDetalleCotizacion().getFechaCotizacion();
					} else if (solicitudProducto.getTipoProducto().equals("1")) {
						model = cotizacionRepository.detalleCotizacion(Long.parseLong(solicitud.getNumeroPropuesta()))
								.get(0);
						fechaCotizacion = sourceFormat.format(model.getFechaCotizacion());
					}

					String fechaEDN_string = valorMultiTabla("060", "1", "valor");
					Date fechaEdnDate = sourceFormat.parse(fechaEDN_string);
					Date fechaCotizacionDate = sourceFormat.parse(fechaCotizacion);

					if (fechaCotizacionDate.compareTo(fechaEdnDate) >= 0) {

						// CarpetaCS
						Persona persona = personaRepository.findById(solicitud.getIdAsegurado()).get();
						String contactVal = persona.getIdPersonaCS();
						LOGGER.info("Fin PersonaxDocumento: " + contactVal);
						// obtener datos
						String planesFuturo = "";
						String ahorroDeseasRecibir = "";
						String frecuenciaPago = "";
						String porcentajeDevolucion = "";
						String montoMes = "";
						String aniosAsegurado = "";
						String aniosPago = "";

						if (solicitudProducto.getTipoProducto().equals("2")) {
							PlanFuturo planFuturoDomain = planFuturoRepository
									.findByIdPersona(personaAsegurado.getIdPersona());

							List<String> planes = new ArrayList<>();
							if (planFuturoDomain.getPlanEducacion() == 1)
								planes.add("Educación");
							if (planFuturoDomain.getPlanProyecto() == 1)
								planes.add("Sueños");
							if (planFuturoDomain.getPlanJubilacion() == 1)
								planes.add("Jubilación");
							planesFuturo = StringUtils.join(planes, ',');

							ConsultaCotizacionDetalleResponse response0 = vidaFreeRestClient
									.obtenerCotizacionDetalle(request0);
							aniosAsegurado = response0.getData().getDetalleCotizacion().getPeriodoVigencia().toString();
							aniosPago = response0.getData().getDetalleCotizacion().getPeriodoPago().toString();
							String moneda = response0.getData().getDetalleCotizacion().getMonedaDescripcion()
									.equalsIgnoreCase("Nuevo Sol") ? "S/. " : "$ ";
							montoMes = moneda
									+ response0.getData().getDetalleCotizacion().getPrimaRecurrenteTotal().toString();
							porcentajeDevolucion = response0.getData().getDetalleCotizacion().getPorcentajeDevolucion()
									.toString() + "%";
							frecuenciaPago = response0.getData().getDetalleCotizacion().getFrecuenciaPagoDescripcion();

							switch (solicitud.getSubplan()) {
							case "1":
								ahorroDeseasRecibir = "Fin de cobertura";
								break;
							case "2":
								ahorroDeseasRecibir = "Fin de periodo de cobertura";
								break;
							case "3":
								ahorroDeseasRecibir = "Fin de cobertura";
								break;
							default:
								break;
							}
						}
						else if (solicitudProducto.getTipoProducto().equals("1")) {
							switch (solicitud.getSubplan()) {
							case "1":
								planesFuturo = "Educación";
								break;
							case "2":
								planesFuturo = "Jubilación";
								break;
							case "3":
								planesFuturo = "Sueño";
								break;
							default:
								break;
							}
							frecuenciaPago = model.getFrecuenciaPagoPrima();
							String moneda = model.getMoneda().equalsIgnoreCase("SOLES") ? "S/. " : "$ ";
							montoMes = moneda + String.valueOf(model.getPrimaComercial());
							aniosAsegurado = String.valueOf(model.getPeriodoCobertura());
							aniosPago = String.valueOf(Integer.parseInt(model.getPer_cob()) / 12);
							porcentajeDevolucion = "No aplica";
							ahorroDeseasRecibir = "Fin de cobertura";
						}

						// consumir esn generar pdf
						GenerarPdfRequest esnGenerarPdfRequest = new GenerarPdfRequest();
						esnGenerarPdfRequest.setProducto("VIDA");
						esnGenerarPdfRequest.setRepositorioCS("CRM");
						esnGenerarPdfRequest.setCarpetaCS(contactVal.toLowerCase());
						esnGenerarPdfRequest.setSolicitud(solicitud.getNumeroPropuesta());
						esnGenerarPdfRequest.setNumerodocumento(personaAsegurado.getNumeroDocumento());
						esnGenerarPdfRequest.setCodigoPlantillaEN("1");
						List<GenerarPdfCabeceras> generarPdfCabeceras = new ArrayList<>();
						GenerarPdfCabeceras generarPdfCabecera1 = new GenerarPdfCabeceras();
						generarPdfCabecera1.setCampo("CLIENTE");
						String nombreCliente = personaAsegurado.getNombres();
						String apePatCliente = personaAsegurado.getApellidoPaterno();
						String apeMatCliente = personaAsegurado.getApellidoMaterno() == null ? ""
								: personaAsegurado.getApellidoMaterno();
						generarPdfCabecera1.setValor(nombreCliente + " " + apePatCliente + " " + apeMatCliente);
						GenerarPdfCabeceras generarPdfCabecera2 = new GenerarPdfCabeceras();
						generarPdfCabecera2.setCampo(personaAsegurado.getTipoDocumento().equals("1") ? "DNI" : "CE");
						generarPdfCabecera2.setValor(personaAsegurado.getNumeroDocumento());
						GenerarPdfCabeceras generarPdfCabecera3 = new GenerarPdfCabeceras();
						generarPdfCabecera3.setCampo("FECHA");
						generarPdfCabecera3.setValor(fechaCotizacion);
						generarPdfCabeceras.add(generarPdfCabecera1);
						generarPdfCabeceras.add(generarPdfCabecera2);
						generarPdfCabeceras.add(generarPdfCabecera3);
						esnGenerarPdfRequest.setCabeceras(generarPdfCabeceras);
						List<GenerarPdfPreguntas> generarPdfPreguntas = new ArrayList<>();
						GenerarPdfPreguntas generarPdfPregunta = new GenerarPdfPreguntas();
						generarPdfPregunta.setPregunta("¿Cuáles son los planes a futuro?");
						generarPdfPregunta.setRespuesta(planesFuturo);
						generarPdfPreguntas.add(generarPdfPregunta);
						esnGenerarPdfRequest.setPreguntas(generarPdfPreguntas);
						List<String[]> cuadroDetalles = new ArrayList<>();
						String[] detalles1 = { "Cotización|10%", "¿Por cuántos años quisieras estar asegurado?|15%",
								"¿Por cuántos años quisieras pagar tu seguro?|15%",
								"¿Cuánto deseas invertir al mes, y qué moneda elegirías?|15%",
								"¿Qué porcentaje de devolución deseas recibir por tu inversión?|15%",
								"¿Cuál es la frecuencia de pago para su seguro?|15%",
								"¿El ahorro lo deseas recibir al término del periodo de pago o cobertura?|15%" };
						String[] detalles2 = { solicitud.getNumeroPropuesta(), aniosAsegurado, aniosPago, montoMes,
								porcentajeDevolucion, frecuenciaPago, ahorroDeseasRecibir };
						cuadroDetalles.add(detalles1);
						cuadroDetalles.add(detalles2);
						esnGenerarPdfRequest.setCuadroDetalle(cuadroDetalles);
						guardarServicioEdn(esnGenerarPdfRequest, solicitudFinalizarProcesoRequestDTO.getUsuarioLogin());
					}
				} catch (Exception e) {
					LOGGER.info("ERROR :" + e);
				}
			}
		} catch (Exception ex) {
			LOGGER.info("ERROR finalizarProcesoSolicitud.message:" + ex.getMessage());
			LOGGER.info("ERROR finalizarProcesoSolicitud:" + ex);
			mensajeRespuesta = "[ADN-008]" + ex.getMessage();
			response.setObjErrorResource(new ErrorResourceDTO("[ADN-008]", ex.getMessage()));
		}
		response.setMensajeRespuesta(mensajeRespuesta);
		response.setCodigoRespuesta(codigoRespuesta);
		LOGGER.info("SolicitudFinalizarProcesoResponseDTO.finalizarProcesoSolicitud.FIN");
		return response;
	}

	public void generarInformacionSolicitud(Solicitud solicitud, String tipoProducto) {
		SolicitudRunnable tarea = new SolicitudRunnable(solicitud.getIdSolicitud(), tipoProducto);
		applicationContext.getAutowireCapableBeanFactory().autowireBean(tarea);
		Thread thread1 = new Thread(tarea);
		thread1.start();
	}

	private void enviarCorreoError(Object requestDTO, String tipoError, String asunto, String mensajeError, String motivo, String cotizacion, String documento) {

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
			interseguroRestClient.enviarCorreo(enviarCorreoRequest,  motivo,  cotizacion,  documento);

		} catch (Exception e) {
			LOGGER.error("ERROR - AdnServiceImpl#enviarCorreoError(requestDTO, tipoError, asunto, mensajeError): "
					+ e.getMessage());
			e.printStackTrace();
		}

	}

	@Override
	public BaseResponseDTO enviarDatosSolicitudManualCRM(String numeroPropuesta) {
		BaseResponseDTO respuesta = new BaseResponseDTO();
		String codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_EXITO;
		String mensajeRespuesta = Strings.EMPTY;

		try {
			Solicitud solicitud = new Solicitud();
			StringBuilder sbError = new StringBuilder();
			solicitud = solicitudRepository.findByNumeroCotizacion(numeroPropuesta);
			if (solicitud != null && solicitud.getEstado().equals(Constantes.CODIGO_SOLICITUD_POR_TRANSMITIR)) {

				// 1. ACTUALIZAR COTIZACION
				UpdateCotizacionRequest updateCotizacionRequest = this.completarCotizacionRequestCRM(solicitud);

				// 2. ACTUALIZAR CONTACTO
				UpdateContactoRequest updateContactoRequest = this.completarContactoRequestCRM(solicitud);

				// 3. ACTUALIZAR DPS
				UpdateFormatoDPSRequest updateFormatoDPSRequest = this.completarFormatoDPSRequestCRM(solicitud);
				LOGGER.info("enviarDatosSolicitudCRM.updateFormatoDPSRequest=>" + gson.toJson(updateFormatoDPSRequest));
				UpdateResponse responseUpdateFormatoDPSRequest = crmRestClient.updateDPS(updateFormatoDPSRequest);
				LOGGER.info("enviarDatosSolicitudCRM.responseUpdateFormatoDPSRequest=>"
						+ gson.toJson(responseUpdateFormatoDPSRequest));

				if (!responseUpdateFormatoDPSRequest.getRespuesta().equals(Constantes.CODIGO_CRM_RESPUESTA_TRUE)) {
					codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_ERROR;
					sbError.append(responseUpdateFormatoDPSRequest.getMensajeCrm()).append("|");
				}

				// 4. CREAR BENEFICIARIOS
				CrearBeneficiariosRequest requestCrearBeneficario = this.completarBeneficiariosRequestCRM(solicitud);
				LOGGER.info("enviarDatosSolicitudCRM.requestCrearBeneficario=>" + gson.toJson(requestCrearBeneficario));
				UpdateResponse responseCrearBeneficiariosCRM = crmRestClient.crearBenficiarios(requestCrearBeneficario);
				LOGGER.info("enviarDatosSolicitudCRM.responseCrearBeneficiariosCRM=>"
						+ gson.toJson(responseCrearBeneficiariosCRM));

				if (!responseCrearBeneficiariosCRM.getRespuesta().equals(Constantes.CODIGO_CRM_RESPUESTA_TRUE)) {
					codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_ERROR;
					sbError.append(responseCrearBeneficiariosCRM.getMensajeCrm()).append("|");
				}

				mensajeRespuesta = sbError.toString();

			}
		} catch (Exception ex) {
			LOGGER.error("Error enviarDatosSolicitudCRM=>" + ex.getMessage(), ex);
			codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_ERROR;
			mensajeRespuesta = Utilitarios.obtenerMensaje(messageSource,
					Constantes.MENSAJE_SOLICITUD_ERROR_DATOS_NO_ENVIADOS_CRM);
		}
		respuesta.setCodigoRespuesta(codigoRespuesta);
		respuesta.setMensajeRespuesta(mensajeRespuesta);
		return respuesta;
	}

	public String formatoBBVA(String numeroTarjeta, String numeroPropuesta) {
		LocalDateTime ldt = LocalDateTime.now();
		ZonedDateTime zdt = ldt.atZone(ZoneId.of("America/Los_Angeles"));
		String traza = String.valueOf(zdt.toInstant().toEpochMilli());
		traza = traza + "-" + numeroPropuesta;
		LOGGER.info("[" + traza + "] Entra a SolicitudServiceImpl.formatoBBVA");
		String resultado = "";
		String cadena1;
		String cadena2;
		if (numeroTarjeta.length() == 18) {
			cadena1 = numeroTarjeta.substring(4, 5);
			cadena1 = cadena1 + Integer.valueOf(numeroTarjeta.substring(5, 6)) * 2;
			cadena1 = cadena1 + Integer.valueOf(numeroTarjeta.substring(6, 7)) * 1;
			cadena1 = cadena1 + Integer.valueOf(numeroTarjeta.substring(7, 8)) * 2;
			int valor = 3;
			for (int i = 0; i < cadena1.length(); i++) {
				valor = valor + Integer.valueOf(cadena1.substring(i, i + 1));
			}
			String digito1 = String.valueOf(((valor / 10) + 1) * 10 - valor);
			digito1 = digito1.substring(digito1.length() - 1);

			cadena2 = "";
			for (int i = 8; i < 17; i += 2) {
				cadena2 = cadena2 + Integer.valueOf(numeroTarjeta.substring(i, i + 1)) * 1;
				cadena2 = cadena2 + Integer.valueOf(numeroTarjeta.substring(i + 1, i + 2)) * 2;
			}

			valor = 0;
			for (int i = 0; i < cadena2.length(); i++) {
				valor = valor + Integer.valueOf(cadena2.substring(i, i + 1));
			}

			String digito2 = String.valueOf(((valor / 10) + 1) * 10 - valor);
			digito2 = digito2.substring(digito2.length() - 1);

			String digitoCalculado = digito1 + digito2;
			resultado = numeroTarjeta.substring(0, 8) + "" + digitoCalculado + "" + numeroTarjeta.substring(8, 18);
		} else {
			resultado = numeroTarjeta;
		}
		LOGGER.info("[" + traza + "] Sale de SolicitudServiceImpl.formatoBBVA");
		return resultado;
	}

	@Override
	public BaseResponseDTO registrarAfiliacionPropuesta(SolicitudRegistroPagoRequestDTO pagoRequestDTO) {

		String traza = Utilitarios.trazaLog();
		LOGGER.info("[{}] Entra a SolicitudServiceImpl.registrarAfiliacionPropuesta", traza);
		BaseResponseDTO response = new BaseResponseDTO();

		String codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_ERROR;
		String mensajeRespuesta = Strings.EMPTY;

		String numeroPropuesta = String.valueOf(pagoRequestDTO.getNumeroPropuesta());
		Solicitud solicitud = solicitudRepository.findByNumeroCotizacion(numeroPropuesta);
		String trazaPropuesta = traza + "-" + solicitud.getNumeroPropuesta();

		// HCR -
		BaseResponseDTO response1 = validaTraspaso(pagoRequestDTO, response, codigoRespuesta, solicitud,
				trazaPropuesta);
		if (response1 != null)
			return response1;

		try {
			LOGGER.info("{} valida estado de pago si se pago primera prima {}", trazaPropuesta,
					solicitud.getPagoPrima());
			BaseResponseDTO response2 = validaPagoPrimeraPrima(solicitud.getPagoPrima(), response, trazaPropuesta);
			if (response2 != null)
				return response2;

			LOGGER.info("[{}] Intentando registrar pago afiliacion.", trazaPropuesta);

			String tokenSitc = generateTokenCulqi(pagoRequestDTO, trazaPropuesta);

			String tarjetaPrimeraPrima = solicitud.getNumeroTarjetaPP();
			Integer tipoViaCobroPrimeraPrima = Integer.valueOf(solicitud.getIdViaCobroPP());
			Integer viaCobroPrimeraPrima = Integer.valueOf(solicitud.getIdViaCobroPP());

			SolicitudProducto producto = solicitudProductoRepository.findByIdSolicitud(solicitud.getIdSolicitud());
			Persona persona = personaRepository.findById(solicitud.getPersonaByIdAsegurado().getIdPersona()).get();
			String nombres = persona.getNombres();
			if (!Constantes.SOLICITUD_ASEGURADO_IGUAL_CONTRATANTE_TRUE
					.equals(solicitud.getAseguradoIgualContratante())) {
				LOGGER.info("[{}] Contratante es diferente a asegurado.", trazaPropuesta);
				persona = personaRepository.findById(solicitud.getPersonaByIdContratante().getIdPersona()).get();
				nombres = persona.getRazonSocial();
			}
			Map<String, Object> map = new HashMap<>();
			map.put("p_documentoIdentidad", persona.getNumeroDocumento());
			map.put("p_tipoDocumento", persona.getTipoDocumento());
			map.put("p_apePaterno", persona.getApellidoPaterno());
			map.put("p_apeMaterno", persona.getApellidoMaterno());
			map.put("p_nomPersona", nombres);
			map.put("p_numeroPropuesta", pagoRequestDTO.getNumeroPropuesta());
			map.put("p_moneda", isValidateReturnValue(Constantes.MONEDA_ADN_SOLES.equals(solicitud.getMoneda()),
					Constantes.MONEDA_COTIZADOR_SOLES, Constantes.MONEDA_COTIZADOR_DOLARES));
			map.put("p_pasarela_pp", isValidateReturnValue(solicitud.getFechaPagoCulqi() != null,
					Constantes.PASARELA_CULQI, Constantes.PASARELA_NIUBIZ));
			map.put("p_pasarela", pagoRequestDTO.getPasarela());
			String numeroTarjetaCuenta = isViaCobroCuentaBBVA(pagoRequestDTO, numeroPropuesta, trazaPropuesta);
			map.put("p_numeroTarjeta", numeroTarjetaCuenta);
			map.put("p_fechaVencimiento", "");
			if (pagoRequestDTO.getPasarela() != null) {
				String tipoViaCobro=pagoRequestDTO.getPasarela().equalsIgnoreCase("cuenta")?"1":"2";
				map.put("p_tipoViaCobro", tipoViaCobro);
			} else {
				map.put("p_tipoViaCobro", pagoRequestDTO.getTipoViaCobro());
			}
			Integer tipoViaCobroRecurrente = pagoRequestDTO.getTipoViaCobro();
			map.put("p_viaCobro", pagoRequestDTO.getViaCobro());
			map.put("p_tipoCuenta", pagoRequestDTO.getTipoCuenta());
			map.put("p_estado", pagoRequestDTO.getEstado());
			map.put("p_numTelefono", persona.getCelular());
			map.put("p_codPeriodoPago", pagoRequestDTO.getCodPeriodoPago().toUpperCase());
			map.put("p_codRamo", pagoRequestDTO.getCodRamo());
			map.put("p_codSubRamo", pagoRequestDTO.getCodSubRamo());
			map.put("p_fecIngresoMandato", producto.getFechaCotizacion());
			map.put("p_numDiaCobro", 15);
			map.put("p_valPrimaFormaPago", producto.getPrimaComercial());
			map.put("p_usuario", pagoRequestDTO.getUsuario());
			map.put("p_usuarioAplicacion", Constantes.SAMP_USUARIO_ADN);
			map.put("p_glsToken", tokenSitc);
			map.put("p_num_tarjeta_pp", tarjetaPrimeraPrima.replaceAll("-", "").replace(" ", ""));
			map.put("p_fecha_venc_tar_pp", "");
			map.put("p_tipoviaCobro_pp", tipoViaCobroPrimeraPrima);
			map.put("p_viaCobro_pp", viaCobroPrimeraPrima);
			map.put("p_moneda_pp", isValidateReturnValue(Constantes.MONEDA_ADN_SOLES.equals(solicitud.getMoneda()),
					Constantes.MONEDA_COTIZADOR_SOLES, Constantes.MONEDA_COTIZADOR_DOLARES));
			map.put("p_tipoCuenta_pp", pagoRequestDTO.getTipoCuentaPp());
			map.put("p_idAfiliacion_pro", solicitud.getIdAfiliacionPropuesta());
			map.put("p_retorno", 0);
			map.put("p_mensaje", "0");
			LOGGER.info("[{}] REQUEST.RegistroSITC: {}", trazaPropuesta, gson.toJson(map));
			// sitcRepository.registrarViaCobro(map);
			LOGGER.info("[{}] RESPONSE.RegistroSITC: {}", trazaPropuesta, gson.toJson(map));

			String codigoAfiliacion = map.get("p_idAfiliacion_pro").toString();
			String retorno = map.get("p_retorno").toString();
			if (Constantes.SAMP_RESPUESTA_OK.equals(retorno)) {
				solicitud.setIdAfiliacionPropuesta(String.valueOf(codigoAfiliacion));
				solicitud.setFechaAfiliacionPropuesta(new Date());
				solicitud.setEstado(Constantes.CODIGO_SOLICITUD_PAGO_SAMP);
				solicitud.setFechaRegistroSamp(new Date());
				solicitud.setIdViaCobro(Integer.parseInt(pagoRequestDTO.getViaCobro()));
				solicitud.setIdCuenta(pagoRequestDTO.getTipoCuenta());
				solicitud.setIdCuentaMoneda(pagoRequestDTO.getMoneda());

				solicitud.setTarjetaAfiliacion(
						isValidateReturnValue(tipoViaCobroRecurrente == Constantes.SAMP_TIPO_VIA_COBRO_TARJETA,
								pagoRequestDTO.getTarjetaCobroRecurrente(), pagoRequestDTO.getNumeroTarjeta()));
				solicitudRepository.save(solicitud);

				codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_EXITO;
				mensajeRespuesta = Constantes.MENSAJE_RESPUESTA_GENERAL_EXITO;
				LOGGER.info("[{}] Registro de afiliación completado correctamente.", trazaPropuesta);

			} else {
				codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_ERROR;
				mensajeRespuesta = Constantes.MENSAJE_PAGO_SITC_AFILIACION_ERROR + " "
						+ map.get("p_mensaje").toString();
				LOGGER.error("[{}] Error al intentar registrar afiliación en SITC.", trazaPropuesta);
			}
		} catch (Exception e) {
			LOGGER.error("[{} Error {}] ", trazaPropuesta, e.getMessage(), e);
			codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_ERROR;
			mensajeRespuesta = e.getMessage();
		}
		response.setCodigoRespuesta(codigoRespuesta);
		response.setMensajeRespuesta(mensajeRespuesta);
		LOGGER.info("[{}] Sale de SolicitudServiceImpl.registrarAfiliacionPropuesta", trazaPropuesta);
		return response;
	}

	/**
	 * Valida si es traspaso
	 * 
	 * @param pagoRequestDTO
	 * @param response
	 * @param codigoRespuesta
	 * @param solicitud
	 * @param trazaPropuesta
	 * @return
	 */
	private BaseResponseDTO validaTraspaso(SolicitudRegistroPagoRequestDTO pagoRequestDTO, BaseResponseDTO response,
			String codigoRespuesta, Solicitud solicitud, String trazaPropuesta) {
		String mensajeRespuesta;
		if (solicitud.getEstadoTraspaso() != null) {
			ValidarTraspasoRequest request = new ValidarTraspasoRequest();
			request.setNumCotizacionOrigen(String.valueOf(pagoRequestDTO.getNumeroPropuesta()));
			request.setNumCotizacionDestino(String.valueOf(pagoRequestDTO.getNumeroPropuestaNew()));
			LOGGER.info("{} responseValidarTraspaso {}", trazaPropuesta, gson.toJson(request));
			ValidarTraspasoResponse responseValidarTraspaso = globalRestClient.validarTraspaso(request);

			if (Boolean.FALSE.equals(responseValidarTraspaso.getTraspaso())) {
				response.setCodigoRespuesta(codigoRespuesta);
				mensajeRespuesta = String.format(Constantes.MENSAJE_VALIDACION_TRASPASO_ERROR,
						request.getNumCotizacionOrigen(), request.getNumCotizacionDestino());
				response.setMensajeRespuesta(mensajeRespuesta);
				return response;
			}
		}
		return null;
	}

	/**
	 * Valida si se ha pagado primera prima
	 * 
	 * @param primeraPrima
	 * @param response
	 * @param traza
	 * @return
	 */
	private BaseResponseDTO validaPagoPrimeraPrima(String primeraPrima, BaseResponseDTO response, String traza) {
		if (!StringUtils.equals(Constantes.PAGO_PRIMA_VAL, primeraPrima)) {
			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			response.setMensajeRespuesta("propuesta no se encuentra pagada");
			LOGGER.error("[{}] propuesta no se encuentra pagada", traza);
			return response;
		}
		return null;
	}

	private String generateTokenCulqi(SolicitudRegistroPagoRequestDTO pagoRequestDTO, String traza) throws Exception {
		TokenSitcRequest request1 = new TokenSitcRequest();
		request1.setNumIp(isValidateReturnValue(StringUtils.isNotBlank(pagoRequestDTO.getIp()), pagoRequestDTO.getIp(),
				"10.29.21.170"));
		request1.setNumPropuesta(String.valueOf(pagoRequestDTO.getNumeroPropuesta()));
		request1.setUsuario(pagoRequestDTO.getUsuario());
		request1.setUsuarioAplicacion("ADN");
		TokenSitcResponse responseSoap1 = sitcSoapClient.obtenerToken(request1);
		String tokenSitc = responseSoap1.getRegistrarTokenResult();
		if (StringUtils.isBlank(tokenSitc)) {
			LOGGER.error("[{}] Error al generar token en sitc.", traza);
			LOGGER.error("[{}] REQUEST.TokenSITC: {}", traza, gson.toJson(request1));
			throw new Exception("No se pudo generar token de afiliación.");
		}
		return tokenSitc;
	}

	private String isViaCobroCuentaBBVA(SolicitudRegistroPagoRequestDTO pagoRequestDTO, String numeroPropuesta,
			String trazaPropuesta) {
		String numeroTarjetaCuenta="";
		LOGGER.info("[{}] Entro isViaCobroCuentaBBVA", trazaPropuesta);
		LOGGER.info("[{}] isViaCobroCuentaBBVA getTarjetaCobroRecurrente => [{}]", trazaPropuesta, gson.toJson(pagoRequestDTO));
		numeroTarjetaCuenta = pagoRequestDTO.getTarjetaCobroRecurrente().replaceAll("-", "").replace(" ", "");
		//numeroTarjetaCuenta = pagoRequestDTO.getNumeroTarjeta().replaceAll("-", "").replace(" ", "");
		LOGGER.info("[{}] isViaCobroCuentaBBVA antes del if", trazaPropuesta);
			if (Constantes.SAMP_TIPO_VIA_COBRO_CUENTA == pagoRequestDTO.getTipoViaCobro()
					&& ConstantesAcpPDF.REPORTE_ENTIDAD_FINANCIERA_CONTINENTAL.equals(pagoRequestDTO.getViaCobro())) {
				LOGGER.info("[{}] isViaCobroCuentaBBVA entro al if", trazaPropuesta);
				LOGGER.info("[{}] Pago recurrente con cuenta de banco Continental.", trazaPropuesta);
				numeroTarjetaCuenta = this.formatoBBVA(numeroTarjetaCuenta, numeroPropuesta);
				LOGGER.info("[{}] isViaCobroCuentaBBVA luego del if", trazaPropuesta);
			}
		LOGGER.info("[{}] Salio isViaCobroCuentaBBVA", trazaPropuesta);
			return numeroTarjetaCuenta;
	}

	/**
	 * Valida si es correcto la condicion
	 * 
	 * @param validation condicion
	 * @param valueTrue  value condicion verdad
	 * @param valuefalse value condicion falso
	 * @return retorna valor segun condicion.
	 */
	private static String isValidateReturnValue(boolean validation, String valueTrue, String valuefalse) {
		return validation ? valueTrue : valuefalse;
	}

	@Override
	public BaseResponseDTO registrarAfiliacionTraspaso(SolicitudRegistroPagoRequestDTO pagoRequestDTO) {
		LocalDateTime ldt = LocalDateTime.now();
		ZonedDateTime zdt = ldt.atZone(ZoneId.of("America/Los_Angeles"));
		BaseResponseDTO response = new BaseResponseDTO();

		String codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_ERROR;
		String mensajeRespuesta = Strings.EMPTY;

		String numeroPropuesta = String.valueOf(pagoRequestDTO.getNumeroPropuesta());
		String traza = String.valueOf(zdt.toInstant().toEpochMilli()) + " - " + numeroPropuesta;
		LOGGER.info("[" + traza + "] Entra a SolicitudServiceImpl.registrarAfiliacionTraspaso");

		Solicitud solicitud = solicitudRepository.findByNumeroCotizacion(numeroPropuesta);

		try {

			SolicitudProducto producto = solicitudProductoRepository.findByIdSolicitud(solicitud.getIdSolicitud());

			String numeroPropuestaNew = String.valueOf(pagoRequestDTO.getNumeroPropuestaNew());
			Solicitud solicitudNew = solicitudRepository.findByNumeroCotizacion(numeroPropuestaNew);
			SolicitudProducto productoNew = solicitudProductoRepository
					.findByIdSolicitud(solicitudNew.getIdSolicitud());

			// HCR - Se envia al api-solicitud para registrar la afiliación traspaso
			RegistrarAfiliacionTraspasoRequest requestTraspaso = new RegistrarAfiliacionTraspasoRequest();
			requestTraspaso.setP_numeropropuesta(pagoRequestDTO.getNumeroPropuesta());
			requestTraspaso.setP_numeropropuesta_new(pagoRequestDTO.getNumeroPropuestaNew());
			requestTraspaso.setP_moneda(Constantes.MONEDA_ADN_SOLES.equals(solicitud.getMoneda()) ? Constantes.MONEDA_COTIZADOR_SOLES
			: Constantes.MONEDA_COTIZADOR_DOLARES);
			requestTraspaso.setP_codperiodopago(Constantes.FRECUENCIA.equals(productoNew.getFrecuencia()) ? Constantes.FRECUENCIA_MENSUAL
			: Constantes.FRECUENCIA_TRIMESTRAL);
			requestTraspaso.setP_valprimaformapago(producto.getPrimaComercial());

			LOGGER.info("[" + traza + "] SolicitudServiceImpl.registrarAfiliacionTraspaso invoca globalRestClient.registrarAfiliacionTraspaso");
			RegistrarAfiliacionTraspasoResponse requestTraspasoResponse = globalRestClient.registrarAfiliacionTraspaso(requestTraspaso);
			
			LOGGER.info("[" + traza + "] SolicitudServiceImpl.registrarAfiliacionTraspaso response globalRestClient.registrarAfiliacionTraspaso p_retorno: " + requestTraspasoResponse.getP_retorno());
			if (Integer.parseInt(requestTraspasoResponse.getP_retorno()) > 0) {

				/* Evaluar la propuesta diferente a emision automatica */
				if (solicitudNew.getEstadoPropuesta().equals(1)) {
					LOGGER.info("traspaso " + solicitudNew.getNumeroCotizacion() + " - inicia evaluación");
					ObtenerEvaluacionSolicitudEmisionRequest obtenerEvaluacionSolicitudEmisionRequest = new ObtenerEvaluacionSolicitudEmisionRequest();
					obtenerEvaluacionSolicitudEmisionRequest.setNroSolicitud(solicitudNew.getNumeroCotizacion());
					ObtenerEvaluacionSolicitudEmisionResponse responseGlobal = globalRestClient
							.obtenerReEvaluacionSolicitud(obtenerEvaluacionSolicitudEmisionRequest);
					LOGGER.info("traspaso " + solicitudNew.getNumeroCotizacion() + " - finaliza evaluación");
					LOGGER.info("GTI - 53329");
					LOGGER.info("traspaso " + solicitudNew.getNumeroCotizacion() + " - " + gson.toJson(responseGlobal));
					LOGGER.info(responseGlobal.getMensajeRespuesta());
					solicitudNew.setEstadoPropuesta(Integer.valueOf(responseGlobal.getMensajeRespuesta()));

					LOGGER.info("EVALUAR-SOLICITUD ==> Entro a SolicitudServiceImpl#obtenerRequestEvaluator()");
				}

				solicitudNew.setEstado(Constantes.CODIGO_SOLICITUD_PAGO_SAMP);
				solicitudNew.setEstadoTraspaso("1");
				solicitudRepository.save(solicitudNew);

				SolicitudTraspaso solicitudTraspaso = new SolicitudTraspaso();
				solicitudTraspaso.setNumPropuesta(String.valueOf(pagoRequestDTO.getNumeroPropuesta()));
				solicitudTraspaso.setNumPropuestaOriginal(String.valueOf(pagoRequestDTO.getNumeroPropuestaNew()));// historico
				solicitudTraspaso.setMontoActual(producto.getPrimaComercial());
				solicitudTraspaso.setMontoOriginal(productoNew.getPrimaComercial()); // propuesta 2
				solicitudTraspaso.setMonedaActual(solicitud.getMoneda());
				solicitudTraspaso.setMonedaOriginal(solicitudNew.getMoneda());
				solicitudTraspasoRepository.save(solicitudTraspaso);

				codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_EXITO;
				mensajeRespuesta = Constantes.MENSAJE_RESPUESTA_GENERAL_EXITO;

			} else {

				codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_ERROR;
				mensajeRespuesta = Constantes.MENSAJE_PAGO_SITC_AFILIACION_ERROR;
			}

		} catch (Exception e) {

			codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_ERROR;
			mensajeRespuesta = Constantes.MENSAJE_RESPUESTA_GENERAL_ERR0R;
		}

		response.setCodigoRespuesta(codigoRespuesta);
		response.setMensajeRespuesta(mensajeRespuesta);
		return response;
	}

	@Async
	public void buildBodyLinkPago(Solicitud solicitudDomain, SolicitudProducto productoDomain,
			Map<String, String> valores, boolean help) {
		String traza = Utilitarios.trazaLog() + "-" + solicitudDomain.getNumeroPropuesta();
		LOGGER.info("LINK-PAGO ==> [{}] Entra a SolicitudServiceImpl.buildBodyLinkPago({})", traza,
				solicitudDomain.getNumeroCotizacion());

		try {
			String bodyHtml = getBodyHtml(traza);
			String monto = Utilitarios.formatoMiles(Double.parseDouble(productoDomain.getPrimaComercial().toString()),
					Utilitarios.FORMATO_MILES_CON_DECIMAL);
			String montoValue = String.valueOf(Double.parseDouble(productoDomain.getPrimaComercial().toString()));
			LOGGER.info("LINK-PAGO ==> {}] Monto:: {}", traza, montoValue);

			Persona personaAsegurado = solicitudDomain.getPersonaByIdAsegurado();
			String cliente = Utilitarios.nombresCompletos(personaAsegurado.getNombres(),
					personaAsegurado.getApellidoPaterno(), personaAsegurado.getApellidoMaterno(), "");

			LOGGER.info("LINK-PAGO ==> [{}] Cliente:: {}", traza, cliente);
			String documento = personaAsegurado.getNumeroDocumento();
			String emailCliente = personaAsegurado.getCorreo();

			if (!Constantes.SOLICITUD_ASEGURADO_IGUAL_CONTRATANTE_TRUE
					.equals(solicitudDomain.getAseguradoIgualContratante())) {
				Persona personaContratante = solicitudDomain.getPersonaByIdContratante();
				documento = personaContratante.getNumeroDocumento();
				emailCliente = personaContratante.getCorreo();
			}
			LOGGER.info("LINK-PAGO ==> [{}] Documento Cliente:: {}", traza, documento);
			LOGGER.info("LINK-PAGO ==> [{}] Email Cliente:: {}", traza, emailCliente);

			String imagen = applyImgEmail(solicitudDomain.getSubplan(), productoDomain.getTipoProducto());

			String bodyAsegurado = bodyHtml;
			bodyAsegurado = bodyAsegurado.replaceAll("\\{imagen\\}", imagen);
			bodyAsegurado = bodyAsegurado.replaceAll("\\{cliente\\}", Utilitarios.toCamelCase(cliente));
			bodyAsegurado = bodyAsegurado.replaceAll("\\{numeroPoliza\\}", solicitudDomain.getNumeroPropuesta());
			bodyAsegurado = bodyAsegurado.replace("{producto}", valores.get("producto"));

			String moneda = Constantes.MONEDA_SIMBOLO_SOLES;
			if (Constantes.MONEDA_ADN_DOLARES.equals(solicitudDomain.getMoneda())) {
				moneda = Constantes.MONEDA_SIMBOLO_DOLARES;
			}

			bodyAsegurado = bodyAsegurado.replace("{montoCuota}", moneda + " " + monto);
			bodyAsegurado = bodyAsegurado.replaceAll("\\{frecuenciaPago\\}",
					Utilitarios.toCamelCase(valores.get("frecuencia")));
			LOGGER.info("[{}] TokenGenerado:: {}", traza, valores.get("linkPago"));
			bodyAsegurado = bodyAsegurado.replaceAll("\\{link\\}", valores.get("linkPago"));

			enviarCorreoConAdjunto(this.enviarCorreoLinkPagoPrimaAsunto, emailCliente, bodyAsegurado, null, traza, "LINK-PAGO",solicitudDomain.getNumeroPropuesta(),documento);
			if (help) {
				LOGGER.info("LINK-PAGO ==> [{}] Enviando correo desde if(help)", traza);
				enviarCorreoConAdjunto(this.enviarCorreoLinkPagoPrimaAsunto, solicitudDomain.getAgenteCorreo(), bodyAsegurado, null, traza, "LINK-PAGO",solicitudDomain.getNumeroPropuesta(),documento);
			}
			solicitudDomain.setTokenOnline(valores.get("linkPago"));
			solicitudDomain.setTokenTimeExpiry(valores.get("expiracion"));
			solicitudDomain.setTokenStatus(Constantes.SAMP_RESPUESTA_OK);
			solicitudRepository.save(solicitudDomain);
			LOGGER.info("LINK-PAGO ==> [{}] Guardando solicitud en BD ", traza);

		} catch (Exception e) {
			LOGGER.error("LINK-PAGO ==> [{}] Error crear cuerpo de correo: {}", traza, e.getMessage());
		}
	}

	/**
	 * Obtiene la plantilla y devuelve el html
	 *
	 * @return texto html.
	 * @throws IOException
	 */
	private String getBodyHtml(String traza) throws IOException {
		LOGGER.info("LINK-PAGO ==> [{}] Obteniendo plantilla de correo: {} / {}", traza, Constantes.RUTA_PLANTILLA,
				Constantes.PLANTILLA_LINK_PAGO);
		InputStream stream = new ClassPathResource(Constantes.RUTA_PLANTILLA + "/" + Constantes.PLANTILLA_LINK_PAGO)
				.getInputStream();
		String systemOp = System.getProperty("os.name");
		if (!systemOp.contains("Windows")) {
			stream = new FileInputStream(new File(rutaPlantillaDocLinux + "//" + Constantes.PLANTILLA_LINK_PAGO));
		}
		return Utilitarios.valorString(StreamUtils.copyToString(stream, Charset.defaultCharset()));
	}

	@Override
	public LinkPagoResponseDTO enviarLinkPagoRecargo(String numeroPropuesta, String idUsuario, String device,
			String os) {
		String traza = Utilitarios.trazaLog();
		LOGGER.info("[{}] Entra a SolicitudServiceImpl.enviarLinkPagoRecargo", traza);
		LinkPagoResponseDTO response = new LinkPagoResponseDTO();
		try {
			response = generateLink(numeroPropuesta, true, false, false);

		} catch (Exception e) {
			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			response.setMensajeRespuesta(" Error al enviar link de pago: " + e.getMessage());
			LOGGER.error("[{}] Error al enviar link de pago({}): {}", traza, numeroPropuesta, e.getMessage());
		}
		return response;
	}

	/**
	 * Generar link a pagar en link de pagos
	 *
	 * @param numeroPropuesta Identificador de pago(Numero de propuesta)
	 * @param isRecargo       Es recargo?
	 * @param isPrimeraPrima  es pago primera prima?
	 * @param help            indica si es link de ayuda o el pricipal.
	 * @return respuesta de envio de orden de pago.
	 */
	private LinkPagoResponseDTO generateLinkHelp(String numeroPropuesta, boolean isRecargo, boolean isPrimeraPrima,
			boolean help) {
		LinkPagoResponseDTO response=new LinkPagoResponseDTO();

		Solicitud solicitudDomain = solicitudRepository.findByNumeroCotizacion(numeroPropuesta);

		Optional<SolicitudProducto> solicitudProducto = solicitudDomain.getSolicitudProductos().stream().findFirst();

		if (!solicitudProducto.isPresent()) {
			return LinkPagoResponseDTO.builder().codigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR_FUNCIONAL)
					.mensajeRespuesta("No existe producto para numero de propuesta: " + numeroPropuesta).build();
		}

		int estado= Integer.parseInt(solicitudDomain.getEstado());
		String frecuencia = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_FRECUENCIA,solicitudProducto.get().getFrecuencia(), Constantes.MULTITABLA_COLUMNA_VALOR);
		String producto = "";
		String montoRecargo = "";
		String tokenTimeExpiry = "";

		Boolean expiration= isTimeExpiration(solicitudDomain, isRecargo);
		if(estado == 4 && !expiration){
			Persona persona = solicitudDomain.getPersonaByIdAsegurado();
			if (!Constantes.SOLICITUD_ASEGURADO_IGUAL_CONTRATANTE_TRUE
					.equals(solicitudDomain.getAseguradoIgualContratante())) {
				persona = solicitudDomain.getPersonaByIdContratante();
				if (persona.getTipoDocumento().equals(Constantes.CODIGO_TIPO_DOCUMENTO_RUC_JURIDICO)) {
					persona.setNombres(persona.getRazonSocial());
					persona.setApellidoPaterno(persona.getRazonSocial());
					persona.setApellidoMaterno(persona.getRazonSocial());
				}
			}

			PagoLinkPago pago = new PagoLinkPago();
			pago.setMoneda(solicitudDomain.getMoneda().equals("1") ? "PEN" : "USD");
			pago.setMonedaSimbolo(solicitudDomain.getMoneda().equals("1") ? "S/." : "$.");
			pago.setMonto(solicitudProducto.get().getPrimaComercial().toString());
			//String montoRecargo = "";
			if (isRecargo) {
				montoRecargo = getMontoRecargo(solicitudDomain);
				pago.setMonto(montoRecargo);
			}

			pago.setFrecuencia(frecuencia);
			long[] array = new long[0];
			pago.setCuotas(array);

			ClienteLinkPago cliente = new ClienteLinkPago();
			String tipoDocumento = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_TIPO_DOCUMENTO,
					persona.getTipoDocumento(), Constantes.MULTITABLA_COLUMNA_VALOR_AUXILIAR);

			cliente.setDocumentoIdentidad(persona.getNumeroDocumento());
			cliente.setEmail(persona.getCorreo());
			cliente.setTipoDocumento(tipoDocumento);
			cliente.setNombre(persona.getNombres());
			cliente.setApellidoPaterno(persona.getApellidoPaterno());
			cliente.setApellidoMaterno(persona.getApellidoMaterno());
			cliente.setCelular(persona.getCelular().toString());
			cliente.setDepartamento(persona.getDepartamento());
			cliente.setProvincia(persona.getProvincia());
			cliente.setDistrito(persona.getDistrito());

			LinkPagoRequest linkPagoRequest = new LinkPagoRequest();
			producto = Constantes.COTIZADOR_VIDA_PRODUCTO_VIDA_FREE;
			linkPagoRequest.setProducto(producto);
			linkPagoRequest.setProductoAcsele(producto.toUpperCase());
			if (solicitudProducto.get().getTipoProducto().equals(Constantes.ADN_PRODUCTO_PLAN_GARANTIZADO)) {
				producto = Constantes.COTIZADOR_VIDA_PRODUCTO_PLAN_GARANTIZADO;
				linkPagoRequest.setProducto(producto);
				linkPagoRequest.setProductoAcsele(producto.toUpperCase());
			}

			linkPagoRequest.setExpiracion(expiracion);
			linkPagoRequest.setAplicacion(aplicacion);
			linkPagoRequest.setPago(pago);
			linkPagoRequest.setCliente(cliente);
			linkPagoRequest.setIdentificador(numeroPropuesta);
			linkPagoRequest.setCodigoEnvio("");
			linkPagoRequest.setBloquearEnvio(true);
			linkPagoRequest.setEstadoPropuesta(solicitudDomain.getEstadoPropuesta().byteValue());
			linkPagoRequest.setEsRecargo(isRecargo);
			linkPagoRequest.setPrimeraPrima(isPrimeraPrima);
			LOGGER.info("linkPagoRequest {}", gson.toJson(linkPagoRequest));
			LinkPagoResponse responseLink = interseguroRestClient.generateLinkPago(linkPagoRequest);
			if (responseLink == null) {
				throw new NullPointerException("GENERA LINK NULL");
			}
			response = LinkPagoResponseDTO.builder().codigoRespuesta(responseLink.getCodigoRespuesta())
					.token(responseLink.getToken()).mensajeRespuesta(responseLink.getMensajeRespuesta())
					.link(responseLink.getLink()).build();
			
			LocalDateTime localDateTime = LocalDateTime.now().plusHours(expiracion);
			
			long millis = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			tokenTimeExpiry = String.valueOf(millis);
		}
		else if(estado==5){
			producto = Constantes.COTIZADOR_VIDA_PRODUCTO_VIDA_FREE;
			if (solicitudProducto.get().getTipoProducto().equals(Constantes.ADN_PRODUCTO_PLAN_GARANTIZADO)) {
				producto = Constantes.COTIZADOR_VIDA_PRODUCTO_PLAN_GARANTIZADO;
			}
			response = LinkPagoResponseDTO.builder().codigoRespuesta("01")
					.token(null).mensajeRespuesta("OK")
					.link(solicitudDomain.getTokenOnline()).build();
			
			tokenTimeExpiry = solicitudDomain.getTokenTimeExpiry();
		}
		if (estado == 4 && expiration) {
			producto = Constantes.COTIZADOR_VIDA_PRODUCTO_VIDA_FREE;
			if (solicitudProducto.get().getTipoProducto().equals(Constantes.ADN_PRODUCTO_PLAN_GARANTIZADO)) {
				producto = Constantes.COTIZADOR_VIDA_PRODUCTO_PLAN_GARANTIZADO;
			}

			response = LinkPagoResponseDTO.builder().codigoRespuesta("01").token(null).mensajeRespuesta("OK")
					.link(solicitudDomain.getTokenOnline()).build();
			
			tokenTimeExpiry = solicitudDomain.getTokenTimeExpiry();
		}	

		Map<String, String> valores = new HashMap<>();
		valores.put("frecuencia", frecuencia);
		valores.put("linkPago", response.getLink());
		valores.put("expiracion", tokenTimeExpiry);
		valores.put("producto", producto);
		Long timeStart = System.nanoTime();
		if (isPrimeraPrima) {
			this.buildBodyLinkPago(solicitudDomain, solicitudProducto.get(), valores, help);
		}
		if (isRecargo) {
			this.buildBodyLinkPagoRecargo(solicitudDomain, solicitudProducto.get(), valores, help, montoRecargo);
		}
		Long timeEnd = System.nanoTime();
		LOGGER.info("Diference enviar correo: {}", (timeEnd - timeStart));
		return response;
	}
	
	/**
	 * Valida si el tokenTime es vacio o es menor al tiempo de envio de link
	 * @param solicitudDomain
	 * @return
	 */
	private boolean isTimeExpiration(Solicitud solicitudDomain, Boolean isRecargo) {
		LocalDateTime dateNow = LocalDateTime.now();
		long millis = dateNow.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		if(isRecargo){
			return StringUtils.isNotBlank(solicitudDomain.getTokenTimeRecargo())
					&& millis < Long.parseLong(solicitudDomain.getTokenTimeRecargo());
		}else{
			return StringUtils.isNotBlank(solicitudDomain.getTokenTimeExpiry())
					&& millis < Long.parseLong(solicitudDomain.getTokenTimeExpiry());
		}
	}


	private LinkPagoResponseDTO generateLink(String numeroPropuesta, boolean isRecargo, boolean isPrimeraPrima,
			boolean help) {
		LinkPagoResponseDTO response = new LinkPagoResponseDTO();

		Solicitud solicitudDomain = solicitudRepository.findByNumeroCotizacion(numeroPropuesta);

		Optional<SolicitudProducto> solicitudProducto = solicitudDomain.getSolicitudProductos().stream().findFirst();
		
		String tokenTimeExpiry = ""; 

		if (!solicitudProducto.isPresent()) {
			return LinkPagoResponseDTO.builder().codigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR_FUNCIONAL)
					.mensajeRespuesta("No existe producto para numero de propuesta: " + numeroPropuesta).build();
		}

		int estado = Integer.parseInt(solicitudDomain.getEstado());
		String frecuencia = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_FRECUENCIA,
				solicitudProducto.get().getFrecuencia(), Constantes.MULTITABLA_COLUMNA_VALOR);
		String producto = "";
		String montoRecargo = "";

		Boolean expiration= isTimeExpiration(solicitudDomain, isRecargo);
		if ((estado == 4 || estado == 6) && expiration) {

			producto = Constantes.COTIZADOR_VIDA_PRODUCTO_VIDA_FREE;
			if (solicitudProducto.get().getTipoProducto().equals(Constantes.ADN_PRODUCTO_PLAN_GARANTIZADO)) {
				producto = Constantes.COTIZADOR_VIDA_PRODUCTO_PLAN_GARANTIZADO;
			}
			response = LinkPagoResponseDTO.builder().codigoRespuesta("01").token(null).mensajeRespuesta("OK")
					.link(solicitudDomain.getTokenOnline()).build();
			
			tokenTimeExpiry = solicitudDomain.getTokenTimeExpiry();

		} else if ((estado == 4 || estado == 6) && !expiration) {

			Persona persona = solicitudDomain.getPersonaByIdAsegurado();
			if (!Constantes.SOLICITUD_ASEGURADO_IGUAL_CONTRATANTE_TRUE
					.equals(solicitudDomain.getAseguradoIgualContratante())) {
				persona = solicitudDomain.getPersonaByIdContratante();
				if (persona.getTipoDocumento().equals(Constantes.CODIGO_TIPO_DOCUMENTO_RUC_JURIDICO)) {
					persona.setNombres(persona.getRazonSocial());
					persona.setApellidoPaterno(persona.getRazonSocial());
					persona.setApellidoMaterno(persona.getRazonSocial());
				}
			}

			PagoLinkPago pago = new PagoLinkPago();
			pago.setMoneda(solicitudDomain.getMoneda().equals("1") ? "PEN" : "USD");
			pago.setMonedaSimbolo(solicitudDomain.getMoneda().equals("1") ? "S/." : "$.");
			pago.setMonto(solicitudProducto.get().getPrimaComercial().toString());
			// String montoRecargo = "";
			if (isRecargo) {
				montoRecargo = getMontoRecargo(solicitudDomain);
				pago.setMonto(montoRecargo);
			}

			pago.setFrecuencia(frecuencia);
			long[] array = new long[0];
			pago.setCuotas(array);

			ClienteLinkPago cliente = new ClienteLinkPago();
			String tipoDocumento = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_TIPO_DOCUMENTO,
					persona.getTipoDocumento(), Constantes.MULTITABLA_COLUMNA_VALOR_AUXILIAR);

			cliente.setDocumentoIdentidad(persona.getNumeroDocumento());
			cliente.setEmail(persona.getCorreo());
			cliente.setTipoDocumento(tipoDocumento);
			cliente.setNombre(persona.getNombres());
			cliente.setApellidoPaterno(persona.getApellidoPaterno());
			cliente.setApellidoMaterno(persona.getApellidoMaterno());
			cliente.setCelular(persona.getCelular().toString());
			cliente.setDepartamento(persona.getDepartamento());
			cliente.setProvincia(persona.getProvincia());
			cliente.setDistrito(persona.getDistrito());

			LinkPagoRequest linkPagoRequest = new LinkPagoRequest();
			producto = Constantes.COTIZADOR_VIDA_PRODUCTO_VIDA_FREE;
			linkPagoRequest.setProducto(producto);
			linkPagoRequest.setProductoAcsele(producto.toUpperCase());
			if (solicitudProducto.get().getTipoProducto().equals(Constantes.ADN_PRODUCTO_PLAN_GARANTIZADO)) {
				producto = Constantes.COTIZADOR_VIDA_PRODUCTO_PLAN_GARANTIZADO;
				linkPagoRequest.setProducto(producto);
				linkPagoRequest.setProductoAcsele(producto.toUpperCase());
			}

			linkPagoRequest.setExpiracion(expiracion);
			linkPagoRequest.setAplicacion(aplicacion);
			linkPagoRequest.setPago(pago);
			linkPagoRequest.setCliente(cliente);
			linkPagoRequest.setIdentificador(numeroPropuesta);
			linkPagoRequest.setCodigoEnvio("");
			linkPagoRequest.setBloquearEnvio(true);
			linkPagoRequest.setEstadoPropuesta(solicitudDomain.getEstadoPropuesta().byteValue());
			linkPagoRequest.setEsRecargo(isRecargo);
			linkPagoRequest.setPrimeraPrima(isPrimeraPrima);
			LOGGER.info("linkPagoRequest {}", gson.toJson(linkPagoRequest));
			LinkPagoResponse responseLink = interseguroRestClient.generateLinkPago(linkPagoRequest);
			if (responseLink == null) {
				throw new NullPointerException("GENERA LINK NULL");
			}
			response = LinkPagoResponseDTO.builder().codigoRespuesta(responseLink.getCodigoRespuesta())
					.token(responseLink.getToken()).mensajeRespuesta(responseLink.getMensajeRespuesta())
					.link(responseLink.getLink()).build();
			
			LocalDateTime localDateTime = LocalDateTime.now().plusHours(expiracion);
			LOGGER.info("TokenTime - Localdate: ", LocalDateTime.now());
			LOGGER.info("TokenTime - LocaldateTime: ", localDateTime);
			long millis = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
			LOGGER.info("TokenTime - Milis: ", millis);
			tokenTimeExpiry = String.valueOf(millis);
			LOGGER.info("TokenTime - Expiry: ", tokenTimeExpiry);
		}
		
		Map<String, String> valores = new HashMap<>();
		valores.put("frecuencia", frecuencia);
		valores.put("linkPago", response.getLink());
		valores.put("expiracion", tokenTimeExpiry);
		valores.put("producto", producto);
		Long timeStart = System.nanoTime();

		if (isPrimeraPrima) {
			this.buildBodyLinkPago(solicitudDomain, solicitudProducto.get(), valores, help);
		}
		if (isRecargo) {
			this.buildBodyLinkPagoRecargo(solicitudDomain, solicitudProducto.get(), valores, help, montoRecargo);
		}
		Long timeEnd = System.nanoTime();
		LOGGER.info("Diference enviar correo: {}", (timeEnd - timeStart));
		return response;
	}

	@Async
	public void buildBodyLinkPagoRecargo(Solicitud solicitudDomain, SolicitudProducto productoDomain,
			Map<String, String> valores, boolean help, String montoRecargo) {
		String traza = Utilitarios.trazaLog() + "-" + solicitudDomain.getNumeroPropuesta();
		LOGGER.info("LINK-PAGO ==> [{}] Entra a SolicitudServiceImpl.buildBodyLinkPago({})", traza,
				solicitudDomain.getNumeroCotizacion());
		try {
			String bodyHtml = getBodyHtml(traza);

			Persona personaAsegurado = solicitudDomain.getPersonaByIdAsegurado();
			String cliente = Utilitarios.nombresCompletos(personaAsegurado.getNombres(),
					personaAsegurado.getApellidoPaterno(), personaAsegurado.getApellidoMaterno(), "");

			String emailCliente = personaAsegurado.getCorreo();
			if (!Constantes.SOLICITUD_ASEGURADO_IGUAL_CONTRATANTE_TRUE
					.equals(solicitudDomain.getAseguradoIgualContratante())) {
				Persona personaContratante = solicitudDomain.getPersonaByIdContratante();
				emailCliente = personaContratante.getCorreo();
			}

			String imagen = applyImgEmail(solicitudDomain.getSubplan(), productoDomain.getTipoProducto());

			String bodyAsegurado = bodyHtml;
			bodyAsegurado = bodyAsegurado.replaceAll("\\{imagen\\}", imagen);
			bodyAsegurado = bodyAsegurado.replaceAll("\\{cliente\\}", Utilitarios.toCamelCase(cliente));
			bodyAsegurado = bodyAsegurado.replaceAll("\\{numeroPoliza\\}", solicitudDomain.getNumeroPropuesta());
			bodyAsegurado = bodyAsegurado.replace("{producto}", valores.get("producto"));
			String moneda = Constantes.MONEDA_ADN_SOLES.equals(solicitudDomain.getMoneda())
					? Constantes.MONEDA_SIMBOLO_SOLES
					: Constantes.MONEDA_SIMBOLO_DOLARES;
			// agregar monto
			bodyAsegurado = bodyAsegurado.replace("{montoCuota}", moneda + " " + montoRecargo);
			bodyAsegurado = bodyAsegurado.replaceAll("\\{frecuenciaPago\\}",
					Utilitarios.toCamelCase(valores.get("frecuencia")));
			LOGGER.info("[{}] TokenGenerado:: {}", traza, valores.get("linkPago"));
			bodyAsegurado = bodyAsegurado.replaceAll("\\{link\\}", valores.get("linkPago"));

			enviarCorreoConAdjunto(this.enviarCorreoLinkPagoRecargoAsunto, emailCliente, bodyAsegurado, null, traza, "LINK-RECARGO",solicitudDomain.getNumeroPropuesta(),personaAsegurado.getNumeroDocumento());
			if (help) {
				enviarCorreoConAdjunto(this.enviarCorreoLinkPagoRecargoAsunto, solicitudDomain.getAgenteCorreo(), bodyAsegurado, null, traza,"LINK-RECARGO",solicitudDomain.getNumeroPropuesta(),personaAsegurado.getNumeroDocumento());
			}

			solicitudDomain.setMontoRecargo(Float.valueOf(montoRecargo));
			solicitudDomain.setTokenAdnRecargo(valores.get("linkPago"));
			solicitudDomain.setTokenTimeRecargo(valores.get("expiracion"));
			solicitudDomain.setTokenStatusRecargo(Constantes.SAMP_RESPUESTA_OK);
			solicitudRepository.save(solicitudDomain);

		} catch (Exception e) {
			LOGGER.error("[{}] Error crear cuerpo de correo: {}", traza, e.getMessage());
		}
	}

	/**
	 * Ontiene el monto de recargo.
	 *
	 * @param solicitudDomain
	 * @return monto de recargo
	 */
	private String getMontoRecargo(Solicitud solicitudDomain) {
		ObtenerMontoRecargoRequest requestRecargo = new ObtenerMontoRecargoRequest();
		requestRecargo.setNroCotizacion(solicitudDomain.getNumeroPropuesta());

		ObtenerMontoRecargoResponse globalResponse = globalRestClient.obtenerMontoRecargo(requestRecargo);
		if (globalResponse == null) {
			throw new NullPointerException("MONTO RECARGO NULL");
		}

		String montoRecargo = globalResponse.getMonto();

		if (solicitudDomain.getEstadoTraspaso() != null) {

			SolicitudTraspaso solicitudTraspaso = solicitudTraspasoRepository
					.findByPropuesta(solicitudDomain.getNumeroPropuesta());

			String tag = null;
			double propOne = 0;

			if (solicitudTraspaso.getMonedaOriginal().equals(solicitudTraspaso.getMonedaActual())) {
				propOne = Math.round(solicitudTraspaso.getMontoActual() * 100.0) / 100.0;
			} else {
				tag = solicitudTraspaso.getMonedaActual();

				obtenerTipoCambioRequest tipoCambioRequest = new obtenerTipoCambioRequest();
				tipoCambioRequest.setFecha(String.valueOf(solicitudDomain.getFechaPagoNiubiz()));
				tipoCambioRequest.setTag(tag);

				obtenerTipoCambioResponse tipoCambioResponse = globalRestClient.ObtenerTipoCambio(tipoCambioRequest);
				propOne = Math.round(solicitudTraspaso.getMontoActual() * tipoCambioResponse.getTipoCambio() * 100.0)
						/ 100.0;
			}

			if (solicitudTraspaso.getMontoOriginal() >= propOne) {
				double monto = solicitudTraspaso.getMontoOriginal() - propOne;
				double roundDbl = Math.round(monto * 100.0) / 100.0;
				String montoTraspaso = String.valueOf(roundDbl);

				if (globalResponse.getMonto() != null) {
					double total = roundDbl + Double.valueOf(globalResponse.getMonto());
					montoRecargo = String.valueOf(total);
				} else {
					montoRecargo = montoTraspaso;
				}

			} else {

				double monto = propOne - solicitudTraspaso.getMontoOriginal();
				double roundDbl = Math.round(monto * 100.0) / 100.0;

				if (StringUtils.isNotBlank(globalResponse.getMonto())) {
					double total = Double.valueOf(globalResponse.getMonto()) - roundDbl;
					double roundTotal = Math.round(total * 100.0) / 100.0;
					montoRecargo = String.valueOf(roundTotal);
				}
			}
		}
		return montoRecargo;
	}

	/**
	 * Disponer de una imagen.
	 *
	 * @param subPlan      solicitud subPlan.
	 * @param tipoProducto 1 planGarantizado, 2 vidaFree.
	 * @return imagen elegida.
	 */
	private String applyImgEmail(String subPlan, String tipoProducto) {
		String imagen = "";
		String imagen1 = "http://images.masterbase.com/v1/interseguro/b/cabecera-1.png";// niño
		String imagen2 = "http://images.masterbase.com/v1/interseguro/b/cabecera3-3.png";// chamchito
		String imagen3 = "http://images.masterbase.com/v1/interseguro/b/cabecera2-2.png";// pareja
		if (Constantes.ADN_PRODUCTO_PLAN_GARANTIZADO.equals(tipoProducto)) {
			if (Constantes.ADN_PRODUCTO_PLAN_GARANTIZADO_EDUCACION.equals(subPlan)) {
				imagen = imagen1;
			} else if (Constantes.ADN_PRODUCTO_PLAN_GARANTIZADO_JUBILACION.equals(subPlan)) {
				imagen = imagen3;
			} else {
				imagen = imagen2;
			}
		} else {
			imagen = imagen2;
		}
		return imagen;
	}

	@Override
	public LinkPagoResponseDTO enviarLinkPago(String numeroPropuesta, String idUsuario, String device, String os) {
		Long timeStart = System.nanoTime();
		String traza = Utilitarios.trazaLog();
		LinkPagoResponseDTO response = new LinkPagoResponseDTO();
		LOGGER.info("LINK-PAGO ==> [{}] Entra a SolicitudServiceImpl.enviarLinkPago({})", traza, numeroPropuesta);
		try {
			response = generateLink(numeroPropuesta, false, true, false);
		} catch (Exception e) {
			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			response.setMensajeRespuesta("[ADN-005] Error al enviar link de pago: " + e.getMessage());
			LOGGER.info("LINK-PAGO ==> [{}] Error al enviar link de pago({}): {}", traza, numeroPropuesta,
					e.getMessage());
		}
		LOGGER.info("LINK-PAGO ==> [{}] Sale de SolicitudServiceImpl.enviarLinkPago({})", traza, numeroPropuesta);
		Long timeEnd = System.nanoTime();
		LOGGER.info("Solicitud service Diference enviarLinkPago: {}", (timeEnd - timeStart));
		return response;
	}

	@Override
	public LinkPagoResponseDTO enviarLinkPagoHelp(String numeroPropuesta, String idUsuario, String device, String os) {
		String traza = Utilitarios.trazaLog();
		LinkPagoResponseDTO response = new LinkPagoResponseDTO();
		LOGGER.info("LINK-PAGO ==> [{} ] Entra a SolicitudServiceImpl.enviarLinkPagoHelp({} )", traza, numeroPropuesta);
		try {
			Solicitud solicitud=solicitudRepository.findByNumeroCotizacion(numeroPropuesta);
			int estado= Integer.parseInt(solicitud.getEstado());
			if(estado==4 || estado==5) {
				response = generateLinkHelp(numeroPropuesta, false, true, true);
			}
			LOGGER.info("[{}] Link de pago enviado correctamente.", traza);
		} catch (Exception e) {
			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			response.setMensajeRespuesta("[ADN-005] Error al enviar link de pago: " + e.getMessage());
			LOGGER.error("[{}] Error al enviar link de pago: ", traza + e.getMessage());
		}
		LOGGER.info("[{}] Sale de SolicitudServiceImpl.enviarLinkPagoHelp", traza);
		return response;
	}

	public String generarTokenLinkPago(String idSolicitud, String documento, String cliente, String correoAgente,
			String numeroPropuesta, String plan, String frecuencia, String monto, String time, String moneda,
			String montoValue, String correoCliente, String afiliacion) {

		String traza = Utilitarios.trazaLog();
		LOGGER.info("[{}] Entra a SolicitudServiceImpl.generarTokenLinkPago", traza);
		String encrypt = Strings.EMPTY;

		ObjectMapper om = new ObjectMapper();

		String tokenJson;
		try {
			Map<String, Object> dict = new HashMap<>();
			dict.put("solicitud", idSolicitud);
			dict.put("documento", documento);
			dict.put("cliente", Utilitarios.toCamelCase(cliente));
			dict.put("correoAgente", correoAgente);
			dict.put("numeroPropuesta", numeroPropuesta);
			dict.put("plan", plan);
			dict.put("monto", monto);
			dict.put("frecuencia", frecuencia);
			dict.put("time", time);
			dict.put("moneda", moneda);
			dict.put("montoValue", montoValue);
			dict.put("correoCliente", correoCliente);
			dict.put("afiliacion", afiliacion);

			tokenJson = om.writeValueAsString(dict);
			encrypt = this.encrypt(tokenJson);
			LOGGER.info("[" + traza + "] Token de link de pago generado correctamente.");
		} catch (Exception e) {
			LOGGER.info("[" + traza + "] Error al crear token de link de pago: " + e.getMessage());
			e.printStackTrace();
		}
		LOGGER.info("[" + traza + "] Sale de SolicitudServiceImpl.generarTokenLinkPago");
		return encrypt;
	}

	@Override
	public PagoInicializacionResponseDTO obtenerInicializacionPago() {
		PagoInicializacionResponseDTO response = new PagoInicializacionResponseDTO();
		response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
		response.setMensajeRespuesta(
				Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_EXITO));

		List<ADNInicializacionViaCobroResponseDTO> viasCobro = new ArrayList<>();
		for (SolicitudViaCobro v : viaCobroRepository.findAll()) {
			viasCobro.add(new ADNInicializacionViaCobroResponseDTO(v.getTipoViaCobro(), v.getCodigoViaCobro(),
					v.getNombreViaCobro(), v.getTipoCuenta(), v.getMascara(), v.getLongitud(), v.getCcv()));
		}
		response.setViasCobro(viasCobro);

		LOGGER.info("Salio AdnServiceImpl#obtenerInicializacion()");
		return response;
	}

	@Override
	public SolicitudItemResponseDTO obtenerRegistro(Long id) {
		SolicitudItemResponseDTO response = new SolicitudItemResponseDTO();
		Solicitud solicitudDomain = solicitudRepository.findById(id).get();
		response.setNumeroPropuesta(solicitudDomain.getNumeroPropuesta());
		response.setEstado(Integer.parseInt(solicitudDomain.getEstado()));
		return response;
	}

	@Override
	public SolicitudDetalleResponseDTO obtenerDetalleSolicitud(String numeroCotizacion, String idUsuario, String device,
			String os) {
		LocalDateTime ldt = LocalDateTime.now();
		ZonedDateTime zdt = ldt.atZone(ZoneId.of("America/Los_Angeles"));
		String traza = String.valueOf(zdt.toInstant().toEpochMilli());
		LOGGER.info("[" + traza + "] Entra a SolicitudServiceImpl.obtenerDetalleSolicitud");
		SolicitudDetalleResponseDTO response = new SolicitudDetalleResponseDTO();
		response.setNumeroPropuesta(numeroCotizacion);
		Solicitud solicitudDomain = solicitudRepository.findByNumeroCotizacion(numeroCotizacion);
		response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
		List<String> observaciones = new ArrayList<>();
		if (solicitudDomain != null) {
			String tipoEmision = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TIPO_EMISION,
					String.valueOf(solicitudDomain.getEstadoPropuesta()), Constantes.MULTITABLA_COLUMNA_VALOR);

			response.setAseguradoIgualContratante(solicitudDomain.getAseguradoIgualContratante());
			response.setEstadoSolicitud(solicitudDomain.getEstado());
			response.setEstadoGeneral(solicitudDomain.getEstadoGeneral());
			response.setEstadoRecargo(solicitudDomain.getEstadoRecargo());
			response.setTipoEmision(tipoEmision);
			response.setEstadoPropuesta(String.valueOf(solicitudDomain.getEstadoPropuesta()));
			response.setFlgProcesoEmision(
					solicitudDomain.getFlgProcesoEmision() == null ? "0" : solicitudDomain.getFlgProcesoEmision());
			try {
				response.setFechaFirmaAsegurado(DateUtil.dateToString(solicitudDomain.getFechaFirmaAsegurado(),
						DateUtil.FORMATO_DIA_DDMMYYYY_HHMMSS));
				response.setFechaFirmaContratante(DateUtil.dateToString(solicitudDomain.getFechaFirmaContratante(),
						DateUtil.FORMATO_DIA_DDMMYYYY_HHMMSS));
				response.setFechaSolicitud(DateUtil.dateToString(solicitudDomain.getFechaSolicitud(),
						DateUtil.FORMATO_DIA_DDMMYYYY_HHMMSS));
				response.setFechaPago(DateUtil.dateToString(solicitudDomain.getFechaPagoCulqi(),
						DateUtil.FORMATO_DIA_DDMMYYYY_HHMMSS));
				String passarela = "";
				if (solicitudDomain.getFechaPagoIzipay() != null) {
					response.setFechaPago(DateUtil.dateToString(solicitudDomain.getFechaPagoIzipay(),
							DateUtil.FORMATO_DIA_DDMMYYYY_HHMMSS));
					response.setCodigoTransaccion(solicitudDomain.getIdPagoIzipay());
					passarela = Constantes.PASARELA_IZIPAY;
				} else if (solicitudDomain.getFechaPagoNiubiz() != null) {
					response.setFechaPago(DateUtil.dateToString(solicitudDomain.getFechaPagoNiubiz(),
							DateUtil.FORMATO_DIA_DDMMYYYY_HHMMSS));
					response.setCodigoTransaccion(solicitudDomain.getIdPagoNiubiz());
					passarela = Constantes.PASARELA_NIUBIZ;
				} else {
					response.setFechaPago(DateUtil.dateToString(solicitudDomain.getFechaPagoCulqi(),
						DateUtil.FORMATO_DIA_DDMMYYYY_HHMMSS));
					response.setCodigoTransaccion(solicitudDomain.getIdPagoCulqi());
					passarela = Constantes.PASARELA_CULQI;
				}

				response.setPasarela(passarela);
				response.setFechaSitc(DateUtil.dateToString(solicitudDomain.getFechaAfiliacionPropuesta(),
						DateUtil.FORMATO_DIA_DDMMYYYY_HHMMSS));
				response.setFechaSamp(DateUtil.dateToString(solicitudDomain.getFechaRegistroSamp(),
						DateUtil.FORMATO_DIA_DDMMYYYY_HHMMSS));
			} catch (Exception e1) {
				// e1.printStackTrace();
				response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
				response.setMensajeRespuesta("Error al obtener fechas de solicitud.");
				response.setObjErrorResource(
						new ErrorResourceDTO("ADN-011", "Error al obtener fechas de solicitud: " + e1.getMessage()));
			}
			response.setTienePagoRealizado(solicitudDomain.getFechaPagoCulqi() != null);
			response.setTieneRegistroSitc(solicitudDomain.getFechaAfiliacionPropuesta() != null);
			String idAfiliacionPropuesta = solicitudDomain.getIdAfiliacionPropuesta() == null ? ""
					: solicitudDomain.getIdAfiliacionPropuesta();
			response.setIdAfiliacion(idAfiliacionPropuesta);
			if (!StringUtils.equals(idAfiliacionPropuesta, "")) {
				TarjetaPrimeraPrima primerPago = null;
				try {
					primerPago = sitcRepository.obtenerTarjetaPrimeraPrima(numeroCotizacion);
				} catch (Exception e) {
					response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
					response.setMensajeRespuesta("Error al obtener datos de SITC.");
					response.setObjErrorResource(new ErrorResourceDTO("ADN-011", "Error en SITC: " + e.getMessage()));
				}
				observaciones.add(gson.toJson(primerPago));
				LOGGER.info(gson.toJson(primerPago));
				if (primerPago != null) {
					Integer tipoViaCobro = Integer.parseInt(
							primerPago.getTipoViaCobro() == null || primerPago.getTipoViaCobro().isEmpty() ? "0"
									: primerPago.getTipoViaCobro());
					if (tipoViaCobro == 1) {
						tipoViaCobro = 2;
					} else if (tipoViaCobro == 2) {
						tipoViaCobro = 1;
					}
					String tipoViaCobroDescribe = "";
					switch (tipoViaCobro) {
					case 1:
						tipoViaCobroDescribe = "Tarjeta ";
						break;
					case 2:
						tipoViaCobroDescribe = "Cuenta ";
						break;
					case 3:
						tipoViaCobroDescribe = "Caja Ventanilla ";
						break;
					default:
						tipoViaCobroDescribe = "Caja Ventanilla";
					}
					List<SolicitudViaCobro> viasCobro = solicitudViaCobroRepository.findByTipoVia(tipoViaCobro);

					String viaCobroDescribe = "";
					if (tipoViaCobro != 3) {
						// validar si el dato obtenido es diferente a null y diferente a vacio? 0
						/*
						 * String priPa = primerPago.getViaCobro() != null &&
						 * !primerPago.getViaCobro().isEmpty() ? primerPago.getViaCobro() : "0";
						 */
						Integer viaCobro = Integer.parseInt(primerPago.getViaCobro());
						viaCobroDescribe = viasCobro.stream().filter(v -> v.getCodigoViaCobro() == viaCobro).findFirst()
								.orElse(new SolicitudViaCobro()).getNombreViaCobro();

						viaCobroDescribe = viaCobroDescribe == null ? "" : viaCobroDescribe;

					}
					response.setDescribePagoPrimero(tipoViaCobroDescribe + viaCobroDescribe);
				} else {
					String viaCobroDescribe2 = "";
					Integer tipoViaCobro = 1; // tarjeta
					List<SolicitudViaCobro> viasCobro = solicitudViaCobroRepository.findByTipoVia(tipoViaCobro);

					viaCobroDescribe2 = viasCobro.stream()
							.filter(v -> v.getCodigoViaCobro() == solicitudDomain.getIdViaCobro()).findFirst()
							.orElse(new SolicitudViaCobro()).getNombreViaCobro();

					viaCobroDescribe2 = viaCobroDescribe2 == null ? "" : viaCobroDescribe2;
					response.setDescribePagoPrimero("Tarjeta " + viaCobroDescribe2);
				}
				TarjetaPrimaRecurrente pagoRecurrente = sitcRepository.obtenerTarjetaPrimaRecurrente(numeroCotizacion);
				if (pagoRecurrente != null) {

					Integer tipoViaCobro = Integer.parseInt(pagoRecurrente.getViaCobro() == null
							|| (primerPago != null && primerPago.getTipoViaCobro().isEmpty()) ? "0"
									: pagoRecurrente.getViaCobro());
					if (tipoViaCobro == 1) {
						tipoViaCobro = 2;
					} else if (tipoViaCobro == 2) {
						tipoViaCobro = 1;
					}
					String tipoViaCobroDescribe = "";
					switch (tipoViaCobro) {
					case 1:
						tipoViaCobroDescribe = "Tarjeta ";
						break;
					case 2:
						tipoViaCobroDescribe = "Cuenta ";
						break;
					case 3:
						tipoViaCobroDescribe = "Caja Ventanilla ";
						break;
					default:
						tipoViaCobroDescribe = "Caja Ventanilla";
					}
					List<SolicitudViaCobro> viasCobro = solicitudViaCobroRepository.findByTipoVia(tipoViaCobro);
					String viaCobroDescribe = "";
					if (tipoViaCobro != 3) {

						Integer viaCobro = Integer.parseInt(pagoRecurrente.getMedioPago());
						viaCobroDescribe = viasCobro.stream().filter(v -> v.getCodigoViaCobro() == viaCobro).findFirst()
								.orElse(new SolicitudViaCobro()).getNombreViaCobro();
						viaCobroDescribe = viaCobroDescribe == null ? "" : viaCobroDescribe;
					}
					response.setDescribePagoRecurrente(tipoViaCobroDescribe + viaCobroDescribe);
				}
			}
			response.setTieneRegistroSamp(solicitudDomain.getFechaRegistroSamp() != null);
			response.setJsonContratante(solicitudDomain.getJsonContratante());
			response.setLinkEnviado(solicitudDomain.getTokenStatus() != null);
			response.setLinkUsado("2".equals(solicitudDomain.getTokenStatus()));
			response.setNumeroPropuesta(solicitudDomain.getNumeroPropuesta());

			// Moneda
			List<Multitabla> mMoneda = multitablaRepository.findByCodigoTabla(TablaEnum.TABLA_MONEDA.getCodigoTabla());
			Multitabla tiposMoneda = null;
			if (!mMoneda.isEmpty()) {
				System.out.println("============M1=========" + solicitudDomain.getMoneda());
				if (solicitudDomain.getMoneda() != null) {
					List<Multitabla> listItem = mMoneda.stream()
							.filter(t -> t.getCodigo().equals(solicitudDomain.getMoneda()))
							.collect(Collectors.toList());
					if (!listItem.isEmpty()) {
						tiposMoneda = listItem.get(0);
					}
				}
			}
			response.setMoneda(tiposMoneda.getValor());

			CodigoVerificacion cv = null;
			List<CodigoVerificacion> listaCv = codigoVerificacionRepository
					.findCodigoActivo(solicitudDomain.getIdSolicitud(), solicitudDomain.getIdAsegurado());
			CodigoVerificacion cva = null;
			List<CodigoVerificacion> listaCva = codigoVerificacionRepository
					.findCodigoActivo(solicitudDomain.getIdSolicitud(), solicitudDomain.getIdContratante());

			if (listaCv != null && !listaCv.isEmpty()) {
				cv = listaCv.get(0);
			}
			if (listaCva != null && !listaCva.isEmpty()) {
				cva = listaCva.get(0);
			}
			String codigo = "";
			String codigoCont = "";
			if (cv != null && cv.getIdCodigoVerificacion() != null) {
				codigo = cv.getCodigo();
				response.setCodigoSms(this.decrypt(codigo));
			}
			if (cva != null && cva.getIdCodigoVerificacion() != null) {
				codigoCont = cva.getCodigo();
				response.setCodigoSmsContratante(this.decrypt(codigoCont));
			}
			SolicitudProducto producto = solicitudProductoRepository
					.findByIdSolicitud(solicitudDomain.getIdSolicitud());
			if (producto != null) {
				response.setFechaCotizacion(producto.getFechaCotizacion());
				String frecuencia = this.obtenerCodigoMultiTabla(Constantes.MULTITABLA_CODIGO_FRECUENCIA,
						Constantes.MULTITABLA_COLUMNA_VALOR_AUXILIAR, producto.getFrecuencia());
				response.setFrecuencia(frecuencia);
				String subPlan = "";
				if ("1".equals(producto.getTipoProducto())) {
					subPlan = this.obtenerCodigoMultiTabla(Constantes.MULTITABLA_CODIGO_SUBPLAN_COTIZADOR,
							Constantes.MULTITABLA_COLUMNA_VALOR, solicitudDomain.getSubplan());
				} else {
					subPlan = this.obtenerCodigoMultiTabla(Constantes.MULTITABLA_CODIGO_SUBPLAN_COTIZADOR_VIDA_FREE,
							Constantes.MULTITABLA_COLUMNA_VALOR, solicitudDomain.getSubplan());
				}
				response.setPlan(subPlan);
				try {
					response.setPrimaComercial(
							Utilitarios.formatoMiles(Double.parseDouble(producto.getPrimaComercial().toString()),
									Utilitarios.FORMATO_MILES_CON_DECIMAL));
					response.setPrimaIgv(Utilitarios.formatoMiles(Double.parseDouble(producto.getPrimaIgv().toString()),
							Utilitarios.FORMATO_MILES_CON_DECIMAL));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				response.setProducto("1".equals(producto.getTipoProducto()) ? "PlanGarantizado" : "VidaFree");
				response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
				String mensajeRpta = "";
				if (response.getDescribePagoPrimero() != null && !response.getDescribePagoPrimero().isEmpty()) {
					mensajeRpta = "via cobro pago primero " + response.getDescribePagoPrimero();
				} else if (response.getDescribePagoPrimero() != null && !response.getDescribePagoPrimero().isEmpty()) {
					mensajeRpta = "via cobro pago recurrente " + response.getDescribePagoRecurrente();
				}
				response.setMensajeRespuesta(mensajeRpta);
			}

		}
		/*
		 * logRepository.save(new EventLog("Cotizador", response.getCodigoRespuesta(),
		 * "/cotizaciones/detalle/" + numeroCotizacion, response.getMensajeRespuesta(),
		 * gson.toJson(response.getObjErrorResource()), gson.toJson(observaciones),
		 * idUsuario, device, os));
		 */
		LOGGER.info("[" + traza + "] Salio de SolicitudServiceImpl.obtenerDetalleSolicitud");
		return response;
	}

	@Override
	public ByteArrayInputStream leerArchivo(String numeroCotizacion, String nombreArchivo) {

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		File file = new File(rutaPrivadaPDFSolicitud + numeroCotizacion + "//" + nombreArchivo);

//		File file = new File("C:\\Users\\Anedd\\ti-is-admin\\DOCUMENTOS\\" + numeroCotizacion + "\\" + nombreArchivo);

		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
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
	public BaseResponseDTO subirArchivo(MultipartFile file, String numeroCotizacion) {

		BaseResponseDTO response = new BaseResponseDTO();

		// String nombreArchivoOriginal =
		// FilenameUtils.getBaseName(file.getOriginalFilename());
		String extensionArchivoOriginal = FilenameUtils.getExtension(file.getOriginalFilename());
		// String nombreArchivoTmp = "";

		try {

			if (extensionArchivoOriginal.equals("pdf")) {

				/*
				 * //new File(rutaPrivadaPDFSolicitud + numeroCotizacion).mkdir();
				 *
				 * // new File("C:\\Users\\Anedd\\ti-is-admin\\DOCUMENTOS\\" +
				 * numeroCotizacion).mkdir();
				 *
				 * File newFile = new File(rutaPrivadaPDFSolicitud + numeroCotizacion + "/" +
				 * nombreArchivoOriginal + "_" + new
				 * SimpleDateFormat("yyyyMMddHHmmss").format(new Date()).toString() + "." +
				 * extensionArchivoOriginal);
				 *
				 * // File newFile = new
				 * File("C:\\Users\\Anedd\\ti-is-admin\\DOCUMENTOS\\" + numeroCotizacion + "/"
				 * // + nombreArchivoOriginal + "_" + new
				 * SimpleDateFormat("yyyyMMddHHmmss").format(new Date()).toString() // + "." +
				 * extensionArchivoOriginal);
				 *
				 * FileOutputStream fos = new FileOutputStream(newFile); BufferedOutputStream
				 * salidaStream = new BufferedOutputStream(fos); salidaStream.write(
				 * archivo.getBytes() );
				 *
				 * salidaStream.flush(); salidaStream.close(); fos.close();
				 */

				LocalDateTime ldt = LocalDateTime.now();
				ZonedDateTime zdt = ldt.atZone(ZoneId.of("America/Los_Angeles"));
				String traza = String.valueOf(zdt.toInstant().toEpochMilli());
				LOGGER.info("[" + traza + "] Entra a SolicitudServiceImpl.obtenerDetalleSolicitud");

				Solicitud solicitud = solicitudRepository.findByNumeroCotizacion(numeroCotizacion);
				Persona personaAsegurado = personaRepository
						.findById(solicitud.getPersonaByIdAsegurado().getIdPersona()).get();
				// Grabar solicitud PDFS
				File convFiles = new File(file.getOriginalFilename());
				String archivo = convFiles.getName().substring(0, convFiles.getName().length() - 4);

				StringBuilder sb = new StringBuilder();
				sb.append(archivo);
				sb.append("_");
				sb.append(solicitud.getNumeroPropuesta());
				sb.append(".pdf");

				String nombreArchivoSolicitud = sb.toString();
				LOGGER.info("nombreARchivo: " + nombreArchivoSolicitud);

				String urlPrivadaSolicitud = "" + nombreArchivoSolicitud;
				// String urlPrivadaSolicitud = rutaPDFPrivada + nombreArchivoSolicitud;
				LOGGER.info("convFiles: " + convFiles);
				File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + new File(nombreArchivoSolicitud));
				file.transferTo(convFile);
				LOGGER.info("convFile: " + convFile);

				LOGGER.info("nombreSolicitud=>" + nombreArchivoSolicitud);
				LOGGER.info("SolicituDigital PDF copiado a=>" + urlPrivadaSolicitud);
				solicitudRepository.updateNombreArchivoSolicitud(nombreArchivoSolicitud, solicitud.getIdSolicitud());
				String tipoDocumentoCRM = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_TIPO_DOCUMENTO,
						personaAsegurado.getTipoDocumento(), Constantes.MULTITABLA_COLUMNA_VALOR_CRM);
				String numeroDocumento = personaAsegurado.getNumeroDocumento();
				String tipoDocumento = personaAsegurado.getTipoDocumento();

				this.enviarPDFCRM2(convFile, tipoDocumentoCRM, numeroDocumento, nombreArchivoSolicitud,
						nombreArchivoSolicitud, tipoDocumento);

				response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
				response.setMensajeRespuesta("OK");

			} else {
				response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR_FUNCIONAL);
				response.setMensajeRespuesta("El archivo a subir debe tener la extensión PDF.");
			}

		} catch (Exception e) {
			e.printStackTrace();

			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			response.setMensajeRespuesta("No se puede subir el archivo.");

		}

		return response;
	}

	@Override
	public ObtenerDocumentoResponseDTO obtenerDocumento(String numeroCotizacion) {

		ObtenerDocumentoResponseDTO response = new ObtenerDocumentoResponseDTO();
//		List<ObtenerDocumentoItemResponseDTO> listaArchivos = new ArrayList<ObtenerDocumentoItemResponseDTO>();

		String[] archivos;

		File newFolder = new File(rutaPrivadaPDFSolicitud + numeroCotizacion);

//		File newFolder = new File("C:\\Users\\Anedd\\ti-is-admin\\DOCUMENTOS\\" + numeroCotizacion);

		archivos = newFolder.list();

//		if( archivos != null ) {
//			for(String item : archivos) {
//
//				ObtenerDocumentoItemResponseDTO archivo = new ObtenerDocumentoItemResponseDTO();
//
//				a = a + 1;
//				archivo.setNombre_Archivo(item);
//				archivo.setId_archivo(String.valueOf(a));
//
//				listaArchivos.add(archivo);
//
//			}
//		}
//		response.setArchivos(listaArchivos);

		response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
		response.setMensajeRespuesta("OK");
		response.setArchivos(archivos);

		return response;
	}

	@Override
	public CMRObtenerArchivoResponseDTO obtenerArchivo(String files, String nroCot) {

		CMRObtenerArchivoResponseDTO response = new CMRObtenerArchivoResponseDTO();
		Map<String, CMRArchivoResponseDTO> cotizacionesMap = new HashMap<>();

		Persona personaAsegurado = personaRepository.findByNumeroDocumento(files);

		System.out.println(files);
		ObtenerUploadArchivoResponse globalArchivoResponse = globalRestClient
				.obtenerArchivo(personaAsegurado.getTipoDocumento(), personaAsegurado.getNumeroDocumento(), nroCot);

		for (ArchivoResponse item : globalArchivoResponse.getFiles()) {
			if (item.getName() == null || item.getName().isEmpty()) {
				continue;
			}
			if (item.getName().toLowerCase().contains("cotizacion")) {
				String[] parts = item.getName().split("_");
				// Tomar la parte del nombre que está entre 'cotizacion' y '.pdf'
				String baseName = parts[0].startsWith("C") ? parts[2].replace(".pdf","") : parts[1];

				CMRArchivoResponseDTO existingArchivo = cotizacionesMap.get(baseName);
				if (existingArchivo == null || existingArchivo.getFileDateConvert().compareTo(item.getFileDateConvert()) < 0) {
					cotizacionesMap.put(baseName, createArchivo(item, globalArchivoResponse, files));
				}
			}else{
				cotizacionesMap.put(item.getName(), createArchivo(item, globalArchivoResponse, files));
			}
		}

		//documentos del contratante
		try{
			Solicitud sol= solicitudRepository.findByNumeroCotizacion(nroCot);
			if(sol.getAseguradoIgualContratante().equals("2")){
				Persona personaContratante = personaRepository.findById(sol.getIdContratante()).get();
				String contraDoc = personaContratante.getNumeroDocumento();
				String contraTipoDoc = personaContratante.getTipoDocumento();
				ObtenerUploadArchivoResponse globalArchivoResponse2 = globalRestClient
						.obtenerArchivo(contraTipoDoc, contraDoc, nroCot);
				for (ArchivoResponse item : globalArchivoResponse2.getFiles()) {
					if (item.getName() == null || item.getName().isEmpty()) {
						continue;
					}
					if (item.getName().toLowerCase().contains("ce") || item.getName().toLowerCase().contains("dni") || item.getName().toLowerCase().contains("selfie")
							|| item.getName().toLowerCase().contains("documento_validacion")) {
						String name_contra=item.getName();
						String newname=name_contra.replace(personaContratante.getNumeroDocumento(),"contratante");
						cotizacionesMap.put(newname, createArchivo(item, globalArchivoResponse2, contraDoc));
					}
				}
				String xxx="x";
			}
		}catch (Exception e){
			String xxx="";
			System.out.println(e.getMessage());
		}

		// Agregar al response los archivos de cotización (últimos de cada serie)
		response.setArchivos(new ArrayList<>(cotizacionesMap.values()));
		response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
		response.setMensajeRespuesta("OK");

		return response;
	}
	private CMRArchivoResponseDTO createArchivo(ArchivoResponse item,ObtenerUploadArchivoResponse globalArchivoResponse,String files) {
		CMRArchivoResponseDTO archivo = new CMRArchivoResponseDTO();
		UrlResponse globalUrlArchivoResponse = globalRestClient.obtenerUrlArchivo(globalArchivoResponse.getContactId(), files, item.getName());
		archivo.setFile(globalUrlArchivoResponse.getUrl());
		archivo.setName(item.getName());
		archivo.setFileDateConvert(item.getFileDateConvert());
		return archivo;
	}
	private String evaluator(String propuesta) {
		String trazaLog = Utilitarios.trazaLog() + "-" + propuesta;
		String strData = "EVALUANDO - pubsubsend";
		LOGGER.info("{} Entro  evaluator --> {}", trazaLog, strData);
		Solicitud solicitud = solicitudRepository.findByNumeroCotizacion(propuesta);

		if (solicitud.getEstadoPubSubSend() == null && solicitud.getEstadoPropuesta() == null) {
			ObtenerEvaluacionSolicitudEmisionRequest obtenerEvaluacionSolicitudEmisionRequest = new ObtenerEvaluacionSolicitudEmisionRequest();
			obtenerEvaluacionSolicitudEmisionRequest.setNroSolicitud(propuesta);
			ObtenerEvaluacionSolicitudEmisionResponse responseGlobal = globalRestClient
					.obtenerEvaluacionSolicitudEmision(obtenerEvaluacionSolicitudEmisionRequest);
			LOGGER.info("{} Evaluator {}", trazaLog, gson.toJson(responseGlobal));
			strData = "evaluado por pubsubSend";
		}
		LOGGER.info("{} Salio evaluator --> {}", trazaLog, strData);
		return strData;
	}

	@Override
	public ObtenerPubSubSendResponseDTO obtenerPubSubSend(ObtenerPubSubSendRequestDTO request) {
		String traza = Utilitarios.trazaLog() + "-" + request.getPropuesta();
		LOGGER.info("{} LINK-PAGO ==> Entro a SolicitudServiceImpl#obtenerPubSubSend()", traza);
		// Inicializamos la respuesta
		ObtenerPubSubSendResponseDTO respuesta = new ObtenerPubSubSendResponseDTO();
		// Obtener el estado_propuesta del api solicitud

		String evaluationText = this.evaluator(request.getPropuesta());
		LOGGER.info("{} GTI - 53329 --> {}", traza, evaluationText);
		Solicitud solicitud = solicitudRepository.findByNumeroCotizacion(request.getPropuesta());
		ObtenerEstadoSolicitudEmisionRapidaRequest reqEstadoSolicitud = new ObtenerEstadoSolicitudEmisionRapidaRequest();
		reqEstadoSolicitud.setPropuesta(request.getPropuesta());
		LOGGER.info("{} LINK-PAGO ==>Inicio Obtener el estado_propuesta del api solicitud", traza);
		LOGGER.info("{} LINK-PAGO ==> Request {}", traza, gson.toJson(reqEstadoSolicitud));
		ObtenerEstadoSolicitudEmisionRapidaResponse respEstadoSolicitud = globalRestClient
				.obtenerEstadoSolicitudEmisionRapida(reqEstadoSolicitud);
		LOGGER.info("{} LINK-PAGO ==> Response {}", traza, gson.toJson(respEstadoSolicitud));
		LOGGER.info("{} LINK-PAGO ==>Fin Obtener el estado_propuesta del api solicitud", traza);
		/* Campos para guardar nativamente */
		// *setEstadoPubSubSend
		// *setNumeroPoliza
		if (respEstadoSolicitud.getEstado().equals("1")) {
			LOGGER.info("{} Estado PubSubSend ==> {}", traza, solicitud.getEstadoPubSubSend());
			if (solicitud.getEstadoPubSubSend() == null) {
				LOGGER.info("{} LINK-PAGO ==>Inicio Traer la poliza del api-solicitud", traza);
				ObtenerPolizaEmisionRapidaRequest reqPoliza = new ObtenerPolizaEmisionRapidaRequest();
				reqPoliza.setPropuesta(request.getPropuesta());
				LOGGER.info("{} LINK-PAGO ==> Request {}", traza, gson.toJson(reqPoliza));
				ObtenerPolizaEmisionRapidaResponse respoliza = globalRestClient.obtenerPolizaEmisionRapida(reqPoliza);
				LOGGER.info("{} LINK-PAGO ==> Response {}", traza, gson.toJson(respoliza));
				LOGGER.info("{} LINK-PAGO ==>Fin Traer la poliza del api-solicitud", traza);

				// Obtener credenciales para el X-Apikey
				LOGGER.info("{} LINK-PAGO ==>Inicio ObtenerPubSubSend", traza);
				ObtenerPubSubSendRequest requestPubSubSend = new ObtenerPubSubSendRequest();
				ObtenerMetadataPublishRequest requestMetaDataPublish = new ObtenerMetadataPublishRequest();
				requestPubSubSend.setTopic(topic);
				requestMetaDataPublish.setApplicationUser(applicationUser);
				requestMetaDataPublish.setOrigin(applicationUser);
				LOGGER.info("{} poliza {}", traza, respoliza.getPoliza());
				requestMetaDataPublish.setPoliza(respoliza.getPoliza());
				requestMetaDataPublish.setPropuesta(request.getPropuesta());
				LocalDateTime locaDate = LocalDateTime.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
				String formatDateTime = locaDate.format(formatter);
				LOGGER.info("{} formatDateTime {}", traza, formatDateTime);
				requestMetaDataPublish.setFechaOperacion(formatDateTime);
				requestMetaDataPublish.setTipoOperacion("AUTOMATICO");
				// solicitud.setEstadoPubSubSend("1");
				// solicitud.setNumeroPoliza(respoliza.getPoliza());
				List<ObtenerMetadataPublishRequest> listMetaDataPublish = new ArrayList<>();
				listMetaDataPublish.add(requestMetaDataPublish);
				requestPubSubSend.setMetadataPublish(listMetaDataPublish);
				LOGGER.info("{} LINK-PAGO ==> Request {}", traza, gson.toJson(requestPubSubSend));
				ObtenerPubSubSendResponse pubSubSend = globalRestClient.obtenerPubSubSendResponse(requestPubSubSend);
				LOGGER.info("{} LINK-PAGO ==> Response {}", traza, gson.toJson(pubSubSend));
				LOGGER.info("{} LINK-PAGO ==> Fin ObtenerPubSubSend", traza);

				// Generamos la respuesta
				respuesta.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
				respuesta.setMensajeRespuesta(
						Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_EXITO));
				respuesta.setCode(pubSubSend.getCode());
				respuesta.setTitle(pubSubSend.getTitle());
				respuesta.setMessage(pubSubSend.getMessage());
				ObtenerDataResponseDTO dataResponse = new ObtenerDataResponseDTO();
				dataResponse.setMessage(pubSubSend.getData().getMessage());
				respuesta.setData(dataResponse);
				// solicitudRepository.save(solicitud);
				solicitud.setEstadoPubSubSend("1");
				solicitud.setNumeroPoliza(respoliza.getPoliza());
				solicitudRepository.save(solicitud);
				solicitudRepository.flush();
				// solicitudRepository.updateSolcitudPubSubSend("1",
				// solicitud.getIdSolicitud());
			} else {
				respuesta.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
				respuesta.setMensajeRespuesta(
						Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_EXITO));
			}

		} else {
			respuesta.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
			respuesta.setMensajeRespuesta(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_EXITO));
		}
		LOGGER.info("{} PUBSUBSEND ==>  salio a SolicitudServiceImpl#obtenerPubSubSend()", traza);
		return respuesta;
	}

	@Override
	public SolicitudCotizacionesTraspasoResponseDTO obtenerCotizacionesTraspaso(String numDocCliente) {
		String traza = Utilitarios.trazaLog();
		traza = traza + "-" + numDocCliente;
		LOGGER.info("[" + traza + "] Entro AdnServiceImpl#obtenerReglamento(tipoDocCliente, numDocCliente, idUsuario)");

		List<SolicitudItemCotizacionesTraspasoResponseDTO> listados = new ArrayList<>();

		ObtenerReglasAsigClienteRequest request = new ObtenerReglasAsigClienteRequest();
		request.setNumDoc(numDocCliente);

		ListObtenerCotizacionesResponse responseCot = globalRestClient.obtenerCotizacion(request);

		if (responseCot != null) {

			/*  */
			responseCot.getRespuesta().forEach(i -> {

				SolicitudItemCotizacionesTraspasoResponseDTO bean = new SolicitudItemCotizacionesTraspasoResponseDTO();

				bean.setNumero_cotizacion(i.getNumero_cotizacion());
				bean.setNumero_propuesta(i.getNumero_propuesta());
				bean.setTipo_producto(i.getTipo_producto());
				bean.setEstado(i.getEstado());
				bean.setEstado_general(i.getEstado_general());
				listados.add(bean);
			});
		}

		SolicitudCotizacionesTraspasoResponseDTO response = new SolicitudCotizacionesTraspasoResponseDTO();
		response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
		response.setMensajeRespuesta(
				Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_EXITO));
		response.setLista(listados);

		return response;
	}

	@Override
	public SolicitudAcreditacionResponseDTO consultarAcreditacion(String numCotizacion) {
		String traza = Utilitarios.trazaLog();
		traza = traza + "-" + numCotizacion;
		LOGGER.info("[" + traza + "] Entro AdnServiceImpl#obtenerReglamento(tipoDocCliente, numDocCliente, idUsuario)");

		List<SolicitudItemAcreditacionResponseDTO> listados = new ArrayList<>();

		Gson gson = new Gson();

		ObtenerReglasAsigClienteRequest request = new ObtenerReglasAsigClienteRequest();
		request.setNumCotizacion(numCotizacion);
		System.out.println(gson.toJson(request));
		ConsultarAcreditacionResponse responseCot = globalRestClient.consultarAcreditacion(request);

		if (responseCot != null) {

			/* ; */
			SolicitudItemAcreditacionResponseDTO bean = new SolicitudItemAcreditacionResponseDTO();

			bean.setNumCot(responseCot.getNumCot());
			bean.setRpta(responseCot.getRpta());
			listados.add(bean);

		}

		SolicitudAcreditacionResponseDTO response = new SolicitudAcreditacionResponseDTO();
		response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
		response.setMensajeRespuesta(
				Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_EXITO));
		response.setLista(listados);

		return response;
	}

	@Override
	public RecotizacionValidarResponseDTO validarRecotizacion(String cotizacion) {
		RecotizacionValidarResponseDTO recotizacionValidarResponseDTO = new RecotizacionValidarResponseDTO();
		Solicitud solicitud = solicitudRepository.findByNumeroCotizacion(cotizacion);

		if (solicitud == null) {
			recotizacionValidarResponseDTO.setCodigoRespuesta("204");
			recotizacionValidarResponseDTO.setMensajeRespuesta("No se encontró la cotización");
			recotizacionValidarResponseDTO.setObservaciones("No se encontró la cotización");
			recotizacionValidarResponseDTO.setPuedeRecotizar(true);
			return recotizacionValidarResponseDTO;
		}

		recotizacionValidarResponseDTO.setCodigoRespuesta("200");
		recotizacionValidarResponseDTO.setMensajeRespuesta("OK");
		recotizacionValidarResponseDTO.setEstado(Integer.parseInt(solicitud.getEstado()));
		recotizacionValidarResponseDTO.setCotizacion(cotizacion);
		if (Integer.parseInt(solicitud.getEstado()) < 4) {
			recotizacionValidarResponseDTO.setPuedeRecotizar(true);
		}
		return recotizacionValidarResponseDTO;
	}

	@Override
	public ObtenerRequestEvaluatorResponseDTO obtenerRequestEvaluator(ObtenerRequestEvaluatorRequestDTO request) {
		LOGGER.info("EVALUAR-SOLICITUD ==> Entro a SolicitudServiceImpl#obtenerRequestEvaluator()");
		// Inicializamos la respuesta
		ObtenerRequestEvaluatorResponseDTO respuesta = new ObtenerRequestEvaluatorResponseDTO();
		Solicitud solicitud = solicitudRepository.findById(request.getIdSolicitud()).get();
		SolicitudProducto solicitudProducto = solicitudProductoRepository.findByIdSolicitud(solicitud.getIdSolicitud());
		Integer estadoPropuesta = solicitud.getEstadoPropuesta();
		String estado = solicitud.getEstado();
		String alertaPlaft = solicitud.getAlertaPlaft();

		try {
			// Integer alertaPlaft = 1;
			CalificacionPropuestaRequest calificacionPropuestaRequest = new CalificacionPropuestaRequest();
			CalificacionPropuestaResponse plaftResponse = new CalificacionPropuestaResponse();
			ViewSolicitudPlaft view = viewSolicitudPlaftRepository.findById(solicitud.getIdSolicitud()).get();
			LOGGER.info("Seguimiento al transmitir");
			// if (solicitudProducto.getTipoProducto().equals("1")) {

				if (view != null) {

					calificacionPropuestaRequest
							.setPropuesta(view.getNumeroPropuesta() == null ? "" : view.getNumeroPropuesta());
					calificacionPropuestaRequest.setPrima_anualizada(
							view.getPrimaAnualizada().toString() == null ? "" : view.getPrimaAnualizada().toString());
					calificacionPropuestaRequest.setProducto(view.getProducto() == null ? "" : view.getProducto());
					calificacionPropuestaRequest.setMoneda(view.getMoneda() == null ? "" : view.getMoneda());
					calificacionPropuestaRequest.setContratante_tipo_documento(
							view.getContratanteTipoDocumento() == null ? "" : view.getContratanteTipoDocumento());
					calificacionPropuestaRequest.setContratante_documento(
							view.getContratanteDocumento() == null ? "" : view.getContratanteDocumento());
					calificacionPropuestaRequest
							.setContratante_actividad_economica(view.getContratanteActividadEconomica() == null ? ""
									: view.getContratanteActividadEconomica());
					calificacionPropuestaRequest.setContratante_profesion(
							view.getContratanteProfesion() == null ? "" : view.getContratanteProfesion());
					calificacionPropuestaRequest.setContratante_sujeto_obligado(
							view.getContratanteSujetoObligado() == null ? "" : view.getContratanteSujetoObligado());
					calificacionPropuestaRequest.setContratante_nacionalidad_residencia(
							view.getContratanteNacionalidad() == null ? "" : view.getContratanteNacionalidad());
					calificacionPropuestaRequest
							.setContratante_pep(view.getContratantePep() == null ? "" : view.getContratantePep());

					calificacionPropuestaRequest.setContratante_nombre1(
							view.getContratanteNombres().equals("") ? null : view.getContratanteNombres()); // Se pone
					// al revés
					// ya que no
					// acepta
					// vacíos
					// pero sí
					// nulos

					calificacionPropuestaRequest.setContratante_nombre2("");
					calificacionPropuestaRequest.setContratante_nombre3("");
					calificacionPropuestaRequest.setContratante_apellido_paterno(
							view.getContratanteApellidoPaterno() == null ? "" : view.getContratanteApellidoPaterno());
					calificacionPropuestaRequest.setContratante_apellido_materno(
							view.getContratanteApellidoMaterno() == null ? "" : view.getContratanteApellidoMaterno());
					calificacionPropuestaRequest.setContratante_razon_social(
							view.getContratanteRazonSocial() == null ? "" : view.getContratanteRazonSocial());
					calificacionPropuestaRequest.setAsegurado_tipo_documento(
							view.getAseguradoTipoDocumento() == null ? "" : view.getAseguradoTipoDocumento());
					calificacionPropuestaRequest.setAsegurado_documento(
							view.getAseguradoDocumento() == null ? "" : view.getAseguradoDocumento());
					calificacionPropuestaRequest.setAsegurado_actividad_economica(
							view.getAseguradoActividadEconomica() == null ? "" : view.getAseguradoActividadEconomica());
					calificacionPropuestaRequest.setAsegurado_profesion(
							view.getAseguradoProfesion() == null ? "" : view.getAseguradoProfesion());
					calificacionPropuestaRequest.setAsegurado_sujeto_obligado(
							view.getAseguradoSujetoObligado() == null ? "" : view.getAseguradoSujetoObligado());
					calificacionPropuestaRequest.setAsegurado_nacionalidad_residencia(
							view.getAseguradoNacionalidad() == null ? "" : view.getAseguradoNacionalidad());
					calificacionPropuestaRequest
							.setAsegurado_pep(view.getAseguradoPep() == null ? "" : view.getAseguradoPep());
					calificacionPropuestaRequest
							.setAsegurado_nombre1(view.getAseguradoNombres() == null ? "" : view.getAseguradoNombres());
					calificacionPropuestaRequest.setAsegurado_nombre2("");
					calificacionPropuestaRequest.setAsegurado_nombre3("");
					calificacionPropuestaRequest.setAsegurado_apellido_paterno(
							view.getAseguradoApellidoPaterno() == null ? "" : view.getAseguradoApellidoPaterno());
					calificacionPropuestaRequest.setAsegurado_apellido_materno(
							view.getAseguradoApellidoMaterno() == null ? "" : view.getAseguradoApellidoMaterno());
					calificacionPropuestaRequest.setAsegurado_razon_social(
							view.getAseguradoRazonSocial() == null ? "" : view.getAseguradoRazonSocial());
					calificacionPropuestaRequest.setContratante_residencia(
							view.getContratanteResidencia() == null ? "" : view.getContratanteResidencia());
					calificacionPropuestaRequest.setAsegurado_residencia(
							view.getAseguradoResidencia() == null ? "" : view.getAseguradoResidencia());
					plaftResponse = plaftRestClient.calificarPropuestaPlaft(calificacionPropuestaRequest);
				}
				//
				// Solicitud solicitudPlaft =
				// solicitudRepository.findById(request.getIdSolicitud()).get();
				if (plaftResponse.getMeta().getStatus().equals("SUCCESS")) {
					if (plaftResponse.getRecords().getImprimible().equals(0)) {
						// alertaPlaft = 2;
						solicitud.setAlertaPlaft("PLAFT");
						alertaPlaft = "PLAFT";
						// solicitudRepository.save(solicitudPlaft);
					}
				}
			// }

			LOGGER.info("EVALUAR-SOLICITUD ==> GOLPEAR AL API EVALUACION");
			/* DESARROLLO - GOLPEAR AL API EVALUACION */
			ObtenerEvaluacionSolicitudEmisionRequest obtenerEvaluacionSolicitudEmisionRequest = new ObtenerEvaluacionSolicitudEmisionRequest();
			obtenerEvaluacionSolicitudEmisionRequest.setNroSolicitud(solicitud.getNumeroCotizacion());
			ObtenerEvaluacionSolicitudEmisionResponse responseGlobal = globalRestClient
					.obtenerEvaluacionSolicitudEmision(obtenerEvaluacionSolicitudEmisionRequest);
			LOGGER.info("GTI - 53329");
			LOGGER.info(gson.toJson(responseGlobal));
			LOGGER.info(responseGlobal.getMensajeRespuesta());
			solicitud.setEstadoPropuesta(Integer.valueOf(responseGlobal.getMensajeRespuesta()));

			solicitud.setEstado(Constantes.CODIGO_SOLICITUD_POR_TRANSMITIR);

			LOGGER.info("Evaluacion teminado de la propuesta: " + solicitud.getNumeroPropuesta()
					+ " | estado propuesta --> " + responseGlobal.getMensajeRespuesta() + " | estado -->  "
					+ Constantes.CODIGO_SOLICITUD_POR_TRANSMITIR);

			respuesta.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
			respuesta.setMensajeRespuesta(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_EXITO));
			respuesta.setEstadoPropuesta(responseGlobal.getMensajeRespuesta());
			respuesta.setNumeroPropuesta(solicitud.getNumeroCotizacion());

		} catch (Exception e) {
			/* Actualizar estado de la solicitud al estado 3 */

			solicitud.setEstado(Constantes.CODIGO_SOLICITUD_FIRMA_FINALIZADA);

			// solicitudRepository.save(solicitud);
			respuesta.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			respuesta.setMensajeRespuesta(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_REQUEST_EVALUATOR));
			LOGGER.info("EVALUAR-SOLICITUD ==> Catch SolicitudServiceImpl#obtenerRequestEvaluator()");
		}

		LOGGER.info("PrimerUpdate " + solicitud.getNumeroPropuesta() + " ==> updateSolcitudEvaluacion = "
				+ estadoPropuesta + " | " + estado + " | " + alertaPlaft + " | " + request.getIdSolicitud());
		// solicitudRepository.updateSolcitudEvaluacion(estadoPropuesta, estado,
		// alertaPlaft, request.getIdSolicitud());
		// LOGGER.info("SegundoUpdate");
		solicitudRepository.save(solicitud);

		return respuesta;
	}

	@Override
	public BaseResponseDTO policyIssued(PolicyIssuedRequestDTO policyIssuedRequestDTO) {
		long timeStart = System.nanoTime();
		LOGGER.info("timeStart policyIssued {}", timeStart);
		long timeEnd;
		String trazalog = Utilitarios.trazaLog() + "-" + policyIssuedRequestDTO.getNumeroPropuesta();
		LOGGER.info("{} EMISION-SOLICITUD ==> Entro a SolicitudServiceImpl#policyIssued()", trazalog);
		// Inicializamos la respuesta
		BaseResponseDTO respuesta = new BaseResponseDTO();
		try {
			String numeroPropuesta = String.valueOf(policyIssuedRequestDTO.getNumeroPropuesta());
			Solicitud solicitud = solicitudRepository.findByNumeroCotizacion(numeroPropuesta);

			/* DESARROLLO - GOLPEAR AL API EVALUACION */
			/* Guardando datos nativos */
			String estadoGeneral;
			String fechaModific = DateUtil.dateToString(new Date(), DateUtil.FORMATO_DIA_DDMMYYYY_M_HHMMSS);
			String fechaEmision = null;
			String procesoEmision = null;

			Integer estadoPropuesta = solicitud.getEstadoPropuesta();
			String numeroPoliza = solicitud.getNumeroPoliza();
			String estadoPubSubSend = solicitud.getEstadoPubSubSend();

			String flagEstadoPropuesta = (solicitud.getEstadoPropuesta() == 1 ? "1" : "2");
			LOGGER.info("{} EMISION-SOLICITUD ==> Tarjeta afiliacion --> {}", trazalog,
					solicitud.getTarjetaAfiliacion());
			SolicitudFiltrado solicitudFiltrado = solicitudFiltradoRepository
					.findByNumeroCotizacion(solicitud.getNumeroPropuesta());

			if (solicitud.getEstado().equals(Constantes.CODIGO_SOLICITUD_PAGO_SAMP)) {
				ObtenerMensajeEmisionesResponse globalArchivoResponse = globalRestClient.validarArchivos(
						solicitudFiltrado.getTipoDocumento(), solicitudFiltrado.getNumeroDocumento(),
						solicitud.getNumeroPropuesta());
				timeEnd = System.nanoTime();
				LOGGER.info("{} diferencia globalRestClient.validarArchivos: {}", trazalog, (timeEnd - timeStart));
				timeStart = System.nanoTime();
				if (flagEstadoPropuesta.equals("1")) {

					String docAdn = globalArchivoResponse.getAdn();
					String docEdn = globalArchivoResponse.getEdn();
					String docSolicitud = globalArchivoResponse.getSolicitud();
					String docCotizacion = globalArchivoResponse.getCotizacion();
					String docAcp = globalArchivoResponse.getAcp();
					estadoGeneral = "5";
					SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.FORMATO_DIA_DDMMYYYY_M_HHMMSS);
					if (isExistFilesCompleted(globalArchivoResponse)) {
						LOGGER.info("{} LINK-PAGO ==> Validacion Documentos completadoss", trazalog);
						procesoEmision = "1";
						fechaEmision = DateUtil.dateToString(new Date(), DateUtil.FORMATO_DIA_DDMMYYYY_M_HHMMSS);

						Map<String, String> logPolicyIssued = new HashMap<>();
						logPolicyIssued.put("numeroPoliza", numeroPoliza);
						logPolicyIssued.put("estadoPropuesta", estadoPropuesta.toString());
						logPolicyIssued.put("estadoGeneral", estadoGeneral);
						logPolicyIssued.put("fechaModific", fechaModific);
						logPolicyIssued.put("fechaEmision", fechaEmision);
						logPolicyIssued.put("procesoEmision", procesoEmision);
						logPolicyIssued.put("estadoPubSubSend", estadoPubSubSend);
						logPolicyIssued.put("solicitud", solicitud.getIdSolicitud().toString());

						LOGGER.info("{} Update Proceso Emision  {}", trazalog, logPolicyIssued);
						// solicitudRepository.updateSolcitudPolicyIssued(numeroPoliza, estadoPropuesta, estadoGeneral,
						// 		fechaModific, fechaEmision, procesoEmision, estadoPubSubSend,
						// 		solicitud.getIdSolicitud());
						solicitud.setNumeroPoliza(numeroPoliza);
						solicitud.setEstadoPropuesta(estadoPropuesta);
						solicitud.setEstadoGeneral(estadoGeneral);
						Date fechaModificFormat = sdf.parse(fechaModific);
						solicitud.setFechaModif(fechaModificFormat);
						Date fechaEmisionFormat = sdf.parse(fechaEmision);
						solicitud.setFechaEmision(fechaEmisionFormat);
						solicitud.setFlgProcesoEmision(procesoEmision);
						solicitud.setEstadoPubSubSend(estadoPubSubSend);
						solicitudRepository.save(solicitud);
						solicitudRepository.flush();
						LOGGER.info("{} Update Proceso Emision estado_general updated = {} saved = {}", trazalog, estadoGeneral, solicitud.getEstadoGeneral());

						timeStart = System.nanoTime();
						this.emitirPoliza(solicitud.getIdSolicitud());
						timeEnd = System.nanoTime();
						LOGGER.info("{} diferencia emitirPoliza: {}", trazalog, (timeEnd - timeStart));
						timeStart = System.nanoTime();
						this.comunicacionesSME(solicitud.getIdSolicitud());
						timeEnd = System.nanoTime();
						LOGGER.info("{} diferencia comunicacionesSME: {}", trazalog, (timeEnd - timeStart));

					} else {
						procesoEmision = "0";
						LOGGER.info("{} Reprocesamiento emision Entrando a reprocesar documentos estado_general actual {}", trazalog, solicitud.getEstadoGeneral());
						fechaEmision = DateUtil.dateToString(new Date(), DateUtil.FORMATO_DIA_DDMMYYYY_M_HHMMSS);
						// solicitudRepository.updateSolcitudPolicyIssued(numeroPoliza, estadoPropuesta, estadoGeneral,
						// 		fechaModific, fechaEmision, procesoEmision, estadoPubSubSend,
						// 		solicitud.getIdSolicitud());
						solicitud.setNumeroPoliza(numeroPoliza);
						solicitud.setEstadoPropuesta(estadoPropuesta);
						solicitud.setEstadoGeneral(estadoGeneral);
						Date fechaModificFormat = sdf.parse(fechaModific);
						solicitud.setFechaModif(fechaModificFormat);
						Date fechaEmisionFormat = sdf.parse(fechaEmision);
						solicitud.setFechaEmision(fechaEmisionFormat);
						solicitud.setFlgProcesoEmision(procesoEmision);
						solicitud.setEstadoPubSubSend(estadoPubSubSend);
						solicitudRepository.save(solicitud);
						solicitudRepository.flush();
						LOGGER.info("{} Reprocesamiento emision Entrando a reprocesar documentos estado_general updated = {} saved = {}", trazalog, estadoGeneral, solicitud.getEstadoGeneral());
						this.reprocesarEmisionAutomatica(solicitud, docAdn, docEdn, docSolicitud, docCotizacion,
								docAcp);
								
					}
					respuesta.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
					respuesta.setMensajeRespuesta(
							Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_EXITO));
				} else {
					this.validarDocumentos(solicitud, globalArchivoResponse);
					String codigoRespuesta = this.envioCorreoEmision(solicitud);
					LOGGER.info(codigoRespuesta);
					enviarCorreoEmisionCliente(solicitud, solicitudFiltrado);
					respuesta.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
					respuesta.setMensajeRespuesta(
							Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_NO_EMISION));
				}
			} else {
				respuesta.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
				respuesta.setMensajeRespuesta(
						Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_EMISION_ERROR));
			}

		} catch (Exception e) {
			respuesta.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			respuesta.setMensajeRespuesta(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_ERR0R));
			LOGGER.error("{} Error en policyIssued {}", trazalog, e.getMessage());
		}
		timeEnd = System.nanoTime();
		LOGGER.info("{} Diference policyIssued: {}", trazalog, (timeEnd - timeStart));
		return respuesta;
	}

	/**
	 * Enviar correo de emision al asegurado o al contratante
	 *
	 * @param solicitud
	 */
	private void enviarCorreoEmisionCliente(Solicitud solicitud, SolicitudFiltrado persona) {
		EnvioCorreoClienteEmisionRequest envioCorreoClienteEmisionRequest = new EnvioCorreoClienteEmisionRequest();
		envioCorreoClienteEmisionRequest.setFlagCliente(true);
		envioCorreoClienteEmisionRequest.setNroSolicitud(solicitud.getNumeroCotizacion());

		String nombreAsegurado = persona.getNombres();
		String correoAsegurado = persona.getCorreo();
		if (!Constantes.SOLICITUD_ASEGURADO_IGUAL_CONTRATANTE_TRUE.equals(solicitud.getAseguradoIgualContratante())) {
			nombreAsegurado = solicitud.getPersonaByIdContratante().getNombres();
			correoAsegurado = solicitud.getPersonaByIdContratante().getCorreo();

			if (solicitud.getPersonaByIdContratante().getTipoDocumento().equals("11")) {
				nombreAsegurado = solicitud.getPersonaByIdContratante().getNombres().equals("")
						? solicitud.getPersonaByIdContratante().getRazonSocial()
						: solicitud.getPersonaByIdContratante().getNombres();
			}
		}

		envioCorreoClienteEmisionRequest.setNombre(nombreAsegurado);
		envioCorreoClienteEmisionRequest.setCorreo(correoAsegurado);
		EnvioCorreoClienteEmisionResponse responseCorreoGlobal = globalRestClient
				.envioCorreoCliente(envioCorreoClienteEmisionRequest);
		LOGGER.info("{} respuesta enviacorreo", responseCorreoGlobal.getResults());
	}

	/**
	 * Valida si todos los documentos estan completados.
	 *
	 * @param globalArchivoResponse
	 * @return
	 */
	private boolean isExistFilesCompleted(ObtenerMensajeEmisionesResponse globalArchivoResponse) {
		List<String> globalList = Arrays.asList(globalArchivoResponse.getAdn(), globalArchivoResponse.getEdn(),
				globalArchivoResponse.getSolicitud(), globalArchivoResponse.getCotizacion(),
				globalArchivoResponse.getAcp());
		return globalList.stream().allMatch(x -> StringUtils.equalsIgnoreCase(x, "completado"));
	}

	@Async("processExecutorComunicaciones")
	private void comunicacionesSME(Long idSolicitud) {
		GestiorDocumentoRequest gestiorDocumentoRequest = new GestiorDocumentoRequest();
		gestiorDocumentoRequest.setIdSolicitud(idSolicitud);
		try {
			GestiorDocumentoResponse responseGestorDocumentoGlobal = globalRestClient
					.gestorDocumentos(gestiorDocumentoRequest);
			if (responseGestorDocumentoGlobal.getData() == null) {
				throw new NullPointerException("responseGestorDocumento NULL");
			}
			LOGGER.info("----------------GestorDocumentos---------------");
			LOGGER.info(gson.toJson(responseGestorDocumentoGlobal));

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String fecha = format.format(new Date());
			DocumentoCorreoRequest documentoCorreoRequest = new DocumentoCorreoRequest();
			List<ListDocumentoCorreoRequest> listData = new ArrayList<>();
			documentoCorreoRequest.setFechaEmision(fecha);
			documentoCorreoRequest.setIdSolicitud(idSolicitud);

			responseGestorDocumentoGlobal.getData().forEach(data -> {
				ListDocumentoCorreoRequest adjunto = new ListDocumentoCorreoRequest();
				LOGGER.info("data {}", data.getDocumentos());
				adjunto.setName(data.getDocumentos());
				listData.add(adjunto);
			});
			documentoCorreoRequest.setDocumentos(listData);
			LOGGER.info(gson.toJson(documentoCorreoRequest));

			DocumentoCorreoResponse responseDocumentoCorreoGlobal = globalRestClient
					.documentoCorreo(documentoCorreoRequest);
			LOGGER.info("----------------DocumentoCorreo---------------");
			LOGGER.info(gson.toJson(responseDocumentoCorreoGlobal));

		} catch (Exception e) {
			LOGGER.info("EMISION-SOLICITUD ==> ERROR - documentoCorreo ");
			LOGGER.info("Error {}", e.getMessage());
		}
	}

	@Async("processExecutorEmision")
	private void emitirPoliza(Long idSolicitud) {

		EmisionAutomaticaRequest emisionAutomaticaRequest = new EmisionAutomaticaRequest();
		emisionAutomaticaRequest.setIdSolicitud(idSolicitud);
		LOGGER.info("processExecutorEmision <----- {}", gson.toJson(emisionAutomaticaRequest));

		try {
			EmisionAutomaticaResponse responseGlobal = globalRestClient.emisionAutomatica(emisionAutomaticaRequest);
			LOGGER.info("----------------ASL---------------");
			if (responseGlobal == null) {
				throw new NullPointerException("Emision automatica NULL");
			}

			LOGGER.info("Response emision automatica {}", gson.toJson(responseGlobal));
		} catch (Exception e) {
			LOGGER.error("EMISION-SOLICITUD ==> ERROR - emisionAutomatica ");
			LOGGER.error("Error {}", e.getMessage());
		}

	}

	private BaseResponseDTO reprocesarPdfAdn(Map<String, Object> dataConsentimiento) {
		BaseResponseDTO response = new BaseResponseDTO();

		String filename = new StringBuilder(smeGenerarPDFPrefijo)
				.append(dataConsentimiento.get("gls_num_identificacion").toString()).append(".pdf").toString();

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

	private void cargarArchivos(Solicitud solicitud, String docAdn, String docEdn, String docSolicitud,
			String docCotizacion, String docAcp) {
		String traza = Utilitarios.trazaLog() + "-" + solicitud.getNumeroCotizacion();
		SolicitudProducto producto = solicitudProductoRepository.findByIdSolicitud(solicitud.getIdSolicitud());
		Persona personaAsegurado = personaRepository.findById(solicitud.getIdAsegurado()).get();
		LOGGER.info("Reprocesamiento emision {} ==> cargando archivos()", solicitud.getNumeroCotizacion());
		String estateDocGenerate = "completado";
		// CARGAR CONSENTMIENTO
		if (!StringUtils.equals(estateDocGenerate, docAdn)) {
			/* Documento Consentimiento */
			generatePdfAdn(solicitud, traza, personaAsegurado);
		}

		String tipoProducto = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TIPO_PRODUCTO,
				producto.getTipoProducto(), Constantes.MULTITABLA_COLUMNA_VALOR_AUXILIAR);

		/* EDN */
		if (!StringUtils.equals(estateDocGenerate, docEdn)) {
			LOGGER.info("Reprocesamiento emision " + solicitud.getNumeroCotizacion() + " ==> Documento EDN");
			CotizacionCrmRequestDTO cotizacionCrmRequestDTO = new CotizacionCrmRequestDTO();
			cotizacionCrmRequestDTO.setNombreProducto(tipoProducto);
			cotizacionCrmRequestDTO.setNumeroCotizacion(solicitud.getNumeroCotizacion());
			cotizacionCrmRequestDTO.setNumeroDocumento(personaAsegurado.getNumeroDocumento());
			cotizacionCrmRequestDTO
					.setTipoDocumento((personaAsegurado.getTipoDocumento().equals("1")) ? "DocumentoIdentidad" : "CE");
			cotizacionCrmRequestDTO.setUsuarioLogin(solicitud.getUsuarioModif());
			generarPdfEdn(cotizacionCrmRequestDTO, solicitud);
		}

		/* Documento Solicitud */
		if (!StringUtils.equals(estateDocGenerate, docSolicitud)) {
			LOGGER.info("{} Reprocesamiento emision  ==> Documento Soliciotud", traza);
			BaseResponseDTO responsePDF = solicitudPDFService.generarSolicitudPDF(solicitud.getIdSolicitud(),
					tipoProducto);
			LOGGER.info("{} Reprocesamiento emision responsePDF {}", traza, gson.toJson(responsePDF));
		}

		/* Documento Cotizacion */
		if (!StringUtils.equals(estateDocGenerate, docCotizacion)) {
			generatePdfCotizacion(solicitud, traza, personaAsegurado, tipoProducto);
		}

		/* Documento ACP */
		if (!StringUtils.equals(estateDocGenerate, docAcp)) {
			LOGGER.info("{} Reprocesamiento emision ==> Documento ACP", traza);
			BaseResponseDTO responsePDF = acpPDFService.generarPDF(solicitud.getNumeroCotizacion(), 0);
			LOGGER.info("{} response {}", traza, gson.toJson(responsePDF));
		}


	}

	private void generatePdfCotizacion(Solicitud solicitud, String traza, Persona personaAsegurado,
			String tipoProducto) {
		try {
			LOGGER.info("{} Reprocesamiento emision  ==> Documento cotizacion", traza);
			SimpleDateFormat formatterTimeStamp = new SimpleDateFormat("yyyyMMddHHmmss");
			Date timestamp = new Date(System.currentTimeMillis());

			String url = "";

			String cotizacionFileName = "".concat("cotizacion-").concat(solicitud.getNumeroCotizacion()).concat("-")
					.concat(formatterTimeStamp.format(timestamp)).concat(".pdf");

			String cotizacionFilePrivada = "";
			String cotizacionFilePublica = "";

			LOGGER.info("{} enviarCorreo.cotizacionFilePrivada: {}", traza, cotizacionFilePrivada);

			File tmpFileCotizacion = new File(cotizacionFileName);

			if ("VidaFree".equals(tipoProducto)) {
				LOGGER.info(solicitud.getNumeroCotizacion());
				url = urlCotizacionVidaFreePDF.concat(solicitud.getNumeroCotizacion());

				LOGGER.info("{} COPIADO COTIZACION", traza);

				tmpFileCotizacion = generarPDFCotizacionVidaFree(solicitud.getNumeroPropuesta());
				cotizacionFilePrivada = "" + cotizacionFileName;
				cotizacionFilePublica = "" + cotizacionFileName;
				LOGGER.info("{}", cotizacionFilePrivada);
				LOGGER.info("{}", cotizacionFilePublica);

			} else {

				cotizacionFilePrivada = "" + cotizacionFileName;
				LOGGER.info(url);

				LOGGER.info("{} enviarCorreo.url: {}", traza, url);
				LOGGER.info("{} COPIANDO A TEMPORALES ", traza);
				LOGGER.info("{} url: {}", traza, url);
				LOGGER.info("{} propuesta: ", traza);
				LOGGER.info("{} cotizacionFilePrivada: {}", traza, cotizacionFilePrivada);
				LOGGER.info("{} **************", traza);

				tmpFileCotizacion = generarPDFCotizacionVida(solicitud.getNumeroPropuesta(),
						solicitud.getAgenteNombres(), personaAsegurado.getNombres());

			}
			String nombreArchivoSolicitud = "Cotizacion_" + solicitud.getNumeroCotizacion() + "_"
					+ formatterTimeStamp.format(timestamp) + ".pdf";
		
			String tipoDocumentoCRM = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_TIPO_DOCUMENTO,
					personaAsegurado.getTipoDocumento(), Constantes.MULTITABLA_COLUMNA_VALOR_CRM);
			String numeroDocumento = personaAsegurado.getNumeroDocumento();
			String tipoDocumento = personaAsegurado.getTipoDocumento();

			LOGGER.info("{} Antes de enviarPDFCRM2 -----", traza);
			this.enviarPDFCRM2(tmpFileCotizacion, tipoDocumentoCRM, numeroDocumento, nombreArchivoSolicitud,
					nombreArchivoSolicitud, tipoDocumento);
			eliminarFilePrivado(String.valueOf(tmpFileCotizacion));
		} catch (Exception e) {
			LOGGER.error("{} No se pudo enviar PDF Cotizacion a CRM. {}", traza, e.getMessage(), e);
		}
	}

	/**
	 * Generar documento adn si es que no existe
	 * 
	 * @param solicitud
	 * @param traza
	 * @param personaAsegurado
	 */
	private void generatePdfAdn(Solicitud solicitud, String traza, Persona personaAsegurado) {
		try {
			LOGGER.info("{} Reprocesamiento emision ==> Documento consentimiento", traza);
			String tipoDocumentoInput = personaAsegurado.getTipoDocumento();
			List<Multitabla> m1 = multitablaRepository
					.findByCodigoTabla(TablaEnum.TABLA_TIPO_DOCUMENTO.getCodigoTabla());
			Multitabla tiposDocumentoCRM = null;
			if (!m1.isEmpty()) {

				tiposDocumentoCRM = m1.stream().filter(t -> t.getCodigo().equals(tipoDocumentoInput))
						.collect(Collectors.toList()).get(0);
			}
			Map<String, Object> consentimientoData = this.getDataConsentimiento(solicitud.getUsuarioModif(),
					personaAsegurado.getNumeroDocumento(), Integer.valueOf(personaAsegurado.getTipoDocumento()));
			consentimientoData.put("usuario", solicitud.getUsuarioModif());
			LOGGER.info("{} registros consentimientoData: {}", traza, gson.toJson(consentimientoData));

			String numeroDocumento = personaAsegurado.getNumeroDocumento();
			String urlPDFADNCloud = System.getProperty("java.io.tmpdir") + File.separator + "adn_" + numeroDocumento
					+ ".pdf";

			// se reprocesa el adn si no existe
			File fileAdn = new File(urlPDFADNCloud);
			boolean exists = fileAdn.exists();
			LOGGER.info("{} UrlPDFADNCloud: {}", traza, urlPDFADNCloud);
			LOGGER.info("{} FileADN: {}", traza, fileAdn.exists());
			if (!exists) {
				this.reprocesarPdfAdn(consentimientoData);
				fileAdn = new File(urlPDFADNCloud);
			}

			String tipoDocumentoCRM = tiposDocumentoCRM.getValorCrm();
			String nombreArchivoPDF = "adn_" + numeroDocumento + ".pdf";
			String descripcionArchivo = "";
			personaAsegurado.setDocumentoAdn(nombreArchivoPDF);
			personaRepository.save(personaAsegurado);
			this.enviarPDFCRM2(fileAdn, tipoDocumentoCRM, numeroDocumento, nombreArchivoPDF, descripcionArchivo,
					tipoDocumentoInput);

			eliminarFilePrivado(String.valueOf(fileAdn));
			eliminarFilePrivado(urlPDFADNCloud);

		} catch (Exception e) {
			LOGGER.info("No se pudo enviar PDF Consentimiento a CRM.");

		}
	}

	@Override
	public BaseResponseDTO validadorDocumentos(PolicyIssuedRequestDTO policyIssuedRequestDTO) {
		LOGGER.info("EMISION-SOLICITUD ==> Entro a SolicitudServiceImpl#validarDocumentos()");
		// Inicializamos la respuesta
		BaseResponseDTO respuesta = new BaseResponseDTO();
		String numeroPropuesta = String.valueOf(policyIssuedRequestDTO.getNumeroPropuesta());
		Solicitud solicitud = solicitudRepository.findByNumeroCotizacion(numeroPropuesta);
		try {
			String flagEstadoPropuesta = (solicitud.getEstadoPropuesta().equals(1) ? "1" : "2");
			LOGGER.info("EMISION-SOLICITUD ==> Tarjeta afiliacion --> {}", solicitud.getTarjetaAfiliacion());

			if (solicitud.getEstado().equals(Constantes.CODIGO_SOLICITUD_PAGO_SAMP)) {
				if (flagEstadoPropuesta.equals("1")) {
					SolicitudFiltrado solicitudFiltrado = solicitudFiltradoRepository
							.findByNumeroCotizacion(solicitud.getNumeroPropuesta());

					ObtenerMensajeEmisionesResponse globalArchivoResponse = globalRestClient.validarArchivos(
							solicitudFiltrado.getTipoDocumento(), solicitudFiltrado.getNumeroDocumento(),
							solicitud.getNumeroPropuesta());
					

					if (isExistFilesCompleted(globalArchivoResponse)) {
						LOGGER.info("LINK-PAGO ==> Validacion Documentos completadoss");
						solicitud.setFechaModif(new Date());
						solicitud.setFlgProcesoEmision( "1");	
						solicitudRepository.save(solicitud);
						respuesta.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
						respuesta.setMensajeRespuesta("Validacion Documentos completadoss");

					} else {					
						
						this.validarDocumentos(solicitud, globalArchivoResponse);
						
						try {
							Thread.sleep(1000);
							
							globalArchivoResponse = globalRestClient.validarArchivos(
									solicitudFiltrado.getTipoDocumento(), solicitudFiltrado.getNumeroDocumento(),
									solicitud.getNumeroPropuesta());
							
							if (isExistFilesCompleted(globalArchivoResponse)) {		
								solicitud.setFechaModif(new Date());
								solicitud.setFlgProcesoEmision( "1");	
								solicitudRepository.save(solicitud);
								LOGGER.info("LINK-PAGO ==> Validacion Documentos completadoss");
								respuesta.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
								respuesta.setMensajeRespuesta("Validacion Documentos completadoss");
							} else {
								respuesta.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
								respuesta.setMensajeRespuesta(
										Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_ERR0R));
							}
						} catch (Exception e) {
							respuesta.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
							respuesta.setMensajeRespuesta(
									Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_ERR0R));
						}
						
						respuesta.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
						respuesta.setMensajeRespuesta(
								Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_ERR0R));
					}
				}
			} else {
				respuesta.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
				respuesta.setMensajeRespuesta(
						Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_EMISION_ERROR));
			}

		} catch (Exception e) {
			respuesta.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			respuesta.setMensajeRespuesta(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_ERR0R));
		}
		return respuesta;
	}

	private Map<String, Object> formatFechaConsentimiento(Map<String, Object> dataConsentimiento) {
		Map<String, Object> response = new HashMap<>();
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

	public Map<String, Object> getDataConsentimiento(String idUsuario, String numeroDocumento,
			Integer tipoDocumentoADN) {
		Map<String, Object> response = null;
		TipoDocumentoADN tipoDocumento = TipoDocumentoADN.parse(tipoDocumentoADN);

		if (null == tipoDocumento) {
			response = new HashMap<>();
			response.put("hasConsentimiento", false);
			response.put("acceptedConsentimiento", false);

			return response;
		}

		ConsentimientoUniversalRequest request = new ConsentimientoUniversalRequest();
		request.setTipoDocumento(
				tipoDocumento.equals(TipoDocumentoADN.DNI) ? Constantes.CONSENTIMIENTO_TIPODDOCUMENTO_DNI
						: Constantes.CONSENTIMIENTO_TIPODDOCUMENTO_CE);
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
			response.putAll(this.formatFechaConsentimiento(response));
			return response;
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
			response.putAll(this.formatFechaConsentimiento(response));
			return response;
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
			response.putAll(this.formatFechaConsentimiento(response));
			return response;
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
			response.putAll(this.formatFechaConsentimiento(response));
		}
		response.put("id_tratamiento", 0);
		response.put("hasConsentimientoIntercorp", false);
		response.put("hasConsentimientoUniversal", false);

		return response;
	}

	@Async
	private void validarDocumentos(Solicitud solicitud, ObtenerMensajeEmisionesResponse globalArchivoResponse) {
		try {
			String docAdn = globalArchivoResponse.getAdn();
			String docEdn = globalArchivoResponse.getEdn();
			String docSolicitud = globalArchivoResponse.getSolicitud();
			String docCotizacion = globalArchivoResponse.getCotizacion();
			String docAcp = globalArchivoResponse.getAcp();
			this.cargarArchivos(solicitud, docAdn, docEdn, docSolicitud, docCotizacion, docAcp);
		} catch (Exception ex) {
			LOGGER.info("ERROR eliminarFilePrivado=> {}", ex.getMessage());
		}
	}

	@Async
	public void eliminarFilePrivado(String urlPDFFirmaPrivada) {
		try {
			File file = new File(urlPDFFirmaPrivada);
			Files.deleteIfExists(file.toPath());

		} catch (Exception ex) {
			LOGGER.info("ERROR eliminarFilePrivado=> {}", ex.getMessage());
		}
	}
	
	public String envioCorreoEmision(Solicitud solicitud) {
		String codigoMendaje = null;
		try {			
			String traza = Utilitarios.trazaLog() + "-" + solicitud.getNumeroCotizacion();
			
			Thread.sleep(1000);
			
			Integer estadoPropuesta = solicitud.getEstadoPropuesta();
			String estadoPubSubSend = solicitud.getEstadoPubSubSend();
			String flagEstadoPropuesta = (solicitud.getEstadoPropuesta().equals(1) ? "1" : "2");
			
			SolicitudFiltrado solicitudFiltrado = solicitudFiltradoRepository
					.findByNumeroCotizacion(solicitud.getNumeroPropuesta());
			
			ObtenerMensajeEmisionesResponse globalArchivoResponse = globalRestClient.validarArchivos(
					solicitudFiltrado.getTipoDocumento(), solicitudFiltrado.getNumeroDocumento(),
					solicitud.getNumeroPropuesta());
			
			SolicitudProducto producto = solicitudProductoRepository.findByIdSolicitud(solicitud.getIdSolicitud());
			String nombreProducto = "1".equals(producto.getTipoProducto()) ? "Plan Garantizado" : "Vida Free";
			String nombresAsegurado = Utilitarios.nombresCompletos(solicitudFiltrado.getNombres(),
					solicitudFiltrado.getApellidoPaterno(), solicitudFiltrado.getApellidoMaterno(),
					solicitudFiltrado.getRazonSocial());
			
			if (isExistFilesCompleted(globalArchivoResponse)) {						
				
				LOGGER.info("LINK-PAGO ==> Validacion Documentos completadoss");
				if (flagEstadoPropuesta.equals("1")) {
					solicitud.setFechaModif(new Date());
					solicitud.setFlgProcesoEmision( "1");
					solicitudRepository.save(solicitud);						

					Map<String, String> mapUpdateSolictud = new HashMap<>();
					mapUpdateSolictud.put("numeroPoliza", solicitud.getNumeroPropuesta());
					mapUpdateSolictud.put("estadoPropuesta", estadoPropuesta.toString());
					mapUpdateSolictud.put("estadoPubSubSend", estadoPubSubSend);
					mapUpdateSolictud.put("idSolicitud", solicitud.getIdSolicitud().toString());
					LOGGER.info("Update Proceso Emision  {}", mapUpdateSolictud);

					this.emitirPoliza(solicitud.getIdSolicitud());
					this.comunicacionesSME(solicitud.getIdSolicitud());
				}
				codigoMendaje = Constantes.CODIGO_RESPUESTA_GENERAL_EXITO;
			} else {	
				
				LOGGER.info("{} Reprocesamiento emision   ==> Documentos no encontrados al reprocesar", traza);
				String rutaPlantilla = "";
				String rutaPlantillaLinux = "";
				if (solicitud.getEstadoPropuesta().equals(1)) {
					solicitudRepository.updateSolcitudReprocesoEision("0", solicitud.getIdSolicitud());
					rutaPlantilla = Constantes.RUTA_PLANTILLA + "/" + Constantes.PLANTILLA_REPROCESO_EMISION;
					rutaPlantillaLinux = rutaPlantillaDocLinux + "//" + Constantes.PLANTILLA_REPROCESO_EMISION;
				} else {
					rutaPlantilla = Constantes.RUTA_PLANTILLA + "/" + Constantes.PLANTILLA_REPROCESO_SUSCRIPCION;
					rutaPlantillaLinux = rutaPlantillaDocLinux + "//" + Constantes.PLANTILLA_REPROCESO_SUSCRIPCION;
				}
				InputStream stream = new ClassPathResource(rutaPlantilla).getInputStream();

				String systemOp = System.getProperty("os.name");
				if (!systemOp.contains("Windows")) {
					stream = new FileInputStream(new File(rutaPlantillaLinux));
				}

				String bodyHtml = Utilitarios.valorString(StreamUtils.copyToString(stream, Charset.defaultCharset()));

				ObtenerAgenteRequest obtenerAgenteRequest = new ObtenerAgenteRequest();
				obtenerAgenteRequest.setCodAgencia(solicitud.getUsuarioModif().toLowerCase());
				ObtenerAgenteResponse responseGlobal = globalRestClient.obtenerAgente(obtenerAgenteRequest);

				String nombres = responseGlobal.getNOM_AGENTE();
				String bodyAgente = bodyHtml;
				bodyAgente = bodyAgente.replaceAll("\\{nombre\\}", nombres);
				bodyAgente = bodyAgente.replaceAll("\\{cliente\\}", nombresAsegurado);
				bodyAgente = bodyAgente.replaceAll("\\{dni\\}", solicitudFiltrado.getNumeroDocumento());
				bodyAgente = bodyAgente.replaceAll("\\{propuesta\\}", solicitud.getNumeroCotizacion());
				bodyAgente = bodyAgente.replaceAll("\\{producto\\}", nombreProducto);

				enviarCorreoConAdjunto(this.enviarCorreoEmisionAsunto, responseGlobal.getGLS_CORREO_AGENTE(), bodyAgente, null, traza, "EMISION",solicitudFiltrado.getNumeroDocumento(),solicitud.getNumeroCotizacion());

				nombres = responseGlobal.getNOM_AGENTE();
				String bodySupervisor = bodyHtml;
				bodySupervisor = bodySupervisor.replaceAll("\\{nombre\\}", nombres);
				bodySupervisor = bodySupervisor.replaceAll("\\{cliente\\}", nombresAsegurado);
				bodySupervisor = bodySupervisor.replaceAll("\\{dni\\}", solicitudFiltrado.getNumeroDocumento());
				bodySupervisor = bodySupervisor.replaceAll("\\{propuesta\\}", solicitud.getNumeroCotizacion());
				bodySupervisor = bodySupervisor.replaceAll("\\{producto\\}", nombreProducto);

				enviarCorreoConAdjunto(this.enviarCorreoEmisionAsunto, responseGlobal.getGLS_CORREO_SUPERVIS(), bodySupervisor, null, traza,"EMISION",solicitud.getNumeroCotizacion(),solicitudFiltrado.getNumeroDocumento());
				
				codigoMendaje = Constantes.CODIGO_RESPUESTA_GENERAL_ERROR;
				
			}
			
			
		} catch (Exception ex) {
			LOGGER.info("ERROR envioCorreoEmision=> {}", ex.getMessage());
		}
		return codigoMendaje;
	}

	@Async("processExecutorReproceso")
	public void reprocesarEmisionAutomatica(Solicitud solicitud, String docAdn, String docEdn, String docSolicitud,
			String docCotizacion, String docAcp) {
		try {
			LOGGER.info("Reprocesamiento emision {} ==> INICIO", solicitud.getNumeroCotizacion());
			this.cargarArchivos(solicitud, docAdn, docEdn, docSolicitud, docCotizacion, docAcp);
			solicitud = solicitudRepository.findById(solicitud.getIdSolicitud()).get();
			String codigoRespuesta = this.envioCorreoEmision(solicitud);
			LOGGER.info(codigoRespuesta);
			LOGGER.info("Reprocesamiento emision {} ==> FIN", solicitud.getNumeroCotizacion());
		} catch (Exception e) {
			LOGGER.error("ERROR {}", e.getMessage(), e);
		}
	}

	@Override
	public BaseResponseDTO reprocesarEmisionAutomaticaDocumentos(PolicyIssuedRequestDTO policyIssuedRequestDTO) {
		LOGGER.info("EMISION-SOLICITUD ==> Entro a SolicitudServiceImpl#reprocesarEmisionAutomatica()");
		// Inicializamos la respuesta
		
		BaseResponseDTO respuesta = new BaseResponseDTO();
		String numeroPropuesta = String.valueOf(policyIssuedRequestDTO.getNumeroPropuesta());
		Solicitud solicitud = solicitudRepository.findByNumeroCotizacion(numeroPropuesta);
		String traza = Utilitarios.trazaLog() + "-" + solicitud.getNumeroCotizacion();
		try {
			/* DESARROLLO - GOLPEAR AL API EVALUACION */

			// HistoricoEmisiones emisiones =
			/* Guardando datos nativos */

			//String fechaModific = DateUtil.dateToString(new Date(), DateUtil.FORMATO_DIA_DDMMYYYY_M_HHMMSS);
			//String fechaEmision = null;
			// String procesoEmision = solicitud.getFlgProcesoEmision();

			Integer estadoPropuesta = solicitud.getEstadoPropuesta();
			String numeroPoliza = solicitud.getNumeroPoliza();
			String estadoPubSubSend = solicitud.getEstadoPubSubSend();
			String mensajeRpta = "";

			String flagEstadoPropuesta = (solicitud.getEstadoPropuesta().equals(1) ? "1" : "2");
			LOGGER.info("EMISION-SOLICITUD ==> Tarjeta afiliacion --> {}", solicitud.getTarjetaAfiliacion());

			if (solicitud.getEstado().equals(Constantes.CODIGO_SOLICITUD_PAGO_SAMP)) {
				//String estadoGeneral = "5";
				if (flagEstadoPropuesta.equals("1")) {
					SolicitudFiltrado solicitudFiltrado = solicitudFiltradoRepository
							.findByNumeroCotizacion(solicitud.getNumeroPropuesta());

					ObtenerMensajeEmisionesResponse globalArchivoResponse = globalRestClient.validarArchivos(
							solicitudFiltrado.getTipoDocumento(), solicitudFiltrado.getNumeroDocumento(),
							solicitud.getNumeroPropuesta());					
					
					if (isExistFilesCompleted(globalArchivoResponse)) {						
						
						LOGGER.info("LINK-PAGO ==> Validacion Documentos completadoss");
						
						solicitud.setFechaModif(new Date());
						solicitud.setFlgProcesoEmision( "1");	
						solicitudRepository.save(solicitud);
						
						Map<String, String> mapUpdateSolictud = new HashMap<>();
						mapUpdateSolictud.put("numeroPoliza", numeroPoliza);
						mapUpdateSolictud.put("estadoPropuesta", estadoPropuesta.toString());
						mapUpdateSolictud.put("estadoPubSubSend", estadoPubSubSend);
						mapUpdateSolictud.put("idSolicitud", solicitud.getIdSolicitud().toString());
						LOGGER.info("Update Proceso Emision  {}", mapUpdateSolictud);

						this.emitirPoliza(solicitud.getIdSolicitud());
						this.comunicacionesSME(solicitud.getIdSolicitud());

						respuesta.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
						respuesta.setMensajeRespuesta(mensajeRpta);

					} else {
						
						this.validarDocumentos(solicitud, globalArchivoResponse);
						
						String codigoRespuesta = this.envioCorreoEmision(solicitud);
						respuesta.setCodigoRespuesta(codigoRespuesta);
						respuesta.setMensajeRespuesta( codigoRespuesta.equals(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR)? Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_ERR0R)
								:Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_EXITO));
					}
				}
			} else {
				respuesta.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
				respuesta.setMensajeRespuesta(
						Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_EMISION_ERROR));
			}

		} catch (Exception e) {
			respuesta.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			respuesta.setMensajeRespuesta(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_ERR0R));
		}
		return respuesta;
	}

	@Override
	public BaseResponseDTO reprocesarDocumentos(PolicyIssuedRequestDTO policyIssuedRequestDTO) {
		String trazaLog = Utilitarios.trazaLog() + "-" + policyIssuedRequestDTO.getNumeroPropuesta();
		LOGGER.info("{} EMISION-SOLICITUD ==> Entro a SolicitudServiceImpl#reprocesarEmisionAutomatica()", trazaLog);
		// Inicializamos la respuesta
		BaseResponseDTO respuesta = new BaseResponseDTO();
		String numeroPropuesta = String.valueOf(policyIssuedRequestDTO.getNumeroPropuesta());
		Solicitud solicitud = solicitudRepository.findByNumeroCotizacion(numeroPropuesta);

		try {
			LOGGER.info("{} EMISION-SOLICITUD ==> Tarjeta afiliacion --> {}", trazaLog,
					solicitud.getTarjetaAfiliacion());
			SolicitudFiltrado solicitudFiltrado = solicitudFiltradoRepository
					.findByNumeroCotizacion(solicitud.getNumeroPropuesta());

			ObtenerMensajeEmisionesResponse globalArchivoResponse = globalRestClient.validarArchivos(
					solicitudFiltrado.getTipoDocumento(), solicitudFiltrado.getNumeroDocumento(),
					solicitud.getNumeroPropuesta());
			//this.validarDocumentos(solicitud, globalArchivoResponse);

			if (isExistFilesCompleted(globalArchivoResponse)) {
				
				this.validarDocumentos(solicitud, globalArchivoResponse);
				String codigoRespuesta = this.envioCorreoEmision(solicitud);
				respuesta.setCodigoRespuesta(codigoRespuesta);
				respuesta.setMensajeRespuesta( codigoRespuesta.equals(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR)? Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_ERR0R)
						:Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_EXITO));
				
				//LOGGER.info("{} LINK-PAGO ==> Validacion Documentos completadoss", trazaLog);
			} else {
				respuesta.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
				respuesta.setMensajeRespuesta(
						Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_ERR0R));
			}

		} catch (Exception e) {
			respuesta.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			respuesta.setMensajeRespuesta(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_ERR0R));
		}
		return respuesta;
	}

	public static int calcularEdadActurial(String fechaNacimientoStr) {
		// Validar la fecha de nacimiento
		if (!esFechaValida(fechaNacimientoStr)) {
			return 0;
		}

		// Convertir la fecha de nacimiento a LocalDate
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate fechaNacimiento = LocalDate.parse(fechaNacimientoStr, formatter);
		LocalDate fechaActual = LocalDate.now();

		// Calcular la edad
		int edad = Period.between(fechaNacimiento, fechaActual).getYears();

		// Calcular si se debe incrementar la edad
		LocalDate proximoCumpleanios = fechaNacimiento.plusYears(edad + 1);
		int mesesHastaProximoCumple = Period.between(fechaActual, proximoCumpleanios).getMonths();
		int diasHastaProximoCumple = Period.between(fechaActual, proximoCumpleanios).getDays();

		if (mesesHastaProximoCumple < 6 || (mesesHastaProximoCumple == 6 && diasHastaProximoCumple <= 1)) {
			return edad;
		} else {
			return edad + 1;
		}
	}
	private static boolean esFechaValida(String fecha) {
		Pattern pattern = Pattern.compile("^\\d{2}/\\d{2}/\\d{4}$");
		Matcher matcher = pattern.matcher(fecha);
		return matcher.matches();
	}

}