package pe.interseguro.siv.admin.config.filter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;

import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import pe.interseguro.siv.common.util.Constantes;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para JwtFilter
 * 
 * Principios aplicados:
 * - Aislamiento: No depende de servicios externos
 * - Rapidez: Todas las pruebas se ejecutan en milisegundos
 * - Determinismo: Siempre producen los mismos resultados
 * - Independencia: Cada prueba es autónoma
 * 
 * IMPORTANTE: Estas pruebas validan la seguridad de la aplicación
 */
@RunWith(MockitoJUnitRunner.class)
public class JwtFilterTest {

    private JwtFilter jwtFilter;

    @Mock
    private AuthenticationManager authenticationManager;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockFilterChain filterChain;

    @Before
    public void setUp() {
        jwtFilter = new JwtFilter(authenticationManager);
        jwtFilter.inicializarConfiguracion("X-API-KEY", "test-api-key-value");
        
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = new MockFilterChain();
        
        // Limpiar contexto de seguridad
        SecurityContextHolder.clearContext();
    }

    /**
     * Test: Token válido debe permitir acceso
     */
    @Test
    public void doFilterInternal_DebePermitirAcceso_CuandoTokenEsValido() throws ServletException, IOException {
        // Given
        String tokenValido = generarTokenValido("usuario.test");
        request.addHeader(Constantes.JWT_RESPONSE_HEADER_KEY, 
            Constantes.JWT_RESPONSE_HEADER_PREFIX + tokenValido);
        request.setRequestURI("/api/v1/solicitudes");

        // When
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Then
        assertNotNull("El contexto de seguridad debe estar configurado", 
            SecurityContextHolder.getContext().getAuthentication());
        assertEquals("El usuario debe estar autenticado", 
            "usuario.test", 
            SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    /**
     * Test: Token inválido debe denegar acceso
     */
    @Test
    public void doFilterInternal_DebeDenegarAcceso_CuandoTokenEsInvalido() throws ServletException, IOException {
        // Given
        String tokenInvalido = "token.invalido.aqui";
        request.addHeader(Constantes.JWT_RESPONSE_HEADER_KEY, 
            Constantes.JWT_RESPONSE_HEADER_PREFIX + tokenInvalido);
        request.setRequestURI("/api/v1/solicitudes");

        // When
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Then
        assertNull("El contexto de seguridad no debe estar configurado", 
            SecurityContextHolder.getContext().getAuthentication());
        assertTrue("La respuesta debe contener mensaje de error", 
            response.getContentAsString().contains("Token - Expirado o Modificado"));
    }

    /**
     * Test: Sin token debe denegar acceso
     */
    @Test
    public void doFilterInternal_DebeDenegarAcceso_CuandoNoHayToken() throws ServletException, IOException {
        // Given
        request.setRequestURI("/api/v1/solicitudes");
        // No se agrega header de token

        // When
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Then
        assertNull("El contexto de seguridad no debe estar configurado", 
            SecurityContextHolder.getContext().getAuthentication());
        assertTrue("La respuesta debe contener mensaje de error", 
            response.getContentAsString().contains("Token - Authorization header invalido"));
    }

    /**
     * Test: Token expirado debe denegar acceso
     */
    @Test
    public void doFilterInternal_DebeDenegarAcceso_CuandoTokenEstaExpirado() throws ServletException, IOException {
        // Given
        String tokenExpirado = generarTokenExpirado("usuario.test");
        request.addHeader(Constantes.JWT_RESPONSE_HEADER_KEY, 
            Constantes.JWT_RESPONSE_HEADER_PREFIX + tokenExpirado);
        request.setRequestURI("/api/v1/solicitudes");

        // When
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Then
        assertNull("El contexto de seguridad no debe estar configurado", 
            SecurityContextHolder.getContext().getAuthentication());
        assertTrue("La respuesta debe contener mensaje de error", 
            response.getContentAsString().contains("Token - Expirado o Modificado"));
    }

    /**
     * Test: URL de excepción debe permitir acceso sin token (Swagger)
     */
    @Test
    public void doFilterInternal_DebePermitirAcceso_CuandoEsURLExcepcionSwagger() throws ServletException, IOException {
        // Given
        request.setRequestURI("/swagger-ui.html");
        // No se agrega token

        // When
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Then
        assertNotNull("El contexto de seguridad debe estar configurado", 
            SecurityContextHolder.getContext().getAuthentication());
        assertEquals("Debe usar usuario bypass", 
            Constantes.LOGIN_BYPASS_USER, 
            SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    /**
     * Test: URL de excepción debe permitir acceso sin token (Liveness)
     */
    @Test
    public void doFilterInternal_DebePermitirAcceso_CuandoEsURLExcepcionLiveness() throws ServletException, IOException {
        // Given
        request.setRequestURI("/liveness");
        // No se agrega token

        // When
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Then
        assertNotNull("El contexto de seguridad debe estar configurado", 
            SecurityContextHolder.getContext().getAuthentication());
    }

    /**
     * Test: URL de webhook Indenova debe permitir acceso sin token
     */
    @Test
    public void doFilterInternal_DebePermitirAcceso_CuandoEsWebhookIndenova() throws ServletException, IOException {
        // Given
        request.setRequestURI(Constantes.URL_EXCEPTION_INDENOVA_WEBHOOK_NOTIFICACION + "/notificacion");
        // No se agrega token

        // When
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Then
        assertNotNull("El contexto de seguridad debe estar configurado", 
            SecurityContextHolder.getContext().getAuthentication());
    }

    /**
     * Test: API Key válido debe permitir acceso sin token
     */
    @Test
    public void doFilterInternal_DebePermitirAcceso_CuandoAPIKeyEsValido() throws ServletException, IOException {
        // Given
        request.addHeader("X-API-KEY", "test-api-key-value");
        request.setRequestURI("/api/v1/solicitudes");
        // No se agrega token JWT

        // When
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Then
        assertNotNull("El contexto de seguridad debe estar configurado", 
            SecurityContextHolder.getContext().getAuthentication());
    }

    /**
     * Test: API Key inválido debe requerir token
     */
    @Test
    public void doFilterInternal_DebeRequerirToken_CuandoAPIKeyEsInvalido() throws ServletException, IOException {
        // Given
        request.addHeader("X-API-KEY", "api-key-invalido");
        request.setRequestURI("/api/v1/solicitudes");
        // No se agrega token JWT

        // When
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Then
        assertNull("El contexto de seguridad no debe estar configurado", 
            SecurityContextHolder.getContext().getAuthentication());
        assertTrue("La respuesta debe contener mensaje de error", 
            response.getContentAsString().contains("Token - Authorization header invalido"));
    }

    /**
     * Test: Token con formato incorrecto debe denegar acceso
     */
    @Test
    public void doFilterInternal_DebeDenegarAcceso_CuandoFormatoTokenEsIncorrecto() throws ServletException, IOException {
        // Given
        request.addHeader(Constantes.JWT_RESPONSE_HEADER_KEY, "TokenSinPrefijo");
        request.setRequestURI("/api/v1/solicitudes");

        // When
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Then
        assertNull("El contexto de seguridad no debe estar configurado", 
            SecurityContextHolder.getContext().getAuthentication());
        assertTrue("La respuesta debe contener mensaje de error", 
            response.getContentAsString().contains("Token - Authorization header invalido"));
    }

    /**
     * Test: URL de refresh token debe incluir claims en request
     */
    @Test
    public void doFilterInternal_DebeIncluirClaims_CuandoEsRefreshTokenURL() throws ServletException, IOException {
        // Given
        String tokenValido = generarTokenValido("usuario.test");
        request.addHeader(Constantes.JWT_RESPONSE_HEADER_KEY, 
            Constantes.JWT_RESPONSE_HEADER_PREFIX + tokenValido);
        request.setRequestURI(Constantes.JWT_URL_REFRESH_TOKEN);

        // When
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Then
        assertNotNull("Los claims deben estar en el request", 
            request.getAttribute("claims"));
    }

    // ========== Métodos auxiliares ==========

    /**
     * Genera un token JWT válido para pruebas
     */
    private String generarTokenValido(String usuario) {
        return Jwts.builder()
            .setHeaderParam(JwsHeader.TYPE, JwsHeader.JWT_TYPE)
            .setIssuer(Constantes.JWT_ISSUER)
            .setSubject(usuario)
            .setAudience(Constantes.JWT_AUDIENCE)
            .setExpiration(new Date(System.currentTimeMillis() + Constantes.JWT_EXPIRATION_TIME))
            .setIssuedAt(new Date())
            .setId(UUID.randomUUID().toString())
            .compressWith(CompressionCodecs.DEFLATE)
            .signWith(SignatureAlgorithm.HS256, Constantes.JWT_SECRET_KEY)
            .compact();
    }

    /**
     * Genera un token JWT expirado para pruebas
     */
    private String generarTokenExpirado(String usuario) {
        return Jwts.builder()
            .setHeaderParam(JwsHeader.TYPE, JwsHeader.JWT_TYPE)
            .setIssuer(Constantes.JWT_ISSUER)
            .setSubject(usuario)
            .setAudience(Constantes.JWT_AUDIENCE)
            .setExpiration(new Date(System.currentTimeMillis() - 10000)) // Expirado hace 10 segundos
            .setIssuedAt(new Date(System.currentTimeMillis() - 20000))
            .setId(UUID.randomUUID().toString())
            .compressWith(CompressionCodecs.DEFLATE)
            .signWith(SignatureAlgorithm.HS256, Constantes.JWT_SECRET_KEY)
            .compact();
    }
}

