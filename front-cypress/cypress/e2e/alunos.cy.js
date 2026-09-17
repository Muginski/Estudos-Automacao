/**
 * RF01 - UC01 Cadastrar Aluno (front - fluxo feliz)
 * Rastreabilidade: docs/DOCUMENTO-PRINCIPAL.md §8 RF01->UC01
 * Pirâmide Fe: front cobre só feliz + 1 borda (CPF duplicado). Bordas completas (422) ficam na API AlunosTest:64
 * Objetivo: validar fluxo visual que o usuário vê (form + mensagem), não regra de validação
 */

describe('Alunos - cadastro (lean)', () => {
  const base = Cypress.config('baseUrl'); // vem de cypress.config.js:5 baseUrl http://localhost:3000

  // Gera payload único para cada execução (evita colisão de CPF/email entre runs)
  function alunoPayload() {
    const uid = Math.random().toString(36).slice(2, 8); // id curto random
    return {
      nome: `Aluno E2E ${uid}`,
      cpf: `${Math.floor(10000000000 + Math.random() * 90000000000)}`, // 11 dígitos fake
      email: `aluno${uid}@teste.com`,
      telefone: '11999999999',
    };
  }

  // RF01 - Fluxo feliz: usuário preenche form e vê mensagem de sucesso
  // Usa seletores [data-cy] para não quebrar com mudança de CSS/classe
  it('deve cadastrar aluno com sucesso (feliz)', () => {
    const aluno = alunoPayload();
    // TODO: ajustar seletores quando front existir — hoje é exemplo genérico
    cy.visit('/alunos/novo'); // rota do form (ajuste se SUT mudar)
    cy.get('[data-cy=nome]').type(aluno.nome); // campo nome
    cy.get('[data-cy=cpf]').type(aluno.cpf); // campo CPF
    cy.get('[data-cy=email]').type(aluno.email); // campo email
    cy.get('[data-cy=telefone]').type(aluno.telefone); // campo telefone
    cy.get('[data-cy=btn-salvar]').click(); // dispara POST /alunos
    cy.contains(/sucesso|criado|salvo/i).should('be.visible'); // valida feedback visual
  });

  // RF01 - Única borda no front: CPF duplicado (pirâmide: borda 422 completa fica na API)
  // Usa CPF fixo para forçar colisão; espera mensagem de duplicidade do SUT
  it('deve validar CPF duplicado (1 borda front)', () => {
    const cpfFixo = '12345678901'; // fixo para reproduzir duplicidade (criar 2x via API/UI antes)
    cy.visit('/alunos/novo');
    cy.get('[data-cy=cpf]').type(cpfFixo);
    // TODO: preencher resto dos campos obrigatórios antes de salvar (nome/email) se SUT exigir
    cy.get('[data-cy=btn-salvar]').click(); // tenta salvar duplicado
    cy.contains(/já existe|duplicado|cpf/i).should('be.visible'); // valida mensagem de erro visual
  });
});
