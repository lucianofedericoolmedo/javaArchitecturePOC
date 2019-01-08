package ar.com.mobeats.consolidar.backend.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import ar.com.mobeats.consolidar.backend.model.Permiso;
import ar.com.mobeats.consolidar.backend.model.User;
import ar.com.mobeats.consolidar.backend.pagination.JPAPaginationRepository;

public interface UserRepository extends JPAPaginationRepository<User, String>, JpaSpecificationExecutor<User> {

    User findByUsername(String username);
    
    User findOneByUsername(String username);

    @Query("SELECT permiso FROM User user "
            + "JOIN user.roles rol "
            + "JOIN rol.permisos permiso "
            + "WHERE permiso.nombre = ?2 "
            + "AND user.id = ?1 ")
    Permiso userHasPermission(String userId, String permissionName);
}
