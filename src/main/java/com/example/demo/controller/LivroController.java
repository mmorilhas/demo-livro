package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Livro;
import com.example.demo.service.LivroService;

@RestController
@RequestMapping("/livros")
public class LivroController {

	private final LivroService service;

	@Autowired
	public LivroController(LivroService livroService) {
		this.service = livroService;
	}

	@GetMapping("/listar")
	@PreAuthorize("hasAnyAuthority('CLIENTE', 'ADMIN')")
	public ResponseEntity<List<Livro>> listarTodos() {

		return ResponseEntity.status(HttpStatus.OK).body(service.listarTodos());
	}

	@GetMapping("/buscar/{id}")
	@PreAuthorize("hasAnyAuthority('CLIENTE', 'ADMIN')")
	public ResponseEntity<Livro> buscarPorId(@PathVariable Long id) {
		return ResponseEntity.ok(service.buscarPorId(id));
	}

	@PostMapping("/cadastrar")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Livro> cadastrar(@RequestBody Livro livro) {

		service.salvar(livro);

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PutMapping("/atualizar/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Livro> atualizar(@PathVariable Long id, @RequestBody Livro livro) {

		return ResponseEntity.ok(service.atualizar(id, livro));
	}

	@DeleteMapping("/deletar/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Void> deletar(@PathVariable Long id) {

		service.deletarPorId(id);
		return ResponseEntity.noContent().build();
	}
}
