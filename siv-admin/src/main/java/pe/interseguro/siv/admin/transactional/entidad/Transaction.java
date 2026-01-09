package pe.interseguro.siv.admin.transactional.entidad;

import java.util.Date;

import lombok.Data;

@Data
public class Transaction {

	private String transactionID;
	private String action;
	private String paycard;
	private String cardbrand;
	private String transactionDate;
	private Date transactionDate2;
	private String status;
	private double amount;
	private String subscriptionId;
	private String providerId;
	private String passarella;
	private String purchaseNumber;
	private String creationToken;
	private int viaCobro;
	private String moneda;
	private String tipoCuenta;
	private String cuentaTarjeta;

}
