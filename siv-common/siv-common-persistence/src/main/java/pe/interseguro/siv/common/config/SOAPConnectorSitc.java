package pe.interseguro.siv.common.config;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

public class SOAPConnectorSitc extends WebServiceGatewaySupport {
	
	public Object callWebService(String url, Object request, SoapActionCallback callback){
		return getWebServiceTemplate().marshalSendAndReceive(url, request, callback);
	}
}
