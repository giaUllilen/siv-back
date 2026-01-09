package pe.interseguro.siv.common.persistence.rest.global.response;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class obtenerPersonaResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonProperty("_id")
	private String _id; 
	
	@JsonProperty("ID_PERSONA")
	private String ID_PERSONA; 
	
	@JsonProperty("ID_ROL")
	private String ID_ROL; 
	
	@JsonProperty("ID_TIPO_DOCUMENTO")
	private String ID_TIPO_DOCUMENTO; 
	
	@JsonProperty("TIPO_DOCUMENTO")
	private String TIPO_DOCUMENTO; 
	
	@JsonProperty("NUMERO_DOCUMENTO")
	private String NUMERO_DOCUMENTO; 
	
	@JsonProperty("APELLIDO_PATERNO")
	private String APELLIDO_PATERNO; 
	
	@JsonProperty("APELLIDO_MATERNO")
	private String APELLIDO_MATERNO; 
	
	@JsonProperty("NOMBRE")
	private String NOMBRE; 
	
	@JsonProperty("RAZON_SOCIAL")
	private String RAZON_SOCIAL; 
	
	@JsonProperty("FECHA_NACIMIENTO")
	private Date FECHA_NACIMIENTO; 
	
	@JsonProperty("FECHA_FALLECIMIENTO")
	private String FECHA_FALLECIMIENTO; 
	
	@JsonProperty("ID_GENERO")
	private String ID_GENERO; 
	
	@JsonProperty("GENERO")
	private String GENERO; 
	
	@JsonProperty("ID_ESTADO_CIVIL")
	private String ID_ESTADO_CIVIL; 
	
	@JsonProperty("ESTADO_CIVIL")
	private String ESTADO_CIVIL; 
	
	@JsonProperty("PROFESION")
	private String PROFESION; 
	
	@JsonProperty("NACIONALIDAD")
	private String NACIONALIDAD; 
	
	@JsonProperty("CENTRO_TRABAJO")
	private String CENTRO_TRABAJO; 
	
	@JsonProperty("ID_CONDICION_FUMADOR")
	private String ID_CONDICION_FUMADOR; 
	
	@JsonProperty("CONDICION_FUMADOR")
	private String CONDICION_FUMADOR; 
	
	@JsonProperty("CUSPP")
	private String CUSPP; 
	
	@JsonProperty("AFP")
	private String AFP; 
	
	@JsonProperty("PARENTESCO")
	private String PARENTESCO; 
	
	@JsonProperty("ID_PADRE")
	private String ID_PADRE; 
	
	@JsonProperty("INGRESO_PROMEDIO_MENSUAL")
	private String INGRESO_PROMEDIO_MENSUAL; 
	
	@JsonProperty("CIC")
	private String CIC; 
	
	@JsonProperty("CORREO_VIDA")
	private String CORREO_VIDA; 
	
	@JsonProperty("CORREO_RENTA")
	private String CORREO_RENTA; 
	
	@JsonProperty("CORREO_DESGRAVAMEN")
	private String CORREO_DESGRAVAMEN; 
	
	@JsonProperty("CORREO_BANCA_SEGURO")
	private String CORREO_BANCA_SEGURO; 
	
	@JsonProperty("CORREO_MASIVO")
	private String CORREO_MASIVO; 
	
	@JsonProperty("CORREO_SOAT")
	private String CORREO_SOAT; 
	
	@JsonProperty("CORREO_VEHICULAR")
	private String CORREO_VEHICULAR; 
	
	@JsonProperty("CORREO_VIAJE")
	private String CORREO_VIAJE; 
	
	@JsonProperty("TELEFONO_VIDA")
	private String TELEFONO_VIDA; 
	
	@JsonProperty("TELEFONO_RENTA")
	private String TELEFONO_RENTA; 
	
	@JsonProperty("TELEFONO_DESGRAVAMEN")
	private String TELEFONO_DESGRAVAMEN; 
	
	@JsonProperty("TELEFONO_BANCA_SEGURO")
	private String TELEFONO_BANCA_SEGURO; 
	
	@JsonProperty("TELEFONO_MASIVO")
	private String TELEFONO_MASIVO; 
	
	@JsonProperty("TELEFONO_SOAT")
	private String TELEFONO_SOAT; 
	
	@JsonProperty("TELEFONO_VEHICULAR")
	private String TELEFONO_VEHICULAR; 
	
	@JsonProperty("TELEFONO_VIAJE")
	private String TELEFONO_VIAJE; 
	
	@JsonProperty("DIRECCION_VIDA")
	private String DIRECCION_VIDA; 
	
	@JsonProperty("DIRECCION_RENTA")
	private String DIRECCION_RENTA; 
	
	@JsonProperty("DIRECCION_DESGRAVAMEN")
	private String DIRECCION_DESGRAVAMEN; 
	
	@JsonProperty("DIRECCION_BANCA_SEGURO")
	private String DIRECCION_BANCA_SEGURO; 
	
	@JsonProperty("DIRECCION_MASIVO")
	private String DIRECCION_MASIVO; 
	
	@JsonProperty("DIRECCION_SOAT")
	private String DIRECCION_SOAT; 
	
	@JsonProperty("DIRECCION_VEHICULAR")
	private String DIRECCION_VEHICULAR; 
	
	@JsonProperty("FECHA_CREACION")
	private String FECHA_CREACION; 
	
	@JsonProperty("FECHA_MODIFICACION")
	private String FECHA_MODIFICACION; 
	
	@JsonProperty("TELEFONO_BUC")
	private String TELEFONO_BUC; 
	
	@JsonProperty("CORREO_BUC")
	private String CORREO_BUC; 
	
	@JsonProperty("DIRECCION_BUC")
	private String DIRECCION_BUC; 
}
