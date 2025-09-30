package br.com.adeweb.ordemservico.core.usecase;

import br.com.adeweb.ordemservico.core.domain.model.OrdemServico;
import br.com.adeweb.ordemservico.core.exception.EntidadeNaoEncontradaExecption;
import br.com.adeweb.ordemservico.port.output.OrdemServicoOutputPort;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class OrdemServicoUseCaseTest {
    @Mock
    private OrdemServicoOutputPort outputPort;
    //Mock de dependências

    @InjectMocks
    private OrdemServicoUseCase ordemServicoUseCase;
    //Classe a ser testada

    @Test
    void deveRetornarListaDeOrdensServico(){
        // Given - Prepara lista de ordens de serviço mockadas(simuladas)
        OrdemServico os1  = new OrdemServico();
        OrdemServico os2  = new OrdemServico();
        List<OrdemServico> ordens = List.of(os1, os2);
        Page<OrdemServico> pageOrdens = new PageImpl<>(ordens);

         //Configura mock para retornar a lista simaluda
        when(outputPort.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(ordens));

        ////When - Executa metdo que sera testado
        Page<OrdemServico> resultado = ordemServicoUseCase.findAll(Pageable.unpaged());

        //Then - Verifica se o resultado não é nulo e contém a lista mockada
        assertNotNull(resultado);
        assertEquals(2, resultado.getContent().size());
        verify(outputPort,times(1)).findAll(any(Pageable.class));

    }
    @Test
    void deveRetornarOrdemServicoPorIdQuandoExistir(){
        // Given - Prepara uma ordem de serviço mockada
        OrdemServico os = new OrdemServico();
        os.setId(1L);

        // Testa que o metodo findById retorna objeto OrdemServico correto quando existe
        when(outputPort.findById(1L)).thenReturn(Optional.of(os));

        // When - Executa o metodo que sera testado

        // Verifica se a consulta pelo ID chama o metodo do outputPort exatamente uma vez
        OrdemServico resultado = ordemServicoUseCase.findById(1L);

        // Then - Verifica se o resultado não é nulo e corresponde a ordem de serviço mockada

        //Garante que o resultado não seja nulo e o ID corresponda ao esperado
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(outputPort, times(1)).findById(1L);

    }
    @Test
    void deveLancarExcecaoQuandoServicoNaoEncontrado() {
        // Given (Dado) - Configura o mock para retornar Optional vazio simulando que a ordem não existe
        when(outputPort.findById(1L)).thenReturn(Optional.empty());

        //When (Quando):Chama o metodo que deve lancar excecao
        EntidadeNaoEncontradaExecption exception = assertThrows(EntidadeNaoEncontradaExecption.class, () -> {
            ordemServicoUseCase.findById(1L);
        });

        //Then (Entao): Verifica se a excecao foi lancada com a mensagem correta

        assertTrue(exception.getMessage().contains("Ordem Serviço Não encontrado"));
        verify(outputPort, times(1)).findById(1L);
    }

}
