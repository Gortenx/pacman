import com.thoughtworks.xstream.XStream;
import java.nio.file.*;

public class XMLConfigParameters {
    //(01)
    private String dbName, dbHostname, dbUsername, dbPassword;
    private int dbPort;
    //(02)
    private String pacmanColor, ghostColor, wallColor, foodColor, bgColor;
    //(03)
    private int width, height;
    //(04)
    private int scoreMultiplier;
    //(05)
    private int kRanked;
    //(06)
    private int gameSpeed;
    
    private String XMLConfig; 

    public XMLConfigParameters(){
        XStream xs = new XStream();
        try {
            XMLConfig = new String(Files.readAllBytes(Paths.get("files/config.xml")));  //(07)
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        XMLConfigParameters cfg = (XMLConfigParameters)xs.fromXML(XMLConfig); //(08)
        if(XMLValidator.validate("files/config.xml", "files/config.xsd", true)){ //(09)
            dbName = cfg.dbName;
            dbHostname = cfg.dbHostname;
            dbPort = cfg.dbPort;    
            dbUsername = cfg.dbUsername;
            dbPassword = cfg.dbPassword;
            pacmanColor = cfg.pacmanColor;
            ghostColor = cfg.ghostColor;
            wallColor = cfg.wallColor;
            foodColor = cfg.foodColor;
            bgColor = cfg.bgColor;
            width = cfg.width;
            height = cfg.height;
            scoreMultiplier = cfg.scoreMultiplier;
            kRanked = cfg.kRanked;
            gameSpeed = cfg.gameSpeed;
        }else{ 
            setDefaultValues();
        }
        
    }
    
    private void setDefaultValues(){ //(10)
        dbName = "pacman";
        dbHostname = "localhost";
        dbPort = 3306;    
        dbUsername = "root";
        dbPassword = "";
        pacmanColor = "yellow";
        ghostColor = "red";
        wallColor = "blue";
        foodColor = "gray";
        bgColor = "#4C4C4C";
        width = 854;
        height = 480;
        scoreMultiplier = 2;
        kRanked = 10;
        gameSpeed = 6;
    }
    public String getDBName(){return dbName;}    
    public String getDBHostname(){return dbHostname;}
    public int getDBPort(){return dbPort;}
    public String getDBUsername(){return dbUsername;}
    public String getDBPassword(){return dbPassword;}
    public String getPacmanColor(){return pacmanColor;}
    public String getGhostColor(){return ghostColor;}
    public String getWallColor(){return wallColor;}
    public String getFoodColor(){return foodColor;}
    public String getBGColor(){return bgColor;}
    public int getWidth(){return width;}
    public int getHeight(){return height;}
    public int getScoreMultiplier(){return scoreMultiplier;}
    public int getKRanked(){return kRanked;}
    public int getGameSpeed(){return gameSpeed;}
    
}
/*
Note
(01)
    Parametri database (classe DBManager).
(02)
    Parametri stile (classe GameStyle).
(03)
    Dimensione finestra (classe Game).
(04)
    Moltiplicatore punteggio (classe Game).
(05)
    K istanze di Ranked nella classifica TOP-K (classi GUI e DBManager).
(06)
    Velocità di gioco (classe Game).
(07)
    Lettura del file di configurazione e memorizzazione in una stringa.
(08)
    Dopo aver letto dal file di configurazione, istanzia un oggetto 
    della classe stessa e scrive i parametri sulle variabili di istanza.
(09)
    Se il file di configurazione è stato validato correttamente
    allora copia i parametri, altrimenti assegna i parametri di default.
(10)
    Assegna i parametri di default.
    
*/