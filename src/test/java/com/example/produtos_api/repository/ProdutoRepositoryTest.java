package com.example.produtos_api.repository;

import com.example.produtos_api.model.Produto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ProdutoRepositoryTest {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final String CAMINHO_BANCO_TESTE = "src/test/resources/data/produtos_test.json";

    private Produto produto;

    @BeforeEach
    void setUp() throws Exception {
        File file = new File(CAMINHO_BANCO_TESTE);
        file.getParentFile().mkdirs();
        objectMapper.writeValue(file, new ArrayList<>());

        produto = new Produto();
        produto.setCodigo(1L);
        produto.setNome("Produto Teste");
        produto.setDescricao("Descrição Teste");
        produto.setPreco(100.0);
        produto.setClassificacao(4.5);
        produto.setUrlImagem("https://imagem.com");
        produto.setEspecificacoes("Especificações Teste");
    }

    @Test
    void listar_DeveRetornarListaVazia_QuandoArquivoEstiverVazio() {
        List<Produto> produtos = produtoRepository.listar();

        assertNotNull(produtos);
        assertTrue(produtos.isEmpty(), "A lista deve estar vazia se o JSON estiver vazio");
    }

    @Test
    void salvar_DevePersistirProdutoNoArquivo() {
        produtoRepository.salvar(produto);

        List<Produto> produtos = produtoRepository.listar();

        assertNotNull(produtos);
        assertEquals(1, produtos.size());

        Produto salvo = produtos.get(0);
        assertEquals(produto.getCodigo(), salvo.getCodigo());
        assertEquals(produto.getNome(), salvo.getNome());
        assertEquals(produto.getDescricao(), salvo.getDescricao());
    }

    @Test
    void salvar_DevePermitirSalvarMultiplosProdutos() {
        Produto outroProduto = new Produto();
        outroProduto.setCodigo(2L);
        outroProduto.setNome("Outro Produto");

        produtoRepository.salvar(produto);
        produtoRepository.salvar(outroProduto);

        List<Produto> produtos = produtoRepository.listar();
        assertEquals(2, produtos.size());
    }
}