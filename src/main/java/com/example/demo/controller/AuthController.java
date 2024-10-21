package com.example.demo.controller;


import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.example.demo.model.Role;
import com.example.demo.model.Usuario;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.security.JwtTokenUtil;



@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UsuarioRepository usuarioRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public AuthController(
            AuthenticationManager authenticationManager,
            UsuarioRepository usuarioRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenUtil jwtTokenUtil
    ) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
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

        // Definindo a role padrão como ROLE_USER
        Role roleUser = roleRepository.findByAuthority("ROLE_USER");
        if (roleUser == null) {
            roleUser = new Role();
            roleUser.setAuthority("ROLE_USER");
            roleRepository.save(roleUser);
        }

        HashSet<Role> roles = new HashSet<>();
        roles.add(roleUser);
        user.setRoles(roles);

        usuarioRepository.save(user);

        return ResponseEntity.ok("Usuário registrado com sucesso");
    }
    
    @PostMapping("/register-admin")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> registerAdmin(@RequestBody @Validated AuthRequest authRequest) {
        if (usuarioRepository.findByUsername(authRequest.username).isPresent()) {
            return ResponseEntity.badRequest().body("Nome de usuário já existe");
        }

        Usuario user = new Usuario();
        user.setUsername(authRequest.username);
        user.setPassword(passwordEncoder.encode(authRequest.password));

        // Atribuindo a role ROLE_ADMIN
        Role roleAdmin = roleRepository.findByAuthority("ROLE_ADMIN");
        if (roleAdmin == null) {
            roleAdmin = new Role();
            roleAdmin.setAuthority("ROLE_ADMIN");
            roleRepository.save(roleAdmin);
        }

        HashSet<Role> roles = new HashSet<>();
        roles.add(roleAdmin);
        user.setRoles(roles);

        usuarioRepository.save(user);

        return ResponseEntity.ok("Usuário administrador registrado com sucesso");
    }
}