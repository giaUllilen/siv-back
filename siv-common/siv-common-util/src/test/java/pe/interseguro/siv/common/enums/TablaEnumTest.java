package pe.interseguro.siv.common.enums;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Pruebas unitarias para el enum TablaEnum
 */
public class TablaEnumTest {

    @Test
    public void testTablaTipoDocumento() {
        assertEquals(Integer.valueOf(1), TablaEnum.TABLA_TIPO_DOCUMENTO.getCodigo());
        assertEquals("001", TablaEnum.TABLA_TIPO_DOCUMENTO.getCodigoTabla());
    }

    @Test
    public void testTablaGenero() {
        assertEquals(Integer.valueOf(2), TablaEnum.TABLA_GENERO.getCodigo());
        assertEquals("002", TablaEnum.TABLA_GENERO.getCodigoTabla());
    }

    @Test
    public void testTablaProfesion() {
        assertEquals(Integer.valueOf(3), TablaEnum.TABLA_PROFESION.getCodigo());
        assertEquals("003", TablaEnum.TABLA_PROFESION.getCodigoTabla());
    }

    @Test
    public void testTablaActividadEconomica() {
        assertEquals(Integer.valueOf(4), TablaEnum.TABLA_ACTIVIDAD_ECONOMICA.getCodigo());
        assertEquals("004", TablaEnum.TABLA_ACTIVIDAD_ECONOMICA.getCodigoTabla());
    }

    @Test
    public void testTablaFumador() {
        assertEquals(Integer.valueOf(5), TablaEnum.TABLA_FUMADOR.getCodigo());
        assertEquals("005", TablaEnum.TABLA_FUMADOR.getCodigoTabla());
    }

    @Test
    public void testTablaTipoRelacion() {
        assertEquals(Integer.valueOf(6), TablaEnum.TABLA_TIPO_RELACION.getCodigo());
        assertEquals("006", TablaEnum.TABLA_TIPO_RELACION.getCodigoTabla());
    }

    @Test
    public void testTablaEstadoGeneral() {
        assertEquals(Integer.valueOf(7), TablaEnum.TABLA_ESTADO_GENERAL.getCodigo());
        assertEquals("007", TablaEnum.TABLA_ESTADO_GENERAL.getCodigoTabla());
    }

    @Test
    public void testTablaParametroADN() {
        assertEquals(Integer.valueOf(8), TablaEnum.TABLA_PARAMETRO_ADN.getCodigo());
        assertEquals("008", TablaEnum.TABLA_PARAMETRO_ADN.getCodigoTabla());
    }

    @Test
    public void testTablaPerfil() {
        assertEquals(Integer.valueOf(9), TablaEnum.TABLA_PERFIL.getCodigo());
        assertEquals("009", TablaEnum.TABLA_PERFIL.getCodigoTabla());
    }

    @Test
    public void testTablaEstadoCivil() {
        assertEquals(Integer.valueOf(12), TablaEnum.TABLA_ESTADO_CIVIL.getCodigo());
        assertEquals("012", TablaEnum.TABLA_ESTADO_CIVIL.getCodigoTabla());
    }

    @Test
    public void testTablaNacionalidad() {
        assertEquals(Integer.valueOf(10), TablaEnum.TABLA_NACIONALIDAD.getCodigo());
        assertEquals("010", TablaEnum.TABLA_NACIONALIDAD.getCodigoTabla());
    }

    @Test
    public void testTablaTipoDireccion() {
        assertEquals(Integer.valueOf(13), TablaEnum.TABLA_TIPO_DIRECCION.getCodigo());
        assertEquals("013", TablaEnum.TABLA_TIPO_DIRECCION.getCodigoTabla());
    }

    @Test
    public void testCantidadTablas() {
        TablaEnum[] tablas = TablaEnum.values();
        assertTrue(tablas.length >= 12);
    }

    @Test
    public void testValueOf() {
        TablaEnum tabla = TablaEnum.valueOf("TABLA_TIPO_DOCUMENTO");
        assertNotNull(tabla);
        assertEquals(Integer.valueOf(1), tabla.getCodigo());
    }

    @Test
    public void testSetCodigo() {
        TablaEnum tabla = TablaEnum.TABLA_TIPO_DOCUMENTO;
        Integer codigoOriginal = tabla.getCodigo();
        
        tabla.setCodigo(999);
        assertEquals(Integer.valueOf(999), tabla.getCodigo());
        
        // Restaurar valor original
        tabla.setCodigo(codigoOriginal);
    }

    @Test
    public void testSetCodigoTabla() {
        TablaEnum tabla = TablaEnum.TABLA_TIPO_DOCUMENTO;
        String codigoTablaOriginal = tabla.getCodigoTabla();
        
        tabla.setCodigoTabla("999");
        assertEquals("999", tabla.getCodigoTabla());
        
        // Restaurar valor original
        tabla.setCodigoTabla(codigoTablaOriginal);
    }
}
