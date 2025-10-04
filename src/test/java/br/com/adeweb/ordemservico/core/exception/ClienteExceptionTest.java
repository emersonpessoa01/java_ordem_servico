package br.com.adeweb.ordemservico.core.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClienteExceptionTest {

    @Test
    void deveCriarExceptionComMensagemECausa() {
        // Given: uma mensagem de erro e uma causa (Throwable) definida
        String mensagem = "Erro ao salvar cliente";
        Throwable causa = new RuntimeException("Erro interno");

        // When: instancia ClienteException com mensagem e causa
        ClienteException exception = new ClienteException(mensagem, causa);

        // Then: deve guardar mensagem e causa corretamente
        assertEquals(mensagem, exception.getMessage());
        assertEquals(causa, exception.getCause());
    }

    @Test
    void deveAceitarCausaNula() {
        // Given: uma mensagem de erro e causa nula
        String mensagem = "Erro específico do cliente";

        // When: instancia ClienteException com causa nula
        ClienteException exception = new ClienteException(mensagem, null);

        // Then: mensagem correta e causa nula
        assertEquals(mensagem, exception.getMessage());
        assertNull(exception.getCause());
    }
}

