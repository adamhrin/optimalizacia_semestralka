/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package primarnaheuristika;

import java.util.Comparator;

/**
 *
 * @author Adam
 */
class Prvok implements Comparator<Prvok>, Comparable<Prvok> {
    private final int cena_;
    private final int hmotnost_;
    
    public Prvok(int cena, int hmotnost) 
    {
        this.cena_ = cena;
        this.hmotnost_ = hmotnost;
    }
    public int getCena() 
    {
        return this.cena_;
    }

    public int getHmotnost() 
    {
        return this.hmotnost_;
    }
    
    public double getVyhodnostnyKoeficient()
    {
        return ((double)this.cena_ / (double)this.hmotnost_);
    }

    @Override
    public int compare(Prvok o1, Prvok o2) {
        return Double.compare(o2.getVyhodnostnyKoeficient(), o1.getVyhodnostnyKoeficient());
    }

    @Override
    public int compareTo(Prvok p) {
        //Double cena = new Double(this.cena_);
        //Double hmotnost = new Double(this.hmotnost_);
        Double vyhKoefMoj = new Double(this.getVyhodnostnyKoeficient());
        Double vyhKoefOther = new Double(p.getVyhodnostnyKoeficient());
        
        return vyhKoefOther.compareTo(vyhKoefMoj);
    }
}
