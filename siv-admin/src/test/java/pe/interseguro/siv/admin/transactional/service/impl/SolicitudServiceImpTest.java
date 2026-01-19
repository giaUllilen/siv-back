package pe.interseguro.siv.admin.transactional.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;

import com.google.gson.Gson;

import pe.interseguro.siv.admin.config.BaseTest;
import pe.interseguro.siv.common.dto.request.PolicyIssuedRequestDTO;
import pe.interseguro.siv.common.dto.response.BaseResponseDTO;
import pe.interseguro.siv.common.dto.response.LinkPagoResponseDTO;
import pe.interseguro.siv.common.persistence.db.mysql.domain.Multitabla;
import pe.interseguro.siv.common.persistence.db.mysql.domain.Persona;
import pe.interseguro.siv.common.persistence.db.mysql.domain.Solicitud;
import pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudFiltrado;
import pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudProducto;
import pe.interseguro.siv.common.persistence.db.mysql.repository.MultitablaRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.PersonaRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.SolicitudFiltradoRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.SolicitudProductoRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.SolicitudRepository;
import pe.interseguro.siv.common.persistence.rest.base.BaseRestClient;
import pe.interseguro.siv.common.persistence.rest.global.GlobalRestClient;
import pe.interseguro.siv.common.persistence.rest.global.request.DocumentoCorreoRequest;
import pe.interseguro.siv.common.persistence.rest.global.request.EmisionAutomaticaRequest;
import pe.interseguro.siv.common.persistence.rest.global.request.EnvioCorreoClienteEmisionRequest;
import pe.interseguro.siv.common.persistence.rest.global.request.GestiorDocumentoRequest;
import pe.interseguro.siv.common.persistence.rest.global.request.ListDocumentoCorreoRequest;
import pe.interseguro.siv.common.persistence.rest.global.request.ObtenerMontoRecargoRequest;
import pe.interseguro.siv.common.persistence.rest.global.response.*;
import pe.interseguro.siv.common.persistence.rest.interseguro.InterseguroRestClient;
import pe.interseguro.siv.common.persistence.rest.interseguro.request.ClienteLinkPago;
import pe.interseguro.siv.common.persistence.rest.interseguro.request.LinkPagoRequest;
import pe.interseguro.siv.common.persistence.rest.interseguro.request.PagoLinkPago;
import pe.interseguro.siv.common.persistence.rest.interseguro.response.LinkPagoResponse;
import pe.interseguro.siv.common.util.Constantes;

@ExtendWith(MockitoExtension.class)
public class SolicitudServiceImpTest extends BaseTest {// extends BaseTest

	@Mock
	private SolicitudRepository solicitudRepository;
	@Mock
	private SolicitudFiltradoRepository solicitudFiltradoRepository;
	@Mock
	private MultitablaRepository multitablaRepository;

	@Mock
	private SolicitudProductoRepository solicitudProductoRepository;

	@Mock
	private PersonaRepository personaRepository;

	@Mock
	private BaseRestClient baseRestClient;

	@Mock
	private GlobalRestClient globalRestClient;

	@Mock
	private InterseguroRestClient interseguroRestClient;

	@Mock
	private MessageSource messageSource;

	@InjectMocks
	private SolicitudServiceImpl solicitudService;

	@Value("#{ environment['url.linkPago'] }")
	private String urlLinkPago;

	@Value("#{ environment['url.linkPago.link'] }")
	private String link;

	@Value("#{ environment['url.linkPago.aplicacion'] }")
	private String aplicacion;

	Solicitud solicitud;
	SolicitudProducto solicitudProducto;
	Persona persona;
	Map<String, String> valores;

	@Before
	public void setUp() {

		solicitudProducto = new SolicitudProducto();
		solicitudProducto.setIdSolicitudProducto(1l);
		solicitudProducto.setPrimaComercial(100f);
		solicitudProducto.setFrecuencia("1");
		solicitudProducto.setTipoProducto("1");

		Set<SolicitudProducto> liProductos = Collections.singleton(solicitudProducto);

		persona = new Persona();
		persona.setIdPersona(1l);
		persona.setTipoDocumento("1");
		persona.setNombres("carlos");
		persona.setApellidoPaterno("papa");
		persona.setApellidoMaterno("mama");
		persona.setNumeroDocumento("12345678");
		persona.setCelular(987654321);
		persona.setCorreo("correo@interseguro.com.pe");

		Persona persona2 = new Persona();
		persona2.setIdPersona(1l);
		persona2.setTipoDocumento("1");
		persona2.setNombres("carlos");
		persona2.setApellidoPaterno("papa");
		persona2.setApellidoMaterno("mama");
		persona2.setNumeroDocumento("12345678");
		persona2.setCelular(987654321);
		persona2.setCorreo("correo@interseguro.com.pe");

		solicitud = new Solicitud();
		solicitud.setIdSolicitud(1l);
		solicitud.setNumeroCotizacion("1");
		solicitud.setNumeroPropuesta("1");
		solicitud.setSolicitudProductos(liProductos);
		solicitud.setMoneda("1");
		solicitud.setPersonaByIdAsegurado(persona);
		solicitud.setPersonaByIdContratante(persona2);
		solicitud.setAseguradoIgualContratante("1");
		solicitud.setIdContratante(1l);
		solicitud.setSubplan("1");

		valores = new HashMap<>();
		valores.put("frecuencia", "Mensual");
		valores.put("linkPago", "mmmdmdm");
		valores.put("expiracion", String.valueOf(1));
		valores.put("producto", "VidaFree");
	}

