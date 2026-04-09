$ErrorActionPreference = 'Stop'

$baseUrl = 'http://localhost:8848/nacos/v1/cs/configs'
$group = 'DEFAULT_GROUP'

$configs = @(
  @{ DataId = 'system.yaml'; File = 'system.yaml' },
  @{ DataId = 'data-source.yaml'; File = 'data-source.yaml' },
  @{ DataId = 'third-services.yaml'; File = 'third-services.yaml' },
  @{ DataId = 'key-config.yaml'; File = 'key-config.yaml' }
)

$root = Split-Path -Parent $MyInvocation.MyCommand.Path

foreach ($cfg in $configs) {
  $fullPath = Join-Path $root $cfg.File
  if (-not (Test-Path $fullPath)) {
    throw "Missing config file: $fullPath"
  }

  $content = Get-Content -Path $fullPath -Raw
  $body = @{
    dataId = $cfg.DataId
    group = $group
    type = 'yaml'
    content = $content
  }

  Invoke-RestMethod -Method Post -Uri $baseUrl -Body $body | Out-Null
  Write-Host "Imported $($cfg.DataId)"
}

Write-Host 'All Nacos configs imported.'
