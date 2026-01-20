package pe.interseguro.siv.common.exception;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Pruebas unitarias para ErrorResourceDTO
 */
public class ErrorResourceDTOTest {

    @Test
    public void testConstructorSinParametros() {
        ErrorResourceDTO error = new ErrorResourceDTO();
        assertNotNull(error);
    }

    @Test
    public void testConstructorConCodigoYMensaje() {
        ErrorResourceDTO error = new ErrorResourceDTO("ERR001", "Error de validación");
        
        assertEquals("ERR001", error.getCode());
        assertEquals("Error de validación", error.getMessage());
        assertNull(error.getService());
        assertNull(error.getRequest());
    }

    @Test
    public void testConstructorConCodigoMensajeYServicio() {
        ErrorResourceDTO error = new ErrorResourceDTO("ERR002", "Error de servicio", "UsuarioService");
        
        assertEquals("ERR002", error.getCode());
        assertEquals("Error de servicio", error.getMessage());
        assertEquals("UsuarioService", error.getService());
        assertNull(error.getRequest());
    }

    @Test
    public void testConstructorConTodosLosParametros() {
        ErrorResourceDTO error = new ErrorResourceDTO("ERR003", "Error de request", "CotizaService", "/api/cotizacion");
        
        assertEquals("ERR003", error.getCode());
        assertEquals("Error de request", error.getMessage());
        assertEquals("CotizaService", error.getService());
        assertEquals("/api/cotizacion", error.getRequest());
    }

    @Test
    public void testSetYGetCode() {
        ErrorResourceDTO error = new ErrorResourceDTO();
        error.setCode("ERR004");
        
        assertEquals("ERR004", error.getCode());
    }

    @Test
    public void testSetYGetMessage() {
        ErrorResourceDTO error = new ErrorResourceDTO();
        error.setMessage("Mensaje de error");
        
        assertEquals("Mensaje de error", error.getMessage());
    }

    @Test
    public void testSetYGetService() {
        ErrorResourceDTO error = new ErrorResourceDTO();
        error.setService("AdnService");
        
        assertEquals("AdnService", error.getService());
    }

    @Test
    public void testSetYGetRequest() {
        ErrorResourceDTO error = new ErrorResourceDTO();
        error.setRequest("/api/adn/registro");
        
        assertEquals("/api/adn/registro", error.getRequest());
    }

    @Test
    public void testSetYGetFieldErrors() {
        ErrorResourceDTO error = new ErrorResourceDTO();
        List<FieldErrorResourceDTO> fieldErrors = new ArrayList<FieldErrorResourceDTO>();
        error.setFieldErrors(fieldErrors);
        
        assertNotNull(error.getFieldErrors());
        assertTrue(error.getFieldErrors().isEmpty());
    }

    @Test
    public void testValoresIniciales() {
        ErrorResourceDTO error = new ErrorResourceDTO();
        
        assertNull(error.getCode());
        assertNull(error.getMessage());
        assertNull(error.getService());
        assertNull(error.getRequest());
        assertNull(error.getFieldErrors());
    }
}
