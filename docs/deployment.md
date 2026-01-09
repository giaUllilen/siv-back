# Guía de Despliegue - Rentas API Cotizador RV

## Descripción General

**Rentas API Cotizador RV** es un microservicio especializado en el cotizador de Rentas Vitalicias. Desarrollado con Hapi.js, TypeScript y MSSQL, el servicio gestiona solicitudes de cotización, cálculos actuariales mediante integración SOAP, y validaciones de negocio complejas.

## Arquitectura de Despliegue

### Plataforma
- **Google Cloud Platform (GCP)**
- **Cloud Run** - Servicio serverless para contenedores

### Entornos Disponibles
- **Development** (IS - Development)
- **Staging** (IS - Staging)
- **Production** (IS - Production)

## Requisitos Previos

### Herramientas Necesarias
- Node.js 18.x o superior
- npm 8.x o superior
- Docker 20.x o superior
- Google Cloud SDK (gcloud CLI)
- Acceso al repositorio Git
- Credenciales de GCP con permisos adecuados

### Accesos Requeridos
- Acceso al proyecto GCP correspondiente al entorno
- Permisos de Cloud Run Admin
- Permisos de Artifact Registry
- Acceso a Secret Manager para variables de entorno
- Acceso a la base de datos SQL Server (EXP_SERV)
- Credenciales para APIs externas (Rentas Parámetros, SOAP CWRV, Logs Auditoría, etc.)

## Proceso de Compilación

### Compilación Local

```bash
# Instalar dependencias
npm ci

# Compilar el proyecto (TypeScript -> JavaScript)
npm run build

# Los archivos compilados se generan en la carpeta dist/
```

### Variables de Compilación
No se requieren variables especiales para la compilación. Todas las configuraciones se manejan en tiempo de ejecución mediante variables de entorno.

**Importante:** El comando `npm run build` ejecuta:
1. `tsc -p .` - Compila TypeScript a JavaScript en `dist/`

## Construcción de Imagen Docker

### Dockerfile
El proyecto incluye un `Dockerfile` optimizado para producción con Alpine Linux:

```dockerfile
# Imagen base Alpine - Ligera y segura
FROM node:18.16.1-alpine3.17

ARG APP_DIR=/app

# Crear el directorio de trabajo
WORKDIR ${APP_DIR}

# Copiar todos los archivos necesarios para la compilación
COPY . ${APP_DIR}/

# Instalar todas las dependencias (incluyendo las de desarrollo para la compilación)
RUN npm install

# Compilar el código TypeScript
RUN npm run build

# Eliminar dependencias de desarrollo para reducir el tamaño de la imagen
RUN npm prune --production

# Exponer el puerto 8080
EXPOSE 8080

# Iniciar la aplicación
CMD ["npm", "start"]
```

**Ventajas de Alpine Linux:**
- ✅ Imagen más ligera (~150MB)
- ✅ Mayor seguridad (menos superficie de ataque)
- ✅ Despliegues más rápidos
- ✅ Compatible con todas las dependencias del proyecto

### Construcción de la Imagen

```bash
# Construir la imagen Docker
docker build -t rentas-api-cotizador-rv:latest .

# Etiquetar para Artifact Registry (ejemplo)
docker tag rentas-api-cotizador-rv:latest us-docker.pkg.dev/PROJECT_ID/REPO/rentas-api-cotizador-rv:TAG
```

## Despliegue en Cloud Run

### CI/CD Automático

El proyecto utiliza GitHub Actions para el despliegue automático. El workflow principal está ubicado en `.github/workflows/build-deploy.yaml`.

**Branches Monitoreados:**
- `main` - Producción
- `dev` - Desarrollo
- `integrator` - Integración
- `test/release-*` - Testing/Staging

### Flujo de Despliegue

#### 1. Validación de Pull Requests (opcional)
   - Se ejecuta un contenedor de validación con reglas de compliance
   - Valida estructura del código y documentación
   - Requiere autenticación con WIF (Workload Identity Federation)

#### 2. Compilación (Job: `build`)
   - Checkout del código
   - Setup de Node.js 20.x
   - `npm install` - Instalación de dependencias
   - `npm run build` - Compilación TypeScript
   - Upload del artefacto (`dist/`, `package.json`, `package-lock.json`)

#### 3. Construcción de Imagen Docker (Job: `artifact`)
   - Download del artefacto compilado
   - Autenticación con GCP mediante WIF
   - Docker build con buildx
   - Push a Artifact Registry: `us-docker.pkg.dev/.../rentas-api-cotizador-rv:$SHA`

