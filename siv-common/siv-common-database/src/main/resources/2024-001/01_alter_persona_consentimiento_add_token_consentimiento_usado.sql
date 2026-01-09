-- Script de migración para agregar la columna token_consentimiento_usado a la tabla persona_consentimiento
-- Fecha: 2024
-- Descripción: Agregar campo booleano token_consentimiento_usado para controlar el uso de tokens de consentimiento

-- Agregar la nueva columna token_consentimiento_usado como TINYINT(1) (equivalente a boolean en MySQL)
ALTER TABLE `siv_db`.`persona_consentimiento` 
ADD COLUMN `token_consentimiento_usado` TINYINT(1) NULL DEFAULT NULL 
AFTER `fecha_modif`;

-- Comentario explicativo de la columna
ALTER TABLE `siv_db`.`persona_consentimiento` 
MODIFY COLUMN `token_consentimiento_usado` TINYINT(1) NULL DEFAULT NULL 
COMMENT 'Indica si el token de consentimiento ha sido usado (0=No usado, 1=Usado, NULL=No aplica)';
