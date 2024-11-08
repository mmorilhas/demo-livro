package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import com.example.demo.model.Autor;
import com.example.demo.model.Genero;
import com.example.demo.model.Livro;
import com.example.demo.service.AutorService;
import com.example.demo.service.GeneroService;
import com.example.demo.service.LivroService;

@Controller
public class LivroGraphQLController {

	private final LivroService livroService;
	private final AutorService autorService;
	private final GeneroService generoService;

	@Autowired
	public LivroGraphQLController(LivroService livroService, AutorService autorService, GeneroService generoService) {
		this.livroService = livroService;
		this.autorService = autorService;
		this.generoService = generoService;
	}

	// Queries
	@QueryMapping
	@PreAuthorize("hasAnyAuthority('CLIENTE', 'ADMIN')")
	public List<Livro> listarLivros() {
		return livroService.listarLivros();
	}

	@QueryMapping
	@PreAuthorize("hasAnyAuthority('CLIENTE', 'ADMIN')")
	public Livro buscarLivroPorId(@Argument Long id) {
		return livroService.buscarPorId(id);
	}

	@QueryMapping
	@PreAuthorize("hasAnyAuthority('CLIENTE', 'ADMIN')")
	public List<Autor> listarAutores() {
		return autorService.listarTodos();
	}

	@QueryMapping
	@PreAuthorize("hasAnyAuthority('CLIENTE', 'ADMIN')")
	public Autor buscarAutorPorNome(@Argument String nome) {
		return autorService.buscarPorNome(nome);
	}

	@QueryMapping
	@PreAuthorize("hasAnyAuthority('CLIENTE', 'ADMIN')")
	public List<Genero> listarGeneros() {
		return generoService.listarTodos();
	}

	@QueryMapping
	@PreAuthorize("hasAnyAuthority('CLIENTE', 'ADMIN')")
	public Genero buscarGeneroPorNome(@Argument String nome) {
		return generoService.buscarPorNome(nome);
	}

	// Mutations
	@MutationMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public Livro cadastrarLivro(@Argument String titulo, @Argument List<String> autoresNomes,
			@Argument String generoNome) {
		Livro livro = livroService.criarLivro(titulo, autoresNomes, generoNome);

		return livroService.salvar(livro);
	}

	@MutationMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public Livro atualizarLivro(@Argument Long id, @Argument String titulo, @Argument List<String> autoresNomes,
			@Argument String generoNome) {

		Livro livro = livroService.criarLivro(titulo, autoresNomes, generoNome);

		return livroService.atualizar(id, livro);
	}

	@MutationMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public Boolean deletarLivro(@Argument Long id) {
		livroService.deletarPorId(id);
		return true;
	}

	@MutationMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public Autor cadastrarAutor(@Argument String nome, @Argument String biografia) {
		Autor autor = autorService.criarAutor(nome, biografia);

		return autorService.salvar(autor);
	}

	@MutationMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public Autor atualizarAutor(@Argument Long id, @Argument String nome, @Argument String biografia) {

		Autor autor = autorService.criarAutor(nome, biografia);

		return autorService.atualizar(id, autor);
	}

	@MutationMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public Boolean deletarAutor(@Argument Long id) {
		autorService.deletarPorId(id);
		return true;
	}

	@MutationMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public Genero cadastrarGenero(@Argument String nome) {
		Genero genero = generoService.criarGenero(nome);

		return generoService.salvar(genero);
	}

	@MutationMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public Genero atualizarGenero(@Argument Long id, @Argument String nome) {
		Genero genero = generoService.criarGenero(nome);

		return generoService.atualizar(id, genero);
	}

	@MutationMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public Boolean deletarGenero(@Argument Long id) {
		generoService.deletarPorId(id);
		return true;
	}

}
