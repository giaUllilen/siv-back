package pe.interseguro.siv.common.dto.request;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class IndenovaNotificacionEventDataRequestDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3772852832598424461L;
	private String circuitId;
	private String eventType;
	private IndenovaNotificacionActorRequestDTO actor;
	private IndenovaNotificacionCircuitDataRequestDTO circuitData;
	
}
