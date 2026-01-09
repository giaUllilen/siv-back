package pe.interseguro.siv.common.bean;

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
public class DocumentoCotizacionRequerimiento implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3033386578174908548L;
	
	private String item;
	private String codigo;
	private String descripcion;
}
