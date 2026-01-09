package pe.interseguro.siv.common.bean;

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
public class CulqiErrorBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6275489507678505723L;
	private String object;
	private String type;
	private String charge_id;
	private String code;
	private String decline_code;
	private String merchant_message;
	private String user_message;
}
