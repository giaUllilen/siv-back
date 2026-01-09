package pe.interseguro.siv.common.persistence.rest.crm.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetConsentLogLog {
	@SerializedName("logDetalle")
	@JsonProperty("logDetalle")
	private List<GetConsentLogDetailLog> logDetalle = null;
}
