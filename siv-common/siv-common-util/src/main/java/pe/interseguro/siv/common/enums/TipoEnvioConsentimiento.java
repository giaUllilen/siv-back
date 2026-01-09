package pe.interseguro.siv.common.enums;

public enum TipoEnvioConsentimiento {
	MAIL(1),
	SMS(2),
	WHATSAPP(3);
	
	private Integer codigo;
	
	
	private TipoEnvioConsentimiento(Integer codigo) {
		this.codigo = codigo;
	}
	public Integer getCodigo() {
		return codigo;
	}
	
	public static TipoEnvioConsentimiento parse(int id) {
		TipoEnvioConsentimiento tipo = null; // Default
        for (TipoEnvioConsentimiento item : TipoEnvioConsentimiento.values()) {
            if (item.getCodigo() == id) {
            	tipo = item;
                break;
            }
        }
        return tipo;
    }
}
