-- Script para agregar campo para controlar el uso del token de consentimiento
-- Tabla: persona_consentimiento
-- Base de datos: siv_db

-- Agregar campo para controlar si el token de consentimiento ya fue usado
ALTER TABLE `siv_db`.`persona_consentimiento` 
ADD COLUMN `token_consentimiento_usado` INTEGER NULL DEFAULT NULL COMMENT 'Indica si el token de consentimiento ya fue usado (1=usado, NULL=no usado)' AFTER `fecha_modif`;

-- Agregar índice para optimizar consultas por uso del token
ALTER TABLE `siv_db`.`persona_consentimiento` 
ADD INDEX `idx_token_consentimiento_usado` (`token_consentimiento_usado`);

-- Comentario adicional en la tabla para documentar el nuevo campo
ALTER TABLE `siv_db`.`persona_consentimiento` 
COMMENT = 'Tabla para gestionar el consentimiento de personas con control de uso único del token';

-- Verificar la estructura actualizada de la tabla
-- DESCRIBE `siv_db`.`persona_consentimiento`;

/*
Explicación del nuevo campo:
- token_consentimiento_usado: INTEGER NULL
  - NULL = Token no ha sido usado aún
  - 1 = Token ya fue usado (consentimiento aceptado)
  - Se actualiza a 1 cuando se procesa el consentimiento aceptado por primera vez

Lógica de funcionamiento:
1. Al generar el consentimiento: El campo permanece NULL
2. Al usar el token por primera vez: Se actualiza a 1
3. En usos posteriores: Si es 1, se envía PDF de constancia en lugar del correo normal

Ventajas:
- No requiere cache en memoria
- Funciona entre diferentes servidores/aplicaciones
- Persiste el control de uso en base de datos
- Permite envío automático de constancia cuando el token ya fue usado
- Simple y eficiente
*/
