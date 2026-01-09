package pe.interseguro.siv.common.dto.request;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CotizadorCrmObjetoAseguradoDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 234482019111049638L;
	
	private String idIOConf;
    private String numeroObjAseg;
    private String descripcionIO;
    private String grupoFamiliar;
    private String tipoAseguradoAcc;
    private ArrayList<CotizadorCrmCoberturaDTO> coberturas;
    private ArrayList<CotizadorCrmParticipacionDTO> participaciones;
}
