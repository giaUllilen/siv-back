select * from siv_db.multitabla order by id_multitabla desc;
select * from siv_db.multitabla where nombre_tabla="Estado General";
select * from multitabla where codigo_tabla = '007';
select * from multitabla where codigo_tabla = '001' and estado_tabla = '1' and estado = '1' and codigo is not null; -- tipo documento identidad
select * from multitabla where codigo_tabla = '002' and estado_tabla = '1' and estado = '1' and codigo is not null; -- Género
select * from multitabla where codigo_tabla = '003' and estado_tabla = '1' and estado = '1' and codigo is not null; -- Profesión
select * from multitabla where codigo_tabla = '004' and estado_tabla = '1' and estado = '1' and codigo is not null; -- Actividad Económica
select * from multitabla where codigo_tabla = '005' and estado_tabla = '1' and estado = '1' and codigo is not null; -- Fumador
select * from multitabla where codigo_tabla = '006' and estado_tabla = '1' and estado = '1' and codigo is not null; -- Tipo Relación
select * from multitabla where codigo_tabla = '007' and estado_tabla = '1' and estado = '1' and codigo ; -- Estado General
select * from multitabla where codigo_tabla = '050' and estado_tabla = '1' and estado = '1' and codigo ; -- Estado auditoria
select * from multitabla where codigo_tabla = '051' and estado_tabla = '1' and estado = '1' and codigo ; -- Motivo observación
select * from multitabla where codigo_tabla = '008' and estado_tabla = '1' and estado = '1' and codigo is not null order by orden; -- Parámetros ADN
select * from multitabla where codigo_tabla = '011' and estado_tabla = '1' and estado = '1' and codigo is not null; -- Estado General
select * from multitabla where codigo_tabla = '050' and estado_tabla = '1' and estado = '1' and codigo is not null; 

insert into siv_db.multitabla values (3334,"007","Estado General",4,4,"Transmitido",null,null,null,1,1,"admin","2021-08-13 00:54:00",null,null);
insert into siv_db.multitabla values (3335,"007","Estado General",5,5,"Emitido",null,null,null,1,1,"admin","2021-08-13 00:54:00",null,null);
insert into siv_db.multitabla values (3336,"007","Estado General",6,6,"Observada ADC",null,null,null,1,1,"admin","2021-08-13 00:54:00",null,null);
insert into siv_db.multitabla values (3337,"007","Estado General",7,7,"Observada PLAFT",null,null,null,1,1,"admin","2021-08-13 00:54:00",null,null);
insert into siv_db.multitabla values (3338,"007","Estado General",8,8,"Rechazado ADC",null,null,null,1,1,"admin","2021-08-13 00:54:00",null,null);
insert into siv_db.multitabla values (3339,"007","Estado General",9,9,"Rechazado PLAFT",null,null,null,1,1,"admin","2021-08-13 00:54:00",null,null);
insert into siv_db.multitabla values (3340,"007","Estado General",10,10,"En Revision PLAFT",null,null,null,1,1,"admin","2021-08-13 00:54:00",null,null);

select  id_persona ,numero_documento ,es_pep, p.* from siv_db.persona p WHERE numero_documento = '46589885';
select numero_propuesta, numero_cotizacion_origen , s.* from solicitud s where id_asegurado = 71535;
select numero_propuesta, numero_cotizacion_origen , s.* from solicitud s where numero_cotizacion = '500270573';

-- 43813910   id-asegurado 228
select estado_plaft ,s.* from solicitud s where numero_cotizacion = '500270626';

SELECT token_status , fecha_firma_asegurado , fecha_afiliacion AS fechaSitc , fecha_samp ,moneda , s.* from  solicitud s  
where numero_cotizacion = '500270626';

SELECT sp.prima_comercial ,sp.* FROM solicitud_producto sp ;

select    *
from 	   solicitud_via_cobro v 
where 	   v.tipo_via_cobro = 1;

select id_via_cobro , s.* from solicitud s where  s.numero_cotizacion = '500064974 ';

SELECT id_solicitud , agente_num_vendedor ,estado_general ,id_afiliacion token_status ,id_pago_niubiz ,fecha_pago_niubiz, fecha_pago_culqi ,fecha_firma_asegurado , fecha_afiliacion AS fechaSitc , fecha_samp ,moneda , s.* from  solicitud s  
where numero_cotizacion = '500270626';