	@Test
	@DisplayName("Generar link pago")
	public void miTest() {
		String numeroCotiza = "1";

		when(solicitudRepository.findByNumeroCotizacion(numeroCotiza)).thenReturn(solicitud);

		LinkPagoRequest linkPagoRequest = new LinkPagoRequest();
		linkPagoRequest.setProducto(Constantes.COTIZADOR_VIDA_PRODUCTO_VIDA_FREE);
		linkPagoRequest.setProductoAcsele(Constantes.COTIZADOR_VIDA_PRODUCTO_VIDA_FREE.toUpperCase());
		if (solicitudProducto.getTipoProducto().equals("1")) {
			linkPagoRequest.setProducto(Constantes.COTIZADOR_VIDA_PRODUCTO_PLAN_GARANTIZADO);
			linkPagoRequest.setProductoAcsele(Constantes.COTIZADOR_VIDA_PRODUCTO_PLAN_GARANTIZADO.toUpperCase());
		}

		linkPagoRequest.setExpiracion(0);
		// linkPagoRequest.setAplicacion(aplicacion);
		PagoLinkPago pago = new PagoLinkPago();
		pago.setMoneda(solicitud.getMoneda().equals("1") ? "PEN" : "USD");
		pago.setMonedaSimbolo(solicitud.getMoneda().equals("1") ? "S/." : "$.");
		pago.setMonto(solicitudProducto.getPrimaComercial().toString());

		Multitabla multitabla = new Multitabla();
		multitabla.setCodigo("1");
		multitabla.setValor("val");

		when(multitablaRepository.findByCodigoTablaSinEstado("016")).thenReturn(Collections.singletonList(multitabla));
		String frecuencia = multitabla.getValor();
		pago.setFrecuencia(frecuencia);
		long[] array = new long[0];
		pago.setCuotas(array);

		linkPagoRequest.setPago(pago);
		ClienteLinkPago cliente = new ClienteLinkPago();
		multitabla.setValorAux("aux");
		when(multitablaRepository.findByCodigoTablaSinEstado("001")).thenReturn(Collections.singletonList(multitabla));
		cliente.setTipoDocumento(multitabla.getValorAux());
		cliente.setDocumentoIdentidad(persona.getNumeroDocumento());
		cliente.setNombre(persona.getNombres());
		cliente.setApellidoPaterno(persona.getApellidoPaterno());
		cliente.setApellidoMaterno(persona.getApellidoMaterno());
		cliente.setEmail(persona.getCorreo());
		cliente.setCelular(persona.getCelular().toString());
		cliente.setDepartamento(persona.getDepartamento());
		cliente.setProvincia(persona.getProvincia());
		cliente.setDistrito(persona.getDistrito());

		linkPagoRequest.setCliente(cliente);
		linkPagoRequest.setIdentificador(solicitud.getNumeroPropuesta());
		linkPagoRequest.setCodigoEnvio("");
		linkPagoRequest.setEsRecargo(false);
		linkPagoRequest.setPrimeraPrima(true);
		linkPagoRequest.setBloquearEnvio(true);

		LinkPagoResponse linkPagoResponse = new LinkPagoResponse();
		linkPagoResponse.setCodigoRespuesta("01");
		linkPagoResponse.setMensajeRespuesta("OK");
		linkPagoResponse.setToken("token");
		linkPagoResponse.setLink("link");
		System.out.println("linkPagoRequest " + new Gson().toJson(linkPagoRequest));
		when(interseguroRestClient.generateLinkPago(linkPagoRequest)).thenReturn(linkPagoResponse);

		LinkPagoResponseDTO response = solicitudService.enviarLinkPago(numeroCotiza, "", null, null);
		assertThat(response.getCodigoRespuesta()).isEqualTo("01");

	}

	@Test
	@DisplayName("Generar link pago recargo")
	public void generaLinkRecargo() {
		String numeroCotiza = "1";
		solicitud.setAseguradoIgualContratante("2");
		when(solicitudRepository.findByNumeroCotizacion(numeroCotiza)).thenReturn(solicitud);

		LinkPagoRequest linkPagoRequest = new LinkPagoRequest();
		linkPagoRequest.setProducto(Constantes.COTIZADOR_VIDA_PRODUCTO_VIDA_FREE);
		linkPagoRequest.setProductoAcsele(Constantes.COTIZADOR_VIDA_PRODUCTO_VIDA_FREE.toUpperCase());
		if (solicitudProducto.getTipoProducto().equals("1")) {
			linkPagoRequest.setProducto(Constantes.COTIZADOR_VIDA_PRODUCTO_PLAN_GARANTIZADO);
			linkPagoRequest.setProductoAcsele(Constantes.COTIZADOR_VIDA_PRODUCTO_PLAN_GARANTIZADO.toUpperCase());
		}

		linkPagoRequest.setExpiracion(0);
		// linkPagoRequest.setAplicacion(aplicacion);
		PagoLinkPago pago = new PagoLinkPago();
		pago.setMoneda(solicitud.getMoneda().equals("1") ? "PEN" : "USD");
		pago.setMonedaSimbolo(solicitud.getMoneda().equals("1") ? "S/." : "$.");
		ObtenerMontoRecargoRequest request = new ObtenerMontoRecargoRequest();
		request.setNroCotizacion(solicitud.getNumeroPropuesta());
		ObtenerMontoRecargoResponse globalResponse = new ObtenerMontoRecargoResponse();
		globalResponse.setMonto("100");
		globalResponse.setNumCotizacion(solicitud.getNumeroCotizacion());
		when(globalRestClient.obtenerMontoRecargo(request)).thenReturn(globalResponse);

		pago.setMonto(globalResponse.getMonto());

		Multitabla multitabla = new Multitabla();
		multitabla.setCodigo("1");
		multitabla.setValor("val");

		when(multitablaRepository.findByCodigoTablaSinEstado("016")).thenReturn(Collections.singletonList(multitabla));
		String frecuencia = multitabla.getValor();
		pago.setFrecuencia(frecuencia);
		long[] array = new long[0];
		pago.setCuotas(array);

		linkPagoRequest.setPago(pago);
		ClienteLinkPago cliente = new ClienteLinkPago();
		multitabla.setValorAux("aux");
		when(multitablaRepository.findByCodigoTablaSinEstado("001")).thenReturn(Collections.singletonList(multitabla));
		cliente.setTipoDocumento(multitabla.getValorAux());
		cliente.setDocumentoIdentidad(persona.getNumeroDocumento());
		cliente.setNombre(persona.getNombres());
		cliente.setApellidoPaterno(persona.getApellidoPaterno());
		cliente.setApellidoMaterno(persona.getApellidoMaterno());
		cliente.setEmail(persona.getCorreo());
		cliente.setCelular(persona.getCelular().toString());
		cliente.setDepartamento(persona.getDepartamento());
		cliente.setProvincia(persona.getProvincia());
		cliente.setDistrito(persona.getDistrito());

		linkPagoRequest.setCliente(cliente);
		linkPagoRequest.setIdentificador(solicitud.getNumeroPropuesta());
		linkPagoRequest.setCodigoEnvio("");
		linkPagoRequest.setEsRecargo(true);
		linkPagoRequest.setPrimeraPrima(false);
		linkPagoRequest.setBloquearEnvio(true);

		LinkPagoResponse linkPagoResponse = new LinkPagoResponse();
		linkPagoResponse.setCodigoRespuesta("01");
		linkPagoResponse.setMensajeRespuesta("OK");
		linkPagoResponse.setToken("token");
		linkPagoResponse.setLink("link");

		System.out.println("linkPagoRequest " + new Gson().toJson(linkPagoRequest));
		when(interseguroRestClient.generateLinkPago(linkPagoRequest)).thenReturn(linkPagoResponse);

		LinkPagoResponseDTO response = solicitudService.enviarLinkPagoRecargo(numeroCotiza, "", null, null);

		assertThat(response.getCodigoRespuesta()).isEqualTo("01");

	}

