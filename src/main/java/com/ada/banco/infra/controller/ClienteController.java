package com.ada.banco.infra.controller;

import com.ada.banco.domain.model.Cliente;
import com.ada.banco.domain.usecase.cliente.BuscarClientePorCpf;
import com.ada.banco.domain.usecase.cliente.CadastrarNovoCliente;
import com.ada.banco.domain.usecase.cliente.ListarTodosOsClientes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final BuscarClientePorCpf buscarClientePorCpf;
    private final CadastrarNovoCliente cadastrarNovoCliente;

    private final ListarTodosOsClientes listarTodosOsClientes;

    public ClienteController(BuscarClientePorCpf buscarClientePorCpf,
                             CadastrarNovoCliente cadastrarNovoCliente,
                             ListarTodosOsClientes listarTodosOsClientes) {
        this.buscarClientePorCpf = buscarClientePorCpf;
        this.cadastrarNovoCliente = cadastrarNovoCliente;
        this.listarTodosOsClientes = listarTodosOsClientes;
    }

    @GetMapping("/{cpf:.+}")
    public ResponseEntity<Cliente> buscarCliente(@PathVariable String cpf) {
        Cliente cliente = buscarClientePorCpf.execute(cpf);
        return ResponseEntity.ok(cliente);
    }

    @PostMapping
    public ResponseEntity<Cliente> criarCliente(@RequestBody Cliente cliente) {
        try{
            cadastrarNovoCliente.execute(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cliente);
        }
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes() {
        List<Cliente> clientes = listarTodosOsClientes.execute();
        return ResponseEntity.ok(clientes);
    }
}