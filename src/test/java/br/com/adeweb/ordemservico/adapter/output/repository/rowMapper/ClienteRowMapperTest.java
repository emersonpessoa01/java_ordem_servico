package br.com.adeweb.ordemservico.adapter.output.repository.rowMapper;


import br.com.adeweb.ordemservico.adapter.output.entities.ClienteEntity;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ClienteRowMapperTest {

    @Test
    void deveMapearClienteCorretamente() throws Exception {
        // Given: criar mock do ResultSet configurando os retornos esperados
        ResultSet rsMock = mock(ResultSet.class);

        // Configurando o mock para retornar valores específicos quando os métodos do ResultSet forem chamados
        when(rsMock.getLong("id")).thenReturn(123L);
        when(rsMock.getString("nome")).thenReturn("Cliente de Teste");
        when(rsMock.getString("email")).thenReturn("teste@cliente.com");

        ClienteRowMapper mapper = new ClienteRowMapper();

        // When: executa o método mapRow passando o ResultSet mockado e número da linha
        ClienteEntity cliente = mapper.mapRow(rsMock, 1);

        // Then: verifica se os campos do objeto foram preenchidos corretamente conforme o mock
        assertEquals(123L, cliente.getId());
        assertEquals("Cliente de Teste", cliente.getNome());
        assertEquals("teste@cliente.com", cliente.getEmail());
    }
}
