package ar.com.mobeats.consolidar.backend.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import ar.com.mobeats.consolidar.backend.model.Organizacion;
import ar.com.mobeats.consolidar.backend.model.User;

public class UserSpecification {

	public static Specification<User> andIgnoringBlanks(final String username, final String lastName,
			final String organizacion) {
		
		return new SpecificationBuilder<User>()
				.attrEsParecidoQueSiNoEsBlanco("username", username)
				.attrEsParecidoQueSiNoEsBlanco("lastName", lastName)
				.attrOfObjectEsIgualQueYNoEsNull("organizacion.id", organizacion)
				//.and(organizacionEsIgualQueSiNoEsBlanco(organizacion))
				.getSpecification();
	}
	
	public static Specification<User> idEsIgualQue(final String id) {
		return new CommonSpecification<User>().attrEsIgualQue("id", id).getSpecification();
	}
	
	private static Specification<User> organizacionEsIgualQueSiNoEsBlanco(final Organizacion organizacion) {
		
		if (organizacion == null) {
			return null;
		}
		
		return new Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get("organizacion"), organizacion);
			}
		};		
	}
}
