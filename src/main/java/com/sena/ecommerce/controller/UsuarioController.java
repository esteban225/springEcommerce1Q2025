package com.sena.ecommerce.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sena.ecommerce.model.Orden;
import com.sena.ecommerce.model.Usuario;
import com.sena.ecommerce.service.IOrdenService;
import com.sena.ecommerce.service.IUsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	private final Logger LOGGER = (Logger) LoggerFactory.getLogger(UsuarioController.class);

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private IOrdenService ordenService;

	// nuevo objeto encryptador
	BCryptPasswordEncoder passEncode = new BCryptPasswordEncoder();

	@GetMapping("/registro")
	public String createUser() {
		return "usuario/registro";
	}

	@PostMapping("/save")
	public String save(Usuario usuario, Model model) {
		LOGGER.info("Usuario a registrar: {}", usuario);
		usuario.setTipo("USER");

		// encriptador de contraseña
		usuario.setPassword(passEncode.encode(usuario.getPassword()));
		usuarioService.save(usuario);
		return "redirect:/";
	}

	// metodo de redireccion al login
	@GetMapping("/login")
	public String login() {
		return "usuario/login";
	}

	// metodo de acceso de usuario
	@GetMapping("/acceder")
	public String acceder(Usuario usuario, HttpSession session) {
		LOGGER.info("Accesos: {}", usuario);
		// Optional<Usuario> userEmail = usuarioService.findByEmail(usuario.getEmail());
		Optional<Usuario> user = usuarioService
				.findById(Integer.parseInt(session.getAttribute("idUsuario").toString()));
		LOGGER.info("Usuario db obtenido: {}", user.get());
		if (user.isPresent()) {
			session.setAttribute("idUsuario", user.get().getId());
			if (user.get().getTipo().equals("ADMIN")) {
				return "redirect:/administrador";
			} else {
				return "redirect:/";
			}
		} else {
			LOGGER.warn("Usuario no existe en db");
		}
		return "redirect:/";
	}

	// metodo para cerrar secion
	@GetMapping("/cerrar")
	public String serrarSesion(HttpSession session) {
		session.removeAttribute("idUsuario");
		return "redirect:/";
	}

	// metodo para dedirigir a la vista de compras del usuario
	@GetMapping("/compras")
	public String compras(HttpSession session, Model model) {
		model.addAttribute("sesion", session.getAttribute("idUsuario"));
		Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idUsuario").toString())).get();
		List<Orden> ordenes = ordenService.findByUsuario(usuario);
		model.addAttribute("ordenes", ordenes);
		return "usuario/compras";
	}

	@GetMapping("/detalle/{id}")
	public String detalleCompra(HttpSession session, Model model, @PathVariable Integer id) {

		LOGGER.info("Id orden: {}", id);

		Optional<Orden> orden = ordenService.findById(id);
		model.addAttribute("detalles", orden.get().getDetalle());

		// sesión de usuario o id usuario
		model.addAttribute("sesion", session.getAttribute("idUsuario"));
		return "usuario/detallecompra";
	}

}
