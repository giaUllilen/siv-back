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
public class ADNUsuarioDTResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 597053658414275214L;
	
	private String id;
	private String user_name;
	private String first_name;
	private String last_name;
	private String roleid;
	private String rolename;
}
