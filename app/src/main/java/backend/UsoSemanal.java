package backend;

import android.content.Context;
import android.content.SharedPreferences;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;

// Clase encargada de compilar estadisticas semanales
public class UsoSemanal {

    private final Context context;

    public UsoSemanal(Context context) {
        this.context = context;
    }

    // Retorna datos detallados por dia para su uso en graficos locales
    public Map<String, Long> obtenerEstadisticasSemanales(String packageName) {
        // Accedemos a las mismas preferencias que UsoDiario
        SharedPreferences prefs = context.getSharedPreferences(UsoDiario.PREFS_NAME, Context.MODE_PRIVATE);

        LocalDate hoy = LocalDate.now();
        // Empezamos la semana el Lunes
        LocalDate inicioSemana = hoy.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        Map<String, Long> estadisticasSemana = new HashMap<>();

        for (int i = 0; i < 7; i++) {
            LocalDate fecha = inicioSemana.plusDays(i);
            String clave = UsoDiario.KEY_PREFIX + packageName + "_" + fecha.toString();

            if (prefs.contains(clave)) {
                long tiempoMilis = prefs.getLong(clave, 0);
                estadisticasSemana.put(fecha.toString(), tiempoMilis);
            } else {
                // Si no existe la clave, no ponemos nada en el mapa para indicar "no data"
                // Opcionalmente podriamos poner null explicitamente si cambiaramos el value a
                // Long
                estadisticasSemana.put(fecha.toString(), null);
            }
        }

        return estadisticasSemana;
    }
}
