package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import com.example.demo.model.Livro;
import com.example.demo.service.LivroService;

@Controller
public class LivroGraphQLController {

	private final LivroService livroService;

	@Autowired
	public LivroGraphQLController(LivroService livroService) {
		this.livroService = livroService;
	}

	@QueryMapping
	@PreAuthorize("hasAnyAuthority('CLIENTE', 'ADMIN')")
	public List<Livro> listarLivros() {
		return livroService.listarTodos();
	}

	@QueryMapping
	@PreAuthorize("hasAnyAuthority('CLIENTE', 'ADMIN')")
	public Livro buscarLivroPorId(@Argument Long id) {
		return livroService.buscarPorId(id);
	}

	@MutationMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public Livro criarLivro(@Argument String titulo, @Argument String autor) {
		Livro livro = new Livro();
		livro.setTitulo(titulo);
		livro.setAutor(autor);
		return livroService.salvar(livro);
	}

	@MutationMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public Livro atualizarLivro(@Argument Long id, @Argument String titulo, @Argument String autor) {
		Livro livroAtualizado = new Livro();
		livroAtualizado.setTitulo(titulo);
		livroAtualizado.setAutor(autor);
		return livroService.atualizar(id, livroAtualizado);
	}

	@MutationMapping
	@PreAuthorize("hasAuthority('ADMIN')")
	public Boolean deletarLivro(@Argument Long id) {
		livroService.deletarPorId(id);
		return true;
	}
}
