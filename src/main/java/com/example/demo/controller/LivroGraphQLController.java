package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.example.demo.service.LivroService;

@Controller
public class LivroGraphQLController {

	private final LivroService livroService;
	
	@Autowired
	public LivroGraphQLController(LivroService livroService) {
		this.livroService = livroService;
	}
	
	
	
	
}
