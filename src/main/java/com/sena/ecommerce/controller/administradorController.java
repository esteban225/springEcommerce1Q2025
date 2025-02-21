package com.sena.ecommerce.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sena.ecommerce.model.Orden;
import com.sena.ecommerce.model.Producto;
import com.sena.ecommerce.service.IOrdenService;
import com.sena.ecommerce.service.IProductoService;
import com.sena.ecommerce.service.IUsuarioService;

@Controller
@RequestMapping("/administrador")
public class administradorController {

	private final Logger LOGGER = (Logger) LoggerFactory.getLogger(administradorController.class);

	@Autowired
	private IProductoService productoService;

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private IOrdenService ordenService;

	@GetMapping("")
	public String home(Model model) {
		List<Producto> producto = productoService.findAll();
		model.addAttribute("productos", producto);
		return "administrador/home";
	}

	// metodo de listar los usuarios
	@GetMapping("/usuarios")
	public String Usuarios(Model model) {
		model.addAttribute("usuarios", usuarioService.findAll());
		return "administrador/usuarios";
	}

	// metodo de listar ordenes
	@GetMapping("/ordenes")
	public String ordenes(Model model) {
		model.addAttribute("ordenes", ordenService.findAll());
		return "administrador/ordenes";
	}

	// metodo detalles de una orden
	@GetMapping("/detalle/{id}")
	public String detalleorden(@PathVariable Integer id, Model model) {
		LOGGER.info("Id de la orden: {} ", id);
		Optional<Orden> orden = ordenService.findById(id);
		model.addAttribute("detalles", orden.get().getDetalle());
		return "administrador/detalleorden";
	}
}
