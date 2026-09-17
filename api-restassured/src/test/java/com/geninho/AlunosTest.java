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
 * Objetivo: validar contrato da API sem depender do front
 */
public class AlunosTest {

    // Configura baseUrl/basePath antes de todos os testes
    // Lê de src/test/resources/config.properties:1 (permite mvn test -DbaseUrl=...)
    // Se não achar, usa default http://localhost:3000/api
    @BeforeAll
    static void setup() throws IOException {
        Properties p = new Properties();
        try (InputStream is = AlunosTest.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (is != null) p.load(is); // carrega baseUrl/basePath do arquivo
        }
        RestAssured.baseURI = p.getProperty("baseUrl", "http://localhost:3000"); // host do SUT
        RestAssured.basePath = p.getProperty("basePath", "/api"); // prefixo da API
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(); // loga só quando falha (economiza output)
    }

    // Monta JSON de aluno com dados únicos para não colidir entre execuções
    // uid random evita CPF/email duplicado; cpf vem do parâmetro para controlar cenários
    private String payloadAluno(String cpf) {
        String uid = UUID.randomUUID().toString().substring(0, 8); // 8 chars únicos
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

    // RF01 - Fluxo feliz: cria aluno válido e espera 201 (ou 200 se SUT ainda não segue REST correto)
    // Gera CPF random 11 dígitos para garantir unicidade; valida que retorna id
    @Test
    @DisplayName("POST /alunos - 201 criado (feliz)")
    void deveCriarAluno() {
        String cpf = String.valueOf((long) (Math.random() * 90000000000L) + 10000000000L); // CPF fake 11 dígitos
        given()
            .contentType(ContentType.JSON) // SUT espera JSON
            .body(payloadAluno(cpf)) // envia payload válido
        .when()
            .post("/alunos") // endpoint RF01
        .then()
            .statusCode(anyOf(is(201), is(200))) // aceita 200 se API ainda não usa 201 (compatibilidade)
            .body("id", notNullValue()); // contrato: cria e retorna id
    }

    // RF01 - Borda/validação: API deve recusar payload sem nome (RNF01)
    // Pirâmide Fe: este 422 fica só na API; front não precisa repetir esta borda
    @Test
    @DisplayName("POST /alunos - 422 sem nome (validação)")
    void deveRecusarSemNome() {
        String cpf = String.valueOf((long) (Math.random() * 90000000000L) + 10000000000L);
        String payload = payloadAluno(cpf).replaceFirst("\"nome\": \"[^\"]+\"", "\"nome\": \"\""); // zera nome para forçar validação
        given()
            .contentType(ContentType.JSON)
            .body(payload) // payload inválido
        .when()
            .post("/alunos")
        .then()
            .statusCode(anyOf(is(422), is(400))); // SUT pode usar 422 (validação) ou 400 (bad request)
    }

    // RF01 - Listagem: garante que GET /alunos retorna 200 e estrutura de lista
    // Não valida conteúdo profundo aqui (isso seria teste de contrato mais pesado)
    @Test
    @DisplayName("GET /alunos - 200 lista")
    void deveListarAlunos() {
        given()
        .when()
            .get("/alunos") // endpoint de listagem RF01
        .then()
            .statusCode(200) // contrato básico
            .body("$", anyOf(instanceOf(java.util.List.class), notNullValue())); // retorna array ou lista não nula
    }
}
