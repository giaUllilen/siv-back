-- //VETERINARIO, ZOÓLOGO, ZOOTÉCNICO
UPDATE multitabla
SET valor = "VETERINARIO, ZOOTÉCNICO" 
WHERE id_multitabla = 3312;

insert into siv_db.multitabla values 
(3456,"003","Profesión",119,119,"ZOÓLOGO","Otros","119","119",1,1,"admin","2022-09-21 00:54:00",null,null);

-- //AGRICULTOR, AGRÓNOMO, AGRÓLOGO, ARBORICULTOR, AGRIMENSOR, TOPÓGRAFO, GEÓGRAFO
UPDATE multitabla
SET valor = "AGRICULTOR, AGRÓNOMO, AGRÓLOGO, ARBORICULTOR" ,valor_aux = "Agricultor"
WHERE id_multitabla = 3211;

insert into siv_db.multitabla values 
(3457,"003","Profesión",120,120,"TOPÓGRAFO, GEÓGRAFO, AGRIMENSOR","Geógrafo","120","51",1,1,"admin","2022-09-21 00:54:00",null,null);

-- //CAMARERO / BARMAN / MESERO/ COCINERO / CHEF
UPDATE multitabla
SET valor = "CAMARERO / BARMAN / MESERO" 
WHERE id_multitabla = 3226;

insert into siv_db.multitabla values 
(3458,"003","Profesión",121,121,"COCINERO, CHEF","Personal de cocina","121","82",1,1,"admin","2022-09-21 00:54:00",null,null);

-- //FOTÓGRAFO / OPERADORES CÁMARA, CINE Y TV, LOCUTOR DE RADIO Y TV, GUIONISTA
UPDATE multitabla
SET valor = "CINE Y TV, LOCUTOR DE RADIO Y TV, GUIONISTA" 
WHERE id_multitabla = 3256;
insert into siv_db.multitabla values 
(3459,"003","Profesión",122,122,"FOTÓGRAFO, OPERADORES CÁMARA","Fotógrafo (en zonas de conflicto)","122","47",1,1,"admin","2022-09-21 00:54:00",null,null);

-- //Lugar de trabajo
insert into siv_db.multitabla values 
(3460,"057","Lugar de Trabajo",1,1,"Oficina",null,null,null,1,1,"admin","2022-09-21 00:54:00",null,null);

insert into siv_db.multitabla values 
(3461,"057","Lugar de Trabajo",2,2,"Campo",null,null,null,1,1,"admin","2022-09-21 00:54:00",null,null);

insert into siv_db.multitabla values 
(3462,"057","Lugar de Trabajo",3,3,"Ambos (Oficina | Campo)",null,null,null,1,1,"admin","2022-09-21 00:54:00",null,null);

SELECT * FROM multitabla m where codigo_tabla = "057" ;

SELECT * FROM persona p ;
-- // add column

ALTER TABLE `siv_db`.`persona` 
ADD COLUMN `lugarTrabajo` VARCHAR(3) NULL DEFAULT NULL AFTER `profesion_detalle`;

-- Reglas Adicionales

insert into siv_db.multitabla values 
(3463,"061","Suma Asegurada",1,1,"1000",null,null,null,1,1,"admin","2022-09-21 00:54:00",null,null);

insert into siv_db.multitabla values 
(3475,"065","Respuesta DPS",1,1,"NO",null,null,null,null,1,1,"admin","2022-09-21 00:54:00",null,null);

insert into siv_db.multitabla values 
(3476,"066","Indice de Masa Corporal",1,1,"35",null,null,null,null,1,1,"admin","2022-09-21 00:54:00",null,null);

insert into siv_db.multitabla values 
(3477,"067","Cumulo",1,1,"175000",null,null,null,null,1,1,"admin","2022-09-21 00:54:00",null,null);

insert into siv_db.multitabla values 
(3474,"062","Edad Maxima Actuarial",1,1,"45",null,null,null,1,1,"admin","2022-09-21 00:54:00",null,null);

