package com.example.springboot_backend.repositorios;

import com.example.springboot_backend.entidades.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepositorioUsuario extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCorreo(String correo);
}
