package com.example.blockrott.backend.dto;
import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RespuestaReporteSemanal {
    private Long id;
    private String nombreApp;
    private LocalDate semanaInicio;
    private Long promedioUsoSeg;
    private Long usuarioId;
}
