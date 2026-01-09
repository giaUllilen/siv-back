package pe.interseguro.siv.common.bean;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class DocumentoADN {
	private String tipo_documento;
	private String documento;
	private String nombres;
	private String apellido_paterno;
	private String apellido_materno;
	private String nacimiento;
	private String sexo;
	private String telefono;
	private String fumador;
	private String email;
	private String profesion;
	private String actividad;
	private String check_educacion;
	private String check_proyectos;
	private String check_jubilacion;
	private String titular_ingreso;
	private String titular_anios;
	private String titular_porcentaje;
	private String titular_total;
	private String ahorros;
	private String check_ahorros;
	private String propiedades;
	private String check_propiedades;
	private String vehiculos;
	private String check_vehiculos;
	private String seguro_vida;
	private String check_seguro_vida;
	private String seguro_ley;
	private String check_seguro_ley;
	private String capital_proteccion;
	private String respaldo_economico;
	private String afp;
	private String check_afp;
	private String capital_proteger;
	private String adicional;
	private String nuevo_capital;
	private String fecha;
	private String hora;
	private String tipoCasa;
	private String tieneVehiculo;
	private String tieneAFP;
	private String tieneSeguroVida;
}
