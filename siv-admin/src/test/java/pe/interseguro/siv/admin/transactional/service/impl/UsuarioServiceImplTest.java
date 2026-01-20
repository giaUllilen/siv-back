package pe.interseguro.siv.admin.transactional.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import pe.interseguro.siv.common.dto.request.UsuarioIngresoRequestDTO;
import pe.interseguro.siv.common.dto.response.UsuarioIngresoResponseDTO;
import pe.interseguro.siv.common.enums.PerfilEnum;
import pe.interseguro.siv.common.persistence.db.mysql.repository.UsuarioPerfilRepository;
import pe.interseguro.siv.common.persistence.db.mysql.response.Perfil;
import pe.interseguro.siv.common.persistence.rest.crm.CrmRestClient;
import pe.interseguro.siv.common.persistence.rest.global.GlobalRestClient;
import pe.interseguro.siv.common.persistence.rest.global.request.ObtenerAgenteRequest;
import pe.interseguro.siv.common.persistence.rest.global.response.ObtenerAgenteResponse;
import pe.interseguro.siv.common.persistence.rest.interseguro.InterseguroRestClient;
import pe.interseguro.siv.common.persistence.rest.interseguro.request.ObtenerDatosUsuarioRequest;
import pe.interseguro.siv.common.persistence.rest.interseguro.response.ObtenerDatosUsuarioResponse;
import pe.interseguro.siv.common.util.Constantes;

import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para UsuarioServiceImpl
 * 
 * Principios aplicados:
 * - Aislamiento: No depende de BD ni APIs externas (usa Mockito)
 * - Rapidez: Todas las pruebas se ejecutan en milisegundos
 * - Determinismo: Siempre producen los mismos resultados
 * - Independencia: Cada prueba es autónoma
 */
@RunWith(MockitoJUnitRunner.class)
public class UsuarioServiceImplTest {

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @Mock
    private MessageSource messageSource;

    @Mock
    private InterseguroRestClient interseguroRestClient;

    @Mock
    private CrmRestClient crmRestClient;

    @Mock
    private GlobalRestClient globalRestClient;

    @Mock
    private UsuarioPerfilRepository usuarioPerfilRepository;

    @Before
    public void setUp() {
        // Configuración común para todas las pruebas
        when(messageSource.getMessage(anyString(), any(), any()))
            .thenReturn("Mensaje de prueba");
    }

    /**
     * Test: validarUsuario - Usuario válido con perfil administrativo
     */
    @Test
    public void validarUsuario_DebeRetornarExito_CuandoUsuarioAdministrativoEsValido() {
        // Given
        UsuarioIngresoRequestDTO request = crearRequestUsuarioValido();
        
        ObtenerDatosUsuarioResponse azmanResponse = crearAzmanResponseExitoso();
        when(interseguroRestClient.obtenerDatosUsuario(any(ObtenerDatosUsuarioRequest.class)))
            .thenReturn(azmanResponse);

        Perfil perfil = crearPerfilAdministrador();
        when(usuarioPerfilRepository.findByUsuario(anyString())).thenReturn(perfil);

        // When
        UsuarioIngresoResponseDTO resultado = usuarioService.validarUsuario(request);

        // Then
        assertNotNull("El resultado no debe ser nulo", resultado);
        assertEquals("El código debe ser éxito", 
            Constantes.CODIGO_RESPUESTA_GENERAL_EXITO, resultado.getCodigoRespuesta());
        assertNotNull("El token JWT no debe ser nulo", resultado.getJwtToken());
        assertNotNull("El ID de usuario no debe ser nulo", resultado.getIdUsuario());
        assertNotNull("El perfil no debe ser nulo", resultado.getPerfil());
        verify(interseguroRestClient, times(1))
            .obtenerDatosUsuario(any(ObtenerDatosUsuarioRequest.class));
    }

    /**
     * Test: validarUsuario - Usuario válido con perfil agente
     */
    @Test
    public void validarUsuario_DebeRetornarExito_CuandoUsuarioAgenteEsValido() {
        // Given
        UsuarioIngresoRequestDTO request = crearRequestUsuarioValido();
        
        ObtenerDatosUsuarioResponse azmanResponse = crearAzmanResponseAgenteExitoso();
        when(interseguroRestClient.obtenerDatosUsuario(any(ObtenerDatosUsuarioRequest.class)))
            .thenReturn(azmanResponse);

        Perfil perfil = crearPerfilAgente();
        when(usuarioPerfilRepository.findByUsuario(anyString())).thenReturn(perfil);

        ObtenerAgenteResponse agenteResponse = crearAgenteResponseExitoso();
        when(globalRestClient.obtenerAgente(any(ObtenerAgenteRequest.class)))
            .thenReturn(agenteResponse);

        // When
        UsuarioIngresoResponseDTO resultado = usuarioService.validarUsuario(request);

        // Then
        assertNotNull("El resultado no debe ser nulo", resultado);
        assertEquals("El código debe ser éxito", 
            Constantes.CODIGO_RESPUESTA_GENERAL_EXITO, resultado.getCodigoRespuesta());
        assertNotNull("El token JWT no debe ser nulo", resultado.getJwtToken());
        assertTrue("Debe ser agente de solicitud", resultado.getEsAgenteSolicitud());
        assertNotNull("El código de vendedor CRM no debe ser nulo", 
            resultado.getCodigoVendedorCRM());
        verify(globalRestClient, times(1)).obtenerAgente(any(ObtenerAgenteRequest.class));
    }

