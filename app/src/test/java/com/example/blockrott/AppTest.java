package com.example.blockrott;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import androidx.annotation.NonNull;
import backend.Usuario;
class MockEspecificacionApp {
    private String nombrePaquete;
    private long tiempoMaximoUso;
    private boolean isBloqueada;

    public MockEspecificacionApp(String nombrePaquete, long tiempoMaximoUso, boolean isBloqueada) {
        this.nombrePaquete = nombrePaquete;
        this.tiempoMaximoUso = tiempoMaximoUso;
        this.isBloqueada = isBloqueada;
    }

    public void agregarTiempoMaximoUso(long extensionMillis) {
        this.tiempoMaximoUso += extensionMillis;
        System.out.println(nombrePaquete + " - Límite extendido por " + extensionMillis + "ms. Nuevo Límite: " + tiempoMaximoUso);
    }

    public boolean isBloqueada() {
        return isBloqueada;
    }

    public void verificarLimiteTiempo() {
        // Lógica de verificación simulada
    }

    public String getNombrePaquete() {
        return nombrePaquete;
    }

    public long getTiempoMaximoUso() {
        return tiempoMaximoUso;
    }
}

// Clase de prueba mock para simular Usuario
class MockUsuario {
    private List<MockEspecificacionApp> especificacionesApp;

    public MockUsuario(List<MockEspecificacionApp> specs) {
        this.especificacionesApp = specs;
    }

    public void extenderTiempoLimite(String packageName, long extensionMillis) {
        for (MockEspecificacionApp app : especificacionesApp) {
            if (app.getNombrePaquete().equals(packageName)) {
                app.agregarTiempoMaximoUso(extensionMillis);
                if (app.isBloqueada()) {
                    app.verificarLimiteTiempo();
                }
                break;
            }
        }
    }
}

@RunWith(MockitoJUnitRunner.class)
public class AppTest {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.LENIENT);
    @Mock
    Context mockContext;
    @Mock
    RewardedAd mockRewardedAd;
    @Mock
    LoadAdError mockLoadAdError;
    @Mock
    Usuario mockUsuario;

    private class TestBlockerActivity {
        private RewardedAd rewardedAd;
        private Context context;
        private Usuario usuario;
        private final long EXTENSION_TIME_MS = 60 * 60 * 1000; // 1 hora
        private final String TAG = "BlockerActivity";

        public TestBlockerActivity(Context context, Usuario usuario) {
            this.context = context;
            this.usuario = usuario;
        }

        public void setRewardedAd(RewardedAd ad) {
            this.rewardedAd = ad;
        }
        public RewardedAd getRewardedAd() {
            return rewardedAd;
        }
        public void showRewardedAd(String packageName) {
            if (rewardedAd == null) {
                Log.e(TAG, "El auncio de recompensa aun no esta listo.");
                return;
            }
            rewardedAd.show((Activity) context, rewardItem -> {
                System.out.println("El usuario gana: " + rewardItem.getAmount());

                usuario.extenderTiempoLimite(packageName, EXTENSION_TIME_MS);

                System.out.println("Actividad finalizada.");
            });
        }
    }

    private TestBlockerActivity blockerActivity;

    @Before
    public void setUp() {
        blockerActivity = new TestBlockerActivity(mockContext, mockUsuario);
    }

    @Test
    public void testAgregarTiempoMaximoUso() {
        long initialTime = 10000L;
        long extension = 3600000L; // 1 hora
        MockEspecificacionApp appSpec = new MockEspecificacionApp("com.example.app", initialTime, false);
        appSpec.agregarTiempoMaximoUso(extension);
        long expectedTime = initialTime + extension;
        assertEquals("El tiempo máximo de uso debe ser la suma del tiempo inicial y la extensión.",
                expectedTime, appSpec.getTiempoMaximoUso());
    }

    @Test
    public void testExtenderTiempoLimite_AppExistenteBloqueada() {
        MockEspecificacionApp blockedApp = mock(MockEspecificacionApp.class);
        when(blockedApp.getNombrePaquete()).thenReturn("com.blocked.app");
        when(blockedApp.isBloqueada()).thenReturn(true);

        MockEspecificacionApp otherApp = mock(MockEspecificacionApp.class);

        List<MockEspecificacionApp> appList = new ArrayList<>();
        appList.add(blockedApp);
        appList.add(otherApp);

        MockUsuario usuario = new MockUsuario(appList);
        long extension = 300000L;
        usuario.extenderTiempoLimite("com.blocked.app", extension);

        verify(blockedApp).agregarTiempoMaximoUso(extension);
        verify(blockedApp).verificarLimiteTiempo();
        verifyNoInteractions(otherApp);
    }

    @Test
    public void testExtenderTiempoLimite_AppExistenteNoBloqueada() {
        MockEspecificacionApp nonBlockedApp = mock(MockEspecificacionApp.class);
        when(nonBlockedApp.getNombrePaquete()).thenReturn("com.nonblocked.app");
        when(nonBlockedApp.isBloqueada()).thenReturn(false);

        List<MockEspecificacionApp> appList = new ArrayList<>();
        appList.add(nonBlockedApp);

        MockUsuario usuario = new MockUsuario(appList);
        long extension = 1000L;

        usuario.extenderTiempoLimite("com.nonblocked.app", extension);

        verify(nonBlockedApp, times(1)).agregarTiempoMaximoUso(extension);
        verify(nonBlockedApp, never()).verificarLimiteTiempo();
    }

    @Test
    public void testExtenderTiempoLimite_AppNoExistente() {
        MockEspecificacionApp app1 = mock(MockEspecificacionApp.class);
        when(app1.getNombrePaquete()).thenReturn("com.app1");

        List<MockEspecificacionApp> appList = new ArrayList<>();
        appList.add(app1);

        MockUsuario usuario = new MockUsuario(appList);
        long extension = 5000L;

        usuario.extenderTiempoLimite("com.nonexistent.app", extension);

        verify(app1, never()).agregarTiempoMaximoUso(anyLong());
        verify(app1, never()).verificarLimiteTiempo();
    }

    @Test
    public void testLoadRewardedAd_CargaExitosa() {
        RewardedAdLoadCallback successCallback = new RewardedAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                fail("No se esperaba que la carga fallara.");
            }

            @Override
            public void onAdLoaded(@NonNull RewardedAd ad) {
                blockerActivity.setRewardedAd(ad);
            }
        };

        successCallback.onAdLoaded(mockRewardedAd);

        assertNotNull("El rewardedAd no debe ser nulo después de una carga exitosa.", blockerActivity.getRewardedAd());
        assertEquals("El rewardedAd asignado debe ser el mock proporcionado.", mockRewardedAd, blockerActivity.getRewardedAd());
    }

    @Test
    public void testLoadRewardedAd_CargaFallida() {
        RewardedAdLoadCallback failureCallback = new RewardedAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                blockerActivity.setRewardedAd(null);
            }

            @Override
            public void onAdLoaded(@NonNull RewardedAd ad) {
                fail("No se esperaba que la carga fuera exitosa.");
            }
        };
        blockerActivity.setRewardedAd(mockRewardedAd);
        failureCallback.onAdFailedToLoad(mockLoadAdError);

        assertNull("El rewardedAd debe ser nulo después de una carga fallida.", blockerActivity.getRewardedAd());
    }
}