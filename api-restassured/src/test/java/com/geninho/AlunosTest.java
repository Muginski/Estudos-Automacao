package com.geninho;

import com.geninho.payload.AlunosPayload;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * RF01 - UC01 Cadastrar Aluno (refatorado — v1.1)
 * Rastreabilidade: docs/DOCUMENTO-PRINCIPAL.md §8 RF01->UC01
 * Arquitetura: herda BaseApiTest (config) + usa AlunosPayload (factory) — sem "linguição"
 * Pirâmide de testes: API cobre 201/422 — front cobre só feliz
 */
public class AlunosTest extends BaseApiTest {

    // RF01 - Fluxo feliz: cria aluno válido e espera 201 (ou 200 compatibilidade)
    // Agora delega geração de payload para AlunosPayload — 1 responsabilidade por classe
    @Test
    @DisplayName("POST /alunos - 201 criado (feliz)")
    void deveCriarAluno() {
        String cpf = AlunosPayload.cpfRandom(); // factory gera CPF único
        given()
            .contentType(ContentType.JSON)
            .body(AlunosPayload.criarValido(cpf)) // factory monta JSON válido
        .when()
            .post("/alunos")
        .then()
            .statusCode(anyOf(is(201), is(200)))
            .body("id", notNullValue());
    }

    // RF01 - Borda: API deve recusar sem nome — 422 fica só na API (pirâmide)
    @Test
    @DisplayName("POST /alunos - 422 sem nome (validação)")
    void deveRecusarSemNome() {
        String cpf = AlunosPayload.cpfRandom();
        String payload = AlunosPayload.criarSemNome(cpf); // factory já retorna caso inválido
        given()
            .contentType(ContentType.JSON)
            .body(payload)
        .when()
            .post("/alunos")
        .then()
            .statusCode(anyOf(is(422), is(400)));
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
