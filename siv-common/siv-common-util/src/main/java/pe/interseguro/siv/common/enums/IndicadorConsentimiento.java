package pe.interseguro.siv.common.enums;

public enum IndicadorConsentimiento {
	SI("S"),
	NO("N");
	
	private String codigo;
	
	
	private IndicadorConsentimiento(String codigo) {
		this.codigo = codigo;
	}
	public String getCodigo() {
		return codigo;
	}
	
	public static IndicadorConsentimiento parse(String id) {
		IndicadorConsentimiento tipo = null; // Default
        for (IndicadorConsentimiento item : IndicadorConsentimiento.values()) {
            if (item.getCodigo().equals(id)) {
            	tipo = item;
                break;
            }
        }
        return tipo;
    }
}
