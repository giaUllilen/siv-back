package pe.interseguro.siv.common.persistence.rest.acsele.request;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class AcseleTercero implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5240234694413882978L;

	private TipoPersona tipoPersona;
}
