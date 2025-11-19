package com.example.blockrott.backend.servicios;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.example.blockrott.backend.dto.RespuestaReporteSemanal;
import com.example.blockrott.backend.dto.SolicitudReporteSemanal;
import com.example.blockrott.backend.entidades.ReporteSemanal;
import com.example.blockrott.backend.entidades.Usuario;
import com.example.blockrott.backend.repositorio.RepositorioReporteSemanal;
import com.example.blockrott.backend.repositorio.RepositorioUsuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class ServicioReporteTest {

    @Mock
    private RepositorioUsuario repositorioUsuario;
    @Mock private RepositorioReporteSemanal repositorioReporteSemanal;
    @InjectMocks
    private ServicioReporte servicioReporte;

    private Optional<Usuario> mockUsuario() {
        Usuario u = new Usuario();
        u.setId(10L);
        return Optional.of(u);
    }

    @Test
    void guardarReporte() {
        // se simulan datos en la base de datos
        SolicitudReporteSemanal solicitud = new SolicitudReporteSemanal();
        solicitud.setUsuarioCorreo("usuario@existente.com");
        solicitud.setNombreApp("TikTok");
        solicitud.setPromedioUsoSeg(3600L);
        solicitud.setSemanaInicio(LocalDate.of(2025, 10, 20));
        //se simula la base de datos
        when(repositorioUsuario.findByCorreo(solicitud.getUsuarioCorreo())).thenReturn(mockUsuario());
        when(repositorioReporteSemanal.save(any(ReporteSemanal.class))).thenAnswer(i -> {
            ReporteSemanal r = i.getArgument(0);
            r.setId(101L);
            return r;
        });
        //se verifica que se guarden los datos en la base de datos simulada
        servicioReporte.guardarReporte(solicitud);
        verify(repositorioReporteSemanal).save(any(ReporteSemanal.class));
        verify(repositorioUsuario).findByCorreo(solicitud.getUsuarioCorreo());

        System.out.println("Se paso el test de guardar reporte");
    }


    @Test
    void obtenerReportesPorCorreo() {
        //simulamos la base de datos
        String correo = "usuario@existente.com";
        Usuario usuarioEntidad = mockUsuario().get();
        ReporteSemanal reporteSimulado = new ReporteSemanal();
        reporteSimulado.setNombreApp("Youtube");
        reporteSimulado.setUsuario(usuarioEntidad);
        //simulamos las llamadas a la base de datos ficticia
        when(repositorioReporteSemanal.findByUsuario_Correo(correo)).thenReturn(List.of(reporteSimulado));
        Set<RespuestaReporteSemanal> resultado = servicioReporte.obtenerReportesPorCorreo(correo);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        RespuestaReporteSemanal dto = resultado.iterator().next();
        assertEquals("Youtube", dto.getNombreApp());
        assertEquals(10L, dto.getUsuarioId());

        System.out.println("Se paso el test de buscar reportes");
    }

    }
