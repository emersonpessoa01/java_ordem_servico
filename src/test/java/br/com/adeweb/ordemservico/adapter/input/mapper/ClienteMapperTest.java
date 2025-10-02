package br.com.adeweb.ordemservico.adapter.input.mapper;


import br.com.adeweb.ordemservico.adapter.input.request.ClienteRequest;
import br.com.adeweb.ordemservico.adapter.output.entities.ClienteEntity;
import br.com.adeweb.ordemservico.core.domain.model.Cliente;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClienteMapperTest {
    private static ClienteMapper clienteMapper;

    @BeforeAll
    public  static void setup(){
        clienteMapper = Mappers.getMapper(ClienteMapper.class);
    }

    @Test
    void deveMapearRequestParaDominio() {
        // Give: Um cliente request preenchido
        ClienteRequest request = new ClienteRequest();
        request.setNome("José");
        request.setEmail("jose@email.com");

        // When: Converter para domínio com mapper
        Cliente cliente = clienteMapper.toDomainFromRequest(request);

        // Then: Verificar se os campos foram mapeados corretamente
        assertEquals("José", cliente.getNome());
        assertEquals("jose@email.com", cliente.getEmail());
    }
    @Test
    void deveMapearEntidadeParaDominio(){
        // Given: ClienteEntity disponível
        ClienteEntity entity = new ClienteEntity(1L, "Maria", "maria@gmail.com");

        // When: Converter para domínio com mapper
        Cliente cliente = clienteMapper.toDomainEntity(entity);

        // Then: Verificar se os campos foram mapeados corretamente
        assertEquals(1L, cliente.getId());
        assertEquals("Maria", cliente.getNome());
        assertEquals("maria@gmail.com", cliente.getEmail());
    }

}
