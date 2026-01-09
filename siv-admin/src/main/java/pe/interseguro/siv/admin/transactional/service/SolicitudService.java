package pe.interseguro.siv.admin.transactional.service;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import pe.interseguro.siv.common.dto.request.IndenovaNotificacionRequestDTO;
import pe.interseguro.siv.common.dto.request.ObtenerPubSubSendRequestDTO;
import pe.interseguro.siv.common.dto.request.ObtenerRequestEvaluatorRequestDTO;
import pe.interseguro.siv.common.dto.request.PolicyIssuedRequestDTO;
import pe.interseguro.siv.common.dto.request.SolicitudFiltroRequestDTO;
import pe.interseguro.siv.common.dto.request.SolicitudFinalizarProcesoRequestDTO;
import pe.interseguro.siv.common.dto.request.SolicitudFinalizarProcesoResponseDTO;
import pe.interseguro.siv.common.dto.request.SolicitudGuardarRequestDTO;
import pe.interseguro.siv.common.dto.request.SolicitudRegistroPagoRequestDTO;
import pe.interseguro.siv.common.dto.request.SolicitudRequestDTO;
import pe.interseguro.siv.common.dto.request.SolicitudSMSRequestDTO;
import pe.interseguro.siv.common.dto.request.SolicitudValidarCodigoRequestDTO;
import pe.interseguro.siv.common.dto.request.TokenRequestDTO;
import pe.interseguro.siv.common.dto.response.BaseResponseDTO;
import pe.interseguro.siv.common.dto.response.CMRObtenerArchivoResponseDTO;
import pe.interseguro.siv.common.dto.response.CotizaDetalleResponseDTO;
import pe.interseguro.siv.common.dto.response.CotizadorCorrelativoResponseDTO;
import pe.interseguro.siv.common.dto.response.LinkPagoResponseDTO;
import pe.interseguro.siv.common.dto.response.ObtenerDocumentoResponseDTO;
import pe.interseguro.siv.common.dto.response.ObtenerPubSubSendResponseDTO;
import pe.interseguro.siv.common.dto.response.ObtenerRequestEvaluatorResponseDTO;
import pe.interseguro.siv.common.dto.response.PagoInicializacionResponseDTO;
import pe.interseguro.siv.common.dto.response.RecotizacionValidarResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudAcreditacionResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudCotizacionesTraspasoResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudDetalleResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudFormularioResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudGuardarResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudItemResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudReglamentoResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudResponseDTO;
import pe.interseguro.siv.common.dto.response.TokenLinkPagoResponseDTO;
import pe.interseguro.siv.common.persistence.db.mysql.domain.Solicitud;

public interface SolicitudService {

	/**
	 * Valida las noticiaciones de la Firma de Solicitud
	 * 
	 * @param indenovaNotificacionRequestDTO
	 * @return boolean
	 */
	public boolean validarNotificacion(IndenovaNotificacionRequestDTO indenovaNotificacionRequestDTO);

	/**
	 * Lista de solicitudes
	 * 
	 * @param solicitudFiltroRequestDTO Filtros para la solicitud
	 * @return
	 */
	public SolicitudResponseDTO lista(SolicitudFiltroRequestDTO solicitudFiltroRequestDTO);

	/**
	 * Cambia el estado de la solicitud
	 * 
	 * @param SolicitudRequestDTO
	 * @return
	 */
	public SolicitudItemResponseDTO editar(SolicitudRequestDTO solicitud);

	public SolicitudItemResponseDTO obtenerRegistro(Long id);

	public SolicitudDetalleResponseDTO obtenerDetalleSolicitud(String numeroCotizacion, String idUsuario, String device,
			String os);

	public String generateTemp(Long id) throws FileNotFoundException, IOException;

	public ByteArrayInputStream printPDFSolicitud(String numeroCotizacion);

	public ByteArrayInputStream printPDFSolicitud2(String numeroCotizacion, String numeroDocumento);

	public ByteArrayInputStream printPDFAcp(String numeroCotizacion);

	public boolean enviarCorreo(Long id, String tipoProducto);

//	public boolean enviarCorreoSME(Long id, String tipoProducto);

	public BaseResponseDTO enviarDatosSolicitudCRM(Long idSolicitud);

	public boolean enviarSMS(SolicitudSMSRequestDTO solicitudSMSRequestDTO);

	public BaseResponseDTO validarCodigo(SolicitudValidarCodigoRequestDTO solicitudValidarCodigoRequestDTO);

	public BaseResponseDTO validarCodigoAseguradoyContratante(
			SolicitudValidarCodigoRequestDTO solicitudValidarCodigoRequestDTO);

	public SolicitudReglamentoResponseDTO obtenerReglamento(String tipoDocumento, String numeroDocumento,
			Long numeroCotizacion, String usuarioLogin, String agenteNombres, String agenteCorreo,
			String agenteNumVendedor, String agenteIdCRM, CotizaDetalleResponseDTO cotizacionDetalle,
			String tipoProducto);

