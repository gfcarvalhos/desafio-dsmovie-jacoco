# Desafio: DSMovie Jacoco

## Descrição

Este projeto consiste na implementação dos **testes unitários da camada de serviço** do sistema **DSMovie**, uma aplicação de filmes e avaliações de filmes. A cobertura dos testes é medida com o **Jacoco**.

O sistema possui as seguintes características:

- **Consulta de filmes:** a visualização dos dados dos filmes é pública e não exige login.
- **Gerenciamento de filmes:** inserir, atualizar e deletar filmes são operações permitidas apenas para usuários **ADMIN**.
- **Avaliação de filmes:** qualquer usuário logado (**CLIENT** ou **ADMIN**) pode registrar uma nota para um filme.


<img width="889" height="267" alt="image" src="https://github.com/user-attachments/assets/71b35378-b051-47b0-9263-7ff5b1c7e33c" />


## Especificações

### Modelo de domínio

O modelo é composto pelas seguintes entidades:

- **Movie:** id, título, nota média (`score`), contagem de votos (`count`) e imagem;
- **User:** id e email;
- **Score:** armazena a nota (`value`), de 0 a 5, que cada usuário deu a cada filme. Associa um usuário a um filme.

#### Regras

- Sempre que um usuário registra uma nota, o sistema deverá calcular a **média das notas de todos os usuários** para aquele filme.
- A nota média deverá ser armazenada no atributo `score` da entidade `Movie`, juntamente com a **contagem de votos** (`count`).

---

### Testes unitários

Devem ser implementados os testes unitários listados abaixo, organizados por classe de teste.

#### MovieServiceTests

- `findAllShouldReturnPagedMovieDTO`
- `findByIdShouldReturnMovieDTOWhenIdExists`
- `findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist`
- `insertShouldReturnMovieDTO`
- `updateShouldReturnMovieDTOWhenIdExists`
- `updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist`
- `deleteShouldDoNothingWhenIdExists`
- `deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist`
- `deleteShouldThrowDatabaseExceptionWhenDependentId`

#### ScoreServiceTests

- `saveScoreShouldReturnMovieDTO`
- `saveScoreShouldThrowResourceNotFoundExceptionWhenNonExistingMovieId`

#### UserServiceTests

- `authenticatedShouldReturnUserEntityWhenUserExists`
- `authenticatedShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists`
- `loadUserByUsernameShouldReturnUserDetailsWhenUserExists`
- `loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExists`

## Critério de aprovação

- Com todos os testes implementados, o Jacoco deve reportar **pelo menos 90% de cobertura**.
- O mínimo para aprovação no desafio é de **12 dos 15 testes** implementados.

## Tecnologias

Este projeto utiliza tecnologias do ecossistema Java e Spring, com testes unitários da camada de serviço e geração de relatório de cobertura de código com o Jacoco.

## Referência

Desafio proposto pela [DevSuperior](https://devsuperior.com.br/).

---

**Todos os direitos reservados à DevSuperior.**
