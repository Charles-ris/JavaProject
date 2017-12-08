/*
 *  License and Copyright:
 *  This file is part of "exercice" project.
 *
 *   It is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   any later version.
 *
 *   It is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 *
 *  Copyright 2017 by LORIA, Université de Lorraine
 *  All right reserved
 */
/**
 * Projet : exercice
 * Classe de chargement d'une liste d'épreuve au format CSV
 * et de stockage dans la base de donnée
 * @author Azim Roussanaly
 * sept 2017
 */
package fr.ul.exercice.Export;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

import fr.ul.exercice.DBManager;


public class NoteExtract {
    private static final Logger LOG = Logger.getLogger(NoteExtract.class.getName());

    //attributs
    private String filename;


    //constructeurs
    public NoteExtract(String filename) {
        super();
        this.filename = filename;
    }



    public void run(String serie, String g){
        DBManager.connect();
        File ff=new File("src/fr/ul/exercice/Result.csv");
        //CODE
        //Extraire les notes au premier tour pour les étudiants d’une série
        String sql = "";
        PreparedStatement ps = null;
        sql = "SELECT note, idCandidat, idEpreuve FROM Note natural join Candidat WHERE serie=? AND idEpreuve in (Select idEpreuve FROM Groupe WHERE groupe = ?) ORDER BY idCandidat";
        try {
            ps = DBManager.CONNECTION.prepareStatement(sql);
            ps.setString(1,serie);
            ps.setString(2,g);
            ResultSet rs = ps.executeQuery();
            FileWriter ffw= null;
            ffw = new FileWriter(ff);
            ffw.write("idCandidat;idEpreuve;Note\n");
            while (rs.next()) {
                String idc = rs.getString("idCandidat");
                String ide = rs.getString("idEpreuve");
                String no = rs.getString("note");
                //System.out.println("Candidat : "+candidat+" epreuve : "+ epreuve +" note : "+ note);
                //System.out.println("");

                //ECRIRE RESULATS DANS FICHIER


                    ffw.write(idc+";"+ide+";"+no+"\n");  // écrire une ligne dans le fichier result.csv
            }
            ffw.close(); // fermer le fichier à la fin des traitements

        } catch (SQLException e) {
            LOG.warning(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }


        DBManager.quit();
    }

    public void sql6(){
        DBManager.connect();
        //CODE
        //Extraire les moyennes au premier tour pour les étudiants d’une série
        // idCandidat;idEpreuve;note
        String idc = "";
        double somme = 0;
        double c = 0;
        ArrayList<String> li = new ArrayList<>();

        try{
            boolean b = true;
            InputStream ips=new FileInputStream("Result.csv");
            InputStreamReader ipsr=new InputStreamReader(ips);
            BufferedReader br=new BufferedReader(ipsr);
            String ligne;
            // PARCOURS DE L'ENSEMBLE DES NOTES EN FONCTION D'UNE SERIE
            while ((ligne=br.readLine())!=null){
                if(b){
                    b = false;
                }else {
                    //System.out.println(ligne);
                    String[] l = ligne.split(";");
                    //SI C'EST UN NOUVEL ETUDIANT
                    if (idc != l[0]){
                        li.add(idc+" : "+ (somme/c));
                        idc = l[0];
                        c = 0;
                        somme = 0;
                    }

                    //RECUPERATION DES COEEFICIENTS EN FONCTION DU PROFIL DE L'ETUDIANT
                    String sql = "";
                    PreparedStatement ps = null;
                    sql = "SELECT idEpreuve, coefficient FROM Coefficient  natural join Candidat WHERE idCandidat = ? ";
                    try {
                        ps = DBManager.CONNECTION.prepareStatement(sql);
                        ps.setString(1,l[0]);

                        ResultSet rs = ps.executeQuery();

                        while (rs.next()) {
                            String epreuve = rs.getString("idEpreuve");
                            String coeff = rs.getString("coeff");
                            //System.out.println(epreuve+"  "+coeff);

                            //ECRIRE RESULATS DANS FICHIER

                        }

                    } catch (SQLException e) {
                        LOG.warning(e.getMessage());
                    }
                }
            }
            br.close();
            // ECRIRE LE RESULTAT DE LI
        }
        catch (Exception e){
            System.out.println(e.toString());
        }

        DBManager.quit();
    }

    //setters & getters
    public String getFilename() {
        return filename;
    }
    public Logger getLog() {
        return this.LOG;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }
    //===========================================
    public static void main(String[] args) {

        //traitement
        NoteExtract el = new NoteExtract("assoc-remplacer-Tableau 1.csv");

        DBManager.URI= "jdbc:mysql://127.0.0.1:8889/projetSCA";
        DBManager.USER = "root";
        DBManager.PASSWORD = "root";

        el.run("S","1");


    }

}
