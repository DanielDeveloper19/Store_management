package com.finaltodocode.final_todocode.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor

public class VentaProducto {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

    @JsonBackReference
        @ManyToOne
        @JoinColumn(name = "venta_id")
        private Venta venta;

        @ManyToOne
        @JoinColumn(name = "producto_id")
        private Producto producto;

        @Column(nullable = false)
        private int cantidadAComprar;

    }




