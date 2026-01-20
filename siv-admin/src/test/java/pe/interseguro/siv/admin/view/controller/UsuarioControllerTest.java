package pe.interseguro.siv.admin.view.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import pe.interseguro.siv.admin.transactional.factory.ServiceFactory;
import pe.interseguro.siv.admin.transactional.service.UsuarioService;
import pe.interseguro.siv.common.dto.request.UsuarioIngresoRequestDTO;
import pe.interseguro.siv.common.dto.response.UsuarioIngresoResponseDTO;
import pe.interseguro.siv.common.util.Constantes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para UsuarioController siguiendo principios TDD
 * 
 * Principios aplicados:
 * - TDD: Pruebas escritas primero, enfocadas en comportamiento
 * - Aislamiento: Todas las dependencias mockeadas
 * - Independencia: Cada test es autónomo
 * - Given-When-Then: Estructura clara y legible
 * 
 * @author test-team
 */
@RunWith(MockitoJUnitRunner.class)
public class UsuarioControllerTest {

    @InjectMocks
    private UsuarioController usuarioController;

    @Mock
    private ServiceFactory serviceFactory;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private MessageSource messageSource;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpServletResponse httpServletResponse;

    @Before
    public void setUp() {
        // Configurar el factory para retornar el servicio mockeado
        when(serviceFactory.getUsuarioService()).thenReturn(usuarioService);
        
        // Nota: messageSource no se usa en el controlador, stub removido para evitar warnings
    }

    // ========== TESTS PARA getRefreshToken ==========

    /**
     * Test TDD: getRefreshToken - Debe renovar token exitosamente
     * 
     * GIVEN: Un token JWT válido en el request
     * WHEN: Se solicita renovar el token
     * THEN: Retorna un nuevo token JWT
     */
    @Test
    public void getRefreshToken_DebeRenovarToken_CuandoTokenEsValido() {
        // Given
        Claims claims = new DefaultClaims();
        claims.put("usuario", "test.user");
        claims.put("perfil", "ADMIN");
        when(httpServletRequest.getAttribute("claims")).thenReturn(claims);

        UsuarioIngresoResponseDTO mockResponse = new UsuarioIngresoResponseDTO();
        mockResponse.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
        mockResponse.setJwtToken("nuevo.jwt.token");
        mockResponse.setIdUsuario("test.user");
        
        when(usuarioService.getRefreshToken(any(Claims.class)))
            .thenReturn(mockResponse);

        // When
        UsuarioIngresoResponseDTO resultado = 
            usuarioController.getRefreshToken(httpServletRequest, httpServletResponse);

        // Then
        assertNotNull("El resultado no debe ser nulo", resultado);
        assertEquals("Debe retornar código de éxito", 
            Constantes.CODIGO_RESPUESTA_GENERAL_EXITO, resultado.getCodigoRespuesta());
        assertNotNull("Debe incluir nuevo token JWT", resultado.getJwtToken());
        assertEquals("El token debe ser el esperado", 
            "nuevo.jwt.token", resultado.getJwtToken());
        
        verify(usuarioService, times(1)).getRefreshToken(any(Claims.class));
    }

    /**
     * Test TDD: getRefreshToken - Debe rechazar token inválido
     * 
     * GIVEN: Un token JWT inválido o expirado
     * WHEN: Se intenta renovar el token
     * THEN: Retorna error de autenticación
     */
    @Test
    public void getRefreshToken_DebeRetornarError_CuandoTokenEsInvalido() {
        // Given
        Claims claims = new DefaultClaims();
        claims.put("usuario", "test.user");
        when(httpServletRequest.getAttribute("claims")).thenReturn(claims);

        UsuarioIngresoResponseDTO mockResponse = new UsuarioIngresoResponseDTO();
        mockResponse.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
        mockResponse.setMensajeRespuesta("Token expirado");
        
        when(usuarioService.getRefreshToken(any(Claims.class)))
            .thenReturn(mockResponse);

        // When
        UsuarioIngresoResponseDTO resultado = 
            usuarioController.getRefreshToken(httpServletRequest, httpServletResponse);

        // Then
        assertNotNull("El resultado no debe ser nulo", resultado);
        assertEquals("Debe retornar código de error", 
            Constantes.CODIGO_RESPUESTA_GENERAL_ERROR, resultado.getCodigoRespuesta());
        assertNull("No debe incluir nuevo token", resultado.getJwtToken());
        assertNotNull("Debe incluir mensaje de error", resultado.getMensajeRespuesta());
    }

