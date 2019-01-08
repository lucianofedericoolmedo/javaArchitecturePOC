package ar.com.mobeats.consolidar.backend.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONObjectConverter {
    
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public static <T> T convertToObject(String json, Class<T> clazz) {

        ObjectMapper mapper = createMapper();

        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Ocurrio un ERROR en la des-serializacion de "
                            + clazz.getSimpleName(), e);
        }
    }

    public static String convertToJSON(Object obj) {

        ObjectMapper mapper = createMapper();

        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Ocurrio un ERROR en la serializacion de "
                            + obj.getClass().getSimpleName(), e);
        }
    }
    
    private static ObjectMapper createMapper() {
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(df);
        return mapper;
    }
}
