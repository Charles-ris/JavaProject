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

/**
 * Created by Charles on 22/11/2017.
 */
public class EpreuveMatiereImport{
    private static final Logger LOG = Logger.getLogger(EpreuveImport.class.getName());

    //attributs
    private String filename;


    //constructeurs
    public EpreuveMatiereImport(String filename) {
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

    private boolean add(String ie, String le, String im, String lm) {
        boolean res = true;
        String sql = "";
        PreparedStatement ps = null;
        sql = "INSERT into EpreuveAPourMatieres(idEpreuve,libelleE, idMatiere, libelleM)"
                +" VALUES(?,?,?,?)";
        try {
            ps = DBManager.CONNECTION.prepareStatement(sql);
            ps.setString(1,ie);
            ps.setString(2, le);
            ps.setString(3, im);
            ps.setString(4, lm);
            LOG.info(ps.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            LOG.warning(e.getMessage());
            res = false;
        }
        return res;
    }

    public int updateDB(CSVParser parser) {
        int res = 0;
        DBManager.connect();
        boolean b = false;
        for (CSVRecord item : parser) {
            if(!b){
                b =true;
            }else{
                String ie = item.get(0).trim();
                String le = item.get(1).trim();
                String im = item.get(2).trim();
                String lm = item.get(3).trim();

                System.out.println(ie);
                //enregistrer

                if (add(ie,le,im,lm)){
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
        EpreuveMatiereImport el = new EpreuveMatiereImport("data/assoc-preciser-Tableau 1.csv");

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
