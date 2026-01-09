/**
 *
 */
package pe.interseguro.siv.admin.config.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.google.gson.Gson;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import pe.interseguro.siv.common.dto.response.BaseResponseDTO;
import pe.interseguro.siv.common.exception.ErrorResourceDTO;
import pe.interseguro.siv.common.util.Constantes;

/**
 * @author digital-is
 *
 */
public class JwtFilter extends BasicAuthenticationFilter {
	

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    
    // Config header vis-api
    public String visApiKeyName = "";
    public String visApiKeyValue = "";

    /**
     * Constructor
     * @param authenticationManager
     */
    public JwtFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }
    
    public JwtFilter inicializarConfiguracion (String apiKeyName, String apiKeyValue) {
    	this.visApiKeyName = apiKeyName;
    	this.visApiKeyValue = apiKeyValue;
    	return this;
    }

    /* (non-Javadoc)
     * @see org.springframework.security.web.authentication.www.BasicAuthenticationFilter#doFilterInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        LOGGER.info("Entro a JwtFilter#doFilterInternal(request, response, filterChain)");

        String token = request.getHeader(Constantes.JWT_RESPONSE_HEADER_KEY);
        
        String apiKeyOrigen = request.getHeader(visApiKeyName);
        boolean esOrigenVisApi = visApiKeyValue.equals(apiKeyOrigen);
        
        System.out.println("### HEADER REFERER: " + apiKeyOrigen);
        System.out.println("### HOST ORIGEN: " + request.getRemoteHost());
        System.out.println("### IP ORIGEN: " + request.getRemoteAddr());
        System.out.println("### ES UNA SESSION VIS API: " + esOrigenVisApi);
        
        LOGGER.info("### HEADER REFERER: " + apiKeyOrigen);
        LOGGER.info("### HOST ORIGEN: " + request.getRemoteHost());
        LOGGER.info("### IP ORIGEN: " + request.getRemoteAddr());
        LOGGER.info("### ES UNA SESSION VIS API: " + esOrigenVisApi);
        
        
        if (esOrigenVisApi || request.getRequestURI().contains(Constantes.URL_EXCEPTION_INDENOVA_WEBHOOK_NOTIFICACION) || request.getRequestURI().contains(Constantes.URL_EXCEPTION_DESCARGAS)
                || request.getRequestURI().contains(Constantes.URL_EXCEPTION_TOKEN_VIDA_FREE) || request.getRequestURI().contains(Constantes.COMPARTIDO_URL)
                || request.getRequestURI().contains(Constantes.URL_EXCEPTION_PRINT_COTIZACION_VIDA_FREE) || request.getRequestURI().contains(Constantes.URL_EXCEPTION_CORRELATIVO_VIDA_FREE)
                || request.getRequestURI().contains(Constantes.URL_EXCEPTION_PRINT_ADN)
                || request.getRequestURI().contains(Constantes.URL_EXCEPTION_PRINT_LISTAR_ARCHIVOS)
                || request.getRequestURI().contains(Constantes.URL_EXCEPTION_CUMULO)
                || request.getRequestURI().contains(Constantes.URL_EXCEPTION_TIPO_CAMBIO)
                || request.getRequestURI().contains(Constantes.URL_EXCEPTION_CONVERSION_MONEDA)
                || request.getRequestURI().contains(Constantes.URL_EXCEPTION_PRINT_SOLICITUD_VIDA_FREE)
                || request.getRequestURI().contains(Constantes.URL_EXCEPTION_CLONAR_COTIZACION_VIDA_FREE)
                || request.getRequestURI().contains(Constantes.URL_EXCEPTION_PRINT_CREAR_SOLICITUD_ACP)
                || request.getRequestURI().contains(Constantes.URL_EXCEPTION_PRINT_COTIZACION_VIDA)
                || request.getRequestURI().contains(Constantes.URL_EXCEPTION_FORZAR_COTIZACION_VIDA_FREE)
                || request.getRequestURI().contains(Constantes.URL_EXCEPTION_SOLICITUD_CRM_MANUAL)
                || request.getRequestURI().contains(Constantes.URL_EXCEPTION_VALIDAR_COTIZACION_VIDA_FREE)
                || request.getRequestURI().contains(Constantes.URL_EXCEPTION_TEMP)
                || request.getRequestURI().contains(Constantes.URL_EXCEPTION_REGISTRO_AUDITORIA)
                || request.getRequestURI().contains(Constantes.URL_EXCEPTION_PAGO_WEB)
                || request.getRequestURI().contains(Constantes.URL_EXCEPTION_CRM_REPORTE_RRVV)
                || request.getRequestURI().contains(Constantes.URL_EXCEPTION_AZMAN_SERVICE)
                || request.getRequestURI().contains(Constantes.URL_EXCEPTION_LOGGER)
                || request.getRequestURI().contains(Constantes.URL_EXCEPTION_TRANSMISION_COTIZACION)
                || request.getRequestURI().contains(Constantes.URL_EXCEPTION_OBTENER_COTIZACION)
                || request.getRequestURI().contains(Constantes.URL_EXCEPTION_AFILIACION_TRASPASO)
                || request.getRequestURI().contains(Constantes.URL_EXCEPTION_LOG_BOTON)
                || request.getRequestURI().contains(Constantes.CONSENTIMIENTO_ACEPTADO_URL)
                || request.getRequestURI().contains(Constantes.CONSENTIMIENTO_RECOTIZAR_VALIDAR)
                || request.getRequestURI().contains(Constantes.URL_EMISION_AUTOMATICA)
                || request.getRequestURI().contains(Constantes.URL_POLICY_ISSUED)
                || request.getRequestURI().contains("/api-docs")
                || request.getRequestURI().contains("/swagger-ui/")
                || request.getRequestURI().contains("/swagger-ui.html")
                || request.getRequestURI().contains("v2/swagger-ui.html")
                || request.getRequestURI().contains("liveness")
                || request.getRequestURI().contains("validateToken")
                || request.getRequestURI().contains("/api/v1/solicitudes/registro-pago")
                || request.getRequestURI().contains("/api/v1/cotizaciones/get-link-pago")
                || request.getRequestURI().contains("/pdf/edn/")
                || request.getRequestURI().contains("/api/v1/solicitudes/validadoDocumentos")
                || request.getRequestURI().contains("/api/v1/solicitudes/acp-pdf/")
                || request.getRequestURI().contains("/getDataConsentimiento/")
                || request.getRequestURI().contains("obtenerURLVidaFree")
                || request.getRequestURI().contains("exmedicas")
                || request.getRequestURI().contains("obtenerArchivo")
                || request.getRequestURI().contains("requestEvaluator")
                || request.getRequestURI().contains("reglamentos")


        ) {
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(Constantes.LOGIN_BYPASS_USER, null, new ArrayList<>())
            );
            filterChain.doFilter(request, response);
            return;
        }

        if (token == null || !token.startsWith(Constantes.JWT_RESPONSE_HEADER_PREFIX)) {
            this.getCustomException(request, response, "Token - Authorization header invalido");
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request, response, token, request.getRequestURL().toString());
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } else {
            this.getCustomException(request, response, "Token - Invalido");
        }

        LOGGER.info("Salio a JwtFilter#doFilterInternal(request, response, filterChain)");
    }

    /**
     * Validar token
     * @param token
     * @return
     * @throws IOException
     */
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request, HttpServletResponse response, String token, String url) throws ServletException, IOException {
        if (token != null) {

            //-- Obtener el JWS
            Jws<Claims> jws = null;
            try {
                jws = Jwts.parser()
                        .setSigningKey(Constantes.JWT_SECRET_KEY)
                        .parseClaimsJws(token.replace(Constantes.JWT_RESPONSE_HEADER_PREFIX, "").trim());
                if (jws == null) {
                    return null;
                }
            } catch (Exception e) {
                this.getCustomException(request, response, "Token - Expirado o Modificado");
                return null;
            }

            System.out.println("tokenTiempoExpira = " + (jws.getBody().getExpiration().getTime() - new Date().getTime()));

            //-- Validar si existe el usuario
            String usuario = jws.getBody().getSubject();
            if (usuario != null) {
                if (url.contains(Constantes.JWT_URL_REFRESH_TOKEN)) { //-- enviar claims jwt para usarlo en el refreshToken
                    request.setAttribute("claims", jws.getBody());
                }
                return new UsernamePasswordAuthenticationToken(usuario, null, new ArrayList<>());
            }

            return null;
        }

        return null;
    }

    /**
     * Mensaje de exception
     * @param message
     * @throws IOException
     * @throws ServletException
     */
    private void getCustomException(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException {

        //-- opcion 1 (devolver un json_format)
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        BaseResponseDTO baseResponseDTO = new BaseResponseDTO();
        baseResponseDTO.setObjErrorResource(new ErrorResourceDTO(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR, message));
        baseResponseDTO.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
        baseResponseDTO.setMensajeRespuesta(message);
        System.out.println("baseResponseDTO = " + new Gson().toJson(baseResponseDTO));

        PrintWriter out = response.getWriter();
        out.print(new Gson().toJson(baseResponseDTO));
        out.flush();
        out.close();

    }


}
