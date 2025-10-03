package br.com.adeweb.ordemservico.adapter.output.repository;

import br.com.adeweb.ordemservico.adapter.input.mapper.OrdemServicoMapper;
import br.com.adeweb.ordemservico.adapter.output.entities.OrdemServicoEntity;
import br.com.adeweb.ordemservico.adapter.output.repository.rowMapper.OrdemServicoRowMapper;
import br.com.adeweb.ordemservico.core.domain.model.OrdemServico;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrdemServicoRepositoryTest {

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
}
