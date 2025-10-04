package br.com.adeweb.ordemservico.adapter.output.repository;

import br.com.adeweb.ordemservico.adapter.input.mapper.OrdemServicoMapper;
import br.com.adeweb.ordemservico.adapter.output.entities.OrdemServicoEntity;
import br.com.adeweb.ordemservico.adapter.output.repository.rowMapper.OrdemServicoRowMapper;
import br.com.adeweb.ordemservico.core.domain.model.OrdemServico;
import br.com.adeweb.ordemservico.core.exception.OrdemServicoException;
import br.com.adeweb.ordemservico.utils.ConstantUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;


import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrdemServicoRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private OrdemServicoMapper ordemServicoMapper;

    @Mock
    private OrdemServicoRowMapper ordemServicoRowMapper;

    @InjectMocks
    private OrdemServicoRepository ordemServicoRepository;

    @Test
    void deveBuscarTodasOrdensServico() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<OrdemServicoEntity> entidades = List.of(
                new OrdemServicoEntity(
                        1L,
                        1L,
                        "Descricao",
                        null,
                        new BigDecimal("100"),
                        LocalDateTime.now(),
                        null,
                        null)
        );
        when(jdbcTemplate.query(
                ConstantUtils.SQL_ALL_ORDEM_SERVICO,
                ordemServicoRowMapper,
                pageable.getPageSize(),
                pageable.getOffset())).thenReturn(entidades);
        when(jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM ordem_servico",
                Long.class)).thenReturn(1L);

        List<OrdemServico> dominios = List.of(
                new OrdemServico(1L,
                        1L,
                        "Descricao",
                        null,
                        new BigDecimal("100"),
                        LocalDateTime.now(),
                        null,
                        null)
        );
        when(ordemServicoMapper.toDomainList(entidades)).thenReturn(dominios);

        // When
        Page<OrdemServico> resultado = ordemServicoRepository.findAll(pageable);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        verify(jdbcTemplate).query(
                ConstantUtils.SQL_ALL_ORDEM_SERVICO,
                ordemServicoRowMapper,
                pageable.getPageSize(),
                pageable.getOffset());
        verify(jdbcTemplate).queryForObject(
                "SELECT COUNT(*) FROM ordem_servico",
                Long.class);
        verify(ordemServicoMapper).toDomainList(entidades);

    }

    @Test
    void deveBuscarOrdemServicoPorId() {
        // Given
        Long id = 1L;
        OrdemServicoEntity entity = new OrdemServicoEntity(
                1L,
                1L,
                "Descricao",
                null,
                new BigDecimal("100"),
                LocalDateTime.now(),
                null,
                null
        );
        when(jdbcTemplate.queryForObject(ConstantUtils.SQL_SELECT_BY_ID_ORDEM_SERVICO,
                ordemServicoRowMapper,
                id)).thenReturn(entity);
        OrdemServico dominio = new OrdemServico(
                1L,
                1L,
                "Descricao",
                null,
                new BigDecimal("100"),
                LocalDateTime.now(),
                null,
                null
        );
        when(ordemServicoMapper.toDomainFromEntity(entity)).thenReturn(dominio);

        // When
        Optional<OrdemServico> resultado = ordemServicoRepository.findById(id);

        // Then
        assertNotNull(resultado.isPresent());
        assertEquals(id, resultado.get().getId());
        verify(jdbcTemplate).queryForObject(ConstantUtils.SQL_SELECT_BY_ID_ORDEM_SERVICO,
                ordemServicoRowMapper, id);
        verify(ordemServicoMapper).toDomainFromEntity(entity);

    }

    @Test
    void deveSalvarOrdemServico() {
        // Given (Preparação):
        // Cria a instância da OrdemServico sem ID (novo objeto a ser salvo)
        OrdemServico ordemServico = new OrdemServico(
                null,               // id null pois ainda não tem valor
                1L,                 // id do cliente relacionado
                "Descricao",        // descrição do serviço
                null,               // status ou outros campos opcionais nulos
                new BigDecimal("100"), // valor do serviço
                null, null, null    // outros campos opcionais
        );

        // Cria a entidade correspondente antes do save, sem ID (como vai ser enviada ao banco)
        OrdemServicoEntity entity = new OrdemServicoEntity(
                null,
                1L,
                "Descricao",
                null,
                new BigDecimal("100"),
                null, null, null
        );

        // Cria a entidade simulando o registro já salvo, com ID gerado (5L por exemplo)
        OrdemServicoEntity entityComId = new OrdemServicoEntity(
                5L,
                1L,
                "Descricao",
                null,
                new BigDecimal("100"),
                null, null, null
        );

        // Cria o modelo de domínio para o registro salvo após converter a entidadeComId
        OrdemServico ordemServicoComId = new OrdemServico(
                5L,
                1L,
                "Descricao",
                null,
                new BigDecimal("100"),
                null, null, null
        );

        // Configura o mock do mapper para converter domínio para entidade durante o save
        when(ordemServicoMapper.toEntity(ordemServico)).thenReturn(entity);

        // Configura o mock para o metodo 'execute' do JdbcTemplate, simulando a procedure:
        // A procedure deve setar o ID gerado no banco em entity e retornar este ID
        when(jdbcTemplate.execute(
                (org.springframework.jdbc.core.CallableStatementCreator) any(),
                any(org.springframework.jdbc.core.CallableStatementCallback.class)))
                .thenAnswer(invocation -> {
                    entity.setId(5L); // seta o id gerado na entidade mockada
                    return 5L;        // retorna o id gerado simulando comportamento real
                });

        // Configura o mock para o jdbcTemplate.queryForObject usado internamente no findById:
        // ele deve retornar a entidade salva com ID (entityComId)
        when(jdbcTemplate.queryForObject(
                eq(ConstantUtils.SQL_SELECT_BY_ID_ORDEM_SERVICO),
                any(OrdemServicoRowMapper.class),
                eq(5L)
        )).thenReturn(entityComId);

        // Configura o mock para o mapper que converte a entidade com ID para modelo domínio
        when(ordemServicoMapper.toDomainFromEntity(entityComId)).thenReturn(ordemServicoComId);

        // When (Ação):
        // Executa o metodo save da classe testada
        OrdemServico resultado = ordemServicoRepository.save(ordemServico);

        // Then (Assertivas):
        // Verifica se resultado não é nulo e possui o ID gerado esperado
        assertNotNull(resultado);
        assertEquals(5L, resultado.getId());

        // Verifica se os métodos mockados foram chamados com os parâmetros esperados
        verify(jdbcTemplate).execute(
                (org.springframework.jdbc.core.CallableStatementCreator) any(),
                any(org.springframework.jdbc.core.CallableStatementCallback.class));
        verify(jdbcTemplate).queryForObject(
                eq(ConstantUtils.SQL_SELECT_BY_ID_ORDEM_SERVICO),
                any(OrdemServicoRowMapper.class),
                eq(5L));
        verify(ordemServicoMapper).toEntity(ordemServico);
        verify(ordemServicoMapper).toDomainFromEntity(entityComId);
    }

    @Test
    void deveAtualizarOrdemServico() {
        // Given: Objeto domínio para atualização sem ID (novo conteúdo para atualizar)
        OrdemServico ordemServicoAtualizar = new OrdemServico(
                null,
                1L,
                "Descricao Atualizada",
                null,
                new BigDecimal("150"),
                null,
                null,
                null
        );

        // Entidade correspondente antes do save, sem ID pois é o conteúdo enviado
        OrdemServicoEntity entitySemId = new OrdemServicoEntity(
                null,
                1L,
                "Descricao Atualizada",
                null,
                new BigDecimal("150"),
                null,
                null,
                null
        );

        // Entidade simulando registro salvo no banco com ID já existente
        OrdemServicoEntity entityComId = new OrdemServicoEntity(
                1L,
                1L,
                "Descricao Atualizada",
                null,
                new BigDecimal("150"),
                null,
                null,
                null
        );

        // Modelo domínio resultado esperado após a atualização
        OrdemServico ordemServicoAtualizada = new OrdemServico(
                1L,
                1L,
                "Descricao Atualizada",
                null,
                new BigDecimal("150"),
                null,
                null,
                null
        );

        // Mock do mapper para converter domínio para entidade antes do save
        when(ordemServicoMapper.toEntity(ordemServicoAtualizar)).thenReturn(entitySemId);

        // Mock do método execute para simular procedure de update que retorna o id gerado (1L)
        when(jdbcTemplate.execute(
                (org.springframework.jdbc.core.CallableStatementCreator) any(),
                any(org.springframework.jdbc.core.CallableStatementCallback.class)
        )).thenAnswer(invocation -> {
            // Simula setar o id da entidade após update/procedure
            entitySemId.setId(1L);
            return 1L;
        });

        // Mock da queryForObject usada internamente para buscar a entidade atualizada pelo id
        when(jdbcTemplate.queryForObject(
                eq(ConstantUtils.SQL_SELECT_BY_ID_ORDEM_SERVICO),
                any(OrdemServicoRowMapper.class),
                eq(1L)
        )).thenReturn(entityComId);

        // Mock do mapper para converter a entidade buscada para o domínio
        when(ordemServicoMapper.toDomainFromEntity(entityComId)).thenReturn(ordemServicoAtualizada);

        // When: executa update passando id e objeto domínio com dados para atualizar
        OrdemServico resultado = ordemServicoRepository.update(1L, ordemServicoAtualizar);

        // Then: valida resultado não nulo e id correto
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());

        // Verifica se mocks foram chamados com os parâmetros corretos
        verify(jdbcTemplate).execute(
                (org.springframework.jdbc.core.CallableStatementCreator) any(),
                any(org.springframework.jdbc.core.CallableStatementCallback.class)
        );
        verify(jdbcTemplate).queryForObject(
                eq(ConstantUtils.SQL_SELECT_BY_ID_ORDEM_SERVICO),
                any(OrdemServicoRowMapper.class),
                eq(1L)
        );
        verify(ordemServicoMapper).toEntity(ordemServicoAtualizar);
        verify(ordemServicoMapper).toDomainFromEntity(entityComId);
    }
    @Test
    void deveLancarExcecaoAoBuscarOrdemServicoPorId() {
        Long id = 1L;
        when(jdbcTemplate.queryForObject(
                ConstantUtils.SQL_SELECT_BY_ID_ORDEM_SERVICO,
                ordemServicoRowMapper,
                id
        )).thenThrow(new org.springframework.dao.DataAccessException("nao existe") {});

        Exception ex = assertThrows(OrdemServicoException.class,
                () -> ordemServicoRepository.findById(id));

        assertTrue(ex.getMessage().contains("Codigo Não Existe"));
    }
    @Test
    void deveLancarExcecaoAoSalvarOrdemServico() {
        OrdemServico ordemServico = new OrdemServico(null, 1L, "Descricao", null,
                new BigDecimal("100"), null, null, null);

        OrdemServicoEntity entity = new OrdemServicoEntity(null, 1L, "Descricao", null,
                new BigDecimal("100"), null, null, null);

        when(ordemServicoMapper.toEntity(ordemServico)).thenReturn(entity);

        when(jdbcTemplate.execute(any(org.springframework.jdbc.core.CallableStatementCreator.class),
                any(org.springframework.jdbc.core.CallableStatementCallback.class)))
                .thenThrow(new org.springframework.dao.DataAccessException("erro procedure") {});

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> ordemServicoRepository.save(ordemServico));

        assertTrue(ex.getMessage().contains("Erro ao Cadastrar Ordem Servico"));
    }
    @Test
    void deveRetornarNullAoDeletarOrdemServico() {
        OrdemServico ordemServico = new OrdemServico(1L, 1L, "Teste", null,
                new BigDecimal("100"), null, null, null);

        OrdemServico resultado = ordemServicoRepository.delete(ordemServico);

        assertEquals(null, resultado);
    }


}
