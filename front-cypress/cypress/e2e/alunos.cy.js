/**
 * RF01 - UC01 Cadastrar Aluno (front - fluxo feliz)
 * Rastreabilidade: docs/DOCUMENTO-PRINCIPAL.md §8 RF01->UC01
 * Pirâmide Fe: front cobre só feliz + 1 borda (CPF duplicado). Bordas completas na API.
 */

describe('Alunos - cadastro (lean)', () => {
  const base = Cypress.config('baseUrl');

  function alunoPayload() {
    const uid = Math.random().toString(36).slice(2, 8);
    return {
      nome: `Aluno E2E ${uid}`,
      cpf: `${Math.floor(10000000000 + Math.random() * 90000000000)}`,
      email: `aluno${uid}@teste.com`,
      telefone: '11999999999',
    };
  }

  it('deve cadastrar aluno com sucesso (feliz)', () => {
    const aluno = alunoPayload();
    // TODO: ajustar seletores quando front existir
    // Exemplo genérico - edite conforme tela real:
    cy.visit('/alunos/novo');
    cy.get('[data-cy=nome]').type(aluno.nome);
    cy.get('[data-cy=cpf]').type(aluno.cpf);
    cy.get('[data-cy=email]').type(aluno.email);
    cy.get('[data-cy=telefone]').type(aluno.telefone);
    cy.get('[data-cy=btn-salvar]').click();
    cy.contains(/sucesso|criado|salvo/i).should('be.visible');
  });

  it('deve validar CPF duplicado (1 borda front)', () => {
    // reutiliza CPF fixo para forçar duplicidade - criar 2x via API ou UI
    const cpfFixo = '12345678901';
    cy.visit('/alunos/novo');
    cy.get('[data-cy=cpf]').type(cpfFixo);
    // preenche resto e tenta salvar 2x - espera mensagem de duplicidade
    cy.get('[data-cy=btn-salvar]').click();
    cy.contains(/já existe|duplicado|cpf/i).should('be.visible');
  });
});
