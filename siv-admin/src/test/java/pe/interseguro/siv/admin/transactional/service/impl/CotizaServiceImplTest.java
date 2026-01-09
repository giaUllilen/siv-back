package pe.interseguro.siv.admin.transactional.service.impl;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import pe.interseguro.siv.admin.config.BaseTest;
import pe.interseguro.siv.admin.transactional.service.CotizaService;
import pe.interseguro.siv.common.bean.CotizacionPDFBean;
import pe.interseguro.siv.common.dto.request.TokenRequestDTO;
import pe.interseguro.siv.common.dto.response.BaseResponseDTO;
import pe.interseguro.siv.common.dto.response.CotizaListaResponseDTO;
import pe.interseguro.siv.common.dto.response.CotizaTablaResponseDTO;
import pe.interseguro.siv.common.dto.response.CotizaUrlResponse;
import pe.interseguro.siv.common.dto.response.TokenResponseDTO;

public class CotizaServiceImplTest extends BaseTest {

	@Autowired
	private CotizaService cotizaService;

	@Test
	public void obtenerTablaTest() {

		CotizaTablaResponseDTO response = cotizaService.obtenerTabla("001");
		System.out.println("response = " + gson.toJson(response));

		assertNotNull(response);
	}





	@Test
	public void obtenerUrlCotizador() {

		CotizaUrlResponse response = cotizaService.ObtenerURLCotizadorVida("c461f39e-98ee-e911-840b-00505689366a",
				"dev", "", "");
		System.out.println("response = " + gson.toJson(response));

		assertNotNull(response);
	}

	/*@Test
	public void obtenerUrlRecotizarVida() {

		CotizaUrlResponse response = cotizaService.ObtenerURLRecotizarVida("e7d41d43-b6e0-e911-840b-00505689366a");
		System.out.println("response = " + gson.toJson(response));

		assertNotNull(response);
	}*/

	@Test
	public void listaCotizacionesVidaTest() {

		CotizaListaResponseDTO response = cotizaService.listaCotizacionesVida("45730615","dev","","");
		System.out.println("response = " + gson.toJson(response));

		assertNotNull(response);
		assertNotNull(response.getLista());
	}

	@Test
	public void listaEnvioCorreoCotizacionesTest() {
		List<CotizacionPDFBean> pdfs = new ArrayList<>();
		pdfs.add(new CotizacionPDFBean("2142062",
				"http://S432VCI:8080/cotvidaServicesRpt/report/export?tipofum=0&user=DEVELOPER&printCarta=&printCot=si&printValGar=si&printResCot=&idCotResumen=0&fecot=26/09/2019&aseg=EDWIN+QUISPE+RAMOS&idCot=2142062"));
		pdfs.add(new CotizacionPDFBean("500000000",
				"http://S432VCI:8080/cotvidaServicesRpt/report/export?tipofum=0&user=DEVELOPER&printCarta=&printCot=si&printValGar=si&printResCot=&idCotResumen=0&fecot=02/10/2019&aseg=EDWIN+QUISPE+RAMOS&idCot=500000000"));
		BaseResponseDTO response = cotizaService.enviarCorreo("jav.xander@gmail.com", pdfs, "ALEXANDER", "Everth Cerna",
				"everth.cerna@interseguro.com.pe");
		System.out.println("response = " + gson.toJson(response));

		assertNotNull(response);
	}

	@Test
	public void TokeVidaFreeTest() {

		TokenRequestDTO tokenRequestDTO = new TokenRequestDTO();
		tokenRequestDTO.setEdadActuarial("44");
		tokenRequestDTO.setFechaCotizacion("29/11/2019");
		tokenRequestDTO.setFechaNacimiento("21/11/1975");
		tokenRequestDTO.setTipoDocumentoCliente("2");
		tokenRequestDTO.setNumeroDocumentoCliente("441020304133");
		tokenRequestDTO.setNombreCliente("LORIA ALEJANDRINA ANGULO PRADA");
		tokenRequestDTO.setIdUsuarioCrm("21e7bd1b-5b02-ea11-8b0c-005056895ba3");
		tokenRequestDTO.setNombreUsuario("Juan Salomon Aybar Amar");
		tokenRequestDTO.setNumeroCotizacion("500000566");
		tokenRequestDTO.setRol("AGENTE");
		tokenRequestDTO.setSexo("2");
		tokenRequestDTO.setIdOportunidadCrm("bae2d412-eb11-ea11-9ffc-005056878577");
		tokenRequestDTO.setIdCotizacionCrm("0");

		BaseResponseDTO response = cotizaService.obtenerURLVidaFree(tokenRequestDTO);
		System.out.println("response = " + gson.toJson(response));

		assertNotNull(response);
	}

	@Test
	public void TokeVidaDecryptFreeTest() {
		TokenResponseDTO response = cotizaService.decryptToken(
				"tcZKx5yPUM7oQa2vpTD3Zoz/iiKCyGV3tz5NPOmXlA8owuGDyexQlr3GgSj463bQyi+QpQSl0hadD/DF4KE/o8ewC0ptdhGevTFHawGdham4HCQ6W+mdrxIVzR5xIBgB+RHUI7MxXfDQBlZrZ1e75/LdtjIwUHZRIQYLR0jVavHNcKMDGlmTdt9Na5Z5nw8RUye6ALsUu7FTwhery84hpTuvCQYa4Jf6zJDgeWKm0Ig7NU7nAOzl8qjbIcB0C6TZB2fY8y9NCPIXKGCcUSASC+K9KW9iFGVb2WMsxEyDpGV5+Kepmzdz4d5xUjRVreG/vMbu6qW4T+mUcnyn29PhM4yy1FmdsJHhHGyjngaLgPMySZfPkiybiNILhd9rbCWbSzGT2MDpGh2OhqPrpqeOO2IVOutk6Gmfl43vliF+YqeOXrgpA6zFjwkFgOJyLbIuXDW7sgbgD51waGulg2KPkU2dQk3SpjBF91tTJde+NpKLnipNiArRcQFxE7SRezDvozMaIp8yxJI8sUNcKXV/P46Go+ump447isEDyRSmKgIVQfA0mm5BAdnvML1RyVo74tkXN0k3YLnM9pJNUX3VXR7JF3DuWgkX");

		System.out.println("response = " + gson.toJson(response));

		assertNotNull(response);
	}

}
