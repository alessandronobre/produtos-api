package com.example.produtos_api.exceptions;

public class ArquivoException extends RuntimeException{

    public ArquivoException() {
        super();
    }
    public ArquivoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}

