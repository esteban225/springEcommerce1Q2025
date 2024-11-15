package com.sena.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeUserController {

	@GetMapping("")
	public String home() {
		return "usuario/home";
	}
}