	@Test
	public void numeroSolicitudNull() {
		String numeroCotiza = "2";
		solicitud.setSolicitudProductos(Collections.emptySet());
		when(solicitudRepository.findByNumeroCotizacion(numeroCotiza)).thenReturn(solicitud);
		LinkPagoResponseDTO response = solicitudService.enviarLinkPago(numeroCotiza, "", null, null);

		assertThat(response.getCodigoRespuesta()).isEqualTo("98");
	}

	@Test
	public void generateLinkNull() {
		String numeroCotiza = "1";

		when(solicitudRepository.findByNumeroCotizacion(numeroCotiza)).thenReturn(solicitud);

		LinkPagoRequest linkPagoRequest = new LinkPagoRequest();
		linkPagoRequest.setProducto(Constantes.COTIZADOR_VIDA_PRODUCTO_VIDA_FREE);
		linkPagoRequest.setProductoAcsele(Constantes.COTIZADOR_VIDA_PRODUCTO_VIDA_FREE.toUpperCase());
		if (solicitudProducto.getTipoProducto().equals("1")) {
			linkPagoRequest.setProducto(Constantes.COTIZADOR_VIDA_PRODUCTO_PLAN_GARANTIZADO);
			linkPagoRequest.setProductoAcsele(Constantes.COTIZADOR_VIDA_PRODUCTO_PLAN_GARANTIZADO.toUpperCase());
		}

		linkPagoRequest.setExpiracion(0);
		// linkPagoRequest.setAplicacion(aplicacion);
		PagoLinkPago pago = new PagoLinkPago();
		pago.setMoneda(solicitud.getMoneda().equals("1") ? "PEN" : "USD");
		pago.setMonedaSimbolo(solicitud.getMoneda().equals("1") ? "S/." : "$.");
		pago.setMonto(solicitudProducto.getPrimaComercial().toString());

		Multitabla multitabla = new Multitabla();
		multitabla.setCodigo("1");
		multitabla.setValor("val");

		when(multitablaRepository.findByCodigoTablaSinEstado("016")).thenReturn(Collections.singletonList(multitabla));
		String frecuencia = multitabla.getValor();
		pago.setFrecuencia(frecuencia);
		long[] array = new long[0];
		pago.setCuotas(array);

		linkPagoRequest.setPago(pago);
		ClienteLinkPago cliente = new ClienteLinkPago();
		multitabla.setValorAux("aux");
		when(multitablaRepository.findByCodigoTablaSinEstado("001")).thenReturn(Collections.singletonList(multitabla));
		cliente.setTipoDocumento(multitabla.getValorAux());
		cliente.setDocumentoIdentidad(persona.getNumeroDocumento());
		cliente.setNombre(persona.getNombres());
		cliente.setApellidoPaterno(persona.getApellidoPaterno());
		cliente.setApellidoMaterno(persona.getApellidoMaterno());
		cliente.setEmail(persona.getCorreo());
		cliente.setCelular(persona.getCelular().toString());
		cliente.setDepartamento(persona.getDepartamento());
		cliente.setProvincia(persona.getProvincia());
		cliente.setDistrito(persona.getDistrito());

		linkPagoRequest.setCliente(cliente);
		linkPagoRequest.setIdentificador(solicitud.getNumeroPropuesta());
		linkPagoRequest.setCodigoEnvio("");
		linkPagoRequest.setEsRecargo(false);
		linkPagoRequest.setPrimeraPrima(true);
		linkPagoRequest.setBloquearEnvio(true);

		LinkPagoResponse linkPagoResponse = new LinkPagoResponse();
		linkPagoResponse.setCodigoRespuesta("01");
		linkPagoResponse.setMensajeRespuesta("OK");
		linkPagoResponse.setToken("token");
		linkPagoResponse.setLink("link");
		System.out.println("linkPagoRequest " + new Gson().toJson(linkPagoRequest));
		when(interseguroRestClient.generateLinkPago(linkPagoRequest)).thenReturn(null);
		LinkPagoResponseDTO response = solicitudService.enviarLinkPago(numeroCotiza, "", null, null);
		assertThat(response.getCodigoRespuesta()).isEqualTo("99");
	}

	@Test
	public void buildBodyLinkPagoTest() {
		when(solicitudProductoRepository.findByIdSolicitud(solicitud.getIdSolicitud())).thenReturn(solicitudProducto);
		solicitudService.buildBodyLinkPago(solicitud, solicitudProducto, valores, false);
		assertThat(solicitud.getNumeroPropuesta()).isNotEmpty();

	}

	@Test
	public void buildBodyLinkPagoRecargoTest() {
		when(solicitudProductoRepository.findByIdSolicitud(solicitud.getIdSolicitud())).thenReturn(solicitudProducto);
		solicitudService.buildBodyLinkPagoRecargo(solicitud, solicitudProducto, valores, false, "");
		assertThat(link).isNotEmpty();
	}

	@Test
	public void buildBodyLinkPagoVidaFreeTest() {
		solicitudProducto.setTipoProducto("2");
		Set<SolicitudProducto> liProductosVF = Collections.singleton(solicitudProducto);
		solicitud.setAseguradoIgualContratante("2");
		solicitud.setSolicitudProductos(liProductosVF);
		solicitud.setMoneda("2");
		solicitud.setSubplan("2");
		when(solicitudProductoRepository.findByIdSolicitud(solicitud.getIdSolicitud())).thenReturn(solicitudProducto);
		solicitudService.buildBodyLinkPago(solicitud, solicitudProducto, valores, false);
		assertThat(solicitud.getNumeroPropuesta()).isNotEmpty();

	}

	@Test
	public void buildBodyLinkPagoRecargoVidaFreeTest() {
		solicitudProducto.setTipoProducto("2");
		Set<SolicitudProducto> liProductosVF = Collections.singleton(solicitudProducto);
		solicitud.setAseguradoIgualContratante("2");
		solicitud.setSolicitudProductos(liProductosVF);
		solicitud.setMoneda("2");
		solicitud.setSubplan("2");
		when(solicitudProductoRepository.findByIdSolicitud(solicitud.getIdSolicitud())).thenReturn(solicitudProducto);
		solicitudService.buildBodyLinkPagoRecargo(solicitud, solicitudProducto, valores, false, "");
		assertThat(link).isNotEmpty();
	}

