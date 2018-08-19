package com.example.gilles.g_hw_sl_pv_9200.model;

import java.util.List;

public class Gezin2 {

    public String naam;
    public List<Gebruiker> gezinsLeden;

    public Gezin2(String naam,List<Gebruiker> gezinsLeden){
        this.naam = naam;
        this.gezinsLeden = gezinsLeden;
    }
}
