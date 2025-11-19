package com.example.blockrott;

import android.app.AppOpsManager;
import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import backend.EspecificacionApp;
import backend.Usuario;

@RunWith(RobolectricTestRunner.class)
public class UsuarioTest {
    @Test
    public void testSingleton() {
        Context ctx = ApplicationProvider.getApplicationContext();
        Usuario u1 = Usuario.getInstance(ctx);
        Usuario u2 = Usuario.getInstance(ctx);
        assertSame(u1, u2);
    }
    @Test
    public void testAgregarEspecificacionNueva() {
        Context ctx = ApplicationProvider.getApplicationContext();
        Usuario usuario = Usuario.getInstance(ctx);
        usuario.getEspecificacionesApp().clear();
        usuario.agregarEspecificacionNueva("YT", "com.youtube", 5000);
        assertEquals(1, usuario.getEspecificacionesApp().size());
    }
    @Test
    public void testMonitoreoApps() {
        Context ctx = ApplicationProvider.getApplicationContext();
        Usuario usuario = Usuario.getInstance(ctx);
        usuario.getEspecificacionesApp().clear();
        EspecificacionApp esp = Mockito.spy(new EspecificacionApp("Telegram", "org.telegram", 1000, ctx));
        doReturn(5000L).when(esp).obtenerTiempoDeUsoAplicacion();
        usuario.getEspecificacionesApp().add(esp);
        usuario.monitoreoApps();
        assertTrue(esp.isBloqueada());
    }

    @Test
    public void testVerificarPermisosUsoEstadistica() {
        Context ctx = Mockito.mock(Context.class);
        AppOpsManager manager = Mockito.mock(AppOpsManager.class);
        when(ctx.getSystemService(Context.APP_OPS_SERVICE)).thenReturn(manager);
        when(ctx.getPackageName()).thenReturn("testpkg");
        when(manager.checkOpNoThrow(
                eq(AppOpsManager.OPSTR_GET_USAGE_STATS),
                anyInt(),
                anyString()))
                .thenReturn(AppOpsManager.MODE_ALLOWED);
        Usuario user = Usuario.getInstance(ApplicationProvider.getApplicationContext());
        assertTrue(user.verificarPermisosUsoEstadistica(ctx));
    }
}
