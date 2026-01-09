package pe.interseguro.siv.common.persistence.rest.global.request;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObtenerPubSubSendRequest implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	private String topic;
	private List<ObtenerMetadataPublishRequest> metadataPublish;
}
