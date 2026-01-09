package pe.interseguro.siv.admin.transactional.service.impl;

import static com.culqi.Culqi.public_key;
import static com.culqi.Culqi.secret_key;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.util.Strings;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.culqi.Culqi;
import com.culqi.model.Config;

import pe.interseguro.siv.admin.config.BaseTest;
import pe.interseguro.siv.admin.transactional.service.CotizaService;
import pe.interseguro.siv.admin.transactional.service.SolicitudPDFService;
import pe.interseguro.siv.admin.transactional.service.SolicitudService;
import pe.interseguro.siv.common.dto.request.ADNRegistroRequestDTO;
import pe.interseguro.siv.common.dto.request.ADNRegistroTitularRequestDTO;
import pe.interseguro.siv.common.dto.request.IndenovaNotificacionActorRequestDTO;
import pe.interseguro.siv.common.dto.request.IndenovaNotificacionCircuitDataRequestDTO;
import pe.interseguro.siv.common.dto.request.IndenovaNotificacionEventDataRequestDTO;
import pe.interseguro.siv.common.dto.request.IndenovaNotificacionRequestDTO;
import pe.interseguro.siv.common.dto.request.SolicitudGuardarAseguradoRequestDTO;
import pe.interseguro.siv.common.dto.request.SolicitudGuardarRequestDTO;
import pe.interseguro.siv.common.dto.request.SolicitudSMSRequestDTO;
import pe.interseguro.siv.common.dto.response.BaseResponseDTO;
import pe.interseguro.siv.common.dto.response.CotizaDetalleResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudFormularioResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudGuardarResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudReglamentoResponseDTO;
import pe.interseguro.siv.common.persistence.db.mysql.domain.CodigoVerificacion;
import pe.interseguro.siv.common.persistence.db.mysql.domain.Multitabla;
import pe.interseguro.siv.common.persistence.db.mysql.domain.Persona;
import pe.interseguro.siv.common.persistence.db.mysql.domain.Solicitud;
import pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudBeneficiario;
import pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudDps;
import pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudDpsPregunta;
import pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudProducto;
import pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudProductoDetalle;
import pe.interseguro.siv.common.persistence.db.mysql.repository.CodigoVerificacionRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.MultitablaRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.PersonaRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.SolicitudBeneficiarioRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.SolicitudDpsPreguntaRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.SolicitudDpsRepositorioRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.SolicitudProductoDetalleRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.SolicitudProductoRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.SolicitudRepository;
import pe.interseguro.siv.common.util.Constantes;
import pe.interseguro.siv.common.util.DateUtil;
import pe.interseguro.siv.common.util.Utilitarios;

public class SolicitudServiceImplTest extends BaseTest {

	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private MultitablaRepository multitablaRepository;

	@Autowired
	private SolicitudService solicitudService;

	@Autowired
	private SolicitudPDFService solicitudPDFService;

	@Autowired
	private SolicitudRepository solicitudRepository;

	@Autowired
	private SolicitudBeneficiarioRepository solicitudBeneficiarioRepository;

	@Autowired
	private SolicitudDpsRepositorioRepository solicitudDpsRepositorioRepository;

	@Autowired
	private SolicitudDpsPreguntaRepository solicitudDpsPreguntaRepository;

	@Autowired
	private SolicitudProductoRepository solicitudProductoRepository;

	@Autowired
	private SolicitudProductoDetalleRepository solicitudProductoDetalleRepository;

	@Autowired
	private PersonaRepository personaRepository;

	@Autowired
	private CodigoVerificacionRepository codigoVerificacionRepository;

	@Autowired
	private CotizaService cotizaService;

	@Test

