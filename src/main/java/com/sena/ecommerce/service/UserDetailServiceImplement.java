package com.sena.ecommerce.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sena.ecommerce.model.Usuario;

import jakarta.servlet.http.HttpSession;

@Service
public class UserDetailServiceImplement implements UserDetailsService {

	@Autowired
	private IUsuarioService usuarioService;

	// encriptador de contraseña
	private BCryptPasswordEncoder bCrypt;

	// guardado de sesion de usuario
	@Autowired
	HttpSession session;

	// logger - para evitar el usu del sistem.out.println
	private Logger log = LoggerFactory.getLogger(UserDetailServiceImplement.class);

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("Este es el username");
		Optional<Usuario> optionalUser = usuarioService.findByEmail(username);
		if (optionalUser.isPresent()) {
			log.info("Esto es el ID del usuario: {}", optionalUser.get().getId());
			session.setAttribute("idUsuario", optionalUser.get().getId());
			Usuario usuario = optionalUser.get();
			return User.builder().username(usuario.getNombre()).password(usuario.getPassword()).roles(usuario.getTipo())
					.build();
		} else {
			//exepcion
			throw new UsernameNotFoundException("usuario no encontrado");
		}
	}

}