select estado_general,  numero_cotizacion ,s.* from solicitud s where id_solicitud = '16503';

SELECT id_solicitud , agente_num_vendedor ,estado_general ,id_afiliacion token_status ,id_pago_niubiz ,fecha_pago_niubiz, fecha_pago_culqi ,fecha_firma_asegurado , fecha_afiliacion AS fechaSitc , fecha_samp ,moneda , s.* from  solicitud s  
where numero_cotizacion = '500270638';

SELECT id_solicitud ,sp.prima_comercial ,sp.* FROM solicitud_producto sp ;

select * from multitabla m where m.codigo_tabla  = '014'  and	m.estado_tabla  = '1' and m.estado = '1' and m.codigo is not null order by  m.orden  ;

select * from persona p where numero_documento = '09537567';

select a.ldpdp ,p.tipo_documento ,p.numero_documento from adn a
inner join persona p on p.id_persona = a.id_persona 
inner join solicitud s on s.id_asegurado = p.id_persona 
where s.agente_num_vendedor = '15270'and a.ldpdp = 2; p.tipo_documento= '' and p.numero_documento ='';

select p.id_persona ,a.ldpdp ,p.tipo_documento ,p.numero_documento from adn a
inner join persona p on p.id_persona = a.id_persona where p.numero_documento = '40224582';

select * from adn a where id_persona = '71559';
select * from multitabla m ;
select agente_num_vendedor,estado_general ,estado , s.* from siv_db.solicitud s
where numero_propuesta = '500488666'; -- //47456823;   6
select nombre_archivo_solicitud ,agente_num_vendedor ,estado, estado_general , s.* from siv_db.solicitud s where numero_cotizacion = '500487927';		-- 42021594
select estado,nombre_archivo_solicitud, agente_num_vendedor, estado_general ,estado , s.* from solicitud s 
where numero_cotizacion = '500488666';  -- pep
select es_pep , p.* from persona p where numero_documento = '08154617';
select es.pep, p.* from solicitud s where numero_cotizacion = '500270693';
select * from autoguardado a ;


select nacionalidad , p.* from siv_db.persona p where numero_documento = '000853961';

SELECT * FROM siv_db.multitabla;

select s.agente_num_vendedor , s.* 
from solicitud s where id_solicitud = '55163';

/*
* ldpdp 
 * 21523336
 * */
-- ######################################################################################
-- Verificar asegurado y/o contratante
select * from persona p where numero_documento = "74965244";

UPDATE persona
SET es_pep = '2'
WHERE numero_documento = "41334575";

-- ######################################################################################
-- Verificar si afiliado tiene Solicitud por Id de asegurado
select estado , s.*from solicitud s where id_asegurado = '9080';


-- ######################################################################################
-- Obtener id-adn por numero de documento de la persna
select estado_recargo , s.* from solicitud s; 

inner join persona p on p.id_persona = a.id_persona 
where p.numero_documento = '74965228';


-- ######################################################################################
-- modificar tabla de adn
select * from adn where id_adn = '31593';

SELECT * from solicitud s2 ;
-- ######################################################################################
-- Obtener documento para iniciar una solicitud
-- select s.id_solicitud ,p.tipo_documento , p.numero_documento, p.nombres ,p.tipo_documento ,p.nacionalidad ,s.agente_num_vendedor , s.*
select agente_correo,s.id_contratante ,p.nombres ,agente_nombres ,id_asegurado , p.numero_documento ,estado , estado_general , numero_cotizacion ,estado_traspaso , s.fecha_crea 
from siv_db.solicitud s 
inner join siv_db.persona p on p.id_persona  = s.id_asegurado 
-- where p.tipo_documento = '2';
-- pvega => 15270
-- where s.agente_num_vendedor = '15270' and s.estado_general = '4' and
-- where s.agente_num_vendedor = '15270' and
-- llozadag = > 16319
-- <<<<<<< HEAD
 where s.agente_num_vendedor = '15270' and p.numero_documento = '45724350'-- and s.estado_general = '4' and
