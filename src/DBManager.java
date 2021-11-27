import java.sql.*;
import javafx.collections.*;

public class DBManager {
    private static ObservableList<Ranked> olRanking; 
    private static String dbHostname, dbUsername, dbPassword, dbName;
    private static int dbPort, kRanked;
    
    public DBManager(XMLConfigParameters cfg){ //(01)
        dbHostname = cfg.getDBHostname();
        dbPort = cfg.getDBPort();
        dbUsername = cfg.getDBUsername();
        dbPassword = cfg.getDBPassword();
        dbName = cfg.getDBName();
        kRanked = cfg.getKRanked();     
    }
    public static void saveScore(String name, int score){ //(02)

        try(Connection co = DriverManager.getConnection("jdbc:mysql://"+dbHostname+":"+dbPort+"/"+dbName, dbUsername,dbPassword);    
            PreparedStatement ps = co.prepareStatement("CALL saveScore(?,?);"); //(03)
        ){
            ps.setString(1, name);
            ps.setInt(2, score);
            ps.executeUpdate();
        }catch(SQLException e){ System.err.println(e.getMessage());}
    }  
    public static ObservableList<Ranked> getRanking(){ //(04)
        olRanking = FXCollections.observableArrayList();
        
        try(Connection co = DriverManager.getConnection("jdbc:mysql://"+dbHostname+":"+dbPort+"/"+dbName, dbUsername,dbPassword);    
            PreparedStatement ps = co.prepareStatement("CALL getRanking(?)"); //(05)
        ){
            ps.setInt(1, kRanked); //(06)
            ResultSet rs = ps.executeQuery();       
            while(rs.next())
                olRanking.add(new Ranked(rs.getString("pName"), rs.getInt("score")));
        }catch(SQLException e){System.err.println(e.getMessage());}
        
        return olRanking;
    }
}

/*
Note
(01)
    Imposta i parametri dal file di configurazione.
(02)
    Salva il nome del giocatore e il punteggio nel database
(03)
    Chiama la procedura 'saveScore' in MySQL. 
    Utilizzo di PreparedStatement per evitare MySQL Injection dal campo del nome.
(04)
    Carica il nome giocatore e punteggio dal database sulla tabella Top-K
    e restituisce l'ObservableList olRanking.
(05)
    Chiama la procedura 'getRanking' in MySQL.
    Utilizzo di PreparedStatement per evitare MySQL Injection dal file XML.
(06)
    Imposta il numero k di righe da caricare nella Top-K.
    
*/