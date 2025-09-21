package backend;

import java.util.ArrayList;

public class Usuario {
    String nombreUsuario;
    String idUsuario;
    ArrayList<EspecificacionUsuario> especificacionesUsuario;
    ConfiguracionVisual configuracionVisual;
    ArrayList<Conexion> conexiones;

    public boolean agregarEspecificacionNueva(EspecificacionUsuario especificacionNueva){
        return true;
    }

    public int revisarUso(EspecificacionUsuario especificacion){
        return especificacion.getTiempoUso();
    }

    public boolean verificarPermisos(){
        return true;
    }
}
