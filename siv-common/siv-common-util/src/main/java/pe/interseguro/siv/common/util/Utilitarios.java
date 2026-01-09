package pe.interseguro.siv.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.springframework.context.MessageSource;

/**
 * Utilitarios de métodos reutilizables
 *
 * @author ti-is
 */
public class Utilitarios {

	public static final String FORMATO_MILES_CON_DECIMAL = "###,###,##0.00";
	public static final String FORMATO_MILES_SIN_DECIMAL = "###,###,###.##";
	public static final String FORMATO_NUMERO_SIN_DECIMAL = "#########.##";
	/**
	 * Obtener mensaje i18n con Locale.Default
	 *
	 * @param messageSource
	 * @param mensaje
	 * @return
	 */
	public static String obtenerMensaje(MessageSource messageSource, String mensaje) {
		return messageSource.getMessage(mensaje, null, Locale.getDefault());
	}

	/**
	 * Obtener mensaje i18n con Locale.Default y parametros
	 *
	 * @param messageSource
	 * @param param
	 * @param mensaje
	 * @return
	 */
	public static String obtenerMensaje(MessageSource messageSource, Object[] param, String mensaje) {
		return messageSource.getMessage(mensaje, param, Locale.getDefault());
	}

	/**
	 * Obtener un número redondeado hacia arriba pasando la cantidad de decimales que necesita
	 * @param numero
	 * @param cantidadDecimales
	 * @return
	 */
	public static BigDecimal obtenerBigDecimalRedondeado(BigDecimal numero, Integer cantidadDecimales) {
		return numero.setScale(cantidadDecimales, RoundingMode.HALF_UP);
	}

	/**
	 * Validar si la cadena de texto es vacía así se obtenga con espacios o un valor NULL
	 * @param texto
	 * @return
	 */
	public static Boolean esVacio(String texto) {
		return Objects.toString(texto, "").trim().equals("");
	}

	/**
	 * Convertir un Objeto a List
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends List<?>> T convertirObjetoAList(Object obj) {
	    return (T) obj;
	}

	/**
	 * Convertir un Objeto a Date
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Date> T convertirObjetoADate(Object obj) {
	    return (T) obj;
	}

	/**
	 * Validar si los digitos son númericos
	 * @param texto
	 * @return
	 */
	public static Boolean esNumero(String texto) {
		return texto != null && texto.matches("(^$|[0-9]+)");
	}

	/**
	 * Validar si los digitos son númericos
	 * @param texto
	 * @return
	 */
	public static Boolean esCEValido(String texto) {
		return texto.matches("(^[N0-9-]*$)");
	}

	/**
	 * Encriptar texto por la cantidad indicada y su caracter
	 *
	 * @param texto
	 * @param cantidad
	 * @param caracter
	 * @return
	 */
	public static String encriptarTexto(String texto, Integer cantidad, String caracter) {

		String text_final = "";

		for( int i =0; i < texto.length(); i++ ) {
			if( (i +1) <= cantidad ) {
				text_final += texto.charAt(i);
			} else {
				text_final += "*";
			}
		}

		return text_final;
	}

	/**
	 * Convertir a String con formato parametrizado
	 * @param valor
	 * @param formato
	 * @return
	 * @throws Exception
	 */
	public static String formatoMiles(Double valor,String formato)  {
		if(valor == null) {
			return null;
		}

		DecimalFormat myFormatter = new DecimalFormat(formato);
		myFormatter = new DecimalFormat(formato, DecimalFormatSymbols.getInstance(new Locale("es","PE")));
		String output = myFormatter.format(valor);

		return output;
	}

	public static String valorString(Object valor) {
		String result = Constantes.VALOR_VACIO;

		if(valor != null) {
			result = valor.toString();
		}
		return result;
	}

	public static String valorStringBoolean(Boolean valor) {
		String result = Constantes.VALOR_VACIO;

		if(valor != null) {
			if(valor){
				result = "1";
			}else {
				result = "0";
			}
		}
		return result;
	}

	public static Boolean checkOpcion(String codigo, String comparar) {
		Boolean check = false;
		if(codigo.equals(comparar)) {
			check = true;
		}
		return check;
	}

