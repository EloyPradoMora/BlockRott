package com.example.blockrott;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.Arrays;
import java.util.Collections;

import backend.EspecificacionApp;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class EspecificacionAppTest {

    private Context context;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        context = RuntimeEnvironment.getApplication();
    }

    @Test
    public void testConvertirTiempoLegible_CasosVariados() {
        EspecificacionApp app = new EspecificacionApp("Test", "com.test", 1000, context);

        // Caso: 0 milisegundos
        assertEquals("0m", app.convertirTiempoLegible(0));
        // Caso: Menos de un minuto (59 segundos) -> debería ser 0m o manejarse como <1m según lógica (tu lógica actual hace truncamiento)
        assertEquals("0m", app.convertirTiempoLegible(59_000));
        // Caso: 1 minuto exacto
        assertEquals("1m", app.convertirTiempoLegible(60_000));
        // Caso: 1 hora exacta
        assertEquals("1h 0m", app.convertirTiempoLegible(3600_000));
        // Caso: 1 hora y 1 minuto
        assertEquals("1h 1m", app.convertirTiempoLegible(3660_000));
        // Caso: Tiempo muy largo
        assertEquals("2h 30m", app.convertirTiempoLegible(9000_000));
    }

    @Test
    public void testVerificarLimiteTiempo_BloqueoYDesbloqueo() {
        long limite = 2000L;
        EspecificacionApp app = spy(new EspecificacionApp("TikTok", "com.tiktok", limite, context));

        // Caso 1: Uso MENOR al límite -> No bloqueada
        doReturn(1000L).when(app).obtenerTiempoDeUsoAplicacion();
        app.verificarLimiteTiempo();
        assertFalse("La app no debería estar bloqueada si el uso es menor al límite", app.isBloqueada());

        // Caso 2: Uso MAYOR al límite -> Bloqueada
        doReturn(3000L).when(app).obtenerTiempoDeUsoAplicacion();
        app.verificarLimiteTiempo();
        assertTrue("La app debería estar bloqueada si el uso supera el límite", app.isBloqueada());

        // Caso 3: Volver a un uso menor (ej. reinicio de estadísticas diarias) -> Desbloqueada
        doReturn(0L).when(app).obtenerTiempoDeUsoAplicacion();
        app.verificarLimiteTiempo();
        assertFalse("La app debería desbloquearse si el uso baja", app.isBloqueada());
    }

    @Test
    public void testObtenerTiempoDeUsoAplicacion_SinDatos() {
        // Simulamos que el UsageStatsManager devuelve null o lista vacía
        Context ctxSpy = spy(context);
        UsageStatsManager mockManager = mock(UsageStatsManager.class);
        when(ctxSpy.getSystemService(Context.USAGE_STATS_SERVICE)).thenReturn(mockManager);
        when(mockManager.queryUsageStats(anyInt(), anyLong(), anyLong())).thenReturn(Collections.emptyList());

        EspecificacionApp app = new EspecificacionApp("App", "com.test", 10000, ctxSpy);
        long resultado = app.obtenerTiempoDeUsoAplicacion();

        assertEquals("Si no hay estadísticas, el tiempo debería ser 0", 0L, resultado);
    }

    @Test
    public void testObtenerTiempoDeUsoAplicacion_ConDatos() {
        // 1. Crear el spy
        Context ctxSpy = spy(context);

        // 2. [CORRECCIÓN CRÍTICA] Hacer que getApplicationContext devuelva el propio SPY.
        // Si no hacemos esto, la clase usará el contexto real y perderá nuestros mocks.
        doReturn(ctxSpy).when(ctxSpy).getApplicationContext();

        // 3. Mockear el UsageStatsManager
        UsageStatsManager mockManager = mock(UsageStatsManager.class);
        // Usamos doReturn para evitar efectos secundarios al llamar al método real del spy
        doReturn(mockManager).when(ctxSpy).getSystemService(Context.USAGE_STATS_SERVICE);

        // 4. Preparar los datos de prueba
        UsageStats stat = mock(UsageStats.class);
        when(stat.getPackageName()).thenReturn("com.target.app");
        when(stat.getTotalTimeInForeground()).thenReturn(5555L);

        // 5. Configurar el comportamiento del mockManager
        when(mockManager.queryUsageStats(anyInt(), anyLong(), anyLong()))
                .thenReturn(Arrays.asList(stat));

        // 6. Ejecutar
        EspecificacionApp app = new EspecificacionApp("Target", "com.target.app", 10000, ctxSpy);
        long resultado = app.obtenerTiempoDeUsoAplicacion();

        // 7. Verificar
        assertEquals("Debería obtener el tiempo de uso correcto del UsageStatsManager", 5555L, resultado);
    }
}