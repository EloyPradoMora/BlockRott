package com.example.blockrott.backend;

import com.example.blockrott.backend.entidades.Usuario;
import com.example.blockrott.backend.repositorio.RepositorioUsuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;
import javax.sql.DataSource;

@SpringBootTest
class BlockrottBackendApplicationTests {
	@Autowired
	private DataSource dataSource;

	@Autowired
	private RepositorioUsuario repositorioUsuario;

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
}
