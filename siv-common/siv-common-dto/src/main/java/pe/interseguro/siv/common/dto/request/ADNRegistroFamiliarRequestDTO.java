package pe.interseguro.siv.common.dto.request;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ADNRegistroFamiliarRequestDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 715466335149145429L;
	
	@NotNull
	@Range(min = 1L, max = 6L)	
	private Integer tipoRelacion;
	
	@NotBlank
	private String nombres;
	
	@NotNull
	private Integer edad;
	
	private String centroLaboral;
	
}
