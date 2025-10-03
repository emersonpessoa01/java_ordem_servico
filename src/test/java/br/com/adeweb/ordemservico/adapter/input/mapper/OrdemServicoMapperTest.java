package br.com.adeweb.ordemservico.adapter.input.mapper;

import br.com.adeweb.ordemservico.adapter.input.request.OrdemServicoRequest;
import br.com.adeweb.ordemservico.adapter.input.response.OrdemServicoResponse;
import br.com.adeweb.ordemservico.adapter.output.entities.OrdemServicoEntity;
import br.com.adeweb.ordemservico.core.domain.model.OrdemServico;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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
    void deveMapearEntidadeParaDominio() {
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

    @Test
    void deveMapearDominioParaEntidade() {
        // Given: Objeto de dominio criado
        OrdemServico ordemServico = new OrdemServico();
        ordemServico.setId(2L);
        ordemServico.setClienteId(2L);
        ordemServico.setDescricao("Serviço");
        ordemServico.setValor(new BigDecimal("150.75"));

        // When: Converter para entidade
        OrdemServicoEntity entity = ordemServicoMapper.toEntity(ordemServico);

        // Then: Validar campos convertidos
        assertEquals(2L, entity.getId());
        assertEquals(2L, entity.getClienteId());
        assertEquals("Serviço", entity.getDescricao());
        assertEquals(new BigDecimal("150.75"), entity.getValor());
    }

    @Test
    void devoMapearDominioParaRequest() {
        // Given: Ordem de erviço criado no dominio
        OrdemServico ordemServico = new OrdemServico();
        ordemServico.setClienteId(3L);
        ordemServico.setDescricao("Pedido");
        ordemServico.setValor(new BigDecimal("300.00"));

        // When: Converter para Request
        OrdemServicoRequest request = ordemServicoMapper.toRequest(ordemServico);

        // Then: Validar campos convertidos
        assertEquals(3L, request.getClienteId());
        assertEquals("Pedido", request.getDescricao());
        assertEquals(new BigDecimal("300.00"), request.getValor());

    }

    @Test
    void devoMapearDominioParaResponse() {
        // Given: Instância do dominio
        OrdemServico ordemServico = new OrdemServico();
        ordemServico.setId(4L);
        ordemServico.setDescricao("Resposta");
        ordemServico.setValor(new BigDecimal("400.00"));

        // When:Converter para Response
        OrdemServicoResponse response = ordemServicoMapper.toResponse(ordemServico);

        // Then: Validar dados do
        assertEquals(4L, response.getId());
        assertEquals("Resposta", response.getDescricao());
        assertEquals(new BigDecimal("400.00"), response.getValor());


    }

}