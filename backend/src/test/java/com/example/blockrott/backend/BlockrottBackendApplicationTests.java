package com.example.blockrott.backend;

import com.example.blockrott.backend.dto.RespuestaReporteSemanal;
import com.example.blockrott.backend.dto.SolicitudRegistro;
import com.example.blockrott.backend.entidades.Usuario;
import com.example.blockrott.backend.repositorio.RepositorioUsuario;
import com.example.blockrott.backend.servicios.ServicioReporte;
import com.example.blockrott.backend.servicios.ServicioUsuario;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import javax.sql.DataSource;

import jakarta.transaction.Transactional;

@SpringBootTest
class BlockrottBackendApplicationTests {
	@Autowired
	private DataSource dataSource;

	@Autowired
	private RepositorioUsuario repositorioUsuario;

	@Autowired
	private ServicioUsuario servicioUsuario;
	@Autowired
	private ServicioReporte servicioReporte;

	@Test
	void pruebaDeConexionSQL() throws SQLException {
		System.out.println("Ejecutando prueba de conexion a la BD");
		Connection connection = dataSource.getConnection();
		assertNotNull(connection, "La conexion no deberian ser nula");
		String dbName = connection.getMetaData().getDatabaseProductName();
		assertEquals("PostgreSQL", dbName);
		System.out.println("Conexion exitosa a: " + dbName);
		connection.close();
	}

	@Test
	void pruebaBusquedaUsuarios() {
		System.out.println("\nPRUEBA 2: BUSQUEDA USUARIO ESPECIFICO");
		Usuario usuario = repositorioUsuario.findByCorreo("usuario2@test.com")
				.orElse(null);
		assertNotNull(usuario, "Deberia encontrar a 'usuario2@test.com'");
		assertEquals("Usuario Dos", usuario.getNombreUsuario(), "El nombre no coincide");

		System.out.println(" OK: 'Usuario Dos' encontrado");
	}

	@Test
	void pruebaObtenerReportesPorCorreo() {
		System.out.println("\nPRUEBA 3: OBTENER REPORTES POR CORREO (SERVICIO)]");
		String correoPrueba = "usuario1@test.com";

		Set<RespuestaReporteSemanal> reportesDTO = servicioReporte.obtenerReportesPorCorreo(correoPrueba);

		assertNotNull(reportesDTO);
		assertFalse(reportesDTO.isEmpty(), "Deberia encontrar reportes para " + correoPrueba);

		System.out.println(" Se encontraron " + reportesDTO.size() + " reportes  para " + correoPrueba);

		reportesDTO.forEach(r -> {
			System.out.println(
					"  APP " + r.getNombreApp() +
							" INICIO " + r.getSemanaInicio() +
							" PROMEDIO " + r.getPromedioUsoSeg() + " seg"
			);
		});
	}

	@Test
	@Transactional  // se usa para que no quede en la base de datos
	void pruebaCrearUsuario() {
		System.out.println("\n[PRUEBA 4: CREAR USUARIO NUEVO (SERVICIO)]");

		SolicitudRegistro solicitud = new SolicitudRegistro();
		solicitud.setCorreo("test.registro@dominio.com");
		solicitud.setNombreUsuario("Test Registro");
		solicitud.setContrasena("contrasenaPlana123");

		servicioUsuario.registrarUsuario(solicitud);

		Usuario usuarioGuardado = repositorioUsuario.findByCorreo("test.registro@dominio.com")
				.orElse(null);

		assertNotNull(usuarioGuardado, "El usuario deberia haberse guardado.");
		assertEquals("Test Registro", usuarioGuardado.getNombreUsuario());

		assertNotEquals("contrasenaPlana123", usuarioGuardado.getContrasenaHash(), "La contrasena debe estar cifrada");
		assertTrue(usuarioGuardado.getContrasenaHash().startsWith("$2a$"), "La contrasena debe usar BCrypt");

		System.out.println("-> OK: Usuario 'Test Registro' creado y contrasena cifrada.");

	}
}
