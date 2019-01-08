package ar.com.mobeats.consolidar.backend.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

public class IgnoreNullSpecifications<T> {

	private Specifications<T> specifications;
	
	private IgnoreNullSpecifications() {
	}
	
	public static <T> IgnoreNullSpecifications<T> where(Specification<T> other) {
		IgnoreNullSpecifications<T> spec = new IgnoreNullSpecifications<T>();
		if (other != null) {
			spec.specifications = Specifications.where(other);
		}
		return spec;
	}
	
	public IgnoreNullSpecifications<T> and(Specification<T> other) {
		if (other != null) {
			specifications = (specifications == null) ? Specifications.where(other) : specifications.and(other); 
		}
		return this;
	}
	
	public Specification<T> getSpecification() {
		return specifications;
	}
	
}