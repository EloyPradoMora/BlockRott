package com.example.blockrott.backend.controladores;

import com.example.blockrott.backend.entidades.ReporteSemanal;
import com.example.blockrott.backend.entidades.Usuario;
import com.example.blockrott.backend.repositorio.RepositorioReporteSemanal;
import com.example.blockrott.backend.repositorio.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    private RepositorioReporteSemanal repositorioReporteSemanal;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String menu() {
        return "<html>" +
                "<head><title>Test Backend</title></head>" +
                "<body style='font-family: Arial, sans-serif; padding: 20px;'>" +
                "<h1>Panel de Control Backend</h1>" +
                "<ul>" +
                "  <li><a href='/api/test/conexion'>Probar Conexion BD</a></li>" +
                "  <li><a href='/api/test/poblar'><strong>Poblar base de datos</strong></a></li>" +
                "  <li><a href='/api/test/tablas'>Ver Tablas</a></li>" +
                "  <li><a href='/api/test/usuarios'>Ver Usuarios (JSON)</a></li>" +
                "</ul>" +
                "</body>" +
                "</html>";
    }

    @GetMapping("/poblar")
    public ResponseEntity<String> poblarBaseDeDatos() {
        try {
            repositorioReporteSemanal.deleteAll();
            repositorioUsuario.deleteAll();

            String[] nombres = {"Ana", "Carlos", "Beatriz", "David", "Elena"};
            String[] apps = {"TikTok", "Instagram", "WhatsApp", "YouTube", "Reddit"};
            Random random = new Random();

            StringBuilder log = new StringBuilder("Proceso de poblado iniciado...<br>");

            for (String nombre : nombres) {
                Usuario u = new Usuario();
                String correo = nombre.toLowerCase() + "@test.com";
                u.setNombreUsuario(nombre + " Test");
                u.setCorreo(correo);
                u.setContrasenaHash(passwordEncoder.encode("1234"));
                u.setFechaCreacion(LocalDateTime.now());

                Usuario usuarioGuardado = repositorioUsuario.save(u);
                log.append("Created User: ").append(correo).append("<br>");

                for (String app : apps) {
                    if (random.nextBoolean()) {
                        ReporteSemanal r = new ReporteSemanal();
                        r.setUsuario(usuarioGuardado);
                        r.setNombreApp(app);
                        r.setSemanaInicio(LocalDate.now().minusWeeks(random.nextInt(3)));
                        r.setPromedioUsoSeg((long) random.nextInt(7200));

                        repositorioReporteSemanal.save(r);
                        log.append(" -- Reporte: ").append(app).append("<br>");
                    }
                }
            }

            return ResponseEntity.ok(log.toString());

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al poblar BD: " + e.getMessage());
        }
    }

    @GetMapping("/conexion")
    public ResponseEntity<String> probarConexion() {
        try {
            jdbcTemplate.execute("SELECT 1");
            return ResponseEntity.ok("Conexion exitosa a la base de datos PostgreSQL.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error de conexion: " + e.getMessage());
        }
    }

    @GetMapping("/tablas")
    public ResponseEntity<List<String>> listarTablas() {
        try {
            String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'";
            List<String> tablas = jdbcTemplate.queryForList(sql, String.class);
            return ResponseEntity.ok(tablas);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<Map<String, Object>>> listarUsuarios() {
        try {
            String sql = "SELECT * FROM usuario";
            List<Map<String, Object>> usuarios = jdbcTemplate.queryForList(sql);
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}