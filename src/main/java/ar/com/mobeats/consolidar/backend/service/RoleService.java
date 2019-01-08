package ar.com.mobeats.consolidar.backend.service;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.com.mobeats.consolidar.backend.model.Permiso;
import ar.com.mobeats.consolidar.backend.model.Rol;
import ar.com.mobeats.consolidar.backend.repository.RoleRepository;
import ar.com.mobeats.consolidar.backend.repository.specification.RolSpecification;
import ar.com.mobeats.consolidar.backend.service.exception.NotFoundException;

@Service
public class RoleService {

    private static final Logger LOGGER = Logger.getLogger(RoleService.class);

    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    public Rol create(Rol role) {
        Rol saved = roleRepository.save(role);

        LOGGER.info("Rol creado: " + saved.toString());
        return saved;
    }

    @Transactional
    public Rol update(Rol role) {
        Rol updated = roleRepository.save(role);

        LOGGER.info("Rol actualizado: " + updated.toString());
        return updated;
    }

    public Rol get(Long id) throws NotFoundException {
        Rol role = roleRepository.findOne(id);
        if (role == null) {
            String msg = "GET Rol: No se encontró Rol con ID " + String.valueOf(id);
            LOGGER.debug(msg);
            throw new NotFoundException(msg);
        } else {
            LOGGER.debug("GET Rol: " + role.toString());
        }
        return role;
    }

    @Transactional
    public void delete(Rol role) {
        roleRepository.delete(role);

        LOGGER.info("Rol borrado: " + role.toString());
    }

    public List<Rol> find(String nombre) {
        List<Rol> roles = roleRepository.findAll(RolSpecification.andIgnoringBlanks(nombre));

        if (roles == null || roles.isEmpty()) {
            LOGGER.debug("GET Roles: no encontrados para nombre: " + nombre);
        } else {
            LOGGER.debug("GET Roles: cantidad encontrada " + roles.size());
        }
        return roles;
    }

    public RoleRepository getRoleRepository() {
        return roleRepository;
    }

    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Rol crearRol(String string, Set<Permiso> permisos) {
        Rol rol = this.roleRepository.findOneByNombre(string);
        if (rol == null) {
            rol = new Rol();
            rol.setNombre(string);
        }
        rol.setPermisos(permisos);
        this.roleRepository.save(rol);
        return rol;
    }

}
