package pe.interseguro.siv.common.persistence.rest.vidafree;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import pe.interseguro.siv.common.config.BaseTest;
import pe.interseguro.siv.common.persistence.rest.vidafree.request.ConsultaCotizacionDetalleRequest;
import pe.interseguro.siv.common.persistence.rest.vidafree.response.ConsultaCotizacionDetalleResponse;
import pe.interseguro.siv.common.persistence.rest.vidafree.response.ObtenerCotizacionesResponse;

public class VidafreeRestClientTest extends BaseTest {
	@Autowired
	private VidaFreeRestClient vidaFreeRestClient;
	
	@Test
	public void obtenerCotizacionTest() {
		ObtenerCotizacionesResponse response = vidaFreeRestClient.obtenerCotizaciones("47506306");
		System.out.println("response = " + gson.toJson(response));
	}
	
	@Test
	public void obtenerCotizacionDetalleTest() {
		ConsultaCotizacionDetalleRequest request = new ConsultaCotizacionDetalleRequest();
		request.setIdAgente("xxx");
		request.setNumeroCotizacion("500000560");
		ConsultaCotizacionDetalleResponse response = vidaFreeRestClient.obtenerCotizacionDetalle(request);
		System.out.println("response = " + gson.toJson(response));
	}
}
