package br.com.adeweb.ordemservico.adapter.input.controller;

import br.com.adeweb.ordemservico.adapter.input.mapper.OrdemServicoMapper;
import br.com.adeweb.ordemservico.adapter.input.request.OrdemServicoRequest;
import br.com.adeweb.ordemservico.adapter.input.response.OrdemServicoResponse;
import br.com.adeweb.ordemservico.core.domain.model.OrdemServico;
import br.com.adeweb.ordemservico.port.input.OrdemServicoInputPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrdemServicoControllerTest {

    @Mock
    private OrdemServicoInputPort ordemServicoInputPort;

    @Mock
    private OrdemServicoMapper ordemServicoMapper;

    @InjectMocks
    private OrdemServicoController controller;

    @Test
    void deveRetornarListaDeOrdemServico() {
        // GIVEN: preparar lista simulada de ordens e as respostas
        OrdemServico os1 = new OrdemServico();
        OrdemServico os2 = new OrdemServico();
        List<OrdemServico> ordens = List.of(os1, os2);
        Page<OrdemServico> pageOrdens = new PageImpl<>(ordens);

        OrdemServicoResponse resp1 = new OrdemServicoResponse();
        OrdemServicoResponse resp2 = new OrdemServicoResponse();
        List<OrdemServicoResponse> responses = List.of(resp1, resp2);
        Page<OrdemServicoResponse> pageResponses = new PageImpl<>(responses);

        Pageable pageable = PageRequest.of(0, 10);

        when(ordemServicoInputPort.findAll(pageable)).thenReturn(pageOrdens);
        when(ordemServicoMapper.toResponse(os1)).thenReturn(resp1);
        when(ordemServicoMapper.toResponse(os2)).thenReturn(resp2);

        // WHEN: chamar o metodo getAll do controller
        ResponseEntity<Page<OrdemServicoResponse>> responseEntity = controller.getAll(0, 10);

        // THEN: verifica status da resposta e conteúdo retornado
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
        assertEquals(2, responseEntity.getBody().getContent().size());

        verify(ordemServicoInputPort, times(1)).findAll(pageable);
        verify(ordemServicoMapper, times(1)).toResponse(os1);
        verify(ordemServicoMapper, times(1)).toResponse(os2);
    }

    @Test
    void deveRetornarOrdemServicoPorId() {
        // GIVEN: prepara a ordem de serviço retornada e o objeto resposta mapeado
        Long id = 1L;
        OrdemServico os = new OrdemServico();
        OrdemServicoResponse response = new OrdemServicoResponse();

        when(ordemServicoInputPort.findById(id)).thenReturn(os);
        when(ordemServicoMapper.toResponse(os)).thenReturn(response);

        // WHEN: chama o metodo byId do controller
        ResponseEntity<OrdemServicoResponse> responseEntity = controller.byId(id);

        // THEN: verifica status HTTP e corpo da resposta
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
        assertEquals(response, responseEntity.getBody());

        verify(ordemServicoInputPort, times(1)).findById(id);
        verify(ordemServicoMapper, times(1)).toResponse(os);
    }

    @Test
    void deveSalvarOrdemServico() {
        // GIVEN: cria request, converte para domínio, salva e mapeia resposta
        OrdemServicoRequest request = new OrdemServicoRequest();
        OrdemServico ordemServico = new OrdemServico();
        OrdemServico osSalva = new OrdemServico();
        OrdemServicoResponse response = new OrdemServicoResponse();

        when(ordemServicoMapper.toDaminFromRequest(request)).thenReturn(ordemServico);
        when(ordemServicoInputPort.save(ordemServico)).thenReturn(osSalva);
        when(ordemServicoMapper.toResponse(osSalva)).thenReturn(response);

        // WHEN: chama o método save do controller
        ResponseEntity<OrdemServicoResponse> responseEntity = controller.save(request);

        // THEN: verifica status CREATED e corpo da resposta
        assertEquals(201, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
        assertEquals(response, responseEntity.getBody());

        verify(ordemServicoMapper, times(1)).toDaminFromRequest(request);
        verify(ordemServicoInputPort, times(1)).save(ordemServico);
        verify(ordemServicoMapper, times(1)).toResponse(osSalva);
    }

    @Test
    void deveAtualizarOrdemServico() {
        // Given: Prepara request, converte para dominio,atualiza e mapeia resposta
        Long id = 1L;
        OrdemServicoRequest request = new OrdemServicoRequest();
        OrdemServico ordemServico = new OrdemServico();
        OrdemServico osSalva = new OrdemServico();
        OrdemServicoResponse response = new OrdemServicoResponse();

        when(ordemServicoMapper.toDaminFromRequest(request)).thenReturn(ordemServico);
        when(ordemServicoInputPort.update(id, ordemServico)).thenReturn(osSalva);
        when(ordemServicoMapper.toResponse(osSalva)).thenReturn(response);

        // When: Chama o metodo update do controller
        ResponseEntity<OrdemServicoResponse> responseEntity = controller.update(id, request);

        // Then: valida status Ok e corpo da resposta
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
        assertEquals(response, responseEntity.getBody());

        verify(ordemServicoMapper, times(1)).toDaminFromRequest(request);
        verify(ordemServicoInputPort, times(1)).update(id, ordemServico);
        verify(ordemServicoMapper, times(1)).toResponse(osSalva);


    }
}