	@Test
	public void policyIssued() {
		solicitud.setEstadoPropuesta(1);
		solicitud.setEstado("6");
		PolicyIssuedRequestDTO policyIssuedRequestDTO = new PolicyIssuedRequestDTO();
		policyIssuedRequestDTO.setNumeroPropuesta(Long.parseLong(solicitud.getNumeroPropuesta()));
		when(solicitudRepository.findByNumeroCotizacion(solicitud.getNumeroCotizacion())).thenReturn(solicitud);

		SolicitudFiltrado solicitudFiltrado = new SolicitudFiltrado();
		solicitudFiltrado.setTipoDocumento(persona.getTipoDocumento());
		solicitudFiltrado.setNumeroDocumento(persona.getNumeroDocumento());
		solicitudFiltrado.setEstado("1");
		solicitudFiltrado.setNombres(persona.getNombres());
		solicitudFiltrado.setApellidoPaterno(persona.getApellidoPaterno());
		solicitudFiltrado.setApellidoMaterno(persona.getApellidoMaterno());
		when(solicitudFiltradoRepository.findByNumeroCotizacion(solicitud.getNumeroCotizacion()))
				.thenReturn(solicitudFiltrado);

		ObtenerMensajeEmisionesResponse emisionesResponse = new ObtenerMensajeEmisionesResponse();
		emisionesResponse.setSolicitud("completado");
		emisionesResponse.setAcp("completado");
		emisionesResponse.setCotizacion("completado");
		emisionesResponse.setAdn("completado");
		emisionesResponse.setEdn("completado");
		when(globalRestClient.validarArchivos(persona.getTipoDocumento(), persona.getNumeroDocumento(),
				solicitud.getNumeroCotizacion())).thenReturn(emisionesResponse);

		EmisionAutomaticaRequest emisionAutomaticaRequest = new EmisionAutomaticaRequest();
		emisionAutomaticaRequest.setIdSolicitud(solicitud.getIdSolicitud());
		EmisionAutomaticaResponse responseGlobal = new EmisionAutomaticaResponse();
		responseGlobal.setCode("01");
		when(globalRestClient.emisionAutomatica(emisionAutomaticaRequest)).thenReturn(responseGlobal);

		GestiorDocumentoRequest gestiorDocumentoRequest = new GestiorDocumentoRequest();
		gestiorDocumentoRequest.setIdSolicitud(solicitud.getIdSolicitud());
		ListGestiorDocumentoResponse documentoResponse = new ListGestiorDocumentoResponse();
		documentoResponse.setDocumentos("documento");
		List<ListGestiorDocumentoResponse> data = new ArrayList<>();
		data.add(documentoResponse);
		GestiorDocumentoResponse responseGestorDocumento = new GestiorDocumentoResponse();
		responseGestorDocumento.setData(data);
		when(globalRestClient.gestorDocumentos(gestiorDocumentoRequest)).thenReturn(responseGestorDocumento);

		List<ListDocumentoCorreoRequest> listData = new ArrayList<>();
		ListDocumentoCorreoRequest documentoCorreo = new ListDocumentoCorreoRequest();
		documentoCorreo.setName("documento");
		listData.add(documentoCorreo);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String fecha = format.format(new Date());
		DocumentoCorreoRequest documentoCorreoRequest = new DocumentoCorreoRequest();
		documentoCorreoRequest.setFechaEmision(fecha);
		documentoCorreoRequest.setIdSolicitud(solicitud.getIdSolicitud());
		documentoCorreoRequest.setDocumentos(listData);
		DocumentoCorreoResponse correoResponse = new DocumentoCorreoResponse();
		correoResponse.setCodigo("01");
		when(globalRestClient.documentoCorreo(documentoCorreoRequest)).thenReturn(correoResponse);

		BaseResponseDTO response = solicitudService.policyIssued(policyIssuedRequestDTO);
		assertThat(response.getCodigoRespuesta()).isEqualTo("01");
	}

	@Test
	public void policyIssuedFilesNotCompleted() {
		solicitud.setEstadoPropuesta(1);
		solicitud.setEstado("6");
		PolicyIssuedRequestDTO policyIssuedRequestDTO = new PolicyIssuedRequestDTO();
		policyIssuedRequestDTO.setNumeroPropuesta(Long.parseLong(solicitud.getNumeroPropuesta()));
		when(solicitudRepository.findByNumeroCotizacion(solicitud.getNumeroCotizacion())).thenReturn(solicitud);

		SolicitudFiltrado solicitudFiltrado = new SolicitudFiltrado();
		solicitudFiltrado.setTipoDocumento(persona.getTipoDocumento());
		solicitudFiltrado.setNumeroDocumento(persona.getNumeroDocumento());
		solicitudFiltrado.setEstado("1");
		solicitudFiltrado.setNombres(persona.getNombres());
		solicitudFiltrado.setApellidoPaterno(persona.getApellidoPaterno());
		solicitudFiltrado.setApellidoMaterno(persona.getApellidoMaterno());
		when(solicitudFiltradoRepository.findByNumeroCotizacion(solicitud.getNumeroCotizacion()))
				.thenReturn(solicitudFiltrado);

		ObtenerMensajeEmisionesResponse emisionesResponse = new ObtenerMensajeEmisionesResponse();
		emisionesResponse.setSolicitud("completado");
		emisionesResponse.setAcp("completado");
		emisionesResponse.setCotizacion("completado");
		emisionesResponse.setAdn("completado");

		when(globalRestClient.validarArchivos(persona.getTipoDocumento(), persona.getNumeroDocumento(),
				solicitud.getNumeroCotizacion())).thenReturn(emisionesResponse);

		EmisionAutomaticaRequest emisionAutomaticaRequest = new EmisionAutomaticaRequest();
		emisionAutomaticaRequest.setIdSolicitud(solicitud.getIdSolicitud());
		EmisionAutomaticaResponse responseGlobal = new EmisionAutomaticaResponse();
		responseGlobal.setCode("01");
		when(globalRestClient.emisionAutomatica(emisionAutomaticaRequest)).thenReturn(responseGlobal);

		GestiorDocumentoRequest gestiorDocumentoRequest = new GestiorDocumentoRequest();
		gestiorDocumentoRequest.setIdSolicitud(solicitud.getIdSolicitud());
		ListGestiorDocumentoResponse documentoResponse = new ListGestiorDocumentoResponse();
		documentoResponse.setDocumentos("documento");
		List<ListGestiorDocumentoResponse> data = new ArrayList<>();
		data.add(documentoResponse);
		GestiorDocumentoResponse responseGestorDocumento = new GestiorDocumentoResponse();
		responseGestorDocumento.setData(data);
		when(globalRestClient.gestorDocumentos(gestiorDocumentoRequest)).thenReturn(responseGestorDocumento);

		List<ListDocumentoCorreoRequest> listData = new ArrayList<>();
		ListDocumentoCorreoRequest documentoCorreo = new ListDocumentoCorreoRequest();
		documentoCorreo.setName("documento");
		listData.add(documentoCorreo);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String fecha = format.format(new Date());
		DocumentoCorreoRequest documentoCorreoRequest = new DocumentoCorreoRequest();
		documentoCorreoRequest.setFechaEmision(fecha);
		documentoCorreoRequest.setIdSolicitud(solicitud.getIdSolicitud());
		documentoCorreoRequest.setDocumentos(listData);
		DocumentoCorreoResponse correoResponse = new DocumentoCorreoResponse();
		correoResponse.setCodigo("01");
		when(globalRestClient.documentoCorreo(documentoCorreoRequest)).thenReturn(correoResponse);
		when(solicitudProductoRepository.findByIdSolicitud(solicitud.getIdSolicitud())).thenReturn(solicitudProducto);

		Multitabla multitabla = new Multitabla();
		multitabla.setCodigo("1");
		multitabla.setValorAux("VidaFree");

		when(multitablaRepository.findByCodigoTablaSinEstado("015")).thenReturn(Collections.singletonList(multitabla));

		BaseResponseDTO response = solicitudService.policyIssued(policyIssuedRequestDTO);
		assertThat(response.getCodigoRespuesta()).isEqualTo("01");
	}

