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

public class SpecificationBuilder<T> {
	
	private Specifications<T> specifications;

	public SpecificationBuilder<T> and(Specification<T> other) {
		if (other != null) {
			specifications = (specifications == null) ? Specifications
					.where(other) : specifications.and(other);
		}
		return this;
	}
	
	public SpecificationBuilder<T> or(Specification<T> other) {
        if (other != null) {
            specifications = (specifications == null) ? Specifications
                    .where(other) : specifications.or(other);
        }
        return this;
    }
	
	public SpecificationBuilder<T> andSiNoEsNull(final Object attr, Specification<T> other) {
	    if(attr != null) {
	        return this.and(other);
	    }
	    return this;
	}

	public Specification<T> getSpecification() {
		return specifications;
	}
	
	public SpecificationBuilder<T> attrEsIgualQueSiNoEsNull(final String attr, final Object value) {
		if (value != null) {
			this.attrEsIgualQue(attr, value);
		}
		return this;
	}

	public SpecificationBuilder<T> attrEsIgualQueSiNoEsBlanco(final String attr, final String value) {
		if (!StringUtils.isBlank(value)) {
			this.attrEsIgualQue(attr, value);
		}
		return this;
	}

	public SpecificationBuilder<T> attrEsIgualQueSiNoEsBlanco(final String attr, final Long value) {
		if (value != null) {
			this.attrEsIgualQue(attr, value);
		}
		return this;
	}

	public SpecificationBuilder<T> attrEsIgualQue(final String attr,
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

	public SpecificationBuilder<T> attrEsParecidoQueSiNoEsBlanco(
			final String attr, final String value) {
		if (value != null && !StringUtils.isBlank(value)) {
			this.attrEsParecidoQue(attr, value);
		}
		return this;
	}

	public SpecificationBuilder<T> attrEsParecidoQue(final String attr,
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

	public SpecificationBuilder<T> fechaEsMenorIgualQueSiNoEsBlanco(
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

	public SpecificationBuilder<T> fechaEsMayorIgualQueSiNoEsBlanco(
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
	
	public SpecificationBuilder<T> attrOfObjectEsIgualQueYNoEsNull(final String pathAttr, final Object value){
	    if(value != null){
	        return this.attrOfObjectEsIgualQue(pathAttr, value);
	    }
	    else {
	        return this;
	    }
	}
	
    public SpecificationBuilder<T> orAttrOfObjectEsIgualQueYNoEsNull(final String pathAttr, final Object value){
        if(value != null){
            return this.orAttrOfObjectEsIgualQue(pathAttr, value);
        }
        else {
            return this;
        }
    }

	  public SpecificationBuilder<T> attrOfObjectEsIgualQue(final String pathAttr, final Object value) {
	        this.and(attrOfObjectEsIgualQueSpecification(pathAttr, value));
	        return this;
	  }
	  
	  public SpecificationBuilder<T> orAttrOfObjectEsIgualQue(final String pathAttr, final Object value) {
	      this.or(attrOfObjectEsIgualQueSpecification(pathAttr, value));
	      return this;
	  }
	  
	  private Specification<T> attrOfObjectEsIgualQueSpecification(final String pathAttr, final Object value) {
	      return new Specification<T>() {

              @Override
              public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                  String[] splitPathAttr = pathAttr.split("\\.");
                  Path<T> pathObject = root.get(splitPathAttr[0]);
                  for (int i = 1; i < splitPathAttr.length; i++ ) {
                      pathObject = pathObject.get(splitPathAttr[i]);
                  }
                  return cb.equal(pathObject, value);
              }
          };
	  }
	  
	    public SpecificationBuilder<T> attrEsNull(final String attr) {
	        this.and(new Specification<T>() {
	            
	            @Override
	            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
	                    CriteriaBuilder cb) {
	                return cb.isNull(root.get(attr));
	            }
	        });
	        return this;
	    }
	    
        public SpecificationBuilder<T> attrNoEsNull(final String attr) {
            this.and(new Specification<T>() {
                
                @Override
                public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
                        CriteriaBuilder cb) {
                    return cb.isNotNull(root.get(attr));
                }
            });
            return this;
        }

    public SpecificationBuilder<T> attrOfObjectEsPercido(final String pathAttr, final String value) {
        this.and(new Specification<T>() {

            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                String[] splitPathAttr = pathAttr.split("\\.");
                Path<T> pathObject = root.get(splitPathAttr[0]);
                for (int i = 1; i < splitPathAttr.length - 1; i++ ) {
                    pathObject = pathObject.get(splitPathAttr[i]);
                }
                Path<String> finalPathObject = pathObject.<String> get(splitPathAttr[splitPathAttr.length-1]);
                StringBuilder criteria = new StringBuilder(value.length() + 2);
                criteria.append("%").append(value.trim().toLowerCase())
                        .append("%");

                return cb.like(cb.lower(finalPathObject),
                        criteria.toString());
            }
        });
        return this;
    }

    public SpecificationBuilder<T> attrOfObjectEsPercidoYNoEsNull(final String pathAttr, final String value) {
        if (value != null && !StringUtils.isBlank(value)) {
            this.attrOfObjectEsPercido(pathAttr, value);
        }
        return this;
    }
    
    
}
