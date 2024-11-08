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

import com.example.demo.model.Autor;
import com.example.demo.service.AutorService;

@RestController
@RequestMapping("/autor")
public class AutorController {

	private final AutorService service;

	@Autowired
	public AutorController(AutorService service) {
		this.service = service;
	}

	@GetMapping("/listar")
	@PreAuthorize("hasAnyAuthority('CLIENTE', 'ADMIN')")
	public ResponseEntity<List<Autor>> listarTodos() {

		return ResponseEntity.status(HttpStatus.OK).body(service.listarTodos());
	}

	@GetMapping("/buscar/{nome}")
	@PreAuthorize("hasAnyAuthority('CLIENTE', 'ADMIN')")
	public ResponseEntity<Autor> buscarPorId(@PathVariable String nome) {
		return ResponseEntity.ok(service.buscarPorNome(nome));
	}

	@PostMapping("/cadastrar")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Autor> cadastrar(@RequestBody Autor autor) {

		service.salvar(autor);

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PutMapping("/atualizar/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Autor> atualizar(@PathVariable Long id, @RequestBody Autor autor) {

		return ResponseEntity.ok(service.atualizar(id, autor));
	}

	@DeleteMapping("/deletar/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Void> deletar(@PathVariable Long id) {

		service.deletarPorId(id);
		return ResponseEntity.noContent().build();
	}
}
