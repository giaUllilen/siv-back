package pe.interseguro.siv.common.dto.request;

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
public class IndenovaNotificacionActorRequestDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1973896805102479474L;
	private String name;
	private String email;
	private String identifier;

}
