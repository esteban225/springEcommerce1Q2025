package com.sena.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sena.ecommerce.model.Orden;
import com.sena.ecommerce.model.Usuario;

@Repository
public interface IOrdenReository extends JpaRepository<Orden, Integer> {
	
	List<Orden> findByUsuario(Usuario usuario);
}
