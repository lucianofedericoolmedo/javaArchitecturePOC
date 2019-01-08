package ar.com.mobeats.consolidar.backend.model;

import java.util.Date;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import ar.com.mobeats.consolidar.backend.service.security.UserThreadLocal;
import ar.com.mobeats.consolidar.backend.util.JSONDateTimeDeserialize;
import ar.com.mobeats.consolidar.backend.util.JSONDateTimeSerialize;

@MappedSuperclass
@Audited
public class AuditableObject extends BackEndObject {

	private static final long serialVersionUID = 5709907755723334868L;
	
    private EntityState state = EntityState.ACTIVE;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @JsonDeserialize(using = JSONDateTimeDeserialize.class)
    @JsonSerialize(using = JSONDateTimeSerialize.class)
    private Date fechaCreacion;
    
    private String usuarioCreacionId;
    
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @JsonDeserialize(using = JSONDateTimeDeserialize.class)
    @JsonSerialize(using = JSONDateTimeSerialize.class)
    private Date fechaActualizacion;
    
    private String usuarioActualizoId;

    public EntityState getState() {
        return state;
    }

    public void setState(EntityState state) {
        this.state = state;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioCreacionId() {
		return usuarioCreacionId;
	}
    
    public void setUsuarioCreacionId(String usuarioCreacionId) {
		this.usuarioCreacionId = usuarioCreacionId;
	}

    public Date getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(Date fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public String getUsuarioActualizoId() {
		return usuarioActualizoId;
	}
    
    public void setUsuarioActualizoId(String usuarioActualizoId) {
		this.usuarioActualizoId = usuarioActualizoId;
	}
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = fechaCreacion == null ? new Date() : fechaCreacion;
        fechaActualizacion = fechaActualizacion == null ? fechaCreacion : fechaActualizacion;
        usuarioCreacionId = usuarioCreacionId == null ? UserThreadLocal.get() : usuarioCreacionId;
        usuarioActualizoId = usuarioCreacionId;
    }

    @PreUpdate
    protected void onUpdate() {
        usuarioActualizoId = UserThreadLocal.get();
        fechaActualizacion = new Date();
    }
    
}
