package ar.com.mobeats.consolidar.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import ar.com.mobeats.consolidar.backend.model.Permiso;

public interface PermissionRepository extends JpaRepository<Permiso, String>, JpaSpecificationExecutor<Permiso> {
    
    Permiso findOneByNombre(String nombre);

}
