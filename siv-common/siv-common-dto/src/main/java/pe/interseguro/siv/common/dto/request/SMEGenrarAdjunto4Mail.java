package pe.interseguro.siv.common.dto.request;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SMEGenrarAdjunto4Mail implements Serializable{
	String nombreArchivo;
	String rutaArchivo;
	List<SMEAdjunto4Mail> archivos;
}
