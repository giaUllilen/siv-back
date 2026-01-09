# Configuración - Rentas API Cotizador RV

## Descripción General

Este documento describe todas las variables de entorno, configuraciones y parámetros necesarios para ejecutar correctamente el servicio **Rentas API Cotizador RV** (Rentas Vitalicias).

## Variables de Entorno

Todas las variables de entorno deben configurarse según el entorno (development, staging, production). En GCP Cloud Run, estas variables se gestionan a través de **Secret Manager**.

### Configuración de la Aplicación

#### `PORT`
- **Descripción:** Puerto en el que el servidor escucha peticiones HTTP
- **Tipo:** Number
- **Requerido:** No
- **Valor por defecto:** `8080`
- **Ejemplo:** `8080`
- **Notas:** Cloud Run requiere que la aplicación escuche en el puerto definido por esta variable

#### `APP_NAME`
- **Descripción:** Nombre descriptivo de la aplicación para logs
- **Tipo:** String
- **Requerido:** Sí
- **Ejemplo:** `Rentas API Cotizador RV - Production`

#### `APP_BASE_PATH`
- **Descripción:** Ruta base para todos los endpoints de la API
- **Tipo:** String
- **Requerido:** Sí
- **Ejemplo:** `/api/v1`
- **Notas:** Prefijo para todas las rutas de la API

#### `LOG_LEVEL`
- **Descripción:** Nivel de logging de la aplicación
- **Tipo:** String
- **Requerido:** No
- **Valores permitidos:** `trace`, `debug`, `info`, `warn`, `error`, `fatal`
- **Valor por defecto:** `info`
- **Ejemplo:** `info`
- **Notas:** Afecta la cantidad de logs generados

### Configuración de CORS

#### `CORS_ORIGIN`
- **Descripción:** Orígenes permitidos para peticiones CORS (separados por coma)
- **Tipo:** String (comma-separated)
- **Requerido:** No
- **Ejemplo:** `https://app.interseguro.com,https://admin.interseguro.com`
- **Notas:** Si no se configura, se permite cualquier origen (*)

#### `CORS_ADDITIONAL_HEADERS`
- **Descripción:** Headers adicionales permitidos en CORS (separados por coma)
- **Tipo:** String (comma-separated)
- **Requerido:** No
- **Ejemplo:** `X-Custom-Header,X-Api-Key`
- **Notas:** Headers adicionales que se permiten en las peticiones CORS

### Base de Datos SQL Server (EXP_SERV)

Base de datos principal de la aplicación para operaciones del cotizador.

#### `DB_EXP_SERV_HOST`
- **Descripción:** Host del servidor SQL Server
- **Tipo:** String
- **Requerido:** Sí
- **Ejemplo:** `10.0.0.5` o `sqlserver-expserv.internal`

#### `DB_EXP_SERV_PORT`
- **Descripción:** Puerto de SQL Server
- **Tipo:** Number
- **Requerido:** Sí
- **Valor por defecto:** `1433`
- **Ejemplo:** `1433`

#### `DB_EXP_SERV_USER`
- **Descripción:** Usuario de SQL Server con permisos de lectura/escritura
- **Tipo:** String
- **Requerido:** Sí
- **Ejemplo:** `cotizador_user`
- **Notas:** Debe tener permisos para ejecutar procedimientos almacenados

#### `DB_EXP_SERV_PASSWORD`
- **Descripción:** Contraseña del usuario de SQL Server
- **Tipo:** String (Sensitive)
- **Requerido:** Sí
- **Ejemplo:** `S3cur3P@ssw0rd!`
- **Seguridad:** ⚠️ Usar Secret Manager en GCP

#### `DB_EXP_SERV_DATABASE`
- **Descripción:** Nombre de la base de datos SQL Server
- **Tipo:** String
- **Requerido:** Sí
- **Ejemplo:** `EXP_SERV_PRODUCTION`

### Configuración de Negocio - Parámetros del Cotizador

#### `MAX_ITEM_PBS`
- **Descripción:** Número máximo de ítems PBS permitidos
- **Tipo:** String (Number)
- **Requerido:** Sí
- **Ejemplo:** `10`

#### `KEY_ACOM`
- **Descripción:** Indicador para validar porcentaje de aumento de comisión (S/N)
- **Tipo:** String
- **Requerido:** Sí
- **Valores permitidos:** `S`, `N`
- **Ejemplo:** `S`