	/* Esteban */
	public SolicitudCotizacionesTraspasoResponseDTO obtenerCotizacionesTraspaso(String numeroDocumento);

	public SolicitudAcreditacionResponseDTO consultarAcreditacion(String numCotizacion);
	/**/

	public SolicitudFormularioResponseDTO obtenerDatosFormulario(Long idSolicitud, Long numeroCotizacion,
			CotizaDetalleResponseDTO cotizacionDetalle, String idUsuario, String device, String os);

	public SolicitudGuardarResponseDTO guardar(SolicitudGuardarRequestDTO solicitudGuardarRequestDTO);

	public SolicitudGuardarResponseDTO completarSolicitudFirmar(Long idSolicitud, String usuarioLogin,
			SolicitudGuardarResponseDTO response);

	public SolicitudFinalizarProcesoResponseDTO finalizarProcesoSolicitud(
			SolicitudFinalizarProcesoRequestDTO solicitudFinalizarProcesoRequestDTO);

	public void generarInformacionSolicitud(Solicitud solicitud, String tipoProducto);

	public boolean existeArchivoSolicitud(String numeroCotizacion);

	public BaseResponseDTO obtenerURLVidaFree(TokenRequestDTO tokenRequestDTO);


	// public BaseResponseDTO finalizarCotizacion(CotizacionCrmOportunidadRequestDTO
	// request);

	public BaseResponseDTO enviarDatosSolicitudManualCRM(String numeroPropuesta);

//	public NiubizPrimeraPrimaResponseDTO efectuarPago(SolicitudPagoCargoRequestDTO cargoRequestDTO);

	public BaseResponseDTO registrarAfiliacionPropuesta(SolicitudRegistroPagoRequestDTO pagoRequestDTO);

	public BaseResponseDTO registrarAfiliacionTraspaso(SolicitudRegistroPagoRequestDTO pagoRequestDTO);

	// public String formatoBBVA(String numeroTarjeta);

    public LinkPagoResponseDTO enviarLinkPago(String numeroPropuesta, String idUsuario, String device, String os);
    
    public LinkPagoResponseDTO enviarLinkPagoHelp(String numeroPropuesta, String idUsuario, String device, String os);

	public TokenLinkPagoResponseDTO decryptLinkToken(String token);

	public PagoInicializacionResponseDTO obtenerInicializacionPago();

//	public NiubizSessionResponseDTO activarSesionNiubiz(NiubizSessionRequestDTO request);

//	public NiubizTokenizeResponseDTO tokenizarTarjetaNiubiz(NiubizTokenizeRequestDTO request);

//	public NiubizPrimeraPrimaResponseDTO primeraPrimaNiubiz(NiubizPrimeraPrimaRequestDTO request);

	public BaseResponseDTO subirArchivo(MultipartFile file, String numeroCotizacion);

	public CMRObtenerArchivoResponseDTO obtenerArchivo(String files, String nroCot);

	public ObtenerDocumentoResponseDTO obtenerDocumento(String numeroCotizacion);

	public ByteArrayInputStream leerArchivo(String numeroCotizacion, String nombreArchivo);

//	public BaseResponseDTO registrarAfiliacionPropuestaCvc(SolicitudRegistroPagoRequestDTO pagoRequestDTO);

//	public NiubizSessionResponseDTO activarSesionNiubizRecargo(NiubizSessionRequestDTO request);

//	public TokenLinkPagoResponseDTO decryptLinkTokenRecargo(String token);

//	public NiubizTokenizeResponseDTO tokenizarTarjetaNiubizRecargo(NiubizTokenizeRequestDTO request);

//	public NiubizPrimeraPrimaResponseDTO primeraPrimaNiubizRecargo(NiubizPrimeraPrimaRequestDTO request);

//	public PagoInicializacionResponseDTO obtenerInicializacionPagoRecargo();

//	public NiubizPrimeraPrimaResponseDTO efectuarPagoRecargo(SolicitudPagoCargoRequestDTO cargoRequestDTO);

	public LinkPagoResponseDTO enviarLinkPagoRecargo(String numeroPropuesta, String idUsuario, String device, String os);

	public ObtenerPubSubSendResponseDTO obtenerPubSubSend(ObtenerPubSubSendRequestDTO request);

	public ObtenerRequestEvaluatorResponseDTO obtenerRequestEvaluator(ObtenerRequestEvaluatorRequestDTO request);

	public BaseResponseDTO policyIssued(PolicyIssuedRequestDTO request);

	public BaseResponseDTO reprocesarEmisionAutomaticaDocumentos(PolicyIssuedRequestDTO request);

	public BaseResponseDTO validadorDocumentos(PolicyIssuedRequestDTO request);

	public BaseResponseDTO reprocesarDocumentos(PolicyIssuedRequestDTO request);

	public RecotizacionValidarResponseDTO validarRecotizacion(String cotizacion);

	public String valorMultiTabla(String codigoTabla, String codigo,String campo);
}
