package pe.interseguro.siv.common.dto.request;

import java.io.Serializable;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SMSRequest implements Serializable{
	String celular; 
	String mensaje;
}
