package pe.interseguro.siv.common.persistence.rest.vtiger;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import pe.interseguro.siv.common.config.BaseTest;
import pe.interseguro.siv.common.persistence.rest.vtigger.VTigerRestClient;
import pe.interseguro.siv.common.persistence.rest.vtigger.request.ConsultaClienteRequest;
import pe.interseguro.siv.common.persistence.rest.vtigger.request.ConsultaPotencialRequest;
import pe.interseguro.siv.common.persistence.rest.vtigger.request.ConsultaUsuarioRequest;
import pe.interseguro.siv.common.persistence.rest.vtigger.response.ConsultaClienteResponse;
import pe.interseguro.siv.common.persistence.rest.vtigger.response.ConsultaPotencialResponse;
import pe.interseguro.siv.common.persistence.rest.vtigger.response.ConsultaUsuarioResponse;
import pe.interseguro.siv.common.persistence.rest.vtigger.response.CrearSessionResponse;
import pe.interseguro.siv.common.persistence.rest.vtigger.response.CrearTokenResponse;

public class VTigerRestClientTest extends BaseTest {
	
	@Autowired
	private VTigerRestClient vtigerRestClient;
	
	@Test
	public void vtigerTokenTest() {
		CrearTokenResponse response = null;
		try {
			response = vtigerRestClient.crearToken();
		} catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("response = " + gson.toJson(response));
	}
	
	@Test
	public void vtigerSessionTest() {
		CrearSessionResponse response = null;
		try {
			response = vtigerRestClient.crearSession();
		} catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("response = " + gson.toJson(response));
	}
	
	@Test
	public void vtigerObtenerClienteTest() {
		ConsultaClienteResponse response = null;
		try {
			ConsultaClienteRequest request = new ConsultaClienteRequest();
			request.setDocumentoIdentidad("10472882");
			response = vtigerRestClient.buscarCliente(request);
		} catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("response = " + gson.toJson(response));
	}
	
	@Test
	public void vtigerObtenerUsuarioTest() {
		ConsultaUsuarioResponse response = null;
		try {
			ConsultaUsuarioRequest request = new ConsultaUsuarioRequest();
			request.setId("12x60");
			response = vtigerRestClient.buscarUsuario(request);
		} catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("response = " + gson.toJson(response));
	}
	
	@Test
	public void vtigerObteneClientePotencialTest() {
		ConsultaPotencialResponse response = null;
		try {
			ConsultaPotencialRequest request = new ConsultaPotencialRequest();
			request.setContactID("12x8542");
			response = vtigerRestClient.buscarPotencial(request);
		} catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("response = " + gson.toJson(response));
	}
}
