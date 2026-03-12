package lt.viko.eif.astrukcinskas.Pirmas_PD_PI24SN.adapter;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Pagalbinis metodas, konvertuojantis XML reikšmę į Java LocalDateTime objektą
     * @param v tekstinė datos reikšmė
     * @return suformatuotas objektas arba null
     */
    @Override
    public LocalDateTime unmarshal(String v) {
        return (v != null) ? LocalDateTime.parse(v, formatter): null;
    }

    /**
     * Pagalbinis metodas, konvertuojantis LocalDateTime objektą į XML reikšmę
     * @param v LocalDateTime laiko reikšmė
     * @return suformatuotas tekstas arba null
     */
    @Override
    public String marshal(LocalDateTime v) {
        return (v != null)?  v.format(formatter) : null;
    }

}
