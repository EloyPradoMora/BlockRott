package com.example.blockrott.backend.servicios;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.blockrott.backend.dto.SolicitudReporteSemanal;
import com.example.blockrott.backend.repositorio.RepositorioReporteSemanal;
import com.example.blockrott.backend.repositorio.RepositorioUsuario;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ServicioReporteTest {

    @Mock
    private RepositorioUsuario repositorioUsuario;
    @Mock private RepositorioReporteSemanal repositorioReporteSemanal;
    @InjectMocks
    private ServicioReporte servicioReporte;

    @Test
    void guardarReporte() {

    }


    @Test
    void obtenerReportesPorCorreo() {
    }


    }
