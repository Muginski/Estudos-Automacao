# GeninhoAPP — Documento Principal (Lean)

> **Fonte base:** Drive [GeninhoAPP - Software para Academia](https://drive.google.com/drive/folders/1W7JMz4LE3PI6AaauZ1byani--FfFBtzA?usp=sharing) — `Visão.docx` + `GeninhoAPP ArquiteturaSoftware.docx` + `ESPECIFICACAO DO CASO DE USO2_modelo.pdf` + `Documento_Projeto_Academia_Geninho.docx.pdf` + `UML_class.png` + `Diagramas/` + `Link LucidChart.docx` + `Academia.pdf`
> **Objetivo deste doc:** versão única e enxuta (sem matriz completa) para guiar todo o projeto `F:/Estudos-Automacao/` — API (RestAssured) + Front (Cypress). Este é o **único documento vivo** — todo novo artefato referencia aqui.

---

## 1. Visão Geral

| Campo | Conteúdo |
|-------|----------|
| **Projeto** | GeninhoAPP — Sistema de gestão para academia |
| **Problema** | Controle manual de alunos, treinos, pagamentos e frequência |
| **Solução** | App web para cadastro de alunos, planos, treinos, check-in e financeiro |
| **Público** | Recepção, instrutores, alunos, dono |
| **MVP** | CRUD alunos + planos + autenticação + matrícula/pagamento básico |

Origem: `Visão.docx` (detalhes completos no Drive)

## 2. Escopo MVP vs Futuro

**Dentro do MVP:**
- Autenticação (recepção/instrutor)
- Cadastro e gestão de alunos
- Planos e matrículas
- Treinos / ficha
- Check-in / frequência (básico)
- Pagamentos (registro)

**Fora do MVP (futuro):**
- App mobile aluno, biometria, dashboard BI, integração pagamento online

## 3. Arquitetura & Stack

**Origem:** `GeninhoAPP ArquiteturaSoftware.docx` + `Diagramas/` + `UML_class.png` + `Link LucidChart.docx`

```
Front (Cypress) → API REST → BD
   front-cypress/   api-restassured/   (Postgres/MySQL - a definir)
```

- **API:** REST Assured (Java) — `F:/Estudos-Automacao/api-restassured/`
- **Front:** Cypress (JS/TS) — `F:/Estudos-Automacao/front-cypress/`
- **Padrão camadas:** Controller → Service → Repository → Entity
- **Diagramas:** UML_class.png (classes Aluno, Plano, Matricula, Treino, Pagamento) + LucidChart (link no Drive)

> TODO: colar link LucidChart aqui quando liberar + exportar UML_class.png para `docs/assets/`

## 4. Requisitos (versão lean — sem matriz pesada)

> Base: `Matriz_requisitos_GeninhoAPP.xlsx` + `Documento_Projeto_Academia_Geninho.docx.pdf` — aqui apenas lista enxuta.

### 4.1 Funcionais

| ID | Requisito | Prioridade |
|----|-----------|------------|
| RF01 | Cadastrar / editar / inativar aluno | Alta |
| RF02 | Autenticar usuário (login/logout) | Alta |
| RF03 | Gerenciar planos (criar, editar, listar) | Alta |
| RF04 | Matricular aluno em plano | Alta |
| RF05 | Lançar pagamento / mensalidade | Média |
| RF06 | Criar ficha de treino para aluno | Média |
| RF07 | Registrar frequência / check-in | Média |
| RF08 | Listar / buscar alunos e matrículas | Média |

### 4.2 Não Funcionais

| ID | Requisito |
|----|-----------|
| RNF01 | Validações de formulário (Zod-like) + mensagens claras |
| RNF02 | Tempo resposta API < 500ms (p95) |
| RNF03 | Logs e tratamento de erros padronizado |
| RNF04 | Testes automatizados cobrindo fluxo crítico |

## 5. Casos de Uso Principais (resumo lean)

> Template completo em `ESPECIFICACAO DO CASO DE USO2_modelo.pdf` — aqui só fluxo principal.

**UC01 — Cadastrar Aluno**
- Ator: Recepção
- Pré: autenticado
- Fluxo: informa dados → valida → salva → confirma
- Alt: CPF já existe, campo obrigatório faltando

**UC02 — Autenticar**
- Ator: Qualquer usuário
- Fluxo: login/senha → valida → gera sessão

**UC03 — Matricular em Plano**
- Pré: aluno + plano existem
- Fluxo: seleciona aluno/plano → confirma → gera matrícula ativa

**UC04 — Lançar Pagamento**
- Pré: matrícula ativa
- Fluxo: informa valor/mês → registra → atualiza status

> Detalhar só quando for automatizar — manter 1 parágrafo por UC.

## 6. Diagramas

- **Classes:** `UML_class.png` → mover para `docs/assets/UML_class.png`
- **Outros:** pasta `Diagramas/` no Drive (importar os relevantes)
- **LucidChart:** link em `Link LucidChart.docx` → colar aqui: `TODO_LUCID_URL`

## 7. Estratégia de Testes (lean — pirâmide do Fe)

> Ver `agent/memoria/comandos.md` — não repetir teste em camadas.

| Camada | O que testa | Ferramenta | Onde |
|--------|-------------|------------|------|
| API | contrato, validação, regra, 401/404/422 | RestAssured | `api-restassured/` |
| Front | fluxo feliz + validação visual | Cypress | `front-cypress/` |
| Integração pontual | matrícula→pagamento | API + BD | - |

**Regra:** API cobre borda/erro; Front cobre só caminho feliz + 1 borda.

## 8. Rastreabilidade Lean (sem matriz gigante)

| RF | Caso Uso | Teste API | Teste Front |
|----|----------|-----------|-------------|
| RF01 | UC01 | POST /alunos 201/422, GET, PUT | cadastro feliz + CPF duplicado |
| RF02 | UC02 | POST /auth 200/401 | login feliz + senha inválida |
| RF03 | — | CRUD /planos | — |
| RF04 | UC03 | POST /matriculas | fluxo matrícula |
| RF05 | UC04 | POST /pagamentos | — |

Atualizar esta tabela a cada automação — é a única rastreabilidade necessária.

## 9. Como usar este documento

1. **Novo teste/feature?** Adicione linha em §4/§8 e atualize aqui primeiro
2. **Dúvida de regra?** Consulte Drive original, depois volte e resuma aqui em 1 linha
3. **Nada de docs paralelos** — este é o central; `docs/` só tem este + assets

## 10. Referências

- Drive GeninhoAPP: https://drive.google.com/drive/folders/1W7JMz4LE3PI6AaauZ1byani--FfFBtzA?usp=sharing
- Arquivos: `Academia.pdf`, `Documento_Projeto_Academia_Geninho.docx.pdf`, `GeninhoAPP ArquiteturaSoftware.docx`, `ESPECIFICACAO DO CASO DE USO2_modelo.pdf`, `Matriz_requisitos_GeninhoAPP.xlsx`
- Vault: `F:/BrainSecondary/agent/BOOT.md` + `F:/BrainSecondary/02-projetos/PressLink.md` (modelo de hub, mas agora foco é GeninhoAPP)

---

*Criado: 2026-09-17 — lean, sem matrix. Atualize este arquivo a cada entrega em `F:/Estudos-Automacao/`.*
