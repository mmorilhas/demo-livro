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

import com.example.demo.model.Genero;
import com.example.demo.model.Livro;
import com.example.demo.service.GeneroService;

@RestController
@RequestMapping("/genero")
public class GeneroController {

	private final GeneroService service;

	@Autowired
	public GeneroController(GeneroService service) {
		this.service = service;
	}

	@GetMapping("/listar")
	@PreAuthorize("hasAnyAuthority('CLIENTE', 'ADMIN')")
	public ResponseEntity<List<Genero>> listarTodos() {

		return ResponseEntity.status(HttpStatus.OK).body(service.listarTodos());
	}

	@GetMapping("/buscar/{nome}")
	@PreAuthorize("hasAnyAuthority('CLIENTE', 'ADMIN')")
	public ResponseEntity<Genero> buscarPorId(@PathVariable String nome) {
		return ResponseEntity.ok(service.buscarPorNome(nome));
	}

	@PostMapping("/cadastrar")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Genero> cadastrar(@RequestBody Genero genero) {

		service.salvar(genero);

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PutMapping("/atualizar/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Genero> atualizar(@PathVariable Long id, @RequestBody Genero genero) {

		return ResponseEntity.ok(service.atualizar(id, genero));
	}

	@DeleteMapping("/deletar/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Void> deletar(@PathVariable Long id) {

		service.deletarPorId(id);
		return ResponseEntity.noContent().build();
	}
}
