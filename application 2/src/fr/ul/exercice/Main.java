package fr.ul.exercice;

import fr.ul.exercice.Import.*;
import org.apache.commons.csv.CSVParser;

import java.io.IOException;

/**
 * Created by Charles on 16/11/2017.
 */
public class Main {

    public static void main(String[] args) {

        // Init
        DBManager.URI= "jdbc:mysql://127.0.0.1:8889/projetSCA";
        DBManager.USER = "root";
        DBManager.PASSWORD = "root";
        CSVParser p = null;

        // Creation Tables

        //Création Classes

        //CandidatImport vImport = new CandidatImport("data/tabs-candidats.csv");
        //CoefficientImport vImport = new CoefficientImport("data/assoc-determiner-Tableau 1.csv");
        //EpreuveImport vImport = new EpreuveImport("data/tab-epreuves-Tableau 1.csv");
        //EpreuveMatiereImport vImport = new EpreuveMatiereImport("data/assoc-preciser-Tableau 1.csv");
        //GroupeImport vImport = new GroupeImport("data/attr-groupes-Tableau 1.csv");
        //MatiereImport vImport = new MatiereImport("data/tab-matiere-Tableau 1.csv");
        //ProfilImport vImport = new ProfilImport("data/tab-profils-Tableau 1.csv");
        //RattrapageImport vImport = new RattrapageImport("data/assoc-remplacer-Tableau 1.csv");
        //NoteImport vImport = new NoteImport("data/tab-notes-Tableau 1.csv");
        ComposerImport vImport = new ComposerImport("data/assoc-composer-Tableau 1.csv");

        //Execute Importation
        try {
            p = vImport.buildCVSParser();
            vImport.updateDB(p);
        } catch (IOException e) {
            vImport.getLog().severe(e.getMessage());
        }

    }
}
