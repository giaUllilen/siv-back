package pe.interseguro.siv.common.persistence.db.postgres.repository;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import pe.interseguro.siv.common.persistence.db.postgres.bean.*;

public interface CotizacionRepository {

	@Select(
			"select c.id as nro_cotizacion, c.cot_date fecha_cotizacion, c.prod_id producto_id, " +
			"p.valor producto_nombre, per_pag as periodo_pago, " +
			"m.valor_flex moneda_simbolo, m.valor_aux as moneda, pri_bru prima_bruta, pri_tot_pago prima_pago_total, " +
			"cl.nu_doc numero_documento, '' as grupo_familiar, "+
			"subp.codigo as subplan_id, c.opcion_vida as subplan, c.crm_opor_id as crm_oportunidad_id, c.crm_cot_id as crm_cotizador_id "+
			"from cvw_cot c " + 
			"join multitabla_pg p on p.codigo = c.prod_id and p.codigo_tabla='001' " +
			"join cvw_cli cl on cl.id = c.cli_id " +
			"join multitabla_pg m on m.codigo = c.mon_id " +
			"inner join multitabla_pg pla on pla.codigo = c.plan_id and pla.codigo_tabla='002' " +
			"left join multitabla_pg subp on subp.valor = c.opcion_vida and subp.codigo_tabla='003' "+
			"where c.prod_id is not null and cl.nu_doc = #{documento} and p.valor = #{producto} " +
			"and c.cot_date + INTERVAL '1 month' >= CURRENT_DATE " +
			"order by c.cot_date desc, c.id desc" 
	)
	@Results(value = {
			@Result(column = "nro_cotizacion", property = "nroCotizacion"),
			@Result(column = "fecha_cotizacion", property = "fechaCotizacion"),
			@Result(column = "producto_id", property = "productoId"),
			@Result(column = "producto_nombre", property = "productoNombre"),
			@Result(column = "periodo_pago", property = "periodoPago"),
			@Result(column = "moneda", property = "moneda"),
			@Result(column = "moneda_simbolo", property = "monedaSimbolo"),
			@Result(column = "prima_bruta", property = "primaBruta"),
			@Result(column = "prima_pago_total", property = "primaPagoTotal"),
			@Result(column = "numero_documento", property = "numeroDocumento"),
			@Result(column = "grupo_familiar", property = "grupoFamiliar"),
			@Result(column = "subplan_id", property = "subplanId"),
			@Result(column = "subplan", property = "subplan"),
			@Result(column = "crm_oportunidad_id", property = "crmOportunidadId"),
			@Result(column = "crm_cotizador_id", property = "crmCotizadorId")
	})
	public List<Cotizacion> listaDocumentoProducto(@Param("documento") String documento, @Param("producto") String producto);
	

	
	@Select(
			" select  DISTINCT  " +
					" cot.id as nro_cotizacion  " +
					" ,cot.cum as cumulo  " +
					" ,cot.igv as igv  " +
					" ,cot.tasa_cost_efe_anu as tcea  " +
					" ,cot.edad_act as edad  " +
					" ,cot.cot_date fecha_cotizacion " +
					" ,cot.mon_id as monedaId  " +
					" ,moneda.valor_aux as moneda " +
					" ,CASE WHEN cob.nom='FALLECIMIENTO' then det.cap else 0 end as monto_total_fondo_garantizado  " +
					" ,per_pag as frecuencia_pago_prima  " +
					" ,per_pri_pag as periodo_cobertura_anual  " +
					" ,per_pag_ben as nro_anualidades_pago_beneficio  " +
					" ,plan.codigo as plan_id  " +
					" ,plan.valor as plan  " +
					" ,case when replace(plan.valor,'í','i') ilike '%cuota%comodin%' then 'S' else 'N' end as plan_cuota_comodin  " +
					" ,case when plan.valor ilike '% cd %' then 'S' else 'N' end as plan_cuota_doble  " +
					" ,'x' as fumador  " +
					" ,cot.pri_tot_pago as prima_comercial_total  " +
					" ,cot.pri_bru as prima_comercial_anual  " +
					" ,(select valor from multitabla_pg where valor_aux = cot.per_pag and codigo_tabla='010') as factor_pago  " +
					" ,cot.pri_for_pag as prima_comercial  " +
					" ,subplan.codigo as subplan_id " +
					" ,cot.opcion_vida as subplan  " +
					" ,tir.valor as tir_garantizada  " +
					" ,cob.id as cobertura_id  " +
					" ,cob.nom as cobertura_nombre " +
					" ,case when cob.is_mand = '0' then '2' else '1' end as cobertura_tipo  " +
					" ,det.cap as cobertura_capital  " +
					" ,det.pri_cob as cobertura_prima  " +
					" ,cot.crm_opor_id as crm_oportunidad_id  " +
					" ,cot.crm_cot_id as crm_cotizador_id  " +
					" ,cot.per_cob as per_cob  " +
					" ,percob.valor_aux as nom_per_cob  " +
					" ,null as fe_nac  " +
					" ,null as nu_doc  " +
					" ,f.valor as factor  " +
					" ,null as sex  " +
					" from cvw_cot cot  " +
					" left join multitabla_pg f on cot.per_pag=f.valor_aux and f.codigo_tabla='010' " +
					" inner join cvw_cotd det on det.cot_id = cot.id  " +
					" inner join cvw_cob cob on cob.id = det.cob_id and is_ccv_ben='f'  " +
					" inner join multitabla_pg percob on cot.per_cob = percob.valor and percob.codigo_tabla='007' " +
					" inner join multitabla_pg plan on plan.codigo = cot.plan_id and plan.codigo_tabla='002' " +
					" inner join multitabla_pg moneda on moneda.codigo=cot.mon_id and moneda.codigo_tabla='008' " +
					" left join multitabla_pg subplan on subplan.valor=cot.opcion_vida and subplan.codigo_tabla='003' " +
					" left join multitabla_pg tir on tir.estado='1' and tir.codigo_padre=cot.mon_id and tir.codigo_tabla='009' " +
					" where cot.prod_id is not null AND cot.id = #{id}  " +
					" AND det.cap > 0  "
	)
	@Results(value = {
			@Result(column = "nro_cotizacion", property = "nroCotizacion"),
			@Result(column = "cumulo", property = "cumulo"),
			@Result(column = "edad", property = "edad"),
			@Result(column = "fecha_cotizacion", property = "fechaCotizacion"),
			@Result(column = "monedaId", property = "monedaId"),
			@Result(column = "moneda", property = "moneda"),
			@Result(column = "monto_total_fondo_garantizado", property = "montoTotalFondoGarantizado"),
			@Result(column = "frecuencia_pago_prima", property = "frecuenciaPagoPrima"),
			@Result(column = "periodo_cobertura_anual", property = "periodoCobertura"),
			@Result(column = "nro_anualidades_pago_beneficio", property = "nroAnualidadesPagoBeneficio"),
			@Result(column = "plan_id", property = "planId"),
			@Result(column = "plan", property = "plan"),
			@Result(column = "plan_cuota_comodin", property = "planCuotaComodin"),
			@Result(column = "plan_cuota_doble", property = "planCuotaDoble"),
			@Result(column = "fumador", property = "fumador"),
			@Result(column = "prima_comercial_anual", property = "primaComercialAnual"),
			@Result(column = "factor_pago", property = "factorPago"),
			@Result(column = "prima_comercial", property = "primaComercial"),
			@Result(column = "subplan_id", property = "subplanId"),
			@Result(column = "subplan", property = "subplan"),
			@Result(column = "tir_garantizada", property = "tirGarantizada"),
			@Result(column = "cobertura_id", property = "coberturaId"),
			@Result(column = "cobertura_nombre", property = "coberturaNombre"),
			@Result(column = "cobertura_tipo", property = "coberturaTipo"),
			@Result(column = "cobertura_capital", property = "coberturaCapital"),
			@Result(column = "cobertura_prima", property = "coberturaPrima"),
			@Result(column = "crm_oportunidad_id", property = "crmOportunidadId"),
			@Result(column = "crm_cotizador_id", property = "crmCotizadorId"),
			@Result(column = "per_cob", property = "per_cob"),
			@Result(column = "nom_per_cob", property = "nomPerCob"),
			@Result(column = "fe_nac", property = "feNac"),
			@Result(column = "sex", property = "sex"),
			@Result(column = "tcea", property = "tcea"),
			@Result(column = "igv", property = "igv"),
			@Result(column = "factor", property = "factor"),
			@Result(column = "prima_comercial_total", property = "primaComTotal"),
			@Result(column = "nu_doc", property = "numDoc")
	})
	public List<CotizacionDetalle> detalleCotizacion(@Param("id") Long id);

