# 🏨 Sistema de Reservas de Hotel — REST API

API REST para la gestión integral de un sistema hotelero, desarrollada con **Java 21**, **Spring Boot 4.1.1**, **Spring Data JPA/Hibernate** y **Microsoft SQL Server**.

Aplicando arquitectura en capas, DTOs, transacciones, validaciones, consultas dinámicas, paginación, manejo centralizado de excepciones y mecanismos de control de concurrencia.

El principal desafío de negocio es controlar correctamente la **disponibilidad de habitaciones y evitar el overbooking**, incluso ante operaciones concurrentes. Para ello, el sistema incorpora reservas temporales, expiración automática, **Pessimistic Locking**, **Optimistic Locking** y validaciones orientadas a prevenir **Race Conditions**.

---

## 📋 Índice

- [Características principales](#-características-principales)
- [Tecnologías](#-tecnologías)
- [Arquitectura](#-arquitectura)
- [Estructura del proyecto](#-estructura-del-proyecto)
- [Modelo de dominio](#-modelo-de-dominio)
- [Reglas de negocio](#-reglas-de-negocio)
- [Disponibilidad y solapamiento](#-disponibilidad-y-solapamiento)
- [Reservas temporales](#-reservas-temporales)
- [Concurrencia](#-concurrencia)
- [Estados](#-estados)
- [API REST](#-api-rest)
- [Reportes](#-reportes)
- [Instalación](#-instalación)
- [Configuración](#-configuración)
- [Ejecución](#-ejecución)
- [Pruebas con Postman](#-pruebas-con-postman)
- [Consideraciones de seguridad](#-consideraciones-de-seguridad)
- [Estado del proyecto](#-estado-del-proyecto)
- [Objetivos técnicos](#-objetivos-técnicos)

---

## 🚀 Características principales

El sistema contempla:

- Gestión de hoteles.
- Gestión de tipos de habitación.
- Gestión de habitaciones.
- Gestión de clientes.
- Gestión de reservas.
- Asociación de reservas con una o varias habitaciones.
- Gestión de pagos.
- Consulta de disponibilidad por rango de fechas.
- Detección de conflictos de reserva.
- Reservas temporales en estado `PENDIENTE`.
- Expiración automática de reservas temporales.
- Cancelación manual de reservas.
- Confirmación de reservas mediante pago.
- Modificación de fechas y habitaciones de una reserva.
- Eliminación lógica donde corresponde.
- Paginación.
- Consultas dinámicas mediante **Spring Data JPA Specifications**.
- Consultas por rangos de fechas.
- Reportes operativos.
- Transacciones con `@Transactional`.
- Pessimistic Locking con `PESSIMISTIC_WRITE`.
- Optimistic Locking mediante `@Version`.
- Protección frente a condiciones de carrera.
- Manejo centralizado de excepciones.
- Validaciones de entrada y reglas de negocio.

---

## 🧰 Tecnologías

| Tecnología | Uso |
|---|---|
| **Java 21** | Lenguaje de programación |
| **Spring Boot 4.1.1** | Framework principal |
| **Spring Data JPA** | Persistencia y repositorios |
| **Hibernate** | ORM |
| **Microsoft SQL Server** | Sistema gestor de base de datos |
| **Maven** | Gestión de dependencias y construcción |
| **Lombok** | Reducción de código repetitivo |
| **Spring Data JPA Specifications** | Consultas dinámicas |
| **Spring Scheduling** | Expiración automática de reservas |
| **Postman** | Pruebas de la API |

---

# 🏗️ Arquitectura

El proyecto utiliza una **Arquitectura en Capas (Layered Architecture)**.

```text
┌─────────────────────────────┐
│        Controller           │
│      REST API / HTTP        │
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│          Service            │
│   Reglas de negocio         │
│   Transacciones             │
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│        Repository           │
│      Spring Data JPA        │
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│        SQL Server           │
└─────────────────────────────┘
```

### Responsabilidades

**Controller**
- Expone los endpoints REST.
- Recibe requests.
- Valida la entrada cuando corresponde.
- Devuelve responses HTTP.

**Service**
- Contiene las reglas de negocio.
- Coordina operaciones entre repositorios.
- Define límites transaccionales.
- Implementa las operaciones críticas de concurrencia.

**Repository**
- Acceso a datos.
- Consultas derivadas.
- JPQL.
- Specifications.
- Consultas con locking.

**Entity**
- Representa el modelo persistente del dominio.

**DTO**
- Evita exponer directamente las entidades como contrato de la API.
- Define estructuras específicas para requests, responses y reportes.

**Exception**
- Centraliza excepciones y respuestas de error.

**Util**
- Contiene mappers y utilitarios reutilizables.

---

# 📁 Estructura del proyecto

```text
reservas-hotel-api/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/hotel/reservas/
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │       ├── dto/
│   │   │       │   ├── request/
│   │   │       │   ├── response/
│   │   │       │   └── report/
│   │   │       ├── entity/
│   │   │       ├── exception/
│   │   │       ├── repository/
│   │   │       │   └── specification/
│   │   │       ├── service/
│   │   │       │   └── impl/
│   │   │       └── util/
│   │   │           └── mapper/
│   │   └── resources/
│   │       └── application.yml
│   └── test/
│
├── pom.xml
├── README.md
└── LICENSE
```

---

# 🗃️ Modelo de dominio

Las principales entidades del sistema son:

```text
HOTEL
  │
  │ 1:N
  ▼
HABITACION
  │
  │ N:1
  ▼
TIPO_HABITACION


CLIENTE
  │
  │ 1:N
  ▼
RESERVA
  │
  ├───────────────┐
  │ 1:N           │ 1:N
  ▼               ▼
RESERVA_HABITACION PAGO
  │
  │ N:1
  ▼
HABITACION
```

### Relaciones principales

- Un `Hotel` puede tener muchas `Habitaciones`.
- Una `Habitacion` pertenece a un `Hotel`.
- Un `TipoHabitacion` puede estar asociado a muchas `Habitaciones`.
- Una `Habitacion` pertenece a un `TipoHabitacion`.
- Un `Cliente` puede tener muchas `Reservas`.
- Una `Reserva` pertenece a un `Cliente`.
- Una `Reserva` puede contener una o varias `ReservaHabitacion`.
- Una `ReservaHabitacion` referencia una `Habitacion`.
- Una `Reserva` puede tener uno o varios `Pagos`.
- Un `Pago` pertenece a una `Reserva`.

---

# 📐 Reglas de negocio

Las reglas principales se basan en los requisitos funcionales del proyecto.

### RN-01 — Fechas válidas

La fecha de entrada debe ser estrictamente anterior a la fecha de salida.

```text
fechaEntrada < fechaSalida
```

No se permiten:

```text
fechaEntrada >= fechaSalida
```

### RN-02 — Habitación disponible

Una habitación no puede reservarse si no está operativa o si existe un conflicto de disponibilidad.

### RN-03 — Conflicto de reservas

No se permite una reserva cuando existe otra reserva incompatible para la misma habitación y período.

### RN-04 — Estado operativo de habitación

Las habitaciones en:

```text
MANTENIMIENTO
INACTIVA
```

no pueden ser reservadas.

### RN-05 — Cliente activo

No se pueden crear nuevas reservas para clientes inactivos.

### RN-06 — Cancelación

La cancelación actualiza el estado de la reserva y no elimina físicamente el registro.

### RN-07 — Modificación

Al modificar fechas o habitaciones se vuelve a comprobar la disponibilidad.

### RN-08 — Cálculo del total

El total se calcula como:

```text
precioPorNoche × númeroDeNoches
```

para cada habitación y posteriormente se suman los subtotales.

### RN-09 — Pagos

El monto de un pago debe ser mayor que cero y la reserva debe encontrarse en un estado que permita el pago.

### RN-10 — Consistencia

Las operaciones críticas que modifican varias entidades deben ejecutarse dentro de una transacción.

---

# 📅 Disponibilidad y solapamiento

El sistema utiliza la condición matemática estándar para detectar intersección de períodos:

```text
nuevaEntrada < existenteSalida
AND
nuevaSalida > existenteEntrada
```

Por ejemplo:

```text
Reserva existente
01/09 ───────── 05/09

Nueva reserva
03/09 ───────── 07/09

Resultado: CONFLICTO
```

Mientras que:

```text
Reserva existente
01/09 ───────── 05/09

Nueva reserva
05/09 ───────── 08/09

Resultado: SIN CONFLICTO
```

La fecha de salida se considera un **límite no ocupado**, por lo que una nueva reserva puede comenzar el mismo día del check-out de la reserva anterior.

Esta regla debe mantenerse consistente tanto en:

- consulta de disponibilidad;
- creación de reservas;
- modificación de reservas.

---

# ⏱️ Reservas temporales

Las reservas nuevas se crean inicialmente en estado:

```text
PENDIENTE
```

y reciben una `fechaExpiracion`.

El flujo es:

```text
                  ┌───────────────┐
                  │   PENDIENTE   │
                  └───────┬───────┘
                          │
             ┌────────────┴────────────┐
             │                         │
             ▼                         ▼
        Registrar pago             Expiración
             │                         │
             ▼                         ▼
       ┌────────────┐           ┌────────────┐
       │ CONFIRMADA │           │ CANCELADA  │
       └──────┬─────┘           └────────────┘
              │
              ▼
       ┌────────────┐
       │ FINALIZADA │
       └────────────┘
```

## Expiración automática

El sistema utiliza `@Scheduled` para revisar periódicamente reservas pendientes cuya:

```text
fechaExpiracion <= ahora
```

Cuando una reserva expirada es procesada:

```text
PENDIENTE
    ↓
CANCELADA
```

La tarea programada debe ejecutarse dentro de un contexto transaccional para que las operaciones que utilizan `PESSIMISTIC_WRITE` dispongan de una transacción activa.

> **Nota de desarrollo:** durante las pruebas se puede reducir temporalmente la duración de la reserva y/o la frecuencia del scheduler. La configuración definitiva debe mantenerse acorde con las reglas de negocio del proyecto.

---

# 🔐 Concurrencia

La concurrencia es uno de los componentes técnicos principales del proyecto.

El sistema contempla:

- Transacciones.
- Race Conditions.
- Optimistic Locking.
- Pessimistic Locking.
- `@Version`.
- Consistencia de datos.

## Optimistic Locking

La entidad `Habitacion` dispone de un campo de versión que puede utilizarse mediante:

```java
@Version
private Long version;
```

Su finalidad es detectar que otra transacción modificó la entidad antes de intentar persistir una versión obsoleta.

Conceptualmente:

```text
Transacción A → version = 1
Transacción B → version = 1

A actualiza → version = 2

B intenta actualizar version = 1
        ↓
CONFLICTO DE CONCURRENCIA
```

## Pessimistic Locking

En operaciones críticas se utiliza:

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
```

Este mecanismo se utiliza para proteger recursos durante operaciones transaccionales sensibles.

En particular, se aplica sobre operaciones relacionadas con:

- Habitaciones durante la creación de reservas.
- Reservas durante modificaciones críticas.
- Registro de pagos.
- Cancelación.
- Expiración automática.

## Race Condition: pago vs. expiración

Un escenario crítico es:

```text
                 RESERVA PENDIENTE
                        │
              ┌─────────┴─────────┐
              │                   │
              ▼                   ▼
          Usuario               Scheduler
          registra pago         detecta expiración
              │                   │
              ▼                   ▼
          CONFIRMAR              CANCELAR
              │                   │
              └─────────┬─────────┘
                        │
                        ▼
                 PESSIMISTIC_WRITE
                        │
                        ▼
                 acceso exclusivo
                        │
                        ▼
                validar estado actual
```

Después de adquirir el lock, la operación vuelve a validar el estado actual de la reserva antes de realizar la transición.

Esto evita que una reserva sea confirmada y cancelada simultáneamente de forma inconsistente.

---

# 🔄 Estados

## Estado de habitación

```text
DISPONIBLE
MANTENIMIENTO
INACTIVA
```

Una habitación en `MANTENIMIENTO` o `INACTIVA` no puede ser reservada.

## Estado de reserva

```text
PENDIENTE
CONFIRMADA
CANCELADA
FINALIZADA
```

## Estado de pago

Los pagos deben distinguir al menos entre un pago válido/registrado y un pago anulado o rechazado, según las reglas implementadas por el sistema.

---

# 🌐 API REST

### Base URL

```text
http://localhost:8080/api/v1
```

## Hoteles

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/hoteles` | Crear hotel |
| `GET` | `/hoteles` | Listar hoteles paginados |
| `GET` | `/hoteles/activos` | Listar hoteles activos |
| `PUT` | `/hoteles/{id}` | Actualizar hotel |
| `DELETE` | `/hoteles/{id}` | Eliminación lógica |

## Tipos de habitación

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/tipos-habitacion` | Crear tipo de habitación |
| `GET` | `/tipos-habitacion` | Listar tipos paginados |
| `PUT` | `/tipos-habitacion/{id}` | Actualizar tipo |
| `DELETE` | `/tipos-habitacion/{id}` | Eliminación lógica |

## Habitaciones

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/habitaciones` | Crear habitación |
| `GET` | `/habitaciones/busqueda` | Búsqueda dinámica |
| `PATCH` | `/habitaciones/{id}/estado` | Cambiar estado operativo |

## Disponibilidad

| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/habitaciones/disponibles` | Consultar habitaciones disponibles por período |

## Clientes

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/clientes` | Registrar cliente |
| `GET` | `/clientes` | Listar clientes |
| `PUT` | `/clientes/{id}` | Actualizar cliente |
| `DELETE` | `/clientes/{id}` | Eliminación lógica |

## Reservas

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/reservas` | Crear reserva temporal |
| `GET` | `/reservas/busqueda` | Filtrar reservas |
| `PUT` | `/reservas/{id}` | Modificar reserva |
| `PATCH` | `/reservas/{id}/cancelar` | Cancelar reserva |

## Pagos

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/pagos` | Registrar pago y confirmar reserva |
| `GET` | `/pagos/reserva/{idReserva}` | Consultar pagos de una reserva |
| `PATCH` | `/pagos/{id}/anular` | Anular pago |

## Reportes

| Método | Endpoint | Código | Descripción |
|---|---|---|---|
| `GET` | `/reportes/reservas-periodo` | REP-01 | Cantidad de reservas por período |
| `GET` | `/reportes/habitaciones-mayor-ocupacion` | REP-02 | Habitaciones con mayor ocupación |
| `GET` | `/reportes/ingresos-periodo` | REP-03 | Ingresos por período |
| `GET` | `/reportes/clientes-mayor-gasto` | REP-04 | Clientes con mayor gasto |
| `GET` | `/reportes/ocupacion-tipo-habitacion` | REP-05 | Ocupación por tipo de habitación |

> Los parámetros concretos, cuerpos JSON y códigos HTTP dependen de los DTOs y contratos definitivos implementados por la API.

---

# 📊 Reportes

El proyecto contempla los cinco reportes establecidos en los requisitos:

### REP-01 — Reservas por período

Obtiene la cantidad de reservas registradas dentro de un rango de fechas.

### REP-02 — Habitaciones con mayor ocupación

Obtiene las habitaciones que presentan mayor cantidad de períodos reservados.

### REP-03 — Ingresos por período

Obtiene el monto total de pagos registrados durante un período.

### REP-04 — Clientes con mayor gasto

Ordena los clientes según el monto total pagado en sus reservas.

### REP-05 — Ocupación por tipo de habitación

Obtiene métricas de ocupación agrupadas por tipo de habitación.

Los resultados se transportan mediante DTOs específicos para reportes.

---

# 🔎 Specifications y filtros dinámicos

Las consultas dinámicas utilizan **Spring Data JPA Specifications**.

Esto permite combinar filtros opcionales sin crear un método de repositorio independiente para cada combinación.

Los criterios contemplados incluyen:

```text
hotel
tipo de habitación
número de habitación
estado de habitación
cliente
estado de reserva
fecha de entrada
fecha de salida
```

Ejemplo conceptual:

```text
GET /reservas/busqueda

filtros opcionales:
├── cliente
├── estado
├── fechaEntrada
├── fechaSalida
└── ...
```

---

# 📄 Paginación

Los listados utilizan las capacidades de paginación de Spring Data:

```java
Page<T>
Pageable
```

Esto permite evitar la recuperación innecesaria de grandes cantidades de registros en una sola solicitud.

---

# ⚠️ Manejo de errores

El sistema utiliza excepciones personalizadas y un mecanismo centralizado de manejo de errores.

Entre los escenarios contemplados se encuentran:

- Recurso no encontrado.
- Datos inválidos.
- Regla de negocio incumplida.
- Habitación no disponible.
- Conflicto de reserva.
- Operación no permitida por el estado actual.
- Conflicto de concurrencia.
- Errores inesperados.

Las validaciones se pueden clasificar en:

```text
Validación de entrada
        ↓
Validación de negocio
        ↓
Validación de consistencia/concurrencia
```

---

# 🛠️ Instalación

## Requisitos previos

Antes de ejecutar el proyecto se necesita:

- Java 21.
- Maven.
- Microsoft SQL Server.
- Una herramienta para administrar SQL Server, por ejemplo DBeaver o SQL Server Management Studio.
- Postman para las pruebas de la API.

---

## 1. Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/reservas-hotel-api.git
cd reservas-hotel-api
```

> Sustituye la URL por la URL real del repositorio.

---

## 2. Crear la base de datos

Crear una base de datos:

```text
hotelReservasDB
```

Posteriormente ejecutar los scripts SQL correspondientes a la estructura de la base de datos.

---

## 3. Configurar `application.yml`

Archivo:

```text
src/main/resources/application.yml
```

Ejemplo:

```yaml
server:
  port: 8080
  servlet:
    context-path: /api/v1

spring:
  datasource:
    url: jdbc:sqlserver://localhost:1433;databaseName=hotelReservasDB;encrypt=true;trustServerCertificate=true
    username: sa
    password: ${DB_PASSWORD}
    driver-class-name: com.microsoft.sqlserver.jdbc.SQLServerDriver

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

### Recomendación

No almacenar contraseñas reales directamente en Git.

Es preferible utilizar variables de entorno:

```text
DB_PASSWORD
```

Por ejemplo:

```bash
DB_PASSWORD=TuPasswordSeguro
```

La configuración exacta puede adaptarse al entorno local de desarrollo.

---

# ▶️ Ejecución

## Compilar

```bash
mvn clean package
```

## Ejecutar con Maven

```bash
mvn spring-boot:run
```

La API estará disponible en:

```text
http://localhost:8080/api/v1
```

---

# 🧪 Pruebas con Postman

Las pruebas deben cubrir tanto los flujos exitosos como los escenarios negativos y de concurrencia.

## Flujo de infraestructura

```text
Crear Hotel
    ↓
Crear TipoHabitacion
    ↓
Crear Habitacion
    ↓
Crear Cliente
```

## Flujo de reserva confirmada

```text
POST /reservas
        ↓
    PENDIENTE
        ↓
POST /pagos
        ↓
    CONFIRMADA
```

## Flujo de expiración

```text
POST /reservas
        ↓
    PENDIENTE
        ↓
fechaExpiracion alcanzada
        ↓
Scheduler
        ↓
    CANCELADA
```

## Casos negativos

Se deben probar, como mínimo:

- Fecha de entrada posterior o igual a fecha de salida.
- Habitación inexistente.
- Cliente inexistente.
- Cliente inactivo.
- Habitación en mantenimiento.
- Habitación inactiva.
- Conflicto de disponibilidad.
- Reserva inexistente.
- Pago con monto menor o igual a cero.
- Pago de una reserva cancelada.
- Pago de una reserva expirada.
- Operaciones incompatibles con el estado actual.

## Prueba de solapamiento

### Caso con conflicto

```text
Reserva A:
01/09 → 05/09

Reserva B:
03/09 → 07/09
```

Resultado esperado:

```text
HTTP 4xx
Reserva B rechazada
```

### Caso sin conflicto

```text
Reserva A:
01/09 → 05/09

Reserva B:
05/09 → 08/09
```

Resultado esperado:

```text
Reserva B permitida
```

siempre que se mantenga la regla de salida como límite no ocupado.

---

# 🧵 Pruebas de concurrencia

El proyecto debe considerar escenarios donde varias solicitudes intenten modificar o reservar simultáneamente el mismo recurso.

Ejemplo:

```text
Solicitud A ──► Habitación 101 ──► disponible
Solicitud B ──► Habitación 101 ──► disponible
       │
       ▼
    concurrencia
```

El objetivo es evitar:

```text
Reserva A → CONFIRMADA
Reserva B → CONFIRMADA
```

cuando ambas reservas sean incompatibles.

Los mecanismos utilizados incluyen:

```text
@Transactional
     +
PESSIMISTIC_WRITE
     +
@Version
     +
validaciones de negocio
```

---

# 🔒 Transacciones

Las operaciones críticas se ejecutan mediante transacciones declarativas con:

```java
@Transactional
```

Entre ellas:

- creación de reservas;
- modificación de reservas;
- cancelaciones;
- registro de pagos;
- operaciones críticas de disponibilidad;
- procesamiento de expiración.

Las operaciones que utilizan:

```java
PESSIMISTIC_WRITE
```

deben ejecutarse dentro de una transacción activa.

---

# 🧭 Decisiones de diseño relevantes

### Separación de responsabilidades

El proyecto mantiene separadas las responsabilidades entre:

```text
Controller
    ↓
Service
    ↓
Repository
```

Esto evita colocar reglas de negocio directamente en los controladores o lógica de persistencia en ellos.

### DTOs

Los DTOs permiten controlar el contrato de la API sin exponer directamente las entidades JPA.

### Locking

El locking se utiliza solamente en operaciones donde existe una necesidad real de coordinación entre transacciones.

### Disponibilidad

La disponibilidad se determina mediante reglas de negocio y consultas que consideran:

- estado operativo de la habitación;
- período solicitado;
- reservas existentes;
- estado de las reservas;
- expiración de reservas temporales.

---

# 📌 Criterios de aceptación

El sistema debe cumplir los principales criterios definidos para el proyecto:

- La aplicación se ejecuta correctamente.
- La conexión con SQL Server funciona.
- Las entidades están correctamente relacionadas.
- Los CRUD principales están implementados.
- Las habitaciones se pueden asociar a hoteles y tipos.
- Las reservas pueden crearse, modificarse y cancelarse.
- La disponibilidad puede consultarse por fechas.
- Los períodos solapados son rechazados.
- Los períodos sin conflicto son permitidos.
- Las habitaciones en mantenimiento o inactivas no pueden reservarse.
- Los estados de las reservas funcionan correctamente.
- Los pagos pueden registrarse y consultarse.
- Las operaciones críticas utilizan transacciones.
- Las Specifications funcionan para filtros dinámicos.
- Las consultas por rangos de fechas funcionan.
- Los listados soportan paginación.
- Los reportes requeridos funcionan.
- Las excepciones son gestionadas centralizadamente.
- Existe un mecanismo documentado de control de concurrencia.
- Se consideran escenarios básicos de Race Condition.
- Las APIs pueden probarse mediante Postman.

---

# 🗺️ Estado del proyecto

El proyecto se desarrolla de manera progresiva a partir de los requisitos oficiales.

| Fase | Descripción | Estado |
|---|---|---|
| Fase 00 | Análisis y planificación | ✅ |
| Fase 01 | Diseño de arquitectura | ✅ |
| Fase 02 | Configuración del proyecto | ✅ |
| Fase 03 | Modelo de datos | ✅ |
| Fase 04 | Gestión de Hoteles | ✅ |
| Fase 05 | Gestión de Tipos de Habitación | ✅ |
| Fase 06 | Gestión de Habitaciones | ✅ |
| Fase 07 | Gestión de Clientes | ✅ |
| Fase 08 | Gestión de Reservas | ✅ |
| Fase 09 | Disponibilidad | ✅ |
| Fase 10 | Transacciones y excepciones | ✅ |
| Fase 11 | Specifications y paginación | ✅ |
| Fase 12 | Pagos | ✅ |
| Fase 13 | Concurrencia | ✅ |
| Fase 14 | Reportes | ✅ |
| Fase 15 | Pruebas y documentación | ✅ |

> El estado anterior representa el avance actual del desarrollo. Debe actualizarse conforme se completen y validen las fases restantes.

---

# 🎯 Objetivos técnicos

Este proyecto busca demostrar dominio práctico de:

- Java moderno.
- Spring Boot.
- APIs REST.
- Arquitectura en capas.
- Programación orientada a objetos.
- Spring Data JPA.
- Hibernate.
- Relaciones entre entidades.
- DTOs.
- Validaciones.
- Transacciones.
- Excepciones personalizadas.
- Paginación.
- Consultas por rangos.
- Specifications.
- SQL Server.
- Maven.
- Lombok.
- Estados y transiciones.
- Consistencia de datos.
- Optimistic Locking.
- Pessimistic Locking.
- `@Version`.
- Race Conditions.
- Diseño de operaciones críticas bajo concurrencia.

---

# 📚 Requisitos del proyecto

Los requisitos funcionales y técnicos que sirven como fuente de verdad del proyecto se encuentran documentados en:

```text
requisitosSistemaHotel.md
```

El README resume la implementación y las decisiones técnicas del proyecto; los requisitos originales mantienen la definición formal del alcance.

---

## 👥 Autores

- **Erick Torres** - *Desarrollo inicial* - [ETC Tech](https://github.com/tu-usuario)

---

## 📄 Licencia

Este proyecto está bajo la Licencia Apache 2.0 - ver el archivo [LICENSE](https://license/) para más detalles.

---

## 🙏 Agradecimientos

- Spring Boot por su excelente framework
- La comunidad de desarrolladores por su soporte

---

## 📞 Contacto

Para cualquier consulta o sugerencia, por favor contacta a:

- **Email**: etorresca8\@gmail.com
- **GitHub**: [https://github.com/E-TorresC](https://github.com/E-TorresC)

---

**¡Gracias por usar Gestión de Ventas API!** 🚀

text

````
---

