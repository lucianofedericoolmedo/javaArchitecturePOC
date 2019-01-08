package ar.com.mobeats.consolidar.backend.model.builder;

import org.junit.Assert;

import ar.com.mobeats.consolidar.backend.dto.UserData;
import ar.com.mobeats.consolidar.backend.model.User;
import ar.com.mobeats.consolidar.backend.model.UserDB;

public class UserMockBuilder {

    public static final User createAdrianParedes(){
        User user = new UserDB();
        user.setId(1L);
        user.setEmail("adrianparedes82@gmail.com");
        user.setFirstName("Adrian");
        user.setMiddleName("Marcelo");
        user.setLastName("Paredes");
        user.setUsername("elfrasco");
        user.setLanguage("es");
        return user;
    }
    
    public static void assertUser(UserData expected, UserData actual) {
        Assert.assertNotNull(expected);
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getEmail(), actual.getEmail());
        Assert.assertEquals(expected.getFirstName(), actual.getFirstName());
        Assert.assertEquals(expected.getMiddleName(), actual.getMiddleName());
        Assert.assertEquals(expected.getLastName(), actual.getLastName());
        Assert.assertEquals(expected.getUsername(), actual.getUsername());
    }
}
