package pe.interseguro.siv.common.util;

import static org.junit.Assert.*;
import static pe.interseguro.siv.common.util.DateUtil.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

import org.junit.Test;

/**
 * Pruebas unitarias para DateUtil
 * Cumple con principio de aislamiento de TDD: no depende de recursos externos
 */
public class DateUtilTest {

    @Test
    public void testGetTimestampFromUtilDate_whenDateProvided_returnsTimestamp() {
        // Arrange
        Date date = new Date(1642512000000L); // 2022-01-18 12:00:00
        
        // Act
        Timestamp result = getTimestampFromUtilDate(date);
        
        // Assert
        assertNotNull(result);
        assertEquals(date.getTime(), result.getTime());
    }

    @Test
    public void testGetDateFromTimestamp_whenTimestampProvided_returnsDate() {
        // Arrange
        Timestamp timestamp = new Timestamp(1642512000000L);
        
        // Act
        Date result = getDateFromTimestamp(timestamp);
        
        // Assert
        assertNotNull(result);
        assertEquals(timestamp.getTime(), result.getTime());
    }

    @Test
    public void testGetNumberOfDays_whenValidDates_returnsCorrectDifference() {
        // Arrange
        Date dateIni = new Date(1642512000000L); // 2022-01-18
        Date dateEnd = new Date(1642857600000L); // 2022-01-22 (4 días después)
        
        // Act
        Long days = getNumberOfDays(dateIni, dateEnd);
        
        // Assert
        assertEquals(Long.valueOf(4L), days);
    }

    @Test
    public void testDateToString_whenValidDate_returnsFormattedString() throws Exception {
        // Arrange
        Date date = stringToDate("18/01/2022", FORMATO_DIA_DDMMYYYY);
        
        // Act
        String result = dateToString(date, FORMATO_DIA_DDMMYYYY);
        
        // Assert
        assertEquals("18/01/2022", result);
    }

    @Test
    public void testDateToString_whenNullDate_returnsNull() throws Exception {
        // Act
        String result = dateToString(null, FORMATO_DIA_DDMMYYYY);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void testStringToDate_whenValidString_returnsDate() {
        // Act
        Date result = stringToDate("18/01/2022", FORMATO_DIA_DDMMYYYY);
        
        // Assert
        assertNotNull(result);
    }

    @Test
    public void testStringToDate_whenNullString_returnsNull() {
        // Act
        Date result = stringToDate(null, FORMATO_DIA_DDMMYYYY);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void testStringToDate_whenInvalidFormat_returnsNull() {
        // Act
        Date result = stringToDate("invalid-date", FORMATO_DIA_DDMMYYYY);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void testStringToTimestamp_whenValidString_returnsTimestamp() {
        // Act
        Timestamp result = stringToTimestamp("18/01/2022", FORMATO_DIA_DDMMYYYY);
        
        // Assert
        assertNotNull(result);
    }

    @Test
    public void testStringToTimestamp_whenNullString_returnsNull() {
        // Act
        Timestamp result = stringToTimestamp(null, FORMATO_DIA_DDMMYYYY);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void testTimestampToString_whenValidTimestamp_returnsFormattedString() {
        // Arrange
        Timestamp timestamp = stringToTimestamp("18/01/2022", FORMATO_DIA_DDMMYYYY);
        
        // Act
        String result = timestampToString(timestamp, FORMATO_DIA_DDMMYYYY);
        
        // Assert
        assertEquals("18/01/2022", result);
    }

    @Test
    public void testTimestampToString_whenNullTimestamp_returnsNull() {
        // Act
        String result = timestampToString(null, FORMATO_DIA_DDMMYYYY);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void testGetDateWithFormat_whenValidInput_returnsConvertedFormat() throws ParseException {
        // Act
        String result = getDateWithFormat("18/01/2022", FORMATO_DIA_DDMMYYYY, FORMATO_DIA_YYYYMMDD);
        
        // Assert
        assertEquals("20220118", result);
    }

    @Test
    public void testGetAgePerson_whenDateProvided_returnsCorrectAge() {
        // Arrange - Fecha de nacimiento hace 30 años
        String fechaNacimiento = "18/01/1994";
        
        // Act
        Integer edad = getAgePerson(fechaNacimiento, FORMATO_DIA_DDMMYYYY);
        
        // Assert
        assertNotNull(edad);
        // La persona nacida en 1994 debe tener entre 30 y 33 años (considerando año 2024-2027)
        assertTrue(edad >= 30 && edad <= 33);
    }

    @Test
    public void testGetMinusMonths_whenDateProvided_returnsDateMinusMonths() {
        // Arrange
        Date date = stringToDate("18/06/2022", FORMATO_DIA_DDMMYYYY);
        
        // Act
        Date result = getMinusMonths(date, 3);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.before(date));
    }
}

