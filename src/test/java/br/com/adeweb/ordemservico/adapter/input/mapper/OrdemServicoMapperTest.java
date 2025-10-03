package br.com.adeweb.ordemservico.adapter.input.mapper;

import br.com.adeweb.ordemservico.adapter.input.request.OrdemServicoRequest;
import br.com.adeweb.ordemservico.adapter.output.entities.OrdemServicoEntity;
import br.com.adeweb.ordemservico.core.domain.model.OrdemServico;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OrdemServicoMapperTest {
    private static OrdemServicoMapper ordemServicoMapper;

    @BeforeAll
    public static void setup() {
        // Given: Obtenção da instancia gerada do mapper
        ordemServicoMapper = Mappers.getMapper(OrdemServicoMapper.class);
    }

    @Test
    void deveMapearRequestParaDominio() {
        // Given: Um request preenchido de ordem de serviço
        OrdemServicoRequest request = new OrdemServicoRequest();
        request.setClienteId(1L);
        request.setDescricao("Descrição");
        request.setValor(new BigDecimal("100.50"));

        // When: Converter para domínio
        OrdemServico ordemServico = ordemServicoMapper.toDaminFromRequest(request);

        // Then: Validar que dados foram mapeados corretamente
        assertEquals(1L, ordemServico.getClienteId());
        assertEquals("Descrição", ordemServico.getDescricao());
        assertEquals(new BigDecimal("100.50"), ordemServico.getValor());
    }
    @Test
    void deveMapearEntidadeParaDominio(){
        // Given: OrdemServicoEntity preenchida
        OrdemServicoEntity entity = new OrdemServicoEntity();
        entity.setId(1L);
        entity.setClienteId(1L);
        entity.setDescricao("Descrição");
        entity.setValor(new BigDecimal("200.00"));
        entity.setAbertoEm(LocalDateTime.now());

        // When:Converter para domínio
        OrdemServico ordemServico = ordemServicoMapper.toDomainFromEntity(entity);

        // Then:  Verificar dados convertidos
        assertEquals(1L, ordemServico.getId());
        assertEquals("Descrição", ordemServico.getDescricao());
        assertEquals(new BigDecimal("200.00"), ordemServico.getValor());
        assertNotNull(ordemServico.getAbertoEm());

    }
}