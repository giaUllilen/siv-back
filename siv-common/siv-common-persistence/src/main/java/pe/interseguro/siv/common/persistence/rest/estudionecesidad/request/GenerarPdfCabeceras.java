package pe.interseguro.siv.common.persistence.rest.estudionecesidad.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenerarPdfCabeceras {
	private String campo;
	private String valor;
}
