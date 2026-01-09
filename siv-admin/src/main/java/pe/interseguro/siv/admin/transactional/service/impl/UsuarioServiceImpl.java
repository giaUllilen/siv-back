package pe.interseguro.siv.admin.transactional.service.impl;

import static io.jsonwebtoken.CompressionCodecs.DEFLATE;
import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static java.lang.System.currentTimeMillis;
import static java.util.UUID.randomUUID;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.google.gson.Gson;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NoArgsConstructor;
import pe.interseguro.siv.admin.transactional.service.UsuarioService;
import pe.interseguro.siv.common.dto.request.UsuarioIngresoRequestDTO;
import pe.interseguro.siv.common.dto.response.CotizaListaItemResponseDTO;
import pe.interseguro.siv.common.dto.response.UsuarioIngresoResponseDTO;
import pe.interseguro.siv.common.enums.PerfilEnum;
import pe.interseguro.siv.common.exception.ErrorResourceDTO;
import pe.interseguro.siv.common.exception.SivSOAException;
import pe.interseguro.siv.common.persistence.db.mysql.domain.EventLog;
import pe.interseguro.siv.common.persistence.db.mysql.domain.SolicitudAgente;
import pe.interseguro.siv.common.persistence.db.mysql.repository.EventLogRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.SolicitudRepository;
import pe.interseguro.siv.common.persistence.db.mysql.repository.UsuarioPerfilRepository;
import pe.interseguro.siv.common.persistence.db.mysql.response.Perfil;
import pe.interseguro.siv.common.persistence.rest.crm.CrmRestClient;
import pe.interseguro.siv.common.persistence.rest.crm.request.ValidarAccesoRequest;
import pe.interseguro.siv.common.persistence.rest.crm.response.ValidarAccesoResponse;
import pe.interseguro.siv.common.persistence.rest.global.GlobalRestClient;
import pe.interseguro.siv.common.persistence.rest.global.request.ObtenerAgenteRequest;
import pe.interseguro.siv.common.persistence.rest.global.response.ObtenerAgenteResponse;
import pe.interseguro.siv.common.persistence.rest.interseguro.InterseguroRestClient;
import pe.interseguro.siv.common.persistence.rest.interseguro.request.ObtenerDatosUsuarioRequest;
import pe.interseguro.siv.common.persistence.rest.interseguro.response.ObtenerDatosUsuarioResponse;
import pe.interseguro.siv.common.util.Constantes;
import pe.interseguro.siv.common.util.Utilitarios;


@Service("usuarioService")
@Transactional
public class UsuarioServiceImpl implements UsuarioService {
	
	@NoArgsConstructor
	static class PerfilDto extends pe.interseguro.siv.common.dto.response.Perfil {

		public PerfilDto(int i, String code, String value) {
			super (i, code, value);
		}
    }
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private InterseguroRestClient interseguroRestClient;

	@Autowired
	private CrmRestClient crmRestClient;
	
	@Autowired
	private GlobalRestClient globalRestClient;
	
	@Autowired
	private UsuarioPerfilRepository usuarioPerfilRepository;
	
	/*@Autowired
	private EventLogRepository logRepository;*/
	
    @Value("#{ environment['persistence.rest.client.interseguro.azman.obtenerDatosUsuario.aplicacion'] }")
	private String azmanObtenerDatosUsuario_aplicacion; //portalsistema

	private Gson gson = new Gson();
	
