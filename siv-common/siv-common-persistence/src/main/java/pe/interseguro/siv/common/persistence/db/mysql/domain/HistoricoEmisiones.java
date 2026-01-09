package pe.interseguro.siv.common.persistence.db.mysql.domain;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "historico_emisiones", catalog = "siv_db")
public class HistoricoEmisiones implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private Long idHistoricoEmisiones;	
	private Integer numeroPropuesta;
	private String numeroPoliza;
	private Integer estadoEmision;

	public HistoricoEmisiones() {
	}

	public HistoricoEmisiones(Integer numeroPropuesta, String numeroPoliza, Integer estadoEmision) {
		
		this.numeroPropuesta = numeroPropuesta;
		this.numeroPoliza = numeroPoliza;		
		this.estadoEmision = estadoEmision;
		
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "id_historico_emisiones", unique = true, nullable = false)
	public Long getIdHistoricoEmisiones() {
		return this.idHistoricoEmisiones;
	}

	public void setIdHistoricoEmisiones(Long idHistoricoEmisiones) {
		this.idHistoricoEmisiones = idHistoricoEmisiones;
	}

	@Column(name = "numero_propuesta")
	public Integer getNumeroPropuesta() {
		return this.numeroPropuesta;
	}

	public void setNumeroPropuesta(Integer numeroPropuesta) {
		this.numeroPropuesta = numeroPropuesta;
	}

	@Column(name = "numero_poliza", length = 20)
	public String getNumeroPoliza() {
		return this.numeroPoliza;
	}

	public void setNumeroPoliza(String numeroPoliza) {
		this.numeroPoliza = numeroPoliza;
	}
	@Column(name = "estado_emision")
	public Integer getEstadoEmision() {
		return this.estadoEmision;
	}

	public void setEstadoEmision(Integer estadoEmision) {
		this.estadoEmision = estadoEmision;
	}

	
}