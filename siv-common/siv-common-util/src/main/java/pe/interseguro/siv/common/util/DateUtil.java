package pe.interseguro.siv.common.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;

/**
 * Utilitarios Fechas y Horas
 * 
 * @author ti-is
 *
 */
public class DateUtil {
	
	public static final String FORMATO_DIA_DDMMYYYY = "dd/MM/yyyy";
	public static final String FORMATO_DIA_DDMMYYYY_HHMMSS = "dd/MM/yyyy HH:mm:ss";
	public static final String FORMATO_DIA_DDMMYYYY_T_HHMMSS = "yyyy-MM-dd'T'HH:mm:ss";
	public static final String FORMATO_DIA_DDMMYYYY_M_HHMMSS = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMATO_DIA_YYYYMMDD = "yyyyMMdd";
	public static final String FORMATO_DIA_YYYYMMDD2 = "yyyy-MM-dd";
	public static final String FORMATO_DIA_YYYY = "yyyy";
	public static final String FORMATO_DIA_MMMM = "MMMM";
	public static final String FORMATO_DIA_DDMMYYYYHHMMSS = "ddMMyyyyHHmmss";
	public static final String FORMATO_DIA_MMYYYY = "MM/yyyy";	
	/**
	 * Convertir Timestamp desde un Date
	 * @param date
	 * @return
	 */
	public static Timestamp getTimestampFromUtilDate(Date date){						
		return new Timestamp(date.getTime());
	}
	
	/**
	 * Convertir Date desde un Timestamp
	 * @param timestamp
	 * @return
	 */
	public static Date getDateFromTimestamp(Timestamp timestamp){
		return new Date(timestamp.getTime());
	}
	
	/**
	 * Obtener número de días desde un rango de fechas (Timestamp)
	 * @param tsIni
	 * @param tsEnd
	 * @return
	 */
	public static Long getNumberOfDays(Timestamp tsIni, Timestamp tsEnd){
		return getNumberOfDays(getDateFromTimestamp(tsIni), getDateFromTimestamp(tsEnd));
	}
	
	/**
	 * Obtener número de días desde un rango de fechas (Date)
	 * @param dateIni
	 * @param dateEnd
	 * @return
	 */
	public static Long getNumberOfDays(Date dateIni, Date dateEnd){
		return ( (dateEnd.getTime() - dateIni.getTime()) / (1000 * 60 * 60 * 24) );
	}
		
	/**
	 * Converitr Date a String con formato parametrizado 
	 * @param date
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public static String dateToString(Date date,String format) throws Exception {
		if(date == null) {
			return null;
		}
		return new SimpleDateFormat(format, new Locale("es_PE", "PE")).format(date);
	}
			
	/**
	 * Convertir Timestamp a String con formato parametrizado
	 * @param t
	 * @param format
	 * @return
	 */
	public static String timestampToString(Timestamp t, String format){
		if (t == null) {
			return null;
		}
		return new SimpleDateFormat(format).format( new Date(t.getTime()) );
	}
		
	/**
	 * Convertir String a Date con formato parametrizado
	 * @param date
	 * @param format
	 * @return
	 */
	public static Date stringToDate(String date, String format){
		if (date == null) {
			return null;
		}

		try {
			return new SimpleDateFormat(format).parse(date);
		} catch (ParseException e) {
			return null;
		} 		
	}		
	
	/**
	 * Convertir String a Timestamp con formato parametrizado
	 * @param date
	 * @param format
	 * @return
	 */
	public static Timestamp stringToTimestamp(String date, String format){
		if (date == null) {
			return null;
		}

		try {
			return new Timestamp( new SimpleDateFormat(format).parse(date).getTime() );
		} catch (ParseException e) {
			return null;
		} 		
	}
	
	/**
	 * Obtener fecha en cadena de texto con un formatoFecha de entrada y otra de salida
	 * @param date
	 * @param inputDate
	 * @param outputDate
	 * @return
	 * @throws ParseException
	 */
	public static String getDateWithFormat(String date, String inputDate, String outputDate) throws ParseException {
		return new SimpleDateFormat(outputDate).format( new SimpleDateFormat(inputDate).parse(date) );
	}
	
	/**
	 * Obtener la edad de una persona
	 * @param fecha
	 * @param formatoFecha
	 * @return
	 */
	public static Integer getAgePerson(String date, String format) {
		return 
			Years.yearsBetween( 
				LocalDate.parse(date, DateTimeFormat.forPattern(format)), 
				new LocalDate()
			).getYears();
	}
	
	/**
	 * Convertir date a XMLGregorianCalendar
	 * @param date
	 * @return
	 * @throws DatatypeConfigurationException
	 */
	public static XMLGregorianCalendar dateToXMLGregorianCalendar(Date date) throws DatatypeConfigurationException {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		return DatatypeFactory.newInstance().newXMLGregorianCalendar( cal );
	}
	
	/**
	 * Convertir XMLGregorianCalendar a date
	 * @param date
	 * @return
	 */
	public static Date xmlGregorianCalendarToDate(XMLGregorianCalendar date) {
		return date.toGregorianCalendar().getTime();
	}
	
	/**
	 * Restar meses a partir de una fecha y la cantidad de meses que se quiere restar
	 * @param date
	 * @param countMonth
	 * @return
	 */
	public static Date getMinusMonths( Date date, Integer countMonth ) { 
		Calendar c = Calendar.getInstance(); 
		c.setTime(date); 
		c.add(Calendar.MONTH, -countMonth);
		return c.getTime();
	}
	
	
}
