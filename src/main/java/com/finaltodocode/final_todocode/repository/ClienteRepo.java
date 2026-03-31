package com.finaltodocode.final_todocode.repository;

import com.finaltodocode.final_todocode.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ClienteRepo extends JpaRepository<Cliente, Long> {
}
