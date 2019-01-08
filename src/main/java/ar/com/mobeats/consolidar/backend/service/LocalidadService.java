package ar.com.mobeats.consolidar.backend.service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.com.mobeats.consolidar.backend.dto.LocalidadDTO;
import ar.com.mobeats.consolidar.backend.model.Localidad;
import ar.com.mobeats.consolidar.backend.pagination.JPAPaginationRepository;
import ar.com.mobeats.consolidar.backend.pagination.PageRequest;
import ar.com.mobeats.consolidar.backend.pagination.PageResponse;
import ar.com.mobeats.consolidar.backend.repository.LocalidadRepository;
import ar.com.mobeats.consolidar.backend.repository.specification.CommonSpecification;
import ar.com.mobeats.consolidar.backend.service.annotation.LogMethodCall;

@Service
public class LocalidadService extends CRUDService<Localidad>{
	private static final Logger LOGGER = Logger.getLogger(LocalidadService.class);

    @Autowired
    private LocalidadRepository localidadRepository;
    
    
    @Transactional
    public Localidad createLocalidadFromDTO(LocalidadDTO dto) {
    	Localidad localidad = new Localidad();
    	localidad.setCodigo(dto.codigo);
    	localidad.setValor(dto.valor);
    	return this.createLocalidad(localidad);
    }


    @Transactional
    public Localidad createLocalidad(Localidad localidad) {
        localidad = localidadRepository.save(localidad);

        LOGGER.info("Localidad added: " + localidad.toString());
        return localidad;
    }

    @Transactional
    public Localidad updateLocalidad(Localidad localidad) {
    	localidad = localidadRepository.save(localidad);
        
        LOGGER.info("Localidad updated: " + localidad.toString());
        return localidad;
    }

    @Transactional
    public void deleteLocalidad(Localidad localidad) {
        localidadRepository.delete(localidad);
        LOGGER.info("Localidad deleted: " + localidad.toString());
    }
    
    public Localidad getLocalidad(String id) {
        Localidad localidad = localidadRepository.findOne(id);
        if (localidad==null){
            LOGGER.info("No Localidad found id " + id);
        } else {
            LOGGER.info("Localidad found id " + id + " : " + localidad.toString());
        }
        return localidad;
    }

    public PageResponse<Localidad> find(String codigo, PageRequest pageRequest) {
        Specification<Localidad> sp = new CommonSpecification<Localidad>()
                .attrEsParecidoQueSiNoEsBlanco("codigo", codigo)
                .getSpecification();

        Page<Localidad> response = localidadRepository.findAll(sp,
                pageRequest.convert());

        if (!response.hasContent()) {
            LOGGER.info("Atributos not found");
        } else {
            LOGGER.info("Atributos found count " + response.getSize());
        }

        return new PageResponse<Localidad>(response.getContent(),
                response.getNumber(), response.getSize(),
                response.getTotalElements());
    }
    
    @LogMethodCall
    public PageResponse<Localidad> createOneForTestingPurpose() {
        LocalDateTime localDateTime = LocalDateTime.of(2018, 1, 1, 14, 0);
        List<Localidad> localidades = new LinkedList<Localidad>();
        
        for (int i = 0; i < 20; i++) {
            Localidad localidad = new Localidad();
            localidad.setTestingDateTime(localDateTime);
            localidad.setFirstName("FirstName_" + i);
            localidad.setLastName("LastName_" + i);
            localidad.setUsername("Username_" + i);
            localidad.setEmail("email@email_" + i);
            localidad.setAge(i);
            localidades.add(localidad);
        }
        localidades = localidadRepository.save(localidades);
        
        return new PageResponse<Localidad>(localidades,
                1, 10, (long)
                localidades.size());
    }

    @Override
    protected JPAPaginationRepository<Localidad, String> getRepository() {
        return this.localidadRepository;
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected Class<Localidad> getModelClass() {
        return Localidad.class;
    }

}