#### `KEY_DCOM`
- **Descripción:** Indicador para validar porcentaje de descuento de comisión (S/N)
- **Tipo:** String
- **Requerido:** Sí
- **Valores permitidos:** `S`, `N`
- **Ejemplo:** `S`

#### `VALIDACIONES_CRM`
- **Descripción:** Indicador para activar validaciones de CRM (S/N)
- **Tipo:** String
- **Requerido:** Sí
- **Valores permitidos:** `S`, `N`
- **Ejemplo:** `N`

#### `VALIDACIONES_SDA`
- **Descripción:** Indicador para activar validaciones de SDA (S/N)
- **Tipo:** String
- **Requerido:** Sí
- **Valores permitidos:** `S`, `N`
- **Ejemplo:** `N`

#### `VALIDA_CUOTAS`
- **Descripción:** Indicador para validar cuotas TRA (S/N)
- **Tipo:** String
- **Requerido:** Sí
- **Valores permitidos:** `S`, `N`
- **Ejemplo:** `S`

### Configuración de Usuarios y Roles

#### `RANGO_IPS_PARAMETROS_ESPECIALES`
- **Descripción:** Rangos de IPs permitidas para parámetros especiales (separadas por coma)
- **Tipo:** String (comma-separated)
- **Requerido:** Sí
- **Ejemplo:** `192.168.1.0/24,10.0.0.0/8`
- **Notas:** Define qué IPs pueden usar ACOM y DCOM

#### `USUARIO_JEFE_OPERACIONES`
- **Descripción:** Código del usuario jefe de operaciones
- **Tipo:** String
- **Requerido:** Sí
- **Ejemplo:** `JEFE_OPS`

#### `USUARIO_GERENTE_COMERCIAL`
- **Descripción:** Código del usuario gerente comercial
- **Tipo:** String
- **Requerido:** Sí
- **Ejemplo:** `GERENTE_COM`

#### `USUARIOS_NOTIFICACIONES`
- **Descripción:** Lista de usuarios para notificaciones (separados por coma)
- **Tipo:** String (comma-separated)
- **Requerido:** No
- **Ejemplo:** `usuario1,usuario2,usuario3`

#### `TZ`
- **Descripción:** Zona horaria de la aplicación
- **Tipo:** String
- **Requerido:** Sí
- **Ejemplo:** `America/Lima`
- **Notas:** Usado para el manejo correcto de fechas

### APIs Externas - Integraciones

#### `API_RENTAS_PARAMETROS`
- **Descripción:** URL del servicio de parámetros de rentas para validaciones
- **Tipo:** String (URL)
- **Requerido:** Sí
- **Ejemplo:** `https://is-cr-adn-api-rentas-parametros-uat-m3pd7zj7mq-uc.a.run.app/api/v1`
- **Notas:** API para validar IPs permitidas y obtener parámetros de configuración

#### `API_WS_CWRV_WSDL`
- **Descripción:** URL del WSDL del servicio SOAP de cotización CWRV
- **Tipo:** String (URL)
- **Requerido:** Sí
- **Ejemplo:** `http://soap-service.internal/cwrv?wsdl`
- **Notas:** Servicio SOAP para cálculos actuariales de cotización

#### `API_WS_CWRV_URL`
- **Descripción:** URL del endpoint del servicio SOAP de cotización CWRV
- **Tipo:** String (URL)
- **Requerido:** Sí
- **Ejemplo:** `http://soap-service.internal/cwrv`
- **Notas:** Endpoint para invocar métodos del servicio SOAP

#### `API_LOGS_AUDITORIA`
- **Descripción:** URL del servicio de logs de auditoría
- **Tipo:** String (URL)
- **Requerido:** Sí
- **Ejemplo:** `https://is-cr-adn-api-logs-auditoria-uat-m3pd7zj7mq-uc.a.run.app/api/v1`
- **Notas:** Servicio para registrar eventos de auditoría

#### `API_ESTRUCTURA_AGENTE`
- **Descripción:** URL del servicio de estructura comercial para consultar agentes
- **Tipo:** String (URL)
- **Requerido:** Sí
- **Ejemplo:** `https://is-cr-adn-api-estructura-comercial-uat-m3pd7zj7mq-uc.a.run.app/api/v1`
- **Notas:** Servicio para obtener información de agentes y carteras

