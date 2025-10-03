package backend;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.util.Log;
import java.util.Calendar;
import java.util.List;

public class AccesoUsoApps {
    private Context contexto;

    public AccesoUsoApps(Context contexto) {
        this.contexto = contexto;
    }

    public String obtenerTiempoDeUsoAplicacion(String nombreAplicacion) {
        UsageStatsManager administradorUso = (UsageStatsManager)
                contexto.getSystemService(Context.USAGE_STATS_SERVICE);

        Calendar calendario = Calendar.getInstance();
        long tiempoFinal = calendario.getTimeInMillis();
        calendario.add(Calendar.DAY_OF_MONTH, -1);
        long tiempoInicial = calendario.getTimeInMillis();

        List<UsageStats> listaEstadisticas = administradorUso.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY, tiempoInicial, tiempoFinal);

        if (listaEstadisticas == null) {
            return "No se encontraron estadísticas de uso.";
        }

        for (UsageStats estadistica : listaEstadisticas) {
            if (estadistica.getPackageName().equals(nombreAplicacion)) {
                long tiempoMilisegundos = estadistica.getTotalTimeInForeground();
                if (tiempoMilisegundos > 0) {
                    String tiempoFormateado = convertirTiempoLegible(tiempoMilisegundos);
                    Log.d("AccesoUsoApps",
                            "Aplicación: " + nombreAplicacion
                                    + " -> " + tiempoFormateado);
                    return tiempoFormateado;
                } else {
                    return "0m";
                }
            }
        }
        return "Aplicación no encontrada.";

    }

    private String convertirTiempoLegible(long tiempoMilisegundos) {
        long segundos = tiempoMilisegundos / 1000;
        long minutos = segundos / 60;
        long horas = minutos / 60;
        minutos = minutos % 60;

        if (horas > 0) {
            return horas + "h " + minutos + "m";
        } else {
            return minutos + "m";
        }
    }

}
