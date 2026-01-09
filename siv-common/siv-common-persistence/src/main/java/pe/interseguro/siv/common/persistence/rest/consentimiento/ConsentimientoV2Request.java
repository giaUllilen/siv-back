package pe.interseguro.siv.common.persistence.rest.consentimiento;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConsentimientoV2Request implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String documentType;
	private String documentNumber;
	private String business;
	private String policy;
	private String subtype;
	private String product;
	private String status;
	private Boolean onlyLatest;
	private Integer page;
	private Integer limit;
} 