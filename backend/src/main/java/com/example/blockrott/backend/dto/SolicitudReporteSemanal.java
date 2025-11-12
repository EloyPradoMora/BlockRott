package com.example.blockrott.backend.dto;
import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SolicitudReporteSemanal {
    private String usuarioCorreo;
    private String nombreApp;
    private LocalDate semanaInicio;
    private Long promedioUsoSeg;
}
