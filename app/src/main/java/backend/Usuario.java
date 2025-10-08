package backend;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

public class Usuario {
    private String nombreUsuario;
    private String idUsuario;
    private ArrayList<EspecificacionUsuario> especificacionesUsuario;
    ConfiguracionVisual configuracionVisual;
    ArrayList<Conexion> conexiones;

    public Usuario(){
        especificacionesUsuario = new ArrayList<>();
    }

    public boolean agregarEspecificacionNueva(String nombreApp, String nombrePaquete, long tiempoMaximoDeUso, Context contexto){
        this.especificacionesUsuario.add(new EspecificacionUsuario(nombreApp, nombrePaquete,tiempoMaximoDeUso, contexto));
        return true;
    }

    public String revisarTiempos(){
        String resultado= "";
        for (EspecificacionUsuario especificacion: especificacionesUsuario) {
            resultado += especificacion.obtenerNombreLegibleApp() + "," + revisarUso(especificacion) + "\n";
        }
        return resultado;
    }
    public String revisarUso(EspecificacionUsuario especificacion){
        long rawTimeMillis = especificacion.obtenerTiempoDeUsoAplicacion();
        return especificacion.convertirTiempoLegible(rawTimeMillis);
    }
    /**
    *@param
    *@return
    */
    public boolean verificarPermisos(Context context){
        try {
            UsageStatsManager usageStatsManager =
                    (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);

            if (usageStatsManager == null) {
                return false;
            }
            long currentTime = System.currentTimeMillis();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.SECOND, -1);
            long startTime = calendar.getTimeInMillis();

            List<UsageStats> queryResult = usageStatsManager.queryUsageStats(
                    UsageStatsManager.INTERVAL_DAILY,
                    startTime,
                    currentTime
            );

            return queryResult != null && !queryResult.isEmpty();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}