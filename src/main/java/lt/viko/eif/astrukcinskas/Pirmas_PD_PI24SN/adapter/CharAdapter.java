package lt.viko.eif.astrukcinskas.Pirmas_PD_PI24SN.adapter;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

public class CharAdapter extends XmlAdapter<String, Character> {

    /**
     * Pagalbinis metodas, konvertuojantis XML reikšmę į Java Character objektą
     * @param v tekstinė simbolio reikšmė
     * @return suformatuotas objektas arba null
     */
    @Override
    public Character unmarshal(String v) {
        return (v != null && !v.isEmpty()) ? v.charAt(0) : null;
    }

    /**
     * Pagalbinis metodas, Java Character objektą į XML reikšmę
     * @param v Character simbolio reikšmė
     * @return suformatuotas tekstas arba null
     */
    @Override
    public String marshal(Character v) {
        return (v != null) ? String.valueOf(v) : null;
    }
}