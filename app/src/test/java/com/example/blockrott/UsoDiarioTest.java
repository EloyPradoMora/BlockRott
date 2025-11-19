package com.example.blockrott;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Map;
import backend.UsoDiario;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class UsoDiarioTest {

    @Test
    public void testGuardarYLeerPromedio_LogicaDeSobreescritura() {
        Context ctx = ApplicationProvider.getApplicationContext();
        UsoDiario uso = new UsoDiario(ctx);
        String testPackage = "com.whatsapp";
        String testEmail = "user@test.com";
        String testAppName = "WhatsApp";

        // Simulamos actualizaciones de uso durante el día.
        // La lógica de UsoDiario.guardarUsoDiario usa putLong, lo que SOBREESCRIBE el valor del día,
        // no lo suma. Esto es correcto para UsageStats que ya entrega el total acumulado del día.

        uso.guardarUsoDiario(testPackage, 3000);
        uso.guardarUsoDiario(testPackage, 6000);
        uso.guardarUsoDiario(testPackage, 9000); // Este es el valor final del día

        Map<String, Object> reporte = uso.obtenerReporteParaEnvio(
                testEmail,
                testAppName,
                testPackage
        );

        // Verificaciones básicas
        assertEquals(testEmail, reporte.get("usuarioCorreo"));
        assertEquals(testAppName, reporte.get("nombreApp"));

        // Verificación matemática precisa:
        // El "promedio" calcula (suma_de_dias / dias_con_datos).
        // Como solo hemos metido datos para "hoy", dias_con_datos = 1.
        // Total = 9000 ms.
        // Promedio en segundos = (9000 / 1) / 1000 = 9 segundos.

        long promedioCalculado = (long) reporte.get("promedioUsoSeg");

        assertEquals("El promedio debería ser exactamente 9 segundos (9000ms)", 9L, promedioCalculado);
    }

    @Test
    public void testObtenerReporte_SinDatos() {
        Context ctx = ApplicationProvider.getApplicationContext();
        UsoDiario uso = new UsoDiario(ctx);

        // No guardamos nada
        Map<String, Object> reporte = uso.obtenerReporteParaEnvio("mail", "App", "com.nouse");

        long promedio = (long) reporte.get("promedioUsoSeg");
        assertEquals("Si no hay datos de uso, el promedio debe ser 0", 0L, promedio);
    }
}