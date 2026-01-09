/**
 * 
 */
package pe.interseguro.siv.common.enums;

/**
 * Enum para identificar el profile spring boot
 * 
 * @author ti-is
 */
public enum ProfileEnum {

	PROFILE_DEV(1, "develop"),
	PROFILE_TST(2, "uat"),
	PROFILE_PRD(3, "master");
	
	private Integer codigo;
	private String perfil;
	
	
	private ProfileEnum(Integer codigo, String perfil) {
		this.codigo = codigo;
		this.perfil = perfil;
	}
	public Integer getCodigo() {
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public String getPerfil() {
		return perfil;
	}
	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}
	
	
}
