package pe.interseguro.siv.common.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class SolicitudPagoCargoRequestDTO extends BaseRequestDTO {/**
	 * 
	 */
	private static final long serialVersionUID = -2777126840947429556L;

	//private String compraID;
	private Long idSolicitud;
	private Double compraMonto;
	//private Boolean compraCaptura;
	private String compraMoneda;
	//private String compraDescripcion;
	//private Integer compraCuotas;
	//private String clienteDireccion;
	//private String clienteCiudad;
	//private String clienteNombres;
	//private String clienteApellidos;
	//private String clienteCelular;
	//private String clienteEmail;
	private String tarjetaNumero;
	private String tarjetaCcv;
	private String tarjetaVencimiento;
	private String usuarioLogin;
	private NiubizTokenizerCustomerRequestDTO customer;
	private String clientIp;
	private String purchaseNumber;
	private String idViaCobroPP;
}
