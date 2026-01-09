package pe.interseguro.siv.common.persistence.db.acsele.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.mapping.StatementType;
import org.springframework.stereotype.Repository;

import pe.interseguro.siv.common.persistence.db.acsele.bean.PolizaEstado;
import pe.interseguro.siv.common.persistence.db.acsele.bean.TipoCambio;

@Repository
public interface AcseleRepository {
	@Select(
			"SELECT vp.numeropoliza,vp.producto,vp.estado,vp.fechaultestado " + 
			"FROM interseguror.mir_operaciones_vida_poliza vp " + 
			"WHERE vp.asegurado_documento = #{documentoIdentidad} " + 
			"and vp.estado   IN ('Anulada', 'Caducada', 'Cancelada', 'Prorrogado', 'Saldada', 'Suspendida') " + 
			"AND ROUND(MONTHS_BETWEEN(sysdate,vp.fechaultestado),0) < 12"
		)
	@Results(value = {
		@Result(column = "producto", 					property = "producto"),
		@Result(column = "numeropoliza", 				property = "numeropoliza"),
		@Result(column = "estado", 						property = "estado"),
		@Result(column = "fechaultestado", 				property = "fechaproceso")
	})
	public List<PolizaEstado> obtenerPolizaEstado(@Param("documentoIdentidad") String documentoIdentidad);
	
	/*@Select(
			"SELECT currencyoriginid, currencytargetid, daterate, rate FROM interseguro.currencyrate " +
			"WHERE daterate = TO_DATE(#{fecha},'DD/MM/YYYY')" +
			"ORDER BY daterate DESC"
		)*/
	@Select(
			"SELECT '2123' as CURRENCYORIGINID, '2163' as CURRENCYTARGETID, fechainput as daterate, dolasolinput as rate " +
			"FROM TDTasaCambSBS t " +
			"WHERE TO_CHAR(TO_DATE(fechainput, 'YYYY-MM-DD'), 'YYYY-MM') = TO_CHAR(SYSDATE, 'YYYY-MM') " +
			"UNION " + 
			"SELECT '2163' as CURRENCYORIGINID, '2123' as CURRENCYTARGETID, fechainput as daterate, soladolinput as rate " +
			"FROM TDTasaCambSBS t " +
			"WHERE TO_CHAR(TO_DATE(fechainput, 'YYYY-MM-DD'), 'YYYY-MM') = TO_CHAR(SYSDATE, 'YYYY-MM')"
		)
	@Results(value = {
		@Result(column = "currencyoriginid", 			property = "monedaOrigen"),
		@Result(column = "currencytargetid", 			property = "monedaDestino"),
		@Result(column = "daterate", 					property = "fecha"),
		@Result(column = "rate", 						property = "conversion")
	})
	public List<TipoCambio> obtenerTipoCambio();
	
	@Select("{CALL PKG_SIV_POLIZA.OBTENER_DATA_POLIZA(" +
			"#{P_NUMERO_DOCUMENTO, 				mode=IN, jdbcType=VARCHAR}, " +
			"#{CURSOR_POLIZAS, 					mode=OUT, jdbcType=CURSOR, resultMap=resultCursorPolizas}, " +
			"#{P_ERR_MENSAJE, 					mode=OUT, jdbcType=VARCHAR}, " +
			"#{P_ERR_CODE, 						mode=OUT, jdbcType=INTEGER} " +
		")}")
	@Options(statementType = StatementType.CALLABLE)
	public void obtenerPolizas(Map<String ,Object> map);
}
