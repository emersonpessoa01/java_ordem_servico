package br.com.adeweb.ordemservico.core.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class OrdemServicoExceptionTest {
    @Test
    void deveCriarExceptionComMensagemECausa() {
        // Given: mensagem e causa para a exceção
        String mensagem = "Erro ao salvar ordem";
        Throwable causa = new RuntimeException("Detalhe do erro");

        // When: instanciar a exceção com mensagem e causa
        OrdemServicoException exception = new OrdemServicoException(mensagem, causa);

        // Then: validar mensagem e causa armazenadas corretamente
        assertEquals(mensagem, exception.getMessage());
        assertEquals(causa, exception.getCause());
    }
    @Test
    void devePermitirCausaNula() {
        // Given: mensagem e causa nula
        String mensagem = "Erro genérico";

        // When: instanciar exceção com causa nula
        OrdemServicoException exception = new OrdemServicoException(mensagem, null);

        // Then: validar mensagem correta e causa nula
        assertEquals(mensagem, exception.getMessage());
        assertNull(exception.getCause());
    }
}
