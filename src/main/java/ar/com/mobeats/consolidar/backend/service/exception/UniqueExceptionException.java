package ar.com.mobeats.consolidar.backend.service.exception;


public class UniqueExceptionException extends RuntimeException {

    private static final long serialVersionUID = 4146864146542335710L;

    public UniqueExceptionException(String msg) {
        super(msg);
    }
    
}
