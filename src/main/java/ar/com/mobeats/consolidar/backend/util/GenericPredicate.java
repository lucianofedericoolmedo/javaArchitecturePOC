package ar.com.mobeats.consolidar.backend.util;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.Predicate;

public class GenericPredicate implements Predicate {

    private Object expected;
    private String propertyName;

    public GenericPredicate(String propertyName, Object expected) {
        super();
        this.propertyName = propertyName;
        this.expected = expected;
    }

    @Override
    public boolean evaluate(Object object) {
        try {
            return expected.equals(PropertyUtils.getProperty(object, propertyName));
        } catch (Exception e) {
            return false;
        }
    }
    
}
