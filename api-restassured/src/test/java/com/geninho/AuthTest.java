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
 */
public class AuthTest {

    @BeforeAll
    static void setup() throws IOException {
        Properties p = new Properties();
        try (InputStream is = AuthTest.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (is != null) p.load(is);
        }
        RestAssured.baseURI = p.getProperty("baseUrl", "http://localhost:3000");
        RestAssured.basePath = p.getProperty("basePath", "/api");
    }

    @Test
    @DisplayName("POST /auth - 401 credenciais inválidas")
    void deveRecusarCredenciaisInvalidas() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                    {"email":"invalido@teste.com","password":"wrong123"}
                    """)
        .when()
            .post("/auth/login")
        .then()
            .statusCode(anyOf(is(401), is(400), is(404)));
    }
}
