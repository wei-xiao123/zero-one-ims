$ErrorActionPreference = 'Stop'

$container = 'mysql8'
$rootPassword = '123456'
$passwordArg = "-p$rootPassword"

$cmd = "CREATE DATABASE IF NOT EXISTS zo_psi DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci; SHOW DATABASES LIKE 'zo_psi';"

docker exec $container mysql -uroot $passwordArg -e $cmd
if ($LASTEXITCODE -ne 0) {
	throw "MySQL initialization failed in container '$container'."
}

Write-Host 'MySQL database zo_psi ensured and verified.'
