package pe.interseguro.siv.common.util;
/**
 * Constantes Solicitud PDF
 * 
 * @author ti-is
 */
public interface ConstantesSolicitudPDF {
	
	//Solicitud
	String CODIGO_SOLICITUD_NUMERO_PROPUESTA = "sol_num_propuesta";
	String CODIGO_SOLICITUD_MONEDA_SOLES = "chk_soles";
	String CODIGO_SOLICITUD_MONEDA_DOLARES = "chk_dolares";
	String CODIGO_SOLICITUD_DESGLOSE_SOLES = "chk_desglosable_soles";
	String CODIGO_SOLICITUD_DESGLOSE_DOLARES = "chk_desglosable_dolares";
	String CODIGO_SOLICITUD_PLAN_EDUCACION = "chk_plan_1";
	String CODIGO_SOLICITUD_PLAN_SUENIO = "chk_plan_2";
	String CODIGO_SOLICITUD_PLAN_JUBILACION = "chk_plan_3";
	//--VIDA FREE--
	String CODIGO_SOLICITUD_VIDA_FREE = "chk_plan_1";
	String CODIGO_SOLICITUD_VIDA_FREE_PLUS = "chk_plan_2";
	String CODIGO_SOLICITUD_VIDA_FREE_TOTAL = "chk_plan_3";

	String CODIGO_SOLICITUD_FRECUENCIA_MENSUAL = "chk_frecuencia_mensual";
	String CODIGO_SOLICITUD_FRECUENCIA_TRIMESTRAL = "chk_frecuencia_trimestral";
	String CODIGO_SOLICITUD_FRECUENCIA_SEMESTRAL = "chk_frecuencia_semestral";
	String CODIGO_SOLICITUD_FRECUENCIA_ANUAL = "chk_frecuencia_anual";
	
	String CODIGO_SOLICITUD_DEVOLUCION_0 = "chk_devolucion_0";
	String CODIGO_SOLICITUD_DEVOLUCION_50 = "chk_devolucion_50";
	String CODIGO_SOLICITUD_DEVOLUCION_75 = "chk_devolucion_75";
	String CODIGO_SOLICITUD_DEVOLUCION_100 = "chk_devolucion_100";
	String CODIGO_SOLICITUD_DEVOLUCION_125 = "chk_devolucion_125";
	String CODIGO_SOLICITUD_DEVOLUCION_130 = "chk_devolucion_130";
	String CODIGO_SOLICITUD_DEVOLUCION_135 = "chk_devolucion_135";
	String CODIGO_SOLICITUD_DEVOLUCION_140 = "chk_devolucion_140";
	String CODIGO_SOLICITUD_DEVOLUCION_145 = "chk_devolucion_145";
	String CODIGO_SOLICITUD_DEVOLUCION_150 = "chk_devolucion_150";
	
	String CODIGO_SOLICITUD_NOTA_COBERTURA = "sol_nota_cobertura";
	String CODIGO_SOLICITUD_AGENTE_NOMBRES = "sol_agente_nombres";
	String CODIGO_SOLICITUD_AGENTE_CODIGO = "sol_agente_codigo";
	String CODIGO_SOLICITUD_AGENTE_AGENCIA = "sol_agente_agencia";
	String CODIGO_SOLICITUD_AGENTE_ORIGEN_VENTA = "sol_agente_origen_venta";
	String CODIGO_SOLICITUD_FECHA_SOLICITUD = "sol_fecha_solicitud";
	String CODIGO_SOLICITUD_FECHA_SOLICITUD_DIA = "sol_fecha_solicitud_dia";
	String CODIGO_SOLICITUD_FECHA_SOLICITUD_MES = "sol_fecha_solicitud_mes";
	String CODIGO_SOLICITUD_FECHA_SOLICITUD_ANIO = "sol_fecha_solicitud_anio";

	String CODIGO_SOLICITUD_TRATAMIENTO_CONTRATANTE_SI = "chk_sol_tratamiento_contra_true";
	String CODIGO_SOLICITUD_TRATAMIENTO_CONTRATANTE_NO = "chk_sol_tratamiento_contra_false";
	String CODIGO_SOLICITUD_TRATAMIENTO_ASEGURADO_SI = "chk_sol_tratamiento_aseg_true";
	String CODIGO_SOLICITUD_TRATAMIENTO_ASEGURADO_NO = "chk_sol_tratamiento_aseg_false";
	
