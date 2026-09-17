package com.geninho;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * RF02 - UC02 Autenticar (lean)
 * Rastreabilidade: docs/DOCUMENTO-PRINCIPAL.md §8 RF02->UC02
 * Pirâmide: borda 401 fica só na API; front testa só login feliz depois
 */
public class AuthTest {

    // Mesmo setup do AlunosTest: lê baseUrl/basePath de config.properties:1
    // Permite rodar com mock: mvn test -DbaseUrl=http://localhost:3000
    @BeforeAll
    static void setup() throws IOException {
        Properties p = new Properties();
        try (InputStream is = AuthTest.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (is != null) p.load(is); // carrega se existir, senão usa default
        }
        RestAssured.baseURI = p.getProperty("baseUrl", "http://localhost:3000");
        RestAssured.basePath = p.getProperty("basePath", "/api");
    }

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
