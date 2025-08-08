package com.example.produtos_api.mapper;

import com.example.produtos_api.dto.ProdutoDTO;
import com.example.produtos_api.model.Produto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {

    ProdutoDTO toDto(Produto produto);

    Produto toEntity(ProdutoDTO dto);
}