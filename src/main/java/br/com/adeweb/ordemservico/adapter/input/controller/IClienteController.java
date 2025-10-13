package br.com.adeweb.ordemservico.adapter.input.controller;

import br.com.adeweb.ordemservico.adapter.input.request.ClienteRequest;
import br.com.adeweb.ordemservico.adapter.input.response.ClienteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface IClienteController {

    @Operation(summary = "Listar todos os clientes", description = "Retorna uma lista paginada de clientes.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @GetMapping
    ResponseEntity<Page<ClienteResponse>> getAll(
            @Parameter(description = "Número da página (padrão = 0)", example = "0")
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @Parameter(description = "Tamanho da página (padrão = 10)", example = "10")
            @RequestParam(defaultValue = "10") Integer size
    );

    @Operation(summary = "Buscar cliente por ID", description = "Retorna um cliente específico com base no ID informado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @GetMapping("/{id}")
    ResponseEntity<ClienteResponse> byId(
            @Parameter(description = "ID do cliente a ser buscado", example = "1")
            @PathVariable Long id
    );

    @Operation(summary = "Cadastrar novo cliente", description = "Cria um novo cliente no sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos enviados na requisição", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @PostMapping
    ResponseEntity<ClienteResponse> save(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Objeto contendo os dados do cliente a ser criado",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ClienteRequest.class))
            )
            @RequestBody ClienteRequest clienteRequest
    );

    @Operation(summary = "Atualizar cliente existente", description = "Atualiza os dados de um cliente com base no ID informado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos enviados na requisição", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @PutMapping("/{id}")
    ResponseEntity<ClienteResponse> update(
            @Parameter(description = "ID do cliente a ser atualizado", example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Objeto contendo os dados atualizados do cliente",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ClienteRequest.class))
            )
            @RequestBody ClienteRequest clienteRequest
    );
}
