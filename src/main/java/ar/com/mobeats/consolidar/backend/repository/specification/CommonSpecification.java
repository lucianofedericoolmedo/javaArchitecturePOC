package ar.com.mobeats.consolidar.backend.repository.specification;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

/**
 * Objeto helper para construir especificaciones de forma más simple
 * @author sebax
 *
 * @param <T> El tipo de entidad
 */
public class CommonSpecification<T> {

	private Specifications<T> specifications;

	public CommonSpecification<T> and(Specification<T> other) {
		if (other != null) {
			specifications = (specifications == null) ? Specifications
					.where(other) : specifications.and(other);
		}
		return this;
	}

	public Specification<T> getSpecification() {
		return specifications;
	}
	
	public CommonSpecification<T> attrEsIgualQueSiNoEsNull(final String attr, final Object value) {
		if (value != null) {
			this.attrEsIgualQue(attr, value);
		}
		return this;
	}

	public CommonSpecification<T> attrEsIgualQueSiNoEsBlanco(final String attr, final String value) {
		if (!StringUtils.isBlank(value)) {
			this.attrEsIgualQue(attr, value);
		}
		return this;
	}

	public CommonSpecification<T> attrEsIgualQueSiNoEsBlanco(final String attr, final Long value) {
		if (value != null) {
			this.attrEsIgualQue(attr, value);
		}
		return this;
	}

	public CommonSpecification<T> tipoMaterialIdEsIgualQueSiNoEsBlanco(final String tipoMaterialId) {
		if (!StringUtils.isBlank(tipoMaterialId)) {
			this.and(new Specification<T>() {

				@Override
				public Predicate toPredicate(Root<T> root,
						CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.equal(root.get("tipoMaterial").get("id"),
							tipoMaterialId);
				}
			});
		}

		return this;
	}
	
	public CommonSpecification<T> clienteIdEsIgualQueSiNoEsBlanco(final String clienteId) {
		if (!StringUtils.isBlank(clienteId)) {
			this.and(new Specification<T>() {

				@Override
				public Predicate toPredicate(Root<T> root,
						CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.equal(root.get("cliente").get("id"),
							clienteId);
				}
			});
		}

		return this;
	}

	public CommonSpecification<T> codigoMaterialEsIgualQueSiNoEsBlanco(
			final String codigoMaterial) {
		if (!StringUtils.isBlank(codigoMaterial)) {
			this.and(new Specification<T>() {
				@Override
				public Predicate toPredicate(Root<T> root,
						CriteriaQuery<?> query, CriteriaBuilder cb) {
					return cb.equal(root.get("tipoMaterial").get("codigo"),
							codigoMaterial);
				}
			});
		}
		return this;
	}

	public CommonSpecification<T> attrEsIgualQue(final String attr,
			final Object value) {
		this.and(new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				return cb.equal(root.get(attr), value);
			}
		});
		return this;
	}

	public CommonSpecification<T> attrEsParecidoQueSiNoEsBlanco(
			final String attr, final String value) {
		if (!StringUtils.isBlank(value)) {
			this.attrEsParecidoQue(attr, value);
		}
		return this;
	}

	public CommonSpecification<T> attrEsParecidoQue(final String attr,
			final String value) {
		this.and(new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				StringBuilder criteria = new StringBuilder(value.length() + 2);
				criteria.append("%").append(value.trim().toLowerCase())
						.append("%");

				return cb.like(cb.lower(root.<String> get(attr)),
						criteria.toString());
			}
		});
		return this;
	}

	public CommonSpecification<T> fechaEsMenorIgualQueSiNoEsBlanco(
			final String attr, final Date fecha) {
		if (fecha != null) {
			this.and(new Specification<T>() {
				@Override
				public Predicate toPredicate(Root<T> root,
						CriteriaQuery<?> query, CriteriaBuilder cb) {
					Path<Date> path = root.get(attr);
					return cb.lessThanOrEqualTo(path, fecha);
				}
			});
		}
		return this;
	}

	public CommonSpecification<T> fechaEsMayorIgualQueSiNoEsBlanco(
			final String attr, final Date fecha) {
		if (fecha != null) {
			this.and(new Specification<T>() {
				@Override
				public Predicate toPredicate(Root<T> root,
						CriteriaQuery<?> query, CriteriaBuilder cb) {
					Path<Date> path = root.get(attr);
					return cb.greaterThanOrEqualTo(path, fecha);
				}
			});
		}
		return this;
	}

}
