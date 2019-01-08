package ar.com.mobeats.consolidar.backend.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.format.datetime.DateFormatter;

public class DateUtil {
    
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm";
    public static final String DATE_FORMAT_FROM_PATHPARAM = "dd-MM-yyyy";
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    public static final SimpleDateFormat SIMPLE_DATE_TIME_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    public static final SimpleDateFormat SIMPLE_DATE_TIME_FORMAT_TIMESTAMP = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat SIMPLE_DATE_TIME_FORMAT_ALTERNATIVE = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
    public static final SimpleDateFormat SIMPLE_DATE_TIME_FORMAT_FOR_FILE = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

    public static final Integer HOURS_IN_A_DAY = 24;
    
    private static final Logger LOGGER = Logger.getLogger(DateUtil.class);
    
    public static String toDateString(Date date) {
        if (date != null) {
            return SIMPLE_DATE_FORMAT.format(date);
        } else {
            return null;
        }
    }
    
    public static String toDateTimeString(Date date) {
        return SIMPLE_DATE_TIME_FORMAT.format(date);
    }
    
    public static String toDateTimeStringForFile(Date date) {
        return SIMPLE_DATE_TIME_FORMAT_FOR_FILE.format(date);
    }

    public static String toDateTimeAlternativeString(Date date) {
        return SIMPLE_DATE_TIME_FORMAT_ALTERNATIVE.format(date);
    }
    
    public static String toDateTimeTimeStampString(Date date) {
        return SIMPLE_DATE_TIME_FORMAT_TIMESTAMP.format(date);
    }

    public static Date parsearFechaDesde(String desdeStr, String dateFormat) {
        DateFormatter formatter = new DateFormatter(dateFormat);
        Date desde = DateUtil.parseDate(desdeStr, formatter);
        desde = DateUtil.ajustarFechaDesde(desde);
        return desde;
    }
    
    public static Date parsearFechaDesde(String desdeStr) {
        return parsearFechaDesde(desdeStr, DateUtil.DATE_FORMAT);
    }
    
    public static Date parsearFechaHasta(String hastaStr, String dateFormat) {
        DateFormatter formatter = new DateFormatter(dateFormat);
        Date hasta = DateUtil.parseDate(hastaStr, formatter);        
        hasta = DateUtil.ajustarFechaHasta(hasta);
        return hasta;
    }
    
    public static Date parsearFechaHasta(String hastaStr) {
        return parsearFechaHasta(hastaStr, DateUtil.DATE_FORMAT);
    }

    public static Date ajustarFechaDesde(Date desde) {
        if(desde != null) {
            desde = DateUtils.truncate(desde, Calendar.DAY_OF_MONTH);
        }
        return desde;
    }
    
    public static Date ajustarFechaHasta(Date hasta) {
        if(hasta != null) {
            hasta = DateUtils.addDays(hasta, 1);
            hasta = DateUtils.truncate(hasta, Calendar.DAY_OF_MONTH);
            hasta = DateUtils.addSeconds(hasta, -1);
        }
        return hasta;
    }
    
    public static Date parseDate(String dateAsString, DateFormatter formatter) {
        if (dateAsString == null || formatter == null) {
            return null;
        }

        try {
            return formatter.parse(dateAsString, Locale.getDefault());
        } catch (ParseException e) {
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("Se encontro un parametro de fecha con formato invalido: [").append(dateAsString)
                    .append("]. El formato valido es: [").append(DATE_FORMAT)
                    .append("]. Se esta ignorando el parametro!");

            LOGGER.error(strBuilder.toString());
            return null;
        }
    }

    public static Date moverDiasDesdeHoy(int dias){
    	return DateUtils.addDays(new Date(), dias);
    }
    
    public static Double diferenciaEnDias(Date startDate, Date endDate) {
        long startTime = startDate.getTime();
        long endTime = endDate.getTime();
        Long diffTime = endTime - startTime;
        double diffDays = diffTime.doubleValue() / (1000 * 60 * 60 * 24);
        return diffDays;
    }
    
