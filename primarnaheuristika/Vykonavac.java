/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package primarnaheuristika;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 *
 * @author Adam
 */
public class Vykonavac {
    private final int OBMEDZENIE_HMOTNOST = 15000;
    private final int OBMEDZENIE_POCET = 300;
    private final int CELKOVY_POCET = 500;
    private int HMOTNOST_PLNEHO_BATOHU = 0;
    private PriorityQueue<Prvok> priorFrontVBatohu = new PriorityQueue<Prvok>(CELKOVY_POCET);
    private ArrayList<Prvok> aListMimoBatoh = new ArrayList<>();
    private ArrayList<Prvok> aListVBatohu = new ArrayList<>();
    private int hmotnostBatohu = 0;
    private int cenaBatohu = 0;
    private boolean bolaVymena = false;
        
    public void vykonaj()
        {
            try 
        {
            File fileCena = new File("H6_c.txt");
            File fileHmotnost = new File("H6_a.txt");
            FileReader fileReaderCena = new FileReader(fileCena);
            FileReader fileReaderHmotnost = new FileReader(fileHmotnost);
            BufferedReader bufferedReaderCena = new BufferedReader(fileReaderCena);
            BufferedReader bufferedReaderHmotnost = new BufferedReader(fileReaderHmotnost);
            int cena;
            int hmotnost;
            
            for(int i = 0; i < CELKOVY_POCET; i++)
            {
                cena = Integer.parseInt(bufferedReaderCena.readLine());
                hmotnost = Integer.parseInt(bufferedReaderHmotnost.readLine());
                priorFrontVBatohu.add(new Prvok(cena, hmotnost));
                hmotnostBatohu += hmotnost;
                cenaBatohu += cena;
            }
            HMOTNOST_PLNEHO_BATOHU = hmotnostBatohu;
            fileReaderCena.close();
            fileReaderHmotnost.close();
	} 
        catch (IOException e) 
        {
            e.printStackTrace();
	}
        
        while (priorFrontVBatohu.size() > OBMEDZENIE_POCET 
           && (hmotnostBatohu - priorFrontVBatohu.peek().getHmotnost()) > OBMEDZENIE_HMOTNOST)
        {
            Prvok vyberany = priorFrontVBatohu.poll();
            hmotnostBatohu -= vyberany.getHmotnost();
            cenaBatohu -= vyberany.getCena();
            aListMimoBatoh.add(vyberany);
        }
        int cenaPoVsuvacej = cenaBatohu;
        
        try 
        {
            File zapisPoVsuvacejHeuristike = new File("poVsuvacej.txt");
            PrintWriter zapisovac = new PrintWriter(zapisPoVsuvacejHeuristike);
            zapisovac.println("Ucelova funkcia (cena batohu po vsuvacej heuristike): " + cenaPoVsuvacej);
            zapisovac.println("Hmotnost batohu: " + hmotnostBatohu);
            zapisovac.println("Pocet prvkov v batohu: " + priorFrontVBatohu.size());
            zapisovac.println("Pocet prvkov mimo batoh: " + aListMimoBatoh.size());
        
            int velkostBatohu = priorFrontVBatohu.size();
            zapisovac.println("\nObsah Batohu:");
            for(int j = 0; j < velkostBatohu; j++)
                {
                    Prvok predmet = priorFrontVBatohu.poll();
                    aListVBatohu.add(predmet);
                    zapisovac.println((j+1) + ". a: " + predmet.getHmotnost() + 
                                    " | c: " + predmet.getCena() + 
                                    " | vyh_koef: " + predmet.getVyhodnostnyKoeficient()
                                    );
                }
        
            zapisovac.println("\nMimo batohu:");
            for(int k = 0; k < aListMimoBatoh.size(); k++)
            {
                Prvok predmet = aListMimoBatoh.get(k);
                zapisovac.println((k+1) + ". a: " + predmet.getHmotnost() + 
                                 " | c: " + predmet.getCena() + 
                                 " | vyh_koef: " + predmet.getVyhodnostnyKoeficient()
                                 );
            }
            zapisovac.close();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }

        
        System.out.println("Ucelova funkcia (cena batohu po vsuvacej heuristike): " + cenaPoVsuvacej);
        System.out.println("Hmotnost batohu: " + hmotnostBatohu);
        System.out.println("Pocet prvkov v batohu: " + aListVBatohu.size());
        System.out.println("Pocet prvkov mimo batoh: " + aListMimoBatoh.size());
        

        System.out.println("\nObsah Batohu:");
        for(int j = 0; j < aListVBatohu.size(); j++)
            {
                Prvok predmet = aListVBatohu.get(j);
                System.out.println((j+1) + ". a: " + predmet.getHmotnost() + 
                                   " | c: " + predmet.getCena() + 
                                   " | vyh_koef: " + predmet.getVyhodnostnyKoeficient()
                                  );
            }
        
        System.out.println("\nMimo batohu:");
        for(int k = 0; k < aListMimoBatoh.size(); k++)
        {
            Prvok predmet = aListMimoBatoh.get(k);
            System.out.println((k+1) + ". a: " + predmet.getHmotnost() + 
                               " | c: " + predmet.getCena() + 
                               " | vyh_koef: " + predmet.getVyhodnostnyKoeficient()
                              );
        }   
        
        ////////////// Vymenna heuristika first admissible
        
        int volnaKapacita = HMOTNOST_PLNEHO_BATOHU - hmotnostBatohu;
        int pocetMimoBatoh = aListMimoBatoh.size();
        int pocetVBatohu = aListVBatohu.size();

        for (int j = 0; j < pocetVBatohu; j++) 
        {
            if(bolaVymena)
            {
                bolaVymena = false;
                j = 0;
            }
            insideLoop(j, pocetMimoBatoh, volnaKapacita);
        }
        
        System.out.println("Ucelova funkcia (cena batohu po vymennej heuristike): " + cenaBatohu);
        System.out.println("Hmotnost batohu: " + hmotnostBatohu);
        System.out.println("Pocet prvkov v batohu: " + aListVBatohu.size());
        System.out.println("Pocet prvkov mimo batoh: " + aListMimoBatoh.size());
        
        double percentualneZlepsenie = (((double)cenaPoVsuvacej / (double)cenaBatohu) - 1) * 100;
        System.out.format("Percentualne zlepsenie oproti vsuvacej heuristike: %.2f", percentualneZlepsenie);
        System.out.println("%");
        
        try 
        {
            File zapisPoVymennejHeuristike = new File("poVymennej.txt");
            PrintWriter zapisovac = new PrintWriter(zapisPoVymennejHeuristike);
            zapisovac.println("Ucelova funkcia (cena batohu po vymennej heuristike): " + cenaBatohu);
            zapisovac.println("Hmotnost batohu: " + hmotnostBatohu);
            zapisovac.println("Pocet prvkov v batohu: " + aListVBatohu.size());
            zapisovac.println("Pocet prvkov mimo batoh: " + aListMimoBatoh.size());
            zapisovac.format("Percentualne zlepsenie oproti vsuvacej heuristike: %.2f", percentualneZlepsenie);
            zapisovac.println("%");
        
            zapisovac.println("\nObsah Batohu:");
            for(int j = 0; j < aListVBatohu.size(); j++)
                {
                    Prvok predmet = aListVBatohu.get(j);
                    zapisovac.println((j+1) + ". a: " + predmet.getHmotnost() + 
                                    " | c: " + predmet.getCena() + 
                                    " | vyh_koef: " + predmet.getVyhodnostnyKoeficient()
                                    );
                }
        
            zapisovac.println("\nMimo batohu:");
            for(int k = 0; k < aListMimoBatoh.size(); k++)
            {
                Prvok predmet = aListMimoBatoh.get(k);
                zapisovac.println((k+1) + ". a: " + predmet.getHmotnost() + 
                                 " | c: " + predmet.getCena() + 
                                 " | vyh_koef: " + predmet.getVyhodnostnyKoeficient()
                                 );
            }
            zapisovac.close();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
        
    public void insideLoop(int j, int pocetMimoBatoh, int volnaKapacita)
    {
        for (int k = 0; k < pocetMimoBatoh; k++) 
        {
            if (volnaKapacita + aListVBatohu.get(j).getHmotnost() >= aListMimoBatoh.get(k).getHmotnost()
                && aListVBatohu.get(j).getCena() > aListMimoBatoh.get(k).getCena()
                && hmotnostBatohu - aListVBatohu.get(j).getHmotnost() > OBMEDZENIE_HMOTNOST)
            {
                Prvok vonZBatohu = aListVBatohu.remove(j);
                Prvok doBatohu = aListMimoBatoh.remove(k);
                aListVBatohu.add(aListVBatohu.size(), doBatohu);
                aListMimoBatoh.add(aListMimoBatoh.size(), vonZBatohu);
                hmotnostBatohu -= vonZBatohu.getHmotnost();
                cenaBatohu -= vonZBatohu.getCena();
                hmotnostBatohu += doBatohu.getHmotnost();
                cenaBatohu += doBatohu.getCena();
                bolaVymena = true;
                return;
            }
        }
    }  
}
