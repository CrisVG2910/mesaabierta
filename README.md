# Mesa Abierta · Sumativa 2 (ISY2202, Semana 5)

Evolución del proyecto de la semana 3; conserva diseño Thymeleaf e incorpora MySQL/JPA, API JWT y funciones colaborativas.

## Requisitos

- Java 21, VS Code (Extensiones Java + Spring), Docker Desktop o Docker Engine con Compose v2.
- Para ejecutar sin Docker: MySQL 8.4 (BD `mesaabierta`) y variables `DB_URL`, `DB_USER`, `DB_PASSWORD` y `JWT_SECRET_BASE64`.

## Inicio rápido — Windows + Docker Desktop

```powershell
cd C:\Proyectos\MesaAbierta_Sumativa2
.\scripts\preparar-entorno.ps1
docker compose up -d --build
docker compose ps
```

Entrar a **http://localhost:8080/recetas/**. Primera compilación descarga dependencias Maven e imagen MySQL.
Las recetas y tres usuarios se insertan automáticamente si no existen. Para ver logs:

```powershell
docker compose logs --tail=100 mesaabierta
```

### Cuentas académicas

- `chefana` / `Recetas2026!`
- `chefleo` / `Recetas2026!`
- `chefmia` / `Recetas2026!`

**Solo para una evaluación académica; no son credenciales aptas para un servicio público real.**

## Qué incluye

- MVC: `/` pública, `/buscar` pública, `/login` pública, `/receta/{slug}` privada.
- `/publicar`: un usuario autenticado publica una receta nueva en MySQL.
- Detalle: comentar, valorar 1-5 y visualizar fotos/videos.
- Subir archivo: solo autor de receta; detecta firma real y admite JPG/PNG/WebP (máx 5 MB) y MP4/WebM (máx 20 MB).
- Compartir: solo autor; tablón interno `/compartidas` o intenciones de compartir en WhatsApp, Facebook y X.
- MySQL persiste usuarios, recetas, comentarios, valoraciones, registros de archivos y compartidos.
- Multimedia se almacena en volumen Docker **media_data**. La base está en **mysql_data**.

## API REST JWT (independiente del login Thymeleaf)

Prefijo real: `/recetas/api/`. API sin sesión: Bearer JWT HS256 (1 hora). Por defecto:

| Método | Endpoint | Acceso |
|---|---|---|
| POST | `/api/auth/login` | Público (usuario/contraseña) |
| GET | `/api/recetas` | Público (filtros opcionales) |
| GET | `/api/recetas/{slug}` | JWT |
| POST | `/api/recetas` | JWT (nueva receta, JSON) |
| GET/POST | `/api/recetas/{slug}/comentarios` | JWT |
| GET/PUT | `/api/recetas/{slug}/valoracion` | JWT |
| GET/POST | `/api/recetas/{slug}/archivos` | JWT (subida solo autor, campo multipart `archivo`) |
| GET | `/api/media/{id}` | JWT (descarga de archivo) |
| POST | `/api/recetas/{slug}/compartir` | JWT (solo autor; `SITIO`, `X`, `WHATSAPP`, `FACEBOOK`) |

**Ejemplo de login PowerShell:**

```powershell
$login = Invoke-RestMethod -Method POST -Uri 'http://localhost:8080/recetas/api/auth/login' `
  -ContentType 'application/json' -Body '{"username":"chefana","password":"Recetas2026!"}'
$headers = @{ Authorization = "Bearer $($login.accessToken)" }
Invoke-RestMethod -Headers $headers 'http://localhost:8080/recetas/api/recetas/pasta-pesto'
```

También existe `scripts/probar-api.ps1`, que crea un comentario de demostración en la base.

### Publicar por API

`POST /recetas/api/recetas` con Bearer token y cuerpo JSON:

```json
{
  "nombre":"Cazuela de verduras", "cocina":"Chilena", "pais":"Chile", "dificultad":"Fácil",
  "minutos":50, "porciones":4, "resumen":"Una preparación casera para compartir",
  "introduccion":"Una receta sencilla y tradicional",
  "ingredientes":"Papas\nZapallo\nZanahoria", "pasos":"Pelar verduras\nHervir\nServir"
}
```

## Tests sin MySQL (H2 en memoria solo durante `test`)

```powershell
.\mvnw.cmd test
```

Prueban rutas públicas/privadas, CSRF web, escape de entrada y login API JWT. **Se deben ejecutar localmente antes de afirmar que pasaron**.

## Migración Azure de la sumativa 1

1. Conservar el ZIP y capturas de la primera sumativa.
2. Parar el proyecto anterior desde su directorio: `sudo docker compose down` **sin `-v`**.
3. Subir esta carpeta/ZIP nuevo a Ubuntu y entrar a `MesaAbierta_Sumativa2`.
4. Ejecutar `bash scripts/preparar-entorno.sh`.
5. Editar `.env`: `APP_PORT=80` y `PUBLIC_BASE_URL=http://IP_PUBLICA/recetas`.
6. Ejecutar `sudo docker compose up -d --build`.
7. Confirmar `sudo docker compose ps` y `curl -I http://localhost/recetas/`.
8. Desde un navegador externo, validar inicio, login, comentarios, medios y API JWT.

**IMPORTANTE:** esta VM usa HTTP por exigencia académica; no compartir contraseñas reales ni datos sensibles. Para publicar fuera de una demo, configurar HTTPS, cambiar cuentas, aplicar migraciones versionadas y restringir accesos.

## Seguridad

- Dos cadenas independientes: sesiones Spring Security con CSRF para HTML, y Bearer JWT sin cookies para API.
- Los JWT se firman con un secreto aleatorio de 32 bytes generado en `.env` (no incluido en ZIP).
- Archivos se guardan con UUID, tamaño limitado y MIME verificado por firma; descarga autenticada.
- Datos HTML escapados con `th:text`/`th:value` y política CSP.
- No se aceptan rutas desconocidas; operaciones de archivo/compartir verifican autoría.
- `db/schema.sql` y `db/datos_demo.sql` se entregan para documentación. En ejecución, Hibernate `ddl-auto=update`
  crea las tablas y `DataSeeder` inserta 3 usuarios + 8 recetas si faltan. Para producción sería mejor Flyway.
- **Aún no se han generado informes SAST/SCA;** ver `docs/ANALISIS_SAST_PENDIENTE.md`.

## Entregables a realizar posteriormente

Informes reales OWASP Dependency-Check / Semgrep, capturas de errores críticos y correcciones, despliegue actualizado en la VM, informe Word en formato Duoc y video Teams de 3–8 min.
