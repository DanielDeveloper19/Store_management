package com.finaltodocode.final_todocode.controllers;


import com.finaltodocode.final_todocode.model.Producto;
import com.finaltodocode.final_todocode.service.IProductoService;
import com.finaltodocode.final_todocode.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductoController {

@Autowired
    private IProductoService productoService;

@PostMapping("/productos/guardar")
    public void guardar(@RequestBody Producto producto) {
productoService.guardar(producto);
    }

    @DeleteMapping("/productos/eliminar/{id}")
    public void eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
    }

    @PutMapping("/productos/actualizar")
    public void actualizar(@RequestBody Producto productoActualizado) {
        productoService.actualizar(productoActualizado);

    }

    @GetMapping("/productos/buscarTodos")
    @ResponseBody
    public List<Producto> buscarTodos() {
    return productoService.buscarTodos();
    }

    //Aquí añado más unidades a un producto, recargo stock
    @PutMapping("/productos/sumarUnidades/{codigoProducto}/{unidadesASumar}")
    public void anadirMasUnidades(@PathVariable Long codigoProducto, @PathVariable int unidadesASumar) {
    productoService.sumarUnidades(codigoProducto, unidadesASumar);
}


    //Este método permitirá buscar todos los productos que cumplan con una condición numérica comparada con
    //operadores lógicos, los cuales evaluarán la cantidad disponible de cada producto y traera los productos que cumplan con la condición
    @GetMapping("/productos/buscarPorCantidadDisponible/{cantidadDisponible}/{operadorLogico}")
    @ResponseBody
    public List<Producto> buscarProductosConCantidadDisponible(@PathVariable int cantidadDisponible, @PathVariable String operadorLogico) {
        return productoService.buscarProductosConCantidadDisponible(cantidadDisponible, operadorLogico);

    }//Listo


}
