$ErrorActionPreference = 'Stop'
$project = Split-Path -Parent $PSScriptRoot
$path = Join-Path $project '.env'
if (Test-Path $path) { Write-Host 'Ya existe .env. Se conserva.'; exit 0 }
function Get-RandomBytes([int]$count) {
  $bytes = New-Object byte[] $count
  $rng = [System.Security.Cryptography.RandomNumberGenerator]::Create()
  try { $rng.GetBytes($bytes) } finally { $rng.Dispose() }
  return ,$bytes
}
$mysql = ([BitConverter]::ToString((Get-RandomBytes 20))).Replace('-','')
$root = ([BitConverter]::ToString((Get-RandomBytes 20))).Replace('-','')
$jwt = [Convert]::ToBase64String((Get-RandomBytes 32))
@("MYSQL_PASSWORD=$mysql", "MYSQL_ROOT_PASSWORD=$root", "JWT_SECRET_BASE64=$jwt", "APP_PORT=8080", "PUBLIC_BASE_URL=http://localhost:8080/recetas") | Set-Content -Path $path -Encoding ascii
Write-Host 'Creado .env con claves aleatorias. No lo subas a Git.'