	String CODIGO_SOLICITUD_FIRMA_CONTRATANTE = "sol_firma_contratante";
	String CODIGO_SOLICITUD_FIRMA_ASEGURADO = "sol_firma_asegurado";
	
	//Asegurado
	String CODIGO_ASEGURADO_APELLIDO_PATERNO = "aseg_ape_pat";
	String CODIGO_ASEGURADO_APELLIDO_MATERNO = "aseg_ape_mat";
	String CODIGO_ASEGURADO_NOMBRES = "aseg_nombres";
	String CODIGO_ASEGURADO_NOMBRES_COMPLETOS = "aseg_nombres_completos";
	String CODIGO_ASEGURADO_FNAC_DIA_1 = "aseg_fnac_dia_1";
	String CODIGO_ASEGURADO_FNAC_DIA_2 = "aseg_fnac_dia_2";
	String CODIGO_ASEGURADO_FNAC_MES_1 = "aseg_fnac_mes_1";
	String CODIGO_ASEGURADO_FNAC_MES_2 = "aseg_fnac_mes_2";
	String CODIGO_ASEGURADO_FNAC_ANIO_1 = "aseg_fnac_anio_1";
	String CODIGO_ASEGURADO_FNAC_ANIO_2 = "aseg_fnac_anio_2";
	String CODIGO_ASEGURADO_FNAC_ANIO_3 = "aseg_fnac_anio_3";
	String CODIGO_ASEGURADO_FNAC_ANIO_4 = "aseg_fnac_anio_4";
	String CODIGO_ASEGURADO_DOCUMENTO_TDOC_DNI = "chk_aseg_tdoc_dni";
	String CODIGO_ASEGURADO_DOCUMENTO_TDOC_RUC = "chk_aseg_tdoc_ruc";
	String CODIGO_ASEGURADO_DOCUMENTO_TDOC_CE = "chk_aseg_tdoc_ce";
	String CODIGO_ASEGURADO_DOCUMENTO_NUMERO = "aseg_doc_numero";
	String CODIGO_ASEGURADO_SEXO_MASCULINO = "chk_aseg_sex_mas";
	String CODIGO_ASEGURADO_SEXO_FEMENINO = "chk_aseg_femenino";
	String CODIGO_ASEGURADO_ESTADO_CIVL_SOLTERO = "chk_aseg_civil_sol";
	String CODIGO_ASEGURADO_ESTADO_CIVL_CASADO = "chk_aseg_civil_cas";
	String CODIGO_ASEGURADO_ESTADO_CIVL_VIUDO = "chk_aseg_civil_viu";
	String CODIGO_ASEGURADO_ESTADO_CIVL_DIVORCIADO = "chk_aseg_civil_div";
	String CODIGO_ASEGURADO_ESTADO_CIVL_CONVIVIENTE = "chk_aseg_civil_con";
	String CODIGO_ASEGURADO_NACIONALIDAD = "aseg_nacionalidad";
	String CODIGO_ASEGURADO_DIRECCION_VIA_JIRON = "chk_aseg_dir_jir";
	String CODIGO_ASEGURADO_DIRECCION_VIA_AVENIDA = "chk_aseg_dir_ave";
	String CODIGO_ASEGURADO_DIRECCION_VIA_CALLE = "chk_aseg_dir_call";
	String CODIGO_ASEGURADO_DIRECCION_VIA_PASAJE = "chk_aseg_dir_pje";
	String CODIGO_ASEGURADO_DIRECCION = "aseg_direccion";
	String CODIGO_ASEGURADO_TELEFONO_CASA = "aseg_telefono_casa";
	String CODIGO_ASEGURADO_CELULAR = "aseg_celular";
	String CODIGO_ASEGURADO_EMAIL = "aseg_email";
	String CODIGO_ASEGURADO_PEP_SI = "chk_aseg_pep_true";
	String CODIGO_ASEGURADO_PEP_NO = "chk_aseg_pep_false";
	String CODIGO_ASEGURADO_SUJETO_OBLIGADO_SI = "chk_aseg_suj_obligado_true";
	String CODIGO_ASEGURADO_SUJETO_OBLIGADO_NO = "chk_aseg_suj_obligado_false";
	String CODIGO_ASEGURADO_CENTRO_LABORAL = "aseg_centro_laboral";
	String CODIGO_ASEGURADO_ACTIVIDAD_ECONOMICA = "aseg_actividad_economica";
	String CODIGO_ASEGURADO_PROFESION = "aseg_profesion";
	String CODIGO_ASEGURADO_INGRESO_MONTO = "aseg_ingreso_monto";
	String CODIGO_ASEGURADO_DIRECCION_LABORAL = "aseg_direccion_laboral";
	String CODIGO_ASEGURADO_INGRESO_MONEDA_SOLES = "chk_aseg_ingreso_moneda_soles";
	String CODIGO_ASEGURADO_INGRESO_MONEDA_DOLARES = "chk_aseg_ingreso_moneda_dolares";
	

