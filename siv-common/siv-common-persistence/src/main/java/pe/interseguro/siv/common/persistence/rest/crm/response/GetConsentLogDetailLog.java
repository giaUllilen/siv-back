package pe.interseguro.siv.common.persistence.rest.crm.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetConsentLogDetailLog {
	@SerializedName("id_tratamiento")
	@JsonProperty("id_tratamiento")
	private Integer idTratamiento;
	@SerializedName("ind_tratamiento")
	@JsonProperty("ind_tratamiento")
	private String indTratamiento;
}
