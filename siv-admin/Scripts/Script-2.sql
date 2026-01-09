SELECT td.codigo,c.documento,c.cumulo,cod_moneda
FROM cliente c
INNER JOIN tipo_documento td ON c.idtipo_documento = td.idtipo_documento;

SELECT distinct cod_moneda
FROM cliente c
INNER JOIN tipo_documento td ON c.idtipo_documento = td.idtipo_documento;

select a.idtipo_documento, a.documento, codigo from base a inner join base_categoria b on a.idbase_categoria = b.idbase_categoria 
WHERE idtipo_documento = $1 AND documento = $2 and a.activo = 1 order by b.idbase_categoria