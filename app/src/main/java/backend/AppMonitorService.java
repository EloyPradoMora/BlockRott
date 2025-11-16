package backend;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import backend.BlockerActivity;
import com.example.blockrott.R;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class AppMonitorService extends Service {

    private static final String TAG = "AppMonitorService";
    private static final String CHANNEL_ID = "AppMonitorChannel";
    private static final int NOTIFICATION_ID = 1;
    private static final long MONITOR_INTERVAL_MS = 1500;
    private Handler monitoringHandler;
    private Runnable monitoringRunnable;
    private Usuario usuario;
    private String lastBlockedPackage = "";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Servicio OnCreate");
        this.usuario = Usuario.getInstance(getApplicationContext());
        monitoringHandler = new Handler();
        monitoringRunnable = this::runMonitoringLoop;
    }

    @SuppressLint("ForegroundServiceType")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Servicio OnStartCommand");
        createNotificationChannel();

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("BlockRott Activo")
                .setContentText("Monitoreando el uso de aplicaciones.")
                .setSmallIcon(R.drawable.ic_launcher_foreground) // <-- Cambiar esto por nuestro icono IMPORTANTE
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        startForeground(NOTIFICATION_ID, notification);

        // Inicia el bucle de monitoreo
        monitoringHandler.post(monitoringRunnable);

        return START_STICKY; // El servicio se reiniciará si el sistema lo mata
    }

    private void runMonitoringLoop() {
        if (usuario == null) {
            Log.e(TAG, "Usuario es nulo, deteniendo el bucle.");
            return;
        }
        usuario.monitoreoApps();
        String foregroundApp = getForegroundAppPackageName();
        if (foregroundApp != null && foregroundApp.equals(getPackageName())) {
            monitoringHandler.postDelayed(monitoringRunnable, MONITOR_INTERVAL_MS);
            return; // estamos en blockrott no hacemos nada, pero hay que seguir monitoriando
        }
        if (foregroundApp == null || foregroundApp.equals(getPackageName())) {
            monitoringHandler.postDelayed(monitoringRunnable, MONITOR_INTERVAL_MS);
            return; //terminamos altiro si es que la app actual del usuario no esta en la lista de apps a bloquear
        }
        boolean appIsBlocked = false;
        boolean isGlobalBlock = usuario.isBloqueoGlobal();
        for (EspecificacionApp app : usuario.getEspecificacionesApp()) {
            if (app.getNombrePaquete().equals(foregroundApp)) {
                if (isGlobalBlock || app.isBloqueada()) {
                    appIsBlocked = true;
                    if (!foregroundApp.equals(lastBlockedPackage)) {
                        Log.d(TAG, "Bloqueando app: " + foregroundApp);
                        showBlockerScreen(foregroundApp);
                    }
                }
                break; //Terminamos aqui ya que encontramos una app para bloquear
            }
        }
        if (!appIsBlocked) { //esto esta para que si el usuario se sale de la app bloqueada y trata de volver a entrar el bloqeo salte de nuevo
            if (!lastBlockedPackage.isEmpty()) {
                Log.d(TAG, "Clearing last blocked app: " + lastBlockedPackage);
            }
            lastBlockedPackage = "";
        }
        monitoringHandler.postDelayed(monitoringRunnable, MONITOR_INTERVAL_MS);
    }

    private void showBlockerScreen(String packageName) {
        lastBlockedPackage = packageName;
        Intent intent = new Intent(this, BlockerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("PACKAGE_NAME", packageName);
        startActivity(intent);
    }

    private String getForegroundAppPackageName() {
        UsageStatsManager usm = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        if (usm == null) return null;
        long time = System.currentTimeMillis();
        List<UsageStats> stats = usm.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY, time - (60 * 1000), time);

        if (stats != null && !stats.isEmpty()) {
            SortedMap<Long, UsageStats> sortedStats = new TreeMap<>();
            for (UsageStats usageStats : stats) {
                sortedStats.put(usageStats.getLastTimeUsed(), usageStats);
            }
            if (!sortedStats.isEmpty()) {
                return sortedStats.get(sortedStats.lastKey()).getPackageName();
            }
        }
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Servicio OnDestroy");
        monitoringHandler.removeCallbacks(monitoringRunnable);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Canal de Monitoreo de App",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }
}