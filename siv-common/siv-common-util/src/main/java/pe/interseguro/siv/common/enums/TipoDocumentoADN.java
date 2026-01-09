package pe.interseguro.siv.common.enums;

public enum TipoDocumentoADN {
	DNI(1),
	RUC(11),
	CARNE_EXTRANJERIA(2);
	
	private Integer codigo;
	
	
	private TipoDocumentoADN(Integer codigo) {
		this.codigo = codigo;
	}
	public Integer getCodigo() {
		return codigo;
	}
	
	public static TipoDocumentoADN parse(int id) {
		TipoDocumentoADN tipo = null; // Default
        for (TipoDocumentoADN item : TipoDocumentoADN.values()) {
            if (item.getCodigo() == id) {
            	tipo = item;
                break;
            }
        }
        return tipo;
    }
}
