package pe.interseguro.siv.common.persistence.rest.sitc;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import pe.interseguro.siv.common.exception.SivSOAException;
import pe.interseguro.siv.common.persistence.rest.base.BaseRestClientImpl;
import pe.interseguro.siv.common.persistence.rest.sitc.request.TokenSitcRequest;
import pe.interseguro.siv.common.persistence.rest.sitc.response.TokenSitcResponse;
import pe.interseguro.siv.common.util.Constantes;
import pe.interseguro.siv.common.util.Utilitarios;

@Component
public class SitcRestClient extends BaseRestClientImpl {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MessageSource messageSource;

    @Value("#{ environment['persistence.rest.client.sitc.servidor'] }")
    private String servidorSitc;
    
    @Value("#{ environment['persistence.rest.client.samp.servidor'] }")
    private String servidorSamp;
    
    @Value("#{ environment['persistence.rest.client.sitc.token'] }")
    private String servicioToken;
    
    @Value("#{ environment['persistence.rest.client.samp.registro.digitalizacion'] }")
    private String servicioRegistroDigitalizacion;
    
    @Value("#{ environment['persistence.rest.client.sitc.registro.afiliacion'] }")
    private String servicioRegistroAfiliacion;
    
    private String getUrlSitcToken() {
		return servidorSitc+servicioToken;
	}
    private String getUrlSitcRegistroDigitalizacion() {
		return servidorSitc+servicioRegistroDigitalizacion;
	}
    private String getUrlSitcRegistroAfiliacion() {
		return servidorSitc+servicioRegistroAfiliacion;
	}
    
    public TokenSitcResponse obtenerToken(TokenSitcRequest request) {
    	LOGGER.info("Entro SitcRestClient#obtenerToken(request)");
    	
    	TokenSitcResponse response = null;
    	 
		try {
			response = obtenerPostPorObjeto(getUrlSitcToken(), request, TokenSitcResponse.class);
		} catch (SivSOAException e) {
			throw new SivSOAException(
				Utilitarios.obtenerMensaje(messageSource, Constantes.MENSAJE_RESTCLIENT_CRM_COTIZADOR_TOKEN).concat( Objects.toString(e.getMessage(), "") ), 
				e.getErrors()
			);
		}
    	
		LOGGER.info("Salio SitcRestClient#obtenerToken(request)");
    	return response;
    }
}
