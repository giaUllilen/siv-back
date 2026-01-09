package pe.interseguro.siv.common.persistence.rest.cvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import pe.interseguro.siv.common.persistence.rest.base.BaseRestClientImpl;
import pe.interseguro.siv.common.persistence.rest.cvc.request.CvcRequest;
import pe.interseguro.siv.common.persistence.rest.niubiz.response.FisrtPayResponse;
import pe.interseguro.siv.common.util.Constantes;

@Component
public class CvcRestClient extends BaseRestClientImpl {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	@Value("#{ environment['persistence.rest.client.cvc.servidor'] }")
	private String baseCvcUrl;

	@Value("#{ environment['persistence.rest.client.cvc'] }")
	private String cvc;

	@Value("#{ environment['persistence.rest.client.interseguro.passarella.apiKey'] }")
	private String apikeyPaymnet;

	@Value("#{ environment['persistence.rest.client.cvc.channel'] }")
	private String channel;

	private String getUrlCvc() {
		return baseCvcUrl + cvc;
	}

	public FisrtPayResponse afiliaPagoCvc(CvcRequest request) {
		LOGGER.info("Entro a afiliaPagoCvc()");
		FisrtPayResponse response = new FisrtPayResponse();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
//		headers.set("X-Apikey", request.getToken());
		headers.set("X-Provider", request.getProvider().toUpperCase());
		headers.set("X-channel", channel);
		headers.set("X-PaymentApiKey", apikeyPaymnet);

		// LOGGER.info();
		try {
			response = obtenerPostPorObjeto(getUrlCvc(), new HttpEntity<CvcRequest>(request, headers),
					FisrtPayResponse.class);
		} catch (HttpClientErrorException errorClient) {
			response.setCode(Constantes.CODIGO_RESPUESTA_GENERAL_CULQI_ERROR_400);
			response.setMessage(errorClient.getResponseBodyAsString());
			response.setTitle(errorClient.getMessage());
			response.setStatusHttp(errorClient.getMessage());
		} catch (HttpServerErrorException errorServer) {
			response.setCode(Constantes.CODIGO_RESPUESTA_GENERAL_CULQI_ERROR_500);
			response.setMessage(errorServer.getMessage());
			response.setTitle(errorServer.getMessage());
			response.setStatusHttp(errorServer.getResponseBodyAsString());
		} catch (Exception e) {
			e.getStackTrace();
			System.out.println(e.getMessage());
			response.setCode(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			response.setMessage("Error desconocido");
			response.setTitle("Error");
		}
		LOGGER.info("Salio a afiliaPagoCvc()");
		return response;
	}
}
