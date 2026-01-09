 ALTER TABLE `siv_db`.`persona`
  ADD COLUMN `razon_social` VARCHAR(100) NULL,
  ADD COLUMN `estado_civil` CHAR(1) NULL,
  ADD COLUMN `nacionalidad` VARCHAR(3) NULL,
  ADD COLUMN `direccion_tipo` CHAR(1) NULL,
  ADD COLUMN `direccion_tipo_des` VARCHAR(100) NULL,
  ADD COLUMN `direccion_nro_mz` VARCHAR(20) NULL,
  ADD COLUMN `direccion_interior` VARCHAR(20) NULL,
  ADD COLUMN `direccion_urbanizacion` VARCHAR(45) NULL,
  ADD COLUMN `departamento` VARCHAR(3) NULL,
  ADD COLUMN `provincia` VARCHAR(3) NULL,
  ADD COLUMN `distrito` VARCHAR(4) NULL,
  ADD COLUMN `es_pep` CHAR(1) NULL,
  ADD COLUMN `es_sujeto_obligado` CHAR(1) NULL,
  ADD COLUMN `centro_trabajo` VARCHAR(100) NULL,
  ADD COLUMN `ingreso_moneda` CHAR(1) NULL,
  ADD COLUMN `ingreso_valor` INT(7) NULL,
  ADD COLUMN `profesion_detalle` VARCHAR(100) NULL;

  

ALTER TABLE `siv_db`.`persona` 
CHANGE COLUMN `fecha_nacimiento` `fecha_nacimiento` DATE NULL ,
CHANGE COLUMN `nombres` `nombres` VARCHAR(100) NULL ,
CHANGE COLUMN `apellido_paterno` `apellido_paterno` VARCHAR(50) NULL ,
CHANGE COLUMN `apellido_materno` `apellido_materno` VARCHAR(50) NULL ,
CHANGE COLUMN `genero` `genero` CHAR(1) NULL ;


ALTER TABLE `siv_db`.`persona` 
CHANGE COLUMN `tipo_documento` `tipo_documento` VARCHAR(2) NOT NULL ;


ALTER TABLE `siv_db`.`multitabla` 
CHANGE COLUMN `valor` `valor` TEXT NULL DEFAULT NULL ;


ALTER TABLE `siv_db`.`persona` 
DROP INDEX `numero_documento_UNIQUE` ,
ADD UNIQUE INDEX `numero_documento_UNIQUE` (`numero_documento` ASC, `tipo_documento` ASC) VISIBLE;

-- -----------------------------------------------------
-- Table `siv_db`.`solicitud`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `siv_db`.`solicitud` ;

