package pe.interseguro.siv.common.persistence.db.sitc.repository;

import java.util.Map;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;
import org.springframework.stereotype.Repository;

import pe.interseguro.siv.common.persistence.db.sitc.bean.TarjetaPrimaRecurrente;
import pe.interseguro.siv.common.persistence.db.sitc.bean.TarjetaPrimeraPrima;

@Repository
public interface SitcRepository {

	@Select(
			"SELECT t.id_afiliacion_propuesta,  t.tipo_via_cobro,  t.via_cobro,  t.moneda, " +
					//"TO_CHAR(sys.enc_dec.decrypt( t.numero)) as tarjeta, " +
					"t.numero as tarjeta, " +
					"TO_CHAR( t.fecha_vencimiento, 'mm/yy') as fecha_vencimiento, " +
					"a.id_tarjeta " +
					"FROM sampmed.tarjeta_primeraprima t " +
					"join sampmed.afiliacion_propuesta a on t.id_afiliacion_propuesta = a.id_afiliacion_propuesta " +
					"WHERE  a.num_propuesta = #{idAfiliacionPropuesta}"
	)
	@Results(value = {
			@Result(column = "id_afiliacion_propuesta", 		property = "idAfiliacionPropuesta"),
			@Result(column = "tipo_via_cobro", 					property = "tipoViaCobro"),
			@Result(column = "via_cobro", 						property = "viaCobro"),
			@Result(column = "moneda", 							property = "moneda"),
			@Result(column = "tarjeta", 						property = "tarjeta"),
			@Result(column = "fecha_vencimiento", 				property = "tarjetaVencimiento"),
			@Result(column = "id_tarjeta", 						property = "idTarjetaRecurrenteSitc")
	})
	public TarjetaPrimeraPrima obtenerTarjetaPrimeraPrima(@Param("idAfiliacionPropuesta") String idAfiliacionPropuesta);

	@Select(
			"select " +
					//"CASE WHEN t.via_cobro = '2' THEN TO_CHAR(sys.enc_dec.decrypt(t.numero_tarjeta)) ELSE TO_CHAR(t.numero_tarjeta) END as tarjeta" +
					"t.numero_tarjeta as tarjeta, " +
					"TO_CHAR(t.fecha_vencimiento, 'mm/yy') as fecha_vencimiento, " +
					"t.via_cobro, t.medio_pago, t.moneda, t.id_tipo_cuenta " +
					"from sampmed.tarjeta t " +
					"join sampmed.afiliacion_propuesta a on a.id_tarjeta = t.id_tarjeta " +
					"where a.num_propuesta = #{idAfiliacionPropuesta}"
	)
	@Results(value = {
			@Result(column = "tarjeta", 					property = "tarjeta"),
			@Result(column = "fecha_vencimiento", 			property = "tarjetaVencimiento"),
			@Result(column = "via_cobro", 					property = "viaCobro"),
			@Result(column = "medio_pago", 					property = "medioPago"),
			@Result(column = "moneda", 						property = "moneda"),
			@Result(column = "id_tipo_cuenta", 				property = "tipoCuenta")
	})
	public TarjetaPrimaRecurrente obtenerTarjetaPrimaRecurrente(@Param("idAfiliacionPropuesta") String idAfiliacionPropuesta);

	@Select(value="{CALL VIAS_COBRO.sp_InsertarAfiliacionTraspaso(" +
			"#{p_numeroPropuesta, 				mode=IN, jdbcType=INTEGER}, " +
			"#{p_numeroPropuesta_new, 					mode=IN, jdbcType=INTEGER}, " +
			"#{p_moneda, 						mode=IN, jdbcType=VARCHAR}, " +
			"#{p_codPeriodoPago, 						mode=IN, jdbcType=VARCHAR}, " +
			"#{p_valPrimaFormaPago, 						mode=IN, jdbcType=INTEGER}, " +
			"#{p_retorno, 							mode=OUT, jdbcType=INTEGER} " +
			")}")
	@Options(statementType = StatementType.CALLABLE)
	public void registrarAfiliacionTraspaso(Map<String ,Object> map);
}
