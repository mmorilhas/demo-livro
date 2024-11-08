package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Autor;
import com.example.demo.repository.AutorRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AutorService {

	private final AutorRepository repository;

	@Autowired
	public AutorService(AutorRepository repository) {
		this.repository = repository;
	}

	public List<Autor> listarTodos() {
		return repository.findAll();
	}

	public Autor buscarPorNome(String nome) {
		return repository.findByNomeIgnoreCase(nome)
				.orElseThrow(() -> new EntityNotFoundException("Nenhum autor encontrado com o nome '" + nome + "'."));
	}

	public Autor salvar(Autor autor) {
		return repository.save(autor);
	}

	public Autor atualizar(Long id, Autor atualizaAutor) {
		Autor autor = buscarPorId(id);
		autor.setNome(atualizaAutor.getNome());
		autor.setBiografia(atualizaAutor.getBiografia());
		return repository.save(autor);
	}

	public void deletarPorId(Long id) {
		repository.deleteById(id);
	}

	private Autor buscarPorId(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Autor com o id '" + id + "' não foi encontrado."));
	}
	
	public Autor criarAutor(String nome, String biografia) {
		
		Autor autor = new Autor();
		autor.setNome(nome);
		autor.setBiografia(biografia);
		
		return autor;
	}
}