insert into siv_db.multitabla values 
(3465,"063","Estado Financiero",1,1,"Normal","no",null,null,1,1,"admin","2022-09-21 00:54:00",null,null);
insert into siv_db.multitabla values 
(3466,"063","Estado Financiero",2,2,"Dudoso","no",null,null,1,1,"admin","2022-09-21 00:54:00",null,null);
insert into siv_db.multitabla values 
(3467,"063","Estado Financiero",3,3,"Deficiente","no",null,null,1,1,"admin","2022-09-21 00:54:00",null,null);
insert into siv_db.multitabla values 
(3468,"063","Estado Financiero",4,4,"Perdida","no",null,null,1,1,"admin","2022-09-21 00:54:00",null,null);

insert into siv_db.multitabla values 
(3469,"064","Usuario a Derivar Respuesta",1,1,"Katherine Granados","Suscripcion",null,null,1,1,"admin","2022-09-21 00:54:00",null,null);
insert into siv_db.multitabla values 
(3470,"064","Usuario a Derivar Respuesta",2,2,"Leslie Chilmaza","Emision Rapida",null,null,1,1,"admin","2022-09-21 00:54:00",null,null);
insert into siv_db.multitabla values 
(3471,"064","Usuario a Derivar Respuesta",3,3,"David Bayona","Suscripcion",null,null,1,1,"admin","2022-09-21 00:54:00",null,null);
insert into siv_db.multitabla values 
(3472,"064","Usuario a Derivar Respuesta",4,4,"osep Asenjo","Suscripcion",null,null,1,1,"admin","2022-09-21 00:54:00",null,null);
insert into siv_db.multitabla values 
(3473,"064","Usuario a Derivar Respuesta",4,4,"Alithu Marquez","Emision Rapida",null,null,1,1,"admin","2022-09-21 00:54:00",null,null);


select * from multitabla m  where codigo_tabla in ("061","062","063","064");



-- validando Profesiones Cotizador

SELECT * FROM multitabla m where nombre_tabla = "Profesión" and valor in 
("ACTOR, ACTRIZ, ARTISTA, DIRECTOR DE ESPECTÁCULOS, COREÓGRAFO, MODELO, MÚSICO, ESCENÓGRAFO Y BAILARINES"
,"ACTUARIO"
,"ADMINISTRADOR"
,"AGENTE / INTERMEDIARIO / CORREDOR INMOBILIARIO"
,"AGENTE DE BOLSA"
,"AGENTE DE TURISMO/VIAJES"
,"AGENTE/INTERMEDIARIO/CORREDOR DE SEGUROS"
,"AGRICULTOR, AGRÓNOMO, AGRÓLOGO, ARBORICULTOR"
,"TOPÓGRAFO, GEÓGRAFO, AGRIMENSOR"
,"AMA DE CASA"
,"ANALISTAS DE SISTEMA Y COMPUTACIÓN"
,"ARCHIVERO"
,"ASISTENTE SOCIAL"
,"AUTOR LITERARIO, ESCRITOR Y CRITICO"
,"AVICULTOR"
,"CAJERO"
,"CAMARERO / BARMAN / MESERO"
,"COCINERO, CHEF"
,"CARTERO"
,"CERRAJERO"
,"COBRADOR"
,"CONTADOR"
,"CONTRATISTA"
,"COSMETÓLOGO, PELUQUERO Y BARBERO"
,"DECORADOR, DIBUJANTE, PUBLICISTA, DISEÑADOR DE PUBLICIDAD"
,"DENTISTA / ODONTÓLOGO"
,"DISTRIBUIDOR"
,"DOCENTE"
,"ECONOMISTA"
,"EMPLEADA (O) DEL HOGAR / NANA"
,"EMPRESARIO, EMPRESARIO EXPORTADOR/ EMPRESARIO IMPORTADOR"
,"ENFERMERO"
,"ESTUDIANTE"
,"CINE Y TV, LOCUTOR DE RADIO Y TV, GUIONISTA"
,"FOTÓGRAFO, OPERADORES CÁMARA"
,"HISTORIADOR"
,"INTERPRETE, TRADUCTOR"
,"JARDINERO"
,"JOYERO Y/O PLATERO / ORFEBRE"
,"JUBILADO / PENSIONISTA"
,"LIQUIDADOR, RECLAMACIONES/SEGUROS"
,"MARTILLERO / SUBASTADOR"
,"MAYORISTA, COMERCIO AL POR MAYOR"
,"MEDICO / CIRUJANO"
,"NUTRICIONISTA"
,"OBSTETRIZ"
,"ORGANIZADOR DE EVENTOS"
,"PANADERO / PASTELERO"
,"PARAMÉDICO"
,"PRODUCTOR DE CINE / RADIO / TELEVISIÓN / TEATRO"
,"PROGRAMADOR"
,"PSICÓLOGO/ TERAPEUTA"
,"QUIROPRÁCTICO/ KINESITERAPEUTA (KINESIÓLOGOS)"
,"RELACIONISTA PUBLICO E INDUSTRIAL"
,"RELOJERO"
,"REPARADOR DE APARATOS ELECTRODOMÉSTICOS"
,"REPARTIDOR"
,"SECRETARIA, RECEPCIONISTA, TELEFONISTA"
,"SERVICIO DE ALQUILER DE VEHÍCULOS"
,"SERVICIO DE ALQUILER DE VIDEOS, EQUIPOS DE SONIDO"
,"SOCIÓLOGO"
,"TRAMITADOR"
,"VENDEDOR AMBULANTE"
,"VETERINARIO, ZOOTÉCNICO"
,"VISITADOR MÉDICO"
,"ZAPATERO"
,"NO DECLARA"
,"ZOÓLOGO"
);

