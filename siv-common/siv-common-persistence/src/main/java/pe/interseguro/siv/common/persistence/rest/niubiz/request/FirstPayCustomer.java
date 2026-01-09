package pe.interseguro.siv.common.persistence.rest.niubiz.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FirstPayCustomer {
	private String email;
	private String documentType;
	private String documentNumber;
	private String country;
	private String name;
	private String lastName;
	private String motherLastName;
	private String phone;
	private String address;
	private String addressCity;
	private FirstPayCustomMetadata metadata;
}
