package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Genero;
import com.example.demo.model.Livro;
import com.example.demo.repository.GeneroRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class GeneroService {

	private final GeneroRepository repository;

	@Autowired
	public GeneroService(GeneroRepository repository) {
		this.repository = repository;
	}

	public List<Genero> listarTodos() {
		return repository.findAll();
	}

	public Genero buscarPorNome(String nome) {
		return repository.findByNomeIgnoreCase(nome)
				.orElseThrow(() -> new EntityNotFoundException("Nenhum gênero encontrado com o nome '" + nome + "'."));
	}

	public Genero salvar(Genero genero) {
		return repository.save(genero);
	}

	public Genero atualizar(Long id, Genero atualizaGenero) {
		Genero genero = buscarPorId(id);
		genero.setNome(atualizaGenero.getNome());
		return repository.save(genero);
	}

	public void deletarPorId(Long id) {
		repository.deleteById(id);
	}

	private Genero buscarPorId(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Gênero com o id '" + id + "' não foi encontrado."));
	}
	
	public Genero criarGenero(String nome) {
		Genero genero = new Genero();
		genero.setNome(nome);
		return genero;
		
	}
	
	
}
