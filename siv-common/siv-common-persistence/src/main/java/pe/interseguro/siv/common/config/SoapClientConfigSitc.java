package pe.interseguro.siv.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class SoapClientConfigSitc {
	@Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        // this is the package name specified in the <generatePackage> specified in
        // pom.xml
        marshaller.setContextPath(
        		"pe.interseguro.siv.common.persistence.soap.schema.sitc.token");
        return marshaller;
    }
	
	@Bean
    public SOAPConnectorSitc soapConnector(Jaxb2Marshaller marshaller) {
        SOAPConnectorSitc client = new SOAPConnectorSitc();
        client.setDefaultUri("http://10.29.33.76/Sat/TokenWs.svc");
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }
	
}
