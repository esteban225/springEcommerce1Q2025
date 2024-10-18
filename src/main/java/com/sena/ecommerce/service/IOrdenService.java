package com.sena.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.sena.ecommerce.model.Orden;
import com.sena.ecommerce.model.Usuario;

public interface IOrdenService {

	public Orden save(Orden orden);

	public List<Orden> findAll();

	public String generarNumeroOrden();

	public List<Orden> findByUsuario(Usuario usuario);

	public Optional<Orden> findById(Integer id);

}
