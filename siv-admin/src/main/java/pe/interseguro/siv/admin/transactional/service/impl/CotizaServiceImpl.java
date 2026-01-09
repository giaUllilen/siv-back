package pe.interseguro.siv.admin.transactional.service.impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.itextpdf.html2pdf.HtmlConverter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import okhttp3.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import pe.interseguro.siv.admin.transactional.factory.ServiceFactory;
import pe.interseguro.siv.admin.transactional.service.CotizaService;
import pe.interseguro.siv.common.bean.*;
import pe.interseguro.siv.common.dto.request.*;
import pe.interseguro.siv.common.dto.response.*;
import pe.interseguro.siv.common.exception.ErrorResourceDTO;
import pe.interseguro.siv.common.persistence.db.acsele.repository.AcseleRepository;
import pe.interseguro.siv.common.persistence.db.mysql.domain.*;
import pe.interseguro.siv.common.persistence.db.mysql.repository.*;
import pe.interseguro.siv.common.persistence.db.postgres.bean.*;
import pe.interseguro.siv.common.persistence.db.postgres.repository.AsegurableRepository;
import pe.interseguro.siv.common.persistence.db.postgres.repository.CotizacionRepository;
import pe.interseguro.siv.common.persistence.rest.acsele.AcseleRestClient;
import pe.interseguro.siv.common.persistence.rest.acsele.response.TerceroResponse;
import pe.interseguro.siv.common.persistence.rest.bupo.BupoRestClient;
import pe.interseguro.siv.common.persistence.rest.cotizador.CotizadorRestClient;
import pe.interseguro.siv.common.persistence.rest.cotizador.response.AsegurableResponse;
import pe.interseguro.siv.common.persistence.rest.cotizador.response.ObtenerCumuloResponse;
import pe.interseguro.siv.common.persistence.rest.crm.CrmRestClient;
import pe.interseguro.siv.common.persistence.rest.crm.request.*;
import pe.interseguro.siv.common.persistence.rest.crm.response.CrmCotizacionResponse;
import pe.interseguro.siv.common.persistence.rest.estudionecesidad.request.GenerarPdfCabeceras;
import pe.interseguro.siv.common.persistence.rest.estudionecesidad.request.GenerarPdfPreguntas;
import pe.interseguro.siv.common.persistence.rest.estudionecesidad.request.GenerarPdfRequest;
import pe.interseguro.siv.common.persistence.rest.estudionecesidad.response.GenerarPdfResponse;
import pe.interseguro.siv.common.persistence.rest.global.GlobalRestClient;
import pe.interseguro.siv.common.persistence.rest.global.request.UploadArchivoRequest;
import pe.interseguro.siv.common.persistence.rest.global.request.*;
import pe.interseguro.siv.common.persistence.rest.global.response.*;
import pe.interseguro.siv.common.persistence.rest.interseguro.InterseguroRestClient;
import pe.interseguro.siv.common.persistence.rest.interseguro.dto.Adjuntos;
import pe.interseguro.siv.common.persistence.rest.interseguro.dto.Remitente;
import pe.interseguro.siv.common.persistence.rest.interseguro.request.EnviarCorreoRequestNuevo;
import pe.interseguro.siv.common.persistence.rest.plaft.PlaftRestClient;
import pe.interseguro.siv.common.persistence.rest.vidafree.VidaFreeRestClient;
import pe.interseguro.siv.common.persistence.rest.vidafree.request.ConsultaCotizacionDetalleRequest;
import pe.interseguro.siv.common.persistence.rest.vidafree.response.CoberturaResponse;
import pe.interseguro.siv.common.persistence.rest.vidafree.response.ConsultaCotizacionDetalleResponse;
import pe.interseguro.siv.common.persistence.rest.vidafree.response.GenericoResponse;
import pe.interseguro.siv.common.util.Constantes;
import pe.interseguro.siv.common.util.ConstantesSolicitudPDF;
import pe.interseguro.siv.common.util.DateUtil;
import pe.interseguro.siv.common.util.Utilitarios;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.DoubleStream;

import static pe.interseguro.siv.common.util.Constantes.*;

@Service("cotizaService")
public class CotizaServiceImpl implements CotizaService {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	private static final String KEY_ENCRYPT = Constantes.KEY_ENCRYPT;

	private Gson gson = new Gson();
	@Autowired
	ServiceFactory serviceFactory;
	@Value("#{ environment['ruta.temporal.privada'] }")
	private String rutaPrivadaTemporal;

	@Value("#{ environment['persistence.rest.client.crm.cotizadorVidaIntermedio.servidor'] }")
	private String urlCotizadorVida;

	@Value("#{ environment['persistence.rest.client.cotizador.crm.servidor'] }")
	private String urlCotizadorVidaWeb;

	@Value("#{ environment['persistence.rest.client.cotizador.crm.nuevo'] }")
	private String serviceNuevoCotizador;

	@Value("#{ environment['persistence.rest.client.interseguro.cotizadorfront.url'] }")
	private String serviceUrlBaseCotizador;

	@Value("#{ environment['persistence.rest.client.crm.cotizador.recotizar'] }")
	private String serviceRecotizar;

	@Value("#{ environment['persistence.rest.client.cotizador.crm.export'] }")
	private String serviceReporteVida;

	@Value("#{ environment['url.cotizacion.pdf'] }")
	private String urlCotizacionPDF;

	@Value("#{ environment['url.cotizacion.vidafree.pdf'] }")
	private String urlCotizacionVidaFreePDF;

	@Value("#{ environment['url.adn.pdf'] }")
	private String urlADNPDF;

	@Value("#{ environment['url.cotizacion.vidafree'] }")
	private String urlCotizacionVidaFree;

	@Value("#{ environment['ruta.plantilla.doc.linux'] }")
	private String rutaPlantillaDocLinux;

	@Value("#{ environment['ruta.pdf.privada'] }")
	private String rutaPDFPrivada;

	@Value("#{ environment['ruta.pdf.publica'] }")
	private String rutaPDFPublica;

	@Value("#{ environment['persistence.rest.client.interseguro.correo.enviarCorreo.cotizador.remitente'] }")
	private String enviarCorreoCotizadorRemitente;

	@Value("#{ environment['persistence.rest.client.interseguro.correo.enviarCorreo.cotizador.remitente.display'] }")
	private String enviarCorreoCotizadorRemitenteDisplay;

	@Value("#{ environment['persistence.rest.client.interseguro.correo.enviarCorreo.cotizador.asunto'] }")
	private String enviarCorreoCotizadorAsunto;

	@Value("#{ environment['persistence.rest.client.interseguro.correo.enviarCorreo.solicitud.email.pruebas'] }")
	private String enviarCorreoSolicitudEmailPruebas;

	@Value("#{ environment['server.port'] }")
	private String serverPort;

	@Value("#{ environment['flag_api_cumulo'] }")
	private String flagCumulo;

	@Autowired
	private ServicioEdnRepository servicioEdnRepository;

	@Autowired
	private SolicitudProductoDetalleRepository solicitudProductoDetalleRepository;

	@Autowired
	private MessageSource messageSource;
	@Autowired
	private CotizacionCorrelativoRepository cotizacionCorrelativoRepository;
	@Autowired
	private MultitablaRepository multitablaRepository;

	@Autowired
	private SolicitudPGRepository solicitudPGRepository;

	@Autowired
	private SolicitudVFRepository solicitudVFRepository;

	@Autowired
	private PersonaRepository personaRepository;

	@Autowired
	private PlanFuturoRepository planFuturoRepository;

	@Autowired
	private CotizacionRepository cotizacionRepository;

	@Autowired
	private SolicitudRepository solicitudRepository;

	@Autowired
	private SolicitudGeneralRepository solicitudGeneralRepository;

	@Autowired
	private SolicitudFiltradoRepository solicitudFiltradoRepository;

	@Autowired
	private BupoRestClient bupoRestClient;

	@Autowired
	private SolicitudBeneficiarioRepository solicitudBeneficiarioRepository;

	@Autowired
	private SolicitudDpsRepositorioRepository solicitudDpsRepositorioRepository;

	@Autowired
	private SolicitudDpsPreguntaRepository solicitudDpsPreguntaRepository;

	@Autowired
	private CrmRestClient crmRestClient;

	@Autowired
	private InterseguroRestClient interseguroRestClient;

	@Autowired
	private AcseleRestClient acseleRestClient;

	@Autowired
	private CotizadorRestClient cotizadorRestClient;

	@Autowired
	private VidaFreeRestClient cotizadorFreeRestClient;

	@Autowired
	private VidaFreeRestClient vidaFreeRestClient;

	@Autowired
	private AdnRepository adnRepository;

	@Autowired
	private AcseleRepository acseleRepository;
	
	@Autowired
	private SolicitudExigenciasMedicassRepository solicitudExigenciasMedicasRepository;

	@Autowired
	private AsegurableRepository asegurableRepository;

	/*
	 * @Autowired private EventLogRepository logRepository;
	 */

	@Autowired
	private ViewSolicitudPlaftRepository viewSolicitudPlaftRepository;

	@Autowired
	private PlaftRestClient plaftRestClient;

	@Autowired
	private GlobalRestClient globalRestClient;

	@Autowired
	private SolicitudProductoRepository solicitudProductoRepository;

	@Value("#{ environment['persistence.rest.client.cloud-storage.header.app'] }")
	private String cloudStorageApp;

	@Value("#{ environment['persistence.rest.client.cloud-storage.header.app.value'] }")
	private String cloudStorageAppValue;

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

	@Value("#{ environment['persistence.rest.client.global.documento.servidor'] }")
	private String servidorApiDocumento;

	@Value("#{ environment['persistence.rest.client.global.estructura.comercial.findByDocumento'] }")
	private String globalFindByDocumento;

	public CotizaServiceImpl() {
		super();
	}

	private String getUrlEsnGeneraPdf() {
		return baseUrl + esnGenerarPdf;
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
		String strData = "";
		byte[] encryptedBytes = Base64.decodeBase64(encrypted);

		try {
			SecretKeySpec skeyspec = new SecretKeySpec(KEY_ENCRYPT.getBytes(), "Blowfish");
			Cipher cipher = Cipher.getInstance("Blowfish");
			cipher.init(Cipher.DECRYPT_MODE, skeyspec);
			byte[] decrypted = cipher.doFinal(encryptedBytes);
			strData = new String(decrypted);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return strData;

	}

	@Override
	public CotizadorCorrelativoResponseDTO generarCorrelativo(String tipoDocumento, String numeroDocumento) {
		CotizadorCorrelativoResponseDTO response = new CotizadorCorrelativoResponseDTO();
		try {
			CotizacionCorrelativo cotizacionCorrelativo = cotizacionCorrelativoRepository.findAll().get(0);
			if (cotizacionCorrelativo != null) {
				int correlativo = cotizacionCorrelativo.getCorrelativo() + 1;
				cotizacionCorrelativo.setCorrelativo(correlativo);
				cotizacionCorrelativoRepository.save(cotizacionCorrelativo);

				response.setCodigoRespuesta("01");
				response.setMensajeRespuesta("OK");
				response.setNumeroCotizacion(String.valueOf(correlativo));
				CotizadorCumuloResponseDTO cumulo = this.obtenerCumulo(tipoDocumento, numeroDocumento);
				response.setCumuloMoneda(Constantes.MONEDA_ACSELE_DOLARES);
				response.setCumuloValor(cumulo.getCumuloDolares().toString());
			} else {
				response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
				response.setMensajeRespuesta("No se pudo generar correlativo de Cotizador Vida");
			}

		} catch (Exception e) {
			LOGGER.error("Problemas al invocar servicio de Cotizador[GenerarCorrelativo]");
			LOGGER.error(e.getMessage());
			response.setCodigoRespuesta("99");
			response.setMensajeRespuesta(e.getMessage());
		}
		return response;
	}

	@Override
	public BaseResponseDTO obtenerURLVidaFree(TokenRequestDTO tokenRequestDTO) {
		String traza = Utilitarios.trazaLog();
		traza = traza + "-" + tokenRequestDTO.getNumeroDocumentoCliente();
		LOGGER.info("[" + traza + "] Entro obtenerURLVidaFree#obtenerCumulo(tokenRequestDTO)");
		BaseResponseDTO response = new BaseResponseDTO();
		List<String> observaciones = new ArrayList<>();
		ObjectMapper om = new ObjectMapper();

		String usuarioLogin = "";

		if (tokenRequestDTO.getNombreUsuario().length() > 51) {
			usuarioLogin = tokenRequestDTO.getNombreUsuario().substring(0, 50);
		} else {
			usuarioLogin = tokenRequestDTO.getNombreUsuario();
		}

		Persona persona = personaRepository.findByNumeroDocumento(tokenRequestDTO.getNumeroDocumentoCliente());

		String tokenJson;
		try {
			Map<String, Object> dict = new HashMap();
			dict.put("producto", tokenRequestDTO.getProducto());
			dict.put("usuarioLogin", tokenRequestDTO.getUsuarioLogin());
			dict.put("agenteCorreo", tokenRequestDTO.getAgenteCorreo());
			dict.put("agenteNumVendedor", tokenRequestDTO.getAgenteNumVendedor());
			dict.put("agenteIdCRM", tokenRequestDTO.getAgenteIdCRM());
			dict.put("tipoDocumentoCliente", persona.getTipoDocumento());
			dict.put("numeroDocumentoCliente", tokenRequestDTO.getNumeroDocumentoCliente());
			dict.put("nombreCliente", tokenRequestDTO.getNombreCliente());
			dict.put("edadActuarial", tokenRequestDTO.getEdadActuarial());
			dict.put("fechaNacimiento", tokenRequestDTO.getFechaNacimiento());
			dict.put("sexo", tokenRequestDTO.getSexo());
			dict.put("idUsuarioCrm", tokenRequestDTO.getIdUsuarioCrm());
			dict.put("nombreUsuario", tokenRequestDTO.getNombreUsuario());
			dict.put("rol", tokenRequestDTO.getRol());
			dict.put("fechaCotizacion", tokenRequestDTO.getFechaCotizacion());
			dict.put("idOportunidadCrm", "0");
			dict.put("idCotizacionCrm", "0");
			dict.put("flagIGV", tokenRequestDTO.getFlagIGV());
			// condición exclusiva para caso gerente Interbank 10308468: CARLOS JOSE TORI GRANDE
			// condición exclusiva para 10219344: Alex Woodman Navarrete
			// 06026391: Carlos Ananos (AJE)
			if (tokenRequestDTO.getNumeroDocumentoCliente().equals("10308468") ||
				tokenRequestDTO.getNumeroDocumentoCliente().equals("10219344")
				|| tokenRequestDTO.getNumeroDocumentoCliente().equals("06026391")) {
				dict.put("habilitarFA", "1");
			} else {
				dict.put("habilitarFA", "0");
			}
			observaciones.add(gson.toJson(dict));
			if (tokenRequestDTO.getNumeroCotizacion() == "" || tokenRequestDTO.getNumeroCotizacion().equals("")) {
				try {
					CotizacionCorrelativo cotizacionCorrelativo = cotizacionCorrelativoRepository.findAll().get(0);

					if (cotizacionCorrelativo != null) {
						int correlativo = cotizacionCorrelativo.getCorrelativo() + 1;
						cotizacionCorrelativo.setCorrelativo(correlativo);
						cotizacionCorrelativoRepository.save(cotizacionCorrelativo);
						dict.put("numeroCotizacion", correlativo);
						observaciones.add("Cotizacion: " + correlativo);
					}
				} catch (Exception e) {
					LOGGER.error("[" + traza + "] Problemas al invocar servicio de Cotizador[ObtenerCumulo] ==> "
							+ e.getMessage());
					dict.put("numeroCotizacion", "");
					dict.put("cumuloMoneda", "");
					observaciones.add("Error al obtener cúmulo: " + e.getMessage());
				}
			} else {
				dict.put("numeroCotizacion", tokenRequestDTO.getNumeroCotizacion());
			}

			String idTercero = "";
			/*String tipoDocumentoAcsele = tokenRequestDTO.getTipoDocumentoCliente() == "2" ? "PersonaJuridica"
					: "PersonaNatural";
			TerceroResponse tercero = acseleRestClient.obtenerIDTercero(tipoDocumentoAcsele,
					tokenRequestDTO.getNumeroDocumentoCliente());
			if (tercero.getStatusHttp() == "200" && tercero.getTerceros() != null && tercero.getErrores().size() == 0) {
				idTercero = tercero.getTerceros().get(0).getIdTercero();
			} else {
				idTercero = "0";
			}*/

			dict.put("idTercero", idTercero);

			// obtener cumulo
			CotizadorCumuloResponseDTO cumulo = this.obtenerCumulo(tokenRequestDTO.getTipoDocumentoCliente(),tokenRequestDTO.getNumeroDocumentoCliente());
			dict.put("cumuloMoneda", Constantes.MONEDA_ACSELE_DOLARES);
			dict.put("cumuloMonto", cumulo.getCumuloDolares().toString());
			dict.put("cumuloMontoSoles", cumulo.getCumuloSoles().toString());

			ConversionResponseDTO conversionResponseDTO = serviceFactory.getCotizaService().obtenerTipoCambio();
			dict.put("tipoCambio", conversionResponseDTO.getValor());

			LOGGER.info("[" + traza + "] Objeto token ==> " + gson.toJson(dict));
			observaciones.add("Token: " + gson.toJson(dict));
			tokenJson = om.writeValueAsString(dict);
			// Verifica que el estado sea diferente de 4 para poder recotizar
			if (StringUtils.isNotBlank(tokenRequestDTO.getNumeroCotizacion())) {
				Solicitud solicitud = solicitudRepository.findByNumeroCotizacion(tokenRequestDTO.getNumeroCotizacion());

				int estado = Integer.parseInt(solicitud.getEstado());
				if (estado < 4) {
					response.setCodigoRespuesta("01");
					response.setMensajeRespuesta(urlCotizacionVidaFree + this.encrypt(tokenJson));
					LOGGER.info("[" + traza + "] urlCotizacionVidaFree ==> " + this.encrypt(tokenJson));
				} else {
					response.setCodigoRespuesta("95");
					response.setMensajeRespuesta("Esta propuesta ya está firmada, no puede recotizar");
				}
			} else {
				response.setCodigoRespuesta("01");
				response.setMensajeRespuesta(urlCotizacionVidaFree + this.encrypt(tokenJson));
				LOGGER.info("[" + traza + "] urlCotizacionVidaFree ==> " + this.encrypt(tokenJson));
			}

			// Guardar en PG si es que no existe
			try {
				guardarClientePG(tokenRequestDTO);
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
			}
		} catch (JsonProcessingException e) {
			LOGGER.error("[" + traza + "] Ocurrio un error al generar token.");
			response.setCodigoRespuesta("99");
			response.setMensajeRespuesta("[ADN-010] Ocurrio un error al generar token Vida Free. ");
			response.setObjErrorResource(new ErrorResourceDTO("[ADN-010]",
					"Ocurrio un error al generar token Vida Free. " + e.getMessage()));
		}
		LOGGER.info("[" + traza + "] Salio obtenerURLVidaFree#obtenerCumulo(tokenRequestDTO)");
		return response;
	}

	private void guardarClientePG(TokenRequestDTO tokenRequestDTO) {
		Asegurable asegurable = new Asegurable();
		try {
			String documento = tokenRequestDTO.getNumeroDocumentoCliente();
			LOGGER.info("guardarClientePG => Guardando dccumento nuevo: " + documento);
			asegurable = asegurableRepository.getAsegurable(documento);

			Persona persona = personaRepository.findByNumeroDocumento(documento);
			AsegurablePG asegurablePG = new AsegurablePG();
			asegurablePG.setLogin(tokenRequestDTO.getAgenteNumVendedor());
			if (asegurable == null) {
				asegurablePG.setId(0);
			} else {
				asegurablePG.setId(Integer.valueOf(asegurable.getCodigoCliente()));
			}
			asegurablePG.setCod(tokenRequestDTO.getAgenteNumVendedor());
			asegurablePG.setApepat(persona.getApellidoPaterno());
			asegurablePG.setApemat(persona.getApellidoMaterno());
			asegurablePG.setNom(
					persona.getNombres() + " " + persona.getApellidoPaterno() + " " + persona.getApellidoMaterno());
			asegurablePG.setFenac(formatearFecha(persona.getFechaNacimiento()));
			asegurablePG.setFlagsex(persona.getGenero().equals("1") ? "M" : "F");
			asegurablePG.setTipdoc(tokenRequestDTO.getTipoDocumentoCliente());
			asegurablePG.setNrodoc(documento);
			asegurablePG.setNomAge(tokenRequestDTO.getNombreUsuario());
			asegurablePG.setCorreoAgente(tokenRequestDTO.getAgenteCorreo());

			String json = gson.toJson(asegurablePG);
			LOGGER.info(json);
			ResultType resultType = asegurableRepository.save(json);
			LOGGER.info(gson.toJson(resultType));
			LOGGER.info("guardarClientePG => Guardado: " + documento);
		} catch (Exception e) {
			LOGGER.error("guardarClientePG => Error al guardar en PG");
			LOGGER.error(e.getMessage());
		}
	}

	public String formatearFecha(Date fecha) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		return sdf.format(fecha);
	}

