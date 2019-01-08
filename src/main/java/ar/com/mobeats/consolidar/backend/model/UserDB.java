package ar.com.mobeats.consolidar.backend.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@DiscriminatorValue(value = "DB")
@SQLDelete(sql="UPDATE users SET state = '0' WHERE id = ?")
@Where(clause="state = '2'")
public class UserDB extends User {

	private static final long serialVersionUID = -918799056836655397L;
	
	@JsonIgnore
    private byte[] password;
	
	@JsonIgnore
    private byte[] salt;
	
    private boolean passwordTemp;
    
    @Transient
    private String clearTextPassword;

    public byte[] getPassword() {
		return password;
	}
    
    public void setPassword(byte[] password) {
		this.password = password;
	}
    
    public byte[] getSalt() {
		return salt;
	}
    
    public void setSalt(byte[] salt) {
		this.salt = salt;
	}
    
    public boolean isPasswordTemp() {
		return passwordTemp;
	}
    
    public void setPasswordTemp(boolean passwordTemp) {
		this.passwordTemp = passwordTemp;
	}
    
    public String getClearTextPassword() {
		return clearTextPassword;
	}
    
    public void setClearTextPassword(String clearTextPassword) {
		this.clearTextPassword = clearTextPassword;
	}
}
