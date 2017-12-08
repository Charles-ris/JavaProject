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


public class CandidatImport {
    private static final Logger LOG = Logger.getLogger(EpreuveImport.class.getName());

    //attributs
    private String filename;


    //constructeurs
    public CandidatImport(String filename) {
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
     * insérer ou mettre à jour une épreuve
     * @param libelle
     * @return
     */
    private boolean add(String id, String libelle,String a,String m,String s,String me,String spe,String sec) {
        boolean res = true;
        String sql = "";
        PreparedStatement ps = null;
        sql = "INSERT into Candidat(idCandidat,libelleExam,annee,mois,serie,mention,specialite,sect)"
                +" VALUES(?,?,?,?,?,?,?,?)";
        try {
            ps = DBManager.CONNECTION.prepareStatement(sql);
            ps.setString(1,id);
            ps.setString(2, libelle);
            ps.setString(3, a);
            ps.setString(4, m);
            ps.setString(5, s);
            ps.setString(6, me);
            ps.setString(7, spe);
            ps.setString(8, sec);
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
            if (!b){
                b = true;
            }else{
                String libel = item.get(0).trim();
                String a = item.get(1).trim();
                String m = item.get(2).trim();
                String se = item.get(3).trim();
                String men= item.get(4).trim();
                String spe = item.get(5).trim();
                String sec = item.get(6).trim();
                String num = item.get(7).trim();


                //enregistrer
                if (add(num,libel,a,m,se,men,spe,sec)){
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
    public Logger getLog() {
        return this.LOG;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }
    //===========================================


}
