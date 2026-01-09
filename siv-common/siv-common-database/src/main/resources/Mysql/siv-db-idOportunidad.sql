SELECT * FROM persona p where numero_documento = '07970060' ;
select * from solicitud s where id_asegurado = '145276';
SELECT * from multitabla m ;

-- IDOPORTUNIDAD --

select numero_cotizacion , id_crm_oportunidad ,fecha_crea 
from solicitud s where id_asegurado = '145276' ORDER by fecha_crea asc;

Exec SP_ODON_OBT_GASTO null,'2','2022-01-01','2022-01-31';
Exec SP_ODON_OBT_GASTO null,'1',null,null;
Exec SP_ODON_OBT_GASTO null,2,'2022-01-01','2022-01-31';
Exec SP_ODON_OBT_HORARIO_CONSULTA '2022-01-20';


SELECT * FROM solicitud WHERE numero_cotizacion  = '500493580'; -- 99792146

select * from persona p where numero_documento = '40349406';

-- 44396404
SELECT CF_852, Cf_854, Cf_928 FROM vtiger_contactscf where cf_854 = '44396404';

DNI

CALL sp_obtener_contact('DNI','44396404');

select * from vtigerprod.vtiger_users where user_name like 'deyvi';

select * from vtigerprod.vtiger_users where user_name = 'deyvi';

select user_name , status  from vtigerprod.vtiger_users where user_name not like '%@%';

SELECT * FROM solicitud_traspaso st ;
-- num propuesta original deberia enviar el pago recargo
SELECT estado_traspaso,estado_general, estado , s.* FROM solicitud s where numero_propuesta = '500601350' ;
SELECT estado_traspaso,estado_general, estado ,s.* FROM solicitud s where numero_propuesta = '500585711' ;

















