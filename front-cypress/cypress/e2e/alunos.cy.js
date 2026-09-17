/**
 * RF01 - UC01 Cadastrar Aluno (front — refatorado v1.1 Page Object)
 * Rastreabilidade: docs/DOCUMENTO-PRINCIPAL.md §8 RF01->UC01
 * Arquitetura: spec só orquestra; Page Object cuida de seletores/ações — sem "linguição"
 * Pirâmide de testes: front feliz + 1 borda, resto na API AlunosTest:64
 */
import AlunosPage from '../pages/AlunosPage';

describe('Alunos - cadastro (Page Object)', () => {
  // Helper ainda no spec — poderia ir para fixtures, mas mantido simples pra estudar
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
    // Fluxo legível: visitar -> preencher -> salvar -> assert (cada método 1 responsa)
    AlunosPage.visitar().preencher(aluno).salvar().deveVerSucesso();
  });

  it('deve validar CPF duplicado (1 borda front)', () => {
    const cpfFixo = '12345678901'; // fixo para forçar colisão
    // Page Object esconde seletores — se [data-cy] mudar, muda só em AlunosPage.js
    AlunosPage.visitar();
    cy.get(AlunosPage.selectors.cpf).type(cpfFixo);
    AlunosPage.salvar().deveVerErroDuplicado();
  });
});
