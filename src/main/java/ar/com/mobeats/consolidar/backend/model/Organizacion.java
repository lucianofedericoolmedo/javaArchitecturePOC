package ar.com.mobeats.consolidar.backend.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "organizaciones")
@SQLDelete(sql = "UPDATE organizaciones SET state = '0' WHERE id = ?")
@Where(clause="state = '2'")
@JsonIgnoreProperties(ignoreUnknown = true)
@Audited
public class Organizacion extends BackEndEntity {

	private static final long serialVersionUID = 2177144201437123921L;
	
	public Organizacion() {
	}

	public Organizacion(Long id){
		super.setId(id);
	}
	
	@NotEmpty
	private String nombre;

	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
}
