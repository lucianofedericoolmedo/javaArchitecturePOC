package ar.com.mobeats.consolidar.backend.factory.builder;

import ar.com.mobeats.consolidar.backend.model.Localidad;

public class LocalidadBuilder {

    // Instance Variables
    private Localidad buildingLocalidad;
    
    // Constructors
    public LocalidadBuilder() {
        this.buildingLocalidad = new Localidad();
    }
    
    public LocalidadBuilder(Localidad localidad) {
        this.buildingLocalidad = localidad;
    }
    
    // Methods
    public LocalidadBuilder setLocalidad(Localidad localidad) {
        this.buildingLocalidad = localidad;
        return this;
    }
    
    public Localidad getLocalidad() {
        return this.buildingLocalidad;
    }
    
    public LocalidadBuilder withCodigo(String codigo) {
        this.buildingLocalidad.setCodigo(codigo);
        return this;
    }

    public LocalidadBuilder withValor(String valor) {
        this.buildingLocalidad.setValor(valor);
        return this;
    }

}
