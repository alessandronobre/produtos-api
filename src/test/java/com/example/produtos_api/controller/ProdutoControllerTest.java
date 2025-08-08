package com.example.produtos_api.controller;

import com.example.produtos_api.dto.ProdutoDTO;
import com.example.produtos_api.exceptions.ArquivoException;
import com.example.produtos_api.exceptions.ProdutoDuplicadoException;
import com.example.produtos_api.exceptions.ProdutoNotFoundException;
import com.example.produtos_api.service.ProdutoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProdutoController.class)
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProdutoService produtoService;

    private ProdutoDTO criarProdutoExemplo() {
        return new ProdutoDTO(
                1L,
                "Notebook Gamer",
                "https://img.com/notebook.jpg",
                "Notebook top",
                4999.99,
                4.7,
                "16GB RAM, SSD 512GB"
        );
    }

    @Test
    @DisplayName("Deve retornar 200 ao listar todos os produtos")
    void deveListarProdutos() throws Exception {
        when(produtoService.listar()).thenReturn(List.of(criarProdutoExemplo()));

        mockMvc.perform(get("/api/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Notebook Gamer"));
    }

    @Test
    @DisplayName("Deve retornar 404 se a lista de produtos for vazia")
    void deveRetornar404SeListaProdutosVazia() throws Exception {
        Mockito.doThrow(new ProdutoNotFoundException())
                .when(produtoService).listar();

        mockMvc.perform(get("/api/produtos"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Nenhum produto encontrado"));
    }

    @Test
    @DisplayName("Deve retornar 500 se houver erro de arquivo ao listar produtos")
    void deveRetornar500SeHouverErroArquivoAoListarProdutos() throws Exception {
        Mockito.doThrow(new ArquivoException())
                .when(produtoService).listar();

        mockMvc.perform(get("/api/produtos"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Deve retornar 200 ao buscar produto por código")
    void deveBuscarProdutoPorCodigo() throws Exception {
        when(produtoService.buscarPorCodigo(1L)).thenReturn(criarProdutoExemplo());

        mockMvc.perform(get("/api/produtos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value(1));
    }

    @Test
    @DisplayName("Deve retornar 404 se busca produto por codigo retornar vazio")
    void deveRetornar404SeBuscaProdutoPorCodigoRetornarVazio() throws Exception {
        Mockito.doThrow(new ProdutoNotFoundException(1L))
                .when(produtoService).buscarPorCodigo(1L);

        mockMvc.perform(get("/api/produtos/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Produto não encontrado com código: 1"));
    }

    @Test
    @DisplayName("Deve retornar 500 se busca produto por codigo se houver erro de arquivo")
    void deveRetornar500SeHouverErroArquivoAoBuscarProdutoPorCodigo() throws Exception {
        Mockito.doThrow(new ArquivoException())
                .when(produtoService).buscarPorCodigo(1L);

        mockMvc.perform(get("/api/produtos/1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Deve buscar produtos por nome com sucesso")
    void deveBuscarProdutosPorNome() throws Exception {
        when(produtoService.buscarPorNome("Notebook")).thenReturn(List.of(criarProdutoExemplo()));

        mockMvc.perform(get("/api/produtos/nome")
                        .param("nome", "Notebook"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Notebook Gamer"));
    }

    @Test
    @DisplayName("Deve retornar 404 se busca produto por nome retornar vazio")
    void deveRetornar404SeBuscaProdutoPorNomeRetornarVazio() throws Exception {
        Mockito.doThrow(new ProdutoNotFoundException("Notebook"))
                .when(produtoService).buscarPorNome("Notebook");

        mockMvc.perform(get("/api/produtos/nome")
                        .param("nome", "Notebook"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Produto não encontrado com nome: Notebook"));
    }

    @Test
    @DisplayName("Deve retornar 500 se busca produto por nome se houver erro de arquivo")
    void deveRetornar500SeHouverErroArquivoAoBuscarProdutoPorNome() throws Exception {
        Mockito.doThrow(new ArquivoException())
                .when(produtoService).buscarPorNome("Notebook");

        mockMvc.perform(get("/api/produtos/nome")
                        .param("nome", "Notebook"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Deve retornar 201 ao cadastrar novo produto")
    void deveCadastrarProduto() throws Exception {

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "codigo": 1,
                                    "nome": "Notebook Gamer",
                                    "urlImagem": "https://img.com/notebook.jpg",
                                    "descricao": "Notebook top",
                                    "preco": 4999.99,
                                    "classificacao": 4.7,
                                    "especificacoes": "16GB RAM, SSD 512GB"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/produtos/1"));
    }

    @Test
    @DisplayName("Deve retornar 409 ao cadastrar produto e ja existir um produto igual")
    void deveRetornar409AoCadastrarProduto() throws Exception {
        Mockito.doThrow(new ProdutoDuplicadoException(1L))
                .when(produtoService).cadastrar(Mockito.any(ProdutoDTO.class));

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "codigo": 1,
                            "nome": "Notebook Gamer",
                            "urlImagem": "https://img.com/notebook.jpg",
                            "descricao": "Notebook top",
                            "preco": 4999.99,
                            "classificacao": 4.7,
                            "especificacoes": "16GB RAM, SSD 512GB"
                        }
                        """))
                .andExpect(status().isConflict())
                .andExpect(content().string("Produto com código 1 já existe."));
    }

    @Test
    @DisplayName("Deve retornar 500 ao cadastrar produto e houver erro de arquivo")
    void deveRetornar500AoCadastrarProduto() throws Exception {
        Mockito.doThrow(new ArquivoException())
                .when(produtoService).cadastrar(Mockito.any(ProdutoDTO.class));

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "codigo": 1,
                            "nome": "Notebook Gamer",
                            "urlImagem": "https://img.com/notebook.jpg",
                            "descricao": "Notebook top",
                            "preco": 4999.99,
                            "classificacao": 4.7,
                            "especificacoes": "16GB RAM, SSD 512GB"
                        }
                        """))
                .andExpect(status().isInternalServerError());
    }
}
