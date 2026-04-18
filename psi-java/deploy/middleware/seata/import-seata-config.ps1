$ErrorActionPreference = 'Stop'

$baseUrl = 'http://localhost:8848/nacos/v1/cs/configs'
$dataId = 'seataServer.properties'
$group = 'SEATA_GROUP'
$file = Join-Path (Split-Path -Parent $MyInvocation.MyCommand.Path) 'seataServer.properties'

if (-not (Test-Path $file)) {
  throw "Missing file: $file"
}

$content = Get-Content -Path $file -Raw

$body = @{
  dataId = $dataId
  group = $group
  type = 'properties'
  content = $content
}

Invoke-RestMethod -Method Post -Uri $baseUrl -Body $body | Out-Null
Write-Host 'Imported seataServer.properties to Nacos group SEATA_GROUP.'
