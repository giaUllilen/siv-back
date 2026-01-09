package pe.interseguro.siv.common.persistence.db.postgres.bean;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class OtroAsegurable {
    private String apepat;
    private String nom;
    private String rel;
    private String fenac;
    private String flagsex;
    private String nrodoc;
    private String tipdoc;
    private String apemat;

    // para datos de otro asegurable con crm
    private String idCrm;
}
