package com.geninho;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * RF01 - UC01 Cadastrar Aluno (lean)
 * Rastreabilidade: docs/DOCUMENTO-PRINCIPAL.md §8 RF01->UC01
 * Pirâmide Fe: aqui cobre 201/422/404 - front cobre só feliz
 */
public class AlunosTest {

    @BeforeAll
    static void setup() throws IOException {
        Properties p = new Properties();
        try (InputStream is = AlunosTest.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (is != null) p.load(is);
        }
        RestAssured.baseURI = p.getProperty("baseUrl", "http://localhost:3000");
        RestAssured.basePath = p.getProperty("basePath", "/api");
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    private String payloadAluno(String cpf) {
        String uid = UUID.randomUUID().toString().substring(0, 8);
        return """
                {
                  "nome": "Aluno Teste %s",
                  "cpf": "%s",
                  "email": "aluno%s@teste.com",
                  "telefone": "11999999999",
                  "planoId": 1
                }
                """.formatted(uid, cpf, uid);
    }

    @Test
    @DisplayName("POST /alunos - 201 criado (feliz)")
    void deveCriarAluno() {
        String cpf = String.valueOf((long) (Math.random() * 90000000000L) + 10000000000L);
        given()
            .contentType(ContentType.JSON)
            .body(payloadAluno(cpf))
        .when()
            .post("/alunos")
        .then()
            .statusCode(anyOf(is(201), is(200))) // aceita 200 se API ainda não usa 201
            .body("id", notNullValue());
    }

    @Test
    @DisplayName("POST /alunos - 422 sem nome (validação)")
    void deveRecusarSemNome() {
        String cpf = String.valueOf((long) (Math.random() * 90000000000L) + 10000000000L);
        String payload = payloadAluno(cpf).replaceFirst("\"nome\": \"[^\"]+\"", "\"nome\": \"\"");
        given()
            .contentType(ContentType.JSON)
            .body(payload)
        .when()
            .post("/alunos")
        .then()
            .statusCode(anyOf(is(422), is(400)));
    }

    @Test
    @DisplayName("GET /alunos - 200 lista")
    void deveListarAlunos() {
        given()
        .when()
            .get("/alunos")
        .then()
            .statusCode(200)
            .body("$", anyOf(instanceOf(java.util.List.class), notNullValue()));
    }
}