	@Test
	public void policyIssuedEstadoPropuesta2() {
		solicitud.setEstadoPropuesta(2);
		solicitud.setEstado("6");
		PolicyIssuedRequestDTO policyIssuedRequestDTO = new PolicyIssuedRequestDTO();
		policyIssuedRequestDTO.setNumeroPropuesta(Long.parseLong(solicitud.getNumeroPropuesta()));
		when(solicitudRepository.findByNumeroCotizacion(solicitud.getNumeroCotizacion())).thenReturn(solicitud);

		SolicitudFiltrado solicitudFiltrado = new SolicitudFiltrado();
		solicitudFiltrado.setTipoDocumento(persona.getTipoDocumento());
		solicitudFiltrado.setNumeroDocumento(persona.getNumeroDocumento());
		solicitudFiltrado.setEstado("1");
		solicitudFiltrado.setNombres(persona.getNombres());
		solicitudFiltrado.setApellidoPaterno(persona.getApellidoPaterno());
		solicitudFiltrado.setApellidoMaterno(persona.getApellidoMaterno());
		when(solicitudFiltradoRepository.findByNumeroCotizacion(solicitud.getNumeroCotizacion()))
				.thenReturn(solicitudFiltrado);

		ObtenerMensajeEmisionesResponse emisionesResponse = new ObtenerMensajeEmisionesResponse();
		emisionesResponse.setSolicitud("completado");
		emisionesResponse.setAcp("completado");
		emisionesResponse.setCotizacion("completado");
		emisionesResponse.setAdn("completado");
		emisionesResponse.setEdn("completados");
		when(globalRestClient.validarArchivos(persona.getTipoDocumento(), persona.getNumeroDocumento(),
				solicitud.getNumeroCotizacion())).thenReturn(emisionesResponse);

		EmisionAutomaticaRequest emisionAutomaticaRequest = new EmisionAutomaticaRequest();
		emisionAutomaticaRequest.setIdSolicitud(solicitud.getIdSolicitud());
		EmisionAutomaticaResponse responseGlobal = new EmisionAutomaticaResponse();
		responseGlobal.setCode("01");
		when(globalRestClient.emisionAutomatica(emisionAutomaticaRequest)).thenReturn(responseGlobal);

		GestiorDocumentoRequest gestiorDocumentoRequest = new GestiorDocumentoRequest();
		gestiorDocumentoRequest.setIdSolicitud(solicitud.getIdSolicitud());
		ListGestiorDocumentoResponse documentoResponse = new ListGestiorDocumentoResponse();
		documentoResponse.setDocumentos("documento");
		List<ListGestiorDocumentoResponse> data = new ArrayList<>();
		data.add(documentoResponse);
		GestiorDocumentoResponse responseGestorDocumento = new GestiorDocumentoResponse();
		responseGestorDocumento.setData(data);
		when(globalRestClient.gestorDocumentos(gestiorDocumentoRequest)).thenReturn(responseGestorDocumento);

		List<ListDocumentoCorreoRequest> listData = new ArrayList<>();
		ListDocumentoCorreoRequest documentoCorreo = new ListDocumentoCorreoRequest();
		documentoCorreo.setName("documento");
		listData.add(documentoCorreo);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String fecha = format.format(new Date());
		DocumentoCorreoRequest documentoCorreoRequest = new DocumentoCorreoRequest();
		documentoCorreoRequest.setFechaEmision(fecha);
		documentoCorreoRequest.setIdSolicitud(solicitud.getIdSolicitud());
		documentoCorreoRequest.setDocumentos(listData);
		DocumentoCorreoResponse correoResponse = new DocumentoCorreoResponse();
		correoResponse.setCodigo("01");
		when(globalRestClient.documentoCorreo(documentoCorreoRequest)).thenReturn(correoResponse);

		EnvioCorreoClienteEmisionRequest envioCorreoClienteEmisionRequest = new EnvioCorreoClienteEmisionRequest();
		envioCorreoClienteEmisionRequest.setFlagCliente(true);
		envioCorreoClienteEmisionRequest.setNroSolicitud(solicitud.getNumeroCotizacion());
		envioCorreoClienteEmisionRequest.setNombre(persona.getNombres());
		envioCorreoClienteEmisionRequest.setCorreo(persona.getCorreo());
		EnvioCorreoClienteEmisionResponse responseCorreoGlobal = new EnvioCorreoClienteEmisionResponse();
		responseCorreoGlobal.setCodigo("01");
		when(globalRestClient.envioCorreoCliente(envioCorreoClienteEmisionRequest)).thenReturn(responseCorreoGlobal);

		BaseResponseDTO response = solicitudService.policyIssued(policyIssuedRequestDTO);
		assertThat(response.getCodigoRespuesta()).isEqualTo("01");
	}

