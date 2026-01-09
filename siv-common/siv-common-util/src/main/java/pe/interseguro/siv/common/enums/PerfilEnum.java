/**
 * 
 */
package pe.interseguro.siv.common.enums;

/**
 * Enum para identificar el profile spring boot
 * 
 * @author ti-is
 */
public enum PerfilEnum {

	PERFIL_AGENTE("1", "Agente"),
	PERFIL_AUDITORIA("2", "Auditoría"),
	PERFIL_OPERACIONES("3", "Operaciones");
	
	private String codigo;
	private String perfil;
	
	private PerfilEnum(String codigo, String perfil) {
		this.codigo = codigo;
		this.perfil = perfil;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getPerfil() {
		return perfil;
	}
	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}	
}