#### `API_SOLICITUD_CAMBIO`
- **Descripción:** URL del servicio de solicitud de cambio
- **Tipo:** String (URL)
- **Requerido:** Sí
- **Ejemplo:** `https://is-cr-adn-api-solicitud-cambio-uat-m3pd7zj7mq-uc.a.run.app/api/v1`

#### `API_WEB_SEGURIDAD_URL`
- **Descripción:** URL del servicio de seguridad web
- **Tipo:** String (URL)
- **Requerido:** Sí
- **Ejemplo:** `https://is-cr-adn-api-seguridad-uat-m3pd7zj7mq-uc.a.run.app/api/v1`

#### `API_RVIADM_BACKEND`
- **Descripción:** URL del servicio backend de RVIADM
- **Tipo:** String (URL)
- **Requerido:** Sí
- **Ejemplo:** `https://is-cr-adn-api-rviadm-backend-uat-m3pd7zj7mq-uc.a.run.app/api/v1`

### Configuración de Notificaciones y Correos

#### `ENVIAR_NOTIFICACIONES`
- **Descripción:** Indicador para activar envío de notificaciones (S/N)
- **Tipo:** String
- **Requerido:** Sí
- **Valores permitidos:** `S`, `N`
- **Ejemplo:** `S`

#### `ENVIAR_CORREO_PRUEBA`
- **Descripción:** Indicador para enviar correos a dirección de prueba (S/N)
- **Tipo:** String
- **Requerido:** No
- **Valores permitidos:** `S`, `N`
- **Ejemplo:** `N`
- **Notas:** Útil en ambientes de desarrollo/testing

#### `CORREO_PRUEBA`
- **Descripción:** Dirección de correo para pruebas
- **Tipo:** String (Email)
- **Requerido:** No (Sí si ENVIAR_CORREO_PRUEBA=S)
- **Ejemplo:** `test@interseguro.com`

#### `URL_API_NOTIFY_URL`
- **Descripción:** URL del servicio de notificaciones por correo
- **Tipo:** String (URL)
- **Requerido:** Sí
- **Ejemplo:** `https://is-cr-adn-api-notify-uat-m3pd7zj7mq-uc.a.run.app/api/v1`
- **Notas:** API para envío de correos electrónicos

#### `CORREO_REMITENTE`
- **Descripción:** Dirección de correo del remitente
- **Tipo:** String (Email)
- **Requerido:** Sí
- **Ejemplo:** `noreply@interseguro.com`

#### `CORREO_REMITENTE_NOMBRE`
- **Descripción:** Nombre visible del remitente de correos
- **Tipo:** String
- **Requerido:** Sí
- **Ejemplo:** `Interseguro - Rentas Vitalicias`

#### `API_NOTIFY_PRIORITY`
- **Descripción:** Prioridad de envío de notificaciones
- **Tipo:** String
- **Requerido:** No
- **Valores permitidos:** `HIGH`, `NORMAL`, `LOW`
- **Ejemplo:** `NORMAL`

### Configuración de Swagger

La documentación de la API se expone automáticamente mediante Swagger UI. No requiere configuración adicional de autenticación básica en este proyecto.

- **Endpoint:** `/documentation`
- **Notas:** Acceso público a la documentación de la API

## Ejemplo de Archivo .env

