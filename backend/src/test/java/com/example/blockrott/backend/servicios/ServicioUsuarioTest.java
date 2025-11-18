package com.example.blockrott.backend.servicios;
import com.example.blockrott.backend.dto.RespuestaUsuario;
import com.example.blockrott.backend.dto.SolicitudRegistro;
import com.example.blockrott.backend.entidades.Usuario;
import com.example.blockrott.backend.repositorio.RepositorioUsuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicioUsuarioTest {

    @Mock
    private RepositorioUsuario repositorio;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private ServicioUsuario servicio;

    @Test
    void registrarUsuario() {
        // aqui se introducen los datos simulados
        SolicitudRegistro solicitud = new SolicitudRegistro();
        solicitud.setCorreo("usuariodetesteo@test.com");
        solicitud.setContrasena("1234");
        solicitud.setNombreUsuario("usuariodetesteo");

        // se simula la base de datos
        when(repositorio.findByCorreo(solicitud.getCorreo())).thenReturn(Optional.empty());
        when(encoder.encode("1234")).thenReturn("hash_seguro");
        when(repositorio.save(any(Usuario.class))).thenAnswer(i -> {
            Usuario u = i.getArgument(0);
            u.setId(1L);
            return u;
        });
        // se verifica
        Usuario resultado = servicio.registrarUsuario(solicitud);
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("hash_seguro", resultado.getContrasenaHash());
        verify(repositorio).save(any(Usuario.class));
        System.out.println("Se paso el test");
    }

    @Test
    void obtenerUsuarioPorCorreo() {
        String correo = "juan@mail.com";
        Usuario usuarioSimulado = new Usuario();

        usuarioSimulado.setCorreo(correo);
        when(repositorio.findByCorreo(correo)).thenReturn(Optional.of(usuarioSimulado));

        RespuestaUsuario resultado = servicio.obtenerUsuarioPorCorreo(correo);
        assertNotNull(resultado);
        assertEquals(correo, resultado.getCorreo());
    }
}