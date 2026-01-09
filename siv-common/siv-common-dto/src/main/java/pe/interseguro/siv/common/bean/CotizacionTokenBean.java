package pe.interseguro.siv.common.bean;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class CotizacionTokenBean {
	private String fechaCotizacion;
	private String numeroCotizacion;
	private String tipoDocumentoCliente;
    private String numeroDocumentoCliente;
	private String nombreCliente;
	private String fechaNacimiento;
	private String edadActuarial;
	private String idUsuarioCrm;
	private String nombreUsuario;
	private String sexo;
	private String rol;
	private String idOportunidadCrm;
	private String idCotizacionCrm;
	private String cumuloMonto;
	private String cumuloMoneda;
	private String cumuloMontoSoles;
	private String flagIGV;

	private String usuarioLogin;
	private String agenteCorreo;
	private String agenteNumVendedor;
	private String agenteIdCRM;
	private String fumador;
	private String idTercero;
	private String producto;
	private String tipoCambio;

	private String habilitarFA;
}
