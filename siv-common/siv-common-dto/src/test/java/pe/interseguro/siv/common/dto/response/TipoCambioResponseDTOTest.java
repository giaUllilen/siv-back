package pe.interseguro.siv.common.dto.response;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Pruebas unitarias para TipoCambioResponseDTO
 */
public class TipoCambioResponseDTOTest {

    @Test
    public void testConstructorSinParametros() {
        TipoCambioResponseDTO response = new TipoCambioResponseDTO();
        assertNotNull(response);
        assertNull(response.getConversiones());
    }

    @Test
    public void testConstructorConParametros() {
        List<ConversionResponseDTO> conversiones = new ArrayList<ConversionResponseDTO>();
        TipoCambioResponseDTO response = new TipoCambioResponseDTO(conversiones);
        
        assertNotNull(response);
        assertNotNull(response.getConversiones());
        assertTrue(response.getConversiones().isEmpty());
    }

    @Test
    public void testSetYGetConversiones() {
        TipoCambioResponseDTO response = new TipoCambioResponseDTO();
        List<ConversionResponseDTO> conversiones = new ArrayList<ConversionResponseDTO>();
        ConversionResponseDTO conversion = new ConversionResponseDTO();
        conversiones.add(conversion);
        
        response.setConversiones(conversiones);
        
        assertNotNull(response.getConversiones());
        assertEquals(1, response.getConversiones().size());
    }

    @Test
    public void testConversionesVacia() {
        TipoCambioResponseDTO response = new TipoCambioResponseDTO();
        List<ConversionResponseDTO> conversiones = new ArrayList<ConversionResponseDTO>();
        response.setConversiones(conversiones);
        
        assertNotNull(response.getConversiones());
        assertTrue(response.getConversiones().isEmpty());
    }

    @Test
    public void testHerenciaDeBaseResponseDTO() {
        TipoCambioResponseDTO response = new TipoCambioResponseDTO();
        response.setCodigoRespuesta("01");
        response.setMensajeRespuesta("Tipo de cambio obtenido");
        
        assertEquals("01", response.getCodigoRespuesta());
        assertEquals("Tipo de cambio obtenido", response.getMensajeRespuesta());
    }

    @Test
    public void testSerialVersionUID() {
        TipoCambioResponseDTO response = new TipoCambioResponseDTO();
        assertTrue(response instanceof java.io.Serializable);
    }
}
