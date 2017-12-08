package fr.ul.exercice.Import;

import fr.ul.exercice.DBManager;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Created by Charles on 17/11/2017.
 */
public class MatiereImport {
    private static final Logger LOG = Logger.getLogger(MatiereImport.class.getName());

    //attributs
    private String filename;

    //constructeurs
    public MatiereImport(String filename) {
        super();
        this.filename = filename;
    }

    public CSVParser buildCVSParser() throws IOException {
        CSVParser res = null;
        Reader in;
        in = new FileReader(filename);
        CSVFormat csvf = CSVFormat.DEFAULT.withCommentMarker('#').withDelimiter(';');
        res = new CSVParser(in, csvf);
        return res;
    }

    private boolean add(String id, String libelle) {
        boolean res = true;
        String sql = "";
        PreparedStatement ps = null;
        sql = "INSERT into Matiere(idMatiere,libelle)"
                +" VALUES(?,?)";
        try {
            ps = DBManager.CONNECTION.prepareStatement(sql);
            ps.setString(1,id);
            ps.setString(2, libelle);
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
                b=true;
            }else{
                String id = item.get(0).trim();
                String libel = item.get(1).trim();

                System.out.println(id);
                System.out.println(libel);
                //enregistrer

                if (add(id, libel)){
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

    public static void main(String[] args) {

        //traitement
        MatiereImport el = new MatiereImport("data/tab-matiere-Tableau 1.csv");

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