SELECT * FROM multitabla m where nombre_tabla = "Actividad Económica" and valor in 
("Actividades auxiliares de la financiación de planes de seguros y de pensiones"
,"Actividades auxiliares de la intermediación financiera n.c.p."
,"Actividades bursátiles"
,"Actividades de agencias de viajes y organizadores de viajes; actividades de asistencía a turistas n.c.p."
,"Actividades de asesoramiento empresarial y en matería de gestión"
,"Actividades de bibliotecas y archivos"
,"Actividades de contabilidad, teneduría de libros y auditoría; asesoramiento en matería de impuestos"
,"Actividades de correo distintas de las actividades postales nacionales"
,"Actividades de envase y empaque"
,"Actividades de fotografía"
,"Actividades de hospitales"
,"Actividades de impresión"
,"Actividades de la administración pública en general"
,"Actividades de médicos y odontólogos"
,"Actividades de museos y preservación de lugares y edificios históricos"
,"Actividades de organización empresariales y de empleadores"
,"Actividades de organizaciones profesionales"
,"Actividades de otras agencias de transporte"
,"Actividades de planes de seguridad social de afiliación obligatoria"
,"Actividades de servicios auxiliares para la administración pública en general"
,"Actividades de servicios relacionados con la impresión"
,"Actividades inmobiliarias realizadas a cambio de una retribución o por contrata"
,"Actividades inmobiliarias realizadas con bienes propios o arrendados"
,"Actividades postales nacionales"
,"Actividades teatrales y musicales y otras actividades artísticas"
,"Actividades veterinarias"
,"Administración de mercados financieros"
,"Almacenamiento y depósito"
,"Alquiler de efectos personales y enseres domésticos n.c.p."
,"Alquiler de equipo de construcción y demolición dotado de operarios"
,"Alquiler de equipo de transporte por vía acuática"
,"Alquiler de equipo de transporte por vía aérea"
,"Alquiler de equipo de transporte por vía terrestre"
,"Alquiler de maquinaria y equipo agropecuario"
,"Alquiler de maquinaria y equipo de construcción e ingeniería civil"
,"Alquiler de maquinaria y equipo de oficina (incluso computadoras)"
,"Alquiler de otros tipos de maquinaria y equipo n.c.p."
,"Arrendamiento financiero"
,"Banca central"
,"Captacion, depuración y distribución de agua"
,"Consultores en equipo de informática"
,"Consultores en programas de informática y suministros de programas de informática"
,"Corte, tallado y acabado de la piedra"
,"Cría de ganado vacuno y de ovejas, cabras, caballos, asnos, mulas y burdeganos; cría de ganado lechero"
,"Cría de otros animales; elaboración de productos animales n.c.p."
,"Edición de grabaciones"
,"Edición de libros, folletos, partituras y otras publicaciones"
,"Edición de periodicos, revistas y publicaciones periodicas"
,"Enseñanza de adultos y otros tipos de enseñanza"
,"Enseñanza primaria"
,"Enseñanza secundaría de formación general"
,"Enseñanza secundaría de formación técnica y profesional"
,"Enseñanza superior"
,"Exhibición de filmes y videocintas"
,"Fabricación de aparatos de uso domestico n.c.p."
,"Fabricación de artículos de deporte"
,"Fabricación de bicicletas y de sillones de ruedas para inválidos"
,"Fabricación de instrumentos de música"
,"Fabricación de instrumentos de óptica y equipo fotográfico"
,"Fabricación de instrumentos y aparatos para medir, verificar, ensayar, navegar y otros fines, excepto el equipo de control de procesos industriales"
,"Fabricación de joyas y artículos conexos"
,"Fabricación de juegos y juguetes"
,"Fabricación de maletas, bolsos de mano y artículos similares, y de artículos de talabartería y guarnicionería"
,"Fabricación de otros artículos de papel y carton"
,"Fabricación de productos de ceramica no refractaría para uso no estructural"
,"Fabricación de relojes"
,"Fabricación de tapices y alfombras"
,"Fabricación de tejidos y artículos de punto y ganchillo"
,"Hogares privados con servicio doméstico"
,"Hoteles; campamentos y otros tipos de hospedaje temporal"
,"Investigación de mercados y realización de encuestas de opinión pública"
,"Investigaciones y desarrollo experimental en el campo de las ciencias naturales y la ingeniería"
,"Investigaciones y desarrollo experimental en el campo de las ciencias sociales y las humanidades"
,"Lavado y limpieza de prendas de tela y de piel, incluso la limpieza en seco"
,"Mantenimiento y reparación de maquinaria de oficina, contabilidad e informática"
,"Obtención y dotación de personal"
,"Organizaciones y órganos extraterritoriales"
,"Otras actividades de edición"
,"Otras actividades de entretenimiento n.c.p."
,"Otras actividades de esparcimiento"
,"Otras actividades de informática"
,"Otras actividades de servicios n.c.p."
,"Otras actividades empresariales n.c.p."
,"Otras actividades relacionadas con la salud humana"
,"Otros tipos de crédito"
,"Otros tipos de intermediación financiera n.c.p."
,"Otros tipos de intermediación monetaria"
,"Otros tipos de venta al por menor no realizado en almacenes"
,"Peluquería y otros tratamientos de belleza"
,"Planes de pensiones"
,"Planes de seguros de vida"
,"Planes de seguros generales"
,"Pompas fúnebres y actividades conexas"
,"Procesamiento de datos"
,"Producción y distribución de filmes y videocintas"
,"Publicidad"
,"Regulación de las actividades de organismos que prestan servicios sanitarios, educativos, culturales y otros servicios sociales, excepto servicios de seguridad social."
,"Regulación y facilitación de la actividad económica"
,"Relaciones exteriores"
,"Reparación de efectos personales y enseres domesticos"
,"Reproducción de grabaciones"
,"Servicios sociales con alojamiento"
,"Servicios sociales sin alojamiento"
,"Telecomunicaciones"
,"Venta al por mayor a cambio de una retribución o por contrata"
,"Venta al por mayor de alimentos, bebidas y tabaco"
,"Venta al por mayor de maquinaria, equipo y materiales"
,"Venta al por mayor de materiales de contrucción, artículos de ferretería y equipo y materiales de fontanería y calefacción"
,"Venta al por mayor de materias primas agropecuarias y de animales vivos"
,"Venta al por mayor de metales y minerales metaliferos"
,"Venta al por mayor de otros enseres domesticos"
,"Venta al por mayor de otros productos"
,"Venta al por mayor de otros productos intermedios, desperdicios y desechos"
,"Venta al por mayor de productos textiles, prendas de vestir y calzado"
,"Venta al por menor de alimentos, bebidas y tabaco en almacenes especializados"
,"Venta al por menor de aparatos, artículos y equipo de uso domestico"
,"Venta al por menor de artículos de ferreteria, pinturas y productos de vidrio"
,"Venta al por menor de casas de venta por correo"
,"Venta al por menor de otros productos en almacenes especializados"
,"Venta al por menor de otros productos en almacenes no especializados"
,"Venta al por menor de productos farmaceuticos y medicinales, cosmeticos y artículos de tocador"
,"Venta al por menor de productos textiles, prendas de vestir, calzado y artículos de cuero"
,"Venta al por menor en almacenes de artículos usados"
,"Venta al por menor en almacenes no especializados con surtido compuesto principalemente de alimentos, bebidas y tabaco"
,"Venta al por menor en puestos de venta y mercados"
,"Venta al por menor no especificado"
,"Venta de partes, piezas y accesorios de vehiculos automotores"
,"Venta de vehiculos automotores"
);

