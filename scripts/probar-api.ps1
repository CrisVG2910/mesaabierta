param([string]$BaseUrl='http://localhost:8080/recetas')
$ErrorActionPreference='Stop'
Write-Host '1. Listado público:'
(Invoke-RestMethod "$BaseUrl/api/recetas").Count
Write-Host '2. Login JWT:'
$cred='{"username":"chefana","password":"Recetas2026!"}'
$login=Invoke-RestMethod -Method Post -Uri "$BaseUrl/api/auth/login" -ContentType 'application/json' -Body $cred
Write-Host ('Token Bearer recibido, longitud: '+$login.accessToken.Length)
$headers=@{Authorization="Bearer $($login.accessToken)"}
Write-Host '3. Detalle privado mediante JWT:'
$r=Invoke-RestMethod -Headers $headers "$BaseUrl/api/recetas/pasta-pesto"
Write-Host ('Receta: '+$r.nombre)
Write-Host '4. Comentario autorizado:'
Invoke-RestMethod -Method Post -Headers $headers -Uri "$BaseUrl/api/recetas/pasta-pesto/comentarios" -ContentType 'application/json' -Body '{"texto":"Comentario de prueba API JWT"}' | Format-List
Write-Host '5. Valoración autorizada:'
Invoke-RestMethod -Method Put -Headers $headers -Uri "$BaseUrl/api/recetas/pasta-pesto/valoracion" -ContentType 'application/json' -Body '{"puntuacion":5}' | Format-List
