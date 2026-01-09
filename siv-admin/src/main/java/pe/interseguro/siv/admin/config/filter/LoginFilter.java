/**
 * 
 */
package pe.interseguro.siv.admin.config.filter;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import pe.interseguro.siv.admin.transactional.factory.ServiceFactory;
import pe.interseguro.siv.common.dto.request.UsuarioIngresoRequestDTO;
import pe.interseguro.siv.common.dto.response.BaseResponseDTO;
import pe.interseguro.siv.common.dto.response.UsuarioIngresoResponseDTO;
import pe.interseguro.siv.common.exception.ErrorResourceDTO;
import pe.interseguro.siv.common.exception.SivTXException;
import pe.interseguro.siv.common.util.Constantes;

/**
 * @author digital-is
 *
 */
public class LoginFilter extends AbstractAuthenticationProcessingFilter {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	
	/**
	 * Constructor
	 * @param url. path login
	 * @param authManager. Manager of Web Security
	 */
	public LoginFilter(String url, AuthenticationManager authManager) {
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authManager);
    }
	
	/* (non-Javadoc)
	 * @see org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter#attemptAuthentication(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		LOGGER.info("Entro a LoginFilter#attemptAuthentication(request, response)");
		
		Authentication authentication = getAuthenticationManager().authenticate(
			new UsernamePasswordAuthenticationToken(
				Constantes.LOGIN_BYPASS_USER,
				Constantes.LOGIN_BYPASS_PASSWORD,
				Collections.emptyList()
			)
		);
		
		LOGGER.info("Salio a LoginFilter#attemptAuthentication(request, response)");
		return authentication;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter#successfulAuthentication(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain, org.springframework.security.core.Authentication)
	 */
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain,
			Authentication authResult) throws IOException, ServletException {
		LOGGER.info("Entro a LoginFilter#successfulAuthentication(request, response, filterChain, authResult)");		
		
		//-- Variables
		WebApplicationContext applicationContext = WebApplicationContextUtils.
			    getRequiredWebApplicationContext(request.getServletContext());		
		ServiceFactory serviceFactory = (ServiceFactory) applicationContext.getBean("serviceFactory");
		InputStream body = request.getInputStream();
		Gson gson = new Gson();
		
		//-- Peticiones
		UsuarioIngresoRequestDTO usuarioIngresoRequestDTO = new ObjectMapper().readValue(body, UsuarioIngresoRequestDTO.class);
//		System.out.println("usuarioIngresoRequestDTO = " + gson.toJson(usuarioIngresoRequestDTO) );
		
		//-- Respuestas
		try {
			UsuarioIngresoResponseDTO usuarioIngresoResponseDTO = serviceFactory.getUsuarioService().validarUsuario(usuarioIngresoRequestDTO);
//			System.out.println("usuarioIngresoResponseDTO = " + gson.toJson(usuarioIngresoResponseDTO) ); //-- pq muestra la clave del usuario
			
			this.getResponseJsonFormat(response, usuarioIngresoResponseDTO, gson);
			
		} catch(SivTXException e) {
						
	        BaseResponseDTO baseResponseDTO = new BaseResponseDTO();
	        baseResponseDTO.setObjErrorResource(new ErrorResourceDTO(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR, e.getMsjError()));
	        baseResponseDTO.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
	        baseResponseDTO.setMensajeRespuesta(e.getMsjError());
			System.out.println("baseResponseDTO = " + gson.toJson(baseResponseDTO) );
			
	        this.getResponseJsonFormat(response, baseResponseDTO, gson);

		}
		
		LOGGER.info("Salio a LoginFilter#successfulAuthentication(request, response, filterChain, authResult)");
	}
	
	/**
	 * Enviar respuesta al cliente en formato JSON
	 * @param response
	 * @param object
	 * @throws IOException
	 */
	private void getResponseJsonFormat(HttpServletResponse response, Object object, Gson gson) throws IOException {
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		
		PrintWriter out = response.getWriter();
		out.print(gson.toJson(object));
		out.flush();
		out.close();
	}

	
}