ALTER TABLE `siv_db`.`multitabla` 
ADD COLUMN `estado_emision` CHAR(1) NULL DEFAULT NULL AFTER `valor_flex`;


SELECT * FROM multitabla m where nombre_tabla = "Actividad Económica" and codigo in 
("2"
,"6"
,"8"
,"13"
,"18"
,"19"
,"21"
,"22"
,"26"
,"28"
,"30"
,"32"
,"33"
,"35"
,"37"
,"38"
,"41"
,"46"
,"58"
,"61"
,"62"
,"63"
,"64"
,"70"
,"71"
,"72"
,"73"
,"74"
,"75"
,"79"
,"80"
,"81"
,"82"
,"83"
,"84"
,"85"
,"86"
,"87"
,"88"
,"89"
,"90"
,"91"
,"92"
,"93"
,"94"
,"95"
,"96"
,"97"
,"98"
,"99"
,"106"
,"107"
,"108"
,"109"
,"110"
,"111"
,"112"
,"113"
,"114"
,"115"
,"116"
,"117"
,"118"
,"119"
,"121"
,"122"
,"123"
,"125"
,"127"
,"128"
,"129"
,"130"
,"131"
,"132"
,"133"
,"134"
,"135"
,"136"
,"137"
,"138"
,"139"
,"140"
,"141"
,"142"
,"146"
,"149"
,"150"
,"152"
,"153"
,"154"
,"155"
,"156"
,"157"
,"158"
,"159"
,"160"
,"161"
,"162"
,"164"
,"165"
,"166"
,"167"
,"168"
,"169"
,"170"
,"171"
,"172"
,"173"
,"174"
,"175"
,"176"
,"177"
,"178"
,"179"
,"180"
,"181"
,"182"
,"184"
,"185"
,"186"
,"187"
,"188"
,"189"
,"190"
,"191"
,"192"
,"194"
,"195"
,"198"
,"199"
,"200"
,"201"
,"202"
,"203"
,"204"
,"205"
,"208"
,"213"
,"215"
,"223"
,"225"
,"227"
,"231"
,"232"
,"235"
,"240"
,"241"
,"244"
,"246"
,"247"
,"253"
,"256"
,"257"
,"259"
,"260"
,"261"
,"262"
,"263"
,"264"
,"265"
,"266"
,"267"
,"270"
,"283");