	public void notificacionFirma() {
		String circuitId = "80338";
		String eventType = "finalized";

		IndenovaNotificacionRequestDTO indenovaNotificacionRequestDTO = new IndenovaNotificacionRequestDTO();
		indenovaNotificacionRequestDTO.setDate(new Date());
		indenovaNotificacionRequestDTO.setAttempts(1);
		indenovaNotificacionRequestDTO.setService("circuits");

		IndenovaNotificacionEventDataRequestDTO indenovaNotificacionEventDataRequestDTO = new IndenovaNotificacionEventDataRequestDTO();
		IndenovaNotificacionActorRequestDTO indenovaNotificacionActorRequestDTO = new IndenovaNotificacionActorRequestDTO();
		IndenovaNotificacionCircuitDataRequestDTO indenovaNotificacionCircuitDataRequestDTO = new IndenovaNotificacionCircuitDataRequestDTO();

		indenovaNotificacionActorRequestDTO.setName("Interseguro Test");
		indenovaNotificacionActorRequestDTO.setEmail("carlos.fernandez@interseguro.com.pe");
		indenovaNotificacionActorRequestDTO.setIdentifier("intersegurotest");

		indenovaNotificacionCircuitDataRequestDTO.setSubject("Solicitud de Seguro 2019-09-04");
		indenovaNotificacionCircuitDataRequestDTO.setCreatorId(23142);
		indenovaNotificacionCircuitDataRequestDTO.setCreatedOn(new Date());

		indenovaNotificacionEventDataRequestDTO.setCircuitId(circuitId);
		indenovaNotificacionEventDataRequestDTO.setEventType(eventType);
		indenovaNotificacionEventDataRequestDTO.setActor(indenovaNotificacionActorRequestDTO);
		indenovaNotificacionEventDataRequestDTO.setCircuitData(indenovaNotificacionCircuitDataRequestDTO);

		indenovaNotificacionRequestDTO.setEventData(indenovaNotificacionEventDataRequestDTO);

		boolean respuesta = solicitudService.validarNotificacion(indenovaNotificacionRequestDTO);

		assertTrue(respuesta);
	}

	@Test

	public void guardarSolicitud() {
		String numeroCotizacion = "101";
		String numeroPropuesta = numeroCotizacion;

		String tipoDocumento = "1";
		String numeroDocumento = "43459267";
		String usuario = "equisper";
		String codigoEstado = Constantes.CODIGO_SOLICITUD_PENDIENTE;
		String codigoSubPlan = "2";
		String codigoMoneda = "2";
		String agenteNombres = "EDWIN QUISPE";
		String agenteNumVendedor = "10442";

		// Codigos Asegurado
		String codigoEstadoCivil = "1";
		String codigoNacionalidad = "1";
		String codigoDireccionTipo = "1";
		String codigoDepartamento = "1";
		String codigoProvincia = "1";
		String codigoDistrito = "1";
		String codigoEsPep = "1";
		String codigoEsSujetoObligado = "1";
		String codigoIngresoMoneda = "1";
		String idCrmUsuario = "3A6B39AE-F041-E911-9128-005056878577";

		Persona asegurado = new Persona();
		asegurado.setNumeroDocumento(numeroDocumento);

		asegurado = personaRepository.findByTipoNumeroDocumento(tipoDocumento, numeroDocumento);
		if (asegurado.getIdPersona() != null) {

			// ACTUALIZAR ASEGURADO
			asegurado.setRazonSocial("");
			asegurado.setEstadoCivil(codigoEstadoCivil);
			asegurado.setNacionalidad(codigoNacionalidad);
			asegurado.setDireccionTipo(codigoDireccionTipo);
			asegurado.setDireccionTipoDes("LOS INGENIEROS");
			asegurado.setDireccionNroMz("MZ K14 LOTE 17");
			asegurado.setDireccionInterior("2");
			asegurado.setDireccionUrbanizacion("JESUS ALBERTO PAEZ");
			asegurado.setDepartamento(codigoDepartamento);
			asegurado.setProvincia(codigoProvincia);
			asegurado.setDistrito(codigoDistrito);
			asegurado.setEsPep(codigoEsPep);
			asegurado.setEsSujetoObligado(codigoEsSujetoObligado);
			asegurado.setCentroTrabajo("INTERSEGURO");
			asegurado.setIngresoMoneda(codigoIngresoMoneda);
			asegurado.setIngresoValor(1000);
			asegurado.setProfesionDetalle("ANALISTA DE SISTEMAS");

			personaRepository.save(asegurado);

			// Registrar Cotizacion
			Solicitud solicitudDomain = new Solicitud();
			solicitudDomain.setNumeroCotizacion(numeroCotizacion);
			solicitudDomain = solicitudRepository.findByNumeroCotizacion(numeroCotizacion);
			if (solicitudDomain == null) {
				solicitudDomain = new Solicitud();
				solicitudDomain.setNumeroCotizacion(numeroCotizacion);
				solicitudDomain.setEstado(codigoEstado);
				solicitudDomain.setPersonaByIdAsegurado(asegurado);
				solicitudDomain.setAgenteNombres(agenteNombres);
				solicitudDomain.setAgenteNumVendedor(agenteNumVendedor);
				solicitudDomain.setIdCrmUsuario(idCrmUsuario);
				solicitudDomain.setUsuarioCrea(usuario);
				solicitudDomain.setFechaCrea(new Date());
			} else {
				solicitudDomain.setUsuarioModif(usuario);
				solicitudDomain.setFechaModif(new Date());
			}

			solicitudDomain.setNumeroPropuesta(numeroPropuesta);
			solicitudDomain.setSubplan(codigoSubPlan);
			solicitudDomain.setMoneda(codigoMoneda);

			solicitudRepository.save(solicitudDomain);

		}

	}

