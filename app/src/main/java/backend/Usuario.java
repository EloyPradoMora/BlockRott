package backend;

import android.app.AlertDialog;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.DialogInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

public class Usuario {
    private String nombreUsuario;
    private String idUsuario;
    private ArrayList<EspecificacionApp> especificacionesApp;
    ConfiguracionVisual configuracionVisual;
    ArrayList<Conexion> conexiones;

    boolean bloqueoGlobal;

    public Usuario(){
        especificacionesApp = new ArrayList<>();
    }

    public boolean agregarEspecificacionNueva(String nombreApp, String nombrePaquete, long tiempoMaximoDeUso, Context contexto){
        this.especificacionesApp.add(new EspecificacionApp(nombreApp, nombrePaquete,tiempoMaximoDeUso, contexto));
        bloqueoGlobal = false;
        return true;
    }

    public String revisarTiempos(){
        String resultado= "";
        for (EspecificacionApp especificacion: especificacionesApp) {
            resultado += especificacion.obtenerNombreLegibleApp() + "," + revisarUso(especificacion) + "\n";
        }
        return resultado;
    }
    public String revisarUso(EspecificacionApp especificacion){
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

    public void bloquearApps(Context context){
        for (EspecificacionApp especifico: this.especificacionesApp) {
            if (!bloqueoGlobal) {
                especifico.bloquearApp();
            }else {
                especifico.desbloquearApp();
            }
        }
        mostrarMensajeDeBloqueo(context);
        this.bloqueoGlobal = !bloqueoGlobal;
    }
    private void mostrarMensajeDeBloqueo(Context context){
        if(!bloqueoGlobal){
            new AlertDialog.Builder(context)
                    .setTitle("App Bloqueada")
                    .setMessage("Se han bloqueado las aplicaciones")
                    .setPositiveButton("Entendido", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }else{
            new AlertDialog.Builder(context)
                    .setTitle("App Desbloqueada")
                    .setMessage("Se han liberado las aplicaciones")
                    .setPositiveButton("Entendido", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }
}