	public static String decrypt(String encrypted) {
		String strData="";
		byte[] encryptedBytes = Base64.decodeBase64(encrypted);

		try {
			SecretKeySpec skeyspec=new SecretKeySpec(Constantes.KEY_ENCRYPT.getBytes(),"Blowfish");
			Cipher cipher=Cipher.getInstance("Blowfish");
			cipher.init(Cipher.DECRYPT_MODE, skeyspec);
			byte[] decrypted=cipher.doFinal(encryptedBytes);
			strData=new String(decrypted);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return strData;

	}

	public static String nombresCompletos(String nombres, String apellidoPaterno, String apellidoMaterno, String razonSocial) {
		StringBuilder sb = new StringBuilder();

		if(!valorString(razonSocial).equals(Constantes.VALOR_VACIO)) {
			sb.append(razonSocial);
		}else{
			sb.append(valorString(nombres)).append(" ");
			sb.append(valorString(apellidoPaterno)).append(" ");

			if(!valorString(apellidoMaterno).equals(Constantes.VALOR_VACIO)) {
				sb.append(apellidoMaterno);
			}
		}

		return sb.toString().toUpperCase();
	}

	public static String nombreMes(String numeroMes) {
		String nombreMes = "";
		switch(numeroMes){
			case "01": nombreMes = "Enero";
			break;
			case "02": nombreMes = "Febrero";
			break;
			case "03": nombreMes = "Marzo";
			break;
			case "04": nombreMes = "Abril";
			break;
			case "05": nombreMes = "Mayo";
			break;
			case "06": nombreMes = "Junio";
			break;
			case "07": nombreMes = "Julio";
			break;
			case "08": nombreMes = "Agosto";
			break;
			case "09": nombreMes = "Septiembre";
			break;
			case "10": nombreMes = "Octubre";
			break;
			case "11": nombreMes = "Noviembre";
			break;
			case "12": nombreMes = "Diciembre";
			break;
		}
		return nombreMes;
	}

	public static String toCamelCase(final String init) {
	    if (init == null)
	        return null;

	    final StringBuilder ret = new StringBuilder(init.length());

	    for (final String word : init.split(" ")) {
	        if (!word.isEmpty()) {
	            ret.append(Character.toUpperCase(word.charAt(0)));
	            ret.append(word.substring(1).toLowerCase());
	        }
	        if (!(ret.length() == init.length()))
	            ret.append(" ");
	    }

	    return ret.toString();
	}

	public static String quitaEspacios(String texto) {
        java.util.StringTokenizer tokens = new java.util.StringTokenizer(texto);
        StringBuilder buff = new StringBuilder();
        while (tokens.hasMoreTokens()) {
            buff.append(" ").append(tokens.nextToken());
        }
        return buff.toString().trim();
    }

	public static String trazaLog() {
		LocalDateTime ldt = LocalDateTime.now();
		ZonedDateTime zdt = ldt.atZone(ZoneId.of("America/Los_Angeles"));
		String traza = String.valueOf(zdt.toInstant().toEpochMilli());

		return traza;
	}
	public static String dateNow() {
		LocalDateTime ldt = LocalDateTime.now();
		ZonedDateTime ldtZoned = ldt.atZone(ZoneId.systemDefault());
		ZonedDateTime utcZoned = ldtZoned.withZoneSameInstant(ZoneId.of("-5"));
		final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");
		String traza = utcZoned.format(dateFormat);
		return traza;
	}
	public static String dateNowShort() {
		LocalDateTime ldt = LocalDateTime.now();
		ZonedDateTime ldtZoned = ldt.atZone(ZoneId.systemDefault());
		ZonedDateTime utcZoned = ldtZoned.withZoneSameInstant(ZoneId.of("-5"));
		final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String traza = utcZoned.format(dateFormat);
		return traza;
	}

	public static String encrypt2(String text) {
		String strData = "";
		String CHARSET_NAME = "UTF-8";
		String CIPHER_INSTANCE = "Blowfish/CBC/PKCS5Padding";
		IvParameterSpec IV_SPEC = new IvParameterSpec(new byte[8]);
		try {
			SecretKeySpec skeyspec = new SecretKeySpec(Constantes.KEY_ENCRYPT.getBytes(CHARSET_NAME), "Blowfish");
			Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE);
			cipher.init(Cipher.ENCRYPT_MODE, skeyspec, IV_SPEC);
			byte[] encrypted = cipher.doFinal(text.getBytes(CHARSET_NAME));
			strData = Base64.encodeBase64String(encrypted);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strData;
	}

	public static String decrypt2(String encrypted) {
		String strData = "";
		String CHARSET_NAME = "UTF-8";
		String CIPHER_INSTANCE = "Blowfish/CBC/PKCS5Padding";
		IvParameterSpec IV_SPEC = new IvParameterSpec(new byte[8]);
		byte[] encryptedBytes = Base64.decodeBase64(encrypted);
		try {
			SecretKeySpec skeyspec = new SecretKeySpec(Constantes.KEY_ENCRYPT.getBytes(CHARSET_NAME), "Blowfish");
			Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE);
			cipher.init(Cipher.DECRYPT_MODE, skeyspec, IV_SPEC);
			byte[] decrypted = cipher.doFinal(encryptedBytes);
			strData = new String(decrypted, CHARSET_NAME);
		} catch (Exception e) {
			e.printStackTrace();
			strData=decrypt2Old(encrypted);
		}
		return strData;
	}

	public static String decrypt2Old(String encrypted) {
		String strData = "";
		byte[] encryptedBytes = Base64.decodeBase64(encrypted);
		try {
			SecretKeySpec skeyspec = new SecretKeySpec(Constantes.KEY_ENCRYPT.getBytes(), "Blowfish");
			Cipher cipher = Cipher.getInstance("Blowfish");
			cipher.init(Cipher.DECRYPT_MODE, skeyspec);
			byte[] decrypted = cipher.doFinal(encryptedBytes);
			strData = new String(decrypted);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strData;
	}
}
