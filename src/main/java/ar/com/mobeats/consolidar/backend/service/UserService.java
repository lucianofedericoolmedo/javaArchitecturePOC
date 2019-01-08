package ar.com.mobeats.consolidar.backend.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.com.mobeats.consolidar.backend.dto.PasswordChangeDTO;
import ar.com.mobeats.consolidar.backend.dto.UserData;
import ar.com.mobeats.consolidar.backend.model.Permiso;
import ar.com.mobeats.consolidar.backend.model.User;
import ar.com.mobeats.consolidar.backend.model.UserDB;
import ar.com.mobeats.consolidar.backend.pagination.JPAPaginationRepository;
import ar.com.mobeats.consolidar.backend.pagination.PageRequest;
import ar.com.mobeats.consolidar.backend.pagination.PageResponse;
import ar.com.mobeats.consolidar.backend.repository.UserRepository;
import ar.com.mobeats.consolidar.backend.repository.specification.UserSpecification;
import ar.com.mobeats.consolidar.backend.service.exception.AuthenticationException;
import ar.com.mobeats.consolidar.backend.service.exception.ChangePasswordException;
import ar.com.mobeats.consolidar.backend.service.exception.NotFoundException;
import ar.com.mobeats.consolidar.backend.service.security.PasswordEncriptionService;
import ar.com.mobeats.consolidar.backend.service.security.TemporaryAuthorizationService;

@Service
public class UserService extends CRUDService<User>{

	private static final Logger LOGGER = Logger.getLogger(UserService.class );
	
	@Autowired
	private UserRepository userRepository;
    
    @Value("${images.dir}")
    private String directoryForImages;
    
    @Autowired
    private PasswordEncriptionService passwordEncriptionService;
    
    @Autowired
    private TemporaryAuthorizationService temporaryAuthService;
    
    public User login(String username, String password) {

        LOGGER.info(MessageFormat.format("--- Login: {0} ---", username));
        
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new AuthenticationException("User does not exists in Trazabilidad DB!");
        }

        if (!authenticate(user, password)) {
            throw new AuthenticationException("Authentication method FAIL!");
        }
        
        return user;
    }
    
    @Transactional
    public User register(User newUser) {
    	
    	if (newUser instanceof UserDB) {
    		setPasswordFields((UserDB) newUser);
    		
    	}
    	
        User createdUser = addUser(newUser);
        return createdUser;
    }
    
    @Transactional
    public void changePassword(String username, PasswordChangeDTO passwordChange) {
    	
    	LOGGER.info(MessageFormat.format("--- Changing Password: {0} ---", username));
    	
    	User user = userRepository.findByUsername(username);    	
    	validateUser(user);
    	
    	if (!authenticate(user, passwordChange.getOldPassword())) {
    		throw new ChangePasswordException("Wrong OLD password");
    	}
    	
    	setNewPassword((UserDB) user, passwordChange);
    	update(user);
    }
    
    @Transactional
    public User update(User user) {
		user = userRepository.save(user);
        return user;
    }
    
    @Transactional
    public User update(User profile, UserData userData) {
		User persistedUser = find(userData.getId());
		
		//solo se actualizan los datos que se exponen en el frontend
		persistedUser.setFirstName(profile.getFirstName());
		//persistedUser.setMiddleName(profile.getMiddleName());
		persistedUser.setLastName(profile.getLastName());

		persistedUser.setOrganizacion(profile.getOrganizacion());
		persistedUser.setEmail(profile.getEmail());
		persistedUser.setTelefono(profile.getTelefono());
		persistedUser.setImagen(profile.getImagen());
		persistedUser.setImpresora(profile.getImpresora());
		
        return update(persistedUser);
    }
    
    @Transactional
    public void delete(String id) {
    	User user = userRepository.findOne(id);
    	userRepository.delete(user);
    }
    
    private void validateUser(final User user) {
    	if (user == null) {
    		throw new ChangePasswordException("User does not exists!");
    	}
    	
    }
    
    private boolean authenticate(User user, String password) {
    	
    	if (user instanceof UserDB) {
    		return authenticate((UserDB) user, password);
    	}
    	return false;
    }
    
    
    private boolean authenticate(UserDB user, String password) {
    	return StringUtils.isNotBlank(password) && passwordEncriptionService.authenticate(
    			password, user.getPassword(), user.getSalt());
    }    
    
	private User addUser(User newUser) {
	    
		newUser = userRepository.save(newUser);
        
        LOGGER.info("User added: " + newUser.getFirstName() + " " + newUser.getLastName());
        return newUser;
	}
    
    private void setPasswordFields(UserDB user) {
    	user.setSalt(passwordEncriptionService.generateSalt());
    	user.setPassword(passwordEncriptionService.getEncryptedPassword(
    			user.getClearTextPassword(), user.getSalt()));
    	user.setClearTextPassword(null);
    }
    
    private void setNewPassword(UserDB user, PasswordChangeDTO passwordChange) {
    	user.setPassword(passwordEncriptionService.getEncryptedPassword(
    			passwordChange.getNewPassword(), user.getSalt()));
    	user.setPasswordTemp(false);
    }
    
    public User find(String id) {
    	
    	// Tuve que cambiar el findOne() comun por el que recibe specification,
    	// porque volvimos a pegarle a ese extraño bug que multiplica elementos en listas;
    	// en este caso: roles con permisos.
    	User user = userRepository.findOne(UserSpecification.idEsIgualQue(id));
    	if (user == null) {
    		String message = "No se encuentra el Usuario con ID: " + id;
    		LOGGER.warn(message);
    		throw new NotFoundException(message);
    	}
    	
    	return user;
    }
    
	public PageResponse<UserData> find(String username, String lastname, String organizacionId, PageRequest pageRequest) {
		
		Page<User> response = userRepository.findAll(
				UserSpecification.andIgnoringBlanks(username, lastname, organizacionId),
				pageRequest.convert());
		
		List<UserData> usersData = UserData.fromUsers(response.getContent());
		return new PageResponse<UserData>(usersData, response.getNumber(),
				response.getSize(), response.getTotalElements());
	}
	
	public Boolean exists(String username) {
		User user = userRepository.findByUsername(username);
		return user != null;
	}
	
	public Boolean userHasPermission(String userId, String permissionName) {
	    Permiso permisoEncontrado = userRepository.userHasPermission(userId, permissionName);
	    return permisoEncontrado != null;
	}
	

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public InputStream findImage(String userId) throws IOException {
		User user = this.find(userId);
		if (user == null)
			throw new NotFoundException();
		
		StringBuffer absolutePath = new StringBuffer();
		absolutePath
			.append(directoryForImages)
			.append("/")
			.append(user.getId());
		
		return new FileInputStream(absolutePath.toString());
	}

	public String generateTemporaryAuthorization(String id) {
        User user = find(id);
        return temporaryAuthService.generate(user);
    }

    @Override
    protected JPAPaginationRepository<User, String> getRepository() {
        return userRepository;
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected Class<User> getModelClass() {
        return User.class;
    }

    public User updateImpresora(UserData userData) {
        User usuario = this.get(userData.getId());
        usuario.setImpresora(userData.getImpresora());
        return this.update(usuario, userData);
    }

}
