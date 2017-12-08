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


public class CoefficientExtract {
    private static final Logger LOG = Logger.getLogger(NoteExtract.class.getName());

    //attributs
    private String filename;


    //constructeurs
    public CoefficientExtract(String filename) {
        super();
        this.filename = filename;
    }

    public void run(String se, String men, String spe, String sec){
        DBManager.connect();
        String sql = "";
        PreparedStatement ps = null;
        sql = "SELECT idEpreuve, coefficient fROM Coefficient WHERE serie = ? AND mention = ? AND specialite = ? AND sect = ?";
        try {
            ps = DBManager.CONNECTION.prepareStatement(sql);
            ps.setString(1,se);
            ps.setString(2,men);
            ps.setString(3,spe);
            ps.setString(4,sec);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String e = rs.getString("idEpreuve");
                String coef = rs.getString("coefficient");
                System.out.print(e+" ");
                System.out.println(coef);

                //ECRIRE FIN
            }
        }catch (SQLException e) {
            LOG.warning(e.getMessage());
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
        CoefficientExtract el = new CoefficientExtract("assoc-remplacer-Tableau 1.csv");

        DBManager.URI= "jdbc:mysql://127.0.0.1:8889/projetSCA";
        DBManager.USER = "root";
        DBManager.PASSWORD = "root";

        el.run("S","EAT","E","R");


    }

}
