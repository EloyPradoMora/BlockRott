package backend;

import android.app.AlertDialog;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.app.AppOpsManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

public class Usuario {
    private static volatile Usuario instance;
    private Context applicationContext;
    private String nombreUsuario;
    private String idUsuario;
    private ArrayList<EspecificacionApp> especificacionesApp;
    private ConfiguracionVisual configuracionVisual;
    private ArrayList<Conexion> conexiones;

    private boolean bloqueoGlobal;
    public boolean agregarEspecificacionNueva(String nombreApp, String nombrePaquete, long tiempoMaximoDeUso){
        this.especificacionesApp.add(new EspecificacionApp(nombreApp, nombrePaquete,tiempoMaximoDeUso, this.applicationContext));
        return true;
    }

    private Usuario(Context context){
        this.applicationContext = context.getApplicationContext(); // Importante
        especificacionesApp = new ArrayList<>();
        this.bloqueoGlobal = false;
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
    public boolean verificarPermisosUsoEstadistica(Context context){
        try {
            AppOpsManager appOpsManager =
                    (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);

            if (appOpsManager == null) {
                return false;
            }

            int mode = appOpsManager.checkOpNoThrow(
                    AppOpsManager.OPSTR_GET_USAGE_STATS,
                    android.os.Process.myUid(),
                    context.getPackageName()
            );
            return mode == AppOpsManager.MODE_ALLOWED;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean verificarPermisoSuperposicion(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context);
        }
        return true;
    }

    public void bloquearApps(Context context){
        this.bloqueoGlobal = !this.bloqueoGlobal;
        mostrarMensajeDeBloqueo(context);
    }
    private void mostrarMensajeDeBloqueo(Context context){
        if(bloqueoGlobal){
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

    public void monitoreoApps(){
        for (EspecificacionApp especifico: this.especificacionesApp) {
            especifico.verificarLimiteTiempo();
        }
    }

    public static Usuario getInstance(Context context) {
        if (instance == null) {
            synchronized (Usuario.class) {
                if (instance == null) {
                    instance = new Usuario(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    public boolean isBloqueoGlobal() {
        return this.bloqueoGlobal;
    }
    public ArrayList<EspecificacionApp> getEspecificacionesApp() {
        return this.especificacionesApp;
    }
    public void extenderTiempoLimite(String packageName, long extensionMillis) {
        for (EspecificacionApp app : especificacionesApp) {
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