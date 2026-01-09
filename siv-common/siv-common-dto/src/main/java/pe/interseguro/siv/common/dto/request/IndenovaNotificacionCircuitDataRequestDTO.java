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
public class IndenovaNotificacionCircuitDataRequestDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7811069036240601070L;
	private String subject;
	private Integer creatorId;
	private Date createdOn;

}
