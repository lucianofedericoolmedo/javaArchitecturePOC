package ar.com.mobeats.consolidar.backend.dto;

import java.io.Serializable;

public class WrappedBoolean implements Serializable {

    private static final long serialVersionUID = -7264336519301938125L;

    private final boolean value;

    public WrappedBoolean(boolean value) {
        this.value = value;
    }
    
    public boolean getValue() {
        return value;
    }
    
}
