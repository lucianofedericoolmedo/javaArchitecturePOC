package ar.com.mobeats.consolidar.backend.service.exception;

public class UnauthorizedException extends RuntimeException {

    private static final long serialVersionUID = -5672567588735793025L;
    
    public UnauthorizedException() {
    }
    
    public UnauthorizedException(String message) {
        super(message);
    }
    
    public UnauthorizedException(Throwable t) {
        super(t);
    }

}
