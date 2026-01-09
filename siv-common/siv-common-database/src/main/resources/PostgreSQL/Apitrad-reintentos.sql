
select * from ext_recepcion_resp err where "idEnvio" = 242;
select * from "logEndoso" le where "idEnvio" = '242';

select * from solicitud s where poliza =  '690011686';
select * from solicituddevolucion s2 where nrosolicitud  =  'REQ_DNI_08188803_87';



SELECT idsolicitud, iddocumento, nombres, apellidopaterno, apellidomaterno, nombrecompleto, tipodocumento, nrodocumento, poliza, nrosolicitud, tiposolicitud, categoria, subcategoria, producto, lineanegocio, telefono, email, direccion, descripcion, fechvencinterna, fechvencnormativa, comentarioflujo, fechadictamen, areausuario, canalingreso, canalrespuesta, fechasolicitud, estadosucave, origen, tipodevolucion, fecharegistro, usuarioregistro, fechamodifica, usuariomodifica, usuarioasignado, endosocesion, estadopoliza, estadosolicitud, observacionsolicitud
	FROM public.solicitud where poliza in ('690013642','690013643');

select * from ext_recepcion_resp err  where "numeroPoliza"  = '06100005798';

select * from "logEndoso"   where "numeroPoliza"  = '06100005798';

-- Finalizados
ALTER TABLE public.solicitud ADD dias_transcurridos int4 null;
ALTER TABLE public.solicitud DROP COLUMN diastranscurridos;

-- Nuevos
ALTER TABLE public.solicituddevolucion  ADD nroIncidente varchar(100) null;
ALTER TABLE public.solicituddevolucion  ADD fechaActualizacionBCTS varchar(100) null;




select * from solicitud s where poliza = '6900001131';
select * from solicituddevolucion s where estadointentos = 'en proceso' and estadoservicio = 'OK'  ;
select * from solicituddevolucion s where idsolicituddevol  = 34;
select * from multitabla m where codigo_tabla = '009';

ALTER TABLE public.solicitud  ADD nombres varchar(50) null;
ALTER TABLE public.solicitud  ADD apellidopaterno varchar(50) null;
ALTER TABLE public.solicitud  ADD apellidomaterno varchar(50) null;
ALTER TABLE public.solicituddevolucion DROP COLUMN nombres;
ALTER TABLE public.solicituddevolucion DROP COLUMN apellidopaterno;
ALTER TABLE public.solicituddevolucion DROP COLUMN apellidomaterno;


CREATE TABLE solicituddevolucionresp (
    idsolicituddevolucionresp serial4,
    idsolicitud int4,
    openitems varchar(1000),
    estado bpchar(1)
);

select * from solicituddevolucion where nroregularizado ;

select * from ext_recepcion_resp err where "numeroPoliza" = '06100004996';

-- Nuevos
ALTER TABLE public.solicituddevolucionresp  ADD devolucionpendiente numeric null;
ALTER TABLE public.solicituddevolucionresp  ADD mensaje varchar(500) null;
ALTER TABLE public.solicituddevolucionresp  ADD logsPayment varchar(5000) null;
ALTER TABLE public.solicituddevolucion  ADD respuestaBanco varchar(500) null;
ALTER TABLE public.solicituddevolucion  ADD nroregularizado varchar(100) null;

DROP FUNCTION fn_find_solicitud_reporte(date,date);

SELECT * FROM public.solicituddevolucionresp s where estado = '1';


 SELECT 
    s.idsolicitud, 
    s.nrosolicitud, 
    to_char(s.fechasolicitud, 'YYYY-MM-DD') AS fechasolicitud,
    m1.valor AS tiposolicitud, 
    m2.valor AS categoria,
    s.producto, 
    s.nombrecompleto, 
    m3.valor AS dictamen, 
    m4.valor as tipodevolucion,
    sdr.mensaje,
    sd.incultimus,
    sd.monto,
    sdr.devolucionpendiente,
    sd.nroregularizado
  FROM solicitud s
  left join solicituddevolucion sd on s.idsolicitud = sd.idsolicitud 
  left join solicituddevolucionresp sdr on s.idsolicitud  = sdr.idsolicitud  
  LEFT join multitabla m1 ON s.tiposolicitud = m1.codigo AND m1.codigo_tabla = '003'
  LEFT join multitabla m2 ON s.categoria = m2.codigo AND m2.codigo_tabla = '006'
  LEFT join multitabla m3 ON s.estadosolicitud::varchar = m3.codigo AND m3.codigo_tabla = '016'
  left join multitabla m7 on s.subcategoria = m7.codigo and m7.codigo_tabla = '018'
  left join multitabla m4 on sd.destdevolucion = m4.codigo and m4.codigo_tabla = '009'
  -- WHERE s.fechasolicitud between fechaini and fechafin
  ORDER BY fechasolicitud DESC;
 
 
 SELECT 
    s.idsolicitud, 
    s.nrosolicitud, 
    to_char(s.fechasolicitud, 'YYYY-MM-DD') AS fechasolicitud,
    m1.valor AS tiposolicitud, 
    m2.valor AS categoria,
    s.producto, 
    s.nombrecompleto, 
    m3.valor AS dictamen, 
    m4.valor as tipodevolucion,
    sdr.mensaje,
    sd.incultimus,
    sd.monto,
    sdr.devolucionpendiente,
    sd.nroregularizado,
    case when  sd.destdevolucion = '4' then sdr.mensaje else sd.respuestaBanco end as motivoError     
  FROM solicitud s 
  left join solicituddevolucion sd on s.idsolicitud = sd.idsolicitud 
  left join solicituddevolucionresp sdr on s.idsolicitud  = sdr.idsolicitud  and sdr.estado = '1'
  LEFT join multitabla m1 ON s.tiposolicitud = m1.codigo AND m1.codigo_tabla = '003'
  LEFT join multitabla m2 ON s.categoria = m2.codigo AND m2.codigo_tabla = '006'
  LEFT join multitabla m3 ON s.estadosolicitud::varchar = m3.codigo AND m3.codigo_tabla = '016'
  left join multitabla m7 on s.subcategoria = m7.codigo and m7.codigo_tabla = '018'
  left join multitabla m4 on sd.destdevolucion = m4.codigo and m4.codigo_tabla = '009'
  WHERE s.tiposolicitud = '1' and s.categoria in ('1','2') and s.fechasolicitud between '2022-03-02' and '2022-10-21'
  ORDER BY fechasolicitud DESC;
 
 SELECT * FROM fn_find_solicitud_reporte('2022-03-02','2022-10-21');

UPDATE public.solicituddevolucion
set res = value1,
    column2 = value2,
    ...
WHERE id;
46





select * from public.solicituddevolucion; 
---------------------------------------------------------------------------------------------------------------------------------

alter table public.solicituddevolucionresp  alter column openitems type varchar(5000);

