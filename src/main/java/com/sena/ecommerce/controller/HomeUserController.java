package com.sena.ecommerce.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

import com.sena.ecommerce.model.DetalleOrden;
import com.sena.ecommerce.model.Orden;
import com.sena.ecommerce.model.Producto;
import com.sena.ecommerce.service.IProductoService;

@Controller
@RequestMapping("/") // la raiz del proyecto
public class HomeUserController {

	// instancia del LOGGER para ver datos por consola
	private final Logger LOGGER = (Logger) LoggerFactory.getLogger(HomeUserController.class);

	// instancia de objeto - servicio
	@Autowired
	private IProductoService productoService;

	// dos variables
	// lista de detalles de la orden para guardarlos en la db
	List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();

	// objeto que almacena los datos de la orden
	Orden orden = new Orden();

	// metodo que mapea la vista de usuario en la raiz del proyecto
	@GetMapping("")
	public String home(Model model) {
		model.addAttribute("productos", productoService.findAll());
		return "usuario/home";
	}

	// metodo que carga el producto del usuario con el id
	@GetMapping("productoHome/{id}")
	public String productoHome(@PathVariable Integer id, Model model) {
		LOGGER.info("ID producto enviado como parametro {}", id);
		// variable de clase producto
		Producto p = new Producto();
		// objeto de tipo optional
		Optional<Producto> op = productoService.get(id);
		p = op.get();
		// enviar a la vista con el model los detalles del producto con el id
		model.addAttribute("producto", p);
		return "usuario/productoHome";
	}

	// metodo para enviar del boton del producto home al carrito
	@PostMapping("/cart")
	public String addCar(@RequestParam Integer id, @RequestParam Double cantidad, Model model) {
		DetalleOrden detaorden = new DetalleOrden();
		Producto p = new Producto();
		// variable que siempre que este en el metodo inicialida en cero despues de cada
		// coma
		double sumaTotal = 0;

		Optional<Producto> op = productoService.get(id);
		LOGGER.info("Producto añadido: {}", op.get());
		LOGGER.info("Cantidad añadida: {}", cantidad);

		p = op.get();
		detaorden.setCantidad(cantidad);
		detaorden.setPrecio(p.getPrecio());
		detaorden.setNombre(p.getNombre());
		detaorden.setTotal(p.getPrecio() * cantidad);
		detaorden.setProducto(p);
		// validacion para evitar duplicados de productos
		Integer idProducto = p.getId();
		// funciON LAMDA stram y funcion anonima con predicado anyMatch
		boolean insertado = detalles.stream().anyMatch(prod -> prod.getProducto().getId() == idProducto);
		// si no es true añade el producto
		if (!insertado) {
			// detalles
			detalles.add(detaorden);
		}

		// suma de totales de la lista que el usuario añada al carrito
		// funcion de lava 8 lamda stream
		// funcion java 8 anonima dt
		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();
		// pasar variables a la vista
		orden.setTotal(sumaTotal);
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		return "usuario/carrito";
	}

	// metodo para quitar productos del carrito
	@GetMapping("/delete/cart/{id}")
	public String deleteProductoCart(@PathVariable Integer id, Model model) {
		// lista nueva de productos
		List<DetalleOrden> ordenesNuevas = new ArrayList<DetalleOrden>();
		// quitar objeto de la lista de detalleOrden

		for (DetalleOrden detalleOrden : detalles) {
			if (detalleOrden.getProducto().getId() != id) {
				ordenesNuevas.add(detalleOrden);
			}
		}
		// poner la nueva lista con los productos restantes del carrito
		detalles = ordenesNuevas;
		// recalcular los productos del carrito
		double sumaTotal = 0;
		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();
		// pasar variables a la vista
		orden.setTotal(sumaTotal);
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		return "usuario/carrito";
	}

	// metodo para redirigir al carrito sun productos

	public String getCart(Model model) {
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		return "/usuario/carrito";
	}

}
