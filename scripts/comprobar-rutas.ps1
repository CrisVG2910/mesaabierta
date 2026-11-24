param([string]$BaseUrl = 'http://localhost:8080/recetas')
$ErrorActionPreference = 'Stop'
function Status([string]$Url) {
  try {
    $r = Invoke-WebRequest -Uri $Url -MaximumRedirection 0 -SkipHttpErrorCheck -UseBasicParsing
    return [int]$r.StatusCode
  } catch { return "ERROR: $($_.Exception.Message)" }
}
Write-Host "Pruebas HTTP para $BaseUrl" -ForegroundColor Cyan
Write-Host "Publica:  /                     -> $(Status "$BaseUrl/") (esperado: 200)"
Write-Host "Publica:  /buscar               -> $(Status "$BaseUrl/buscar") (esperado: 200)"
Write-Host "Publica:  /login                -> $(Status "$BaseUrl/login") (esperado: 200)"
Write-Host "Privada:  /receta/pasta-pesto   -> $(Status "$BaseUrl/receta/pasta-pesto") (esperado: 302 a login)"
Write-Host 'Para validar autenticacion y CSRF, ejecutar mvnw.cmd test o probar por navegador.'