-- s.fecha_solicitud + INTERVAL 1 MONTH >= CURRENT_DATE 
ORDER  BY s.fecha_crea desc;
-- con consentimiento 45724350
-- 07549380
-- 40812119
-- 09792214  --> Perfecto
-- 48310073  -- 500489451
-- ######################################################################################
-- dni sin cotizacion
select * from persona p where id_persona = '145478';
select * from persona p2 where numero_documento  = '45724350';
select * from solicitud s where numero_propuesta = '500493174';
select * from multitabla m ;
select id_asegurado ,numero_cotizacion ,id_crm_oportunidad from solicitud s 
where id_asegurado = '145502';

select * from persona p 
WHERE not EXISTS (SELECT 1 from solicitud s WHERE s.id_asegurado=p.id_persona);

select * from solicitud s2 where id_asegurado = '';
-- 08130249
-- 500491563
select * from solicitud s where id_asegurado  = '218';
select estado_plaft, estado , estado_general  ,s.* from 
solicitud s where numero_propuesta  = '500491531';

-- 500488796 transmitido  07788991
select pago_prima ,fecha_samp ,estado_plaft ,estado , estado_general  ,s.* from 	
solicitud s where numero_propuesta  = '500491531';
=======
-- where s.agente_num_vendedor = '15918';

select * from multitabla m ;

SELECT sp.prima_devolucion , sp.* from solicitud_producto sp  where id_solicitud = '56039';
select * from solicitud_producto_detalle spd 
>>>>>>> master
-- ######################################################################################
-- documentos pvega
/*41591788 => 20edefff-30e8-e511-aca7-005056a36eba  = 500488164 - tipo N
 * 44223816
 * 41602942
 * 41334575 = adns	
 * 23563184 = doc cot
 * 41606470
 * 44303675
 * 43439279
<<<<<<< HEAD
<<<<<<< HEAD
 * 45051869
 * 48310073
 * 
=======
 * 48310073
 * 41638666
>>>>>>> master
=======
 * 45051869
 * 48310073
>>>>>>> features/Fase3-2020-242ReingenieriaADN
 * 
 * 47666957
 * 18032285
 * 40812119
 * */

-- documentos burgos
/*41591788 => 20edefff-30e8-e511-aca7-005056a36eba  = 500488164 - tipo N
 * 43899445
 * */
-- llozadag
-- 47455736

-- ######################################################################################
-- obtener producto
-- 25705594
select p.numero_documento ,sp.tipo_producto , s.numero_propuesta, s.numero_cotizacion , s.estado, s.estado_general , s.usuario_modif from solicitud s -- where id_asegurado ='37384';
inner join solicitud_producto sp on sp.id_solicitud = s.id_solicitud 
inner join persona p on p.id_persona = s.id_asegurado;
where p.numero_documento = '48310073' and estado = '6' and estado_general = '4'
and s.fecha_solicitud + INTERVAL 1 MONTH >= CURRENT_DATE ORDER  BY s.fecha_crea desc;



select * from multitabla m where nombre_tabla = 'Tipo Producto';

-- frecuencia -> Producto
select moneda ,id_afiliacion ,purchase_number ,id_tarjeta_sitc ,id_pago_culqi ,
id_pago_niubiz , tarjeta_afiliacion ,agente_correo ,estado, estado_general , s.* from solicitud s 
where numero_cotizacion = '500491518';

select frecuencia , sp.* from solicitud_producto sp where id_solicitud = '56025';
select * from solicitud_producto_detalle spd  where id_solicitud_producto = '30183';

-- pago recargo  500491518  99792214  500489682 -> 07934674
=======
select purchase_number ,id_tarjeta_sitc ,id_pago_culqi , id_pago_niubiz , tarjeta_afiliacion ,agente_correo ,estado, estado_general , s.* from solicitud s 
where numero_cotizacion = '500489876';

select * from persona p2 where id_persona = '144803';
select estado_recargo , s.* from solicitud s where numero_cotizacion = '500493289';

-- pago recargo  
>>>>>>> features/Fase3-2020-242ReingenieriaADN

