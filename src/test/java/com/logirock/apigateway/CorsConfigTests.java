package com.logirock.apigateway;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Verifica el CORS global del gateway con el preflight (OPTIONS) que manda el navegador.
 * La configuración es la misma que vive en api-gateway.yml del config-server.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = {
        "spring.cloud.config.enabled=false",
        "eureka.client.enabled=false",
        "spring.cloud.gateway.server.webflux.globalcors.add-to-simple-url-handler-mapping=true",
        "spring.cloud.gateway.server.webflux.globalcors.cors-configurations.[/**].allowed-origins[0]=http://localhost:5173",
        "spring.cloud.gateway.server.webflux.globalcors.cors-configurations.[/**].allowed-origins[1]=http://localhost:3000",
        "spring.cloud.gateway.server.webflux.globalcors.cors-configurations.[/**].allowed-methods[0]=GET",
        "spring.cloud.gateway.server.webflux.globalcors.cors-configurations.[/**].allowed-methods[1]=POST",
        "spring.cloud.gateway.server.webflux.globalcors.cors-configurations.[/**].allowed-methods[2]=PUT",
        "spring.cloud.gateway.server.webflux.globalcors.cors-configurations.[/**].allowed-methods[3]=PATCH",
        "spring.cloud.gateway.server.webflux.globalcors.cors-configurations.[/**].allowed-methods[4]=DELETE",
        "spring.cloud.gateway.server.webflux.globalcors.cors-configurations.[/**].allowed-methods[5]=OPTIONS",
        "spring.cloud.gateway.server.webflux.globalcors.cors-configurations.[/**].allowed-headers[0]=Authorization",
        "spring.cloud.gateway.server.webflux.globalcors.cors-configurations.[/**].allowed-headers[1]=Content-Type",
        "spring.cloud.gateway.server.webflux.globalcors.cors-configurations.[/**].allow-credentials=true",
        "spring.cloud.gateway.server.webflux.globalcors.cors-configurations.[/**].max-age=3600"
})
class CorsConfigTests {

    @LocalServerPort
    int port;

    WebTestClient client;

    @BeforeEach
    void setUp() {
        client = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
    }

    @Test
    void preflightDesdeOrigenPermitidoDevuelve200ConElHeader() {
        client.options().uri("/application/shipments/api/envios")
                .header(HttpHeaders.ORIGIN, "http://localhost:5173")
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST")
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "authorization,content-type")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:5173")
                .expectHeader().valueEquals(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
    }

    @Test
    @SuppressWarnings("HttpUrlsUsage")
    void preflightDesdeOrigenNoPermitidoNoRecibeElHeader() {
        client.options().uri("/application/shipments/api/envios")
                .header(HttpHeaders.ORIGIN, "http://sitio-malicioso.com")
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST")
                .exchange()
                .expectStatus().isForbidden()
                .expectHeader().doesNotExist(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN);
    }
}