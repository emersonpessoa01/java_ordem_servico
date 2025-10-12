package br.com.adeweb.ordemservico.adapter.input.controller;

import br.com.adeweb.ordemservico.adapter.input.request.ClienteRequest;
import br.com.adeweb.ordemservico.adapter.input.response.ClienteResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface IClienteController {

    @GetMapping
    ResponseEntity<Page<ClienteResponse>> getAll(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer size
    );

    @GetMapping("/{id}")
    ResponseEntity<ClienteResponse> byId(@PathVariable Long id);

    @PostMapping
    ResponseEntity<ClienteResponse> save(@RequestBody ClienteRequest clienteRequest);

    @PutMapping("/{id}")
    ResponseEntity<ClienteResponse> update(@PathVariable Long id, @RequestBody ClienteRequest clienteRequest);
}
