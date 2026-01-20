package pe.interseguro.siv.common.util;

import org.junit.Test;
import org.springframework.context.MessageSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la clase Utilitarios
 */
public class UtilitariosTest {

    @Test
    public void testObtenerMensaje() {
        MessageSource messageSource = mock(MessageSource.class);
        when(messageSource.getMessage("test.message", null, Locale.getDefault()))
                .thenReturn("Mensaje de prueba");

        String resultado = Utilitarios.obtenerMensaje(messageSource, "test.message");

        assertEquals("Mensaje de prueba", resultado);
        verify(messageSource).getMessage("test.message", null, Locale.getDefault());
    }

    @Test
    public void testObtenerMensajeConParametros() {
        MessageSource messageSource = mock(MessageSource.class);
        Object[] params = {"param1", "param2"};
        when(messageSource.getMessage("test.message", params, Locale.getDefault()))
                .thenReturn("Mensaje con parámetros");

        String resultado = Utilitarios.obtenerMensaje(messageSource, params, "test.message");

        assertEquals("Mensaje con parámetros", resultado);
        verify(messageSource).getMessage("test.message", params, Locale.getDefault());
    }

    @Test
    public void testObtenerBigDecimalRedondeado() {
        BigDecimal numero = new BigDecimal("10.12345");
        BigDecimal resultado = Utilitarios.obtenerBigDecimalRedondeado(numero, 2);

        assertEquals(new BigDecimal("10.12"), resultado);
    }

    @Test
    public void testObtenerBigDecimalRedondeadoHaciaArriba() {
        BigDecimal numero = new BigDecimal("10.12567");
        BigDecimal resultado = Utilitarios.obtenerBigDecimalRedondeado(numero, 2);

        assertEquals(new BigDecimal("10.13"), resultado);
    }

    @Test
    public void testEsVacioConTextoVacio() {
        assertTrue(Utilitarios.esVacio(""));
        assertTrue(Utilitarios.esVacio("   "));
        assertTrue(Utilitarios.esVacio(null));
    }

    @Test
    public void testEsVacioConTextoNoVacio() {
        assertFalse(Utilitarios.esVacio("texto"));
        assertFalse(Utilitarios.esVacio("  texto  "));
    }

    @Test
    public void testConvertirObjetoAList() {
        List<String> lista = new ArrayList<String>();
        lista.add("elemento1");
        lista.add("elemento2");

        List<String> resultado = Utilitarios.convertirObjetoAList(lista);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("elemento1", resultado.get(0));
    }

    @Test
    public void testConvertirObjetoADate() {
        Date fecha = new Date();
        Date resultado = Utilitarios.convertirObjetoADate(fecha);

        assertNotNull(resultado);
        assertEquals(fecha, resultado);
    }

    @Test
    public void testEsNumeroConNumeroValido() {
        assertTrue(Utilitarios.esNumero("12345"));
        assertTrue(Utilitarios.esNumero("0"));
        assertTrue(Utilitarios.esNumero(""));
    }

    @Test
    public void testEsNumeroConNumeroInvalido() {
        assertFalse(Utilitarios.esNumero("123abc"));
        assertFalse(Utilitarios.esNumero("abc"));
        assertFalse(Utilitarios.esNumero(null));
    }

    @Test
    public void testEsCEValido() {
        assertTrue(Utilitarios.esCEValido("N123456"));
        assertTrue(Utilitarios.esCEValido("123456"));
        assertTrue(Utilitarios.esCEValido("N-123-456"));
    }

    @Test
    public void testEsCEInvalido() {
        assertFalse(Utilitarios.esCEValido("ABC123"));
        assertFalse(Utilitarios.esCEValido("N@123"));
    }

    @Test
    public void testEncriptarTexto() {
        String texto = "12345678";
        String resultado = Utilitarios.encriptarTexto(texto, 3, "*");

        assertEquals("123*****", resultado);
    }

    @Test
    public void testFormatoMilesConDecimal() {
        Double valor = 1234567.89;
        String resultado = Utilitarios.formatoMiles(valor, Utilitarios.FORMATO_MILES_CON_DECIMAL);

        assertNotNull(resultado);
        assertTrue(resultado.contains("1"));
    }

    @Test
    public void testFormatoMilesConValorNull() {
        String resultado = Utilitarios.formatoMiles(null, Utilitarios.FORMATO_MILES_CON_DECIMAL);

        assertNull(resultado);
    }

    @Test
    public void testValorStringConValorNoNull() {
        assertEquals("test", Utilitarios.valorString("test"));
        assertEquals("123", Utilitarios.valorString(123));
    }

    @Test
    public void testValorStringConValorNull() {
        assertEquals(Constantes.VALOR_VACIO, Utilitarios.valorString(null));
    }

    @Test
    public void testValorStringBooleanTrue() {
        assertEquals("1", Utilitarios.valorStringBoolean(true));
    }

    @Test
    public void testValorStringBooleanFalse() {
        assertEquals("0", Utilitarios.valorStringBoolean(false));
    }

    @Test
    public void testValorStringBooleanNull() {
        assertEquals(Constantes.VALOR_VACIO, Utilitarios.valorStringBoolean(null));
    }

    @Test
    public void testCheckOpcionIguales() {
        assertTrue(Utilitarios.checkOpcion("A", "A"));
    }

    @Test
    public void testCheckOpcionDiferentes() {
        assertFalse(Utilitarios.checkOpcion("A", "B"));
    }

    @Test
    public void testNombresCompletosConPersonaNatural() {
        String resultado = Utilitarios.nombresCompletos("Juan", "Perez", "Garcia", "");

        assertEquals("JUAN PEREZ GARCIA", resultado);
    }

    @Test
    public void testNombresCompletosConRazonSocial() {
        String resultado = Utilitarios.nombresCompletos("", "", "", "Empresa SAC");

        assertEquals("EMPRESA SAC", resultado);
    }

    @Test
    public void testNombresCompletosSinApellidoMaterno() {
        String resultado = Utilitarios.nombresCompletos("Juan", "Perez", "", "");

        assertEquals("JUAN PEREZ ", resultado);
    }

    @Test
    public void testNombreMesEnero() {
        assertEquals("Enero", Utilitarios.nombreMes("01"));
    }

    @Test
    public void testNombreMesDiciembre() {
        assertEquals("Diciembre", Utilitarios.nombreMes("12"));
    }

    @Test
    public void testNombreMesInvalido() {
        assertEquals("", Utilitarios.nombreMes("13"));
    }

    @Test
    public void testToCamelCase() {
        String resultado = Utilitarios.toCamelCase("hola mundo java");

        assertEquals("Hola Mundo Java", resultado);
    }

    @Test
    public void testToCamelCaseConNull() {
        assertNull(Utilitarios.toCamelCase(null));
    }

    @Test
    public void testQuitaEspacios() {
        String resultado = Utilitarios.quitaEspacios("  texto   con    espacios  ");

        assertEquals("texto con espacios", resultado);
    }

    @Test
    public void testTrazaLog() {
        String resultado = Utilitarios.trazaLog();

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
    }

    @Test
    public void testDateNow() {
        String resultado = Utilitarios.dateNow();

        assertNotNull(resultado);
        assertTrue(resultado.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}:\\d{3}"));
    }

    @Test
    public void testDateNowShort() {
        String resultado = Utilitarios.dateNowShort();

        assertNotNull(resultado);
        assertTrue(resultado.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
    }
}
