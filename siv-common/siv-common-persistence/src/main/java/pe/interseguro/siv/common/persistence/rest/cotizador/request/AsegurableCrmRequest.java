package pe.interseguro.siv.common.persistence.rest.cotizador.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AsegurableCrmRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6748875120888469498L;

	private int id;
	private String cod;
	private String login;
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
    private String nomAge;
    private String otros;
    private String idCrm;
    private int idCot;
    private String idCotCrm;
    private String idOporCrm;
    private boolean flagUpdate;
    private String idTerceroCumulo;
}
