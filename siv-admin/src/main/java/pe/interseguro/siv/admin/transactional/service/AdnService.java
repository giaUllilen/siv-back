package pe.interseguro.siv.admin.transactional.service;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

import pe.interseguro.siv.common.bean.GenericoComboBean;
import pe.interseguro.siv.common.persistence.rest.consentimiento.ConsentimientoRequest;
import pe.interseguro.siv.common.dto.request.ADNAutoguardadoRequestDTO;
import pe.interseguro.siv.common.dto.request.ADNClienteRequestDTO;
import pe.interseguro.siv.common.dto.request.ADNFirmaRequestDTO;
import pe.interseguro.siv.common.dto.request.ADNPolizaRequestDTO;
import pe.interseguro.siv.common.dto.request.ADNPotencialRequestDTO;
import pe.interseguro.siv.common.dto.request.ADNRegistroReferidoRequestDTO;
import pe.interseguro.siv.common.dto.request.ADNRegistroRequestDTO;
import pe.interseguro.siv.common.dto.request.ADNUsuarioRequestDTO;
import pe.interseguro.siv.common.dto.request.UsuarioIngresoRequestDTO;
import pe.interseguro.siv.common.dto.response.ADNAutoguardadoResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNConsentimientoAceptadoResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNConsultaClienteDTResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNConsultaLdpdpResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNConsultaPolizaBUCResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNConsultaPolizaResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNConsultaPotencialDTResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNConsultaUsuarioDTResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNFirmaResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNInicializacionResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNRegistroReferidoResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNRegistroResponseDTO;
import pe.interseguro.siv.common.dto.response.ADNReglamentoResponseDTO;
import pe.interseguro.siv.common.dto.response.BaseResponseDTO;
import pe.interseguro.siv.common.dto.response.UsuarioIngresoResponseDTO;

/**
 * Transaccional del ADN
 * 
 * @author ti-is
 *
 */
public interface AdnService {

	/**
	 * Obtener los datos iniciales del ADN
	 * 
	 * @return
	 */
	public ADNInicializacionResponseDTO obtenerInicializacion(String idUsuario, String device, String os);

	/**
	 * Obtener los datos del Reglamento del ADN
	 * 
	 * @param tipoDocCliente
	 * @param numDocCliente
	 * @param idUsuario
	 * @return
	 */
	public ADNReglamentoResponseDTO obtenerReglamento(String tipoDocCliente, String numDocCliente, String idUsuario,
			String correoUsuario, String device, String os);

	/**
	 * 
	 * @param tipoDocCliente
	 * @param numDocCliente
	 * @return
	 */
	public ADNReglamentoResponseDTO obtenerInformacionClienteVarios(String tipoDocCliente, String numDocCliente,
			String idUsuario, String device, String os);

	/**
	 * Obtener Polizas Cliente
	 * 
	 * @param tipoDocCliente
	 * @param numDocCliente
	 * @return
	 */
	public ADNConsultaPolizaBUCResponseDTO obtenerPolizasCliente(String tipoDocCliente, String numDocCliente,
			String idUsuario, String device, String os);

	/**
	 * Registrar firma electornica
	 * 
	 * @param aDNFirmaRequestDTO
	 * @return
	 */
	public ADNFirmaResponseDTO registrarFirma(ADNFirmaRequestDTO aDNFirmaRequestDTO);

	/**
	 * 
	 * @param aDNFirmaRequestDTO
	 * @return
	 */
	public ADNFirmaResponseDTO registrarLDPDP(ADNFirmaRequestDTO aDNFirmaRequestDTO);

	/**
	 * Registro del ADN
	 * 
	 * @param aDNRegistroRequestDTO
	 * @return
	 */
	public ADNRegistroResponseDTO registrarADNS(ADNRegistroRequestDTO aDNRegistroRequestDTO);

	/**
	 * Registro del ADN
	 * 
	 * @param aDNRegistroRequestDTO
	 * @return
	 */
	public ADNRegistroResponseDTO registrarInternoADNS(ADNRegistroRequestDTO aDNRegistroRequestDTO);

	/**
	 * Auto guardado del ADN
	 * 
	 * @param aDNAutoguardadoRequestDTO
	 * @return
	 */
	public ADNAutoguardadoResponseDTO autoguardado(ADNAutoguardadoRequestDTO aDNAutoguardadoRequestDTO);

	/**
	 * Usuario Login
	 * 
	 * @param aDNAutoguardadoRequestDTO
	 * @return
	 */
	// public UsuarioIngresoResponseDTO validarUsuario(UsuarioIngresoRequestDTO
	// usuarioIngresoRequestDTO);

	/**
	 * 
	 * @param aDNRegistroReferidoRequestDTO
	 * @return
	 */
	public ADNRegistroReferidoResponseDTO registrarReferidoADNS(
			ADNRegistroReferidoRequestDTO aDNRegistroReferidoRequestDTO);

	/**
	 * 
	 * @param aDNPolizaRequestDTO
	 * @return
	 */
	public ADNConsultaPolizaResponseDTO busquedaPoliza(ADNPolizaRequestDTO aDNPolizaRequestDTO);

	/**
	 * 
	 * @param aDNClienteRequestDTO
	 * @return
	 */
	public ADNConsultaClienteDTResponseDTO clienteDT(ADNClienteRequestDTO aDNClienteRequestDTO);

	/**
	 * 
	 * @param aDNUsuarioRequestDTO
	 * @return
	 */
	public ADNConsultaUsuarioDTResponseDTO usuarioDT(ADNUsuarioRequestDTO aDNUsuarioRequestDTO);

	/**
	 * 
	 * @param aDNUsuarioRequestDTO
	 * @return
	 */
	public ADNConsultaPotencialDTResponseDTO clientePotencialDT(ADNPotencialRequestDTO aDNUsuarioRequestDTO);

	/**
	 * 
	 * @param String
	 * @return
	 */
	public List<GenericoComboBean> codigoTabla(String codigo);

	// public ByteArrayInputStream generarPDFADN2(String tipoDocumento, String
	// numeroDocumento);

	public ByteArrayInputStream printPDFAdn(ADNRegistroRequestDTO aDNRegistroRequestDTO);

	public ADNConsultaLdpdpResponseDTO obtenerldpdp(String tipoDocumento, String numeroDocumento);

	public BaseResponseDTO generarPDFADN2(String tipoDocumento, String numeroDocumento);
	
	public ByteArrayInputStream descargarPDF(String nombreDocumento);

	public BaseResponseDTO createConsentimiento(Integer idTipo, ADNRegistroRequestDTO payload);

	public BaseResponseDTO sendConsentimiento(Integer idTipo, String dni, String usuario, Integer tipoDocumento,
			Boolean userOnly, Map<String, Object> request);

	public BaseResponseDTO sendPDFConsentimiento(Integer idTipo, String dni, Map<String, Object> dataConsentimiento,
			Boolean userOnly, Map<String, Object> request);

	public ADNConsentimientoAceptadoResponseDTO processConsentimientoAccepted(Map<String, Object> token);
	
	public BaseResponseDTO updateConsentimiento(String idTipo, ADNRegistroRequestDTO payload);

	public Map<String, Object> getDataConsentimiento(String idUsuario, String numeroDocumento,
			Integer tipoDocumentoADN);

	/**
	 * Obtener datos de consentimiento
	 * 
	 * @param idUsuario
	 * @param tipoDocumento
	 * @param numeroDocumento
	 * @return Map con los datos de consentimiento
	 */
	public Map<String, Object> obtenerConsentimiento(String idUsuario, String tipoDocumento, String numeroDocumento);
}
