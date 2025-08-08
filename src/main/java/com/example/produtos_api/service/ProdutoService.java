package com.example.produtos_api.service;

import com.example.produtos_api.dto.ProdutoDTO;
import com.example.produtos_api.exceptions.ArquivoException;
import com.example.produtos_api.exceptions.ProdutoDuplicadoException;
import com.example.produtos_api.exceptions.ProdutoNotFoundException;
import com.example.produtos_api.mapper.ProdutoMapper;
import com.example.produtos_api.model.Produto;
import com.example.produtos_api.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ProdutoMapper produtoMapper;

    public List<ProdutoDTO> listar() {
        var produtos = produtoRepository.listar()
                .stream()
                .map(produtoMapper::toDto)
                .toList();

        if (produtos.isEmpty()) throw new ProdutoNotFoundException();
        return produtos;
    }

    public ProdutoDTO buscarPorCodigo(Long codigo) {
        try {
            return produtoRepository.listar()
                    .stream()
                    .filter(produto -> produto.getCodigo().equals(codigo))
                    .findFirst()
                    .map(produtoMapper::toDto)
                    .orElseThrow(() -> new ProdutoNotFoundException(codigo));
        }catch (ArquivoException e) {
            throw new ArquivoException("Erro interno ao buscar produto por codigo. Tente novamente mais tarde.", e);
        }
    }

    public List<ProdutoDTO> buscarPorNome(String nome) {
        try {
            var produtos = produtoRepository.listar().stream()
                    .filter(p -> p.getNome().toLowerCase().contains(nome.toLowerCase()))
                    .map(produtoMapper::toDto)
                    .toList();

            if (produtos.isEmpty()) throw new ProdutoNotFoundException(nome);
            return produtos;

        }catch (ArquivoException e) {
            throw new ArquivoException("Erro interno ao buscar produtos por nome. Tente novamente mais tarde.", e);
        }
    }

    public void cadastrar(ProdutoDTO produto) {
        boolean jaExiste = produtoRepository.listar().stream()
                .anyMatch(p -> Objects.equals(p.getCodigo(), produto.codigo()));

        if (jaExiste) {
            throw new ProdutoDuplicadoException(produto.codigo());
        }
        produtoRepository.salvar(produtoMapper.toEntity(produto));
    }

}