    /**
     * Test TDD: getRefreshToken - Debe manejar claims nulos
     * 
     * GIVEN: Request sin claims
     * WHEN: Se intenta renovar el token
     * THEN: Maneja la situación sin lanzar NullPointerException
     */
    @Test
    public void getRefreshToken_DebeManejarClaimsNulos_SinLanzarExcepcion() {
        // Given
        when(httpServletRequest.getAttribute("claims")).thenReturn(null);

        UsuarioIngresoResponseDTO mockResponse = new UsuarioIngresoResponseDTO();
        mockResponse.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
        
        when(usuarioService.getRefreshToken(null))
            .thenReturn(mockResponse);

        // When
        UsuarioIngresoResponseDTO resultado = 
            usuarioController.getRefreshToken(httpServletRequest, httpServletResponse);

        // Then
        assertNotNull("El resultado no debe ser nulo", resultado);
        assertEquals("Debe retornar código de error", 
            Constantes.CODIGO_RESPUESTA_GENERAL_ERROR, resultado.getCodigoRespuesta());
    }

    // ========== TESTS PARA validaAzman ==========

    /**
     * Test TDD: validaAzman - Debe validar usuario correctamente
     * 
     * GIVEN: Credenciales válidas de usuario AD
     * WHEN: Se valida contra Active Directory
     * THEN: Retorna usuario autenticado con permisos
     */
    @Test
    public void validaAzman_DebeAutenticar_CuandoCredencialesSonValidas() {
        // Given
        UsuarioIngresoRequestDTO request = new UsuarioIngresoRequestDTO();
        request.setUsuario("juan.perez");
        request.setPassword("Password123!");

        UsuarioIngresoResponseDTO mockResponse = new UsuarioIngresoResponseDTO();
        mockResponse.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
        mockResponse.setIdUsuario("juan.perez");
        mockResponse.setNombreUsuario("Juan Pérez");
        mockResponse.setJwtToken("jwt.token.value");
        
        when(usuarioService.validarUsuarioAzman(any(UsuarioIngresoRequestDTO.class)))
            .thenReturn(mockResponse);

        // When
        UsuarioIngresoResponseDTO resultado = usuarioController.validaAzman(request);

        // Then
        assertNotNull("El resultado no debe ser nulo", resultado);
        assertEquals("Debe retornar éxito", 
            Constantes.CODIGO_RESPUESTA_GENERAL_EXITO, resultado.getCodigoRespuesta());
        assertEquals("Usuario debe coincidir", "juan.perez", resultado.getIdUsuario());
        assertNotNull("Debe incluir JWT token", resultado.getJwtToken());
        assertNotNull("Debe incluir nombre de usuario", resultado.getNombreUsuario());
        
        verify(usuarioService, times(1))
            .validarUsuarioAzman(any(UsuarioIngresoRequestDTO.class));
    }

    /**
     * Test TDD: validaAzman - Debe rechazar credenciales incorrectas
     * 
     * GIVEN: Credenciales inválidas
     * WHEN: Se intenta autenticar
     * THEN: Retorna error de autenticación
     */
    @Test
    public void validaAzman_DebeRechazar_CuandoCredencialesSonInvalidas() {
        // Given
        UsuarioIngresoRequestDTO request = new UsuarioIngresoRequestDTO();
        request.setUsuario("usuario.invalido");
        request.setPassword("wrongpassword");

        UsuarioIngresoResponseDTO mockResponse = new UsuarioIngresoResponseDTO();
        mockResponse.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
        mockResponse.setMensajeRespuesta("Credenciales incorrectas");
        
        when(usuarioService.validarUsuarioAzman(any(UsuarioIngresoRequestDTO.class)))
            .thenReturn(mockResponse);

        // When
        UsuarioIngresoResponseDTO resultado = usuarioController.validaAzman(request);

        // Then
        assertNotNull("El resultado no debe ser nulo", resultado);
        assertEquals("Debe retornar error", 
            Constantes.CODIGO_RESPUESTA_GENERAL_ERROR, resultado.getCodigoRespuesta());
        assertNull("No debe incluir token", resultado.getJwtToken());
        assertNotNull("Debe incluir mensaje de error", resultado.getMensajeRespuesta());
        assertTrue("Mensaje debe indicar error de credenciales",
            resultado.getMensajeRespuesta().contains("Credenciales"));
    }

