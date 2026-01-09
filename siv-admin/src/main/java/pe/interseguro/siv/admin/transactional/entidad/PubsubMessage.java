package pe.interseguro.siv.admin.transactional.entidad;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PubsubMessage implements Serializable {
	private String estadoPubSubSend;
	private String numeroPoliza;
	private String numeroPropuesta;

}