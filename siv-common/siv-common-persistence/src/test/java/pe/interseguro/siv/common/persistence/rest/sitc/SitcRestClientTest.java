package pe.interseguro.siv.common.persistence.rest.sitc;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import pe.interseguro.siv.common.config.BaseTest;
import pe.interseguro.siv.common.persistence.rest.sitc.request.TokenSitcRequest;
import pe.interseguro.siv.common.persistence.rest.sitc.response.TokenSitcResponse;

public class SitcRestClientTest extends BaseTest {
	@Autowired
	private SitcRestClient sitcRestClient;
	
	@Test
	public void crearTokenTest() {
		TokenSitcResponse response = null;
		try {
			TokenSitcRequest request = new TokenSitcRequest();
			request.setNumIp("");
			request.setNumPropuesta("");
			request.setUsuario("");
			request.setUsuarioAplicacion("");
			response = sitcRestClient.obtenerToken(request); // -- parametros x entorno
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("response = " + gson.toJson(response));

	}
}
