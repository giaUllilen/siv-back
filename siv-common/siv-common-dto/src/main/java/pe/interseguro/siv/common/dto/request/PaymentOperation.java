package pe.interseguro.siv.common.dto.request;

/**
 * Operacion que identifica la accion a realizar para el flujo de pagos.
 * 
 * @author cpalagua
 * 
 * @TOKENIZE operación solo para PAGAR.
 * @CVC operación para PAGAR Y AFILIAR.
 * @ONLYCVC operación para solo AFILIAR.
 */

public enum PaymentOperation {
	
	TOKENIZE("PAGAR"), CVC("PAGAR Y AFILIAR"), ONLYCVC("AFILIAR"), NOT_ALLOWED("");
	
	private String key;
	
	private PaymentOperation (String key) {
		this.key = key;
	}
	
	public String getValue() {
		return key;
	}
	
	public static PaymentOperation fromValue(String key) {
        for (PaymentOperation b : PaymentOperation.values()) {
            if (b.key.equals(key)) {
                return b;
            }
        }
        return PaymentOperation.NOT_ALLOWED;
    }

}
