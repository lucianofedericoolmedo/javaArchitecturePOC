package ar.com.mobeats.consolidar.backend.service.aspect;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ar.com.mobeats.consolidar.backend.dto.UserData;
import ar.com.mobeats.consolidar.backend.util.JSONObjectConverter;

public class RequestInfo implements Serializable {

    private static final long serialVersionUID = 2458502855765163303L;
    
    private String id;

    private UserData user;
    
    private Date start;
    private Date end;
    private Long duration;
    
    private String ipAddress;
    private String httpMethod;
    private String endpoint;
    
    private String serviceClass;
    private String serviceMethod;    
    private List<Object> parameters;
    
    private Integer responseStatus;
    private Object responseEntity;
    
    private Map<String, String> header;
    
    public RequestInfo() {
        this.id = UUID.randomUUID().toString();
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public UserData getUser() {
        return user;
    }
    
    public void setUser(UserData user) {
        this.user = user;
    }
    
    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getEndpoint() {
        return endpoint;
    }
    
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(String serviceClass) {
        this.serviceClass = serviceClass;
    }

    public String getServiceMethod() {
        return serviceMethod;
    }

    public void setServiceMethod(String serviceMethod) {
        this.serviceMethod = serviceMethod;
    }
    
    public void setParameters(List<Object> parameters) {
        this.parameters = parameters;
    }
    
    public List<Object> getParameters() {
        return parameters;
    }

    public Object getResponseEntity() {
        return responseEntity;
    }
    
    public void setResponseEntity(Object responseEntity) {
        this.responseEntity = responseEntity;
    }
    
    public Integer getResponseStatus() {
        return responseStatus;
    }
    
    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }
    
    public Map<String, String> getHeader() {
        return header;
    }
    
    public void setHeader(Map<String, String> header) {
        this.header = header;
    }
    
    public String toJSON() {
        return JSONObjectConverter.convertToJSON(this);
    }
    
}
