package pe.interseguro.siv.common.persistence.rest.niubiz.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class FisrtPayResponse extends BaseResponse {
	private FirstPayDataResponse data;
}
