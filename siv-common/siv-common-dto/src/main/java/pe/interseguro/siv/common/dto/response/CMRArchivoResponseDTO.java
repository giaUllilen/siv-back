package pe.interseguro.siv.common.dto.response;

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
public class CMRArchivoResponseDTO implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name; 
	private String fileDateConvert; 
	private String file; 

}
