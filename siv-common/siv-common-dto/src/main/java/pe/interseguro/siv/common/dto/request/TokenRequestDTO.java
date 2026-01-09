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
public class TokenRequestDTO extends BaseRequestDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6369430183364278491L;
	
	private String idUsuarioCrm;
	private String nombreUsuario;
    private String rol;
    private String tipoDocumentoCliente;
    private String numeroDocumentoCliente;
    private String nombreCliente;
    private String edadActuarial;
    private String fechaNacimiento;
    private String sexo;
    private String numeroCotizacion;
    private String fechaCotizacion;
    private String idOportunidadCrm;
    private String idCotizacionCrm;
    private String flagIGV;
    
	private String token;
    private String producto;
	
	private String usuarioLogin;
	private String agenteCorreo;
	private String agenteNumVendedor;
	private String agenteIdCRM;

    private String habilitarFA;
}
