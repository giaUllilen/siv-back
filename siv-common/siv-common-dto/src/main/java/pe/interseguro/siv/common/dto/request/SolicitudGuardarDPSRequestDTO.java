package pe.interseguro.siv.common.dto.request;

import java.io.Serializable;
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
public class SolicitudGuardarDPSRequestDTO implements Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -1523356469781132247L;
	
	private Float estatura;
    private Float peso;
    private Float imc;
    private Integer pesoVariacion;
    private Integer pesoAumentoDisminuyo;
    private Float pesoCantidad;
    private String pesoMotivo;
    private Integer fumador;
    private String fumadorCantidad;
    private String fumadorFrecuencia;
    private Integer drogas;
    private String drogasFecha;
    private Integer alcohol;
    private String alcoholCantidad;
    private String alcoholFrecuencia;
    private List<SolicitudGuardarDPSPreguntaRequestDTO> preguntas;

}
