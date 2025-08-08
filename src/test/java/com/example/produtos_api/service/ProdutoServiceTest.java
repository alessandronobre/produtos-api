package com.example.produtos_api.service;

import com.example.produtos_api.dto.ProdutoDTO;
import com.example.produtos_api.exceptions.ArquivoException;
import com.example.produtos_api.exceptions.ProdutoDuplicadoException;
import com.example.produtos_api.exceptions.ProdutoNotFoundException;
import com.example.produtos_api.mapper.ProdutoMapper;
import com.example.produtos_api.model.Produto;
import com.example.produtos_api.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ProdutoMapper produtoMapper;

    @InjectMocks
    private ProdutoService produtoService;

    private Produto produto;
    private ProdutoDTO produtoDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        produto = new Produto();
        produto.setCodigo(1L);
        produto.setNome("Notebook");
        produto.setUrlImagem("http://imagem.com/notebook.png");
        produto.setDescricao("Notebook Gamer");
        produto.setPreco(4500.0);
        produto.setClassificacao(4.8);
        produto.setEspecificacoes("16GB RAM, SSD 512GB");

        produtoDTO = new ProdutoDTO(
                1L,
                "Notebook",
                "http://imagem.com/notebook.png",
                "Notebook Gamer",
                4500.0,
                4.8,
                "16GB RAM, SSD 512GB"
        );
    }

    @Test
    void listar_DeveRetornarListaDeProdutos() {
        when(produtoRepository.listar()).thenReturn(List.of(produto));
        when(produtoMapper.toDto(produto)).thenReturn(produtoDTO);

        List<ProdutoDTO> resultado = produtoService.listar();

        assertEquals(1, resultado.size());
        assertEquals("Notebook", resultado.get(0).nome());
        verify(produtoRepository, times(1)).listar();
    }

    @Test
    void listar_QuandoListaVazia_DeveLancarExcecao() {
        when(produtoRepository.listar()).thenReturn(List.of());

        assertThrows(ProdutoNotFoundException.class, () -> produtoService.listar());
    }

    @Test
    void buscarPorCodigo_DeveRetornarProduto() {
        when(produtoRepository.listar()).thenReturn(List.of(produto));
        when(produtoMapper.toDto(produto)).thenReturn(produtoDTO);

        ProdutoDTO resultado = produtoService.buscarPorCodigo(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.codigo());
    }

    @Test
    void buscarPorCodigo_QuandoNaoEncontrado_DeveLancarExcecao() {
        when(produtoRepository.listar()).thenReturn(List.of(produto));

        assertThrows(ProdutoNotFoundException.class, () -> produtoService.buscarPorCodigo(99L));
    }

    @Test
    void buscarPorCodigo_QuandoArquivoException_DevePropagar() {
        when(produtoRepository.listar()).thenThrow(new ArquivoException());

        assertThrows(ArquivoException.class, () -> produtoService.buscarPorCodigo(1L));
    }

    @Test
    void buscarPorNome_DeveRetornarListaDeProdutos() {
        when(produtoRepository.listar()).thenReturn(List.of(produto));
        when(produtoMapper.toDto(produto)).thenReturn(produtoDTO);

        List<ProdutoDTO> resultado = produtoService.buscarPorNome("note");

        assertEquals(1, resultado.size());
    }

    @Test
    void buscarPorNome_QuandoNaoEncontrar_DeveLancarExcecao() {
        when(produtoRepository.listar()).thenReturn(List.of(produto));

        assertThrows(ProdutoNotFoundException.class, () -> produtoService.buscarPorNome("celular"));
    }

    @Test
    void buscarPorNome_QuandoArquivoException_DevePropagar() {
        when(produtoRepository.listar()).thenThrow(new ArquivoException());

        assertThrows(ArquivoException.class, () -> produtoService.buscarPorNome("note"));
    }

    @Test
    void cadastrar_DeveSalvarProduto() {
        when(produtoRepository.listar()).thenReturn(List.of());
        when(produtoMapper.toEntity(produtoDTO)).thenReturn(produto);

        produtoService.cadastrar(produtoDTO);

        verify(produtoRepository, times(1)).salvar(produto);
    }

    @Test
    void cadastrar_QuandoCodigoDuplicado_DeveLancarExcecao() {
        when(produtoRepository.listar()).thenReturn(List.of(produto));

        assertThrows(ProdutoDuplicadoException.class, () -> produtoService.cadastrar(produtoDTO));
        verify(produtoRepository, never()).salvar(any());
    }
}