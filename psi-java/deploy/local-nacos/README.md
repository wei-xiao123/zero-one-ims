# Local Middleware Bootstrap

This folder contains local config templates and scripts for Docker-based middleware.

## What it configures

- Nacos configs:
  - `system.yaml`
  - `data-source.yaml`
  - `third-services.yaml`
  - `key-config.yaml`
- MySQL database:
  - `zo_psi`

## Prerequisites

- Running containers:
  - `nacos` on `localhost:8848`
  - `mysql8` on `localhost:3306`
  - `redis` on `localhost:6379`
- MySQL root password in `init-mysql.ps1` matches your container.

## Run

```powershell
cd psi-java/deploy/local-nacos
powershell -ExecutionPolicy Bypass -File .\init-mysql.ps1
powershell -ExecutionPolicy Bypass -File .\import-to-nacos.ps1
```

## Verify Nacos configs

```powershell
curl "http://localhost:8848/nacos/v1/cs/configs?dataId=system.yaml&group=DEFAULT_GROUP"
curl "http://localhost:8848/nacos/v1/cs/configs?dataId=data-source.yaml&group=DEFAULT_GROUP"
```

## Notes

- `third-services.yaml` includes placeholders for Seata/FastDFS/RocketMQ/Easy-ES.
- If you do not run these middleware, avoid starting modules that require them.
- Frontend proxy can stay on public gateway or switch to local gateway `http://localhost:10001`.
