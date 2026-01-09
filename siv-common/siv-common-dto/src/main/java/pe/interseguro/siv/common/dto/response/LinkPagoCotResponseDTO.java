package pe.interseguro.siv.common.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class LinkPagoCotResponseDTO extends BaseResponseDTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7392470339007516089L;
	
	private String numero_cotizacion;
	private String link_pago;
	private String link_recargo;

	
}
