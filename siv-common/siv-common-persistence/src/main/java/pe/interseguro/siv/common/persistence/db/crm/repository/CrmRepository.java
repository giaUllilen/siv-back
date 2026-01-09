package pe.interseguro.siv.common.persistence.db.crm.repository;

import java.util.List;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import pe.interseguro.siv.common.persistence.db.crm.bean.ReporteRRVV;

@Repository
public interface CrmRepository {
	@Select(
			"select " +	
			"c.inter_cuspp as CUSPP," + 
			"c.inter_numerodedocumento as DocumentoProspecto," + 
			"c.FullName as Prospecto," + 
			"op.name as Oportunidad," + 
			"( select value " + 
			"from stringmap " + 
			"where ObjectTypeCode = 3 " + 
			"and attributename = 'inter_categoria' " + 
			"and attributevalue = op.inter_categoria " + 
			") as Categoria," + 
			"( select value " + 
			"from stringmap " + 
			"where ObjectTypeCode = 3 " + 
			"and attributename = 'inter_modulo' " + 
			"and attributevalue = op.inter_modulo " + 
			") as Modulo," + 
			"( select value " + 
			"from stringmap " + 
			"where ObjectTypeCode = 3 " + 
			"and attributename = 'inter_estadorv' " + 
			"and attributevalue = op.inter_estadorv " + 
			") as Estado," + 
			"inter_valorsaldocictotal as SaldoCICTotal," + 
			"DATEADD(hh, -5, c.birthdate) as FechaNacimientoProspecto," + 
			"DATEADD(hh, -5, c.inter_fechaasignacion) as FechaAsignacionProspecto," + 
			"c.inter_ram as RAMProspecto," + 
			"DATEADD(hh, -5, op.inter_fechaultimoaporte) as FechaUltimoAporte," + 
			"( select value " + 
			"from stringmap " + 
			"where ObjectTypeCode = 3 " + 
			"and attributename = 'inter_pronosticorv' " + 
			"and attributevalue = op.inter_pronosticorv " + 
			") as Pronostico," +
			"c.address2_line1 as DireccionParticularProspecto," + 
			"( select  inter_name " + 
			"from inter_distrito " + 
			"where inter_distritoId = c.inter_distritoparticular " + 
			") as DistritoParticularProspecto," + 
			"( select  inter_name " + 
			"from inter_provincia " + 
			"where inter_provinciaId = c.inter_provinciaparticular " + 
			") as ProvinciaParticularProspecto," + 
			"( select  inter_name " + 
			"from inter_departamento " + 
			"where inter_departamentoId = c.inter_departamentoparticular " + 
			") as DepartamentoParticularProspecto," + 
			"op.inter_scoring as Scoring," + 
			"op.inter_fechaultimaactividadefectiva as FechaUltimaCitaCompletada," + 
			"su2.FullName as Propietario," + 
			"su2.ParentSystemUserIdName as AdministradorUsuarioPropietario, " + 
			"m.currencyname as Divisa, " +
			"( select value " +
			"from stringmap " +
			"where ObjectTypeCode = 4201 " +  
			"and attributename = 'inter_asunto' " +
			"and attributevalue = op.inter_ultima_actividad_completada " +
			") as inter_ultima_actividad_completada " +
			",dateadd(hour,-5, op.inter_fechaultimaactividadcompletada) AS inter_fechaultimaactividadcompletada " +
			",( select TOP 1 a.inter_PeriodoName " +
			"from inter_auditoradecarteradetalle ad " +
			"LEFT JOIN inter_auditoradecartera a ON ad.inter_codigodeauditoria=a.inter_auditoradecarteraid " +
			"where ad.inter_Oportunidad = op.OpportunityId " +
			"and ad.inter_compromiso=1 " +
			"order by ad.CreatedOn desc " +
			") as inter_periodo_plan_trabajo " +
			",( select TOP 1 ad.inter_Cumplimiento " +
			"from inter_auditoradecarteradetalle ad " +
			"where ad.inter_Oportunidad = op.OpportunityId " +
			"and ad.inter_compromiso=1 " +
			"order by ad.CreatedOn desc " +
			") as inter_cumplimiento_plan_trabajo " +
			"from Opportunity op " +
			"left outer join systemuser su on op.CreatedBy = su.SystemUserId " + 
			"left outer join contact c on op.CustomerId = c.ContactId " + 
			"left outer join systemuser su2 on c.inter_agenterentavitalicia = su2.SystemUserId " + 
			"left outer join transactioncurrency m on op.TransactionCurrencyId = m.TransactionCurrencyId " +
			"where op.inter_tipooportunidadgeneral = '538560001' " +
			"and op.inter_ultimaoportunidadRV = '1'"
		)
	@Results(value = {
		@Result(column = "CUSPP", 								property = "cuspp"),
		@Result(column = "DocumentoProspecto", 					property = "documentoProspecto"),
		@Result(column = "Prospecto", 							property = "prospecto"),
		@Result(column = "Oportunidad", 						property = "oportunidad"),
		@Result(column = "Categoria", 							property = "categoria"),
		@Result(column = "Modulo", 								property = "modulo"),
		@Result(column = "Estado", 								property = "estado"),
		@Result(column = "SaldoCICTotal", 						property = "saldoCICTotal"),
		@Result(column = "FechaNacimientoProspecto", 			property = "fechaNacimientoProspecto"),
		@Result(column = "FechaAsignacionProspecto", 			property = "fechaAsignacionProspecto"),
		@Result(column = "RAMProspecto", 						property = "ramProspecto"),
		@Result(column = "FechaUltimoAporte", 					property = "fechaUltimoAporte"),
		@Result(column = "Pronostico", 							property = "pronostico"),
		@Result(column = "DireccionParticularProspecto", 		property = "direccionParticularProspecto"),
		@Result(column = "DistritoParticularProspecto", 		property = "distritoParticularProspecto"),
		@Result(column = "ProvinciaParticularProspecto", 		property = "provinciaParticularProspecto"),
		@Result(column = "DepartamentoParticularProspecto", 	property = "departamentoParticularProspecto"),
		@Result(column = "Scoring", 							property = "scoring"),
		@Result(column = "FechaUltimaCitaCompletada", 			property = "fechaUltimaCitaCompletada"),
		@Result(column = "Propietario", 						property = "propietario"),
		@Result(column = "AdministradorUsuarioPropietario", 	property = "administradorUsuarioPropietario"),
		@Result(column = "Divisa", 								property = "divisa"),
		@Result(column = "inter_ultima_actividad_completada", 	property = "ultimaActividadCompletada"),
		@Result(column = "inter_fechaultimaactividadcompletada", property = "fechaUltimaActividadCompletada"),
		@Result(column = "inter_periodo_plan_trabajo", 			property = "periodoPlanTrabajo"),
		@Result(column = "inter_cumplimiento_plan_trabajo", 	property = "cumplimientoPlanTrabajo")
	})
	public List<ReporteRRVV> obtenerReporteRrvv();
	
	
}
