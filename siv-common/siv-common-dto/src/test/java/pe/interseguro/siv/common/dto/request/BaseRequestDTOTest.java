package pe.interseguro.siv.common.dto.request;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Pruebas unitarias para BaseRequestDTO
 */
public class BaseRequestDTOTest {

    @Test
    public void testConstructor() {
        BaseRequestDTO request = new BaseRequestDTO();
        assertNotNull(request);
    }

    @Test
    public void testSetYGetDevice() {
        BaseRequestDTO request = new BaseRequestDTO();
        request.setDevice("mobile");
        
        assertEquals("mobile", request.getDevice());
    }

    @Test
    public void testSetYGetOs() {
        BaseRequestDTO request = new BaseRequestDTO();
        request.setOs("Android 12");
        
        assertEquals("Android 12", request.getOs());
    }

    @Test
    public void testSetYGetPath() {
        BaseRequestDTO request = new BaseRequestDTO();
        request.setPath("/api/usuarios");
        
        assertEquals("/api/usuarios", request.getPath());
    }

    @Test
    public void testSetYGetMethod() {
        BaseRequestDTO request = new BaseRequestDTO();
        request.setMethod("POST");
        
        assertEquals("POST", request.getMethod());
    }

    @Test
    public void testValoresIniciales() {
        BaseRequestDTO request = new BaseRequestDTO();
        
        assertNull(request.getDevice());
        assertNull(request.getOs());
        assertNull(request.getPath());
        assertNull(request.getMethod());
    }

    @Test
    public void testSerializable() {
        BaseRequestDTO request = new BaseRequestDTO();
        assertTrue(request instanceof java.io.Serializable);
    }

    @Test
    public void testSetTodosCampos() {
        BaseRequestDTO request = new BaseRequestDTO();
        request.setDevice("desktop");
        request.setOs("Windows 10");
        request.setPath("/api/solicitudes");
        request.setMethod("GET");
        
        assertEquals("desktop", request.getDevice());
        assertEquals("Windows 10", request.getOs());
        assertEquals("/api/solicitudes", request.getPath());
        assertEquals("GET", request.getMethod());
    }
}
