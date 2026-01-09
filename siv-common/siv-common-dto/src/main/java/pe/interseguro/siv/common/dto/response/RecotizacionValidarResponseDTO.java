package pe.interseguro.siv.common.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class RecotizacionValidarResponseDTO extends BaseResponseDTO {
    private static final long serialVersionUID = -8975202474261982294L;

    private String cotizacion;
    private int estado;
    private boolean puedeRecotizar;
}