	//Contratante
	String CODIGO_CONTRATANTE_VINCULO_ASEGURADO = "contra_vinculo_asegurado";
	String CODIGO_CONTRATANTE_RAZON_SOCIAL = "contra_razon_social";
	String CODIGO_CONTRATANTE_APELLIDO_PATERNO = "contra_ape_pat";
	String CODIGO_CONTRATANTE_APELLIDO_MATERNO = "contra_ape_mat";
	String CODIGO_CONTRATANTE_NOMBRES = "contra_nombres";
	String CODIGO_CONTRATANTE_NOMBRES_COMPLETOS = "contra_nombres_completos";
	String CODIGO_CONTRATANTE_DOCUMENTO_NUMERO_NOTA_COBERTURA = "contra_doc_numero_nc";
	String CODIGO_CONTRATANTE_FNAC_DIA_1 = "contra_fnac_dia_1";
	String CODIGO_CONTRATANTE_FNAC_DIA_2 = "contra_fnac_dia_2";
	String CODIGO_CONTRATANTE_FNAC_MES_1 = "contra_fnac_mes_1";
	String CODIGO_CONTRATANTE_FNAC_MES_2 = "contra_fnac_mes_2";
	String CODIGO_CONTRATANTE_FNAC_ANIO_1 = "contra_fnac_anio_1";
	String CODIGO_CONTRATANTE_FNAC_ANIO_2 = "contra_fnac_anio_2";
	String CODIGO_CONTRATANTE_FNAC_ANIO_3 = "contra_fnac_anio_3";
	String CODIGO_CONTRATANTE_FNAC_ANIO_4 = "contra_fnac_anio_4";
	String CODIGO_CONTRATANTE_DOCUMENTO_TDOC_DNI = "chk_contra_tdoc_dni";
	String CODIGO_CONTRATANTE_DOCUMENTO_TDOC_RUC = "chk_contra_tdoc_ruc";
	String CODIGO_CONTRATANTE_DOCUMENTO_TDOC_CE = "chk_contra_tdoc_ce";
	String CODIGO_CONTRATANTE_DOCUMENTO_NUMERO = "contra_doc_numero";
	String CODIGO_CONTRATANTE_SEXO_MASCULINO = "chk_contra_sex_mas";
	String CODIGO_CONTRATANTE_SEXO_FEMENINO = "chk_contra_femenino";
	String CODIGO_CONTRATANTE_ESTADO_CIVL_SOLTERO = "chk_contra_civil_sol";
	String CODIGO_CONTRATANTE_ESTADO_CIVL_CASADO = "chk_contra_civil_cas";
	String CODIGO_CONTRATANTE_ESTADO_CIVL_VIUDO = "chk_contra_civil_viu";
	String CODIGO_CONTRATANTE_ESTADO_CIVL_DIVORCIADO = "chk_contra_civil_div";
	String CODIGO_CONTRATANTE_ESTADO_CIVL_CONVIVIENTE = "chk_contra_civil_con";
	String CODIGO_CONTRATANTE_NACIONALIDAD = "contra_nacionalidad";
	String CODIGO_CONTRATANTE_DIRECCION_VIA_JIRON = "chk_contra_dir_jir";
	String CODIGO_CONTRATANTE_DIRECCION_VIA_AVENIDA = "chk_contra_dir_ave";
	String CODIGO_CONTRATANTE_DIRECCION_VIA_CALLE = "chk_contra_dir_call";
	String CODIGO_CONTRATANTE_DIRECCION_VIA_PASAJE = "chk_contra_dir_pje";
	String CODIGO_CONTRATANTE_DIRECCION = "contra_direccion";
	String CODIGO_CONTRATANTE_TELEFONO_CASA = "contra_telefono_casa";
	String CODIGO_CONTRATANTE_CELULAR = "contra_celular";
	String CODIGO_CONTRATANTE_EMAIL = "contra_email";
	String CODIGO_CONTRATANTE_PEP_SI = "chk_contra_pep_true";
	String CODIGO_CONTRATANTE_PEP_NO = "chk_contra_pep_false";
	String CODIGO_CONTRATANTE_SUJETO_OBLIGADO_SI = "chk_contra_suj_obligado_true";
	String CODIGO_CONTRATANTE_SUJETO_OBLIGADO_NO = "chk_contra_suj_obligado_false";
	String CODIGO_CONTRATANTE_CENTRO_LABORAL = "contra_centro_laboral";
	String CODIGO_CONTRATANTE_ACTIVIDAD_ECONOMICA = "contra_actividad_economica";
	String CODIGO_CONTRATANTE_PROFESION = "contra_profesion";
	String CODIGO_CONTRATANTE_INGRESO_MONTO = "contra_ingreso_monto";
	String CODIGO_CONTRATANTE_INGRESO_MONEDA_SOLES = "chk_contra_ingreso_moneda_soles";
	String CODIGO_CONTRATANTE_INGRESO_MONEDA_DOLARES = "chk_contra_ingreso_moneda_dolares";
	String CODIGO_CONTRATANTE_DIRECCION_LABORAL = "contra_direccion_laboral";
	
