package pe.interseguro.siv.common.dto.response;

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
public class ADNPotencialDTResponseDTO implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = -7459972898038397563L;

	private String id;
	private String contactId;
	private String leadSource;
	private String salesStage;
	private String fechaEnvioComercial;
}
