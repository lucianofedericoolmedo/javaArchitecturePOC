package ar.com.mobeats.consolidar.backend.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@Entity
@Table(name = "localidades")
@SQLDelete(sql = "UPDATE localidades SET state = '0' WHERE id = ?")
@Where(clause="state = '2'")
@JsonIgnoreProperties(ignoreUnknown = true)
@Audited
public class Localidad extends BackEndEntity{
	private static final long serialVersionUID = -4825907868168211278L;
	
	// Instance Variables
	private String codigo;
	private String valor;
	
	private String firstName;
	private String lastName;
	private String username;
	private String email;
	private Integer age;
	
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime testingDateTime;
	
	// Constructors
    public Localidad() {

    }
    
    public Localidad(String codigo, String valor) {
        this.codigo = codigo;
        this.valor = valor;
    }
	
	// Accessors
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}

    public LocalDateTime getTestingDateTime() {
        return testingDateTime;
    }

    public void setTestingDateTime(LocalDateTime testingDateTime) {
        this.testingDateTime = testingDateTime;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

}
