# Lista de evidencias para la segunda sumativa ISY2202

No se declara evidencia todavía. Capturar estos resultados al ejecutar en tu PC:

- `docker compose ps`: contenedores MySQL y MesaAbierta en estado running/healthy.
- Home, buscador, formulario de publicar, detalle con foto/video, comentario, promedio y tablón compartidas.
- Postman: login API devuelve JWT; sin Bearer un endpoint privado devuelve 401; con Bearer funciona.
- MySQL: `SELECT * FROM app_user`, `SELECT * FROM receta`, `SELECT * FROM comentario`, `SELECT * FROM valoracion`, `SELECT * FROM archivo`, `SELECT * FROM compartido` (ocultar hashes y secretos en capturas).
- Investigación: al menos SonarQube, Semgrep y OWASP Dependency-Check (tipo, características, URL).
- Instalación y configuración herramienta elegida, escaneo inicial y reporte final.
- Clasificación y corrección de hallazgos críticos reales si existen; nunca inventar hallazgos.
- Azure: misma VM sumativa 1, nuevo despliegue con MySQL y almacenamiento persistente.
- Documento Word en formato semana 5; enlace a VM, video Teams (3 a 8 min), Git según pauta.
