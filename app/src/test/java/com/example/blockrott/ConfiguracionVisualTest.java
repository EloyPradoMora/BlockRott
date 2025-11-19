package com.example.blockrott;

import org.junit.Test;
import static org.junit.Assert.*;
import backend.ConfiguracionVisual;

public class ConfiguracionVisualTest {
    @Test
    public void testDetectarEstiloReturnsEmptyString() {
        ConfiguracionVisual cv = new ConfiguracionVisual();
        String estilo = cv.detectarEstilo();
        assertNotNull(estilo);
        assertEquals("", estilo);
    }

    @Test
    public void testActualizarConfigReturnsTrue() {
        ConfiguracionVisual cv = new ConfiguracionVisual();
        assertTrue(cv.actualizarConfig());
    }
}