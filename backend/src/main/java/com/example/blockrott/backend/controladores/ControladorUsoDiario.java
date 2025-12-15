package com.example.blockrott.backend.controladores;

import com.example.blockrott.backend.dto.SolicitudUsoDiario;
import com.example.blockrott.backend.servicios.ServicioUsoDiario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usodiario")
public class ControladorUsoDiario {

    private final ServicioUsoDiario servicioUsoDiario;

    public ControladorUsoDiario(ServicioUsoDiario servicioUsoDiario) {
        this.servicioUsoDiario = servicioUsoDiario;
    }

    @PostMapping
    public ResponseEntity<String> guardarUsoDiario(@RequestBody SolicitudUsoDiario solicitud) {
        try {
            servicioUsoDiario.guardarUsoDiario(solicitud);
            return ResponseEntity.status(HttpStatus.CREATED).body("Tiempo de uso diario registrado con éxito");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
