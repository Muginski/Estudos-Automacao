// Config central do Cypress para Estudos Automação
// baseUrl: SUT local (ajuste se subir em outra porta); usado em cy.visit() e Cypress.config('baseUrl')
const { defineConfig } = require('cypress');

module.exports = defineConfig({
  e2e: {
    baseUrl: 'http://localhost:3000', // SUT front — mude para http://localhost:5173 se for Vite, etc.
    specPattern: 'cypress/e2e/**/*.cy.js', // onde ficam os specs (alunos.cy.js)
    supportFile: 'cypress/support/e2e.js', // setup global (comandos custom)
    setupNodeEvents(on, config) {
      return config; // hook para plugins Node (ex: mochawesome)
    },
  },
  video: false, // não grava vídeo no cy:run (economiza disco)
  screenshotOnRunFailure: true, // tira print só quando falha (debug)
});
