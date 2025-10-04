package br.com.adeweb.ordemservico.core.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EntidadeNaoEncontradaExecptionTest {

    @Test
    void deveCriarexceptionComMensagem(){
        // Given: Uma mensagem simulando erro de entidade não encontrado
        String mensagem = "Entidade não localizada no sistema";

        // When: Instancia a exceção passando a mensagem
        EntidadeNaoEncontradaExecption exception = new EntidadeNaoEncontradaExecption(mensagem);

        // Then: Verifica se a mensagem foi armazenada corretamente pela RuntimeException
       assertEquals(mensagem, exception.getMessage());
    }
}