select estado_traspaso,id_pago_niubiz ,id_pago_culqi , id_tarjeta_sitc , purchase_number , fecha_pago_culqi , tarjeta_afiliacion , pago_prima  from solicitud s 
where numero_cotizacion = '500493513';
-- 48310073
-- 500489682 --> pago normal
-- 500489684 --> pago recargo  500489876

<<<<<<< HEAD
select * from solicitud s 
where numero_cotizacion = '500493178';

=======
select * from 500489650
>>>>>>> features/Fase3-2020-242ReingenieriaADN

select estado_general ,token_status ,agente_num_vendedor , estado, s.* 
from solicitud s where numero_cotizacion = '500488906';

select * from persona p where id_persona = '145478';

select frecuencia , sp.* from solicitud_producto sp where id_solicitud = '56037';

select * from solicitud_producto_detalle spd where id_solicitud_producto = '29625';

-- -------------------------------------------------------------- 
select  fecha_solicitud ,pago_prima ,estado, estado_general ,estado_traspaso, s.* from solicitud s 
where numero_cotizacion = '500491661';
select prima_comercial ,sp.* from solicitud_producto sp where id_solicitud = '56036';

select * from solicitud_producto_detalle spd where id_solicitud_producto = '56036';

-- ######################################################################################


-- ######################################################################################

-- ######################################################################################
select * from solicitud_producto_detalle spd ;
select agente_correo ,s.* from solicitud s ;
select * from solicitud_producto sp ;
select * from solicitud_producto_detalle spd ;
select  * from solicitud s ;
select * from multitabla m ;

select    *
from 	   siv_db.persona p where p.numero_documento = "44347226"
where 	   p.tipo_documento = "01"
  and	   p.numero_documento = "44347226";
  
  select    *
from 	   familiar f 
where 	   f.id_persona = '228' ;
  /* documento de prueba 
   * 76459164 --> adn completo 
   * 21523336 --> adn - 
   * 47881067 --> transmision
   */15270
 
select * from persona p where numero_documento = '23563184';
SELECT estado , s.* FROM solicitud s WHERE id_asegurado = '33082';
SELECT * FROM solicitud s WHERE numero_cotizacion = '23563184';
select * from solicitud_dps sd where id_solicitud = '55321';
select * from solicitud_dps_pregunta sdp where id_solicitud_dps = '26460';
SELECT  * from solicitud_dps_pregunta sdp where id_solicitud_dps = '26460';
select * from solicitud_producto_detalle spd;
select prima_comercial, prima_comercial_anual , sp.* FROM solicitud_producto sp 
where id_solicitud = '56887';

SELECT * FROM poliza where numeroDocumento = '41334575';

UPDATE poliza
SET nombreProducto = 'VIDA'
WHERE numeroPoliza = "06100010872";
/*40812119*/

select token_adn_recargo , token_status_recargo , token_time_recargo , estado_general , s.* from solicitud s 
where numero_cotizacion = '500488800';

SELECT s.id_crm_oportunidad , s.* FROM solicitud s 
inner join persona p on p.id_persona = s.id_asegurado  where p.numero_documento = '45724350';

select alerta_plaft , s.* from solicitud s where numero_cotizacion = '500493543';

select * from transmision t ;

select * from transmision_detalle td ;

select * from solicitud_traspaso st ;

SELECT agente_correo ,subplan , estado_recargo ,id_solicitud ,fecha_solicitud ,numero_propuesta ,estado , estado_general ,estado_traspaso ,s.* FROM 
solicitud s WHERE numero_cotizacion = '500492740';

select estado_traspaso, s.* from solicitud s;
select prima_comercial , sp.* from solicitud_producto sp 
where id_solicitud = '56623' ;
select * from multitabla m where codigo_tabla in ('021','032');
select * from multitabla m where codigo_tabla in ('001');
select * from persona p ;

UPDATE solicitud
SET estado_traspaso = '1'
WHERE numero_propuesta = "500491810";

-- pvega
-- transmitido
-- 500491663
-- 90045678
-- --- 80080080

select p.numero_documento ,sp.tipo_producto , s.numero_propuesta, s.numero_cotizacion 
, s.estado, s.estado_general  from solicitud s inner join solicitud_producto sp on sp.id_solicitud = s.id_solicitud
 inner join persona p on p.id_persona = s.id_asegurado where p.numero_documento = '99792112' and estado = '6' and estado_general = '4'
 and s.fecha_crea + INTERVAL 1 MONTH >= CURRENT_DATE ORDER  BY s.fecha_crea desc;

