# Estudos Automação — Documento Principal (Lean)

> **Inspiração:** `GeninhoAPP` (`Visão.docx`, `ArquiteturaSoftware.docx`, `ESPECIFICACAO DO CASO DE USO2_modelo.pdf`) serviu como molde de formato lean. Este doc é o **único documento vivo** do projeto `F:/Estudos-Automacao/` — API (RestAssured) + Front (Cypress). Todo novo artefato referencia aqui.

---

## 1. Visão Geral

| Campo | Conteúdo |
|-------|----------|
| **Projeto** | Estudos Automação — ADS 4º sem (Dimi) |
| **Problema** | Aprender pirâmide de testes automatizados sem repetir cobertura entre camadas |
| **Solução** | Framework lean com API RestAssured (Java) + Front Cypress (JS) rodando contra SUT `http://localhost:3000` |
| **Público** | Dimi (estudo), Fe (pirâmide), PressLink como referência de projeto real |
| **MVP** | `api-restassured/` 3 testes (RF01/RF02) + `front-cypress/` 2 specs com seletores `[data-cy]` + docs rastreáveis |

> Vault: `F:/BrainSecondary/agent/BOOT.md:23` + `F:/BrainSecondary/agent/memoria/comandos.md` (pirâmide Fe)

## 2. Escopo MVP vs Futuro

**Dentro do MVP (feito):**
- `api-restassured/src/test/java/com/geninho/AlunosTest.java:48` — POST 201/422, GET 200 (RF01 UC01)
- `api-restassured/src/test/java/com/geninho/AuthTest.java:33` — POST /auth 401 (RF02 UC02)
- `front-cypress/cypress/e2e/alunos.cy.js:20` — cadastro feliz + CPF duplicado (RF01, pirâmide: front só feliz+1 borda)
- `config.properties:1` `baseUrl=http://localhost:3000` `basePath=/api` + `cypress.config.js:5` `baseUrl`

**Fora do MVP (próximos):**
- RF03 Planos (`/planos`), RF04 Matrículas, RF05 Pagamentos, RF06 Treinos (seguir §8)
- Mock desacoplado (`cy.intercept` / WireMock) para front não depender de `localhost:3000`
- CI (`mvn test` + `cy:run` no GitHub Actions)

## 3. Arquitetura & Stack

```
front-cypress/ (Cypress 13.13.0) → API REST (localhost:3000/api) → BD mock
api-restassured/ (Java 17, Maven, RestAssured 5.4.0, JUnit 5.10.3, Hamcrest 2.2)
docs/DOCUMENTO-PRINCIPAL.md (este arquivo)
```

- **Padrão:** Controller → Service → Repository → Entity (SUT, não do framework)
- **Pontos de entrada:** `F:/Estudos-Automacao/api-restassured/pom.xml:10` + `F:/Estudos-Automacao/front-cypress/package.json:10`
- **Base inspirada:** `GeninhoAPP ArquiteturaSoftware.docx` + `UML_class.png` (Aluno, Plano, Matricula)

## 4. Requisitos (lean — foco automação)

### 4.1 Funcionais (SUT de estudo)

| ID | Requisito (SUT) | Prioridade | Teste atual |
|----|-----------------|------------|-------------|
| RF01 | Cadastrar/listar aluno (`POST/GET /alunos`) | Alta | `AlunosTest` 201/422 + `alunos.cy.js` feliz |
| RF02 | Autenticar (`POST /auth/login`) | Alta | `AuthTest` 401 |
| RF03 | Gerenciar planos (`/planos`) | Alta | — próximo |
| RF04 | Matricular (`/matriculas`) | Média | — |
| RF05 | Pagamentos (`/pagamentos`) | Média | — |

### 4.2 Não Funcionais (framework)

| ID | Requisito |
|----|-----------|
| RNF01 | Validações 400/422 com mensagens claras (API cobre borda) |
| RNF02 | Front cobre só feliz + 1 borda (pirâmide Fe) |
| RNF03 | `mvn test -DbaseUrl` e `npm run cy:run` verdes sem `localhost` (mock) |

## 5. Casos de Uso (resumo lean — SUT)

**UC01 — Cadastrar Aluno** — Recepção informa dados → valida → 201/422 — coberto `AlunosTest:50,64` + `alunos.cy.js:20`
**UC02 — Autenticar** — login/senha → 200/401 — coberto `AuthTest:33`

> Novo UC? Adicionar 1 parágrafo aqui antes de automatizar.

## 6. Diagramas

- **GeninhoAPP inspiração:** `docs/assets/` (mover `UML_class.png` e `Diagramas/` do Drive quando precisar)
- **Estudos:** sem diagrama próprio no MVP; usar prints de `cy:open` e relatório `surefire` como evidência

## 7. Estratégia de Testes (pirâmide Fe — lean)

> Ver `F:/BrainSecondary/agent/memoria/comandos.md` — não repetir teste em camadas.

| Camada | O que testa | Ferramenta | Onde |
|--------|-------------|------------|------|
| API | contrato, validação 422/401/404, regra | RestAssured | `api-restassured/` |
| Front | fluxo feliz + 1 borda visual | Cypress | `front-cypress/` |
| Integração | matrícula→pagamento (pontual) | API+BD | futuro |

**Regra:** API cobre bordas; Front só feliz + CPF duplicado.

## 8. Rastreabilidade Lean

| RF | UC | Teste API | Teste Front |
|----|----|-----------|-------------|
| RF01 | UC01 | `AlunosTest:48` POST 201, `AlunosTest:64` 422, `AlunosTest:77` GET | `alunos.cy.js:20` feliz + `alunos.cy.js:33` CPF duplicado |
| RF02 | UC02 | `AuthTest:33` 401 | — (login feliz futuro) |
| RF03 | — | — | — |

Atualizar esta tabela a cada automação.

## 9. Como usar este documento

1. Novo teste? Adicione `RF` em §4 e linha em §8 **antes** de codar
2. Dúvida? Consulte `GeninhoAPP` no Drive, resuma aqui em 1 linha
3. Nada de docs paralelos — este + `api-restassured/README.md:1` + `front-cypress/README.md:1` são os únicos

## 10. Referências

- Molde: Drive GeninhoAPP `https://drive.google.com/drive/folders/1W7JMz4LE3PI6AaauZ1byani--FfFBtzA?usp=sharing` (`Visão.docx`, `ArquiteturaSoftware.docx`, `ESPECIFICACAO DO CASO DE USO2_modelo.pdf`)
- Projeto: `F:/Estudos-Automacao/api-restassured/pom.xml:10` + `F:/Estudos-Automacao/front-cypress/cypress.config.js:5` + `F:/Estudos-Automacao/.vscode/tasks.json:5` (`Estudos launcher Opcao A+`)
- Vault: `F:/BrainSecondary/agent/BOOT.md:23` + `F:/BrainSecondary/agent/memoria/MEMORIA-ATUAL.md:177` (regra melhores opções)

---
*Criado: 2026-09-17 — Estudos Automação lean, inspirado no GeninhoAPP. Atualize a cada entrega.*
