package pe.interseguro.siv.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SolicitudRegistroResponseDTO {
	private Long idSolicitud;
	private Boolean esNuevo;
}
