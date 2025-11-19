package com.example.blockrott;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import java.util.Arrays;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import backend.EspecificacionApp;

@RunWith(RobolectricTestRunner.class)
public class EspecificacionAppTest {

    @Test
    public void testConvertirTiempoLegible() {
        Context ctx = RuntimeEnvironment.getApplication();
        EspecificacionApp app = new EspecificacionApp("YouTube", "com.youtube", 1000, ctx);
        assertEquals("1h 0m", app.convertirTiempoLegible(3600_000));
        assertEquals("5m", app.convertirTiempoLegible(300_000));
        assertEquals("0m", app.convertirTiempoLegible(10_000));
    }

    @Test
    public void testVerificarLimiteTiempo() {
        Context ctx = RuntimeEnvironment.getApplication();
        EspecificacionApp app = Mockito.spy(new EspecificacionApp("TikTok", "com.tiktok", 2000, ctx));
        doReturn(3000L).when(app).obtenerTiempoDeUsoAplicacion();
        app.verificarLimiteTiempo();
        assertTrue(app.isBloqueada());
    }

    @Test
    public void testObtenerTiempoDeUsoAplicacion() {
        Context ctx = spy(RuntimeEnvironment.getApplication());
        UsageStatsManager mockManager = mock(UsageStatsManager.class);
        when(ctx.getSystemService(Context.USAGE_STATS_SERVICE)).thenReturn(mockManager);
        long now = System.currentTimeMillis();
        UsageStats stat = mock(UsageStats.class);
        when(stat.getPackageName()).thenReturn("com.test");
        when(stat.getLastTimeUsed()).thenReturn(now);
        when(stat.getTotalTimeInForeground()).thenReturn(5000L);
        when(mockManager.queryUsageStats(anyInt(), anyLong(), anyLong()))
                .thenReturn(Arrays.asList(stat));
        EspecificacionApp app = new EspecificacionApp("App", "com.test", 10000, ctx);
        long result = app.obtenerTiempoDeUsoAplicacion();
        assertEquals(5000L, result);
    }
}