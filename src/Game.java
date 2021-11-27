import javafx.animation.*;
import javafx.application.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.stage.*;
import javafx.util.*;

public final class Game extends Application{
    private XMLConfigParameters cfg;
    private DBManager dbManager;
    private Scene scene;
    private ActivityLog log;
    private static int gameSpeed; 
    private static int scoreMultiplier;
    public static int level;
    public static boolean pause = false;
    public static boolean gameover = false;
    public static boolean inGame = false;
    public static int score;
    public static GUI GUI;
    public static GameArea gameArea;
    public static Timeline timeline;    
    public static int nGhost; 
    public Pacman player; 
    public Ghost[] enemy;
    
    public void start(Stage stage){ //(01)
        cfg = new XMLConfigParameters(); //(02)
        gameSpeed = cfg.getGameSpeed();
        scoreMultiplier = cfg.getScoreMultiplier();
        gameArea = new GameArea(cfg);
        GUI = new GUI(cfg);
        dbManager = new DBManager(cfg);
        GUI.updateRankingTable(); //(03)
        log = new ActivityLog(); //(04)

        scene = new Scene(GUI.getRoot(), cfg.getWidth(), cfg.getHeight()); //(05)        
        
        log.send("Apertura");
        update(); //(06)
        initInput(); //(07)

        stage.setOnCloseRequest((WindowEvent we) -> { //(08)
            CacheManager.save(this); 
            log.send("Chiusura");
        });
          
        stage.setTitle("Pacman");
        stage.setResizable(false);
        stage.setMinWidth(854);
        stage.setMinHeight(480);
        stage.setScene(scene);
        stage.show();
        
        reloadFromCache(); //(09)
    }
    public void initInput(){
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case UP:
                    player.checkDir(Character.direction.UP);                    
                    break;
                case DOWN:
                    player.checkDir(Character.direction.DOWN);                    
                    break;
                case LEFT:
                    player.checkDir(Character.direction.LEFT);                    
                    break;
                case RIGHT:
                    player.checkDir(Character.direction.RIGHT);                    
                    break;
                case SPACE:
                    player.checkDir(Character.direction.STOP);
                    break;
                case P:
                    if(inGame){
                        if(pause){
                            pauseGame();
                            GUI.setNotify("");
                            log.send("Riprendi");
                        }else {
                            pauseGame();
                            GUI.setNotify("Pausa, premi P per riprendere");
                            log.send("Pausa");
                        }
                    }
                    break;
            }
        });
        GUI.getStartButton().setOnAction(new EventHandler<ActionEvent>(){    
            public void handle(ActionEvent e){
                log.send(GUI.getStartButton().getText());
                if(GUI.getNameField().getText().length() != 0){ //isEmpty() o == "" non vanno
                    newGame();
                }else
                    GUI.setNotify("Inserisci prima il tuo nome!");
            }        
        });
        
    }
    public void levelUp(){ //(10)
        pauseGame();
        score++;
        level++;
        scoreMultiplier += level;
        startGame();
    }
    public void pauseGame(){ //(11)
        if(pause)
            timeline.play();
        else
            timeline.stop(); 
        pause = !pause;
    }
    public void newGame(){ //(12)
        GUI.getStartButton().setDisable(true);
        GUI.getNameField().setDisable(true);
        score = 0;
        level = 1;
        inGame = true;
        startGame();
    }
    public void startGame(){ //(13)
        GUI.setNotify("");
        nGhost = level + 1;
        player = new Pacman();
        enemy = new Ghost[nGhost];
        for(int i=0; i<nGhost; ++i)
            enemy[i] = new Ghost();
        
        gameArea.resetGameArea();        
        pause = false;
        gameover = false;
        timeline.play();   
    }
    public void gameover(){ //(14)
        gameover = true;
        inGame = false;
        pauseGame();
        GUI.getStartButton().setText("Riprova");;
        GUI.getStartButton().setDisable(false);
        GUI.getNameField().setDisable(false);
        dbManager.saveScore(GUI.getName(), score*scoreMultiplier);
        GUI.updateRankingTable();
        GUI.setNotify("Gameover");
    }
    public void update(){ //(06)
        timeline = new Timeline(new KeyFrame(Duration.seconds(1/(double)gameSpeed), event -> { //(15)
            GameArea.getArea().requestFocus(); //(16)
            if(((score+1) % 140) == 0) //(17)
                levelUp();        
            player.updatePosition(); 
            for(Ghost x: enemy) 
                x.updatePosition();            
            gameArea.update(); //(18)
            GUI.updateScoreField();
            if(gameArea.currentMap[player.getPosY()][player.getPosX()] != 'P') //(19)
                gameover();     
        }));
        timeline.setCycleCount(Animation.INDEFINITE); //(20)
    }
    public static int getScore(){return score*scoreMultiplier;} //(21)
    public void reloadFromCache(){ //(09)
        if(CacheManager.load() != null){
            pauseGame();
            Cache cache = CacheManager.load();
            level = cache.level;
            score = cache.score;
            nGhost = cache.nGhost;
            gameover = cache.gameover;
            player = cache.player;
            enemy = cache.enemy;
            inGame = cache.inGame;
            if(inGame || gameover) gameArea.update(); //(22)
        }     
    }
}

