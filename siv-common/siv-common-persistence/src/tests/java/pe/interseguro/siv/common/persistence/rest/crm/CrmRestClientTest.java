package pe.interseguro.siv.common.persistence.rest.crm;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import pe.interseguro.siv.common.config.BaseTest;
import pe.interseguro.siv.common.persistence.rest.crm.request.DatoContactoRequest;
import pe.interseguro.siv.common.persistence.rest.crm.request.GetIdOportunidadRequest;
import pe.interseguro.siv.common.persistence.rest.crm.request.TokenCotizadorRequest;
import pe.interseguro.siv.common.persistence.rest.crm.request.UpdateDetRequest;
import pe.interseguro.siv.common.persistence.rest.crm.request.UpdateRequest;
import pe.interseguro.siv.common.persistence.rest.crm.request.ValidarAccesoRequest;
import pe.interseguro.siv.common.persistence.rest.crm.request.ValidarCreacionAsignacionDetRequest;
import pe.interseguro.siv.common.persistence.rest.crm.request.ValidarCreacionAsignacionRequest;
import pe.interseguro.siv.common.persistence.rest.crm.response.DatoContactoResponse;
import pe.interseguro.siv.common.persistence.rest.crm.response.GetIdOportunidadResponse;
import pe.interseguro.siv.common.persistence.rest.crm.response.TokenCotizadorResponse;
import pe.interseguro.siv.common.persistence.rest.crm.response.UpdateResponse;
import pe.interseguro.siv.common.persistence.rest.crm.response.ValidarAccesoResponse;
import pe.interseguro.siv.common.persistence.rest.crm.response.ValidarCreacionAsignacionResponse;

public class CrmRestClientTest extends BaseTest {

	@Autowired
	private CrmRestClient crmRestClient;
	
	
	/*@Test
	public void validarCreacionAsignacionTest() {
		
		ValidarCreacionAsignacionDetRequest request = new ValidarCreacionAsignacionDetRequest();
		request.setDomainName("DINTERSEGURO\\\\plat_desa");
		request.setTipoDocumento("538560000");
		request.setNumeroDocumento("10459302");
		
		ValidarCreacionAsignacionResponse response = crmRestClient.validarCreacionAsignacion(request);
		System.out.println("response = " + gson.toJson(response));
		
	}*/
	
	/*@Test
	public void getDatosContactoTest() {
		
		DatoContactoRequest request = new DatoContactoRequest();
		request.setDomainName("DINTERSEGURO\\\\plat_desa");
		request.setTipoDocumento("538560000");
		request.setNumeroDocumento("10459302");
		
		DatoContactoResponse response = crmRestClient.getDatosContacto(request);
		System.out.println("response = " + gson.toJson(response));
		
	}*/
	
	/*@Test
	public void validarAccesoCrmTest() {
		
		ValidarAccesoRequest request = new ValidarAccesoRequest();
		request.setDomainName("DINTERSEGURO\\\\plat_desa");
		
		ValidarAccesoResponse response = crmRestClient.validarAccesoCrm(request);
		System.out.println("response = " + gson.toJson(response));
	}*/
	
	/*@Test
	public void updateCrmTest() {
		
		UpdateRequest request = new UpdateRequest();
		request.setParam( new UpdateDetRequest() );
		request.getParam().setTipoDocumento("DNI");
		request.getParam().setCodigoTipoDocumento("538560000");
		request.getParam().setNumeroDocumento("12345678");
		request.getParam().setFechaNacimiento("11/03/1986");
		request.getParam().setNombres("Pedro");
		request.getParam().setApellidoPaterno("Perez");
		request.getParam().setApellidoMaterno("Del Solar");
		request.getParam().setSexo("Masculino");
		request.getParam().setCodigoSexo("538560000");
		request.getParam().setTelefonoCelular("952304752");
		request.getParam().setEmail("juanperez@gmail.com");
		request.getParam().setProfesion("Abogados (no penalistas)");
		request.getParam().setCodigoProfesion("1");
		request.getParam().setActividadEconomica("Acabado de productos textiles");
		request.getParam().setCodigoActividadEconomica("1712");
		request.getParam().setCodigoFumador("538560000");
		request.getParam().setFumador("Si");
		request.getParam().setDomainName("DINTERSEGURO\\\\plat_desa");
		
		UpdateResponse response = crmRestClient.updateCrm(request);
		System.out.println("response = " + gson.toJson(response));
		
	}*/
	
	/*@Test
	public void obtenerTokenCotizador() {
		TokenCotizadorRequest request = new TokenCotizadorRequest();
		request.setIdOportunidad("B77BFF67-D47C-E911-A7BC-005056AE4A14");
		TokenCotizadorResponse response = crmRestClient.obtenerTokenCotizador(request);
		System.out.println("response = " + gson.toJson(response));
	}*/
	
	/*@Test
	public void getIdOportunidadCRM() {
		GetIdOportunidadRequest request = new GetIdOportunidadRequest();
		request.setTipoDocumento("538560000");
		request.setNumeroDocumento("11459303");
		request.setDomainName("DINTERSEGURO\\plat_cert");
		GetIdOportunidadResponse response = crmRestClient.getIdOportunidad(request);
		System.out.println("response = " + gson.toJson(response));
	}*/
	
	/*@Test
	public void postUpload() {
		GetIdOportunidadRequest request = new GetIdOportunidadRequest();
		request.setTipoDocumento("538560000");
		request.setNumeroDocumento("11459303");
		request.setDomainName("DINTERSEGURO\\plat_cert");
		GetIdOportunidadResponse response = crmRestClient.getIdOportunidad(request);
		System.out.println("response = " + gson.toJson(response));
	}*/
}
