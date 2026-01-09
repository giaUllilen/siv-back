package pe.interseguro.siv.admin.transactional.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import pe.interseguro.siv.common.util.Constantes;
import pe.interseguro.siv.common.util.DateUtil;

public class DesencryptTest {
	private static final String KEY_ENCRYPT = Constantes.KEY_ENCRYPT;

	private String decrypt(String encrypted) {

		String strData = "";
		byte[] encryptedBytes = Base64.decodeBase64(encrypted);

		try {
			// LOGGER.info("Logger decrypt [TRY]");
			SecretKeySpec skeyspec = new SecretKeySpec(KEY_ENCRYPT.getBytes(), "Blowfish");
			Cipher cipher = Cipher.getInstance("Blowfish");
			cipher.init(Cipher.DECRYPT_MODE, skeyspec);
			byte[] decrypted = cipher.doFinal(encryptedBytes);
			strData = new String(decrypted);
			// LOGGER.info("Logger decrypt [RESULTADO]" + strData);
		} catch (Exception e) {
			// LOGGER.info("Logger decrypt [CATCH]" + e.getMessage());
			e.printStackTrace();
		}
		// LOGGER.info("Logger decrypt [FIN]");
		return strData;

	}

	@Test
	public void desencrypt() {
		String codigoAnt = "dfTAGOObKiQ=";
		String codigoDes = this.decrypt(codigoAnt);
		System.out.println(codigoDes);

	}

//	@Test
	public void convierteFecha() {
		String fecha = "2021-12-21T09:21:20";
		String formato = "yyyy-MM-dd'T'HH:mm:ss";
		Date fechaFormat = DateUtil.stringToDate(fecha, formato);
		System.out.println(fechaFormat);

	}

//	@Test
	public void validaNumero() {
		String a = "0";

		if ((!a.equals("") && Integer.parseInt(a) == 0) || a.equals("")) {
			System.out.println("miii" + a);
		}

	}

//	@Test
	public void validaTratamiento() {

		Map<String, Object> request = new HashMap<>();

		request.put("id_tratamiento", "10");
		request.put("id_configuracion", "3");
		request.put("aud_usr_ingreso", "leads-backend");

		String tratamiento[] = { "4", "5", "9", "10" };
		String idTratamiento = "3";
		String audUsrIngreso = "leads-backend";
		for (String t : tratamiento) {
			if ((request.get("id_tratamiento").toString().equals(t)
					&& !request.get("id_configuracion").toString().equals(idTratamiento))
					|| (request.get("id_tratamiento").toString().equals(t)
							&& request.get("id_configuracion").toString().equals(idTratamiento)
							&& request.get("aud_usr_ingreso").toString().equals(audUsrIngreso))) {
				System.out.println(t);
			} 

		}

		 

		if ((Integer.parseInt(request.get("id_tratamiento").toString()) == 4
				|| Integer.parseInt(request.get("id_tratamiento").toString()) == 5
				|| Integer.parseInt(request.get("id_tratamiento").toString()) == 9
				|| Integer.parseInt(request.get("id_tratamiento").toString()) == 10)
				&& Integer.parseInt(request.get("id_tratamiento").toString()) != 3) {
//			System.out.println("ddddddddddddd");

		}
//		System.out.println(request.get("id_tratamiento"));

	}
}
