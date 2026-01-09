package pe.interseguro.siv.common.dto.request;

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
public class ReprocesoRequestDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1401282215473383741L;

	private List<String> cotizaciones;
}
