package com.finaltodocode.final_todocode.controllers;


import com.finaltodocode.final_todocode.dto.VentaClienteDTO;
import com.finaltodocode.final_todocode.model.Producto;
import com.finaltodocode.final_todocode.model.Venta;
import com.finaltodocode.final_todocode.model.VentaProducto;
import com.finaltodocode.final_todocode.service.IVentaService;
import com.finaltodocode.final_todocode.service.VentaService;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class VentaController {


    @Autowired
    private IVentaService ventaService;

    @PostMapping("/ventas/guardar")
    public void guardar(@RequestBody Venta venta) {
ventaService.guardar(venta);
    }//listo

    @DeleteMapping("/ventas/eliminar/{id}")
    public void eliminar(@PathVariable Long id) {//listo
ventaService.eliminar(id);

    }

    @PutMapping("/ventas/actualizar")//listo
    public void actualizar(@RequestBody Venta ventaActualizada) {
ventaService.actualizar(ventaActualizada);
    }

    @GetMapping("/ventas/buscarTodos")//listo
    @ResponseBody
    public List<Venta>buscarTodos() {
        return ventaService.buscarTodos();
    }


    //Modificar la lista de productos

    @PostMapping("/ventas/agregarProducto/{idVenta}/{idProducto}/{cantidadProducto}")//listo
    public void agregarProducto(@PathVariable Long idVenta, @PathVariable Long idProducto, @PathVariable int cantidadProducto) {
        ventaService.agregarProductos(idVenta, idProducto, cantidadProducto);
    }

    @DeleteMapping("/ventas/eliminarProducto/{idVenta}/{idProducto}")//listo
    public void eliminarProducto(@PathVariable Long idVenta, @PathVariable Long idProducto) {
        ventaService.eliminarProducto(idVenta, idProducto);

    }


    //--------------------------------------

   @GetMapping("/ventas/buscarTodosProductosDeUnaVenta/{idVenta}")
    @ResponseBody
    public List<Producto>lsTodosProductosDeUnaVenta(@PathVariable Long idVenta) {//Listo

return ventaService.buscarTodosProductosDeUnaVenta(idVenta);
    }


    @GetMapping("/ventas/sumatoriaDelTotalyNumeroVentasEnUnDia/{fecha}")
    @ResponseBody
    public String sumatoriaDelTotalyNumeroVentasEnUnDia(@PathVariable String fecha) {

return ventaService.sumatoriaDelTotalyNumeroVentasEnUnDia(fecha);

    }


    @GetMapping("/ventas/ventaMayor")
    @ResponseBody
    public List<VentaClienteDTO> ventaConMayorPrecio(){ //Listo
return ventaService.ventaMayor();
    }


}
