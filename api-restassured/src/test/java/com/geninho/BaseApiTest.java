package com.geninho;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Base para todos os testes de API — 1 responsabilidade só: configurar RestAssured.
 * Por que separar? Audio 11:50 — "não faz linguição, separa por classe".
 * Antes AlunosTest.java:29 fazia setup duplicado em cada classe (AlunosTest + AuthTest).
 * Agora 1 lugar só -> DRY, se mudar baseUrl muda aqui e todas herdam.
 *
 * Como funciona: lê src/test/resources/config.properties:1
 * - mvn test (default localhost:3000/api)
 * - mvn test -DbaseUrl=http://... -DbasePath=/api (CI/esteira sobrescreve)
 */
public abstract class BaseApiTest {

    @BeforeAll
    static void setupBase() throws IOException {
        Properties p = new Properties();
        // ClassLoader busca no classpath (src/test/resources)
        try (InputStream is = BaseApiTest.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (is != null) p.load(is);
        }
        // System.getProperty permite -DbaseUrl da esteira ter prioridade sobre arquivo
        String baseUrl = System.getProperty("baseUrl", p.getProperty("baseUrl", "http://localhost:3000"));
        String basePath = System.getProperty("basePath", p.getProperty("basePath", "/api"));

        RestAssured.baseURI = baseUrl;  // ex: http://localhost:3000
        RestAssured.basePath = basePath; // ex: /api -> POST /api/alunos
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}
