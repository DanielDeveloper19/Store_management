package com.finaltodocode.final_todocode.service;

import com.finaltodocode.final_todocode.model.Cliente;
import com.finaltodocode.final_todocode.model.Producto;
import com.finaltodocode.final_todocode.repository.ProductoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductoService implements IProductoService {

    @Autowired
    private ProductoRepo productoRepo;

    @Override
    public void guardar(Producto producto) {
productoRepo.save(producto);
    }

    @Override
    public void eliminar(Long idProducto) {
productoRepo.deleteById(idProducto);
    }

    @Override
    public List<Producto> buscarTodos() {
        return productoRepo.findAll();
    }

    @Override
    public void actualizar(Producto producto) {

        Producto productoBuscado= productoRepo.findById(producto.getCodigoProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (productoBuscado != null) { //Este abarca a todos lo if

            if(producto.getNombre() != null){
                productoBuscado.setNombre(producto.getNombre());
            }
            if(producto.getMarca() != null){
                productoBuscado.setMarca(producto.getMarca());
            }
            if(producto.getPrecio() != null){
                productoBuscado.setPrecio(producto.getPrecio());
            }
            if(producto.getCantidadDisponible() != null){
                productoBuscado.setCantidadDisponible(producto.getCantidadDisponible());
            }
            productoRepo.save(productoBuscado);

        }
    }

    @Override
    public void sumarUnidades(Long codigoProducto, int unidadesASumar) {

        Producto productoBuscado= productoRepo.findById(codigoProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado, asegurese de ingresar el id de un producto que ya esté en la base de datos"));

        if (productoBuscado != null) { //Este abarca a todos lo if
            productoBuscado.setCantidadDisponible(productoBuscado.getCantidadDisponible() + unidadesASumar);
            productoRepo.save(productoBuscado);
    }
}

      @Override
    public List<Producto> buscarProductosConCantidadDisponible(int cantidadDisponible, String operadorLogico){

        List<Producto> productos = productoRepo.findAll();

        if(productos.isEmpty()){
            throw new RuntimeException("No se encontraron productos en el inventario");
        }

        if (operadorLogico.equals(">")||operadorLogico.equals("<")||operadorLogico.equals(">=")||operadorLogico.equals("<=")||operadorLogico.equals("=") || operadorLogico.equals("!=")) { //Este abarca a todos lo if

            if (operadorLogico.equals("=")) {

                return productos.stream().filter(producto -> producto.getCantidadDisponible() == cantidadDisponible).toList();
            }

            if (operadorLogico.equals("!=")) {

                return productos.stream().filter(producto -> producto.getCantidadDisponible() != cantidadDisponible).toList();
            }

            if (operadorLogico.equals(">")) {

                return productos.stream().filter(producto -> producto.getCantidadDisponible() > cantidadDisponible).toList();
            }

            if (operadorLogico.equals("<")) {

                return productos.stream().filter(producto -> producto.getCantidadDisponible() < cantidadDisponible).toList();
            }

            if (operadorLogico.equals(">=")) {

                return productos.stream().filter(producto -> producto.getCantidadDisponible() >= cantidadDisponible).toList();
            }

            if (operadorLogico.equals("<=")) {

                return productos.stream().filter(producto -> producto.getCantidadDisponible() <= cantidadDisponible).toList();
            }


            throw new RuntimeException("Ningun producto cumple con la condición");

        }else{
            throw new RuntimeException("Operador lógico no reconocido" + operadorLogico); // En caso de que no se encuentre el operador lógico, se lanza una excepción
        }


}




}
