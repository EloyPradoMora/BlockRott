package com.example.blockrott.backend.repositorio;

import com.example.blockrott.backend.entidades.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RepositorioUsuario extends JpaRepository<Usuario, Long> {


    // encontrar el usuario por su correo
    Optional<Usuario> findByCorreo(String correo);


    //encontrar el usuario por su nombre
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);

    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.reportes")
    List<Usuario> findAllWithReportes();


}
