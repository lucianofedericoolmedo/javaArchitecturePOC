package ar.com.mobeats.consolidar.backend.service.security;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import ar.com.mobeats.consolidar.backend.model.User;

@Service
public class TemporaryAuthorizationService {

    private static Map<String, TemporaryAuthorizationEntry> temporaries = new ConcurrentHashMap<String, TemporaryAuthorizationService.TemporaryAuthorizationEntry>();

    public String generate(final User user) {
        String key = UUID.randomUUID().toString();
        temporaries.put(key, new TemporaryAuthorizationEntry(user));
        return key;
    }

    public boolean isAuthorized(String key, String permission) {

        TemporaryAuthorizationEntry entry = temporaries.remove(key);

        if (entry == null || entry.isExpired()) {
            return false;
        }

        User user = entry.getUser();
        return (StringUtils.isBlank(permission)) ? true : user.tieneAlgunPermiso(Arrays.asList(permission));
    }

    private class TemporaryAuthorizationEntry {

        private static final int EXPIRATION_TIME_IN_SECONDS = 60;

        private final User user;

        private final Calendar expiration;

        public TemporaryAuthorizationEntry(final User user) {
            this.user = user;
            this.expiration = calculateExpiration();
        }

        private Calendar calculateExpiration() {
            Calendar now = new GregorianCalendar();
            now.add(Calendar.SECOND, EXPIRATION_TIME_IN_SECONDS);
            return now;
        }

        public User getUser() {
            return user;
        }

        public boolean isExpired() {
            Calendar now = new GregorianCalendar();
            return now.after(expiration);
        }
    }

}
