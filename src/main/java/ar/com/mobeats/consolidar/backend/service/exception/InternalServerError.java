package ar.com.mobeats.consolidar.backend.service.exception;

import java.io.Serializable;

public class InternalServerError implements Serializable {

    private static final long serialVersionUID = 5908831902887077484L;
    
    private Long code;
    private String message;
    
    public InternalServerError() {
    }

    public InternalServerError(Long code, String message) {
        this.code = code;
        this.message = message;
    }

    public Long getCode() {
		return code;
	}
    
    public void setCode(Long code) {
		this.code = code;
	}
    
    public String getMessage() {
		return message;
	}
    
    public void setMessage(String message) {
		this.message = message;
	}
}
