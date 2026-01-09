package pe.interseguro.siv.common.persistence.db.postgres.bean;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class CotizacionCobertura {
    private String nombre;
    private String capital;
    private String tasaAnual;
    private String priCob;
    private String edadMinIngreso;
    private String edadMaxIngreso;
    private String edadMaxPermanencia;
    private boolean bold;
    private String ord;
    private String factor;
    private String item;
    private boolean isLead;
}
