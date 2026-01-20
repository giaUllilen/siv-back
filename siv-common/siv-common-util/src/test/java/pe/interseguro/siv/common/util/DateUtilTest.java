package pe.interseguro.siv.common.util;

import org.junit.Test;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Pruebas unitarias para la clase DateUtil
 */
public class DateUtilTest {

    @Test
    public void testGetTimestampFromUtilDate() {
        Date fecha = new Date();
        Timestamp resultado = DateUtil.getTimestampFromUtilDate(fecha);

        assertNotNull(resultado);
        assertEquals(fecha.getTime(), resultado.getTime());
    }

    @Test
    public void testGetDateFromTimestamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Date resultado = DateUtil.getDateFromTimestamp(timestamp);

        assertNotNull(resultado);
        assertEquals(timestamp.getTime(), resultado.getTime());
    }

    @Test
    public void testGetNumberOfDaysConTimestamp() {
        Calendar cal = Calendar.getInstance();
        Timestamp tsInicio = new Timestamp(cal.getTimeInMillis());
        
        cal.add(Calendar.DAY_OF_MONTH, 5);
        Timestamp tsFin = new Timestamp(cal.getTimeInMillis());

        Long dias = DateUtil.getNumberOfDays(tsInicio, tsFin);

        assertEquals(Long.valueOf(5L), dias);
    }

    @Test
    public void testGetNumberOfDaysConDate() {
        Calendar cal = Calendar.getInstance();
        Date fechaInicio = cal.getTime();
        
        cal.add(Calendar.DAY_OF_MONTH, 10);
        Date fechaFin = cal.getTime();

        Long dias = DateUtil.getNumberOfDays(fechaInicio, fechaFin);

        assertEquals(Long.valueOf(10L), dias);
    }

    @Test
    public void testDateToStringConFormatoDDMMYYYY() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.set(2024, Calendar.JANUARY, 15);
        Date fecha = cal.getTime();

        String resultado = DateUtil.dateToString(fecha, DateUtil.FORMATO_DIA_DDMMYYYY);

        assertNotNull(resultado);
        assertTrue(resultado.contains("15"));
        assertTrue(resultado.contains("01"));
        assertTrue(resultado.contains("2024"));
    }

    @Test
    public void testDateToStringConNull() throws Exception {
        String resultado = DateUtil.dateToString(null, DateUtil.FORMATO_DIA_DDMMYYYY);

        assertNull(resultado);
    }

    @Test
    public void testTimestampToString() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String resultado = DateUtil.timestampToString(timestamp, DateUtil.FORMATO_DIA_YYYYMMDD);

        assertNotNull(resultado);
        assertEquals(8, resultado.length());
    }

    @Test
    public void testTimestampToStringConNull() {
        String resultado = DateUtil.timestampToString(null, DateUtil.FORMATO_DIA_YYYYMMDD);

        assertNull(resultado);
    }

    @Test
    public void testStringToDate() {
        String fechaStr = "15/01/2024";
        Date resultado = DateUtil.stringToDate(fechaStr, DateUtil.FORMATO_DIA_DDMMYYYY);

        assertNotNull(resultado);
    }

    @Test
    public void testStringToDateConNull() {
        Date resultado = DateUtil.stringToDate(null, DateUtil.FORMATO_DIA_DDMMYYYY);

        assertNull(resultado);
    }

    @Test
    public void testStringToDateConFormatoInvalido() {
        String fechaStr = "fecha-invalida";
        Date resultado = DateUtil.stringToDate(fechaStr, DateUtil.FORMATO_DIA_DDMMYYYY);

        assertNull(resultado);
    }

    @Test
    public void testStringToTimestamp() {
        String fechaStr = "15/01/2024";
        Timestamp resultado = DateUtil.stringToTimestamp(fechaStr, DateUtil.FORMATO_DIA_DDMMYYYY);

        assertNotNull(resultado);
    }

    @Test
    public void testStringToTimestampConNull() {
        Timestamp resultado = DateUtil.stringToTimestamp(null, DateUtil.FORMATO_DIA_DDMMYYYY);

        assertNull(resultado);
    }

    @Test
    public void testStringToTimestampConFormatoInvalido() {
        String fechaStr = "fecha-invalida";
        Timestamp resultado = DateUtil.stringToTimestamp(fechaStr, DateUtil.FORMATO_DIA_DDMMYYYY);

        assertNull(resultado);
    }

    @Test
    public void testGetDateWithFormat() throws ParseException {
        String fechaStr = "15/01/2024";
        String resultado = DateUtil.getDateWithFormat(
            fechaStr, 
            DateUtil.FORMATO_DIA_DDMMYYYY, 
            DateUtil.FORMATO_DIA_YYYYMMDD
        );

        assertNotNull(resultado);
        assertEquals("20240115", resultado);
    }

    @Test
    public void testGetAgePerson() {
        // Fecha de hace 25 años
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -25);
        String fechaNacimiento = String.format("%02d/%02d/%d", 
            cal.get(Calendar.DAY_OF_MONTH),
            cal.get(Calendar.MONTH) + 1,
            cal.get(Calendar.YEAR)
        );

        Integer edad = DateUtil.getAgePerson(fechaNacimiento, DateUtil.FORMATO_DIA_DDMMYYYY);

        assertTrue(edad >= 24 && edad <= 25);
    }

    @Test
    public void testDateToXMLGregorianCalendar() throws DatatypeConfigurationException {
        Date fecha = new Date();
        XMLGregorianCalendar resultado = DateUtil.dateToXMLGregorianCalendar(fecha);

        assertNotNull(resultado);
    }

    @Test
    public void testXmlGregorianCalendarToDate() throws DatatypeConfigurationException {
        Date fechaOriginal = new Date();
        XMLGregorianCalendar xmlCal = DateUtil.dateToXMLGregorianCalendar(fechaOriginal);
        Date resultado = DateUtil.xmlGregorianCalendarToDate(xmlCal);

        assertNotNull(resultado);
    }

    @Test
    public void testGetMinusMonths() {
        Calendar cal = Calendar.getInstance();
        cal.set(2024, Calendar.JUNE, 15);
        Date fecha = cal.getTime();

        Date resultado = DateUtil.getMinusMonths(fecha, 3);

        assertNotNull(resultado);
        Calendar calResultado = Calendar.getInstance();
        calResultado.setTime(resultado);
        
        assertEquals(Calendar.MARCH, calResultado.get(Calendar.MONTH));
    }

    @Test
    public void testGetMinusMonthsConUnMes() {
        Calendar cal = Calendar.getInstance();
        cal.set(2024, Calendar.FEBRUARY, 15);
        Date fecha = cal.getTime();

        Date resultado = DateUtil.getMinusMonths(fecha, 1);

        assertNotNull(resultado);
        Calendar calResultado = Calendar.getInstance();
        calResultado.setTime(resultado);
        
        assertEquals(Calendar.JANUARY, calResultado.get(Calendar.MONTH));
    }
}
