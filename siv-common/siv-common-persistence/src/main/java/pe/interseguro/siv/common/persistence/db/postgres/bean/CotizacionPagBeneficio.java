package pe.interseguro.siv.common.persistence.db.postgres.bean;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class CotizacionPagBeneficio {
    private String item;
    private String fecha;
    private double monto;
    private String label;
}
