package com.example.blockrott.backend.servicios;

import com.example.blockrott.backend.dto.SolicitudUsoDiario;
import com.example.blockrott.backend.entidades.UsoDiario;
import com.example.blockrott.backend.entidades.Usuario;
import com.example.blockrott.backend.repositorio.RepositorioUsoDiario;
import com.example.blockrott.backend.repositorio.RepositorioUsuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicioUsoDiarioTest {

    @Mock
    private RepositorioUsoDiario repositorioUsoDiario;

    @Mock
    private RepositorioUsuario repositorioUsuario;

    @InjectMocks
    private ServicioUsoDiario servicioUsoDiario;

    @Test
    void guardarUsoDiario_Exito() {
        String codigoUsuario = "usuario@test.com";
        Long tiempoSegundos = 1200L;
        SolicitudUsoDiario solicitud = new SolicitudUsoDiario();
        solicitud.setUsuarioCodigo(codigoUsuario);
        solicitud.setTiempoTotalSegundos(tiempoSegundos);

        Usuario usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        usuarioMock.setCorreo(codigoUsuario);

        when(repositorioUsuario.findByCorreo(codigoUsuario)).thenReturn(Optional.of(usuarioMock));
        when(repositorioUsoDiario.findByUsuarioIdAndFecha(eq(1L), any(LocalDate.class))).thenReturn(Optional.empty());

        servicioUsoDiario.guardarUsoDiario(solicitud);

        verify(repositorioUsoDiario, times(1)).save(any(UsoDiario.class));
    }

    @Test
    void guardarUsoDiario_UsuarioNoEncontrado() {
        SolicitudUsoDiario solicitud = new SolicitudUsoDiario();
        solicitud.setUsuarioCodigo("no-existe@test.com");

        when(repositorioUsuario.findByCorreo("no-existe@test.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            servicioUsoDiario.guardarUsoDiario(solicitud);
        });

        assertTrue(exception.getMessage().contains("Usuario no encontrado"));
        verify(repositorioUsoDiario, never()).save(any());
    }
}
