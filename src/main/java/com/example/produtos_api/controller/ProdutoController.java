package com.example.produtos_api.controller;

import com.example.produtos_api.dto.ProdutoDTO;
import com.example.produtos_api.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@Tag(name = "Produtos", description = "Operações relacionadas a produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem de produtos retornada sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum produto encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro interno ao listar produtos. Tente novamente mais tarde.")
    })
    @Operation(summary = "Listagem de produtos", description = "Retorna uma lista de todos os produtos.")
    public ResponseEntity<List<ProdutoDTO>> listar() {
        return ResponseEntity.ok(produtoService.listar());
    }

    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto nao encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro interno ao buscar produto. Tente novamente mais tarde.")
    })
    @Operation(summary = "Busca produto por codigo", description = "Retorna um produto por codigo.")
    public ResponseEntity<ProdutoDTO> buscarPorCodigo(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.buscarPorCodigo(id));
    }

    @GetMapping("/nome")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produtos encontrados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto nao encontrado."),
            @ApiResponse(responseCode = "500", description = "Erro interno ao listar produtos. Tente novamente mais tarde.")
    })
    @Operation(summary = "Listagem de produtos por nome", description = "Retorna uma lista de produtos por nome.")
    public ResponseEntity<List<ProdutoDTO>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(produtoService.buscarPorNome(nome));
    }

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto cadastrado com sucesso"),
            @ApiResponse(responseCode = "409", description = "Produto com código já existente"),
            @ApiResponse(responseCode = "500", description = "Erro interno ao cadastrar produtos. Tente novamente mais tarde.")
    })
    public ResponseEntity<Void> cadastrar(@RequestBody ProdutoDTO produto) {
        produtoService.cadastrar(produto);
        return ResponseEntity.created(URI.create("/api/produtos/" + produto.codigo())).build();
    }
}