	//Producto
	String CODIGO_PRODUCTO_FONDO_GARANTIZADO_MONEDA_SOLES = "prod_fondo_moneda_soles";
	String CODIGO_PRODUCTO_FONDO_GARANTIZADO_MONEDA_DOLARES = "prod_fondo_moneda_dolares";
	String CODIGO_PRODUCTO_FONDO_GARANTIZADO_MONTO = "prod_fondo_monto";
	String CODIGO_PRODUCTO_PERIODO_COBERTURA = "prod_periodo_cobertura";
	String CODIGO_PRODUCTO_PERIODO_ANUALIDADES = "prod_anualidades";
	String CODIGO_PRODUCTO_FRECUENCIA_PAGO_PRIMA_MENSUAL = "prod_frec_pago_men";
	String CODIGO_PRODUCTO_FRECUENCIA_PAGO_PRIMA_TRIMESTRAL = "prod_frec_pago_tri";
	String CODIGO_PRODUCTO_FRECUENCIA_PAGO_PRIMA_SEMESTRAL = "prod_frec_pago_sem";
	String CODIGO_PRODUCTO_FRECUENCIA_PAGO_PRIMA_ANUAL = "prod_frec_pago_anu";
	String CODIGO_PRODUCTO_FRECUENCIA_PAGO_PRIMA_UNICA = "prod_frec_pago_uni";
	String CODIGO_PRODUCTO_FRECUENCIA_CUOTA_COMODIN = "prod_frec_cuota_comodin";
	String CODIGO_PRODUCTO_FRECUENCIA_CUOTA_DOBLE = "prod_frec_cuota_doble";
	String CODIGO_PRODUCTO_TIPO_RIESGO_FUMADOR_SI = "prod_riesgo_fumador_true";
	String CODIGO_PRODUCTO_TIPO_RIESGO_FUMADOR_NO = "prod_riesgo_fumador_false";
	String CODIGO_PRODUCTO_DETALLE_PRIMA_MONEDA_DOLARES = "chk_prod_prima_dolares";
	String CODIGO_PRODUCTO_DETALLE_PRIMA_MONEDA_SOLES = "chk_prod_prima_soles";
	String CODIGO_PRODUCTO_GARANTIZADO_PRIMA_COMERCIAL_ANUAL = "prod_garan_prima_comercial_anual";
	String CODIGO_PRODUCTO_GARANTIZADO_FACTOR_PAGO = "prod_garan_factor_pago";
	String CODIGO_PRODUCTO_GARANTIZADO_PRIMA_COMERCIAL = "prod_garan_prima_comercial";
	
