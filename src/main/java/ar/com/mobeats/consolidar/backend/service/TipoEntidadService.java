package ar.com.mobeats.consolidar.backend.service;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import ar.com.mobeats.consolidar.backend.model.TipoEntidad;
import ar.com.mobeats.consolidar.backend.pagination.JPAPaginationRepository;
import ar.com.mobeats.consolidar.backend.pagination.PageRequest;
import ar.com.mobeats.consolidar.backend.pagination.PageResponse;
import ar.com.mobeats.consolidar.backend.repository.TipoEntidadRepository;
import ar.com.mobeats.consolidar.backend.repository.specification.CommonSpecification;

@Service
public class TipoEntidadService extends CRUDService<TipoEntidad>
{
	private static final Logger LOGGER = Logger.getLogger(TipoEntidadService.class);

    @Autowired
    private TipoEntidadRepository tipoEntidadRepository;
	
    @Override
    protected JPAPaginationRepository<TipoEntidad, String> getRepository() {
        return this.tipoEntidadRepository;
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected Class<TipoEntidad> getModelClass() {
        return TipoEntidad.class;
    }	

	public PageResponse<TipoEntidad> find(String codigo, String valor, PageRequest pageRequest) {
		Specification<TipoEntidad> sp = new CommonSpecification<TipoEntidad>()
				.attrEsParecidoQueSiNoEsBlanco("codigo", codigo)
				.attrEsParecidoQueSiNoEsBlanco("valor", valor)
				.getSpecification();

		Page<TipoEntidad> response = tipoEntidadRepository.findAll(sp,
				pageRequest.convert());

		if (!response.hasContent()) {
			LOGGER.info("TiposEntidades not found");
		} else {
			LOGGER.info("TiposEntidades found count " + response.getSize());
		}

		return new PageResponse<TipoEntidad>(response.getContent(),
				response.getNumber(), response.getSize(),
				response.getTotalElements());
	}

}

