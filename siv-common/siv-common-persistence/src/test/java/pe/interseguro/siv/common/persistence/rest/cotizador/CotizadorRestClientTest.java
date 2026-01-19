package pe.interseguro.siv.common.persistence.rest.cotizador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import pe.interseguro.siv.common.bean.CotizacionTokenBean;
import pe.interseguro.siv.common.config.BaseTest;
import pe.interseguro.siv.common.persistence.rest.cotizador.response.AsegurableResponse;
import pe.interseguro.siv.common.persistence.rest.cotizador.response.ExigenciaMedicaResponse;
import pe.interseguro.siv.common.persistence.rest.cotizador.response.ObtenerCorrelativoResponse;
import pe.interseguro.siv.common.persistence.rest.cotizador.response.ObtenerCumuloResponse;
import pe.interseguro.siv.common.persistence.rest.cotizador.response.ObtenerExigenciaMedicaResponse;

public class CotizadorRestClientTest extends BaseTest {
	@Autowired
	private CotizadorRestClient cotizadorRestClient;
	
	@Test
	public void obtenerCumuloResponseTest() {

		ObtenerCumuloResponse response = cotizadorRestClient.obtenerCumuloGeneral("4279703","1");
		System.out.println("response = " + gson.toJson(response));
	}
	
	@Test
	public void obtenerCorrelativoResponseTest() {

		ObtenerCorrelativoResponse response = cotizadorRestClient.generarCorrelativo();
		ObjectMapper mapper = new ObjectMapper();
		AsegurableResponse bean = null;
		try {
			bean = mapper.readValue(response.getMsg(), AsegurableResponse.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("numeroCotizacion = " + gson.toJson(bean));
		System.out.println("response = " + gson.toJson(response));

	}
	
	@Test
	@Ignore
	public void obtenerExigenciasMedicasTest() {
		List<ExigenciaMedicaResponse> response = new ArrayList<ExigenciaMedicaResponse>();
		response = cotizadorRestClient.obtenerExigenciasMedicas("45125", "2123", "32", "0", "50000", "0");
		System.out.println("response = " + gson.toJson(response));
	}
}
