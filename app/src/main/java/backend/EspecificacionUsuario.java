package backend;

public class EspecificacionUsuario {
    String nombreApp;
    int tiempoUso;
    int tiempoMaximoUso;
    boolean bloqueada;

    public int getTiempoUso() {
        return tiempoUso;
    }

    public void bloquearApp(){
        bloqueada = true;
    }

    public void desploquearApp(){
        bloqueada = false;
    }

    public void accionConApp(){}
}