	@Override
	public TokenResponseDTO decryptToken(String token) {
		TokenResponseDTO response = new TokenResponseDTO();
		CotizacionTokenBean bean = null;
		// Map<String, Object> dict;

		ObjectMapper mapper = new ObjectMapper();

		String json = this.decrypt(token);

		try {
			bean = mapper.readValue(json, CotizacionTokenBean.class);
			Persona p = personaRepository.findByNumeroDocumento(bean.getNumeroDocumentoCliente());
			bean.setFumador(p.getFumador());
			response.setMensajeRespuesta("OK");
			response.setCodigoRespuesta("01");
			response.setTokenDecrypt(bean);
		} catch (JsonParseException e) {
			response.setMensajeRespuesta("Error en parseo de JSON");
			response.setCodigoRespuesta("99");
			response.setTokenDecrypt(bean);
			e.printStackTrace();
		} catch (JsonMappingException e) {
			response.setMensajeRespuesta("Error en mapeo de JSON");
			response.setCodigoRespuesta("99");
			response.setTokenDecrypt(bean);
			e.printStackTrace();
		} catch (IOException e) {
			response.setMensajeRespuesta("Error");
			response.setCodigoRespuesta("99");
			response.setTokenDecrypt(bean);
			e.printStackTrace();
		}

		return response;
	}

	@Override
	public TokenResponseDTO validateToken(String token) {
		Jws<Claims> jws = null;
		TokenResponseDTO tokenResponseDTO=new TokenResponseDTO();
		try {
			jws = Jwts.parser()
					.setSigningKey(Constantes.JWT_SECRET_KEY)
					.parseClaimsJws(token.replace(Constantes.JWT_RESPONSE_HEADER_PREFIX, "").trim());
			if (jws == null) {
				tokenResponseDTO.setCodigoRespuesta("99");
				tokenResponseDTO.setMensajeRespuesta("Token invalido");
				return tokenResponseDTO;
			}
			tokenResponseDTO.setCodigoRespuesta("200");
			tokenResponseDTO.setMensajeRespuesta("OK");
			return tokenResponseDTO;
		} catch (Exception e) {
			tokenResponseDTO.setCodigoRespuesta("99");
			tokenResponseDTO.setMensajeRespuesta("Token invalido");
			return tokenResponseDTO;
		}
	}

	@Override
	public CotizaTablaResponseDTO obtenerTabla(String codigoTabla) {
		LOGGER.info("Entro AdnServiceImpl#obtenerTabla(codigoTabla)");

		// List<CotizaTablaItemResponseDTO> lista = new
		// ArrayList<CotizaTablaItemResponseDTO>();

		CotizaTablaResponseDTO response = new CotizaTablaResponseDTO();
		response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
		response.setMensajeRespuesta(
				Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_EXITO));

