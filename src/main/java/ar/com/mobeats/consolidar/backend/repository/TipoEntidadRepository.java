package ar.com.mobeats.consolidar.backend.repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import ar.com.mobeats.consolidar.backend.model.TipoEntidad;
import ar.com.mobeats.consolidar.backend.pagination.JPAPaginationRepository;

public interface TipoEntidadRepository extends JPAPaginationRepository<TipoEntidad, String>, JpaSpecificationExecutor<TipoEntidad> {

}
