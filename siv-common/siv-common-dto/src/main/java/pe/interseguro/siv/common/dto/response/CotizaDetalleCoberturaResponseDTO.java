package pe.interseguro.siv.common.dto.response;

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
public class CotizaDetalleCoberturaResponseDTO {

	private String id;
	private String tipo;
	private String nombre;
	private Float capital;
	private Float prima;
}
