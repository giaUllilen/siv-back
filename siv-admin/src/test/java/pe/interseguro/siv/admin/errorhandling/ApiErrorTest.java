package pe.interseguro.siv.admin.errorhandling;

import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Pruebas unitarias para ApiError
 */
public class ApiErrorTest {

    @Test
    public void testConstructorSinParametros() {
        ApiError apiError = new ApiError();
        assertNotNull(apiError);
    }

    @Test
    public void testConstructorConListaErrores() {
        List<String> errors = Arrays.asList("Error 1", "Error 2");
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "400", "Error en la solicitud", errors);
        
        assertNotNull(apiError);
        assertEquals(HttpStatus.BAD_REQUEST, apiError.getStatus());
        assertEquals("400", apiError.getCodigoRespuesta());
        assertEquals("Error en la solicitud", apiError.getMessage());
        assertEquals(2, apiError.getErrors().size());
        assertEquals("Error 1", apiError.getErrors().get(0));
    }

    @Test
    public void testConstructorConUnSoloError() {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error interno", "Error específico");
        
        assertNotNull(apiError);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, apiError.getStatus());
        assertEquals("500", apiError.getCodigoRespuesta());
        assertEquals("Error interno", apiError.getMessage());
        assertEquals(1, apiError.getErrors().size());
        assertEquals("Error específico", apiError.getErrors().get(0));
    }

    @Test
    public void testSetYGetCodigoRespuesta() {
        ApiError apiError = new ApiError();
        apiError.setCodigoRespuesta("404");
        
        assertEquals("404", apiError.getCodigoRespuesta());
    }

    @Test
    public void testSetYGetStatus() {
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.NOT_FOUND);
        
        assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
    }

    @Test
    public void testSetYGetMessage() {
        ApiError apiError = new ApiError();
        apiError.setMessage("Recurso no encontrado");
        
        assertEquals("Recurso no encontrado", apiError.getMessage());
    }

    @Test
    public void testSetYGetErrors() {
        ApiError apiError = new ApiError();
        List<String> errors = Arrays.asList("Error 1", "Error 2", "Error 3");
        apiError.setErrors(errors);
        
        assertEquals(3, apiError.getErrors().size());
        assertEquals("Error 1", apiError.getErrors().get(0));
    }

    @Test
    public void testApiErrorConHttpStatusOK() {
        ApiError apiError = new ApiError(HttpStatus.OK, "200", "Operación exitosa", "Sin errores");
        
        assertEquals(HttpStatus.OK, apiError.getStatus());
        assertEquals("200", apiError.getCodigoRespuesta());
    }

    @Test
    public void testApiErrorConHttpStatusBadRequest() {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "400", "Solicitud incorrecta", "Parámetro inválido");
        
        assertEquals(HttpStatus.BAD_REQUEST, apiError.getStatus());
        assertEquals("400", apiError.getCodigoRespuesta());
    }

    @Test
    public void testApiErrorConHttpStatusUnauthorized() {
        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, "401", "No autorizado", "Token inválido");
        
        assertEquals(HttpStatus.UNAUTHORIZED, apiError.getStatus());
        assertEquals("401", apiError.getCodigoRespuesta());
    }
}
