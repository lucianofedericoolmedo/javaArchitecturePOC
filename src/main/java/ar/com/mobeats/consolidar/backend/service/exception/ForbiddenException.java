package ar.com.mobeats.consolidar.backend.service.exception;

public class ForbiddenException extends RuntimeException {

    private static final long serialVersionUID = -5190175422286428079L;

    public ForbiddenException() {
    }
    
    public ForbiddenException(String message) {
        super(message);
    }
    
    public ForbiddenException(Throwable t) {
        super(t);
    }

}
