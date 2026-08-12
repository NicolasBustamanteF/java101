# Script para iniciar el servicio de PostgreSQL en Windows

Write-Host "Buscando servicio de PostgreSQL..."

# Obtener lista de servicios de PostgreSQL
$postgresServices = Get-Service | Where-Object { $_.Name -like "postgresql*" }

if ($postgresServices.Count -eq 0) {
    Write-Error "No se encontro ningun servicio de PostgreSQL instalado"
    Write-Host "Asegurate de que PostgreSQL esta instalado en tu sistema"
    exit 1
}

# Si hay multiples servicios, usar el primero (generalmente el mas reciente)
$service = $postgresServices[0]
$serviceName = $service.Name
$serviceStatus = $service.Status

Write-Host "Servicio encontrado: $serviceName"
Write-Host "Estado actual: $serviceStatus"

if ($serviceStatus -eq "Running") {
    Write-Host "OK - El servicio ya esta en ejecucion"
    exit 0
}

Write-Host "Iniciando servicio de PostgreSQL..."

try {
    Start-Service -Name $serviceName -ErrorAction Stop
    Write-Host "OK - Servicio iniciado correctamente"

    # Esperar un poco para que se estabilice
    Start-Sleep -Seconds 2

    # Verificar que esta corriendo
    $serviceCheck = Get-Service -Name $serviceName
    if ($serviceCheck.Status -eq "Running") {
        Write-Host "OK - PostgreSQL esta en ejecucion en localhost:5432"
    } else {
        Write-Warning "El servicio se inició pero su estado es: $($serviceCheck.Status)"
    }
} catch {
    Write-Error "Error al iniciar el servicio: $_"
    Write-Host ""
    Write-Host "Posibles soluciones:"
    Write-Host "1) Ejecuta PowerShell como Administrador"
    Write-Host "2) Verifica que PostgreSQL esta instalado correctamente"
    exit 2
}