    public static Double diferenciaEnMinutosEntreFecha(Date startDate, Date endDate) {
        long startTime = startDate.getTime();
        long endTime = endDate.getTime();
        Long diffTime = endTime - startTime;
        double diffDays = diffTime.doubleValue() / (1000 * 60);
        return diffDays;
    }
    
    /**
     * Retorna la fecha actual menos la cantidad de horas indicadas.
     * @param haceHoras : Un Integer indicando la cantidad de horas a restar.
     * @return La fecha con la hora restada.
     */
    public static Date fechaHaceHoras(Integer haceHoras) {
        Calendar calendar = DateUtil.getCalendar(new Date());
        calendar.add(Calendar.HOUR, - haceHoras);
        return calendar.getTime();
    }
    
    /**
     * Retorna la fecha actual menos la cantidad de minutos indicados.
     * @param haceHoras : Un Integer indicando la cantidad de minutos a restar.
     * @return La fecha con los minutos restados.
     */
    public static Date fechaHaceMinutos(Integer haceMinutos) {
        Calendar calendar = DateUtil.getCalendar(new Date());
        calendar.add(Calendar.MINUTE, - haceMinutos);
        return calendar.getTime();
    }
    
    public static Date createDate(Integer day, Integer month, Integer year) {
        Calendar calendar = DateUtil.getCalendar(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month - 1); // Substract one for compensate the Calendar's month starting at pos zero.
        calendar.set(Calendar.YEAR, year);
        return calendar.getTime();
    }
    
    public static Boolean firstAfterSecond(Date firstDate, Date secondDate) {
        Calendar firstCalendar = DateUtil.getCalendar(firstDate);
        Calendar secondCalendar = DateUtil.getCalendar(secondDate);
        return firstCalendar.after(secondCalendar);
    }
    
    public static Calendar getCalendar(Date aDate) {
         Calendar calendar = Calendar.getInstance();
         calendar.setTime(aDate);
         return calendar;
    }
    
    public static Date addTime(Date aDate, Integer aField, Integer amount) {
        Calendar calendar = DateUtil.getCalendar(aDate);
        calendar.add(aField, amount);
        return calendar.getTime();
   }
    
    public static Date parseFechaDesdeParaQuery(String fechaDesde) {
        if (fechaDesde == null || fechaDesde.isEmpty()) {
            fechaDesde = "01/01/2000"; // Puse esta fecha porque el sistema empezo mucho despues.
        }
        return DateUtil.parsearFechaDesde(fechaDesde);
    }
    
    public static Date parseFechaHastaParaQuery(String fechaHasta) {
        if (fechaHasta == null || fechaHasta.isEmpty()) {
            fechaHasta = "01/01/9999";
        }
        return DateUtil.parsearFechaHasta(fechaHasta);
    }

    public static Date setFieldWithValue(Date date, int field, Integer valueToSet) {
        Calendar calendarDate = getCalendar(date);
        calendarDate.set(field, valueToSet);
        return calendarDate.getTime();
    }

    public static Date ultimoFinDeMes() {
        Date ultimoFinDeMes = setFieldWithValue(new Date(), Calendar.DAY_OF_MONTH, 1);
        ultimoFinDeMes = addTime(ultimoFinDeMes, Calendar.DAY_OF_MONTH, -1);
        return ultimoFinDeMes;
    }
    
    public static Date proximoInicioDeDia() {
        Date inicioFechaHoy = ajustarFechaDesde(new Date());
        return addTime(inicioFechaHoy, Calendar.DAY_OF_MONTH, 1);
    }

    public static Date putCurrentTimeInDate(Date fecha) {
        Calendar calendarToday = getCalendar(new Date());
        fecha = setFieldWithValue(fecha, Calendar.HOUR_OF_DAY, calendarToday.get(Calendar.HOUR_OF_DAY));
        fecha = setFieldWithValue(fecha, Calendar.MINUTE, calendarToday.get(Calendar.MINUTE));
        fecha = setFieldWithValue(fecha, Calendar.SECOND, calendarToday.get(Calendar.SECOND));
        fecha = setFieldWithValue(fecha, Calendar.MILLISECOND, calendarToday.get(Calendar.MILLISECOND));
        return fecha;
    }
    
}