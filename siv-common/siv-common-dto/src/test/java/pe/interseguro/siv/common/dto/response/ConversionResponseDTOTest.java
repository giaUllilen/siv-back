package pe.interseguro.siv.common.dto.response;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Pruebas unitarias para ConversionResponseDTO
 */
public class ConversionResponseDTOTest {

    @Test
    public void testConstructorSinParametros() {
        ConversionResponseDTO response = new ConversionResponseDTO();
        assertNotNull(response);
    }

    @Test
    public void testConstructorConParametros() {
        ConversionResponseDTO response = new ConversionResponseDTO("USD", "PEN", "2024-01-15", 3.75);
        
        assertNotNull(response);
        assertEquals("USD", response.getMonedaOrigen());
        assertEquals("PEN", response.getMonedaConversion());
        assertEquals("2024-01-15", response.getFecha());
        assertEquals(Double.valueOf(3.75), response.getValor());
    }

    @Test
    public void testSetYGetMonedaOrigen() {
        ConversionResponseDTO response = new ConversionResponseDTO();
        response.setMonedaOrigen("USD");
        
        assertEquals("USD", response.getMonedaOrigen());
    }

    @Test
    public void testSetYGetMonedaConversion() {
        ConversionResponseDTO response = new ConversionResponseDTO();
        response.setMonedaConversion("PEN");
        
        assertEquals("PEN", response.getMonedaConversion());
    }

    @Test
    public void testSetYGetFecha() {
        ConversionResponseDTO response = new ConversionResponseDTO();
        response.setFecha("2024-01-15");
        
        assertEquals("2024-01-15", response.getFecha());
    }

    @Test
    public void testSetYGetValor() {
        ConversionResponseDTO response = new ConversionResponseDTO();
        response.setValor(3.75);
        
        assertEquals(Double.valueOf(3.75), response.getValor());
    }

    @Test
    public void testHerenciaDeBaseResponseDTO() {
        ConversionResponseDTO response = new ConversionResponseDTO();
        response.setCodigoRespuesta("01");
        response.setMensajeRespuesta("Conversión exitosa");
        
        assertEquals("01", response.getCodigoRespuesta());
        assertEquals("Conversión exitosa", response.getMensajeRespuesta());
    }

    @Test
    public void testSerialVersionUID() {
        ConversionResponseDTO response = new ConversionResponseDTO();
        assertTrue(response instanceof java.io.Serializable);
    }
}
