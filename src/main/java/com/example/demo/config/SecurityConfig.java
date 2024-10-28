package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.security.JwtAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	@Autowired
	public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				// Desativa o CSRF (não necessário para APIs RESTful)
				.csrf(csrf -> csrf.disable())

				// Define a política de gerenciamento de sessões como Stateless
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				// Configura as regras de autorização
				.authorizeHttpRequests(authorize -> authorize
						// Permite acesso sem autenticação aos endpoints de autenticação
						.requestMatchers("/auth/**").permitAll().requestMatchers("/graphiql").permitAll()
						.requestMatchers("/graphql").authenticated()
						// Exige autenticação para qualquer outra requisição
						.anyRequest().authenticated())

				// Configura o tratamento de exceções
				.exceptionHandling(
						exception -> exception.authenticationEntryPoint((request, response, authException) -> response
								.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Acesso não autorizado")))

				// Adiciona o filtro JWT antes do filtro de autenticação padrão
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	// Bean para o AuthenticationManager
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}
