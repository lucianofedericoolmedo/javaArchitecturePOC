package ar.com.mobeats.consolidar.backend.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonProperty;

@MappedSuperclass
public abstract class BackEndEntity extends AuditableObject {

    private static final long serialVersionUID = -5354058460706112830L;

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @JsonProperty("_id")
    private Long id;
    
    // Default value
    private Byte orden = 0;
    

    public Byte getOrden() {
		return orden;
	}

	public void setOrden(Byte orden) {		
		this.orden = orden;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
}
