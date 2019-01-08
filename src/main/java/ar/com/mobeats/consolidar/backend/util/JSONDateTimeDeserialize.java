package ar.com.mobeats.consolidar.backend.util;

import java.io.IOException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

@Component
public class JSONDateTimeDeserialize extends JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {
        try {
            return new Date(jp.getLongValue());
        } catch (Exception ex) {
            Logger.getLogger(JSONDateTimeDeserialize.class.getName()).error("Could not deserialize Date. Returning null.");
        }
        return null;

    }

}
