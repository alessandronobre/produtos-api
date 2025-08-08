package com.example.produtos_api.exceptions;

public class ProdutoDuplicadoException extends RuntimeException {
    public ProdutoDuplicadoException(Long codigo) {
        super("Produto com código " + codigo + " já existe.");
    }
}
