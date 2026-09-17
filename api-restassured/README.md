# api-restassured — GeninhoAPP

Base: `F:/Estudos-Automacao/docs/DOCUMENTO-PRINCIPAL.md` §4 RF01/RF02 + §7 pirâmide

## Rodar
```bash
mvn test -DbaseUrl=http://localhost:3000
# ou com mock/default: mvn test
```

## Estrutura
```
src/test/java/com/geninho/
  AlunosTest.java  -> RF01 UC01 (POST/GET/PUT 201/422)
  AuthTest.java    -> RF02 UC02 (POST /auth 200/401)
src/test/resources/
  config.properties
```

## Rastreabilidade
Ver `docs/DOCUMENTO-PRINCIPAL.md:119` §8 — cada teste referencia RF/UC.
