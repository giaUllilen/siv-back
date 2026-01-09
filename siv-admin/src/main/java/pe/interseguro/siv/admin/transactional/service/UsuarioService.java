package pe.interseguro.siv.admin.transactional.service;

import io.jsonwebtoken.Claims;
import pe.interseguro.siv.common.dto.request.UsuarioIngresoRequestDTO;
import pe.interseguro.siv.common.dto.response.UsuarioIngresoResponseDTO;

public interface UsuarioService {
	
	/**
	 * Validar Usuario en el AD + CRM 
	 * 
	 * @param usuarioIngresoRequestDTO
	 * @return
	 */
	public UsuarioIngresoResponseDTO validarUsuario(UsuarioIngresoRequestDTO usuarioIngresoRequestDTO);

	
	/**
	 * Resetear el tiempo del Token JWT actual 
	 * 
	 * @param currentClaims
	 * @return
	 */
	public UsuarioIngresoResponseDTO getRefreshToken(Claims currentClaims);
	
	public UsuarioIngresoResponseDTO validarUsuarioAzman(UsuarioIngresoRequestDTO usuarioIngresoRequestDTO);
	
	public UsuarioIngresoResponseDTO obtenerPerfilUsuario(String usuario);
}
