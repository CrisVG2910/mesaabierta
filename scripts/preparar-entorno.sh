#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
if [ -f .env ]; then echo '.env existente: se conserva'; exit 0; fi
command -v openssl >/dev/null || { echo 'Instala openssl'; exit 1; }
cat > .env <<EOF
MYSQL_PASSWORD=$(openssl rand -hex 20)
MYSQL_ROOT_PASSWORD=$(openssl rand -hex 20)
JWT_SECRET_BASE64=$(openssl rand -base64 32 | tr -d '\n')
APP_PORT=8080
PUBLIC_BASE_URL=http://localhost:8080/recetas
EOF
chmod 600 .env
echo 'Archivo .env generado sin exponer las credenciales.'
