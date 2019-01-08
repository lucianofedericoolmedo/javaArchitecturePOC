package ar.com.mobeats.consolidar.backend.service.exception;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class InternalServerErrorFactory {

    private Map<String, InternalServerError> exceptionMapping;
    
    private static final String UNKNOWN_ERROR_CODE = "Unknown";
    
    public InternalServerError buildInternalError(Exception e) {
        String className = e.getClass().getName();
        InternalServerError error = exceptionMapping.get(className);
        if (error == null) {
        	error = exceptionMapping.get(UNKNOWN_ERROR_CODE);
        }
        if (StringUtils.isBlank(error.getMessage())) {
        	error = new InternalServerError(error.getCode(), error.getMessage());
        	error.setMessage(e.getMessage());
        }
        return error;
    }
    
    public Map<String, InternalServerError> getExceptionMapping() {
		return exceptionMapping;
	}
    
    public void setExceptionMapping(Map<String, InternalServerError> exceptionMapping) {
		this.exceptionMapping = exceptionMapping;
	}
    
}