SELECT * FROM multitabla m where nombre_tabla = "Profesión" and codigo in 
("1"
,"5"
,"6"
,"9"
,"13"
,"16"
,"18"
,"19"
,"20"
,"24"
,"25"
,"28"
,"29"
,"30"
,"31"
,"32"
,"36"
,"37"
,"38"
,"39"
,"42"
,"46"
,"50"
,"54"
,"55"
,"58"
,"59"
,"61"
,"64"
,"67"
,"69"
,"72"
,"74"
,"75"
,"77"
,"82"
,"83"
,"84"
,"85"
,"86"
,"87"
,"88"
,"90"
,"96"
,"99"
,"101"
,"102"
,"106"
,"107"
,"108"
,"110"
,"111"
,"118"
,"119"
,"120"
,"121"
,"122"
);

select * from multitabla m ;
select * from persona p where numero_documento = "74965244";
select * from solicitud s where id_asegurado = 176722;
select * from solicitud_producto sp where id_solicitud = 111030;
select * from solicitud_producto_detalle spd where id_solicitud_producto = 41616;
select * from solicitud_dps sd where id_solicitud  = 111030;
select * from solicitud_dps_pregunta sdp where id_solicitud_dps  = 34997;
select * from solicitud_beneficiario sb where id_solicitud  = 167452;

configuraciondeparametro <-- validar parametros

insert into siv_db.multitabla values 
(3473,"065","Coonfiguracion de Parametros de Evaluacion",1,1,"Alithu Marquez","Emision Rapida",null,null,1,1,"admin","2022-09-21 00:54:00",null,null);


SELECT idsolicituddevolucionresp, idsolicitud, openitems, estado, devolucionpendiente, mensaje, logspayment FROM public.solicituddevolucionresp where idsolicitud = 65 and estado = 1;
