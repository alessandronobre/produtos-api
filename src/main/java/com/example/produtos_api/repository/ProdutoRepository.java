package com.example.produtos_api.repository;

import com.example.produtos_api.exceptions.ArquivoException;
import com.example.produtos_api.model.Produto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProdutoRepository {

    private static final Logger logger = LoggerFactory.getLogger(ProdutoRepository.class);

    @Autowired
    private  ObjectMapper mapper = new ObjectMapper();

    @Value("${db.produtos}")
    private String banco;

    public List<Produto> listar() {
        File file = new File(banco);
        try {
            if(file.exists()){
                return mapper.readValue(file, new TypeReference<List<Produto>>() {});
            }
            return new ArrayList<>();

        }  catch (IOException e) {
    // É logado o trace para analise do erro
    // É lançado um novo ArquivoException que sera capturado pelo Handler e retornado o status e mensgem corretos para o cliente

            logger.error("Erro ao listar produtos", e);
            throw new ArquivoException("Erro interno ao listar produtos. Tente novamente mais tarde.", e);
        }
    }

    public void salvar(Produto produto) {
        File file = new File(banco);
        try {
            List<Produto> produtos = listar();
            produtos.add(produto);
            mapper.writeValue(file, produtos);

        } catch (IOException e) {
            logger.error("Erro ao salvar produtos", e);
            throw new ArquivoException("Erro interno ao salvar os dados. Tente novamente mais tarde.", e);
        }
    }
}
