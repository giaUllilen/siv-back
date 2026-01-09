package pe.interseguro.siv.common.dto.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NiubizTokenizerCardResponseDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4202089695308678207L;
	private String id;
	private String creationToken;
	private String resourceId;
	private String number;
	private String brand;
	private String type;
	private String category;
	private Boolean active;
}