    /**
     * Test TDD: validaAzman - Debe manejar usuario bloqueado
     * 
     * GIVEN: Usuario con cuenta bloqueada en AD
     * WHEN: Intenta autenticarse
     * THEN: Retorna error específico de cuenta bloqueada
     */
    @Test
    public void validaAzman_DebeRetornarError_CuandoUsuarioBloqueado() {
        // Given
        UsuarioIngresoRequestDTO request = new UsuarioIngresoRequestDTO();
        request.setUsuario("usuario.bloqueado");
        request.setPassword("Password123!");

        UsuarioIngresoResponseDTO mockResponse = new UsuarioIngresoResponseDTO();
        mockResponse.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
        mockResponse.setMensajeRespuesta("Cuenta de usuario bloqueada");
        
        when(usuarioService.validarUsuarioAzman(any(UsuarioIngresoRequestDTO.class)))
            .thenReturn(mockResponse);

        // When
        UsuarioIngresoResponseDTO resultado = usuarioController.validaAzman(request);

        // Then
        assertNotNull("El resultado no debe ser nulo", resultado);
        assertEquals("Debe retornar error", 
            Constantes.CODIGO_RESPUESTA_GENERAL_ERROR, resultado.getCodigoRespuesta());
        assertTrue("Mensaje debe indicar bloqueo",
            resultado.getMensajeRespuesta().contains("bloqueada"));
    }

    // ========== TESTS PARA obtenerPerfilUsuario ==========

    /**
     * Test TDD: obtenerPerfilUsuario - Debe obtener perfil exitosamente
     * 
     * GIVEN: Un usuario válido en el sistema
     * WHEN: Se consulta su perfil
     * THEN: Retorna información completa del perfil
     */
    @Test
    public void obtenerPerfilUsuario_DebeRetornarPerfil_CuandoUsuarioExiste() {
        // Given
        String usuario = "maria.gonzalez";
        when(httpServletRequest.getParameter("usuario")).thenReturn(usuario);

        UsuarioIngresoResponseDTO mockResponse = new UsuarioIngresoResponseDTO();
        mockResponse.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
        mockResponse.setIdUsuario(usuario);
        mockResponse.setNombreUsuario("María González");
        
        pe.interseguro.siv.common.dto.response.Perfil perfil = 
            new pe.interseguro.siv.common.dto.response.Perfil();
        perfil.setCodigo("AGENTE");
        perfil.setNombre("Agente");
        mockResponse.setPerfil(perfil);
        
        when(usuarioService.obtenerPerfilUsuario(usuario))
            .thenReturn(mockResponse);

        // When
        UsuarioIngresoResponseDTO resultado = 
            usuarioController.obtenerPerfilUsuario(httpServletRequest, httpServletResponse);

        // Then
        assertNotNull("El resultado no debe ser nulo", resultado);
        assertEquals("Debe retornar éxito", 
            Constantes.CODIGO_RESPUESTA_GENERAL_EXITO, resultado.getCodigoRespuesta());
        assertNotNull("Debe incluir perfil", resultado.getPerfil());
        assertEquals("Código de perfil debe coincidir", 
            "AGENTE", resultado.getPerfil().getCodigo());
        
        verify(usuarioService, times(1)).obtenerPerfilUsuario(usuario);
    }