#### 4. Despliegue a Cloud Run (Job: `deploy`)
   - Autenticación con GCP
   - Deploy del contenedor desde Artifact Registry
   - Servicio: `is-cr-rentas-api-cotizador-rv`
   - Configuración de variables de entorno desde Secret Manager
   - Asignación de recursos (CPU/Memoria)
   - Exposición del servicio URL

### Configuración de Cloud Run

#### Recursos Recomendados

**Development:**
```yaml
CPU: 1
Memoria: 1Gi
Min instancias: 0
Max instancias: 5
Timeout: 300s
Concurrency: 10
```

**Staging/Test:**
```yaml
CPU: 2
Memoria: 2Gi
Min instancias: 0
Max instancias: 10
Timeout: 300s
Concurrency: 20
```

**Production:**
```yaml
CPU: 2
Memoria: 4Gi
Min instancias: 1
Max instancias: 30
Timeout: 300s
Concurrency: 40
```

**Notas sobre recursos:**
- Las operaciones de cotización pueden ser intensivas en CPU
- La integración SOAP requiere memoria adecuada
- Ajustar `Concurrency` según carga de solicitudes
- `Timeout` de 300s permite cálculos actuariales complejos
- Monitorear uso de memoria y ajustar según carga real

#### Variables de Entorno

Las variables de entorno se gestionan a través de **Secret Manager**. Ver `docs/configurations.md` para el detalle completo.

Variables críticas para el despliegue:
- `PORT` - Puerto del servicio (default: 8080)
- `APP_NAME` - Nombre de la aplicación
- `APP_BASE_PATH` - Ruta base de la API (default: /api/v1)
- `LOG_LEVEL` - Nivel de logging
- Credenciales de SQL Server (EXP_SERV)
- URLs de APIs externas (42 variables - ver configurations.md)
- Parámetros de negocio (ACOM, DCOM, validaciones)
- Configuración de notificaciones y correos

#### Health Checks

El servicio expone los siguientes endpoints para health checks (mediante plugin `hapi-k8s-health`):

```
GET /liveness    - Liveness probe (verifica que la aplicación esté viva)
GET /readiness   - Readiness probe (verifica que la aplicación esté lista para recibir tráfico)
```

Configuración recomendada en Cloud Run:
```yaml
livenessProbe:
  httpGet:
    path: /liveness
    port: 8080
  initialDelaySeconds: 30
  periodSeconds: 10
  timeoutSeconds: 5
  failureThreshold: 3

readinessProbe:
  httpGet:
    path: /readiness
    port: 8080
  initialDelaySeconds: 10
  periodSeconds: 5
  timeoutSeconds: 3
```

## Despliegue Manual

### Usando gcloud CLI

```bash
# Autenticarse en GCP
gcloud auth login

# Configurar el proyecto
gcloud config set project PROJECT_ID

# Construir y subir la imagen a Artifact Registry
gcloud builds submit --tag us-docker.pkg.dev/PROJECT_ID/REPO/rentas-api-cotizador-rv:VERSION

# Desplegar en Cloud Run
gcloud run deploy is-cr-rentas-api-cotizador-rv \
  --image us-docker.pkg.dev/PROJECT_ID/REPO/rentas-api-cotizador-rv:VERSION \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated \
  --port 8080 \
  --cpu 2 \
  --memory 4Gi \
  --min-instances 1 \
  --max-instances 30 \
  --timeout 300 \
  --concurrency 40 \
  --set-env-vars APP_NAME="Rentas API Cotizador RV",APP_BASE_PATH="/api/v1",LOG_LEVEL="info" \
  --set-secrets [SECRETS_CONFIG]
```

### Usando Docker Local

```bash
# Construir imagen
docker build -t rentas-api-cotizador-rv:local .

# Ejecutar localmente con variables de entorno
docker run -p 8080:8080 \
  --env-file .env \
  rentas-api-cotizador-rv:local
```

## Verificación Post-Despliegue

### 1. Health Checks
```bash
# Verificar que el servicio responda (liveness)
curl https://SERVICE_URL/liveness

# Verificar que esté listo para recibir tráfico (readiness)
curl https://SERVICE_URL/readiness

# Respuesta esperada (liveness):
# {"statusCode":200,"status":"ok"}

# Respuesta esperada (readiness):
# {"statusCode":200,"status":"ok"}
```

### 2. Logs
```bash
# Ver logs en Cloud Run
gcloud run services logs read is-cr-rentas-api-cotizador-rv \
  --region us-central1 \
  --limit 50
```

Buscar los siguientes mensajes de inicio:
- ✅ `[Rentas API Cotizador RV] Server running on http://0.0.0.0:8080`
- ✅ `Conectando a <DB_HOST>...`
- ✅ `Conectado a la base de datos`
- ✅ Logs de inicialización de plugins (health, logger, swagger)

