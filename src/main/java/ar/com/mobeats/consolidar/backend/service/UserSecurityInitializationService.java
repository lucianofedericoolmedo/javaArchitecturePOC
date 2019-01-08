package ar.com.mobeats.consolidar.backend.service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.com.mobeats.consolidar.backend.model.Genero;
import ar.com.mobeats.consolidar.backend.model.Organizacion;
import ar.com.mobeats.consolidar.backend.model.Permiso;
import ar.com.mobeats.consolidar.backend.model.Rol;
import ar.com.mobeats.consolidar.backend.model.User;
import ar.com.mobeats.consolidar.backend.model.UserDB;
import ar.com.mobeats.consolidar.backend.repository.OrganizacionRepository;
import ar.com.mobeats.consolidar.backend.repository.UserRepository;
import ar.com.mobeats.consolidar.backend.service.security.PasswordEncriptionService;
import ar.com.mobeats.consolidar.backend.service.security.TemporaryAuthorizationService;

@Service
public class UserSecurityInitializationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganizacionRepository organizacionRepository;

    @Autowired
    private PasswordEncriptionService passwordEncriptionService;

    @Autowired
    private TemporaryAuthorizationService temporaryAuthService;

    @Autowired
    private RoleService roleService;
    
    @Autowired
    private PermissionService permissionService;

    /**
     * En este método se deben inicializar todas las entidades básicas y
     * necesarias para comenzar.
     */
    @PostConstruct
    @Transactional
    public void initialize() {
        Organizacion organizacion = this.organizacionRepository.findOneByNombre("Mobeats S.A.");
        if (organizacion == null) {
            organizacion = new Organizacion();
            organizacion.setNombre("Mobeats S.A.");
            this.organizacionRepository.save(organizacion);
        }

        Set<Permiso> permisos_basicos = new HashSet<Permiso>();
        permisos_basicos.addAll(this.permissionService.crearPermisos("USER"));
        permisos_basicos.addAll(this.permissionService.crearPermisos("CLIENTE"));
        permisos_basicos.addAll(this.permissionService.crearPermisos("ROL"));
        permisos_basicos.addAll(this.permissionService.crearPermisos("PERMISOS"));
        permisos_basicos.addAll(this.permissionService.crearPermisos("ORGANIZACIONES"));
        permisos_basicos.add(this.permissionService.crearPermiso("RESET", "PASSWORD"));
        permisos_basicos.add(this.permissionService.crearPermiso("READ", "USERNAME_EXISTS"));

       
        
        Rol rol_admin = this.roleService.crearRol("administrador", permisos_basicos);

        Set<Rol> roles_admin = new HashSet<Rol>();
        roles_admin.add(rol_admin);
        //roles_admin.add(this.roleService.crearRol("Cliente", permisos_basicos));
        
        User user = this.userRepository.findOneByUsername("admin");
        if (user == null) {
            UserDB u = new UserDB();
            u.setUsername("admin");
            u.setEmail("admin@mobeats.com.ar");
            u.setFechaActualizacion(new Date());
            u.setFechaCreacion(new Date());
            u.setFirstName("administrador");
            u.setLastName("administrador");
            u.setFechaNacimiento(new Date());
            u.setGenero(Genero.MASCULINO);
            u.setClearTextPassword("123456");
            u.setOrganizacion(organizacion);
            u.setSalt(passwordEncriptionService.generateSalt());
            u.setPassword(passwordEncriptionService.getEncryptedPassword(u.getClearTextPassword(), u.getSalt()));
            u.setClearTextPassword(null);
            u.setPasswordTemp(false);
            u.setRoles(roles_admin);
            this.userRepository.save(u);
        }
        
//        if(configGeneralRepository.findAll().isEmpty()) {
//            configGeneralRepository.save(new ConfigGeneral());
//        }
    }
}