    /**
     * Test: validarUsuario - Credenciales incorrectas
     */
    @Test
    public void validarUsuario_DebeRetornarError_CuandoCredencialesSonIncorrectas() {
        // Given
        UsuarioIngresoRequestDTO request = crearRequestUsuarioInvalido();
        
        ObtenerDatosUsuarioResponse azmanResponse = crearAzmanResponseError();
        when(interseguroRestClient.obtenerDatosUsuario(any(ObtenerDatosUsuarioRequest.class)))
            .thenReturn(azmanResponse);

        // When
        UsuarioIngresoResponseDTO resultado = usuarioService.validarUsuario(request);

        // Then
        assertNotNull("El resultado no debe ser nulo", resultado);
        assertEquals("El código debe ser error", 
            Constantes.CODIGO_RESPUESTA_GENERAL_ERROR, resultado.getCodigoRespuesta());
        assertNull("El token JWT debe ser nulo", resultado.getJwtToken());
        assertNotNull("Debe tener mensaje de error", resultado.getMensajeRespuesta());
    }

    /**
     * Test: validarUsuario - Usuario sin rol adecuado
     */
    @Test
    public void validarUsuario_DebeRetornarError_CuandoUsuarioNoTieneRolAdecuado() {
        // Given
        UsuarioIngresoRequestDTO request = crearRequestUsuarioValido();
        
        ObtenerDatosUsuarioResponse azmanResponse = crearAzmanResponseRolInvalido();
        when(interseguroRestClient.obtenerDatosUsuario(any(ObtenerDatosUsuarioRequest.class)))
            .thenReturn(azmanResponse);

        // When
        UsuarioIngresoResponseDTO resultado = usuarioService.validarUsuario(request);

        // Then
        assertNotNull("El resultado no debe ser nulo", resultado);
        assertEquals("El código debe ser error", 
            Constantes.CODIGO_RESPUESTA_GENERAL_ERROR, resultado.getCodigoRespuesta());
        assertNull("El token JWT debe ser nulo", resultado.getJwtToken());
    }

    /**
     * Test: getRefreshToken - Debe generar nuevo token correctamente
     */
    @Test
    public void getRefreshToken_DebeGenerarNuevoToken_Correctamente() {
        // Given
        Claims claims = Jwts.claims().setSubject("usuario.test");
        claims.setIssuer(Constantes.JWT_ISSUER);
        claims.setAudience(Constantes.JWT_AUDIENCE);
        claims.setIssuedAt(new Date());

        // When
        UsuarioIngresoResponseDTO resultado = usuarioService.getRefreshToken(claims);

        // Then
        assertNotNull("El resultado no debe ser nulo", resultado);
        assertEquals("El código debe ser éxito", 
            Constantes.CODIGO_RESPUESTA_GENERAL_EXITO, resultado.getCodigoRespuesta());
        assertNotNull("El nuevo token JWT no debe ser nulo", resultado.getJwtToken());
        assertTrue("El token debe tener formato válido", 
            resultado.getJwtToken().split("\\.").length == 3);
    }

    /**
     * Test: getRefreshToken - Claims nulos deben manejarse correctamente
     */
    @Test
    public void getRefreshToken_DebeManejarError_CuandoClaimsEsNulo() {
        // Given
        Claims claims = null;

        // When - Then
        try {
            usuarioService.getRefreshToken(claims);
            fail("Debe lanzar excepción cuando claims es nulo");
        } catch (NullPointerException e) {
            // Esperado
            assertNotNull("Debe lanzar NullPointerException", e);
        }
    }

    /**
     * Test: generateToken - Validar estructura del token generado
     */
    @Test
    public void generateToken_DebeGenerarTokenConEstructuraValida() {
        // Given
        UsuarioIngresoRequestDTO request = crearRequestUsuarioValido();
        
        ObtenerDatosUsuarioResponse azmanResponse = crearAzmanResponseExitoso();
        when(interseguroRestClient.obtenerDatosUsuario(any(ObtenerDatosUsuarioRequest.class)))
            .thenReturn(azmanResponse);

        Perfil perfil = crearPerfilAdministrador();
        when(usuarioPerfilRepository.findByUsuario(anyString())).thenReturn(perfil);

        // When
        UsuarioIngresoResponseDTO resultado = usuarioService.validarUsuario(request);

        // Then
        String token = resultado.getJwtToken();
        assertNotNull("El token no debe ser nulo", token);
        
        // Validar estructura JWT (header.payload.signature)
        String[] partes = token.split("\\.");
        assertEquals("El token debe tener 3 partes", 3, partes.length);
        assertTrue("Cada parte debe tener contenido", 
            partes[0].length() > 0 && partes[1].length() > 0 && partes[2].length() > 0);
    }

