package br.com.adeweb.ordemservico.adapter.input.mapper;


import br.com.adeweb.ordemservico.adapter.input.request.ClienteRequest;
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
        ClienteRequest request = new ClienteRequest();
        request.setNome("José");
        request.setEmail("jose@email.com");

        Cliente cliente = clienteMapper.toDomainFromRequest(request);

        assertEquals("José", cliente.getNome());
        assertEquals("jose@email.com", cliente.getEmail());
    }

}
