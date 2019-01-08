package ar.com.mobeats.consolidar.backend.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import ar.com.mobeats.consolidar.backend.model.Rol;

public class RolSpecification {

	public static Specification<Rol> andIgnoringBlanks(String nombre) {
		return new CommonSpecification<Rol>()
				.attrEsParecidoQueSiNoEsBlanco("nombre", nombre)
				.getSpecification();
	}
}
