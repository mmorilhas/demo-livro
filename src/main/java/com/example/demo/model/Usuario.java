package com.example.demo.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_usuario")
public class Usuario implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pk_usuario")
	private Long id;

	@Column(unique = true)
	private String username;

	private String password;

	// Mapeamento dos perfis do usuário
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "tb_usuario_perfil", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "perfil_id"))
	private Set<Perfil> perfis = new HashSet<>();

	// Implementação dos métodos da interface UserDetails

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return perfis;
	}

	// Retorna a senha do usuário
	@Override
	public String getPassword() {
		return password;
	}

	// Retorna o nome de usuário (login)
	@Override
	public String getUsername() {
		return username;
	}

	// Indica se a conta não está expirada
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	// Indica se a conta não está bloqueada
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	// Indica se as credenciais (senha) não estão expiradas
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	// Indica se a conta está habilitada
	@Override
	public boolean isEnabled() {
		return true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<Perfil> getPerfis() {
		return perfis;
	}

	public void setPerfis(Set<Perfil> perfis) {
		this.perfis = perfis;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
