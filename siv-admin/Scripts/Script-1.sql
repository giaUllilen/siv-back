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
select estado, s.* from solicitud s where numero_cotizacion = '500492648';
=======
select estado, s.* from solicitud s where numero_cotizacion = '500492648';

select  fecha_pago_niubiz from solicitud where numero_propuesta ='500493535';

>>>>>>> features/Fase3-2020-242ReingenieriaADN