	String CODIGO_PRODUCTO_FUMADOR_SI = "chk_fumador";
	String CODIGO_PRODUCTO_FUMADOR_NO = "chk_no_fumador";
	
	String CODIGO_PRODUCTO_PERIODO_TEMPORAL = "chk_prod_per_temporal";
	String CODIGO_PRODUCTO_PERIODO_VITALICIO = "chk_prod_per_vitalicio";
	
	String CODIGO_PRODUCTO_VF_PRIMA_COMERCIAL_ANUAL = "prod_vidafree_prima_comercial_anual";
	String CODIGO_PRODUCTO_VF_FACTOR_PAGO = "prod_vidafree_factor_pago";
	String CODIGO_PRODUCTO_VF_PRIMA_COMERCIAL = "prod_vidafree_prima_comercial";
	String CODIGO_PRODUCTO_VF_PRIMA_COMERCIAL_IGV = "prod_vidafree_prima_comercial_igv";
	String CODIGO_PRODUCTO_VF_PRIMA_COMERCIAL_TOTAL = "prod_vidafree_prima_comercial_total";
	
	//Producto Prima
	String CODIGO_PRODUCTO_COBERTURA_EDUCACION_FALLECIMIENTO = "prod_cob_{0}_{1}";
	String CODIGO_PRODUCTO_COBERTURA_EDUCACION_EXONERACION = "prod_cob_1_2";
	String CODIGO_PRODUCTO_COBERTURA_EDUCACION_ADI_FALLECIMIENTO = "prod_cob_1_3";
	String CODIGO_PRODUCTO_COBERTURA_EDUCACION_ADI_FALLECIMIENTO_TRANSITO = "prod_cob_1_4";
	String CODIGO_PRODUCTO_COBERTURA_EDUCACION_ADI_INVALIDEZ = "prod_cob_1_5";
	String CODIGO_PRODUCTO_COBERTURA_EDUCACION_ADI_INVALIDEZ_TOTAL = "prod_cob_1_6";
	String CODIGO_PRODUCTO_COBERTURA_EDUCACION_ADI_ENFERMEDADES = "prod_cob_1_7";
	String CODIGO_PRODUCTO_COBERTURA_EDUCACION_ADI_INVALIDEZ_ADELANTO = "prod_cob_1_8";
	String CODIGO_PRODUCTO_COBERTURA_EDUCACION_ADI_VIDA_ADICIONAL = "prod_cob_1_9";
	String CODIGO_PRODUCTO_COBERTURA_PRODUCTO = "prod_cob_{0}_{1}";
	String CODIGO_PRODUCTO_COBERTURA_PRIMA = "prod_cob_{0}_prima";
	

	//Beneficiario principal
	String CODIGO_BENEFICIARIO_PRINCIPAL_APELLIDO_PATERNO = "bene_item{0}_ape_pat";
	String CODIGO_BENEFICIARIO_PRINCIPAL_APELLIDO_MATERNO = "bene_item{0}_ape_mat";
	String CODIGO_BENEFICIARIO_PRINCIPAL_NOMBRES = "bene_item{0}_ape_mombres";
	String CODIGO_BENEFICIARIO_PRINCIPAL_FECHA_NACIMIENTO = "bene_item{0}_fecha_nac";
	String CODIGO_BENEFICIARIO_PRINCIPAL_DOCUMENTO = "bene_item{0}_documento";
	String CODIGO_BENEFICIARIO_PRINCIPAL_PARENTESCO = "bene_item{0}_parentesco";
	String CODIGO_BENEFICIARIO_PRINCIPAL_DISTRIBUCION = "bene_item{0}_distri";
	
	//Beneficiario adicional
	String CODIGO_BENEFICIARIO_ADICIONAL_APELLIDO_PATERNO = "bene_adi_item{0}_ape_pat";
	String CODIGO_BENEFICIARIO_ADICIONAL_APELLIDO_MATERNO = "bene_adi_item{0}_ape_mat";
	String CODIGO_BENEFICIARIO_ADICIONAL_NOMBRES = "bene_adi_item{0}_ape_mombres";
	String CODIGO_BENEFICIARIO_ADICIONAL_FECHA_NACIMIENTO = "bene_adi_item{0}_fecha_nac";
	String CODIGO_BENEFICIARIO_ADICIONAL_DOCUMENTO = "bene_adi_item{0}_documento";
	String CODIGO_BENEFICIARIO_ADICIONAL_PARENTESCO = "bene_adi_item{0}_parentesco";
	String CODIGO_BENEFICIARIO_ADICIONAL_DISTRIBUCION = "bene_adi_item{0}_distri";
	

