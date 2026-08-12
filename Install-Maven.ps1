# Script para instalar Maven en Windows
# Ejecutar como administrador

Write-Host "===============================================" -ForegroundColor Cyan
Write-Host "Instalando Maven con Chocolatey..." -ForegroundColor Cyan
Write-Host "===============================================" -ForegroundColor Cyan
Write-Host ""

# Verificar si Chocolatey está instalado
try {
    choco --version | Out-Null
    Write-Host "✓ Chocolatey detectado" -ForegroundColor Green
} catch {
    Write-Host "✗ Chocolatey no está instalado" -ForegroundColor Red
    Write-Host ""
    Write-Host "Para instalar Chocolatey, abre PowerShell como administrador y ejecuta:" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))" -ForegroundColor White
    Write-Host ""
    Write-Host "Después vuelve a ejecutar este script." -ForegroundColor Yellow
    Read-Host "Presiona Enter para salir"
    exit 1
}

# Instalar Maven
Write-Host ""
Write-Host "Instalando Maven..." -ForegroundColor Cyan
choco install maven -y

Write-Host ""
Write-Host "===============================================" -ForegroundColor Cyan
Write-Host "Verificando instalación..." -ForegroundColor Cyan
Write-Host "===============================================" -ForegroundColor Cyan
Write-Host ""

# Verificar instalación
mvn --version

Write-Host ""
Write-Host "===============================================" -ForegroundColor Green
Write-Host "✓ ¡Maven instalado correctamente!" -ForegroundColor Green
Write-Host "===============================================" -ForegroundColor Green
Write-Host ""
Write-Host "Ahora puedes ejecutar:" -ForegroundColor Cyan
Write-Host "  mvn clean compile" -ForegroundColor White
Write-Host "  mvn compile" -ForegroundColor White
Write-Host ""
Read-Host "Presiona Enter para salir"

