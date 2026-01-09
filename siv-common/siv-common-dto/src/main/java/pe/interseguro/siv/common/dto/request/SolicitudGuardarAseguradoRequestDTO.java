package pe.interseguro.siv.common.dto.request;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)

public class SolicitudGuardarAseguradoRequestDTO implements Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -1690226137707164696L;
	
	private String estadoCivil;
    private String nacionalidad;
    private String direccionTipo;
    private String direccionNombreVia;
    private String direccionNroMz;
    private String direccionInterior;
    private String direccionUrbanizacion;
    private String direccionDepartamento;
    private String direccionProvincia;
    private String direccionDistrito;
    private String centroTrabajo;
    private String actividadEconomica;
    private String lugarTrabajo;
    private String ingresoMoneda;
    private Integer ingresoValor;
    private String profesion;
    private String profesionDetalle;
    private String esPEP;
    private String esSujetoObligado;

}
