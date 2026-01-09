package pe.interseguro.siv.common.persistence.rest.estudionecesidad.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class GenerarPdfResponse extends BaseResponse {
	//private GenerarPdfDataResponse data;
    private String data;
}
