package pe.interseguro.siv.common.dto.response;

import java.util.Map;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ADNConsentimientoAceptadoResponseDTO extends BaseResponseDTO {
	Map<String, Object> data;
}
