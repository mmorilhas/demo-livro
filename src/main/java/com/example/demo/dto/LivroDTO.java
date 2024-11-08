package com.example.demo.dto;

import java.util.List;

import com.example.demo.model.Livro;

public record LivroDTO(

		Long id, String titulo, List<String> autores, String genero

) {

	public LivroDTO(Livro livro) {
		this(livro.getId(), livro.getTitulo(), livro.getAutores().stream().map(autor -> autor.getNome()).toList(),
				livro.getGenero().getNome());
	}

}
