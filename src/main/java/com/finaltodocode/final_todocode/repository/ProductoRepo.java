package com.finaltodocode.final_todocode.repository;

import com.finaltodocode.final_todocode.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductoRepo extends JpaRepository<Producto, Long> {
}
