package ar.com.mobeats.consolidar.backend.dto;

import java.io.Serializable;

public class WrappedLong implements Serializable {

    private static final long serialVersionUID = -5223849039413378944L;
    
    private final Long value;
    
    public WrappedLong(Long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }
}
