package com.finaltodocode.final_todocode.service;

import com.finaltodocode.final_todocode.model.Producto;

import java.util.List;

public interface IProductoService {

    public void guardar(Producto producto);
    public void eliminar(Long idProducto);
    public List<Producto> buscarTodos();
    public void actualizar(Producto producto);
    public void sumarUnidades(Long codigoProducto, int unidadesASumar);


    public List<Producto> buscarProductosConCantidadDisponible(int cantidadDisponible, String operadorLogico);



}
