# 📦 produtos-api

API REST para gerenciamento de produtos, desenvolvida em **Java 21** com **Spring Boot 3.2**.  
A API oferece operações de listagem, busca e cadastro de produtos, utilizando um **arquivo JSON local** como persistência.

---

## 🛠️ Estratégia Técnica

### 📌 Stack Tecnológica (Backend)
- **Java 21**
- **Spring Boot 3.2.5**
    - Spring Web (API REST)
    - Spring Test (testes unitários)
- **Maven 3.8.5** (gerenciamento de dependências)
- **Swagger / OpenAPI** (documentação da API)
- **MapStruct** (conversão entre entidades e DTOs)
- **Armazenamento**: Arquivo JSON local
- **Tratamento de erros**: `@ControllerAdvice` + exceções personalizadas

---

## 🎯 Design da API

A API segue o padrão REST, com os seguintes recursos:

| Método | Endpoint        | Descrição                       | Corpo da Requisição |
|--------|-----------------|----------------------------------|---------------------|
| GET    | `/produtos`     | Lista todos os produtos         | -                   |
| GET    | `/produtos/{id}`| Busca produto pelo código        | -                   |
| GET    | `/produtos/nome`| Busca produto pelo nome          | -                   |
| POST   | `/produtos`     | Cadastra um novo produto         | JSON (ProdutoDTO)   |

### 📌 Exemplo de ProdutoDTO (record)

```json
{
  "codigo": 12345,
  "nome": "Smartphone XYZ",
  "urlImagem": "http://example.com/imagens/smartphone-xyz.jpg",
  "descricao": "Um smartphone de última geração com tela infinita e câmera de alta resolução.",
  "preco": 999.99,
  "classificacao": 4.8,
  "especificacoes": "Processador octa-core, 128GB de armazenamento, 8GB de RAM, tela OLED de 6.5 polegadas."
}
```
## 📚 Documentação
A documentação interativa está disponível via Swagger: http://localhost:8080/swagger-ui/index.html


## 🔧 Configuração
Não é necessário banco de dados — o projeto utiliza um arquivo JSON local para persistir dados.
O arquivo é criado automaticamente na primeira execução.

### 📦 Para ver como rodar o projeto, acesse o [run.md](./run.md).