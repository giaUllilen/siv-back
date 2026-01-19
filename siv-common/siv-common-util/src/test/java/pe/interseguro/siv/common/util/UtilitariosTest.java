package pe.interseguro.siv.common.util;

import static org.junit.Assert.*;
import static pe.interseguro.siv.common.util.Utilitarios.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

/**
 * Pruebas unitarias para Utilitarios
 * Cumple con principio de aislamiento de TDD: no depende de recursos externos
 */
public class UtilitariosTest {

    @Test
    public void testObtenerBigDecimalRedondeado_whenValidNumber_returnsRounded() {
        // Arrange
        BigDecimal numero = new BigDecimal("123.456789");
        
        // Act
        BigDecimal result = obtenerBigDecimalRedondeado(numero, 2);
        
        // Assert
        assertEquals(new BigDecimal("123.46"), result);
    }

    @Test
    public void testEsVacio_whenEmptyString_returnsTrue() {
        // Act & Assert
        assertTrue(esVacio(""));
        assertTrue(esVacio("   "));
        assertTrue(esVacio(null));
    }

    @Test
    public void testEsVacio_whenNonEmptyString_returnsFalse() {
        // Act & Assert
        assertFalse(esVacio("texto"));
        assertFalse(esVacio("  texto  "));
    }

    @Test
    public void testEsNumero_whenNumericString_returnsTrue() {
        // Act & Assert
        assertTrue(esNumero("123"));
        assertTrue(esNumero("0"));
        assertTrue(esNumero("999999"));
    }

    @Test
    public void testEsNumero_whenNonNumericString_returnsFalse() {
        // Act & Assert
        assertFalse(esNumero("abc"));
        assertFalse(esNumero("12.34"));
        assertFalse(esNumero("12a3"));
        assertFalse(esNumero(null));
    }

    @Test
    public void testEsCEValido_whenValidCE_returnsTrue() {
        // Act & Assert
        assertTrue(esCEValido("N123456"));
        assertTrue(esCEValido("123-456"));
        assertTrue(esCEValido("N-123"));
    }

    @Test
    public void testEsCEValido_whenInvalidCE_returnsFalse() {
        // Act & Assert
        assertFalse(esCEValido("ABC123"));
        assertFalse(esCEValido("N@123"));
    }

    @Test
    public void testEncriptarTexto_whenValidInput_returnsEncrypted() {
        // Arrange
        String texto = "1234567890";
        
        // Act
        String result = encriptarTexto(texto, 4, "*");
        
        // Assert
        assertEquals("1234******", result);
    }

    @Test
    public void testFormatoMiles_whenValidNumber_returnsFormatted() {
        // Arrange
        Double valor = 123456.789;
        
        // Act
        String result = formatoMiles(valor, FORMATO_MILES_CON_DECIMAL);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.contains("123"));
        assertTrue(result.contains("456"));
    }

    @Test
    public void testFormatoMiles_whenNullNumber_returnsNull() {
        // Act
        String result = formatoMiles(null, FORMATO_MILES_CON_DECIMAL);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void testValorString_whenNullValue_returnsEmpty() {
        // Act
        String result = valorString(null);
        
        // Assert
        assertEquals("", result);
    }

    @Test
    public void testValorString_whenValidValue_returnsString() {
        // Act
        String result = valorString("texto");
        
        // Assert
        assertEquals("texto", result);
    }

    @Test
    public void testValorStringBoolean_whenTrue_returnsOne() {
        // Act
        String result = valorStringBoolean(true);
        
        // Assert
        assertEquals("1", result);
    }

    @Test
    public void testValorStringBoolean_whenFalse_returnsZero() {
        // Act
        String result = valorStringBoolean(false);
        
        // Assert
        assertEquals("0", result);
    }

    @Test
    public void testValorStringBoolean_whenNull_returnsEmpty() {
        // Act
        String result = valorStringBoolean(null);
        
        // Assert
        assertEquals("", result);
    }

    @Test
    public void testCheckOpcion_whenCodesMatch_returnsTrue() {
        // Act
        Boolean result = checkOpcion("01", "01");
        
        // Assert
        assertTrue(result);
    }

    @Test
    public void testCheckOpcion_whenCodesDoNotMatch_returnsFalse() {
        // Act
        Boolean result = checkOpcion("01", "02");
        
        // Assert
        assertFalse(result);
    }

    @Test
    public void testNombresCompletos_whenPersonaNatural_returnsFullName() {
        // Act
        String result = nombresCompletos("Juan", "Perez", "Garcia", null);
        
        // Assert
        assertEquals("JUAN PEREZ GARCIA", result);
    }

    @Test
    public void testNombresCompletos_whenPersonaJuridica_returnsRazonSocial() {
        // Act
        String result = nombresCompletos(null, null, null, "Empresa SAC");
        
        // Assert
        assertEquals("EMPRESA SAC", result);
    }

    @Test
    public void testNombreMes_whenValidMonth_returnsMonthName() {
        // Act & Assert
        assertEquals("Enero", nombreMes("01"));
        assertEquals("Febrero", nombreMes("02"));
        assertEquals("Marzo", nombreMes("03"));
        assertEquals("Diciembre", nombreMes("12"));
    }

    @Test
    public void testNombreMes_whenInvalidMonth_returnsEmpty() {
        // Act
        String result = nombreMes("13");
        
        // Assert
        assertEquals("", result);
    }

    @Test
    public void testToCamelCase_whenLowerCaseString_returnsCamelCase() {
        // Act
        String result = toCamelCase("juan perez garcia");
        
        // Assert
        assertEquals("Juan Perez Garcia", result);
    }

    @Test
    public void testToCamelCase_whenNullString_returnsNull() {
        // Act
        String result = toCamelCase(null);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void testQuitaEspacios_whenMultipleSpaces_returnsSingleSpace() {
        // Act
        String result = quitaEspacios("texto   con    espacios");
        
        // Assert
        assertEquals("texto con espacios", result);
    }

    @Test
    public void testConvertirObjetoAList_whenListProvided_returnsList() {
        // Arrange
        List<String> originalList = new ArrayList<>();
        originalList.add("item1");
        originalList.add("item2");
        
        // Act
        List<String> result = convertirObjetoAList(originalList);
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("item1", result.get(0));
    }

    @Test
    public void testConvertirObjetoADate_whenDateProvided_returnsDate() {
        // Arrange
        Date originalDate = new Date();
        
        // Act
        Date result = convertirObjetoADate(originalDate);
        
        // Assert
        assertNotNull(result);
        assertEquals(originalDate, result);
    }

    @Test
    public void testTrazaLog_whenCalled_returnsNonEmptyString() {
        // Act
        String result = trazaLog();
        
        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.matches("\\d+"));
    }

    @Test
    public void testDateNow_whenCalled_returnsFormattedDate() {
        // Act
        String result = dateNow();
        
        // Assert
        assertNotNull(result);
        assertTrue(result.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}:\\d{3}"));
    }

    @Test
    public void testDateNowShort_whenCalled_returnsFormattedDate() {
        // Act
        String result = dateNowShort();
        
        // Assert
        assertNotNull(result);
        assertTrue(result.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
    }
}

