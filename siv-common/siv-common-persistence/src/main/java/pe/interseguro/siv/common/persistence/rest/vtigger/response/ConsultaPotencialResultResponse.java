package pe.interseguro.siv.common.persistence.rest.vtigger.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsultaPotencialResultResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2058018005602560592L;
	
	private String id;
	private String contact_id;
	private String leadsource;
	private String sales_stage;
	private String cf_872;
	private String cf_924;
}
