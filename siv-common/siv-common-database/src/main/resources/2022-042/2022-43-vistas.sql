-- siv_db.view_solicitudes_cotizacion source

-- CREATE OR REPLACE
-- ALGORITHM = UNDEFINED VIEW `view_solicitudes_validacion_vida` AS
select
    distinct `s`.`id_solicitud` AS `id_solicitud`,
    `a`.`profesion` AS `profesion`,
    `a`.`actividad_economica` AS `actividad_economica`,
    `spd`.`capital_asegurado` AS `capital_asegurado`,
    -- `dpsp`.`respuesta` AS `respuesta`,
    (case
        when ( `dpsp`.`respuesta` != 0) then 1 else 0
    end) AS `respuesta`,
    (case
        when ( `a`.`edad_actuarial` <= `mea`.`valor`) then 1
        else 0
    end) AS `estado_edad_actuarial`,
    (case
        when ( `dps`.`imc` <= `mimc`.`valor`) then 1
        else 0
    end) AS `imc`,
    (case
        when ( `a`.`lugarTrabajo` = `mlt`.`codigo`) then 1
        else 0
    end) AS `lugarTrabajo`,
    `s`.`numero_propuesta` AS `numero_propuesta`,
    `s`.`numero_poliza` AS `numero_poliza`,
    `td`.`valor` AS `tipo_documento`,
    `a`.`numero_documento` AS `numero_documento`,
    concat(`a`.`apellido_paterno`, ' ', `a`.`apellido_materno`, ', ', `a`.`nombres`) AS `cliente`,
    `s`.`fecha_solicitud` AS `fecha_solicitud`,
    `s`.`fecha_crea` AS `fecha_creacion`,
    `s`.`agente_nombres` AS `agente_nombres`,
    `s`.`agente_num_vendedor` AS `agente_num_vendedor`,
    (case
        when (`s`.`estado_general` = 5) then 'Emitida'
        when (`s`.`estado` = 6) then 'Cerrada'
        when ((to_days(now()) - to_days(ifnull(`s`.`fecha_solicitud`, `s`.`fecha_crea`))) < 31) then 'Vigente'
        when ((to_days(now()) - to_days(ifnull(`s`.`fecha_solicitud`, `s`.`fecha_crea`))) > 30) then 'Vencida'
        else ''
    end) AS `estado_cotizacion`,
    (case
        when (`sp2`.`tipo_producto` = 1) then 'Plan Garantizado'
        else 'Vida Free'
    end) AS `Producto`
from
    (((((((((`solicitud` `s`
left join `persona` `a` on
    ((`s`.`id_asegurado` = `a`.`id_persona`)))
left join `multitabla` `td` on
    (((`td`.`codigo_tabla` = '001')
        and (`td`.`codigo` = `a`.`tipo_documento`))))
join `solicitud_producto` `sp2` on
    ((`sp2`.`id_solicitud` = `s`.`id_solicitud`)))
left join `solicitud_producto_detalle` `spd` on
    ((`spd`.`id_solicitud_producto` = `sp2`.`id_solicitud_producto`)))
left join `solicitud_dps` `dps` on
    ((`dps`.`id_solicitud` = `s`.`id_solicitud`)))
left join `solicitud_dps_pregunta` `dpsp` on
    ((`dpsp`.`id_solicitud_dps` = `dps`.`id_solicitud_dps`)))
left join `multitabla` `mea` on
    (((`mea`.`codigo_tabla` = '062')
        and (`mea`.`codigo` = '1')))) 
left join `multitabla` `mimc` on
    (((`mea`.`codigo_tabla` = '066')
        and (`mea`.`codigo` = '1'))))
left join `multitabla` `mlt` on
    (((`mea`.`codigo_tabla` = '057')
        and (`mea`.`estado_emision` = '1'))));
       

   