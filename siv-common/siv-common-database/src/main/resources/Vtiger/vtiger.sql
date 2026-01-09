SELECT * from vtigerprod.

Call vtigerprod.sp_reglas_asig_cliente ('000164625' ,'');

select * from vtigerprod.vtiger_users;

SELECT  
			cf.cf_852,
			e.smownerid as CodigoAgente,
            u.user_name as Usuario,            
            CONCAT (  u.first_name ,' ', u.last_name) as Nom_agente,
            u.status as Estado_agente,
            cs.leadsource Origen,
            date_format(e.createdtime + interval - (5) hour, '%d/%m/%Y') fecha_crea,
            '' as Mensaje,
            case when date_format(  DATE_ADD(e.createdtime  + interval - (5) hour , INTERVAL 90 DAY) , '%d/%m/%Y') >=  date_format( curdate(), '%d/%m/%Y') then 0 else 1 end flg_transfer
            FROM vtiger_contactdetails c
			INNER JOIN vtiger_contactsubdetails cs on c.contactid = cs.contactsubscriptionid -- Tabla de campos adicionales
			INNER JOIN vtiger_contactaddress ca on c.contactid = ca.contactaddressid -- Tabla de campos adicionales
			INNER JOIN vtiger_contactscf cf on c.contactid = cf.contactid -- tabla de campos nuevos
			INNER JOIN vtiger_crmentity e on c.contactid = e.crmid and e.deleted = 0 -- Tabla maestra de vtiger
			INNER JOIN vtiger_users u on e.smownerid = u.id -- usuarios
            -- WHERE cf.cf_854 = '000164625';
            WHERE CF_852 = 'Carnet de Extranjeria';
            
           -- Carnet de Extranjeria 000456789
           -- Carnet Extranjeria 000164625
           
Select cf.cf_852 , count(cf.cf_852) 
from vtiger_contactscf cf group by cf.cf_852;

-- Contactos
SELECT cf.cf_852 tipodocumento,cf.cf_854 nrodocumento 
FROM vtiger_contactdetails c -- Tabla de campos nativos
INNER JOIN vtiger_contactsubdetails cs on c.contactid = cs.contactsubscriptionid -- Tabla de campos adicionales
INNER JOIN vtiger_contactaddress ca on c.contactid = ca.contactaddressid -- Tabla de campos adicionales
INNER JOIN vtiger_contactscf cf on c.contactid = cf.contactid -- tabla de campos nuevos
INNER JOIN vtiger_crmentity e on c.contactid = e.crmid and e.deleted = 0 -- Tabla maestra de vtiger
INNER JOIN vtiger_users u on e.smownerid = u.id-- usuarios
-- where cf.cf_852 = p_tipoDocumento and cf.cf_854 = p_nroDocumento;
where cf.cf_852 = 'Carnet Extranjeria' and cf.cf_854 = '002547888';

CALL sp_obtener_contact('DNI','77777776');

SELECT * from vtiger_contactscf cf;

-- Oportunidades
SELECT * 
FROM vtiger_potential p  -- Tabla de campos nativos
INNER JOIN vtiger_potentialscf pf on p.potentialid = pf.potentialid -- tabla de campos nuevos
INNER JOIN vtiger_crmentity e on p.potentialid = e…;



UPDATE vtiger_contactscf SET CF_852 = 'Carnet Extranjeria' WHERE CF_852 = 'CARNE EXTRANJERIA'; 

UPDATE vtiger_contactscf SET CF_852 = 'Carnet Extranjeria' WHERE CF_852 = 'Carnet de Extranjeria'; 

UPDATE vtiger_contactscf SET CF_852 = 'Carnet Extranjeria' WHERE CF_852 = 'CE'; 

SELECT	e.smownerid as CodigoAgente,
            u.user_name as Usuario,
            CONCAT (  u.first_name ,' ', u.last_name) as Nom_agente,
            u.status as Estado_agente,
            cs.leadsource Origen,
            date_format(e.createdtime + interval - (5) hour, '%d/%m/%Y') fecha_crea,
            '' as Mensaje,
            case when date_format(  DATE_ADD(e.createdtime  + interval - (5) hour , INTERVAL 90 DAY) , '%d/%m/%Y') >=  date_format( curdate(), '%d/%m/%Y') then 1 else 0 end flg_transfer
        	FROM vtiger_contactdetails c -- Tabla de campos nativos
			INNER JOIN vtiger_contactsubdetails cs on c.contactid = cs.contactsubscriptionid -- Tabla de campos adicionales
			INNER JOIN vtiger_contactaddress ca on c.contactid = ca.contactaddressid -- Tabla de campos adicionales
			INNER JOIN vtiger_contactscf cf on c.contactid = cf.contactid -- tabla de campos nuevos
			INNER JOIN vtiger_crmentity e on c.contactid = e.crmid and e.deleted = 0 -- Tabla maestra de vtiger
			INNER JOIN vtiger_users u on e.smownerid = u.id -- usuarios;
		
SELECT * FROM vtiger_contactscf where cf_854  = '000570276';

UPDATE vtiger_contactscf SET cf_1044 = null WHERE cf_854 = '000456789';

select * from vtiger_users where user_name LIKE 'scaldero' ;

select user_name , status , first_name from vtiger_users where user_name LIKE 'zonia.jauregui@interseguro.com.pe' ;
select * from vtiger_contactscf vc where cf_854 = '70014552'; 