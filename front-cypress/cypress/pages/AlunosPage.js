/**
 * Page Object — Alunos (front)
 * Por que separar? Audio 11:50 — mesma lógica do Java: 1 classe = 1 responsabilidade.
 * Antes alunos.cy.js:20 tinha seletores espalhados (linguição). Agora página centraliza.
 * Se o front mudar [data-cy=nome] -> [data-testid=nome], muda só aqui, não nos 2 its.
 *
 * Uso: import AlunosPage from '../pages/AlunosPage'; AlunosPage.visitar(); AlunosPage.preencher(aluno)
 */
class AlunosPage {
  // Seletores — única fonte da verdade (facilita manutenção)
  selectors = {
    nome: '[data-cy=nome]',
    cpf: '[data-cy=cpf]',
    email: '[data-cy=email]',
    telefone: '[data-cy=telefone]',
    btnSalvar: '[data-cy=btn-salvar]',
    // mensagens do SUT (ajuste conforme front real)
    msgSucesso: /sucesso|criado|salvo/i,
    msgDuplicado: /já existe|duplicado|cpf/i,
  };

  visitar() {
    cy.visit('/alunos/novo'); // rota do form — cypress.config.js:5 baseUrl resolve
    return this;
  }

  // Preenche todos os campos — recebe objeto gerado pelo helper do spec
  preencher(aluno) {
    cy.get(this.selectors.nome).type(aluno.nome);
    cy.get(this.selectors.cpf).type(aluno.cpf);
    cy.get(this.selectors.email).type(aluno.email);
    cy.get(this.selectors.telefone).type(aluno.telefone);
    return this;
  }

  salvar() {
    cy.get(this.selectors.btnSalvar).click(); // dispara POST /alunos
    return this;
  }

  // Asserts visuais — o que o usuário vê (pirâmide: front valida só UI, não regra 422)
  deveVerSucesso() {
    cy.contains(this.selectors.msgSucesso).should('be.visible');
    return this;
  }

  deveVerErroDuplicado() {
    cy.contains(this.selectors.msgDuplicado).should('be.visible');
    return this;
  }
}

export default new AlunosPage();
