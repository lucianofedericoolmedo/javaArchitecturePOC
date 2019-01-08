package ar.com.mobeats.consolidar.backend.service.security;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ar.com.mobeats.consolidar.backend.service.security.PasswordEncriptionService;

public class PasswordEncriptionServiceTest {

    private PasswordEncriptionService passwordEncriptionService;
    
    private static final String VALID_PASSWORD = "aPassword";
    private static final String INVALID_PASSWORD = "invalidPassword";
    private static final String NEW_PASSWORD = "newPassword";
    
    private byte[] salt;
    private byte[] encryptedPassword;
    
    @Before
    public void init() {
        passwordEncriptionService = new PasswordEncriptionService();
        
        salt = passwordEncriptionService.generateSalt();
        Assert.assertNotNull(salt);
        Assert.assertEquals(8, salt.length);
        
        encryptedPassword = passwordEncriptionService.getEncryptedPassword(
                VALID_PASSWORD, salt);
        Assert.assertNotNull(encryptedPassword);
    }
    
    @Test
    public void authenticateAValidUser() {
        boolean valid = passwordEncriptionService.authenticate(
                VALID_PASSWORD, encryptedPassword, salt);
        Assert.assertTrue(valid);
    }
    
    @Test
    public void authenticateAnInvalidUser() {
        boolean valid = passwordEncriptionService.authenticate(
                INVALID_PASSWORD, encryptedPassword, salt);
        Assert.assertFalse(valid);
    }
    
    @Test
    public void changePassword() {
        
        byte[] newEncryptedPassword = passwordEncriptionService.getEncryptedPassword(
                NEW_PASSWORD, salt);
        Assert.assertNotNull(newEncryptedPassword);
        
        Assert.assertTrue(passwordEncriptionService.authenticate(
                NEW_PASSWORD, newEncryptedPassword, salt));
        
        Assert.assertFalse(passwordEncriptionService.authenticate(
                VALID_PASSWORD, newEncryptedPassword, salt));
    }
}
