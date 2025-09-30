package br.com.adeweb.ordemservico.core.usecase;

import br.com.adeweb.ordemservico.core.domain.model.Cliente;

import br.com.adeweb.ordemservico.core.exception.EntidadeNaoEncontradaExecption;
import br.com.adeweb.ordemservico.port.output.ClienteOutputPort;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteUseCaseTest {
    @Mock
    private ClienteOutputPort outputPort;
    // Mock da dependência ClienteOutputPort

    @InjectMocks
    private ClienteUseCase clienteUseCase;
    // Classe sob teste (ClienteUseCase) com OutputPort injetado


    @Test
    void deveRetornarListaDeClientes() {
        // Given - preparação do cenário
        Cliente cliente1 = new Cliente();
        Cliente cliente2 = new Cliente();
        List<Cliente> clientes = List.of(cliente1, cliente2);
        Page<Cliente> pageClientes = new PageImpl<>(clientes);
        //  Quando outputPort.findAll for chamado, retorna a lista simulada
        when(outputPort.findAll(any(Pageable.class))).thenReturn(pageClientes);

        // When - ação que queremos testar
        Page<Cliente> resultado = clienteUseCase.findAll(Pageable.unpaged());

        // Then - verificação do resultado esperado
        assertNotNull(resultado);
        assertEquals(2, resultado.getContent().size()); // Deve retornar a lista mock
        verify(outputPort, times(1)).findAll(any(Pageable.class));
        // Verifica se o metodo foi chamado uma vez
    }
    @Test
    void deveLancarExcecaoQuandoClienteNaoEncontrado() {
        // Given - preparação do cenário
        when(outputPort.buscarPorId(1L)).thenReturn(Optional.empty());

        // When & Then - ação e verificação da exceção
        EntidadeNaoEncontradaExecption exception = assertThrows(EntidadeNaoEncontradaExecption.class, () -> {
            clienteUseCase.findById(1L);
        });

        // Then - verificação da mensagem da exceção
        assertTrue(exception.getMessage().contains("Cliente com id 1 não encontrado"));
        verify(outputPort, times(1)).buscarPorId(1L);
    }
    @Test
    void deveSalvarCliente() {
        // Given - preparação do cenário
        Cliente cliente = new Cliente();
        when(outputPort.salvar(cliente)).thenReturn(cliente);

        // When - ação que queremos testar
        Cliente resultado = clienteUseCase.salvar(cliente);

        // Then - verificação do resultado esperado
        assertNotNull(resultado);
        verify(outputPort, times(1)).salvar(cliente);
    }
    @Test
    void dveAtualizarCliente(){
        // Given - preparação do cenário
        Cliente cliente = new Cliente();
        when(outputPort.buscarPorId(1L)).thenReturn(Optional.of(cliente));
        when(outputPort.update(1L,cliente)).thenReturn(cliente);

        // When - ação que queremos testar
        Cliente resultado = clienteUseCase.update(1L, cliente);

        // Then - verificação do resultado esperado
        assertNotNull(resultado);
        verify(outputPort, times(1)).buscarPorId(1L);
        verify(outputPort, times(1)).update(1L,cliente);
    }
}
