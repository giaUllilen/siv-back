/**
 * 
 */
package  pe.interseguro.siv.common.enums;

/**
 * Enum para tipificar las tablas de parametria
 * 
 * @author ti-is
 */
public enum TablaEnum {

	TABLA_TIPO_DOCUMENTO(1, "001"),
	TABLA_GENERO(2, "002"),
	TABLA_PROFESION(3, "003"),
	TABLA_ACTIVIDAD_ECONOMICA(4, "004"),
	TABLA_FUMADOR(5, "005"),
	TABLA_TIPO_RELACION(6, "006"),
	TABLA_ESTADO_GENERAL(7, "007"),
	TABLA_PARAMETRO_ADN(8, "008"),
	TABLA_PERFIL(9, "009"),
	TABLA_ESTADO_CIVIL(12, "012"),
	TABLA_NACIONALIDAD(10, "010"),
	TABLA_TIPO_DIRECCION(13, "013"),
	TABLA_DEPARTAMENTO(27, "027"),
	TABLA_PROVINCIA(28, "028"),
	TABLA_DISTRITO(29, "029"),
	TABLA_MONEDA(29, "014"),
	TABLA_TIPO_CUENTA(29, "029"),
	TABLA_ENTIDAD_BANCARIA(29, "029"),
	TABLA_OPERADOR_FINANCIERO(29, "029"),
	TABLA_CONFIGURACION_SOLICITUD(24, "024"),
	TABLA_LUGAR_TRABAJO(25, "057"),
	TABLA_FRECUENCIA(26, "074"),
	TABLA_CANT_FRECUENCIA(27, "075");
	
	private Integer codigo;
	private String codigoTabla;
	
	
	private TablaEnum(Integer codigo, String codigoTabla) {
		this.setCodigo(codigo);
		this.setCodigoTabla(codigoTabla);
	}

	public Integer getCodigo() {
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public String getCodigoTabla() {
		return codigoTabla;
	}
	public void setCodigoTabla(String codigoTabla) {
		this.codigoTabla = codigoTabla;
	}
	
	
}
