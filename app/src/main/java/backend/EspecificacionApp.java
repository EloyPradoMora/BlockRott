package backend;

import android.app.AlertDialog;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import java.util.Calendar;
import java.util.List;
public class EspecificacionApp {
    String nombreApp;
    String nombrePaquete;
    long tiempoUso;
    long tiempoMaximoUso;
    boolean bloqueada;
    private Context contexto;

    public EspecificacionApp(String nombreApp, String nombrePaquete, long tiempoMaximoUso, Context contexto){
        this.nombreApp = nombreApp;
        this.nombrePaquete= nombrePaquete;
        this.tiempoUso = 0;
        this.tiempoMaximoUso = tiempoMaximoUso;
        this.contexto = contexto;
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

    public void bloquearApp(){
        bloqueada = true;
        Log.d("BlockRott", "La app " + nombreApp + " ha sido marcada como bloqueada.");
    }

    public void desbloquearApp(){
        bloqueada = false;
        Log.d("BlockRott", "La app " + nombreApp + " ha sido marcada como desbloqueada.");
    }
    public void accionConApp(){
        if (bloqueada) {
            bloqueoApp();
        }
    }

    //Por ahora BloqueoApp solo tira una alerta cuando Bloqueda es true
    private void bloqueoApp()   {
        if (contexto == null) {
            Log.e("BlockRott", "El contexto es nulo, no se puede mostrar la alerta.");
            return;
        }
        new AlertDialog.Builder(contexto)
                .setTitle("App Bloqueada")
                .setMessage("Has excedido el tiempo de uso para la aplicación: " + nombreApp)
                .setPositiveButton("Entendido", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public String obtenerNombreLegibleApp() {
        return this.nombreApp;
    }
}
