package br.com.adeweb.ordemservico.adapter.input.controller;

import br.com.adeweb.ordemservico.adapter.input.mapper.OrdemServicoMapper;
import br.com.adeweb.ordemservico.adapter.input.request.OrdemServicoRequest;
import br.com.adeweb.ordemservico.adapter.input.response.OrdemServicoResponse;
import br.com.adeweb.ordemservico.core.domain.model.OrdemServico;
import br.com.adeweb.ordemservico.port.input.OrdemServicoInputPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("ordemservico")
public class OrdemServicoController implements IOrdemServicoController {
    private final OrdemServicoInputPort ordemServicoInputPort;
    private final OrdemServicoMapper ordemServicoMapper;

    public OrdemServicoController(OrdemServicoInputPort ordemServicoInputPort, OrdemServicoMapper ordemServicoMapper) {
        this.ordemServicoInputPort = ordemServicoInputPort;
        this.ordemServicoMapper = ordemServicoMapper;
    }
    @Override
    public ResponseEntity<Page<OrdemServicoResponse>> getAll(
            Integer pageNumber,
            Integer size
    ){
        Pageable pageable = PageRequest.of(pageNumber, size);
        Page<OrdemServicoResponse> responses = ordemServicoInputPort.findAll(pageable)
                .map(ordemServicoMapper::toResponse);
        return ResponseEntity.ok(responses);
    }

    @Override
    public ResponseEntity<OrdemServicoResponse> byId(Long id) {
        OrdemServico ordemServico = ordemServicoInputPort.findById(id);
        return  ResponseEntity.ok(ordemServicoMapper.toResponse(ordemServico));
    }
    @Override
    public ResponseEntity<OrdemServicoResponse> save(OrdemServicoRequest ordemServicoRequest){
        OrdemServico ordemServico = ordemServicoMapper.toDaminFromRequest(ordemServicoRequest);
        OrdemServico osSalvo = ordemServicoInputPort.save(ordemServico);
        OrdemServicoResponse response = ordemServicoMapper.toResponse(osSalvo);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<OrdemServicoResponse> update(Long id, OrdemServicoRequest ordemServicoRequest){
        OrdemServico ordemServico = ordemServicoMapper.toDaminFromRequest(ordemServicoRequest);
        OrdemServico osSalvo = ordemServicoInputPort.update(id,ordemServico);
        OrdemServicoResponse response = ordemServicoMapper.toResponse(osSalvo);
        return ResponseEntity.ok(response);
    }



}
