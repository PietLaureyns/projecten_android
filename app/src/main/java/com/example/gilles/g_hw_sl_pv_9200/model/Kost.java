package com.example.gilles.g_hw_sl_pv_9200.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by tomde on 11/13/2017.
 */

public class Kost implements Serializable{
    public String naam, aanmakerId, aanmaker, omschrijving, categorie;
    public Date datum;
    public int bedrag;
    public List<String> bekeuringen;
    private String _id;

    /**
     *
     * @param naam
     * @param aanmakerId
     * @param datum
     * @param bedrag
     * @param omschrijving
     * @param categorie
     */
    public Kost(String naam, String aanmakerId, String aanmaker, Date datum, int bedrag,
                String omschrijving, String categorie, List<String> bekeuringen){
        this.naam = naam;
        this.aanmaker = aanmaker;
        this.aanmakerId = aanmakerId;
        this.datum = datum;
        this.bedrag = bedrag;
        this.omschrijving =omschrijving;
        this.categorie = categorie;
        this.bekeuringen = bekeuringen;
    }

    public String getNaam() {
        return naam;
    }
    public String getAanmaker() {
        return aanmaker;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public int getBedrag() {
        return bedrag;
    }

    public void setBedrag(int kost) {
        this.bedrag = kost;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public void setOmschrijving(String mededeling) {
        this.omschrijving = mededeling;
    }

    public String toString(){
        return String.format(this.naam+"\t"+this.bedrag+"€");
    }
    public String getId() { return this._id;}
    public Date getDatum() { return this.datum;}

}
