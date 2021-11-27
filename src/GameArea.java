import java.io.*;
import javafx.geometry.*;
import javafx.scene.layout.*;

public class GameArea{
    private static final int NUMCOLS = 20 ;
    private static final int NUMROWS = 15 ;
    private static GridPane area = new GridPane();
    private static GameStyle gameStyle;
    public static char[][] currentMap; //(01)
    public static char[][] map; //(02)

    public GameArea(XMLConfigParameters cfg){ //(03)
        
        for (int i=0; i<NUMCOLS; ++i) {
            ColumnConstraints colConst = new ColumnConstraints();//(04)
            colConst.setPercentWidth(90 / NUMCOLS); //(04)
            area.getColumnConstraints().add(colConst);
        }
        
        for (int i=0; i<NUMROWS; ++i) {
            RowConstraints rowConst = new RowConstraints();//(05)
            rowConst.setPercentHeight(120 / NUMROWS); 
            area.getRowConstraints().add(rowConst);         
        }
        
        for(int i=0; i<NUMROWS; ++i){
            for(int j=0; j<NUMCOLS; ++j){
                Pane pane = new Pane(); //(06)
                area.add(pane, j, i);
            }
        }

        area.setPadding(new Insets(50, 0, 0, 50));
        gameStyle = new GameStyle(cfg);
        
        reloadFromCache(); //(07)
    }
    public void resetGameArea(){ //(08)
        currentMap = loadMap();
        map = loadMap();
    }
    public void update(){ //(09)
        gameStyle.setAreaStyle();
    }
    public char[][] loadMap(){ //(10)
        
        char[][] m = null;
        try {
            FileInputStream fis = new FileInputStream("./files/map.bin");
            ObjectInputStream ois = new ObjectInputStream(fis);
            m = (char[][]) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("ERRORE: file map.bin non trovato");
        }
        return m;     
    }
    public static GridPane getArea(){ return area; }
    public static int getNUMCOLS(){return NUMCOLS;};
    public static int getNUMROWS(){return NUMROWS;};
    public static char[][] getMap(){return map;}  
    public static char[][] getCurrentMap(){return currentMap;}  
    public void reloadFromCache(){ 
        if(CacheManager.load() != null){
            Cache cache = CacheManager.load();
            map = cache.map;
            currentMap = cache.currentMap;
        }
    }
}

/*
Note
(01)
    Mappa corrente che tiene conto della posizione dei personaggi.
(02)
    Mappa che tiene conto del cibo consumato da Pacman.
(03)
    Inizializzazione area di gioco.
(04)
    Definisce un vincolo sulla larghezza della colonna in un GridPane:
    https://docs.oracle.com/javafx/2/api/javafx/scene/layout/ColumnConstraints.html
    Se un ColumnConstraint viene aggiunto come colonna in un GridPane,
    allora il GridPane terrà conto di tali vincoli alla costruzione della griglia.
(05)
    Definisce un vincolo sull'altezza della riga in un GridPane:
    https://docs.oracle.com/javafx/2/api/javafx/scene/layout/RowConstraints.html
    Se un RowConstraint viene aggiunto come riga in un GridPane,
    allora il GridPane terrà conto di tali vincoli alla costruzione della griglia.
(06)
    Utilizzo del Pane per avere un oggetto stilizzabile all'interno di ogni singola cella.
    Non viene adottato il Rectangle perché al cambio della risoluzione della finestra,
    il ridimensionamento non avviene correttamente come succede per il Pane.
(07)
    Caricamento delle matrici map e currentMap dalla Cache.
(08)
    Invoca l'aggiornamento dello stile di tutte le celle della griglia.
(10)
    Carica la mappa da file binario.
*/