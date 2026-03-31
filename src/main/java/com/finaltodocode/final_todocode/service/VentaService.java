package com.finaltodocode.final_todocode.service;

import com.finaltodocode.final_todocode.dto.VentaClienteDTO;
import com.finaltodocode.final_todocode.model.Producto;
import com.finaltodocode.final_todocode.model.Venta;
import com.finaltodocode.final_todocode.model.VentaProducto;
import com.finaltodocode.final_todocode.repository.ClienteRepo;
import com.finaltodocode.final_todocode.repository.ProductoRepo;
import com.finaltodocode.final_todocode.repository.VentaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;


@Service
public class VentaService implements IVentaService {


    @Autowired
    private VentaRepo ventaRepo;

    @Autowired
    private ProductoRepo productoRepo;

    @Autowired
    private ClienteRepo clienteRepo;

    //Aquí doy el id de cada venta
    private Long idVenta = 258L;

    @Override
    public void guardar(Venta venta) {//Listo

        //Este método hacer que no se pueda incluir en una venta un producto que no exista en el inventario,
        // verifica que la cantidad disponible sea suficiente para la compra, y además actualiza la cantidad disponible de un producto directamente en su entidad después de realizar la compra
        List<VentaProducto> productos = venta.getProductos();

        if (productos.isEmpty()) {
            throw new RuntimeException("La venta debe contener al menos un producto");
        }

        for (VentaProducto producto : productos) {
            Producto productoBuscado = productoRepo.findById(producto.getProducto().getCodigoProducto())
                    .orElseThrow(() -> new RuntimeException("El producto con el código " + producto.getProducto().getCodigoProducto() + " no se encuentra en el inventario de productos"));

            if (producto.getCantidadAComprar() <= 0) {
                throw new RuntimeException("La cantidad de unidades a comprar debe ser mayor a cero en el producto con código " + producto.getProducto().getCodigoProducto());

            }

            if (productoBuscado.getCantidadDisponible() < producto.getCantidadAComprar()) { //Este abarca a todos lo if
                throw new RuntimeException("El producto con el código " + producto.getProducto().getCodigoProducto() + " solo tiene :" + productoBuscado.getCantidadDisponible() + " unidades disponibles" + " y usted desea comprar: " + producto.getCantidadAComprar() + " unidades");
            }


        }
        venta.setTotalVenta(0.0);
        for (VentaProducto producto : productos) {
            //Actualizamos la cantidad disponible de cada producto
            Producto productoBuscado = productoRepo.findById(producto.getProducto().getCodigoProducto()).orElse(null);
            productoBuscado.setCantidadDisponible(productoBuscado.getCantidadDisponible() - producto.getCantidadAComprar());

            //Le asignamos el idVenta a cada producto
            producto.setVenta(venta);

            //calculamos el total de la venta
            venta.setTotalVenta(venta.getTotalVenta() + (productoBuscado.getPrecio() * producto.getCantidadAComprar()));

            productoRepo.save(productoBuscado);


        }
        venta.setCodigoVenta(idVenta);
        ventaRepo.save(venta);
        idVenta++;
    }//El anterior método esta listo


    @Override
    public void eliminar(Long idVenta) {
        ventaRepo.deleteById(idVenta);
    }

    @Override
    public void actualizar(Venta venta) {


        Venta ventaBuscada = ventaRepo.findById(venta.getCodigoVenta())
                .orElseThrow(() -> new RuntimeException("Venta no encontrado, asegurese de ingresar el id de una venta que ya esté en la base de datos"));

        if (ventaBuscada != null) { //Este abarca a todos lo if

            if (venta.getFechaVenta() != null) {
                ventaBuscada.setFechaVenta(venta.getFechaVenta());
            }
            if (venta.getTotalVenta() != null) {
                throw new RuntimeException("El total de la venta no puede ser actualizado");
            }
            if (venta.getCliente() != null) {

                ventaBuscada.setCliente(clienteRepo.findById(venta.getCliente().getIdCliente()).orElseThrow(() -> new RuntimeException("El cliente con el id " + venta.getCliente().getIdCliente() + " no se encuentra en la base de Clientes, debe registrarlo primero")));
            }


            ventaRepo.save(ventaBuscada);

        }
    }

    @Override
    public List<Venta> buscarTodos() {
        return ventaRepo.findAll();
    } //listo

//Area para modificar los productos de las ventas ya realizadas

