package com.example.blockrott.backend.controladores;


import com.example.blockrott.backend.dto.RespuestaUsuario;
import com.example.blockrott.backend.dto.SolicitudRegistro;
import com.example.blockrott.backend.entidades.Usuario;
import com.example.blockrott.backend.repositorio.RepositorioUsuario;
import com.example.blockrott.backend.servicios.ServicioUsuario;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
public class ControladorUsuario {

    private final ServicioUsuario servicioUsuario;

    public ControladorUsuario(ServicioUsuario servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }

    @PostMapping("/registro")
    public ResponseEntity<String> registrarUsuario(@RequestBody SolicitudRegistro solicitud) {
        try {
            servicioUsuario.registrarUsuario(solicitud);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // busca un usario por el correo
    @GetMapping("/buscar")
    public ResponseEntity<RespuestaUsuario> obtenerUsuarioPorCorreo(@RequestParam String correo) {
        try {
            RespuestaUsuario respuesta = servicioUsuario.obtenerUsuarioPorCorreo(correo);
            return ResponseEntity.ok(respuesta);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
