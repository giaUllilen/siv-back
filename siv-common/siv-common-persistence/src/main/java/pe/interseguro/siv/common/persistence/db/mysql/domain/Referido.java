package pe.interseguro.siv.common.persistence.db.mysql.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "referido", catalog = "siv_db")
public class Referido implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5619175634134477585L;
	
	private Long idReferido;
	private Persona persona;
	private String nombres;
	private String telefono;
	private String usuarioCrea;
	private Date fechaCrea;
	private String usuarioModif;
	private String fechaModif;
	
	public Referido() {}
	
	public Referido(Long idReferido, Persona persona, String nombres, String telefono) {
		this.idReferido = idReferido;
		this.persona = persona;
		this.nombres = nombres;
		this.telefono = telefono;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id_referido", unique = true, nullable = false)
	public Long getIdReferido() {
		return idReferido;
	}

	public void setIdReferido(Long idReferido) {
		this.idReferido = idReferido;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_persona", nullable = false)
	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	@Column(name = "nombres", length = 200)
	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	@Column(name = "telefono", length = 20)
	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	@Column(name = "usuario_crea", length = 50)
	public String getUsuarioCrea() {
		return usuarioCrea;
	}

	public void setUsuarioCrea(String usuarioCrea) {
		this.usuarioCrea = usuarioCrea;
	}

	@Column(name = "fecha_crea")
	public Date getFechaCrea() {
		return fechaCrea;
	}

	public void setFechaCrea(Date fechaCrea) {
		this.fechaCrea = fechaCrea;
	}

	@Column(name = "usuario_modif", length = 50)
	public String getUsuarioModif() {
		return usuarioModif;
	}

	public void setUsuarioModif(String usuarioModif) {
		this.usuarioModif = usuarioModif;
	}

	@Column(name = "fecha_modif", length = 45)
	public String getFechaModif() {
		return fechaModif;
	}

	public void setFechaModif(String fechaModif) {
		this.fechaModif = fechaModif;
	}
	
}