### Development
```bash
# Aplicación
PORT=8080
APP_NAME=Rentas API Cotizador RV - Development
APP_BASE_PATH=/api/v1
LOG_LEVEL=debug
CORS_ORIGIN=http://localhost:3000,http://localhost:4200
CORS_ADDITIONAL_HEADERS=

# SQL Server (EXP_SERV)
DB_EXP_SERV_HOST=localhost
DB_EXP_SERV_PORT=1433
DB_EXP_SERV_USER=sa
DB_EXP_SERV_PASSWORD=YourPassword123!
DB_EXP_SERV_DATABASE=EXP_SERV_DEV

# Parámetros del Cotizador
MAX_ITEM_PBS=10
KEY_ACOM=S
KEY_DCOM=S
VALIDACIONES_CRM=N
VALIDACIONES_SDA=N
VALIDA_CUOTAS=S

# Usuarios y Roles
RANGO_IPS_PARAMETROS_ESPECIALES=192.168.1.0/24,10.0.0.0/8
USUARIO_JEFE_OPERACIONES=JEFE_OPS_DEV
USUARIO_GERENTE_COMERCIAL=GERENTE_COM_DEV
USUARIOS_NOTIFICACIONES=dev1,dev2
TZ=America/Lima

# APIs Externas
API_RENTAS_PARAMETROS=https://is-cr-adn-api-rentas-parametros-dev-m3pd7zj7mq-uc.a.run.app/api/v1
API_WS_CWRV_WSDL=http://soap-service-dev.internal/cwrv?wsdl
API_WS_CWRV_URL=http://soap-service-dev.internal/cwrv
API_LOGS_AUDITORIA=https://is-cr-adn-api-logs-auditoria-dev-m3pd7zj7mq-uc.a.run.app/api/v1
API_ESTRUCTURA_AGENTE=https://is-cr-adn-api-estructura-comercial-dev-m3pd7zj7mq-uc.a.run.app/api/v1
API_SOLICITUD_CAMBIO=https://is-cr-adn-api-solicitud-cambio-dev-m3pd7zj7mq-uc.a.run.app/api/v1
API_WEB_SEGURIDAD_URL=https://is-cr-adn-api-seguridad-dev-m3pd7zj7mq-uc.a.run.app/api/v1
API_RVIADM_BACKEND=https://is-cr-adn-api-rviadm-backend-dev-m3pd7zj7mq-uc.a.run.app/api/v1

# Notificaciones y Correos
ENVIAR_NOTIFICACIONES=S
ENVIAR_CORREO_PRUEBA=S
CORREO_PRUEBA=dev@interseguro.com
URL_API_NOTIFY_URL=https://is-cr-adn-api-notify-dev-m3pd7zj7mq-uc.a.run.app/api/v1
CORREO_REMITENTE=noreply-dev@interseguro.com
CORREO_REMITENTE_NOMBRE=Interseguro RV - Development
API_NOTIFY_PRIORITY=NORMAL
```

### Staging/Test
```bash
# Aplicación
PORT=8080
APP_NAME=Rentas API Cotizador RV - Staging
APP_BASE_PATH=/api/v1
LOG_LEVEL=info
CORS_ORIGIN=https://staging.interseguro.com
CORS_ADDITIONAL_HEADERS=

# SQL Server (EXP_SERV)
DB_EXP_SERV_HOST=10.0.1.5
DB_EXP_SERV_PORT=1433
DB_EXP_SERV_USER=cotizador_staging
DB_EXP_SERV_PASSWORD=<SECRET_FROM_SECRET_MANAGER>
DB_EXP_SERV_DATABASE=EXP_SERV_STAGING

# Parámetros del Cotizador
MAX_ITEM_PBS=10
KEY_ACOM=S
KEY_DCOM=S
VALIDACIONES_CRM=N
VALIDACIONES_SDA=N
VALIDA_CUOTAS=S

# Usuarios y Roles
RANGO_IPS_PARAMETROS_ESPECIALES=<SECRET_FROM_SECRET_MANAGER>
USUARIO_JEFE_OPERACIONES=<SECRET_FROM_SECRET_MANAGER>
USUARIO_GERENTE_COMERCIAL=<SECRET_FROM_SECRET_MANAGER>
USUARIOS_NOTIFICACIONES=user1,user2
TZ=America/Lima

# APIs Externas
API_RENTAS_PARAMETROS=https://is-cr-adn-api-rentas-parametros-uat-m3pd7zj7mq-uc.a.run.app/api/v1
API_WS_CWRV_WSDL=http://soap-service-uat.internal/cwrv?wsdl
API_WS_CWRV_URL=http://soap-service-uat.internal/cwrv
API_LOGS_AUDITORIA=https://is-cr-adn-api-logs-auditoria-uat-m3pd7zj7mq-uc.a.run.app/api/v1
API_ESTRUCTURA_AGENTE=https://is-cr-adn-api-estructura-comercial-uat-m3pd7zj7mq-uc.a.run.app/api/v1
API_SOLICITUD_CAMBIO=https://is-cr-adn-api-solicitud-cambio-uat-m3pd7zj7mq-uc.a.run.app/api/v1
API_WEB_SEGURIDAD_URL=https://is-cr-adn-api-seguridad-uat-m3pd7zj7mq-uc.a.run.app/api/v1
API_RVIADM_BACKEND=https://is-cr-adn-api-rviadm-backend-uat-m3pd7zj7mq-uc.a.run.app/api/v1

# Notificaciones y Correos
ENVIAR_NOTIFICACIONES=S
ENVIAR_CORREO_PRUEBA=N
CORREO_PRUEBA=
URL_API_NOTIFY_URL=https://is-cr-adn-api-notify-uat-m3pd7zj7mq-uc.a.run.app/api/v1
CORREO_REMITENTE=noreply@interseguro.com
CORREO_REMITENTE_NOMBRE=Interseguro - Rentas Vitalicias
API_NOTIFY_PRIORITY=NORMAL
```

