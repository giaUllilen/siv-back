package pe.interseguro.siv.common.persistence.db.mysql.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Perfil {
	
	private Long id;
	private String codigo;
	private String nombre;
}