		LOGGER.info("Salio AdnServiceImpl#obtenerTabla(codigoTabla)");
		return response;
	}

	public LinkPagoCotResponseDTO getLinkPago(String cotizacion) {
		LOGGER.info("Entro AdnServiceImpl#getLinkPago(cotizacion)");
		LinkPagoCotResponseDTO response = new LinkPagoCotResponseDTO();
		List<Object[]> results = solicitudRepository.listaLinkPago(cotizacion);
		if (results != null && !results.isEmpty()) {
			response.setNumero_cotizacion((String) results.get(0)[0]);
			String linkPago = (String) results.get(0)[1];
			String linkRecargo = (String) results.get(0)[2];
			response.setLink_pago(linkPago != null ? linkPago : "No hay");
			response.setLink_recargo(linkRecargo != null ? linkRecargo : "No hay");
		} else {
			response.setNumero_cotizacion("No existo u.u");
			response.setLink_pago("");
			response.setLink_recargo("");
		}
		LOGGER.info("Salio AdnServiceImpl#getLinkPago(cotizacion)");
		return response;
	}

	@Override
	public CotizaListaResponseDTO listaDocumentoProducto(String documento, String producto) {
		LOGGER.info("Entro CotizaListaResponseDTO#listaDocumentoProducto(documento)");

		CotizaListaResponseDTO response = new CotizaListaResponseDTO();

		List<Cotizacion> cotizaciones = cotizacionRepository.listaDocumentoProducto(documento, producto);
		LOGGER.info("Cotizaciones:");

		List<CotizaListaItemResponseDTO> lista = new ArrayList<CotizaListaItemResponseDTO>();
		cotizaciones.forEach(cotizacion -> {

			CotizaListaItemResponseDTO item = new CotizaListaItemResponseDTO();
			try {
				BeanUtils.copyProperties(item, cotizacion);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}

			lista.add(item);
		});

		response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
		response.setMensajeRespuesta(
				Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_EXITO));
		response.setLista(lista);
		LOGGER.info("Salio CotizaListaResponseDTO#listaDocumentoProducto(documento)");
		return response;
	}

	@Override
	public CotizaDetalleResponseDTO detalle(Long nroCotizacion) {
		LOGGER.info("Entro CotizaDetalleResponseDTO#detalle(String nroCotizacion)");

		CotizaDetalleResponseDTO response = new CotizaDetalleResponseDTO();

		List<CotizacionDetalle> model = cotizacionRepository.detalleCotizacion(nroCotizacion);
		List<CotizaDetalleCoberturaResponseDTO> listaDetalle = new ArrayList<>();
		int i = 0;
		model.forEach(cotizacion -> {

			if (i == 0) {
				response.setNroCotizacion(cotizacion.getNroCotizacion());
				response.setFechaCotizacion(cotizacion.getFechaCotizacion());
				response.setMoneda(cotizacion.getMoneda());
				// response.setMontoTotalFondoGarantizado(cotizacion.getMontoTotalFondoGarantizado());
				response.setFrecuenciaPagoPrima(cotizacion.getFrecuenciaPagoPrima());
				response.setPeriodoCobertura(cotizacion.getPeriodoCobertura());
				response.setNroAnualidadesPagoBeneficio(cotizacion.getNroAnualidadesPagoBeneficio());
				response.setPlanId(cotizacion.getPlanId());
				response.setPlan(cotizacion.getPlan());
				response.setPlanCuotaComodin(cotizacion.getPlanCuotaComodin());
				response.setPlanCuotaDoble(cotizacion.getPlanCuotaDoble());
				response.setFumador(cotizacion.getFumador());
				response.setPrimaComercialAnual(cotizacion.getPrimaComercialAnual());
				response.setFactorPago(cotizacion.getFactorPago());
				response.setPrimaComercial(cotizacion.getPrimaComercial());
				response.setSubplanId(cotizacion.getSubplanId());
				response.setSubplan(cotizacion.getSubplan());
				response.setTirGarantizada(cotizacion.getTirGarantizada());
				response.setPrimaIgv(0F);
				response.setCrmCotizadorId(cotizacion.getCrmCotizadorId());
				response.setCrmOportunidadId(cotizacion.getCrmOportunidadId());
			}

			if (cotizacion.getMontoTotalFondoGarantizado() != null && cotizacion.getMontoTotalFondoGarantizado() > 0) {
				response.setMontoTotalFondoGarantizado(cotizacion.getMontoTotalFondoGarantizado());
			}

			listaDetalle.add(new CotizaDetalleCoberturaResponseDTO(cotizacion.getCoberturaId(),
					cotizacion.getCoberturaTipo(), cotizacion.getCoberturaNombre(), cotizacion.getCoberturaCapital(),
					cotizacion.getCoberturaPrima()));

		});

		response.setCobertura(listaDetalle);

		response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
		response.setMensajeRespuesta(
				Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_EXITO));

		LOGGER.info("Salio CotizaDetalleResponseDTO#detalle(String nroCotizacion)");
		return response;
	}

	@Override
	public CotizaListaResponseDTO listaCotizacionesVida(String documento, String idUsuario, String device, String os) {
		final String traza = Utilitarios.trazaLog() + "-" + documento;
		LOGGER.info("[" + traza + "] Entro CotizaServiceImpl#listaCotizacionesVida(documento)");

		CotizaListaResponseDTO response = new CotizaListaResponseDTO();
		List<CotizaListaItemResponseDTO> lista = new ArrayList<>();
		List<String> observaciones = new ArrayList<>();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -31);
		Date dateLimite = cal.getTime();
		String fechaLimite = dateFormat.format(dateLimite);

		LOGGER.info("[" + traza + "] Listando cotizaciones VF y PG...");
		List<CotizacionGeneralResponse> cotizacionGeneral = solicitudGeneralRepository
				.findCotizacionGeneralbyDocumento(documento, fechaLimite);

		LOGGER.info("Cotizaciones Generales: " + cotizacionGeneral.toString());

		if (!cotizacionGeneral.isEmpty()) {
			cotizacionGeneral.forEach(cotizacion -> {
				if (cotizacion.getTipoProducto().equals("2")) {
					CotizaListaItemResponseDTO item = new CotizaListaItemResponseDTO();
					String estadoSolicitud = "0";
					String cotizacionOriginal = "";
					String cotizacionActual = "";
					String monedaSimbolo = "";
					try {
						Solicitud model = new Solicitud();
						model.setNumeroCotizacion(cotizacion.getNroCotizacion());
						model.setNumeroCotizacionOrigen(cotizacion.getNroCotizacionOriginal());
						model.setMoneda(cotizacion.getMoneda());
						model.setEstado(cotizacion.getEstado());
						model.setEstadoGeneral(cotizacion.getEstadoGeneral());
						model.setFechaRegistroSamp(cotizacion.getFechaRegistroSamp());
						LOGGER.info("Se obtienen los datos de solicitud VF");

						monedaSimbolo = model.getMoneda().equals("1") ? "S/." : "$";

						if (model != null) {
							cotizacionOriginal = model.getNumeroCotizacionOrigen();
							cotizacionActual = model.getNumeroCotizacion();

							if (model.getEstado().equals("0")) {
								estadoSolicitud = model.getEstado();
							} else {
								estadoSolicitud = "1";
							}
							if (Integer.parseInt(model.getEstado()) > Integer
									.parseInt(Constantes.CODIGO_SOLICITUD_FIRMA_FINALIZADA)) {
								observaciones.add("Cotizacion " + cotizacionActual + " tiene solicitud finalizada.");
								LOGGER.info("Cotizacion tiene solicitud finalizada");
								estadoSolicitud = "2";
								if (model.getFechaRegistroSamp() != null) {
									estadoSolicitud = "10";
									observaciones.add("Cotizacion " + cotizacionActual + " tiene pago realizado.");
									LOGGER.info("Cotizacion tiene pago realizado");
								}

								if (model.getEstadoGeneral().equals(Constantes.ENTREGADO_A_GDC)) {
									estadoSolicitud = model.getEstadoGeneral();
								} else if (model.getEstadoGeneral().equals(Constantes.TRANSMITIDA)) {
									estadoSolicitud = model.getEstadoGeneral();
								}
							} else {
								observaciones.add("Cotizacion " + cotizacionActual + " no tiene propuesta.");
							}
						}
					} catch (Exception e) {
						LOGGER.error("[" + traza + "] Error al buscar cotizacion " + cotizacion.getNroCotizacion()
								+ " ==> " + e.getMessage());
						observaciones.add("Error al intentar buscar cotizacion " + cotizacion.getNroCotizacion()
								+ " ==> " + e.getMessage());
					}
					item.setNroCotizacion(cotizacion.getNroCotizacion());
					item.setNroCotizacionOriginal("");
					item.setPlanNombre(cotizacion.getPlanNombre());
					if (cotizacion.getTipoProducto().equals("2")) {
						item.setProductoNombre("Vida Free");
					}
					item.setPeriodoPago(cotizacion.getPeriodoPago());
					item.setMonedaSimbolo(monedaSimbolo);
					item.setFechaCotizacion(cotizacion.getFechaCotizacion());
					item.setPrimaBruta(cotizacion.getPrimaAnual());
					item.setPrimaPagoTotal(monedaSimbolo + cotizacion.getPrimaPagoTotal());
					String url = urlCotizacionVidaFreePDF.concat(cotizacion.getNroCotizacion());
					String urlADN = urlADNPDF; // Anedd
					item.setUrlReporte(url);
					item.setUrlReporteADN(urlADN); // Anedd
					item.setEstadoSolicitud(estadoSolicitud);
					lista.add(item);
					LOGGER.info("Lista: " + lista);
				} else if (cotizacion.getTipoProducto().equals("1")) {
					CotizaListaItemResponseDTO item = new CotizaListaItemResponseDTO();
					try {
						BeanUtils.copyProperties(item, cotizacion);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
					LOGGER.info("Cotizacion: " + cotizacion.toString());
					LOGGER.info("Item: " + item.toString());

					item.setPrimaPagoTotal(item.getMonedaSimbolo() + item.getPrimaPagoTotal());
					String estadoSolicitud = "0";
					String cotizacionActual = "";
					try {
						Solicitud model = new Solicitud();
						model.setNumeroCotizacion(cotizacion.getNroCotizacion());
						model.setNumeroCotizacionOrigen(cotizacion.getNroCotizacionOriginal());
						model.setEstado(cotizacion.getEstado());
						model.setEstadoGeneral(cotizacion.getEstadoGeneral());
						model.setFechaRegistroSamp(cotizacion.getFechaRegistroSamp());

						if (model != null) {
							cotizacionActual = model.getNumeroCotizacion();
							if (model.getEstado().equals("0")) {
								estadoSolicitud = model.getEstado();
							} else {
								estadoSolicitud = "1";
							}
							if (Integer.parseInt(model.getEstado()) > Integer
									.parseInt(Constantes.CODIGO_SOLICITUD_FIRMA_FINALIZADA)) {
								observaciones.add("Cotizacion " + cotizacionActual + " tiene solicitud finalizada.");
								estadoSolicitud = "2";
								if (model.getFechaRegistroSamp() != null) {
									estadoSolicitud = "10";
									observaciones.add("Cotizacion " + cotizacionActual + " tiene pago realizado.");
								}

								if (model.getEstadoGeneral().equals(Constantes.ENTREGADO_A_GDC)) {
									estadoSolicitud = model.getEstadoGeneral();
								} else if (model.getEstadoGeneral().equals(Constantes.TRANSMITIDA)) {
									estadoSolicitud = model.getEstadoGeneral();
								}
							}
						}
					} catch (Exception e) {
						LOGGER.error("[" + traza + "] Error al procesar Cotizaciones Vida ==> " + e.getMessage());
						observaciones.add("Error al procesar estados de cotizaciones ==> " + e.getMessage());
					}
					item.setEstadoSolicitud(estadoSolicitud);
					if (cotizacion.getTipoProducto().equals("1")) {
						item.setProductoNombre("Plan Garantizado");
					}
					item.setNroCotizacionOriginal("");
					String url = urlCotizacionPDF.concat(cotizacion.getNroCotizacion()).concat("/agente/nombrecliente");
					item.setUrlReporte(url);
					lista.add(item);
				}
			});
		}

		if (lista.size() > 0) {
			if (lista.get(0).getProductoNombre().equalsIgnoreCase("Plan Garantizado")) {
				validarPotencialVtigerPlanGarantizado(idUsuario, cotizacionGeneral);
			} else {
				validarPotencialVtigerVidaFree(idUsuario, cotizacionGeneral);
			}
		}

		response.setLista(lista);
		response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
		response.setMensajeRespuesta(
				Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_EXITO));
		LOGGER.info("[" + traza + "] Salio CotizaServiceImpl#listaCotizacionesVida(documento)");
		return response;
	}
	/*
	 * @Override public CotizaUrlResponse ObtenerURLCotizadorVida2(String
	 * numeroDocumento, String idOportunidadCrm, String idAgenteCrm, String
	 * codigoAgente, String nombreAgente, String usuario) { CotizaUrlResponse
	 * urlCotizador = new CotizaUrlResponse(); String idTercero = ""; try { String
	 * tipoDocumentoAcsele = "PersonaNatural"; if (numeroDocumento != null) {
	 * TerceroResponse tercero =
	 * acseleRestClient.obtenerIDTercero(tipoDocumentoAcsele, numeroDocumento); if
	 * (tercero != null && tercero.getErrores().size() == 0 && tercero.getTerceros()
	 * != null) { idTercero = tercero.getTerceros().get(0).getIdTercero(); } ; } }
	 * catch (Exception e) {
	 * LOGGER.error("Problemas al invocar servicio de Acsele[ObtenerIdTercero]");
	 * LOGGER.error(e.getMessage()); } Asegurable asegurable =
	 * cotizacionRepository.obtenerDataAsegurable(numeroDocumento);
	 * AsegurableCrmResponse response =
	 * cotizadorRestClient.obtenerAsegurable(asegurable.getCodigoCliente(),
	 * asegurable.getApellidoPaterno(), asegurable.getApellidoMaterno(),
	 * asegurable.getNombres(), asegurable.getFechaNacimiento(),
	 * asegurable.getSexo(), asegurable.getTipoDocumento(),
	 * asegurable.getNumeroDocumento(), idAgenteCrm, "0", "", idOportunidadCrm,
	 * "true", idTercero, codigoAgente, nombreAgente, usuario);
	 *
	 * try { if (response != null) { urlCotizador.setResult(response.getRs());
	 * urlCotizador.setURL(this.serviceUrlBaseCotizador + response.getMsg());
	 * urlCotizador.setMessage(response.getMsg()); } } catch (Exception e) {
	 * e.printStackTrace(); }
	 *
	 * LOGGER.info("obtenerURLCotizadorVidaurlCotizadorVida.NEXT=>" + urlCotizador);
	 * return urlCotizador; }
	 */

	@Async("processExecutor")
	private void validarPotencialVtigerVidaFree(String idUsuario, List<CotizacionGeneralResponse> cotizacionesVF) {

		LOGGER.info("ENTRO MEDOTO POTENTIALVTIGERVF");

//		Solicitud model = solicitudRepository.findByNumeroCotizacion(cotizacionesVF.get(0).getNroCotizacion());

		/* consulta potential */
//		Persona personaAsegurado = personaRepository.findById(model.getIdAsegurado()).get();

		Persona personaAsegurado = personaRepository.findByNumeroDocumento(cotizacionesVF.get(0).getNumeroDocumento());

		if (StringUtils.isNotBlank(personaAsegurado.getNumeroDocumento())) {
			obtenerPotentialVtigerRequest requestgetPotential = new obtenerPotentialVtigerRequest();
			requestgetPotential
					.setTipoDoc(personaAsegurado.getTipoDocumento().equals("1") ? "DNI" : "Carnet Extranjería");
			requestgetPotential.setNroDoc(personaAsegurado.getNumeroDocumento());

			try {
				LOGGER.info("INICIO METODO OBTENER POTENTIAL");
				obtenerPotentialVtigerResponse obtenerPotential = globalRestClient
						.obtenerPotentialVtiger(requestgetPotential);
				LOGGER.info("FIN METODO OBTENER POTENTIAL");

				if (!obtenerPotential.getMensaje().equals("1")) {
					ObtenerAgenteRequest obtenerAgenteRequest = new ObtenerAgenteRequest();
					obtenerAgenteRequest.setCodAgencia(idUsuario);
					ObtenerAgenteResponse responseGlobal = globalRestClient.obtenerAgente(obtenerAgenteRequest);
					String myString = cotizacionesVF.get(0).getPrimaPagoTotal();
					myString = myString.replace("S/.", "");
					/* registrar potential */
					CrearPotentialVtigerRequest requestPotential = new CrearPotentialVtigerRequest();
					requestPotential.setTipo_documento(
							personaAsegurado.getTipoDocumento().equals("1") ? "DNI" : "Carnet Extranjería");
					requestPotential.setNumero_documento(personaAsegurado.getNumeroDocumento());
					requestPotential.setPotentialname(cotizacionesVF.get(0).getPlanNombre());
					requestPotential.setCf_864(cotizacionesVF.get(0).getPlanNombre());
					requestPotential.setCf_880(cotizacionesVF.get(0).getPlanNombre());
					requestPotential.setAmount(String.valueOf(myString));
					requestPotential.setOpportunity_type("Múltiples Pólizas (Registro Padre)");
					requestPotential.setLeadsource("Referido ADN");
					requestPotential.setSales_stage("Nuevo");
					requestPotential.setAssigned_user_id(responseGlobal.getGLS_CORREO_AGENTE());
					Date fecha = new Date();
					requestPotential
							.setCreatedtime(DateUtil.dateToString(fecha, DateUtil.FORMATO_DIA_DDMMYYYY_M_HHMMSS));
					requestPotential
							.setCf_852(personaAsegurado.getTipoDocumento().equals("1") ? "DNI" : "Carnet Extranjería");
					requestPotential.setCf_1050(personaAsegurado.getNumeroDocumento());

					requestPotential.setCf_870("90");
					requestPotential.setCf_872("En Plazo");
					requestPotential.setCf_876("15");
					requestPotential.setCf_878(String.valueOf(myString));
					LOGGER.info(gson.toJson(requestPotential));
					try {
						globalRestClient.crearPotentialVtiger(requestPotential);
					} catch (Exception e) {
						LOGGER.info("error response");
						LOGGER.info("Contact does not exist" + e);
					}
				}
			} catch (Exception e) {
				LOGGER.info("error en llamado obtenerPotential" + e);
				LOGGER.info("Contact does not exist");
			}
		}
		LOGGER.info("FIN DEL METODO");
	}

	@Async("processExecutor")
	private void validarPotencialVtigerPlanGarantizado(String idUsuario, List<CotizacionGeneralResponse> cotizaciones) {

		Persona personaAsegurado = personaRepository.findByNumeroDocumento(cotizaciones.get(0).getNumeroDocumento());

		if (StringUtils.isNotBlank(personaAsegurado.getNumeroDocumento())) {
			LOGGER.info("OBTENIENDO POTENCIAL VTIGER");
			obtenerPotentialVtigerRequest requestgetPotential = new obtenerPotentialVtigerRequest();
			requestgetPotential
					.setTipoDoc(personaAsegurado.getTipoDocumento().equals("1") ? "DNI" : "Carnet Extranjería");
			requestgetPotential.setNroDoc(personaAsegurado.getNumeroDocumento());

			try {
				obtenerPotentialVtigerResponse obtenerPotential = globalRestClient
						.obtenerPotentialVtiger(requestgetPotential);
				if (!obtenerPotential.getMensaje().equals("1")) {
					ObtenerAgenteRequest obtenerAgenteRequest = new ObtenerAgenteRequest();
					obtenerAgenteRequest.setCodAgencia(idUsuario);
					LOGGER.info("OBTENIENDO CODIGO GENTE");
					ObtenerAgenteResponse responseGlobal = globalRestClient.obtenerAgente(obtenerAgenteRequest);
					/* registrar potential */
					CrearPotentialVtigerRequest requestPotential = new CrearPotentialVtigerRequest();
					requestPotential.setTipo_documento(
							personaAsegurado.getTipoDocumento().equals("1") ? "DNI" : "Carnet Extranjería");
					requestPotential.setNumero_documento(cotizaciones.get(0).getNumeroDocumento());
					requestPotential.setPotentialname(cotizaciones.get(0).getSubplan());
					requestPotential.setCf_864(cotizaciones.get(0).getSubplan());
					requestPotential.setCf_880(cotizaciones.get(0).getSubplan());
					requestPotential.setAmount(String.valueOf(cotizaciones.get(0).getPrimaPagoTotal()));
					requestPotential.setOpportunity_type("Múltiples Pólizas (Registro Padre)");
					requestPotential.setLeadsource("Referido ADN");
					requestPotential.setSales_stage("Nuevo");
					requestPotential.setAssigned_user_id(responseGlobal.getGLS_CORREO_AGENTE());
					Date fecha = new Date();
					requestPotential
							.setCreatedtime(DateUtil.dateToString(fecha, DateUtil.FORMATO_DIA_DDMMYYYY_M_HHMMSS));
					requestPotential
							.setCf_852(personaAsegurado.getTipoDocumento().equals("1") ? "DNI" : "Carnet Extranjería");
					requestPotential.setCf_1050(personaAsegurado.getNumeroDocumento());
					requestPotential.setCf_870("90");
					requestPotential.setCf_872("En Plazo");
					requestPotential.setCf_876("15");
					requestPotential.setCf_878(String.valueOf(cotizaciones.get(0).getPrimaPagoTotal()));
					LOGGER.info(gson.toJson(requestPotential));
					try {
						LOGGER.info("CREAR POTENCIAL VTIGER");
						globalRestClient.crearPotentialVtiger(requestPotential);
					} catch (Exception e) {
						LOGGER.info("Contact does not exist");
					}
				}
			} catch (Exception e) {
				LOGGER.info("error en llamado obtenerPotential" + e);
				LOGGER.info("Contact does not exist");
			}
		}
	}

	@Override
	public CotizaUrlResponse ObtenerURLCotizadorVida(String numDocumento, String idUsuario, String device, String os) {
		String traza = Utilitarios.trazaLog() + "-" + numDocumento;
		LOGGER.info("[" + traza + "] Entro CotizaServiceImpl#ObtenerURLCotizadorVida(idOportunidad)");
		CotizaUrlResponse urlCotizador = new CotizaUrlResponse();
		List<String> observaciones = new ArrayList<>();

		// CRM antiguo
		/*
		 * UrlCotizadorNuevoRequest request = new UrlCotizadorNuevoRequest();
		 * request.setStrIdOportunidad(idOportunidad); CrmIntermedioResponse response =
		 * crmRestClient.obtenerUrlCotizador(request);
		 */

		Persona persona = personaRepository.findByNumeroDocumento(numDocumento);

		// obtener agente
		ObtenerAgenteRequest obtenerAgenteRequest = new ObtenerAgenteRequest();
		obtenerAgenteRequest.setCodAgencia(idUsuario);
		ObtenerAgenteResponse responseGlobal = globalRestClient.obtenerAgente(obtenerAgenteRequest);

		// api-plangarantizado
		UrlCotizadorNuevoPGRequest globalRequest = new UrlCotizadorNuevoPGRequest();
		globalRequest.setCodigoVendedorCRM(idUsuario);
		globalRequest.setCodigoAgente(responseGlobal.getCOD_AGENTE());
		globalRequest.setAgente(responseGlobal.getNOM_AGENTE());
		globalRequest.setIdPersona(String.valueOf(persona.getIdPersona()));
		globalRequest.setCorreoAgente(responseGlobal.getGLS_CORREO_AGENTE());

		System.out.println(gson.toJson(globalRequest));
		UrlCotizadorNuevoPGResponse response = globalRestClient.obtenerUrlCotizador(globalRequest);

		if (response.getStatusHttp() == "200") {
			urlCotizador.setCodigoRespuesta(response.getRs() ? "01" : "99");
			urlCotizador.setResult(response.getRs());
			urlCotizador.setURL(response.getUrl());
			urlCotizador.setMessage(response.getMsg());
			urlCotizador.setMensajeRespuesta(response.getMsg() == null ? "" : response.getMsg());
			observaciones.add("URL: " + response.getUrl());
		} else {
			urlCotizador.setCodigoRespuesta("99");
			urlCotizador.setMensajeRespuesta(
					"[CRM-006] No se encontró respuesta al intentar obtener URL de cotizador Vida.");
			urlCotizador.setObjErrorResource(new ErrorResourceDTO("CRM-006", "Error: " + response.getMessage()));
		}
		/*
		 * logRepository.save(new EventLog("Cotizaciones",
		 * urlCotizador.getCodigoRespuesta(), "/cotizaciones/intermedio/nuevo/" +
		 * numDocumento, urlCotizador.getMensajeRespuesta(),
		 * gson.toJson(urlCotizador.getObjErrorResource()), gson.toJson(observaciones),
		 * idUsuario, device, os));
		 */
		LOGGER.info("[" + traza + "] Salio CotizaServiceImpl#ObtenerURLCotizadorVida(idOportunidad)");
		return urlCotizador;
	}

	@Override
	public CotizaUrlResponse ObtenerURLRecotizarVida(String nroDocumento, String nroCotizacion, String idUsuario) {
		CotizaUrlResponse urlCotizador = new CotizaUrlResponse();

		try {
			Persona persona = personaRepository.findByNumeroDocumento(nroDocumento);

			// obtener agente
			ObtenerAgenteRequest obtenerAgenteRequest = new ObtenerAgenteRequest();
			obtenerAgenteRequest.setCodAgencia(idUsuario);
			ObtenerAgenteResponse responseGlobal = globalRestClient.obtenerAgente(obtenerAgenteRequest);

			UrlRecotizarVidaPGRequest request = new UrlRecotizarVidaPGRequest();
			request.setAgente(responseGlobal.getNOM_AGENTE()); // iduausrio x
			request.setCodigoAgente(responseGlobal.getCOD_AGENTE()); // iduausrio
			request.setCodigoUsuario(responseGlobal.getCOD_USERNAME_AGENTE()); // adn
			request.setFumador(Integer.valueOf(persona.getFumador())); // adn
			request.setNumeroCotizacion(nroCotizacion); // adn x

			System.out.println(gson.toJson(request));
			UrlCotizadorNuevoPGResponse response = globalRestClient.obtenerUrlRecotizarVida(request);
			Solicitud solicitud = solicitudRepository.findByNumeroCotizacion(nroCotizacion);
			LOGGER.info("ESTADO ---> " + solicitud.getEstado());
			if (response != null) {
				if (solicitud.getEstado().equals(CODIGO_SOLICITUD_POR_TRANSMITIR)) {
					urlCotizador.setCodigoRespuesta("95");
					urlCotizador.setMensajeRespuesta("Esta propuesta ya está firmada, no puede recotizar");
				} else {
					urlCotizador.setResult(response.getRs());
					urlCotizador.setURL(response.getUrl());
					urlCotizador.setMessage(response.getMsg());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		LOGGER.info("ObtenerURLRecotizarVida.NEXT=>" + urlCotizador.toString());
		return urlCotizador;
	}

	@Override
	public BaseResponseDTO enviarCorreo(String destinatario, List<CotizacionPDFBean> adjuntos, String asegurado,
			String agenteNombre, String agenteCorreo) {
		BaseResponseDTO response = new BaseResponseDTO();
		InputStream stream;
		try {
			LOGGER.info("Contruyendo correo...");
			stream = new ClassPathResource(Constantes.RUTA_PLANTILLA + "/" + Constantes.PLANTILLA_COTIZADOR_CORREO)
					.getInputStream();
			String systemOp = System.getProperty("os.name");
			if (!systemOp.contains("Windows")) {
				stream = new FileInputStream(
						new File(rutaPlantillaDocLinux + "//" + Constantes.PLANTILLA_COTIZADOR_CORREO));
			}

			String bodyHtml = StreamUtils.copyToString(stream, Charset.defaultCharset());

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date date = new Date(System.currentTimeMillis());

			String bodyCotizacion = bodyHtml;

			SimpleDateFormat formatterTimeStamp = new SimpleDateFormat("yyyyMMddHHmmss");
			Date timestamp = new Date(System.currentTimeMillis());
			List<Adjuntos> files = new ArrayList<>();
			Adjuntos adjunto2;
			String templateFile = null;
			String ArchivoCreado;
			String cotizaciones = "";
			LOGGER.info("COPIANDO A TEMPORALES");
			String cot="";
			String doc="";
			if (adjuntos != null && adjuntos.size() > 0) {
				for (CotizacionPDFBean pdf : adjuntos) {
					String traza = Utilitarios.trazaLog() + "-" + pdf.getNumeroCotizacion();
					String cotizacionFileName = "".concat("cotizacion-").concat(pdf.getNumeroCotizacion()).concat("-")
							.concat(formatterTimeStamp.format(timestamp)).concat(".pdf");
					cotizaciones += pdf.getNumeroCotizacion().concat("<br>");
					String cotizacionFilePrivada = "" + cotizacionFileName;
					String cotizacionFilePublica = "" + cotizacionFileName;

					SolicitudFiltrado solicitud = solicitudFiltradoRepository
							.findByNumeroCotizacion(pdf.getNumeroCotizacion());
					cot=pdf.getNumeroCotizacion();
					doc=solicitud.getNumeroDocumento();
					ArchivoCreado = pdf.getNumeroCotizacion();
					Solicitud model = solicitudRepository.findByNumeroCotizacion(pdf.getNumeroCotizacion());
					model.setDocumentoCotizacion(cotizacionFileName);
					solicitudRepository.save(model);
					LOGGER.info("[" + traza + "] enviarCorreo.url: " + pdf.getUrlPDFCotizacion());
					LOGGER.info("enviarCorreo.cotizacionFilePrivada: " + cotizacionFilePrivada);
					File tmpFileCotizacion = new File(cotizacionFileName);

					try {
						String url = pdf.getUrlPDFCotizacion();
						System.out.println(url);
						int index = url.indexOf("vidafree");
						if (index != -1) {
							tmpFileCotizacion = generarPDFCotizacionVidaFree2(pdf.getNumeroCotizacion());
							cotizacionFilePrivada = "" + cotizacionFileName;
							cotizacionFilePublica = "" + cotizacionFileName;
							LOGGER.info(cotizacionFilePrivada);
							LOGGER.info(cotizacionFilePublica);
							// this.generarPDFCotizacionVidaFree(pdf.getNumeroCotizacion());
							// FileUtils.copyURLToFile(new URL(url), new File(cotizacionFilePrivada));
						} else {
							tmpFileCotizacion = new File(System.getProperty("java.io.tmpdir") + File.separator
									+ new File(cotizacionFileName));
							FileUtils.copyURLToFile(new URL(url), tmpFileCotizacion);
						}
						// FileUtils.copyURLToFile(new URL(url), new File(cotizacionFilePrivada));
					} catch (Exception ex) {
						LOGGER.info("ERROR AL COPIAR PDF COTIZACION EN TEMPORALES +", ex);
						cotizacionFilePublica = Strings.EMPTY;
					}

					LOGGER.info("cotizacionFilePublica: " + cotizacionFilePublica);

					if (Integer.parseInt(solicitud.getEstado()) >= 4) {
						ObtenerUploadArchivoResponse globalArchivoResponse = globalRestClient.obtenerArchivo(
								solicitud.getTipoDocumento(), solicitud.getNumeroDocumento(),
								pdf.getNumeroCotizacion());

						for (ArchivoResponse item : globalArchivoResponse.getFiles()) {

							if (item.getName().contains("cotizacion-" + pdf.getNumeroCotizacion())
									|| item.getName().contains("cotizacion_" + pdf.getNumeroCotizacion())) {
								LOGGER.info("[" + traza + "] Ingresamos a obtenerUrlArchivo");
								UrlResponse globalUrlArchivoResponse = globalRestClient.obtenerUrlArchivo(
										globalArchivoResponse.getContactId(), solicitud.getNumeroDocumento(),
										item.getName());
								adjunto2 = new Adjuntos();
								String urlArchivo = globalUrlArchivoResponse.getUrl();
								LOGGER.info("[" + traza + "] Url del archivo: " + urlArchivo);
								templateFile = downloadAndConvertToBase64(urlArchivo);
								LOGGER.info("[" + traza + "] file del archivo: " + templateFile);
								adjunto2.setName(item.getName());
								adjunto2.setContent(templateFile);
								files.add(adjunto2);
								break;
							}
						}
					} else {

						Boolean siExiste = false;
						ArchivoResponse archivo = null;

						ObtenerUploadArchivoResponse globalArchivoResponse = globalRestClient.obtenerArchivo(
								solicitud.getTipoDocumento(), solicitud.getNumeroDocumento(),
								pdf.getNumeroCotizacion());

						for (ArchivoResponse item : globalArchivoResponse.getFiles()) {
							if (item.getName().contains("cotizacion-" + pdf.getNumeroCotizacion())
									|| item.getName().contains("cotizacion_" + pdf.getNumeroCotizacion())) {
								siExiste = true;
								archivo = item;
								break;
							}
						}

						if (!siExiste) {
							// creamos el documento en la nube
							this.enviarPDFCRM2(tmpFileCotizacion, solicitud.getTipoDocumento(),
									solicitud.getNumeroDocumento(), cotizacionFileName, cotizacionFileName,
									solicitud.getTipoDocumento());
							LOGGER.info("Se subio documento a cloud");
							Thread.sleep(5000);
							globalArchivoResponse = globalRestClient.obtenerArchivo(solicitud.getTipoDocumento(),
									solicitud.getNumeroDocumento(), pdf.getNumeroCotizacion());

							LOGGER.info("ObtenerArchivo => " + gson.toJson(globalArchivoResponse));

							for (ArchivoResponse item : globalArchivoResponse.getFiles()) {
								if (item.getName().contains("cotizacion_" + pdf.getNumeroCotizacion())
										|| item.getName().contains("cotizacion-" + pdf.getNumeroCotizacion())) {
									archivo = item;
									break;
								}
							}
						}
						LOGGER.info("CORREOOO linea 1274 => " + gson.toJson(globalArchivoResponse.getContactId()));
						LOGGER.info("CORREOOO linea 1275 => " + gson.toJson(solicitud.getNumeroDocumento()));
						LOGGER.info("CORREOOO linea 1276 => " + gson.toJson(archivo));

						UrlResponse globalUrlArchivoResponse = globalRestClient.obtenerUrlArchivo(
								globalArchivoResponse.getContactId(), solicitud.getNumeroDocumento(),
								archivo.getName());

						LOGGER.info("[" + traza + "] url del Archivo => " + globalUrlArchivoResponse.getUrl());

						adjunto2 = new Adjuntos();
						String urlArchivo = globalUrlArchivoResponse.getUrl();
						templateFile = downloadAndConvertToBase64(urlArchivo);
						LOGGER.info("[" + traza + "] file del archivo: " + templateFile);
						adjunto2.setName(cotizacionFileName);
						adjunto2.setContent(templateFile);
						files.add(adjunto2);

					}
				}

				bodyCotizacion = bodyCotizacion.replaceAll("\\{asegurado\\}", asegurado);
				bodyCotizacion = bodyCotizacion.replaceAll("\\{fechaAtencion\\}", formatter.format(date));
				bodyCotizacion = bodyCotizacion.replaceAll("\\{numeroCotizacion\\}", cotizaciones);
				bodyCotizacion = bodyCotizacion.replaceAll("\\{agenteNombre\\}", agenteNombre);
				bodyCotizacion = bodyCotizacion.replaceAll("\\{agenteCorreo\\}", agenteCorreo);
				LOGGER.info(bodyCotizacion);
				destinatario = destinatario + "," + agenteCorreo;
				LOGGER.info("destinatario: " + destinatario);
				enviarCorreoConAdjunto(destinatario, bodyCotizacion, files,  "COTIZACION",  cot,  doc);

				response.setCodigoRespuesta("01");
				response.setMensajeRespuesta("OK");
			} else {
				response.setCodigoRespuesta("98");
				response.setMensajeRespuesta("No se encontraron archivos para envío de correo.");
			}
		} catch (IOException e) {
			e.printStackTrace();
			response.setCodigoRespuesta("99");
			response.setMensajeRespuesta("Error al generar correo.");
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		return response;
	}

	public String downloadAndConvertToBase64(String urlStr) {
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

			LOGGER.info("Documento en Base64: " + base64Content);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return base64Content;
	}

	private void enviarPDFCRM2(File fileTmp, String tipoDocumentoCRM, String numeroDocumento, String nombreArchivoPDF,
			String descripcionArchivo, String tipoDocumentoInput) {
		try {
			if (!fileTmp.equals(Strings.EMPTY)) {
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
		} catch (Exception e) {
			LOGGER.info("Error enviar PDF Solicitud a CRM=>" + e);
			e.printStackTrace();
		}
	}

	public File generarPDFCotizacionVidaFree2(String nroCotizacion) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		String urlPublicoADN = Strings.EMPTY;
		LOGGER.info("Preparando PDF");
		File adn = new File("");
		try {
			String formatoCotizacionFilePrivada = "";
			String formatoCotizacionFilePublica = "";
			String nombreArchivoTemporal = "cotizacion";
			Date fecha = new Date();
			File fileTemportalPDFADN = File.createTempFile(nombreArchivoTemporal, Constantes.ARCHIVO_EXT_PDF);
			formatoCotizacionFilePrivada = "".concat(nombreArchivoTemporal).concat("_").concat(nroCotizacion)
					.concat("_").concat(DateUtil.dateToString(fecha, DateUtil.FORMATO_DIA_DDMMYYYYHHMMSS) + ".pdf");
			formatoCotizacionFilePublica = "".concat(nombreArchivoTemporal).concat("_").concat(nroCotizacion)
					.concat("_").concat(DateUtil.dateToString(fecha, DateUtil.FORMATO_DIA_DDMMYYYYHHMMSS) + ".pdf");

			LOGGER.info("Ruta temporal JAVA java.io.tmpdir: " + System.getProperty("java.io.tmpdir"));
			adn = new File(System.getProperty("java.io.tmpdir") + "/" + new File(formatoCotizacionFilePrivada));
			nombreArchivoTemporal = fileTemportalPDFADN.getName();

			LOGGER.info("nombreArchivoTemporal: " + nombreArchivoTemporal);
			// -- Generar archivo
			out = generarPDFCotizacionVidaFree(nroCotizacion);
			LOGGER.info("urlPublicoADN=>" + urlPublicoADN);
			File docOut = new File("docOut");
			FileUtils.writeByteArrayToFile(docOut, out.toByteArray());
			FileUtils.copyFile(docOut, adn);
		} catch (Exception e) {

			LOGGER.info("Error GenerarPDFADN =>" + e);

			/*
			 * throw new SivTXException( Utilitarios.obtenerMensaje( messageSource, new
			 * Object[] {Constantes.MENSAJE_ADMINISTRADOR},
			 * Constantes.MENSAJE_RESPUESTA_GENERAL_ERR0R ), null );
			 */
		}
		return adn;
	}

	private void enviarCorreoConAdjunto(String email, String body, List<Adjuntos> files, String motivo, String cotizacion, String documento) {
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
			sbAsunto.append("TEST: ");
		} else {
			String[] correoFiltrado = email.split(",");
			LOGGER.info("correoSeparadoporComas: " + correoFiltrado.length);
			for (String correoSeparadoPrd : correoFiltrado) {
				remitente = new Remitente();
				remitente.setEmail(correoSeparadoPrd);
				listaRemitente.add(remitente);
				LOGGER.info("Correo: " + correoSeparadoPrd);
			}
		}
		enviarCorreoRequest.setTo(listaRemitente);
		sbAsunto.append(this.enviarCorreoCotizadorAsunto);

		enviarCorreoRequest.setSubject(sbAsunto.toString());
		enviarCorreoRequest.setHtmlContent(body.toString());
		enviarCorreoRequest.setAttachments(files);

		LOGGER.info("EnviarCorreoRequest=>" + gson.toJson(enviarCorreoRequest));

		try {
			interseguroRestClient.enviarCorreo(enviarCorreoRequest,  motivo,  cotizacion,  documento);
		} catch (Exception ex) {
			LOGGER.error("interseguroRestClient.enviarCorreo: " + email + " - " + ex.toString());
		}
	}

	@Override
	public CotizacionCrmResponseDTO guardarCotizacionCrm(CotizacionCrmRequestDTO cotizacionCrmRequestDTO) {
		LOGGER.info("Entro guardarCotizacionCrm()");
		CotizacionCrmResponseDTO result = new CotizacionCrmResponseDTO();
		List<String> observaciones = new ArrayList<>();
		CrmCotizacionRequest request = new CrmCotizacionRequest();
		CrmCotizacionBERequest beCotizacion = new CrmCotizacionBERequest();
		try {
			beCotizacion.setAplicaDescuento(cotizacionCrmRequestDTO.getAplicaDescuento());
			beCotizacion.setBeContacto(new CrmBEContacto());
			beCotizacion.getBeContacto().setIdCRM(cotizacionCrmRequestDTO.getBeContacto().getIdCRM());
			beCotizacion.setClaseAccPers(cotizacionCrmRequestDTO.getClaseAccPers());
			beCotizacion.setFechaFinal(cotizacionCrmRequestDTO.getFechaFinal());
			beCotizacion.setFechaInicial(cotizacionCrmRequestDTO.getFechaInicial());
			beCotizacion.setFechaMovimiento(cotizacionCrmRequestDTO.getFechaMovimiento());
			beCotizacion.setFrecuenciaPago(cotizacionCrmRequestDTO.getFrecuenciaPago());
			beCotizacion.setGrupoFamiliar(cotizacionCrmRequestDTO.getGrupoFamiliar());
			// beCotizacion.setIdCRM(cotizacionCrmRequestDTO.getIdCRM());
			beCotizacion.setIdCRM("0");
			beCotizacion.setIdOportunidadCRM(cotizacionCrmRequestDTO.getIdOportunidadCRM());
			beCotizacion.setIdPlan(cotizacionCrmRequestDTO.getIdPlan());
			beCotizacion.setIdProducto(cotizacionCrmRequestDTO.getIdProducto());
			beCotizacion.setMoneda(cotizacionCrmRequestDTO.getMoneda());
			beCotizacion.setMonedaPrima(cotizacionCrmRequestDTO.getMonedaPrima());
			beCotizacion.setMontoPrima(cotizacionCrmRequestDTO.getMontoPrima());
			beCotizacion.setNivelAgrupReaseg(cotizacionCrmRequestDTO.getNivelAgrupReaseg());
			beCotizacion.setNombrePlan(cotizacionCrmRequestDTO.getNombrePlan());
			beCotizacion.setNombreProducto(cotizacionCrmRequestDTO.getNombreProducto());
			beCotizacion.setNumeroCotizacion(cotizacionCrmRequestDTO.getNumeroCotizacion());
			beCotizacion.setNumeroDocumento(cotizacionCrmRequestDTO.getNumeroDocumento());
			beCotizacion.setOpcionTD(cotizacionCrmRequestDTO.getOpcionTD());
			beCotizacion.setOpcionVida(cotizacionCrmRequestDTO.getOpcionVida());
			beCotizacion.setPlanVida(cotizacionCrmRequestDTO.getPlanVida());
			beCotizacion.setPrimaFormaPago(cotizacionCrmRequestDTO.getPrimaFormaPago());
			beCotizacion.setProfesionActividad(cotizacionCrmRequestDTO.getProfesionActividad());
			beCotizacion.setTipoDescuento(cotizacionCrmRequestDTO.getTipoDescuento());
			beCotizacion.setTipoDocumento(cotizacionCrmRequestDTO.getTipoDocumento());
			beCotizacion.setTipoMoneda(cotizacionCrmRequestDTO.getTipoMoneda());
			beCotizacion.setTipoVigencia(cotizacionCrmRequestDTO.getTipoVigencia());
			beCotizacion.setVigencia(cotizacionCrmRequestDTO.getVigencia());
			beCotizacion.setPlanFinanciamiento(new CrmPlanFinanciamiento());
			beCotizacion.getPlanFinanciamiento()
					.setIdPFConf(cotizacionCrmRequestDTO.getPlanFinanciamiento().getIdPFConf());
			beCotizacion.getPlanFinanciamiento().setMoneda(cotizacionCrmRequestDTO.getPlanFinanciamiento().getMoneda());
			beCotizacion.getPlanFinanciamiento().setNombrePlanFinanciamiento(
					cotizacionCrmRequestDTO.getPlanFinanciamiento().getNombrePlanFinanciamiento());

			List<CrmUnidadDeRiesgo> urb = new ArrayList<CrmUnidadDeRiesgo>();
			for (CotizadorCrmUnidadRiesgo ur : cotizacionCrmRequestDTO.getUnidadesDeRiesgo()) {
				CrmUnidadDeRiesgo urt = new CrmUnidadDeRiesgo();
				urt.setCapitalAsegurado(ur.getCapitalAsegurado());
				urt.setIdURConf(ur.getIdURConf());
				urt.setNombreUR(ur.getNombreUR());
				urt.setNumeroAnualidades(ur.getNumeroAnualidades());
				urt.setNumUnidRiesgo(ur.getNumUnidRiesgo());
				urt.setOpcionVida(ur.getOpcionVida());
				urt.setPeriodoPagoPrima(ur.getPeriodoPagoPrima());
				urt.setPeriodoPagoPrimaVE(ur.getPeriodoPagoPrimaVE());
				urt.setPlanVida(ur.getPlanVida());
				urt.setProductosVida(ur.getProductosVida());
				urt.setVigenciaEG(ur.getVigenciaEG());
				urt.setObjetosAsegurados(new ArrayList<CrmObjetoAsegurado>());
				for (CotizadorCrmObjetoAseguradoDTO oba : ur.getObjetosAsegurados()) {
					CrmObjetoAsegurado obat = new CrmObjetoAsegurado();
					obat.setDescripcionIO(oba.getDescripcionIO());
					obat.setGrupoFamiliar(oba.getGrupoFamiliar());
					obat.setIdIOConf(oba.getIdIOConf());
					obat.setNumeroObjAseg(oba.getNumeroObjAseg());
					obat.setTipoAseguradoAcc(oba.getTipoAseguradoAcc());
					obat.setCoberturas(new ArrayList<>());
					obat.setParticipaciones(new ArrayList<CrmParticipacion>());
					for (CotizadorCrmCoberturaDTO cob : oba.getCoberturas()) {
						CrmCobertura cobt = new CrmCobertura();
						cobt.setCovBeneficioMaximo(cob.getCovBeneficioMaximo());
						cobt.setIdCovConf(cob.getIdCovConf());
						cobt.setNombreCov(cob.getNombreCov());
						obat.getCoberturas().add(cobt);
					}
					for (CotizadorCrmParticipacionDTO par : oba.getParticipaciones()) {
						CrmParticipacion part = new CrmParticipacion();
						part.setIdDireccion(par.getIdDireccion());
						part.setIdRol(par.getIdRol());
						part.setIdTercero(par.getIdTercero());
						part.setNumeroDocumento(par.getNumeroDocumento());
						obat.getParticipaciones().add(part);
					}
					urt.getObjetosAsegurados().add(obat);
				}
				urb.add(urt);
			}
			beCotizacion.setUnidadesDeRiesgo(urb);
			request.setBeCotizacion(beCotizacion);

			Gson gson = new Gson();
			LOGGER.info(gson.toJson(request));
			observaciones.add(gson.toJson(request));

			CrmCotizacionResponse response = crmRestClient.guardarCotizacionCrm(request);
			if (response.getStatusHttp() == "200") {
				result.setCodigoRespuesta(response.getRs() ? "01" : "99");
				result.setMensajeRespuesta(response.getMsg());
				result.setIdCotizacionCRM(response.getIdCotizacionCRM());
			} else {
				result.setCodigoRespuesta("99");
				result.setMensajeRespuesta(
						"[" + response.getCode() + "] Error al intentar registrar cotizacion en CRM");
				result.setObjErrorResource(new ErrorResourceDTO(response.getCode(), response.getMessage()));
			}
			LOGGER.info("Resultado cotizacion=====>");
			LOGGER.info(gson.toJson(response));
			observaciones.add("Response CRM: " + gson.toJson(response));

			LOGGER.info("INI - Intentando actualizar cotizacion ===>" + cotizacionCrmRequestDTO.getNumeroCotizacion());
			LOGGER.info(gson.toJson(result));
			cotizacionRepository.guardarIDCotizacionCRM(Integer.parseInt(cotizacionCrmRequestDTO.getNumeroCotizacion()),
					response.getIdCotizacionCRM());
			LOGGER.info("FIN - Intentando actualizar cotizacion");

		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * logRepository.save(new EventLog("Cotizador", result.getCodigoRespuesta(),
		 * "/cotizador/crm/cotiza", result.getMensajeRespuesta(),
		 * gson.toJson(result.getObjErrorResource()), gson.toJson(observaciones), "App",
		 * "", ""));
		 */
		LOGGER.info("guardarCotizacionCrm.NEXT=>" + result.toString());
		return result;
	}

	@Override
	public ProcesoResponseDTO forzarRecotizacion(List<String> cotizaciones) {
		LOGGER.info("Entro CotizaDetalleResponseDTO#forzarRecotizacion(List<String> cotizaciones)");
		ProcesoResponseDTO response = new ProcesoResponseDTO();
		List<String> exitos = new ArrayList<String>();
		List<String> errores = new ArrayList<String>();
		if (cotizaciones != null) {
			for (String cotizacion : cotizaciones) {
				try {
					ConsultaCotizacionDetalleRequest request0 = new ConsultaCotizacionDetalleRequest();
					request0.setIdAgente("");
					request0.setNumeroCotizacion(cotizacion);
					LOGGER.info("=== OBTENIENDO COTIZACION VIDA FREE ===" + cotizacion);
					ConsultaCotizacionDetalleResponse response0 = vidaFreeRestClient.obtenerCotizacionDetalle(request0);
					CotizacionCrmRequestDTO cotizacionCrmRequestDTO = new CotizacionCrmRequestDTO();

					LOGGER.info("=== OBTENIENDO ID CRM COTIZACION ===" + cotizacion);
					Long nroCotizacionL = Long.parseLong(cotizacion);
					List<CotizacionDetalle> cotizacionVida = cotizacionRepository.detalleCotizacion2(nroCotizacionL);

					LOGGER.info("=== OBTENIENDO ADN ===" + cotizacion);
					Adn adn = adnRepository.findByTipoNumeroDocumento(
							response0.getData().getCliente().getTipoDocumento(),
							response0.getData().getCliente().getNumeroDocumento());

					LOGGER.info("=== LLENANDO DATA COTIZACION ===" + cotizacion);
					cotizacionCrmRequestDTO.setIdCRM(cotizacionVida.get(0).getCrmCotizadorId());
					cotizacionCrmRequestDTO.setIdOportunidadCRM(adn.getIdOportunidadCrm());
					CotizadorCrmContactoDTO contacto = new CotizadorCrmContactoDTO();
					contacto.setIdCRM(response0.getData().getAgente().getId());
					cotizacionCrmRequestDTO.setBeContacto(contacto);
					cotizacionCrmRequestDTO.setIdProducto("77909");
					cotizacionCrmRequestDTO.setNombreProducto("VidaFree");
					cotizacionCrmRequestDTO.setIdPlan(response0.getData().getDetalleCotizacion().getCodPlanAcsele());
					String planVidaOpcional = "";
					if ("2129".equals(response0.getData().getDetalleCotizacion().getCodPlanAcsele())) {
						planVidaOpcional = "Plan Vida Free";
					}
					if ("2130".equals(response0.getData().getDetalleCotizacion().getCodPlanAcsele())) {
						planVidaOpcional = "Plan Vida Plus";
					}
					if ("2131".equals(response0.getData().getDetalleCotizacion().getCodPlanAcsele())) {
						planVidaOpcional = "Plan Vida Total";
					}
					cotizacionCrmRequestDTO.setNombrePlan("PlanVidaFree");
					String vigencia = Strings.EMPTY;
					switch (response0.getData().getDetalleCotizacion().getPeriodoVigencia()) {
					case 10:
						vigencia = "Diez Años";
						break;
					case 11:
						vigencia = "Once Años";
						break;
					case 12:
						vigencia = "Doce Años";
						break;
					case 13:
						vigencia = "Trece Años";
						break;
					case 14:
						vigencia = "Catorce Años";
						break;
					case 15:
						vigencia = "Quince Años";
						break;
					case 16:
						vigencia = "Dieciseis Años";
						break;
					case 17:
						vigencia = "Diecisiete Años";
						break;
					case 18:
						vigencia = "Dieciocho Años";
						break;
					case 19:
						vigencia = "Diecinueve Años";
						break;
					case 20:
						vigencia = "Veinte Años";
						break;
					case 21:
						vigencia = "Veintiun Años";
						break;
					case 22:
						vigencia = "Veintidos Años";
						break;
					case 23:
						vigencia = "Veintitres Años";
						break;
					case 24:
						vigencia = "Veinticuatro Años";
						break;
					case 25:
						vigencia = "Veinticinco Años";
						break;
					case 26:
						vigencia = "Veintiseis Años";
						break;
					case 27:
						vigencia = "Veintisiete Años";
						break;
					case 28:
						vigencia = "Veintiocho Años";
						break;
					case 29:
						vigencia = "Veintinueve Años";
						break;
					case 30:
						vigencia = "Treinta Años";
						break;
					case 31:
						vigencia = "Treinta y Uno Años";
						break;
					case 32:
						vigencia = "Treinta y Dos Años";
						break;
					case 33:
						vigencia = "Treinta y Tres Años";
						break;
					case 34:
						vigencia = "Treinta y Cuatro Años";
						break;
					case 35:
						vigencia = "Treinta y Cinco Años";
						break;
					case 36:
						vigencia = "Treinta y Seis Años";
						break;
					case 37:
						vigencia = "Treinta y Siete Años";
						break;
					case 38:
						vigencia = "Treinta y Ocho Años";
						break;
					case 39:
						vigencia = "Treinta y Nueve Años";
						break;
					case 40:
						vigencia = "Cuarenta Años";
						break;
					case 41:
						vigencia = "Cuarenta y Uno Años";
						break;
					case 42:
						vigencia = "Cuarenta y Dos Años";
						break;
					case 43:
						vigencia = "Cuarenta y Tres Años";
						break;
					case 44:
						vigencia = "Cuarenta y Cuatro Años";
						break;
					case 45:
						vigencia = "Cuarenta y Cinco Años";
						break;
					case 46:
						vigencia = "Cuarenta y Seis Años";
						break;
					case 47:
						vigencia = "Cuarenta y Siete Años";
						break;
					case 48:
						vigencia = "Cuarenta y Ocho Años";
						break;
					case 49:
						vigencia = "Cuarenta y Nueve Años";
						break;
					case 50:
						vigencia = "Cincuenta Años";
						break;
					case 51:
						vigencia = "Cincuenta y Uno Años";
						break;
					case 52:
						vigencia = "Cincuenta y Dos Años";
						break;
					case 53:
						vigencia = "Cincuenta y Tres Años";
						break;
					case 54:
						vigencia = "Cincuenta y Cuatro Años";
						break;
					case 55:
						vigencia = "Cincuenta y Cinco Años";
						break;
					case 56:
						vigencia = "Cincuenta y Seis Años";
						break;
					case 57:
						vigencia = "Cincuenta y Siete Años";
						break;
					case 58:
						vigencia = "Cincuenta y Ocho Años";
						break;
					case 59:
						vigencia = "Cincuenta y Nueve Años";
						break;
					}
					cotizacionCrmRequestDTO.setVigencia(vigencia);
					String fechaCotizacion = DateUtil.getDateWithFormat(
							response0.getData().getDetalleCotizacion().getFechaCotizacion(),
							DateUtil.FORMATO_DIA_DDMMYYYY, DateUtil.FORMATO_DIA_YYYYMMDD2);
					String fechaVigencia = DateUtil.getDateWithFormat(
							response0.getData().getDetalleCotizacion().getFechaVigencia(),
							DateUtil.FORMATO_DIA_DDMMYYYY, DateUtil.FORMATO_DIA_YYYYMMDD2);
					cotizacionCrmRequestDTO.setFechaInicial(fechaCotizacion);
					cotizacionCrmRequestDTO.setFechaFinal(fechaVigencia);
					cotizacionCrmRequestDTO.setTipoDocumento("DocumentoIdentidad");
					cotizacionCrmRequestDTO.setNumeroDocumento(response0.getData().getCliente().getNumeroDocumento());
					cotizacionCrmRequestDTO.setMontoPrima(
							response0.getData().getDetalleCotizacion().getPrimaClienteAnualTotal().toString());
					cotizacionCrmRequestDTO.setPrimaFormaPago(
							response0.getData().getDetalleCotizacion().getPrimaRecurrenteTotal().toString());
					cotizacionCrmRequestDTO
							.setNumeroCotizacion(response0.getData().getDetalleCotizacion().getNumeroCotizacion());
					cotizacionCrmRequestDTO.setPlanVida(response0.getData().getDetalleCotizacion().getNombrePlan());
					cotizacionCrmRequestDTO.setOpcionVida(planVidaOpcional);
					cotizacionCrmRequestDTO
							.setTipoVigencia(response0.getData().getDetalleCotizacion().getFrecuenciaPagoDescripcion());
					cotizacionCrmRequestDTO.setFechaMovimiento(fechaCotizacion);
					cotizacionCrmRequestDTO
							.setTipoMoneda(response0.getData().getDetalleCotizacion().getCodMonedaAcsele());
					cotizacionCrmRequestDTO.setMoneda(response0.getData().getDetalleCotizacion().getCodMonedaAcsele());
					cotizacionCrmRequestDTO
							.setMonedaPrima(response0.getData().getDetalleCotizacion().getCodMonedaAcsele());
					cotizacionCrmRequestDTO.setFrecuenciaPago(
							response0.getData().getDetalleCotizacion().getFrecuenciaPagoDescripcion());
					cotizacionCrmRequestDTO.setAplicaDescuento("NO");
					cotizacionCrmRequestDTO.setTipoDescuento("");
					cotizacionCrmRequestDTO.setProfesionActividad("Administrador");
					cotizacionCrmRequestDTO.setGrupoFamiliar("");
					cotizacionCrmRequestDTO.setClaseAccPers("Clase I");
					cotizacionCrmRequestDTO.setOpcionTD("OTD");
					cotizacionCrmRequestDTO.setNivelAgrupReaseg("1");
					CotizadorCrmPlanFinanciamientoDTO planFinanciamiento = new CotizadorCrmPlanFinanciamientoDTO();
					if ("Mensual".equals(response0.getData().getDetalleCotizacion().getFrecuenciaPagoDescripcion())) {
						planFinanciamiento.setIdPFConf("84531");
					}
					if ("Anual".equals(response0.getData().getDetalleCotizacion().getFrecuenciaPagoDescripcion())) {
						planFinanciamiento.setIdPFConf("84534");
					}
					if ("Semestral".equals(response0.getData().getDetalleCotizacion().getFrecuenciaPagoDescripcion())) {
						planFinanciamiento.setIdPFConf("84533");
					}
					if ("Trimestral"
							.equals(response0.getData().getDetalleCotizacion().getFrecuenciaPagoDescripcion())) {
						planFinanciamiento.setIdPFConf("84532");
					}
					planFinanciamiento.setMoneda(response0.getData().getDetalleCotizacion().getCodMonedaAcsele());
					planFinanciamiento.setNombrePlanFinanciamiento(
							response0.getData().getDetalleCotizacion().getFrecuenciaPagoDescripcion());
					cotizacionCrmRequestDTO.setPlanFinanciamiento(planFinanciamiento);
					List<CotizadorCrmUnidadRiesgo> unidadesDeRiesgo = new ArrayList<>();
					CotizadorCrmUnidadRiesgo ur = new CotizadorCrmUnidadRiesgo();
					ur.setIdURConf("50310828");
					ur.setNombreUR("");
					ur.setNumUnidRiesgo("1");
					ur.setProductosVida("Seguro " + response0.getData().getDetalleCotizacion().getNombreProducto());
					ur.setPlanVida(planVidaOpcional);
					ur.setOpcionVida(response0.getData().getDetalleCotizacion().getNombrePlan());
					ur.setCapitalAsegurado(response0.getData().getDetalleCotizacion().getCoberturas().get(0)
							.getSumaAsegurada().toString());
					ur.setPeriodoPagoPrima(response0.getData().getDetalleCotizacion().getPeriodoPago().toString());
					ur.setPeriodoPagoPrimaVE(response0.getData().getDetalleCotizacion().getPeriodoPago().toString());
					ur.setNumeroAnualidades("0");
					ur.setVigenciaEG(response0.getData().getDetalleCotizacion().getPeriodoPago().toString());
					List<CotizadorCrmObjetoAseguradoDTO> objetosAsegurados = new ArrayList<CotizadorCrmObjetoAseguradoDTO>();
					CotizadorCrmObjetoAseguradoDTO obj = new CotizadorCrmObjetoAseguradoDTO();
					obj.setIdIOConf("93394");
					obj.setNumeroObjAseg("1");
					obj.setDescripcionIO("");
					obj.setGrupoFamiliar("");
					obj.setTipoAseguradoAcc("PRINCIPAL");
					ArrayList<CotizadorCrmCoberturaDTO> coberturas = new ArrayList<CotizadorCrmCoberturaDTO>();
					for (CoberturaResponse cob : response0.getData().getDetalleCotizacion().getCoberturas()) {
						String codigoCobertura = Strings.EMPTY;
						if ("49299361".equals(cob.getCodCoberturaAcsele())) {
							codigoCobertura = "49748112";
						}
						if ("49299358".equals(cob.getCodCoberturaAcsele())) {
							codigoCobertura = "49748109";
						}
						if ("49299359".equals(cob.getCodCoberturaAcsele())) {
							codigoCobertura = "49748110";
						}
						if ("49299356".equals(cob.getCodCoberturaAcsele())) {
							codigoCobertura = "49748107";
						}
						if ("49299360".equals(cob.getCodCoberturaAcsele())) {
							codigoCobertura = "49748111";
						}
						if ("49299357".equals(cob.getCodCoberturaAcsele())) {
							codigoCobertura = "49748108";
						}
						if ("49299362".equals(cob.getCodCoberturaAcsele())) {
							codigoCobertura = "49748113";
						}
						CotizadorCrmCoberturaDTO temp = new CotizadorCrmCoberturaDTO();
						temp.setIdCovConf(cob.getCodCoberturaAcsele());
						temp.setNombreCov(cob.getNombreCobertura().toUpperCase());
						temp.setCovBeneficioMaximo(cob.getSumaAsegurada().toString());
						coberturas.add(temp);
					}
					obj.setCoberturas(coberturas);
					ArrayList<CotizadorCrmParticipacionDTO> participaciones = new ArrayList<CotizadorCrmParticipacionDTO>();
					CotizadorCrmParticipacionDTO part = new CotizadorCrmParticipacionDTO();
					part.setIdTercero("");
					part.setNumeroDocumento(response0.getData().getCliente().getNumeroDocumento());
					part.setIdRol("");
					participaciones.add(part);
					obj.setParticipaciones(participaciones);

					objetosAsegurados.add(obj);
					ur.setObjetosAsegurados(objetosAsegurados);
					unidadesDeRiesgo.add(ur);
					cotizacionCrmRequestDTO.setUnidadesDeRiesgo(unidadesDeRiesgo);
					CotizacionCrmResponseDTO r = this.guardarCotizacionCrm(cotizacionCrmRequestDTO);
					Gson gson = new Gson();
					LOGGER.info("====Respuesta CRM====");
					LOGGER.info(gson.toJson(r));
					if (r.getIdCotizacionCRM() != null) {
						exitos.add("Se actualizó cotizacion " + cotizacion + ", idCrmCotizacion: "
								+ r.getIdCotizacionCRM());
						// response.setCodigoRespuesta("01");
						// response.setMensajeRespuesta("Se actualizó cotizacion " + cotizacion + ",
						// idCrmCotizacion: " + r.getIdCotizacionCRM());
						// LOGGER.info("Cotizacion actualizada ====> " + cotizacion + " ===> " +
						// r.getIdCotizacionCRM());
					} else {
						errores.add(cotizacion);
						// response.setCodigoRespuesta("99");
						// response.setMensajeRespuesta(r.getMensajeRespuesta());
					}
				} catch (Exception e) {
					errores.add(cotizacion);
					e.printStackTrace();
					LOGGER.error(e.getMessage());
					// LOGGER.info("Fallo cotizacion ===>" + cotizacion);
					// response.setCodigoRespuesta("99");
					// response.setMensajeRespuesta("Error al intentar actualizar cotización " +
					// cotizacion);
				}
			}
			response.setCodigoRespuesta("01");
			response.setMensajeRespuesta("OK");
			response.setExitos(exitos);
			response.setErrores(errores);
		} else {
			response.setCodigoRespuesta("99");
			response.setMensajeRespuesta("No hay datos");
		}

		LOGGER.info("Salio CotizaDetalleResponseDTO#forzarRecotizacion(List<String> cotizaciones)");
		return response;
	}

	@Override
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
			ConsultaCotizacionDetalleResponse cotizacionVF = cotizadorFreeRestClient.obtenerCotizacionDetalle(request);

			response.setNroCotizacion(cotizacionVF.getData().getDetalleCotizacion().getNumeroCotizacion());
			response.setFechaCotizacion(DateUtil.stringToDate(
					cotizacionVF.getData().getDetalleCotizacion().getFechaCotizacion(), DateUtil.FORMATO_DIA_DDMMYYYY));
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

	public ByteArrayOutputStream generarPDFCotizacionVidaFree(String nroCotizacion) {
		long startTime = System.nanoTime();
		long endTime;
		String traza = Utilitarios.trazaLog() + "-" + nroCotizacion;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			Solicitud solicitud = solicitudRepository.findByNumeroCotizacion(nroCotizacion);
			List<DocumentoCotizacionCobertura> coberturas = new ArrayList<>();
			List<DocumentoCotizacionRequerimiento> requerimientos = new ArrayList<>();
			List<DocumentoCotizacionDevolucion> devoluciones = new ArrayList<>();

			ConsultaCotizacionDetalleRequest request = new ConsultaCotizacionDetalleRequest();
			request.setNumeroCotizacion(nroCotizacion);
			request.setIdAgente("xxx");
			LOGGER.info("{} RES {}", traza, gson.toJson(request));
			ConsultaCotizacionDetalleResponse cotizacionVF = cotizadorFreeRestClient.obtenerCotizacionDetalle(request);
			LOGGER.info("{} cotizacionVF {}", traza, (cotizacionVF));
			AtomicReference<Double> capitalPolizaSoles = new AtomicReference<>(0.0);
			AtomicReference<Double> capitalPolizaDolares = new AtomicReference<>(0.0);
			double cumuloDolares = 0.0;
			double capitalDolares = 0.0;

			LOGGER.info("{} cotizacionVF {}", traza, gson.toJson(cotizacionVF));

			cotizacionVF.getData().getDetalleCotizacion().getCoberturas().stream()
					.filter(cob -> StringUtils.equals(cob.getNombreCobertura(), "Fallecimiento")).forEach(i -> {
						if (Constantes.MONEDA_ADN_SOLES
								.equals(cotizacionVF.getData().getDetalleCotizacion().getCodMoneda())) {
							capitalPolizaSoles.set(i.getSumaAsegurada());
						} else {
							capitalPolizaDolares.set(i.getSumaAsegurada());
						}
					});

			ConversionResponseDTO tipocambio = serviceFactory.getCotizaService().obtenerTipoCambio();

			Optional<Persona> persona = personaRepository.findById(solicitud.getIdAsegurado());
			if (!persona.isPresent()) {
				throw new NullPointerException("Persona NOT FOUND");
			}

			double factorSoles = 0.0;
			TipoCambioResponseDTO tipoCambioCumulo = this.obtenerTipoCambioAcsele();
			if (tipoCambioCumulo.getCodigoRespuesta().equals(CODIGO_RESPUESTA_GENERAL_EXITO)) {
				factorSoles = tipoCambioCumulo.getConversiones().stream()
						.filter(c -> Constantes.MONEDA_ACSELE_SOLES.equals(c.getMonedaOrigen()))
						.flatMapToDouble(x -> DoubleStream.of(x.getValor())).findFirst().orElse(0.0);

			}
			capitalDolares = capitalPolizaDolares.get() + (capitalPolizaSoles.get() * factorSoles);
			LOGGER.info("{} capitalTotal: {}", traza, capitalDolares);

			ObtenerCumuloResponse cumulor = cotizadorRestClient.obtenerCumuloGeneral(persona.get().getNumeroDocumento(),persona.get().getTipoDocumento());
			AtomicReference<Double> cumuloPolizaSoles = new AtomicReference<>(0.0);
			AtomicReference<Double> cumuloPolizaDolares = new AtomicReference<>(0.0);
			if(flagCumulo.equals("1")){
				if (cumulor.getStatusHttp().equals("200") && cumulor.getMontosdet() != null) {
					cumulor.getMontosdet().forEach(i -> {
						if (Constantes.MONEDA_ACSELE_SOLES.equals(i.getIdmoneda())) {
							cumuloPolizaSoles.set(Double.parseDouble(i.getMonto()));
						} else {
							cumuloPolizaDolares.set(Double.parseDouble(i.getMonto()));
						}
					});

					// Double cumuloSoles = obtenerCumuloSoles + obtenerCumuloDolares *
					// factorDolares
					cumuloDolares = (cumuloPolizaDolares.get() + (cumuloPolizaSoles.get()) * factorSoles);
					LOGGER.info("{} cumuloTotal: {}", traza, cumuloPolizaDolares);
					capitalDolares = (capitalPolizaDolares.get() + (capitalPolizaSoles.get()) * factorSoles);
					LOGGER.info("{} capitalTotal: {}", traza, capitalDolares);

				}
				cumuloDolares = cumuloDolares + capitalDolares;
			}else if(flagCumulo.equals("2")){
				if (cumulor.getStatusHttp().equals("200") && cumulor.getMontosdet() != null) {
					cumulor.getMontosdet().forEach(i -> {
						if (Constantes.MONEDA_ACSELE_SOLES.equals(i.getIdmoneda())) {
							cumuloPolizaSoles.set(Double.parseDouble(i.getMonto()));
						} else {
							cumuloPolizaDolares.set(Double.parseDouble(i.getMonto()));
						}
					});

					// Double cumuloSoles = obtenerCumuloSoles + obtenerCumuloDolares *
					// factorDolares
					cumuloDolares = cumuloPolizaDolares.get();
					LOGGER.info("{} cumuloTotal: {}", traza, cumuloPolizaDolares);
					capitalDolares = cumuloPolizaDolares.get();
					LOGGER.info("{} capitalTotal: {}", traza, capitalDolares);

				}
				cumuloDolares = cumuloDolares + capitalDolares;
			}

			LOGGER.info("{} propducto: {}", traza,
					cotizacionVF.getData().getDetalleCotizacion().getCodProductoAcsele());
			LOGGER.info("{} moneda: 2123", traza);
			LOGGER.info("{} edad: {}", traza, cotizacionVF.getData().getCliente().getEdadContratacion());
			LOGGER.info("{} cumulo: {}", traza, cumuloDolares);
			LOGGER.info("{} capital1: {}", traza, cumuloDolares);
			LOGGER.info("{} capital2: {}", traza, capitalDolares);
			LOGGER.info("{} tipo de cambio: {}", traza, BigDecimal.valueOf(tipocambio.getValor()));
			
			List<ExigenciaMedica> exigenciasMedicas = this.getExigenciasMedicas(solicitud);

			if (!exigenciasMedicas.isEmpty()) {
				AtomicInteger counti = new AtomicInteger();
				exigenciasMedicas.forEach(i -> {
					requerimientos.add(new DocumentoCotizacionRequerimiento("" + (counti.get() + 1), i.getCodigo(),
							i.getDescripcion()));
					counti.getAndIncrement();
				});
			}

			ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
			templateResolver.setPrefix("templates/");
			templateResolver.setSuffix(".html");
			templateResolver.setTemplateMode(TemplateMode.HTML);

			TemplateEngine templateEngine = new TemplateEngine();
			templateEngine.setTemplateResolver(templateResolver);
			Context context = new Context(new Locale("es", "PE"));
			NumberFormat nf = NumberFormat.getInstance(new Locale("en", "US"));
			DecimalFormat df = new DecimalFormat("#,##0.00");
			context.setVariable("cotizacion", nroCotizacion);
			context.setVariable("nom_asegurable", cotizacionVF.getData().getCliente().getNombre().toUpperCase());
			context.setVariable("fec_nac", cotizacionVF.getData().getCliente().getFechaNacimiento());
			context.setVariable("sexo", cotizacionVF.getData().getCliente().getSexoDescripcion().toUpperCase());
			context.setVariable("documento", cotizacionVF.getData().getCliente().getNumeroDocumento());
			context.setVariable("producto",
					cotizacionVF.getData().getDetalleCotizacion().getNombreProducto().toUpperCase());
			context.setVariable("plan", cotizacionVF.getData().getDetalleCotizacion().getNombrePlan().toUpperCase());
			context.setVariable("per_cobertura",
					cotizacionVF.getData().getDetalleCotizacion().getPeriodoVigencia().toString());
			context.setVariable("per_pago_prima",
					cotizacionVF.getData().getDetalleCotizacion().getPeriodoPago().toString());
			context.setVariable("frecuencia_pago",
					cotizacionVF.getData().getDetalleCotizacion().getFrecuenciaPagoDescripcion().toUpperCase());
			context.setVariable("moneda",
					cotizacionVF.getData().getDetalleCotizacion().getMonedaDescripcion().toUpperCase());
			context.setVariable("moneda_simbolo",
					cotizacionVF.getData().getDetalleCotizacion().getMonedaDescripcion().equals("Nuevo Sol") ? "S/."
							: "$");
			context.setVariable("devolucion",
					cotizacionVF.getData().getDetalleCotizacion().getPorcentajeDevolucion().toString());

			String tot_devolucion = df.format(new BigDecimal(cotizacionVF.getData().getDetalleCotizacion().getMontoDevolucionTotal()));
			context.setVariable("tot_devolucion",tot_devolucion);
			context.setVariable("nom_agente", cotizacionVF.getData().getAgente().getNombre());
			context.setVariable("fec_cot", cotizacionVF.getData().getDetalleCotizacion().getFechaCotizacion());
			context.setVariable("fec_val_cot", cotizacionVF.getData().getDetalleCotizacion().getFechaVigencia());
			context.setVariable("tcea",
					StringUtils.isNotBlank(cotizacionVF.getData().getDetalleCotizacion().getTcea().toString())
							? cotizacionVF.getData().getDetalleCotizacion().getTcea().toString()
							: "0.00");

			cotizacionVF.getData().getDetalleCotizacion().getCoberturas().forEach(c -> {
				if ("1".equals(c.getCodCobertura())) {
					context.setVariable("cob_principal", c.getNombreCobertura());
					String cob_principal_monto = df.format(new BigDecimal(c.getSumaAsegurada()));
					context.setVariable("cob_principal_monto",cob_principal_monto);
					context.setVariable("cob_principal_tasa", c.getTasaAnual().toString());
					context.setVariable("cob_principal_edad_min", Utilitarios.valorString(c.getEdadMinimaIngreso()));
					context.setVariable("cob_principal_edad_max", Utilitarios.valorString(c.getEdadMaximaIngreso()));
					context.setVariable("cob_principal_permanencia",
							Utilitarios.valorString(c.getEdadMaximaPermanencia()));
					context.setVariable("cob_principal_vigencia", c.getPeriodoVigencia().toString());
					String cob_principal_prima_anual = df.format(new BigDecimal(c.getPrimaClienteAnual()));
					context.setVariable("cob_principal_prima_anual",cob_principal_prima_anual);

				} else {
					coberturas.add(new DocumentoCotizacionCobertura(
							c.getNombreCoberturaReporte() == null ? c.getNombreCobertura()
									: c.getNombreCoberturaReporte(),
							Utilitarios.formatoMiles(c.getSumaAsegurada(), Utilitarios.FORMATO_MILES_CON_DECIMAL),
							c.getTasaAnual().toString(), c.getEdadMinimaIngreso(), c.getEdadMaximaIngreso(),
							c.getEdadMaximaPermanencia(), c.getPeriodoVigencia().toString(),
							Utilitarios.formatoMiles(c.getPrimaClienteAnual(), Utilitarios.FORMATO_MILES_CON_DECIMAL)));

				}
			});
			for (DocumentoCotizacionCobertura doc : coberturas) {
				doc.setCapital(doc.getCapital().replace(".", "_").replace(",", ".").replace("_", ","));
				doc.setPrimaAnual(doc.getPrimaAnual().replace(".", "_").replace(",", ".").replace("_", ","));
			}
			context.setVariable("coberturas", coberturas);

			String prima_anual = df.format(new BigDecimal(cotizacionVF.getData().getDetalleCotizacion().getPrimaClienteAnualTotal()));
			context.setVariable("prima_anual",prima_anual);
			context.setVariable("factor_pago",cotizacionVF.getData().getDetalleCotizacion().getFactorPago().substring(0, 5));
			String prima_comercial = df.format(new BigDecimal(cotizacionVF.getData().getDetalleCotizacion().getPrimaRecurrenteTotal()
					- cotizacionVF.getData().getDetalleCotizacion().getIgv()));
			context.setVariable("prima_comercial",prima_comercial);
			context.setVariable("igv", cotizacionVF.getData().getDetalleCotizacion().getIgv().toString());
			String prima_comercial_total = df.format(new BigDecimal(cotizacionVF.getData().getDetalleCotizacion().getPrimaRecurrenteTotal()));
			context.setVariable("prima_comercial_total",prima_comercial_total);

			context.setVariable("requerimientos", requerimientos);

			DateTimeFormatter formatters = DateTimeFormatter.ofPattern("d/MM/uuuu");
			String fechaCotizacion = cotizacionVF.getData().getDetalleCotizacion().getFechaCotizacion();
			context.setVariable("fec_termino", LocalDate.parse(fechaCotizacion, formatters)
					.plusYears(cotizacionVF.getData().getDetalleCotizacion().getPeriodoVigencia()).format(formatters));
			String capitalAsegurado = "0";
			if(!cotizacionVF.getData().getDetalleCotizacion().getCoberturas().isEmpty()){
				String x = df.format(new BigDecimal(cotizacionVF.getData().getDetalleCotizacion().getCoberturas().get(0).getSumaAsegurada()));
				capitalAsegurado=x;
			}
			context.setVariable("cap_asegurado", capitalAsegurado);
			context.setVariable("edad", cotizacionVF.getData().getCliente().getEdadContratacion());

			if (!cotizacionVF.getData().getDetalleCotizacion().getTablaDevolucion().isEmpty()) {
				cotizacionVF.getData().getDetalleCotizacion().getTablaDevolucion().forEach(t -> {
					devoluciones.add(new DocumentoCotizacionDevolucion(t.getAnio(),
							Utilitarios.formatoMiles((Double.parseDouble(t.getDevEstandar()) * 100),
									Utilitarios.FORMATO_MILES_CON_DECIMAL).concat("%"),
							Utilitarios.formatoMiles((Double.parseDouble(t.getDevCliente()) * 100),
									Utilitarios.FORMATO_MILES_CON_DECIMAL).concat("%"),
							Utilitarios.formatoMiles((Double.parseDouble(t.getDevolucion()) * 100),
									Utilitarios.FORMATO_MILES_CON_DECIMAL).concat("%")));
				});
			}
			for (DocumentoCotizacionDevolucion doc : devoluciones) {
				doc.setDevEstandar(doc.getDevEstandar().replaceAll(",","."));
				doc.setDevCliente(doc.getDevCliente().replaceAll(",","."));
				doc.setDevolucion(doc.getDevolucion().replaceAll(",","."));
			}
			List<DocumentoCotizacionDevolucion> devoluciones1 = devoluciones;
			List<DocumentoCotizacionDevolucion> devoluciones2 = new ArrayList<>();
			if (devoluciones.size() > 29) {
				devoluciones1 = devoluciones.subList(0, 29);
				devoluciones2 = devoluciones.subList(29, devoluciones.size());
			}
			context.setVariable("devoluciones", devoluciones1);
			context.setVariable("devoluciones_extra", devoluciones2);

			getBase64ImgStrg(traza, context);

			String htmlContent = templateEngine.process(PLANTILLA_COTIZADOR_VF_HTML, context);

			ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
			HtmlConverter.convertToPdf(htmlContent, pdfOutputStream);
			out = pdfOutputStream;

		} catch (Exception e) {
			LOGGER.error("{} Error===> {}", traza, e.getMessage());
		}
		endTime = System.nanoTime();
		LOGGER.info("{} END generarPDFCotizacionVidaFree {}", traza, (endTime - startTime));
		return out;
	}

	public GenerarPdfResponse generarPDFEdn(String nroCotizacion) {
		long startTime = System.nanoTime();
		long endTime;
		String traza = Utilitarios.trazaLog() + "-" + nroCotizacion;
		LOGGER.info("{} INICIO generarPDFEdn", traza);
		GenerarPdfResponse esnGenerarPdfResponse = new GenerarPdfResponse();
		try {
			Solicitud solicitud = solicitudRepository.findByNumeroCotizacion(nroCotizacion);
			LOGGER.info("{} generarPDFEdn - solicitud => {}", solicitud.getIdSolicitud());
			SolicitudProducto solicitudProducto = solicitudProductoRepository.findByIdSolicitud(solicitud.getIdSolicitud());
			Persona persona = personaRepository.findById(solicitud.getIdAsegurado()).get();
			ConsultaCotizacionDetalleRequest requestVF = new ConsultaCotizacionDetalleRequest();
			requestVF.setIdAgente("");
			requestVF.setNumeroCotizacion(solicitud.getNumeroCotizacion());
			DateFormat sourceFormat = new SimpleDateFormat(DateUtil.FORMATO_DIA_DDMMYYYY);
			CotizacionDetalle detallePG = new CotizacionDetalle();
			String fechaCotizacion = new String();
			ConsultaCotizacionDetalleResponse detalleVF = new ConsultaCotizacionDetalleResponse();
			if (solicitudProducto.getTipoProducto().equals("2")) {
				detalleVF = vidaFreeRestClient.obtenerCotizacionDetalle(requestVF);
				fechaCotizacion = detalleVF.getData().getDetalleCotizacion().getFechaCotizacion();
			}
			else if (solicitudProducto.getTipoProducto().equals("1")) {
				detallePG = cotizacionRepository.detalleCotizacion(Long.parseLong(solicitud.getNumeroPropuesta())).get(0);
				fechaCotizacion = sourceFormat.format(detallePG.getFechaCotizacion());
			}
			LOGGER.info("{} generarPDFEdn - Buscando idPersonaCD", traza);
			String carpetaCS = persona.getIdPersonaCS();
			LOGGER.info("{} generarPDFEdn -Fin Buscando idPersonaCD => {}", traza,carpetaCS);
			// obtener datos
			String planesFuturo = "";
			String ahorroDeseasRecibir = "";
			String frecuenciaPago = "";
			String porcentajeDevolucion = "";
			String montoMes = "";
			String aniosAsegurado = "";
			String aniosPago = "";

			if (solicitudProducto.getTipoProducto().equals("2")) {
				PlanFuturo planFuturoDomain = planFuturoRepository.findByIdPersona(persona.getIdPersona());

				List<String> planes = new ArrayList<>();
				if (planFuturoDomain.getPlanEducacion() == 1)
					planes.add("Educación");
				if (planFuturoDomain.getPlanProyecto() == 1)
					planes.add("Sueños");
				if (planFuturoDomain.getPlanJubilacion() == 1)
					planes.add("Jubilación");
				planesFuturo = StringUtils.join(planes, ',');

				aniosAsegurado = detalleVF.getData().getDetalleCotizacion().getPeriodoVigencia().toString();
				aniosPago = detalleVF.getData().getDetalleCotizacion().getPeriodoPago().toString();
				String moneda = detalleVF.getData().getDetalleCotizacion().getMonedaDescripcion()
						.equalsIgnoreCase("Nuevo Sol") ? "S/. " : "$ ";
				montoMes = moneda
						+ detalleVF.getData().getDetalleCotizacion().getPrimaRecurrenteTotal().toString();
				porcentajeDevolucion = detalleVF.getData().getDetalleCotizacion().getPorcentajeDevolucion()
						.toString() + "%";
				frecuenciaPago = detalleVF.getData().getDetalleCotizacion().getFrecuenciaPagoDescripcion();

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
				frecuenciaPago = detallePG.getFrecuenciaPagoPrima();
				String moneda = detallePG.getMoneda().equalsIgnoreCase("SOLES") ? "S/. " : "$ ";
				montoMes = moneda + String.valueOf(detallePG.getPrimaComercial());
				aniosAsegurado = String.valueOf(detallePG.getPeriodoCobertura());
				aniosPago = String.valueOf(Integer.parseInt(detallePG.getPer_cob()) / 12);
				porcentajeDevolucion = "No aplica";
				ahorroDeseasRecibir = "Fin de cobertura";
			}

			// consumir esn generar pdf
			GenerarPdfRequest esnGenerarPdfRequest = new GenerarPdfRequest();
			esnGenerarPdfRequest.setProducto("VIDA");
			esnGenerarPdfRequest.setRepositorioCS("CRM");
			esnGenerarPdfRequest.setCarpetaCS(carpetaCS.toLowerCase());
			esnGenerarPdfRequest.setSolicitud(solicitud.getNumeroPropuesta());
			esnGenerarPdfRequest.setNumerodocumento(persona.getNumeroDocumento());
			esnGenerarPdfRequest.setCodigoPlantillaEN("1");
			List<GenerarPdfCabeceras> generarPdfCabeceras = new ArrayList<>();
			GenerarPdfCabeceras generarPdfCabecera1 = new GenerarPdfCabeceras();
			generarPdfCabecera1.setCampo("CLIENTE");
			String nombreCliente = persona.getNombres();
			String apePatCliente = persona.getApellidoPaterno();
			String apeMatCliente = persona.getApellidoMaterno() == null ? "": persona.getApellidoMaterno();
			generarPdfCabecera1.setValor(nombreCliente + " " + apePatCliente + " " + apeMatCliente);
			GenerarPdfCabeceras generarPdfCabecera2 = new GenerarPdfCabeceras();
			generarPdfCabecera2.setCampo(persona.getTipoDocumento().equals("1") ? "DNI" : "CE");
			generarPdfCabecera2.setValor(persona.getNumeroDocumento());
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
			LOGGER.info("{} generarPDFEdn - INICIA GuardarServicioEdn request => {}", traza,gson.toJson(esnGenerarPdfRequest));
			String jsonRequest = gson.toJson(esnGenerarPdfRequest);
			OkHttpClient client = new OkHttpClient().newBuilder().build();
			okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType, jsonRequest);
			String credential = Credentials.basic(esnUser, esnPasword);
			ServicioEdn servicioEdn = new ServicioEdn();
			servicioEdn.setUsuario_crea(solicitud.getUsuarioCrea());
			servicioEdn.setFecha_crea(new Date());
			servicioEdn.setSolicitud(esnGenerarPdfRequest.getSolicitud());
			servicioEdn.setRequest(gson.toJson(esnGenerarPdfRequest));

			Request request = new Request.Builder().url(getUrlEsnGeneraPdf()).method("POST", body)
					.addHeader("Authorization", credential).addHeader("Content-Type", "application/json").build();
			Response response = client.newCall(request).execute();
			esnGenerarPdfResponse.setCode(String.valueOf(response.code()));
			esnGenerarPdfResponse.setStatusHttp(String.valueOf(response.code()));
			esnGenerarPdfResponse.setMessage(response.message());
			LOGGER.info("{} generarPDFEdn - FIN GuardarServicioEdn response => {}", traza,gson.toJson(response));

			if (response.code() == 200) {
				String rpta = response.body().string();
				esnGenerarPdfResponse.setData(rpta);
				servicioEdn.setFlag_edn("S");
				servicioEdn.setResponse(gson.toJson(esnGenerarPdfResponse));
				solicitud.setDocumentoEdn("1");
				LOGGER.info("Respuesta servicio: {}", gson.toJson(esnGenerarPdfResponse));
			} else {
				servicioEdn.setFlag_edn("N");
				solicitud.setDocumentoEdn("2");
			}
			LOGGER.info("{} generarPDFEdn - Guardando respuesta en tabla servicio_edn", traza);
			solicitudRepository.save(solicitud);
			servicioEdnRepository.save(servicioEdn);
		}
		catch (Exception e) {
			LOGGER.error("{} generarPDFEdn Error===> {}", traza, e.getMessage());
		}
		endTime = System.nanoTime();
		LOGGER.info("{} END generarPDFEdn {}", traza, (endTime - startTime));
		return esnGenerarPdfResponse;
	}

	private void getBase64ImgStrg(String traza, Context context) {
		String path = "templates/Logo_is.png";
		String imageString = "";
		ClassPathResource cpr = new ClassPathResource(path);
		try (InputStream in = cpr.getInputStream()) {
			byte[] fileContent = IOUtils.toByteArray(in);
			imageString = DatatypeConverter.printBase64Binary(fileContent);
		} catch (IOException e) {
			LOGGER.error("{} Error===> {}", traza, e.getMessage());
		}
		context.setVariable("logoImage", imageString);
	}

	@Override
	public ByteArrayOutputStream generarPDFCotizacionVida(String nroCotizacion, String agente, String asegurado) {
		SimpleDateFormat formatterDDMMYYY = new SimpleDateFormat("dd/MM/yyyy");
		ByteArrayInputStream pdfInputStream = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Solicitud solicitud = solicitudRepository.findByNumeroCotizacion(nroCotizacion);
		SolicitudExigenciasMedicas solicitudExigenciasMedicas = new SolicitudExigenciasMedicas();
		solicitudExigenciasMedicas.setSolicitud(solicitud);
		Calendar calendar = Calendar.getInstance();
		try {
			List<DocumentoCotizacionCobertura> coberturas = new ArrayList<>();
			List<DocumentoCotizacionRequerimiento> requerimientos = new ArrayList<>();
			List<DocumentoCotizacionDevolucion> devoluciones = new ArrayList<>();
			List<CotizacionDetalle> listaDetalle = new ArrayList<>();
			CotizacionDetalle cotizacionDetalle = new CotizacionDetalle();
			List<CotizacionCobertura> cotizacionCoberturas = new ArrayList<>();
			List<CotizacionVG> cotizacionVGS = new ArrayList<>();
			List<CotizacionPagBeneficio> cotizacionPagBeneficios = new ArrayList<CotizacionPagBeneficio>();

			listaDetalle = cotizacionRepository.detalleCotizacion(Long.parseLong(nroCotizacion));
			cotizacionDetalle = listaDetalle.get(0);

			Double capitalPolizaSoles = 0.0;
			Double capitalPolizaDolares = 0.0;
			Double cumuloDolares = 0.0;
			Double capitalDolares = 0.0;

			for (CotizacionDetalle bean : listaDetalle) {
				if (bean.getCoberturaNombre().equals("FALLECIMIENTO")
						|| bean.getCoberturaNombre().equals("VIDA ADICIONAL POR LA VIGENCIA DEL SEGURO")) {
					if (bean.getMoneda().equalsIgnoreCase("Soles")) {
						capitalPolizaSoles = capitalPolizaSoles + Double.valueOf(bean.getCoberturaCapital());
					} else {
						capitalPolizaDolares = capitalPolizaDolares + Double.valueOf(bean.getCoberturaCapital());
					}
				}
			}

			ConversionResponseDTO tipocambio = serviceFactory.getCotizaService().obtenerTipoCambio();

			List<Persona> persona = personaRepository.findAllById(Collections.singleton(solicitud.getIdAsegurado()));
			// obtener cumulo
			// Obteniendo ID Tercero
			String idTercero = "";
			String tipoDocumentoAcsele = persona.get(0).getTipoDocumento() == "2" ? "PersonaJuridica"
					: "PersonaNatural";
			System.out.println(tipoDocumentoAcsele);
			if (persona.get(0).getNumeroDocumento() != null) {
				TerceroResponse tercero = acseleRestClient.obtenerIDTercero(tipoDocumentoAcsele,
						persona.get(0).getNumeroDocumento());
				if (tercero.getStatusHttp() == "200" && tercero.getTerceros() != null
						&& tercero.getErrores().size() == 0) {
					idTercero = tercero.getTerceros().get(0).getIdTercero();
				}
			} else {
				System.out.println("No ha especificado un numero de documento...");
			}
			if (idTercero != "") {
				ObtenerCumuloResponse cumulor = cotizadorRestClient.obtenerCumuloGeneral(persona.get(0).getNumeroDocumento(),persona.get(0).getTipoDocumento());
				Double cumuloPolizaSoles = 0.0;
				Double cumuloPolizaDolares = 0.0;
				solicitudExigenciasMedicas.setResponseCumulo(gson.toJson(cumulor));
				if (cumulor.getStatusHttp() == "200" && cumulor.getMontosdet() != null) {
					for (int i = 0; i < cumulor.getMontosdet().size(); i++) {
						if (Constantes.MONEDA_ACSELE_SOLES.equals(cumulor.getMontosdet().get(i).getIdmoneda())) {
							cumuloPolizaSoles = Double.parseDouble(cumulor.getMontosdet().get(i).getMonto());
						} else {
							cumuloPolizaDolares = Double.parseDouble(cumulor.getMontosdet().get(i).getMonto());
						}
					}
					TipoCambioResponseDTO tipoCambioCumulo = this.obtenerTipoCambioAcsele();
					if (tipoCambioCumulo.getCodigoRespuesta() == Constantes.CODIGO_RESPUESTA_GENERAL_EXITO) {
						Double factorSoles = tipoCambioCumulo.getConversiones().stream()
								.filter(c -> Constantes.MONEDA_ACSELE_SOLES.equals(c.getMonedaOrigen())).findFirst()
								.get().getValor();
						solicitudExigenciasMedicas.setTipoCambio(String.valueOf(factorSoles));
						// Double cumuloSoles = obtenerCumuloSoles + obtenerCumuloDolares *
						// factorDolares;
						cumuloDolares = cumuloPolizaDolares + (cumuloPolizaSoles * factorSoles);
						System.out.println("cumuloTotal: " + cumuloPolizaDolares.toString());
						capitalDolares = capitalPolizaDolares + (capitalPolizaSoles * factorSoles);
						System.out.println("capitalTotal: " + capitalDolares.toString());
					}
				}
			} else {
				TipoCambioResponseDTO tipoCambio = this.obtenerTipoCambioAcsele();
				if (tipoCambio.getCodigoRespuesta() == Constantes.CODIGO_RESPUESTA_GENERAL_EXITO) {
					Double factorSoles = tipoCambio.getConversiones().stream()
							.filter(c -> Constantes.MONEDA_ACSELE_SOLES.equals(c.getMonedaOrigen())).findFirst().get()
							.getValor();
					solicitudExigenciasMedicas.setTipoCambio(String.valueOf(factorSoles));
					solicitudExigenciasMedicas.setResponseCumulo("");
					capitalDolares = capitalPolizaDolares + (capitalPolizaSoles * factorSoles);
					System.out.println("capitalTotal: " + capitalDolares.toString());
				}
			}
			solicitudExigenciasMedicas.setCumulo(String.valueOf(cumuloDolares));
			solicitudExigenciasMedicas.setCapital(String.valueOf(capitalDolares));
			cumuloDolares = cumuloDolares + capitalDolares;
			//

			LOGGER.info("propducto: " + "64899");
			LOGGER.info("moneda: " + "2123");
			LOGGER.info("edad: " + cotizacionDetalle.getEdad());
			LOGGER.info("cumulo: " + cumuloDolares.toString());
			LOGGER.info("capital1: " + cumuloDolares.toString());
			LOGGER.info("capital2: " + capitalDolares.toString());
			LOGGER.info("tipo de cambio: " + BigDecimal.valueOf(tipocambio.getValor()));
			solicitudExigenciasMedicas.setMoneda("2123");
			solicitudExigenciasMedicas.setCapitalTotal(String.valueOf(cumuloDolares));
			solicitudExigenciasMedicas.setEdad(cotizacionDetalle.getEdad());

			List<ExigenciaMedica> exigenciasMedicas = cotizacionRepository.exigenciasMedicas("64899", "2123",
					cotizacionDetalle.getEdad(), cumuloDolares.toString(), capitalDolares.toString(),
					capitalDolares.toString(), BigDecimal.valueOf(tipocambio.getValor()));
			solicitudExigenciasMedicas.setExamenesMedicos(gson.toJson(exigenciasMedicas));
			if (exigenciasMedicas != null && exigenciasMedicas.size() > 0) {
				for (int i = 0; i < exigenciasMedicas.size(); i++) {

					requerimientos.add(new DocumentoCotizacionRequerimiento("" + (i + 1),
							exigenciasMedicas.get(i).getCodigo(), exigenciasMedicas.get(i).getDescripcion()));
				}
			}
			solicitudExigenciasMedicas.setUsuarioCrea(solicitud.getUsuarioModif()!= null?solicitud.getUsuarioModif():solicitud.getUsuarioCrea());
			solicitudExigenciasMedicas.setFechaCrea(new Date());
			solicitudExigenciasMedicasRepository.save(solicitudExigenciasMedicas);
			// List<Persona> persona =
			// personaRepository.findAllById(Collections.singleton(solicitud.getIdAsegurado()));

			ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
			templateResolver.setPrefix("templates/");
			templateResolver.setSuffix(".html");
			templateResolver.setTemplateMode(TemplateMode.HTML);

			TemplateEngine templateEngine = new TemplateEngine();
			templateEngine.setTemplateResolver(templateResolver);
			System.out.println(Locale.getDefault());
			Context context = new Context(new Locale("es", "PE"));
			NumberFormat nf = NumberFormat.getInstance(new Locale("en", "US"));
			DecimalFormat df = new DecimalFormat("#,##0.00");
			context.setVariable("cotizacion", nroCotizacion);
			String nombre_asegurado = persona.get(0).getNombres() + " " + persona.get(0).getApellidoPaterno();
			String materno = persona.get(0).getApellidoMaterno() != null ? persona.get(0).getApellidoMaterno() : "";
			context.setVariable("nom_asegurable", nombre_asegurado + " " + materno);
			context.setVariable("fec_nac", String.valueOf(persona.get(0).getFechaNacimiento()));
			context.setVariable("sexo", persona.get(0).getGenero().equals("1") ? "MASCULINO" : "FEMENINO");
			context.setVariable("documento", (persona.get(0).getNumeroDocumento().length() == 8 ? "DNI" : "CE") + " "
					+ persona.get(0).getNumeroDocumento());
			context.setVariable("producto", "PLAN GARANTIZADO");
			context.setVariable("plan", cotizacionDetalle.getPlan().toUpperCase());
			context.setVariable("sub_plan", cotizacionDetalle.getSubplan().toUpperCase());
			context.setVariable("per_cobertura", String.valueOf(cotizacionDetalle.getPeriodoCobertura()));
			context.setVariable("per_pago_beneficio",
					String.valueOf(cotizacionDetalle.getNroAnualidadesPagoBeneficio()));
			context.setVariable("per_pago_prima", String.valueOf(cotizacionDetalle.getPeriodoCobertura()));
			context.setVariable("frecuencia_pago", cotizacionDetalle.getFrecuenciaPagoPrima().toUpperCase());
			context.setVariable("moneda", cotizacionDetalle.getMoneda().toUpperCase());
			context.setVariable("moneda_simbolo",
					cotizacionDetalle.getMoneda().equalsIgnoreCase("soles") ? "S/." : "$");
			context.setVariable("nom_agente", solicitud.getAgenteCorreo());
			context.setVariable("fec_cot", formatterDDMMYYY.format(cotizacionDetalle.getFechaCotizacion()));
			calendar.setTime(cotizacionDetalle.getFechaCotizacion());
			calendar.add(Calendar.DAY_OF_MONTH, 30);
			context.setVariable("fec_val_cot", formatterDDMMYYY.format(calendar.getTime()));
			context.setVariable("tcea", cotizacionDetalle.getTcea() != null ? cotizacionDetalle.getTcea() : "0.00");

			cotizacionCoberturas = cotizacionRepository.detalleCotizacionCoberturas(Integer.parseInt(nroCotizacion));
			Optional<CotizacionCobertura> cob = cotizacionCoberturas.stream().filter(c -> c.isLead() && !c.isBold())
					.findFirst();
			String montoPrincipal = df.format(new BigDecimal(Double.valueOf(cob.get().getCapital())));
			cotizacionCoberturas.forEach(c -> {
				if (c.isLead() && !c.isBold()) {
					try {
						context.setVariable("cob_principal", c.getNombre());
						String cob_principal_monto = df.format(new BigDecimal(Double.valueOf(c.getCapital())));
						context.setVariable("cob_principal_monto", cob_principal_monto);
						context.setVariable("cob_principal_tasa", c.getTasaAnual());
						context.setVariable("cob_principal_edad_min",
								c.getEdadMinIngreso() != null ? c.getEdadMinIngreso() : "");
						context.setVariable("cob_principal_edad_max",
								c.getEdadMaxIngreso() != null ? c.getEdadMaxIngreso() : "");
						context.setVariable("cob_principal_permanencia",
								c.getEdadMaxPermanencia() != null ? c.getEdadMaxPermanencia() : "");
						String cob_principal_prima_anual = df.format(new BigDecimal(Double.valueOf(c.getPriCob())));
						context.setVariable("cob_principal_prima_anual", cob_principal_prima_anual);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (!c.isLead() && !c.isBold()) {
					try {
						coberturas.add(new DocumentoCotizacionCobertura(c.getNombre(),
								Utilitarios.formatoMiles(Double.valueOf(c.getCapital()),
										Utilitarios.FORMATO_MILES_CON_DECIMAL),
								c.getTasaAnual(), c.getEdadMinIngreso(), c.getEdadMaxIngreso(),
								c.getEdadMaxPermanencia(), "xxx", Utilitarios.formatoMiles(
								Double.valueOf(c.getPriCob()), Utilitarios.FORMATO_MILES_CON_DECIMAL)));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			for (DocumentoCotizacionCobertura doc : coberturas) {
				doc.setCapital(doc.getCapital().replace(".", "_").replace(",", ".").replace("_", ","));
				doc.setPrimaAnual(doc.getPrimaAnual().replace(".", "_").replace(",", ".").replace("_", ","));
			}
			context.setVariable("coberturas", coberturas);

			String prima_anual = df.format(new BigDecimal(Double.valueOf(cotizacionDetalle.getPrimaComercialAnual())));
			context.setVariable("prima_anual", prima_anual);
			context.setVariable("factor_pago", cotizacionDetalle.getFactor());
			String prima_comercial = df.format(new BigDecimal((double) cotizacionDetalle.getPrimaComercial() - Double.parseDouble(cotizacionDetalle.getIgv())));
			context.setVariable("prima_comercial", prima_comercial);
			context.setVariable("igv", cotizacionDetalle.getIgv());
			context.setVariable("tir", Utilitarios.formatoMiles(Double.valueOf(cotizacionDetalle.getTirGarantizada()),
					Utilitarios.FORMATO_MILES_CON_DECIMAL).replace(",","."));
			String prima_comercial_total = df.format(new BigDecimal(Double.valueOf(cotizacionDetalle.getPrimaComTotal())));
			context.setVariable("prima_comercial_total", prima_comercial_total);
			context.setVariable("requerimientos", requerimientos);

			String capitalAsegurado = (cotizacionDetalle.getMoneda().equalsIgnoreCase("soles") ? "S/." : "$") + " "
					+ montoPrincipal;
			context.setVariable("cap_asegurado", capitalAsegurado);
			context.setVariable("nom_per_cob", cotizacionDetalle.getNomPerCob());
			context.setVariable("edad", cotizacionDetalle.getEdad());

			cotizacionVGS = cotizacionRepository.detalleCotizacionVg(Integer.parseInt(nroCotizacion));
			cotizacionPagBeneficios = cotizacionRepository.detalleCotizacionPagBen(Integer.parseInt(nroCotizacion));
			double fondoGarantizado = cotizacionPagBeneficios.stream().mapToDouble(CotizacionPagBeneficio::getMonto)
					.sum();
			context.setVariable("fondog", fondoGarantizado);
			context.setVariable("valores_garantizados", cotizacionVGS);
			context.setVariable("pago_beneficios", cotizacionPagBeneficios);

			String path = "templates/Logo_is.png";
			String imageString = "";
			ClassPathResource cpr = new ClassPathResource(path);
			try (InputStream in = cpr.getInputStream()) {
				byte[] fileContent = IOUtils.toByteArray(in);
				imageString = DatatypeConverter.printBase64Binary(fileContent);
			} catch (IOException e) {
				LOGGER.error("Error===>" + e.getMessage());
				e.printStackTrace();
			}
			context.setVariable("logoImage", imageString);

			String htmlContent = templateEngine.process(PLANTILLA_COTIZADOR_PG_HTML, context);

			ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
			HtmlConverter.convertToPdf(htmlContent, pdfOutputStream);
			out = pdfOutputStream;

		} catch (IOException e) {
			LOGGER.error("Error===>" + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			LOGGER.error("Error===>" + e.getMessage());
			throw new RuntimeException(e);
		}
		return out;
	}

	@Override
	public CotizadorCumuloResponseDTO obtenerCumulo(String tipoDocumento, String numeroDocumento) {
		String traza = Utilitarios.trazaLog();
		traza = traza + "-" + numeroDocumento;
		LOGGER.info("[" + traza + "] Entro CotizaServiceImpl#obtenerCumulo(tipoDocumento,numeroDocumento)");
		CotizadorCumuloResponseDTO response = new CotizadorCumuloResponseDTO();
		List<String> observaciones = new ArrayList<>();
			ObtenerCumuloResponse cumulor = cotizadorRestClient.obtenerCumuloGeneral(numeroDocumento, tipoDocumento);
			Double cumuloPolizaSoles = 0.0;
			Double cumuloPolizaDolares = 0.0;
		if(flagCumulo.equals("1")){
			if (cumulor.getStatusHttp() == "200" && cumulor.getMontosdet() != null) {
				for (int i = 0; i < cumulor.getMontosdet().size(); i++) {
					if (Constantes.MONEDA_ACSELE_SOLES.equals(cumulor.getMontosdet().get(i).getIdmoneda())) {
						cumuloPolizaSoles = Double.parseDouble(cumulor.getMontosdet().get(i).getMonto());
					} else {
						cumuloPolizaDolares = Double.parseDouble(cumulor.getMontosdet().get(i).getMonto());
					}
				}
				TipoCambioResponseDTO tipoCambio = this.obtenerTipoCambioAcsele();
				if (tipoCambio.getCodigoRespuesta() == Constantes.CODIGO_RESPUESTA_GENERAL_EXITO) {
					Double factorSoles = tipoCambio.getConversiones().stream()
							.filter(c -> Constantes.MONEDA_ACSELE_SOLES.equals(c.getMonedaOrigen())).findFirst().get()
							.getValor();
					Double factorDolares = tipoCambio.getConversiones().stream()
							.filter(c -> Constantes.MONEDA_ACSELE_DOLARES.equals(c.getMonedaOrigen())).findFirst().get()
							.getValor();
					Double cumuloSoles = cumuloPolizaSoles + cumuloPolizaDolares * factorDolares;
					Double cumuloDolares = cumuloPolizaDolares + cumuloPolizaSoles * factorSoles;

					response.setCumuloSoles(cumuloSoles);
					response.setCumuloDolares(cumuloDolares);
					response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
					response.setMensajeRespuesta("OK");
					observaciones.add("cumuloSoles: " + cumuloSoles + ", cumuloDolares: " + cumuloDolares);
				} else {
					response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
					response.setMensajeRespuesta(tipoCambio.getMensajeRespuesta());
					response.setObjErrorResource(tipoCambio.getObjErrorResource());
				}

			}
			else {
				response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR_FUNCIONAL);
				response.setMensajeRespuesta("Problemas al ejecutar de obtener cúmulo");
				response.setCumuloDolares(0.0);
				response.setCumuloSoles(0.0);
				observaciones.add("Problemas al invocar servicio de cumulo.");
				response.setObjErrorResource(new ErrorResourceDTO(cumulor.getCode(), cumulor.getMessage()));
			}
		}else if(flagCumulo.equals("2")){
			if (cumulor.getStatusHttp() == "200" && cumulor.getMontosdet() != null) {
				for (int i = 0; i < cumulor.getMontosdet().size(); i++) {
					if (Constantes.MONEDA_ACSELE_SOLES.equals(cumulor.getMontosdet().get(i).getIdmoneda())) {
						cumuloPolizaSoles = Double.parseDouble(cumulor.getMontosdet().get(i).getMonto());
					} else {
						cumuloPolizaDolares = Double.parseDouble(cumulor.getMontosdet().get(i).getMonto());
					}
				}
					response.setCumuloSoles(cumuloPolizaSoles);
					response.setCumuloDolares(cumuloPolizaDolares);
					response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
					response.setMensajeRespuesta("OK");
					observaciones.add("cumuloSoles: " + cumuloPolizaSoles + ", cumuloDolares: " + cumuloPolizaDolares);


			}
			else {
				response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR_FUNCIONAL);
				response.setMensajeRespuesta("Problemas al ejecutar de obtener cúmulo");
				response.setCumuloDolares(0.0);
				response.setCumuloSoles(0.0);
				observaciones.add("Problemas al invocar servicio de cumulo.");
				response.setObjErrorResource(new ErrorResourceDTO(cumulor.getCode(), cumulor.getMessage()));
			}
		}

		LOGGER.info("[" + traza + "] Salio CotizaServiceImpl#obtenerCumulo(tipoDocumento,numeroDocumento)");
		return response;
	}

	@Override
	public ConversionResponseDTO obtenerTipoCambio() {
		ConversionResponseDTO response = new ConversionResponseDTO();
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date(System.currentTimeMillis());
			response.setCodigoRespuesta("01");
			response.setMensajeRespuesta("Ok");
			response.setMonedaConversion(Constantes.MONEDA_COTIZADOR_DOLARES);
			response.setFecha(formatter.format(date));
			response.setValor(this.obtenerTipoCambioAcsele().getConversiones().stream()
					.filter(c -> Constantes.MONEDA_ACSELE_DOLARES.equals(c.getMonedaOrigen())).findFirst().get()
					.getValor());
		} catch (Exception e) {
			response.setCodigoRespuesta("99");
			response.setMensajeRespuesta(e.getMessage());
		}

		return response;
	}

	@Override
	public ClonarCVFResponseDTO clonarCotizacionVidaFree(String nroCotizacion) {
		ClonarCVFResponseDTO clonarCVFResponseDTO = new ClonarCVFResponseDTO();
		// TODO 1. Generar nuevo número de cotización
		String numeroNuevaCotizacion = "";
		try {
			ObjectMapper mapper = new ObjectMapper();
			AsegurableResponse bean = null;
			try {
				bean = mapper.readValue(cotizadorRestClient.generarCorrelativo().getMsg(), AsegurableResponse.class);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (bean != null) {
				numeroNuevaCotizacion = bean.getCotnum();
				// TODO 2. Obtener data de cotización inicial
				ConsultaCotizacionDetalleRequest request0 = new ConsultaCotizacionDetalleRequest();
				request0.setIdAgente("");
				request0.setNumeroCotizacion(nroCotizacion);
				LOGGER.info("=== OBTENIENDO COTIZACION VIDA FREE ===");
				ConsultaCotizacionDetalleResponse response0 = vidaFreeRestClient.obtenerCotizacionDetalle(request0);

				if (response0 == null || response0.getCode().equals("99")) {
					throw new Exception("No se pudo obtener información de cotización de vida free");
				}
				Solicitud solicitud = solicitudRepository.findByNumeroCotizacion(nroCotizacion);
				LOGGER.info("=== SOLICITUD === {}", solicitud);
				if (solicitud != null) {
					Persona personaAsegurado = personaRepository.findById(solicitud.getIdAsegurado()).get();
					LOGGER.info("=== PERSONA === {}", personaAsegurado);
					Adn adn = adnRepository.findByIdPersona(solicitud.getIdAsegurado());
					LOGGER.info("=== ADN === {}", adn);
					TokenRequestDTO requestToken = new TokenRequestDTO();
					requestToken.setEdadActuarial(response0.getData().getCliente().getEdadContratacion());
					requestToken.setFechaCotizacion(response0.getData().getDetalleCotizacion().getFechaCotizacion());
					requestToken.setFechaNacimiento(response0.getData().getCliente().getFechaNacimiento());
					requestToken.setIdCotizacionCrm("");
					requestToken.setIdOportunidadCrm(adn.getIdOportunidadCrm());
					requestToken.setIdUsuarioCrm(response0.getData().getAgente().getId());
					requestToken.setNombreCliente(response0.getData().getCliente().getNombre());
					requestToken.setNombreUsuario(response0.getData().getAgente().getNombre());
					requestToken.setNumeroCotizacion(numeroNuevaCotizacion);
					requestToken.setNumeroDocumentoCliente(response0.getData().getCliente().getNumeroDocumento());
					requestToken.setRol("AGENTE");
					requestToken.setSexo("M".equals(response0.getData().getCliente().getSexo()) ? "1" : "2");
					requestToken.setTipoDocumentoCliente("1");
					requestToken.setFlagIGV("0");
					requestToken.setToken("");
					LOGGER.info("=== OBTENIENDO TOKEN VIDA FREE ===");
					BaseResponseDTO responseToken = obtenerURLVidaFree(requestToken);
					String token = "";
					if (responseToken == null || responseToken.getCodigoRespuesta().equals("99")) {
						throw new Exception("No se pudo obtener token de vida free");
					} else {
						token = responseToken.getMensajeRespuesta()
								.substring(responseToken.getMensajeRespuesta().indexOf("=") + 1);
					}

					LOGGER.info("=== OBTENIENDO CUMULO PARA VIDA FREE ===");
					CotizadorCumuloResponseDTO responseCumulo = this.obtenerCumulo(personaAsegurado.getTipoDocumento(),
							personaAsegurado.getNumeroDocumento());
					String monedaCumulo = "";
					String montoCumulo = "0.0";
					if (responseCumulo != null) {
						monedaCumulo = Constantes.MONEDA_ACSELE_DOLARES;
						montoCumulo = responseCumulo.getCumuloDolares().toString();
					}

					// TODO 3. Guardar cotización
					LOGGER.info("=== GUARDANDO COTIZACION ===");
					GenericoResponse responseCotizacion = vidaFreeRestClient.guardarCotizacion(token, nroCotizacion,
							numeroNuevaCotizacion, monedaCumulo, montoCumulo, true);

					if ("01".equals(responseCotizacion.getCode())) {
						Long nroCotizacionL = Long.parseLong(numeroNuevaCotizacion);
						LOGGER.info("NRO COTIZACION VIDA ===> {}", nroCotizacionL);
						List<CotizacionDetalle> cotizacionVida = cotizacionRepository
								.detalleCotizacion2(nroCotizacionL);
						LOGGER.info("COTIZACION VIDA ===> {}", cotizacionVida);
						String idCrmCotizador = "";
						if (cotizacionVida != null) {
							LOGGER.info("COTIZADOR CRM ==> {}", cotizacionVida.size());
							if (cotizacionVida.size() > 0) {
								LOGGER.info("COTIZADOR CRM 1 ==> " + cotizacionVida.get(0).getCrmCotizadorId());
								idCrmCotizador = cotizacionVida.get(0).getCrmCotizadorId();
							}
						}
						Solicitud solicitudClonada = solicitudRepository.findByNumeroCotizacion(nroCotizacion);
						// solicitudClonada.setIdSolicitud(null);
						solicitudClonada.setNumeroCotizacion(numeroNuevaCotizacion);
						solicitudClonada.setNumeroCotizacionOrigen(nroCotizacion);
						solicitudClonada.setNumeroPropuesta(numeroNuevaCotizacion);
						// String archivoSolicitud = solicitudClonada.getNombreArchivoSolicitud();
						solicitudClonada.setFechaFirmaAsegurado(null);
						solicitudClonada.setFechaFirmaContratante(null);
						solicitudClonada.setNombreArchivoSolicitud(null);
						solicitudClonada.setEstado(Constantes.CODIGO_SOLICITUD_PENDIENTE_FIRMA);
						solicitudRepository.save(solicitudClonada);

						/*
						 * Solicitud solicitudNuevo =
						 * solicitudRepository.findByNumeroCotizacion(nroCotizacion);
						 * solicitudNuevo.setNombreArchivoSolicitud(archivoSolicitud);
						 * solicitudRepository.save(solicitudNuevo);
						 */

						for (SolicitudBeneficiario beneficiarioClonado : solicitudBeneficiarioRepository
								.findByIdSolicitud(solicitud.getIdSolicitud())) {
							beneficiarioClonado.setSolicitud(solicitudClonada);
							solicitudBeneficiarioRepository.save(beneficiarioClonado);
						}

						SolicitudDps dpsClonado = solicitudDpsRepositorioRepository
								.findByIdSolicitud(solicitud.getIdSolicitud());
						dpsClonado.setSolicitud(solicitudClonada);
						solicitudDpsRepositorioRepository.save(dpsClonado);

						clonarCVFResponseDTO.setErr("0");
						clonarCVFResponseDTO.setMsg("OK");
						clonarCVFResponseDTO.setRs("true");
						clonarCVFResponseDTO.setId(numeroNuevaCotizacion);
						clonarCVFResponseDTO.setIdCotizacionCRM(idCrmCotizador);
					}
				} else {
					clonarCVFResponseDTO.setErr("0");
					clonarCVFResponseDTO.setMsg("Error: Cotización aún no tiene solicitud.");
					clonarCVFResponseDTO.setRs("false");
					clonarCVFResponseDTO.setId("");
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Problemas al invocar servicio de Cotizador[ObtenerCumulo]");
			LOGGER.error(e.getMessage());
			clonarCVFResponseDTO.setErr("0");
			clonarCVFResponseDTO.setMsg("Error: No se pudo generar número de cotización");
			clonarCVFResponseDTO.setRs("false");
			clonarCVFResponseDTO.setId("");
		}

		return clonarCVFResponseDTO;
	}

	@Override
	public BaseResponseDTO buscarEnArchivo(String dato, String archivo) {
		BaseResponseDTO respuesta = new BaseResponseDTO();
		String codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_NO_EXISTE;
		String mensajeRespuesta = Strings.EMPTY;

		try {
			File file = new File(rutaPrivadaTemporal + archivo);
			FileReader fr = null;
			BufferedReader br = null;
			try {
				fr = new FileReader(file);
				br = new BufferedReader(fr);

				String linea;
				while ((linea = br.readLine()) != null) {
					if (linea.trim().equals(dato)) {
						codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_EXITO;
					}
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
				codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_ERROR;
				mensajeRespuesta = Utilitarios.obtenerMensaje(messageSource,
						Constantes.MENSAJE_SOLICITUD_ERROR_OBTENER_DATOS_FORMULARIO);
			} finally {
				try {
					if (null != fr) {
						fr.close();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_ERROR;
			mensajeRespuesta = Utilitarios.obtenerMensaje(messageSource,
					Constantes.MENSAJE_SOLICITUD_ERROR_OBTENER_DATOS_FORMULARIO);
		}
		respuesta.setCodigoRespuesta(codigoRespuesta);
		respuesta.setMensajeRespuesta(mensajeRespuesta);
		return respuesta;
	}

	@Override
	public BaseResponseDTO validarNoObservado(String numeroCotizacion) {
		BaseResponseDTO respuesta = new BaseResponseDTO();
		String codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_NO_EXISTE;
		String mensajeRespuesta = Strings.EMPTY;
		if (Constantes.CODIGO_RESPUESTA_GENERAL_EXITO
				.equals(this.buscarEnArchivo(numeroCotizacion, "observados.txt").getCodigoRespuesta())) {
			if (Constantes.CODIGO_RESPUESTA_GENERAL_EXITO
					.equals(this.buscarEnArchivo(numeroCotizacion, "atendidos.txt").getCodigoRespuesta())) {
				codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_EXITO;
			} else {
				codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_ERROR_FUNCIONAL;
			}
		} else {
			codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_EXITO;
		}
		respuesta.setCodigoRespuesta(codigoRespuesta);
		respuesta.setMensajeRespuesta(mensajeRespuesta);

		return respuesta;
	}

	@Override
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

	/*
	 * @Override public ByteArrayInputStream generarPDFCotizacionVida(String
	 * nroCotizacion, String agente, String asegurado) { ByteArrayOutputStream out =
	 * new ByteArrayOutputStream(); SimpleDateFormat formatter = new
	 * SimpleDateFormat("dd/MM/yyyy"); Date date = new
	 * Date(System.currentTimeMillis()); String pdfUrl =
	 * urlCotizacionPDF.concat("&fecot=").concat(formatter.format(date)).concat(
	 * "&aseg=") .concat(asegurado).replaceAll("\\s",
	 * "+").concat("&idCot=").concat(nroCotizacion).concat("&user=")
	 * .concat(agente); System.out.println(pdfUrl); SimpleDateFormat
	 * formatterTimeStamp = new SimpleDateFormat("yyyyMMddHHmmss"); Date timestamp =
	 * new Date(System.currentTimeMillis()); LOGGER.info("COPIANDO A TEMPORALES");
	 * String cotizacionFileName =
	 * "".concat("cotizacion-").concat(nroCotizacion).concat("-")
	 * .concat(formatterTimeStamp.format(timestamp)).concat(".pdf"); String
	 * cotizacionFilePrivada = rutaPDFPrivada + "" + cotizacionFileName; // String
	 * cotizacionFilePrivada = "E:\\INTERSEGURO\\PROYECTOS\\" + "" + //
	 * cotizacionFileName; String cotizacionFilePublica = rutaPDFPublica + "" +
	 * cotizacionFileName; // String cotizacionFilePublica =
	 * "E:\\INTERSEGURO\\PROYECTOS\\SIV\\siv-front\\" // + "" + cotizacionFileName;
	 * 
	 * try { String url = pdfUrl; FileUtils.copyURLToFile(new URL(url), new
	 * File(cotizacionFilePrivada)); } catch (Exception ex) {
	 * LOGGER.info("ERROR AL COPIAR PDF COTIZACION EN TEMPORALES +", ex);
	 * cotizacionFilePublica = Strings.EMPTY; }
	 * 
	 * if (cotizacionFilePublica != null && !cotizacionFilePublica.equals("")) {
	 * File file = new File(cotizacionFilePrivada);
	 * 
	 * FileInputStream fis; try { fis = new FileInputStream(file); //
	 * System.out.println(file.exists() + "!!"); // InputStream in =
	 * resource.openStream(); ByteArrayOutputStream bos = new
	 * ByteArrayOutputStream(); byte[] buf = new byte[1024]; try { for (int readNum;
	 * (readNum = fis.read(buf)) != -1;) { bos.write(buf, 0, readNum); // no doubt
	 * here is 0 // Writes len bytes from the specified byte array starting at
	 * offset off to this // byte array output stream. System.out.println("read " +
	 * readNum + " bytes,"); } } catch (IOException ex) { //
	 * Logger.getLogger(genJpeg.class.getName()).log(Level.SEVERE, null, ex); }
	 * byte[] bytes = bos.toByteArray();
	 * 
	 * try { out.write(bytes); } catch (IOException e) { e.printStackTrace(); } }
	 * catch (FileNotFoundException e) { e.printStackTrace(); }
	 * 
	 * return new ByteArrayInputStream(out.toByteArray()); } else { return null; } }
	 */

	@Override
	public CotizacionCrmResponseDTO guardaCotizacionAdn(CotizacionCrmRequestDTO cotizacionCrmRequestDTO) {
		LOGGER.info("guardaCotizacionAdn - request {}", gson.toJson(cotizacionCrmRequestDTO));
		CotizacionCrmResponseDTO response = new CotizacionCrmResponseDTO();
		Solicitud solicitud = new Solicitud();
		Persona persona = new Persona();
		try {
			LOGGER.info("longitud numero documento {}", cotizacionCrmRequestDTO.getNumeroDocumento().length());
			String tipoDocumento = cotizacionCrmRequestDTO.getNumeroDocumento().length() == 8 ? "1" : "2";
			LOGGER.info("Tipo documento {}", tipoDocumento);
			persona = personaRepository.findByTipoNumeroDocumento(tipoDocumento,
					cotizacionCrmRequestDTO.getNumeroDocumento());

			String idCRM = !cotizacionCrmRequestDTO.getIdCRM().equals("") && cotizacionCrmRequestDTO.getIdCRM() != null
					&& !cotizacionCrmRequestDTO.getIdCRM().equals("0") ? cotizacionCrmRequestDTO.getIdCRM() : null;

			LOGGER.info("idCRM " + idCRM);

			if (idCRM != null) {
				LOGGER.info("Existe cotizacion en crm " + cotizacionCrmRequestDTO.getNumeroCotizacion());
				solicitud = solicitudRepository.getOne(Long.parseLong(idCRM));
//				solicitud = solicitudRepository.findByNumeroCotizacion(cotizacionCrmRequestDTO.getNumeroCotizacion());
				solicitud.setFechaModif(new Date());
			} else {
				solicitud.setPersonaByIdAsegurado(persona);
				solicitud.setNumeroCotizacion(cotizacionCrmRequestDTO.getNumeroCotizacion());
				solicitud.setNumeroPropuesta(cotizacionCrmRequestDTO.getNumeroCotizacion());
				solicitud.setEstado("0");
				if ("VidaFree".equals(cotizacionCrmRequestDTO.getNombreProducto())) {
					if (cotizacionCrmRequestDTO.getUsuarioLogin().equals("")) {
						solicitud.setUsuarioCrea("VidaFree");
					} else {
						solicitud.setUsuarioCrea(cotizacionCrmRequestDTO.getUsuarioLogin());
					}
				} else {
					if (cotizacionCrmRequestDTO.getUsuarioLogin().equals("")) {
						solicitud.setUsuarioCrea("PlanGarantizado");
					} else {
						solicitud.setUsuarioCrea(cotizacionCrmRequestDTO.getUsuarioLogin());
					}
				}
				solicitud.setAgenteCorreo(cotizacionCrmRequestDTO.getAgenteCorreo());
				solicitud.setAgenteNombres(cotizacionCrmRequestDTO.getAgenteNombres());
				solicitud.setAgenteNumVendedor(cotizacionCrmRequestDTO.getAgenteNumVendedor());
				solicitud.setFechaCrea(new Date());
			}
			solicitud.setRequestCrm(gson.toJson(cotizacionCrmRequestDTO));
			solicitud.setIdPlan(cotizacionCrmRequestDTO.getIdPlan());
			solicitud.setNombrePlan(cotizacionCrmRequestDTO.getPlanVida());
			LOGGER.info("inserta en adn cotizacion");

			Solicitud model = solicitudRepository.findByNumeroCotizacion(cotizacionCrmRequestDTO.getNumeroCotizacion());

			Solicitud soli = new Solicitud();
			if (model == null) {
				solicitud.setFechaSolicitud(new Date());
				soli = solicitudRepository.save(solicitud);
			} else {
				soli = model;
				soli.setRequestCrm(gson.toJson(cotizacionCrmRequestDTO));
				soli.setIdPlan(cotizacionCrmRequestDTO.getIdPlan());
				soli.setNombrePlan(cotizacionCrmRequestDTO.getPlanVida());
			}

			///////
			CotizaDetalleResponseDTO cotizacionDetalle = new CotizaDetalleResponseDTO();
			if ("VidaFree".equals(cotizacionCrmRequestDTO.getNombreProducto())) {
				cotizacionDetalle = this.detalleCotizacionVidaFree(Long.valueOf(solicitud.getNumeroCotizacion()));
			} else {
				cotizacionDetalle = this.detalle(Long.valueOf(solicitud.getNumeroCotizacion()));
			}
			this.guardarSolicitud(soli, cotizacionDetalle, solicitud.getPersonaByIdAsegurado(),
					cotizacionCrmRequestDTO.getUsuarioLogin(), cotizacionCrmRequestDTO.getAgenteNombres(),
					cotizacionCrmRequestDTO.getAgenteCorreo(), cotizacionCrmRequestDTO.getAgenteNumVendedor(),
					cotizacionCrmRequestDTO.getAgenteIdCRM(), cotizacionCrmRequestDTO.getNombreProducto());
			//////

			generarPdfEdn(cotizacionCrmRequestDTO, solicitud, cotizacionDetalle, solicitud.getPersonaByIdAsegurado());

			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
			response.setIdCotizacionCRM(soli.getIdSolicitud().toString());
			response.setMensajeRespuesta("Se guardo en adn");

			LOGGER.info("Guardo cotizacion adn {}", gson.toJson(response));
		} catch (Exception e) {
			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			response.setMensajeRespuesta("Error al guardar cotizacion en adn");
			LOGGER.error("Error en guardar cotizacion adn => " + e.getMessage());
		}
		// Consulta cumulo y guarda exigencias médicas
		if ( !solicitud.getNumeroCotizacion().isEmpty() ) {
			this.saveCumuloExigenciasMedicas(solicitud.getNumeroCotizacion());
		}
		LOGGER.info("salio a la guardaCotizacionAdn ");
		return response;

	}

	private void generarPdfEdn(CotizacionCrmRequestDTO cotizacionCrmRequestDTO, Solicitud solicitud,
							   CotizaDetalleResponseDTO cotizacionDetalle, Persona personaAsegurado) {
		try {
			LOGGER.info("Entro generarPdfEdn");
			DateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
			CotizacionDetalle model = new CotizacionDetalle();
			ConsultaCotizacionDetalleRequest request0 = new ConsultaCotizacionDetalleRequest();
			ConsultaCotizacionDetalleResponse response0 = new ConsultaCotizacionDetalleResponse();
			String coti=cotizacionCrmRequestDTO.getNumeroCotizacion();

			String fechaCotizacion = "";
			if (cotizacionCrmRequestDTO.getNombreProducto().equals("VidaFree")) {
				request0.setIdAgente("");
				request0.setNumeroCotizacion(cotizacionCrmRequestDTO.getNumeroCotizacion());
				response0 = vidaFreeRestClient.obtenerCotizacionDetalle(request0);
				fechaCotizacion = response0.getData().getDetalleCotizacion().getFechaCotizacion();
			}
			else {
				model = cotizacionRepository.detalleCotizacion(Long.parseLong(solicitud.getNumeroPropuesta())).get(0);
				fechaCotizacion = sourceFormat.format(model.getFechaCotizacion());
			}

			String fechaEDNString = valorMultiTabla("060", "1", "valor");
			Date fechaEdnDate = sourceFormat.parse(fechaEDNString);
			Date fechaCotizacionDate = sourceFormat.parse(fechaCotizacion);

			if (fechaCotizacionDate.compareTo(fechaEdnDate) >= 0) {
				// obtener CarpetaCS
				LOGGER.info("generarPdfEdn obtener CarpetaCS - {}", coti);
				Persona persona = personaRepository.findByNumeroDocumento(cotizacionCrmRequestDTO.getNumeroDocumento());
				String contactVal = persona.getIdPersonaCS();
				LOGGER.info("generarPdfEdn PersonaxDocumento - {} => {}",coti, contactVal);
				// obtener datos
				String planesFuturo = "";
				String ahorroDeseasRecibir = "";
				String frecuenciaPago = "";
				String porcentajeDevolucion = "";
				String montoMes = "";
				String aniosAsegurado = "";
				String aniosPago = "";

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
								ahorroDeseasRecibir = "Fin de cobertura";
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
					switch (cotizacionDetalle.getSubplanId()) {
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
				esnGenerarPdfRequest.setSolicitud(cotizacionCrmRequestDTO.getNumeroCotizacion());
				esnGenerarPdfRequest.setNumerodocumento(cotizacionCrmRequestDTO.getNumeroDocumento());
				esnGenerarPdfRequest.setCodigoPlantillaEN("1");
				List<GenerarPdfCabeceras> generarPdfCabeceras = new ArrayList<>();
				GenerarPdfCabeceras generarPdfCabecera1 = new GenerarPdfCabeceras();
				generarPdfCabecera1.setCampo("CLIENTE");
				String nombreCliente = personaAsegurado.getNombres(); // responseObtenerPersonaDocumento.getData().get(0).getNOMBRE();
				String apePatCliente = personaAsegurado.getApellidoPaterno();// responseObtenerPersonaDocumento.getData().get(0).getAPELLIDO_PATERNO();
				String apeMatCliente = personaAsegurado.getApellidoMaterno() == null ? ""
						: personaAsegurado.getApellidoMaterno();// responseObtenerPersonaDocumento.getData().get(0).getAPELLIDO_MARNO()
				// == null ? "" :
				// responseObtenerPersonaDocumento.getData().get(0).getAPELLIDO_MARNO();
				generarPdfCabecera1.setValor(nombreCliente + " " + apePatCliente + " " + apeMatCliente);
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
			System.out.println(e.getMessage());
			LOGGER.error("Error generarPdfEdn => " + e.getMessage());
		}
	}

	@Async("processExecutor")
	public GenerarPdfResponse guardarServicioEdn(GenerarPdfRequest generarPdfRequest, String usuarioLogin) {
		GenerarPdfResponse esnGenerarPdfResponse = new GenerarPdfResponse();
		try {
			LOGGER.info("Ingresa a GuardarServicioEdn");
			LOGGER.info("request  --->" + gson.toJson(generarPdfRequest));
			Solicitud solicitud = solicitudRepository.findByNumeroCotizacion(generarPdfRequest.getSolicitud());

			String jsonRequest = gson.toJson(generarPdfRequest);
			OkHttpClient client = new OkHttpClient().newBuilder().build();
			okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType, jsonRequest);
			String credential = Credentials.basic(esnUser, esnPasword);

			ServicioEdn servicioEdn = new ServicioEdn();
			servicioEdn.setUsuario_crea(usuarioLogin);
			servicioEdn.setFecha_crea(new Date());
			servicioEdn.setSolicitud(generarPdfRequest.getSolicitud());
			servicioEdn.setRequest(gson.toJson(generarPdfRequest));

			Request request = new Request.Builder().url(getUrlEsnGeneraPdf()).method("POST", body)
					.addHeader("Authorization", credential).addHeader("Content-Type", "application/json").build();
			Response response = client.newCall(request).execute();
			esnGenerarPdfResponse.setCode(String.valueOf(response.code()));
			esnGenerarPdfResponse.setStatusHttp(String.valueOf(response.code()));
			esnGenerarPdfResponse.setMessage(response.message());

			if (response.code() == 200) {
				String rpta = response.body().string();
				esnGenerarPdfResponse.setData(rpta);
				servicioEdn.setFlag_edn("S");
				servicioEdn.setResponse(gson.toJson(esnGenerarPdfResponse));
				solicitud.setDocumentoEdn("1");
				LOGGER.info("Respuesta servicio: {}", gson.toJson(esnGenerarPdfResponse));
			} else {
				servicioEdn.setFlag_edn("N");
				solicitud.setDocumentoEdn("2");
			}
			LOGGER.info("Guardando respuesta en tabla servicio_edn");
			solicitudRepository.save(solicitud);
			servicioEdnRepository.save(servicioEdn);
		} catch (Exception e) {
			LOGGER.error("Error guardarServicioEdn => " + e.getMessage());
		}
		LOGGER.info("Fin GuardarServicioEdn");
		return esnGenerarPdfResponse;
	}

	public ListObtenerIdPersonaResponse obtenerPersonaDocumento(ObtenerIdPersonaRequest obtenerIdPersonaRequest) {
		LOGGER.info("Entro a ObtenerPersonaDocumento(obtenerIdPersonaRequest)");
		ListObtenerIdPersonaResponse listObtenerIdPersonaResponse = new ListObtenerIdPersonaResponse();
		try {
			String jsonRequest = gson.toJson(obtenerIdPersonaRequest);
			LOGGER.info("obtenerPersonaDocumento url post => " + servidorApiDocumento + globalFindByDocumento);
			LOGGER.info("obtenerPersonaDocumento request => " + jsonRequest);
			OkHttpClient client = new OkHttpClient().newBuilder().build();
			okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType, jsonRequest);

			Request request = new Request.Builder().url(servidorApiDocumento + globalFindByDocumento)
					.method("POST", body).addHeader("Content-Type", "application/json").build();
			Response response = client.newCall(request).execute();

			if (response.code() == 200) {
				String data = response.body().string();
				listObtenerIdPersonaResponse = gson.fromJson(data, ListObtenerIdPersonaResponse.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		}
		LOGGER.info("Salió obtenerPersonaDocumento");
		return listObtenerIdPersonaResponse;
	}

	private SolicitudRegistroResponseDTO guardarSolicitud(Solicitud solicitudDomain,
			CotizaDetalleResponseDTO cotizacionDetalle, Persona personaAsegurado, String usuarioLogin,
			String agenteNombres, String agenteCorreo, String agenteNumVendedor, String agenteIdCRM,
			String tipoProducto) {
		Long idSolicitud = null;
		String numeroCotizacion = cotizacionDetalle.getNroCotizacion();
		String idCrmCotizador = cotizacionDetalle.getCrmCotizadorId();
		String idCrmOportunidad = cotizacionDetalle.getCrmOportunidadId();
		String fechaCotizacion = "";
		try {
			fechaCotizacion = DateUtil.dateToString(solicitudDomain.getFechaSolicitud(), DateUtil.FORMATO_DIA_DDMMYYYY);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		/*
		 * Solicitud solicitudDomain = new Solicitud();
		 * solicitudDomain.setNumeroCotizacion(numeroCotizacion); solicitudDomain =
		 * solicitudRepository.findByNumeroCotizacion(numeroCotizacion);
		 */

		if (solicitudDomain != null && !solicitudDomain.getEstado().equals("0")) {
//			CrmCotizacion crmCotizacion = crmRepository.obtenerDataCotizacion(numeroCotizacion);
//			idCrmCotizador = crmCotizacion != null ? crmCotizacion.getCotizacionId() : null;
			idCrmCotizador = null;
		}

		solicitudDomain.setEstado("0");
		solicitudDomain.setAgenteNombres(agenteNombres);
		solicitudDomain.setAgenteNumVendedor(agenteNumVendedor);
		solicitudDomain.setIdCrmUsuario(agenteIdCRM);
		solicitudDomain.setIdCrmCotizador(idCrmCotizador);
		solicitudDomain.setIdCrmOportunidad(idCrmOportunidad);
		solicitudDomain.setUsuarioCrea(usuarioLogin);
		solicitudDomain.setFechaSolicitud(solicitudDomain.getFechaSolicitud());
		solicitudDomain.setNumeroPropuesta(numeroCotizacion);
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
				String cobertura = "VidaFree".equals(tipoProducto) ? coberturaItem.getId()
						: this.obtenerCodigoMultiTabla(Constantes.MULTITABLA_CODIGO_TABLA_COBERTURAS,
								Constantes.MULTITABLA_COLUMNA_VALOR_AUXILIAR, coberturaItem.getId());
				String tipoCobertura = coberturaItem.getTipo();
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
		String result = Strings.EMPTY;

		if (valorBuscar == null || valorBuscar.equals(Strings.EMPTY))
			return result;

		List<Multitabla> lista = multitablaRepository.findByCodigoTablaSinEstado(codigoTabla);
		if (lista != null && !lista.isEmpty()) {
			for (Multitabla m : lista) {
				switch (campo) {
				case Constantes.MULTITABLA_COLUMNA_VALOR:
					if (m.getValor() != null && m.getValor().equals(valorBuscar)) {
						result = m.getCodigo();
						break;
					}
				case Constantes.MULTITABLA_COLUMNA_VALOR_AUXILIAR:
					if (m.getValorAux() != null && m.getValorAux().equals(valorBuscar)) {
						result = m.getCodigo();
						break;
					}
				case Constantes.MULTITABLA_COLUMNA_VALOR_CRM:
					if (m.getValorCrm() != null && m.getValorCrm().equals(valorBuscar)) {
						result = m.getCodigo();
						break;
					}
				}
			}
		}
		return result;
	}

	private String valorMultiTabla(String codigoTabla, String codigo, String campo) {
		String result = Strings.EMPTY;

		if (codigo == null || codigo.equals(Strings.EMPTY))
			return result;

		List<Multitabla> lista = multitablaRepository.findByCodigoTablaSinEstado(codigoTabla);
		if (lista != null && !lista.isEmpty()) {
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
					case Constantes.MULTITABLA_COLUMNA_VALOR_FLEX:
						result = m.getValorFlex();
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

	public void saveCumuloExigenciasMedicas(String nroCotizacion) {
		long startTime = System.nanoTime();
		long endTime;
		String traza = Utilitarios.trazaLog() + "- consultaCumuloExigenciasMedicas " + nroCotizacion;
		LOGGER.info("{} inicia {}", traza, startTime);	
		try {
			Solicitud solicitud = solicitudRepository.findByNumeroCotizacion(nroCotizacion);
			SolicitudExigenciasMedicas solicitudExigenciasMedicas = new SolicitudExigenciasMedicas();
			solicitudExigenciasMedicas.setSolicitud(solicitud);

			String nombreProducto = solicitud.getNombrePlan().startsWith("Vida Free")? "VidaFree" : "PlanGarantizado";
			if ( nombreProducto == "VidaFree" ) {
				// VF
				ConsultaCotizacionDetalleRequest request = new ConsultaCotizacionDetalleRequest();
				request.setNumeroCotizacion(nroCotizacion);
				request.setIdAgente("xxx");
				LOGGER.info("{} RES {}", traza, gson.toJson(request));
				ConsultaCotizacionDetalleResponse cotizacionVF = cotizadorFreeRestClient.obtenerCotizacionDetalle(request);
				LOGGER.info("{} cotizacionVF {}", traza, (cotizacionVF));
				AtomicReference<Double> capitalPolizaSoles = new AtomicReference<>(0.0);
				AtomicReference<Double> capitalPolizaDolares = new AtomicReference<>(0.0);
				double cumuloDolares = 0.0;
				double capitalDolares = 0.0;

				LOGGER.info("{} cotizacionVF {}", traza, gson.toJson(cotizacionVF));

				cotizacionVF.getData().getDetalleCotizacion().getCoberturas().stream()
						.filter(cob -> StringUtils.equals(cob.getNombreCobertura(), "Fallecimiento")).forEach(i -> {
							if (Constantes.MONEDA_ADN_SOLES
									.equals(cotizacionVF.getData().getDetalleCotizacion().getCodMoneda())) {
								capitalPolizaSoles.set(i.getSumaAsegurada());
							} else {
								capitalPolizaDolares.set(i.getSumaAsegurada());
							}
						});

				ConversionResponseDTO tipocambio = serviceFactory.getCotizaService().obtenerTipoCambio();
				LOGGER.info("{} tipocambio: {}", traza, tipocambio.getValor());

				Optional<Persona> persona = personaRepository.findById(solicitud.getIdAsegurado());
				if (!persona.isPresent()) {
					throw new NullPointerException("Asegurado NOT FOUND");
				}

				double factorSoles = 0.0;
				TipoCambioResponseDTO tipoCambioCumulo = this.obtenerTipoCambioAcsele();
				if (tipoCambioCumulo.getCodigoRespuesta().equals(CODIGO_RESPUESTA_GENERAL_EXITO)) {
					factorSoles = tipoCambioCumulo.getConversiones().stream()
							.filter(c -> Constantes.MONEDA_ACSELE_SOLES.equals(c.getMonedaOrigen()))
							.flatMapToDouble(x -> DoubleStream.of(x.getValor())).findFirst().orElse(0.0);

				}
				capitalDolares = capitalPolizaDolares.get() + (capitalPolizaSoles.get() * factorSoles);
				LOGGER.info("{} capitalTotal: {}", traza, capitalDolares);

				ObtenerCumuloResponse cumulor = cotizadorRestClient.obtenerCumuloGeneral(persona.get().getNumeroDocumento(),persona.get().getTipoDocumento());
				AtomicReference<Double> cumuloPolizaSoles = new AtomicReference<>(0.0);
				AtomicReference<Double> cumuloPolizaDolares = new AtomicReference<>(0.0);
				solicitudExigenciasMedicas.setResponseCumulo(gson.toJson(cumulor));
				if (cumulor.getStatusHttp().equals("200") && cumulor.getMontosdet() != null) {
					cumulor.getMontosdet().forEach(i -> {
						if (Constantes.MONEDA_ACSELE_SOLES.equals(i.getIdmoneda())) {
							cumuloPolizaSoles.set(Double.parseDouble(i.getMonto()));
						} else {
							cumuloPolizaDolares.set(Double.parseDouble(i.getMonto()));
						}
					});
					if(flagCumulo.equals("1")) {
						cumuloDolares = (cumuloPolizaDolares.get() + (cumuloPolizaSoles.get()) * factorSoles);
					} else if(flagCumulo.equals("2")) {
						cumuloDolares = cumuloPolizaDolares.get();
					}
					LOGGER.info("{} cumuloTotal: {}", traza, cumuloPolizaDolares);
					capitalDolares = (capitalPolizaDolares.get() + (capitalPolizaSoles.get()) * factorSoles);
					LOGGER.info("{} capitalTotal: {}", traza, capitalDolares);

				}
				solicitudExigenciasMedicas.setCumulo(String.valueOf(cumuloDolares));
				solicitudExigenciasMedicas.setCapital(String.valueOf(capitalDolares));
				cumuloDolares = cumuloDolares + capitalDolares;
				LOGGER.info("{} propducto: {}", traza,
						cotizacionVF.getData().getDetalleCotizacion().getCodProductoAcsele());
				LOGGER.info("{} moneda: 2123", traza);
				LOGGER.info("{} edad: {}", traza, cotizacionVF.getData().getCliente().getEdadContratacion());
				LOGGER.info("{} setCumulo: {}", traza, cumuloDolares);
				LOGGER.info("{} setCapitalTotal: {}", traza, cumuloDolares);
				LOGGER.info("{} setCapital: {}", traza, capitalDolares);
				LOGGER.info("{} tipo de cambio: {}", traza, BigDecimal.valueOf(tipocambio.getValor()));
				solicitudExigenciasMedicas.setMoneda("2123");
				solicitudExigenciasMedicas.setCapitalTotal(String.valueOf(cumuloDolares));
				solicitudExigenciasMedicas.setEdad(cotizacionVF.getData().getCliente().getEdadContratacion());
				solicitudExigenciasMedicas.setTipoCambio(String.valueOf(factorSoles) );
				List<ExigenciaMedica> exigenciasMedicas = cotizacionRepository.exigenciasMedicas(
						cotizacionVF.getData().getDetalleCotizacion().getCodProductoAcsele(), "2123",
						cotizacionVF.getData().getCliente().getEdadContratacion(), Double.toString(cumuloDolares),
						Double.toString(capitalDolares), String.valueOf(capitalDolares),
						BigDecimal.valueOf(tipocambio.getValor()));
				LOGGER.info("{} exigenciasMedicas {}", traza, gson.toJson(exigenciasMedicas));
				solicitudExigenciasMedicas.setExamenesMedicos(gson.toJson(exigenciasMedicas));
				solicitudExigenciasMedicas.setUsuarioCrea(solicitud.getUsuarioModif()!= null?solicitud.getUsuarioModif():solicitud.getUsuarioCrea());
				solicitudExigenciasMedicas.setFechaCrea(new Date());
			}
			solicitudExigenciasMedicasRepository.save(solicitudExigenciasMedicas);

		} catch (Exception e) {
			LOGGER.error("{} Error ===> {}", traza, e.getMessage());
		}
		endTime = System.nanoTime();
		LOGGER.info("{} termina {}", traza, endTime);		
	}

	public List<ExigenciaMedica> getExigenciasMedicas(Solicitud solicitud) {
		String traza = Utilitarios.trazaLog() + "- getExigenciasMedicas " + solicitud.getNumeroCotizacion();
		LOGGER.info("{} getExigenciasMedicas {}", traza, " ingresa");

		List<SolicitudExigenciasMedicas> listExigenciasMedicas = solicitudExigenciasMedicasRepository.findByIdSolicitud(solicitud.getIdSolicitud());
			if ( listExigenciasMedicas.size() == 0 ) {
				this.saveCumuloExigenciasMedicas(solicitud.getNumeroCotizacion());
				listExigenciasMedicas = solicitudExigenciasMedicasRepository.findByIdSolicitud(solicitud.getIdSolicitud());
			}
			ObjectMapper mapper = new ObjectMapper();
			List<ExigenciaMedica> exigenciasMedicas = null;
			try {
				exigenciasMedicas = mapper.readValue(listExigenciasMedicas.get(0).getExamenesMedicos(), new TypeReference<List<ExigenciaMedica>>(){});
				LOGGER.info("{} getExigenciasMedicas {}", traza, gson.toJson(exigenciasMedicas));
			} catch (JsonParseException e) {
				LOGGER.error("{} JsonParseException ===> {}", traza, e.getMessage());
				throw new NullPointerException("getExigenciasMedicas NOT FOUND");
			} catch (JsonMappingException e) {
				LOGGER.error("{} JsonMappingException ===> {}", traza, e.getMessage());
				throw new NullPointerException("getExigenciasMedicas NOT FOUND");
			} catch (IOException e) {
				LOGGER.error("{} IOException ===> {}", traza, e.getMessage());
				throw new NullPointerException("getExigenciasMedicas NOT FOUND");
			}
		return exigenciasMedicas;
	}

}
