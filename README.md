# JWT Auth Backend

Backend de autenticación y autorización basado en **JSON Web Tokens (JWT)** construido con Spring Boot 3, Spring Security y PostgreSQL.

---

## Tabla de Contenidos

- [Descripción General](#descripción-general)
- [Características Principales](#características-principales)
- [Tecnologías Utilizadas](#tecnologías-utilizadas)
- [Arquitectura del Proyecto](#arquitectura-del-proyecto)
- [Requisitos Previos](#requisitos-previos)
- [Instalación](#instalación)
- [Configuración](#configuración)
- [Uso](#uso)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Scripts Disponibles](#scripts-disponibles)
- [Pruebas](#pruebas)
- [Despliegue](#despliegue)
- [Buenas Prácticas y Consideraciones Técnicas](#buenas-prácticas-y-consideraciones-técnicas)
- [Roadmap](#roadmap)
- [Contribución](#contribución)
- [Licencia](#licencia)
- [Autor](#autor)

---

## Descripción General

### Qué problema resuelve

Provee una API REST segura que implementa un sistema completo de autenticación y autorización mediante JWT. Permite registrar usuarios, autenticarlos y proteger endpoints con tokens Bearer, eliminando la necesidad de sesiones en el servidor (stateless).

### Para quién está dirigido

- Desarrolladores backend que necesitan un sistema de autenticación listo para integrar en sus aplicaciones.
- Equipos que buscan una referencia de implementación de Spring Security con JWT.
- Proyectos que requieren una arquitectura stateless con control de acceso basado en roles.

### Principales beneficios

- Autenticación stateless mediante JWT, ideal para microservicios y SPAs.
- Control de acceso basado en roles (`USER`, `ADMIN`).
- Contraseñas almacenadas con hash BCrypt.
- Documentación interactiva de la API con Swagger UI / OpenAPI 3.
- Listo para contenedores con Dockerfile incluido.

---

## Características Principales

- **Registro de usuarios** con asignación automática de rol `USER`.
- **Login** con generación de token JWT firmado con HMAC-SHA256.
- **Filtro JWT** que intercepta cada request, valida el token y establece el contexto de seguridad.
- **Endpoints protegidos** que requieren autenticación Bearer.
- **Roles** (`ADMIN`, `USER`) mapeados como authorities de Spring Security.
- **Swagger UI** integrado para explorar y probar la API sin herramientas externas.
- **Persistencia** en PostgreSQL con JPA/Hibernate y esquema auto-generado.

---

## Tecnologías Utilizadas

![Java](https://img.shields.io/badge/Java_21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_3.5.3-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)
![Lombok](https://img.shields.io/badge/Lombok-DC382D?style=for-the-badge&logo=lombok&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger_UI-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)

---

## Arquitectura del Proyecto

La aplicación sigue una arquitectura por capas con separación clara de responsabilidades:

```
Request → JwtAuthenticationFilter → SecurityFilterChain → Controller → Service → Repository → PostgreSQL
```

**Flujo de autenticación:**

1. El cliente envía `POST /auth/login` con credenciales.
2. `AuthService` autentica vía `AuthenticationManager` (BCrypt).
3. Si es válido, `JwtService` genera un token JWT firmado con HS256.
4. El cliente incluye el token en el header `Authorization: Bearer <token>` en requests posteriores.
5. `JwtAuthenticationFilter` intercepta cada request, extrae el token, lo valida y establece el `SecurityContext`.

---

## Requisitos Previos

- **Java 21** (JDK) — [Descargar](https://adoptium.net/)
- **Maven 3.8+** (o usar el wrapper `mvnw` incluido)
- **PostgreSQL 14+** con una base de datos creada
- **Docker** (opcional, para despliegue con contenedores)

---

## Instalación

### 1. Clonar el repositorio

```bash
git clone https://github.com/andresavilesdev/jwt-auth-backend.git
cd jwt-auth-backend
```

### 2. Crear la base de datos en PostgreSQL

```sql
CREATE DATABASE jwt_auth_db;
```

### 3. Configurar las variables de entorno

Crear un archivo `.env` en la raíz del proyecto:

```env
DB_USERNAME=tu_usuario_postgres
DB_PASSWORD=tu_contraseña_postgres
DB_URL=jdbc:postgresql://localhost:5432/jwt_auth_db
JWT_SECRET=TuClaveSecretaEnBase64DeMinimoUnTextoLargoEnBase64==
```

> **Nota:** `JWT_SECRET` debe ser una cadena codificada en Base64 de al menos 256 bits para el algoritmo HS256.

### 4. Compilar y ejecutar

```bash
# Con el Maven Wrapper incluido
./mvnw spring-boot:run

# O en Windows
mvnw.cmd spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`.

---

## Configuración

### Variables de entorno

| Variable | Descripción | Ejemplo |
|---|---|---|
| `DB_USERNAME` | Usuario de la base de datos PostgreSQL | `postgres` |
| `DB_PASSWORD` | Contraseña de la base de datos | `mi_password` |
| `DB_URL` | URL JDBC de conexión a PostgreSQL | `jdbc:postgresql://localhost:5432/jwt_auth_db` |
| `JWT_SECRET` | Clave secreta en Base64 para firmar tokens | `U2VjdXJlYm...` |

### Archivos de configuración

- **`src/main/resources/application.properties`** — Configuración de Spring Boot, datasource, JPA y JWT.
- **`.env`** — Variables de entorno locales (no versionado).

---

## Uso

### Swagger UI

Con la aplicación corriendo, accede a la documentación interactiva:

```
http://localhost:8080/swagger-ui/index.html
```

### Endpoints de la API

#### Registro de usuario

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "johndoe",
    "password": "secret123",
    "firstname": "John",
    "lastname": "Doe",
    "country": "US"
  }'
```

**Respuesta:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

#### Login

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "johndoe",
    "password": "secret123"
  }'
```

**Respuesta:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

#### Acceder a un endpoint protegido

```bash
curl http://localhost:8080/api/v1/demo \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

**Respuesta:**

```
Welcome to SECURE - API
```

#### Acceso sin token (401 Unauthorized)

```bash
curl http://localhost:8080/api/v1/demo
# Respuesta: 401 Unauthorized
```

---

## Estructura del Proyecto

```
src/main/java/com/andresavilesdev/demo_jwt/
├── DemoJwtApplication.java              # Clase principal de Spring Boot
├── config/
│   └── AppConfig.java                   # Beans de seguridad (AuthenticationManager, PasswordEncoder)
├── demo/
│   └── controller/
│       └── DemoController.java          # Endpoint protegido de ejemplo (/api/v1/demo)
├── openApi/
│   └── config/
│       └── OpenApiConfig.java           # Configuración de Swagger/OpenAPI con Bearer Auth
└── security/
    ├── auth/
    │   ├── controller/
    │   │   └── AuthController.java      # Endpoints públicos /auth/login y /auth/register
    │   ├── dto/
    │   │   ├── requests/
    │   │   │   ├── LoginRequest.java     # DTO de login (username, password)
    │   │   │   └── RegisterRequest.java  # DTO de registro (username, password, firstname, lastname, country)
    │   │   └── responses/
    │   │       └── AuthResponse.java     # DTO de respuesta (token)
    │   └── service/
    │       └── AuthService.java          # Lógica de autenticación y registro
    ├── config/
    │   └── SecurityConfig.java          # Cadena de filtros de seguridad
    ├── jwt/
    │   ├── filter/
    │   │   └── JwtAuthenticationFilter.java  # Filtro que valida JWT en cada request
    │   └── service/
    │       └── JwtService.java          # Generación, firma y validación de tokens
    └── user/
        ├── model/
        │   └── UserEntity.java          # Entidad JPA que implementa UserDetails
        ├── repository/
        │   └── IUserRepository.java     # Repositorio JPA
        ├── role/
        │   └── Role.java                # Enum de roles (ADMIN, USER)
        └── service/
            ├── IUserService.java        # Interfaz de servicio de usuario
            └── UserServiceImpl.java     # Implementación del servicio de usuario
```

---

## Scripts Disponibles

| Comando | Descripción |
|---|---|
| `./mvnw spring-boot:run` | Ejecutar la aplicación en modo desarrollo |
| `./mvnw clean package` | Compilar y empaquetar como JAR |
| `./mvnw clean package -DskipTests` | Empaquetar sin ejecutar tests |
| `./mvnw test` | Ejecutar todas las pruebas |
| `docker build -t jwt-auth-backend .` | Construir imagen Docker |
| `docker run -p 8080:8080 jwt-auth-backend` | Ejecutar contenedor Docker |

---

## Pruebas

### Ejecutar pruebas

```bash
./mvnw test
```

### Stack de pruebas

- **JUnit 5** — Framework de pruebas (incluido en `spring-boot-starter-test`).
- **Mockito** — Mocking de dependencias.
- **Spring Security Test** — Utilidades para probar configuraciones de seguridad.

---

## Despliegue

### Docker

El proyecto incluye un `Dockerfile` multi-stage listo para producción:

```bash
# Construir la imagen
docker build -t jwt-auth-backend .

# Ejecutar el contenedor
docker run -p 8080:8080 --env-file .env jwt-auth-backend
```

> **Importante:** El archivo `.env` debe existir en el directorio desde donde se ejecuta `docker build`, ya que se copia al contenedor.

### JAR ejecutable

```bash
./mvnw clean package -DskipTests
java -jar target/demo-jwt-0.0.1-SNAPSHOT.jar
```

---

## Buenas Prácticas y Consideraciones Técnicas

- **Contraseñas:** Se almacenan hasheadas con BCrypt. Nunca se persisten en texto plano.
- **Tokens JWT:** Se firman con HMAC-SHA256. La clave secreta debe mantenerse segura y nunca versionarse.
- **Sesiones:** La aplicación es completamente stateless (`SessionCreationPolicy.STATELESS`).
- **CSRF:** Deshabilitado intencionalmente, dado que la autenticación se realiza mediante tokens Bearer (no cookies).
- **Variables sensibles:** Se cargan desde variables de entorno o archivo `.env`. El archivo `.env` debe incluirse en `.gitignore`.
- **Expiración de tokens:** Los tokens expiran tras 24 minutos (`1000 * 60 * 24` ms). Ajustar según los requisitos del proyecto.

---

## Roadmap

- [ ] Implementar refresh tokens para renovar sesiones sin re-autenticación.
- [ ] Agregar validación de campos en DTOs con Bean Validation (`@NotBlank`, `@Size`, etc.).
- [ ] Implementar manejo global de excepciones con `@ControllerAdvice`.
- [ ] Agregar endpoints CRUD de usuarios para el rol `ADMIN`.
- [ ] Implementar rate limiting para los endpoints de autenticación.
- [ ] Agregar pruebas de integración con `@SpringBootTest` y Testcontainers.
- [ ] Migrar la gestión de esquema a Flyway o Liquibase.
- [ ] Agregar logging estructurado con SLF4J.
- [ ] Configurar CORS para permitir solicitudes desde frontends específicos.

---

## Contribución

Las contribuciones son bienvenidas. Para colaborar:

1. Haz un fork del repositorio.
2. Crea una rama para tu feature o fix:
   ```bash
   git checkout -b feature/mi-nueva-funcionalidad
   ```
3. Realiza tus cambios y commitea con mensajes descriptivos:
   ```bash
   git commit -m "feat: agregar refresh token endpoint"
   ```
4. Asegúrate de que todos los tests pasen:
   ```bash
   ./mvnw test
   ```
5. Envía un Pull Request describiendo los cambios realizados.

### Estándares de código

- Seguir las convenciones de nomenclatura de Java (camelCase para variables/métodos, PascalCase para clases).
- Usar inyección de dependencias por constructor (no por campo).
- Mantener la separación de capas: Controller → Service → Repository.
- No versionar credenciales ni claves secretas.

---

## Licencia

Este proyecto se encuentra bajo una licencia por definir. Consultar con el autor para más información.

---

## Autor

**Andrés Avilés** — [@andresavilesdev](https://github.com/andresavilesdev)
