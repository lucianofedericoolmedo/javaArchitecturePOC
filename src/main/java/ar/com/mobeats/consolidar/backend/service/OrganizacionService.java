package ar.com.mobeats.consolidar.backend.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.mobeats.consolidar.backend.model.Organizacion;
import ar.com.mobeats.consolidar.backend.repository.OrganizacionRepository;

@Service
public class OrganizacionService {

    private static final Logger LOGGER = Logger.getLogger(OrganizacionService.class);

    @Autowired
    private OrganizacionRepository organizacionRepository;

	public List<Organizacion> findAll() {
		List<Organizacion> organizaciones = organizacionRepository.findAll();
		
		if (organizaciones == null || organizaciones.isEmpty()) {
			LOGGER.warn("Organizaciones no encontradas");
		} else {
			LOGGER.debug("Organizaciones encontradas: " + organizaciones.size());
		}
		return organizaciones;
	}
}