    /**
     * Test: validarUsuario - Servicio de agente falla
     */
    @Test
    public void validarUsuario_DebeManejarError_CuandoServicioAgenteFalla() {
        // Given
        UsuarioIngresoRequestDTO request = crearRequestUsuarioValido();
        
        ObtenerDatosUsuarioResponse azmanResponse = crearAzmanResponseAgenteExitoso();
        when(interseguroRestClient.obtenerDatosUsuario(any(ObtenerDatosUsuarioRequest.class)))
            .thenReturn(azmanResponse);

        Perfil perfil = crearPerfilAgente();
        when(usuarioPerfilRepository.findByUsuario(anyString())).thenReturn(perfil);

        when(globalRestClient.obtenerAgente(any(ObtenerAgenteRequest.class)))
            .thenThrow(new RuntimeException("Error en servicio de agente"));

        // When
        UsuarioIngresoResponseDTO resultado = usuarioService.validarUsuario(request);

        // Then
        assertNotNull("El resultado no debe ser nulo", resultado);
        assertEquals("El código debe ser error", 
            Constantes.CODIGO_RESPUESTA_GENERAL_ERROR, resultado.getCodigoRespuesta());
    }

    // ========== Métodos auxiliares para crear objetos mock ==========

    private UsuarioIngresoRequestDTO crearRequestUsuarioValido() {
        UsuarioIngresoRequestDTO request = new UsuarioIngresoRequestDTO();
        request.setUsuario("usuario.test");
        request.setPassword("password123");
        request.setPath("/api/v1/login");
        request.setDevice("WEB");
        request.setOs("Windows");
        return request;
    }

    private UsuarioIngresoRequestDTO crearRequestUsuarioInvalido() {
        UsuarioIngresoRequestDTO request = new UsuarioIngresoRequestDTO();
        request.setUsuario("usuario.invalido");
        request.setPassword("wrongpassword");
        request.setPath("/api/v1/login");
        request.setDevice("WEB");
        request.setOs("Windows");
        return request;
    }

    private ObtenerDatosUsuarioResponse crearAzmanResponseExitoso() {
        ObtenerDatosUsuarioResponse response = new ObtenerDatosUsuarioResponse();
        response.setStatusHttp("200");
        response.setCode("00");
        response.setMessage("Éxito");
        response.setRolAzman("Perfil_Agente_Comercial_vida");
        response.setNombres("Juan");
        response.setApellidos("Pérez García");
        response.setNombreCompleto("Juan Pérez García");
        response.setCorreo("juan.perez@interseguro.pe");
        return response;
    }

    private ObtenerDatosUsuarioResponse crearAzmanResponseAgenteExitoso() {
        ObtenerDatosUsuarioResponse response = crearAzmanResponseExitoso();
        response.setRolAzman("Perfil_Agente_Comercial_vida");
        return response;
    }

    private ObtenerDatosUsuarioResponse crearAzmanResponseError() {
        ObtenerDatosUsuarioResponse response = new ObtenerDatosUsuarioResponse();
        response.setStatusHttp("401");
        response.setCode("01");
        response.setMessage("Credenciales incorrectas");
        response.setTitle("Error de autenticación");
        return response;
    }

    private ObtenerDatosUsuarioResponse crearAzmanResponseRolInvalido() {
        ObtenerDatosUsuarioResponse response = new ObtenerDatosUsuarioResponse();
        response.setStatusHttp("200");
        response.setCode("00");
        response.setMessage("Éxito");
        response.setRolAzman("Perfil_Sin_Acceso");
        response.setNombres("Usuario");
        response.setApellidos("Sin Acceso");
        response.setNombreCompleto("Usuario Sin Acceso");
        return response;
    }

    private Perfil crearPerfilAdministrador() {
        Perfil perfil = new Perfil();
        perfil.setId(1L);
        perfil.setCodigo(PerfilEnum.PERFIL_OPERACIONES.getCodigo());
        perfil.setNombre(PerfilEnum.PERFIL_OPERACIONES.getPerfil());
        return perfil;
    }

    private Perfil crearPerfilAgente() {
        Perfil perfil = new Perfil();
        perfil.setId(2L);
        perfil.setCodigo(PerfilEnum.PERFIL_AGENTE.getCodigo());
        perfil.setNombre(PerfilEnum.PERFIL_AGENTE.getPerfil());
        return perfil;
    }

    private ObtenerAgenteResponse crearAgenteResponseExitoso() {
        ObtenerAgenteResponse response = new ObtenerAgenteResponse();
        response.setCOD_USERNAME_AGENTE("AGT001");
        response.setCOD_AGENTE("12345");
        response.setCOD_AGENCIA("AG001");
        response.setGLS_CORREO_AGENTE("agente@interseguro.pe");
        response.setNOM_AGENTE("Agente Comercial");
        return response;
    }
}
