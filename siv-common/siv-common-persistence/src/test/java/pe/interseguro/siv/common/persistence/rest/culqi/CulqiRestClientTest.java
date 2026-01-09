package pe.interseguro.siv.common.persistence.rest.culqi;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import pe.interseguro.siv.common.config.BaseTest;
import pe.interseguro.siv.common.persistence.rest.culqui.request.CulqiTokenRequest;
import pe.interseguro.siv.common.persistence.rest.culqui.response.CulqiTokenResponse;

public class CulqiRestClientTest extends BaseTest {
	@Autowired
	private CulqiRestClient culqiRestClient;
	
	@Test
	public void crearTokenTest() {
		CulqiTokenResponse response = null;
		try {
			CulqiTokenRequest request = new CulqiTokenRequest();
			request.setNumeroTarjeta("4111111111111111");
			request.setCvv("123");
			request.setEmail("jav.xander@gmail.com");
			request.setMesExpiracion("09");
			request.setAnioExpiracion("2020");
			response = culqiRestClient.tokenizarTarjeta(request); // -- parametros x entorno
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("response = " + gson.toJson(response));

	}
}
