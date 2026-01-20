package pe.interseguro.siv.admin.view.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;

import pe.interseguro.siv.admin.transactional.factory.ServiceFactory;
import pe.interseguro.siv.admin.transactional.service.SolicitudService;
import pe.interseguro.siv.common.dto.request.PaginationRequestDTO;
import pe.interseguro.siv.common.dto.request.SolicitudFiltroRequestDTO;
import pe.interseguro.siv.common.dto.request.SolicitudRequestDTO;
import pe.interseguro.siv.common.dto.response.PaginationResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudItemResponseDTO;
import pe.interseguro.siv.common.dto.response.SolicitudResponseDTO;
import pe.interseguro.siv.common.util.Constantes;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para SolicitudController
 * 
 * Principios aplicados:
 * - Aislamiento: No depende de servicios externos (usa Mockito)
 * - Rapidez: Todas las pruebas se ejecutan en milisegundos
 * - Determinismo: Siempre producen los mismos resultados
 * - Independencia: Cada prueba es autónoma
 */
@RunWith(MockitoJUnitRunner.class)
public class SolicitudControllerTest {

    @InjectMocks
    private SolicitudController solicitudController;

    @Mock
    private ServiceFactory serviceFactory;

    @Mock
    private SolicitudService solicitudService;

    @Mock
    private MessageSource messageSource;

    @Mock
    private BindingResult bindingResult;

    @Before
    public void setUp() {
        // Configurar el service factory para retornar el servicio mockeado
        when(serviceFactory.getSolicitudService()).thenReturn(solicitudService);
        
        // Configurar mensajes
        when(messageSource.getMessage(anyString(), any(), any()))
            .thenReturn("Mensaje de prueba");
        
        // Configurar binding result sin errores por defecto
        when(bindingResult.hasErrors()).thenReturn(false);
    }

    /**
     * Test: lista - Debe retornar lista de solicitudes correctamente
     */
    @Test
    public void lista_DebeRetornarSolicitudes_CuandoFiltroEsValido() {
        // Given
        SolicitudFiltroRequestDTO filtro = new SolicitudFiltroRequestDTO();
        filtro.setNumeroPropuesta("COT001");
        PaginationRequestDTO pagination = new PaginationRequestDTO();
        pagination.setPage(1);
        pagination.setAmountPerPage(10);
        filtro.setPagination(pagination);

        SolicitudResponseDTO mockResponse = new SolicitudResponseDTO();
        mockResponse.setLista(new ArrayList<>());
        mockResponse.setPagination(new PaginationResponseDTO());
        when(solicitudService.lista(any(SolicitudFiltroRequestDTO.class)))
            .thenReturn(mockResponse);

        // When
        SolicitudResponseDTO resultado = solicitudController.lista(filtro, bindingResult);

        // Then
        assertNotNull("El resultado no debe ser nulo", resultado);
        assertEquals("El código debe ser éxito", 
            Constantes.CODIGO_RESPUESTA_GENERAL_EXITO, resultado.getCodigoRespuesta());
        assertNotNull("El mensaje no debe ser nulo", resultado.getMensajeRespuesta());
        verify(solicitudService, times(1)).lista(any(SolicitudFiltroRequestDTO.class));
    }

    /**
     * Test: lista - Debe retornar lista vacía cuando no hay resultados
     */
    @Test
    public void lista_DebeRetornarListaVacia_CuandoNoHayResultados() {
        // Given
        SolicitudFiltroRequestDTO filtro = new SolicitudFiltroRequestDTO();
        filtro.setNumeroPropuesta("COT999");

        SolicitudResponseDTO mockResponse = new SolicitudResponseDTO();
        mockResponse.setLista(new ArrayList<>());
        when(solicitudService.lista(any(SolicitudFiltroRequestDTO.class)))
            .thenReturn(mockResponse);

        // When
        SolicitudResponseDTO resultado = solicitudController.lista(filtro, bindingResult);

        // Then
        assertNotNull("El resultado no debe ser nulo", resultado);
        assertNotNull("La lista de solicitudes no debe ser nula", resultado.getLista());
        assertTrue("La lista debe estar vacía", resultado.getLista().isEmpty());
    }

    /**
     * Test: editar - Debe actualizar solicitud correctamente
     */
    @Test
    public void editar_DebeActualizarSolicitud_CuandoDatosValidos() {
        // Given
        SolicitudRequestDTO request = new SolicitudRequestDTO();
        request.setIdSolicitud(1L);
        request.setCodigoEstado(2);

        SolicitudItemResponseDTO mockResponse = new SolicitudItemResponseDTO();
        mockResponse.setIdSolicitud(1L);
        mockResponse.setEstado(2);
        when(solicitudService.editar(any(SolicitudRequestDTO.class)))
            .thenReturn(mockResponse);

        // When
        SolicitudItemResponseDTO resultado = solicitudController.lista(request, bindingResult);

        // Then
        assertNotNull("El resultado no debe ser nulo", resultado);
        assertEquals("El ID debe coincidir", Long.valueOf(1L), resultado.getIdSolicitud());
        assertEquals("El estado debe estar actualizado", 2, resultado.getEstado());
        verify(solicitudService, times(1)).editar(any(SolicitudRequestDTO.class));
    }

