package pe.interseguro.siv.common.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class AcpPDFCuentaBean implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = -8788992710719475114L;
	@JsonProperty("check_tipo_cuenta_ahorro")
	private boolean checkTipoCuentaAhorro;
	@JsonProperty("check_tipo_cuenta_corriente")
	private boolean checkTipoCuentaCorriente;
	@JsonProperty("check_tipo_cuenta_otro")
	private boolean checkTipoCuentaOtro;
	@JsonProperty("check_banco_continental")
	private boolean checkBancoContinental;
	@JsonProperty("check_banco_credito")
	private boolean checkBancoCredito;
	@JsonProperty("check_banco_interbank")
	private boolean checkBancoInterbank;
	@JsonProperty("check_banco_scotiabank")
	private boolean checkBancoScotiabank;
	@JsonProperty("check_banco_otro")
	private boolean checkBancoOtro;
	@JsonProperty("check_moneda_soles")
	private boolean checkMonedaSoles;
	@JsonProperty("check_moneda_dolares")
	private boolean checkMonedaDolares;
	@JsonProperty("numero_cuenta")
	private String numeroCuenta;
}
