package com.example.blockrott.backend.repositorio;

import com.example.blockrott.backend.entidades.UsoDiario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.Optional;

public interface RepositorioUsoDiario extends JpaRepository<UsoDiario, Long> {

    Optional<UsoDiario> findByUsuarioIdAndFecha(Long usuarioId, LocalDate fecha);

}
