package com.example.gilles.g_hw_sl_pv_9200.model;

import java.util.Date;

public class KostenOverzicht {

    public int maand;
    public int jaar;
    public Kost[] kosten;

    /**
     *
     * @param kosten
     * @param maand
     * @param jaar
     */
    public KostenOverzicht(int maand, int jaar, Kost[] kosten){
        this.maand = maand;
        this.jaar = jaar;
        this.kosten = kosten;
    }

    public Date getOverzichtDatum(){
        Date date = new Date();
        date.setMonth(maand);
        date.setYear(jaar-1900);
        return date;
    }

}
