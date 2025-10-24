package com.example.blockrott.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;


@SpringBootApplication
public class BlockrottBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlockrottBackendApplication.class, args);
	}

	@Bean
	public CommandLineRunner pruebaPrimeraConexion() {
		return args -> {
			System.out.println(probarConexionSQL());

		};
	}


	@Autowired
	private DataSource dataSource;



	public String probarConexionSQL(){
		try (Connection connection = dataSource.getConnection()) {
			return "Conexión exitosa a la base de datos" + connection.getMetaData().getDatabaseProductName();
		} catch (SQLException e) {
			return "Error al conectar a la base de datos: " + e.getMessage();
		}

	}




}
