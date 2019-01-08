/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.mobeats.consolidar.backend.util;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 *
 * @author Juan Pablo Vello
 */
@Component
public class JSONDateDeserialize extends JsonDeserializer<Date> {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public Date deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {
        try {
            String date = jp.getText();
            
            return dateFormat.parse(date);
        } catch (ParseException ex) {
            Logger.getLogger(JSONDateDeserialize.class.getName()).log(Level.SEVERE, "Error en el parseo " + jp.getText());
        } catch (Exception ex) {
            Logger.getLogger(JSONDateDeserialize.class.getName()).log(Level.SEVERE, "Error por fecha enviada " + jp.getText());
        }
        return null;

    }


}