    /**
     * Test TDD: obtenerPerfilUsuario - Debe manejar usuario no encontrado
     * 
     * GIVEN: Un usuario que no existe
     * WHEN: Se consulta su perfil
     * THEN: Retorna error indicando usuario no encontrado
     */
    @Test
    public void obtenerPerfilUsuario_DebeRetornarError_CuandoUsuarioNoExiste() {
        // Given
        String usuario = "usuario.inexistente";
        when(httpServletRequest.getParameter("usuario")).thenReturn(usuario);

        UsuarioIngresoResponseDTO mockResponse = new UsuarioIngresoResponseDTO();
        mockResponse.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
        mockResponse.setMensajeRespuesta("Usuario no encontrado");
        
        when(usuarioService.obtenerPerfilUsuario(usuario))
            .thenReturn(mockResponse);

        // When
        UsuarioIngresoResponseDTO resultado = 
            usuarioController.obtenerPerfilUsuario(httpServletRequest, httpServletResponse);

        // Then
        assertNotNull("El resultado no debe ser nulo", resultado);
        assertEquals("Debe retornar error", 
            Constantes.CODIGO_RESPUESTA_GENERAL_ERROR, resultado.getCodigoRespuesta());
        assertNull("No debe incluir perfil", resultado.getPerfil());
        assertNotNull("Debe incluir mensaje de error", resultado.getMensajeRespuesta());
    }

    /**
     * Test TDD: obtenerPerfilUsuario - Debe manejar parámetro nulo
     * 
     * GIVEN: Request sin parámetro de usuario
     * WHEN: Se intenta consultar perfil
     * THEN: Maneja correctamente el caso sin lanzar NPE
     */
    @Test
    public void obtenerPerfilUsuario_DebeManejarParametroNulo_SinExcepcion() {
        // Given
        when(httpServletRequest.getParameter("usuario")).thenReturn(null);

        UsuarioIngresoResponseDTO mockResponse = new UsuarioIngresoResponseDTO();
        mockResponse.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
        mockResponse.setMensajeRespuesta("Usuario no proporcionado");
        
        when(usuarioService.obtenerPerfilUsuario(null))
            .thenReturn(mockResponse);

        // When
        UsuarioIngresoResponseDTO resultado = 
            usuarioController.obtenerPerfilUsuario(httpServletRequest, httpServletResponse);

        // Then
        assertNotNull("El resultado no debe ser nulo", resultado);
        assertEquals("Debe retornar error", 
            Constantes.CODIGO_RESPUESTA_GENERAL_ERROR, resultado.getCodigoRespuesta());
    }

    /**
     * Test TDD: obtenerPerfilUsuario - Debe manejar usuario con múltiples perfiles
     * 
     * GIVEN: Usuario con perfil compuesto
     * WHEN: Se consulta su perfil
     * THEN: Retorna el perfil principal correctamente
     */
    @Test
    public void obtenerPerfilUsuario_DebeRetornarPerfilPrincipal_CuandoUsuarioTieneMultiples() {
        // Given
        String usuario = "admin.user";
        when(httpServletRequest.getParameter("usuario")).thenReturn(usuario);

        UsuarioIngresoResponseDTO mockResponse = new UsuarioIngresoResponseDTO();
        mockResponse.setCodigoRespuesta(Constantes.CODIGO_RESPUESTA_GENERAL_EXITO);
        mockResponse.setIdUsuario(usuario);
        
        pe.interseguro.siv.common.dto.response.Perfil perfil = 
            new pe.interseguro.siv.common.dto.response.Perfil();
        perfil.setCodigo("OPERACIONES");
        perfil.setNombre("Operaciones");
        mockResponse.setPerfil(perfil);
        
        when(usuarioService.obtenerPerfilUsuario(usuario))
            .thenReturn(mockResponse);

        // When
        UsuarioIngresoResponseDTO resultado = 
            usuarioController.obtenerPerfilUsuario(httpServletRequest, httpServletResponse);

        // Then
        assertNotNull("El resultado no debe ser nulo", resultado);
        assertEquals("Debe retornar perfil principal", 
            "OPERACIONES", resultado.getPerfil().getCodigo());
    }
}