	@Test

	public void guardarSolicitudBeneficiarios() {
		Long idSolicitud = 1L;
		String tipoBeneficiario = "1";
		String tipoDocumento = "1";
		String tipoRelacion = "1";
		String usuario = "equisper";
		Integer distribucion = 100;

		Solicitud solicitud = new Solicitud();
		solicitud.setIdSolicitud(idSolicitud);

		List<SolicitudBeneficiario> listaBeneficiario = new ArrayList<SolicitudBeneficiario>();
		SolicitudBeneficiario beneficiario = new SolicitudBeneficiario();
		beneficiario.setSolicitud(solicitud);
		beneficiario.setTipoBeneficiario(tipoBeneficiario);
		beneficiario.setNombres("EDWIN");
		beneficiario.setApellidoPaterno("QUISPE");
		beneficiario.setApellidoMaterno("RAMOS");
		beneficiario.setFechaNacimiento(DateUtil.stringToDate("18/12/1985", DateUtil.FORMATO_DIA_DDMMYYYY));
		beneficiario.setTipoDocumento(tipoDocumento);
		beneficiario.setTipoRelacion(tipoRelacion);
		beneficiario.setDistribucion(distribucion);
		beneficiario.setUsuarioCrea(usuario);
		beneficiario.setFechaCrea(new Date());

		listaBeneficiario.add(beneficiario);

		for (SolicitudBeneficiario itemBeneficiario : listaBeneficiario) {
			solicitudBeneficiarioRepository.save(itemBeneficiario);
		}
	}

	@Test

	public void listarSolicitudBeneficiarios() {
		Long idSolicitud = 1L;
		List<SolicitudBeneficiario> listaBeneficiarios = new ArrayList<SolicitudBeneficiario>();
		listaBeneficiarios = solicitudBeneficiarioRepository.findByIdSolicitud(idSolicitud);
		assertNotNull(listaBeneficiarios);
	}

	@Test

