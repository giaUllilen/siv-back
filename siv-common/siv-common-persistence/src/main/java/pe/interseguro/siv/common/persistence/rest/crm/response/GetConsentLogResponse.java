package pe.interseguro.siv.common.persistence.rest.crm.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetConsentLogResponse {
	@SerializedName("id_consentimiento_asesoria")
	@JsonProperty("id_consentimiento_asesoria")
	private Integer idConsentimientoAsesoria;
	@SerializedName("log")
	@JsonProperty("log")
	private GetConsentLogLog log;
	@SerializedName("aud_usr_modificacion")
	@JsonProperty("aud_usr_modificacion")
	private String audUsrModificacion;
}