	@Test
	public void policyIssuedEstadoPropuesta2Contratante() {
		solicitud.setEstadoPropuesta(2);
		solicitud.setEstado("6");
		solicitud.setAseguradoIgualContratante("2");
		PolicyIssuedRequestDTO policyIssuedRequestDTO = new PolicyIssuedRequestDTO();
		policyIssuedRequestDTO.setNumeroPropuesta(Long.parseLong(solicitud.getNumeroPropuesta()));
		when(solicitudRepository.findByNumeroCotizacion(solicitud.getNumeroCotizacion())).thenReturn(solicitud);

		SolicitudFiltrado solicitudFiltrado = new SolicitudFiltrado();
		solicitudFiltrado.setTipoDocumento(persona.getTipoDocumento());
		solicitudFiltrado.setNumeroDocumento(persona.getNumeroDocumento());
		solicitudFiltrado.setEstado("1");
		solicitudFiltrado.setNombres(persona.getNombres());
		solicitudFiltrado.setApellidoPaterno(persona.getApellidoPaterno());
		solicitudFiltrado.setApellidoMaterno(persona.getApellidoMaterno());
		when(solicitudFiltradoRepository.findByNumeroCotizacion(solicitud.getNumeroCotizacion()))
				.thenReturn(solicitudFiltrado);

		ObtenerMensajeEmisionesResponse emisionesResponse = new ObtenerMensajeEmisionesResponse();
		emisionesResponse.setSolicitud("completado");
		emisionesResponse.setAcp("completado");
		emisionesResponse.setCotizacion("completado");
		emisionesResponse.setAdn("completado");
		emisionesResponse.setEdn("completados");
		when(globalRestClient.validarArchivos(persona.getTipoDocumento(), persona.getNumeroDocumento(),
				solicitud.getNumeroCotizacion())).thenReturn(emisionesResponse);

		EmisionAutomaticaRequest emisionAutomaticaRequest = new EmisionAutomaticaRequest();
		emisionAutomaticaRequest.setIdSolicitud(solicitud.getIdSolicitud());
		EmisionAutomaticaResponse responseGlobal = new EmisionAutomaticaResponse();
		responseGlobal.setCode("01");
		when(globalRestClient.emisionAutomatica(emisionAutomaticaRequest)).thenReturn(responseGlobal);

		GestiorDocumentoRequest gestiorDocumentoRequest = new GestiorDocumentoRequest();
		gestiorDocumentoRequest.setIdSolicitud(solicitud.getIdSolicitud());
		ListGestiorDocumentoResponse documentoResponse = new ListGestiorDocumentoResponse();
		documentoResponse.setDocumentos("documento");
		List<ListGestiorDocumentoResponse> data = new ArrayList<>();
		data.add(documentoResponse);
		GestiorDocumentoResponse responseGestorDocumento = new GestiorDocumentoResponse();
		responseGestorDocumento.setData(data);
		when(globalRestClient.gestorDocumentos(gestiorDocumentoRequest)).thenReturn(responseGestorDocumento);

		List<ListDocumentoCorreoRequest> listData = new ArrayList<>();
		ListDocumentoCorreoRequest documentoCorreo = new ListDocumentoCorreoRequest();
		documentoCorreo.setName("documento");
		listData.add(documentoCorreo);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String fecha = format.format(new Date());
		DocumentoCorreoRequest documentoCorreoRequest = new DocumentoCorreoRequest();
		documentoCorreoRequest.setFechaEmision(fecha);
		documentoCorreoRequest.setIdSolicitud(solicitud.getIdSolicitud());
		documentoCorreoRequest.setDocumentos(listData);
		DocumentoCorreoResponse correoResponse = new DocumentoCorreoResponse();
		correoResponse.setCodigo("01");
		when(globalRestClient.documentoCorreo(documentoCorreoRequest)).thenReturn(correoResponse);

		EnvioCorreoClienteEmisionRequest envioCorreoClienteEmisionRequest = new EnvioCorreoClienteEmisionRequest();
		envioCorreoClienteEmisionRequest.setFlagCliente(true);
		envioCorreoClienteEmisionRequest.setNroSolicitud(solicitud.getNumeroCotizacion());
		envioCorreoClienteEmisionRequest.setNombre(persona.getNombres());
		envioCorreoClienteEmisionRequest.setCorreo(persona.getCorreo());
		EnvioCorreoClienteEmisionResponse responseCorreoGlobal = new EnvioCorreoClienteEmisionResponse();
		responseCorreoGlobal.setCodigo("01");
		when(globalRestClient.envioCorreoCliente(envioCorreoClienteEmisionRequest)).thenReturn(responseCorreoGlobal);

		BaseResponseDTO response = solicitudService.policyIssued(policyIssuedRequestDTO);
		assertThat(response.getCodigoRespuesta()).isEqualTo("01");
	}

	@Test
	public void policyIssuedPendientePago() {
		solicitud.setEstadoPropuesta(2);
		solicitud.setEstado("5");
		PolicyIssuedRequestDTO policyIssuedRequestDTO = new PolicyIssuedRequestDTO();
		policyIssuedRequestDTO.setNumeroPropuesta(Long.parseLong(solicitud.getNumeroPropuesta()));
		when(solicitudRepository.findByNumeroCotizacion(solicitud.getNumeroCotizacion())).thenReturn(solicitud);

		SolicitudFiltrado solicitudFiltrado = new SolicitudFiltrado();
		solicitudFiltrado.setTipoDocumento(persona.getTipoDocumento());
		solicitudFiltrado.setNumeroDocumento(persona.getNumeroDocumento());
		solicitudFiltrado.setEstado("1");
		solicitudFiltrado.setNombres(persona.getNombres());
		solicitudFiltrado.setApellidoPaterno(persona.getApellidoPaterno());
		solicitudFiltrado.setApellidoMaterno(persona.getApellidoMaterno());
		when(solicitudFiltradoRepository.findByNumeroCotizacion(solicitud.getNumeroCotizacion()))
				.thenReturn(solicitudFiltrado);

		BaseResponseDTO response = solicitudService.policyIssued(policyIssuedRequestDTO);
		assertThat(response.getCodigoRespuesta()).isEqualTo("99");
	}

	@Test
	public void policyIssuedNull() {
		solicitud.setEstadoPropuesta(1);
		solicitud.setEstado("6");
		PolicyIssuedRequestDTO policyIssuedRequestDTO = new PolicyIssuedRequestDTO();
		policyIssuedRequestDTO.setNumeroPropuesta(Long.parseLong(solicitud.getNumeroPropuesta()));
		when(solicitudRepository.findByNumeroCotizacion(solicitud.getNumeroCotizacion())).thenReturn(solicitud);

		SolicitudFiltrado solicitudFiltrado = new SolicitudFiltrado();
		solicitudFiltrado.setTipoDocumento(persona.getTipoDocumento());
		solicitudFiltrado.setNumeroDocumento(persona.getNumeroDocumento());
		solicitudFiltrado.setEstado("1");
		solicitudFiltrado.setNombres(persona.getNombres());
		solicitudFiltrado.setApellidoPaterno(persona.getApellidoPaterno());
		solicitudFiltrado.setApellidoMaterno(persona.getApellidoMaterno());
		when(solicitudFiltradoRepository.findByNumeroCotizacion(solicitud.getNumeroCotizacion()))
				.thenReturn(solicitudFiltrado);

		ObtenerMensajeEmisionesResponse emisionesResponse = new ObtenerMensajeEmisionesResponse();
		emisionesResponse.setSolicitud("completado");
		emisionesResponse.setAcp("completado");
		emisionesResponse.setCotizacion("completado");
		emisionesResponse.setAdn("completado");
		emisionesResponse.setEdn("completado");
		when(globalRestClient.validarArchivos(persona.getTipoDocumento(), persona.getNumeroDocumento(),
				solicitud.getNumeroCotizacion())).thenReturn(emisionesResponse);

		EmisionAutomaticaRequest emisionAutomaticaRequest = new EmisionAutomaticaRequest();
		emisionAutomaticaRequest.setIdSolicitud(solicitud.getIdSolicitud());
		EmisionAutomaticaResponse responseGlobal = new EmisionAutomaticaResponse();
		responseGlobal.setCode("01");
		when(globalRestClient.emisionAutomatica(emisionAutomaticaRequest)).thenReturn(responseGlobal);

		GestiorDocumentoRequest gestiorDocumentoRequest = new GestiorDocumentoRequest();
		gestiorDocumentoRequest.setIdSolicitud(solicitud.getIdSolicitud());
		ListGestiorDocumentoResponse documentoResponse = new ListGestiorDocumentoResponse();
		documentoResponse.setDocumentos("documento");
		List<ListGestiorDocumentoResponse> data = new ArrayList<>();
		data.add(documentoResponse);
		GestiorDocumentoResponse responseGestorDocumento = new GestiorDocumentoResponse();
		responseGestorDocumento.setData(data);
		when(globalRestClient.gestorDocumentos(gestiorDocumentoRequest)).thenReturn(null);

		BaseResponseDTO response = solicitudService.policyIssued(policyIssuedRequestDTO);
		assertThat(response.getCodigoRespuesta()).isEqualTo("99");
	}

