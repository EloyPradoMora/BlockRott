package com.example.blockrott.backend.servicios;

import com.example.blockrott.backend.dto.RespuestaReporteSemanal;
import com.example.blockrott.backend.dto.RespuestaUsuario;
import com.example.blockrott.backend.dto.SolicitudRegistro;
import com.example.blockrott.backend.entidades.Usuario;
import com.example.blockrott.backend.repositorio.RepositorioUsuario;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ServicioUsuario {
    private final RepositorioUsuario repositorioUsuario;
    private final PasswordEncoder passwordEncoder;

    public ServicioUsuario(RepositorioUsuario repositorioUsuario, PasswordEncoder passwordEncoder) {
        this.repositorioUsuario = repositorioUsuario;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario registrarUsuario(SolicitudRegistro solicitud) {
        if (repositorioUsuario.findByCorreo(solicitud.getCorreo()).isPresent()) {
            throw new RuntimeException("El correo ya esta registrado");
        }
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombreUsuario(solicitud.getNombreUsuario());
        nuevoUsuario.setCorreo(solicitud.getCorreo());
        nuevoUsuario.setContrasenaHash(passwordEncoder.encode(solicitud.getContrasena()));
        nuevoUsuario.setFechaCreacion(LocalDateTime.now());
        return repositorioUsuario.save(nuevoUsuario);
    }

    public RespuestaUsuario obtenerUsuarioPorCorreo(String correo) {
        Usuario usuario = repositorioUsuario.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        RespuestaUsuario respuesta = new RespuestaUsuario();
        respuesta.setId(usuario.getId());
        respuesta.setCorreo(usuario.getCorreo());
        respuesta.setNombreUsuario(usuario.getNombreUsuario());
        respuesta.setFechaCreacion(usuario.getFechaCreacion());

        return respuesta;
    }
}
