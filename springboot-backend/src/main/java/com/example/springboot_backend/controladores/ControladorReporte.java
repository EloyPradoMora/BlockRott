package com.example.springboot_backend.controladores;


import com.example.springboot_backend.dto.SolicitudReporteSemanal;
import com.example.springboot_backend.servicios.ServicioReporte;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reportes")
public class ControladorReporte {

    private final ServicioReporte servicioReporte;

    public ControladorReporte(ServicioReporte servicioReporte) {
        this.servicioReporte = servicioReporte;
    }

    // endpoint para recibir y guardar el promedio semanal desde android

    @PostMapping
    public ResponseEntity<String> subirReporteSemanal(@RequestBody SolicitudReporteSemanal solicitud) {
        try {
            servicioReporte.guardarPromedioSemanal(solicitud);

            return ResponseEntity.status(HttpStatus.CREATED).body("Reporte semanal guardado");

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en el servidor al guardar el reporte.");
        }
    }


}
