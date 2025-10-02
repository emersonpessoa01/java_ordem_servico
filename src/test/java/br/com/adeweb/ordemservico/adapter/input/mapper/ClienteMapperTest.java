package br.com.adeweb.ordemservico.adapter.input.mapper;


import br.com.adeweb.ordemservico.adapter.input.request.ClienteRequest;
import br.com.adeweb.ordemservico.adapter.output.entities.ClienteEntity;
import br.com.adeweb.ordemservico.core.domain.model.Cliente;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

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
    @Test
    void deveMapearListaDeEntidadesParaListaDeDominio(){
        // Given: Lista de entidades para converter
        List<ClienteEntity> entidades = List.of(
                new ClienteEntity(1L, "Ana","ana@gmail.com"),
                new ClienteEntity(2L, "Bruno","bruno@gmail.com")
        );

        // When: Converter para lista de dominio
        List<Cliente> clientes = clienteMapper.toDomainList(entidades);

        // Then: validar tamanho e dados mapeados
        assertEquals(2, clientes.size());
        assertEquals("Ana", clientes.get(0).getNome());
        assertEquals("Bruno", clientes.get(1).getNome());
    }
    @Test
    void deveMapearDominioParaRequest() {
        // Given: Objeto de domínio Cliente
        Cliente cliente = new Cliente(1L, "Lucas", "lucas@email.com");

        // When: Converter para Request
        ClienteRequest request = clienteMapper.toRequest(cliente);

        // Then: Validar campos
        assertEquals("Lucas", request.getNome());
        assertEquals("lucas@email.com", request.getEmail());
    }

}
