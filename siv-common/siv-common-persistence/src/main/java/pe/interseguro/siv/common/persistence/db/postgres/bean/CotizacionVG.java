package pe.interseguro.siv.common.persistence.db.postgres.bean;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class CotizacionVG {
    private String anioVigencia;
    private String valorRescate;
    private String valorSaldado;
    private String aniosCoberturas;
    private String mesesCobertura;
    private String valorProrrogado;
}
