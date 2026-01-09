package pe.interseguro.siv.admin.view.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.serviceloader.ServiceFactoryBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import io.jsonwebtoken.Claims;
import pe.interseguro.siv.common.dto.request.UsuarioIngresoRequestDTO;
import pe.interseguro.siv.common.dto.response.CRMReporteDetalleRrvvResponseDTO;
import pe.interseguro.siv.common.dto.response.UsuarioIngresoResponseDTO;
import pe.interseguro.siv.common.util.Constantes;

/**
 * RestFull del USUARIO
 * 
 * @author ti-is
 */
@RestController
@RequestMapping(value = {"/api/v1/usuarios"})
public class UsuarioController extends BaseController{
	
	/**
	 * Actualizar el token de sesión del usuario
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = Constantes.JWT_URL_REFRESH_TOKEN, method = RequestMethod.GET)
	@ResponseBody
	public UsuarioIngresoResponseDTO getRefreshToken(HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info("Entro a LoginController#getRefreshToken(request, response)");
		
		UsuarioIngresoResponseDTO respuesta = serviceFactory.getUsuarioService().getRefreshToken( (Claims)request.getAttribute("claims") );
		
		LOGGER.info("Salio a LoginController#getRefreshToken(request, response)");
		return respuesta;
	}
	
	@RequestMapping(value = "/valida-ad", method = RequestMethod.POST)
	public UsuarioIngresoResponseDTO validaAzman(@RequestBody UsuarioIngresoRequestDTO request) {
		LOGGER.info("Entro a LoginController#validaAzman()");
		
		UsuarioIngresoResponseDTO respuesta = serviceFactory.getUsuarioService().validarUsuarioAzman(request);
		
		LOGGER.info("Salio a LoginController#validaAzman()");
		return respuesta;
	}
	
	@RequestMapping(value = "/obtenerPerfil", method = RequestMethod.GET)
	public UsuarioIngresoResponseDTO obtenerPerfilUsuario (HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info("Entro a LoginController#obtenerPerfilUsuario()");
		
		String usuario = request.getParameter("usuario");
		UsuarioIngresoResponseDTO respuesta = serviceFactory.getUsuarioService().obtenerPerfilUsuario(usuario);
		
		LOGGER.info("Salio a LoginController#obtenerPerfilUsuario()");
		return respuesta;
	}
	
	
}
