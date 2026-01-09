package pe.interseguro.siv.common.persistence.db.mysql.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "event_log", catalog = "siv_db")
public class EventLog implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4955768893522689043L;
	
	private Long idLog;
	private String screen;
	private String code;
	private String path;
	private String message;
	private Date eventDate; 
	private String errorDetail;
	private String comment;
	private String agent;
	private String device;
	private String os;
	
	public EventLog() {}
	
	public EventLog (String screen, String code, String path, String message, String errorDetail, 
			String comment, String agent, String device, String os) {
		this.screen = screen;
		this.code = code;
		this.path = path;
		this.message = message;
		this.errorDetail = errorDetail;
		this.comment = comment;
		this.agent = agent;
		this.device = device;
		this.os = os;
		this.eventDate = new Date();
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id_log", unique = true, nullable = false)
	public Long getIdLog() {
		return idLog;
	}
	public void setIdLog(Long idLog) {
		this.idLog = idLog;
	}
	@Column(name = "screen", nullable = false, length = 50)
	public String getScreen() {
		return screen;
	}
	public void setScreen(String screen) {
		this.screen = screen;
	}
	@Column(name = "code", nullable = false, length = 10)
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@Column(name = "path", nullable = false, length = 150)
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	@Column(name = "message", nullable = false, length = 150)
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "event_date", length = 19)
	public Date getEventDate() {
		return eventDate;
	}
	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}
	@Column(name = "error_detail")
	public String getErrorDetail() {
		return errorDetail;
	}
	public void setErrorDetail(String errorDetail) {
		this.errorDetail = errorDetail;
	}
	@Column(name = "comment")
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	@Column(name = "agent_register", nullable = false, length = 50)
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	@Column(name = "device", length = 20)
	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}
	@Column(name = "os", length = 20)
	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}
}
