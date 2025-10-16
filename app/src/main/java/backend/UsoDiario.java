package backend;

import android.content.Context;
import android.content.SharedPreferences;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;
import android.util.Log;


public class UsoDiario {

    // constantes para almacenar localmente

    private static final String PREFS_NAME = "UsoDiarioPrefs";
    private static final String KEY_PREFIX = "uso_diario_";

    private final Context context;

    public UsoDiario(Context context) {
        this.context = context;
    }

    // guarda el tiempo de uso en milisegundos para un paquete en un día

    public void guardarUsoDiario(String packageName, long tiempoUsoMilisegundos) {
        String fechaHoy = LocalDate.now().toString(); // formato YYYY-MM-dd
        String clave = KEY_PREFIX + packageName + "_" + fechaHoy;

        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putLong(clave, tiempoUsoMilisegundos).apply();
    }

    private Map<String, Object> calcularPromedioSemanal(String packageName) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Map<String, ?> allEntries = prefs.getAll();

        long tiempoTotalMilis = 0;
        int diasContados = 0;

        // Determinar el Lunes de la semana actual para definir el rango de 7 dias
        LocalDate hoy = LocalDate.now();
        LocalDate inicioSemana = hoy.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));


        for (int i = 0; i < 7; i++) {
            LocalDate fecha = inicioSemana.plusDays(i);
            String clave = KEY_PREFIX + packageName + "_" + fecha.toString();

            Object valor = allEntries.get(clave);

            if (valor instanceof Long) {
                long usoMilis = (Long) valor;
                tiempoTotalMilis += usoMilis;
                diasContados++;
            }
        }

        // Calculo de promedio en segundos
        long promedioSegundos = (diasContados > 0) ? (tiempoTotalMilis / diasContados) / 1000 : 0;

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("semanaInicio", inicioSemana.toString()); // La fecha de inicio de la semana (Lunes)
        resultado.put("promedioUsoSeg", promedioSegundos);
        resultado.put("diasContados", diasContados);

        return resultado;
    }

    // se encarga de generar el reporte para springboot
    public Map<String, Object> obtenerReporteParaEnvio(String correoUsuario, String nombreApp, String packageName) {
        Map<String, Object> calculo = calcularPromedioSemanal(packageName);

        Map<String, Object> reporteFinal = new HashMap<>();

        // Mapeo directo a ReporteSemanalRequest
        reporteFinal.put("usuarioCorreo", correoUsuario);
        reporteFinal.put("nombreApp", nombreApp);
        reporteFinal.put("semanaInicio", calculo.get("semanaInicio"));
        reporteFinal.put("promedioUsoSeg", calculo.get("promedioUsoSeg"));

        return reporteFinal;
    }





}