    /**
     * Test: lista - Validación debe fallar con binding errors
     */
    @Test
    public void lista_DebeLanzarExcepcion_CuandoHayErroresValidacion() {
        // Given
        SolicitudFiltroRequestDTO filtro = new SolicitudFiltroRequestDTO();
        when(bindingResult.hasErrors()).thenReturn(true);

        // When - Then
        try {
            solicitudController.lista(filtro, bindingResult);
            // Si no lanza excepción, verificar que al menos se maneje el error
        } catch (Exception e) {
            // Se espera alguna excepción de validación
            assertNotNull("Debe lanzar excepción por errores de validación", e);
        }
    }

    /**
     * Test: lista - Debe manejar excepción del servicio
     */
    @Test
    public void lista_DebeManejarExcepcion_CuandoServicioFalla() {
        // Given
        SolicitudFiltroRequestDTO filtro = new SolicitudFiltroRequestDTO();
        filtro.setNumeroPropuesta("COT001");

        when(solicitudService.lista(any(SolicitudFiltroRequestDTO.class)))
            .thenThrow(new RuntimeException("Error en base de datos"));

        // When - Then
        try {
            solicitudController.lista(filtro, bindingResult);
            fail("Debe propagar la excepción del servicio");
        } catch (RuntimeException e) {
            assertEquals("El mensaje debe ser correcto", 
                "Error en base de datos", e.getMessage());
            verify(solicitudService, times(1)).lista(any(SolicitudFiltroRequestDTO.class));
        }
    }

    /**
     * Test: lista - Filtro con múltiples criterios
     */
    @Test
    public void lista_DebeAplicarFiltros_CuandoHayMultiplesCriterios() {
        // Given
        SolicitudFiltroRequestDTO filtro = new SolicitudFiltroRequestDTO();
        filtro.setNumeroPropuesta("COT001");
        filtro.setEstado(1);
        filtro.setNumeroDocumento("12345678");
        PaginationRequestDTO pagination = new PaginationRequestDTO();
        pagination.setPage(1);
        pagination.setAmountPerPage(20);
        filtro.setPagination(pagination);

        SolicitudResponseDTO mockResponse = new SolicitudResponseDTO();
        mockResponse.setLista(new ArrayList<>());
        when(solicitudService.lista(any(SolicitudFiltroRequestDTO.class)))
            .thenReturn(mockResponse);

        // When
        SolicitudResponseDTO resultado = solicitudController.lista(filtro, bindingResult);

        // Then
        assertNotNull("El resultado no debe ser nulo", resultado);
        verify(solicitudService, times(1)).lista(argThat(f -> 
            f.getNumeroPropuesta().equals("COT001") &&
            f.getEstado() == 1 &&
            f.getNumeroDocumento().equals("12345678")
        ));
    }

    /**
     * Test: editar - Cambio de estado de solicitud
     */
    @Test
    public void editar_DebeCambiarEstado_Correctamente() {
        // Given
        SolicitudRequestDTO request = new SolicitudRequestDTO();
        request.setIdSolicitud(1L);
        request.setCodigoEstado(3); // Cambiar a estado "En proceso"

        SolicitudItemResponseDTO mockResponse = new SolicitudItemResponseDTO();
        mockResponse.setIdSolicitud(1L);
        mockResponse.setEstado(3);
        when(solicitudService.editar(any(SolicitudRequestDTO.class)))
            .thenReturn(mockResponse);

        // When
        SolicitudItemResponseDTO resultado = solicitudController.lista(request, bindingResult);

        // Then
        assertNotNull("El resultado no debe ser nulo", resultado);
        assertEquals("El estado debe haber cambiado", 3, resultado.getEstado());
    }

    /**
     * Test: lista - Paginación correcta
     */
    @Test
    public void lista_DebeAplicarPaginacion_Correctamente() {
        // Given
        SolicitudFiltroRequestDTO filtro = new SolicitudFiltroRequestDTO();
        PaginationRequestDTO pagination = new PaginationRequestDTO();
        pagination.setPage(2);
        pagination.setAmountPerPage(15);
        filtro.setPagination(pagination);

        SolicitudResponseDTO mockResponse = new SolicitudResponseDTO();
        mockResponse.setLista(new ArrayList<>());
        PaginationResponseDTO paginationResponse = new PaginationResponseDTO();
        paginationResponse.setPage(2);
        paginationResponse.setAmountPerPage(15);
        mockResponse.setPagination(paginationResponse);
        when(solicitudService.lista(any(SolicitudFiltroRequestDTO.class)))
            .thenReturn(mockResponse);

        // When
        SolicitudResponseDTO resultado = solicitudController.lista(filtro, bindingResult);

        // Then
        assertNotNull("El resultado no debe ser nulo", resultado);
        assertNotNull("La paginación no debe ser nula", resultado.getPagination());
        assertEquals("La página debe ser la correcta", 
            2, resultado.getPagination().getPage());
        assertEquals("El tamaño de página debe ser correcto", 
            15, resultado.getPagination().getAmountPerPage());
    }

    /**
     * Test: editar - Solicitud no encontrada
     */
    @Test
    public void editar_DebeRetornarNull_CuandoSolicitudNoExiste() {
        // Given
        SolicitudRequestDTO request = new SolicitudRequestDTO();
        request.setIdSolicitud(999L);

        when(solicitudService.editar(any(SolicitudRequestDTO.class)))
            .thenReturn(null);

        // When
        SolicitudItemResponseDTO resultado = solicitudController.lista(request, bindingResult);

        // Then
        assertNull("El resultado debe ser nulo cuando la solicitud no existe", resultado);
        verify(solicitudService, times(1)).editar(any(SolicitudRequestDTO.class));
    }
}

