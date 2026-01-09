package pe.interseguro.siv.admin.transactional.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import pe.interseguro.siv.common.bean.CotizacionPDFBean;
import pe.interseguro.siv.common.dto.request.CotizacionCrmRequestDTO;
import pe.interseguro.siv.common.dto.request.TokenRequestDTO;
import pe.interseguro.siv.common.dto.request.TransmisionAcseleRequestDTO;
import pe.interseguro.siv.common.dto.response.*;
import pe.interseguro.siv.common.persistence.rest.estudionecesidad.response.GenerarPdfResponse;

public interface CotizaService {

	/**
	 * Ejemplo mongoDB con multitabla
	 * 
	 * @param codigoTabla
	 * @return
	 */
	public CotizaTablaResponseDTO obtenerTabla(String codigoTabla);

	public LinkPagoCotResponseDTO getLinkPago(String codigoTabla);


	/**
	 * Retorna la lista de cotizaciones de un producto por cliente en base a su
	 * documento
	 * 
	 * @param documento
	 * @param producto
	 * @return
	 */
	public CotizaListaResponseDTO listaDocumentoProducto(String documento, String producto);

	/**
	 * Retorna el detalle de la cotizacion
	 * 
	 * @param nroCotizacion
	 * @return
	 */
	public CotizaDetalleResponseDTO detalle(Long nroCotizacion);

	/**
	 * 
	 * @param nroCotizacion
	 * @return
	 */
	public CotizaDetalleResponseDTO detalleCotizacionVidaFree(Long nroCotizacion);

	/**
	 * 
	 * @param documento
	 * @return
	 */
	public CotizaListaResponseDTO listaCotizacionesVida(String documento, String idUsuario, String device, String os);

	/**
	 * 
	 * @param idOportunidad
	 * @return
	 */
	//public CotizaUrlResponse ObtenerURLCotizadorVida(String idOportunidad, String idUsuario, String device, String os);
	public CotizaUrlResponse ObtenerURLCotizadorVida(String numDocumento, String idUsuario, String device, String os);
	/**
	 * 
	 * @param numeroDocumento
	 * @param idOportunidadCrm
	 * @param idAgenteCrm
	 * @param codigoAgente
	 * @param nombreAgente
	 * @param usuario
	 * @return
	 */
	//CotizaUrlResponse ObtenerURLCotizadorVida2(String numeroDocumento, String idOportunidadCrm, String idAgenteCrm,
	//		String codigoAgente, String nombreAgente, String usuario);

	/**
	 * 
	 * @param idCotizacion
	 * @return
	 */
	public CotizaUrlResponse ObtenerURLRecotizarVida(String nroDocumento,String nroCotizacion, String idUsuario);

	/**
	 * 
	 * @param destinatario
	 * @param adjuntos
	 * @return
	 */
	public BaseResponseDTO enviarCorreo(String destinatario, List<CotizacionPDFBean> adjuntos, String asegurado,
			String agenteNombre, String agenteCorreo);

	public BaseResponseDTO obtenerURLVidaFree(TokenRequestDTO tokenRequestDTO);

	public CotizadorCorrelativoResponseDTO generarCorrelativo(String tipoDocumento, String numeroDocumento);

	public CotizadorCumuloResponseDTO obtenerCumulo(String tipoDocumento, String numeroDocumento);


	public TokenResponseDTO decryptToken(String token);
	public TokenResponseDTO validateToken(String token);


	public CotizacionCrmResponseDTO guardarCotizacionCrm(CotizacionCrmRequestDTO cotizacionCrmRequestDTO);

	public ByteArrayOutputStream generarPDFCotizacionVida(String nroCotizacion, String agente, String asegurado);

	public ByteArrayOutputStream generarPDFCotizacionVidaFree(String nroCotizacion);
	public GenerarPdfResponse generarPDFEdn(String nroCotizacion);

	//public BaseResponseDTO finalizarCotizacion(CotizacionCrmOportunidadRequestDTO request);

	public ConversionResponseDTO obtenerTipoCambio();

	public ClonarCVFResponseDTO clonarCotizacionVidaFree(String nroCotizacion);

	public ProcesoResponseDTO forzarRecotizacion(List<String> cotizaciones);

	public BaseResponseDTO buscarEnArchivo(String dato, String archivo);

	public BaseResponseDTO validarNoObservado(String numeroCotizacion);

	public TipoCambioResponseDTO obtenerTipoCambioAcsele();
	
	/*public TransmisionAcseleResponseDTO transmitirCotizacion(TransmisionAcseleRequestDTO request);*/

	public CotizacionCrmResponseDTO guardaCotizacionAdn(CotizacionCrmRequestDTO cotizacionCrmRequestDTO);
	public void saveCumuloExigenciasMedicas(String nroCotizacion);

//	public BaseResponseDTO transmitirCotizacion(TransmisionAcseleRequestDTO request);
}
