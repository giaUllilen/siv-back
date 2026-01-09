package pe.interseguro.siv.common.persistence.db.mysql.domain;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "solicitud_agente", catalog = "siv_db")
public class SolicitudAgente implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2442484968328422860L;

	private Long idSolicitudAgente;
	private String codigoAgente;
	private String numeroDocumento;
	private String nombreAgente;
	
	public SolicitudAgente() {}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "idsolicitud_agente", unique = true, nullable = false)
	public Long getIdSolicitudAgente() {
		return idSolicitudAgente;
	}
	public void setIdSolicitudAgente(Long idSolicitudAgente) {
		this.idSolicitudAgente = idSolicitudAgente;
	}
	
	@Column(name = "codigo_agente", insertable = false, updatable = false)
	public String getCodigoAgente() {
		return codigoAgente;
	}
	public void setCodigoAgente(String codigoAgente) {
		this.codigoAgente = codigoAgente;
	}
	
	@Column(name = "numero_documento", insertable = false, updatable = false)
	public String getNumeroDocumento() {
		return numeroDocumento;
	}
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}
	
	@Column(name = "nombre_agente", insertable = false, updatable = false)
	public String getNombreAgente() {
		return nombreAgente;
	}
	public void setNombreAgente(String nombreAgente) {
		this.nombreAgente = nombreAgente;
	}
	
}
