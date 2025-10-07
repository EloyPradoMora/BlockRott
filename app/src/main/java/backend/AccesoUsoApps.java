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

    public long obtenerTiempoDeUsoAplicacion(String nombreAplicacion) {
        UsageStatsManager administradorUso = (UsageStatsManager)
                contexto.getSystemService(Context.USAGE_STATS_SERVICE);

        Calendar calendario = Calendar.getInstance();
        long tiempoFinal = calendario.getTimeInMillis();
        calendario.add(Calendar.DAY_OF_MONTH, -1);
        long tiempoInicial = calendario.getTimeInMillis();

        List<UsageStats> listaEstadisticas = administradorUso.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY, tiempoInicial, tiempoFinal);

        if (listaEstadisticas == null) {
            return 0;
        }

        for (UsageStats estadistica : listaEstadisticas) {
            if (estadistica.getPackageName().equals(nombreAplicacion)) {
                long tiempoMilisegundos = estadistica.getTotalTimeInForeground();
                Log.d("AccesoUsoApps",
                            "App: " + nombreAplicacion
                                    + " -> " + convertirTiempoLegible(tiempoMilisegundos));
                    return tiempoMilisegundos;
                }

            }
        return 0;
    }

    public long calcularTiempoUsoTotal() {
        long totalMilis = 0;
        totalMilis += obtenerTiempoDeUsoAplicacion("com.zhiliaoapp.musically");
        totalMilis += obtenerTiempoDeUsoAplicacion("com.google.android.youtube");
        totalMilis += obtenerTiempoDeUsoAplicacion("com.reddit.frontpage");
        return totalMilis;
    }


    private String convertirTiempoLegible(long tiempoMilisegundos) {
        long segundos = tiempoMilisegundos / 1000;
        long minutos = segundos / 60;
        long horas = minutos / 60;
        minutos = minutos % 60;

        if (horas > 0) {
            return horas + "h " + minutos + "m";
        } else if (minutos > 0){
            return minutos + "m";
        }else{
            return "0m";
        }
    }

    }

}
