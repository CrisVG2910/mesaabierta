$ErrorActionPreference = 'Stop'
$root = Split-Path -Parent (Split-Path -Parent $MyInvocation.MyCommand.Path)
$version = '3.9.11'
$target = Join-Path $root ".mvn\apache-maven-$version"
if (Test-Path (Join-Path $target 'bin\mvn.cmd')) { return }
$url = "https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/$version/apache-maven-$version-bin.zip"
$archive = Join-Path $root ".mvn\apache-maven-$version-bin.zip"
Write-Host "Descargando Apache Maven $version (solo la primera vez)..."
Invoke-WebRequest -Uri $url -OutFile $archive
Expand-Archive -Path $archive -DestinationPath (Join-Path $root '.mvn') -Force
Remove-Item $archive -Force
if (-not (Test-Path (Join-Path $target 'bin\mvn.cmd'))) { throw 'No se pudo instalar Maven.' }
