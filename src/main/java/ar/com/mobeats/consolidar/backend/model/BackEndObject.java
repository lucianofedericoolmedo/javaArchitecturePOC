package ar.com.mobeats.consolidar.backend.model;

import java.io.Serializable;

import ar.com.mobeats.consolidar.backend.util.JSONObjectConverter;

public abstract class BackEndObject implements Serializable {

    private static final long serialVersionUID = -3704224041413428471L;
    
    @Override
    public String toString() {
        return JSONObjectConverter.convertToJSON(this);
    }
    
}
