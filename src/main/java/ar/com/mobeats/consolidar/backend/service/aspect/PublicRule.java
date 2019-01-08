package ar.com.mobeats.consolidar.backend.service.aspect;

import java.io.Serializable;
import java.util.List;

public class PublicRule implements Serializable {

    private static final long serialVersionUID = -3189053287483766795L;
    
    private String httpMethod;
    
    private List<String> endpoints;

    public String getHttpMethod() {
        return httpMethod;
    }
    
    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }
    
    public List<String> getEndpoints() {
        return endpoints;
    }
    
    public void setEndpoints(List<String> endpoints) {
        this.endpoints = endpoints;
    }

}
