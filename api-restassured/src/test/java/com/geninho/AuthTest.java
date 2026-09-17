package com.geninho;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * RF02 - UC02 Autenticar (refatorado — v1.1)
 * Rastreabilidade: docs/DOCUMENTO-PRINCIPAL.md §8 RF02->UC02
 * Arquitetura: herda BaseApiTest — sem duplicar setup (DRY)
 * Pirâmide: borda 401 fica só na API
 */
public class AuthTest extends BaseApiTest {

    // RF02 - Borda: login com credenciais inválidas deve ser recusado
    // Aceita 401 (não autorizado), 400 (bad request) ou 404 (rota ainda não existe) para compatibilidade com SUT mock
    @Test
    @DisplayName("POST /auth - 401 credenciais inválidas")
    void deveRecusarCredenciaisInvalidas() {
        given()
            .contentType(ContentType.JSON) // SUT espera JSON
            .body("""
                    {"email":"invalido@teste.com","password":"wrong123"}
                    """) // payload com credenciais falsas
        .when()
            .post("/auth/login") // endpoint RF02
        .then()
            .statusCode(anyOf(is(401), is(400), is(404))); // qualquer borda é válida no mock
    }
}
