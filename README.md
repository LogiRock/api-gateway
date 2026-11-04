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