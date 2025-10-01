package br.com.adeweb.ordemservico.adapter.controller;

import br.com.adeweb.ordemservico.adapter.input.controller.ClienteController;
import br.com.adeweb.ordemservico.adapter.input.mapper.ClienteMapper;
import br.com.adeweb.ordemservico.adapter.input.response.ClienteResponse;
import br.com.adeweb.ordemservico.core.domain.model.Cliente;
import br.com.adeweb.ordemservico.core.usecase.ClienteUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteControllerTest {
    @Mock
    private ClienteUseCase clienteUseCase;

    @Mock
    private ClienteMapper clienteMapper;

    @InjectMocks
    private ClienteController clienteController;

    @Test
    void deveRetornarListaDeClientes() {
        // Given (Dado): prepara lista simulada de clientes e response
        Cliente cliente1 = new Cliente();
        Cliente cliente2 = new Cliente();
        List<Cliente> clientes = List.of(cliente1, cliente2);
        Page<Cliente> pageClientes = new PageImpl<>(clientes);

        ClienteResponse response1 = new ClienteResponse();
        ClienteResponse response2 = new ClienteResponse();
        List<ClienteResponse> responses = List.of(response1, response2);
        Page<ClienteResponse> pageResponses = new PageImpl<>(responses);

        Pageable pageable = PageRequest.of(0, 10);

        when(clienteUseCase.findAll(pageable)).thenReturn(pageClientes);
        when(clienteMapper.toResponse(cliente1)).thenReturn(response1);
        when(clienteMapper.toResponse(cliente2)).thenReturn(response2);

        // When (Quando): chama o método getAll do controller
        ResponseEntity<Page<ClienteResponse>> responseEntity = clienteController.getAll(0, 10);

        // Then (Então): verifica se o status e conteúdo são os esperados
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
        assertEquals(2, responseEntity.getBody().getContent().size());
        verify(clienteUseCase, times(1)).findAll(pageable);
        verify(clienteMapper, times(1)).toResponse(cliente1);
        verify(clienteMapper, times(1)).toResponse(cliente2);
    }

    @Test
    void deveRetornarClientePrId(){
        // Given (dado): Configuração do cliente retornado pelo usecase e a resposta mapeada
        Long clienteId = 1L;
        Cliente cliente = new Cliente();
        ClienteResponse clienteResponse = new ClienteResponse();

        when(clienteUseCase.findById(clienteId)).thenReturn(cliente);
        when(clienteMapper.toResponse(cliente)).thenReturn(clienteResponse);

        // When (quando): Chama o metodo byId do controller
        ResponseEntity<ClienteResponse> responseEntity = clienteController.byId(clienteId);

        // Then (então): Verifica status HTTP e contéudo da resposta
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
        assertEquals(clienteResponse, responseEntity.getBody());
        verify(clienteUseCase, times(1)).findById(clienteId);
        verify(clienteMapper, times(1)).toResponse(cliente);

    }


}
