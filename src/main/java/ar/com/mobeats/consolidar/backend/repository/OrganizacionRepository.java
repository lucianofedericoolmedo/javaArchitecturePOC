package ar.com.mobeats.consolidar.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import ar.com.mobeats.consolidar.backend.model.Organizacion;

public interface OrganizacionRepository extends JpaRepository<Organizacion, String>, JpaSpecificationExecutor<Organizacion> {
    
    Organizacion findOneByNombre(String nombre);

}