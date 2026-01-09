package pe.interseguro.siv.common.persistence.rest.global.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidarPlaftRequest implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8143774760668500446L;
	
	private String documento;
	
}
