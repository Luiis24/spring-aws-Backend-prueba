# Demo Franchises API

API REST para administrar franquicias, sucursales y productos, desarrollada como prueba técnica de Backend. La aplicación está construida con Java 17 y Spring Boot, usa Spring MVC y Spring Data JPA, y persiste la información en MySQL.

## Contenido

- [Requisitos](#requisitos)
- [Configuración de MySQL](#configuración-de-mysql)
- [Construir y ejecutar con Docker](#construir-y-ejecutar-con-docker)
- [Probar la API](#probar-la-api)
- [Endpoints](#endpoints)
- [Criterios de aceptación y puntos extra](#criterios-de-aceptación-y-puntos-extra)
- [Estructura del proyecto](#estructura-del-proyecto)

## Requisitos

Para ejecutar la aplicación en Docker:

- Docker Desktop (Windows/macOS) o Docker Engine (Linux), iniciado.
- Acceso de red a una instancia MySQL compatible.
- Una base de datos con el esquema requerido por la aplicación.

No es necesario instalar Java ni Maven para construir la imagen: el `Dockerfile` compila el proyecto en una etapa con JDK 17 y ejecuta el JAR en una imagen JRE 17.

## Configuración de MySQL

La aplicación **no crea ni migra el esquema**: Hibernate está configurado con `ddl-auto=none`. La base de datos y sus tablas deben existir antes de iniciar el servicio. Las entidades usan las tablas `franquicias`, `sucursales`, `productos` y `sucursal_productos`.

La conexión se configura con estas variables de entorno:

| Variable | Descripción | Ejemplo |
| --- | --- | --- |
| `SPRING_DATASOURCE_URL` | URL JDBC de MySQL | `jdbc:mysql://<host>:3306/franquicias_db` |
| `SPRING_DATASOURCE_USERNAME` | Usuario de la base de datos | `app_user` |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de la base de datos | Proporcionarla de forma segura |

Si la instancia está en AWS RDS, verifica que esté disponible desde tu equipo y que sus reglas de red permitan la conexión entrante a MySQL (normalmente el puerto `3306`) desde el origen autorizado. No uses `localhost` para una instancia RDS. Para una base de datos que corre en el mismo equipo que Docker Desktop, normalmente se usa `host.docker.internal` como host desde el contenedor.

> **Seguridad:** las credenciales de base de datos deben permanecer fuera del código, de la imagen Docker y del control de versiones. La configuración de la aplicación usa ahora las variables anteriores. Como antes había credenciales en el archivo de propiedades, rota la contraseña de esa base de datos antes de compartir o desplegar el proyecto; eliminarla del archivo actual no la elimina del historial de Git.

## Construir y ejecutar con Docker

Abre PowerShell en la carpeta del proyecto Maven:

```powershell
cd demo-franchises
```

### 1. Crear el archivo local de variables

Copia la plantilla y edita `demo-franchises\.env` con los datos de tu instancia. El archivo `.env` está excluido de Git y del contexto de Docker.

```powershell
Copy-Item .env.example .env
notepad .env
```

Reemplaza todos los valores de ejemplo; no uses literalmente los valores entre `< >`:

```dotenv
SPRING_DATASOURCE_URL=jdbc:mysql://<host>:3306/franquicias_db
SPRING_DATASOURCE_USERNAME=<usuario>
SPRING_DATASOURCE_PASSWORD=<contraseña>
```

### 2. Construir la imagen

Ejecuta desde `demo-franchises`, donde se encuentra el `Dockerfile`:

```powershell
docker build -t demo-franchises .
```

### 3. Iniciar el contenedor

```powershell
docker run --rm -p 8080:8080 --env-file .env demo-franchises
```

La API queda disponible en `http://localhost:8080`. `--rm` elimina el contenedor al detenerlo; la información permanece en MySQL, no en el contenedor.

El comando indicado originalmente, `docker run --rm -p 8080:8080 demo-franchises`, **no carga automáticamente** un archivo `.env`. Para esta configuración segura, utiliza `--env-file .env` (o pasa las tres variables con `-e`). No agregues credenciales directamente al comando si quedarán guardadas en el historial de la terminal.

Detén el servicio con `Ctrl+C`.

## Probar la API

Usa el identificador de franquicia, sucursal o producto que exista en tu base de datos; los IDs de los ejemplos son ilustrativos. Las operaciones que reciben un JSON esperan `Content-Type: application/json`.

Ejemplo para listar franquicias:

```powershell
curl.exe http://localhost:8080/franchises
```

Ejemplo para crear una franquicia:

```powershell
curl.exe -X POST http://localhost:8080/franchises `
  -H "Content-Type: application/json" `
  -d '{"name":"Franquicia Norte"}'
```

## Endpoints

La URL base local es `http://localhost:8080`. No se configura un prefijo adicional para las rutas.

### Franquicias

| Método | Ruta | Descripción |
| --- | --- | --- |
| `GET` | `/franchises` | Listar franquicias. |
| `GET` | `/franchises/{id}` | Consultar una franquicia por ID. |
| `POST` | `/franchises` | Crear una franquicia. |
| `PUT` | `/franchises/{id}` | Actualizar el nombre de una franquicia. |
| `DELETE` | `/franchises/{id}` | Eliminar una franquicia. |

Crear:

```json
{
  "name": "Franquicia Norte"
}
```

Actualizar (el ID se indica en la ruta):

```json
{
  "name": "Franquicia Norte Actualizada"
}
```

### Sucursales

| Método | Ruta | Descripción |
| --- | --- | --- |
| `GET` | `/branches` | Listar sucursales. |
| `GET` | `/branches/{id}` | Consultar una sucursal por ID. |
| `POST` | `/branches` | Crear una sucursal asociada a una franquicia. |
| `PUT` | `/branches/{id}` | Actualizar los datos de una sucursal. |
| `DELETE` | `/branches/{id}` | Eliminar una sucursal. |

Crear una sucursal para la franquicia con ID `1`:

```json
{
  "idBranch": 1,
  "name": "Sucursal Centro"
}
```

En el cuerpo de estas operaciones, `idBranch` representa el **ID de la franquicia propietaria**. Para actualizar la sucursal, envía ambos campos:

```json
{
  "idBranch": 1,
  "name": "Sucursal Centro Actualizada"
}
```

### Productos

| Método | Ruta | Descripción |
| --- | --- | --- |
| `GET` | `/products` | Listar productos. |
| `GET` | `/products/{id}` | Consultar un producto por ID. |
| `POST` | `/products` | Crear un producto. |
| `PUT` | `/products/{id}` | Actualizar nombre y precio del producto. |
| `DELETE` | `/products/{id}` | Eliminar un producto. |

Crear:

```json
{
  "name": "Producto A",
  "price": 2500
}
```

Actualizar (el ID se indica en la ruta):

```json
{
  "name": "Producto A Actualizado",
  "price": 2800
}
```

### Productos y stock por sucursal

| Método | Ruta | Descripción |
| --- | --- | --- |
| `POST` | `/branch-products` | Asociar un producto a una sucursal con un stock inicial. |
| `DELETE` | `/branch-products/branch/{idBranch}/product/{idProduct}` | Quitar la asociación del producto con la sucursal. |
| `PATCH` | `/branch-products/branch/{idBranch}/product/{idProduct}/stock` | Actualizar el stock de un producto en una sucursal. |
| `GET` | `/branch-products/franchise/{franchiseId}/top-stock` | Obtener el producto o los productos con mayor stock de cada sucursal de una franquicia. |

Asociar el producto `3` a la sucursal `2`:

```json
{
  "idBranch": 2,
  "idProduct": 3,
  "stock": 10
}
```

Actualizar el stock a `25`:

```json
{
  "stock": 25
}
```

La consulta de mayor stock devuelve una lista con información de la sucursal y el producto. Si hay empate en una sucursal, la consulta puede devolver más de un producto para esa sucursal.

Ejemplo de respuesta:

```json
[
  {
    "branchId": 2,
    "branchName": "Sucursal Centro",
    "productId": 3,
    "productName": "Producto A",
    "stock": 25
  }
]
```

La ruta de actualización de stock devuelve `400 Bad Request` si el valor falta o es negativo. La asociación repetida de un producto en una sucursal devuelve `409 Conflict`; quitar o actualizar una asociación que no existe devuelve `404 Not Found`.

## Criterios de aceptación y puntos extra

Evaluación contrastada con los criterios del PDF y con los controladores, servicios, entidades y consultas del proyecto:

| Criterio | Resultado | Evidencia |
| --- | --- | --- |
| API desarrollada con Spring Boot | Cumplido | Aplicación Spring Boot con Java 17. |
| Agregar una franquicia | Cumplido | `POST /franchises`. |
| Agregar una sucursal a una franquicia | Cumplido | `POST /branches`, con el ID de la franquicia en `idBranch`. |
| Agregar un producto a una sucursal | Cumplido | `POST /branch-products`, con stock inicial. |
| Eliminar un producto de una sucursal | Cumplido | `DELETE /branch-products/branch/{idBranch}/product/{idProduct}`. |
| Modificar el stock de un producto | Cumplido | `PATCH /branch-products/branch/{idBranch}/product/{idProduct}/stock`. |
| Consultar el producto con más stock por sucursal de una franquicia | Cumplido | `GET /branch-products/franchise/{franchiseId}/top-stock`; la consulta obtiene máximos por sucursal y devuelve los datos de sucursal y producto. |
| Persistencia en una base de datos en la nube | Cumplido | Persistencia MySQL configurada para AWS RDS. La conexión requiere credenciales y acceso de red válidos. |
| Empaquetar la aplicación con Docker | Cumplido | `Dockerfile` multi-stage; la imagen se construye con `docker build`. |
| Programación funcional/reactiva | No acreditado | La aplicación usa Spring MVC y JPA de forma síncrona e imperativa; no se encontraron WebFlux, `Mono` o `Flux`. |
| Endpoint para cambiar el nombre de la franquicia | Cumplido | `PUT /franchises/{id}`. |
| Endpoint para cambiar el nombre de la sucursal | Cumplido | `PUT /branches/{id}`. |
| Endpoint para cambiar el nombre del producto | Cumplido | `PUT /products/{id}`. |
| Aprovisionar persistencia como infraestructura como código (IaC) | No cumplido en el repositorio | No se encontraron plantillas Terraform, CloudFormation u otra definición IaC. |
| Desplegar toda la solución en la nube | No cumplido como solución completa | La base de datos está en AWS, pero el contenedor de la aplicación se ejecuta localmente según el flujo documentado. |

## Estructura del proyecto

```text
.
├── README.md
└── demo-franchises/
    ├── Dockerfile
    ├── pom.xml
    ├── .env.example
    └── src/
        ├── main/
        │   ├── java/com/franchises/demo_franchises/
        │   │   ├── controller/
        │   │   ├── dao/
        │   │   ├── entity/
        │   │   └── service/
        │   └── resources/application.properties
        └── test/
```

El repositorio contiene una prueba de contexto de Spring (`contextLoads`); no sustituye pruebas de integración que verifiquen las operaciones de la API contra una base de datos.
