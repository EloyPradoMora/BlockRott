package com.example.blockrott;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import java.util.Map;
import static org.junit.Assert.*;
import backend.UsoDiario;

@RunWith(RobolectricTestRunner.class)
public class UsoDiarioTest {

    @Test
    public void testGuardarYLeerPromedio() {
        Context ctx = ApplicationProvider.getApplicationContext();
        UsoDiario uso = new UsoDiario(ctx);
        uso.guardarUsoDiario("com.whatsapp", 3000);
        uso.guardarUsoDiario("com.whatsapp", 6000);
        uso.guardarUsoDiario("com.whatsapp", 9000);
        Map<String, Object> reporte = uso.obtenerReporteParaEnvio(
                "user@test.com",
                "WhatsApp",
                "com.whatsapp"
        );
        assertEquals("user@test.com", reporte.get("usuarioCorreo"));
        assertEquals("WhatsApp", reporte.get("nombreApp"));
        assertTrue(((long) reporte.get("promedioUsoSeg")) >= 3);
    }
}