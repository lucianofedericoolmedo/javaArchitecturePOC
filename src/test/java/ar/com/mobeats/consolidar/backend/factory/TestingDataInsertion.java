package ar.com.mobeats.consolidar.backend.factory;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.mobeats.consolidar.backend.model.Localidad;
import ar.com.mobeats.consolidar.backend.service.LocalidadService;

@Service
public class TestingDataInsertion {

    @Autowired
    LocalidadService localidadService;

    public Localidad localidad;

    @PostConstruct
    public void inserData() {
        localidad = LocalidadFactory.anyLocalidad().getLocalidad();
        localidad = localidadService.create(localidad);
    }

}
