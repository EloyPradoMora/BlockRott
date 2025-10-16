package com.example.springboot_backend.servicios;

import com.example.springboot_backend.dto.SolicitudReporteSemanal;
import com.example.springboot_backend.entidades.ReporteSemanal;
import com.example.springboot_backend.entidades.Usuario;
import com.example.springboot_backend.repositorios.RepositorioReporteSemanal;
import com.example.springboot_backend.repositorios.RepositorioUsuario;

public class ServicioReporte {

    private final RepositorioUsuario repositorioUsuario;
    private final RepositorioReporteSemanal repositorioReporteSemanal;

    public ServicioReporte(RepositorioUsuario repositorioUsuario, RepositorioReporteSemanal repositorioReporteSemanal) {
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioReporteSemanal = repositorioReporteSemanal;
    }

    public void guardarPromedioSemanal(SolicitudReporteSemanal solicitud) {

       // se busca el usuario por su correo
        Usuario usuario = repositorioUsuario.findByCorreo(solicitud.getUsuarioCorreo())
                .orElseThrow(() -> new RuntimeException("Error: Usuario no existe. No se puede guardar el reporte."));

        // se mapea la data del dto a la entidad y se genera el reporte a la base de datos
        ReporteSemanal nuevoReporte = new ReporteSemanal();

        nuevoReporte.setUsuario(usuario);
        nuevoReporte.setNombreApp(solicitud.getNombreApp());
        nuevoReporte.setSemanaInicio(solicitud.getSemanaInicio());
        nuevoReporte.setPromedioUsoSeg(solicitud.getPromedioUsoSeg());

        repositorioReporteSemanal.save(nuevoReporte);
    }


}