	@Select(
			"select DISTINCT * from listdetailcotcobreport(#{id})  "
	)
	@Results(value = {
			@Result(column = "nom", property = "nombre"),
			@Result(column = "cap", property = "capital"),
			@Result(column = "tasa_anual", property = "tasaAnual"),
			@Result(column = "pri_cob", property = "priCob"),
			@Result(column = "edadminingreso", property = "edadMinIngreso"),
			@Result(column = "edadmaxingreso", property = "edadMaxIngreso"),
			@Result(column = "edadmaxpermanencia", property = "edadMaxPermanencia"),
			@Result(column = "bold", property = "bold"),
			@Result(column = "ord", property = "ord"),
			@Result(column = "factor", property = "factor"),
			@Result(column = "item", property = "item"),
			@Result(column = "islead", property = "isLead"),
	})
	public List<CotizacionCobertura> detalleCotizacionCoberturas(@Param("id") int id);

	@Select(
			"select LPAD(fin_anio_vig, 2, '0') as fin_anio_vig, to_char(val_res::numeric,'99,999,990.99') as val_res,to_char(seg_sal::numeric,'99,999,990.99') as seg_sal, cob_anio, cob_mes,'-' as val_pro from cvw_cotv where cot_id = #{id} order by LPAD(fin_anio_vig, 2, '0')  "
	)
	@Results(value = {
			@Result(column = "fin_anio_vig", property = "anioVigencia"),
			@Result(column = "val_res", property = "valorRescate"),
			@Result(column = "seg_sal", property = "valorSaldado"),
			@Result(column = "cob_anio", property = "aniosCoberturas"),
			@Result(column = "cob_mes", property = "mesesCobertura"),
			@Result(column = "val_pro", property = "valorProrrogado")
	})
	public List<CotizacionVG> detalleCotizacionVg(@Param("id") int id);

