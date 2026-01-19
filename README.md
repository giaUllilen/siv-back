# SIV Backend

Servicios RESTful a utilizar por toda la suite de ventas de Interseguro.

## 🔐 Configuración Segura - IMPORTANTE

**⚠️ ANTES DE EMPEZAR:** Este proyecto ha sido configurado para NO exponer credenciales sensibles. 

**LEE LA DOCUMENTACIÓN COMPLETA:** [CONFIGURACION_SEGURIDAD.md](CONFIGURACION_SEGURIDAD.md)

### Inicio Rápido Seguro

```bash
# 1. Clonar el repositorio
git clone <repository-url>
cd siv-back

# 2. Copiar template de configuración
cd siv-admin/src/main/resources/
cp application.properties.template application-local.properties

# 3. Configurar variables de entorno (ver variables-entorno.example)
# Editar application-local.properties con tus credenciales locales

# 4. Configurar New Relic
cd ../../../../newrelic/
cp newrelic.yml.template newrelic.yml
# Configurar NEW_RELIC_LICENSE_KEY

# 5. Compilar y ejecutar
cd ..
mvn clean install
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

## 📋 Estructura del Proyecto

```
siv-back/
├── siv-admin/              # Aplicación principal Spring Boot
│   └── src/
│       ├── main/
│       │   ├── java/       # Código fuente Java
│       │   └── resources/  # Configuraciones (NO SUBIR CON CREDENCIALES)
│       └── test/           # Tests unitarios
├── siv-common/             # Módulos comunes
│   ├── siv-common-database/    # Capa de persistencia
│   ├── siv-common-dto/         # DTOs y entidades
│   ├── siv-common-persistence/ # Repositorios
│   └── siv-common-util/        # Utilidades compartidas
├── newrelic/               # Configuración New Relic (NO SUBIR CON LICENSE KEY)
├── docs/                   # Documentación del proyecto
└── README.md
```

## 🛠️ Tecnologías

- **Java 11+**
- **Spring Boot** - Framework principal
- **Maven** - Gestión de dependencias
- **MySQL** - Base de datos principal
- **PostgreSQL** - Bases de datos ACSELE, SITC
- **SQL Server** - Base de datos CRM
- **New Relic** - Monitoreo y observabilidad

## 🚀 Desarrollo Local

### Prerrequisitos

- Java JDK 11 o superior
- Maven 3.6+
- MySQL 5.7+
- PostgreSQL 11+
- SQL Server (opcional, según necesidades)

### Configuración de Base de Datos

Ver [CONFIGURACION_SEGURIDAD.md](CONFIGURACION_SEGURIDAD.md) para configurar las conexiones a base de datos de forma segura.

### Ejecutar Tests

```bash
mvn test
```

### Compilar Proyecto

```bash
mvn clean package
```

### Ejecutar Aplicación

```bash
# Con Maven
mvn spring-boot:run -Dspring-boot.run.profiles=local

# O con Java
java -jar siv-admin/target/siv-admin.jar --spring.profiles.active=local
```

## 📁 Archivos Importantes

### ✅ Archivos seguros para el repositorio:
- `application.properties.template` - Template de configuración
- `newrelic.yml.template` - Template de New Relic
- `variables-entorno.example` - Ejemplo de variables de entorno
- `.gitignore` - Archivos excluidos del repositorio

### ❌ Archivos CON credenciales (NO SUBIR):
- `application.properties`
- `application-*.properties` (local, develop, dsd, master, uat)
- `newrelic.yml`
- `*.jks`, `*.p12` (keystores)

## 🔒 Seguridad

### Antes de hacer commit

1. **Verificar** que no hay credenciales en archivos que vas a subir
2. **Revisar** el `.gitignore` para asegurar que archivos sensibles están excluidos
3. **Usar** el script de respaldo:

```bash
chmod +x backup-credenciales.sh
./backup-credenciales.sh
```

### Si expusiste credenciales accidentalmente

Ver sección "Si Expusiste Credenciales Accidentalmente" en [CONFIGURACION_SEGURIDAD.md](CONFIGURACION_SEGURIDAD.md)

## 📚 Documentación Adicional

- [Configuración de Seguridad](CONFIGURACION_SEGURIDAD.md) - **LEER PRIMERO**
- [Commits según lineamiento](docs/commits-segun-lineamiento.md)
- [Pull Requests según lineamiento](docs/pr-segun-lineamiento.md)
- [Configuraciones](docs/configurations.md)
- [Deployment](docs/deployment.md)
- [OpenAPI Specification](docs/openapi.json)

## 🤝 Contribuir

1. Crear una rama desde `develop`
2. Hacer cambios
3. **Asegurar que NO hay credenciales** en los commits
4. Hacer commit siguiendo [lineamientos](docs/commits-segun-lineamiento.md)
5. Push y crear Pull Request
6. Revisar y aprobar según [lineamientos de PR](docs/pr-segun-lineamiento.md)

## 📞 Soporte

Para preguntas sobre:
- **Configuración**: Ver [CONFIGURACION_SEGURIDAD.md](CONFIGURACION_SEGURIDAD.md)
- **Seguridad**: Contactar al equipo de Seguridad
- **Desarrollo**: Contactar al equipo de Desarrollo

## ⚖️ Licencia

Propiedad de Interseguro. Todos los derechos reservados.
