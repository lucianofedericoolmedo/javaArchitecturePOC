package ar.com.mobeats.consolidar.backend.service.exception;

public class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = -6772473082599618122L;

    public NotFoundException(){
        super();
    }
    
    public NotFoundException(String message){
        super(message);
    }
}
