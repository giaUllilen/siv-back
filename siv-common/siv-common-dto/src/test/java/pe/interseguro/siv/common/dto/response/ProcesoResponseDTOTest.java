package pe.interseguro.siv.common.dto.response;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Pruebas unitarias para ProcesoResponseDTO
 */
public class ProcesoResponseDTOTest {

    @Test
    public void testConstructor() {
        ProcesoResponseDTO response = new ProcesoResponseDTO();
        assertNotNull(response);
    }

    @Test
    public void testSetYGetExitos() {
        ProcesoResponseDTO response = new ProcesoResponseDTO();
        List<String> exitos = Arrays.asList("Proceso 1 exitoso", "Proceso 2 exitoso");
        response.setExitos(exitos);
        
        assertEquals(2, response.getExitos().size());
        assertEquals("Proceso 1 exitoso", response.getExitos().get(0));
    }

    @Test
    public void testSetYGetErrores() {
        ProcesoResponseDTO response = new ProcesoResponseDTO();
        List<String> errores = Arrays.asList("Error en proceso 1", "Error en proceso 2");
        response.setErrores(errores);
        
        assertEquals(2, response.getErrores().size());
        assertEquals("Error en proceso 1", response.getErrores().get(0));
    }

    @Test
    public void testExitosVacio() {
        ProcesoResponseDTO response = new ProcesoResponseDTO();
        List<String> exitos = new ArrayList<String>();
        response.setExitos(exitos);
        
        assertNotNull(response.getExitos());
        assertTrue(response.getExitos().isEmpty());
    }

    @Test
    public void testErroresVacio() {
        ProcesoResponseDTO response = new ProcesoResponseDTO();
        List<String> errores = new ArrayList<String>();
        response.setErrores(errores);
        
        assertNotNull(response.getErrores());
        assertTrue(response.getErrores().isEmpty());
    }

    @Test
    public void testValoresIniciales() {
        ProcesoResponseDTO response = new ProcesoResponseDTO();
        
        assertNull(response.getExitos());
        assertNull(response.getErrores());
    }

    @Test
    public void testHerenciaDeBaseResponseDTO() {
        ProcesoResponseDTO response = new ProcesoResponseDTO();
        response.setCodigoRespuesta("01");
        response.setMensajeRespuesta("Proceso completado");
        
        assertEquals("01", response.getCodigoRespuesta());
        assertEquals("Proceso completado", response.getMensajeRespuesta());
    }

    @Test
    public void testSerialVersionUID() {
        ProcesoResponseDTO response = new ProcesoResponseDTO();
        assertTrue(response instanceof java.io.Serializable);
    }
}