### 3. Conectividad de Base de Datos
Verificar en los logs que:
- Se conectó a SQL Server (EXP_SERV) correctamente
- No hay errores de autenticación o conexión
- Pool de conexiones inicializado

### 4. Endpoints
```bash
# Swagger Documentation
curl https://SERVICE_URL/documentation

# Health endpoints
curl https://SERVICE_URL/liveness
curl https://SERVICE_URL/readiness

# Ejemplo de endpoint de cotización (requiere autenticación según implementación)
curl -X POST https://SERVICE_URL/api/v1/cotizacion-extra-oficial/insertar \
  -H "Content-Type: application/json" \
  -H "x-username: test_user" \
  -H "x-rol: test_rol" \
  -d '{ /* payload validado por Joi */ }'
```

## Rollback

### Usando Cloud Run Console
1. Ir a Cloud Run Console
2. Seleccionar el servicio `is-cr-rentas-api-cotizador-rv`
3. Pestaña "Revisions"
4. Seleccionar la revisión anterior estable
5. "Manage Traffic" → Asignar 100% de tráfico
6. Confirmar

### Usando gcloud CLI
```bash
# Listar revisiones
gcloud run revisions list --service is-cr-rentas-api-cotizador-rv

# Hacer rollback a una revisión específica
gcloud run services update-traffic is-cr-rentas-api-cotizador-rv \
  --to-revisions REVISION_NAME=100
```

## Monitoreo

### Métricas Clave
- **Request Count** - Número de requests HTTP (cotizaciones procesadas)
- **Request Latency** - Latencia de respuesta (incluyendo cálculos SOAP)
- **Container Instance Count** - Número de instancias activas
- **CPU Utilization** - Uso de CPU (cálculos actuariales)
- **Memory Utilization** - Uso de memoria
- **Error Rate** - Tasa de errores
- **Request Duration** - Duración de procesamiento de cotizaciones
- **Database Connections** - Pool de conexiones a SQL Server

### Logs Importantes
- Conexión a SQL Server (EXP_SERV)
- Llamadas a servicio SOAP de cotización
- Llamadas a APIs externas (Rentas Parámetros, Estructura Agente, Logs Auditoría)
- Errores de validación (Joi + Sanitizer)
- Validaciones de negocio (ACOM, DCOM, cuotas TRA)
- Envío de notificaciones por correo
- Auditoría de eventos

### Alertas Recomendadas
1. **Alta Tasa de Errores** - Error rate > 5%
2. **Latencia Alta** - P95 > 30s (SOAP puede tomar tiempo)
3. **Instancias al Máximo** - Instances >= max_instances
4. **Uso Alto de Memoria** - Memory > 80%
5. **Health Check Failures** - Failures consecutivos
6. **Errores de Integración** - Fallos en SOAP o APIs externas
7. **Errores de BD** - Fallos de conexión a SQL Server
8. **Errores de Validación** - Tasa alta de Joi validation errors

## Troubleshooting

### Problema: Servicio no inicia
**Síntomas:** Container fails to start
**Solución:**
1. Verificar logs: `gcloud run services logs read is-cr-rentas-api-cotizador-rv`
2. Verificar variables de entorno en Secret Manager (42 variables)
3. Verificar conectividad a SQL Server
4. Verificar que se ejecutó `npm run build` correctamente
5. Verificar que el puerto 8080 esté expuesto

### Problema: Error de conexión a SQL Server
**Síntomas:** `ConnectionError: Failed to connect to DB_EXP_SERV_HOST`
**Solución:**
1. Verificar host, puerto, usuario y contraseña en variables de entorno
2. Verificar conectividad de red desde Cloud Run a SQL Server
3. Verificar firewall y reglas de VPC
4. Verificar configuración SSL/TLS (`encrypt: true`)
5. Verificar que SQL Server esté activo

### Problema: Error en servicio SOAP
**Síntomas:** `SoapError: WSDL parse error` o `Connection timeout`
**Solución:**
1. Verificar variables `API_WS_CWRV_WSDL` y `API_WS_CWRV_URL`
2. Verificar que el servicio SOAP esté activo
3. Verificar conectividad de red interna
4. Revisar logs del servicio SOAP
5. Verificar timeout de axios/soap client

### Problema: Validaciones Joi fallando
**Síntomas:** Error 400 con mensaje "Datos de entrada inválidos"
**Solución:**
1. Revisar logs de validación (incluyen detalles de campos inválidos)
2. Verificar que el payload cumpla con el schema Joi
3. Consultar Swagger para ver estructura esperada
4. Verificar formato de fechas (DD/MM/YYYY)
5. Verificar longitud de campos (CUSPP 12 dígitos, etc.)

