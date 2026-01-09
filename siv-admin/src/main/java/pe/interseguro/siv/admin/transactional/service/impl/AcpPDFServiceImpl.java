package pe.interseguro.siv.admin.transactional.service.impl;

import java.io.*;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.reflect.TypeToken;
import com.lowagie.text.DocumentException;
import lombok.RequiredArgsConstructor;

import org.apache.commons.codec.binary.Base64;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import com.google.gson.Gson;

import fr.opensagres.xdocreport.document.images.FileImageProvider;
import fr.opensagres.xdocreport.document.images.IImageProvider;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;
import pe.interseguro.siv.admin.transactional.factory.ServiceFactory;
import pe.interseguro.siv.admin.transactional.service.AcpPDFService;
import pe.interseguro.siv.admin.transactional.service.CotizaService;
import pe.interseguro.siv.common.bean.*;
import pe.interseguro.siv.common.dto.response.*;
import pe.interseguro.siv.common.exception.ErrorResourceDTO;
import pe.interseguro.siv.common.exception.SivTXException;
import pe.interseguro.siv.common.persistence.db.mysql.domain.Multitabla;
import pe.interseguro.siv.common.persistence.db.mysql.domain.Persona;
import pe.interseguro.siv.common.persistence.db.mysql.domain.Solicitud;
import pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudDps;
import pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudDpsPregunta;
import pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudFiltrado;
import pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudProducto;
import pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudViaCobro;
import pe.interseguro.siv.common.persistence.db.mysql.repository.MultitablaRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.PersonaRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.SolicitudDpsPreguntaRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.SolicitudDpsRepositorioRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.SolicitudFiltradoRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.SolicitudProductoRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.SolicitudRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.SolicitudViaCobroRepository;
import pe.interseguro.siv.common.persistence.db.postgres.repository.CotizacionRepository;
import pe.interseguro.siv.common.persistence.rest.crm.CrmRestClient;
import pe.interseguro.siv.common.persistence.rest.global.GlobalRestClient;
import pe.interseguro.siv.common.persistence.rest.global.request.ObtenerIdPersonaRequest;
import pe.interseguro.siv.common.persistence.rest.global.request.UploadArchivoRequest;
import pe.interseguro.siv.common.persistence.rest.global.response.*;
import pe.interseguro.siv.common.persistence.rest.interseguro.InterseguroRestClient;
import pe.interseguro.siv.common.persistence.rest.interseguro.dto.Adjuntos;
import pe.interseguro.siv.common.persistence.rest.interseguro.dto.Remitente;
import pe.interseguro.siv.common.persistence.rest.interseguro.request.EnviarCorreoRequestNuevo;
import pe.interseguro.siv.common.persistence.rest.vidafree.VidaFreeRestClient;
import pe.interseguro.siv.common.util.Constantes;
import pe.interseguro.siv.common.util.ConstantesAcpPDF;
import pe.interseguro.siv.common.util.ConstantesSolicitudPDF;
import pe.interseguro.siv.common.util.DateUtil;
import pe.interseguro.siv.common.util.IXDocReportUtil;
import pe.interseguro.siv.common.util.Utilitarios;

@Service("acpPDFService")
@Transactional
@RequiredArgsConstructor
public class AcpPDFServiceImpl implements AcpPDFService {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MessageSource messageSource;

	private final Gson gson;

	@Autowired
	private SolicitudRepository solicitudRepository;

	@Autowired
	private SolicitudFiltradoRepository solicitudFiltradoRepository;

	@Autowired
	private SolicitudProductoRepository solicitudProductoRepository;

	@Autowired
	private SolicitudDpsRepositorioRepository solicitudDpsRepository;

	@Autowired
	private SolicitudDpsPreguntaRepository solicitudDpsPreguntaRepository;

	@Autowired
	private SolicitudViaCobroRepository solicitudViaCobroRepository;

	@Autowired
	private MultitablaRepository multitablaRepository;

	@Autowired
	private PersonaRepository personaRepository;
	@Autowired
	ServiceFactory serviceFactory;

	@Autowired
	private CrmRestClient crmRestClient;

	@Autowired
	private GlobalRestClient globalRestClient;

	@Autowired
	private InterseguroRestClient interseguroRestClient;

	@Autowired
	private CotizaService cotizaService;

	@Autowired
	private CotizacionRepository cotizacionRepository;

	@Autowired
	private VidaFreeRestClient cotizadorFreeRestClient;

	@Value("#{ environment['server.port'] }")
	private String serverPort;

	@Value("#{ environment['ruta.pdf.solicitud.publica'] }")
	private String rutaPDFSolicitud;

	@Value("#{ environment['ruta.plantilla.doc.linux'] }")
	private String rutaPlantillaDocLinux;

	@Value("${spring.profiles.active}")
	private String activeProfile;

	@Value("#{ environment['ruta.pdf.privada'] }")
	private String rutaPDFPrivada;

	@Value("#{ environment['persistence.rest.client.interseguro.correo.enviarCorreo.solicitud.remitente'] }")
	private String enviarCorreoSolicitudRemitente;

	@Value("#{ environment['persistence.rest.client.interseguro.correo.enviarCorreo.solicitud.remitente.display'] }")
	private String enviarCorreoSolicitudRemitenteDisplay;

	@Value("#{ environment['persistence.rest.client.interseguro.correo.enviarCorreo.acp.asunto'] }")
	private String enviarCorreoAcpAsunto;

	@Value("#{ environment['persistence.rest.client.interseguro.correo.enviarCorreo.solicitud.asunto'] }")
	private String enviarCorreoSolicitudAsunto;

	@Value("#{ environment['persistence.rest.client.interseguro.correo.enviarCorreo.solicitud.email.pruebas'] }")
	private String enviarCorreoSolicitudEmailPruebas;

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

	@Value("#{ environment['url.cotizacion.vidafree.pdf'] }")
	private String urlCotizacionVidaFreePDF;

	@Value("#{ environment['url.cotizacion.vidafree'] }")
	private String urlCotizacionVidaFree;

	@Value("#{ environment['ruta.pdf.publica'] }")
	private String rutaPDFPublica;

	@Value("#{ environment['url.cotizacion.pdf'] }")
	private String urlCotizacionPDF;

	@Value("#{ environment['pasarela.config.alerta.recurrente.cumulo.max'] }")
	private Double cumuloMaximoAlerta;

	@Value("#{ environment['pasarela.config.alerta.recurrente.imc.max'] }")
	private Double ICMMaximoAlerta;

	@Value("#{ environment['persistence.rest.client.interseguro.correo.enviarCorreo.destinatario.supervisor.alerta.pago'] }")
	private String enviarCorreoDestinatarioSupervisor;

	@Value("#{ environment['persistence.rest.client.interseguro.correo.enviarCorreo.solicitud.asunto.supervisor.alerta'] }")
	private String enviarCorreoSolicitudAlertaAsunto;

	@PersistenceContext
    private EntityManager entityManager;

