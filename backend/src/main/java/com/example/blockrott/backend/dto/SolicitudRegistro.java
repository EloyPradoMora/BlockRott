package com.example.blockrott.backend.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SolicitudRegistro {
    private String nombreUsuario;
    private String correo;
    private String contrasena;
}
