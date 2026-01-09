select a.idtipo_documento, a.documento, codigo, b.descripcion ,a.activo from base a 
inner join base_categoria b on a.idbase_categoria = b.idbase_categoria
where documento = '46589885' and  a.activo = 1 order by b.idbase_categoria;
44347226  --46589885
select distinct codigo from base a 
inner join base_categoria b on a.idbase_categoria = b.idbase_categoria;