    @Override
    public void agregarProductos(Long idVenta, Long idProducto, int cantidadProducto) {//listo

        Venta ventaBuscada = ventaRepo.findById(idVenta)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada, asegurese de ingresar el id de una venta que ya esté en la base de datos"));

        if (ventaBuscada != null) { //Este abarca a todos lo if

            List<VentaProducto> productos = ventaBuscada.getProductos();

            VentaProducto productoVenta = new VentaProducto();
            productoVenta.setVenta(ventaBuscada);
            productoVenta.setProducto(productoRepo.findById(idProducto).orElse(null));
            productoVenta.setCantidadAComprar(cantidadProducto);

            productos.add(productoVenta);

            ventaBuscada.setProductos(productos);

            ventaRepo.save(ventaBuscada);
        }
        //----------------------------------------------------------------------

    }

    @Override
    public void eliminarProducto(Long idVenta, Long idProducto) { //listo

        Venta ventaBuscada = ventaRepo.findById(idVenta)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada, asegurese de ingresar el id de una venta que ya esté en la base de datos"));

        if (ventaBuscada != null) { //Este abarca a todos lo if

            List<VentaProducto> productos = ventaBuscada.getProductos();

            Boolean productoEncontrado = false;
            for (VentaProducto producto : productos) {


                if (Objects.equals(producto.getProducto().getCodigoProducto(), idProducto)) {
                    productos.remove(producto);
                    ventaBuscada.setProductos(productos);
                    productoEncontrado = true;
                    ventaRepo.save(ventaBuscada);
                }
            }
            if (productoEncontrado == false) {
                throw new RuntimeException("El producto con el código " + idProducto + " no se encuentra en la venta con el código " + idVenta);
            }


        }
    }


    @Override
    public List<Producto> buscarTodosProductosDeUnaVenta(Long idVenta) {

        Venta ventaBuscada = ventaRepo.findById(idVenta).orElseThrow(() -> new RuntimeException("Venta no encontrada, asegurese de ingresar el id de una venta que ya esté en la base de datos"));
        List<VentaProducto> productos = ventaBuscada.getProductos();

        List<Producto> productosEncontrados = new ArrayList<>();

        for (VentaProducto producto : productos) {
            productosEncontrados.add(producto.getProducto());
        }
        return productosEncontrados;

    }


    //Método para verificar que la fecha que se ingresa tenga un formato adecuado


    public static boolean esFormatoLocalDateValido(String fechaStr) {

        String formato = "yyyy-MM-dd";
        try {
            LocalDate.parse(fechaStr, java.time.format.DateTimeFormatter.ofPattern(formato));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }

    }

    @Override
    public String sumatoriaDelTotalyNumeroVentasEnUnDia(String fecha) {//listo

        if (esFormatoLocalDateValido(fecha)) {

            LocalDate localDate = LocalDate.parse(fecha);
            List<Venta> ventas = ventaRepo.findByFechaVenta(localDate);

            if (ventas.isEmpty()) {
                return "No se encontraron ventas en la fecha ingresada" + fecha;
            }

            int cantidadDeVentas = ventas.size();
            double sumatoriaDeLosTotales = 0.0;
            for (Venta venta : ventas) {
                sumatoriaDeLosTotales = sumatoriaDeLosTotales + venta.getTotalVenta();
            }

            return "En la fecha "+fecha+" se realizaron "+cantidadDeVentas+" ventas con un total de $"+sumatoriaDeLosTotales;
        }else{
            return "La fecha ingresada no tiene un formato adecuado, el formato debe ser yyyy-MM-dd";
        }


    }

    @Override
    public List<VentaClienteDTO> ventaMayor() {

        List<Venta> ventasTraidas = ventaRepo.findVentasConMontoMaximo();
        List<VentaClienteDTO> listVentaCliente= new ArrayList<>();

        for(Venta venta : ventasTraidas ){
            VentaClienteDTO clientVentaDto= new VentaClienteDTO();

            clientVentaDto.setIdVenta(venta.getCodigoVenta());
             clientVentaDto.setTotalVenta(venta.getTotalVenta());
             clientVentaDto.setCantidadProductos(venta.getProductos().size());
             clientVentaDto.setNombreCliente(venta.getCliente().getNombre());
              clientVentaDto.setApellidoCliente(venta.getCliente().getApellido());

            listVentaCliente.add(clientVentaDto);
        }

        return listVentaCliente;
    }
}

