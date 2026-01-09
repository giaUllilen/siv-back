package pe.interseguro.siv.common.dto.response;

import java.io.Serializable;
import java.util.Date;
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
public class ObtenerDocumentoItemResponseDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3934429400707689197L;
	
	private String id_archivo;
	private String nombre_Archivo;
	
}
