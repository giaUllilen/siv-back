package pe.interseguro.siv.common.persistence.soap.sitc;

import java.math.BigDecimal;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import pe.interseguro.siv.common.config.SOAPConnectorCobranza;
import pe.interseguro.siv.common.config.SOAPConnectorSitc;
import pe.interseguro.siv.common.persistence.rest.sitc.request.DigitalizacionPagoRequest;
import pe.interseguro.siv.common.persistence.rest.sitc.request.TokenSitcRequest;
import pe.interseguro.siv.common.persistence.rest.sitc.response.DigitalizacionPagoResponse;
import pe.interseguro.siv.common.persistence.rest.sitc.response.TokenSitcResponse;
import pe.interseguro.siv.common.persistence.soap.schema.samp.cobranza.DigitalizacionPagosVidaConsulta;
import pe.interseguro.siv.common.persistence.soap.schema.samp.cobranza.RegistroDigitalizacionPagosVida;
import pe.interseguro.siv.common.persistence.soap.schema.samp.cobranza.RegistroDigitalizacionPagosVidaResponse;
import pe.interseguro.siv.common.persistence.soap.schema.sitc.token.ObjectFactory;
import pe.interseguro.siv.common.persistence.soap.schema.sitc.token.RegistrarToken;
import pe.interseguro.siv.common.persistence.soap.schema.sitc.token.RegistrarTokenResponse;

@Component
public class SitcSoapClient {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@Autowired SOAPConnectorSitc soapConnector;
	//@Autowired SOAPConnectorAfiliacion soapConnectorAfiliacion;
	@Autowired SOAPConnectorCobranza soapConnectorCobranza;
	
	@Value("#{ environment['persistence.rest.client.sitc.servidor'] }")
    private String baseSitcUrl;
	
	@Value("#{ environment['persistence.rest.client.samp.servidor'] }")
    private String baseSampUrl;
	
	@Value("#{ environment['persistence.rest.client.sitc.token'] }")
	private String tokenSitcUrl;
	
	@Value("#{ environment['persistence.rest.client.samp.registro.digitalizacion'] }")
	private String digitalizacionSampUrl;
	
	private String getUrlSitcToken() {
		return baseSitcUrl + tokenSitcUrl;
	}
	
	private String getUrlDigitalizacionSamp() {
		return baseSampUrl + digitalizacionSampUrl;
	}
	
	public TokenSitcResponse obtenerToken(TokenSitcRequest request) {
    	LOGGER.info("Entro SitcSoapClient#obtenerToken(request)");
    	
    	TokenSitcResponse response = new TokenSitcResponse();
    	
    	ObjectFactory factory = new ObjectFactory();
    	RegistrarToken requestSoap = factory.createRegistrarToken();
    	JAXBElement<String> ip = factory.createRegistrarTokenNumIp(request.getNumIp());
    	JAXBElement<String> usuario = factory.createRegistrarTokenUsuario(request.getUsuario());
    	JAXBElement<String> usuarioApp = factory.createRegistrarTokenUsuarioAplicacion(request.getUsuarioAplicacion());
    	requestSoap.setNumIp(ip);
    	requestSoap.setUsuario(usuario);
    	requestSoap.setUsuarioAplicacion(usuarioApp);
    	requestSoap.setNumPropuesta(Long.valueOf(request.getNumPropuesta()));
    	
    	RegistrarTokenResponse responseSoap = (RegistrarTokenResponse) soapConnector.callWebService(getUrlSitcToken(), 
    			requestSoap,
    			new SoapActionCallback("http://www.interseguro.com.pe/TokenServices/ITokenWs/RegistrarToken"));
    	
    	if(responseSoap != null) {
    		System.out.println(responseSoap.getRegistrarTokenResult().getValue());
    		response.setRegistrarTokenResult(responseSoap.getRegistrarTokenResult().getValue());
    	}
    	
		LOGGER.info("Salio SitcSoapClient#obtenerToken(request)");
    	return response;
    }
	
	public DigitalizacionPagoResponse digitalizacionPagoSamp(DigitalizacionPagoRequest request) {
		LOGGER.info("Entro SitcSoapClient#digitalizacionPagoSamp(request)");
		DigitalizacionPagoResponse response = new DigitalizacionPagoResponse();
		
		pe.interseguro.siv.common.persistence.soap.schema.samp.cobranza.ObjectFactory factory = new pe.interseguro.siv.common.persistence.soap.schema.samp.cobranza.ObjectFactory();
		RegistroDigitalizacionPagosVida requestSoap = factory.createRegistroDigitalizacionPagosVida();
		
		DigitalizacionPagosVidaConsulta pago = factory.createDigitalizacionPagosVidaConsulta();
		pago.setCodUsuario(request.getUsuario());
		pago.setCodUsuarioAplicacion(request.getUsuarioAplicacion());
		pago.setIdCulqui(request.getIdCulqui());
		pago.setIdPropuestaAfiliacion(request.getIdPropuestaAfiliacion());
		pago.setMoneda(request.getMoneda());
		pago.setMonto(BigDecimal.valueOf(request.getMonto()));
		pago.setNumeroPropuestaCadena(request.getNumeroPropuesta());
		pago.setPasarela(request.getPasarela());
		pago.setPasarela_pp(request.getPasarelaPp());
		pago.setIndPagoPasarela(request.getIndPagoPasarela());
		requestSoap.setConsulta(factory.createDigitalizacionPagosVidaConsulta(pago));
		RegistroDigitalizacionPagosVidaResponse responseSoap = (RegistroDigitalizacionPagosVidaResponse) soapConnectorCobranza.callWebService(getUrlDigitalizacionSamp(), 
    			requestSoap,
    			new SoapActionCallback("http://tempuri.org/ICobranzaMorosaWs/RegistroDigitalizacionPagosVida"));
    	
    	if(responseSoap != null) {
    		response.setIdDigitalizacionPagosVida(String.valueOf(responseSoap.getRegistroDigitalizacionPagosVidaResult().getValue().getIdDigitalizacionPagosVida()));
    		response.setCodigoRespuesta(responseSoap.getRegistroDigitalizacionPagosVidaResult().getValue().getCodigoRespuesta());
    		response.setMensajeRespuesta(responseSoap.getRegistroDigitalizacionPagosVidaResult().getValue().getMensajeRespuesta());
    	}
		
		LOGGER.info("Entro SitcSoapClient#digitalizacionPagoSamp(request)");
		return response;
	}
	
}
