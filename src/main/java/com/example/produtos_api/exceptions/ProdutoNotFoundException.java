package com.example.produtos_api.exceptions;

public class ProdutoNotFoundException extends RuntimeException{

    public ProdutoNotFoundException() {
        super("Nenhum produto encontrado");
    }
    public ProdutoNotFoundException(Long codigo) {
        super("Produto não encontrado com código: " + codigo);
    }

    public ProdutoNotFoundException(String nome) {
        super("Produto não encontrado com nome: " + nome);
    }
}

