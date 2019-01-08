package ar.com.mobeats.consolidar.backend.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "tipos_entidades")
@SQLDelete(sql = "UPDATE tipos_entidades SET state = '0' WHERE id = ?")
@Where(clause = "state = '2'")
@JsonIgnoreProperties(ignoreUnknown = true)
@Audited
public class TipoEntidad extends BackEndEntity {

    private static final long serialVersionUID = -7867459016649575086L;
    
    private String codigo;
    private String valor;

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

}
