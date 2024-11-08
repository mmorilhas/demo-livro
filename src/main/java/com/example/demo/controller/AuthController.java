package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Perfil;
import com.example.demo.model.Usuario;
import com.example.demo.repository.PerfilRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.security.JwtTokenUtil;



@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UsuarioRepository usuarioRepository;

    private final PerfilRepository perfilRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public AuthController(
            AuthenticationManager authenticationManager,
            UsuarioRepository usuarioRepository,
            PerfilRepository perfilRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenUtil jwtTokenUtil
    ) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    // Classe DTO para receber os dados de autenticação
    public static class AuthRequest {
        public String username;
        public String password;
    }

    // Classe DTO para enviar o token JWT
    public static class AuthResponse {
        public String token;

        public AuthResponse(String token) {
            this.token = token;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Validated AuthRequest authRequest) {
        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    authRequest.username, authRequest.password);

            authenticationManager.authenticate(authentication);

            final Usuario userDetails = usuarioRepository.findByUsername(authRequest.username)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            final String token = jwtTokenUtil.generateToken(userDetails);

            return ResponseEntity.ok(new AuthResponse(token));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Validated AuthRequest authRequest) {
        if (usuarioRepository.findByUsername(authRequest.username).isPresent()) {
            return ResponseEntity.badRequest().body("Nome de usuário já existe");
        }

        Usuario user = new Usuario();
        user.setUsername(authRequest.username);
        user.setPassword(passwordEncoder.encode(authRequest.password));

        // Definindo o perfil como CLIENTE
        Perfil cliente = perfilRepository.findByAuthority("CLIENTE");
        if (cliente == null) {
            cliente = new Perfil();
            cliente.setAuthority("CLIENTE");
            perfilRepository.save(cliente);
        }

        user.setPerfil(cliente);

        usuarioRepository.save(user);

        return ResponseEntity.ok("Usuário registrado com sucesso:");
    }
    
    @PostMapping("/register-admin")
    //@PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> registerAdmin(@RequestBody @Validated AuthRequest authRequest) {
        if (usuarioRepository.findByUsername(authRequest.username).isPresent()) {
            return ResponseEntity.badRequest().body("Nome de usuário já existe");
        }

        Usuario user = new Usuario();
        user.setUsername(authRequest.username);
        user.setPassword(passwordEncoder.encode(authRequest.password));

        // Atribuindo o perfil ADMIN
        Perfil admin = perfilRepository.findByAuthority("ADMIN");
        if (admin == null) {
            admin = new Perfil();
            admin.setAuthority("ADMIN");
            perfilRepository.save(admin);
        }

        user.setPerfil(admin);

        usuarioRepository.save(user);

        return ResponseEntity.ok("Usuário administrador registrado com sucesso");
    }
}