	public void guardarSolicitudDps() {
		Long idSolicitud = 5L;

		Float estatura = Float.parseFloat("170.00");
		Float peso = Float.parseFloat("70.00");
		Float pesoAdCantidad = Float.parseFloat("4.00");
		Integer pesoVariacionCantidad = 5;
		String pesoMotivo = "MOTIVO DE CAMBIO DE PESO";
		Boolean respuestaGenerica = true;

		String usuario = "equisper";

		Solicitud solicitud = new Solicitud();
		solicitud.setIdSolicitud(idSolicitud);

		SolicitudDps solicitudDps = new SolicitudDps();
		solicitudDps.setSolicitud(solicitud);

		solicitudDps = solicitudDpsRepositorioRepository.findByIdSolicitud(idSolicitud);

		if (solicitudDps == null) {
			solicitudDps = new SolicitudDps();
			solicitudDps.setSolicitud(solicitud);
		} else {
			solicitudDps.setUsuarioModif(usuario);
			solicitudDps.setFechaModif(new Date());
		}

		solicitudDps.setEstatura(estatura);
		solicitudDps.setUsuarioCrea(usuario);
		solicitudDps.setPeso(peso);
		solicitudDps.setPesoVariacion(1);
		solicitudDps.setPesoVariacionCantidad(pesoVariacionCantidad);
		solicitudDps.setFechaCrea(new Date());
		solicitudDps.setPesoAumentoDisminuyo(respuestaGenerica);
		solicitudDps.setPesoAdCantidad(pesoAdCantidad);
		solicitudDps.setPesoMotivo(pesoMotivo);
		solicitudDps.setFumador(respuestaGenerica);
		solicitudDps.setFumadorCantidad("2 CAJETILLAS");
		solicitudDps.setFumadorFrecuencia("A LA SEMANA");
		solicitudDps.setDrogas(respuestaGenerica);
		// solicitudDps.setDrogasFecha(DateUtil.stringToDate("01/01/2017",DateUtil.FORMATO_DIA_DDMMYYYY));
		solicitudDps.setDrogasFecha("01/01/2017");
		solicitudDps.setAlcohol(respuestaGenerica);
		solicitudDps.setAlcoholCantidad("2 BOTELLAS");
		solicitudDps.setAlcoholFrecuencia("MES");

		solicitudDps.setUsuarioCrea(usuario);
		solicitudDps.setFechaCrea(new Date());

		solicitudDpsRepositorioRepository.save(solicitudDps);

		// Inicio - Eliminar las creadas previamente
		List<SolicitudDpsPregunta> listaPreguntas = solicitudDpsPreguntaRepository.findByIdSolicitud(idSolicitud);
		for (SolicitudDpsPregunta pregunta : listaPreguntas) {
			solicitudDpsPreguntaRepository.delete(pregunta);
		}
		// Fin Eliminar las creadas previamente

		for (Integer i = 1; i <= 12; i++) {
			String pregunta = i.toString();
			String item = pregunta; // Numero de Pregunta
			SolicitudDpsPregunta solicitudDpsPregunta = new SolicitudDpsPregunta();
			solicitudDpsPregunta.setSolicitudDps(solicitudDps);
			solicitudDpsPregunta.setbloquePregunta(Constantes.SOLICITUD_DPS_PREGUNTA_BLOQUE_1);
			solicitudDpsPregunta.setItem(item);
			solicitudDpsPregunta.setPregunta(pregunta);
			solicitudDpsPregunta.setRespuesta(respuestaGenerica);
			solicitudDpsPregunta.setDetalle("DETALLE");
			solicitudDpsPregunta.setEnfermedad("ENFERMEDAD");
			// solicitudDpsPregunta.setFechaDiagnostico(DateUtil.stringToDate("01/01/2017",DateUtil.FORMATO_DIA_DDMMYYYY));
			solicitudDpsPregunta.setFechaDiagnostico("01/01/2017");
			solicitudDpsPregunta.setCondicionActual("CONDICION");
			solicitudDpsPregunta.setNombreMedicoHospital("NOMBRE MEDICO / HOSPITAL");

			solicitudDpsPregunta.setUsuarioCrea(usuario);
			solicitudDpsPregunta.setFechaCrea(new Date());

			solicitudDpsPreguntaRepository.save(solicitudDpsPregunta);
		}

	}

	@Test
	public void guardarSolicitudProducto() {
		Long idSolicitud = 1L;
		String usuario = "equisper";

		String tipoProducto = "1";
		String frecuencia = "1";
		String tipoCuota = "1";
		String tipoRiesgo = "1";

		Solicitud solicitud = new Solicitud();
		solicitud.setIdSolicitud(idSolicitud);

		SolicitudProducto solicitudProducto = new SolicitudProducto();
		solicitudProducto.setSolicitud(solicitud);

		solicitudProducto = solicitudProductoRepository.findByIdSolicitud(idSolicitud);

		if (solicitudProducto == null) {
			solicitudProducto = new SolicitudProducto();
			solicitudProducto.setSolicitud(solicitud);
		} else {
			solicitudProducto.setUsuarioModif(usuario);
			solicitudProducto.setFechaModif(new Date());
		}

		solicitudProducto.setTipoProducto(tipoProducto);
		solicitudProducto.setMontoFondoGarantizado(55000F);
		solicitudProducto.setPeriodoCoberturaAnual(15);
		solicitudProducto.setAnualidadPago(1);
		solicitudProducto.setFrecuencia(frecuencia);
		solicitudProducto.setTipoCuota(tipoCuota);
		solicitudProducto.setTipoRiesgo(tipoRiesgo);
		solicitudProducto.setPrimaComercialAnual(1565.95F);
		solicitudProducto.setFactorPago(0.09F);
		solicitudProducto.setPrimaComercial(140.12F);

		solicitudProducto.setUsuarioCrea(usuario);
		solicitudProducto.setFechaCrea(new Date());

		solicitudProductoRepository.save(solicitudProducto);

		// Inicio - Eliminar las creadas previamente
		List<SolicitudProductoDetalle> listaProductoDetalle = solicitudProductoDetalleRepository
				.findByIdSolicitud(idSolicitud);
		for (SolicitudProductoDetalle item : listaProductoDetalle) {
			solicitudProductoDetalleRepository.delete(item);
		}
		// Fin Eliminar las creadas previamente

		String cobertura = "1";
		String tipoCobertura = "1";

		SolicitudProductoDetalle solicitudProductoDetalle = new SolicitudProductoDetalle();
		solicitudProductoDetalle.setSolicitudProducto(solicitudProducto);
		solicitudProductoDetalle.setCobertura(cobertura);
		solicitudProductoDetalle.setTipoCobertura(tipoCobertura);
		solicitudProductoDetalle.setCapitalAsegurado(5000F);
		solicitudProductoDetalle.setPrimaAnual(800F);

		solicitudProductoDetalle.setUsuarioCrea(usuario);
		solicitudProductoDetalle.setFechaCrea(new Date());

		solicitudProductoDetalleRepository.save(solicitudProductoDetalle);

	}

