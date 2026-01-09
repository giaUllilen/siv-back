/**
 * 
 */
package pe.interseguro.siv.common.dto.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Clase DTO Base para las peticiones (request) al REST API
 * 
 * @author ti-is
 *
 */
@Getter
@Setter
public class BaseRequestDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7497882177722112586L;

	private String device;
	private String os;
	private String path;
	private String method;
}
