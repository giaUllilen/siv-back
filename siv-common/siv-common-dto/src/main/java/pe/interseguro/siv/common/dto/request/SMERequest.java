package pe.interseguro.siv.common.dto.request;

import java.io.Serializable;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SMERequest implements Serializable {
	String email;
	String numeroPoliza;
	String numeroDocumento;
	String destinatario;
	String procesoSme;
	String contrasenia;
	String rutaPdf;
	Map<String, Object> camposDinamicos;
}
