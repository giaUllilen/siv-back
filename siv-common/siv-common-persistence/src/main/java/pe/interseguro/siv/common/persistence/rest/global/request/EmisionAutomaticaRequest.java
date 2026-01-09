package pe.interseguro.siv.common.persistence.rest.global.request;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class EmisionAutomaticaRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long IdSolicitud;
	/*
	 * private EmisionAutomaticaEndosoRequest endoso; private
	 * List<ListEmisionAutomaticaDynamicDataRequest> dynamicData;
	 */

}
