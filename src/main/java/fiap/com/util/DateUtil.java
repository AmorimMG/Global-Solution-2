package fiap.com.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {
    public static java.sql.Date sqlDateFromJavaDate(Date date) {
        return new java.sql.Date(date.getTime());
    }

    /**
     * Instancia uma data a partir de uma data no formato dd/MM/yyyy
     * <p>
     * Ex: 31/12/2024
     */
    public static Date dateFromString(String str) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return dateFormat.parse(str);
        } catch (ParseException e) {
            System.out.println("Erro convertendo data: " + str + "\n" + e.getMessage());
            return new Date();
        }
    }

    public static String formatLocalDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        return dateTime.format(formatter);
    }
}
