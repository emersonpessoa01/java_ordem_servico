package br.com.adeweb.ordemservico.adapter.input.exception;

import br.com.adeweb.ordemservico.adapter.input.response.ErrorResponse;
import br.com.adeweb.ordemservico.core.exception.EntidadeNaoEncontradaExecption;
import br.com.adeweb.ordemservico.core.exception.OrdemServicoException;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalHandlerExceptionTest {

    @Test
    void deveRetornarNotFoundParaEntidadeNaoEncontrada() {
        // Given: Ambiente de testes configurado com handler e exception específica.
        GlobalHandlerException handler = new GlobalHandlerException();
        String mensagemDeErro = "cliente não encontrado";
        EntidadeNaoEncontradaExecption exception = new EntidadeNaoEncontradaExecption(mensagemDeErro);

        // When: Simula o lançamento da exception e captura o response.
        ResponseEntity<ErrorResponse> response = handler.handlerNotFound(exception);

        // Then: Valida status, mensagem e descrição de erro retornados.
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Not Found", response.getBody().getError());
        assertEquals(mensagemDeErro, response.getBody().getMessage());
    }

    @Test
    void deveRetornarInternalServerErrorParaErroBanco() {
        // Given: Handler global e uma OrdemServicoException (erro genérico de negócio).
        GlobalHandlerException handler = new GlobalHandlerException();
        String mensagemDeErro = "erro inesperado no banco";
        OrdemServicoException exception = new OrdemServicoException(mensagemDeErro, null);

        // When: Handler captura a exception e monta response.
        ResponseEntity<ErrorResponse> response = handler.handleErrorDatabase(exception);

        // Then: Verifica status HTTP, body de erro e mensagem preservados corretamente.
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Internal Server Error", response.getBody().getError());
        assertEquals(mensagemDeErro, response.getBody().getMessage());
    }
}