	@Test
	public void reprocesarEmisionAutomaticaDocumentos() {
		String numeroCotiza = "1";
		solicitud.setEstadoPropuesta(1);
		solicitud.setEstado("6");
		when(solicitudRepository.findByNumeroCotizacion(numeroCotiza)).thenReturn(solicitud);

		SolicitudFiltrado solicitudFiltrado = new SolicitudFiltrado();
		solicitudFiltrado.setTipoDocumento(persona.getTipoDocumento());
		solicitudFiltrado.setNumeroDocumento(persona.getNumeroDocumento());
		solicitudFiltrado.setEstado("1");
		solicitudFiltrado.setNombres(persona.getNombres());
		solicitudFiltrado.setApellidoPaterno(persona.getApellidoPaterno());
		solicitudFiltrado.setApellidoMaterno(persona.getApellidoMaterno());
		when(solicitudFiltradoRepository.findByNumeroCotizacion(solicitud.getNumeroCotizacion()))
				.thenReturn(solicitudFiltrado);
		ObtenerMensajeEmisionesResponse emisionesResponse = new ObtenerMensajeEmisionesResponse();
		emisionesResponse.setSolicitud("completado");
		emisionesResponse.setAcp("completado");
		emisionesResponse.setCotizacion("completado");
		emisionesResponse.setAdn("completado");
		emisionesResponse.setEdn("completado");
		when(globalRestClient.validarArchivos(persona.getTipoDocumento(), persona.getNumeroDocumento(),
				solicitud.getNumeroCotizacion())).thenReturn(emisionesResponse);

		PolicyIssuedRequestDTO policyIssuedRequestDTO = new PolicyIssuedRequestDTO();
		policyIssuedRequestDTO.setNumeroPropuesta(Long.parseLong(solicitud.getNumeroPropuesta()));
		BaseResponseDTO response = solicitudService.reprocesarEmisionAutomaticaDocumentos(policyIssuedRequestDTO);
		assertThat(response.getCodigoRespuesta()).isEqualTo("01");
	}

	@Test
	public void reprocesarEmisionAutomaticaDocumentosDocFalta() {
		String numeroCotiza = "1";
		solicitud.setEstadoPropuesta(1);
		solicitud.setEstado("6");
		when(solicitudRepository.findByNumeroCotizacion(numeroCotiza)).thenReturn(solicitud);

		SolicitudFiltrado solicitudFiltrado = new SolicitudFiltrado();
		solicitudFiltrado.setTipoDocumento(persona.getTipoDocumento());
		solicitudFiltrado.setNumeroDocumento(persona.getNumeroDocumento());
		solicitudFiltrado.setEstado("1");
		solicitudFiltrado.setNombres(persona.getNombres());
		solicitudFiltrado.setApellidoPaterno(persona.getApellidoPaterno());
		solicitudFiltrado.setApellidoMaterno(persona.getApellidoMaterno());
		when(solicitudFiltradoRepository.findByNumeroCotizacion(solicitud.getNumeroCotizacion()))
				.thenReturn(solicitudFiltrado);
		ObtenerMensajeEmisionesResponse emisionesResponse = new ObtenerMensajeEmisionesResponse();
		emisionesResponse.setSolicitud("completado");
		emisionesResponse.setAcp("completado");
		emisionesResponse.setCotizacion("completados");
		emisionesResponse.setAdn("completado");
		emisionesResponse.setEdn("completado");
		when(globalRestClient.validarArchivos(persona.getTipoDocumento(), persona.getNumeroDocumento(),
				solicitud.getNumeroCotizacion())).thenReturn(emisionesResponse);

		PolicyIssuedRequestDTO policyIssuedRequestDTO = new PolicyIssuedRequestDTO();
		policyIssuedRequestDTO.setNumeroPropuesta(Long.parseLong(solicitud.getNumeroPropuesta()));
		BaseResponseDTO response = solicitudService.reprocesarEmisionAutomaticaDocumentos(policyIssuedRequestDTO);
		assertThat(response.getCodigoRespuesta()).isEqualTo("99");
	}

	@Test
	public void reprocesarEmisionAutomaticaNoPago() {
		String numeroCotiza = "1";
		solicitud.setEstadoPropuesta(1);
		solicitud.setEstado("5");
		when(solicitudRepository.findByNumeroCotizacion(numeroCotiza)).thenReturn(solicitud);

		SolicitudFiltrado solicitudFiltrado = new SolicitudFiltrado();
		solicitudFiltrado.setTipoDocumento(persona.getTipoDocumento());
		solicitudFiltrado.setNumeroDocumento(persona.getNumeroDocumento());
		solicitudFiltrado.setEstado("1");
		solicitudFiltrado.setNombres(persona.getNombres());
		solicitudFiltrado.setApellidoPaterno(persona.getApellidoPaterno());
		solicitudFiltrado.setApellidoMaterno(persona.getApellidoMaterno());

		PolicyIssuedRequestDTO policyIssuedRequestDTO = new PolicyIssuedRequestDTO();
		policyIssuedRequestDTO.setNumeroPropuesta(Long.parseLong(solicitud.getNumeroPropuesta()));
		BaseResponseDTO response = solicitudService.reprocesarEmisionAutomaticaDocumentos(policyIssuedRequestDTO);
		assertThat(response.getCodigoRespuesta()).isEqualTo("99");
	}

