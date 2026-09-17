# front-cypress — GeninhoAPP

Base: `F:/Estudos-Automacao/docs/DOCUMENTO-PRINCIPAL.md` §4 RF01 + §7 pirâmide

## Rodar
```bash
npm install
npm run cy:open  # modo interativo
npm run cy:run   # headless
```

BaseUrl: `http://localhost:3000` (ajuste em `cypress.config.js` e `docs/DOCUMENTO-PRINCIPAL.md:38`)

## Specs
- `cypress/e2e/alunos.cy.js` -> RF01 feliz + CPF duplicado (1 borda)

Ajuste seletores `[data-cy=*]` conforme front real.
