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
package fr.ul.exercice.Import;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import fr.ul.exercice.DBManager;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;


public class CoefficientImport {
    private static final Logger LOG = Logger.getLogger(EpreuveImport.class.getName());

    //attributs
    private String filename;


    //constructeurs
    public CoefficientImport(String filename) {
        super();
        this.filename = filename;
    }
    /**
     * chargement d'un fichier au format CSV
     * dont la première ligne est le nom des champs
     * @return CSVParser
     * @throws IOException
     */
    public CSVParser buildCVSParser() throws IOException {
        CSVParser res = null;
        Reader in;
        in = new FileReader(filename);
        CSVFormat csvf = CSVFormat.DEFAULT.withCommentMarker('#').withDelimiter(';');
        res = new CSVParser(in, csvf);
        return res;
    }
    /**
     *
     */
    private boolean add(String serie, String mention, String specialite,String option,String code,String libelle,String coeff,String composition,String bonus,String facult) {
        boolean res = true;
        String sql = "";
        PreparedStatement ps = null;
        sql = "INSERT into Coefficient(serie,mention,specialite,sect,idEpreuve,libelleE,coefficient,composition,bonus,facultatif)"
                +" VALUES(?,?,?,?,?,?,?,?,?,?)";
        try {
            ps = DBManager.CONNECTION.prepareStatement(sql);
            ps.setString(1,serie);
            ps.setString(2, mention);
            ps.setString(3, specialite);
            ps.setString(4, option);
            ps.setString(5, code);
            ps.setString(6, libelle);
            ps.setString(7, coeff);
            ps.setString(8, composition);
            ps.setString(9, bonus);
            ps.setString(10, facult);
            LOG.info(ps.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            LOG.warning(e.getMessage());
            res = false;
        }
        return res;
    }
    /**
     * sauvegarde dans la base de données
     * @param parser
     * @return
     */
    public int updateDB(CSVParser parser) {
        int res = 0;
        DBManager.connect();
        boolean b = false;
        for (CSVRecord item : parser) {
            if(!b){
                b=true;
            }else {
                String serie = item.get(0).trim();
                String mention = item.get(1).trim();
                String specialite = item.get(2).trim();
                String option = item.get(3).trim();
                String code = item.get(4).trim();
                String libelle = item.get(5).trim();
                String coeff = item.get(6).trim();
                String composition = item.get(7).trim();
                String bonus = item.get(8).trim();
                String facult = item.get(9).trim();


                if (add(serie, mention, specialite, option, code, libelle, coeff, composition, bonus, facult)) {
                    res++;
                }

            }



        }
        DBManager.quit();
        return res;
    }

    //setters & getters
    public String getFilename() {
        return filename;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }
    public Logger getLog() {
        return this.LOG;
    }
    //===========================================
    public static void main(String[] args) {

        //traitement
        CoefficientImport el = new CoefficientImport("data/assoc-determiner-Tableau 1.csv");

        DBManager.URI= "jdbc:mysql://127.0.0.1:8889/projetSCA";
        DBManager.USER = "root";
        DBManager.PASSWORD = "root";
        CSVParser p = null;
        try {
            p = el.buildCVSParser();
            el.updateDB(p);
        } catch (IOException e) {
            LOG.severe(e.getMessage());
        }

    }

}
