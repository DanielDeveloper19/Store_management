package com.finaltodocode.final_todocode.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VentaClienteDTO {

   private Long idVenta;
   private Double totalVenta;
   private int cantidadProductos;
   private String nombreCliente;
   private String apellidoCliente;



}
