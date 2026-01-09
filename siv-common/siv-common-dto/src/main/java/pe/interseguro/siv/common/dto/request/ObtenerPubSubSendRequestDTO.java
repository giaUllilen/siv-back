package pe.interseguro.siv.common.dto.request;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ObtenerPubSubSendRequestDTO extends BaseRequestDTO{

	private static final long serialVersionUID = 2646147106251429899L;
	
	private String propuesta;
}
