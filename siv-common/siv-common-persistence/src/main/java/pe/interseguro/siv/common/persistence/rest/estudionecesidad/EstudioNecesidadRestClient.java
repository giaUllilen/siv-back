package pe.interseguro.siv.common.persistence.rest.estudionecesidad;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.google.gson.Gson;

import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pe.interseguro.siv.common.exception.SivSOAException;
import pe.interseguro.siv.common.persistence.rest.base.BaseRestClientImpl;
import pe.interseguro.siv.common.persistence.rest.estudionecesidad.request.GenerarPdfRequest;
import pe.interseguro.siv.common.persistence.rest.estudionecesidad.response.GenerarPdfDataResponse;
import pe.interseguro.siv.common.persistence.rest.estudionecesidad.response.GenerarPdfResponse;
import pe.interseguro.siv.common.util.Constantes;

@Component
public class EstudioNecesidadRestClient extends BaseRestClientImpl {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Value("#{ environment['persistence.rest.client.esn.servidor'] }")
    private String baseUrl;
	
	@Value("#{ environment['persistence.rest.client.esn.credential.user'] }")
    private String esnUser;
	
	@Value("#{ environment['persistence.rest.client.esn.credential.pass'] }")
    private String esnPasword;
	
	@Value("#{ environment['persistence.rest.client.esn.generarpdf'] }")
    private String esnGenerarPdf;
	
	private String getUrlEsnGeneraPdf() {
		return baseUrl + esnGenerarPdf;
	}

	
    public GenerarPdfResponse generarPdf(GenerarPdfRequest generarPdfRequest) throws SivSOAException {
    	LOGGER.info("Entro a EstudioNecesidadRestClient#generarPdf(generarPdfRequest)");
    	GenerarPdfResponse esnGenerarPdfResponse = new GenerarPdfResponse();		
		try {
			Gson gson = new Gson();
			String jsonRequest = gson.toJson(generarPdfRequest);
			//OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(5, TimeUnit.SECONDS).build();
			OkHttpClient client = new OkHttpClient().newBuilder().build();
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType, jsonRequest);
			String credential = Credentials.basic(esnUser,esnPasword);
			
			Request request = new Request.Builder()
					.url(getUrlEsnGeneraPdf())
					.method("POST", body).addHeader("Authorization", credential)
					.addHeader("Content-Type", "application/json").build();			
			Response response = client.newCall(request).execute();
			esnGenerarPdfResponse.setCode(String.valueOf(response.code()));
			esnGenerarPdfResponse.setStatusHttp(String.valueOf(response.code()));
			esnGenerarPdfResponse.setMessage(response.message());
			if (response.code() == 200) {
			    String rpta=response.body().string();
				esnGenerarPdfResponse.setData(rpta);
			}
		} catch (Exception e) {
			e.getStackTrace();
			LOGGER.error(e.getMessage());
			System.out.println(e.getMessage());
			esnGenerarPdfResponse.setCode(Constantes.CODIGO_RESPUESTA_GENERAL_ERROR);
			esnGenerarPdfResponse.setMessage("Error desconocido");
		}
    	LOGGER.info("Salió EstudioNecesidadRestClient#generarPdf(generarPdfRequest)");
		return esnGenerarPdfResponse;
    }

}