	@Test

	public void obtenerSolicitud() {
		Long idSolicitud = 1L;
		Solicitud solicitud = new Solicitud();
		solicitud = solicitudRepository.findById(idSolicitud).get();

		Persona asegurado = solicitud.getPersonaByIdAsegurado();
		Persona contratante = solicitud.getPersonaByIdContratante();

		// Completar Datos del Asegurado
		if (asegurado != null) {
			asegurado = personaRepository.findById(asegurado.getIdPersona()).get();
			System.out.println("getIdPersona() => " + asegurado.getIdPersona());
			System.out.println("getNombres() => " + asegurado.getNombres());
		}

		// Completar Datos del Contratante
		if (contratante != null) {
			contratante = personaRepository.findById(contratante.getIdPersona()).get();
		}

		SolicitudProducto solicitudProducto = solicitudProductoRepository.findByIdSolicitud(idSolicitud);
		List<SolicitudProductoDetalle> listaProductoDetalle = solicitudProductoDetalleRepository
				.findByIdSolicitud(idSolicitud);
		List<SolicitudBeneficiario> listaBeneficiario = solicitudBeneficiarioRepository.findByIdSolicitud(idSolicitud);

		SolicitudDps solicitudDps = solicitudDpsRepositorioRepository.findByIdSolicitud(idSolicitud);
		List<SolicitudDpsPregunta> listaDpsPregunta = solicitudDpsPreguntaRepository.findByIdSolicitud(idSolicitud);

		System.out
				.println("solicitudProducto.getIdSolicitudProducto() => " + solicitudProducto.getIdSolicitudProducto());
		System.out.println("solicitudDps.getIdSolicitudDps() => " + solicitudDps.getIdSolicitudDps());

		System.out.println("listaProductoDetalle.size() => " + listaProductoDetalle.size());
		System.out.println("listaBeneficiario.size() => " + listaBeneficiario.size());
		System.out.println("listaDpsDetalle.size() => " + listaDpsPregunta.size());

		assertNotNull(solicitud);

	}

	@Test

	public void generarSolicitudFirma() {
		Persona asegurado = new Persona();

		// System.out.println("response = " + gson.toJson(asegurado));

		// 1. Traer Información de BD
		// 2. Generar PDF (Leer plantilla y guardar en tmp)
		// 3. Crear Circuito Indenova
		// 4. Actualizar Estado Solicitud

	}

	@Test

