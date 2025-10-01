package br.com.adeweb.ordemservico.adapter.output.repository;

import br.com.adeweb.ordemservico.adapter.input.mapper.ClienteMapper;
import br.com.adeweb.ordemservico.adapter.input.mapper.OrdemServicoMapper;
import br.com.adeweb.ordemservico.adapter.output.entities.ClienteEntity;
import br.com.adeweb.ordemservico.adapter.output.entities.OrdemServicoEntity;
import br.com.adeweb.ordemservico.adapter.output.repository.OrdemServicoRepository;
import br.com.adeweb.ordemservico.adapter.output.repository.rowMapper.ClienteRowMapper;
import br.com.adeweb.ordemservico.adapter.output.repository.rowMapper.OrdemServicoRowMapper;
import br.com.adeweb.ordemservico.core.domain.model.Cliente;
import br.com.adeweb.ordemservico.core.domain.model.OrdemServico;
import br.com.adeweb.ordemservico.core.exception.OrdemServicoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


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
        // GIVEN: dados simulados de clientes e paginação
        Pageable pageable = PageRequest.of(0, 10);
        List<ClienteEntity> clientesEntidade = List.of(new ClienteEntity(1L, "Nome1", "email1@email.com"));
        when(jdbcTemplate.query(anyString(), eq(clienteRowMapper), anyInt(), anyLong())).thenReturn(clientesEntidade);
        when(jdbcTemplate.queryForObject(anyString(), eq(Long.class))).thenReturn(1L);

        List<Cliente> clientesDominio = List.of(new Cliente(1L, "Nome1", "email1@email.com"));
        when(clienteMapper.toDomainList(clientesEntidade)).thenReturn(clientesDominio);

        // WHEN: executa consulta no repository
        Page<Cliente> resultado = clienteRepository.findAll(pageable);

        // THEN: valida retorno e chamadas
        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        verify(jdbcTemplate).query(anyString(), eq(clienteRowMapper), anyInt(), anyLong());
        verify(jdbcTemplate).queryForObject(anyString(), eq(Long.class));
        verify(clienteMapper).toDomainList(clientesEntidade);
    }
}
