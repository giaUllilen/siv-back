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
public class CotizaUrlResponse extends BaseResponseDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7106089827226550176L;
	private Boolean result;
	private String URL;
	private String message;
}
