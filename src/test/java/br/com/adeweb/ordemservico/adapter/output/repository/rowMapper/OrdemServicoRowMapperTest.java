package br.com.adeweb.ordemservico.adapter.output.repository.rowMapper;

import br.com.adeweb.ordemservico.adapter.output.entities.OrdemServicoEntity;
import br.com.adeweb.ordemservico.Enum.StatusOrdemServicoEnum;
import br.com.adeweb.ordemservico.utils.ConstantUtils;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrdemServicoRowMapperTest {

    @Test
    void deveMapearOrdemServicoCorretamente() throws SQLException {
        // Given: Mock do ResultSet com valores válidos para simular cada campo do mapeamento
        ResultSet rs = mock(ResultSet.class);

        when(rs.getLong(ConstantUtils.ID)).thenReturn(100L);
        when(rs.getLong(ConstantUtils.CLIENTE_ID)).thenReturn(200L);
        when(rs.getString(ConstantUtils.DESCRICAO)).thenReturn("Serviço de Teste");
        when(rs.getString(ConstantUtils.STATUS)).thenReturn("ABERTA");  // Valor correto do enum
        when(rs.getBigDecimal(ConstantUtils.VALOR)).thenReturn(new java.math.BigDecimal("123.45"));

        LocalDateTime agora = LocalDateTime.now();
        when(rs.getTimestamp(ConstantUtils.ABERTO_EM)).thenReturn(Timestamp.valueOf(agora));
        when(rs.getTimestamp(ConstantUtils.FECHADO_EM)).thenReturn(null);
        when(rs.getTimestamp(ConstantUtils.ATUALIZADO_EM)).thenReturn(Timestamp.valueOf(agora));

        OrdemServicoRowMapper mapper = new OrdemServicoRowMapper();

        // When: Executa o método mapRow com o ResultSet mockado
        OrdemServicoEntity ordem = mapper.mapRow(rs, 1);

        // Then: Verifica se os campos foram corretamente mapeados para a entidade
        assertEquals(100L, ordem.getId());
        assertEquals(200L, ordem.getClienteId());
        assertEquals("Serviço de Teste", ordem.getDescricao());
        assertEquals(StatusOrdemServicoEnum.ABERTA, ordem.getStatus());
        assertEquals(0, new java.math.BigDecimal("123.45").compareTo(ordem.getValor()));
        assertEquals(agora, ordem.getAbertoEm());
        assertNull(ordem.getFechadoEm());
        assertEquals(agora, ordem.getAtualizadoEm());
    }
}
