package com.example.blockrott.backend.controladores;


import com.example.blockrott.backend.dto.SolicitudReporteSemanal;
import com.example.blockrott.backend.servicios.ServicioReporte;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;
import lombok.NoArgsConstructor;

@RestController
@RequestMapping("/api/reportes")
public class ControladorReporteSemanal {

    private final ServicioReporte servicioReporte;

    public ControladorReporteSemanal(ServicioReporte servicioReporte) {
        this.servicioReporte = servicioReporte;
    }

    @PostMapping
    public ResponseEntity<String> subirReporte(@RequestBody SolicitudReporteSemanal solicitud) {
        try {
            servicioReporte.guardarReporte(solicitud);
            return ResponseEntity.status(HttpStatus.CREATED).body("Reporte guardado");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }



}
