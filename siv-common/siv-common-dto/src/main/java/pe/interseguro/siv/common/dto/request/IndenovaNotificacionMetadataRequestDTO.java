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
public class IndenovaNotificacionMetadataRequestDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6677439229318591608L;
	private String[] groups;
	private String id;
	private String user;

}
