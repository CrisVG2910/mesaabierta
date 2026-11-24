# Evidencia histórica: sumativa 1, OWASP ZAP (DAST)

# Guía para la fase de OWASP ZAP y el informe (NO es un resultado de análisis)

## Antes de empezar
1. Inicia la aplicación localmente con `./mvnw.cmd spring-boot:run` (Windows PowerShell: `\.\mvnw.cmd spring-boot:run`).
2. Comprueba que `http://localhost:8080/recetas/` y `/recetas/buscar` responden sin login.
3. Abre `/recetas/receta/pasta-pesto` en sesión anónima: debe redirigir a `/recetas/login`.
4. Inicia sesión con un usuario de prueba y valida que la página de detalle abre.

## ZAP Desktop
1. Instala OWASP ZAP desde https://www.zaproxy.org/download/.
2. Inicia ZAP y selecciona una sesión de pruebas dedicada.
3. Usa el navegador integrado de ZAP para navegar páginas públicas: inicio, buscador, login.
4. Realiza un análisis pasivo del tráfico y exporta reporte. Conserva captura de alertas y resumen de riesgos.
5. Realiza inicio de sesión desde el navegador instrumentado. Recorre detalles de recetas (la zona privada).
6. Analiza las solicitudes y respuestas de ambos conjuntos de URLs. Agrega si corresponde el contexto y manejo de sesión autenticada en ZAP.
7. Para análisis activo, utiliza SOLO la aplicación local o una instancia autorizada; no escanees sistemas ajenos.
8. Exporta informes HTML y JSON y conserva capturas de evidencia (antes/después).
9. Valida cada hallazgo: tipo, URL, riesgo, certeza, CWE, posible categoría OWASP Top 10, impacto, mitigación y resultado de una nueva prueba.

## Evidencias recomendadas
- E01: Inicio de aplicación en consola y nombre de proyecto.
- E02: Home y búsqueda pública sin sesión.
- E03: URL privada redirige a login (sin autenticación).
- E04: Login con uno de los tres usuarios y receta privada visible.
- E05: Instalación / configuración de ZAP.
- E06: Árbol de sitios y escaneo de páginas públicas.
- E07: Escaneo de páginas privadas durante sesión autenticada.
- E08: Tabla de alertas con severidad.
- E09: Detalle técnico de cada vulnerabilidad OWASP confirmada.
- E10: Cambio de código o configuración y repetición del análisis.
- E11: Verificación de acceso público a http://IP_PUBLICA/recetas.

## Plantilla de registro de hallazgos
| ID | URL | Alerta ZAP | Riesgo | Certeza | CWE | OWASP Top 10 | Corrección | Evidencia antes | Evidencia después |
|---|---|---|---|---|---|---|---|---|---|
| Por completar | Por completar | Por completar | Por completar | Por completar | Por completar | Por confirmar | Por completar | E## | E## |

**No inventes resultados**. Un resultado sin alertas detectadas no es garantía de ausencia de vulnerabilidades.

## Sugerencias OWASP para revisar
- Broken Access Control: verificar GET anónimo vs sesión autenticada.
- Cryptographic Failures: evitar HTTP sin TLS en un despliegue real y proteger credenciales.
- Injection: filtros Thymeleaf escapados, JDBC con consulta parametrizada.
- Security Misconfiguration: cabeceras, CSP, consola H2 deshabilitada, errores sin stacktraces.
- Identification and Authentication Failures: contraseñas BCrypt y cierre de sesión POST con CSRF.
- Vulnerable and Outdated Components: mantener Spring Boot y dependencias actualizadas.

**Advertencia:** la pauta académica solicita explícitamente `http://IP/recetas`, pero en entornos reales se debe usar HTTPS con certificado válido. Las tres cuentas del proyecto son SOLO de demostración.