	//DPS
	String CODIGO_SOLICITUD_DPS_ESTATURA = "sol_dps_estatura";
	String CODIGO_SOLICITUD_DPS_PESO = "sol_dps_peso";
	String CODIGO_SOLICITUD_DPS_PESO_VARIACION_SI = "chk_dps_variacion_true";
	String CODIGO_SOLICITUD_DPS_PESO_VARIACION_NO = "chk_dps_variacion_false";
	String CODIGO_SOLICITUD_DPS_PESO_AUMENTO_DESCUENTO_SI = "chk_dps_ad_true";
	String CODIGO_SOLICITUD_DPS_PESO_AUMENTO_DESCUENTO_NO = "chk_dps_ad_false";
	String CODIGO_SOLICITUD_DPS_PESO_AUMENTO_DESCUENTO_CANTIDAD = "sol_dps_ad_cantidad";
	String CODIGO_SOLICITUD_DPS_PESO_AUMENTO_DESCUENTO_MOTIVO = "sol_dps_ad_motivo";
	
	String CODIGO_SOLICITUD_DPS_FUMA_SI = "chk_dps_fuma_true";
	String CODIGO_SOLICITUD_DPS_FUMA_NO = "chk_dps_fuma_false";
	String CODIGO_SOLICITUD_DPS_FUMA_CANTIDAD = "sol_dps_fuma_cantidad";
	String CODIGO_SOLICITUD_DPS_FUMA_FRECUENCIA = "sol_dps_fuma_frecuencia";
	
	String CODIGO_SOLICITUD_DPS_DROGAS_SI = "chk_dps_drogas_true";
	String CODIGO_SOLICITUD_DPS_DROGAS_NO = "chk_dps_drogas_false";
	String CODIGO_SOLICITUD_DPS_DROGAS_FECHA = "sol_dps_drogas_fecha";

	String CODIGO_SOLICITUD_DPS_ALCOHOL_SI = "chk_dps_alcohol_true";
	String CODIGO_SOLICITUD_DPS_ALCOHOL_NO = "chk_dps_alcohol_false";
	String CODIGO_SOLICITUD_DPS_ALCOHOL_CANTIDAD = "sol_dps_alcohol_cantidad";
	String CODIGO_SOLICITUD_DPS_ALCOHOL_FRECUENCIA = "sol_dps_alcohol_frecuencia";
	
	String CODIGO_SOLICITUD_DPS_PREGUNTA_SI = "chk_dps_pregunta_{0}_true";
	String CODIGO_SOLICITUD_DPS_PREGUNTA_NO = "chk_dps_pregunta_{0}_false";
	String CODIGO_SOLICITUD_DPS_PREGUNTA_DETALLE = "sol_dps_pregunta_{0}_detalle";
	String CODIGO_SOLICITUD_DPS_PREGUNTA_NUMERO = "sol_dps_pregunta_{0}_numero";
	String CODIGO_SOLICITUD_DPS_PREGUNTA_ENFERMEDAD = "sol_dps_pregunta_{0}_enfermedad";
	String CODIGO_SOLICITUD_DPS_PREGUNTA_FECHA = "sol_dps_pregunta_{0}_fecha";
	String CODIGO_SOLICITUD_DPS_PREGUNTA_CONDICION = "sol_dps_pregunta_{0}_condicion";
	String CODIGO_SOLICITUD_DPS_PREGUNTA_MEDICO = "sol_dps_pregunta_{0}_medico";
	
	String CODIGO_SOLICITUD_DPS_LABOR_DESCRIBE = "dps_labor_describe";
	
	String ESPACIO_EN_BLANCO = " ";
	
	//INICIO CODIGO DE VALORES DE MULTITABLA
	
	
	String MULTITABLA_CHECK_TRUE = "1";
	String MULTITABLA_CHECK_FALSE = "0";

