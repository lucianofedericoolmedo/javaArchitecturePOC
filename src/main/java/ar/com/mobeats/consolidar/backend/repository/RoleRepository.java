package ar.com.mobeats.consolidar.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import ar.com.mobeats.consolidar.backend.model.Rol;

public interface RoleRepository extends JpaRepository<Rol, Long>, JpaSpecificationExecutor<Rol> {
    
    Rol findOneByNombre(String string);

}
