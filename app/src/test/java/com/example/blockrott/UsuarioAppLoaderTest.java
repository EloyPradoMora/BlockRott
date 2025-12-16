package com.example.blockrott;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import androidx.test.core.app.ApplicationProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import backend.EspecificacionApp;
import backend.Usuario;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 34)
public class UsuarioAppLoaderTest {
    private Context context;
    private PackageManager packageManager;

    @Before
    public void setUp() {
        context = mock(Context.class);
        packageManager = mock(PackageManager.class);
        when(context.getPackageManager()).thenReturn(packageManager);
        when(context.getApplicationContext()).thenReturn(context);

        SharedPreferences sharedPreferences = mock(SharedPreferences.class);
        when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPreferences);
        when(sharedPreferences.getString(anyString(), anyString())).thenReturn(null);
        resetSingleton();
    }

    private void resetSingleton() {
        try {
            java.lang.reflect.Field instance = Usuario.class.getDeclaredField("instance");
            instance.setAccessible(true);
            instance.set(null, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCargarAppsInstaladas_LoadsSocialAndVideoApps() {
        List<PackageInfo> packages = new ArrayList<>();
        PackageInfo youtube = new PackageInfo();
        youtube.packageName = "com.google.android.youtube";
        youtube.applicationInfo = new ApplicationInfo();
        youtube.applicationInfo.packageName = "com.google.android.youtube";
        youtube.applicationInfo.category = ApplicationInfo.CATEGORY_UNDEFINED;
        youtube.applicationInfo.nonLocalizedLabel = "YouTube";
        packages.add(youtube);

        PackageInfo facebook = new PackageInfo();
        facebook.packageName = "com.facebook.katana";
        facebook.applicationInfo = new ApplicationInfo();
        facebook.applicationInfo.packageName = "com.facebook.katana";
        facebook.applicationInfo.category = ApplicationInfo.CATEGORY_SOCIAL;
        facebook.applicationInfo.nonLocalizedLabel = "Facebook";
        packages.add(facebook);

        PackageInfo netflix = new PackageInfo();
        netflix.packageName = "com.netflix.mediaclient";
        netflix.applicationInfo = new ApplicationInfo();
        netflix.applicationInfo.packageName = "com.netflix.mediaclient";
        netflix.applicationInfo.category = ApplicationInfo.CATEGORY_VIDEO;
        netflix.applicationInfo.nonLocalizedLabel = "Netflix";
        packages.add(netflix);

        PackageInfo calculator = new PackageInfo();
        calculator.packageName = "com.android.calculator2";
        calculator.applicationInfo = new ApplicationInfo();
        calculator.applicationInfo.name = "Calculator";
        calculator.applicationInfo.category = ApplicationInfo.CATEGORY_PRODUCTIVITY;
        packages.add(calculator);

        PackageInfo random = new PackageInfo();
        random.packageName = "com.example.random";
        random.applicationInfo = new ApplicationInfo();
        packages.add(random);

        PackageInfo gmail = new PackageInfo();
        gmail.packageName = "com.google.android.gm";
        gmail.applicationInfo = new ApplicationInfo();
        gmail.applicationInfo.packageName = "com.google.android.gm";
        gmail.applicationInfo.category = ApplicationInfo.CATEGORY_SOCIAL;
        gmail.applicationInfo.nonLocalizedLabel = "Gmail";
        packages.add(gmail);

        PackageInfo chrome = new PackageInfo();
        chrome.packageName = "com.android.chrome";
        chrome.applicationInfo = new ApplicationInfo();
        chrome.applicationInfo.category = ApplicationInfo.CATEGORY_PRODUCTIVITY;
        chrome.applicationInfo.nonLocalizedLabel = "Chrome";
        packages.add(chrome);

        when(packageManager.getInstalledPackages(0)).thenReturn(packages);
        Usuario usuario = Usuario.getInstance(context);
        ArrayList<EspecificacionApp> apps = usuario.getEspecificacionesApp();
        assertEquals("Should have found 3 matching apps", 3, apps.size());
        List<String> loadedPackageNames = new ArrayList<>();
        for (EspecificacionApp app : apps) {
            loadedPackageNames.add(app.getNombrePaquete());
        }
        assertTrue(loadedPackageNames.contains("com.google.android.youtube"));
        assertTrue(loadedPackageNames.contains("com.facebook.katana"));
        assertTrue(loadedPackageNames.contains("com.netflix.mediaclient"));

        for (EspecificacionApp app : apps) {
            String pkg = app.getNombrePaquete();
            if (pkg.equals("com.google.android.gm") || pkg.equals("com.android.chrome")) {
                throw new AssertionError("Blacklisted app found: " + pkg);
            }
        }

        for (EspecificacionApp app : apps) {
            if ("com.google.android.youtube".equals(app.getNombrePaquete())) {
                assertEquals("YouTube should have 1 minute limit", 60000L, app.getTiempoMaximoUso());
            } else {
                assertEquals("Default time limit should be 1 hour", 3600000L, app.getTiempoMaximoUso());
            }
        }
    }
}