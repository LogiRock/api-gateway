# api-gateway

Puerta de entrada única de LogiTrack (Spring Cloud Gateway). Rutea cada pedido al servicio que corresponde, buscándolo por nombre en Eureka.

- **Puerto:** 8066
- **Rutas:** `api-gateway.yml` en [LogiRock/config-server](https://github.com/LogiRock/config-server)

| Ruta | Servicio |
|---|---|
| `/application/auth/**` | SECURITY-SERVICE |
| `/application/couriers/**` | COURIER-SERVICE |
| `/application/shipments/**` | SHIPMENT-SERVICE |

Ejemplo: `POST http://localhost:8066/application/auth/api/auth/login`

Rutas cargadas: http://localhost:8066/actuator/gateway/routes

## Variables de entorno

| Variable | Default | Para qué |
|---|---|---|
| `CONFIG_SERVER_URL` | `http://localhost:8088` | Config-server. En Docker: `http://config-server:8088` |
| `EUREKA_URL` | `http://eureka-server:8065/eureka/` (del config común) | Eureka. Desde el IDE: `http://localhost:8065/eureka/` |
| `PORT` | `8066` | Puerto |

## CORS

El CORS global está en `api-gateway.yml` del config-server (`spring.cloud.gateway.server.webflux.globalcors`).

| Variable | Default | Para qué |
|---|---|---|
| `FRONTEND_ORIGINS` | `http://localhost:5173,http://localhost:3000` | Orígenes del front que pueden llamar al gateway (separados por coma) |

- Métodos: GET, POST, PUT, PATCH, DELETE y OPTIONS. Headers: `Authorization` y `Content-Type`. Preflight cacheado 1 hora.
- El preflight `OPTIONS` lo responde el gateway; no llega a los microservicios.
- Test: `CorsConfigTests` (origen permitido → 200 con header; origen no permitido → 403).