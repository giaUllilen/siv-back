package pe.interseguro.siv.common.dto.request;

import java.io.Serializable;
import java.util.Date;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class IndenovaNotificacionRequestDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3585828547243703201L;
	private Date date;
	private int attempts;
	private String service;
	private IndenovaNotificacionEventDataRequestDTO eventData;
	private IndenovaNotificacionMetadataRequestDTO metadata;
	

}