	@Test
	public void reprocesarEmisionAutomaticaDocumentosException() {
		String numeroCotiza = "1";

		when(solicitudRepository.findByNumeroCotizacion(numeroCotiza)).thenReturn(solicitud);

		PolicyIssuedRequestDTO policyIssuedRequestDTO = new PolicyIssuedRequestDTO();
		policyIssuedRequestDTO.setNumeroPropuesta(Long.parseLong(solicitud.getNumeroPropuesta()));
		BaseResponseDTO response = solicitudService.reprocesarEmisionAutomaticaDocumentos(policyIssuedRequestDTO);
		assertThat(response.getCodigoRespuesta()).isEqualTo("99");
	}

	@Test
	public void valorMultiTabla() {
		Multitabla multitabla = new Multitabla();
		multitabla.setCodigo("1");
		multitabla.setValorAux("VidaFree");
		multitabla.setValor("DNI");
		when(multitablaRepository.findByCodigoTablaSinEstado("001")).thenReturn(Collections.singletonList(multitabla));

		String codigoTabla = "001";
		String codigo = "1";
		String campo = "valor";
		String valorMultitabla = solicitudService.valorMultiTabla(codigoTabla, codigo, campo);

		assertThat(valorMultitabla).isEqualTo("DNI");
	}

	@Test
	public void valorAuxMultiTabla() {
		Multitabla multitabla = new Multitabla();
		multitabla.setCodigo("1");
		multitabla.setValorAux("VidaFree");
		multitabla.setValor("DNI");
		when(multitablaRepository.findByCodigoTablaSinEstado("001")).thenReturn(Collections.singletonList(multitabla));

		String codigoTabla = "001";
		String codigo = "1";
		String campo = "valorAux";
		String valorMultitabla = solicitudService.valorMultiTabla(codigoTabla, codigo, campo);

		assertThat(valorMultitabla).isEqualTo("VidaFree");
	}

	@Test
	public void valorCrmMultiTabla() {
		Multitabla multitabla = new Multitabla();
		multitabla.setCodigo("1");
		multitabla.setValorAux("00001");
		multitabla.setValorCrm("00001");
		multitabla.setValor("DNI");
		when(multitablaRepository.findByCodigoTablaSinEstado("001")).thenReturn(Collections.singletonList(multitabla));

		String codigoTabla = "001";
		String codigo = "1";
		String campo = "valorCRM";
		String valorMultitabla = solicitudService.valorMultiTabla(codigoTabla, codigo, campo);

		assertThat(valorMultitabla).isEqualTo("00001");
	}

	@Test
	public void valorCodigoNullMultiTabla() {
		String codigoTabla = "001";
		String codigo = "";
		String campo = "valorCRM";
		String valorMultitabla = solicitudService.valorMultiTabla(codigoTabla, codigo, campo);

		assertThat(valorMultitabla).isEmpty();
	}

	@Test
	public void valorNullMultiTabla() {
		String codigoTabla = "001";
		String codigo = "1";
		String campo = "valorCRM";
		String valorMultitabla = solicitudService.valorMultiTabla(codigoTabla, codigo, campo);
		when(multitablaRepository.findByCodigoTablaSinEstado("001")).thenReturn(Collections.emptyList());

		assertThat(valorMultitabla).isEmpty();
	}

	@Test
	public void validaFiles() {
		ArchivoResponse archivoResponse = new ArchivoResponse();
		ArchivoResponse[] fileResponse = new ArchivoResponse[6];
		archivoResponse.setName("adn_47195955.pdf");
		fileResponse[0] = archivoResponse;
		archivoResponse = new ArchivoResponse();
		archivoResponse.setName("SolicitudDigital_47195955_501007478.pdf");
		fileResponse[1] = archivoResponse;
		archivoResponse = new ArchivoResponse();
		archivoResponse.setName("cotizacion_501007478_18072023093019.pdf");
		fileResponse[2] = archivoResponse;
		archivoResponse = new ArchivoResponse();
		archivoResponse.setName("EN_501007478_47195955.pdf");
		fileResponse[3] = archivoResponse;
		archivoResponse = new ArchivoResponse();
		archivoResponse.setName("AcpDigital_47195955_501007478.pdf");
		fileResponse[4] = archivoResponse;
		archivoResponse = new ArchivoResponse();
		archivoResponse.setName("cotizacion-501007478_18072023093019.pdf");
		fileResponse[5] = archivoResponse;

		ObtenerMensajeEmisionesResponse obtenerMsge = new ObtenerMensajeEmisionesResponse();
		String numDoc = "47195955";
		String nroCot = "501007478";

		String tagSolicitud = "SolicitudDigital_" + numDoc + "_" + nroCot;
		String tagCotizacion = "cotizacion_" + nroCot;
		String tagAdn = "adn_" + numDoc;
		String tagEdn = "EN_" + nroCot;
		String tagAcp = "AcpDigital_" + numDoc + "_" + nroCot;

		List<String> listTag = Arrays.asList(tagSolicitud, tagCotizacion, tagAdn, tagEdn, tagAcp);
		System.out.println("listTag" + listTag);
		Arrays.stream(fileResponse).forEach(x -> {
			x.setName(x.getName().replace("cotizacion-", "cotizacion_"));
			boolean ans = listTag.stream().anyMatch(l -> x.getName().contains(l));
			System.out.println("listTag--------" + ans + "------------" + x.getName());
			if (ans) {
				System.out.println("Documento " + x.getName() + "Encontrado!!!!!!");
				obtenerMsge.setAcp("completado");
			}

		});

		System.out.println(new Gson().toJson(obtenerMsge));
		for (int i = 0; i < fileResponse.length; i++) {
			fileResponse[i].setName(fileResponse[i].getName().replace("cotizacion-", "cotizacion_"));
			System.out.println(fileResponse[i].getName());

			if (fileResponse[i].getName().contains(tagAcp)) {
				System.out.println("Documento Acp Encontrado!!!!!!");
				obtenerMsge.setAcp("completado");
			}
			if (fileResponse[i].getName().contains(tagAdn)) {
				System.out.println("Documento Adn Encontrado!!!!!!");
				obtenerMsge.setAdn("completado");
			}
			if (fileResponse[i].getName().contains(tagEdn)) {
				System.out.println("Documento Edn Encontrado!!!!!!");
				obtenerMsge.setEdn("completado");
			}
			if (fileResponse[i].getName().contains(tagCotizacion)) {
				System.out.println("Documento Cotizacion Encontrado!!!!!!");
				obtenerMsge.setCotizacion("completado");
			}
			if (fileResponse[i].getName().contains(tagSolicitud)) {
				System.out.println("Documento SolicitudDigital Encontrado!!!!!!");
				obtenerMsge.setSolicitud("completado");
			}
		}

		System.out.println(new Gson().toJson(obtenerMsge));
		assertThat(obtenerMsge.getAcp()).isNotEmpty();
	}

}
