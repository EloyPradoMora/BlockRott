package com.example.blockrott;

import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowAlertDialog;

import java.util.ArrayList;

import backend.EspecificacionApp;
import backend.Usuario;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class UsuarioTest {

    private Context context;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        // Limpiamos las especificaciones antes de cada test para evitar contaminación entre tests
        // dado que Usuario es un Singleton.
        Usuario.getInstance(context).getEspecificacionesApp().clear();
    }

    @Test
    public void testSingleton_MismaInstancia() {
        Usuario u1 = Usuario.getInstance(context);
        Usuario u2 = Usuario.getInstance(context);
        assertNotNull(u1);
        assertSame("Debería devolver siempre la misma instancia (Singleton)", u1, u2);
    }

    @Test
    public void testAgregarEspecificacionNueva() {
        Usuario usuario = Usuario.getInstance(context);
        boolean resultado = usuario.agregarEspecificacionNueva("YouTube", "com.youtube", 5000);

        assertTrue(resultado);
        assertEquals("La lista debería tener 1 elemento", 1, usuario.getEspecificacionesApp().size());
        assertEquals("El nombre de la app debería coincidir", "YouTube", usuario.getEspecificacionesApp().get(0).getNombreApp());
    }

    @Test
    public void testRevisarTiempos_FormatoCorrecto() {
        Usuario usuario = Usuario.getInstance(context);
        // Inyectamos un mock de EspecificacionApp para controlar el tiempo devuelto
        EspecificacionApp mockApp = spy(new EspecificacionApp("Demo", "com.demo", 1000, context));
        doReturn(3600_000L).when(mockApp).obtenerTiempoDeUsoAplicacion(); // 1 hora

        // Usamos reflexión o acceso directo si fuera posible, pero como es lista privada/protegida usamos el método público add
        // Nota: Como getEspecificacionesApp devuelve la referencia al ArrayList, podemos manipularlo.
        usuario.getEspecificacionesApp().add(mockApp);

        String reporte = usuario.revisarTiempos();

        // Verificamos que el string contenga el formato esperado "Nombre,Tiempo"
        assertTrue("El reporte debe contener el nombre", reporte.contains("Demo"));
        assertTrue("El reporte debe contener el tiempo formateado", reporte.contains("1h 0m"));
    }

    @Test
    public void testBloquearApps_CambiaEstadoYMuestraDialogo() {
        Usuario usuario = Usuario.getInstance(context);
        // Estado inicial (asumimos false)
        if (usuario.isBloqueoGlobal()) usuario.bloquearApps(context); // Reset si estaba true

        assertFalse(usuario.isBloqueoGlobal());

        // Ejecutar bloqueo
        usuario.bloquearApps(context);

        assertTrue("El bloqueo global debería activarse", usuario.isBloqueoGlobal());

        // Verificar que se mostró un AlertDialog
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        assertNotNull("Debería haberse mostrado un diálogo", dialog);
        ShadowAlertDialog shadowDialog = org.robolectric.Shadows.shadowOf(dialog);
        assertEquals("App Bloqueada", shadowDialog.getTitle());
    }

    @Test
    public void testVerificarPermisosUsoEstadistica() {
        Context ctxMock = mock(Context.class);
        AppOpsManager managerMock = mock(AppOpsManager.class);

        when(ctxMock.getSystemService(Context.APP_OPS_SERVICE)).thenReturn(managerMock);
        when(ctxMock.getPackageName()).thenReturn("com.example.blockrott");

        // Caso: Permiso CONCEDIDO
        when(managerMock.checkOpNoThrow(
                eq(AppOpsManager.OPSTR_GET_USAGE_STATS),
                anyInt(),
                anyString()))
                .thenReturn(AppOpsManager.MODE_ALLOWED);

        Usuario user = Usuario.getInstance(context);
        assertTrue("Debería devolver true si el permiso está concedido", user.verificarPermisosUsoEstadistica(ctxMock));

        // Caso: Permiso DENEGADO
        when(managerMock.checkOpNoThrow(
                eq(AppOpsManager.OPSTR_GET_USAGE_STATS),
                anyInt(),
                anyString()))
                .thenReturn(AppOpsManager.MODE_IGNORED);

        assertFalse("Debería devolver false si el permiso no está concedido", user.verificarPermisosUsoEstadistica(ctxMock));
    }
}