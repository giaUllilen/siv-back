package pe.interseguro.siv.common.persistence.rest.global.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrarAfiliacionTraspasoRequest implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Long p_numeropropuesta;
	private Long p_numeropropuesta_new;
	private String p_moneda;
	private String p_codperiodopago;
	private Float p_valprimaformapago;
}
