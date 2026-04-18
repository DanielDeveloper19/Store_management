package com.finaltodocode.final_todocode.serviceTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finaltodocode.final_todocode.integrationTestsConfig.BaseIntegrationTest;
import com.finaltodocode.final_todocode.model.Cliente;
import com.finaltodocode.final_todocode.model.Producto;
import com.finaltodocode.final_todocode.model.Venta;
import com.finaltodocode.final_todocode.model.VentaProducto;
import com.finaltodocode.final_todocode.repository.ClienteRepo;
import com.finaltodocode.final_todocode.repository.ProductoRepo;
import com.finaltodocode.final_todocode.repository.VentaRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VentaServiceIT extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductoRepo productoRepo;

    @Autowired
    private VentaRepo ventaRepo;

    @Autowired
    private ClienteRepo clienteRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Rollback: Si la venta falla, el stock del producto no debe cambiar")
    void testRollbackVentaFalla() throws Exception {
        // 1. Preparar el escenario: Un producto con 10 unidades
        Producto p = new Producto();
        p.setNombre("Producto Test");
        p.setPrecio(100.0);
        p.setCantidadDisponible(10.0);
        p = productoRepo.save(p);

        // 2. Crear una venta que fallará (sin Cliente, asumiendo que es obligatorio)
        VentaProducto vp = new VentaProducto();
        vp.setProducto(p);
        vp.setCantidadAComprar(5);

        Venta ventaInvalida = new Venta();
        ventaInvalida.setProductos(List.of(vp));
        ventaInvalida.setFechaVenta(LocalDate.now());
        // NO seteamos el cliente para forzar el error en la DB al final

        // 3. Ejecutar el POST
        mockMvc.perform(post("/ventas/guardar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ventaInvalida)))
                .andExpect(status().isInternalServerError()); // Esperamos un error 500

        // 4. VERIFICACIÓN CRÍTICA (El alma del test)
        // El stock debería seguir siendo 10, no 5.
        Producto productoPostFalla = productoRepo.findById(p.getCodigoProducto()).get();
        assertEquals(10.0, productoPostFalla.getCantidadDisponible(),
                "El stock no debió reducirse porque la transacción debió hacer Rollback");

        // La venta no debe existir en la DB
        assertEquals(0, ventaRepo.count());
    }

    @Test
    @Transactional
    @DisplayName("Éxito: La venta se guarda y el stock se actualiza correctamente")
    void testGuardarVentaExito() throws Exception {
        // Arrange
        Producto p = productoRepo.save(new Producto(null, "Laptop", "Marca", 1000.0, 5.0));
        Cliente c = clienteRepo.save(new Cliente(null, "Aizen", "Sousa", "123"));

        Venta v = new Venta();
        v.setCliente(c);
        v.setFechaVenta(LocalDate.now());
        VentaProducto vp = new VentaProducto(null, v, p, 2); // Compra 2 de 5
        v.setProductos(List.of(vp));

        // Act
        mockMvc.perform(post("/ventas/guardar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(v)))
                .andExpect(status().isOk());

        // Assert
        Producto productoActualizado = productoRepo.findById(p.getCodigoProducto()).get();
        assertEquals(3.0, productoActualizado.getCantidadDisponible()); // 5 - 2 = 3
        assertEquals(1, ventaRepo.count());
    }

    @Test
    @DisplayName("Safety Drill: Rollback total cuando el Cliente no existe en la DB")
    void testVentaConClienteInexistenteRollback() throws Exception {
        // 1. Preparar un producto real en la DB
        Producto p = productoRepo.save(new Producto(null, "Smartphone", "Tech", 500.0, 10.0));

        // 2. Crear JSON con un ID de cliente que NO existe (ej. 999)
        // Usamos un String manual o el ObjectMapper para construir el objeto
        String ventaJson = """
            {
                "fechaVenta": "2026-04-16",
                "cliente": {
                    "idCliente": 999 
                },
                "productos": [
                    {
                        "producto": { "codigoProducto": %d },
                        "cantidadAComprar": 1
                    }
                ]
            }
        """.formatted(p.getCodigoProducto());

        // 3. Ejecutar el POST al endpoint real
        mockMvc.perform(post("/ventas/guardar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ventaJson))
                .andExpect(status().isInternalServerError()); // Debería fallar por el orElseThrow o FK violation

        // 4. VERIFICACIÓN DE INTEGRIDAD
        // El stock del producto debe seguir siendo 10.0
        Producto productoEnDB = productoRepo.findById(p.getCodigoProducto()).get();
        assertEquals(10.0, productoEnDB.getCantidadDisponible(),
                "El stock no debió tocarse porque el cliente no existía.");

        // No debe haber ninguna venta registrada
        assertEquals(0, ventaRepo.count(), "No debe haber registros de venta huérfanos.");
    }


}
