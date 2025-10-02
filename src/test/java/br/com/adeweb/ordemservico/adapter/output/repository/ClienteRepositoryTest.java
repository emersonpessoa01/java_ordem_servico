package br.com.adeweb.ordemservico.adapter.output.repository;

import br.com.adeweb.ordemservico.adapter.input.mapper.ClienteMapper;
import br.com.adeweb.ordemservico.adapter.output.entities.ClienteEntity;
import br.com.adeweb.ordemservico.adapter.output.repository.rowMapper.ClienteRowMapper;
import br.com.adeweb.ordemservico.core.domain.model.Cliente;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private ClienteMapper clienteMapper;

    @Mock
    private ClienteRowMapper clienteRowMapper;

    @InjectMocks
    private ClienteRepository clienteRepository;

    @Test
    void deveBuscarTodosClientes() {
        Pageable pageable = PageRequest.of(0, 10);
        List<ClienteEntity> clientesEntidade = List.of(new ClienteEntity(1L, "Nome1", "email1@email.com"));
        when(jdbcTemplate.query(anyString(), eq(clienteRowMapper), anyInt(), anyLong())).thenReturn(clientesEntidade);
        when(jdbcTemplate.queryForObject(anyString(), eq(Long.class))).thenReturn(1L);

        List<Cliente> clientesDominio = List.of(new Cliente(1L, "Nome1", "email1@email.com"));
        when(clienteMapper.toDomainList(clientesEntidade)).thenReturn(clientesDominio);

        Page<Cliente> resultado = clienteRepository.findAll(pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());

        verify(jdbcTemplate).query(anyString(), eq(clienteRowMapper), anyInt(), anyLong());
        verify(jdbcTemplate).queryForObject(anyString(), eq(Long.class));
        verify(clienteMapper).toDomainList(clientesEntidade);
    }

    @Test
    void deveBuscarClientePorId() {
        Long id = 1L;
        ClienteEntity clienteEntity = new ClienteEntity(id, "Nome1", "email1@email.com");
        when(jdbcTemplate.queryForObject(anyString(), eq(clienteRowMapper), eq(id))).thenReturn(clienteEntity);
        when(clienteMapper.toDomainEntity(clienteEntity)).thenReturn(new Cliente(id, "Nome1", "email1@email.com"));

        Optional<Cliente> clienteOptional = clienteRepository.buscarPorId(id);

        assertTrue(clienteOptional.isPresent());
        assertEquals(id, clienteOptional.get().getId());

        verify(jdbcTemplate).queryForObject(anyString(), eq(clienteRowMapper), eq(id));
        verify(clienteMapper).toDomainEntity(clienteEntity);
    }

    @Test
    void deveSalvarCliente() {
        Cliente cliente = new Cliente(null, "Nome Novo", "emailnovo@email.com");
        ClienteEntity clienteEntity = new ClienteEntity(null, "Nome Novo", "emailnovo@email.com");

        when(clienteMapper.toEntity(cliente)).thenReturn(clienteEntity);

        // Corrigindo a chamada execute para evitar ambiguidades, definindo mocks com cast
        when(jdbcTemplate.execute(any(CallableStatementCreator.class), any(CallableStatementCallback.class)))
                .thenAnswer(invocation -> {
                    CallableStatementCreator csc = invocation.getArgument(0);
                    CallableStatementCallback<Long> cscb = invocation.getArgument(1);
                    // Simula a execução da procedure e retorno do id
                    Long idGerado = 10L;
                    // Simula alteração do id no entity (como faria na procedure)
                    clienteEntity.setId(idGerado);
                    // Retorna o id gerado
                    return idGerado;
                });

        // Agora, chamar toDomainEntity UM única vez retornando o cliente com ID populado
        when(clienteMapper.toDomainEntity(clienteEntity)).thenReturn(new Cliente(10L, "Nome Novo", "emailnovo@email.com"));

        Cliente salvo = clienteRepository.salvar(cliente);

        assertNotNull(salvo);
        assertEquals(10L, salvo.getId());

        verify(jdbcTemplate).execute(any(CallableStatementCreator.class), any(CallableStatementCallback.class));
        verify(clienteMapper).toEntity(cliente);
        verify(clienteMapper).toDomainEntity(clienteEntity);  // somente 1 vez
    }


    @Test
    void deveAtualizarCliente() {
        Cliente cliente = new Cliente(null, "Nome Atualizado", "emailatualizado@email.com");

        ClienteEntity clienteEntity = new ClienteEntity(null, "Nome Atualizado", "emailatualizado@email.com");

        when(clienteMapper.toEntity(cliente)).thenReturn(clienteEntity);

        when(jdbcTemplate.execute(any(CallableStatementCreator.class), any(CallableStatementCallback.class)))
                .thenAnswer(invocation -> {
                    clienteEntity.setId(1L);
                    return 1L;
                });

        when(clienteMapper.toDomainEntity(any())).thenReturn(new Cliente(1L, "Nome Atualizado", "emailatualizado@email.com"));

        Cliente atualizado = clienteRepository.update(1L, cliente);

        assertNotNull(atualizado);
        assertEquals(1L, atualizado.getId());

        verify(clienteMapper).toEntity(cliente);
        verify(jdbcTemplate).execute(any(CallableStatementCreator.class), any(CallableStatementCallback.class));
        verify(clienteMapper).toDomainEntity(clienteEntity);
    }


}
