package com.example.produtos_api.dto;

public record ProdutoDTO(
        Long codigo,
        String nome,
        String urlImagem,
        String descricao,
        double preco,
        double classificacao,
        String especificacoes
) {}
