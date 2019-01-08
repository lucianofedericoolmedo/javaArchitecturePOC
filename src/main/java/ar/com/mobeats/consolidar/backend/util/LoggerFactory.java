package ar.com.mobeats.consolidar.backend.util;

import org.apache.log4j.Logger;

public class LoggerFactory {

    public static <T> Logger getLogger (T loggingObject) {
        return Logger.getLogger(loggingObject.getClass());
    }

    public static <T> Logger getLogger (String loggingPath) {
        return Logger.getLogger(loggingPath);
    }

}
