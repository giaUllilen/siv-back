package pe.interseguro.siv.common.dto.response;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Pruebas unitarias para CotizaUrlResponse
 */
public class CotizaUrlResponseTest {

    @Test
    public void testConstructorSinParametros() {
        CotizaUrlResponse response = new CotizaUrlResponse();
        assertNotNull(response);
    }

    @Test
    public void testConstructorConParametros() {
        CotizaUrlResponse response = new CotizaUrlResponse(true, "https://cotizador.example.com", "Cotización exitosa");
        
        assertNotNull(response);
        assertEquals(Boolean.TRUE, response.getResult());
        assertEquals("https://cotizador.example.com", response.getURL());
        assertEquals("Cotización exitosa", response.getMessage());
    }

    @Test
    public void testSetYGetResult() {
        CotizaUrlResponse response = new CotizaUrlResponse();
        response.setResult(true);
        
        assertEquals(Boolean.TRUE, response.getResult());
    }

    @Test
    public void testSetYGetURL() {
        CotizaUrlResponse response = new CotizaUrlResponse();
        response.setURL("https://cotizador.example.com");
        
        assertEquals("https://cotizador.example.com", response.getURL());
    }

    @Test
    public void testSetYGetMessage() {
        CotizaUrlResponse response = new CotizaUrlResponse();
        response.setMessage("Mensaje de prueba");
        
        assertEquals("Mensaje de prueba", response.getMessage());
    }

    @Test
    public void testResultFalse() {
        CotizaUrlResponse response = new CotizaUrlResponse(false, null, "Error en cotización");
        
        assertEquals(Boolean.FALSE, response.getResult());
        assertNull(response.getURL());
        assertEquals("Error en cotización", response.getMessage());
    }

    @Test
    public void testHerenciaDeBaseResponseDTO() {
        CotizaUrlResponse response = new CotizaUrlResponse();
        response.setCodigoRespuesta("01");
        response.setMensajeRespuesta("Proceso exitoso");
        
        assertEquals("01", response.getCodigoRespuesta());
        assertEquals("Proceso exitoso", response.getMensajeRespuesta());
    }

    @Test
    public void testSerializable() {
        CotizaUrlResponse response = new CotizaUrlResponse();
        assertTrue(response instanceof java.io.Serializable);
    }
}
