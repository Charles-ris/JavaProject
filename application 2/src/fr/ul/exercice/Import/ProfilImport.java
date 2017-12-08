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
public class ProfilImport {
    private static final Logger LOG = Logger.getLogger(EpreuveImport.class.getName());

    //attributs
    private String filename;


    //constructeurs
    public ProfilImport(String filename) {
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

    private boolean add(String s, String m, String spe, String sec) {
        boolean res = true;
        String sql = "";
        PreparedStatement ps = null;
        sql = "INSERT into Profil(serie,mention, specialite, sect)"
                +" VALUES(?,?,?,?)";
        try {
            ps = DBManager.CONNECTION.prepareStatement(sql);
            ps.setString(1,s);
            ps.setString(2, m);
            ps.setString(3, spe);
            ps.setString(4, sec);
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
        boolean b =false;
        for (CSVRecord item : parser) {
            if(!b){
                b=true;
            }else{
                String s = item.get(0).trim();
                String m = item.get(1).trim();
                String spe = item.get(2).trim();
                String sec = item.get(3).trim();
                //enregistrer
                if (add(s,m,spe,sec)){
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
       ProfilImport el = new ProfilImport("data/tab-profils-Tableau 1.csv");

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
