package com.sena.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//esta clase es el interceptor con dos anotaciones jpa
@Configuration
public class SpringBootSecurity {

	// objeto inyectado a la clase
	@Autowired
	private UserDetailsService userDetailsService;

	// validar que el usuario sea el correcto
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(getEnecoder());

	}

	// metodo para restringir vistas al usuario
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.requestMatchers("/administrador/").hasRole("ADMIN").requestMatchers("/productos/")
				.hasRole("ADMIN").and()
				// csrf es el que evita que inyecten codigo malisioso a la aplicación
				.csrf(csfr -> csfr.disable()).formLogin().loginPage("/usuario/login").permitAll()
				.defaultSuccessUrl("/usuario/acceder").and().logout().permitAll();
		return http.build();
	}

	@Bean
	public BCryptPasswordEncoder getEnecoder() {
		return new BCryptPasswordEncoder();
	}
}