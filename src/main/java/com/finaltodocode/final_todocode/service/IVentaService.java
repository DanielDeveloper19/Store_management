package com.finaltodocode.final_todocode.service;

import com.finaltodocode.final_todocode.dto.VentaClienteDTO;
import com.finaltodocode.final_todocode.model.Producto;
import com.finaltodocode.final_todocode.model.Venta;
import com.finaltodocode.final_todocode.model.VentaProducto;

import java.util.List;

public interface IVentaService {

    public void guardar(Venta venta);
    public void eliminar(Long idVenta);
    public void actualizar(Venta venta);
    public List<Venta> buscarTodos();
    public void agregarProductos(Long idVenta, Long idProducto, int cantidadProducto);
    public void eliminarProducto(Long idVenta, Long idProducto);

    public List<Producto> buscarTodosProductosDeUnaVenta(Long idVenta);
     public String sumatoriaDelTotalyNumeroVentasEnUnDia(String fecha);
    public List<VentaClienteDTO> ventaMayor();
}
