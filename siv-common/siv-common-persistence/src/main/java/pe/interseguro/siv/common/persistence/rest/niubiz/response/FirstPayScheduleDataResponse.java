package pe.interseguro.siv.common.persistence.rest.niubiz.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FirstPayScheduleDataResponse {
	private Integer feeNumber;
    private String objectId;
    private Double feeAmount;
    private String chargeDate;
    private String expirationDate;
}
