package ar.com.mobeats.consolidar.backend.service.strategy.validacion;

public class Validacion {

    private String mensaje;
    private Boolean exitoso;

    public Validacion( String mensaje, Boolean exitoso ) {
        this.mensaje = mensaje;
        this.exitoso = exitoso;
    }
    
    public String getMensaje() {
        return mensaje;
    }

    public Boolean isNotSuccessful() {
        return !exitoso;
    }
    
}
