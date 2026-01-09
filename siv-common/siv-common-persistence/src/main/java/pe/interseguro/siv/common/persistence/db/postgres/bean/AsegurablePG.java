package pe.interseguro.siv.common.persistence.db.postgres.bean;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class AsegurablePG {
    private int id;
    private String login;
    private String cod;
    private String desc;
    private String apepat;
    private String nom;
    private String fenac;
    private String flagsex;
    private String flagfum;
    private String nrodoc;
    private String tipdoc;
    private String apemat;
    private String profes;
    private String flagigv;
    private String correoAgente;
    //    NombreAgente
    private String nomAge;
    private OtroAsegurable[] otros;

    // para datos de cotizacion y asegurable con crm
    private String idCrm;
    private int idCot;
    private String idCotCrm;
    private String idOporCrm;
    private boolean flagUpdate;
    private String idTerceroCumulo;
}
