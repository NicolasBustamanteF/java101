@echo off
REM Script para instalar Maven en Windows
REM Requiere Chocolatey: https://chocolatey.org/install

echo ===============================================
echo Instalando Maven con Chocolatey...
echo ===============================================

REM Verificar si Chocolatey está instalado
where choco >nul 2>nul
if %ERRORLEVEL% neq 0 (
    echo.
    echo ERROR: Chocolatey no está instalado
    echo.
    echo Para instalar Chocolatey, abre PowerShell como administrador y ejecuta:
    echo.
    echo Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))
    echo.
    echo Después vuelve a ejecutar este script.
    pause
    exit /b 1
)

REM Instalar Maven
choco install maven -y

REM Verificar instalación
echo.
echo ===============================================
echo Verificando instalación...
echo ===============================================
mvn --version

echo.
echo ===============================================
echo ¡Maven instalado correctamente!
echo ===============================================
echo.
echo Ahora puedes ejecutar:
echo   mvn clean compile
echo   mvn compile
echo.
pause