CREATE TABLE IF NOT EXISTS `siv_db`.`solicitud` (
  `id_solicitud` INT NOT NULL AUTO_INCREMENT,
  `id_asegurado` INT(11) NOT NULL,
  `id_contratante` INT(11) NULL,
  `numero_cotizacion` VARCHAR(20) NOT NULL,
  `numero_propuesta` VARCHAR(20) NULL,
  `numero_poliza` VARCHAR(20) NULL,
  `asegurado_igual_contratante` CHAR(1) NULL,
  `vinculo_asegurado` CHAR(1) NULL,
  `json_contratante` TEXT NULL,
  `json_beneficiario` TEXT NULL,
  `estado` CHAR(1) NOT NULL,
  `subplan` CHAR(1) NULL,
  `moneda` CHAR(1) NULL,
  `id_circuito_firma` VARCHAR(10) NULL,
  `tratamiento_asegurado_check` TINYINT(4) NULL,
  `tratamiento_contratante_check` TINYINT(4) NULL,
  `fecha_firma_asegurado` TIMESTAMP NULL,
  `fecha_firma_contratante` TIMESTAMP NULL,
  `nombre_archivo_solicitud` VARCHAR(50) NULL,
  `id_crm_oportunidad` VARCHAR(40) NULL,
  `id_crm_cotizador` VARCHAR(40) NULL,
  `id_crm_usuario` VARCHAR(40) NULL,
  `agente_nombres` VARCHAR(150) NULL,
  `agente_num_vendedor` VARCHAR(20),
  `agente_correo` VARCHAR(150) NULL,
  `fecha_solicitud` TIMESTAMP NULL,
  `usuario_crea` VARCHAR(50) NOT NULL,
  `fecha_crea` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modif` VARCHAR(50) NULL,
  `fecha_modif` TIMESTAMP NULL,
  PRIMARY KEY (`id_solicitud`),
  CONSTRAINT `fk_solicitud_persona`
    FOREIGN KEY (`id_asegurado`)
    REFERENCES `siv_db`.`persona` (`id_persona`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_solicitud_persona1`
    FOREIGN KEY (`id_contratante`)
    REFERENCES `siv_db`.`persona` (`id_persona`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_solicitud_persona_idx` ON `siv_db`.`solicitud` (`id_asegurado` ASC) VISIBLE;

CREATE INDEX `fk_solicitud_persona1_idx` ON `siv_db`.`solicitud` (`id_contratante` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `siv_db`.`solicitud_beneficiario`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `siv_db`.`solicitud_beneficiario` ;

CREATE TABLE IF NOT EXISTS `siv_db`.`solicitud_beneficiario` (
  `id_solicitud_beneficiario` INT NOT NULL AUTO_INCREMENT,
  `id_solicitud` INT NOT NULL,
  `tipo_beneficiario` CHAR(1) NOT NULL,
  `nombres` VARCHAR(100) NULL,
  `apellido_paterno` VARCHAR(50) NULL,
  `apellido_materno` VARCHAR(50) NULL,
  `fecha_nacimiento` DATE NULL,
  `tipo_documento` VARCHAR(2) NULL,
  `numero_documento` VARCHAR(20) NULL,
  `tipo_relacion` VARCHAR(2) NULL,
  `distribucion` INT(3) NULL,
  `usuario_crea` VARCHAR(50) NOT NULL,
  `fecha_crea` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modif` VARCHAR(50) NULL,
  `fecha_modif` TIMESTAMP NULL,
  PRIMARY KEY (`id_solicitud_beneficiario`),
  CONSTRAINT `fk_solicitud_beneficiario_solicitud1`
    FOREIGN KEY (`id_solicitud`)
    REFERENCES `siv_db`.`solicitud` (`id_solicitud`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_solicitud_beneficiario_solicitud1_idx` ON `siv_db`.`solicitud_beneficiario` (`id_solicitud` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `siv_db`.`solicitud_dps`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `siv_db`.`solicitud_dps` ;

CREATE TABLE IF NOT EXISTS `siv_db`.`solicitud_dps` (
  `id_solicitud_dps` INT NOT NULL AUTO_INCREMENT,
  `id_solicitud` INT NOT NULL,
  `estatura` FLOAT(5,2) NULL,
  `peso` FLOAT(5,2) NULL,
  `imc` FLOAT(5,2) NULL,
  `peso_variacion_cantidad` INT(3) NULL,
  `peso_variacion` INT(1) NULL,
  `peso_aumento_disminuyo` TINYINT(1) NULL,
  `peso_ad_cantidad` FLOAT(5,2) NULL,
  `peso_motivo` VARCHAR(80) NULL,
  `fumador` TINYINT(1) NULL,
  `fumador_cantidad` VARCHAR(50) NULL,
  `fumador_frecuencia` VARCHAR(50) NULL,
  `drogas` TINYINT(1) NULL,
  `drogas_fecha` VARCHAR(50) NULL,
  `alcohol` TINYINT(1) NULL,
  `alcohol_cantidad` VARCHAR(50) NULL,
  `alcohol_frecuencia` VARCHAR(50) NULL,
  `usuario_crea` VARCHAR(50) NOT NULL,
  `fecha_crea` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modif` VARCHAR(50) NULL,
  `fecha_modif` TIMESTAMP NULL,
  PRIMARY KEY (`id_solicitud_dps`),
  CONSTRAINT `fk_solicitud_dps_solicitud1`
    FOREIGN KEY (`id_solicitud`)
    REFERENCES `siv_db`.`solicitud` (`id_solicitud`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_solicitud_dps_solicitud1_idx` ON `siv_db`.`solicitud_dps` (`id_solicitud` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `siv_db`.`solicitud_dps_pregunta`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `siv_db`.`solicitud_dps_pregunta` ;

CREATE TABLE IF NOT EXISTS `siv_db`.`solicitud_dps_pregunta` (
  `id_solicitud_dps_pregunta` INT NOT NULL AUTO_INCREMENT,
  `id_solicitud_dps` INT NOT NULL,
  `bloque_pregunta` INT(1) NULL,
  `item` VARCHAR(2) NULL,
  `pregunta` VARCHAR(2) NULL,
  `respuesta` TINYINT(1) NULL,
  `detalle` TEXT NULL,
  `enfermedad` VARCHAR(150) NULL,
  `fecha_diagnostico` VARCHAR(50) NULL,
  `condicion_actual` VARCHAR(100) NULL,
  `nombre_medico_hospital` VARCHAR(100) NULL,
  `usuario_crea` VARCHAR(50) NOT NULL,
  `fecha_crea` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modif` VARCHAR(50) NULL,
  `fecha_modif` TIMESTAMP NULL,
  PRIMARY KEY (`id_solicitud_dps_pregunta`),
  CONSTRAINT `fk_solicitud_dps_pregunta_solicitud_dps1`
    FOREIGN KEY (`id_solicitud_dps`)
    REFERENCES `siv_db`.`solicitud_dps` (`id_solicitud_dps`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_solicitud_dps_pregunta_solicitud_dps1_idx` ON `siv_db`.`solicitud_dps_pregunta` (`id_solicitud_dps` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `siv_db`.`solicitud_producto`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `siv_db`.`solicitud_producto` ;

CREATE TABLE IF NOT EXISTS `siv_db`.`solicitud_producto` (
  `id_solicitud_producto` INT NOT NULL AUTO_INCREMENT,
  `id_solicitud` INT NOT NULL,
  `tipo_producto` VARCHAR(2) NOT NULL,
  `monto_fondo_garantizado` FLOAT(10,2) NOT NULL,
  `periodo_cobertura_anual` INT(3) NOT NULL,
  `anualidad_pago` INT(3) NOT NULL,
  `frecuencia` CHAR(1) NOT NULL,
  `tipo_cuota` CHAR(1) NOT NULL,
  `tipo_riesgo` CHAR(1) NULL,
  `prima_comercial_anual` FLOAT(10,2) NOT NULL,
  `factor_pago` FLOAT(10,2) NOT NULL,
  `prima_comercial` FLOAT(10,2) NOT NULL,
  `usuario_crea` VARCHAR(50) NOT NULL,
  `fecha_crea` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modif` VARCHAR(50) NULL,
  `fecha_modif` TIMESTAMP NULL,
  PRIMARY KEY (`id_solicitud_producto`),
  CONSTRAINT `fk_solicitud_producto_solicitud1`
    FOREIGN KEY (`id_solicitud`)
    REFERENCES `siv_db`.`solicitud` (`id_solicitud`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_solicitud_producto_solicitud1_idx` ON `siv_db`.`solicitud_producto` (`id_solicitud` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `siv_db`.`solicitud_producto_detalle`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `siv_db`.`solicitud_producto_detalle` ;

CREATE TABLE IF NOT EXISTS `siv_db`.`solicitud_producto_detalle` (
  `id_solicitud_producto_detalle` INT NOT NULL AUTO_INCREMENT,
  `id_solicitud_producto` INT NOT NULL,
  `cobertura` VARCHAR(2) NOT NULL,
  `tipo_cobertura` CHAR(1) NOT NULL,
  `capital_asegurado` FLOAT(10,2) NOT NULL,
  `prima_anual` FLOAT(10,2) NOT NULL,
  `usuario_crea` VARCHAR(50) NOT NULL,
  `fecha_crea` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modif` VARCHAR(50) NULL,
  `fecha_modif` TIMESTAMP NULL,
  PRIMARY KEY (`id_solicitud_producto_detalle`),
  CONSTRAINT `fk_solicitud_producto_detalle_solicitud_producto1`
    FOREIGN KEY (`id_solicitud_producto`)
    REFERENCES `siv_db`.`solicitud_producto` (`id_solicitud_producto`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_solicitud_producto_detalle_solicitud_producto1_idx` ON `siv_db`.`solicitud_producto_detalle` (`id_solicitud_producto` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `siv_db`.`solicitud_agente`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `siv_db`.`solicitud_agente` ;

CREATE TABLE IF NOT EXISTS `siv_db`.`solicitud_agente` (
  `idsolicitud_agente` INT NOT NULL AUTO_INCREMENT,
  `codigo_agente` VARCHAR(10) NOT NULL,
  `numero_documento` VARCHAR(12) NULL,
  `nombre_agente` VARCHAR(100) NULL,
  PRIMARY KEY (`idsolicitud_agente`))
ENGINE = InnoDB;


-------------------------------------------------
-------------------------------------------------
-------------------------------------------------


CREATE TABLE `codigo_verificacion` (
  `id_codigo_verificacion` int(11) NOT NULL AUTO_INCREMENT,
  `id_solicitud` int(11) NOT NULL,
  `id_persona` int(11) NOT NULL,
  `celular` int(9) NOT NULL,
  `codigo` varchar(100) COLLATE utf8_spanish_ci NOT NULL,
  `enviado` tinyint(1) NOT NULL DEFAULT '0',
  `usado` tinyint(1) NOT NULL DEFAULT '0',
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `fecha_expiracion` timestamp NOT NULL,
  `fecha_usado` timestamp NULL DEFAULT NULL,
  `usuario_crea` varchar(50) COLLATE utf8_spanish_ci DEFAULT NULL,
  `fecha_crea` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modif` varchar(50) COLLATE utf8_spanish_ci DEFAULT NULL,
  `fecha_modif` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id_codigo_verificacion`)
) ;


CREATE TABLE `usuario_perfil` (
  `id_usuario_perfil` int(11) NOT NULL AUTO_INCREMENT,
  `usuario` varchar(100) CHARACTER SET utf8 COLLATE utf8_spanish_ci NOT NULL,
  `perfil` varchar(2) CHARACTER SET utf8 COLLATE utf8_spanish_ci NOT NULL,
  `usuario_crea` varchar(50) CHARACTER SET utf8 COLLATE utf8_spanish_ci NOT NULL,
  `fecha_crea` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_modif` varchar(50) CHARACTER SET utf8 COLLATE utf8_spanish_ci DEFAULT NULL,
  `fecha_modif` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id_usuario_perfil`)
);



CREATE VIEW siv_db.view_solicitudes
AS
select `s`.`id_solicitud` AS `id_solicitud`,`s`.`numero_propuesta` AS `numero_propuesta`,`s`.`numero_poliza` AS `numero_poliza`,`td`.`valor` AS `tipo_documento`,`a`.`numero_documento` AS `numero_documento`,concat(`a`.`apellido_paterno`,' ',`a`.`apellido_materno`,', ',`a`.`nombres`) AS `cliente`,'Nuevo Educación Garantizada' AS `sub_plan`,`s`.`fecha_solicitud` AS `fecha_solicitud`,`e`.`valor` AS `estado_nombre`,`s`.`estado` AS `estado` from ((((`solicitud` `s` left join `persona` `a` on((`s`.`id_asegurado` = `a`.`id_persona`))) left join `multitabla` `td` on(((`td`.`codigo_tabla` = '001') and (`td`.`codigo` = `a`.`tipo_documento`)))) left join `multitabla` `e` on(((`e`.`codigo_tabla` = '026') and (`e`.`codigo` = `s`.`estado`)))) left join `multitabla` `sp` on(((`sp`.`codigo_tabla` = '021') and (`sp`.`codigo` = `s`.`subplan`))));




