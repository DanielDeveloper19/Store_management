package com.finaltodocode.final_todocode.repository;

import com.finaltodocode.final_todocode.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface VentaRepo extends JpaRepository<Venta, Long> {

    List<Venta> findByFechaVenta(LocalDate fechaVenta);

    @Query("SELECT v FROM Venta v WHERE v.totalVenta = (SELECT MAX(v2.totalVenta) FROM Venta v2)")
    List<Venta> findVentasConMontoMaximo();


}
