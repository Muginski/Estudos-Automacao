# Estudos-Automação

> Repositório didático para aprender automação de testes na prática — API com RestAssured + Front com Cypress + Esteira CI no GitHub Actions.

**Objetivo:** reproduzir de forma enxuta o ciclo de automação visto no GeninhoAPP, com pirâmide de testes, DRY e pipeline com gate. Sem SUT real o projeto já roda (modo estudo) e evolui para testes integrados quando houver backend.

---

## Para que serve

- **Estudar automação** em 2 camadas: API (contrato/validação) e Front (fluxo usuário)
- **Entender pirâmide de testes:** não repetir bordas — cobertura completa na API, só feliz + 1 borda no Front
- **Praticar esteira CI/CD:** cada `push`/`PR` roda `api` + `front` + `gate` e bloqueia `merge` se falhar (branch protection)
- **Base reaplicável:** padrões `BaseApiTest` + `Payload Factory` + `Page Object` servem para qualquer projeto (PressLink, CMH, etc.)

---

## Stack

| Camada | Tecnologia | Versão |
|--------|------------|--------|
| API | Java 17, RestAssured 5.4.0, JUnit 5.10.3, Hamcrest 2.2 | `api-restassured/pom.xml:10` |
| Front | Cypress 13.17.0 | `front-cypress/package.json:11` |
| CI | GitHub Actions `esteira` (3 jobs + gate) | `.github/workflows/ci.yml:6` |

---

## Estrutura

```
.
├── api-restassured/          # 4 testes API
│   ├── pom.xml
│   └── src/test/java/com/geninho/
│       ├── BaseApiTest.java      # config central RestAssured (DRY)
│       ├── payload/AlunosPayload.java # factory de payloads
│       ├── AlunosTest.java       # RF01: POST 201, 422, GET 200
│       └── AuthTest.java         # RF02: POST /auth 401
│   └── src/test/resources/config.properties # baseUrl/basePath
├── front-cypress/            # 2 specs E2E (Page Object)
│   ├── cypress.config.js     # baseUrl http://localhost:3000
│   ├── cypress/e2e/alunos.cy.js
│   ├── cypress/pages/AlunosPage.js
│   └── cypress/support/e2e.js
├── .github/workflows/ci.yml  # esteira api + front + gate
└── docs/DOCUMENTO-PRINCIPAL.md # doc técnico detalhado
```

---

## Como rodar

**Pré-requisitos:** Java 17, Node 24, SUT opcional em `http://localhost:3000`

**API (sem SUT — modo estudo):**
```bash
cd api-restassured
mvn test-compile          # só compila, verde sem SUT (CI usa este)
# com SUT real:
mvn test -DbaseUrl=http://localhost:3000 -DbasePath=/api
```

**Front (sem SUT — modo estudo):**
```bash
cd front-cypress
npm ci                    # requer package-lock.json commitado
npx cypress verify        # só valida instalação (CI usa este)
# com SUT real:
npm run cy:open           # interativo
npm run cy:run            # headless
```

**Config:** `api-restassured/src/test/resources/config.properties:1` e `front-cypress/cypress.config.js:7` — troque `baseUrl` se o SUT estiver em outra porta.

---

## Pirâmide de testes

| Camada | O que cobre | O que NÃO repete |
|--------|-------------|------------------|
| API | 201/422/401/404, validações, contrato | — |
| Front | feliz + 1 borda (CPF duplicado) | bordas completas ficam na API |

Regra: `BaseApiTest` + `AlunosPayload` + `AlunosPage` seguem DRY — 1 responsabilidade por classe (áudio 11:50 "não faz linguição").

---

## Esteira CI

`.github/workflows/ci.yml:6` — 3 jobs:

- `api` → `mvn test-compile` (sem SUT) ou `mvn test` (com SUT)
- `front` → `npx cypress verify` (sem SUT) ou `npm run cy:run` (com SUT)
- `gate` → `needs: [api, front]` bloqueia merge se algum falhar

Proteção ativa em `Settings → Branches → master` com `Required status check: gate`. Histórico: `PR #1` fix lockfile + `PR #2` bump Node 24 deixaram a esteira verde sem warnings.

---

## Rastreabilidade

| RF | API | Front |
|----|-----|-------|
| RF01 Alunos | `AlunosTest:23` 201, `:38` 422, `:54` GET | `alunos.cy.js:21` feliz, `:27` CPF duplicado |
| RF02 Auth | `AuthTest:22` 401 | — |

Próximo: RF03 `POST /planos` seguindo mesmo padrão DRY.

---

## Docs

Detalhe completo em `docs/DOCUMENTO-PRINCIPAL.md` — único doc vivo (stack, testes linha-a-linha, config e próximos passos).

*Licença: estudo — use como template para qualquer projeto.*