	String MULTITABLA_CHECK_CODIGO_TRUE = "1";
	String MULTITABLA_CHECK_CODIGO_FALSE = "2";
	
	String MULTITABLA_MONEDA_SOLES = "1";
	String MULTITABLA_MONEDA_DOLARES = "2";
	String MULTITABLA_SUBPLAN_EDUCACION = "1";
	String MULTITABLA_SUBPLAN_JUBILACION = "2";
	String MULTITABLA_SUBPLAN_SUENIO = "3";
	
	String MULTITABLA_SUBPLAN_VIDA_FREE = "1";
	String MULTITABLA_SUBPLAN_VIDA_FREE_PLUS = "2";
	String MULTITABLA_SUBPLAN_VIDA_FREE_TOTAL = "3";
	
	String MULTITABLA_DOCUMENTO_DNI = "1";
	String MULTITABLA_DOCUMENTO_CE = "2";
	String MULTITABLA_DOCUMENTO_RUC_NATURAL = "10";
	String MULTITABLA_DOCUMENTO_RUC_JURIDICO = "11";
	
	String MULTITABLA_GENERO_MASCULINO = "1";
	String MULTITABLA_GENERO_FEMENINO = "2";
	
	String MULTITABLA_ESTADO_CIVIL_SOLTERO = "1";
	String MULTITABLA_ESTADO_CIVIL_CASADO = "2";
	String MULTITABLA_ESTADO_CIVIL_VIUDO = "3";
	String MULTITABLA_ESTADO_CIVIL_DIVORCIADO = "4";
	String MULTITABLA_ESTADO_CIVIL_CONVIVIENTE= "5";
	

	String MULTITABLA_DIRECCION_JIRON = "1";
	String MULTITABLA_DIRECCION_AVENIDA = "2";
	String MULTITABLA_DIRECCION_CALLE = "3";
	String MULTITABLA_DIRECCION_PASAJE = "4";
	
	String MULTITABLA_PRODUCTO_TIPO_PRODUCTO_PLAN_GARANTIZADO = "1";
	String MULTITABLA_PRODUCTO_TIPO_PRODUCTO_VIDA_FREE = "2";
	String MULTITABLA_PRODUCTO_FRECUENCIA_MENSUAL = "1";
	String MULTITABLA_PRODUCTO_FRECUENCIA_TRIMESTRAL = "2";
	String MULTITABLA_PRODUCTO_FRECUENCIA_SEMESTRAL = "3";
	String MULTITABLA_PRODUCTO_FRECUENCIA_ANUAL = "4";
	String MULTITABLA_PRODUCTO_FRECUENCIA_UNICA = "5";
	String MULTITABLA_PRODUCTO_TIPO_CUOTA_COMODIN = "1";
	String MULTITABLA_PRODUCTO_TIPO_CUOTA_DOBLE = "2";
	String MULTITABLA_PRODUCTO_TIPO_CUOTA_SIN_CUOTA = "3";
	String MULTITABLA_PRODUCTO_TIPO_RIESGO_FUMADOR_SI = "1";
	String MULTITABLA_PRODUCTO_TIPO_RIESGO_FUMADOR_NO = "2";
	String MULTITABLA_PRODUCTO_VIDA_FREE_0 = "0";
	String MULTITABLA_PRODUCTO_VIDA_FREE_50 = "50";
	String MULTITABLA_PRODUCTO_VIDA_FREE_75 = "75";
	String MULTITABLA_PRODUCTO_VIDA_FREE_100 = "100";
	String MULTITABLA_PRODUCTO_VIDA_FREE_125 = "125";
	String MULTITABLA_PRODUCTO_VIDA_FREE_130 = "130";
	String MULTITABLA_PRODUCTO_VIDA_FREE_135 = "135";
	String MULTITABLA_PRODUCTO_VIDA_FREE_140 = "140";
	String MULTITABLA_PRODUCTO_VIDA_FREE_145 = "145";
	String MULTITABLA_PRODUCTO_VIDA_FREE_150 = "150";
	
	String MULTITABLA_BENEFICIARIO_PRINCIPAL = "1";
	String MULTITABLA_BENEFICIARIO_CONTINGENTE = "2";
	//FIN  CODIGO DE VALORES DE MULTITABLA
	
}