	public void testFormato() {

		try {
			Double valor = 1345D;
			String formato = Utilitarios.formatoMiles(valor, Utilitarios.FORMATO_MILES_SIN_DECIMAL);

			System.out.println("FORMATO=>" + formato);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test

	public void enviarDatosSolicitudCRMTest() {
		Long idSolicitud = 1L;

		BaseResponseDTO respuesta = new BaseResponseDTO();
		respuesta = solicitudService.enviarDatosSolicitudCRM(idSolicitud);

		assertEquals(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO, respuesta.getCodigoRespuesta());
	}

	@Test

	public void enviarCorreoSolicitud() {
		Long idSolicitud = 70L;

		Boolean respuesta = solicitudService.enviarCorreo(idSolicitud, "VidaFree");

		assertEquals(true, respuesta);
	}

	@Test

	public void obtenerReglamentoSolicitudTest() {
		String tipoDocumento = "1";
		String numeroDocumento = "43459267";
		Long numeroCotizacion = 2142006L;
		String usuarioLogin = "jaybaram";
		String agenteCorreo = "juan.aybar@interseguro.com.pe";
		String agenteNombres = "Juan Salomon Aybar Amar";
		String agenteNumVendedor = "16011";
		String agenteIdCRM = "815ec29c-2c87-e911-903d-005056ae48c6";

		CotizaDetalleResponseDTO cotizacionDetalle = cotizaService.detalle(numeroCotizacion);

		// System.out.println("cotizacionDetalle.gson=>"+gson.toJson(cotizacionDetalle));

		SolicitudReglamentoResponseDTO solicitudRegalmento = solicitudService.obtenerReglamento(tipoDocumento,
				numeroDocumento, numeroCotizacion, usuarioLogin, agenteNombres, agenteCorreo, agenteNumVendedor,
				agenteIdCRM, cotizacionDetalle, "VidaFree");

		assertEquals("01", solicitudRegalmento.getCodigoRespuesta());
	}

	@Test

	public void obtenerDatosFormularioTest() {
		Long idSolicitud = 5L;
		Long numeroCotizacion = 2142006L;

		CotizaDetalleResponseDTO cotizacionDetalle = cotizaService.detalle(numeroCotizacion);

		System.out.println("cotizacionDetalle =>" + cotizacionDetalle.getFechaCotizacion());

		SolicitudFormularioResponseDTO solicitudFormulario = solicitudService.obtenerDatosFormulario(idSolicitud,
				numeroCotizacion, cotizacionDetalle, "dev", "", "");

		System.out.println("CODIGO RESPUESTA =>" + solicitudFormulario.getCodigoRespuesta());
		System.out.println("MENSAJE RESULTADO =>" + solicitudFormulario.getMensajeRespuesta());
		System.out.println("ID SOLICITUD =>" + solicitudFormulario.getIdSolicitud());

//		System.out.println("solicitudFormulario.json =>" + gson.toJson(solicitudFormulario));

		// System.out.println("solicitudFormulario.json =>"+
		// gson.toJson(solicitudFormulario));

		assertEquals("01", solicitudFormulario.getCodigoRespuesta());
	}

	@Test

	public void sendSMSTest() {
		SolicitudSMSRequestDTO dto = new SolicitudSMSRequestDTO();
		dto.setIdSolicitud((long) 1);

		boolean result = solicitudService.enviarSMS(dto);

		assertTrue(result);
	}

	@Test

	public void guardarSolicitudTest() {
		Long idSolicitud = 5L;

		SolicitudGuardarRequestDTO solicitudGuardarRequestDTO = new SolicitudGuardarRequestDTO();
		solicitudGuardarRequestDTO.setIdSolicitud(idSolicitud);
		solicitudGuardarRequestDTO.setAsegurado(new SolicitudGuardarAseguradoRequestDTO());
		solicitudGuardarRequestDTO.getAsegurado().setDireccionNombreVia("LOS INGENIEROS");

		SolicitudGuardarResponseDTO respuesta = solicitudService.guardar(solicitudGuardarRequestDTO);

		assertEquals(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO, respuesta.getCodigoRespuesta());

	}

	@Test

	public void generarInformacionSolicitudTest() {

		Long idSolicitud = 5L;

		Solicitud solicitud = new Solicitud();
		solicitud.setIdSolicitud(idSolicitud);
		solicitudService.generarInformacionSolicitud(solicitud, "VidaFree");

		// SolicitudRunnable tarea = new SolicitudRunnable(solicitud.getIdSolicitud());
		// applicationContext.getAutowireCapableBeanFactory().autowireBean(tarea);

		// Thread thread1 =new Thread(tarea);
		// thread1.start();

	}

	@Test

	public void generarSolicitudPDFTest() {
		Long idSolicitud = 8L;
		BaseResponseDTO responsePDF = solicitudPDFService.generarSolicitudPDF(idSolicitud, "VidaFree");

		// System.out.println("responsePDF=>"+gson.toJson(responsePDF));

		assertEquals(responsePDF.getCodigoRespuesta(), Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);

	}

	@Test

	public void codigoFirmadoTest() {
		Long idSolicitud = 9L;
		Long idPersona = 200L;

		CodigoVerificacion cv = codigoVerificacionRepository.findCodigoFirmado(idSolicitud, idPersona);

		assertNotNull(cv);

	}

	@Test

	public void espacioBlanco() {
		String nombres = "Roman Andre Fernández Quiroz";

		System.out.println("nombres.prev => " + nombres);
		System.out.println("nombres.next => " + nombres.replaceAll("\\s", "+"));

		assertNotNull(nombres);

	}

	@Test

	public void generarSolicitudPDF() {
		Long idSolicitud = 337L;

		BaseResponseDTO respuesta = new BaseResponseDTO();
		respuesta = solicitudPDFService.generarSolicitudPDF(idSolicitud, "VidaFree");

		assertEquals(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO, respuesta.getCodigoRespuesta());
	}

	@Test

	public void personaFechaNacimiento() throws Exception {
		Persona persona = new Persona();
		// Date fechaNacimiento = new Date(1994, 1, 26);
		Date fechaNacimiento = new SimpleDateFormat("dd/MM/yyyy").parse("26/01/1994");

		persona.setFechaNacimiento(fechaNacimiento);

		String fechaNacimientoConvert = DateUtil.dateToString(persona.getFechaNacimiento(),
				DateUtil.FORMATO_DIA_DDMMYYYY);
		System.out.println("persona.fechaNacimiento=>" + fechaNacimiento);

		System.out.println("persona.fechaNacimientoConvert=>" + fechaNacimientoConvert);

		assertNotNull(fechaNacimientoConvert);

	}

	@Test

	public void fechaNacimientoTest() {
		ADNRegistroRequestDTO aDNRegistroRequestDTO = new ADNRegistroRequestDTO();
		ADNRegistroTitularRequestDTO titular = new ADNRegistroTitularRequestDTO();
		titular.setFechaNacimiento("10/03/1986");
		aDNRegistroRequestDTO.setTitular(titular);

		Date fechaNacimientoOrigen = aDNRegistroRequestDTO.getTitular().getFechaNacimiento() == null ? null
				: DateUtil.stringToDate(aDNRegistroRequestDTO.getTitular().getFechaNacimiento(),
						DateUtil.FORMATO_DIA_DDMMYYYY);

		Persona persona = personaRepository.findByTipoNumeroDocumento("1", "43459267");
		persona.setFechaNacimiento(fechaNacimientoOrigen);
		personaRepository.save(persona);

		System.out.println("aDNRegistroRequestDTO=>" + aDNRegistroRequestDTO.getTitular().getFechaNacimiento());
		System.out.println("fechaNacimientoOrigen=>" + fechaNacimientoOrigen);

		assertNotNull(fechaNacimientoOrigen);
	}

	@Test
	public void pagoCulquiTest() {
		Culqi culqi = null;

		culqi = new Culqi();
		public_key = "pk_test_kYDAgmLaJrt5DcED";
		secret_key = "sk_test_LMviyNz7PVcj3Olb";

		Config config = new Config();

		Map<String, Object> token = new HashMap<String, Object>();
		Map<String, Object> tarjetaInput = new HashMap<String, Object>();
		tarjetaInput.put("card_number", "4111111111111111");
		tarjetaInput.put("cvv", "123");
		// tarjeta.put("email", email);
		tarjetaInput.put("email", "wm@wm.com");
		tarjetaInput.put("expiration_month", 9);
		tarjetaInput.put("expiration_year", 2020);
		try {
			token = culqi.token.create(tarjetaInput);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// System.out.println("Response: " + gson.toJson(token));

		assertNotNull(token);
	}

	@Test
	public void testFloat() {
		Float a = 303.58f;
		System.out.println("Response: " + a);
	}

	@Test
	public void testDecrypt() {
		String KEY_ENCRYPT = Constantes.KEY_ENCRYPT;
		String encrypted = "xkK8PIbgU/E=";
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

		System.out.println(strData);
		;
	}

	@Test
	public void testValorMultitabla() {
		String codigoTabla = "001";
		String codigo = "2";
		String campo = "valorAux";
		String valor = this.valorMultiTabla(codigoTabla, codigo, campo);
		System.out.println(valor);

	}

	public String valorMultiTabla(String codigoTabla, String codigo, String campo) {
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

}