### Problema: Errores en APIs externas
**Síntomas:** `AxiosError: Request failed with status code 401/404/500`
**Solución:**
1. Verificar URLs en variables de entorno (API_RENTAS_PARAMETROS, etc.)
2. Verificar conectividad de red desde Cloud Run
3. Revisar logs de las APIs externas
4. Verificar que los servicios externos estén activos
5. Verificar timeouts de axios

### Problema: Alto uso de memoria
**Síntomas:** Container OOM (Out of Memory) o lentitud
**Solución:**
1. Monitorear uso de memoria en Cloud Run Console
2. Aumentar límite de memoria (de 2Gi a 4Gi o más)
3. Reducir concurrency para limitar solicitudes simultáneas
4. Optimizar consultas a base de datos
5. Revisar si hay memory leaks en integraciones

## Seguridad

### Mejores Prácticas Implementadas ✅
- ✅ Usar Secret Manager para credenciales (BD, APIs, usuarios)
- ✅ No hardcodear secrets en el código
- ✅ Mantener imágenes Docker actualizadas (Alpine Linux)
- ✅ Limitar acceso a Cloud Run con IAM
- ✅ Usar conexiones SSL/TLS para SQL Server (`encrypt: true`)
- ✅ **Validación Joi en todas las rutas (100% coverage)**
- ✅ **Sanitización con clase Sanitizer (prevención de inyecciones)**
- ✅ **Tipado seguro con DTOs (sin `as any`)**
- ✅ **failAction personalizado con logging**
- ✅ **Middleware de errores con transactionId**
- ✅ Revisar logs regularmente (usar LOG_LEVEL apropiado)
- ✅ Rotar credenciales regularmente

### Variables Sensibles
Nunca incluir en el código fuente:
- Contraseña de SQL Server (`DB_EXP_SERV_PASSWORD`)
- Códigos de usuarios especiales
- Rangos de IPs internas
- URLs internas de producción
- Datos sensibles de clientes en logs

### Cumplimiento de Seguridad
- **Validación:** 100% de rutas validadas con Joi
- **Sanitización:** 100% de datos sanitizados
- **Consultas BD:** 100% parametrizadas (Stored Procedures)
- **Cumplimiento general:** 99% según `security.mdc`

## Mantenimiento

### Actualizaciones Regulares
- Actualizar dependencias: `npm update` (revisar breaking changes)
- Actualizar imagen base de Docker (Alpine)
- Revisar y aplicar parches de seguridad
- Limpiar revisiones antiguas de Cloud Run
- Actualizar documentación de configuración
- Ejecutar pruebas unitarias: `npm test`

### Limpieza de Recursos
```bash
# Eliminar revisiones antiguas (mantener últimas 5)
gcloud run revisions list --service is-cr-rentas-api-cotizador-rv --format="value(name)" \
  | tail -n +6 \
  | xargs -I {} gcloud run revisions delete {} --quiet
```

### Monitoreo Regular
- Revisar métricas de uso de memoria
- Monitorear latencia de endpoints de cotización
- Verificar conectividad con APIs externas y SOAP
- Revisar logs de errores semanalmente
- Validar integridad de cálculos actuariales

## Contacto y Soporte

### Equipo Responsable
- **Equipo:** SquadRentas
- **Línea de Negocio:** Rentas Vitalicias
- **DevOps:** Equipo de DevOps Interseguro
- **Arquitectura:** Equipo de Arquitectura TI

### Recursos Adicionales
- Repositorio Git: [GitHub Repository]
- Documentación técnica: `docs/`
- Swagger UI: `https://SERVICE_URL/documentation`
- Confluence: [Documentación de arquitectura]

## Changelog

### Versión 0.0.1 (Inicial)
- ✅ Implementación inicial del servicio cotizador
- ✅ Integración con SQL Server (EXP_SERV) mediante TypeORM
- ✅ Integración SOAP para cálculos actuariales (CWRV)
- ✅ Integración con APIs externas (8 servicios)
- ✅ Health checks para Cloud Run (`/liveness`, `/readiness`)
- ✅ Documentación Swagger automática
- ✅ Logging estructurado con Pino
- ✅ Pipeline CI/CD con GitHub Actions
- ✅ Despliegue en Cloud Run
- ✅ **Seguridad: Validación Joi 100%**
- ✅ **Seguridad: Sanitización con clase Sanitizer**
- ✅ **Seguridad: Middleware de errores con transactionId**
- ✅ **Seguridad: Cumplimiento 99% de security.mdc**
- ✅ Documentación completa (README, configurations, deployment)
