package pe.interseguro.siv.common.persistence.rest.global.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import pe.interseguro.siv.common.persistence.rest.niubiz.response.BaseResponse;

@Getter
@Setter
public class CrearPotentialVtigerResponse extends BaseResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("potentialname")
	private String potentialname;
	
	@JsonProperty("potential_no")
    private String potential_no;
	
	@JsonProperty("amount")
    private String amount;
	
	@JsonProperty("related_to")
    private String related_to;
	
	@JsonProperty("closingdate")
    private String closingdate;
	
	@JsonProperty("opportunity_type")
    private String opportunity_type;
	
	@JsonProperty("nextstep")
    private String nextstep;
	
	@JsonProperty("leadsource")
    private String leadsource;
	
	@JsonProperty("sales_stage")
    private String sales_stage;
	
	@JsonProperty("assigned_user_id")
    private String assigned_user_id;
	
	@JsonProperty("probability")
    private String probability;
	
	@JsonProperty("campaignid")
    private String campaignid;
	
	@JsonProperty("createdtime")
    private String createdtime;
	
	@JsonProperty("modifiedtime")
    private String modifiedtime;
	
	@JsonProperty("modifiedby")
    private String modifiedby;
	
	@JsonProperty("description")
    private String description;
	
	@JsonProperty("forecast_amount")
    private String forecast_amount;
	
	@JsonProperty("isconvertedfromlead")
    private String isconvertedfromlead;
	
	@JsonProperty("contact_id")
    private String contact_id;
	
	@JsonProperty("source")
    private String source;
	
	@JsonProperty("starred")
    private String starred;
	
	@JsonProperty("tags")
    private String tags;
	
	@JsonProperty("cf_864")
    private String cf_864;
	
	@JsonProperty("cf_866")
    private String cf_866;
	
	@JsonProperty("cf_868")
    private String cf_868;
	
	@JsonProperty("cf_870")
    private String cf_870;
	
	@JsonProperty("cf_872")
    private String cf_872;
	
	@JsonProperty("cf_874")
    private String cf_874;
	
	@JsonProperty("cf_876")
    private String cf_876;
	
	@JsonProperty("cf_878")
    private String cf_878;
	
	@JsonProperty("cf_880")
    private String cf_880;
	
	@JsonProperty("cf_882")
    private String cf_882;
	
	@JsonProperty("cf_884")
    private String cf_884;
	
	@JsonProperty("cf_909")
    private String cf_909;
	
	@JsonProperty("cf_911")
    private String cf_911;
	
	@JsonProperty("cf_913")
    private String cf_913;
	
	@JsonProperty("cf_915")
    private String cf_915;
	
	@JsonProperty("cf_918")
    private String cf_918;
	
	@JsonProperty("cf_922")
    private String cf_922;
	
	@JsonProperty("cf_924")
    private String cf_924;
	
	@JsonProperty("cf_926")
    private String cf_926;
	
	@JsonProperty("cf_932")
    private String cf_932;
	
	@JsonProperty("cf_934")
    private String cf_934;
	
	@JsonProperty("cf_936")
    private String cf_936;
	
	@JsonProperty("cf_1036")
    private String cf_1036;
	
	@JsonProperty("cf_1038")
    private String cf_1038;
	
	@JsonProperty("cf_1040")
    private String cf_1040;
	
	@JsonProperty("cf_852")
    private String cf_852;
	
	@JsonProperty("cf_1050")
    private String cf_1050;
	
	@JsonProperty("id")
    private String id;
	
	@JsonProperty("label")
    private String label;
}
