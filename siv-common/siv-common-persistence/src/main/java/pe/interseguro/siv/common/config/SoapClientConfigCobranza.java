package pe.interseguro.siv.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class SoapClientConfigCobranza {
	@Bean
    public Jaxb2Marshaller marshallerCobranza() {
        Jaxb2Marshaller marshallerCobranza = new Jaxb2Marshaller();
        // this is the package name specified in the <generatePackage> specified in
        // pom.xml
        marshallerCobranza.setContextPath(
        		"pe.interseguro.siv.common.persistence.soap.schema.samp.cobranza");
        return marshallerCobranza;
    }
	
	@Bean
    public SOAPConnectorCobranza soapConnectorCobranza(Jaxb2Marshaller marshallerCobranza) {
		SOAPConnectorCobranza client = new SOAPConnectorCobranza();
        client.setDefaultUri("http://10.29.33.18/Samp/CobranzaMorosaWs.svc");
        client.setMarshaller(marshallerCobranza);
        client.setUnmarshaller(marshallerCobranza);
        return client;
    }
}
