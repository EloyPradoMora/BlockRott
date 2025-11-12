package com.example.blockrott.backend.dto;
import java.time.LocalDateTime;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RespuestaUsuario {
    private Long id;
    private String correo;
    private String nombreUsuario;
    private LocalDateTime fechaCreacion;
    private Set<RespuestaReporteSemanal> reportes;
}
