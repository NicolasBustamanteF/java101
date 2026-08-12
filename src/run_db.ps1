# PostgreSQL Setup Script
# Este script crea la base de datos db_depositos y el usuario develop

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$sqlFile = Join-Path $scriptDir "db\setup_db.sql"

if (-not (Test-Path $sqlFile)) {
    Write-Error "SQL file not found: $sqlFile"
    exit 1
}

# Buscar psql
$psqlPath = $null

if (Get-Command psql -ErrorAction SilentlyContinue) {
    $psqlPath = (Get-Command psql).Source
} else {
    $commonPaths = @(
        "C:\Program Files\PostgreSQL\18\bin\psql.exe",
        "C:\Program Files\PostgreSQL\17\bin\psql.exe",
        "C:\Program Files\PostgreSQL\16\bin\psql.exe",
        "C:\Program Files\PostgreSQL\15\bin\psql.exe",
        "C:\Program Files\PostgreSQL\14\bin\psql.exe",
        "C:\Program Files\PostgreSQL\13\bin\psql.exe"
    )

    foreach ($path in $commonPaths) {
        if (Test-Path $path) {
            $psqlPath = $path
            Write-Host "Found psql at: $psqlPath"
            break
        }
    }
}

if (-not $psqlPath) {
    Write-Error "psql not found"
    exit 2
}

# Pedir la contraseña
$pgPass = Read-Host -AsSecureString "PostgreSQL password for user 'postgres'"
$bstr = [System.Runtime.InteropServices.Marshal]::SecureStringToBSTR($pgPass)
$plain = [System.Runtime.InteropServices.Marshal]::PtrToStringAuto($bstr)
[System.Runtime.InteropServices.Marshal]::ZeroFreeBSTR($bstr)

$env:PGPASSWORD = $plain

try {
    Write-Host "Creating database db_depositos..."
    $dbExists = (& $psqlPath -U postgres -d postgres -tAc "SELECT 1 FROM pg_database WHERE datname='db_depositos'") -replace "\s+", ""

    if ($dbExists -ne '1') {
        & $psqlPath -U postgres -d postgres -c "CREATE DATABASE db_depositos"
        Write-Host "OK - Database created"
    } else {
        Write-Host "OK - Database already exists"
    }

    Write-Host "Setting up user 'develop'..."
    $roleExists = (& $psqlPath -U postgres -d postgres -tAc "SELECT 1 FROM pg_roles WHERE rolname='develop'") -replace "\s+", ""

    if ($roleExists -ne '1') {
        & $psqlPath -U postgres -d postgres -c "CREATE USER develop WITH PASSWORD 'develop123'"
        Write-Host "OK - User created"
    } else {
        & $psqlPath -U postgres -d postgres -c "ALTER USER develop WITH PASSWORD 'develop123'"
        Write-Host "OK - User password updated"
    }

    Write-Host "Granting privileges..."
    & $psqlPath -U postgres -d db_depositos -c "GRANT ALL PRIVILEGES ON DATABASE db_depositos TO develop"
    & $psqlPath -U postgres -d db_depositos -c "GRANT USAGE ON SCHEMA public TO develop"
    Write-Host "OK - Privileges granted"

    Write-Host "Done! Database is ready."
    Write-Host "Database: db_depositos"
    Write-Host "User: develop"
    Write-Host "Password: develop123"

} catch {
    Write-Error "Error: $_"
} finally {
    Remove-Item Env:PGPASSWORD -ErrorAction SilentlyContinue
}

