package com.example.blockrott;

import org.junit.Test;
import static org.junit.Assert.*;
import backend.Conexion;

public class ConexionTest {
    @Test
    public void testPedirDatosReturnsEmptyString() {
        Conexion c = new Conexion();
        String datos = c.pedirDatos();
        assertNotNull(datos);
        assertEquals("", datos);
    }

    @Test
    public void testEnviarDatosReturnsTrue() {
        Conexion c = new Conexion();
        boolean ok = c.enviarDatos();
        assertTrue(ok);
    }
}