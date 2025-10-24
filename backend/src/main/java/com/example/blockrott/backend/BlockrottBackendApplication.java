package com.example.blockrott.backend;

import com.example.blockrott.backend.entidades.ReporteSemanal;
import com.example.blockrott.backend.entidades.Usuario;
import com.example.blockrott.backend.repositorio.RepositorioUsuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;


@SpringBootApplication
public class BlockrottBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlockrottBackendApplication.class, args);
	}

	@Bean

	public CommandLineRunner primeraPrueba(RepositorioUsuario repositorioUsuario, DataSource dataSource) {
		return args -> {


			try (Connection connection = dataSource.getConnection()) {
				System.out.println("Conexion exitosa a la base de datos: "
						+ connection.getMetaData().getDatabaseProductName());
			} catch (SQLException e) {
				System.out.println("Error al conectar a la base de datos: "
						+ e.getMessage());
			}

			System.out.println("\n Iniciando lectura de repositorio ");

			List<Usuario> usuariosConReportes = repositorioUsuario.findAllWithReportes();

			if (usuariosConReportes.isEmpty()) {
				System.out.println(" No se encontraron usuarios en la BD.");
				return;
			}

			for (Usuario u : usuariosConReportes) {
				System.out.println("\n.......................");
				System.out.println("USUARIO ENCONTRADO:");
				System.out.println("  ID: " + u.getId());
				System.out.println("  Nombre: " + u.getNombreUsuario());
				System.out.println("  Correo: " + u.getCorreo());

				if (u.getReportes() == null || u.getReportes().isEmpty()) {
					System.out.println(" Este usuario NO tiene reportes semanales.");
				} else {
					System.out.println("  REPORTES ASIGNADOS (" + u.getReportes().size() + "):");
					for (ReporteSemanal r : u.getReportes()) {
						System.out.println(
								"    App: " + r.getNombreApp() +
										" | Inicio: " + r.getSemanaInicio() +
										" | Promedio: " + r.getPromedioUsoSeg() + " seg."
						);
					}
				}
			}
			System.out.println(".................................");
			System.out.println(" Prueba Finalizada  ");
		};
	}
}