package ar.com.mobeats.consolidar.backend.factory;

import ar.com.mobeats.consolidar.backend.factory.builder.LocalidadBuilder;

public class LocalidadFactory {

    public static Integer randomNumber() {
        return new Double(Math.random() * 5000).intValue();
    }

    public static LocalidadBuilder anyLocalidad() {
        LocalidadBuilder localidad = new LocalidadBuilder();
        localidad
            .withCodigo("codigo_" + randomNumber())
            .withValor("valor_" + randomNumber());
        return localidad;
    }
    
}
