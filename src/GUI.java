import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

public class GUI{
    private BorderPane root;    
    private VBox rightPanel;
    private Label notify;
    private TextField scoreField;
    private TableView<Ranked> rankingTable;
    private TextField nameField;
    private Button startButton;

    public GUI(XMLConfigParameters cfg){ //(01)
        rankingTable = new TableView();
        scoreField = new TextField();
        notify = new Label("Notifica:");
        nameField = new TextField();
        Label scoreLabel = new Label("Punteggio");
        scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        Label TopKLabel = new Label("Top "+cfg.getKRanked()); 
        TopKLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        Label nameLabel = new Label("Inserisci il tuo nome:");
        initScoreField();
        initRankingTable();
        initNotify();
        initNameField();
        startButton = new Button("Nuova Partita");
        startButton.setPrefWidth(200);
        rightPanel = new VBox();        
        rightPanel.setAlignment(Pos.CENTER);
        rightPanel.setPadding(new Insets(30, 30, 0, 0));
        rightPanel.getChildren().addAll(scoreLabel, scoreField, TopKLabel, rankingTable, nameLabel, nameField, startButton);  

        reloadFromCache(); 
        
        root = new BorderPane();      
        root.setRight(rightPanel);
        root.setBottom(notify);
        root.setCenter(GameArea.getArea());
    }
    private void initRankingTable(){ //(02)
        TableColumn nameColumn = new TableColumn("Nome"); //prendere k da file di config
        nameColumn.setPrefWidth(120);
        nameColumn.setMaxWidth(120);
        nameColumn.setMinWidth(120);
        nameColumn.setSortable(false);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name")); //4
        TableColumn scoreColumn = new TableColumn("Punteggio"); //prendere k da file di config
        scoreColumn.setPrefWidth(80);
        scoreColumn.setMaxWidth(80);
        scoreColumn.setMinWidth(80);
        scoreColumn.setSortable(false);
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score")); //4
        rankingTable.getColumns().addAll(nameColumn, scoreColumn);
        rankingTable.setStyle("-fx-focus-color: transparent;");
        rankingTable.setPlaceholder(new Label("Nessun classificato"));
        rankingTable.setSelectionModel(null);
        rankingTable.setPrefWidth(202);
        rankingTable.setPrefHeight(193);
    }
    private void initScoreField(){ 
        scoreField.setDisable(true);
        scoreField.setStyle("-fx-opacity: 1;");
        scoreField.setPrefWidth(120);
        scoreField.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        scoreField.setAlignment(Pos.CENTER);
        scoreField.setPadding(new Insets(5, 0, 5, 0));
    }
    private void initNotify(){ 
        notify.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        notify.setPadding(new Insets(0, 0, 50, 50));   
    }
    private void initNameField(){ 
        nameField.setPrefWidth(120);
        nameField.setFont(Font.font("Arial", FontWeight.BOLD, 20));   
    }
    public void updateRankingTable(){rankingTable.setItems(DBManager.getRanking());} //(03)
    public void updateScoreField(){scoreField.setText(Integer.toString(Game.getScore()));} //(04)
    public void setNotify(String m){notify.setText("Notifica: " + m);} //(05)
    public String getName(){return nameField.getText();}
    public TextField getScoreField(){return scoreField;}
    public Button getStartButton(){return startButton;}     
    public TextField getNameField(){return nameField;}
    public BorderPane getRoot(){return root;} 
    public void reloadFromCache(){ //(06) 
        if(CacheManager.load() != null){
            Cache cache = CacheManager.load();
            nameField.setText(cache.nameField);
            scoreField.setText(cache.scoreField);
            if(cache.inGame){
                startButton.setDisable(true);
                startButton.setText("Riprova");
                nameField.setDisable(true);
                setNotify("Pausa, premi P per riprendere");
            }else if(cache.gameover){
                setNotify("Gameover");
            }
        }
    }

}

/*
Note
(01)
    Inizializza la GUI e chiama i metodi di inizializzazione delle singole componenti.
(02)
    Inizializza il layout della RankingTable (Top-K).
(03)
    Aggiorna la RankingTable sostituendo il contenuto attuale con la
    ObservableList restituita dalla classe DBManager.
(04)
    Aggiorna la ScoreField sostituendo il contenuto attuale con il valore
    'score' della classe Game.
(05)
    Imposta un nuovo messaggio di notifica.
(06)
    Ricarica dalla Cache: 
    - nameField
    - scoreField 
    - startButton (attivato/disattivato e label).
*/