select estado , estado_general , s.* from solicitud s where id_asegurado = '145288';
 SELECT * FROM persona p where numero_documento = '99792112';
 
select p.numero_documento ,sp.tipo_producto , s.numero_propuesta, s.numero_cotizacion 
, s.estado, s.estado_general  from solicitud s inner join solicitud_producto sp on sp.id_solicitud = s.id_solicitud
 inner join persona p on p.id_persona = s.id_asegurado where estado = '6' and estado_general = '4'
 and s.fecha_crea + INTERVAL 1 MONTH >= CURRENT_DATE ORDER  BY s.fecha_crea desc;
 
SELECT * FROM solicitud s where numero_propuesta = '500493512' ; -- 6

SELECT * FROM solicitud s where numero_propuesta = '500493511' ; -- 6

SELECT estado_traspaso ,s.* FROM solicitud s where numero_propuesta = '500595226' ;

select * from solici where numero_propuesta = '500493512';

select c from persona p ;

SELECT * FROM solicitud_traspaso st ;



SELECT estado_traspaso, estado ,estado_general ,s.* 
FROM solicitud s where numero_propuesta = '500599013' ;


select * from multitabla m ;
UPDATE solicitud_traspaso
SET moneda_actual = '1'
WHERE num_propuesta = '500493555';

UPDATE solicitud_traspaso
SET moneda_original = '1'
WHERE num_propuesta = '500493555';

select * from transmision t where numero_propuesta = '500493562';
select * from transmision_detalle td where numero_propuesta = '500493562';

select * from persona p where numero_documento = '45838202';

select estado ,estado_general from solicitud s where numero_propuesta = '500564342';

select sp.tipo_producto , s.numero_propuesta, s.numero_cotizacion , s.estado, s.estado_general  
from solicitud s inner join solicitud_producto sp on sp.id_solicitud = s.id_solicitud 
inner join persona p on p.id_persona = s.id_asegurado 
where p.numero_documento = '09948895' 
and estado = '6' 
and estado_general = '4' 
and s.fecha_crea + INTERVAL 1 MONTH >= CURRENT_DATE ORDER  BY s.fecha_crea ;

select sp.tipo_producto , s.numero_propuesta, s.numero_cotizacion , s.estado, s.estado_general  from solicitud s inner join solicitud_producto sp on sp.id_solicitud = s.id_solicitud inner join persona p on p.id_persona = s.id_asegurado where p.numero_documento = '09948895' and estado = '6' and estado_general = '4'; 



SELECT 
      P.ASEGURADO_DOCUMENTO,
      P.NUMEROPROPUESTA,
      P.NUMEROPOLIZA,
      P.ESTADO,
      P.ASEGURADO_APELLIDOS,
      P.ASEGURADO_NOMBRES,
      P.CODIGOAGENTE,
      P.AGENTE,
      P.AGENCIA,
      SP.EMAILAGENTEFINAL ,
      CASE WHEN  ( P.Agencia IN ('906','1044','1067') or p.codigoagente in ('15905'))  THEN 'MDC' ELSE 'COMERCIAL' END TIPO_ASIGNACION,
      case when ADD_MONTHS(p.iniciovigencia, (case when estado in ('Saldada','Prorrogado') then  12 else 24  end ))
        > to_Date(current_Date,'dd/mm/RRRR') then 1 else 0 end Flag  
 FROM INTERSEGUROR.MIR_OPERACIONES_VIDA_POLIZA P
LEFT JOIN MIR_STAGEPOLIZA SP ON P.IDOPERACION = SP.IDOPERACION
WHERE ESTADO IN ('Vigente','Saldada','Prorrogado');



SELECT estado ,sp.tipo_producto ,numero_propuesta ,id_crm_cotizador, estado , estado_general , s.* 
FROM solicitud s
inner join solicitud_producto sp on s.id_solicitud = sp.id_solicitud 
where s.estado = '6';


select * from solicitud where numero_cotizacion = '500610279';

