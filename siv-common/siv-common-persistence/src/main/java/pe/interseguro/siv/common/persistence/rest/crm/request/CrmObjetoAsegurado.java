package pe.interseguro.siv.common.persistence.rest.crm.request;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Setter;

import lombok.Getter;

@Getter
@Setter
public class CrmObjetoAsegurado implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4408093409578841500L;
	private String idIOConf;
    private String numeroObjAseg;
    private String descripcionIO;
    private String grupoFamiliar;
    private String tipoAseguradoAcc;
    private ArrayList<CrmCobertura> coberturas;
    private ArrayList<CrmParticipacion> participaciones;
}
