/**
 * 
 */
package pe.interseguro.siv.common.persistence.rest.interseguro;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import pe.interseguro.siv.common.config.BaseTest;
import pe.interseguro.siv.common.persistence.rest.interseguro.request.EnviarSmsRequest;
import pe.interseguro.siv.common.persistence.rest.interseguro.request.ObtenerDatosUsuarioRequest;
import pe.interseguro.siv.common.persistence.rest.interseguro.response.DigitalTokenResponse;
import pe.interseguro.siv.common.persistence.rest.interseguro.response.EnviarSmsResponse;
import pe.interseguro.siv.common.persistence.rest.interseguro.response.ObtenerDatosClienteResponse;
import pe.interseguro.siv.common.persistence.rest.interseguro.response.ObtenerDatosUsuarioResponse;

/**
 * @author digital-is
 *
 */
public class InterseguroRestClientTest extends BaseTest {

	@Autowired
	private InterseguroRestClient interseguroRestClient;
	
	@Test
	public void enviarSmsTest() {
		
		EnviarSmsRequest request = new EnviarSmsRequest();
		request.setCelular("973868825");
		request.setMensaje("TEST: Estimado Cliente, le envíamos su codigo de verificación para su solicitud digital.");
		
		EnviarSmsResponse response = null;
		try {
			response = interseguroRestClient.enviarSms(request);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("response = " + gson.toJson(response));// si es estado:1 entonces es OK
		
	}
	
	@Test
	public void obtenerDatosUsuarioAzman() {
		ObtenerDatosUsuarioRequest request = new ObtenerDatosUsuarioRequest();
		request.setUsuario("plat_desa");
		//request.setContrasena("s1st3m2s");
		request.setAplicacion("CUPONERA");
		request.setDominio("dinterseguro");
		
		ObtenerDatosUsuarioResponse response = interseguroRestClient.obtenerDatosUsuario(request);
		System.out.println("response = " + gson.toJson(response));
	}

	@Test
	public void digitalTokenTest() {
		DigitalTokenResponse response = null;
		try {
			response = interseguroRestClient.obtenerTokenDigital();
		} catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("response = " + gson.toJson(response));
	}
	
	@Test
	public void digitalObtenerClienteTest() {
		ObtenerDatosClienteResponse response = null;
		try {
			response = interseguroRestClient.obtenerDatosCliente("12345678");
		} catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("response = " + gson.toJson(response));
	}
}