### Production
```bash
# Aplicación
PORT=8080
APP_NAME=Rentas API Cotizador RV - Production
APP_BASE_PATH=/api/v1
LOG_LEVEL=info
CORS_ORIGIN=https://app.interseguro.com,https://portal.interseguro.com
CORS_ADDITIONAL_HEADERS=

# SQL Server (EXP_SERV)
DB_EXP_SERV_HOST=10.0.2.5
DB_EXP_SERV_PORT=1433
DB_EXP_SERV_USER=cotizador_production
DB_EXP_SERV_PASSWORD=<SECRET_FROM_SECRET_MANAGER>
DB_EXP_SERV_DATABASE=EXP_SERV_PRODUCTION

# Parámetros del Cotizador
MAX_ITEM_PBS=10
KEY_ACOM=S
KEY_DCOM=S
VALIDACIONES_CRM=S
VALIDACIONES_SDA=S
VALIDA_CUOTAS=S

# Usuarios y Roles
RANGO_IPS_PARAMETROS_ESPECIALES=<SECRET_FROM_SECRET_MANAGER>
USUARIO_JEFE_OPERACIONES=<SECRET_FROM_SECRET_MANAGER>
USUARIO_GERENTE_COMERCIAL=<SECRET_FROM_SECRET_MANAGER>
USUARIOS_NOTIFICACIONES=<SECRET_FROM_SECRET_MANAGER>
TZ=America/Lima

# APIs Externas
API_RENTAS_PARAMETROS=https://is-cr-adn-api-rentas-parametros-prod-m3pd7zj7mq-uc.a.run.app/api/v1
API_WS_CWRV_WSDL=http://soap-service-prod.internal/cwrv?wsdl
API_WS_CWRV_URL=http://soap-service-prod.internal/cwrv
API_LOGS_AUDITORIA=https://is-cr-adn-api-logs-auditoria-prod-m3pd7zj7mq-uc.a.run.app/api/v1
API_ESTRUCTURA_AGENTE=https://is-cr-adn-api-estructura-comercial-prod-m3pd7zj7mq-uc.a.run.app/api/v1
API_SOLICITUD_CAMBIO=https://is-cr-adn-api-solicitud-cambio-prod-m3pd7zj7mq-uc.a.run.app/api/v1
API_WEB_SEGURIDAD_URL=https://is-cr-adn-api-seguridad-prod-m3pd7zj7mq-uc.a.run.app/api/v1
API_RVIADM_BACKEND=https://is-cr-adn-api-rviadm-backend-prod-m3pd7zj7mq-uc.a.run.app/api/v1

# Notificaciones y Correos
ENVIAR_NOTIFICACIONES=S
ENVIAR_CORREO_PRUEBA=N
CORREO_PRUEBA=
URL_API_NOTIFY_URL=https://is-cr-adn-api-notify-prod-m3pd7zj7mq-uc.a.run.app/api/v1
CORREO_REMITENTE=noreply@interseguro.com
CORREO_REMITENTE_NOMBRE=Interseguro - Rentas Vitalicias
API_NOTIFY_PRIORITY=HIGH
```

## Configuración de Secret Manager en GCP

### Crear Secrets

```bash
# Crear secret para contraseña de SQL Server
echo -n "password_here" | gcloud secrets create DB_EXP_SERV_PASSWORD \
  --data-file=-

# Crear secrets para configuración de usuarios
echo -n "192.168.1.0/24,10.0.0.0/8" | gcloud secrets create RANGO_IPS_PARAMETROS_ESPECIALES --data-file=-
echo -n "JEFE_OPS_PROD" | gcloud secrets create USUARIO_JEFE_OPERACIONES --data-file=-
echo -n "GERENTE_COM_PROD" | gcloud secrets create USUARIO_GERENTE_COMERCIAL --data-file=-
echo -n "user1,user2,user3" | gcloud secrets create USUARIOS_NOTIFICACIONES --data-file=-
```

