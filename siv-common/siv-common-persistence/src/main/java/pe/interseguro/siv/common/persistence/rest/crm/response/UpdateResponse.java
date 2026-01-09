package pe.interseguro.siv.common.persistence.rest.crm.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import pe.interseguro.siv.common.persistence.rest.niubiz.response.BaseResponse;

@Getter
@Setter
public class UpdateResponse extends BaseResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4496478746937394204L;

	@JsonProperty("IdContacto")
	private String idContacto;
	
	@JsonProperty("IdCotizacion")
	private String idCotizacion;
	
	@JsonProperty("IdOportunidad")
	private String idOportunidad;
	
	@JsonProperty("IdUsuario")
	private String idUsuario;
	
	private String agencia;

	private String mensaje;

	@JsonProperty("mensaje_crm")
	private String mensajeCrm;

	@JsonProperty("num_vendedor")
	private String numVendedor;
	
	private String respuesta;
	
	
}