/*
Note
(01)
    Inizializza la finestra di gioco, la scena e le classi coinvolte.
(02)
    Richiede i parametri al file di configurazione.
(03)
    Aggiorna la vista della Top-K dopo aver effettuato la connessione al database.
(04)
    Istanzia ActivityLog per poter usare il metodo send.
(05)
    Preleva dalla classe GUI il BorderPane root.
    Imposta la dimensione della finestra con i parametri del file di configurazione.
(06)
    Inizializza la timeline per l'aggiornamento globale della finestra.
(07)
    Inizializza l'input e gestisce i casi di errore.
(08) 
    Invia la classe Game al CacheManager per il salvataggio dello stato
    della partita alla chiusura della finestra.
(09)
    Ricarica dalla Cache:
    - level;
    - score;
    - nGhost (numero fantasmi da istanziare);
    - gameover (boolean);
    - player (Pacman);
    - enemy (Ghosts);
    - inGame (boolean 'true' se la partita è in corso e non è in pausa).
    Di default lo stato di pausa è 'true' al caricamento della partita dalla Cache.
(10)
    Invocato al completamento di un livello (dopo che Pacman ha ripulito la mappa),
    aumenta il livello attuale e il moltiplicatore dello score.
(11)
    Metodo invocato alla pressione del tasto P.
    Imposta lo stato di pausa invocando il metodo timeline.stop()
    o riprende la partita invocando timeline.play().
(12)
    Metodo invocato alla pressione del bottone 'Nuova Partita' o 'Riprova'.
    Carica una nuova partita.
(13)
    Metodo invocato da 'levelUp' e 'newGame'.
    Imposta in uno stato consistente la mappa di gioco per l'inizio di una nuova
    partita o di un nuovo livello.
    Inizializza i personaggi e invoca il metodo timeline.play() per l'aggiornamento.
(14)
    Imposta lo stato di Gameover e invoca il salvataggio del punteggio nella Top-K
    con DBManager.savescore(...).
(15)
    Thread che gestisce l'aggiornamento delle animazioni:
    https://docs.oracle.com/javase/8/javafx/api/javafx/animation/Timeline.html
    
    Duration.seconds(1/(double)gameSpeed) indica la velocità di aggiornamento.
    Aumentando il valore gameSpeed dal file di configurazione la velocità
    di gioco aumenterà.
    L'aggiornamento globale non si avvierà finche non verrà invocato il metodo
    timeline.play().
(16)
    Viene dato il focus all'area di gioco per poter riceve i comandi di
    input dalla tastiera.
(17)
    Invoca levelUp quando Pacman ripulisce l'intera mappa.
(18)
    Invoca il metodo update dell'area di gioco per l'aggiornamento delle posizioni
    dei personaggi.
(19)
    Se Pacman non si trova nella sua posizione attuale allora è stato ucciso da un Ghost.
(20)
    Imposta il loop infinito della timeline.
(21)
    Lo score finale è dato dallo score x scoreMultiplier
(22)
    Aggiorna l'area di gioco solo se è stata caricata sulla Cache.
*/