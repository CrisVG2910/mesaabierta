# Plan de análisis de seguridad — después de probar el sistema

**No se han ejecutado herramientas SAST ni SCA sobre esta versión desde el entorno de creación.**
La sumativa pide evidencias verificables antes y después de corregir hallazgos críticos.

1. Revisar primero funcionalidad: `./mvnw.cmd test` y `docker compose up -d --build`.
2. Herramientas a investigar: SonarQube, Semgrep y OWASP Dependency-Check.
3. Herramienta propuesta de SCA: OWASP Dependency-Check (Maven), como recomienda la guía.
4. Para patrones estáticos en Java/HTML: ejecutar Semgrep Community, reportando qué reglas se activaron.
5. Exportar informes JSON/HTML, capturar las pantallas de instalación, configuración, ejecución y resultados.
6. Evaluar cada hallazgo (severidad, archivo/línea, evidencia, posible falso positivo).
7. Resolver los hallazgos críticos reales sin introducir riesgos nuevos; ejecutar otra vez y comparar.
8. No afirmar «cero vulnerabilidades» solo porque una herramienta no reporta alertas.

Documentación:
- https://owasp.org/www-project-dependency-check/
- https://semgrep.dev/docs/
- https://docs.sonarsource.com/sonarqube-community-build/
