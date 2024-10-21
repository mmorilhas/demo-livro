package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Livro;
import com.example.demo.repository.LivroRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class LivroService {

	private final LivroRepository livroRepository;

	@Autowired
	public LivroService(LivroRepository livroRepository) {
		this.livroRepository = livroRepository;
	}

	public List<Livro> listarTodos() {
		return livroRepository.findAll();
	}

	public Livro buscarPorId(Long id) {
		return livroRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Livro com o id '" + id + "' não foi encontrado."));
	}

	public Livro salvar(Livro livro) {
		return livroRepository.save(livro);
	}

	public Livro atualizar(Long id, Livro atualizaLivro) {
		Livro livro = buscarPorId(id);
		livro.setTitulo(atualizaLivro.getTitulo());
		livro.setAutor(atualizaLivro.getAutor());
		return livroRepository.save(livro);
	}

	public void deletarPorId(Long id) {
		livroRepository.deleteById(id);
	}
}