### Asignar a Cloud Run

```bash
gcloud run services update is-cr-rentas-api-cotizador-rv \
  --set-secrets=DB_EXP_SERV_PASSWORD=DB_EXP_SERV_PASSWORD:latest,\
RANGO_IPS_PARAMETROS_ESPECIALES=RANGO_IPS_PARAMETROS_ESPECIALES:latest,\
USUARIO_JEFE_OPERACIONES=USUARIO_JEFE_OPERACIONES:latest,\
USUARIO_GERENTE_COMERCIAL=USUARIO_GERENTE_COMERCIAL:latest,\
USUARIOS_NOTIFICACIONES=USUARIOS_NOTIFICACIONES:latest
```

## Configuración de TypeORM (MSSQL)

### SQL Server Connection
Configurada automáticamente en `src/connections/database/Exp_serv.connector.ts` usando variables de entorno para SQL Server (EXP_SERV).

```typescript
import { DataSource } from 'typeorm';
import * as mssql from 'mssql';

export const dataSource = new DataSource({
  type: 'mssql',
  host: DB_EXP_SERV_HOST,
  port: Number(DB_EXP_SERV_PORT),
  username: DB_EXP_SERV_USER,
  password: DB_EXP_SERV_PASSWORD,
  database: DB_EXP_SERV_DATABASE,
  synchronize: false,
  logging: LOG_LEVEL === 'debug',
  options: {
    encrypt: true,
    trustServerCertificate: true
  }
});
```

## Troubleshooting de Configuración

### Error: Variable de entorno no encontrada
```
Error: Configuration key "DB_EXP_SERV_HOST" does not exist
```
**Solución:** Verificar que la variable esté definida en `.env` o Secret Manager.

### Error: No se puede conectar a SQL Server
```
ConnectionError: Failed to connect to DB_EXP_SERV_HOST:1433
```
**Solución:** 
1. Verificar host, puerto, usuario y contraseña
2. Verificar conectividad de red (firewall, VPC)
3. Verificar que SQL Server esté activo y aceptando conexiones
4. Verificar configuración de `encrypt` y `trustServerCertificate`

### Error: CORS bloqueado
```
Access to fetch at '...' from origin '...' has been blocked by CORS policy
```
**Solución:** Agregar el origen a `CORS_ORIGIN`.

### Error: Fallo en integración con APIs externas
```
AxiosError: Request failed with status code 401/404/500
```
**Solución:**
1. Verificar URLs de las APIs en variables de entorno
2. Verificar conectividad de red desde Cloud Run
3. Revisar logs de las APIs externas
4. Verificar que los servicios externos estén activos

## Mejores Prácticas

### Seguridad
- ✅ Usar Secret Manager para todos los secrets (contraseñas, usuarios sensibles)
- ✅ No commitear archivos `.env` con datos sensibles
- ✅ Rotar contraseñas regularmente
- ✅ Usar usuarios de BD con permisos mínimos necesarios
- ✅ Habilitar SSL/TLS para conexiones de SQL Server (`encrypt: true`)
- ✅ **Implementar validación Joi en todas las rutas**
- ✅ **Sanitizar todos los datos de entrada con la clase Sanitizer**

### Performance
- ✅ Configurar connection pooling en mssql
- ✅ Ajustar recursos de Cloud Run según carga esperada
- ✅ Monitorear uso de memoria y CPU

### Mantenimiento
- ✅ Documentar cambios en configuración en `docs/configurations.md`
- ✅ Mantener consistencia entre entornos (dev/staging/prod)
- ✅ Versionar cambios en configuración mediante Git

## Referencias

- [Hapi.js Documentation](https://hapi.dev/)
- [TypeORM MSSQL](https://typeorm.io/#/connection-options/mssql-connection-options)
- [GCP Secret Manager](https://cloud.google.com/secret-manager/docs)
- [Cloud Run Environment Variables](https://cloud.google.com/run/docs/configuring/environment-variables)
- [Axios Documentation](https://axios-http.com/docs/intro)
- [SOAP Client](https://www.npmjs.com/package/soap)
