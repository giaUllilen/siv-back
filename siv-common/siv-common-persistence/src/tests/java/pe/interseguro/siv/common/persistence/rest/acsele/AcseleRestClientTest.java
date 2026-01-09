package pe.interseguro.siv.common.persistence.rest.acsele;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import pe.interseguro.siv.common.config.BaseTest;
import pe.interseguro.siv.common.persistence.rest.acsele.response.TerceroResponse;
import pe.interseguro.siv.common.persistence.rest.indenova.response.CrearTokenResponse;

public class AcseleRestClientTest extends BaseTest {
	@Autowired
	private AcseleRestClient acseleRestClient;
	
	@Test
	public void crearTokenTest() {

		CrearTokenResponse response = acseleRestClient.crearToken();
		System.out.println("response = " + gson.toJson(response));

	}
	
	@Test
	public void obtenerIDTerceroTest() {

		TerceroResponse response = acseleRestClient.obtenerIDTercero("PersonaNatural", "29597880");
		System.out.println("response = " + gson.toJson(response));

	}
}
