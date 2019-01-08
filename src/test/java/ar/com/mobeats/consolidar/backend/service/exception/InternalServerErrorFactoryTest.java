package ar.com.mobeats.consolidar.backend.service.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ar.com.mobeats.consolidar.backend.service.exception.InternalServerError;
import ar.com.mobeats.consolidar.backend.service.exception.InternalServerErrorFactory;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/spring/commons.xml", "/spring/errors.xml"})
@ActiveProfiles("localhost")
public class InternalServerErrorFactoryTest {

    @Autowired
    private InternalServerErrorFactory errorFactory;
    
    @Test
    public void genericException() {
        String message = "Hubo un error inesperado";
        try {
            throw new RuntimeException(message);
        } catch (Exception e) {
            InternalServerError internalError = errorFactory.buildInternalError(e);
            Assert.assertEquals(new Long(1L), internalError.getCode());
            Assert.assertEquals(message, internalError.getMessage());
        }
    }
    
    @Test
    public void dataIntegrityViolationException() {
        String message = "Hubo una violación de integridad de datos";
        try {
            throw new DataIntegrityViolationException(message);
        } catch (Exception e) {
            InternalServerError internalError = errorFactory.buildInternalError(e);
            Assert.assertEquals(new Long(2L), internalError.getCode());
            Assert.assertEquals(message, internalError.getMessage());
        }
    }
}