	@Select(
"select to_char(row_number() over (order by id),'fm00') as item, fecha, monto::numeric,COALESCE((select case when opcion_vida = 'Seleccione' then NULL else 'Fondo Garantizado' end from cvw_cot where id = #{id}),'Fondo Garantizado') as label from cvw_cot_pagben where id_cot = #{id}::text "	)
	@Results(value = {
			@Result(column = "item", property = "item"),
			@Result(column = "fecha", property = "fecha"),
			@Result(column = "monto", property = "monto"),
			@Result(column = "label", property = "label"),
	})
	public List<CotizacionPagBeneficio> detalleCotizacionPagBen(@Param("id") int id);

	@Select(
			"select  " +
			"cot.id as nro_cotizacion " +
			",cot.cot_date fecha_cotizacion " +
			",cot.pri_bru as prima_comercial_anual " +
			",cot.pri_for_pag as prima_comercial " +
			",cot.opcion_vida as subplan " +
			",cot.crm_opor_id as crm_oportunidad_id "+
			",cot.crm_cot_id as crm_cotizador_id "+
			"from cvw_cot cot  " +
			"where cot.id = #{id} "
	)
	@Results(value = {
			@Result(column = "nro_cotizacion", property = "nroCotizacion"),
			@Result(column = "fecha_cotizacion", property = "fechaCotizacion"),
			@Result(column = "prima_comercial_anual", property = "primaComercialAnual"),
			@Result(column = "prima_comercial", property = "primaComercial"),
			@Result(column = "subplan", property = "subplan"),
			@Result(column = "crm_oportunidad_id", property = "crmOportunidadId"),
			@Result(column = "crm_cotizador_id", property = "crmCotizadorId")
	})
	public List<CotizacionDetalle> detalleCotizacion2(@Param("id") Long id);
	
	@Select(
			"select * from fn_listExmed(#{producto}, #{moneda}, #{edad}, #{cumulo}, #{capital1}, #{capital2}, #{tipocambio}) "
	)
	@Results(value = {
			@Result(column = "codreq", property = "codigo"),
			@Result(column = "descreq", property = "descripcion")
	})
	public List<ExigenciaMedica> exigenciasMedicas(
			@Param("producto") String producto,
			@Param("moneda") String moneda,
			@Param("edad") String edad,
			@Param("cumulo") String cumulo,
			@Param("capital1") String capital1,
			@Param("capital2") String capital2,
			@Param("tipocambio") BigDecimal tipocambio);
	
	@Update("UPDATE cvw_cot set mod_user = 'ADN Digital', mod_date = timeofday()::timestamptz," + 
			"crm_cot_id = #{crmCotizadorId} WHERE id = #{numeroCotizacion}")
	public void guardarIDCotizacionCRM(@Param("numeroCotizacion") Integer numeroCotizacion, 
			@Param("crmCotizadorId") String crmCotizadorId);

}