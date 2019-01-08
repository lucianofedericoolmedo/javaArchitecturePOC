package ar.com.mobeats.consolidar.backend.util;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class JSONDateTimeCustomDeserialize extends JsonDeserializer<Date> {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private static final String datePatternRgx = "^(\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2})$";
    private static final Logger LOGGER = Logger.getLogger(JSONDateTimeCustomDeserialize.class.getName());

    @Override
    public Date deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {
        try {
            String date = jp.getText();
            if (date.matches(datePatternRgx)) {
                return dateFormat.parse(date);
            } else {
                Long dateAsLong = Long.valueOf(date);
                return new Date(dateAsLong);
            }
        } catch (ParseException ex) {
            LOGGER.error("Error en el parseo " + jp.getText());
        } catch (Exception ex) {
            LOGGER.error("Error por fecha enviada " + jp.getText());
        }
        return null;
    }
}
