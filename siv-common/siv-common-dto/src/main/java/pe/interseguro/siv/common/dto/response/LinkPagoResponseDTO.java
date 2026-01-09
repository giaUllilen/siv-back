package pe.interseguro.siv.common.dto.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinkPagoResponseDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String codigoRespuesta;
	private String token;
	private String mensajeRespuesta;
	private String link;

}
