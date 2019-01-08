package ar.com.mobeats.consolidar.backend.service.exception;

public class ChangePasswordException extends RuntimeException {

    private static final long serialVersionUID = 8938210723928544036L;
    
    public ChangePasswordException(String message) {
        super(message);
    }
}
