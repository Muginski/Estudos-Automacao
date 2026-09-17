package com.geninho.payload;

import java.util.UUID;

/**
 * Factory de payloads — 1 responsabilidade: montar JSONs válidos/inválidos.
 * Por que separar? Audio 11:50 — "orientação a objeto, cria classe com cada responsa".
 * Antes AlunosTest.java:41 gerava payload inline. Agora quem precisa chama AlunosPayload.criar(cpf)
 * Facilita criar cenários novos (sem nome, CPF duplicado) sem duplicar lógica.
 */
public class AlunosPayload {

    private AlunosPayload() {} // utilitária, não instancia

    /**
     * Gera payload válido com dados únicos (uid random evita colisão entre runs)
     * @param cpf 11 dígitos — caller controla para testar duplicidade
     */
    public static String criarValido(String cpf) {
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

    /** CPF fake random 11 dígitos — helper para fluxo feliz */
    public static String cpfRandom() {
        return String.valueOf((long) (Math.random() * 90000000000L) + 10000000000L);
    }

    /** Retorna payload válido mas com nome vazio — para teste de validação 422 */
    public static String criarSemNome(String cpf) {
        return criarValido(cpf).replaceFirst("\"nome\": \"[^\"]+\"", "\"nome\": \"\"");
    }
}
