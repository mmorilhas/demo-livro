package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.LivroDTO;
import com.example.demo.model.Autor;
import com.example.demo.model.Genero;
import com.example.demo.model.Livro;
import com.example.demo.repository.LivroRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class LivroService {

	private final LivroRepository livroRepository;
	private final AutorService autorService;
	private final GeneroService generoService;


	@Autowired
	public LivroService(LivroRepository livroRepository, AutorService autorService, GeneroService generoService) {
		this.livroRepository = livroRepository;
		this.autorService = autorService;
		this.generoService = generoService;
	}

	public List<LivroDTO> listarTodos() {
		
		return livroRepository.findAll().stream()
				.map(LivroDTO::new) // Converte Livro para LivroDTO
				.toList();
		
	}
	
	
	public List<Livro> listarLivros() {
		return livroRepository.findAll();
	}

	public Livro buscarPorId(Long id) {
		return livroRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Livro com o id '" + id + "' não foi encontrado."));
	}

	public Livro salvar(Livro livro) {
		
		List<Autor> autores = livro.getAutores().stream()
				.map(autor -> autorService.buscarPorNome(autor.getNome()))
				.toList();
		
		Genero genero = generoService.buscarPorNome(livro.getGenero().getNome());
		
		livro.setAutores(autores);
		livro.setGenero(genero);
		
		return livroRepository.save(livro);
	}

	public Livro atualizar(Long id, Livro atualizaLivro) {
		Livro livro = buscarPorId(id);
		livro.setTitulo(atualizaLivro.getTitulo());
		livro.setAutores(atualizaLivro.getAutores());
		livro.setGenero(atualizaLivro.getGenero());
		return livroRepository.save(livro);
	}

	public void deletarPorId(Long id) {
		livroRepository.deleteById(id);
	}
	
	public Livro criarLivro(String titulo, List<String> autoresNome, String generoNome ) {
		
		Livro livro = new Livro();
		
		List<Autor> autores =  autoresNome.stream()
				.map(autorService::buscarPorNome)
				.toList();
		
		
		Genero genero = generoService.buscarPorNome(generoNome);
		
		livro.setAutores(autores);
		livro.setGenero(genero);
		livro.setTitulo(titulo);
		
		return livro;
	}
}
