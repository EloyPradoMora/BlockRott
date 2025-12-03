package backend;

import android.app.AlertDialog;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.WindowManager;
import java.util.Calendar;
import java.util.List;
import android.content.Context;
import android.util.Log;
public class EspecificacionApp {
    private String nombreApp;
    private String nombrePaquete;
    private long tiempoUso;
    private long tiempoMaximoUso;
    protected boolean bloqueada;
    private Context contexto;

    public EspecificacionApp(String nombreApp, String nombrePaquete, long tiempoMaximoUso, Context contexto){
        this.nombreApp = nombreApp;
        this.nombrePaquete= nombrePaquete;
        this.tiempoUso = 0;
        this.tiempoMaximoUso = tiempoMaximoUso;
        this.contexto = contexto.getApplicationContext();
        this.bloqueada = false;
    }

    public long obtenerTiempoDeUsoAplicacion() {
        UsageStatsManager administradorUso = (UsageStatsManager)
                contexto.getSystemService(Context.USAGE_STATS_SERVICE);
        long tiempoFinal = System.currentTimeMillis();
        Calendar calendario = Calendar.getInstance();
        calendario.set(Calendar.HOUR_OF_DAY, 0);
        calendario.set(Calendar.MINUTE, 0);
        calendario.set(Calendar.SECOND, 0);
        calendario.set(Calendar.MILLISECOND, 0);
        long tiempoInicial = calendario.getTimeInMillis();

        List<UsageStats> listaEstadisticas = administradorUso.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY, tiempoInicial, tiempoFinal);
        if (listaEstadisticas == null) {
            return 0;
        }
        String nombreDisplay = obtenerNombreLegibleApp();
        for (UsageStats estadistica : listaEstadisticas) {
            if (estadistica.getPackageName().equals(nombrePaquete)) {
                long tiempoMilisegundos = estadistica.getTotalTimeInForeground();
                Log.d("AccesoUsoApps",
                        "App: " + nombreDisplay
                                + " -> " + convertirTiempoLegible(tiempoMilisegundos));
                this.tiempoUso = tiempoMilisegundos;
                return tiempoMilisegundos;
            }
        }
        return 0;
    }

    public String convertirTiempoLegible(long tiempoMilisegundos) {
        long segundos = tiempoMilisegundos / 1000;
        long minutos = segundos / 60;
        long horas = minutos / 60;
        minutos = minutos % 60;

        if (horas > 0) {
            return horas + "h " + minutos + "m";
        }else if (minutos > 0){
            return minutos + "m";
        }else{
            return "0m";
        }
    }
    public String getNombreApp() { return nombreApp; }

    public void verificarLimiteTiempo() {
        this.tiempoUso = obtenerTiempoDeUsoAplicacion();
        if (this.tiempoUso > tiempoMaximoUso) {
            this.bloqueada = true;
        } else {
            this.bloqueada = false;
        }
        Log.d("BlockRott", nombreApp + " - Uso Actual: " + this.tiempoUso +
                ", Límite: " + tiempoMaximoUso + ", Bloqueada: " + this.bloqueada);
    }
    public String obtenerNombreLegibleApp() {
        return this.nombreApp;
    }

    public String getNombrePaquete() { return this.nombrePaquete; }

    public boolean isBloqueada() { return this.bloqueada; }
}