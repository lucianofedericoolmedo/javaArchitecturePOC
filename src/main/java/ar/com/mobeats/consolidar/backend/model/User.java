package ar.com.mobeats.consolidar.backend.model;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import ar.com.mobeats.consolidar.backend.util.GenericPredicate;
import ar.com.mobeats.consolidar.backend.util.JSONDateDeserialize;
import ar.com.mobeats.consolidar.backend.util.JSONDateSerialize;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo", discriminatorType = DiscriminatorType.STRING, length = 5)
@SQLDelete(sql="UPDATE users SET state = '0' WHERE id = ?")
@Where(clause="state = '2'")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
	    use = JsonTypeInfo.Id.NAME,  
	    include = JsonTypeInfo.As.PROPERTY,  
	    property = "tipo")  
@JsonSubTypes({  
	@Type(value = UserDB.class, name = "DB"),  
})
public abstract class User extends BackEndEntity {

    private static final long serialVersionUID = 1294418189803999949L;

    // Instance Variables
    @NotNull
    @Column(unique = true)
	private String username;
        
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    
    private String language;
    private String token;
    
    private String impresora;
    
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_user_organizacion"), name = "organizacion_id")
    private Organizacion organizacion;
	
	@Temporal(javax.persistence.TemporalType.DATE)
    @JsonDeserialize(using = JSONDateDeserialize.class)
    @JsonSerialize(using = JSONDateSerialize.class)    
    private Date fechaNacimiento;
    
    @Enumerated(EnumType.ORDINAL)
    private Genero genero;

    private String telefono;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Rol> roles;

    private String imagen;

    // Logic
    public boolean tieneAlgunPermiso(Collection<String> permisos) {
        if (CollectionUtils.isNotEmpty(roles)) {
            for (Rol rol : roles) {
                for (String permiso : permisos) {
                    Predicate predicate = new GenericPredicate("nombre", permiso);
                    if (CollectionUtils.find(rol.getPermisos(), predicate) != null) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
	// Getters and Setters
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLanguage() {
		return language;
	}
	
	public void setLanguage(String language) {
		this.language = language;
	}
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}

    public Set<Rol> getRoles() {
        return roles;
    }

    public void setRoles(Set<Rol> roles) {
        this.roles = roles;
    }
    
    public Organizacion getOrganizacion() {
		return organizacion;
	}

	public void setOrganizacion(Organizacion organizacion) {
		this.organizacion = organizacion;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public Genero getGenero() {
		return genero;
	}

	public void setGenero(Genero genero) {
		this.genero = genero;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

    
    public String getImpresora() {
        return impresora;
    }

    public void setImpresora(String impresora) {
        this.impresora = impresora;
    }

}
