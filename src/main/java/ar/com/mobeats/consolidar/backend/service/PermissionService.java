package ar.com.mobeats.consolidar.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.mobeats.consolidar.backend.model.Permiso;
import ar.com.mobeats.consolidar.backend.repository.PermissionRepository;

@Service
public class PermissionService {

    private static final Logger LOGGER = Logger.getLogger(RoleService.class);

    @Autowired
    private PermissionRepository permissionRepository;

    public List<Permiso> findAll() {
        List<Permiso> permisos = getPermissionRepository().findAll();

        if (permisos == null || permisos.isEmpty()) {
            LOGGER.debug("GET Permisos: no encontrados.");
        } else {
            LOGGER.debug("GET Permisos: cantidad encontrada " + permisos.size());
        }
        return permisos;
    }

    public PermissionRepository getPermissionRepository() {
        return permissionRepository;
    }

    public void setPermissionRepository(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public List<Permiso> crearPermisos(String nombre) {
        List<Permiso> permisos = new ArrayList<Permiso>();
        String[] prefijos = new String[] { "READ", "UPDATE", "CREATE", "DELETE", "FIND" };
        for (String prefix : prefijos) {
            permisos.add(crearPermiso(prefix, nombre));
        }
        return permisos;
    }

    public Permiso crearPermiso(String prefix, String nombre) {
        String nombrePermiso = prefix + "_" + nombre;
        System.out.print(nombrePermiso);
        Permiso p = this.permissionRepository.findOneByNombre(nombrePermiso);
        if (p == null) {
            p = new Permiso();
            p.setNombre(nombrePermiso);
            this.permissionRepository.save(p);
        }
        return p;
    }

}