	private String valorMultiTabla(String codigoTabla, String codigo, String campo) {
		AtomicReference<String> result = new AtomicReference<>();
		result.set("");
		if (StringUtils.isBlank(codigo)) {
			return result.get();
		}
		List<Multitabla> lista = multitablaRepository.findByCodigoTablaSinEstado(codigoTabla);
		if (!lista.isEmpty()) {
			lista.stream().filter(fm -> StringUtils.equals(codigo, fm.getCodigo())).forEach(m -> {
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

	private boolean checkOpcion(String codigo, String comparar) {
		return codigo != null && codigo.equals(comparar);
	}

	private String valorString(Object valor) {
		String result = Strings.EMPTY;

		if (valor != null) {
			result = valor.toString();
		}
		return result;
	}

	public File obtenerPlantilla(String plantilla) {
		String traza = Utilitarios.trazaLog() + "-" + plantilla;
		LOGGER.info("{} obtenerPlantilla", traza);
		File plantillaFile = null;
		String rutaPlantilla = "";
		String systemOp = System.getProperty("os.name");

		if (systemOp.contains("Windows")) {
			rutaPlantilla = Constantes.RUTA_PLANTILLA + "/" + plantilla;

			plantillaFile = new File(
					Objects.requireNonNull(getClass().getClassLoader().getResource(rutaPlantilla)).getFile());

		} else {
			plantillaFile = new File(this.rutaPlantillaDocLinux + "//" + plantilla);
		}
		LOGGER.info("{} end plantillaFile", traza);
		return plantillaFile;
	}

	private Map<String, Object> valoresParametrosDefault() {

		Map<String, Object> parametros = new HashMap<>();
		parametros.put(ConstantesAcpPDF.CODIGO_ACP_NUMERO_PROPUESTA, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesAcpPDF.CODIGO_NOMBRE_PRODUCTO, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesAcpPDF.CODIGO_ACP_NOMBRES_COMPLETO, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesAcpPDF.CODIGO_ACP_FIRMA_DIA, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesAcpPDF.CODIGO_ACP_FIRMA_MES, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesAcpPDF.CODIGO_ACP_FIRMA_ANIO, Constantes.ESPACIO_BLANCO);

		// Datos del Contratante
		parametros.put(ConstantesAcpPDF.CODIGO_ACP_APELLIDO_PATERNO, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesAcpPDF.CODIGO_ACP_APELLIDO_MATERNO, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesAcpPDF.CODIGO_ACP_NOMBRES, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesAcpPDF.CODIGO_ACP_RAZON_SOIAL, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesAcpPDF.CODIGO_ACP_DOCUMENTO_IDENTIDAD, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesAcpPDF.CODIGO_ACP_NUMERO_DOCUMENTO, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesAcpPDF.CODIGO_ACP_DIRECCION, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesAcpPDF.CODIGO_ACP_NRO_MZ_LT, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesAcpPDF.CODIGO_ACP_DPTO_OFI_INT, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesAcpPDF.CODIGO_ACP_DISTRITO, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesAcpPDF.CODIGO_ACP_PROVINCIA, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesAcpPDF.CODIGO_ACP_DEPARTAMENTO, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesAcpPDF.CODIGO_ACP_CELULAR, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesAcpPDF.CODIGO_ACP_EMAIL, Constantes.ESPACIO_BLANCO);

		// Datos de tarjeta
		parametros.put(ConstantesAcpPDF.CODIGO_ACP_TARJETA_MES_VENCIMIENTO_1, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesAcpPDF.CODIGO_ACP_TARJETA_MES_VENCIMIENTO_2, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesAcpPDF.CODIGO_ACP_TARJETA_ANIO_VENCIMIENTO_1, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesAcpPDF.CODIGO_ACP_TARJETA_ANIO_VENCIMIENTO_2, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesAcpPDF.CODIGO_ACP_TARJETA_ANIO_VENCIMIENTO_3, Constantes.ESPACIO_BLANCO);
		parametros.put(ConstantesAcpPDF.CODIGO_ACP_TARJETA_ANIO_VENCIMIENTO_4, Constantes.ESPACIO_BLANCO);
		for (int i = 1; i <= 16; i++) {
			parametros.put(MessageFormat.format(ConstantesAcpPDF.CODIGO_ACP_TARJETA_NUMERO, i),
					Constantes.ESPACIO_BLANCO);
		}

		// Datos de cuenta
		for (int i = 1; i <= 20; i++) {
			parametros.put(MessageFormat.format(ConstantesAcpPDF.CODIGO_ACP_CUENTA_NUMERO, i),
					Constantes.ESPACIO_BLANCO);
		}

		return parametros;
	}

	private List<String> valoresMetacamposDefault() {
		List<String> metacampos = new ArrayList<>();

		// Checks ACP
		metacampos.add(ConstantesAcpPDF.CODIGO_CONTRATANTE_DOCUMENTO_TDOC_DNI);
		metacampos.add(ConstantesAcpPDF.CODIGO_CONTRATANTE_DOCUMENTO_TDOC_RUC);
		metacampos.add(ConstantesAcpPDF.CODIGO_CONTRATANTE_DOCUMENTO_TDOC_CE);
		metacampos.add(ConstantesAcpPDF.CODIGO_CONTRATANTE_DOCUMENTO_TDOC_CI);
		metacampos.add(ConstantesAcpPDF.CODIGO_CONTRATANTE_DOCUMENTO_TDOC_OTRO);
		metacampos.add(ConstantesAcpPDF.CODIGO_CONTRATANTE_DIRECCION_VIA_JIRON);
		metacampos.add(ConstantesAcpPDF.CODIGO_CONTRATANTE_DIRECCION_VIA_AV);
		metacampos.add(ConstantesAcpPDF.CODIGO_CONTRATANTE_DIRECCION_VIA_CALLE);
		metacampos.add(ConstantesAcpPDF.CODIGO_CONTRATANTE_DIRECCION_VIA_OTRO);
		metacampos.add(ConstantesAcpPDF.CODIGO_FRECUENCIA_ANUAL);

		metacampos.add(ConstantesAcpPDF.CODIGO_FRECUENCIA_SEMESTRAL);
		metacampos.add(ConstantesAcpPDF.CODIGO_FRECUENCIA_TRIMESTRAL);
		metacampos.add(ConstantesAcpPDF.CODIGO_FRECUENCIA_MENSUAL);
		metacampos.add(ConstantesAcpPDF.CODIGO_TARJETA_VISA);
		metacampos.add(ConstantesAcpPDF.CODIGO_TARJETA_MASTERCARD);
		metacampos.add(ConstantesAcpPDF.CODIGO_TARJETA_AMEX);
		metacampos.add(ConstantesAcpPDF.CODIGO_TARJETA_DINERS);
		metacampos.add(ConstantesAcpPDF.CODIGO_CUENTA_TIPO_AHORRO);
		metacampos.add(ConstantesAcpPDF.CODIGO_CUENTA_TIPO_CORRIENTE);
		metacampos.add(ConstantesAcpPDF.CODIGO_CUENTA_TIPO_OTRO);
		metacampos.add(ConstantesAcpPDF.CODIGO_CUENTA_BANCO_CONTINENTAL);
		metacampos.add(ConstantesAcpPDF.CODIGO_CUENTA_BANCO_BCP);
		metacampos.add(ConstantesAcpPDF.CODIGO_CUENTA_BANCO_INTERBANK);
		metacampos.add(ConstantesAcpPDF.CODIGO_CUENTA_BANCO_SCOTIABANK);
		metacampos.add(ConstantesAcpPDF.CODIGO_CUENTA_MONEDA_SOLES);
		metacampos.add(ConstantesAcpPDF.CODIGO_CUENTA_MONEDA_DOLARES);

		return metacampos;
	}

	private Map<String, Object> valoresMetaDatosDefault(List<String> metacampos, File ImagenNOK) {
		Map<String, Object> metadatos = new HashMap<>();
		IImageProvider checkNok = new FileImageProvider(ImagenNOK, true);

		for (String campo : metacampos) {
			metadatos.put(campo, checkNok);
		}
		return metadatos;
	}
	private Map<String, Object> valoresMetaDatosDefaultV2(List<String> metacampos) {
		Map<String, Object> metadatos = new HashMap<>();
		String checkNok = Constantes.PLANTILLA_SOLICITUD_IMAGE_INCOMPLETE;

		for (String campo : metacampos) {
			metadatos.put(campo, checkNok);
		}
		return metadatos;
	}
	private Map<String, Object> completarParametros(AcpPDFBean acpPDFBean, Map<String, Object> parametros) {
		if (acpPDFBean != null) {
			parametros.put(ConstantesAcpPDF.CODIGO_ACP_NUMERO_PROPUESTA, acpPDFBean.getNumeroPropuesta());
			parametros.put(ConstantesAcpPDF.CODIGO_NOMBRE_PRODUCTO, acpPDFBean.getProducto());
			parametros.put(ConstantesAcpPDF.CODIGO_ACP_FIRMA_DIA, acpPDFBean.getFechaFirmaDia());
			parametros.put(ConstantesAcpPDF.CODIGO_ACP_FIRMA_MES, acpPDFBean.getFechaFirmaMes());
			parametros.put(ConstantesAcpPDF.CODIGO_ACP_FIRMA_ANIO, acpPDFBean.getFechaFirmaAnio());

			// Datos contratante
			if (acpPDFBean.getContratante() != null) {
				parametros.put(ConstantesAcpPDF.CODIGO_ACP_APELLIDO_PATERNO,
						acpPDFBean.getContratante().getApellidoPaterno());
				parametros.put(ConstantesAcpPDF.CODIGO_ACP_APELLIDO_MATERNO,
						acpPDFBean.getContratante().getApellidoMaterno());
				parametros.put(ConstantesAcpPDF.CODIGO_ACP_NOMBRES, acpPDFBean.getContratante().getNombres());
				parametros.put(ConstantesAcpPDF.CODIGO_ACP_NOMBRES_COMPLETO,
						acpPDFBean.getContratante().getNombresCompletos());
				parametros.put(ConstantesAcpPDF.CODIGO_ACP_DOCUMENTO_IDENTIDAD,
						acpPDFBean.getContratante().getNumeroDocumento());
				parametros.put(ConstantesAcpPDF.CODIGO_ACP_CELULAR, acpPDFBean.getContratante().getCelular());
				parametros.put(ConstantesAcpPDF.CODIGO_ACP_DIRECCION, acpPDFBean.getContratante().getDireccion());
				parametros.put(ConstantesAcpPDF.CODIGO_ACP_NRO_MZ_LT, acpPDFBean.getContratante().getNroMzLt());
				parametros.put(ConstantesAcpPDF.CODIGO_ACP_DPTO_OFI_INT, acpPDFBean.getContratante().getDptoOfInt());
				parametros.put(ConstantesAcpPDF.CODIGO_ACP_DISTRITO, acpPDFBean.getContratante().getDistrito());
				parametros.put(ConstantesAcpPDF.CODIGO_ACP_PROVINCIA, acpPDFBean.getContratante().getProvincia());
				parametros.put(ConstantesAcpPDF.CODIGO_ACP_DEPARTAMENTO, acpPDFBean.getContratante().getDepartamento());
				parametros.put(ConstantesAcpPDF.CODIGO_ACP_EMAIL, acpPDFBean.getContratante().getEmail());
			}

			// Datos Tarjeta
			if (acpPDFBean.getTarjeta() != null) {
				parametros.put(ConstantesAcpPDF.CODIGO_ACP_TARJETA_ANIO_VENCIMIENTO_1,
						acpPDFBean.getTarjeta().getVencimientoAnio1());
				parametros.put(ConstantesAcpPDF.CODIGO_ACP_TARJETA_ANIO_VENCIMIENTO_2,
						acpPDFBean.getTarjeta().getVencimientoAnio2());
				parametros.put(ConstantesAcpPDF.CODIGO_ACP_TARJETA_ANIO_VENCIMIENTO_3,
						acpPDFBean.getTarjeta().getVencimientoAnio3());
				parametros.put(ConstantesAcpPDF.CODIGO_ACP_TARJETA_ANIO_VENCIMIENTO_4,
						acpPDFBean.getTarjeta().getVencimientoAnio4());
				parametros.put(ConstantesAcpPDF.CODIGO_ACP_TARJETA_MES_VENCIMIENTO_1,
						acpPDFBean.getTarjeta().getVencimientoMes1());
				parametros.put(ConstantesAcpPDF.CODIGO_ACP_TARJETA_MES_VENCIMIENTO_2,
						acpPDFBean.getTarjeta().getVencimientoMes2());
				if (acpPDFBean.getTarjeta() != null && acpPDFBean.getTarjeta().getNumeroTarjeta() != null) {
					for (int i = 1; i <= acpPDFBean.getTarjeta().getNumeroTarjeta().length(); i++) {
						parametros.put(MessageFormat.format(ConstantesAcpPDF.CODIGO_ACP_TARJETA_NUMERO, i),
								acpPDFBean.getTarjeta().getNumeroTarjeta().charAt(i - 1));
					}
				}
			}

			// Datos Cuenta
			if (acpPDFBean.getCuenta() != null && acpPDFBean.getCuenta().getNumeroCuenta() != null) {
				for (int i = 1; i <= acpPDFBean.getCuenta().getNumeroCuenta().length(); i++) {
					parametros.put(MessageFormat.format(ConstantesAcpPDF.CODIGO_ACP_CUENTA_NUMERO, i),
							acpPDFBean.getCuenta().getNumeroCuenta().charAt(i - 1));
				}
			}

		}
		return parametros;
	}

	private Map<String, Object> completarMetadatos(AcpPDFBean acpPDFBean, Map<String, Object> metadatos,
			File imagenOK) {
		IImageProvider checkOk = new FileImageProvider(imagenOK, true);

		if (acpPDFBean.getContratante() != null) {
			if (acpPDFBean.getContratante().isCheckDocumentoDNI()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_CONTRATANTE_DOCUMENTO_TDOC_DNI, checkOk);
			}
			if (acpPDFBean.getContratante().isCheckDocumentoRUC()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_CONTRATANTE_DOCUMENTO_TDOC_RUC, checkOk);
			}
			if (acpPDFBean.getContratante().isCheckDocumentoCE()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_CONTRATANTE_DOCUMENTO_TDOC_CE, checkOk);
			}
			if (acpPDFBean.getContratante().isCheckDocumentoCI()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_CONTRATANTE_DOCUMENTO_TDOC_CI, checkOk);
			}
			if (acpPDFBean.getContratante().isCheckDocumentoOtro()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_CONTRATANTE_DOCUMENTO_TDOC_OTRO, checkOk);
			}

			if (acpPDFBean.getContratante().isCheckDireccionJR()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_CONTRATANTE_DIRECCION_VIA_JIRON, checkOk);
			}
			if (acpPDFBean.getContratante().isCheckDireccionAV()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_CONTRATANTE_DIRECCION_VIA_AV, checkOk);
			}
			if (acpPDFBean.getContratante().isCheckDireccionCA()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_CONTRATANTE_DIRECCION_VIA_CALLE, checkOk);
			}
			if (acpPDFBean.getContratante().isCheckDireccionOtro()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_CONTRATANTE_DIRECCION_VIA_OTRO, checkOk);
			}

			if (acpPDFBean.getContratante().isCheckFrecuenciaAnual()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_FRECUENCIA_ANUAL, checkOk);
			}
			if (acpPDFBean.getContratante().isCheckFrecuenciaMensual()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_FRECUENCIA_MENSUAL, checkOk);
			}
			if (acpPDFBean.getContratante().isCheckFrecuenciaSemestral()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_FRECUENCIA_SEMESTRAL, checkOk);
			}
			if (acpPDFBean.getContratante().isCheckFrecuenciaTrimestral()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_FRECUENCIA_TRIMESTRAL, checkOk);
			}
		}

		if (acpPDFBean.getTarjeta() != null) {
			if (acpPDFBean.getTarjeta().isCheckVisa()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_TARJETA_VISA, checkOk);
			}
			if (acpPDFBean.getTarjeta().isCheckMastercard()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_TARJETA_MASTERCARD, checkOk);
			}
			if (acpPDFBean.getTarjeta().isCheckAmex()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_TARJETA_AMEX, checkOk);
			}
			if (acpPDFBean.getTarjeta().isCheckDiners()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_TARJETA_DINERS, checkOk);
			}
		}

		if (acpPDFBean.getCuenta() != null) {
			if (acpPDFBean.getCuenta().isCheckTipoCuentaAhorro()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_CUENTA_TIPO_AHORRO, checkOk);
			}
			if (acpPDFBean.getCuenta().isCheckTipoCuentaCorriente()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_CUENTA_TIPO_CORRIENTE, checkOk);
			}
			if (acpPDFBean.getCuenta().isCheckTipoCuentaOtro()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_CUENTA_TIPO_OTRO, checkOk);
			}
			if (acpPDFBean.getCuenta().isCheckBancoContinental()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_CUENTA_BANCO_CONTINENTAL, checkOk);
			}
			if (acpPDFBean.getCuenta().isCheckBancoCredito()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_CUENTA_BANCO_BCP, checkOk);
			}
			if (acpPDFBean.getCuenta().isCheckBancoInterbank()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_CUENTA_BANCO_INTERBANK, checkOk);
			}
			if (acpPDFBean.getCuenta().isCheckBancoScotiabank()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_CUENTA_BANCO_SCOTIABANK, checkOk);
			}
			if (acpPDFBean.getCuenta().isCheckBancoOtro()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_CUENTA_BANCO_OTRO, checkOk);
			}
			if (acpPDFBean.getCuenta().isCheckMonedaSoles()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_CUENTA_MONEDA_SOLES, checkOk);
			}
			if (acpPDFBean.getCuenta().isCheckMonedaDolares()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_CUENTA_MONEDA_DOLARES, checkOk);
			}
		}

		return metadatos;
	}

	private Map<String, Object> completarMetadatosV2(AcpPDFBean acpPDFBean) {
		Map<String, Object> metadatos = new HashMap<>();
		String checkOk = Constantes.PLANTILLA_SOLICITUD_IMAGE_COMPLETE;
		if (acpPDFBean.getContratante() != null) {
			if (acpPDFBean.getContratante().isCheckDocumentoDNI()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_CONTRATANTE_DOCUMENTO_TDOC_DNI, checkOk);
			}
			if (acpPDFBean.getContratante().isCheckDocumentoRUC()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_CONTRATANTE_DOCUMENTO_TDOC_RUC, checkOk);
			}
			if (acpPDFBean.getContratante().isCheckDocumentoCE()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_CONTRATANTE_DOCUMENTO_TDOC_CE, checkOk);
			}
			if (acpPDFBean.getContratante().isCheckDocumentoCI()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_CONTRATANTE_DOCUMENTO_TDOC_CI, checkOk);
			}
			if (acpPDFBean.getContratante().isCheckDocumentoOtro()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_CONTRATANTE_DOCUMENTO_TDOC_OTRO, checkOk);
			}

			if (acpPDFBean.getContratante().isCheckDireccionJR()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_CONTRATANTE_DIRECCION_VIA_JIRON, checkOk);
			}
			if (acpPDFBean.getContratante().isCheckDireccionAV()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_CONTRATANTE_DIRECCION_VIA_AV, checkOk);
			}
			if (acpPDFBean.getContratante().isCheckDireccionCA()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_CONTRATANTE_DIRECCION_VIA_CALLE, checkOk);
			}
			if (acpPDFBean.getContratante().isCheckDireccionOtro()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_CONTRATANTE_DIRECCION_VIA_OTRO, checkOk);
			}

			if (acpPDFBean.getContratante().isCheckFrecuenciaAnual()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_FRECUENCIA_ANUAL, checkOk);
			}
			if (acpPDFBean.getContratante().isCheckFrecuenciaMensual()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_FRECUENCIA_MENSUAL, checkOk);
			}
			if (acpPDFBean.getContratante().isCheckFrecuenciaSemestral()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_FRECUENCIA_SEMESTRAL, checkOk);
			}
			if (acpPDFBean.getContratante().isCheckFrecuenciaTrimestral()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_FRECUENCIA_TRIMESTRAL, checkOk);
			}
		}

		if (acpPDFBean.getTarjeta() != null) {
			if (acpPDFBean.getTarjeta().isCheckVisa()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_TARJETA_VISA, checkOk);
			}
			if (acpPDFBean.getTarjeta().isCheckMastercard()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_TARJETA_MASTERCARD, checkOk);
			}
			if (acpPDFBean.getTarjeta().isCheckAmex()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_TARJETA_AMEX, checkOk);
			}
			if (acpPDFBean.getTarjeta().isCheckDiners()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_TARJETA_DINERS, checkOk);
			}
		}

		if (acpPDFBean.getCuenta() != null) {
			if (acpPDFBean.getCuenta().isCheckTipoCuentaAhorro()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_CUENTA_TIPO_AHORRO, checkOk);
			}
			if (acpPDFBean.getCuenta().isCheckTipoCuentaCorriente()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_CUENTA_TIPO_CORRIENTE, checkOk);
			}
			if (acpPDFBean.getCuenta().isCheckTipoCuentaOtro()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_CUENTA_TIPO_OTRO, checkOk);
			}
			if (acpPDFBean.getCuenta().isCheckBancoContinental()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_CUENTA_BANCO_CONTINENTAL, checkOk);
			}
			if (acpPDFBean.getCuenta().isCheckBancoCredito()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_CUENTA_BANCO_BCP, checkOk);
			}
			if (acpPDFBean.getCuenta().isCheckBancoInterbank()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_CUENTA_BANCO_INTERBANK, checkOk);
			}
			if (acpPDFBean.getCuenta().isCheckBancoScotiabank()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_CUENTA_BANCO_SCOTIABANK, checkOk);
			}
			if (acpPDFBean.getCuenta().isCheckBancoOtro()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_CUENTA_BANCO_OTRO, checkOk);
			}
			if (acpPDFBean.getCuenta().isCheckMonedaSoles()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_CUENTA_MONEDA_SOLES, checkOk);
			}
			if (acpPDFBean.getCuenta().isCheckMonedaDolares()) {
				metadatos.put(ConstantesAcpPDF.CODIGO_CUENTA_MONEDA_DOLARES, checkOk);
			}
		}

		return metadatos;
	}

	private File generarPDFTemporal(AcpPDFBean acpPDFBean) throws Exception {
		// String nombreArchivo = Strings.EMPTY;
		String traza = Utilitarios.trazaLog() + "-" + acpPDFBean.getNumeroPropuesta();
		LOGGER.info("{} Entro AcpPDFServiceImpl.generarPDFTemporal", traza);

		String nombreArchivo = "acp_generado";

		String plantilla = "Juridico".equals(acpPDFBean.getTipoContratante()) ? Constantes.PLANTILLA_ACP_JURIDICO
				: Constantes.PLANTILLA_ACP_NATURAL;
		LOGGER.info("{} plantilla {}", traza, plantilla);
		File plantillaSolicitud = this.obtenerPlantilla(plantilla);
		File imagenOK = this.obtenerPlantilla(Constantes.PLANTILLA_SOLICITUD_IMAGE_COMPLETE);
		File imagenNOK = this.obtenerPlantilla(Constantes.PLANTILLA_SOLICITUD_IMAGE_INCOMPLETE);
		LOGGER.info("{} plantillaSolicitud imagenOK imagenNOK  ", traza);
		Map<String, Object> parametros = this.valoresParametrosDefault();
		LOGGER.info("{} parametros  {}", traza, gson.toJson(parametros));
		List<String> metacampos = this.valoresMetacamposDefault();
		LOGGER.info("{} metacampos  {}", traza, gson.toJson(metacampos));
		Map<String, Object> metadatos = this.valoresMetaDatosDefault(metacampos, imagenNOK);

		parametros = this.completarParametros(acpPDFBean, parametros);
		metadatos = this.completarMetadatos(acpPDFBean, metadatos, imagenOK);
		LOGGER.info("{} parametros  y metadatos completos", traza);
		nombreArchivo = IXDocReportUtil.generarDocumento(plantillaSolicitud, nombreArchivo, parametros, true,
				metacampos, metadatos);

		File archivo = new File(System.getProperty("java.io.tmpdir").concat(File.separator).concat(nombreArchivo));

		LOGGER.info("{} Salió AcpPDFServiceImpl.generarPDFTemporal", traza);
		return archivo;
	}

	private File generarPDFTemporalV2(AcpPDFBean acpPDFBean, String traza) throws IOException, DocumentException {
		LOGGER.info("{} Entro a generarPDFTemporalV2", traza);
		String nombreArchivo = "acp_generado";

		String outputFolder = System.getProperty("java.io.tmpdir") + File.separator + nombreArchivo;

		LOGGER.info("{} Path {}", traza, outputFolder);
		OutputStream outputStream = new FileOutputStream(outputFolder);

		ITextRenderer renderer = new ITextRenderer();
		String html = this.parseThymeleafTemplate(acpPDFBean, traza);
		LOGGER.info("{} html", traza);
		renderer.setDocumentFromString(html);
		LOGGER.info("{} layout", traza);
		renderer.layout();
		LOGGER.info("{} createPDF", traza);
		renderer.createPDF(outputStream);
		LOGGER.info("{} end createPDF", traza);

		outputStream.close();
		/*try (OutputStream outputStream = Files.newOutputStream(Paths.get(outputFolder))) {
			ITextRenderer renderer = new ITextRenderer();
			String html = this.parseThymeleafTemplate(acpPDFBean, traza);
			LOGGER.info("{} html", traza);
			renderer.setDocumentFromString(html);
			LOGGER.info("{} layout", traza);
			renderer.layout();
			LOGGER.info("{} createPDF", traza);
			renderer.createPDF(outputStream);
			LOGGER.info("{} end createPDF", traza);
		} catch (Exception e) {
			LOGGER.error("{} ERROR {}", traza, e.getMessage(), e);
		}
		*/

		LOGGER.info("{} Salio a generarPDFTemporalV2", traza);

		return new File(outputFolder);
	}

	/**
	 * Obtiene la plantilla y devuelve el html parseado
	 *
	 * @param acpPDFBean data para mostar pdf
	 * @param traza      correlativo para trazabilidad de logs
	 * @return html.
	 */
	private String parseThymeleafTemplate(AcpPDFBean acpPDFBean, String traza) throws JsonProcessingException {
		LOGGER.info("{} entro a parseThymeleafTemplate", traza);
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setPrefix("templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode(TemplateMode.HTML);
		TemplateEngine templateEngine = new TemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);

		Context context = new Context();
		List<String> metacampos = this.valoresMetacamposDefault();
		Map<String, Object> metadatos = this.valoresMetaDatosDefaultV2(metacampos);
		context.setVariables(metadatos);
		ObjectMapper oMapper = new ObjectMapper();
		String jsonResult = oMapper.writerWithDefaultPrettyPrinter().writeValueAsString(acpPDFBean);
		Map<String, Object> map = gson.fromJson(jsonResult, new TypeToken<Map<String, Object>>() {
		}.getType());

		context.setVariables(map);
		isValueNumberTarjet(acpPDFBean, context);
		isValueNumberAccount(acpPDFBean, context);
		context.setVariables(this.completarMetadatosV2(acpPDFBean));
		LOGGER.info("{} context {}", traza, gson.toJson(context));

		String plantilla =  Constantes.PLANTILLA_ACP_NATURAL_HTML;
		if (acpPDFBean.getContratante().isCheckDocumentoRUC()) {
			plantilla=	Constantes.PLANTILLA_ACP_JURIDICO_HTML;
		}
		LOGGER.info("{} sale de parseThymeleafTemplate", traza);
		return templateEngine.process(plantilla, context);
	}

	/**
	 * Valida si tiene varlo numero de tarjeta y se asigna al context
	 *
	 * @param acpPDFBean
	 * @param context
	 */
	private static void isValueNumberTarjet(AcpPDFBean acpPDFBean, Context context) {
		if (!Objects.isNull(acpPDFBean.getTarjeta()) && !acpPDFBean.getTarjeta().getNumeroTarjeta().isEmpty()) {
			AtomicInteger i = new AtomicInteger();
			acpPDFBean.getTarjeta().getNumeroTarjeta().chars().mapToObj(c -> (char) c).forEach(ta -> {
				context.setVariable("t_" + (i.get() + 1), ta);
				i.getAndIncrement();
			});
		}
	}

	/**
	 * Valida si tiene varlo numero de tarjeta y se asigna al context
	 *
	 * @param acpPDFBean
	 * @param context
	 */
	private static void isValueNumberAccount(AcpPDFBean acpPDFBean, Context context) {
		if (!Objects.isNull(acpPDFBean.getCuenta()) && !acpPDFBean.getCuenta().getNumeroCuenta().isEmpty()) {
			AtomicInteger i = new AtomicInteger();
			acpPDFBean.getCuenta().getNumeroCuenta().chars().mapToObj(c -> (char) c).forEach(ta -> {
				context.setVariable("c_" + (i.get() + 1), ta);
				i.getAndIncrement();
			});
		}
	}

	private AcpPDFBean obtenerDatosACPPlantilla(Solicitud solicitud, SolicitudProducto producto, Persona persona,
			AcpFormularioTarjetaResponse tarjeta, AcpFormularioCuentaResponse cuenta) throws Exception {
		String traza = Utilitarios.trazaLog() + "-" + solicitud.getNumeroCotizacion();
		LOGGER.info("[{}] Entro AcpPDFServiceImpl.obtenerDatosACPPlantilla", traza);
		AcpPDFBean beanAcpPDF = new AcpPDFBean();

		LOGGER.info("{} Buscando persona", traza);

		if (!Constantes.SOLICITUD_ASEGURADO_IGUAL_CONTRATANTE_TRUE.equals(solicitud.getAseguradoIgualContratante())) {
			persona = personaRepository.findById(solicitud.getPersonaByIdContratante().getIdPersona()).get();
			LOGGER.info("{} Persona es contratante", traza);
		}

		beanAcpPDF.setNumeroPropuesta(this.valorString(solicitud.getNumeroPropuesta()));
		String nombreProducto = "1".equals(producto.getTipoProducto()) ? "Plan Garantizado" : "Vida Free";
		beanAcpPDF.setProducto(nombreProducto);
		String simboloMoneda = "1".equals(solicitud.getMoneda()) ? "S/." : "$";
		beanAcpPDF.setMontoPrimeraPrima(simboloMoneda + producto.getPrimaComercial().toString());
		beanAcpPDF.setFrecuenciaPago(producto.getFrecuencia());
		beanAcpPDF.setAgenteCorreo(solicitud.getAgenteCorreo());

		LOGGER.info("Seteando datos de persona");
		AcpPDFPersonaBean contratante = new AcpPDFPersonaBean();

		String apellidoPaterno = this.valorString(persona.getApellidoPaterno());
		String apellidoMaterno = this.valorString(persona.getApellidoMaterno());
		String nombres = this.valorString(persona.getNombres());
		String nombreCompleto = apellidoPaterno + " " + apellidoMaterno + " " + nombres;
		if(persona.getTipoDocumento().equals(ConstantesSolicitudPDF.MULTITABLA_DOCUMENTO_RUC_JURIDICO)){
			nombreCompleto = persona.getRazonSocial() ;
		}

		contratante.setApellidoPaterno(apellidoPaterno);
		contratante.setApellidoMaterno(apellidoMaterno);
		contratante.setNombres(nombres);
		contratante.setRazonSocial(this.valorString(persona.getRazonSocial()));
		contratante.setNombresCompletos(nombreCompleto);
		contratante.setCheckDocumentoDNI(
				this.checkOpcion(persona.getTipoDocumento(), ConstantesSolicitudPDF.MULTITABLA_DOCUMENTO_DNI));
		contratante.setCheckDocumentoCE(
				this.checkOpcion(persona.getTipoDocumento(), ConstantesSolicitudPDF.MULTITABLA_DOCUMENTO_CE));
		contratante.setCheckDocumentoRUC(
				this.checkOpcion(persona.getTipoDocumento(), ConstantesSolicitudPDF.MULTITABLA_DOCUMENTO_RUC_JURIDICO));
		if (!contratante.isCheckDocumentoRUC()) {
			contratante.setCheckDocumentoRUC(this.checkOpcion(persona.getTipoDocumento(),
					ConstantesSolicitudPDF.MULTITABLA_DOCUMENTO_RUC_NATURAL));
		}
		contratante.setNumeroDocumento(this.valorString(persona.getNumeroDocumento()));
		contratante.setCheckDireccionJR(
				this.checkOpcion(persona.getDireccionTipo(), ConstantesSolicitudPDF.MULTITABLA_DIRECCION_JIRON));
		contratante.setCheckDireccionAV(
				this.checkOpcion(persona.getDireccionTipo(), ConstantesSolicitudPDF.MULTITABLA_DIRECCION_AVENIDA));
		contratante.setCheckDireccionCA(
				this.checkOpcion(persona.getDireccionTipo(), ConstantesSolicitudPDF.MULTITABLA_DIRECCION_CALLE));
		contratante.setCheckDireccionOtro(
				this.checkOpcion(persona.getDireccionTipo(), ConstantesSolicitudPDF.MULTITABLA_DIRECCION_PASAJE));
		contratante.setDireccion(this.valorString(persona.getDireccionTipoDes()));
		String interior = this.valorString(persona.getDireccionInterior());
		String nro = this.valorString(persona.getDireccionNroMz());
		PersonaUbigeoBean personaUbigeoBean= personaRepository.findByNumeroDocumentoUbigeo(persona.getNumeroDocumento(),
								Constantes.MULTITABLA_CODIGO_DEPARTAMENTO,
								Constantes.MULTITABLA_CODIGO_PROVINCIA,
								Constantes.MULTITABLA_CODIGO_DISTRITO);
		String distrito = personaUbigeoBean.getDistrito();
		String provincia = personaUbigeoBean.getProvincia();
		String departamento = personaUbigeoBean.getDepartamento();
		contratante.setDptoOfInt(interior);
		contratante.setNroMzLt(nro);
		contratante.setDistrito(distrito);
		contratante.setProvincia(provincia);
		contratante.setDepartamento(departamento);
		contratante.setCelular(this.valorString(persona.getCelular()));

		contratante.setCheckFrecuenciaAnual(this.checkOpcion(producto.getFrecuencia(),
				ConstantesSolicitudPDF.MULTITABLA_PRODUCTO_FRECUENCIA_ANUAL));
		contratante.setCheckFrecuenciaMensual(this.checkOpcion(producto.getFrecuencia(),
				ConstantesSolicitudPDF.MULTITABLA_PRODUCTO_FRECUENCIA_MENSUAL));
		contratante.setCheckFrecuenciaTrimestral(this.checkOpcion(producto.getFrecuencia(),
				ConstantesSolicitudPDF.MULTITABLA_PRODUCTO_FRECUENCIA_TRIMESTRAL));
		contratante.setCheckFrecuenciaSemestral(this.checkOpcion(producto.getFrecuencia(),
				ConstantesSolicitudPDF.MULTITABLA_PRODUCTO_FRECUENCIA_SEMESTRAL));
		contratante.setEmail(this.valorString(persona.getCorreo()));

		LOGGER.info("{} Seteando datos de firma", traza);
		String fechaSolicitud = DateUtil.dateToString(solicitud.getFechaSolicitud(), DateUtil.FORMATO_DIA_DDMMYYYY);
		String fechaSolicitudDia = Constantes.ESPACIO_BLANCO;
		String fechaSolicitudMes = Constantes.ESPACIO_BLANCO;
		String fechaSolicitudAnio = Constantes.ESPACIO_BLANCO;

		if (fechaSolicitud.length() == 10) {
			String[] arrayFecha = fechaSolicitud.split("/");
			fechaSolicitudDia = arrayFecha[0];
			fechaSolicitudMes = arrayFecha[1];
			fechaSolicitudAnio = arrayFecha[2];
		}

		beanAcpPDF.setFechaFirmaDia(fechaSolicitudDia);
		beanAcpPDF.setFechaFirmaMes(Utilitarios.nombreMes(fechaSolicitudMes));
		beanAcpPDF.setFechaFirmaAnio(fechaSolicitudAnio);

		beanAcpPDF.setContratante(contratante);

		LOGGER.info("{} Seteando datos de tarjeta", traza);
		AcpPDFTarjetaBean tarjetaInput = new AcpPDFTarjetaBean();
		if (tarjeta != null) {
			LOGGER.info("{} Es tarjeta", traza);
			LOGGER.info("{} tarjeta {}", traza, gson.toJson(tarjeta));
			tarjetaInput.setNumeroTarjeta(tarjeta.getNumeroTarjeta().replace("-", "").replace(" ", ""));
			switch (tarjeta.getEntidadFinanciera()) {
			case ConstantesAcpPDF.REPORTE_OPERADOR_FINANCIERO_VISA:
				tarjetaInput.setCheckVisa(true);
				break;
			case ConstantesAcpPDF.REPORTE_OPERADOR_FINANCIERO_MASTERCARD:
				tarjetaInput.setCheckMastercard(true);
				break;
			case ConstantesAcpPDF.REPORTE_OPERADOR_FINANCIERO_AMEX:
				tarjetaInput.setCheckAmex(true);
				break;
			case ConstantesAcpPDF.REPORTE_OPERADOR_FINANCIERO_DINERS:
				tarjetaInput.setCheckDiners(true);
				break;
			default:
				break;
			}

			String[] vencimientoArray = null;
			String vencimientoMes = Strings.EMPTY;
			String vencimientoMesD1 = Strings.EMPTY;
			String vencimientoMesD2 = Strings.EMPTY;
			String vencimientoAnioD1 = Strings.EMPTY;
			String vencimientoAnioD2 = Strings.EMPTY;
			String vencimientoAnioD3 = Strings.EMPTY;
			String vencimientoAnioD4 = Strings.EMPTY;
			String vencimientoAnio = Strings.EMPTY;
			String vencimiento = tarjeta.getFechaVencimiento();
			if (vencimiento != null && vencimiento.length() == 5) {
				vencimientoArray = vencimiento.split("/");
				vencimientoMes = vencimientoArray[0];
				vencimientoAnio = "20".concat(vencimientoArray[1]);
			}
			if (vencimientoMes.length() == 2) {
				vencimientoMesD1 = vencimientoMes.substring(0, 1);
				vencimientoMesD2 = vencimientoMes.substring(1, 2);
			}
			if (vencimientoAnio.length() == 4) {
				vencimientoAnioD1 = vencimientoAnio.substring(0, 1);
				vencimientoAnioD2 = vencimientoAnio.substring(1, 2);
				vencimientoAnioD3 = vencimientoAnio.substring(2, 3);
				vencimientoAnioD4 = vencimientoAnio.substring(3, 4);
			}
			tarjetaInput.setVencimientoMes1(vencimientoMesD1);
			tarjetaInput.setVencimientoMes2(vencimientoMesD2);
			tarjetaInput.setVencimientoAnio1(vencimientoAnioD1);
			tarjetaInput.setVencimientoAnio2(vencimientoAnioD2);
			tarjetaInput.setVencimientoAnio3(vencimientoAnioD3);
			tarjetaInput.setVencimientoAnio4(vencimientoAnioD4);
			LOGGER.info("{} tarjetaInput {}", traza, gson.toJson(tarjetaInput));
			beanAcpPDF.setTarjeta(tarjetaInput);
		}

		LOGGER.info("{} Seteando datos de cuenta", traza);
		AcpPDFCuentaBean cuentaInput = new AcpPDFCuentaBean();
		if (cuenta != null) {
			LOGGER.info("{} Es cuenta", traza);
			LOGGER.info("{}  Es cuenta {}", traza, gson.toJson(cuenta));
			cuentaInput.setNumeroCuenta(cuenta.getNumeroCuenta().replace("-", "").replace(" ", ""));
			switch (cuenta.getEntidadBancaria()) {
			case ConstantesAcpPDF.REPORTE_ENTIDAD_FINANCIERA_BCP:
				cuentaInput.setCheckBancoCredito(Boolean.TRUE);
				break;
			case ConstantesAcpPDF.REPORTE_ENTIDAD_FINANCIERA_INTERBANK:
				cuentaInput.setCheckBancoInterbank(Boolean.TRUE);
				break;
			case ConstantesAcpPDF.REPORTE_ENTIDAD_FINANCIERA_CONTINENTAL:
				cuentaInput.setCheckBancoContinental(Boolean.TRUE);
				break;
			case ConstantesAcpPDF.REPORTE_ENTIDAD_FINANCIERA_SCOTIABANK:
				cuentaInput.setCheckBancoScotiabank(Boolean.TRUE);
				break;
			default:
				break;
			}
			switch (cuenta.getTipoCuenta()) {
			case ConstantesAcpPDF.REPORTE_ENTIDAD_FINANCIERA_TIPO_CUENTA_CORRIENTE:
				cuentaInput.setCheckTipoCuentaCorriente(Boolean.TRUE);
				break;
			case ConstantesAcpPDF.REPORTE_ENTIDAD_FINANCIERA_TIPO_CUENTA_AHORRO:
				cuentaInput.setCheckTipoCuentaAhorro(Boolean.TRUE);
				break;
			default:
				cuentaInput.setCheckTipoCuentaOtro(Boolean.TRUE);
				break;
			}

			if (ConstantesAcpPDF.REPORTE_ENTIDAD_FINANCIERA_MONEDA_DOLARES.equals(cuenta.getMoneda())) {
				cuentaInput.setCheckMonedaDolares(Boolean.TRUE);
				cuentaInput.setCheckMonedaSoles(Boolean.FALSE);
			}else{
				cuentaInput.setCheckMonedaDolares(Boolean.FALSE);
				cuentaInput.setCheckMonedaSoles(Boolean.TRUE);
			}
			LOGGER.info("{} cuentaInput {}", traza, gson.toJson(cuentaInput));
			beanAcpPDF.setCuenta(cuentaInput);
		}

		LOGGER.info("{} Salió AcpPDFServiceImpl.obtenerDatosACPPlantilla", traza);
		return beanAcpPDF;
	}

	private void enviarPDFCRM2(File fileTmp, String tipoDocumentoCRM, String numeroDocumento, String nombreArchivoPDF,
			String descripcionArchivo, String tipoDocumentoInput,String contactVal) {
		try {
			if (fileTmp.exists()) {
				UploadArchivoRequest requestArchivo = new UploadArchivoRequest();
				requestArchivo.setTipoDocumento(tipoDocumentoCRM);
				requestArchivo.setNumeroDocumento(numeroDocumento);
				requestArchivo.setFile(fileTmp);
				requestArchivo.setNombreArchivo(nombreArchivoPDF);
				requestArchivo.setDescripcionArchivo(descripcionArchivo);
				requestArchivo.setTipoDocumentoAdn(tipoDocumentoInput);
				requestArchivo.setContactVal(contactVal);
				LOGGER.info("enviarPDFCRM.requestArchivo=> {}" , gson.toJson(requestArchivo));
				UploadArchivoResponse responseUploadCRM = globalRestClient.uploadArchivoV2(requestArchivo);
				LOGGER.info("enviarPDFCRM.responseUploadCRM=> {}" , gson.toJson(responseUploadCRM));
			}
		} catch (Exception e) {
			LOGGER.error("Error enviar PDF Solicitud a CRM=> {}" , e.getMessage(),e);
		}
	}

	@Async("processExecutorComunicaciones")
	public void enviarCorreoConAdjunto(String asunto, String email, String body, List<Adjuntos> files, int solicitud, String cotizacion, String documento) {
		// -- Enviar
		StringBuilder sbAsunto = new StringBuilder();
		EnviarCorreoRequestNuevo enviarCorreoRequest = new EnviarCorreoRequestNuevo();
		enviarCorreoRequest.setTitle("Encuesta de clientes");
		String[] correoSeparado = enviarCorreoSolicitudEmailPruebas.split(",");
		List<Remitente> listaRemitente = new ArrayList<>();
		Remitente remitente;

		if (!serverPort.equals(Constantes.SERVER_PORT_PRODUCCION)) {
			for (String correo : correoSeparado) {
				remitente = new Remitente();
				remitente.setEmail(correo);
				listaRemitente.add(remitente);
			}
			sbAsunto.append("TEST:");
		} else {
			String[] correoSeparadoPrd = email.split(",");
			for (String correo : correoSeparadoPrd) {
				remitente = new Remitente();
				remitente.setEmail(correo);
				listaRemitente.add(remitente);
			}
		}
		enviarCorreoRequest.setTo(listaRemitente);
		sbAsunto.append(asunto);

		enviarCorreoRequest.setSubject(sbAsunto.toString());
		enviarCorreoRequest.setHtmlContent(body);
		enviarCorreoRequest.setAttachments(files);

		LOGGER.info("EnviarCorreoRequest=> {}", gson.toJson(enviarCorreoRequest));

		try {
			interseguroRestClient.enviarCorreo(enviarCorreoRequest,"ACP",cotizacion,documento);
		} catch (Exception ex) {
			LOGGER.error("interseguroRestClient.enviarCorreo: {}", email, ex);
		}
	}

	// Hay que optimizar y pasar método a un componente comun
	@Async
	public void procesarPlantilla(String asunto, String destinatarios, String plantilla, Map<String, String> params,
			List<Adjuntos> archivosAdjuntos, String motivo, String cotizacion, String documento) throws IOException {

		InputStream stream = new ClassPathResource(Constantes.RUTA_PLANTILLA + "/" + plantilla).getInputStream();

		String systemOp = System.getProperty("os.name");
		if (!systemOp.contains("Windows")) {
			stream = Files.newInputStream(new File(rutaPlantillaDocLinux + "//" + plantilla).toPath());
		}

		String bodyAsegurado = Utilitarios.valorString(StreamUtils.copyToString(stream, Charset.defaultCharset()));

		LOGGER.info("params {}",gson.toJson(params));
		for (Map.Entry<String, String> entry : params.entrySet()) {
			LOGGER.info("key= {} ,value= {}", entry.getKey(), entry.getValue());
			bodyAsegurado = bodyAsegurado.replaceAll(entry.getKey(), entry.getValue());
		}
		Solicitud solicitud = solicitudRepository.findByNumeroCotizacion(cotizacion);
		this.enviarCorreoConAdjunto(asunto, destinatarios, bodyAsegurado, archivosAdjuntos, Math.toIntExact(solicitud.getIdSolicitud()), cotizacion, documento);
	}

	@Async("processExecutorComunicaciones")
	public void enviarCorreoSupervisor(Solicitud solicitud, List<Adjuntos> archivosAdjuntos, Persona asegurado) {
		SolicitudDps solicitudDPS = solicitudDpsRepository.findByIdSolicitud(solicitud.getIdSolicitud());
		List<SolicitudDpsPregunta> solicitudDpsPreguntas = solicitudDpsPreguntaRepository
				.findByIdSolicitud(solicitud.getIdSolicitud());
		boolean flagSIDps = false;
		for (SolicitudDpsPregunta pregunta : solicitudDpsPreguntas) {
			if (Boolean.TRUE.equals(pregunta.getRespuesta())) {
				flagSIDps = true;
				break;
			}
		}

		CotizadorCumuloResponseDTO cumulo = cotizaService.obtenerCumulo(asegurado.getTipoDocumento(),
				asegurado.getNumeroDocumento());

		LOGGER.info("IMC.default={}, Cumulo.default= {}", this.ICMMaximoAlerta, this.cumuloMaximoAlerta);
		LOGGER.info("Flag={},IMC={}, Cumulo= {}", flagSIDps, solicitudDPS.getImc(), cumulo.getCumuloDolares());
		if (flagSIDps || solicitudDPS.getImc() > this.ICMMaximoAlerta
				|| cumulo.getCumuloDolares() >= this.cumuloMaximoAlerta) {
			String plantilla = Constantes.PLANTILLA_ACP_CORREO_SUPERVISOR;
			Map<String, String> params = new HashMap<>();
			params.put("\\{numeroPropuesta\\}", solicitud.getNumeroPropuesta());
			params.put("\\{cliente\\}", asegurado.getNombres() + " " + asegurado.getApellidoPaterno() + " "
					+ asegurado.getApellidoMaterno());
			params.put("\\{agente\\}", solicitud.getAgenteNombres());

			String destinatarios = this.enviarCorreoDestinatarioSupervisor;

			String asunto = this.enviarCorreoSolicitudAlertaAsunto;
			try {
				this.procesarPlantilla(asunto, destinatarios, plantilla, params, archivosAdjuntos, "ACP", solicitud.getNumeroCotizacion(), asegurado.getNumeroDocumento());

			} catch (IOException e) {
				// Auto-generated catch block
				LOGGER.error("Error enviar correo supervisor {}", e.getMessage());
			}
		}

	}

	private File generarArchivo(Solicitud solicitud, SolicitudProducto solicitudProducto, Persona persona) {
		long startTime = System.nanoTime();
		long endTime;
		String traza = Utilitarios.trazaLog() + "-" + solicitud.getNumeroPropuesta();
		LOGGER.info("{} Entro AcpPDFServiceImpl#generarArchivo", traza);

		File archivoPDFTemporal = null;
		try {
			List<SolicitudViaCobro> viasCobro = solicitudViaCobroRepository.findByViaCobro(solicitud.getIdViaCobro());

			int tipoViaCobro = viasCobro.get(0).getTipoViaCobro() == 1 ? 2 : 1;

			AcpFormularioTarjetaResponse tarjeta = null;
			AcpFormularioCuentaResponse cuenta = null;
			if (Constantes.SAMP_TIPO_VIA_COBRO_TARJETA == tipoViaCobro) {
				tarjeta = new AcpFormularioTarjetaResponse();
				tarjeta.setNumeroTarjeta(solicitud.getTarjetaAfiliacion());
				tarjeta.setCcv("***");
				tarjeta.setEntidadFinanciera(solicitud.getIdViaCobro().toString());
				tarjeta.setFechaVencimiento("");
			} else {
				cuenta = new AcpFormularioCuentaResponse();
				cuenta.setEntidadBancaria(solicitud.getIdViaCobro().toString());
				cuenta.setNumeroCuenta(solicitud.getTarjetaAfiliacion());
				cuenta.setTipoCuenta(String.valueOf(solicitud.getIdCuenta()));
				LOGGER.info("{} Moneda ==> {}", traza, solicitud.getIdCuentaMoneda());
				cuenta.setMoneda(solicitud.getIdCuentaMoneda());
			}
			int estado = Integer.parseInt(solicitud.getEstado());
			if (estado >= 4) {
				AcpPDFBean acpPDFBean = this.obtenerDatosACPPlantilla(solicitud, solicitudProducto, persona, tarjeta,
						cuenta);
				// archivo acp temporal en master
				 archivoPDFTemporal = this.generarPDFTemporal(acpPDFBean);
				// nuevo archivo temporal
				//archivoPDFTemporal = this.generarPDFTemporalV2(acpPDFBean, traza);
			}
		} catch (Exception e) {
			LOGGER.error("{} error {}", traza, e.getMessage());
		}
		endTime = System.nanoTime();
		LOGGER.info("{} Fin generar archivo. diferencia {}", traza, (endTime - startTime));
		return archivoPDFTemporal;
	}

	public BaseResponseDTO generarPDFPrueba(String numeroPropuesta, int tag) {
		String codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_EXITO;
		String mensajeRespuesta = Strings.EMPTY;

		BaseResponseDTO respuesta = new BaseResponseDTO();
		Solicitud solicitud = solicitudRepository.findByNumeroCotizacion(numeroPropuesta);

		SolicitudFiltrado solicitudFiltrado = solicitudFiltradoRepository
				.findByNumeroCotizacion(solicitud.getNumeroPropuesta());

		ObtenerMensajeEmisionesResponse globalArchivoResponse = globalRestClient.validarArchivos(
				solicitudFiltrado.getTipoDocumento(), solicitudFiltrado.getNumeroDocumento(),
				solicitud.getNumeroPropuesta());

		if (globalArchivoResponse.getCotizacion().equals("completado")
				&& globalArchivoResponse.getSolicitud().equals("completado")) {
			LOGGER.info("LINK-PAGO ==> Validacion Documentos completadoss");
		}
		respuesta.setCodigoRespuesta(codigoRespuesta);
		respuesta.setMensajeRespuesta(mensajeRespuesta);
		return respuesta;
	}

	@Override
	public BaseResponseDTO generarPDF(String numeroPropuesta, int tag) {
		long startTime = System.nanoTime();
		long endTime;
		String codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_EXITO;
		String mensajeRespuesta = Strings.EMPTY;

		String traza = Utilitarios.trazaLog() + "-" + numeroPropuesta;
		LOGGER.info("[{}] Entro AcpPDFServiceImpl#generarPDF", traza);
		BaseResponseDTO respuesta = new BaseResponseDTO();

		try {
			Solicitud solicitud = solicitudRepository.findByNumeroCotizacion(numeroPropuesta);
			SolicitudProducto solicitudProducto = solicitudProductoRepository
					.findByIdSolicitud(solicitud.getIdSolicitud());
			Persona personaAsegurado = personaRepository.findById(solicitud.getIdAsegurado()).get();
			Persona personaCopiada = copyPersonaData(personaAsegurado);

			File archivoPDFTemporal = this.generarArchivo(solicitud, solicitudProducto, personaCopiada);
			endTime = System.nanoTime();
			LOGGER.info("{} diferencia generarPDF archivo {}", traza, (endTime - startTime));
			if (archivoPDFTemporal == null) {
				throw new SivTXException(Utilitarios.obtenerMensaje(messageSource,
						new Object[] { Constantes.MENSAJE_ADMINISTRADOR }, Constantes.MENSAJE_RESPUESTA_GENERAL_ERR0R),
						null);
			}

			// Obtener PDF ACP
			String nombreArchivoAcp = "AcpDigital_" +
					personaAsegurado.getNumeroDocumento() +
					"_" +
					solicitud.getNumeroPropuesta() +
					".pdf";
			
			LOGGER.info("{} Archivo ACP estado_general antes guardar {} {}", traza, solicitud.getEstadoGeneral(), solicitud.getDocumentoAcp());
			solicitudRepository.updateNombreDocumentoAcp(nombreArchivoAcp, solicitud.getIdSolicitud());
			LOGGER.info("{} Archivo ACP estado_general despues guardar {}", traza, nombreArchivoAcp);
			solicitud.setDocumentoAcp(nombreArchivoAcp);
			solicitudRepository.flush();
			entityManager.refresh(solicitud);

			File acpPDFTemporal = new File(
					System.getProperty("java.io.tmpdir") + File.separator + new File(nombreArchivoAcp));
			archivoPDFTemporal.renameTo(acpPDFTemporal);



			Multitabla multiTipoDocumento= multitablaRepository.findByCodigoAndCodigoTabla(personaAsegurado.getTipoDocumento(),
										Constantes.MULTITABLA_CODIGO_TABLA_TIPO_DOCUMENTO);
			String tipoDocumentoCRM =multiTipoDocumento.getValorCrm();
			String numeroDocumento = personaAsegurado.getNumeroDocumento();
			String tipoDocumento = personaAsegurado.getTipoDocumento();
			LOGGER.info("[{}] Ingresa a enviar el documento a la nube", traza);

			String contactVal="";
			 Persona persona=personaRepository.findByNumeroDocumento(personaAsegurado.getNumeroDocumento());
			if (persona != null && persona.getIdPersonaCS() != null) {
				contactVal=persona.getIdPersonaCS();
			} else {
				String tipoDocVal = personaAsegurado.getTipoDocumento().equals("1")?"DNI":"CARNET DE EXTRANJERIA";
				//Ini obtener personaIdpor documento
				ObtenerIdPersonaRequest obtenerIdPersonaRequest = new ObtenerIdPersonaRequest();
				obtenerIdPersonaRequest.setNumero_documento(personaAsegurado.getNumeroDocumento());
				obtenerIdPersonaRequest.setTipo_documento(tipoDocVal);
				ListObtenerIdPersonaResponse responseObtenerPersonaDocumento = globalRestClient.obtenerPersonaDocumento(obtenerIdPersonaRequest);
				LOGGER.info("{} responseObtenerPersonaDocumento {}",traza, gson.toJson(responseObtenerPersonaDocumento));
				//Fin obtener personaIdpor documento
				List<ObtenerPersonaDocumentoResponse> lista = new ArrayList<>();
				ObtenerUploadArchivoResponse uploadArchivoResponse = new ObtenerUploadArchivoResponse();
				responseObtenerPersonaDocumento.getData().forEach(cot -> {
					ObtenerPersonaDocumentoResponse item = new ObtenerPersonaDocumentoResponse();
					item.setID_PERSONA(cot.getID_PERSONA().toLowerCase());
					lista.add(item);
					ArchivoResponse[] listFiles = globalRestClient.getFilesStorage(cot.getID_PERSONA());

					if (listFiles.length > 0) {
						uploadArchivoResponse.setContactId(cot.getID_PERSONA().toLowerCase());
					}
				});
				contactVal = getContactVal(lista, uploadArchivoResponse);
				if(persona!=null){
					persona.setIdPersonaCS(contactVal);
					personaRepository.save(persona);
				}
			}

			LOGGER.info("[" + traza + "] Antes de enviarPDFCRM2 -----");
			this.enviarPDFCRM2(acpPDFTemporal, tipoDocumentoCRM, numeroDocumento, nombreArchivoAcp, nombreArchivoAcp,
					tipoDocumento,contactVal);
			LOGGER.info("[" + traza + "] Despues de enviarPDFCRM2 -----");
			Thread.sleep(2000);

			List<Adjuntos> files = new ArrayList<>();
			Adjuntos adjunto;
			LOGGER.info("[{}] Obtenemos los documentos en la nube", traza);

			String acpDigital = "AcpDigital_" + numeroDocumento + "_" + numeroPropuesta;
			String cotizacion = "cotizacion_" + numeroPropuesta;
			String cotizacion2 = "cotizacion-" + numeroPropuesta;
			String solicitudDigital = "SolicitudDigital_" + numeroDocumento + "_" + numeroPropuesta;
			String templateFile = null;
			ObtenerUploadArchivoResponse globalArchivoResponse = globalRestClient.obtenerArchivo(
					personaAsegurado.getTipoDocumento(), personaAsegurado.getNumeroDocumento(), numeroPropuesta);
			
			for (ArchivoResponse item : globalArchivoResponse.getFiles()) {
				if (item.getName().contains(acpDigital)) {
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
		
			String plantilla = Constantes.PLANTILLA_ACP_CORREO_CONFIRM_VF;
			LOGGER.info("[{}] Obtenemos la plantilla para el correo", traza);
			if ("1".equals(solicitudProducto.getTipoProducto())) {
				plantilla = MessageFormat.format(Constantes.PLANTILLA_ACP_CORREO_CONFIRM_PG, solicitud.getSubplan());
			}

			InputStream stream = new ClassPathResource(Constantes.RUTA_PLANTILLA + "/" + plantilla).getInputStream();

			String systemOp = System.getProperty("os.name");
			if (!systemOp.contains("Windows")) {
				stream = Files.newInputStream(new File(rutaPlantillaDocLinux + "//" + plantilla).toPath());
			}

			String bodyAsegurado = Utilitarios.valorString(StreamUtils.copyToString(stream, Charset.defaultCharset()));
			String mensajeCorreo = "Afiliaci&oacute;n realizada con &eacute;xito";
			if (solicitud.getIdPagoCulqi() != null) {
				mensajeCorreo = "Pago y afiliaci&oacute;n exitosos";
			}


			String monedaFormato = Constantes.MONEDA_ADN_SOLES.equals(solicitud.getMoneda())
					? Constantes.MONEDA_SIMBOLO_SOLES
					: Constantes.MONEDA_SIMBOLO_DOLARES;
			String montoFormato = monedaFormato + " "
					+ Utilitarios.formatoMiles(Double.valueOf(String.valueOf(solicitudProducto.getPrimaComercial())),
							Utilitarios.FORMATO_MILES_CON_DECIMAL);

			String productoPlan = "";
			if ("1".equals(solicitudProducto.getTipoProducto())) {
				productoPlan = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_SUBPLAN_COTIZADOR,
						solicitud.getSubplan(), Constantes.MULTITABLA_COLUMNA_VALOR);
			}else{
				productoPlan = this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_SUBPLAN_COTIZADOR_VIDA_FREE,
						solicitud.getSubplan(), Constantes.MULTITABLA_COLUMNA_VALOR);
			}
			String proximoPago = solicitud.getTarjetaAfiliacion();

			bodyAsegurado = bodyAsegurado.replaceAll("\\{mensaje\\}", mensajeCorreo);
			bodyAsegurado = bodyAsegurado.replaceAll("\\{numeroSolicitud\\}", solicitud.getNumeroCotizacion());
			montoFormato = Matcher.quoteReplacement(montoFormato);
			bodyAsegurado = bodyAsegurado.replaceAll("\\{montoPrimeraPrima}", montoFormato);
			bodyAsegurado = bodyAsegurado.replaceAll("\\{producto\\}", productoPlan);
			bodyAsegurado = bodyAsegurado.replaceAll("\\{frecuenciaPago\\}",
					this.valorMultiTabla(Constantes.MULTITABLA_CODIGO_FRECUENCIA, solicitudProducto.getFrecuencia(),
							Constantes.MULTITABLA_COLUMNA_VALOR));
			bodyAsegurado = bodyAsegurado.replaceAll("\\{tipo\\}",
					(solicitud.getIdCuenta() == null ? "tarjeta" : "cuenta"));
			bodyAsegurado = bodyAsegurado.replaceAll("\\{proximoPago\\}",
					(solicitud.getIdCuenta() == null ? "Tarjeta <br>" + proximoPago
							: "Cuenta Bancaria <br>" + proximoPago));
			String asunto = this.enviarCorreoAcpAsunto;
			LOGGER.info("[{}] Ingresamos a enviarCorreoConAdjunto", traza);

			this.enviarCorreoConAdjunto(asunto, personaAsegurado.getCorreo(), bodyAsegurado, files, Math.toIntExact(solicitud.getIdSolicitud()),numeroPropuesta,numeroDocumento);

			if (!Utilitarios.valorString(solicitud.getAgenteCorreo()).equals(Strings.EMPTY)) {
				this.enviarCorreoConAdjunto(asunto, solicitud.getAgenteCorreo(), bodyAsegurado, files, Math.toIntExact(solicitud.getIdSolicitud()),numeroPropuesta,numeroDocumento);
			}

			List<String> lstSuperv = Arrays.asList(cotizacion, cotizacion2, solicitudDigital);
			List<Adjuntos> filesSuperv = new ArrayList<>();
			Adjuntos adjuntoSuperv;
			for (ArchivoResponse item : globalArchivoResponse.getFiles()) {
				if (item.getName().contains(cotizacion) || item.getName().contains(cotizacion2)) {
					UrlResponse globalUrlArchivoResponse = globalRestClient.obtenerUrlArchivo(
							globalArchivoResponse.getContactId(), personaAsegurado.getNumeroDocumento(),
							item.getName());
					
					String urlArchivo = globalUrlArchivoResponse.getUrl();
					templateFile = downloadAndConvertToBase64(urlArchivo, traza);
					adjuntoSuperv = new Adjuntos();
					
					adjuntoSuperv.setName(item.getName());
					adjuntoSuperv.setContent(templateFile);
					filesSuperv.add(adjuntoSuperv);
				}
				if (item.getName().contains(solicitudDigital)) {
					UrlResponse globalUrlArchivoResponse = globalRestClient.obtenerUrlArchivo(
							globalArchivoResponse.getContactId(), personaAsegurado.getNumeroDocumento(),
							item.getName());
					
					String urlArchivo = globalUrlArchivoResponse.getUrl();
					templateFile = downloadAndConvertToBase64(urlArchivo, traza);
					adjuntoSuperv = new Adjuntos();
					
					adjuntoSuperv.setName(item.getName());
					adjuntoSuperv.setContent(templateFile);
					filesSuperv.add(adjuntoSuperv);
				}
			}

			if (solicitudProducto.getTipoProducto().equals("1")) {
				LOGGER.info("[{}] Ingresamos a enviarCorreoConAdjunto al Supervisor", traza);
				this.enviarCorreoSupervisor(solicitud, filesSuperv, personaAsegurado);
			}

			LOGGER.info("{} acpPDFTemporal.getPath() : {}", traza, acpPDFTemporal.getPath());

			this.eliminarFilePrivado(String.valueOf(acpPDFTemporal));

		} catch (Exception ex) {
			LOGGER.error("[" + traza + "] Error generarSolicitudPDF=>" + ex.getMessage(), ex);
			codigoRespuesta = Constantes.CODIGO_RESPUESTA_GENERAL_ERROR;
			mensajeRespuesta = "Error al crear PDF Solicitud";
		}

		respuesta.setCodigoRespuesta(codigoRespuesta);
		respuesta.setMensajeRespuesta(mensajeRespuesta);
		endTime = System.nanoTime();
		LOGGER.info("{} End generarPDF: diferencia tiempo {}", traza, (endTime - startTime));
		return respuesta;
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

	private String getContactVal(List<ObtenerPersonaDocumentoResponse> lista, ObtenerUploadArchivoResponse uploadArchivoResponse) {
		String contactVal ="";
		if (uploadArchivoResponse.getContactId() != null) {
			contactVal = uploadArchivoResponse.getContactId();
		} else {
			if (lista.size() > 1) {
				contactVal = lista.get(1).getID_PERSONA();
			} else {
				contactVal = lista.get(0).getID_PERSONA();
			}
		}
		return contactVal;
	}

	boolean predUrlCloud(List<String> lists, String nameFile) {
		return lists.stream().anyMatch(nameFile::contains);
	}

	public TipoCambioResponseDTO obtenerTipoCambioAcsele() {
		TipoCambioResponseDTO response = new TipoCambioResponseDTO();
		try {
			// List<pe.interseguro.siv.common.persistence.db.acsele.bean.TipoCambio> data =
			// acseleRepository.obtenerTipoCambio()
			LOGGER.info("***** llama consultarTasaDeCambio: *****");
			TasaCambioResponse data = globalRestClient.consultarTasaDeCambio();
			LOGGER.info("responde consultarTasaDeCambio: {}", gson.toJson(data.getRespuesta()));
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

	@Override
	public ByteArrayInputStream crearPDF(String numeroPropuesta) {
		LOGGER.info("INI AcpPDFServiceImpl.crearPDF(numeroPropuesta, tarjeta, cuenta)");

		this.generarPDF(numeroPropuesta, 1);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		File archivoPDFTemporal = null;

		// String acpFileName = "AcpDigital_" + numeroPropuesta
		// String acpFilePrivada = rutaPDFPrivada + "" + acpFileName
		Solicitud solicitud = solicitudRepository.findByNumeroCotizacion(numeroPropuesta);
		SolicitudProducto solicitudProducto = solicitudProductoRepository.findByIdSolicitud(solicitud.getIdSolicitud());
		Optional<Persona> personaAsegurado = personaRepository
				.findById(solicitud.getPersonaByIdAsegurado().getIdPersona());
		if (!personaAsegurado.isPresent())
			return null;
		archivoPDFTemporal = this.generarArchivo(solicitud, solicitudProducto, personaAsegurado.get());
		if (archivoPDFTemporal != null) {

			try (FileInputStream fis = new FileInputStream(archivoPDFTemporal)) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				byte[] buf = new byte[1024];
				for (int readNum; (readNum = fis.read(buf)) != -1;) {
					bos.write(buf, 0, readNum);
				}
				byte[] bytes = bos.toByteArray();
				out.write(bytes);
			} catch (IOException ex) {
				LOGGER.error("ERROR AL ESCRIBIR ARCHIVO {} ", ex.getMessage(), ex);
			}

			return new ByteArrayInputStream(out.toByteArray());
		} else {
			return null;
		}
	}
	@Override
	public AcpByteArrayInput crearPDFV2(String numeroPropuesta) {
		LOGGER.info("INI AcpPDFServiceImpl.crearPDFV2(numeroPropuesta, tarjeta, cuenta)");
		AcpByteArrayInput response = new  AcpByteArrayInput();
	  	this.generarPDF(numeroPropuesta, 1);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		File archivoPDFTemporal;
		Solicitud solicitud = solicitudRepository.findByNumeroCotizacion(numeroPropuesta);
		SolicitudProducto solicitudProducto = solicitudProductoRepository.findByIdSolicitud(solicitud.getIdSolicitud());
		Optional<Persona> personaAsegurado = personaRepository
				.findById(solicitud.getPersonaByIdAsegurado().getIdPersona());
		if (!personaAsegurado.isPresent())
			return null;
		archivoPDFTemporal = this.generarArchivo(solicitud, solicitudProducto, personaAsegurado.get());
		if (archivoPDFTemporal != null) {

			try (FileInputStream fis = new FileInputStream(archivoPDFTemporal)) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				byte[] buf = new byte[1024];
				for (int readNum; (readNum = fis.read(buf)) != -1;) {
					bos.write(buf, 0, readNum);
				}
				byte[] bytes = bos.toByteArray();
				out.write(bytes);
			} catch (IOException ex) {
				LOGGER.error("ERROR AL ESCRIBIR ARCHIVO {} ", ex.getMessage(), ex);
			}
			response.setNumeroDocumento(personaAsegurado.get().getNumeroDocumento());
			response.setAcpFile(new ByteArrayInputStream(out.toByteArray()));
		} else {
			return null;
		}
		return response;
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
			interseguroRestClient.enviarCorreo(enviarCorreoRequest, motivo, cotizacion, documento);

		} catch (Exception e) {
			LOGGER.error("ERROR - AdnServiceImpl#enviarCorreoError(requestDTO, tipoError, asunto, mensajeError): "
					+ e.getMessage());
			e.printStackTrace();
		}

	}

	@Async("threadPoolTaskExecutorFileDelete")
	public void eliminarFilePrivado(String urlPDFFirmaPrivada) {

		try {
			File file = new File(urlPDFFirmaPrivada);
			Files.deleteIfExists(file.toPath());
			LOGGER.info("Archivo eliminado!!!");
		} catch (Exception ex) {
			LOGGER.error("ERROR eliminarFilePrivado=> {}", ex.getMessage(), ex);
		}
	}


	public static Persona copyPersonaData(Persona source) {
		Persona target = new Persona();
		copyProperties(source, target);
		return target;
	}

	private static void copyProperties(Object source, Object target) {
		Map<String, Method> getters = findGetters(source.getClass());
		Map<String, Method> setters = findSetters(target.getClass());

		getters.forEach((propertyName, getter) -> {
			if (setters.containsKey(propertyName)) {
				try {
					Method setter = setters.get(propertyName);
					Object value = getter.invoke(source);
					setter.invoke(target, value);
				} catch (Exception e) {
					// Handle exception: log it, wrap it in a runtime exception, etc.
					e.printStackTrace();
				}
			}
		});
	}

	private static Map<String, Method> findGetters(Class<?> clazz) {
		Map<String, Method> getters = new HashMap<>();
		for (Method method : clazz.getMethods()) {
			if (isGetter(method)) {
				String propertyName = method.getName().startsWith("is")
						? method.getName().substring(2)
						: method.getName().substring(3);
				getters.put(propertyName.toLowerCase(), method);
			}
		}
		return getters;
	}

	private static Map<String, Method> findSetters(Class<?> clazz) {
		Map<String, Method> setters = new HashMap<>();
		for (Method method : clazz.getMethods()) {
			if (isSetter(method)) {
				String propertyName = method.getName().substring(3);
				setters.put(propertyName.toLowerCase(), method);
			}
		}
		return setters;
	}

	private static boolean isGetter(Method method) {
		if (!method.getName().startsWith("get") && !method.getName().startsWith("is")) {
			return false;
		}
		if (method.getParameterTypes().length != 0) {
			return false;
		}
		if (void.class.equals(method.getReturnType())) {
			return false;
		}
		return true;
	}

	private static boolean isSetter(Method method) {
		return method.getName().startsWith("set") && method.getParameterTypes().length == 1;
	}
}
