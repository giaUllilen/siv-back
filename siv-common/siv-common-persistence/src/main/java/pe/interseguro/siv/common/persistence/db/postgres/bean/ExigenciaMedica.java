package pe.interseguro.siv.common.persistence.db.postgres.bean;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ExigenciaMedica {
	private String codigo;
	private String descripcion;
}
