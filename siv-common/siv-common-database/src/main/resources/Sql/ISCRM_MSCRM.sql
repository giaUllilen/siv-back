-- ######################################################################################
-- 
SELECT td.codigo,c.documento,c.cumulo,cod_moneda
FROM cliente c
INNER JOIN tipo_documento td ON c.idtipo_documento = td.idtipo_documento;


-- ######################################################################################
-- 
SELECT distinct cod_moneda
FROM cliente c
INNER JOIN tipo_documento td ON c.idtipo_documento = td.idtipo_documento;


-- ######################################################################################
-- 
select a.idtipo_documento, a.documento, codigo from base a inner join base_categoria b on a.idbase_categoria = b.idbase_categoria 
WHERE idtipo_documento = $1 AND documento = $2 and a.activo = 1 order by b.idbase_categoria


-- ######################################################################################
-- obtener codigo del cliente 
select top 1 
c.inter_Cotizaciones_OportunidadId as opportunity,c.inter_cotizacionId as  cotizacionId 
from inter_cotizacion c 
where c.inter_numerocotizacion = '500492646';

select * from inter_cotizacion;

select c.inter_tipodedocumento ,ContactId ,c.*
from contact c 
-- where ContactId = '6CE3CE31-6416-EC11-80BF-00505687D9AF';
where  c.inter_tipodedocumento = '538560000' 
and c.inter_numerodedocumento = '43439279';

select top 1 
c.contactid 
from contact c 
where c.inter_numerodedocumento = '22222222';
c.inter_tipodedocumento = #{tipoDocumento} and c.inter_numerodedocumento = #{numeroDocumento}

