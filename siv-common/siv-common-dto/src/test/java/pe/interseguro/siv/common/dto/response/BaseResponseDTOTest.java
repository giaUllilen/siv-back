package pe.interseguro.siv.common.dto.response;

import org.junit.Test;
import pe.interseguro.siv.common.exception.ErrorResourceDTO;

import static org.junit.Assert.*;

/**
 * Pruebas unitarias para BaseResponseDTO
 */
public class BaseResponseDTOTest {

    @Test
    public void testConstructor() {
        BaseResponseDTO response = new BaseResponseDTO();
        assertNotNull(response);
    }

    @Test
    public void testSetYGetCodigoRespuesta() {
        BaseResponseDTO response = new BaseResponseDTO();
        response.setCodigoRespuesta("01");
        
        assertEquals("01", response.getCodigoRespuesta());
    }

    @Test
    public void testSetYGetMensajeRespuesta() {
        BaseResponseDTO response = new BaseResponseDTO();
        response.setMensajeRespuesta("Operación exitosa");
        
        assertEquals("Operación exitosa", response.getMensajeRespuesta());
    }

    @Test
    public void testSetYGetObservaciones() {
        BaseResponseDTO response = new BaseResponseDTO();
        response.setObservaciones("Sin observaciones");
        
        assertEquals("Sin observaciones", response.getObservaciones());
    }

    @Test
    public void testSetYGetObjErrorResource() {
        BaseResponseDTO response = new BaseResponseDTO();
        ErrorResourceDTO error = new ErrorResourceDTO();
        response.setObjErrorResource(error);
        
        assertNotNull(response.getObjErrorResource());
        assertEquals(error, response.getObjErrorResource());
    }

    @Test
    public void testValoresIniciales() {
        BaseResponseDTO response = new BaseResponseDTO();
        
        assertNull(response.getCodigoRespuesta());
        assertNull(response.getMensajeRespuesta());
        assertNull(response.getObservaciones());
        assertNull(response.getObjErrorResource());
    }

    @Test
    public void testSerializable() {
        BaseResponseDTO response = new BaseResponseDTO();
        assertTrue(response instanceof java.io.Serializable);
    }
}
