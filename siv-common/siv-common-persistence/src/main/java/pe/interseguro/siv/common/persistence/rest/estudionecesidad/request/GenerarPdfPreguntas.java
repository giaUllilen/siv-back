package pe.interseguro.siv.common.persistence.rest.estudionecesidad.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenerarPdfPreguntas {
	private String pregunta;
	private String respuesta;
}
