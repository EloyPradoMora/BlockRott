package com.example.blockrott.backend.servicios;

import com.example.blockrott.backend.dto.SolicitudUsoDiario;
import com.example.blockrott.backend.entidades.UsoDiario;
import com.example.blockrott.backend.entidades.Usuario;
import com.example.blockrott.backend.repositorio.RepositorioUsoDiario;
import com.example.blockrott.backend.repositorio.RepositorioUsuario;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ServicioUsoDiario {

    private final RepositorioUsoDiario repositorioUsoDiario;
    private final RepositorioUsuario repositorioUsuario;

    public ServicioUsoDiario(RepositorioUsoDiario repositorioUsoDiario, RepositorioUsuario repositorioUsuario) {
        this.repositorioUsoDiario = repositorioUsoDiario;
        this.repositorioUsuario = repositorioUsuario;
    }

    public void guardarUsoDiario(SolicitudUsoDiario solicitud) {
        // a) Busca al usuario usando solicitud.usuarioCodigo (buscando por el campo
        // 'correo' que es donde guardamos la identidad)
        Usuario usuario = repositorioUsuario.findByCorreo(solicitud.getUsuarioCodigo())
                .orElseThrow(() -> new RuntimeException(
                        "Usuario no encontrado con código: " + solicitud.getUsuarioCodigo()));

        // b) Obtiene la fecha actual (LocalDate.now())
        LocalDate fechaActual = LocalDate.now();

        // c) Busca si ya existe un UsoDiario para ese usuario y esa fecha
        Optional<UsoDiario> usoExistente = repositorioUsoDiario.findByUsuarioIdAndFecha(usuario.getId(), fechaActual);

        if (usoExistente.isPresent()) {
            // d) Si existe, actualiza los segundos
            UsoDiario uso = usoExistente.get();
            uso.setTiempoTotalSegundos(solicitud.getTiempoTotalSegundos());
            // e) Guarda los cambios
            repositorioUsoDiario.save(uso);
        } else {
            // d) Si no, crea uno nuevo
            UsoDiario nuevoUso = new UsoDiario();
            nuevoUso.setUsuario(usuario);
            nuevoUso.setFecha(fechaActual);
            nuevoUso.setTiempoTotalSegundos(solicitud.getTiempoTotalSegundos());
            // e) Guarda los cambios
            repositorioUsoDiario.save(nuevoUso);
        }
    }
}
