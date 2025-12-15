package backend;

import android.app.AlertDialog;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.app.AppOpsManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
        cargarAppsInstaladas(context);
    }

    private void cargarAppsInstaladas(Context context) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
        for (PackageInfo packageInfo : installedPackages) {
            if (isAppExcluded(packageInfo.packageName)) {
                continue;
            }
            if ("com.google.android.youtube".equals(packageInfo.packageName)) {
                agregarSiNoExiste(packageInfo, pm, 60 * 1000L);
                continue;
            }
            if (packageInfo.applicationInfo != null) {
                int category = packageInfo.applicationInfo.category;
                if (category == ApplicationInfo.CATEGORY_SOCIAL || category == ApplicationInfo.CATEGORY_VIDEO) {
                    agregarSiNoExiste(packageInfo, pm, 3600000L);
                }
            }
        }
    }

    //Aqui agregamos las apps que no queremos bloquear
    private boolean isAppExcluded(String packageName) {
        return packageName.startsWith("com.android.chrome") ||
                packageName.startsWith("com.google.android.gm") ||
                packageName.startsWith("com.google.android.apps.messaging") ||
                packageName.startsWith("com.android.messaging") ||
                packageName.startsWith("com.android.contacts") ||
                packageName.startsWith("com.google.android.contacts") ||
                packageName.startsWith("com.google.android.calendar") ||
                packageName.startsWith("com.android.calendar") ||
                packageName.startsWith("com.google.android.apps.meetings") ||
                packageName.startsWith("com.brave.browser") ||
                packageName.startsWith("com.google.android.videos") ||
                packageName.startsWith("com.whatsapp") ||
                packageName.startsWith("com.facebook.orca") ||
                packageName.startsWith("com.discord") ||
                packageName.startsWith("org.telegram.messenger") ||
                packageName.startsWith("com.truecaller") ||
                packageName.startsWith("com.sec.spp.push") || // Samsung Push Service
                packageName.startsWith("com.google.android.ims") || // Carrier Services
                packageName.startsWith("com.sec.android.app.sbrowser") || // Samsung Internet
                packageName.startsWith("com.google.audio.hearing.visualization.accessibility.scribe") || // Live
                packageName.startsWith("com.google.android.apps.docs.editors"); // Edits (office pero de google)
    }

    private void agregarSiNoExiste(PackageInfo packageInfo, PackageManager pm, long tiempoMaximo) {
        String packageName = packageInfo.packageName;
        for (EspecificacionApp app : especificacionesApp) {
            if (app.getNombrePaquete().equals(packageName)) {
                return;
            }
        }
        String appName = packageInfo.applicationInfo.loadLabel(pm).toString();
        agregarEspecificacionNueva(appName, packageName, tiempoMaximo);
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
                    context.getPackageName());
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

    private List<String> appsBloqueadasGlobalmente = new ArrayList<>();

    public boolean isAppBlockedGlobal(String packageName) {
        return this.bloqueoGlobal && this.appsBloqueadasGlobalmente.contains(packageName);
    }

    public void bloquearApps(Context context, List<String> appNames) {
        this.bloqueoGlobal = !this.bloqueoGlobal;

        if (this.bloqueoGlobal) {
            // activando el bloqueo
            this.appsBloqueadasGlobalmente.clear();
            for (String name : appNames) {
                for (EspecificacionApp spec : especificacionesApp) {
                    if (spec.getNombreApp().equals(name)) {
                        this.appsBloqueadasGlobalmente.add(spec.getNombrePaquete());
                        break;
                    }
                }
            }
        } else {
            //desactivando el bloqueo
            this.appsBloqueadasGlobalmente.clear();
        }
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
                return;
            }
        }
    }

    public void actualizarTiempoLimite(String nombrePaquete, long nuevoTiempo) {
        for (EspecificacionApp app : especificacionesApp) {
            if (app.getNombrePaquete().equals(nombrePaquete)) {
                app.setTiempoMaximoUso(nuevoTiempo);
                // Si cambiamos el tiempo vemos si debe bloquear o desbloquear
                app.verificarLimiteTiempo();
                return;
            }
        }
    }
}