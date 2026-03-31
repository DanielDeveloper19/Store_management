package com.finaltodocode.final_todocode.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Venta {

    //Cada venta posee una lista de productos y solo un cliente
    @Id
    private Long codigoVenta;
   private LocalDate fechaVenta;
   private Double totalVenta;

    @JsonManagedReference
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VentaProducto> productos = new ArrayList<>();

   @ManyToOne
   @JoinColumn(name = "cliente_id")
   private Cliente cliente;




}
