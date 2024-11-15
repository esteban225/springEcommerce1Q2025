package com.sena.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sena.ecommerce.model.Usuario;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {
	Optional<Usuario> findByEmail(String email);

	Optional<Usuario> findByUsername(String username);
}
