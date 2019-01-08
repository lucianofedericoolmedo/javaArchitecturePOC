package ar.com.mobeats.consolidar.backend.service.security;

public class UserThreadLocal {

    public static final ThreadLocal<String> userThreadLocal = new ThreadLocal<String>();

    public static void set(String userId) {
        userThreadLocal.set(userId);
    }

    public static void unset() {
        userThreadLocal.remove();
    }

    public static String get() {
        return userThreadLocal.get();
    }	
}
