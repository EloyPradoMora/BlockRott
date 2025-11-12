package com.example.blockrott.backend.servicios;

import com.example.blockrott.backend.dto.RespuestaReporteSemanal;
import com.example.blockrott.backend.dto.SolicitudReporteSemanal;
import com.example.blockrott.backend.entidades.ReporteSemanal;
import com.example.blockrott.backend.entidades.Usuario;
import com.example.blockrott.backend.repositorio.RepositorioReporteSemanal;
import com.example.blockrott.backend.repositorio.RepositorioUsuario;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class ServicioReporte {
    private final RepositorioUsuario repositorioUsuario;
    private final RepositorioReporteSemanal repositorioReporteSemanal;

    public ServicioReporte(RepositorioUsuario repositorioUsuario, RepositorioReporteSemanal repositorioReporteSemanal) {
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioReporteSemanal = repositorioReporteSemanal;
    }

    public void guardarReporte(SolicitudReporteSemanal solicitud) {
        Usuario usuario = repositorioUsuario.findByCorreo(solicitud.getUsuarioCorreo())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        ReporteSemanal nuevoReporte = new ReporteSemanal();
        nuevoReporte.setUsuario(usuario);
        nuevoReporte.setNombreApp(solicitud.getNombreApp());
        nuevoReporte.setSemanaInicio(solicitud.getSemanaInicio());
        nuevoReporte.setPromedioUsoSeg(solicitud.getPromedioUsoSeg());
        repositorioReporteSemanal.save(nuevoReporte);
    }

    public Set<RespuestaReporteSemanal> obtenerReportesPorCorreo(String correo) {

        List<ReporteSemanal> reportesEntidad = repositorioReporteSemanal.findByUsuario_Correo(correo);

        return reportesEntidad.stream()
                .map(reporte -> {
                    RespuestaReporteSemanal dto = new RespuestaReporteSemanal();
                    dto.setId(reporte.getId());
                    dto.setNombreApp(reporte.getNombreApp());
                    dto.setSemanaInicio(reporte.getSemanaInicio());
                    dto.setPromedioUsoSeg(reporte.getPromedioUsoSeg());
                    dto.setUsuarioId(reporte.getUsuario().getId()); // Asignamos el ID
                    return dto;
                }).collect(Collectors.toSet());
    }

}
