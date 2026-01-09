package pe.interseguro.siv.common.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NiubizFirstPaySubscriptionRequestDTO {
	private String objectId;
    private String currency;
    private Double amount;
    private NiubizFisrtPaySubscriptionMetadataRequestDTO metadata;
}