	@Override
	public UsuarioIngresoResponseDTO validarUsuario(UsuarioIngresoRequestDTO usuarioIngresoRequestDTO) {
		LOGGER.info("Entro UsuarioServiceImpl#validarUsuario(usuarioIngresoRequestDTO)");

		UsuarioIngresoResponseDTO response = new UsuarioIngresoResponseDTO();
		//ValidarAccesoRequest requestCRM = new ValidarAccesoRequest();
		ObtenerAgenteRequest obtenerAgenteRequest = new ObtenerAgenteRequest();
		String usuario = usuarioIngresoRequestDTO.getUsuario().toLowerCase();
		
		System.out.println(azmanObtenerDatosUsuario_aplicacion);
		//-- Logica - Validar AD (Azman)
		ObtenerDatosUsuarioRequest requestAzman = new ObtenerDatosUsuarioRequest();
		requestAzman.setAplicacion(azmanObtenerDatosUsuario_aplicacion);
		requestAzman.setDominio("DINTERSEGURO");
		requestAzman.setUsuario(usuario);
		requestAzman.setContrasena(usuarioIngresoRequestDTO.getPassword());			
		LOGGER.debug("inicio.responseAzman=>"+requestAzman);
		ObtenerDatosUsuarioResponse responseAzman = null;
		responseAzman = interseguroRestClient.obtenerDatosUsuario(requestAzman);
		
		
		LOGGER.info("fin.responseAzman=>"+gson.toJson(responseAzman));
		
		if (responseAzman.getStatusHttp() != "200") {
			LOGGER.info("Error => " + responseAzman.getStatusHttp());
			
			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			
			if (responseAzman.getStatusHttp().charAt(0) == '5') {
				response.setMensajeRespuesta("[" + responseAzman.getCode() + "] No se pudo encontrar usuario en Azman.");
			} else {
				response.setMensajeRespuesta("[" + responseAzman.getCode() + "] Error de comunicación de servidores. Favor contacte a Mesa de Ayuda.");
			}
			response.setObjErrorResource(new ErrorResourceDTO(responseAzman.getCode(), responseAzman.getMessage(), responseAzman.getTitle()));
		} else {
			
			LOGGER.info("Ingrso " + responseAzman.getRolAzman());
			
			if (responseAzman.getRolAzman().equals("Perfil_Agente_Comercial_vida") ) {
				String nombreUsuario = responseAzman.getNombreCompleto();
				if(!Utilitarios.valorString(responseAzman.getNombres()).equals(Strings.EMPTY) && !Utilitarios.valorString(responseAzman.getApellidos()).equals(Strings.EMPTY)) {
					StringBuilder sb = new StringBuilder();
					sb.append(responseAzman.getNombres()).append(Constantes.ESPACIO_BLANCO);
					sb.append(responseAzman.getApellidos());
					nombreUsuario = sb.toString();
				}
				
				Perfil perfil = usuarioPerfilRepository.findByUsuario(usuario);
				LOGGER.info("Perfil=>"+perfil);
				
				if( perfil != null && perfil.getCodigo() != PerfilEnum.PERFIL_AGENTE.getCodigo()) {
		
					String token = this.generateToken(usuario);
					
					response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
					response.setMensajeRespuesta(
						Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_EXITO)
					);
					response.setJwtToken(token);
					response.setIdUsuario(usuario);
					response.setNombreUsuario(nombreUsuario);
					response.setCorreoUsuario(responseAzman.getCorreo());
					
					PerfilDto perfilDto = new PerfilDto();
					try {
						BeanUtils.copyProperties(perfilDto, perfil);
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					response.setPerfil(perfilDto);
					
					LOGGER.info("No es agente");
					LOGGER.info("Salio UsuarioIngresoResponseDTO#validarUsuario(usuarioIngresoRequestDTO)");
					return response; 
				}
				obtenerAgenteRequest.setCodAgencia(usuarioIngresoRequestDTO.getUsuario().toLowerCase());
				
				try {
					System.out.println(obtenerAgenteRequest);
					ObtenerAgenteResponse responseGlobal = globalRestClient.obtenerAgente(obtenerAgenteRequest);
					//-- Logica - Validación Final
					if (responseAzman!= null && responseGlobal != null){
						
						//-- Generar Token JWT
						String token = this.generateToken(usuario);
						
						//-- Respuesta
						response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
						response.setMensajeRespuesta(
							Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_EXITO)
						);
						response.setJwtToken(token);
						response.setIdUsuario(usuario);
						response.setNombreUsuario(nombreUsuario);
						
						response.setIdUsuarioCRM(responseGlobal.getCOD_USERNAME_AGENTE());
						response.setCodigoVendedorCRM(responseGlobal.getCOD_AGENTE());
						response.setCodigoAgenciaCRM(responseGlobal.getCOD_AGENCIA());
						response.setCorreoUsuario(responseGlobal.getGLS_CORREO_AGENTE().toLowerCase());

						response.setEsAgenteSolicitud(true);
						response.setPerfil(new PerfilDto(0, PerfilEnum.PERFIL_AGENTE.getCodigo(), PerfilEnum.PERFIL_AGENTE.getPerfil()));
								
					
					}	
				} catch (Exception e) {
					LOGGER.info("Hubo uneror al obtener agente ");
					response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
					response.setMensajeRespuesta(
							Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_OBTENER_AGENTE)
						);
				}	
				
			}else {
				
				LOGGER.info("Rol no tiene acceso aL ADN = > " + responseAzman.getRolAzman());
				
				response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
				response.setMensajeRespuesta(
						Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_OBTENER_AGENTE));
			}
			
			
		}						
		List<String> observaciones = new ArrayList<String>();
		/*logRepository.save(new EventLog("Login", response.getCodigoRespuesta(),
				usuarioIngresoRequestDTO.getPath(), response.getMensajeRespuesta(), 
				gson.toJson(response.getObjErrorResource()), gson.toJson(observaciones), usuarioIngresoRequestDTO.getUsuario(),
				usuarioIngresoRequestDTO.getDevice(), usuarioIngresoRequestDTO.getOs()));*/
		LOGGER.info("Salio UsuarioIngresoResponseDTO#validarUsuario(usuarioIngresoRequestDTO)");		
		return response;
	}

	@Override
	public UsuarioIngresoResponseDTO getRefreshToken(Claims currentClaims) {
		LOGGER.info("Entro UsuarioIngresoResponseDTO#getRefreshToken(currentClaims)");		
		
		//-- Generar Token
		String token = Jwts.builder()
			.setHeaderParam(JwsHeader.TYPE, JwsHeader.JWT_TYPE)
			.setIssuer(Constantes.JWT_ISSUER)
			.setSubject(currentClaims.getSubject())
			.setAudience(Constantes.JWT_AUDIENCE)
			.setExpiration(new Date(System.currentTimeMillis()+Constantes.JWT_EXPIRATION_TIME))
			.setIssuedAt(new Date())
			.setId(UUID.randomUUID().toString())
			.compressWith(CompressionCodecs.DEFLATE)
			.signWith(SignatureAlgorithm.HS256, Constantes.JWT_SECRET_KEY)
			.compact();
		
		//-- Respuesta
		UsuarioIngresoResponseDTO usuarioIngresoResponseDTO = new UsuarioIngresoResponseDTO();
		usuarioIngresoResponseDTO.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
		usuarioIngresoResponseDTO.setMensajeRespuesta(Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_EXITO));
		usuarioIngresoResponseDTO.setJwtToken(token);
		
		
		LOGGER.info("Salio UsuarioIngresoResponseDTO#getRefreshToken(currentClaims)");		
		return usuarioIngresoResponseDTO;
	}
	
	private String generateToken(String usuario) {
		//-- Generar Token JWT
		return Jwts.builder()
			.setHeaderParam(JwsHeader.TYPE, JwsHeader.JWT_TYPE)
			
			.setIssuer(Constantes.JWT_ISSUER) // -- empresa
			.setSubject(usuario) // -- pertenece
			.setAudience(Constantes.JWT_AUDIENCE) // -- audiencia (web, mobile)
			.setExpiration(new Date(currentTimeMillis() + Constantes.JWT_EXPIRATION_TIME)) // -- fecha expiracion
			.setIssuedAt(new Date()) // -- fecha creacion
			.setId(randomUUID().toString()) // -- id propio jwt
			
			.compressWith(DEFLATE) // -- comprime el token para que pueda usarse en GETs
			.signWith(HS256, Constantes.JWT_SECRET_KEY)
			.compact();
	}
	
	@Override
	public UsuarioIngresoResponseDTO validarUsuarioAzman(UsuarioIngresoRequestDTO usuarioIngresoRequestDTO) {
		LOGGER.info("Entro UsuarioServiceImpl#validarUsuario(usuarioIngresoRequestDTO)");

		UsuarioIngresoResponseDTO response = new UsuarioIngresoResponseDTO();
		ValidarAccesoRequest requestCRM = new ValidarAccesoRequest();
		String usuario = usuarioIngresoRequestDTO.getUsuario();
		
		try {
			
			//-- Logica - Validar AD (Azman)
			ObtenerDatosUsuarioRequest requestAzman = new ObtenerDatosUsuarioRequest();
			requestAzman.setAplicacion("SEGURIDAD");
			requestAzman.setDominio("DINTERSEGURO");
			requestAzman.setUsuario(usuario);
			requestAzman.setContrasena(usuarioIngresoRequestDTO.getPassword());			
			LOGGER.debug("inicio.responseAzman=>"+requestAzman);
			
			ObtenerDatosUsuarioResponse responseAzman = null;
			try {
				responseAzman = interseguroRestClient.obtenerDatosUsuario(requestAzman);
				
				LOGGER.info("fin.responseAzman=>"+gson.toJson(responseAzman));
				
				if (responseAzman == null) {
					response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
					response.setMensajeRespuesta("Usuario no encontrado en Azman.");
				} else {
					response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
					String nombreUsuario = responseAzman.getNombreCompleto();
					if(!Utilitarios.valorString(responseAzman.getNombres()).equals(Strings.EMPTY) && !Utilitarios.valorString(responseAzman.getApellidos()).equals(Strings.EMPTY)) {
						StringBuilder sb = new StringBuilder();
						sb.append(responseAzman.getNombres()).append(Constantes.ESPACIO_BLANCO);
						sb.append(responseAzman.getApellidos());
						nombreUsuario = sb.toString();
					}

				}

			} catch (Exception e ) {
				response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
				response.setMensajeRespuesta("Usuario no encontrado en Azman.");
			}
			
						
		} catch(SivSOAException e) {
			LOGGER.error("Error SivSOAException=> " + e.getMessage());
			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			response.setMensajeRespuesta(e.getMessage());
			
		} catch(Exception e) {
			LOGGER.error("Error Exception=> " + e.getMessage());
			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			response.setMensajeRespuesta(e.getMessage());
			
		}
		
		LOGGER.info("Salio UsuarioIngresoResponseDTO#validarUsuario(usuarioIngresoRequestDTO)");		
		return response;
	}

	@Override
	public UsuarioIngresoResponseDTO obtenerPerfilUsuario(String usuario) {
		// TODO Auto-generated method stub
		UsuarioIngresoResponseDTO response = new UsuarioIngresoResponseDTO();
		ObtenerAgenteRequest obtenerAgenteRequest = new ObtenerAgenteRequest();
		
		Perfil perfil = usuarioPerfilRepository.findByUsuario(usuario);
		
		if(perfil != null && perfil.getCodigo() != PerfilEnum.PERFIL_AGENTE.getCodigo()) {
			
			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
			response.setMensajeRespuesta(
				Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_EXITO)
			);
			response.setJwtToken("");
			response.setIdUsuario(usuario);
			response.setNombreUsuario("");
			response.setCorreoUsuario("");
			
			PerfilDto perfilDto = new PerfilDto();
			try {
				BeanUtils.copyProperties(perfilDto, perfil);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			response.setPerfil(perfilDto);
			
			LOGGER.info("No es agente");
			LOGGER.info("Salio UsuarioIngresoResponseDTO#validarUsuario(usuarioIngresoRequestDTO)");
			return response; 
		}
		
		obtenerAgenteRequest.setCodAgencia(usuario.toLowerCase());
		
		try {
			System.out.println(obtenerAgenteRequest);
			ObtenerAgenteResponse responseGlobal = globalRestClient.obtenerAgente(obtenerAgenteRequest);
			//-- Logica - Validación Final
			if (responseGlobal != null){
				
				//-- Respuesta
				response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
				response.setMensajeRespuesta(
					Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESPUESTA_GENERAL_EXITO)
				);
				response.setJwtToken("");
				response.setIdUsuario(usuario);
				response.setNombreUsuario("");
				
				response.setIdUsuarioCRM(responseGlobal.getCOD_USERNAME_AGENTE());
				response.setCodigoVendedorCRM(responseGlobal.getCOD_AGENTE());
				response.setCodigoAgenciaCRM(responseGlobal.getCOD_AGENCIA());
				response.setCorreoUsuario(responseGlobal.getGLS_CORREO_AGENTE().toLowerCase());

				response.setEsAgenteSolicitud(true);
				response.setPerfil(new PerfilDto(0, PerfilEnum.PERFIL_AGENTE.getCodigo(), PerfilEnum.PERFIL_AGENTE.getPerfil()));
						
			
			}	
		} catch (Exception e) {
			LOGGER.info("Hubo uneror al obtener agente ");
			response.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			response.setMensajeRespuesta(
					Utilitarios.obtenerMensaje(messageSource, new Object[]{usuario}, Constantes.MENSAJE_RESPUESTA_GENERAL_ERR0R)
				);
		}
		
		return response;
	}
	
}
