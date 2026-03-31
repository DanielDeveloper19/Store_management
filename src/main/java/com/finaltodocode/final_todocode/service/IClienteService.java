package com.finaltodocode.final_todocode.service;

import com.finaltodocode.final_todocode.model.Cliente;

import java.util.List;

public interface IClienteService {

    public void guardar(Cliente cliente);
    public void eliminar(Long idCliente);
    public List<Cliente> buscarTodos();
    public void actualizar(Cliente cliente);


}
