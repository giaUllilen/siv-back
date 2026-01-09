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
public class TipoCambio {
	private String moneda;
	private String fecha;
	private String valor;
}
