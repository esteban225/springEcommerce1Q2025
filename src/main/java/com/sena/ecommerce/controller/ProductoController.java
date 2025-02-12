package com.sena.ecommerce.controller;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.sena.ecommerce.model.Producto;
import com.sena.ecommerce.model.Usuario;
import com.sena.ecommerce.service.IProductoService;
import com.sena.ecommerce.service.IUsuarioService;
import com.sena.ecommerce.service.UploadFileService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/productos")
public class ProductoController {

	// instancia de LOGGER para ver datos en consola
	private final Logger LOGGER = (Logger) LoggerFactory.getLogger(ProductoController.class);

	@Autowired
	private IProductoService productoService;

	@Autowired
	private IUsuarioService usuarioService;

	// micro servicio imgs
	@Autowired
	private UploadFileService upload;
	// metodo para redirigir a la vista show en el template productos

	@GetMapping("")
	public String show(Model model) {
		model.addAttribute("productos", productoService.findAll());
		return "productos/show";
	}

	// metodo el que redirige a la vista de creacion de productos
	@GetMapping("/create")
	public String create() {
		return "productos/create";
	}

	// metodo de creacion de productos
	@PostMapping("/save")
	public String save(Producto producto, @RequestParam("img") MultipartFile file, HttpSession session)
			throws IOException {
		LOGGER.info("Este es el objeto del producto a guardar en la DB {}", producto);
		Usuario u = usuarioService.findById(Integer.parseInt(session.getAttribute("idUsuario").toString())).get();
		producto.setUsuario(u);
		// validacion imagen de producto
		if (producto.getId() == null) {
			String nombreImagen = upload.saveImages(file, producto.getNombre());
			producto.setImagen(nombreImagen);
		}
		productoService.save(producto);
		return "redirect:/productos";

	}

	// meodo para llemar los imputs de la vista edit
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Integer id, Model model) {
		Producto p = new Producto();
		Optional<Producto> op = productoService.get(id);
		p = op.get();
		LOGGER.info("Busqueda de producto por id {}", p);
		model.addAttribute("producto", p);
		return "productos/edit";
	}

	// metodo para actualizar los datos de un producto
	@PostMapping("/update")
	public String update(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {
		LOGGER.info("Este es el objeto del producto a actualizar el DB {}", producto);
		Producto p = new Producto();
		p = productoService.get(producto.getId()).get();
		if (file.isEmpty()) {
			producto.setImagen(p.getImagen());
		} else {
			if (!p.getImagen().equals("defaul.jpg")) {
				upload.deleteImage(p.getImagen());
			}
			String nombreImagen = upload.saveImages(file, p.getNombre());
			producto.setImagen(nombreImagen);
		}
		producto.setUsuario(p.getUsuario());
		productoService.update(producto);
		return "redirect:/productos";
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id, Model model) {
		Producto p = new Producto();
		p = productoService.get(id).get();
		if (!p.getImagen().equals("defaul.jpg")) {
			upload.deleteImage(p.getImagen());
		}
		productoService.delete(id);
		return "redirect:/productos";

	}

}
