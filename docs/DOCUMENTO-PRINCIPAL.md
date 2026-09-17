# Estudos Automação — Documento Técnico (Lean)

> **Objetivo deste doc:** explicar o que foi feito em `F:/Estudos-Automacao/` para você aprender e reproduzir. Formato inspirado no `GeninhoAPP`, mas conteúdo é só do projeto aqui.

---

## 1. O que foi feito

Scaffold lean com 2 projetos independentes que rodam contra `http://localhost:3000`:

- `api-restassured/` — 4 testes de API (Java, Maven)
- `front-cypress/` — 2 specs E2E (Cypress)
- `docs/DOCUMENTO-PRINCIPAL.md` — este arquivo (único doc vivo)

## 2. Estrutura

```
F:/Estudos-Automacao/
├── api-restassured/
│   ├── pom.xml (Java 17, RestAssured 5.4.0, JUnit 5.10.3)
│   └── src/test/java/com/geninho/
│       ├── AlunosTest.java (3 testes)
│       └── AuthTest.java (1 teste)
│   └── src/test/resources/config.properties (baseUrl, basePath)
├── front-cypress/
│   ├── cypress.config.js (baseUrl)
│   ├── package.json (cypress 13.13.0)
│   └── cypress/e2e/alunos.cy.js (2 its)
└── docs/DOCUMENTO-PRINCIPAL.md
```

## 3. Stack

- **API:** `pom.xml:10` `maven.compiler.source 17`, `rest-assured 5.4.0`, `junit-jupiter 5.10.3`, `hamcrest 2.2`, `maven-surefire 3.2.5`
- **Front:** `package.json:10` `cypress 13.13.0`, `cypress.config.js:5` `baseUrl http://localhost:3000`, `specPattern cypress/e2e/**/*.cy.js`

## 4. Como rodar

**API:**
```bash
cd F:/Estudos-Automacao/api-restassured
mvn test                         # usa config.properties (localhost:3000/api)
mvn test -DbaseUrl=http://localhost:3000 -DbasePath=/api
```

**Front:**
```bash
cd F:/Estudos-Automacao/front-cypress
npm install
npm run cy:open   # interativo
npm run cy:run    # headless
```

## 5. Testes — o que cada um faz

**`AlunosTest.java:48` RF01 UC01:**
- `deveCriarAluno:50` `POST /alunos` payload `payloadAluno(cpf)` com `UUID` + `cpf` random → espera `201|200` + `id notNull`
- `deveRecusarSemNome:64` mesmo payload com `nome=""` → espera `422|400`
- `deveListarAlunos:77` `GET /alunos` → `200` + lista

**`AuthTest.java:33` RF02 UC02:**
- `deveRecusarCredenciaisInvalidas:34` `POST /auth/login` `{"email":"invalido@teste.com","password":"wrong123"}` → `401|400|404`

**`alunos.cy.js:20` RF01 (front):**
- `deve cadastrar aluno com sucesso:20` `cy.visit('/alunos/novo')` + `[data-cy=nome/cpf/email/telefone/btn-salvar]` + `contains(/sucesso|criado|salvo/i)`
- `deve validar CPF duplicado:33` `cpfFixo 12345678901` → `contains(/já existe|duplicado|cpf/i)`
- TODO: ajustar seletores `[data-cy=*]` quando front real existir

## 6. Config

- `api-restassured/src/test/resources/config.properties:1` `baseUrl=http://localhost:3000` `basePath=/api` — lido em `AlunosTest:25` e `AuthTest:23` via `RestAssured.baseURI/basePath`
- `front-cypress/cypress.config.js:5` `baseUrl` — usado em `cy.visit` e `Cypress.config('baseUrl')`

## 7. Pirâmide de testes

| Camada | Cobre | Não repete |
|--------|-------|------------|
| API | 201/422/401/404, validação, contrato | — |
| Front | só feliz + 1 borda (CPF duplicado) | bordas completas ficam na API |

Regra: não repetir teste em camadas (bordas completas só na API).

## 8. Rastreabilidade

| RF | Teste API | Teste Front |
|----|-----------|-------------|
| RF01 | `AlunosTest:50` 201, `AlunosTest:64` 422, `AlunosTest:77` GET | `alunos.cy.js:20` feliz + `33` CPF duplicado |
| RF02 | `AuthTest:33` 401 | — |

Atualize aqui antes de codar novo RF.

## 9. Próximos passos técnicos

- RF03 `POST /planos` (API) — próximo `Alta`
- `cy.intercept` no front para não depender de `localhost:3000`
- `mvn test` verde sem SUT (mock) + `cy:run` headless no CI

---
*Atualizado: 2026-09-17 — só técnica do projeto aqui, sem ciclo faculdade/inspiração.*
