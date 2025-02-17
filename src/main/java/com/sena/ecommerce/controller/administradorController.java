package com.sena.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sena.ecommerce.model.Producto;
import com.sena.ecommerce.service.IProductoService;

@RestController
@RequestMapping("/administrador")
public class administradorController {

	@Autowired
	private IProductoService productoService;

	@GetMapping("")
	public String home(Model model) {
		List<Producto> producto = productoService.findAll();
		model.addAttribute("productos", producto);
		return "administrador/home";
	}

}
