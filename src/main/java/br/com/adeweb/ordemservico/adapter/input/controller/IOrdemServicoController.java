package br.com.adeweb.ordemservico.adapter.input.controller;

import br.com.adeweb.ordemservico.adapter.input.request.OrdemServicoRequest;
import br.com.adeweb.ordemservico.adapter.input.response.OrdemServicoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Interface Swagger para documentação e contrato REST da entidade Ordem de Serviço.
 */
public interface IOrdemServicoController {

    @Operation(summary = "Listar ordens de serviço", description = "Retorna uma lista paginada de ordens de serviço cadastradas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @GetMapping
    ResponseEntity<Page<OrdemServicoResponse>> getAll(
            @Parameter(description = "Número da página (padrão = 0)", example = "0")
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @Parameter(description = "Tamanho da página (padrão = 10)", example = "10")
            @RequestParam(defaultValue = "10") Integer size
    );

    @Operation(summary = "Buscar ordem de serviço por ID", description = "Retorna uma ordem de serviço específica com base no ID informado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ordem de serviço encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Ordem de serviço não encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @GetMapping("/{id}")
    ResponseEntity<OrdemServicoResponse> byId(
            @Parameter(description = "ID da ordem de serviço a ser buscada", example = "1")
            @PathVariable Long id
    );

    @Operation(summary = "Cadastrar nova ordem de serviço", description = "Cria uma nova ordem de serviço no sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ordem de serviço criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos enviados na requisição", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @PostMapping
    ResponseEntity<OrdemServicoResponse> save(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Objeto contendo os dados da nova ordem de serviço",
                    required = true,
                    content = @Content(schema = @Schema(implementation = OrdemServicoRequest.class))
            )
            @RequestBody OrdemServicoRequest ordemServicoRequest
    );

    @Operation(summary = "Atualizar ordem de serviço existente", description = "Atualiza os dados de uma ordem de serviço com base no ID informado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ordem de serviço atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos enviados na requisição", content = @Content),
            @ApiResponse(responseCode = "404", description = "Ordem de serviço não encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @PutMapping("/{id}")
    ResponseEntity<OrdemServicoResponse> update(
            @Parameter(description = "ID da ordem de serviço a ser atualizada", example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Objeto contendo os dados atualizados da ordem de serviço",
                    required = true,
                    content = @Content(schema = @Schema(implementation = OrdemServicoRequest.class))
            )
            @RequestBody OrdemServicoRequest ordemServicoRequest
    );
}
